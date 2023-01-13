package sun.security.pkcs;

import java.math.BigInteger;
import java.p026io.ByteArrayInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.AlgorithmParameters;
import java.security.CryptoPrimitive;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SignatureException;
import java.security.Timestamp;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import sun.misc.HexDumpEncoder;
import sun.security.timestamp.TimestampToken;
import sun.security.util.Debug;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.DisabledAlgorithmConstraints;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;

public class SignerInfo implements DerEncoder {
    private static final Set<CryptoPrimitive> DIGEST_PRIMITIVE_SET = Collections.unmodifiableSet(EnumSet.m1722of(CryptoPrimitive.MESSAGE_DIGEST));
    private static final DisabledAlgorithmConstraints JAR_DISABLED_CHECK = new DisabledAlgorithmConstraints(DisabledAlgorithmConstraints.PROPERTY_JAR_DISABLED_ALGS);
    private static final Set<CryptoPrimitive> SIG_PRIMITIVE_SET = Collections.unmodifiableSet(EnumSet.m1722of(CryptoPrimitive.SIGNATURE));
    PKCS9Attributes authenticatedAttributes;
    BigInteger certificateSerialNumber;
    AlgorithmId digestAlgorithmId;
    AlgorithmId digestEncryptionAlgorithmId;
    byte[] encryptedDigest;
    private boolean hasTimestamp;
    X500Name issuerName;
    Timestamp timestamp;
    PKCS9Attributes unauthenticatedAttributes;
    BigInteger version;

    public SignerInfo() {
        this.hasTimestamp = true;
    }

    public SignerInfo(X500Name x500Name, BigInteger bigInteger, AlgorithmId algorithmId, AlgorithmId algorithmId2, byte[] bArr) {
        this.hasTimestamp = true;
        this.version = BigInteger.ONE;
        this.issuerName = x500Name;
        this.certificateSerialNumber = bigInteger;
        this.digestAlgorithmId = algorithmId;
        this.digestEncryptionAlgorithmId = algorithmId2;
        this.encryptedDigest = bArr;
    }

    public SignerInfo(X500Name x500Name, BigInteger bigInteger, AlgorithmId algorithmId, PKCS9Attributes pKCS9Attributes, AlgorithmId algorithmId2, byte[] bArr, PKCS9Attributes pKCS9Attributes2) {
        this.hasTimestamp = true;
        this.version = BigInteger.ONE;
        this.issuerName = x500Name;
        this.certificateSerialNumber = bigInteger;
        this.digestAlgorithmId = algorithmId;
        this.authenticatedAttributes = pKCS9Attributes;
        this.digestEncryptionAlgorithmId = algorithmId2;
        this.encryptedDigest = bArr;
        this.unauthenticatedAttributes = pKCS9Attributes2;
    }

    public SignerInfo(DerInputStream derInputStream) throws IOException, ParsingException {
        this(derInputStream, false);
    }

    public SignerInfo(DerInputStream derInputStream, boolean z) throws IOException, ParsingException {
        this.hasTimestamp = true;
        this.version = derInputStream.getBigInteger();
        DerValue[] sequence = derInputStream.getSequence(2);
        this.issuerName = new X500Name(new DerValue((byte) 48, sequence[0].toByteArray()));
        this.certificateSerialNumber = sequence[1].getBigInteger();
        this.digestAlgorithmId = AlgorithmId.parse(derInputStream.getDerValue());
        if (z) {
            derInputStream.getSet(0);
        } else if (((byte) derInputStream.peekByte()) == -96) {
            this.authenticatedAttributes = new PKCS9Attributes(derInputStream);
        }
        this.digestEncryptionAlgorithmId = AlgorithmId.parse(derInputStream.getDerValue());
        this.encryptedDigest = derInputStream.getOctetString();
        if (z) {
            derInputStream.getSet(0);
        } else if (derInputStream.available() != 0 && ((byte) derInputStream.peekByte()) == -95) {
            this.unauthenticatedAttributes = new PKCS9Attributes(derInputStream, true);
        }
        if (derInputStream.available() != 0) {
            throw new ParsingException("extra data at the end");
        }
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derEncode(derOutputStream);
    }

