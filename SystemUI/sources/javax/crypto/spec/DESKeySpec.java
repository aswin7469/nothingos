package javax.crypto.spec;

import java.security.InvalidKeyException;
import java.security.spec.KeySpec;

public class DESKeySpec implements KeySpec {
    public static final int DES_KEY_LEN = 8;
    private static final byte[][] WEAK_KEYS = {new byte[]{1, 1, 1, 1, 1, 1, 1, 1}, new byte[]{-2, -2, -2, -2, -2, -2, -2, -2}, new byte[]{31, 31, 31, 31, 14, 14, 14, 14}, new byte[]{-32, -32, -32, -32, -15, -15, -15, -15}, new byte[]{1, -2, 1, -2, 1, -2, 1, -2}, new byte[]{31, -32, 31, -32, 14, -15, 14, -15}, new byte[]{1, -32, 1, -32, 1, -15, 1, -15}, new byte[]{31, -2, 31, -2, 14, -2, 14, -2}, new byte[]{1, 31, 1, 31, 1, 14, 1, 14}, new byte[]{-32, -2, -32, -2, -15, -2, -15, -2}, new byte[]{-2, 1, -2, 1, -2, 1, -2, 1}, new byte[]{-32, 31, -32, 31, -15, 14, -15, 14}, new byte[]{-32, 1, -32, 1, -15, 1, -15, 1}, new byte[]{-2, 31, -2, 31, -2, 14, -2, 14}, new byte[]{31, 1, 31, 1, 14, 1, 14, 1}, new byte[]{-2, -32, -2, -32, -2, -15, -2, -15}};
    private byte[] key;

    public DESKeySpec(byte[] bArr) throws InvalidKeyException {
        this(bArr, 0);
    }

    public DESKeySpec(byte[] bArr, int i) throws InvalidKeyException {
        if (bArr.length - i >= 8) {
            byte[] bArr2 = new byte[8];
            this.key = bArr2;
            System.arraycopy((Object) bArr, i, (Object) bArr2, 0, 8);
            return;
        }
        throw new InvalidKeyException("Wrong key size");
    }

    public byte[] getKey() {
        return (byte[]) this.key.clone();
    }

    public static boolean isParityAdjusted(byte[] bArr, int i) throws InvalidKeyException {
        if (bArr == null) {
            throw new InvalidKeyException("null key");
        } else if (bArr.length - i >= 8) {
            int i2 = 0;
            while (i2 < 8) {
                int i3 = i + 1;
                if ((Integer.bitCount(bArr[i] & 255) & 1) == 0) {
                    return false;
                }
                i2++;
                i = i3;
            }
            return true;
        } else {
            throw new InvalidKeyException("Wrong key size");
        }
    }

    public static boolean isWeak(byte[] bArr, int i) throws InvalidKeyException {
        if (bArr == null) {
            throw new InvalidKeyException("null key");
        } else if (bArr.length - i >= 8) {
            for (int i2 = 0; i2 < WEAK_KEYS.length; i2++) {
                boolean z = true;
                for (int i3 = 0; i3 < 8 && z; i3++) {
                    if (WEAK_KEYS[i2][i3] != bArr[i3 + i]) {
                        z = false;
                    }
                }
                if (z) {
                    return z;
                }
            }
            return false;
        } else {
            throw new InvalidKeyException("Wrong key size");
        }
    }
}
