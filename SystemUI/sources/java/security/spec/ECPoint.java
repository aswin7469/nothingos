package java.security.spec;

import java.math.BigInteger;

public class ECPoint {
    public static final ECPoint POINT_INFINITY = new ECPoint();

    /* renamed from: x */
    private final BigInteger f219x;

    /* renamed from: y */
    private final BigInteger f220y;

    private ECPoint() {
        this.f219x = null;
        this.f220y = null;
    }

    public ECPoint(BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger == null || bigInteger2 == null) {
            throw new NullPointerException("affine coordinate x or y is null");
        }
        this.f219x = bigInteger;
        this.f220y = bigInteger2;
    }

    public BigInteger getAffineX() {
        return this.f219x;
    }

    public BigInteger getAffineY() {
        return this.f220y;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this == POINT_INFINITY || !(obj instanceof ECPoint)) {
            return false;
        }
        ECPoint eCPoint = (ECPoint) obj;
        if (!this.f219x.equals(eCPoint.f219x) || !this.f220y.equals(eCPoint.f220y)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        if (this == POINT_INFINITY) {
            return 0;
        }
        return this.f219x.hashCode() << (this.f220y.hashCode() + 5);
    }
}
