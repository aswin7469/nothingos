package sun.security.x509;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.util.Enumeration;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class AuthorityKeyIdentifierExtension extends Extension implements CertAttrSet<String> {
    public static final String AUTH_NAME = "auth_name";
    public static final String IDENT = "x509.info.extensions.AuthorityKeyIdentifier";
    public static final String KEY_ID = "key_id";
    public static final String NAME = "AuthorityKeyIdentifier";
    public static final String SERIAL_NUMBER = "serial_number";
    private static final byte TAG_ID = 0;
    private static final byte TAG_NAMES = 1;
    private static final byte TAG_SERIAL_NUM = 2;

    /* renamed from: id */
    private KeyIdentifier f925id;
    private GeneralNames names;
    private SerialNumber serialNum;

    public String getName() {
        return NAME;
    }

    private void encodeThis() throws IOException {
        if (this.f925id == null && this.names == null && this.serialNum == null) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        if (this.f925id != null) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            this.f925id.encode(derOutputStream3);
            derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 0), derOutputStream3);
        }
        try {
            if (this.names != null) {
                DerOutputStream derOutputStream4 = new DerOutputStream();
                this.names.encode(derOutputStream4);
                derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 1), derOutputStream4);
            }
            if (this.serialNum != null) {
                DerOutputStream derOutputStream5 = new DerOutputStream();
                this.serialNum.encode(derOutputStream5);
                derOutputStream2.writeImplicit(DerValue.createTag(Byte.MIN_VALUE, false, (byte) 2), derOutputStream5);
            }
            derOutputStream.write((byte) 48, derOutputStream2);
            this.extensionValue = derOutputStream.toByteArray();
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
    }

    public AuthorityKeyIdentifierExtension(KeyIdentifier keyIdentifier, GeneralNames generalNames, SerialNumber serialNumber) throws IOException {
        this.f925id = keyIdentifier;
        this.names = generalNames;
        this.serialNum = serialNumber;
        this.extensionId = PKIXExtensions.AuthorityKey_Id;
        this.critical = false;
        encodeThis();
    }

    public AuthorityKeyIdentifierExtension(Boolean bool, Object obj) throws IOException {
        this.f925id = null;
        this.names = null;
        this.serialNum = null;
        this.extensionId = PKIXExtensions.AuthorityKey_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        DerValue derValue = new DerValue(this.extensionValue);
        if (derValue.tag == 48) {
            while (derValue.data != null && derValue.data.available() != 0) {
                DerValue derValue2 = derValue.data.getDerValue();
                if (!derValue2.isContextSpecific((byte) 0) || derValue2.isConstructed()) {
                    if (!derValue2.isContextSpecific((byte) 1) || !derValue2.isConstructed()) {
                        if (!derValue2.isContextSpecific((byte) 2) || derValue2.isConstructed()) {
                            throw new IOException("Invalid encoding of AuthorityKeyIdentifierExtension.");
                        } else if (this.serialNum == null) {
                            derValue2.resetTag((byte) 2);
                            this.serialNum = new SerialNumber(derValue2);
                        } else {
                            throw new IOException("Duplicate SerialNumber in AuthorityKeyIdentifier.");
                        }
                    } else if (this.names == null) {
                        derValue2.resetTag((byte) 48);
                        this.names = new GeneralNames(derValue2);
                    } else {
                        throw new IOException("Duplicate GeneralNames in AuthorityKeyIdentifier.");
                    }
                } else if (this.f925id == null) {
                    derValue2.resetTag((byte) 4);
                    this.f925id = new KeyIdentifier(derValue2);
                } else {
                    throw new IOException("Duplicate KeyIdentifier in AuthorityKeyIdentifier.");
                }
            }
            return;
        }
        throw new IOException("Invalid encoding for AuthorityKeyIdentifierExtension.");
    }

    public String toString() {
        String str = super.toString() + "AuthorityKeyIdentifier [\n";
        if (this.f925id != null) {
            str = str + this.f925id.toString();
        }
        if (this.names != null) {
            str = str + this.names.toString() + "\n";
        }
        if (this.serialNum != null) {
            str = str + this.serialNum.toString() + "\n";
        }
        return str + "]\n";
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.AuthorityKey_Id;
            this.critical = false;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (str.equalsIgnoreCase("key_id")) {
            if (obj instanceof KeyIdentifier) {
                this.f925id = (KeyIdentifier) obj;
            } else {
                throw new IOException("Attribute value should be of type KeyIdentifier.");
            }
        } else if (str.equalsIgnoreCase(AUTH_NAME)) {
            if (obj instanceof GeneralNames) {
                this.names = (GeneralNames) obj;
            } else {
                throw new IOException("Attribute value should be of type GeneralNames.");
            }
        } else if (!str.equalsIgnoreCase(SERIAL_NUMBER)) {
            throw new IOException("Attribute name not recognized by CertAttrSet:AuthorityKeyIdentifier.");
        } else if (obj instanceof SerialNumber) {
            this.serialNum = (SerialNumber) obj;
        } else {
            throw new IOException("Attribute value should be of type SerialNumber.");
        }
        encodeThis();
    }

    public Object get(String str) throws IOException {
        if (str.equalsIgnoreCase("key_id")) {
            return this.f925id;
        }
        if (str.equalsIgnoreCase(AUTH_NAME)) {
            return this.names;
        }
        if (str.equalsIgnoreCase(SERIAL_NUMBER)) {
            return this.serialNum;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:AuthorityKeyIdentifier.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase("key_id")) {
            this.f925id = null;
        } else if (str.equalsIgnoreCase(AUTH_NAME)) {
            this.names = null;
        } else if (str.equalsIgnoreCase(SERIAL_NUMBER)) {
            this.serialNum = null;
        } else {
            throw new IOException("Attribute name not recognized by CertAttrSet:AuthorityKeyIdentifier.");
        }
        encodeThis();
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("key_id");
        attributeNameEnumeration.addElement(AUTH_NAME);
        attributeNameEnumeration.addElement(SERIAL_NUMBER);
        return attributeNameEnumeration.elements();
    }

    public byte[] getEncodedKeyIdentifier() throws IOException {
        if (this.f925id == null) {
            return null;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        this.f925id.encode(derOutputStream);
        return derOutputStream.toByteArray();
    }
}
