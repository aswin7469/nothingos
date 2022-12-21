package sun.security.x509;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class PolicyMappingsExtension extends Extension implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.extensions.PolicyMappings";
    public static final String MAP = "map";
    public static final String NAME = "PolicyMappings";
    private List<CertificatePolicyMap> maps;

    public String getName() {
        return NAME;
    }

    private void encodeThis() throws IOException {
        List<CertificatePolicyMap> list = this.maps;
        if (list == null || list.isEmpty()) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        for (CertificatePolicyMap encode : this.maps) {
            encode.encode(derOutputStream2);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public PolicyMappingsExtension(List<CertificatePolicyMap> list) throws IOException {
        this.maps = list;
        this.extensionId = PKIXExtensions.PolicyMappings_Id;
        this.critical = false;
        encodeThis();
    }

    public PolicyMappingsExtension() {
        this.extensionId = PKIXExtensions.KeyUsage_Id;
        this.critical = false;
        this.maps = Collections.emptyList();
    }

    public PolicyMappingsExtension(Boolean bool, Object obj) throws IOException {
        this.extensionId = PKIXExtensions.PolicyMappings_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        DerValue derValue = new DerValue(this.extensionValue);
        if (derValue.tag == 48) {
            this.maps = new ArrayList();
            while (derValue.data.available() != 0) {
                this.maps.add(new CertificatePolicyMap(derValue.data.getDerValue()));
            }
            return;
        }
        throw new IOException("Invalid encoding for PolicyMappingsExtension.");
    }

    public String toString() {
        if (this.maps == null) {
            return "";
        }
        return super.toString() + "PolicyMappings [\n" + this.maps.toString() + "]\n";
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.PolicyMappings_Id;
            this.critical = false;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (!str.equalsIgnoreCase(MAP)) {
            throw new IOException("Attribute name not recognized by CertAttrSet:PolicyMappingsExtension.");
        } else if (obj instanceof List) {
            this.maps = (List) obj;
            encodeThis();
        } else {
            throw new IOException("Attribute value should be of type List.");
        }
    }

    public List<CertificatePolicyMap> get(String str) throws IOException {
        if (str.equalsIgnoreCase(MAP)) {
            return this.maps;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:PolicyMappingsExtension.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase(MAP)) {
            this.maps = null;
            encodeThis();
            return;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:PolicyMappingsExtension.");
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(MAP);
        return attributeNameEnumeration.elements();
    }
}
