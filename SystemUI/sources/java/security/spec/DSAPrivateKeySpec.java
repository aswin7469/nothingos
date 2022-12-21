package java.security.spec;

import java.math.BigInteger;

public class DSAPrivateKeySpec implements KeySpec {

    /* renamed from: g */
    private BigInteger f204g;

    /* renamed from: p */
    private BigInteger f205p;

    /* renamed from: q */
    private BigInteger f206q;

    /* renamed from: x */
    private BigInteger f207x;

    public DSAPrivateKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        this.f207x = bigInteger;
        this.f205p = bigInteger2;
        this.f206q = bigInteger3;
        this.f204g = bigInteger4;
    }

    public BigInteger getX() {
        return this.f207x;
    }

    public BigInteger getP() {
        return this.f205p;
    }

    public BigInteger getQ() {
        return this.f206q;
    }

    public BigInteger getG() {
        return this.f204g;
    }
}
