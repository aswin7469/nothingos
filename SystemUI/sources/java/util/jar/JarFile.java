package java.util.jar;

import java.p026io.ByteArrayInputStream;
import java.p026io.File;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.security.CodeSigner;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Spliterators;
import java.util.jar.JarVerifier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import sun.misc.IOUtils;
import sun.security.util.ManifestEntryVerifier;
import sun.security.util.SignatureFileVerifier;

public class JarFile extends ZipFile {
    private static final char[] CLASSPATH_CHARS = {'c', 'l', 'a', 's', 's', '-', 'p', 'a', 't', 'h'};
    private static final int[] CLASSPATH_LASTOCC;
    private static final int[] CLASSPATH_OPTOSFT = new int[10];
    public static final String MANIFEST_NAME = "META-INF/MANIFEST.MF";
    private volatile boolean hasCheckedSpecialAttributes;
    private boolean hasClassPathAttribute;
    /* access modifiers changed from: private */

    /* renamed from: jv */
    public JarVerifier f768jv;
    private boolean jvInitialized;
    private JarEntry manEntry;
    private Manifest manifest;
    private boolean verify;

    private native String[] getMetaInfEntryNames();

    public JarFile(String str) throws IOException {
        this(new File(str), true, 1);
    }

    public JarFile(String str, boolean z) throws IOException {
        this(new File(str), z, 1);
    }

    public JarFile(File file) throws IOException {
        this(file, true, 1);
    }

    public JarFile(File file, boolean z) throws IOException {
        this(file, z, 1);
    }

    public JarFile(File file, boolean z, int i) throws IOException {
        super(file, i);
        this.verify = z;
    }

    public Manifest getManifest() throws IOException {
        return getManifestFromReference();
    }

    private synchronized Manifest getManifestFromReference() throws IOException {
        Manifest manifest2;
        JarEntry manEntry2;
        manifest2 = this.manifest;
        if (manifest2 == null && (manEntry2 = getManEntry()) != null) {
            if (this.verify) {
                byte[] bytes = getBytes(manEntry2);
                Manifest manifest3 = new Manifest((InputStream) new ByteArrayInputStream(bytes));
                if (!this.jvInitialized) {
                    this.f768jv = new JarVerifier(bytes);
                }
                manifest2 = manifest3;
            } else {
                manifest2 = new Manifest(super.getInputStream(manEntry2));
            }
            this.manifest = manifest2;
        }
        return manifest2;
    }

    public JarEntry getJarEntry(String str) {
        return (JarEntry) getEntry(str);
    }

    public ZipEntry getEntry(String str) {
        ZipEntry entry = super.getEntry(str);
        if (entry != null) {
            return new JarFileEntry(entry);
        }
        return null;
    }

    private class JarEntryIterator implements Enumeration<JarEntry>, Iterator<JarEntry> {

        /* renamed from: e */
        final Enumeration<? extends ZipEntry> f769e;

        private JarEntryIterator() {
            this.f769e = JarFile.super.entries();
        }

        public boolean hasNext() {
            return this.f769e.hasMoreElements();
        }

        public JarEntry next() {
            return new JarFileEntry((ZipEntry) this.f769e.nextElement());
        }

        public boolean hasMoreElements() {
            return hasNext();
        }

        public JarEntry nextElement() {
            return next();
        }
    }

    public Enumeration<JarEntry> entries() {
        return new JarEntryIterator();
    }

    public Stream<JarEntry> stream() {
        return StreamSupport.stream(Spliterators.spliterator(new JarEntryIterator(), (long) size(), 1297), false);
    }

    private class JarFileEntry extends JarEntry {
        JarFileEntry(ZipEntry zipEntry) {
            super(zipEntry);
        }

        public Attributes getAttributes() throws IOException {
            Manifest manifest = JarFile.this.getManifest();
            if (manifest != null) {
                return manifest.getAttributes(getName());
            }
            return null;
        }

        public Certificate[] getCertificates() {
            try {
                JarFile.this.maybeInstantiateVerifier();
                if (this.certs == null && JarFile.this.f768jv != null) {
                    this.certs = JarFile.this.f768jv.getCerts(JarFile.this, this);
                }
                if (this.certs == null) {
                    return null;
                }
                return (Certificate[]) this.certs.clone();
            } catch (IOException e) {
                throw new RuntimeException((Throwable) e);
            }
        }

