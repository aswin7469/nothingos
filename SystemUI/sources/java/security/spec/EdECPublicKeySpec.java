package java.security.spec;

import java.util.Objects;

public final class EdECPublicKeySpec implements KeySpec {
    private final NamedParameterSpec params;
    private final EdECPoint point;

    public EdECPublicKeySpec(NamedParameterSpec namedParameterSpec, EdECPoint edECPoint) {
        Objects.requireNonNull(namedParameterSpec, "params must not be null");
        Objects.requireNonNull(edECPoint, "point must not be null");
        this.params = namedParameterSpec;
        this.point = edECPoint;
    }

    public NamedParameterSpec getParams() {
        return this.params;
    }

    public EdECPoint getPoint() {
        return this.point;
    }
}
