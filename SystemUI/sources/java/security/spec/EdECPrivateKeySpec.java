package java.security.spec;

import java.util.Objects;

public final class EdECPrivateKeySpec implements KeySpec {
    private final byte[] bytes;
    private final NamedParameterSpec params;

    public EdECPrivateKeySpec(NamedParameterSpec namedParameterSpec, byte[] bArr) {
        Objects.requireNonNull(namedParameterSpec, "params must not be null");
        Objects.requireNonNull(bArr, "bytes must not be null");
        this.params = namedParameterSpec;
        this.bytes = (byte[]) bArr.clone();
    }

    public NamedParameterSpec getParams() {
        return this.params;
    }

    public byte[] getBytes() {
        return (byte[]) this.bytes.clone();
    }
}
