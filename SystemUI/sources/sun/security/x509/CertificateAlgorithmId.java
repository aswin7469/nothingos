package sun.security.x509;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.util.Enumeration;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class CertificateAlgorithmId implements CertAttrSet<String> {
    public static final String ALGORITHM = "algorithm";
    public static final String IDENT = "x509.info.algorithmID";
    public static final String NAME = "algorithmID";
    private AlgorithmId algId;

    public String getName() {
        return "algorithmID";
    }

    public CertificateAlgorithmId(AlgorithmId algorithmId) {
        this.algId = algorithmId;
    }

    public CertificateAlgorithmId(DerInputStream derInputStream) throws IOException {
        this.algId = AlgorithmId.parse(derInputStream.getDerValue());
    }

    public CertificateAlgorithmId(InputStream inputStream) throws IOException {
        this.algId = AlgorithmId.parse(new DerValue(inputStream));
    }

    public String toString() {
        if (this.algId == null) {
            return "";
        }
        return this.algId.toString() + ", OID = " + this.algId.getOID().toString() + "\n";
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        this.algId.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (!(obj instanceof AlgorithmId)) {
            throw new IOException("Attribute must be of type AlgorithmId.");
        } else if (str.equalsIgnoreCase("algorithm")) {
            this.algId = (AlgorithmId) obj;
        } else {
            throw new IOException("Attribute name not recognized by CertAttrSet:CertificateAlgorithmId.");
        }
    }

    public AlgorithmId get(String str) throws IOException {
        if (str.equalsIgnoreCase("algorithm")) {
            return this.algId;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:CertificateAlgorithmId.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase("algorithm")) {
            this.algId = null;
            return;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:CertificateAlgorithmId.");
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("algorithm");
        return attributeNameEnumeration.elements();
    }
}
