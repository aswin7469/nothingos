package java.security.spec;

import java.math.BigInteger;

public class EllipticCurve {

    /* renamed from: a */
    private final BigInteger f224a;

    /* renamed from: b */
    private final BigInteger f225b;
    private final ECField field;
    private final byte[] seed;

    private static void checkValidity(ECField eCField, BigInteger bigInteger, String str) {
        if (eCField instanceof ECFieldFp) {
            if (((ECFieldFp) eCField).getP().compareTo(bigInteger) != 1) {
                throw new IllegalArgumentException(str + " is too large");
            } else if (bigInteger.signum() < 0) {
                throw new IllegalArgumentException(str + " is negative");
            }
        } else if ((eCField instanceof ECFieldF2m) && bigInteger.bitLength() > ((ECFieldF2m) eCField).getM()) {
            throw new IllegalArgumentException(str + " is too large");
        }
    }

    public EllipticCurve(ECField eCField, BigInteger bigInteger, BigInteger bigInteger2) {
        this(eCField, bigInteger, bigInteger2, (byte[]) null);
    }

    public EllipticCurve(ECField eCField, BigInteger bigInteger, BigInteger bigInteger2, byte[] bArr) {
        if (eCField == null) {
            throw new NullPointerException("field is null");
        } else if (bigInteger == null) {
            throw new NullPointerException("first coefficient is null");
        } else if (bigInteger2 != null) {
            checkValidity(eCField, bigInteger, "first coefficient");
            checkValidity(eCField, bigInteger2, "second coefficient");
            this.field = eCField;
            this.f224a = bigInteger;
            this.f225b = bigInteger2;
            if (bArr != null) {
                this.seed = (byte[]) bArr.clone();
            } else {
                this.seed = null;
            }
        } else {
            throw new NullPointerException("second coefficient is null");
        }
    }

    public ECField getField() {
        return this.field;
    }

    public BigInteger getA() {
        return this.f224a;
    }

    public BigInteger getB() {
        return this.f225b;
    }

    public byte[] getSeed() {
        byte[] bArr = this.seed;
        if (bArr == null) {
            return null;
        }
        return (byte[]) bArr.clone();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EllipticCurve)) {
            return false;
        }
        EllipticCurve ellipticCurve = (EllipticCurve) obj;
        if (!this.field.equals(ellipticCurve.field) || !this.f224a.equals(ellipticCurve.f224a) || !this.f225b.equals(ellipticCurve.f225b)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.field.hashCode() << (((this.f224a.hashCode() << 4) + 6) + (this.f225b.hashCode() << 2));
    }
}
