package java.util.jar;

import com.android.launcher3.icons.cache.BaseIconCache;
import java.net.URL;
import java.p026io.ByteArrayOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import sun.misc.JarIndex;
import sun.security.util.Debug;
import sun.security.util.ManifestDigester;
import sun.security.util.ManifestEntryVerifier;
import sun.security.util.SignatureFileVerifier;

class JarVerifier {
    static final Debug debug = Debug.getInstance("jar");
    private boolean anyToVerify = true;
    private ByteArrayOutputStream baos;
    private Object csdomain = new Object();
    boolean eagerValidation;
    private Enumeration<String> emptyEnumeration = new Enumeration<String>() {
        public boolean hasMoreElements() {
            return false;
        }

        public String nextElement() {
            throw new NoSuchElementException();
        }
    };
    private CodeSigner[] emptySigner = new CodeSigner[0];
    private List<CodeSigner[]> jarCodeSigners;
    private URL lastURL;
    private Map<CodeSigner[], CodeSource> lastURLMap;
    private volatile ManifestDigester manDig;
    private List<Object> manifestDigests;
    byte[] manifestRawBytes = null;
    private boolean parsingBlockOrSF = false;
    private boolean parsingMeta = true;
    private ArrayList<SignatureFileVerifier> pendingBlocks;
    private Hashtable<String, byte[]> sigFileData;
    private Hashtable<String, CodeSigner[]> sigFileSigners;
    private ArrayList<CodeSigner[]> signerCache;
    private Map<String, CodeSigner[]> signerMap;
    private Map<CodeSigner[], CodeSource> signerToCodeSource = new HashMap();
    private Map<URL, Map<CodeSigner[], CodeSource>> urlToCodeSourceMap = new HashMap();
    private Hashtable<String, CodeSigner[]> verifiedSigners;

    public JarVerifier(byte[] bArr) {
        this.manifestRawBytes = bArr;
        this.sigFileSigners = new Hashtable<>();
        this.verifiedSigners = new Hashtable<>();
        this.sigFileData = new Hashtable<>(11);
        this.pendingBlocks = new ArrayList<>();
        this.baos = new ByteArrayOutputStream();
        this.manifestDigests = new ArrayList();
    }

