package sun.security.x509;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.math.BigInteger;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import javax.security.auth.x500.X500Principal;
import sun.misc.HexDumpEncoder;
import sun.security.provider.X509Factory;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class X509CertImpl extends X509Certificate implements DerEncoder {
    public static final String ALG_ID = "algorithm";
    private static final String AUTH_INFO_ACCESS_OID = "1.3.6.1.5.5.7.1.1";
    private static final String BASIC_CONSTRAINT_OID = "2.5.29.19";
    private static final String DOT = ".";
    private static final String EXTENDED_KEY_USAGE_OID = "2.5.29.37";
    public static final String INFO = "info";
    private static final String ISSUER_ALT_NAME_OID = "2.5.29.18";
    public static final String ISSUER_DN = "x509.info.issuer.dname";
    private static final String KEY_USAGE_OID = "2.5.29.15";
    public static final String NAME = "x509";
    private static final int NUM_STANDARD_KEY_USAGE = 9;
    public static final String PUBLIC_KEY = "x509.info.key.value";
    public static final String SERIAL_ID = "x509.info.serialNumber.number";
    public static final String SIG = "x509.signature";
    public static final String SIGNATURE = "signature";
    public static final String SIGNED_CERT = "signed_cert";
    public static final String SIG_ALG = "x509.algorithm";
    private static final String SUBJECT_ALT_NAME_OID = "2.5.29.17";
    public static final String SUBJECT_DN = "x509.info.subject.dname";
    public static final String VERSION = "x509.info.version.number";
    private static final long serialVersionUID = -3457612960190864406L;
    protected AlgorithmId algId = null;
    private Set<AccessDescription> authInfoAccess;
    private List<String> extKeyUsage;
    private ConcurrentHashMap<String, String> fingerprints = new ConcurrentHashMap<>(2);
    protected X509CertInfo info = null;
    private Collection<List<?>> issuerAlternativeNames;
    private boolean readOnly = false;
    protected byte[] signature = null;
    private byte[] signedCert = null;
    private Collection<List<?>> subjectAlternativeNames;
    private boolean verificationResult;
    private String verifiedProvider;
    private PublicKey verifiedPublicKey;

    public String getName() {
        return NAME;
    }

    public X509CertImpl() {
    }

    public X509CertImpl(byte[] bArr) throws CertificateException {
        try {
            parse(new DerValue(bArr));
        } catch (IOException e) {
            this.signedCert = null;
            throw new CertificateException("Unable to initialize, " + e, e);
        }
    }

    public X509CertImpl(X509CertInfo x509CertInfo) {
        this.info = x509CertInfo;
    }

    public X509CertImpl(DerValue derValue) throws CertificateException {
        try {
            parse(derValue);
        } catch (IOException e) {
            this.signedCert = null;
            throw new CertificateException("Unable to initialize, " + e, e);
        }
    }

    public X509CertImpl(DerValue derValue, byte[] bArr) throws CertificateException {
        try {
            parse(derValue, bArr);
        } catch (IOException e) {
            this.signedCert = null;
            throw new CertificateException("Unable to initialize, " + e, e);
        }
    }

    public void encode(OutputStream outputStream) throws CertificateEncodingException {
        byte[] bArr = this.signedCert;
        if (bArr != null) {
            try {
                outputStream.write((byte[]) bArr.clone());
            } catch (IOException e) {
                throw new CertificateEncodingException(e.toString());
            }
        } else {
            throw new CertificateEncodingException("Null certificate to encode");
        }
    }

    public void derEncode(OutputStream outputStream) throws IOException {
        byte[] bArr = this.signedCert;
        if (bArr != null) {
            outputStream.write((byte[]) bArr.clone());
            return;
        }
        throw new IOException("Null certificate to encode");
    }

    public byte[] getEncoded() throws CertificateEncodingException {
        return (byte[]) getEncodedInternal().clone();
    }

    public byte[] getEncodedInternal() throws CertificateEncodingException {
        byte[] bArr = this.signedCert;
        if (bArr != null) {
            return bArr;
        }
        throw new CertificateEncodingException("Null certificate to encode");
    }

    public void verify(PublicKey publicKey) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        verify(publicKey, "");
    }

    public synchronized void verify(PublicKey publicKey, String str) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        Signature signature2;
        if (str == null) {
            str = "";
        }
        PublicKey publicKey2 = this.verifiedPublicKey;
        if (publicKey2 == null || !publicKey2.equals(publicKey) || !str.equals(this.verifiedProvider)) {
            if (this.signedCert != null) {
                if (str.length() == 0) {
                    signature2 = Signature.getInstance(this.algId.getName());
                } else {
                    signature2 = Signature.getInstance(this.algId.getName(), str);
                }
                signature2.initVerify(publicKey);
                byte[] encodedInfo = this.info.getEncodedInfo();
                signature2.update(encodedInfo, 0, encodedInfo.length);
                boolean verify = signature2.verify(this.signature);
                this.verificationResult = verify;
                this.verifiedPublicKey = publicKey;
                this.verifiedProvider = str;
                if (!verify) {
                    throw new SignatureException("Signature does not match.");
                }
                return;
            }
            throw new CertificateEncodingException("Uninitialized certificate");
        } else if (!this.verificationResult) {
            throw new SignatureException("Signature does not match.");
        }
    }

    public synchronized void verify(PublicKey publicKey, Provider provider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature2;
        if (this.signedCert != null) {
            if (provider == null) {
                signature2 = Signature.getInstance(this.algId.getName());
            } else {
                signature2 = Signature.getInstance(this.algId.getName(), provider);
            }
            signature2.initVerify(publicKey);
            byte[] encodedInfo = this.info.getEncodedInfo();
            signature2.update(encodedInfo, 0, encodedInfo.length);
            boolean verify = signature2.verify(this.signature);
            this.verificationResult = verify;
            this.verifiedPublicKey = publicKey;
            if (!verify) {
                throw new SignatureException("Signature does not match.");
            }
        } else {
            throw new CertificateEncodingException("Uninitialized certificate");
        }
    }

    public static void verify(X509Certificate x509Certificate, PublicKey publicKey, Provider provider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        x509Certificate.verify(publicKey, provider);
    }

    public void sign(PrivateKey privateKey, String str) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        sign(privateKey, str, (String) null);
    }

    public void sign(PrivateKey privateKey, String str, String str2) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        Signature signature2;
        try {
            if (!this.readOnly) {
                if (str2 != null) {
                    if (str2.length() != 0) {
                        signature2 = Signature.getInstance(str, str2);
                        signature2.initSign(privateKey);
                        this.algId = AlgorithmId.get(signature2.getAlgorithm());
                        DerOutputStream derOutputStream = new DerOutputStream();
                        DerOutputStream derOutputStream2 = new DerOutputStream();
                        this.info.encode(derOutputStream2);
                        byte[] byteArray = derOutputStream2.toByteArray();
                        this.algId.encode(derOutputStream2);
                        signature2.update(byteArray, 0, byteArray.length);
                        byte[] sign = signature2.sign();
                        this.signature = sign;
                        derOutputStream2.putBitString(sign);
                        derOutputStream.write((byte) 48, derOutputStream2);
                        this.signedCert = derOutputStream.toByteArray();
                        this.readOnly = true;
                        return;
                    }
                }
                signature2 = Signature.getInstance(str);
                signature2.initSign(privateKey);
                this.algId = AlgorithmId.get(signature2.getAlgorithm());
                DerOutputStream derOutputStream3 = new DerOutputStream();
                DerOutputStream derOutputStream22 = new DerOutputStream();
                this.info.encode(derOutputStream22);
                byte[] byteArray2 = derOutputStream22.toByteArray();
                this.algId.encode(derOutputStream22);
                signature2.update(byteArray2, 0, byteArray2.length);
                byte[] sign2 = signature2.sign();
                this.signature = sign2;
                derOutputStream22.putBitString(sign2);
                derOutputStream3.write((byte) 48, derOutputStream22);
                this.signedCert = derOutputStream3.toByteArray();
                this.readOnly = true;
                return;
            }
            throw new CertificateEncodingException("cannot over-write existing certificate");
        } catch (IOException e) {
            throw new CertificateEncodingException(e.toString());
        }
    }

    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
        checkValidity(new Date());
    }

    public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
        try {
            CertificateValidity certificateValidity = (CertificateValidity) this.info.get("validity");
            if (certificateValidity != null) {
                certificateValidity.valid(date);
                return;
            }
            throw new CertificateNotYetValidException("Null validity period");
        } catch (Exception unused) {
            throw new CertificateNotYetValidException("Incorrect validity period");
        }
    }

    public Object get(String str) throws CertificateParsingException {
        X509AttributeName x509AttributeName = new X509AttributeName(str);
        String prefix = x509AttributeName.getPrefix();
        if (prefix.equalsIgnoreCase(NAME)) {
            X509AttributeName x509AttributeName2 = new X509AttributeName(x509AttributeName.getSuffix());
            String prefix2 = x509AttributeName2.getPrefix();
            if (prefix2.equalsIgnoreCase("info")) {
                if (this.info == null) {
                    return null;
                }
                if (x509AttributeName2.getSuffix() == null) {
                    return this.info;
                }
                try {
                    return this.info.get(x509AttributeName2.getSuffix());
                } catch (IOException e) {
                    throw new CertificateParsingException(e.toString());
                } catch (CertificateException e2) {
                    throw new CertificateParsingException(e2.toString());
                }
            } else if (prefix2.equalsIgnoreCase("algorithm")) {
                return this.algId;
            } else {
                if (prefix2.equalsIgnoreCase(SIGNATURE)) {
                    byte[] bArr = this.signature;
                    if (bArr != null) {
                        return bArr.clone();
                    }
                    return null;
                } else if (prefix2.equalsIgnoreCase(SIGNED_CERT)) {
                    byte[] bArr2 = this.signedCert;
                    if (bArr2 != null) {
                        return bArr2.clone();
                    }
                    return null;
                } else {
                    throw new CertificateParsingException("Attribute name not recognized or get() not allowed for the same: " + prefix2);
                }
            }
        } else {
            throw new CertificateParsingException("Invalid root of attribute name, expected [x509], received [" + prefix + NavigationBarInflaterView.SIZE_MOD_END);
        }
    }

    public void set(String str, Object obj) throws CertificateException, IOException {
        if (!this.readOnly) {
            X509AttributeName x509AttributeName = new X509AttributeName(str);
            String prefix = x509AttributeName.getPrefix();
            if (prefix.equalsIgnoreCase(NAME)) {
                X509AttributeName x509AttributeName2 = new X509AttributeName(x509AttributeName.getSuffix());
                String prefix2 = x509AttributeName2.getPrefix();
                if (!prefix2.equalsIgnoreCase("info")) {
                    throw new CertificateException("Attribute name not recognized or set() not allowed for the same: " + prefix2);
                } else if (x509AttributeName2.getSuffix() != null) {
                    this.info.set(x509AttributeName2.getSuffix(), obj);
                    this.signedCert = null;
                } else if (obj instanceof X509CertInfo) {
                    this.info = (X509CertInfo) obj;
                    this.signedCert = null;
                } else {
                    throw new CertificateException("Attribute value should be of type X509CertInfo.");
                }
            } else {
                throw new CertificateException("Invalid root of attribute name, expected [x509], received " + prefix);
            }
        } else {
            throw new CertificateException("cannot over-write existing certificate");
        }
    }

    public void delete(String str) throws CertificateException, IOException {
        if (!this.readOnly) {
            X509AttributeName x509AttributeName = new X509AttributeName(str);
            String prefix = x509AttributeName.getPrefix();
            if (prefix.equalsIgnoreCase(NAME)) {
                X509AttributeName x509AttributeName2 = new X509AttributeName(x509AttributeName.getSuffix());
                String prefix2 = x509AttributeName2.getPrefix();
                if (prefix2.equalsIgnoreCase("info")) {
                    if (x509AttributeName2.getSuffix() != null) {
                        this.info = null;
                    } else {
                        this.info.delete(x509AttributeName2.getSuffix());
                    }
                } else if (prefix2.equalsIgnoreCase("algorithm")) {
                    this.algId = null;
                } else if (prefix2.equalsIgnoreCase(SIGNATURE)) {
                    this.signature = null;
                } else if (prefix2.equalsIgnoreCase(SIGNED_CERT)) {
                    this.signedCert = null;
                } else {
                    throw new CertificateException("Attribute name not recognized or delete() not allowed for the same: " + prefix2);
                }
            } else {
                throw new CertificateException("Invalid root of attribute name, expected [x509], received " + prefix);
            }
        } else {
            throw new CertificateException("cannot over-write existing certificate");
        }
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(X509CertInfo.IDENT);
        attributeNameEnumeration.addElement(SIG_ALG);
        attributeNameEnumeration.addElement(SIG);
        attributeNameEnumeration.addElement("x509.signed_cert");
        return attributeNameEnumeration.elements();
    }

    public String toString() {
        if (this.info == null || this.algId == null || this.signature == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder("[\n");
        sb.append(this.info.toString() + "\n");
        sb.append("  Algorithm: [" + this.algId.toString() + "]\n");
        HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
        sb.append("  Signature:\n" + hexDumpEncoder.encodeBuffer(this.signature));
        sb.append("\n]");
        return sb.toString();
    }

    public PublicKey getPublicKey() {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo == null) {
            return null;
        }
        try {
            return (PublicKey) x509CertInfo.get("key.value");
        } catch (Exception unused) {
            return null;
        }
    }

    public int getVersion() {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo == null) {
            return -1;
        }
        try {
            return ((Integer) x509CertInfo.get("version.number")).intValue() + 1;
        } catch (Exception unused) {
            return -1;
        }
    }

    public BigInteger getSerialNumber() {
        SerialNumber serialNumberObject = getSerialNumberObject();
        if (serialNumberObject != null) {
            return serialNumberObject.getNumber();
        }
        return null;
    }

    public SerialNumber getSerialNumberObject() {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo == null) {
            return null;
        }
        try {
            return (SerialNumber) x509CertInfo.get("serialNumber.number");
        } catch (Exception unused) {
            return null;
        }
    }

    public Principal getSubjectDN() {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo == null) {
            return null;
        }
        try {
            return (Principal) x509CertInfo.get("subject.dname");
        } catch (Exception unused) {
            return null;
        }
    }

    public X500Principal getSubjectX500Principal() {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo == null) {
            return null;
        }
        try {
            return (X500Principal) x509CertInfo.get("subject.x500principal");
        } catch (Exception unused) {
            return null;
        }
    }

    public Principal getIssuerDN() {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo == null) {
            return null;
        }
        try {
            return (Principal) x509CertInfo.get("issuer.dname");
        } catch (Exception unused) {
            return null;
        }
    }

    public X500Principal getIssuerX500Principal() {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo == null) {
            return null;
        }
        try {
            return (X500Principal) x509CertInfo.get("issuer.x500principal");
        } catch (Exception unused) {
            return null;
        }
    }

    public Date getNotBefore() {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo == null) {
            return null;
        }
        try {
            return (Date) x509CertInfo.get("validity.notBefore");
        } catch (Exception unused) {
            return null;
        }
    }

    public Date getNotAfter() {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo == null) {
            return null;
        }
        try {
            return (Date) x509CertInfo.get("validity.notAfter");
        } catch (Exception unused) {
            return null;
        }
    }

    public byte[] getTBSCertificate() throws CertificateEncodingException {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo != null) {
            return x509CertInfo.getEncodedInfo();
        }
        throw new CertificateEncodingException("Uninitialized certificate");
    }

    public byte[] getSignature() {
        byte[] bArr = this.signature;
        if (bArr == null) {
            return null;
        }
        return (byte[]) bArr.clone();
    }

    public String getSigAlgName() {
        AlgorithmId algorithmId = this.algId;
        if (algorithmId == null) {
            return null;
        }
        return algorithmId.getName();
    }

    public String getSigAlgOID() {
        AlgorithmId algorithmId = this.algId;
        if (algorithmId == null) {
            return null;
        }
        return algorithmId.getOID().toString();
    }

    public byte[] getSigAlgParams() {
        AlgorithmId algorithmId = this.algId;
        if (algorithmId == null) {
            return null;
        }
        try {
            return algorithmId.getEncodedParams();
        } catch (IOException unused) {
            return null;
        }
    }

    public boolean[] getIssuerUniqueID() {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo == null) {
            return null;
        }
        try {
            UniqueIdentity uniqueIdentity = (UniqueIdentity) x509CertInfo.get(X509CertInfo.ISSUER_ID);
            if (uniqueIdentity == null) {
                return null;
            }
            return uniqueIdentity.getId();
        } catch (Exception unused) {
            return null;
        }
    }

    public boolean[] getSubjectUniqueID() {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo == null) {
            return null;
        }
        try {
            UniqueIdentity uniqueIdentity = (UniqueIdentity) x509CertInfo.get(X509CertInfo.SUBJECT_ID);
            if (uniqueIdentity == null) {
                return null;
            }
            return uniqueIdentity.getId();
        } catch (Exception unused) {
            return null;
        }
    }

    public KeyIdentifier getAuthKeyId() {
        AuthorityKeyIdentifierExtension authorityKeyIdentifierExtension = getAuthorityKeyIdentifierExtension();
        if (authorityKeyIdentifierExtension == null) {
            return null;
        }
        try {
            return (KeyIdentifier) authorityKeyIdentifierExtension.get("key_id");
        } catch (IOException unused) {
            return null;
        }
    }

    public KeyIdentifier getSubjectKeyId() {
        SubjectKeyIdentifierExtension subjectKeyIdentifierExtension = getSubjectKeyIdentifierExtension();
        if (subjectKeyIdentifierExtension == null) {
            return null;
        }
        try {
            return subjectKeyIdentifierExtension.get("key_id");
        } catch (IOException unused) {
            return null;
        }
    }

    public AuthorityKeyIdentifierExtension getAuthorityKeyIdentifierExtension() {
        return (AuthorityKeyIdentifierExtension) getExtension(PKIXExtensions.AuthorityKey_Id);
    }

    public BasicConstraintsExtension getBasicConstraintsExtension() {
        return (BasicConstraintsExtension) getExtension(PKIXExtensions.BasicConstraints_Id);
    }

    public CertificatePoliciesExtension getCertificatePoliciesExtension() {
        return (CertificatePoliciesExtension) getExtension(PKIXExtensions.CertificatePolicies_Id);
    }

    public ExtendedKeyUsageExtension getExtendedKeyUsageExtension() {
        return (ExtendedKeyUsageExtension) getExtension(PKIXExtensions.ExtendedKeyUsage_Id);
    }

    public IssuerAlternativeNameExtension getIssuerAlternativeNameExtension() {
        return (IssuerAlternativeNameExtension) getExtension(PKIXExtensions.IssuerAlternativeName_Id);
    }

    public NameConstraintsExtension getNameConstraintsExtension() {
        return (NameConstraintsExtension) getExtension(PKIXExtensions.NameConstraints_Id);
    }

    public PolicyConstraintsExtension getPolicyConstraintsExtension() {
        return (PolicyConstraintsExtension) getExtension(PKIXExtensions.PolicyConstraints_Id);
    }

    public PolicyMappingsExtension getPolicyMappingsExtension() {
        return (PolicyMappingsExtension) getExtension(PKIXExtensions.PolicyMappings_Id);
    }

    public PrivateKeyUsageExtension getPrivateKeyUsageExtension() {
        return (PrivateKeyUsageExtension) getExtension(PKIXExtensions.PrivateKeyUsage_Id);
    }

    public SubjectAlternativeNameExtension getSubjectAlternativeNameExtension() {
        return (SubjectAlternativeNameExtension) getExtension(PKIXExtensions.SubjectAlternativeName_Id);
    }

    public SubjectKeyIdentifierExtension getSubjectKeyIdentifierExtension() {
        return (SubjectKeyIdentifierExtension) getExtension(PKIXExtensions.SubjectKey_Id);
    }

    public CRLDistributionPointsExtension getCRLDistributionPointsExtension() {
        return (CRLDistributionPointsExtension) getExtension(PKIXExtensions.CRLDistributionPoints_Id);
    }

    public boolean hasUnsupportedCriticalExtension() {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo == null) {
            return false;
        }
        try {
            CertificateExtensions certificateExtensions = (CertificateExtensions) x509CertInfo.get("extensions");
            if (certificateExtensions == null) {
                return false;
            }
            return certificateExtensions.hasUnsupportedCriticalExtension();
        } catch (Exception unused) {
            return false;
        }
    }

    public Set<String> getCriticalExtensionOIDs() {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo == null) {
            return null;
        }
        try {
            CertificateExtensions certificateExtensions = (CertificateExtensions) x509CertInfo.get("extensions");
            if (certificateExtensions == null) {
                return null;
            }
            TreeSet treeSet = new TreeSet();
            for (Extension next : certificateExtensions.getAllExtensions()) {
                if (next.isCritical()) {
                    treeSet.add(next.getExtensionId().toString());
                }
            }
            return treeSet;
        } catch (Exception unused) {
            return null;
        }
    }

    public Set<String> getNonCriticalExtensionOIDs() {
        X509CertInfo x509CertInfo = this.info;
        if (x509CertInfo == null) {
            return null;
        }
        try {
            CertificateExtensions certificateExtensions = (CertificateExtensions) x509CertInfo.get("extensions");
            if (certificateExtensions == null) {
                return null;
            }
            TreeSet treeSet = new TreeSet();
            for (Extension next : certificateExtensions.getAllExtensions()) {
                if (!next.isCritical()) {
                    treeSet.add(next.getExtensionId().toString());
                }
            }
            treeSet.addAll(certificateExtensions.getUnparseableExtensions().keySet());
            return treeSet;
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public sun.security.x509.Extension getExtension(sun.security.util.ObjectIdentifier r4) {
        /*
            r3 = this;
            sun.security.x509.X509CertInfo r3 = r3.info
            r0 = 0
            if (r3 != 0) goto L_0x0006
            return r0
        L_0x0006:
            java.lang.String r1 = "extensions"
            java.lang.Object r3 = r3.get(r1)     // Catch:{ IOException | CertificateException -> 0x003b }
            sun.security.x509.CertificateExtensions r3 = (sun.security.x509.CertificateExtensions) r3     // Catch:{ IOException | CertificateException -> 0x003b }
            if (r3 != 0) goto L_0x0011
            return r0
        L_0x0011:
            java.lang.String r1 = r4.toString()     // Catch:{  }
            sun.security.x509.Extension r1 = r3.getExtension(r1)     // Catch:{  }
            if (r1 == 0) goto L_0x001c
            return r1
        L_0x001c:
            java.util.Collection r3 = r3.getAllExtensions()     // Catch:{  }
            java.util.Iterator r3 = r3.iterator()     // Catch:{  }
        L_0x0024:
            boolean r1 = r3.hasNext()     // Catch:{  }
            if (r1 == 0) goto L_0x003b
            java.lang.Object r1 = r3.next()     // Catch:{  }
            sun.security.x509.Extension r1 = (sun.security.x509.Extension) r1     // Catch:{  }
            sun.security.util.ObjectIdentifier r2 = r1.getExtensionId()     // Catch:{  }
            boolean r2 = r2.equals((java.lang.Object) r4)     // Catch:{  }
            if (r2 == 0) goto L_0x0024
            return r1
        L_0x003b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.x509.X509CertImpl.getExtension(sun.security.util.ObjectIdentifier):sun.security.x509.Extension");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public sun.security.x509.Extension getUnparseableExtension(sun.security.util.ObjectIdentifier r3) {
        /*
            r2 = this;
            sun.security.x509.X509CertInfo r2 = r2.info
            r0 = 0
            if (r2 != 0) goto L_0x0006
            return r0
        L_0x0006:
            java.lang.String r1 = "extensions"
            java.lang.Object r2 = r2.get(r1)     // Catch:{ IOException | CertificateException -> 0x0020 }
            sun.security.x509.CertificateExtensions r2 = (sun.security.x509.CertificateExtensions) r2     // Catch:{ IOException | CertificateException -> 0x0020 }
            if (r2 != 0) goto L_0x0011
            return r0
        L_0x0011:
            java.util.Map r2 = r2.getUnparseableExtensions()     // Catch:{  }
            java.lang.String r3 = r3.toString()     // Catch:{  }
            java.lang.Object r2 = r2.get(r3)     // Catch:{  }
            sun.security.x509.Extension r2 = (sun.security.x509.Extension) r2     // Catch:{  }
            return r2
        L_0x0020:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.x509.X509CertImpl.getUnparseableExtension(sun.security.util.ObjectIdentifier):sun.security.x509.Extension");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v6, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v6, resolved type: sun.security.x509.Extension} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0059 A[Catch:{ Exception -> 0x0067 }, RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x005a A[Catch:{ Exception -> 0x0067 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] getExtensionValue(java.lang.String r6) {
        /*
            r5 = this;
            r0 = 0
            sun.security.util.ObjectIdentifier r1 = new sun.security.util.ObjectIdentifier     // Catch:{ Exception -> 0x0067 }
            r1.<init>((java.lang.String) r6)     // Catch:{ Exception -> 0x0067 }
            java.lang.String r2 = sun.security.x509.OIDMap.getName(r1)     // Catch:{ Exception -> 0x0067 }
            sun.security.x509.X509CertInfo r3 = r5.info     // Catch:{ Exception -> 0x0067 }
            java.lang.String r4 = "extensions"
            java.lang.Object r3 = r3.get(r4)     // Catch:{ Exception -> 0x0067 }
            sun.security.x509.CertificateExtensions r3 = (sun.security.x509.CertificateExtensions) r3     // Catch:{ Exception -> 0x0067 }
            if (r2 != 0) goto L_0x0038
            if (r3 != 0) goto L_0x0019
            return r0
        L_0x0019:
            java.util.Collection r5 = r3.getAllExtensions()     // Catch:{ Exception -> 0x0067 }
            java.util.Iterator r5 = r5.iterator()     // Catch:{ Exception -> 0x0067 }
        L_0x0021:
            boolean r2 = r5.hasNext()     // Catch:{ Exception -> 0x0067 }
            if (r2 == 0) goto L_0x0040
            java.lang.Object r2 = r5.next()     // Catch:{ Exception -> 0x0067 }
            sun.security.x509.Extension r2 = (sun.security.x509.Extension) r2     // Catch:{ Exception -> 0x0067 }
            sun.security.util.ObjectIdentifier r4 = r2.getExtensionId()     // Catch:{ Exception -> 0x0067 }
            boolean r4 = r4.equals((java.lang.Object) r1)     // Catch:{ Exception -> 0x0067 }
            if (r4 == 0) goto L_0x0021
            goto L_0x0041
        L_0x0038:
            java.lang.Object r5 = r5.get(r2)     // Catch:{ CertificateException -> 0x0040 }
            r2 = r5
            sun.security.x509.Extension r2 = (sun.security.x509.Extension) r2     // Catch:{ CertificateException -> 0x0040 }
            goto L_0x0041
        L_0x0040:
            r2 = r0
        L_0x0041:
            if (r2 != 0) goto L_0x0053
            if (r3 == 0) goto L_0x0050
            java.util.Map r5 = r3.getUnparseableExtensions()     // Catch:{ Exception -> 0x0067 }
            java.lang.Object r5 = r5.get(r6)     // Catch:{ Exception -> 0x0067 }
            sun.security.x509.Extension r5 = (sun.security.x509.Extension) r5     // Catch:{ Exception -> 0x0067 }
            r2 = r5
        L_0x0050:
            if (r2 != 0) goto L_0x0053
            return r0
        L_0x0053:
            byte[] r5 = r2.getExtensionValue()     // Catch:{ Exception -> 0x0067 }
            if (r5 != 0) goto L_0x005a
            return r0
        L_0x005a:
            sun.security.util.DerOutputStream r6 = new sun.security.util.DerOutputStream     // Catch:{ Exception -> 0x0067 }
            r6.<init>()     // Catch:{ Exception -> 0x0067 }
            r6.putOctetString(r5)     // Catch:{ Exception -> 0x0067 }
            byte[] r5 = r6.toByteArray()     // Catch:{ Exception -> 0x0067 }
            return r5
        L_0x0067:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.x509.X509CertImpl.getExtensionValue(java.lang.String):byte[]");
    }

    public boolean[] getKeyUsage() {
        KeyUsageExtension keyUsageExtension;
        try {
            String name = OIDMap.getName(PKIXExtensions.KeyUsage_Id);
            if (name == null || (keyUsageExtension = (KeyUsageExtension) get(name)) == null) {
                return null;
            }
            boolean[] bits = keyUsageExtension.getBits();
            if (bits.length >= 9) {
                return bits;
            }
            boolean[] zArr = new boolean[9];
            System.arraycopy((Object) bits, 0, (Object) zArr, 0, bits.length);
            return zArr;
        } catch (Exception unused) {
            return null;
        }
    }

    public synchronized List<String> getExtendedKeyUsage() throws CertificateParsingException {
        List<String> list;
        if (this.readOnly && (list = this.extKeyUsage) != null) {
            return list;
        }
        ExtendedKeyUsageExtension extendedKeyUsageExtension = getExtendedKeyUsageExtension();
        if (extendedKeyUsageExtension == null) {
            return null;
        }
        List<String> unmodifiableList = Collections.unmodifiableList(extendedKeyUsageExtension.getExtendedKeyUsage());
        this.extKeyUsage = unmodifiableList;
        return unmodifiableList;
    }

    public static List<String> getExtendedKeyUsage(X509Certificate x509Certificate) throws CertificateParsingException {
        try {
            byte[] extensionValue = x509Certificate.getExtensionValue(EXTENDED_KEY_USAGE_OID);
            if (extensionValue == null) {
                return null;
            }
            return Collections.unmodifiableList(new ExtendedKeyUsageExtension(Boolean.FALSE, (Object) new DerValue(extensionValue).getOctetString()).getExtendedKeyUsage());
        } catch (IOException e) {
            throw new CertificateParsingException((Throwable) e);
        }
    }

    public int getBasicConstraints() {
        BasicConstraintsExtension basicConstraintsExtension;
        try {
            String name = OIDMap.getName(PKIXExtensions.BasicConstraints_Id);
            if (!(name == null || (basicConstraintsExtension = (BasicConstraintsExtension) get(name)) == null || !((Boolean) basicConstraintsExtension.get(BasicConstraintsExtension.IS_CA)).booleanValue())) {
                return ((Integer) basicConstraintsExtension.get(BasicConstraintsExtension.PATH_LEN)).intValue();
            }
        } catch (Exception unused) {
        }
        return -1;
    }

    private static Collection<List<?>> makeAltNames(GeneralNames generalNames) {
        if (generalNames.isEmpty()) {
            return Collections.emptySet();
        }
        ArrayList arrayList = new ArrayList();
        for (GeneralName name : generalNames.names()) {
            GeneralNameInterface name2 = name.getName();
            ArrayList arrayList2 = new ArrayList(2);
            arrayList2.add(Integer.valueOf(name2.getType()));
            int type = name2.getType();
            if (type == 1) {
                arrayList2.add(((RFC822Name) name2).getName());
            } else if (type == 2) {
                arrayList2.add(((DNSName) name2).getName());
            } else if (type == 4) {
                arrayList2.add(((X500Name) name2).getRFC2253Name());
            } else if (type == 6) {
                arrayList2.add(((URIName) name2).getName());
            } else if (type == 7) {
                try {
                    arrayList2.add(((IPAddressName) name2).getName());
                } catch (IOException e) {
                    throw new RuntimeException("IPAddress cannot be parsed", e);
                }
            } else if (type != 8) {
                DerOutputStream derOutputStream = new DerOutputStream();
                try {
                    name2.encode(derOutputStream);
                    arrayList2.add(derOutputStream.toByteArray());
                } catch (IOException e2) {
                    throw new RuntimeException("name cannot be encoded", e2);
                }
            } else {
                arrayList2.add(((OIDName) name2).getOID().toString());
            }
            arrayList.add(Collections.unmodifiableList(arrayList2));
        }
        return Collections.unmodifiableCollection(arrayList);
    }

    private static Collection<List<?>> cloneAltNames(Collection<List<?>> collection) {
        boolean z = false;
        for (List<?> list : collection) {
            if (list.get(1) instanceof byte[]) {
                z = true;
            }
        }
        if (!z) {
            return collection;
        }
        ArrayList arrayList = new ArrayList();
        for (List next : collection) {
            Object obj = next.get(1);
            if (obj instanceof byte[]) {
                ArrayList arrayList2 = new ArrayList(next);
                arrayList2.set(1, ((byte[]) obj).clone());
                arrayList.add(Collections.unmodifiableList(arrayList2));
            } else {
                arrayList.add(next);
            }
        }
        return Collections.unmodifiableCollection(arrayList);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x002b, code lost:
        return java.util.Collections.emptySet();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0026 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.util.Collection<java.util.List<?>> getSubjectAlternativeNames() throws java.security.cert.CertificateParsingException {
        /*
            r2 = this;
            monitor-enter(r2)
            boolean r0 = r2.readOnly     // Catch:{ all -> 0x002c }
            if (r0 == 0) goto L_0x000f
            java.util.Collection<java.util.List<?>> r0 = r2.subjectAlternativeNames     // Catch:{ all -> 0x002c }
            if (r0 == 0) goto L_0x000f
            java.util.Collection r0 = cloneAltNames(r0)     // Catch:{ all -> 0x002c }
            monitor-exit(r2)
            return r0
        L_0x000f:
            sun.security.x509.SubjectAlternativeNameExtension r0 = r2.getSubjectAlternativeNameExtension()     // Catch:{ all -> 0x002c }
            if (r0 != 0) goto L_0x0018
            monitor-exit(r2)
            r2 = 0
            return r2
        L_0x0018:
            java.lang.String r1 = "subject_name"
            sun.security.x509.GeneralNames r0 = r0.get((java.lang.String) r1)     // Catch:{ IOException -> 0x0026 }
            java.util.Collection r0 = makeAltNames(r0)     // Catch:{ all -> 0x002c }
            r2.subjectAlternativeNames = r0     // Catch:{ all -> 0x002c }
            monitor-exit(r2)
            return r0
        L_0x0026:
            java.util.Set r0 = java.util.Collections.emptySet()     // Catch:{ all -> 0x002c }
            monitor-exit(r2)
            return r0
        L_0x002c:
            r0 = move-exception
            monitor-exit(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.x509.X509CertImpl.getSubjectAlternativeNames():java.util.Collection");
    }

    public static Collection<List<?>> getSubjectAlternativeNames(X509Certificate x509Certificate) throws CertificateParsingException {
        try {
            byte[] extensionValue = x509Certificate.getExtensionValue(SUBJECT_ALT_NAME_OID);
            if (extensionValue == null) {
                return null;
            }
            try {
                return makeAltNames(new SubjectAlternativeNameExtension(Boolean.FALSE, (Object) new DerValue(extensionValue).getOctetString()).get(SubjectAlternativeNameExtension.SUBJECT_NAME));
            } catch (IOException unused) {
                return Collections.emptySet();
            }
        } catch (IOException e) {
            throw new CertificateParsingException((Throwable) e);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x002b, code lost:
        return java.util.Collections.emptySet();
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0026 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.util.Collection<java.util.List<?>> getIssuerAlternativeNames() throws java.security.cert.CertificateParsingException {
        /*
            r2 = this;
            monitor-enter(r2)
            boolean r0 = r2.readOnly     // Catch:{ all -> 0x002c }
            if (r0 == 0) goto L_0x000f
            java.util.Collection<java.util.List<?>> r0 = r2.issuerAlternativeNames     // Catch:{ all -> 0x002c }
            if (r0 == 0) goto L_0x000f
            java.util.Collection r0 = cloneAltNames(r0)     // Catch:{ all -> 0x002c }
            monitor-exit(r2)
            return r0
        L_0x000f:
            sun.security.x509.IssuerAlternativeNameExtension r0 = r2.getIssuerAlternativeNameExtension()     // Catch:{ all -> 0x002c }
            if (r0 != 0) goto L_0x0018
            monitor-exit(r2)
            r2 = 0
            return r2
        L_0x0018:
            java.lang.String r1 = "issuer_name"
            sun.security.x509.GeneralNames r0 = r0.get((java.lang.String) r1)     // Catch:{ IOException -> 0x0026 }
            java.util.Collection r0 = makeAltNames(r0)     // Catch:{ all -> 0x002c }
            r2.issuerAlternativeNames = r0     // Catch:{ all -> 0x002c }
            monitor-exit(r2)
            return r0
        L_0x0026:
            java.util.Set r0 = java.util.Collections.emptySet()     // Catch:{ all -> 0x002c }
            monitor-exit(r2)
            return r0
        L_0x002c:
            r0 = move-exception
            monitor-exit(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.x509.X509CertImpl.getIssuerAlternativeNames():java.util.Collection");
    }

    public static Collection<List<?>> getIssuerAlternativeNames(X509Certificate x509Certificate) throws CertificateParsingException {
        try {
            byte[] extensionValue = x509Certificate.getExtensionValue(ISSUER_ALT_NAME_OID);
            if (extensionValue == null) {
                return null;
            }
            try {
                return makeAltNames(new IssuerAlternativeNameExtension(Boolean.FALSE, (Object) new DerValue(extensionValue).getOctetString()).get(IssuerAlternativeNameExtension.ISSUER_NAME));
            } catch (IOException unused) {
                return Collections.emptySet();
            }
        } catch (IOException e) {
            throw new CertificateParsingException((Throwable) e);
        }
    }

    public AuthorityInfoAccessExtension getAuthorityInfoAccessExtension() {
        return (AuthorityInfoAccessExtension) getExtension(PKIXExtensions.AuthInfoAccess_Id);
    }

    private void parse(DerValue derValue) throws CertificateException, IOException {
        parse(derValue, (byte[]) null);
    }

    private void parse(DerValue derValue, byte[] bArr) throws CertificateException, IOException {
        if (this.readOnly) {
            throw new CertificateParsingException("cannot over-write existing certificate");
        } else if (derValue.data == null || derValue.tag != 48) {
            throw new CertificateParsingException("invalid DER-encoded certificate data");
        } else {
            if (bArr == null) {
                bArr = derValue.toByteArray();
            }
            this.signedCert = bArr;
            DerValue[] derValueArr = {derValue.data.getDerValue(), derValue.data.getDerValue(), derValue.data.getDerValue()};
            if (derValue.data.available() != 0) {
                throw new CertificateParsingException("signed overrun, bytes = " + derValue.data.available());
            } else if (derValueArr[0].tag == 48) {
                this.algId = AlgorithmId.parse(derValueArr[1]);
                this.signature = derValueArr[2].getBitString();
                if (derValueArr[1].data.available() != 0) {
                    throw new CertificateParsingException("algid field overrun");
                } else if (derValueArr[2].data.available() == 0) {
                    X509CertInfo x509CertInfo = new X509CertInfo(derValueArr[0]);
                    this.info = x509CertInfo;
                    if (this.algId.equals((AlgorithmId) x509CertInfo.get("algorithmID.algorithm"))) {
                        this.readOnly = true;
                        return;
                    }
                    throw new CertificateException("Signature algorithm mismatch");
                } else {
                    throw new CertificateParsingException("signed fields overrun");
                }
            } else {
                throw new CertificateParsingException("signed fields invalid");
            }
        }
    }

    private static X500Principal getX500Principal(X509Certificate x509Certificate, boolean z) throws Exception {
        DerInputStream derInputStream = new DerInputStream(x509Certificate.getEncoded()).getSequence(3)[0].data;
        if (derInputStream.getDerValue().isContextSpecific((byte) 0)) {
            derInputStream.getDerValue();
        }
        derInputStream.getDerValue();
        DerValue derValue = derInputStream.getDerValue();
        if (!z) {
            derInputStream.getDerValue();
            derValue = derInputStream.getDerValue();
        }
        return new X500Principal(derValue.toByteArray());
    }

    public static X500Principal getSubjectX500Principal(X509Certificate x509Certificate) {
        try {
            return getX500Principal(x509Certificate, false);
        } catch (Exception e) {
            throw new RuntimeException("Could not parse subject", e);
        }
    }

    public static X500Principal getIssuerX500Principal(X509Certificate x509Certificate) {
        try {
            return getX500Principal(x509Certificate, true);
        } catch (Exception e) {
            throw new RuntimeException("Could not parse issuer", e);
        }
    }

    public static byte[] getEncodedInternal(Certificate certificate) throws CertificateEncodingException {
        if (certificate instanceof X509CertImpl) {
            return ((X509CertImpl) certificate).getEncodedInternal();
        }
        return certificate.getEncoded();
    }

    public static X509CertImpl toImpl(X509Certificate x509Certificate) throws CertificateException {
        if (x509Certificate instanceof X509CertImpl) {
            return (X509CertImpl) x509Certificate;
        }
        return X509Factory.intern(x509Certificate);
    }

    public static boolean isSelfIssued(X509Certificate x509Certificate) {
        return x509Certificate.getSubjectX500Principal().equals(x509Certificate.getIssuerX500Principal());
    }

    public static boolean isSelfSigned(X509Certificate x509Certificate, String str) {
        if (!isSelfIssued(x509Certificate)) {
            return false;
        }
        if (str == null) {
            try {
                x509Certificate.verify(x509Certificate.getPublicKey());
                return true;
            } catch (Exception unused) {
                return false;
            }
        } else {
            x509Certificate.verify(x509Certificate.getPublicKey(), str);
            return true;
        }
    }

    public static String getFingerprint(String str, X509Certificate x509Certificate) {
        try {
            byte[] digest = MessageDigest.getInstance(str).digest(x509Certificate.getEncoded());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte byte2hex : digest) {
                byte2hex(byte2hex, stringBuffer);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException | CertificateEncodingException unused) {
            return "";
        }
    }

    private static void byte2hex(byte b, StringBuffer stringBuffer) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        stringBuffer.append(cArr[(b & 240) >> 4]);
        stringBuffer.append(cArr[b & 15]);
    }
}
