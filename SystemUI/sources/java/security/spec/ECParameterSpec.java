package java.security.spec;

import java.math.BigInteger;

public class ECParameterSpec implements AlgorithmParameterSpec {
    private final EllipticCurve curve;
    private String curveName;

    /* renamed from: g */
    private final ECPoint f216g;

    /* renamed from: h */
    private final int f217h;

    /* renamed from: n */
    private final BigInteger f218n;

    public ECParameterSpec(EllipticCurve ellipticCurve, ECPoint eCPoint, BigInteger bigInteger, int i) {
        if (ellipticCurve == null) {
            throw new NullPointerException("curve is null");
        } else if (eCPoint == null) {
            throw new NullPointerException("g is null");
        } else if (bigInteger == null) {
            throw new NullPointerException("n is null");
        } else if (bigInteger.signum() != 1) {
            throw new IllegalArgumentException("n is not positive");
        } else if (i > 0) {
            this.curve = ellipticCurve;
            this.f216g = eCPoint;
            this.f218n = bigInteger;
            this.f217h = i;
        } else {
            throw new IllegalArgumentException("h is not positive");
        }
    }

    public EllipticCurve getCurve() {
        return this.curve;
    }

    public ECPoint getGenerator() {
        return this.f216g;
    }

    public BigInteger getOrder() {
        return this.f218n;
    }

    public int getCofactor() {
        return this.f217h;
    }

    public void setCurveName(String str) {
        this.curveName = str;
    }

    public String getCurveName() {
        return this.curveName;
    }
}