    public void derEncode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putInteger(this.version);
        DerOutputStream derOutputStream2 = new DerOutputStream();
        this.issuerName.encode(derOutputStream2);
        derOutputStream2.putInteger(this.certificateSerialNumber);
        derOutputStream.write((byte) 48, derOutputStream2);
        this.digestAlgorithmId.encode(derOutputStream);
        PKCS9Attributes pKCS9Attributes = this.authenticatedAttributes;
        if (pKCS9Attributes != null) {
            pKCS9Attributes.encode((byte) -96, derOutputStream);
        }
        this.digestEncryptionAlgorithmId.encode(derOutputStream);
        derOutputStream.putOctetString(this.encryptedDigest);
        PKCS9Attributes pKCS9Attributes2 = this.unauthenticatedAttributes;
        if (pKCS9Attributes2 != null) {
            pKCS9Attributes2.encode((byte) -95, derOutputStream);
        }
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.write((byte) 48, derOutputStream);
        outputStream.write(derOutputStream3.toByteArray());
    }

    public X509Certificate getCertificate(PKCS7 pkcs7) throws IOException {
        return pkcs7.getCertificate(this.certificateSerialNumber, this.issuerName);
    }

    public ArrayList<X509Certificate> getCertificateChain(PKCS7 pkcs7) throws IOException {
        boolean z;
        X509Certificate certificate = pkcs7.getCertificate(this.certificateSerialNumber, this.issuerName);
        if (certificate == null) {
            return null;
        }
        ArrayList<X509Certificate> arrayList = new ArrayList<>();
        arrayList.add(certificate);
        X509Certificate[] certificates = pkcs7.getCertificates();
        if (certificates == null || certificate.getSubjectDN().equals(certificate.getIssuerDN())) {
            return arrayList;
        }
        Principal issuerDN = certificate.getIssuerDN();
        int i = 0;
        do {
            int i2 = i;
            while (true) {
                if (i2 >= certificates.length) {
                    z = false;
                    continue;
                    break;
                } else if (issuerDN.equals(certificates[i2].getSubjectDN())) {
                    arrayList.add(certificates[i2]);
                    if (certificates[i2].getSubjectDN().equals(certificates[i2].getIssuerDN())) {
                        i = certificates.length;
                    } else {
                        issuerDN = certificates[i2].getIssuerDN();
                        X509Certificate x509Certificate = certificates[i];
                        certificates[i] = certificates[i2];
                        certificates[i2] = x509Certificate;
                        i++;
                    }
                    z = true;
                    continue;
                } else {
                    i2++;
                }
            }
        } while (z);
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public SignerInfo verify(PKCS7 pkcs7, byte[] bArr) throws NoSuchAlgorithmException, SignatureException {
        try {
            return verify(pkcs7, (InputStream) new ByteArrayInputStream(bArr));
        } catch (IOException unused) {
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x015e, code lost:
        r9 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0176, code lost:
        throw new java.security.SignatureException("InvalidKey: " + r9.getMessage());
     */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x015e A[ExcHandler: InvalidKeyException (r9v4 'e' java.security.InvalidKeyException A[CUSTOM_DECLARE]), Splitter:B:1:0x0002] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public sun.security.pkcs.SignerInfo verify(sun.security.pkcs.PKCS7 r10, java.p026io.InputStream r11) throws java.security.NoSuchAlgorithmException, java.security.SignatureException, java.p026io.IOException {
        /*
            r9 = this;
            java.lang.String r0 = "Digest check failed. Disabled algorithm used: "
            sun.security.pkcs.ContentInfo r1 = r10.getContentInfo()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r11 != 0) goto L_0x0011
            java.io.ByteArrayInputStream r11 = new java.io.ByteArrayInputStream     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            byte[] r2 = r1.getContentBytes()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            r11.<init>(r2)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
        L_0x0011:
            sun.security.x509.AlgorithmId r2 = r9.getDigestAlgorithmId()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.String r2 = r2.getName()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            sun.security.pkcs.PKCS9Attributes r3 = r9.authenticatedAttributes     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            r4 = -1
            r5 = 4096(0x1000, float:5.74E-42)
            r6 = 0
            r7 = 0
            if (r3 != 0) goto L_0x0023
            goto L_0x0080
        L_0x0023:
            sun.security.util.ObjectIdentifier r8 = sun.security.pkcs.PKCS9Attribute.CONTENT_TYPE_OID     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.Object r3 = r3.getAttributeValue((sun.security.util.ObjectIdentifier) r8)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            sun.security.util.ObjectIdentifier r3 = (sun.security.util.ObjectIdentifier) r3     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r3 == 0) goto L_0x015d
            sun.security.util.ObjectIdentifier r1 = r1.contentType     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            boolean r1 = r3.equals((java.lang.Object) r1)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r1 != 0) goto L_0x0037
            goto L_0x015d
        L_0x0037:
            sun.security.pkcs.PKCS9Attributes r1 = r9.authenticatedAttributes     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            sun.security.util.ObjectIdentifier r3 = sun.security.pkcs.PKCS9Attribute.MESSAGE_DIGEST_OID     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.Object r1 = r1.getAttributeValue((sun.security.util.ObjectIdentifier) r3)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            byte[] r1 = (byte[]) r1     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r1 != 0) goto L_0x0044
            return r7
        L_0x0044:
            sun.security.util.DisabledAlgorithmConstraints r3 = JAR_DISABLED_CHECK     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.util.Set<java.security.CryptoPrimitive> r8 = DIGEST_PRIMITIVE_SET     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            boolean r3 = r3.permits(r8, r2, r7)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r3 == 0) goto L_0x014b
            java.security.MessageDigest r0 = java.security.MessageDigest.getInstance(r2)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            byte[] r3 = new byte[r5]     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
        L_0x0054:
            int r8 = r11.read(r3)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r8 == r4) goto L_0x005e
            r0.update(r3, r6, r8)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            goto L_0x0054
        L_0x005e:
            byte[] r11 = r0.digest()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            int r0 = r1.length     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            int r3 = r11.length     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r0 == r3) goto L_0x0067
            return r7
        L_0x0067:
            r0 = r6
        L_0x0068:
            int r3 = r1.length     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r0 >= r3) goto L_0x0075
            byte r3 = r1[r0]     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            byte r8 = r11[r0]     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r3 == r8) goto L_0x0072
            return r7
        L_0x0072:
            int r0 = r0 + 1
            goto L_0x0068
        L_0x0075:
            java.io.ByteArrayInputStream r11 = new java.io.ByteArrayInputStream     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            sun.security.pkcs.PKCS9Attributes r0 = r9.authenticatedAttributes     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            byte[] r0 = r0.getDerEncoding()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            r11.<init>(r0)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
        L_0x0080:
            sun.security.x509.AlgorithmId r0 = r9.getDigestEncryptionAlgorithmId()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.String r0 = r0.getName()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.String r1 = sun.security.x509.AlgorithmId.getEncAlgFromSigAlg(r0)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r1 == 0) goto L_0x008f
            r0 = r1
        L_0x008f:
            java.lang.String r0 = sun.security.x509.AlgorithmId.makeSigAlg(r2, r0)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            sun.security.util.DisabledAlgorithmConstraints r1 = JAR_DISABLED_CHECK     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.util.Set<java.security.CryptoPrimitive> r2 = SIG_PRIMITIVE_SET     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            boolean r3 = r1.permits(r2, r0, r7)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r3 == 0) goto L_0x0134
            java.security.cert.X509Certificate r10 = r9.getCertificate(r10)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r10 != 0) goto L_0x00a4
            return r7
        L_0x00a4:
            java.security.PublicKey r3 = r10.getPublicKey()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            boolean r1 = r1.permits((java.util.Set<java.security.CryptoPrimitive>) r2, (java.security.Key) r3)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r1 == 0) goto L_0x010d
            boolean r1 = r10.hasUnsupportedCriticalExtension()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r1 != 0) goto L_0x0105
            boolean[] r10 = r10.getKeyUsage()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r10 == 0) goto L_0x00e8
            sun.security.x509.KeyUsageExtension r1 = new sun.security.x509.KeyUsageExtension     // Catch:{ IOException -> 0x00e0, InvalidKeyException -> 0x015e }
            r1.<init>((boolean[]) r10)     // Catch:{ IOException -> 0x00e0, InvalidKeyException -> 0x015e }
            java.lang.String r10 = "digital_signature"
            java.lang.Boolean r10 = r1.get((java.lang.String) r10)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            boolean r10 = r10.booleanValue()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.String r2 = "non_repudiation"
            java.lang.Boolean r1 = r1.get((java.lang.String) r2)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            boolean r1 = r1.booleanValue()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r10 != 0) goto L_0x00e8
            if (r1 == 0) goto L_0x00d8
            goto L_0x00e8
        L_0x00d8:
            java.security.SignatureException r9 = new java.security.SignatureException     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.String r10 = "Key usage restricted: cannot be used for digital signatures"
            r9.<init>((java.lang.String) r10)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            throw r9     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
        L_0x00e0:
            java.security.SignatureException r9 = new java.security.SignatureException     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.String r10 = "Failed to parse keyUsage extension"
            r9.<init>((java.lang.String) r10)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            throw r9     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
        L_0x00e8:
            java.security.Signature r10 = java.security.Signature.getInstance(r0)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            r10.initVerify((java.security.PublicKey) r3)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            byte[] r0 = new byte[r5]     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
        L_0x00f1:
            int r1 = r11.read(r0)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r1 == r4) goto L_0x00fb
            r10.update(r0, r6, r1)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            goto L_0x00f1
        L_0x00fb:
            byte[] r11 = r9.encryptedDigest     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            boolean r10 = r10.verify(r11)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            if (r10 == 0) goto L_0x0104
            return r9
        L_0x0104:
            return r7
        L_0x0105:
            java.security.SignatureException r9 = new java.security.SignatureException     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.String r10 = "Certificate has unsupported critical extension(s)"
            r9.<init>((java.lang.String) r10)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            throw r9     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
        L_0x010d:
            java.security.SignatureException r9 = new java.security.SignatureException     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            r10.<init>()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.String r11 = "Public key check failed. Disabled key used: "
            r10.append((java.lang.String) r11)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            int r11 = sun.security.util.KeyUtil.getKeySize(r3)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            r10.append((int) r11)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.String r11 = " bit "
            r10.append((java.lang.String) r11)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.String r11 = r3.getAlgorithm()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            r10.append((java.lang.String) r11)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.String r10 = r10.toString()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            r9.<init>((java.lang.String) r10)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            throw r9     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
        L_0x0134:
            java.security.SignatureException r9 = new java.security.SignatureException     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            r10.<init>()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.String r11 = "Signature check failed. Disabled algorithm used: "
            r10.append((java.lang.String) r11)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            r10.append((java.lang.String) r0)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.String r10 = r10.toString()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            r9.<init>((java.lang.String) r10)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            throw r9     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
        L_0x014b:
            java.security.SignatureException r9 = new java.security.SignatureException     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            r10.<init>((java.lang.String) r0)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            r10.append((java.lang.String) r2)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            java.lang.String r10 = r10.toString()     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            r9.<init>((java.lang.String) r10)     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
            throw r9     // Catch:{ IOException -> 0x0177, InvalidKeyException -> 0x015e }
        L_0x015d:
            return r7
        L_0x015e:
            r9 = move-exception
            java.security.SignatureException r10 = new java.security.SignatureException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r0 = "InvalidKey: "
            r11.<init>((java.lang.String) r0)
            java.lang.String r9 = r9.getMessage()
            r11.append((java.lang.String) r9)
            java.lang.String r9 = r11.toString()
            r10.<init>((java.lang.String) r9)
            throw r10
        L_0x0177:
            r9 = move-exception
            java.security.SignatureException r10 = new java.security.SignatureException
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r0 = "IO error verifying signature:\n"
            r11.<init>((java.lang.String) r0)
            java.lang.String r9 = r9.getMessage()
            r11.append((java.lang.String) r9)
            java.lang.String r9 = r11.toString()
            r10.<init>((java.lang.String) r9)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.pkcs.SignerInfo.verify(sun.security.pkcs.PKCS7, java.io.InputStream):sun.security.pkcs.SignerInfo");
    }

    /* access modifiers changed from: package-private */
    public SignerInfo verify(PKCS7 pkcs7) throws NoSuchAlgorithmException, SignatureException {
        byte[] bArr = null;
        return verify(pkcs7, (byte[]) null);
    }

    public BigInteger getVersion() {
        return this.version;
    }

    public X500Name getIssuerName() {
        return this.issuerName;
    }

    public BigInteger getCertificateSerialNumber() {
        return this.certificateSerialNumber;
    }

    public AlgorithmId getDigestAlgorithmId() {
        return this.digestAlgorithmId;
    }

    public PKCS9Attributes getAuthenticatedAttributes() {
        return this.authenticatedAttributes;
    }

    public AlgorithmId getDigestEncryptionAlgorithmId() {
        return this.digestEncryptionAlgorithmId;
    }

    public byte[] getEncryptedDigest() {
        return this.encryptedDigest;
    }

    public PKCS9Attributes getUnauthenticatedAttributes() {
        return this.unauthenticatedAttributes;
    }

    public PKCS7 getTsToken() throws IOException {
        PKCS9Attribute attribute;
        PKCS9Attributes pKCS9Attributes = this.unauthenticatedAttributes;
        if (pKCS9Attributes == null || (attribute = pKCS9Attributes.getAttribute(PKCS9Attribute.SIGNATURE_TIMESTAMP_TOKEN_OID)) == null) {
            return null;
        }
        return new PKCS7((byte[]) attribute.getValue());
    }

    public Timestamp getTimestamp() throws IOException, NoSuchAlgorithmException, SignatureException, CertificateException {
        Timestamp timestamp2 = this.timestamp;
        if (timestamp2 != null || !this.hasTimestamp) {
            return timestamp2;
        }
        PKCS7 tsToken = getTsToken();
        if (tsToken == null) {
            this.hasTimestamp = false;
            return null;
        }
        byte[] data = tsToken.getContentInfo().getData();
        CertPath generateCertPath = CertificateFactory.getInstance("X.509").generateCertPath((List<? extends Certificate>) tsToken.verify(data)[0].getCertificateChain(tsToken));
        TimestampToken timestampToken = new TimestampToken(data);
        verifyTimestamp(timestampToken);
        Timestamp timestamp3 = new Timestamp(timestampToken.getDate(), generateCertPath);
        this.timestamp = timestamp3;
        return timestamp3;
    }

    private void verifyTimestamp(TimestampToken timestampToken) throws NoSuchAlgorithmException, SignatureException {
        String name = timestampToken.getHashAlgorithm().getName();
        if (JAR_DISABLED_CHECK.permits(DIGEST_PRIMITIVE_SET, name, (AlgorithmParameters) null)) {
            if (!Arrays.equals(timestampToken.getHashedMessage(), MessageDigest.getInstance(name).digest(this.encryptedDigest))) {
                throw new SignatureException("Signature timestamp (#" + timestampToken.getSerialNumber() + ") generated on " + timestampToken.getDate() + " is inapplicable");
            }
            return;
        }
        throw new SignatureException("Timestamp token digest check failed. Disabled algorithm used: " + name);
    }

    public String toString() {
        HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
        String str = ((("Signer Info for (issuer): " + this.issuerName + "\n") + "\tversion: " + Debug.toHexString(this.version) + "\n") + "\tcertificateSerialNumber: " + Debug.toHexString(this.certificateSerialNumber) + "\n") + "\tdigestAlgorithmId: " + this.digestAlgorithmId + "\n";
        if (this.authenticatedAttributes != null) {
            str = str + "\tauthenticatedAttributes: " + this.authenticatedAttributes + "\n";
        }
        String str2 = (str + "\tdigestEncryptionAlgorithmId: " + this.digestEncryptionAlgorithmId + "\n") + "\tencryptedDigest: \n" + hexDumpEncoder.encodeBuffer(this.encryptedDigest) + "\n";
        if (this.unauthenticatedAttributes == null) {
            return str2;
        }
        return str2 + "\tunauthenticatedAttributes: " + this.unauthenticatedAttributes + "\n";
    }
}
