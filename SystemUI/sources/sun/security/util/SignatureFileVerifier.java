package sun.security.util;

import com.android.launcher3.icons.cache.BaseIconCache;
import java.p026io.ByteArrayInputStream;
import java.p026io.IOException;
import java.security.AlgorithmParameters;
import java.security.CodeSigner;
import java.security.CryptoPrimitive;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarException;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import sun.security.jca.Providers;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;

public class SignatureFileVerifier {
    private static final String ATTR_DIGEST = "-DIGEST-Manifest-Main-Attributes".toUpperCase(Locale.ENGLISH);
    private static final Set<CryptoPrimitive> DIGEST_PRIMITIVE_SET = Collections.unmodifiableSet(EnumSet.m1716of(CryptoPrimitive.MESSAGE_DIGEST));
    private static final DisabledAlgorithmConstraints JAR_DISABLED_CHECK = new DisabledAlgorithmConstraints(DisabledAlgorithmConstraints.PROPERTY_JAR_DISABLED_ALGS);
    private static final Debug debug = Debug.getInstance("jar");
    private static final char[] hexc = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private PKCS7 block;
    private CertificateFactory certificateFactory;
    private HashMap<String, MessageDigest> createdDigests;

    /* renamed from: md */
    private ManifestDigester f926md;
    private String name;
    private byte[] sfBytes;
    private ArrayList<CodeSigner[]> signerCache;
    private boolean workaround = false;

    /* JADX INFO: finally extract failed */
    public SignatureFileVerifier(ArrayList<CodeSigner[]> arrayList, ManifestDigester manifestDigester, String str, byte[] bArr) throws IOException, CertificateException {
        Object obj = null;
        this.certificateFactory = null;
        try {
            obj = Providers.startJarVerification();
            PKCS7 pkcs7 = new PKCS7(bArr);
            this.block = pkcs7;
            this.sfBytes = pkcs7.getContentInfo().getData();
            this.certificateFactory = CertificateFactory.getInstance("X509");
            Providers.stopJarVerification(obj);
            this.name = str.substring(0, str.lastIndexOf(BaseIconCache.EMPTY_CLASS_NAME)).toUpperCase(Locale.ENGLISH);
            this.f926md = manifestDigester;
            this.signerCache = arrayList;
        } catch (Throwable th) {
            Providers.stopJarVerification(obj);
            throw th;
        }
    }

    public boolean needSignatureFileBytes() {
        return this.sfBytes == null;
    }

    public boolean needSignatureFile(String str) {
        return this.name.equalsIgnoreCase(str);
    }

    public void setSignatureFile(byte[] bArr) {
        this.sfBytes = bArr;
    }

    public static boolean isBlockOrSF(String str) {
        return str.endsWith(".SF") || str.endsWith(".DSA") || str.endsWith(".RSA") || str.endsWith(".EC");
    }

    public static boolean isSigningRelated(String str) {
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        if (!upperCase.startsWith("META-INF/")) {
            return false;
        }
        String substring = upperCase.substring(9);
        if (substring.indexOf(47) != -1) {
            return false;
        }
        if (isBlockOrSF(substring) || substring.equals("MANIFEST.MF")) {
            return true;
        }
        if (!substring.startsWith("SIG-")) {
            return false;
        }
        int lastIndexOf = substring.lastIndexOf(46);
        if (lastIndexOf != -1) {
            String substring2 = substring.substring(lastIndexOf + 1);
            if (substring2.length() <= 3 && substring2.length() >= 1) {
                int i = 0;
                while (i < substring2.length()) {
                    char charAt = substring2.charAt(i);
                    if ((charAt >= 'A' && charAt <= 'Z') || (charAt >= '0' && charAt <= '9')) {
                        i++;
                    }
                }
            }
            return false;
        }
        return true;
    }

    private MessageDigest getDigest(String str) throws SignatureException {
        if (JAR_DISABLED_CHECK.permits(DIGEST_PRIMITIVE_SET, str, (AlgorithmParameters) null)) {
            if (this.createdDigests == null) {
                this.createdDigests = new HashMap<>();
            }
            MessageDigest messageDigest = this.createdDigests.get(str);
            if (messageDigest != null) {
                return messageDigest;
            }
            try {
                messageDigest = MessageDigest.getInstance(str);
                this.createdDigests.put(str, messageDigest);
                return messageDigest;
            } catch (NoSuchAlgorithmException unused) {
                return messageDigest;
            }
        } else {
            throw new SignatureException("SignatureFile check failed. Disabled algorithm used: " + str);
        }
    }

