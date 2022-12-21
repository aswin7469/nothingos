package sun.security.pkcs;

import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import javax.xml.datatype.DatatypeConstants;
import sun.misc.HexDumpEncoder;
import sun.security.util.Debug;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.CertificateExtensions;

public class PKCS9Attribute implements DerEncoder {
    private static final Class<?> BYTE_ARRAY_CLASS;
    public static final ObjectIdentifier CHALLENGE_PASSWORD_OID;
    public static final String CHALLENGE_PASSWORD_STR = "ChallengePassword";
    public static final ObjectIdentifier CONTENT_TYPE_OID;
    public static final String CONTENT_TYPE_STR = "ContentType";
    public static final ObjectIdentifier COUNTERSIGNATURE_OID;
    public static final String COUNTERSIGNATURE_STR = "Countersignature";
    public static final ObjectIdentifier EMAIL_ADDRESS_OID;
    public static final String EMAIL_ADDRESS_STR = "EmailAddress";
    public static final ObjectIdentifier EXTENDED_CERTIFICATE_ATTRIBUTES_OID;
    public static final String EXTENDED_CERTIFICATE_ATTRIBUTES_STR = "ExtendedCertificateAttributes";
    public static final ObjectIdentifier EXTENSION_REQUEST_OID;
    public static final String EXTENSION_REQUEST_STR = "ExtensionRequest";
    public static final ObjectIdentifier ISSUER_SERIALNUMBER_OID;
    public static final String ISSUER_SERIALNUMBER_STR = "IssuerAndSerialNumber";
    public static final ObjectIdentifier MESSAGE_DIGEST_OID;
    public static final String MESSAGE_DIGEST_STR = "MessageDigest";
    private static final Hashtable<String, ObjectIdentifier> NAME_OID_TABLE;
    private static final Hashtable<ObjectIdentifier, String> OID_NAME_TABLE;
    static final ObjectIdentifier[] PKCS9_OIDS = new ObjectIdentifier[18];
    private static final Byte[][] PKCS9_VALUE_TAGS = {null, new Byte[]{new Byte((byte) 22)}, new Byte[]{new Byte((byte) 22), new Byte((byte) 19)}, new Byte[]{new Byte((byte) 6)}, new Byte[]{new Byte((byte) 4)}, new Byte[]{new Byte((byte) 23)}, new Byte[]{new Byte((byte) 48)}, new Byte[]{new Byte((byte) 19), new Byte((byte) 20)}, new Byte[]{new Byte((byte) 19), new Byte((byte) 20)}, new Byte[]{new Byte((byte) 49)}, new Byte[]{new Byte((byte) 48)}, null, null, null, new Byte[]{new Byte((byte) 48)}, new Byte[]{new Byte((byte) 48)}, new Byte[]{new Byte((byte) 48)}, new Byte[]{new Byte((byte) 48)}};
    private static final String RSA_PROPRIETARY_STR = "RSAProprietary";
    public static final ObjectIdentifier SIGNATURE_TIMESTAMP_TOKEN_OID;
    public static final String SIGNATURE_TIMESTAMP_TOKEN_STR = "SignatureTimestampToken";
    public static final ObjectIdentifier SIGNING_CERTIFICATE_OID;
    public static final String SIGNING_CERTIFICATE_STR = "SigningCertificate";
    public static final ObjectIdentifier SIGNING_TIME_OID;
    public static final String SIGNING_TIME_STR = "SigningTime";
    private static final boolean[] SINGLE_VALUED = {false, false, false, true, true, true, false, true, false, false, true, false, false, false, true, true, true, true};
    public static final ObjectIdentifier SMIME_CAPABILITY_OID;
    public static final String SMIME_CAPABILITY_STR = "SMIMECapability";
    private static final String SMIME_SIGNING_DESC_STR = "SMIMESigningDesc";
    public static final ObjectIdentifier UNSTRUCTURED_ADDRESS_OID;
    public static final String UNSTRUCTURED_ADDRESS_STR = "UnstructuredAddress";
    public static final ObjectIdentifier UNSTRUCTURED_NAME_OID;
    public static final String UNSTRUCTURED_NAME_STR = "UnstructuredName";
    private static final Class<?>[] VALUE_CLASSES;
    private static final Debug debug = Debug.getInstance("jar");
    private int index;
    private ObjectIdentifier oid;
    private Object value;

