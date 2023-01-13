package javax.crypto.spec;

import java.math.BigInteger;
import java.security.spec.KeySpec;

public class DHPrivateKeySpec implements KeySpec {

    /* renamed from: g */
    private BigInteger f814g;

    /* renamed from: p */
    private BigInteger f815p;

    /* renamed from: x */
    private BigInteger f816x;

    public DHPrivateKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.f816x = bigInteger;
        this.f815p = bigInteger2;
        this.f814g = bigInteger3;
    }

    public BigInteger getX() {
        return this.f816x;
    }

    public BigInteger getP() {
        return this.f815p;
    }

    public BigInteger getG() {
        return this.f814g;
    }
}
