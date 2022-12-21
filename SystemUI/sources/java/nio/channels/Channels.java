package java.nio.channels;

import java.nio.ByteBuffer;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.p026io.FileInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.p026io.Reader;
import java.p026io.Writer;
import java.util.Objects;
import sun.nio.p033ch.ChannelInputStream;
import sun.nio.p034cs.StreamDecoder;
import sun.nio.p034cs.StreamEncoder;

public final class Channels {
    private Channels() {
        throw new Error("no instances");
    }

    private static void writeFullyImpl(WritableByteChannel writableByteChannel, ByteBuffer byteBuffer) throws IOException {
        while (byteBuffer.remaining() > 0) {
            if (writableByteChannel.write(byteBuffer) <= 0) {
                throw new RuntimeException("no bytes written");
            }
        }
    }

    /* access modifiers changed from: private */
    public static void writeFully(WritableByteChannel writableByteChannel, ByteBuffer byteBuffer) throws IOException {
        if (writableByteChannel instanceof SelectableChannel) {
            SelectableChannel selectableChannel = (SelectableChannel) writableByteChannel;
            synchronized (selectableChannel.blockingLock()) {
                if (selectableChannel.isBlocking()) {
                    writeFullyImpl(writableByteChannel, byteBuffer);
                } else {
                    throw new IllegalBlockingModeException();
                }
            }
            return;
        }
        writeFullyImpl(writableByteChannel, byteBuffer);
    }

    public static InputStream newInputStream(ReadableByteChannel readableByteChannel) {
        Objects.requireNonNull(readableByteChannel, "ch");
        return new ChannelInputStream(readableByteChannel);
    }

    public static OutputStream newOutputStream(final WritableByteChannel writableByteChannel) {
        Objects.requireNonNull(writableByteChannel, "ch");
        return new OutputStream() {

            /* renamed from: b1 */
            private byte[] f574b1;

            /* renamed from: bb */
            private ByteBuffer f575bb;

            /* renamed from: bs */
            private byte[] f576bs;

            public synchronized void write(int i) throws IOException {
                if (this.f574b1 == null) {
                    this.f574b1 = new byte[1];
                }
                byte[] bArr = this.f574b1;
                bArr[0] = (byte) i;
                write(bArr);
            }

            public synchronized void write(byte[] bArr, int i, int i2) throws IOException {
                int i3;
                ByteBuffer byteBuffer;
                if (i >= 0) {
                    if (i <= bArr.length && i2 >= 0 && (i3 = i + i2) <= bArr.length && i3 >= 0) {
                        if (i2 != 0) {
                            if (this.f576bs == bArr) {
                                byteBuffer = this.f575bb;
                            } else {
                                byteBuffer = ByteBuffer.wrap(bArr);
                            }
                            byteBuffer.limit(Math.min(i3, byteBuffer.capacity()));
                            byteBuffer.position(i);
                            this.f575bb = byteBuffer;
                            this.f576bs = bArr;
                            Channels.writeFully(WritableByteChannel.this, byteBuffer);
                            return;
                        }
                        return;
                    }
                }
                throw new IndexOutOfBoundsException();
            }

            public void close() throws IOException {
                WritableByteChannel.this.close();
            }
        };
    }

