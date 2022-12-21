package sun.security.x509;

import java.p026io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class CertificatePolicySet {
    private final Vector<CertificatePolicyId> ids;

    public CertificatePolicySet(Vector<CertificatePolicyId> vector) {
        this.ids = vector;
    }

    public CertificatePolicySet(DerInputStream derInputStream) throws IOException {
        this.ids = new Vector<>();
        DerValue[] sequence = derInputStream.getSequence(5);
        for (DerValue certificatePolicyId : sequence) {
            this.ids.addElement(new CertificatePolicyId(certificatePolicyId));
        }
    }

    public String toString() {
        return "CertificatePolicySet:[\n" + this.ids.toString() + "]\n";
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        for (int i = 0; i < this.ids.size(); i++) {
            this.ids.elementAt(i).encode(derOutputStream2);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
    }

    public List<CertificatePolicyId> getCertPolicyIds() {
        return Collections.unmodifiableList(this.ids);
    }
}
