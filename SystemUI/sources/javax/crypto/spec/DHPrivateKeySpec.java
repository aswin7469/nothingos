package javax.crypto.spec;

import java.math.BigInteger;
import java.security.spec.KeySpec;

public class DHPrivateKeySpec implements KeySpec {

    /* renamed from: g */
    private BigInteger f816g;

    /* renamed from: p */
    private BigInteger f817p;

    /* renamed from: x */
    private BigInteger f818x;

    public DHPrivateKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.f818x = bigInteger;
        this.f817p = bigInteger2;
        this.f816g = bigInteger3;
    }

    public BigInteger getX() {
        return this.f818x;
    }

    public BigInteger getP() {
        return this.f817p;
    }

    public BigInteger getG() {
        return this.f816g;
    }
}
