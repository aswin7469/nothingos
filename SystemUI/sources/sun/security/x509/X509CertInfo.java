package sun.security.x509;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import sun.misc.HexDumpEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class X509CertInfo implements CertAttrSet<String> {
    public static final String ALGORITHM_ID = "algorithmID";
    private static final int ATTR_ALGORITHM = 3;
    private static final int ATTR_EXTENSIONS = 10;
    private static final int ATTR_ISSUER = 4;
    private static final int ATTR_ISSUER_ID = 8;
    private static final int ATTR_KEY = 7;
    private static final int ATTR_SERIAL = 2;
    private static final int ATTR_SUBJECT = 6;
    private static final int ATTR_SUBJECT_ID = 9;
    private static final int ATTR_VALIDITY = 5;
    private static final int ATTR_VERSION = 1;
    public static final String DN_NAME = "dname";
    public static final String EXTENSIONS = "extensions";
    public static final String IDENT = "x509.info";
    public static final String ISSUER = "issuer";
    public static final String ISSUER_ID = "issuerID";
    public static final String KEY = "key";
    public static final String NAME = "info";
    public static final String SERIAL_NUMBER = "serialNumber";
    public static final String SUBJECT = "subject";
    public static final String SUBJECT_ID = "subjectID";
    public static final String VALIDITY = "validity";
    public static final String VERSION = "version";
    private static final Map<String, Integer> map;
    protected CertificateAlgorithmId algId = null;
    protected CertificateExtensions extensions = null;
    protected CertificateValidity interval = null;
    protected X500Name issuer = null;
    protected UniqueIdentity issuerUniqueId = null;
    protected CertificateX509Key pubKey = null;
    private byte[] rawCertInfo = null;
    protected CertificateSerialNumber serialNum = null;
    protected X500Name subject = null;
    protected UniqueIdentity subjectUniqueId = null;
    protected CertificateVersion version = new CertificateVersion();

    public String getName() {
        return "info";
    }

    static {
        HashMap hashMap = new HashMap();
        map = hashMap;
        hashMap.put("version", 1);
        hashMap.put("serialNumber", 2);
        hashMap.put("algorithmID", 3);
        hashMap.put("issuer", 4);
        hashMap.put("validity", 5);
        hashMap.put("subject", 6);
        hashMap.put("key", 7);
        hashMap.put(ISSUER_ID, 8);
        hashMap.put(SUBJECT_ID, 9);
        hashMap.put("extensions", 10);
    }

    public X509CertInfo() {
    }

    public X509CertInfo(byte[] bArr) throws CertificateParsingException {
        try {
            parse(new DerValue(bArr));
        } catch (IOException e) {
            throw new CertificateParsingException((Throwable) e);
        }
    }

    public X509CertInfo(DerValue derValue) throws CertificateParsingException {
        try {
            parse(derValue);
        } catch (IOException e) {
            throw new CertificateParsingException((Throwable) e);
        }
    }

    public void encode(OutputStream outputStream) throws CertificateException, IOException {
        if (this.rawCertInfo == null) {
            DerOutputStream derOutputStream = new DerOutputStream();
            emit(derOutputStream);
            this.rawCertInfo = derOutputStream.toByteArray();
        }
        outputStream.write((byte[]) this.rawCertInfo.clone());
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("version");
        attributeNameEnumeration.addElement("serialNumber");
        attributeNameEnumeration.addElement("algorithmID");
        attributeNameEnumeration.addElement("issuer");
        attributeNameEnumeration.addElement("validity");
        attributeNameEnumeration.addElement("subject");
        attributeNameEnumeration.addElement("key");
        attributeNameEnumeration.addElement(ISSUER_ID);
        attributeNameEnumeration.addElement(SUBJECT_ID);
        attributeNameEnumeration.addElement("extensions");
        return attributeNameEnumeration.elements();
    }

    public byte[] getEncodedInfo() throws CertificateEncodingException {
        try {
            if (this.rawCertInfo == null) {
                DerOutputStream derOutputStream = new DerOutputStream();
                emit(derOutputStream);
                this.rawCertInfo = derOutputStream.toByteArray();
            }
            return (byte[]) this.rawCertInfo.clone();
        } catch (IOException e) {
            throw new CertificateEncodingException(e.toString());
        } catch (CertificateException e2) {
            throw new CertificateEncodingException(e2.toString());
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof X509CertInfo) {
            return equals((X509CertInfo) obj);
        }
        return false;
    }

    public boolean equals(X509CertInfo x509CertInfo) {
        byte[] bArr;
        if (this == x509CertInfo) {
            return true;
        }
        byte[] bArr2 = this.rawCertInfo;
        if (bArr2 == null || (bArr = x509CertInfo.rawCertInfo) == null || bArr2.length != bArr.length) {
            return false;
        }
        int i = 0;
        while (true) {
            byte[] bArr3 = this.rawCertInfo;
            if (i >= bArr3.length) {
                return true;
            }
            if (bArr3[i] != x509CertInfo.rawCertInfo[i]) {
                return false;
            }
            i++;
        }
    }

    public int hashCode() {
        int i = 0;
        int i2 = 1;
        while (true) {
            byte[] bArr = this.rawCertInfo;
            if (i2 >= bArr.length) {
                return i;
            }
            i += bArr[i2] * i2;
            i2++;
        }
    }

    public String toString() {
        if (this.subject == null || this.pubKey == null || this.interval == null || this.issuer == null || this.algId == null || this.serialNum == null) {
            throw new NullPointerException("X.509 cert is incomplete");
        }
        StringBuilder sb = new StringBuilder("[\n");
        sb.append("  " + this.version.toString() + "\n");
        sb.append("  Subject: " + this.subject.toString() + "\n");
        sb.append("  Signature Algorithm: " + this.algId.toString() + "\n");
        sb.append("  Key:  " + this.pubKey.toString() + "\n");
        sb.append("  " + this.interval.toString() + "\n");
        sb.append("  Issuer: " + this.issuer.toString() + "\n");
        sb.append("  " + this.serialNum.toString() + "\n");
        if (this.issuerUniqueId != null) {
            sb.append("  Issuer Id:\n" + this.issuerUniqueId.toString() + "\n");
        }
        if (this.subjectUniqueId != null) {
            sb.append("  Subject Id:\n" + this.subjectUniqueId.toString() + "\n");
        }
        CertificateExtensions certificateExtensions = this.extensions;
        if (certificateExtensions != null) {
            int i = 0;
            Extension[] extensionArr = (Extension[]) certificateExtensions.getAllExtensions().toArray((T[]) new Extension[0]);
            sb.append("\nCertificate Extensions: " + extensionArr.length);
            while (i < extensionArr.length) {
                StringBuilder sb2 = new StringBuilder("\n[");
                int i2 = i + 1;
                sb2.append(i2);
                sb2.append("]: ");
                sb.append(sb2.toString());
                Extension extension = extensionArr[i];
                try {
                    if (OIDMap.getClass(extension.getExtensionId()) == null) {
                        sb.append(extension.toString());
                        byte[] extensionValue = extension.getExtensionValue();
                        if (extensionValue != null) {
                            DerOutputStream derOutputStream = new DerOutputStream();
                            derOutputStream.putOctetString(extensionValue);
                            byte[] byteArray = derOutputStream.toByteArray();
                            HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
                            sb.append("Extension unknown: DER encoded OCTET string =\n" + hexDumpEncoder.encodeBuffer(byteArray) + "\n");
                        }
                    } else {
                        sb.append(extension.toString());
                    }
                } catch (Exception unused) {
                    sb.append(", Error parsing this extension");
                }
                i = i2;
            }
            Map<String, Extension> unparseableExtensions = this.extensions.getUnparseableExtensions();
            if (!unparseableExtensions.isEmpty()) {
                sb.append("\nUnparseable certificate extensions: " + unparseableExtensions.size());
                int i3 = 1;
                for (Extension append : unparseableExtensions.values()) {
                    sb.append("\n[" + i3 + "]: ");
                    sb.append((Object) append);
                    i3++;
                }
            }
        }
        sb.append("\n]");
        return sb.toString();
    }

    public void set(String str, Object obj) throws CertificateException, IOException {
        X509AttributeName x509AttributeName = new X509AttributeName(str);
        int attributeMap = attributeMap(x509AttributeName.getPrefix());
        if (attributeMap != 0) {
            this.rawCertInfo = null;
            String suffix = x509AttributeName.getSuffix();
            switch (attributeMap) {
                case 1:
                    if (suffix == null) {
                        setVersion(obj);
                        return;
                    } else {
                        this.version.set(suffix, obj);
                        return;
                    }
                case 2:
                    if (suffix == null) {
                        setSerialNumber(obj);
                        return;
                    } else {
                        this.serialNum.set(suffix, obj);
                        return;
                    }
                case 3:
                    if (suffix == null) {
                        setAlgorithmId(obj);
                        return;
                    } else {
                        this.algId.set(suffix, obj);
                        return;
                    }
                case 4:
                    setIssuer(obj);
                    return;
                case 5:
                    if (suffix == null) {
                        setValidity(obj);
                        return;
                    } else {
                        this.interval.set(suffix, obj);
                        return;
                    }
                case 6:
                    setSubject(obj);
                    return;
                case 7:
                    if (suffix == null) {
                        setKey(obj);
                        return;
                    } else {
                        this.pubKey.set(suffix, obj);
                        return;
                    }
                case 8:
                    setIssuerUniqueId(obj);
                    return;
                case 9:
                    setSubjectUniqueId(obj);
                    return;
                case 10:
                    if (suffix == null) {
                        setExtensions(obj);
                        return;
                    }
                    if (this.extensions == null) {
                        this.extensions = new CertificateExtensions();
                    }
                    this.extensions.set(suffix, obj);
                    return;
                default:
                    return;
            }
        } else {
            throw new CertificateException("Attribute name not recognized: " + str);
        }
    }

    public void delete(String str) throws CertificateException, IOException {
        X509AttributeName x509AttributeName = new X509AttributeName(str);
        int attributeMap = attributeMap(x509AttributeName.getPrefix());
        if (attributeMap != 0) {
            this.rawCertInfo = null;
            String suffix = x509AttributeName.getSuffix();
            switch (attributeMap) {
                case 1:
                    if (suffix == null) {
                        this.version = null;
                        return;
                    } else {
                        this.version.delete(suffix);
                        return;
                    }
                case 2:
                    if (suffix == null) {
                        this.serialNum = null;
                        return;
                    } else {
                        this.serialNum.delete(suffix);
                        return;
                    }
                case 3:
                    if (suffix == null) {
                        this.algId = null;
                        return;
                    } else {
                        this.algId.delete(suffix);
                        return;
                    }
                case 4:
                    this.issuer = null;
                    return;
                case 5:
                    if (suffix == null) {
                        this.interval = null;
                        return;
                    } else {
                        this.interval.delete(suffix);
                        return;
                    }
                case 6:
                    this.subject = null;
                    return;
                case 7:
                    if (suffix == null) {
                        this.pubKey = null;
                        return;
                    } else {
                        this.pubKey.delete(suffix);
                        return;
                    }
                case 8:
                    this.issuerUniqueId = null;
                    return;
                case 9:
                    this.subjectUniqueId = null;
                    return;
                case 10:
                    if (suffix == null) {
                        this.extensions = null;
                        return;
                    }
                    CertificateExtensions certificateExtensions = this.extensions;
                    if (certificateExtensions != null) {
                        certificateExtensions.delete(suffix);
                        return;
                    }
                    return;
                default:
                    return;
            }
        } else {
            throw new CertificateException("Attribute name not recognized: " + str);
        }
    }

    public Object get(String str) throws CertificateException, IOException {
        X509AttributeName x509AttributeName = new X509AttributeName(str);
        int attributeMap = attributeMap(x509AttributeName.getPrefix());
        if (attributeMap != 0) {
            String suffix = x509AttributeName.getSuffix();
            switch (attributeMap) {
                case 1:
                    if (suffix == null) {
                        return this.version;
                    }
                    return this.version.get(suffix);
                case 2:
                    if (suffix == null) {
                        return this.serialNum;
                    }
                    return this.serialNum.get(suffix);
                case 3:
                    if (suffix == null) {
                        return this.algId;
                    }
                    return this.algId.get(suffix);
                case 4:
                    if (suffix == null) {
                        return this.issuer;
                    }
                    return getX500Name(suffix, true);
                case 5:
                    if (suffix == null) {
                        return this.interval;
                    }
                    return this.interval.get(suffix);
                case 6:
                    if (suffix == null) {
                        return this.subject;
                    }
                    return getX500Name(suffix, false);
                case 7:
                    if (suffix == null) {
                        return this.pubKey;
                    }
                    return this.pubKey.get(suffix);
                case 8:
                    return this.issuerUniqueId;
                case 9:
                    return this.subjectUniqueId;
                case 10:
                    if (suffix == null) {
                        return this.extensions;
                    }
                    CertificateExtensions certificateExtensions = this.extensions;
                    if (certificateExtensions == null) {
                        return null;
                    }
                    return certificateExtensions.get(suffix);
                default:
                    return null;
            }
        } else {
            throw new CertificateParsingException("Attribute name not recognized: " + str);
        }
    }

    private Object getX500Name(String str, boolean z) throws IOException {
        if (str.equalsIgnoreCase("dname")) {
            return z ? this.issuer : this.subject;
        }
        if (!str.equalsIgnoreCase("x500principal")) {
            throw new IOException("Attribute name not recognized.");
        } else if (z) {
            return this.issuer.asX500Principal();
        } else {
            return this.subject.asX500Principal();
        }
    }

    private void parse(DerValue derValue) throws CertificateParsingException, IOException {
        if (derValue.tag == 48) {
            this.rawCertInfo = derValue.toByteArray();
            DerInputStream derInputStream = derValue.data;
            DerValue derValue2 = derInputStream.getDerValue();
            if (derValue2.isContextSpecific((byte) 0)) {
                this.version = new CertificateVersion(derValue2);
                derValue2 = derInputStream.getDerValue();
            }
            this.serialNum = new CertificateSerialNumber(derValue2);
            this.algId = new CertificateAlgorithmId(derInputStream);
            X500Name x500Name = new X500Name(derInputStream);
            this.issuer = x500Name;
            if (!x500Name.isEmpty()) {
                this.interval = new CertificateValidity(derInputStream);
                this.subject = new X500Name(derInputStream);
                if (this.version.compare(0) != 0 || !this.subject.isEmpty()) {
                    this.pubKey = new CertificateX509Key(derInputStream);
                    if (derInputStream.available() == 0) {
                        return;
                    }
                    if (this.version.compare(0) != 0) {
                        DerValue derValue3 = derInputStream.getDerValue();
                        if (derValue3.isContextSpecific((byte) 1)) {
                            this.issuerUniqueId = new UniqueIdentity(derValue3);
                            if (derInputStream.available() != 0) {
                                derValue3 = derInputStream.getDerValue();
                            } else {
                                return;
                            }
                        }
                        if (derValue3.isContextSpecific((byte) 2)) {
                            this.subjectUniqueId = new UniqueIdentity(derValue3);
                            if (derInputStream.available() != 0) {
                                derValue3 = derInputStream.getDerValue();
                            } else {
                                return;
                            }
                        }
                        if (this.version.compare(2) == 0) {
                            if (derValue3.isConstructed() && derValue3.isContextSpecific((byte) 3)) {
                                this.extensions = new CertificateExtensions(derValue3.data);
                            }
                            verifyCert(this.subject, this.extensions);
                            return;
                        }
                        throw new CertificateParsingException("Extensions not allowed in v2 certificate");
                    }
                    throw new CertificateParsingException("no more data allowed for version 1 certificate");
                }
                throw new CertificateParsingException("Empty subject DN not allowed in v1 certificate");
            }
            throw new CertificateParsingException("Empty issuer DN not allowed in X509Certificates");
        }
        throw new CertificateParsingException("signed fields invalid");
    }

    private void verifyCert(X500Name x500Name, CertificateExtensions certificateExtensions) throws CertificateParsingException, IOException {
        if (!x500Name.isEmpty()) {
            return;
        }
        if (certificateExtensions != null) {
            try {
                SubjectAlternativeNameExtension subjectAlternativeNameExtension = (SubjectAlternativeNameExtension) certificateExtensions.get(SubjectAlternativeNameExtension.NAME);
                GeneralNames generalNames = subjectAlternativeNameExtension.get(SubjectAlternativeNameExtension.SUBJECT_NAME);
                if (generalNames == null || generalNames.isEmpty()) {
                    throw new CertificateParsingException("X.509 Certificate is incomplete: subject field is empty, and SubjectAlternativeName extension is empty");
                } else if (!subjectAlternativeNameExtension.isCritical()) {
                    throw new CertificateParsingException("X.509 Certificate is incomplete: SubjectAlternativeName extension MUST be marked critical when subject field is empty");
                }
            } catch (IOException unused) {
                throw new CertificateParsingException("X.509 Certificate is incomplete: subject field is empty, and SubjectAlternativeName extension is absent");
            }
        } else {
            throw new CertificateParsingException("X.509 Certificate is incomplete: subject field is empty, and certificate has no extensions");
        }
    }

    private void emit(DerOutputStream derOutputStream) throws CertificateException, IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        this.version.encode(derOutputStream2);
        this.serialNum.encode(derOutputStream2);
        this.algId.encode(derOutputStream2);
        if (this.version.compare(0) == 0 && this.issuer.toString() == null) {
            throw new CertificateParsingException("Null issuer DN not allowed in v1 certificate");
        }
        this.issuer.encode(derOutputStream2);
        this.interval.encode(derOutputStream2);
        if (this.version.compare(0) == 0 && this.subject.toString() == null) {
            throw new CertificateParsingException("Null subject DN not allowed in v1 certificate");
        }
        this.subject.encode(derOutputStream2);
        this.pubKey.encode(derOutputStream2);
        UniqueIdentity uniqueIdentity = this.issuerUniqueId;
        if (uniqueIdentity != null) {
            uniqueIdentity.encode(derOutputStream2, DerValue.createTag(Byte.MIN_VALUE, false, (byte) 1));
        }
        UniqueIdentity uniqueIdentity2 = this.subjectUniqueId;
        if (uniqueIdentity2 != null) {
            uniqueIdentity2.encode(derOutputStream2, DerValue.createTag(Byte.MIN_VALUE, false, (byte) 2));
        }
        CertificateExtensions certificateExtensions = this.extensions;
        if (certificateExtensions != null) {
            certificateExtensions.encode(derOutputStream2);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
    }

    private int attributeMap(String str) {
        Integer num = map.get(str);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    private void setVersion(Object obj) throws CertificateException {
        if (obj instanceof CertificateVersion) {
            this.version = (CertificateVersion) obj;
            return;
        }
        throw new CertificateException("Version class type invalid.");
    }

    private void setSerialNumber(Object obj) throws CertificateException {
        if (obj instanceof CertificateSerialNumber) {
            this.serialNum = (CertificateSerialNumber) obj;
            return;
        }
        throw new CertificateException("SerialNumber class type invalid.");
    }

    private void setAlgorithmId(Object obj) throws CertificateException {
        if (obj instanceof CertificateAlgorithmId) {
            this.algId = (CertificateAlgorithmId) obj;
            return;
        }
        throw new CertificateException("AlgorithmId class type invalid.");
    }

    private void setIssuer(Object obj) throws CertificateException {
        if (obj instanceof X500Name) {
            this.issuer = (X500Name) obj;
            return;
        }
        throw new CertificateException("Issuer class type invalid.");
    }

    private void setValidity(Object obj) throws CertificateException {
        if (obj instanceof CertificateValidity) {
            this.interval = (CertificateValidity) obj;
            return;
        }
        throw new CertificateException("CertificateValidity class type invalid.");
    }

    private void setSubject(Object obj) throws CertificateException {
        if (obj instanceof X500Name) {
            this.subject = (X500Name) obj;
            return;
        }
        throw new CertificateException("Subject class type invalid.");
    }

    private void setKey(Object obj) throws CertificateException {
        if (obj instanceof CertificateX509Key) {
            this.pubKey = (CertificateX509Key) obj;
            return;
        }
        throw new CertificateException("Key class type invalid.");
    }

    private void setIssuerUniqueId(Object obj) throws CertificateException {
        if (this.version.compare(1) < 0) {
            throw new CertificateException("Invalid version");
        } else if (obj instanceof UniqueIdentity) {
            this.issuerUniqueId = (UniqueIdentity) obj;
        } else {
            throw new CertificateException("IssuerUniqueId class type invalid.");
        }
    }

    private void setSubjectUniqueId(Object obj) throws CertificateException {
        if (this.version.compare(1) < 0) {
            throw new CertificateException("Invalid version");
        } else if (obj instanceof UniqueIdentity) {
            this.subjectUniqueId = (UniqueIdentity) obj;
        } else {
            throw new CertificateException("SubjectUniqueId class type invalid.");
        }
    }

    private void setExtensions(Object obj) throws CertificateException {
        if (this.version.compare(2) < 0) {
            throw new CertificateException("Invalid version");
        } else if (obj instanceof CertificateExtensions) {
            this.extensions = (CertificateExtensions) obj;
        } else {
            throw new CertificateException("Extensions class type invalid.");
        }
    }
}
