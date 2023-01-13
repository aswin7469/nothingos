package sun.security.x509;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.util.Enumeration;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class SubjectKeyIdentifierExtension extends Extension implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.extensions.SubjectKeyIdentifier";
    public static final String KEY_ID = "key_id";
    public static final String NAME = "SubjectKeyIdentifier";

    /* renamed from: id */
    private KeyIdentifier f932id = null;

    public String getName() {
        return NAME;
    }

    private void encodeThis() throws IOException {
        if (this.f932id == null) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        this.f932id.encode(derOutputStream);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public SubjectKeyIdentifierExtension(byte[] bArr) throws IOException {
        this.f932id = new KeyIdentifier(bArr);
        this.extensionId = PKIXExtensions.SubjectKey_Id;
        this.critical = false;
        encodeThis();
    }

    public SubjectKeyIdentifierExtension(Boolean bool, Object obj) throws IOException {
        this.extensionId = PKIXExtensions.SubjectKey_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        this.f932id = new KeyIdentifier(new DerValue(this.extensionValue));
    }

    public String toString() {
        return super.toString() + "SubjectKeyIdentifier [\n" + String.valueOf((Object) this.f932id) + "]\n";
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.SubjectKey_Id;
            this.critical = false;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (!str.equalsIgnoreCase("key_id")) {
            throw new IOException("Attribute name not recognized by CertAttrSet:SubjectKeyIdentifierExtension.");
        } else if (obj instanceof KeyIdentifier) {
            this.f932id = (KeyIdentifier) obj;
            encodeThis();
        } else {
            throw new IOException("Attribute value should be of type KeyIdentifier.");
        }
    }

    public KeyIdentifier get(String str) throws IOException {
        if (str.equalsIgnoreCase("key_id")) {
            return this.f932id;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:SubjectKeyIdentifierExtension.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase("key_id")) {
            this.f932id = null;
            encodeThis();
            return;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:SubjectKeyIdentifierExtension.");
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("key_id");
        return attributeNameEnumeration.elements();
    }
}
