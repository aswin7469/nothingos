package java.security.spec;

public class ECPublicKeySpec implements KeySpec {
    private ECParameterSpec params;

    /* renamed from: w */
    private ECPoint f222w;

    public ECPublicKeySpec(ECPoint eCPoint, ECParameterSpec eCParameterSpec) {
        if (eCPoint == null) {
            throw new NullPointerException("w is null");
        } else if (eCParameterSpec == null) {
            throw new NullPointerException("params is null");
        } else if (eCPoint != ECPoint.POINT_INFINITY) {
            this.f222w = eCPoint;
            this.params = eCParameterSpec;
        } else {
            throw new IllegalArgumentException("w is ECPoint.POINT_INFINITY");
        }
    }

    public ECPoint getW() {
        return this.f222w;
    }

    public ECParameterSpec getParams() {
        return this.params;
    }
}