    public static InputStream newInputStream(final AsynchronousByteChannel asynchronousByteChannel) {
        Objects.requireNonNull(asynchronousByteChannel, "ch");
        return new InputStream() {

            /* renamed from: b1 */
            private byte[] f577b1;

            /* renamed from: bb */
            private ByteBuffer f578bb;

            /* renamed from: bs */
            private byte[] f579bs;

            public synchronized int read() throws IOException {
                if (this.f577b1 == null) {
                    this.f577b1 = new byte[1];
                }
                if (read(this.f577b1) != 1) {
                    return -1;
                }
                return this.f577b1[0] & 255;
            }

            /* JADX WARNING: Code restructure failed: missing block: B:25:0x004b, code lost:
                return r3;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public synchronized int read(byte[] r3, int r4, int r5) throws java.p026io.IOException {
                /*
                    r2 = this;
                    monitor-enter(r2)
                    if (r4 < 0) goto L_0x0065
                    int r0 = r3.length     // Catch:{ all -> 0x006b }
                    if (r4 > r0) goto L_0x0065
                    if (r5 < 0) goto L_0x0065
                    int r0 = r4 + r5
                    int r1 = r3.length     // Catch:{ all -> 0x006b }
                    if (r0 > r1) goto L_0x0065
                    if (r0 < 0) goto L_0x0065
                    r1 = 0
                    if (r5 != 0) goto L_0x0014
                    monitor-exit(r2)
                    return r1
                L_0x0014:
                    byte[] r5 = r2.f579bs     // Catch:{ all -> 0x006b }
                    if (r5 != r3) goto L_0x001b
                    java.nio.ByteBuffer r5 = r2.f578bb     // Catch:{ all -> 0x006b }
                    goto L_0x001f
                L_0x001b:
                    java.nio.ByteBuffer r5 = java.nio.ByteBuffer.wrap(r3)     // Catch:{ all -> 0x006b }
                L_0x001f:
                    r5.position((int) r4)     // Catch:{ all -> 0x006b }
                    int r4 = r5.capacity()     // Catch:{ all -> 0x006b }
                    int r4 = java.lang.Math.min((int) r0, (int) r4)     // Catch:{ all -> 0x006b }
                    r5.limit((int) r4)     // Catch:{ all -> 0x006b }
                    r2.f578bb = r5     // Catch:{ all -> 0x006b }
                    r2.f579bs = r3     // Catch:{ all -> 0x006b }
                L_0x0031:
                    java.nio.channels.AsynchronousByteChannel r3 = java.nio.channels.AsynchronousByteChannel.this     // Catch:{ ExecutionException -> 0x0050, InterruptedException -> 0x004e }
                    java.util.concurrent.Future r3 = r3.read(r5)     // Catch:{ ExecutionException -> 0x0050, InterruptedException -> 0x004e }
                    java.lang.Object r3 = r3.get()     // Catch:{ ExecutionException -> 0x0050, InterruptedException -> 0x004e }
                    java.lang.Integer r3 = (java.lang.Integer) r3     // Catch:{ ExecutionException -> 0x0050, InterruptedException -> 0x004e }
                    int r3 = r3.intValue()     // Catch:{ ExecutionException -> 0x0050, InterruptedException -> 0x004e }
                    if (r1 == 0) goto L_0x004a
                    java.lang.Thread r4 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x006b }
                    r4.interrupt()     // Catch:{ all -> 0x006b }
                L_0x004a:
                    monitor-exit(r2)
                    return r3
                L_0x004c:
                    r3 = move-exception
                    goto L_0x005b
                L_0x004e:
                    r1 = 1
                    goto L_0x0031
                L_0x0050:
                    r3 = move-exception
                    java.io.IOException r4 = new java.io.IOException     // Catch:{ all -> 0x004c }
                    java.lang.Throwable r3 = r3.getCause()     // Catch:{ all -> 0x004c }
                    r4.<init>((java.lang.Throwable) r3)     // Catch:{ all -> 0x004c }
                    throw r4     // Catch:{ all -> 0x004c }
                L_0x005b:
                    if (r1 == 0) goto L_0x0064
                    java.lang.Thread r4 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x006b }
                    r4.interrupt()     // Catch:{ all -> 0x006b }
                L_0x0064:
                    throw r3     // Catch:{ all -> 0x006b }
                L_0x0065:
                    java.lang.IndexOutOfBoundsException r3 = new java.lang.IndexOutOfBoundsException     // Catch:{ all -> 0x006b }
                    r3.<init>()     // Catch:{ all -> 0x006b }
                    throw r3     // Catch:{ all -> 0x006b }
                L_0x006b:
                    r3 = move-exception
                    monitor-exit(r2)
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: java.nio.channels.Channels.C43472.read(byte[], int, int):int");
            }

            public void close() throws IOException {
                AsynchronousByteChannel.this.close();
            }
        };
    }

    public static OutputStream newOutputStream(final AsynchronousByteChannel asynchronousByteChannel) {
        Objects.requireNonNull(asynchronousByteChannel, "ch");
        return new OutputStream() {

            /* renamed from: b1 */
            private byte[] f580b1;

            /* renamed from: bb */
            private ByteBuffer f581bb;

            /* renamed from: bs */
            private byte[] f582bs;

            public synchronized void write(int i) throws IOException {
                if (this.f580b1 == null) {
                    this.f580b1 = new byte[1];
                }
                byte[] bArr = this.f580b1;
                bArr[0] = (byte) i;
                write(bArr);
            }

            /* JADX WARNING: Code restructure failed: missing block: B:34:0x0058, code lost:
                return;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public synchronized void write(byte[] r3, int r4, int r5) throws java.p026io.IOException {
                /*
                    r2 = this;
                    monitor-enter(r2)
                    if (r4 < 0) goto L_0x0064
                    int r0 = r3.length     // Catch:{ all -> 0x006a }
                    if (r4 > r0) goto L_0x0064
                    if (r5 < 0) goto L_0x0064
                    int r0 = r4 + r5
                    int r1 = r3.length     // Catch:{ all -> 0x006a }
                    if (r0 > r1) goto L_0x0064
                    if (r0 < 0) goto L_0x0064
                    if (r5 != 0) goto L_0x0013
                    monitor-exit(r2)
                    return
                L_0x0013:
                    byte[] r5 = r2.f582bs     // Catch:{ all -> 0x006a }
                    if (r5 != r3) goto L_0x001a
                    java.nio.ByteBuffer r5 = r2.f581bb     // Catch:{ all -> 0x006a }
                    goto L_0x001e
                L_0x001a:
                    java.nio.ByteBuffer r5 = java.nio.ByteBuffer.wrap(r3)     // Catch:{ all -> 0x006a }
                L_0x001e:
                    int r1 = r5.capacity()     // Catch:{ all -> 0x006a }
                    int r0 = java.lang.Math.min((int) r0, (int) r1)     // Catch:{ all -> 0x006a }
                    r5.limit((int) r0)     // Catch:{ all -> 0x006a }
                    r5.position((int) r4)     // Catch:{ all -> 0x006a }
                    r2.f581bb = r5     // Catch:{ all -> 0x006a }
                    r2.f582bs = r3     // Catch:{ all -> 0x006a }
                    r3 = 0
                L_0x0031:
                    int r4 = r5.remaining()     // Catch:{ all -> 0x0059 }
                    if (r4 <= 0) goto L_0x004e
                    java.nio.channels.AsynchronousByteChannel r4 = java.nio.channels.AsynchronousByteChannel.this     // Catch:{ ExecutionException -> 0x0043, InterruptedException -> 0x0041 }
                    java.util.concurrent.Future r4 = r4.write(r5)     // Catch:{ ExecutionException -> 0x0043, InterruptedException -> 0x0041 }
                    r4.get()     // Catch:{ ExecutionException -> 0x0043, InterruptedException -> 0x0041 }
                    goto L_0x0031
                L_0x0041:
                    r3 = 1
                    goto L_0x0031
                L_0x0043:
                    r4 = move-exception
                    java.io.IOException r5 = new java.io.IOException     // Catch:{ all -> 0x0059 }
                    java.lang.Throwable r4 = r4.getCause()     // Catch:{ all -> 0x0059 }
                    r5.<init>((java.lang.Throwable) r4)     // Catch:{ all -> 0x0059 }
                    throw r5     // Catch:{ all -> 0x0059 }
                L_0x004e:
                    if (r3 == 0) goto L_0x0057
                    java.lang.Thread r3 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x006a }
                    r3.interrupt()     // Catch:{ all -> 0x006a }
                L_0x0057:
                    monitor-exit(r2)
                    return
                L_0x0059:
                    r4 = move-exception
                    if (r3 == 0) goto L_0x0063
                    java.lang.Thread r3 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x006a }
                    r3.interrupt()     // Catch:{ all -> 0x006a }
                L_0x0063:
                    throw r4     // Catch:{ all -> 0x006a }
                L_0x0064:
                    java.lang.IndexOutOfBoundsException r3 = new java.lang.IndexOutOfBoundsException     // Catch:{ all -> 0x006a }
                    r3.<init>()     // Catch:{ all -> 0x006a }
                    throw r3     // Catch:{ all -> 0x006a }
                L_0x006a:
                    r3 = move-exception
                    monitor-exit(r2)
                    throw r3
                */
                throw new UnsupportedOperationException("Method not decompiled: java.nio.channels.Channels.C43483.write(byte[], int, int):void");
            }

            public void close() throws IOException {
                AsynchronousByteChannel.this.close();
            }
        };
    }

    public static ReadableByteChannel newChannel(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "in");
        if (inputStream.getClass() == FileInputStream.class) {
            return ((FileInputStream) inputStream).getChannel();
        }
        return new ReadableByteChannelImpl(inputStream);
    }

    private static class ReadableByteChannelImpl extends AbstractInterruptibleChannel implements ReadableByteChannel {
        private static final int TRANSFER_SIZE = 8192;
        private byte[] buf = new byte[0];

        /* renamed from: in */
        private final InputStream f583in;
        private final Object readLock = new Object();

        ReadableByteChannelImpl(InputStream inputStream) {
            this.f583in = inputStream;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:36:0x005d, code lost:
            return r3;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int read(java.nio.ByteBuffer r10) throws java.p026io.IOException {
            /*
                r9 = this;
                boolean r0 = r9.isOpen()
                if (r0 == 0) goto L_0x0060
                int r0 = r10.remaining()
                java.lang.Object r1 = r9.readLock
                monitor-enter(r1)
                r2 = 0
                r3 = r2
                r4 = r3
            L_0x0010:
                if (r3 >= r0) goto L_0x0055
                int r5 = r0 - r3
                r6 = 8192(0x2000, float:1.14794E-41)
                int r5 = java.lang.Math.min((int) r5, (int) r6)     // Catch:{ all -> 0x0053 }
                byte[] r6 = r9.buf     // Catch:{ all -> 0x0053 }
                int r6 = r6.length     // Catch:{ all -> 0x0053 }
                if (r6 >= r5) goto L_0x0023
                byte[] r6 = new byte[r5]     // Catch:{ all -> 0x0053 }
                r9.buf = r6     // Catch:{ all -> 0x0053 }
            L_0x0023:
                if (r3 <= 0) goto L_0x002e
                java.io.InputStream r6 = r9.f583in     // Catch:{ all -> 0x0053 }
                int r6 = r6.available()     // Catch:{ all -> 0x0053 }
                if (r6 > 0) goto L_0x002e
                goto L_0x0055
            L_0x002e:
                r6 = 1
                r9.begin()     // Catch:{ all -> 0x004b }
                java.io.InputStream r7 = r9.f583in     // Catch:{ all -> 0x004b }
                byte[] r8 = r9.buf     // Catch:{ all -> 0x004b }
                int r4 = r7.read(r8, r2, r5)     // Catch:{ all -> 0x004b }
                if (r4 <= 0) goto L_0x003d
                goto L_0x003e
            L_0x003d:
                r6 = r2
            L_0x003e:
                r9.end(r6)     // Catch:{ all -> 0x0053 }
                if (r4 >= 0) goto L_0x0044
                goto L_0x0055
            L_0x0044:
                int r3 = r3 + r4
                byte[] r5 = r9.buf     // Catch:{ all -> 0x0053 }
                r10.put(r5, r2, r4)     // Catch:{ all -> 0x0053 }
                goto L_0x0010
            L_0x004b:
                r10 = move-exception
                if (r4 <= 0) goto L_0x004f
                r2 = r6
            L_0x004f:
                r9.end(r2)     // Catch:{ all -> 0x0053 }
                throw r10     // Catch:{ all -> 0x0053 }
            L_0x0053:
                r9 = move-exception
                goto L_0x005e
            L_0x0055:
                if (r4 >= 0) goto L_0x005c
                if (r3 != 0) goto L_0x005c
                monitor-exit(r1)     // Catch:{ all -> 0x0053 }
                r9 = -1
                return r9
            L_0x005c:
                monitor-exit(r1)     // Catch:{ all -> 0x0053 }
                return r3
            L_0x005e:
                monitor-exit(r1)     // Catch:{ all -> 0x0053 }
                throw r9
            L_0x0060:
                java.nio.channels.ClosedChannelException r9 = new java.nio.channels.ClosedChannelException
                r9.<init>()
                throw r9
            */
            throw new UnsupportedOperationException("Method not decompiled: java.nio.channels.Channels.ReadableByteChannelImpl.read(java.nio.ByteBuffer):int");
        }

        /* access modifiers changed from: protected */
        public void implCloseChannel() throws IOException {
            this.f583in.close();
        }
    }

    public static WritableByteChannel newChannel(OutputStream outputStream) {
        Objects.requireNonNull(outputStream, "out");
        return new WritableByteChannelImpl(outputStream);
    }

    private static class WritableByteChannelImpl extends AbstractInterruptibleChannel implements WritableByteChannel {
        private static final int TRANSFER_SIZE = 8192;
        private byte[] buf = new byte[0];
        private final OutputStream out;
        private final Object writeLock = new Object();

        WritableByteChannelImpl(OutputStream outputStream) {
            this.out = outputStream;
        }

        public int write(ByteBuffer byteBuffer) throws IOException {
            int i;
            if (isOpen()) {
                int remaining = byteBuffer.remaining();
                synchronized (this.writeLock) {
                    boolean z = false;
                    i = 0;
                    while (i < remaining) {
                        int min = Math.min(remaining - i, 8192);
                        if (this.buf.length < min) {
                            this.buf = new byte[min];
                        }
                        byteBuffer.get(this.buf, 0, min);
                        boolean z2 = true;
                        try {
                            begin();
                            this.out.write(this.buf, 0, min);
                            if (min <= 0) {
                                z2 = false;
                            }
                            end(z2);
                            i += min;
                        } catch (Throwable th) {
                            if (min > 0) {
                                z = true;
                            }
                            end(z);
                            throw th;
                        }
                    }
                }
                return i;
            }
            throw new ClosedChannelException();
        }

        /* access modifiers changed from: protected */
        public void implCloseChannel() throws IOException {
            this.out.close();
        }
    }

    public static Reader newReader(ReadableByteChannel readableByteChannel, CharsetDecoder charsetDecoder, int i) {
        Objects.requireNonNull(readableByteChannel, "ch");
        return StreamDecoder.forDecoder(readableByteChannel, charsetDecoder.reset(), i);
    }

    public static Reader newReader(ReadableByteChannel readableByteChannel, String str) {
        Objects.requireNonNull(str, "csName");
        return newReader(readableByteChannel, Charset.forName(str).newDecoder(), -1);
    }

    public static Reader newReader(ReadableByteChannel readableByteChannel, Charset charset) {
        Objects.requireNonNull(charset, "charset");
        return newReader(readableByteChannel, charset.newDecoder(), -1);
    }

    public static Writer newWriter(WritableByteChannel writableByteChannel, CharsetEncoder charsetEncoder, int i) {
        Objects.requireNonNull(writableByteChannel, "ch");
        return StreamEncoder.forEncoder(writableByteChannel, charsetEncoder.reset(), i);
    }

    public static Writer newWriter(WritableByteChannel writableByteChannel, String str) {
        Objects.requireNonNull(str, "csName");
        return newWriter(writableByteChannel, Charset.forName(str).newEncoder(), -1);
    }

    public static Writer newWriter(WritableByteChannel writableByteChannel, Charset charset) {
        Objects.requireNonNull(charset, "charset");
        return newWriter(writableByteChannel, charset.newEncoder(), -1);
    }
}
