package java.util.zip;

import android.net.wifi.hotspot2.pps.UpdateParameter;
import java.nio.ByteBuffer;
import sun.nio.p033ch.DirectBuffer;

public class Adler32 implements Checksum {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private int adler = 1;

    private static native int update(int i, int i2);

    private static native int updateByteBuffer(int i, long j, int i2, int i3);

    private static native int updateBytes(int i, byte[] bArr, int i2, int i3);

    public void update(int i) {
        this.adler = update(this.adler, i);
    }

    public void update(byte[] bArr, int i, int i2) {
        bArr.getClass();
        if (i < 0 || i2 < 0 || i > bArr.length - i2) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.adler = updateBytes(this.adler, bArr, i, i2);
    }

    public void update(byte[] bArr) {
        this.adler = updateBytes(this.adler, bArr, 0, bArr.length);
    }

    public void update(ByteBuffer byteBuffer) {
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();
        int i = limit - position;
        if (i > 0) {
            if (byteBuffer instanceof DirectBuffer) {
                this.adler = updateByteBuffer(this.adler, ((DirectBuffer) byteBuffer).address(), position, i);
            } else if (byteBuffer.hasArray()) {
                this.adler = updateBytes(this.adler, byteBuffer.array(), position + byteBuffer.arrayOffset(), i);
            } else {
                byte[] bArr = new byte[i];
                byteBuffer.get(bArr);
                this.adler = updateBytes(this.adler, bArr, 0, i);
            }
            byteBuffer.position(limit);
        }
    }

    public void reset() {
        this.adler = 1;
    }

    public long getValue() {
        return ((long) this.adler) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
    }
}
