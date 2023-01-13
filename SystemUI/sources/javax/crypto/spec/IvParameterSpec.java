package javax.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;

public class IvParameterSpec implements AlgorithmParameterSpec {

    /* renamed from: iv */
    private byte[] f821iv;

    public IvParameterSpec(byte[] bArr) {
        this(bArr, 0, bArr.length);
    }

    public IvParameterSpec(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new IllegalArgumentException("IV missing");
        } else if (i < 0) {
            throw new ArrayIndexOutOfBoundsException("offset is negative");
        } else if (i2 < 0) {
            throw new ArrayIndexOutOfBoundsException("len is negative");
        } else if (bArr.length - i >= i2) {
            byte[] bArr2 = new byte[i2];
            this.f821iv = bArr2;
            System.arraycopy((Object) bArr, i, (Object) bArr2, 0, i2);
        } else {
            throw new IllegalArgumentException("IV buffer too short for given offset/length combination");
        }
    }

    public byte[] getIV() {
        return (byte[]) this.f821iv.clone();
    }
}
