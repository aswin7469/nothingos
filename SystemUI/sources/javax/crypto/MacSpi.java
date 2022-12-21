package javax.crypto;

import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

public abstract class MacSpi {
    /* access modifiers changed from: protected */
    public abstract byte[] engineDoFinal();

    /* access modifiers changed from: protected */
    public abstract int engineGetMacLength();

    /* access modifiers changed from: protected */
    public abstract void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException;

    /* access modifiers changed from: protected */
    public abstract void engineReset();

    /* access modifiers changed from: protected */
    public abstract void engineUpdate(byte b);

    /* access modifiers changed from: protected */
    public abstract void engineUpdate(byte[] bArr, int i, int i2);

    /* access modifiers changed from: protected */
    public void engineUpdate(ByteBuffer byteBuffer) {
        if (byteBuffer.hasRemaining()) {
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
            int tempArraySize = CipherSpi.getTempArraySize(remaining);
            byte[] bArr = new byte[tempArraySize];
            while (remaining > 0) {
                int min = Math.min(remaining, tempArraySize);
                byteBuffer.get(bArr, 0, min);
                engineUpdate(bArr, 0, min);
                remaining -= min;
            }
        }
    }

    public Object clone() throws CloneNotSupportedException {
        if (this instanceof Cloneable) {
            return super.clone();
        }
        throw new CloneNotSupportedException();
    }
}
