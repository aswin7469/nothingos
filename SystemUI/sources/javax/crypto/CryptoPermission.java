package javax.crypto;

import java.security.Permission;
import java.security.spec.AlgorithmParameterSpec;

class CryptoPermission extends Permission {
    static final String ALG_NAME_WILDCARD = null;

    public String getActions() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public final String getAlgorithm() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public final AlgorithmParameterSpec getAlgorithmParameterSpec() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public final boolean getCheckParam() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public final String getExemptionMechanism() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public final int getMaxKeySize() {
        return Integer.MAX_VALUE;
    }

    public boolean implies(Permission permission) {
        return true;
    }

    CryptoPermission(String str) {
        super("");
    }

    CryptoPermission(String str, int i) {
        super("");
    }

    CryptoPermission(String str, int i, AlgorithmParameterSpec algorithmParameterSpec) {
        super("");
    }

    CryptoPermission(String str, String str2) {
        super("");
    }

    CryptoPermission(String str, int i, String str2) {
        super("");
    }

    CryptoPermission(String str, int i, AlgorithmParameterSpec algorithmParameterSpec, String str2) {
        super("");
    }
}
