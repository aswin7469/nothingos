package javax.crypto.spec;

import java.security.InvalidKeyException;
import java.security.spec.KeySpec;

public class DESedeKeySpec implements KeySpec {
    public static final int DES_EDE_KEY_LEN = 24;
    private byte[] key;

    public DESedeKeySpec(byte[] bArr) throws InvalidKeyException {
        this(bArr, 0);
    }

    public DESedeKeySpec(byte[] bArr, int i) throws InvalidKeyException {
        if (bArr.length - i >= 24) {
            byte[] bArr2 = new byte[24];
            this.key = bArr2;
            System.arraycopy((Object) bArr, i, (Object) bArr2, 0, 24);
            return;
        }
        throw new InvalidKeyException("Wrong key size");
    }

    public byte[] getKey() {
        return (byte[]) this.key.clone();
    }

    public static boolean isParityAdjusted(byte[] bArr, int i) throws InvalidKeyException {
        if (bArr.length - i >= 24) {
            return DESKeySpec.isParityAdjusted(bArr, i) && DESKeySpec.isParityAdjusted(bArr, i + 8) && DESKeySpec.isParityAdjusted(bArr, i + 16);
        }
        throw new InvalidKeyException("Wrong key size");
    }
}
