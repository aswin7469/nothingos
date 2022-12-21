package javax.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;

public class GCMParameterSpec implements AlgorithmParameterSpec {

    /* renamed from: iv */
    private byte[] f822iv;
    private int tLen;

    public GCMParameterSpec(int i, byte[] bArr) {
        if (bArr != null) {
            init(i, bArr, 0, bArr.length);
            return;
        }
        throw new IllegalArgumentException("src array is null");
    }

    public GCMParameterSpec(int i, byte[] bArr, int i2, int i3) {
        init(i, bArr, i2, i3);
    }

    private void init(int i, byte[] bArr, int i2, int i3) {
        if (i >= 0) {
            this.tLen = i;
            if (bArr == null || i3 < 0 || i2 < 0 || i3 > bArr.length - i2) {
                throw new IllegalArgumentException("Invalid buffer arguments");
            }
            byte[] bArr2 = new byte[i3];
            this.f822iv = bArr2;
            System.arraycopy((Object) bArr, i2, (Object) bArr2, 0, i3);
            return;
        }
        throw new IllegalArgumentException("Length argument is negative");
    }

    public int getTLen() {
        return this.tLen;
    }

    public byte[] getIV() {
        return (byte[]) this.f822iv.clone();
    }
}
