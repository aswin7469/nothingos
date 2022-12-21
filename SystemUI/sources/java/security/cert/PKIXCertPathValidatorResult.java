package java.security.cert;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.security.PublicKey;

public class PKIXCertPathValidatorResult implements CertPathValidatorResult {
    private PolicyNode policyTree;
    private PublicKey subjectPublicKey;
    private TrustAnchor trustAnchor;

    public PKIXCertPathValidatorResult(TrustAnchor trustAnchor2, PolicyNode policyNode, PublicKey publicKey) {
        if (publicKey == null) {
            throw new NullPointerException("subjectPublicKey must be non-null");
        } else if (trustAnchor2 != null) {
            this.trustAnchor = trustAnchor2;
            this.policyTree = policyNode;
            this.subjectPublicKey = publicKey;
        } else {
            throw new NullPointerException("trustAnchor must be non-null");
        }
    }

    public TrustAnchor getTrustAnchor() {
        return this.trustAnchor;
    }

    public PolicyNode getPolicyTree() {
        return this.policyTree;
    }

    public PublicKey getPublicKey() {
        return this.subjectPublicKey;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("PKIXCertPathValidatorResult: [\n");
        sb.append("  Trust Anchor: " + this.trustAnchor.toString() + "\n");
        sb.append("  Policy Tree: " + String.valueOf((Object) this.policyTree) + "\n");
        sb.append("  Subject Public Key: " + this.subjectPublicKey + "\n");
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }
}