    static {
        int i = 1;
        while (true) {
            ObjectIdentifier[] objectIdentifierArr = PKCS9_OIDS;
            if (i < objectIdentifierArr.length - 2) {
                objectIdentifierArr[i] = ObjectIdentifier.newInternal(new int[]{1, 2, 840, 113549, 1, 9, i});
                i++;
            } else {
                objectIdentifierArr[objectIdentifierArr.length - 2] = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 9, 16, 2, 12});
                objectIdentifierArr[objectIdentifierArr.length - 1] = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 9, 16, 2, 14});
                try {
                    Class<?> cls = Class.forName("[B");
                    BYTE_ARRAY_CLASS = cls;
                    EMAIL_ADDRESS_OID = objectIdentifierArr[1];
                    UNSTRUCTURED_NAME_OID = objectIdentifierArr[2];
                    CONTENT_TYPE_OID = objectIdentifierArr[3];
                    MESSAGE_DIGEST_OID = objectIdentifierArr[4];
                    SIGNING_TIME_OID = objectIdentifierArr[5];
                    COUNTERSIGNATURE_OID = objectIdentifierArr[6];
                    CHALLENGE_PASSWORD_OID = objectIdentifierArr[7];
                    UNSTRUCTURED_ADDRESS_OID = objectIdentifierArr[8];
                    EXTENDED_CERTIFICATE_ATTRIBUTES_OID = objectIdentifierArr[9];
                    ISSUER_SERIALNUMBER_OID = objectIdentifierArr[10];
                    EXTENSION_REQUEST_OID = objectIdentifierArr[14];
                    SMIME_CAPABILITY_OID = objectIdentifierArr[15];
                    SIGNING_CERTIFICATE_OID = objectIdentifierArr[16];
                    SIGNATURE_TIMESTAMP_TOKEN_OID = objectIdentifierArr[17];
                    Hashtable<String, ObjectIdentifier> hashtable = new Hashtable<>(18);
                    NAME_OID_TABLE = hashtable;
                    hashtable.put("emailaddress", objectIdentifierArr[1]);
                    hashtable.put("unstructuredname", objectIdentifierArr[2]);
                    hashtable.put("contenttype", objectIdentifierArr[3]);
                    hashtable.put("messagedigest", objectIdentifierArr[4]);
                    hashtable.put("signingtime", objectIdentifierArr[5]);
                    hashtable.put("countersignature", objectIdentifierArr[6]);
                    hashtable.put("challengepassword", objectIdentifierArr[7]);
                    hashtable.put("unstructuredaddress", objectIdentifierArr[8]);
                    hashtable.put("extendedcertificateattributes", objectIdentifierArr[9]);
                    hashtable.put("issuerandserialnumber", objectIdentifierArr[10]);
                    hashtable.put("rsaproprietary", objectIdentifierArr[11]);
                    hashtable.put("rsaproprietary", objectIdentifierArr[12]);
                    hashtable.put("signingdescription", objectIdentifierArr[13]);
                    hashtable.put("extensionrequest", objectIdentifierArr[14]);
                    hashtable.put("smimecapability", objectIdentifierArr[15]);
                    hashtable.put("signingcertificate", objectIdentifierArr[16]);
                    hashtable.put("signaturetimestamptoken", objectIdentifierArr[17]);
                    Hashtable<ObjectIdentifier, String> hashtable2 = new Hashtable<>(16);
                    OID_NAME_TABLE = hashtable2;
                    hashtable2.put(objectIdentifierArr[1], EMAIL_ADDRESS_STR);
                    hashtable2.put(objectIdentifierArr[2], UNSTRUCTURED_NAME_STR);
                    hashtable2.put(objectIdentifierArr[3], CONTENT_TYPE_STR);
                    hashtable2.put(objectIdentifierArr[4], MESSAGE_DIGEST_STR);
                    hashtable2.put(objectIdentifierArr[5], SIGNING_TIME_STR);
                    hashtable2.put(objectIdentifierArr[6], COUNTERSIGNATURE_STR);
                    hashtable2.put(objectIdentifierArr[7], CHALLENGE_PASSWORD_STR);
                    hashtable2.put(objectIdentifierArr[8], UNSTRUCTURED_ADDRESS_STR);
                    hashtable2.put(objectIdentifierArr[9], EXTENDED_CERTIFICATE_ATTRIBUTES_STR);
                    hashtable2.put(objectIdentifierArr[10], ISSUER_SERIALNUMBER_STR);
                    hashtable2.put(objectIdentifierArr[11], RSA_PROPRIETARY_STR);
                    hashtable2.put(objectIdentifierArr[12], RSA_PROPRIETARY_STR);
                    hashtable2.put(objectIdentifierArr[13], SMIME_SIGNING_DESC_STR);
                    hashtable2.put(objectIdentifierArr[14], EXTENSION_REQUEST_STR);
                    hashtable2.put(objectIdentifierArr[15], SMIME_CAPABILITY_STR);
                    hashtable2.put(objectIdentifierArr[16], SIGNING_CERTIFICATE_STR);
                    hashtable2.put(objectIdentifierArr[17], SIGNATURE_TIMESTAMP_TOKEN_STR);
                    Class<?>[] clsArr = new Class[18];
                    VALUE_CLASSES = clsArr;
                    try {
                        Class<?> cls2 = Class.forName("[Ljava.lang.String;");
                        clsArr[0] = null;
                        clsArr[1] = cls2;
                        clsArr[2] = cls2;
                        clsArr[3] = Class.forName("sun.security.util.ObjectIdentifier");
                        clsArr[4] = cls;
                        clsArr[5] = Class.forName("java.util.Date");
                        clsArr[6] = Class.forName("[Lsun.security.pkcs.SignerInfo;");
                        clsArr[7] = Class.forName("java.lang.String");
                        clsArr[8] = cls2;
                        clsArr[9] = null;
                        clsArr[10] = null;
                        clsArr[11] = null;
                        clsArr[12] = null;
                        clsArr[13] = null;
                        clsArr[14] = Class.forName("sun.security.x509.CertificateExtensions");
                        clsArr[15] = null;
                        clsArr[16] = null;
                        clsArr[17] = cls;
                        return;
                    } catch (ClassNotFoundException e) {
                        throw new ExceptionInInitializerError(e.toString());
                    }
                } catch (ClassNotFoundException e2) {
                    throw new ExceptionInInitializerError(e2.toString());
                }
            }
        }
    }

    public PKCS9Attribute(ObjectIdentifier objectIdentifier, Object obj) throws IllegalArgumentException {
        init(objectIdentifier, obj);
    }

    public PKCS9Attribute(String str, Object obj) throws IllegalArgumentException {
        ObjectIdentifier oid2 = getOID(str);
        if (oid2 != null) {
            init(oid2, obj);
            return;
        }
        throw new IllegalArgumentException("Unrecognized attribute name " + str + " constructing PKCS9Attribute.");
    }

    private void init(ObjectIdentifier objectIdentifier, Object obj) throws IllegalArgumentException {
        this.oid = objectIdentifier;
        int indexOf = indexOf(objectIdentifier, PKCS9_OIDS, 1);
        this.index = indexOf;
        Class<?> cls = indexOf == -1 ? BYTE_ARRAY_CLASS : VALUE_CLASSES[indexOf];
        if (cls.isInstance(obj)) {
            this.value = obj;
            return;
        }
        throw new IllegalArgumentException("Wrong value class  for attribute " + objectIdentifier + " constructing PKCS9Attribute; was " + obj.getClass().toString() + ", should be " + cls.toString());
    }

    public PKCS9Attribute(DerValue derValue) throws IOException {
        DerInputStream derInputStream = new DerInputStream(derValue.toByteArray());
        DerValue[] sequence = derInputStream.getSequence(2);
        if (derInputStream.available() != 0) {
            throw new IOException("Excess data parsing PKCS9Attribute");
        } else if (sequence.length == 2) {
            int i = 0;
            this.oid = sequence[0].getOID();
            byte[] byteArray = sequence[1].toByteArray();
            DerValue[] set = new DerInputStream(byteArray).getSet(1);
            int indexOf = indexOf(this.oid, PKCS9_OIDS, 1);
            this.index = indexOf;
            if (indexOf == -1) {
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("Unsupported signer attribute: " + this.oid);
                }
                this.value = byteArray;
                return;
            }
            if (SINGLE_VALUED[indexOf] && set.length > 1) {
                throwSingleValuedException();
            }
            for (DerValue derValue2 : set) {
                Byte b = new Byte(derValue2.tag);
                if (indexOf(b, PKCS9_VALUE_TAGS[this.index], 0) == -1) {
                    throwTagException(b);
                }
            }
            switch (this.index) {
                case 1:
                case 2:
                case 8:
                    String[] strArr = new String[set.length];
                    while (i < set.length) {
                        strArr[i] = set[i].getAsString();
                        i++;
                    }
                    this.value = strArr;
                    return;
                case 3:
                    this.value = set[0].getOID();
                    return;
                case 4:
                    this.value = set[0].getOctetString();
                    return;
                case 5:
                    this.value = new DerInputStream(set[0].toByteArray()).getUTCTime();
                    return;
                case 6:
                    SignerInfo[] signerInfoArr = new SignerInfo[set.length];
                    while (i < set.length) {
                        signerInfoArr[i] = new SignerInfo(set[i].toDerInputStream());
                        i++;
                    }
                    this.value = signerInfoArr;
                    return;
                case 7:
                    this.value = set[0].getAsString();
                    return;
                case 9:
                    throw new IOException("PKCS9 extended-certificate attribute not supported.");
                case 10:
                    throw new IOException("PKCS9 IssuerAndSerialNumberattribute not supported.");
                case 11:
                case 12:
                    throw new IOException("PKCS9 RSA DSI attributes11 and 12, not supported.");
                case 13:
                    throw new IOException("PKCS9 attribute #13 not supported.");
                case 14:
                    this.value = new CertificateExtensions(new DerInputStream(set[0].toByteArray()));
                    return;
                case 15:
                    throw new IOException("PKCS9 SMIMECapability attribute not supported.");
                case 16:
                    this.value = new SigningCertificateInfo(set[0].toByteArray());
                    return;
                case 17:
                    this.value = set[0].toByteArray();
                    return;
                default:
                    return;
            }
        } else {
            throw new IOException("PKCS9Attribute doesn't have two components");
        }
    }

    public void derEncode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putOID(this.oid);
        int i = 0;
        switch (this.index) {
            case -1:
                derOutputStream.write((byte[]) this.value);
                break;
            case 1:
            case 2:
                String[] strArr = (String[]) this.value;
                DerOutputStream[] derOutputStreamArr = new DerOutputStream[strArr.length];
                while (i < strArr.length) {
                    DerOutputStream derOutputStream2 = new DerOutputStream();
                    derOutputStreamArr[i] = derOutputStream2;
                    derOutputStream2.putIA5String(strArr[i]);
                    i++;
                }
                derOutputStream.putOrderedSetOf((byte) 49, derOutputStreamArr);
                break;
            case 3:
                DerOutputStream derOutputStream3 = new DerOutputStream();
                derOutputStream3.putOID((ObjectIdentifier) this.value);
                derOutputStream.write((byte) 49, derOutputStream3.toByteArray());
                break;
            case 4:
                DerOutputStream derOutputStream4 = new DerOutputStream();
                derOutputStream4.putOctetString((byte[]) this.value);
                derOutputStream.write((byte) 49, derOutputStream4.toByteArray());
                break;
            case 5:
                DerOutputStream derOutputStream5 = new DerOutputStream();
                derOutputStream5.putUTCTime((Date) this.value);
                derOutputStream.write((byte) 49, derOutputStream5.toByteArray());
                break;
            case 6:
                derOutputStream.putOrderedSetOf((byte) 49, (DerEncoder[]) this.value);
                break;
            case 7:
                DerOutputStream derOutputStream6 = new DerOutputStream();
                derOutputStream6.putPrintableString((String) this.value);
                derOutputStream.write((byte) 49, derOutputStream6.toByteArray());
                break;
            case 8:
                String[] strArr2 = (String[]) this.value;
                DerOutputStream[] derOutputStreamArr2 = new DerOutputStream[strArr2.length];
                while (i < strArr2.length) {
                    DerOutputStream derOutputStream7 = new DerOutputStream();
                    derOutputStreamArr2[i] = derOutputStream7;
                    derOutputStream7.putPrintableString(strArr2[i]);
                    i++;
                }
                derOutputStream.putOrderedSetOf((byte) 49, derOutputStreamArr2);
                break;
            case 9:
                throw new IOException("PKCS9 extended-certificate attribute not supported.");
            case 10:
                throw new IOException("PKCS9 IssuerAndSerialNumberattribute not supported.");
            case 11:
            case 12:
                throw new IOException("PKCS9 RSA DSI attributes11 and 12, not supported.");
            case 13:
                throw new IOException("PKCS9 attribute #13 not supported.");
            case 14:
                DerOutputStream derOutputStream8 = new DerOutputStream();
                try {
                    ((CertificateExtensions) this.value).encode(derOutputStream8, true);
                    derOutputStream.write((byte) 49, derOutputStream8.toByteArray());
                    break;
                } catch (CertificateException e) {
                    throw new IOException(e.toString());
                }
            case 15:
                throw new IOException("PKCS9 attribute #15 not supported.");
            case 16:
                throw new IOException("PKCS9 SigningCertificate attribute not supported.");
            case 17:
                derOutputStream.write((byte) 49, (byte[]) this.value);
                break;
        }
        DerOutputStream derOutputStream9 = new DerOutputStream();
        derOutputStream9.write((byte) 48, derOutputStream.toByteArray());
        outputStream.write(derOutputStream9.toByteArray());
    }

    public boolean isKnown() {
        return this.index != -1;
    }

    public Object getValue() {
        return this.value;
    }

    public boolean isSingleValued() {
        int i = this.index;
        return i == -1 || SINGLE_VALUED[i];
    }

    public ObjectIdentifier getOID() {
        return this.oid;
    }

    public String getName() {
        int i = this.index;
        if (i == -1) {
            return this.oid.toString();
        }
        return OID_NAME_TABLE.get(PKCS9_OIDS[i]);
    }

    public static ObjectIdentifier getOID(String str) {
        return NAME_OID_TABLE.get(str.toLowerCase(Locale.ENGLISH));
    }

    public static String getName(ObjectIdentifier objectIdentifier) {
        return OID_NAME_TABLE.get(objectIdentifier);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(100);
        stringBuffer.append(NavigationBarInflaterView.SIZE_MOD_START);
        int i = this.index;
        if (i == -1) {
            stringBuffer.append(this.oid.toString());
        } else {
            stringBuffer.append(OID_NAME_TABLE.get(PKCS9_OIDS[i]));
        }
        stringBuffer.append(": ");
        int i2 = this.index;
        if (i2 == -1 || SINGLE_VALUED[i2]) {
            Object obj = this.value;
            if (obj instanceof byte[]) {
                stringBuffer.append(new HexDumpEncoder().encodeBuffer((byte[]) this.value));
            } else {
                stringBuffer.append(obj.toString());
            }
            stringBuffer.append(NavigationBarInflaterView.SIZE_MOD_END);
            return stringBuffer.toString();
        }
        Object[] objArr = (Object[]) this.value;
        boolean z = true;
        for (Object obj2 : objArr) {
            if (z) {
                z = false;
            } else {
                stringBuffer.append(", ");
            }
            stringBuffer.append(obj2.toString());
        }
        return stringBuffer.toString();
    }

    static int indexOf(Object obj, Object[] objArr, int i) {
        while (i < objArr.length) {
            if (obj.equals(objArr[i])) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void throwSingleValuedException() throws IOException {
        throw new IOException("Single-value attribute " + this.oid + " (" + getName() + ") has multiple values.");
    }

    private void throwTagException(Byte b) throws IOException {
        Byte[] bArr = PKCS9_VALUE_TAGS[this.index];
        StringBuffer stringBuffer = new StringBuffer(100);
        stringBuffer.append("Value of attribute ");
        stringBuffer.append(this.oid.toString());
        stringBuffer.append(" (");
        stringBuffer.append(getName());
        stringBuffer.append(") has wrong tag: ");
        stringBuffer.append(b.toString());
        stringBuffer.append(".  Expected tags: ");
        stringBuffer.append(bArr[0].toString());
        for (int i = 1; i < bArr.length; i++) {
            stringBuffer.append(", ");
            stringBuffer.append(bArr[i].toString());
        }
        stringBuffer.append(BaseIconCache.EMPTY_CLASS_NAME);
        throw new IOException(stringBuffer.toString());
    }
}
