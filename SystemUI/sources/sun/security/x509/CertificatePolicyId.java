package sun.security.x509;

import java.p026io.IOException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class CertificatePolicyId {

    /* renamed from: id */
    private ObjectIdentifier f927id;

    public CertificatePolicyId(ObjectIdentifier objectIdentifier) {
        this.f927id = objectIdentifier;
    }

    public CertificatePolicyId(DerValue derValue) throws IOException {
        this.f927id = derValue.getOID();
    }

    public ObjectIdentifier getIdentifier() {
        return this.f927id;
    }

    public String toString() {
        return "CertificatePolicyId: [" + this.f927id.toString() + "]\n";
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putOID(this.f927id);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CertificatePolicyId) {
            return this.f927id.equals((Object) ((CertificatePolicyId) obj).getIdentifier());
        }
        return false;
    }

    public int hashCode() {
        return this.f927id.hashCode();
    }
}
