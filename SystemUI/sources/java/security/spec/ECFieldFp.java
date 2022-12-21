package java.security.spec;

import java.math.BigInteger;

public class ECFieldFp implements ECField {

    /* renamed from: p */
    private BigInteger f215p;

    public ECFieldFp(BigInteger bigInteger) {
        if (bigInteger.signum() == 1) {
            this.f215p = bigInteger;
            return;
        }
        throw new IllegalArgumentException("p is not positive");
    }

    public int getFieldSize() {
        return this.f215p.bitLength();
    }

    public BigInteger getP() {
        return this.f215p;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ECFieldFp) {
            return this.f215p.equals(((ECFieldFp) obj).f215p);
        }
        return false;
    }

    public int hashCode() {
        return this.f215p.hashCode();
    }
}
