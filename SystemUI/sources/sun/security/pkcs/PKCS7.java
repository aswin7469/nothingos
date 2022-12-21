package sun.security.pkcs;

import java.math.BigInteger;
import java.p026io.DataInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.security.auth.x500.X500Principal;
import sun.security.util.Debug;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;
import sun.security.x509.X509CRLImpl;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

public class PKCS7 {
    private Principal[] certIssuerNames;
    private X509Certificate[] certificates;
    private ContentInfo contentInfo;
    private ObjectIdentifier contentType;
    private X509CRL[] crls;
    private AlgorithmId[] digestAlgorithmIds;
    private boolean oldStyle;
    private SignerInfo[] signerInfos;
    private BigInteger version;

    public PKCS7(InputStream inputStream) throws ParsingException, IOException {
        this.version = null;
        this.digestAlgorithmIds = null;
        this.contentInfo = null;
        this.certificates = null;
        this.crls = null;
        this.signerInfos = null;
        this.oldStyle = false;
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        byte[] bArr = new byte[dataInputStream.available()];
        dataInputStream.readFully(bArr);
        parse(new DerInputStream(bArr));
    }

    public PKCS7(DerInputStream derInputStream) throws ParsingException {
        this.version = null;
        this.digestAlgorithmIds = null;
        this.contentInfo = null;
        this.certificates = null;
        this.crls = null;
        this.signerInfos = null;
        this.oldStyle = false;
        parse(derInputStream);
    }

    public PKCS7(byte[] bArr) throws ParsingException {
        this.version = null;
        this.digestAlgorithmIds = null;
        this.contentInfo = null;
        this.certificates = null;
        this.crls = null;
        this.signerInfos = null;
        this.oldStyle = false;
        try {
            parse(new DerInputStream(bArr));
        } catch (IOException e) {
            ParsingException parsingException = new ParsingException("Unable to parse the encoded bytes");
            parsingException.initCause(e);
            throw parsingException;
        }
    }

    private void parse(DerInputStream derInputStream) throws ParsingException {
        try {
            derInputStream.mark(derInputStream.available());
            parse(derInputStream, false);
        } catch (IOException e) {
            try {
                derInputStream.reset();
                parse(derInputStream, true);
                this.oldStyle = true;
            } catch (IOException e2) {
                ParsingException parsingException = new ParsingException(e2.getMessage());
                parsingException.initCause(e);
                parsingException.addSuppressed(e2);
                throw parsingException;
            }
        }
    }

    private void parse(DerInputStream derInputStream, boolean z) throws IOException {
        ContentInfo contentInfo2 = new ContentInfo(derInputStream, z);
        this.contentInfo = contentInfo2;
        this.contentType = contentInfo2.contentType;
        DerValue content = this.contentInfo.getContent();
        if (this.contentType.equals((Object) ContentInfo.SIGNED_DATA_OID)) {
            parseSignedData(content);
        } else if (this.contentType.equals((Object) ContentInfo.OLD_SIGNED_DATA_OID)) {
            parseOldSignedData(content);
        } else if (this.contentType.equals((Object) ContentInfo.NETSCAPE_CERT_SEQUENCE_OID)) {
            parseNetscapeCertChain(content);
        } else {
            throw new ParsingException("content type " + this.contentType + " not supported.");
        }
    }

    public PKCS7(AlgorithmId[] algorithmIdArr, ContentInfo contentInfo2, X509Certificate[] x509CertificateArr, X509CRL[] x509crlArr, SignerInfo[] signerInfoArr) {
        this.version = null;
        this.digestAlgorithmIds = null;
        this.contentInfo = null;
        this.certificates = null;
        this.crls = null;
        this.signerInfos = null;
        this.oldStyle = false;
        this.version = BigInteger.ONE;
        this.digestAlgorithmIds = algorithmIdArr;
        this.contentInfo = contentInfo2;
        this.certificates = x509CertificateArr;
        this.crls = x509crlArr;
        this.signerInfos = signerInfoArr;
    }

