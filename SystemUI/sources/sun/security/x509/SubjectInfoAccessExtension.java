package sun.security.x509;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class SubjectInfoAccessExtension extends Extension implements CertAttrSet<String> {
    public static final String DESCRIPTIONS = "descriptions";
    public static final String IDENT = "x509.info.extensions.SubjectInfoAccess";
    public static final String NAME = "SubjectInfoAccess";
    private List<AccessDescription> accessDescriptions;

    public String getName() {
        return NAME;
    }

    public SubjectInfoAccessExtension(List<AccessDescription> list) throws IOException {
        this.extensionId = PKIXExtensions.SubjectInfoAccess_Id;
        this.critical = false;
        this.accessDescriptions = list;
        encodeThis();
    }

    public SubjectInfoAccessExtension(Boolean bool, Object obj) throws IOException {
        this.extensionId = PKIXExtensions.SubjectInfoAccess_Id;
        this.critical = bool.booleanValue();
        if (obj instanceof byte[]) {
            this.extensionValue = (byte[]) obj;
            DerValue derValue = new DerValue(this.extensionValue);
            if (derValue.tag == 48) {
                this.accessDescriptions = new ArrayList();
                while (derValue.data.available() != 0) {
                    this.accessDescriptions.add(new AccessDescription(derValue.data.getDerValue()));
                }
                return;
            }
            throw new IOException("Invalid encoding for SubjectInfoAccessExtension.");
        }
        throw new IOException("Illegal argument type");
    }

    public List<AccessDescription> getAccessDescriptions() {
        return this.accessDescriptions;
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.SubjectInfoAccess_Id;
            this.critical = false;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (!str.equalsIgnoreCase("descriptions")) {
            throw new IOException("Attribute name [" + str + "] not recognized by CertAttrSet:SubjectInfoAccessExtension.");
        } else if (obj instanceof List) {
            this.accessDescriptions = (List) obj;
            encodeThis();
        } else {
            throw new IOException("Attribute value should be of type List.");
        }
    }

    public List<AccessDescription> get(String str) throws IOException {
        if (str.equalsIgnoreCase("descriptions")) {
            return this.accessDescriptions;
        }
        throw new IOException("Attribute name [" + str + "] not recognized by CertAttrSet:SubjectInfoAccessExtension.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase("descriptions")) {
            this.accessDescriptions = Collections.emptyList();
            encodeThis();
            return;
        }
        throw new IOException("Attribute name [" + str + "] not recognized by CertAttrSet:SubjectInfoAccessExtension.");
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("descriptions");
        return attributeNameEnumeration.elements();
    }

    private void encodeThis() throws IOException {
        if (this.accessDescriptions.isEmpty()) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        for (AccessDescription encode : this.accessDescriptions) {
            encode.encode(derOutputStream);
        }
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.write((byte) 48, derOutputStream);
        this.extensionValue = derOutputStream2.toByteArray();
    }

    public String toString() {
        return super.toString() + "SubjectInfoAccess [\n  " + this.accessDescriptions + "\n]\n";
    }
}
