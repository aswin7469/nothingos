package java.security.spec;

import java.math.BigInteger;

public class ECPrivateKeySpec implements KeySpec {
    private ECParameterSpec params;

    /* renamed from: s */
    private BigInteger f221s;

    public ECPrivateKeySpec(BigInteger bigInteger, ECParameterSpec eCParameterSpec) {
        if (bigInteger == null) {
            throw new NullPointerException("s is null");
        } else if (eCParameterSpec != null) {
            this.f221s = bigInteger;
            this.params = eCParameterSpec;
        } else {
            throw new NullPointerException("params is null");
        }
    }

    public BigInteger getS() {
        return this.f221s;
    }

    public ECParameterSpec getParams() {
        return this.params;
    }
}
