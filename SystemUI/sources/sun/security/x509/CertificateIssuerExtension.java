package sun.security.x509;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.util.Enumeration;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class CertificateIssuerExtension extends Extension implements CertAttrSet<String> {
    public static final String ISSUER = "issuer";
    public static final String NAME = "CertificateIssuer";
    private GeneralNames names;

    public String getName() {
        return NAME;
    }

    private void encodeThis() throws IOException {
        GeneralNames generalNames = this.names;
        if (generalNames == null || generalNames.isEmpty()) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        this.names.encode(derOutputStream);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public CertificateIssuerExtension(GeneralNames generalNames) throws IOException {
        this.extensionId = PKIXExtensions.CertificateIssuer_Id;
        this.critical = true;
        this.names = generalNames;
        encodeThis();
    }

    public CertificateIssuerExtension(Boolean bool, Object obj) throws IOException {
        this.extensionId = PKIXExtensions.CertificateIssuer_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        this.names = new GeneralNames(new DerValue(this.extensionValue));
    }

    public void set(String str, Object obj) throws IOException {
        if (!str.equalsIgnoreCase("issuer")) {
            throw new IOException("Attribute name not recognized by CertAttrSet:CertificateIssuer");
        } else if (obj instanceof GeneralNames) {
            this.names = (GeneralNames) obj;
            encodeThis();
        } else {
            throw new IOException("Attribute value must be of type GeneralNames");
        }
    }

    public GeneralNames get(String str) throws IOException {
        if (str.equalsIgnoreCase("issuer")) {
            return this.names;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:CertificateIssuer");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase("issuer")) {
            this.names = null;
            encodeThis();
            return;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:CertificateIssuer");
    }

    public String toString() {
        return super.toString() + "Certificate Issuer [\n" + String.valueOf((Object) this.names) + "]\n";
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.CertificateIssuer_Id;
            this.critical = true;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("issuer");
        return attributeNameEnumeration.elements();
    }
}
