package java.util.zip;

import android.net.wifi.hotspot2.pps.UpdateParameter;
import java.nio.ByteBuffer;
import sun.nio.p033ch.DirectBuffer;

public class CRC32 implements Checksum {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private int crc;

    private static native int update(int i, int i2);

    private static native int updateByteBuffer(int i, long j, int i2, int i3);

    private static native int updateBytes(int i, byte[] bArr, int i2, int i3);

    public void update(int i) {
        this.crc = update(this.crc, i);
    }

    public void update(byte[] bArr, int i, int i2) {
        bArr.getClass();
        if (i < 0 || i2 < 0 || i > bArr.length - i2) {
            throw new ArrayIndexOutOfBoundsException();
        }
        this.crc = updateBytes(this.crc, bArr, i, i2);
    }

    public void update(byte[] bArr) {
        this.crc = updateBytes(this.crc, bArr, 0, bArr.length);
    }

    public void update(ByteBuffer byteBuffer) {
        int position = byteBuffer.position();
        int limit = byteBuffer.limit();
        int i = limit - position;
        if (i > 0) {
            if (byteBuffer instanceof DirectBuffer) {
                this.crc = updateByteBuffer(this.crc, ((DirectBuffer) byteBuffer).address(), position, i);
            } else if (byteBuffer.hasArray()) {
                this.crc = updateBytes(this.crc, byteBuffer.array(), position + byteBuffer.arrayOffset(), i);
            } else {
                byte[] bArr = new byte[i];
                byteBuffer.get(bArr);
                this.crc = updateBytes(this.crc, bArr, 0, i);
            }
            byteBuffer.position(limit);
        }
    }

    public void reset() {
        this.crc = 0;
    }

    public long getValue() {
        return ((long) this.crc) & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER;
    }
}