    public void process(Hashtable<String, CodeSigner[]> hashtable, List<Object> list) throws IOException, SignatureException, NoSuchAlgorithmException, JarException, CertificateException {
        Object obj;
        try {
            obj = Providers.startJarVerification();
            try {
                processImpl(hashtable, list);
                Providers.stopJarVerification(obj);
            } catch (Throwable th) {
                th = th;
                Providers.stopJarVerification(obj);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            obj = null;
            Providers.stopJarVerification(obj);
            throw th;
        }
    }

    private void processImpl(Hashtable<String, CodeSigner[]> hashtable, List<Object> list) throws IOException, SignatureException, NoSuchAlgorithmException, JarException, CertificateException {
        Manifest manifest = new Manifest();
        manifest.read(new ByteArrayInputStream(this.sfBytes));
        String value = manifest.getMainAttributes().getValue(Attributes.Name.SIGNATURE_VERSION);
        if (value != null && value.equalsIgnoreCase("1.0")) {
            SignerInfo[] verify = this.block.verify(this.sfBytes);
            if (verify != null) {
                CodeSigner[] signers = getSigners(verify, this.block);
                if (signers != null) {
                    boolean verifyManifestHash = verifyManifestHash(manifest, this.f926md, list);
                    if (verifyManifestHash || verifyManifestMainAttrs(manifest, this.f926md)) {
                        for (Map.Entry next : manifest.getEntries().entrySet()) {
                            String str = (String) next.getKey();
                            if (verifyManifestHash || verifySection((Attributes) next.getValue(), str, this.f926md)) {
                                if (str.startsWith("./")) {
                                    str = str.substring(2);
                                }
                                if (str.startsWith("/")) {
                                    str = str.substring(1);
                                }
                                updateSigners(signers, hashtable, str);
                                Debug debug2 = debug;
                                if (debug2 != null) {
                                    debug2.println("processSignature signed name = " + str);
                                }
                            } else {
                                Debug debug3 = debug;
                                if (debug3 != null) {
                                    debug3.println("processSignature unsigned name = " + str);
                                }
                            }
                        }
                        updateSigners(signers, hashtable, JarFile.MANIFEST_NAME);
                        return;
                    }
                    throw new SecurityException("Invalid signature file digest for Manifest main attributes");
                }
                return;
            }
            throw new SecurityException("cannot verify signature block file " + this.name);
        }
    }

    private boolean verifyManifestHash(Manifest manifest, ManifestDigester manifestDigester, List<Object> list) throws IOException, SignatureException {
        boolean z = false;
        for (Map.Entry next : manifest.getMainAttributes().entrySet()) {
            String obj = next.getKey().toString();
            if (obj.toUpperCase(Locale.ENGLISH).endsWith("-DIGEST-MANIFEST")) {
                String substring = obj.substring(0, obj.length() - 16);
                list.add(obj);
                list.add(next.getValue());
                MessageDigest digest = getDigest(substring);
                if (digest != null) {
                    byte[] manifestDigest = manifestDigester.manifestDigest(digest);
                    byte[] decode = Base64.getMimeDecoder().decode((String) next.getValue());
                    Debug debug2 = debug;
                    if (debug2 != null) {
                        debug2.println("Signature File: Manifest digest " + digest.getAlgorithm());
                        debug2.println("  sigfile  " + toHex(decode));
                        debug2.println("  computed " + toHex(manifestDigest));
                        debug2.println();
                    }
                    if (MessageDigest.isEqual(manifestDigest, decode)) {
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    private boolean verifyManifestMainAttrs(Manifest manifest, ManifestDigester manifestDigester) throws IOException, SignatureException {
        MessageDigest digest;
        for (Map.Entry next : manifest.getMainAttributes().entrySet()) {
            String obj = next.getKey().toString();
            String upperCase = obj.toUpperCase(Locale.ENGLISH);
            String str = ATTR_DIGEST;
            if (upperCase.endsWith(str) && (digest = getDigest(obj.substring(0, obj.length() - str.length()))) != null) {
                byte[] digest2 = manifestDigester.get(ManifestDigester.MF_MAIN_ATTRS, false).digest(digest);
                byte[] decode = Base64.getMimeDecoder().decode((String) next.getValue());
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("Signature File: Manifest Main Attributes digest " + digest.getAlgorithm());
                    debug2.println("  sigfile  " + toHex(decode));
                    debug2.println("  computed " + toHex(digest2));
                    debug2.println();
                }
                if (!MessageDigest.isEqual(digest2, decode)) {
                    if (debug2 == null) {
                        return false;
                    }
                    debug2.println("Verification of Manifest main attributes failed");
                    debug2.println();
                    return false;
                }
            }
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x00ec A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0018 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean verifySection(java.util.jar.Attributes r9, java.lang.String r10, sun.security.util.ManifestDigester r11) throws java.p026io.IOException, java.security.SignatureException {
        /*
            r8 = this;
            sun.security.pkcs.PKCS7 r0 = r8.block
            boolean r0 = r0.isOldStyle()
            sun.security.util.ManifestDigester$Entry r11 = r11.get(r10, r0)
            if (r11 == 0) goto L_0x010e
            r0 = 0
            if (r9 == 0) goto L_0x010d
            java.util.Set r9 = r9.entrySet()
            java.util.Iterator r9 = r9.iterator()
            r1 = r0
        L_0x0018:
            boolean r2 = r9.hasNext()
            if (r2 == 0) goto L_0x010c
            java.lang.Object r2 = r9.next()
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2
            java.lang.Object r3 = r2.getKey()
            java.lang.String r3 = r3.toString()
            java.util.Locale r4 = java.util.Locale.ENGLISH
            java.lang.String r4 = r3.toUpperCase(r4)
            java.lang.String r5 = "-DIGEST"
            boolean r4 = r4.endsWith(r5)
            if (r4 == 0) goto L_0x0018
            int r4 = r3.length()
            int r4 = r4 + -7
            java.lang.String r3 = r3.substring(r0, r4)
            java.security.MessageDigest r3 = r8.getDigest(r3)
            if (r3 == 0) goto L_0x0018
            java.util.Base64$Decoder r4 = java.util.Base64.getMimeDecoder()
            java.lang.Object r2 = r2.getValue()
            java.lang.String r2 = (java.lang.String) r2
            byte[] r2 = r4.decode((java.lang.String) r2)
            boolean r4 = r8.workaround
            if (r4 == 0) goto L_0x0061
            byte[] r4 = r11.digestWorkaround(r3)
            goto L_0x0065
        L_0x0061:
            byte[] r4 = r11.digest(r3)
        L_0x0065:
            sun.security.util.Debug r5 = debug
            if (r5 == 0) goto L_0x00b3
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r7 = "Signature Block File: "
            r6.<init>((java.lang.String) r7)
            r6.append((java.lang.String) r10)
            java.lang.String r7 = " digest="
            r6.append((java.lang.String) r7)
            java.lang.String r7 = r3.getAlgorithm()
            r6.append((java.lang.String) r7)
            java.lang.String r6 = r6.toString()
            r5.println(r6)
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r7 = "  expected "
            r6.<init>((java.lang.String) r7)
            java.lang.String r7 = toHex(r2)
            r6.append((java.lang.String) r7)
            java.lang.String r6 = r6.toString()
            r5.println(r6)
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r7 = "  computed "
            r6.<init>((java.lang.String) r7)
            java.lang.String r7 = toHex(r4)
            r6.append((java.lang.String) r7)
            java.lang.String r6 = r6.toString()
            r5.println(r6)
            r5.println()
        L_0x00b3:
            boolean r4 = java.security.MessageDigest.isEqual(r4, r2)
            r6 = 1
            if (r4 == 0) goto L_0x00bc
        L_0x00ba:
            r1 = r6
            goto L_0x00e8
        L_0x00bc:
            boolean r4 = r8.workaround
            if (r4 != 0) goto L_0x00e7
            byte[] r4 = r11.digestWorkaround(r3)
            boolean r2 = java.security.MessageDigest.isEqual(r4, r2)
            if (r2 == 0) goto L_0x00e7
            if (r5 == 0) goto L_0x00e4
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "  re-computed "
            r1.<init>((java.lang.String) r2)
            java.lang.String r2 = toHex(r4)
            r1.append((java.lang.String) r2)
            java.lang.String r1 = r1.toString()
            r5.println(r1)
            r5.println()
        L_0x00e4:
            r8.workaround = r6
            goto L_0x00ba
        L_0x00e7:
            r6 = r0
        L_0x00e8:
            if (r6 == 0) goto L_0x00ec
            goto L_0x0018
        L_0x00ec:
            java.lang.SecurityException r8 = new java.lang.SecurityException
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r11 = "invalid "
            r9.<init>((java.lang.String) r11)
            java.lang.String r11 = r3.getAlgorithm()
            r9.append((java.lang.String) r11)
            java.lang.String r11 = " signature file digest for "
            r9.append((java.lang.String) r11)
            r9.append((java.lang.String) r10)
            java.lang.String r9 = r9.toString()
            r8.<init>((java.lang.String) r9)
            throw r8
        L_0x010c:
            r0 = r1
        L_0x010d:
            return r0
        L_0x010e:
            java.lang.SecurityException r8 = new java.lang.SecurityException
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r11 = "no manifest section for signature file entry "
            r9.<init>((java.lang.String) r11)
            r9.append((java.lang.String) r10)
            java.lang.String r9 = r9.toString()
            r8.<init>((java.lang.String) r9)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.util.SignatureFileVerifier.verifySection(java.util.jar.Attributes, java.lang.String, sun.security.util.ManifestDigester):boolean");
    }

    private CodeSigner[] getSigners(SignerInfo[] signerInfoArr, PKCS7 pkcs7) throws IOException, NoSuchAlgorithmException, SignatureException, CertificateException {
        ArrayList arrayList = null;
        for (SignerInfo signerInfo : signerInfoArr) {
            ArrayList<X509Certificate> certificateChain = signerInfo.getCertificateChain(pkcs7);
            CertPath generateCertPath = this.certificateFactory.generateCertPath((List<? extends Certificate>) certificateChain);
            if (arrayList == null) {
                arrayList = new ArrayList();
            }
            arrayList.add(new CodeSigner(generateCertPath, signerInfo.getTimestamp()));
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("Signature Block Certificate: " + certificateChain.get(0));
            }
        }
        if (arrayList != null) {
            return (CodeSigner[]) arrayList.toArray(new CodeSigner[arrayList.size()]);
        }
        return null;
    }

    static String toHex(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (int i = 0; i < bArr.length; i++) {
            char[] cArr = hexc;
            stringBuffer.append(cArr[(bArr[i] >> 4) & 15]);
            stringBuffer.append(cArr[bArr[i] & 15]);
        }
        return stringBuffer.toString();
    }

    static boolean contains(CodeSigner[] codeSignerArr, CodeSigner codeSigner) {
        for (CodeSigner equals : codeSignerArr) {
            if (equals.equals(codeSigner)) {
                return true;
            }
        }
        return false;
    }

    static boolean isSubSet(CodeSigner[] codeSignerArr, CodeSigner[] codeSignerArr2) {
        if (codeSignerArr2 == codeSignerArr) {
            return true;
        }
        for (CodeSigner contains : codeSignerArr) {
            if (!contains(codeSignerArr2, contains)) {
                return false;
            }
        }
        return true;
    }

    static boolean matches(CodeSigner[] codeSignerArr, CodeSigner[] codeSignerArr2, CodeSigner[] codeSignerArr3) {
        if (codeSignerArr2 == null && codeSignerArr == codeSignerArr3) {
            return true;
        }
        if ((codeSignerArr2 != null && !isSubSet(codeSignerArr2, codeSignerArr)) || !isSubSet(codeSignerArr3, codeSignerArr)) {
            return false;
        }
        for (int i = 0; i < codeSignerArr.length; i++) {
            if (!((codeSignerArr2 != null && contains(codeSignerArr2, codeSignerArr[i])) || contains(codeSignerArr3, codeSignerArr[i]))) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void updateSigners(CodeSigner[] codeSignerArr, Hashtable<String, CodeSigner[]> hashtable, String str) {
        CodeSigner[] codeSignerArr2 = hashtable.get(str);
        int size = this.signerCache.size();
        while (true) {
            size--;
            if (size != -1) {
                CodeSigner[] codeSignerArr3 = this.signerCache.get(size);
                if (matches(codeSignerArr3, codeSignerArr2, codeSignerArr)) {
                    hashtable.put(str, codeSignerArr3);
                    return;
                }
            } else {
                if (codeSignerArr2 != null) {
                    CodeSigner[] codeSignerArr4 = new CodeSigner[(codeSignerArr2.length + codeSignerArr.length)];
                    System.arraycopy((Object) codeSignerArr2, 0, (Object) codeSignerArr4, 0, codeSignerArr2.length);
                    System.arraycopy((Object) codeSignerArr, 0, (Object) codeSignerArr4, codeSignerArr2.length, codeSignerArr.length);
                    codeSignerArr = codeSignerArr4;
                }
                this.signerCache.add(codeSignerArr);
                hashtable.put(str, codeSignerArr);
                return;
            }
        }
    }
}
