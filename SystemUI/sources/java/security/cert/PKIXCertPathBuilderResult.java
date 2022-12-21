package java.security.cert;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.security.PublicKey;

public class PKIXCertPathBuilderResult extends PKIXCertPathValidatorResult implements CertPathBuilderResult {
    private CertPath certPath;

    public PKIXCertPathBuilderResult(CertPath certPath2, TrustAnchor trustAnchor, PolicyNode policyNode, PublicKey publicKey) {
        super(trustAnchor, policyNode, publicKey);
        if (certPath2 != null) {
            this.certPath = certPath2;
            return;
        }
        throw new NullPointerException("certPath must be non-null");
    }

    public CertPath getCertPath() {
        return this.certPath;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("PKIXCertPathBuilderResult: [\n");
        sb.append("  Certification Path: " + this.certPath + "\n");
        sb.append("  Trust Anchor: " + getTrustAnchor().toString() + "\n");
        sb.append("  Policy Tree: " + String.valueOf((Object) getPolicyTree()) + "\n");
        sb.append("  Subject Public Key: " + getPublicKey() + "\n");
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }
}
