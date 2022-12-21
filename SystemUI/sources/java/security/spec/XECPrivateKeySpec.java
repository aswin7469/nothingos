package java.security.spec;

import java.util.Objects;

public class XECPrivateKeySpec implements KeySpec {
    private final AlgorithmParameterSpec params;
    private final byte[] scalar;

    public XECPrivateKeySpec(AlgorithmParameterSpec algorithmParameterSpec, byte[] bArr) {
        Objects.requireNonNull(algorithmParameterSpec, "params must not be null");
        Objects.requireNonNull(bArr, "scalar must not be null");
        this.params = algorithmParameterSpec;
        this.scalar = (byte[]) bArr.clone();
    }

    public AlgorithmParameterSpec getParams() {
        return this.params;
    }

    public byte[] getScalar() {
        return (byte[]) this.scalar.clone();
    }
}
