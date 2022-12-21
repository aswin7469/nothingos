package java.security;

import java.nio.ByteBuffer;
import java.security.spec.AlgorithmParameterSpec;
import sun.security.jca.JCAUtil;

public abstract class SignatureSpi {
    protected SecureRandom appRandom = null;

    /* access modifiers changed from: protected */
    @Deprecated
    public abstract Object engineGetParameter(String str) throws InvalidParameterException;

    /* access modifiers changed from: protected */
    public abstract void engineInitSign(PrivateKey privateKey) throws InvalidKeyException;

    /* access modifiers changed from: protected */
    public abstract void engineInitVerify(PublicKey publicKey) throws InvalidKeyException;

    /* access modifiers changed from: protected */
    @Deprecated
    public abstract void engineSetParameter(String str, Object obj) throws InvalidParameterException;

    /* access modifiers changed from: protected */
    public abstract byte[] engineSign() throws SignatureException;

    /* access modifiers changed from: protected */
    public abstract void engineUpdate(byte b) throws SignatureException;

    /* access modifiers changed from: protected */
    public abstract void engineUpdate(byte[] bArr, int i, int i2) throws SignatureException;

    /* access modifiers changed from: protected */
    public abstract boolean engineVerify(byte[] bArr) throws SignatureException;

    /* access modifiers changed from: protected */
    public void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) throws InvalidKeyException {
        this.appRandom = secureRandom;
        engineInitSign(privateKey);
    }

    /* access modifiers changed from: protected */
    public void engineUpdate(ByteBuffer byteBuffer) {
        if (byteBuffer.hasRemaining()) {
            try {
                if (byteBuffer.hasArray()) {
                    byte[] array = byteBuffer.array();
                    int arrayOffset = byteBuffer.arrayOffset();
                    int position = byteBuffer.position();
                    int limit = byteBuffer.limit();
                    engineUpdate(array, arrayOffset + position, limit - position);
                    byteBuffer.position(limit);
                    return;
                }
                int remaining = byteBuffer.remaining();
                int tempArraySize = JCAUtil.getTempArraySize(remaining);
                byte[] bArr = new byte[tempArraySize];
                while (remaining > 0) {
                    int min = Math.min(remaining, tempArraySize);
                    byteBuffer.get(bArr, 0, min);
                    engineUpdate(bArr, 0, min);
                    remaining -= min;
                }
            } catch (SignatureException e) {
                throw new ProviderException("update() failed", e);
            }
        }
    }

    /* access modifiers changed from: protected */
    public int engineSign(byte[] bArr, int i, int i2) throws SignatureException {
        byte[] engineSign = engineSign();
        if (i2 < engineSign.length) {
            throw new SignatureException("partial signatures not returned");
        } else if (bArr.length - i >= engineSign.length) {
            System.arraycopy((Object) engineSign, 0, (Object) bArr, i, engineSign.length);
            return engineSign.length;
        } else {
            throw new SignatureException("insufficient space in the output buffer to store the signature");
        }
    }

    /* access modifiers changed from: protected */
    public boolean engineVerify(byte[] bArr, int i, int i2) throws SignatureException {
        byte[] bArr2 = new byte[i2];
        System.arraycopy((Object) bArr, i, (Object) bArr2, 0, i2);
        return engineVerify(bArr2);
    }

    /* access modifiers changed from: protected */
    public void engineSetParameter(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public AlgorithmParameters engineGetParameters() {
        throw new UnsupportedOperationException();
    }

    public Object clone() throws CloneNotSupportedException {
        if (this instanceof Cloneable) {
            return super.clone();
        }
        throw new CloneNotSupportedException();
    }
}
