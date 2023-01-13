package javax.crypto.spec;

import java.math.BigInteger;
import java.security.spec.KeySpec;

public class DHPublicKeySpec implements KeySpec {

    /* renamed from: g */
    private BigInteger f817g;

    /* renamed from: p */
    private BigInteger f818p;

    /* renamed from: y */
    private BigInteger f819y;

    public DHPublicKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.f819y = bigInteger;
        this.f818p = bigInteger2;
        this.f817g = bigInteger3;
    }

    public BigInteger getY() {
        return this.f819y;
    }

    public BigInteger getP() {
        return this.f818p;
    }

    public BigInteger getG() {
        return this.f817g;
    }
}
