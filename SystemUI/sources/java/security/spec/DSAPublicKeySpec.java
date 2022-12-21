package java.security.spec;

import java.math.BigInteger;

public class DSAPublicKeySpec implements KeySpec {

    /* renamed from: g */
    private BigInteger f208g;

    /* renamed from: p */
    private BigInteger f209p;

    /* renamed from: q */
    private BigInteger f210q;

    /* renamed from: y */
    private BigInteger f211y;

    public DSAPublicKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        this.f211y = bigInteger;
        this.f209p = bigInteger2;
        this.f210q = bigInteger3;
        this.f208g = bigInteger4;
    }

    public BigInteger getY() {
        return this.f211y;
    }

    public BigInteger getP() {
        return this.f209p;
    }

    public BigInteger getQ() {
        return this.f210q;
    }

    public BigInteger getG() {
        return this.f208g;
    }
}