        public CodeSigner[] getCodeSigners() {
            try {
                JarFile.this.maybeInstantiateVerifier();
                if (this.signers == null && JarFile.this.f768jv != null) {
                    this.signers = JarFile.this.f768jv.getCodeSigners(JarFile.this, this);
                }
                if (this.signers == null) {
                    return null;
                }
                return (CodeSigner[]) this.signers.clone();
            } catch (IOException e) {
                throw new RuntimeException((Throwable) e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void maybeInstantiateVerifier() throws IOException {
        if (this.f768jv == null && this.verify) {
            String[] metaInfEntryNames = getMetaInfEntryNames();
            if (metaInfEntryNames != null) {
                for (String upperCase : metaInfEntryNames) {
                    String upperCase2 = upperCase.toUpperCase(Locale.ENGLISH);
                    if (upperCase2.endsWith(".DSA") || upperCase2.endsWith(".RSA") || upperCase2.endsWith(".EC") || upperCase2.endsWith(".SF")) {
                        getManifest();
                        return;
                    }
                }
            }
            this.verify = false;
        }
    }

    private void initializeVerifier() {
        try {
            String[] metaInfEntryNames = getMetaInfEntryNames();
            if (metaInfEntryNames != null) {
                ManifestEntryVerifier manifestEntryVerifier = null;
                for (int i = 0; i < metaInfEntryNames.length; i++) {
                    String upperCase = metaInfEntryNames[i].toUpperCase(Locale.ENGLISH);
                    if (MANIFEST_NAME.equals(upperCase) || SignatureFileVerifier.isBlockOrSF(upperCase)) {
                        JarEntry jarEntry = getJarEntry(metaInfEntryNames[i]);
                        if (jarEntry != null) {
                            if (manifestEntryVerifier == null) {
                                manifestEntryVerifier = new ManifestEntryVerifier(getManifestFromReference());
                            }
                            ManifestEntryVerifier manifestEntryVerifier2 = manifestEntryVerifier;
                            byte[] bytes = getBytes(jarEntry);
                            if (bytes != null && bytes.length > 0) {
                                this.f768jv.beginEntry(jarEntry, manifestEntryVerifier2);
                                this.f768jv.update(bytes.length, bytes, 0, bytes.length, manifestEntryVerifier2);
                                this.f768jv.update(-1, (byte[]) null, 0, 0, manifestEntryVerifier2);
                            }
                            manifestEntryVerifier = manifestEntryVerifier2;
                        } else {
                            throw new JarException("corrupted jar file");
                        }
                    }
                }
            }
        } catch (IOException e) {
            this.f768jv = null;
            this.verify = false;
            if (JarVerifier.debug != null) {
                JarVerifier.debug.println("jarfile parsing error!");
                e.printStackTrace();
            }
        }
        JarVerifier jarVerifier = this.f768jv;
        if (jarVerifier != null) {
            jarVerifier.doneWithMeta();
            if (JarVerifier.debug != null) {
                JarVerifier.debug.println("done with meta!");
            }
            if (this.f768jv.nothingToVerify()) {
                if (JarVerifier.debug != null) {
                    JarVerifier.debug.println("nothing to verify!");
                }
                this.f768jv = null;
                this.verify = false;
            }
        }
    }

    private byte[] getBytes(ZipEntry zipEntry) throws IOException {
        InputStream inputStream = super.getInputStream(zipEntry);
        try {
            byte[] readFully = IOUtils.readFully(inputStream, (int) zipEntry.getSize(), true);
            if (inputStream != null) {
                inputStream.close();
            }
            return readFully;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public synchronized InputStream getInputStream(ZipEntry zipEntry) throws IOException {
        maybeInstantiateVerifier();
        if (this.f768jv == null) {
            return super.getInputStream(zipEntry);
        }
        if (!this.jvInitialized) {
            initializeVerifier();
            this.jvInitialized = true;
            if (this.f768jv == null) {
                return super.getInputStream(zipEntry);
            }
        }
        return new JarVerifier.VerifierStream(getManifestFromReference(), zipEntry instanceof JarFileEntry ? (JarEntry) zipEntry : getJarEntry(zipEntry.getName()), super.getInputStream(zipEntry), this.f768jv);
    }

    static {
        int[] iArr = new int[128];
        CLASSPATH_LASTOCC = iArr;
        iArr[99] = 1;
        iArr[108] = 2;
        iArr[115] = 5;
        iArr[45] = 6;
        iArr[112] = 7;
        iArr[97] = 8;
        iArr[116] = 9;
        iArr[104] = 10;
        for (int i = 0; i < 9; i++) {
            CLASSPATH_OPTOSFT[i] = 10;
        }
        CLASSPATH_OPTOSFT[9] = 1;
    }

    private synchronized JarEntry getManEntry() {
        String[] metaInfEntryNames;
        if (this.manEntry == null) {
            JarEntry jarEntry = getJarEntry(MANIFEST_NAME);
            this.manEntry = jarEntry;
            if (jarEntry == null && (metaInfEntryNames = getMetaInfEntryNames()) != null) {
                int i = 0;
                while (true) {
                    if (i >= metaInfEntryNames.length) {
                        break;
                    } else if (MANIFEST_NAME.equals(metaInfEntryNames[i].toUpperCase(Locale.ENGLISH))) {
                        this.manEntry = getJarEntry(metaInfEntryNames[i]);
                        break;
                    } else {
                        i++;
                    }
                }
            }
        }
        return this.manEntry;
    }

    public boolean hasClassPathAttribute() throws IOException {
        checkForSpecialAttributes();
        return this.hasClassPathAttribute;
    }

    private boolean match(char[] cArr, byte[] bArr, int[] iArr, int[] iArr2) {
        int length = cArr.length;
        int length2 = bArr.length - length;
        int i = 0;
        while (i <= length2) {
            int i2 = length - 1;
            while (i2 >= 0) {
                char c = (char) bArr[i + i2];
                if (((c - 'A') | ('Z' - c)) >= 0) {
                    c = (char) (c + ' ');
                }
                if (c != cArr[i2]) {
                    i += Math.max((i2 + 1) - iArr[c & 127], iArr2[i2]);
                } else {
                    i2--;
                }
            }
            return true;
        }
        return false;
    }

    private void checkForSpecialAttributes() throws IOException {
        if (!this.hasCheckedSpecialAttributes) {
            JarEntry manEntry2 = getManEntry();
            if (manEntry2 != null) {
                if (match(CLASSPATH_CHARS, getBytes(manEntry2), CLASSPATH_LASTOCC, CLASSPATH_OPTOSFT)) {
                    this.hasClassPathAttribute = true;
                }
            }
            this.hasCheckedSpecialAttributes = true;
        }
    }

    /* access modifiers changed from: package-private */
    public JarEntry newEntry(ZipEntry zipEntry) {
        return new JarFileEntry(zipEntry);
    }
}
