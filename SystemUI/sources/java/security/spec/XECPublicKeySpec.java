package java.security.spec;

import java.math.BigInteger;
import java.util.Objects;

public class XECPublicKeySpec implements KeySpec {
    private final AlgorithmParameterSpec params;

    /* renamed from: u */
    private final BigInteger f228u;

    public XECPublicKeySpec(AlgorithmParameterSpec algorithmParameterSpec, BigInteger bigInteger) {
        Objects.requireNonNull(algorithmParameterSpec, "params must not be null");
        Objects.requireNonNull(bigInteger, "u must not be null");
        this.params = algorithmParameterSpec;
        this.f228u = bigInteger;
    }

    public AlgorithmParameterSpec getParams() {
        return this.params;
    }

    public BigInteger getU() {
        return this.f228u;
    }
}
