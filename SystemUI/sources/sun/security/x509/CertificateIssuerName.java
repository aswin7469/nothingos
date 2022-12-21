package sun.security.x509;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.util.Enumeration;
import javax.security.auth.x500.X500Principal;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class CertificateIssuerName implements CertAttrSet<String> {
    public static final String DN_NAME = "dname";
    public static final String DN_PRINCIPAL = "x500principal";
    public static final String IDENT = "x509.info.issuer";
    public static final String NAME = "issuer";
    private X500Name dnName;
    private X500Principal dnPrincipal;

    public String getName() {
        return "issuer";
    }

    public CertificateIssuerName(X500Name x500Name) {
        this.dnName = x500Name;
    }

    public CertificateIssuerName(DerInputStream derInputStream) throws IOException {
        this.dnName = new X500Name(derInputStream);
    }

    public CertificateIssuerName(InputStream inputStream) throws IOException {
        this.dnName = new X500Name(new DerValue(inputStream));
    }

    public String toString() {
        X500Name x500Name = this.dnName;
        if (x500Name == null) {
            return "";
        }
        return x500Name.toString();
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        this.dnName.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (!(obj instanceof X500Name)) {
            throw new IOException("Attribute must be of type X500Name.");
        } else if (str.equalsIgnoreCase("dname")) {
            this.dnName = (X500Name) obj;
            this.dnPrincipal = null;
        } else {
            throw new IOException("Attribute name not recognized by CertAttrSet:CertificateIssuerName.");
        }
    }

    public Object get(String str) throws IOException {
        X500Name x500Name;
        if (str.equalsIgnoreCase("dname")) {
            return this.dnName;
        }
        if (str.equalsIgnoreCase("x500principal")) {
            if (this.dnPrincipal == null && (x500Name = this.dnName) != null) {
                this.dnPrincipal = x500Name.asX500Principal();
            }
            return this.dnPrincipal;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:CertificateIssuerName.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase("dname")) {
            this.dnName = null;
            this.dnPrincipal = null;
            return;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:CertificateIssuerName.");
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("dname");
        return attributeNameEnumeration.elements();
    }
}
