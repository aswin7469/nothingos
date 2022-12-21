package sun.security.x509;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.PublicKey;
import java.util.Enumeration;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class CertificateX509Key implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.key";
    public static final String KEY = "value";
    public static final String NAME = "key";
    private PublicKey key;

    public String getName() {
        return "key";
    }

    public CertificateX509Key(PublicKey publicKey) {
        this.key = publicKey;
    }

    public CertificateX509Key(DerInputStream derInputStream) throws IOException {
        this.key = X509Key.parse(derInputStream.getDerValue());
    }

    public CertificateX509Key(InputStream inputStream) throws IOException {
        this.key = X509Key.parse(new DerValue(inputStream));
    }

    public String toString() {
        PublicKey publicKey = this.key;
        if (publicKey == null) {
            return "";
        }
        return publicKey.toString();
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.write(this.key.getEncoded());
        outputStream.write(derOutputStream.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (str.equalsIgnoreCase("value")) {
            this.key = (PublicKey) obj;
            return;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet: CertificateX509Key.");
    }

    public PublicKey get(String str) throws IOException {
        if (str.equalsIgnoreCase("value")) {
            return this.key;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet: CertificateX509Key.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase("value")) {
            this.key = null;
            return;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet: CertificateX509Key.");
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("value");
        return attributeNameEnumeration.elements();
    }
}
