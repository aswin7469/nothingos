package java.security.spec;

import java.math.BigInteger;
import java.util.Objects;

public final class EdECPoint {
    private final boolean xOdd;

    /* renamed from: y */
    private final BigInteger f223y;

    public EdECPoint(boolean z, BigInteger bigInteger) {
        Objects.requireNonNull(bigInteger, "y must not be null");
        this.xOdd = z;
        this.f223y = bigInteger;
    }

    public boolean isXOdd() {
        return this.xOdd;
    }

    public BigInteger getY() {
        return this.f223y;
    }
}
