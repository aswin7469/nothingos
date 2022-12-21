package sun.security.x509;

import java.p026io.IOException;
import java.security.cert.PolicyQualifierInfo;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class PolicyInformation {

    /* renamed from: ID */
    public static final String f933ID = "id";
    public static final String NAME = "PolicyInformation";
    public static final String QUALIFIERS = "qualifiers";
    private CertificatePolicyId policyIdentifier;
    private Set<PolicyQualifierInfo> policyQualifiers;

    public String getName() {
        return NAME;
    }

    public PolicyInformation(CertificatePolicyId certificatePolicyId, Set<PolicyQualifierInfo> set) throws IOException {
        if (set != null) {
            this.policyQualifiers = new LinkedHashSet(set);
            this.policyIdentifier = certificatePolicyId;
            return;
        }
        throw new NullPointerException("policyQualifiers is null");
    }

    public PolicyInformation(DerValue derValue) throws IOException {
        if (derValue.tag == 48) {
            this.policyIdentifier = new CertificatePolicyId(derValue.data.getDerValue());
            if (derValue.data.available() != 0) {
                this.policyQualifiers = new LinkedHashSet();
                DerValue derValue2 = derValue.data.getDerValue();
                if (derValue2.tag != 48) {
                    throw new IOException("Invalid encoding of PolicyInformation");
                } else if (derValue2.data.available() != 0) {
                    while (derValue2.data.available() != 0) {
                        this.policyQualifiers.add(new PolicyQualifierInfo(derValue2.data.getDerValue().toByteArray()));
                    }
                } else {
                    throw new IOException("No data available in policyQualifiers");
                }
            } else {
                this.policyQualifiers = Collections.emptySet();
            }
        } else {
            throw new IOException("Invalid encoding of PolicyInformation");
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PolicyInformation)) {
            return false;
        }
        PolicyInformation policyInformation = (PolicyInformation) obj;
        if (!this.policyIdentifier.equals(policyInformation.getPolicyIdentifier())) {
            return false;
        }
        return this.policyQualifiers.equals(policyInformation.getPolicyQualifiers());
    }

    public int hashCode() {
        return ((this.policyIdentifier.hashCode() + 37) * 37) + this.policyQualifiers.hashCode();
    }

    public CertificatePolicyId getPolicyIdentifier() {
        return this.policyIdentifier;
    }

    public Set<PolicyQualifierInfo> getPolicyQualifiers() {
        return this.policyQualifiers;
    }

    public Object get(String str) throws IOException {
        if (str.equalsIgnoreCase("id")) {
            return this.policyIdentifier;
        }
        if (str.equalsIgnoreCase(QUALIFIERS)) {
            return this.policyQualifiers;
        }
        throw new IOException("Attribute name [" + str + "] not recognized by PolicyInformation.");
    }

    public void set(String str, Object obj) throws IOException {
        if (str.equalsIgnoreCase("id")) {
            if (obj instanceof CertificatePolicyId) {
                this.policyIdentifier = (CertificatePolicyId) obj;
                return;
            }
            throw new IOException("Attribute value must be instance of CertificatePolicyId.");
        } else if (!str.equalsIgnoreCase(QUALIFIERS)) {
            throw new IOException("Attribute name [" + str + "] not recognized by PolicyInformation");
        } else if (this.policyIdentifier == null) {
            throw new IOException("Attribute must have a CertificatePolicyIdentifier value before PolicyQualifierInfo can be set.");
        } else if (obj instanceof Set) {
            Set<PolicyQualifierInfo> set = (Set) obj;
            for (PolicyQualifierInfo policyQualifierInfo : set) {
                if (!(policyQualifierInfo instanceof PolicyQualifierInfo)) {
                    throw new IOException("Attribute value must be aSet of PolicyQualifierInfo objects.");
                }
            }
            this.policyQualifiers = set;
        } else {
            throw new IOException("Attribute value must be of type Set.");
        }
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase(QUALIFIERS)) {
            this.policyQualifiers = Collections.emptySet();
        } else if (str.equalsIgnoreCase("id")) {
            throw new IOException("Attribute ID may not be deleted from PolicyInformation.");
        } else {
            throw new IOException("Attribute name [" + str + "] not recognized by PolicyInformation.");
        }
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("id");
        attributeNameEnumeration.addElement(QUALIFIERS);
        return attributeNameEnumeration.elements();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("  [" + this.policyIdentifier.toString());
        sb.append(this.policyQualifiers + "  ]\n");
        return sb.toString();
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        this.policyIdentifier.encode(derOutputStream2);
        if (!this.policyQualifiers.isEmpty()) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            for (PolicyQualifierInfo encoded : this.policyQualifiers) {
                derOutputStream3.write(encoded.getEncoded());
            }
            derOutputStream2.write((byte) 48, derOutputStream3);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
    }
}
