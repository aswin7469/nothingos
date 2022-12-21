package java.security;

import java.nio.ByteBuffer;
import sun.security.jca.JCAUtil;

public abstract class MessageDigestSpi {
    private byte[] tempArray;

    /* access modifiers changed from: protected */
    public abstract byte[] engineDigest();

    /* access modifiers changed from: protected */
    public int engineGetDigestLength() {
        return 0;
    }

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
            int tempArraySize = JCAUtil.getTempArraySize(remaining);
            byte[] bArr = this.tempArray;
            if (bArr == null || tempArraySize > bArr.length) {
                this.tempArray = new byte[tempArraySize];
            }
            while (remaining > 0) {
                int min = Math.min(remaining, this.tempArray.length);
                byteBuffer.get(this.tempArray, 0, min);
                engineUpdate(this.tempArray, 0, min);
                remaining -= min;
            }
        }
    }

    /* access modifiers changed from: protected */
    public int engineDigest(byte[] bArr, int i, int i2) throws DigestException {
        byte[] engineDigest = engineDigest();
        if (i2 < engineDigest.length) {
            throw new DigestException("partial digests not returned");
        } else if (bArr.length - i >= engineDigest.length) {
            System.arraycopy((Object) engineDigest, 0, (Object) bArr, i, engineDigest.length);
            return engineDigest.length;
        } else {
            throw new DigestException("insufficient space in the output buffer to store the digest");
        }
    }

    public Object clone() throws CloneNotSupportedException {
        if (this instanceof Cloneable) {
            return super.clone();
        }
        throw new CloneNotSupportedException();
    }
}
