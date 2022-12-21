package javax.crypto;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

final class NullCipherSpi extends CipherSpi {
    /* access modifiers changed from: protected */
    public int engineGetBlockSize() {
        return 1;
    }

    /* access modifiers changed from: protected */
    public byte[] engineGetIV() {
        return new byte[8];
    }

    /* access modifiers changed from: protected */
    public int engineGetKeySize(Key key) {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int engineGetOutputSize(int i) {
        return i;
    }

    /* access modifiers changed from: protected */
    public AlgorithmParameters engineGetParameters() {
        return null;
    }

    /* access modifiers changed from: protected */
    public void engineInit(int i, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) {
    }

    /* access modifiers changed from: protected */
    public void engineInit(int i, Key key, SecureRandom secureRandom) {
    }

    /* access modifiers changed from: protected */
    public void engineInit(int i, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
    }

    public void engineSetMode(String str) {
    }

    public void engineSetPadding(String str) {
    }

    protected NullCipherSpi() {
    }

    /* access modifiers changed from: protected */
    public byte[] engineUpdate(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return null;
        }
        byte[] bArr2 = new byte[i2];
        System.arraycopy((Object) bArr, i, (Object) bArr2, 0, i2);
        return bArr2;
    }

    /* access modifiers changed from: protected */
    public int engineUpdate(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        if (bArr == null) {
            return 0;
        }
        System.arraycopy((Object) bArr, i, (Object) bArr2, i3, i2);
        return i2;
    }

    /* access modifiers changed from: protected */
    public byte[] engineDoFinal(byte[] bArr, int i, int i2) {
        return engineUpdate(bArr, i, i2);
    }

    /* access modifiers changed from: protected */
    public int engineDoFinal(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        return engineUpdate(bArr, i, i2, bArr2, i3);
    }
}
