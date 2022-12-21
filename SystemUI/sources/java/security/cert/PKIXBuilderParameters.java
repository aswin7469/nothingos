package java.security.cert;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Set;

public class PKIXBuilderParameters extends PKIXParameters {
    private int maxPathLength = 5;

    public PKIXBuilderParameters(Set<TrustAnchor> set, CertSelector certSelector) throws InvalidAlgorithmParameterException {
        super(set);
        setTargetCertConstraints(certSelector);
    }

    public PKIXBuilderParameters(KeyStore keyStore, CertSelector certSelector) throws KeyStoreException, InvalidAlgorithmParameterException {
        super(keyStore);
        setTargetCertConstraints(certSelector);
    }

    public void setMaxPathLength(int i) {
        if (i >= -1) {
            this.maxPathLength = i;
            return;
        }
        throw new InvalidParameterException("the maximum path length parameter can not be less than -1");
    }

    public int getMaxPathLength() {
        return this.maxPathLength;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[\n");
        sb.append(super.toString());
        sb.append("  Maximum Path Length: " + this.maxPathLength + "\n");
        sb.append("]\n");
        return sb.toString();
    }
}