    public PKCS7(AlgorithmId[] algorithmIdArr, ContentInfo contentInfo2, X509Certificate[] x509CertificateArr, SignerInfo[] signerInfoArr) {
        this(algorithmIdArr, contentInfo2, x509CertificateArr, (X509CRL[]) null, signerInfoArr);
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0078  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseNetscapeCertChain(sun.security.util.DerValue r9) throws sun.security.pkcs.ParsingException, java.p026io.IOException {
        /*
            r8 = this;
            sun.security.util.DerInputStream r0 = new sun.security.util.DerInputStream
            byte[] r9 = r9.toByteArray()
            r0.<init>((byte[]) r9)
            r9 = 2
            r1 = 1
            sun.security.util.DerValue[] r9 = r0.getSequence(r9, r1)
            int r0 = r9.length
            java.security.cert.X509Certificate[] r0 = new java.security.cert.X509Certificate[r0]
            r8.certificates = r0
            r0 = 0
            java.lang.String r1 = "X.509"
            java.security.cert.CertificateFactory r1 = java.security.cert.CertificateFactory.getInstance(r1)     // Catch:{ CertificateException -> 0x001c }
            goto L_0x001d
        L_0x001c:
            r1 = r0
        L_0x001d:
            r2 = 0
        L_0x001e:
            int r3 = r9.length
            if (r2 >= r3) goto L_0x007c
            r3 = r9[r2]     // Catch:{ CertificateException -> 0x0068, IOException -> 0x005a }
            byte[] r3 = r3.getOriginalEncodedForm()     // Catch:{ CertificateException -> 0x0068, IOException -> 0x005a }
            if (r1 != 0) goto L_0x0035
            java.security.cert.X509Certificate[] r4 = r8.certificates     // Catch:{ CertificateException -> 0x0068, IOException -> 0x005a }
            sun.security.x509.X509CertImpl r5 = new sun.security.x509.X509CertImpl     // Catch:{ CertificateException -> 0x0068, IOException -> 0x005a }
            r6 = r9[r2]     // Catch:{ CertificateException -> 0x0068, IOException -> 0x005a }
            r5.<init>(r6, r3)     // Catch:{ CertificateException -> 0x0068, IOException -> 0x005a }
            r4[r2] = r5     // Catch:{ CertificateException -> 0x0068, IOException -> 0x005a }
            goto L_0x004c
        L_0x0035:
            java.io.ByteArrayInputStream r4 = new java.io.ByteArrayInputStream     // Catch:{ CertificateException -> 0x0068, IOException -> 0x005a }
            r4.<init>(r3)     // Catch:{ CertificateException -> 0x0068, IOException -> 0x005a }
            java.security.cert.X509Certificate[] r5 = r8.certificates     // Catch:{ CertificateException -> 0x0055, IOException -> 0x0052, all -> 0x004f }
            sun.security.pkcs.PKCS7$VerbatimX509Certificate r6 = new sun.security.pkcs.PKCS7$VerbatimX509Certificate     // Catch:{ CertificateException -> 0x0055, IOException -> 0x0052, all -> 0x004f }
            java.security.cert.Certificate r7 = r1.generateCertificate(r4)     // Catch:{ CertificateException -> 0x0055, IOException -> 0x0052, all -> 0x004f }
            java.security.cert.X509Certificate r7 = (java.security.cert.X509Certificate) r7     // Catch:{ CertificateException -> 0x0055, IOException -> 0x0052, all -> 0x004f }
            r6.<init>(r7, r3)     // Catch:{ CertificateException -> 0x0055, IOException -> 0x0052, all -> 0x004f }
            r5[r2] = r6     // Catch:{ CertificateException -> 0x0055, IOException -> 0x0052, all -> 0x004f }
            r4.close()     // Catch:{ CertificateException -> 0x0055, IOException -> 0x0052, all -> 0x004f }
        L_0x004c:
            int r2 = r2 + 1
            goto L_0x001e
        L_0x004f:
            r8 = move-exception
            r0 = r4
            goto L_0x0076
        L_0x0052:
            r8 = move-exception
            r0 = r4
            goto L_0x005b
        L_0x0055:
            r8 = move-exception
            r0 = r4
            goto L_0x0069
        L_0x0058:
            r8 = move-exception
            goto L_0x0076
        L_0x005a:
            r8 = move-exception
        L_0x005b:
            sun.security.pkcs.ParsingException r9 = new sun.security.pkcs.ParsingException     // Catch:{ all -> 0x0058 }
            java.lang.String r1 = r8.getMessage()     // Catch:{ all -> 0x0058 }
            r9.<init>(r1)     // Catch:{ all -> 0x0058 }
            r9.initCause(r8)     // Catch:{ all -> 0x0058 }
            throw r9     // Catch:{ all -> 0x0058 }
        L_0x0068:
            r8 = move-exception
        L_0x0069:
            sun.security.pkcs.ParsingException r9 = new sun.security.pkcs.ParsingException     // Catch:{ all -> 0x0058 }
            java.lang.String r1 = r8.getMessage()     // Catch:{ all -> 0x0058 }
            r9.<init>(r1)     // Catch:{ all -> 0x0058 }
            r9.initCause(r8)     // Catch:{ all -> 0x0058 }
            throw r9     // Catch:{ all -> 0x0058 }
        L_0x0076:
            if (r0 == 0) goto L_0x007b
            r0.close()
        L_0x007b:
            throw r8
        L_0x007c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.pkcs.PKCS7.parseNetscapeCertChain(sun.security.util.DerValue):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:43:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0132  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseSignedData(sun.security.util.DerValue r14) throws sun.security.pkcs.ParsingException, java.p026io.IOException {
        /*
            r13 = this;
            sun.security.util.DerInputStream r14 = r14.toDerInputStream()
            java.math.BigInteger r0 = r14.getBigInteger()
            r13.version = r0
            r0 = 1
            sun.security.util.DerValue[] r1 = r14.getSet(r0)
            int r2 = r1.length
            sun.security.x509.AlgorithmId[] r3 = new sun.security.x509.AlgorithmId[r2]
            r13.digestAlgorithmIds = r3
            r3 = 0
            r4 = r3
        L_0x0016:
            if (r4 >= r2) goto L_0x0041
            r5 = r1[r4]     // Catch:{ IOException -> 0x0025 }
            sun.security.x509.AlgorithmId[] r6 = r13.digestAlgorithmIds     // Catch:{ IOException -> 0x0025 }
            sun.security.x509.AlgorithmId r5 = sun.security.x509.AlgorithmId.parse(r5)     // Catch:{ IOException -> 0x0025 }
            r6[r4] = r5     // Catch:{ IOException -> 0x0025 }
            int r4 = r4 + 1
            goto L_0x0016
        L_0x0025:
            r13 = move-exception
            sun.security.pkcs.ParsingException r14 = new sun.security.pkcs.ParsingException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Error parsing digest AlgorithmId IDs: "
            r0.<init>((java.lang.String) r1)
            java.lang.String r1 = r13.getMessage()
            r0.append((java.lang.String) r1)
            java.lang.String r0 = r0.toString()
            r14.<init>(r0)
            r14.initCause(r13)
            throw r14
        L_0x0041:
            sun.security.pkcs.ContentInfo r1 = new sun.security.pkcs.ContentInfo
            r1.<init>((sun.security.util.DerInputStream) r14)
            r13.contentInfo = r1
            r1 = 0
            java.lang.String r2 = "X.509"
            java.security.cert.CertificateFactory r2 = java.security.cert.CertificateFactory.getInstance(r2)     // Catch:{ CertificateException -> 0x0050 }
            goto L_0x0051
        L_0x0050:
            r2 = r1
        L_0x0051:
            int r4 = r14.peekByte()
            byte r4 = (byte) r4
            r5 = -96
            if (r4 != r5) goto L_0x00dc
            r4 = 2
            sun.security.util.DerValue[] r4 = r14.getSet(r4, r0, r0)
            int r5 = r4.length
            java.security.cert.X509Certificate[] r6 = new java.security.cert.X509Certificate[r5]
            r13.certificates = r6
            r6 = r3
            r7 = r6
        L_0x0066:
            if (r6 >= r5) goto L_0x00d0
            r8 = r4[r6]     // Catch:{ CertificateException -> 0x00bc, IOException -> 0x00ae }
            byte r8 = r8.getTag()     // Catch:{ CertificateException -> 0x00bc, IOException -> 0x00ae }
            r9 = 48
            if (r8 != r9) goto L_0x00a9
            r8 = r4[r6]     // Catch:{ CertificateException -> 0x00bc, IOException -> 0x00ae }
            byte[] r8 = r8.getOriginalEncodedForm()     // Catch:{ CertificateException -> 0x00bc, IOException -> 0x00ae }
            if (r2 != 0) goto L_0x0086
            java.security.cert.X509Certificate[] r9 = r13.certificates     // Catch:{ CertificateException -> 0x00bc, IOException -> 0x00ae }
            sun.security.x509.X509CertImpl r10 = new sun.security.x509.X509CertImpl     // Catch:{ CertificateException -> 0x00bc, IOException -> 0x00ae }
            r11 = r4[r6]     // Catch:{ CertificateException -> 0x00bc, IOException -> 0x00ae }
            r10.<init>(r11, r8)     // Catch:{ CertificateException -> 0x00bc, IOException -> 0x00ae }
            r9[r7] = r10     // Catch:{ CertificateException -> 0x00bc, IOException -> 0x00ae }
            goto L_0x009d
        L_0x0086:
            java.io.ByteArrayInputStream r9 = new java.io.ByteArrayInputStream     // Catch:{ CertificateException -> 0x00bc, IOException -> 0x00ae }
            r9.<init>(r8)     // Catch:{ CertificateException -> 0x00bc, IOException -> 0x00ae }
            java.security.cert.X509Certificate[] r10 = r13.certificates     // Catch:{ CertificateException -> 0x00a6, IOException -> 0x00a3, all -> 0x00a0 }
            sun.security.pkcs.PKCS7$VerbatimX509Certificate r11 = new sun.security.pkcs.PKCS7$VerbatimX509Certificate     // Catch:{ CertificateException -> 0x00a6, IOException -> 0x00a3, all -> 0x00a0 }
            java.security.cert.Certificate r12 = r2.generateCertificate(r9)     // Catch:{ CertificateException -> 0x00a6, IOException -> 0x00a3, all -> 0x00a0 }
            java.security.cert.X509Certificate r12 = (java.security.cert.X509Certificate) r12     // Catch:{ CertificateException -> 0x00a6, IOException -> 0x00a3, all -> 0x00a0 }
            r11.<init>(r12, r8)     // Catch:{ CertificateException -> 0x00a6, IOException -> 0x00a3, all -> 0x00a0 }
            r10[r7] = r11     // Catch:{ CertificateException -> 0x00a6, IOException -> 0x00a3, all -> 0x00a0 }
            r9.close()     // Catch:{ CertificateException -> 0x00a6, IOException -> 0x00a3, all -> 0x00a0 }
        L_0x009d:
            int r7 = r7 + 1
            goto L_0x00a9
        L_0x00a0:
            r13 = move-exception
            r1 = r9
            goto L_0x00ca
        L_0x00a3:
            r13 = move-exception
            r1 = r9
            goto L_0x00af
        L_0x00a6:
            r13 = move-exception
            r1 = r9
            goto L_0x00bd
        L_0x00a9:
            int r6 = r6 + 1
            goto L_0x0066
        L_0x00ac:
            r13 = move-exception
            goto L_0x00ca
        L_0x00ae:
            r13 = move-exception
        L_0x00af:
            sun.security.pkcs.ParsingException r14 = new sun.security.pkcs.ParsingException     // Catch:{ all -> 0x00ac }
            java.lang.String r0 = r13.getMessage()     // Catch:{ all -> 0x00ac }
            r14.<init>(r0)     // Catch:{ all -> 0x00ac }
            r14.initCause(r13)     // Catch:{ all -> 0x00ac }
            throw r14     // Catch:{ all -> 0x00ac }
        L_0x00bc:
            r13 = move-exception
        L_0x00bd:
            sun.security.pkcs.ParsingException r14 = new sun.security.pkcs.ParsingException     // Catch:{ all -> 0x00ac }
            java.lang.String r0 = r13.getMessage()     // Catch:{ all -> 0x00ac }
            r14.<init>(r0)     // Catch:{ all -> 0x00ac }
            r14.initCause(r13)     // Catch:{ all -> 0x00ac }
            throw r14     // Catch:{ all -> 0x00ac }
        L_0x00ca:
            if (r1 == 0) goto L_0x00cf
            r1.close()
        L_0x00cf:
            throw r13
        L_0x00d0:
            if (r7 == r5) goto L_0x00dc
            java.security.cert.X509Certificate[] r4 = r13.certificates
            java.lang.Object[] r4 = java.util.Arrays.copyOf((T[]) r4, (int) r7)
            java.security.cert.X509Certificate[] r4 = (java.security.cert.X509Certificate[]) r4
            r13.certificates = r4
        L_0x00dc:
            int r4 = r14.peekByte()
            byte r4 = (byte) r4
            r5 = -95
            if (r4 != r5) goto L_0x0136
            sun.security.util.DerValue[] r4 = r14.getSet(r0, r0)
            int r5 = r4.length
            java.security.cert.X509CRL[] r6 = new java.security.cert.X509CRL[r5]
            r13.crls = r6
            r6 = r3
        L_0x00ef:
            if (r6 >= r5) goto L_0x0136
            if (r2 != 0) goto L_0x00ff
            java.security.cert.X509CRL[] r7 = r13.crls     // Catch:{ CRLException -> 0x0122 }
            sun.security.x509.X509CRLImpl r8 = new sun.security.x509.X509CRLImpl     // Catch:{ CRLException -> 0x0122 }
            r9 = r4[r6]     // Catch:{ CRLException -> 0x0122 }
            r8.<init>((sun.security.util.DerValue) r9)     // Catch:{ CRLException -> 0x0122 }
            r7[r6] = r8     // Catch:{ CRLException -> 0x0122 }
            goto L_0x0117
        L_0x00ff:
            r7 = r4[r6]     // Catch:{ CRLException -> 0x0122 }
            byte[] r7 = r7.toByteArray()     // Catch:{ CRLException -> 0x0122 }
            java.io.ByteArrayInputStream r8 = new java.io.ByteArrayInputStream     // Catch:{ CRLException -> 0x0122 }
            r8.<init>(r7)     // Catch:{ CRLException -> 0x0122 }
            java.security.cert.X509CRL[] r7 = r13.crls     // Catch:{ CRLException -> 0x011d, all -> 0x011a }
            java.security.cert.CRL r9 = r2.generateCRL(r8)     // Catch:{ CRLException -> 0x011d, all -> 0x011a }
            java.security.cert.X509CRL r9 = (java.security.cert.X509CRL) r9     // Catch:{ CRLException -> 0x011d, all -> 0x011a }
            r7[r6] = r9     // Catch:{ CRLException -> 0x011d, all -> 0x011a }
            r8.close()     // Catch:{ CRLException -> 0x011d, all -> 0x011a }
        L_0x0117:
            int r6 = r6 + 1
            goto L_0x00ef
        L_0x011a:
            r13 = move-exception
            r1 = r8
            goto L_0x0130
        L_0x011d:
            r13 = move-exception
            r1 = r8
            goto L_0x0123
        L_0x0120:
            r13 = move-exception
            goto L_0x0130
        L_0x0122:
            r13 = move-exception
        L_0x0123:
            sun.security.pkcs.ParsingException r14 = new sun.security.pkcs.ParsingException     // Catch:{ all -> 0x0120 }
            java.lang.String r0 = r13.getMessage()     // Catch:{ all -> 0x0120 }
            r14.<init>(r0)     // Catch:{ all -> 0x0120 }
            r14.initCause(r13)     // Catch:{ all -> 0x0120 }
            throw r14     // Catch:{ all -> 0x0120 }
        L_0x0130:
            if (r1 == 0) goto L_0x0135
            r1.close()
        L_0x0135:
            throw r13
        L_0x0136:
            sun.security.util.DerValue[] r14 = r14.getSet(r0)
            int r0 = r14.length
            sun.security.pkcs.SignerInfo[] r1 = new sun.security.pkcs.SignerInfo[r0]
            r13.signerInfos = r1
        L_0x013f:
            if (r3 >= r0) goto L_0x0153
            r1 = r14[r3]
            sun.security.util.DerInputStream r1 = r1.toDerInputStream()
            sun.security.pkcs.SignerInfo[] r2 = r13.signerInfos
            sun.security.pkcs.SignerInfo r4 = new sun.security.pkcs.SignerInfo
            r4.<init>(r1)
            r2[r3] = r4
            int r3 = r3 + 1
            goto L_0x013f
        L_0x0153:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.pkcs.PKCS7.parseSignedData(sun.security.util.DerValue):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x00a1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseOldSignedData(sun.security.util.DerValue r13) throws sun.security.pkcs.ParsingException, java.p026io.IOException {
        /*
            r12 = this;
            sun.security.util.DerInputStream r13 = r13.toDerInputStream()
            java.math.BigInteger r0 = r13.getBigInteger()
            r12.version = r0
            r0 = 1
            sun.security.util.DerValue[] r1 = r13.getSet(r0)
            int r2 = r1.length
            sun.security.x509.AlgorithmId[] r3 = new sun.security.x509.AlgorithmId[r2]
            r12.digestAlgorithmIds = r3
            r3 = 0
            r4 = r3
        L_0x0016:
            if (r4 >= r2) goto L_0x002d
            r5 = r1[r4]     // Catch:{ IOException -> 0x0025 }
            sun.security.x509.AlgorithmId[] r6 = r12.digestAlgorithmIds     // Catch:{ IOException -> 0x0025 }
            sun.security.x509.AlgorithmId r5 = sun.security.x509.AlgorithmId.parse(r5)     // Catch:{ IOException -> 0x0025 }
            r6[r4] = r5     // Catch:{ IOException -> 0x0025 }
            int r4 = r4 + 1
            goto L_0x0016
        L_0x0025:
            sun.security.pkcs.ParsingException r12 = new sun.security.pkcs.ParsingException
            java.lang.String r13 = "Error parsing digest AlgorithmId IDs"
            r12.<init>(r13)
            throw r12
        L_0x002d:
            sun.security.pkcs.ContentInfo r1 = new sun.security.pkcs.ContentInfo
            r1.<init>((sun.security.util.DerInputStream) r13, (boolean) r0)
            r12.contentInfo = r1
            r1 = 0
            java.lang.String r2 = "X.509"
            java.security.cert.CertificateFactory r2 = java.security.cert.CertificateFactory.getInstance(r2)     // Catch:{ CertificateException -> 0x003c }
            goto L_0x003d
        L_0x003c:
            r2 = r1
        L_0x003d:
            r4 = 2
            sun.security.util.DerValue[] r4 = r13.getSet(r4, r3, r0)
            int r5 = r4.length
            java.security.cert.X509Certificate[] r6 = new java.security.cert.X509Certificate[r5]
            r12.certificates = r6
            r6 = r3
        L_0x0048:
            if (r6 >= r5) goto L_0x00a5
            r7 = r4[r6]     // Catch:{ CertificateException -> 0x0091, IOException -> 0x0083 }
            byte[] r7 = r7.getOriginalEncodedForm()     // Catch:{ CertificateException -> 0x0091, IOException -> 0x0083 }
            if (r2 != 0) goto L_0x005e
            java.security.cert.X509Certificate[] r8 = r12.certificates     // Catch:{ CertificateException -> 0x0091, IOException -> 0x0083 }
            sun.security.x509.X509CertImpl r9 = new sun.security.x509.X509CertImpl     // Catch:{ CertificateException -> 0x0091, IOException -> 0x0083 }
            r10 = r4[r6]     // Catch:{ CertificateException -> 0x0091, IOException -> 0x0083 }
            r9.<init>(r10, r7)     // Catch:{ CertificateException -> 0x0091, IOException -> 0x0083 }
            r8[r6] = r9     // Catch:{ CertificateException -> 0x0091, IOException -> 0x0083 }
            goto L_0x0075
        L_0x005e:
            java.io.ByteArrayInputStream r8 = new java.io.ByteArrayInputStream     // Catch:{ CertificateException -> 0x0091, IOException -> 0x0083 }
            r8.<init>(r7)     // Catch:{ CertificateException -> 0x0091, IOException -> 0x0083 }
            java.security.cert.X509Certificate[] r9 = r12.certificates     // Catch:{ CertificateException -> 0x007e, IOException -> 0x007b, all -> 0x0078 }
            sun.security.pkcs.PKCS7$VerbatimX509Certificate r10 = new sun.security.pkcs.PKCS7$VerbatimX509Certificate     // Catch:{ CertificateException -> 0x007e, IOException -> 0x007b, all -> 0x0078 }
            java.security.cert.Certificate r11 = r2.generateCertificate(r8)     // Catch:{ CertificateException -> 0x007e, IOException -> 0x007b, all -> 0x0078 }
            java.security.cert.X509Certificate r11 = (java.security.cert.X509Certificate) r11     // Catch:{ CertificateException -> 0x007e, IOException -> 0x007b, all -> 0x0078 }
            r10.<init>(r11, r7)     // Catch:{ CertificateException -> 0x007e, IOException -> 0x007b, all -> 0x0078 }
            r9[r6] = r10     // Catch:{ CertificateException -> 0x007e, IOException -> 0x007b, all -> 0x0078 }
            r8.close()     // Catch:{ CertificateException -> 0x007e, IOException -> 0x007b, all -> 0x0078 }
        L_0x0075:
            int r6 = r6 + 1
            goto L_0x0048
        L_0x0078:
            r12 = move-exception
            r1 = r8
            goto L_0x009f
        L_0x007b:
            r12 = move-exception
            r1 = r8
            goto L_0x0084
        L_0x007e:
            r12 = move-exception
            r1 = r8
            goto L_0x0092
        L_0x0081:
            r12 = move-exception
            goto L_0x009f
        L_0x0083:
            r12 = move-exception
        L_0x0084:
            sun.security.pkcs.ParsingException r13 = new sun.security.pkcs.ParsingException     // Catch:{ all -> 0x0081 }
            java.lang.String r0 = r12.getMessage()     // Catch:{ all -> 0x0081 }
            r13.<init>(r0)     // Catch:{ all -> 0x0081 }
            r13.initCause(r12)     // Catch:{ all -> 0x0081 }
            throw r13     // Catch:{ all -> 0x0081 }
        L_0x0091:
            r12 = move-exception
        L_0x0092:
            sun.security.pkcs.ParsingException r13 = new sun.security.pkcs.ParsingException     // Catch:{ all -> 0x0081 }
            java.lang.String r0 = r12.getMessage()     // Catch:{ all -> 0x0081 }
            r13.<init>(r0)     // Catch:{ all -> 0x0081 }
            r13.initCause(r12)     // Catch:{ all -> 0x0081 }
            throw r13     // Catch:{ all -> 0x0081 }
        L_0x009f:
            if (r1 == 0) goto L_0x00a4
            r1.close()
        L_0x00a4:
            throw r12
        L_0x00a5:
            r13.getSet(r3)
            sun.security.util.DerValue[] r13 = r13.getSet(r0)
            int r1 = r13.length
            sun.security.pkcs.SignerInfo[] r2 = new sun.security.pkcs.SignerInfo[r1]
            r12.signerInfos = r2
        L_0x00b1:
            if (r3 >= r1) goto L_0x00c5
            r2 = r13[r3]
            sun.security.util.DerInputStream r2 = r2.toDerInputStream()
            sun.security.pkcs.SignerInfo[] r4 = r12.signerInfos
            sun.security.pkcs.SignerInfo r5 = new sun.security.pkcs.SignerInfo
            r5.<init>(r2, r0)
            r4[r3] = r5
            int r3 = r3 + 1
            goto L_0x00b1
        L_0x00c5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.pkcs.PKCS7.parseOldSignedData(sun.security.util.DerValue):void");
    }

    public void encodeSignedData(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        encodeSignedData(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void encodeSignedData(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(this.version);
        derOutputStream2.putOrderedSetOf((byte) 49, this.digestAlgorithmIds);
        this.contentInfo.encode(derOutputStream2);
        X509Certificate[] x509CertificateArr = this.certificates;
        if (!(x509CertificateArr == null || x509CertificateArr.length == 0)) {
            X509CertImpl[] x509CertImplArr = new X509CertImpl[x509CertificateArr.length];
            int i = 0;
            while (true) {
                X509Certificate[] x509CertificateArr2 = this.certificates;
                if (i >= x509CertificateArr2.length) {
                    break;
                }
                X509Certificate x509Certificate = x509CertificateArr2[i];
                if (x509Certificate instanceof X509CertImpl) {
                    x509CertImplArr[i] = (X509CertImpl) x509Certificate;
                } else {
                    try {
                        x509CertImplArr[i] = new X509CertImpl(x509Certificate.getEncoded());
                    } catch (CertificateException e) {
                        throw new IOException((Throwable) e);
                    }
                }
                i++;
            }
            derOutputStream2.putOrderedSetOf((byte) -96, x509CertImplArr);
        }
        X509CRL[] x509crlArr = this.crls;
        if (!(x509crlArr == null || x509crlArr.length == 0)) {
            HashSet hashSet = new HashSet(this.crls.length);
            for (X509CRL x509crl : this.crls) {
                if (x509crl instanceof X509CRLImpl) {
                    hashSet.add((X509CRLImpl) x509crl);
                } else {
                    try {
                        hashSet.add(new X509CRLImpl(x509crl.getEncoded()));
                    } catch (CRLException e2) {
                        throw new IOException((Throwable) e2);
                    }
                }
            }
            derOutputStream2.putOrderedSetOf((byte) -95, (DerEncoder[]) hashSet.toArray(new X509CRLImpl[hashSet.size()]));
        }
        derOutputStream2.putOrderedSetOf((byte) 49, this.signerInfos);
        new ContentInfo(ContentInfo.SIGNED_DATA_OID, new DerValue((byte) 48, derOutputStream2.toByteArray())).encode(derOutputStream);
    }

    public SignerInfo verify(SignerInfo signerInfo, byte[] bArr) throws NoSuchAlgorithmException, SignatureException {
        return signerInfo.verify(this, bArr);
    }

    public SignerInfo verify(SignerInfo signerInfo, InputStream inputStream) throws NoSuchAlgorithmException, SignatureException, IOException {
        return signerInfo.verify(this, inputStream);
    }

    public SignerInfo[] verify(byte[] bArr) throws NoSuchAlgorithmException, SignatureException {
        Vector vector = new Vector();
        int i = 0;
        while (true) {
            SignerInfo[] signerInfoArr = this.signerInfos;
            if (i >= signerInfoArr.length) {
                break;
            }
            SignerInfo verify = verify(signerInfoArr[i], bArr);
            if (verify != null) {
                vector.addElement(verify);
            }
            i++;
        }
        if (vector.isEmpty()) {
            return null;
        }
        SignerInfo[] signerInfoArr2 = new SignerInfo[vector.size()];
        vector.copyInto(signerInfoArr2);
        return signerInfoArr2;
    }

    public SignerInfo[] verify() throws NoSuchAlgorithmException, SignatureException {
        return verify((byte[]) null);
    }

    public BigInteger getVersion() {
        return this.version;
    }

    public AlgorithmId[] getDigestAlgorithmIds() {
        return this.digestAlgorithmIds;
    }

    public ContentInfo getContentInfo() {
        return this.contentInfo;
    }

    public X509Certificate[] getCertificates() {
        X509Certificate[] x509CertificateArr = this.certificates;
        if (x509CertificateArr != null) {
            return (X509Certificate[]) x509CertificateArr.clone();
        }
        return null;
    }

    public X509CRL[] getCRLs() {
        X509CRL[] x509crlArr = this.crls;
        if (x509crlArr != null) {
            return (X509CRL[]) x509crlArr.clone();
        }
        return null;
    }

    public SignerInfo[] getSignerInfos() {
        return this.signerInfos;
    }

    public X509Certificate getCertificate(BigInteger bigInteger, X500Name x500Name) {
        if (this.certificates == null) {
            return null;
        }
        if (this.certIssuerNames == null) {
            populateCertIssuerNames();
        }
        int i = 0;
        while (true) {
            X509Certificate[] x509CertificateArr = this.certificates;
            if (i >= x509CertificateArr.length) {
                return null;
            }
            X509Certificate x509Certificate = x509CertificateArr[i];
            if (bigInteger.equals(x509Certificate.getSerialNumber()) && x500Name.equals(this.certIssuerNames[i])) {
                return x509Certificate;
            }
            i++;
        }
    }

    private void populateCertIssuerNames() {
        X509Certificate[] x509CertificateArr = this.certificates;
        if (x509CertificateArr != null) {
            this.certIssuerNames = new Principal[x509CertificateArr.length];
            int i = 0;
            while (true) {
                X509Certificate[] x509CertificateArr2 = this.certificates;
                if (i < x509CertificateArr2.length) {
                    X509Certificate x509Certificate = x509CertificateArr2[i];
                    Principal issuerDN = x509Certificate.getIssuerDN();
                    if (!(issuerDN instanceof X500Name)) {
                        try {
                            issuerDN = (Principal) new X509CertInfo(x509Certificate.getTBSCertificate()).get("issuer.dname");
                        } catch (Exception unused) {
                        }
                    }
                    this.certIssuerNames[i] = issuerDN;
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public String toString() {
        String str;
        String str2 = "" + this.contentInfo + "\n";
        if (this.version != null) {
            str2 = str2 + "PKCS7 :: version: " + Debug.toHexString(this.version) + "\n";
        }
        if (this.digestAlgorithmIds != null) {
            str = str + "PKCS7 :: digest AlgorithmIds: \n";
            for (int i = 0; i < this.digestAlgorithmIds.length; i++) {
                str = str + "\t" + this.digestAlgorithmIds[i] + "\n";
            }
        }
        if (this.certificates != null) {
            String str3 = str + "PKCS7 :: certificates: \n";
            for (int i2 = 0; i2 < this.certificates.length; i2++) {
                str3 = str + "\t" + i2 + ".   " + this.certificates[i2] + "\n";
            }
        }
        if (this.crls != null) {
            String str4 = str + "PKCS7 :: crls: \n";
            for (int i3 = 0; i3 < this.crls.length; i3++) {
                str4 = str + "\t" + i3 + ".   " + this.crls[i3] + "\n";
            }
        }
        if (this.signerInfos != null) {
            String str5 = str + "PKCS7 :: signer infos: \n";
            for (int i4 = 0; i4 < this.signerInfos.length; i4++) {
                str5 = str + "\t" + i4 + ".  " + this.signerInfos[i4] + "\n";
            }
        }
        return str;
    }

    public boolean isOldStyle() {
        return this.oldStyle;
    }

    private static class VerbatimX509Certificate extends WrappedX509Certificate {
        private byte[] encodedVerbatim;

        public VerbatimX509Certificate(X509Certificate x509Certificate, byte[] bArr) {
            super(x509Certificate);
            this.encodedVerbatim = bArr;
        }

        public byte[] getEncoded() throws CertificateEncodingException {
            return this.encodedVerbatim;
        }
    }

    private static class WrappedX509Certificate extends X509Certificate {
        private final X509Certificate wrapped;

        public WrappedX509Certificate(X509Certificate x509Certificate) {
            this.wrapped = x509Certificate;
        }

        public Set<String> getCriticalExtensionOIDs() {
            return this.wrapped.getCriticalExtensionOIDs();
        }

        public byte[] getExtensionValue(String str) {
            return this.wrapped.getExtensionValue(str);
        }

        public Set<String> getNonCriticalExtensionOIDs() {
            return this.wrapped.getNonCriticalExtensionOIDs();
        }

        public boolean hasUnsupportedCriticalExtension() {
            return this.wrapped.hasUnsupportedCriticalExtension();
        }

        public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
            this.wrapped.checkValidity();
        }

        public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
            this.wrapped.checkValidity(date);
        }

        public int getVersion() {
            return this.wrapped.getVersion();
        }

        public BigInteger getSerialNumber() {
            return this.wrapped.getSerialNumber();
        }

        public Principal getIssuerDN() {
            return this.wrapped.getIssuerDN();
        }

        public Principal getSubjectDN() {
            return this.wrapped.getSubjectDN();
        }

        public Date getNotBefore() {
            return this.wrapped.getNotBefore();
        }

        public Date getNotAfter() {
            return this.wrapped.getNotAfter();
        }

        public byte[] getTBSCertificate() throws CertificateEncodingException {
            return this.wrapped.getTBSCertificate();
        }

        public byte[] getSignature() {
            return this.wrapped.getSignature();
        }

        public String getSigAlgName() {
            return this.wrapped.getSigAlgName();
        }

        public String getSigAlgOID() {
            return this.wrapped.getSigAlgOID();
        }

        public byte[] getSigAlgParams() {
            return this.wrapped.getSigAlgParams();
        }

        public boolean[] getIssuerUniqueID() {
            return this.wrapped.getIssuerUniqueID();
        }

        public boolean[] getSubjectUniqueID() {
            return this.wrapped.getSubjectUniqueID();
        }

        public boolean[] getKeyUsage() {
            return this.wrapped.getKeyUsage();
        }

        public int getBasicConstraints() {
            return this.wrapped.getBasicConstraints();
        }

        public byte[] getEncoded() throws CertificateEncodingException {
            return this.wrapped.getEncoded();
        }

        public void verify(PublicKey publicKey) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
            this.wrapped.verify(publicKey);
        }

        public void verify(PublicKey publicKey, String str) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
            this.wrapped.verify(publicKey, str);
        }

        public String toString() {
            return this.wrapped.toString();
        }

        public PublicKey getPublicKey() {
            return this.wrapped.getPublicKey();
        }

        public List<String> getExtendedKeyUsage() throws CertificateParsingException {
            return this.wrapped.getExtendedKeyUsage();
        }

        public Collection<List<?>> getIssuerAlternativeNames() throws CertificateParsingException {
            return this.wrapped.getIssuerAlternativeNames();
        }

        public X500Principal getIssuerX500Principal() {
            return this.wrapped.getIssuerX500Principal();
        }

        public Collection<List<?>> getSubjectAlternativeNames() throws CertificateParsingException {
            return this.wrapped.getSubjectAlternativeNames();
        }

        public X500Principal getSubjectX500Principal() {
            return this.wrapped.getSubjectX500Principal();
        }

        public void verify(PublicKey publicKey, Provider provider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
            this.wrapped.verify(publicKey, provider);
        }
    }
}