    public void beginEntry(JarEntry jarEntry, ManifestEntryVerifier manifestEntryVerifier) throws IOException {
        if (jarEntry != null) {
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("beginEntry " + jarEntry.getName());
            }
            String name = jarEntry.getName();
            if (this.parsingMeta) {
                String upperCase = name.toUpperCase(Locale.ENGLISH);
                if (upperCase.startsWith("META-INF/") || upperCase.startsWith("/META-INF/")) {
                    if (jarEntry.isDirectory()) {
                        manifestEntryVerifier.setEntry((String) null, jarEntry);
                        return;
                    } else if (!upperCase.equals(JarFile.MANIFEST_NAME) && !upperCase.equals(JarIndex.INDEX_NAME)) {
                        if (SignatureFileVerifier.isBlockOrSF(upperCase)) {
                            this.parsingBlockOrSF = true;
                            this.baos.reset();
                            manifestEntryVerifier.setEntry((String) null, jarEntry);
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
            if (this.parsingMeta) {
                doneWithMeta();
            }
            if (jarEntry.isDirectory()) {
                manifestEntryVerifier.setEntry((String) null, jarEntry);
                return;
            }
            if (name.startsWith("./")) {
                name = name.substring(2);
            }
            if (name.startsWith("/")) {
                name = name.substring(1);
            }
            if (this.sigFileSigners.get(name) == null && this.verifiedSigners.get(name) == null) {
                manifestEntryVerifier.setEntry((String) null, jarEntry);
            } else {
                manifestEntryVerifier.setEntry(name, jarEntry);
            }
        }
    }

    public void update(int i, ManifestEntryVerifier manifestEntryVerifier) throws IOException {
        if (i == -1) {
            processEntry(manifestEntryVerifier);
        } else if (this.parsingBlockOrSF) {
            this.baos.write(i);
        } else {
            manifestEntryVerifier.update((byte) i);
        }
    }

    public void update(int i, byte[] bArr, int i2, int i3, ManifestEntryVerifier manifestEntryVerifier) throws IOException {
        if (i == -1) {
            processEntry(manifestEntryVerifier);
        } else if (this.parsingBlockOrSF) {
            this.baos.write(bArr, i2, i);
        } else {
            manifestEntryVerifier.update(bArr, i2, i);
        }
    }

    private void processEntry(ManifestEntryVerifier manifestEntryVerifier) throws IOException {
        if (!this.parsingBlockOrSF) {
            JarEntry entry = manifestEntryVerifier.getEntry();
            if (entry != null && entry.signers == null) {
                entry.signers = manifestEntryVerifier.verify(this.verifiedSigners, this.sigFileSigners);
                entry.certs = mapSignersToCertArray(entry.signers);
                return;
            }
            return;
        }
        try {
            this.parsingBlockOrSF = false;
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("processEntry: processing block");
            }
            String upperCase = manifestEntryVerifier.getEntry().getName().toUpperCase(Locale.ENGLISH);
            if (upperCase.endsWith(".SF")) {
                String substring = upperCase.substring(0, upperCase.length() - 3);
                byte[] byteArray = this.baos.toByteArray();
                this.sigFileData.put(substring, byteArray);
                Iterator<SignatureFileVerifier> it = this.pendingBlocks.iterator();
                while (it.hasNext()) {
                    SignatureFileVerifier next = it.next();
                    if (next.needSignatureFile(substring)) {
                        Debug debug3 = debug;
                        if (debug3 != null) {
                            debug3.println("processEntry: processing pending block");
                        }
                        next.setSignatureFile(byteArray);
                        next.process(this.sigFileSigners, this.manifestDigests);
                    }
                }
                return;
            }
            String substring2 = upperCase.substring(0, upperCase.lastIndexOf(BaseIconCache.EMPTY_CLASS_NAME));
            if (this.signerCache == null) {
                this.signerCache = new ArrayList<>();
            }
            if (this.manDig == null) {
                synchronized (this.manifestRawBytes) {
                    if (this.manDig == null) {
                        this.manDig = new ManifestDigester(this.manifestRawBytes);
                        this.manifestRawBytes = null;
                    }
                }
            }
            SignatureFileVerifier signatureFileVerifier = new SignatureFileVerifier(this.signerCache, this.manDig, upperCase, this.baos.toByteArray());
            if (signatureFileVerifier.needSignatureFileBytes()) {
                byte[] bArr = this.sigFileData.get(substring2);
                if (bArr == null) {
                    if (debug2 != null) {
                        debug2.println("adding pending block");
                    }
                    this.pendingBlocks.add(signatureFileVerifier);
                    return;
                }
                signatureFileVerifier.setSignatureFile(bArr);
            }
            signatureFileVerifier.process(this.sigFileSigners, this.manifestDigests);
        } catch (IOException e) {
            Debug debug4 = debug;
            if (debug4 != null) {
                debug4.println("processEntry caught: " + e);
            }
        } catch (SignatureException e2) {
            Debug debug5 = debug;
            if (debug5 != null) {
                debug5.println("processEntry caught: " + e2);
            }
        } catch (NoSuchAlgorithmException e3) {
            Debug debug6 = debug;
            if (debug6 != null) {
                debug6.println("processEntry caught: " + e3);
            }
        } catch (CertificateException e4) {
            Debug debug7 = debug;
            if (debug7 != null) {
                debug7.println("processEntry caught: " + e4);
            }
        }
    }

    @Deprecated
    public Certificate[] getCerts(String str) {
        return mapSignersToCertArray(getCodeSigners(str));
    }

    public Certificate[] getCerts(JarFile jarFile, JarEntry jarEntry) {
        return mapSignersToCertArray(getCodeSigners(jarFile, jarEntry));
    }

    public CodeSigner[] getCodeSigners(String str) {
        return this.verifiedSigners.get(str);
    }

    public CodeSigner[] getCodeSigners(JarFile jarFile, JarEntry jarEntry) {
        String name = jarEntry.getName();
        if (this.eagerValidation && this.sigFileSigners.get(name) != null) {
            try {
                InputStream inputStream = jarFile.getInputStream(jarEntry);
                byte[] bArr = new byte[1024];
                for (int i = 1024; i != -1; i = inputStream.read(bArr, 0, 1024)) {
                }
                inputStream.close();
            } catch (IOException unused) {
            }
        }
        return getCodeSigners(name);
    }

    private static Certificate[] mapSignersToCertArray(CodeSigner[] codeSignerArr) {
        if (codeSignerArr == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (CodeSigner signerCertPath : codeSignerArr) {
            arrayList.addAll(signerCertPath.getSignerCertPath().getCertificates());
        }
        return (Certificate[]) arrayList.toArray(new Certificate[arrayList.size()]);
    }

    /* access modifiers changed from: package-private */
    public boolean nothingToVerify() {
        return !this.anyToVerify;
    }

    /* access modifiers changed from: package-private */
    public void doneWithMeta() {
        this.parsingMeta = false;
        this.anyToVerify = !this.sigFileSigners.isEmpty();
        this.baos = null;
        this.sigFileData = null;
        this.pendingBlocks = null;
        this.signerCache = null;
        this.manDig = null;
        if (this.sigFileSigners.containsKey(JarFile.MANIFEST_NAME)) {
            this.verifiedSigners.put(JarFile.MANIFEST_NAME, this.sigFileSigners.remove(JarFile.MANIFEST_NAME));
        }
    }

    static class VerifierStream extends InputStream {

        /* renamed from: is */
        private InputStream f771is;

        /* renamed from: jv */
        private JarVerifier f772jv;
        private ManifestEntryVerifier mev;
        private long numLeft;

        VerifierStream(Manifest manifest, JarEntry jarEntry, InputStream inputStream, JarVerifier jarVerifier) throws IOException {
            if (inputStream != null) {
                this.f771is = inputStream;
                this.f772jv = jarVerifier;
                ManifestEntryVerifier manifestEntryVerifier = new ManifestEntryVerifier(manifest);
                this.mev = manifestEntryVerifier;
                this.f772jv.beginEntry(jarEntry, manifestEntryVerifier);
                long size = jarEntry.getSize();
                this.numLeft = size;
                if (size == 0) {
                    this.f772jv.update(-1, this.mev);
                    return;
                }
                return;
            }
            throw new NullPointerException("is == null");
        }

        public int read() throws IOException {
            InputStream inputStream = this.f771is;
            if (inputStream == null) {
                throw new IOException("stream closed");
            } else if (this.numLeft <= 0) {
                return -1;
            } else {
                int read = inputStream.read();
                this.f772jv.update(read, this.mev);
                long j = this.numLeft - 1;
                this.numLeft = j;
                if (j == 0) {
                    this.f772jv.update(-1, this.mev);
                }
                return read;
            }
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            InputStream inputStream = this.f771is;
            if (inputStream != null) {
                long j = this.numLeft;
                if (j > 0 && j < ((long) i2)) {
                    i2 = (int) j;
                }
                if (j <= 0) {
                    return -1;
                }
                int read = inputStream.read(bArr, i, i2);
                this.f772jv.update(read, bArr, i, i2, this.mev);
                long j2 = this.numLeft - ((long) read);
                this.numLeft = j2;
                if (j2 == 0) {
                    this.f772jv.update(-1, bArr, i, i2, this.mev);
                }
                return read;
            }
            throw new IOException("stream closed");
        }

        public void close() throws IOException {
            InputStream inputStream = this.f771is;
            if (inputStream != null) {
                inputStream.close();
            }
            this.f771is = null;
            this.mev = null;
            this.f772jv = null;
        }

        public int available() throws IOException {
            InputStream inputStream = this.f771is;
            if (inputStream != null) {
                return inputStream.available();
            }
            throw new IOException("stream closed");
        }
    }

    private synchronized CodeSource mapSignersToCodeSource(URL url, CodeSigner[] codeSignerArr) {
        Map<CodeSigner[], CodeSource> map;
        CodeSource codeSource;
        if (url == this.lastURL) {
            map = this.lastURLMap;
        } else {
            map = this.urlToCodeSourceMap.get(url);
            if (map == null) {
                map = new HashMap<>();
                this.urlToCodeSourceMap.put(url, map);
            }
            this.lastURLMap = map;
            this.lastURL = url;
        }
        codeSource = map.get(codeSignerArr);
        if (codeSource == null) {
            codeSource = new VerifierCodeSource(this.csdomain, url, codeSignerArr);
            this.signerToCodeSource.put(codeSignerArr, codeSource);
        }
        return codeSource;
    }

    private CodeSource[] mapSignersToCodeSources(URL url, List<CodeSigner[]> list, boolean z) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(mapSignersToCodeSource(url, list.get(i)));
        }
        if (z) {
            arrayList.add(mapSignersToCodeSource(url, (CodeSigner[]) null));
        }
        return (CodeSource[]) arrayList.toArray(new CodeSource[arrayList.size()]);
    }

    private CodeSigner[] findMatchingSigners(CodeSource codeSource) {
        if (codeSource instanceof VerifierCodeSource) {
            VerifierCodeSource verifierCodeSource = (VerifierCodeSource) codeSource;
            if (verifierCodeSource.isSameDomain(this.csdomain)) {
                return verifierCodeSource.getPrivateSigners();
            }
        }
        CodeSource[] mapSignersToCodeSources = mapSignersToCodeSources(codeSource.getLocation(), getJarCodeSigners(), true);
        ArrayList arrayList = new ArrayList();
        for (CodeSource add : mapSignersToCodeSources) {
            arrayList.add(add);
        }
        int indexOf = arrayList.indexOf(codeSource);
        if (indexOf == -1) {
            return null;
        }
        CodeSigner[] r5 = ((VerifierCodeSource) arrayList.get(indexOf)).getPrivateSigners();
        return r5 == null ? this.emptySigner : r5;
    }

    private static class VerifierCodeSource extends CodeSource {
        private static final long serialVersionUID = -9047366145967768825L;
        Object csdomain;
        Certificate[] vcerts;
        URL vlocation;
        CodeSigner[] vsigners;

        VerifierCodeSource(Object obj, URL url, CodeSigner[] codeSignerArr) {
            super(url, codeSignerArr);
            this.csdomain = obj;
            this.vlocation = url;
            this.vsigners = codeSignerArr;
        }

        VerifierCodeSource(Object obj, URL url, Certificate[] certificateArr) {
            super(url, certificateArr);
            this.csdomain = obj;
            this.vlocation = url;
            this.vcerts = certificateArr;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof VerifierCodeSource) {
                VerifierCodeSource verifierCodeSource = (VerifierCodeSource) obj;
                if (isSameDomain(verifierCodeSource.csdomain)) {
                    if (verifierCodeSource.vsigners != this.vsigners || verifierCodeSource.vcerts != this.vcerts) {
                        return false;
                    }
                    URL url = verifierCodeSource.vlocation;
                    if (url != null) {
                        return url.equals(this.vlocation);
                    }
                    URL url2 = this.vlocation;
                    if (url2 != null) {
                        return url2.equals(url);
                    }
                    return true;
                }
            }
            return super.equals(obj);
        }

        /* access modifiers changed from: package-private */
        public boolean isSameDomain(Object obj) {
            return this.csdomain == obj;
        }

        /* access modifiers changed from: private */
        public CodeSigner[] getPrivateSigners() {
            return this.vsigners;
        }

        private Certificate[] getPrivateCertificates() {
            return this.vcerts;
        }
    }

