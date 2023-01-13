package javax.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

public class RC2ParameterSpec implements AlgorithmParameterSpec {
    private int effectiveKeyBits;

    /* renamed from: iv */
    private byte[] f823iv;

    public RC2ParameterSpec(int i) {
        this.f823iv = null;
        this.effectiveKeyBits = i;
    }

    public RC2ParameterSpec(int i, byte[] bArr) {
        this(i, bArr, 0);
    }

    public RC2ParameterSpec(int i, byte[] bArr, int i2) {
        this.f823iv = null;
        this.effectiveKeyBits = i;
        if (bArr == null) {
            throw new IllegalArgumentException("IV missing");
        } else if (bArr.length - i2 >= 8) {
            byte[] bArr2 = new byte[8];
            this.f823iv = bArr2;
            System.arraycopy((Object) bArr, i2, (Object) bArr2, 0, 8);
        } else {
            throw new IllegalArgumentException("IV too short");
        }
    }

    public int getEffectiveKeyBits() {
        return this.effectiveKeyBits;
    }

    public byte[] getIV() {
        byte[] bArr = this.f823iv;
        if (bArr == null) {
            return null;
        }
        return (byte[]) bArr.clone();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RC2ParameterSpec)) {
            return false;
        }
        RC2ParameterSpec rC2ParameterSpec = (RC2ParameterSpec) obj;
        if (this.effectiveKeyBits != rC2ParameterSpec.effectiveKeyBits || !Arrays.equals(this.f823iv, rC2ParameterSpec.f823iv)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int i = 0;
        if (this.f823iv != null) {
            int i2 = 1;
            while (true) {
                byte[] bArr = this.f823iv;
                if (i2 >= bArr.length) {
                    break;
                }
                i += bArr[i2] * i2;
                i2++;
            }
        }
        return i + this.effectiveKeyBits;
    }
}
