package sun.security.x509;

import java.p026io.IOException;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class CertificatePolicyId {

    /* renamed from: id */
    private ObjectIdentifier f929id;

    public CertificatePolicyId(ObjectIdentifier objectIdentifier) {
        this.f929id = objectIdentifier;
    }

    public CertificatePolicyId(DerValue derValue) throws IOException {
        this.f929id = derValue.getOID();
    }

    public ObjectIdentifier getIdentifier() {
        return this.f929id;
    }

    public String toString() {
        return "CertificatePolicyId: [" + this.f929id.toString() + "]\n";
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putOID(this.f929id);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CertificatePolicyId) {
            return this.f929id.equals((Object) ((CertificatePolicyId) obj).getIdentifier());
        }
        return false;
    }

    public int hashCode() {
        return this.f929id.hashCode();
    }
}
