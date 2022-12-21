package sun.nio.p033ch;

import java.nio.ByteBuffer;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.SelectableChannel;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.util.Objects;

/* renamed from: sun.nio.ch.ChannelInputStream */
public class ChannelInputStream extends InputStream {

    /* renamed from: b1 */
    private byte[] f875b1 = null;

    /* renamed from: bb */
    private ByteBuffer f876bb = null;

    /* renamed from: bs */
    private byte[] f877bs = null;

    /* renamed from: ch */
    protected final ReadableByteChannel f878ch;

    public static int read(ReadableByteChannel readableByteChannel, ByteBuffer byteBuffer, boolean z) throws IOException {
        int read;
        if (!(readableByteChannel instanceof SelectableChannel)) {
            return readableByteChannel.read(byteBuffer);
        }
        SelectableChannel selectableChannel = (SelectableChannel) readableByteChannel;
        synchronized (selectableChannel.blockingLock()) {
            boolean isBlocking = selectableChannel.isBlocking();
            if (isBlocking) {
                if (isBlocking != z) {
                    selectableChannel.configureBlocking(z);
                }
                read = readableByteChannel.read(byteBuffer);
                if (isBlocking != z) {
                    selectableChannel.configureBlocking(isBlocking);
                }
            } else {
                throw new IllegalBlockingModeException();
            }
        }
        return read;
    }

    public ChannelInputStream(ReadableByteChannel readableByteChannel) {
        this.f878ch = readableByteChannel;
    }

    public synchronized int read() throws IOException {
        if (this.f875b1 == null) {
            this.f875b1 = new byte[1];
        }
        if (read(this.f875b1) != 1) {
            return -1;
        }
        return this.f875b1[0] & 255;
    }

    public synchronized int read(byte[] bArr, int i, int i2) throws IOException {
        ByteBuffer byteBuffer;
        Objects.checkFromIndexSize(i, i2, bArr.length);
        if (i2 == 0) {
            return 0;
        }
        if (this.f877bs == bArr) {
            byteBuffer = this.f876bb;
        } else {
            byteBuffer = ByteBuffer.wrap(bArr);
        }
        byteBuffer.limit(Math.min(i2 + i, byteBuffer.capacity()));
        byteBuffer.position(i);
        this.f876bb = byteBuffer;
        this.f877bs = bArr;
        return read(byteBuffer);
    }

    /* access modifiers changed from: protected */
    public int read(ByteBuffer byteBuffer) throws IOException {
        return read(this.f878ch, byteBuffer, true);
    }

    public int available() throws IOException {
        ReadableByteChannel readableByteChannel = this.f878ch;
        if (!(readableByteChannel instanceof SeekableByteChannel)) {
            return 0;
        }
        SeekableByteChannel seekableByteChannel = (SeekableByteChannel) readableByteChannel;
        long max = Math.max(0, seekableByteChannel.size() - seekableByteChannel.position());
        if (max > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int) max;
    }

    public synchronized long skip(long j) throws IOException {
        long j2;
        ReadableByteChannel readableByteChannel = this.f878ch;
        if (readableByteChannel instanceof SeekableByteChannel) {
            SeekableByteChannel seekableByteChannel = (SeekableByteChannel) readableByteChannel;
            long position = seekableByteChannel.position();
            if (j > 0) {
                j2 = j + position;
                long size = seekableByteChannel.size();
                if (j2 < 0 || j2 > size) {
                    j2 = size;
                }
            } else {
                j2 = Long.max(j + position, 0);
            }
            seekableByteChannel.position(j2);
            return j2 - position;
        }
        return super.skip(j);
    }

    public void close() throws IOException {
        this.f878ch.close();
    }
}
