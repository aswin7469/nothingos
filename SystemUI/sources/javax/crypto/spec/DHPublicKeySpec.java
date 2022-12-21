package javax.crypto.spec;

import java.math.BigInteger;
import java.security.spec.KeySpec;

public class DHPublicKeySpec implements KeySpec {

    /* renamed from: g */
    private BigInteger f819g;

    /* renamed from: p */
    private BigInteger f820p;

    /* renamed from: y */
    private BigInteger f821y;

    public DHPublicKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.f821y = bigInteger;
        this.f820p = bigInteger2;
        this.f819g = bigInteger3;
    }

    public BigInteger getY() {
        return this.f821y;
    }

    public BigInteger getP() {
        return this.f820p;
    }

    public BigInteger getG() {
        return this.f819g;
    }
}