    private synchronized Map<String, CodeSigner[]> signerMap() {
        if (this.signerMap == null) {
            HashMap hashMap = new HashMap(this.verifiedSigners.size() + this.sigFileSigners.size());
            this.signerMap = hashMap;
            hashMap.putAll(this.verifiedSigners);
            this.signerMap.putAll(this.sigFileSigners);
        }
        return this.signerMap;
    }

    public synchronized Enumeration<String> entryNames(JarFile jarFile, CodeSource[] codeSourceArr) {
        final Iterator<Map.Entry<String, CodeSigner[]>> it;
        final ArrayList arrayList;
        final Enumeration<String> unsignedEntryNames;
        it = signerMap().entrySet().iterator();
        arrayList = new ArrayList(codeSourceArr.length);
        boolean z = false;
        for (CodeSource findMatchingSigners : codeSourceArr) {
            CodeSigner[] findMatchingSigners2 = findMatchingSigners(findMatchingSigners);
            if (findMatchingSigners2 == null || findMatchingSigners2.length <= 0) {
                z = true;
            } else {
                arrayList.add(findMatchingSigners2);
            }
        }
        unsignedEntryNames = z ? unsignedEntryNames(jarFile) : this.emptyEnumeration;
        return new Enumeration<String>() {
            String name;

            public boolean hasMoreElements() {
                if (this.name != null) {
                    return true;
                }
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    if (arrayList.contains(entry.getValue())) {
                        this.name = (String) entry.getKey();
                        return true;
                    }
                }
                if (!unsignedEntryNames.hasMoreElements()) {
                    return false;
                }
                this.name = (String) unsignedEntryNames.nextElement();
                return true;
            }

            public String nextElement() {
                if (hasMoreElements()) {
                    String str = this.name;
                    this.name = null;
                    return str;
                }
                throw new NoSuchElementException();
            }
        };
    }

    public Enumeration<JarEntry> entries2(final JarFile jarFile, final Enumeration<? extends ZipEntry> enumeration) {
        final HashMap hashMap = new HashMap();
        hashMap.putAll(signerMap());
        return new Enumeration<JarEntry>() {
            JarEntry entry;
            Enumeration<String> signers = null;

            public boolean hasMoreElements() {
                if (this.entry != null) {
                    return true;
                }
                while (enumeration.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
                    if (!JarVerifier.isSigningRelated(zipEntry.getName())) {
                        this.entry = jarFile.newEntry(zipEntry);
                        return true;
                    }
                }
                if (this.signers == null) {
                    this.signers = Collections.enumeration(hashMap.keySet());
                }
                if (!this.signers.hasMoreElements()) {
                    return false;
                }
                this.entry = jarFile.newEntry(new ZipEntry(this.signers.nextElement()));
                return true;
            }

            public JarEntry nextElement() {
                if (hasMoreElements()) {
                    JarEntry jarEntry = this.entry;
                    hashMap.remove(jarEntry.getName());
                    this.entry = null;
                    return jarEntry;
                }
                throw new NoSuchElementException();
            }
        };
    }

    static boolean isSigningRelated(String str) {
        return SignatureFileVerifier.isSigningRelated(str);
    }

    private Enumeration<String> unsignedEntryNames(JarFile jarFile) {
        final Map<String, CodeSigner[]> signerMap2 = signerMap();
        final Enumeration<JarEntry> entries = jarFile.entries();
        return new Enumeration<String>() {
            String name;

            public boolean hasMoreElements() {
                if (this.name != null) {
                    return true;
                }
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) entries.nextElement();
                    String name2 = zipEntry.getName();
                    if (!zipEntry.isDirectory() && !JarVerifier.isSigningRelated(name2) && signerMap2.get(name2) == null) {
                        this.name = name2;
                        return true;
                    }
                }
                return false;
            }

            public String nextElement() {
                if (hasMoreElements()) {
                    String str = this.name;
                    this.name = null;
                    return str;
                }
                throw new NoSuchElementException();
            }
        };
    }

    private synchronized List<CodeSigner[]> getJarCodeSigners() {
        if (this.jarCodeSigners == null) {
            HashSet hashSet = new HashSet();
            hashSet.addAll(signerMap().values());
            ArrayList arrayList = new ArrayList();
            this.jarCodeSigners = arrayList;
            arrayList.addAll(hashSet);
        }
        return this.jarCodeSigners;
    }

    public synchronized CodeSource[] getCodeSources(JarFile jarFile, URL url) {
        return mapSignersToCodeSources(url, getJarCodeSigners(), unsignedEntryNames(jarFile).hasMoreElements());
    }

    public CodeSource getCodeSource(URL url, String str) {
        return mapSignersToCodeSource(url, signerMap().get(str));
    }

    public CodeSource getCodeSource(URL url, JarFile jarFile, JarEntry jarEntry) {
        return mapSignersToCodeSource(url, getCodeSigners(jarFile, jarEntry));
    }

    public void setEagerValidation(boolean z) {
        this.eagerValidation = z;
    }

    public synchronized List<Object> getManifestDigests() {
        return Collections.unmodifiableList(this.manifestDigests);
    }

    static CodeSource getUnsignedCS(URL url) {
        Certificate[] certificateArr = null;
        return new VerifierCodeSource((Object) null, url, (Certificate[]) null);
    }
}
