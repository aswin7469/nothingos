package javax.crypto.spec;

import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

public class RC5ParameterSpec implements AlgorithmParameterSpec {

    /* renamed from: iv */
    private byte[] f826iv;
    private int rounds;
    private int version;
    private int wordSize;

    public RC5ParameterSpec(int i, int i2, int i3) {
        this.f826iv = null;
        this.version = i;
        this.rounds = i2;
        this.wordSize = i3;
    }

    public RC5ParameterSpec(int i, int i2, int i3, byte[] bArr) {
        this(i, i2, i3, bArr, 0);
    }

    public RC5ParameterSpec(int i, int i2, int i3, byte[] bArr, int i4) {
        this.f826iv = null;
        this.version = i;
        this.rounds = i2;
        this.wordSize = i3;
        if (bArr != null) {
            int i5 = (i3 / 8) * 2;
            if (bArr.length - i4 >= i5) {
                byte[] bArr2 = new byte[i5];
                this.f826iv = bArr2;
                System.arraycopy((Object) bArr, i4, (Object) bArr2, 0, i5);
                return;
            }
            throw new IllegalArgumentException("IV too short");
        }
        throw new IllegalArgumentException("IV missing");
    }

    public int getVersion() {
        return this.version;
    }

    public int getRounds() {
        return this.rounds;
    }

    public int getWordSize() {
        return this.wordSize;
    }

    public byte[] getIV() {
        byte[] bArr = this.f826iv;
        if (bArr == null) {
            return null;
        }
        return (byte[]) bArr.clone();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RC5ParameterSpec)) {
            return false;
        }
        RC5ParameterSpec rC5ParameterSpec = (RC5ParameterSpec) obj;
        if (this.version == rC5ParameterSpec.version && this.rounds == rC5ParameterSpec.rounds && this.wordSize == rC5ParameterSpec.wordSize && Arrays.equals(this.f826iv, rC5ParameterSpec.f826iv)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        if (this.f826iv != null) {
            int i2 = 1;
            while (true) {
                byte[] bArr = this.f826iv;
                if (i2 >= bArr.length) {
                    break;
                }
                i += bArr[i2] * i2;
                i2++;
            }
        }
        return i + this.version + this.rounds + this.wordSize;
    }
}
