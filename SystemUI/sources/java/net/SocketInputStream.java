package java.net;

import java.nio.channels.FileChannel;
import java.p026io.FileDescriptor;
import java.p026io.FileInputStream;
import java.p026io.IOException;

class SocketInputStream extends FileInputStream {
    private boolean closing = false;
    private boolean eof;
    private AbstractPlainSocketImpl impl;
    private Socket socket = null;
    private byte[] temp;

    private native int socketRead0(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3) throws IOException;

    /* access modifiers changed from: protected */
    public void finalize() {
    }

    public final FileChannel getChannel() {
        return null;
    }

    SocketInputStream(AbstractPlainSocketImpl abstractPlainSocketImpl) throws IOException {
        super(abstractPlainSocketImpl.getFileDescriptor());
        this.impl = abstractPlainSocketImpl;
        this.socket = abstractPlainSocketImpl.getSocket();
    }

    private int socketRead(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2, int i3) throws IOException {
        return socketRead0(fileDescriptor, bArr, i, i2, i3);
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        return read(bArr, i, i2, this.impl.getTimeout());
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:16|17) */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0037, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r9.impl.setConnectionReset();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0065, code lost:
        r9.impl.releaseFD();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x006a, code lost:
        throw r10;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x0039 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int read(byte[] r10, int r11, int r12, int r13) throws java.p026io.IOException {
        /*
            r9 = this;
            boolean r0 = r9.eof
            r1 = -1
            if (r0 == 0) goto L_0x0006
            return r1
        L_0x0006:
            java.net.AbstractPlainSocketImpl r0 = r9.impl
            boolean r0 = r0.isConnectionReset()
            java.lang.String r2 = "Connection reset"
            if (r0 != 0) goto L_0x0094
            if (r12 <= 0) goto L_0x006b
            if (r11 < 0) goto L_0x006b
            int r0 = r10.length
            int r0 = r0 - r11
            if (r12 <= r0) goto L_0x0019
            goto L_0x006b
        L_0x0019:
            java.net.AbstractPlainSocketImpl r0 = r9.impl
            java.io.FileDescriptor r4 = r0.acquireFD()
            dalvik.system.BlockGuard$Policy r0 = dalvik.system.BlockGuard.getThreadPolicy()     // Catch:{ ConnectionResetException -> 0x0039 }
            r0.onNetwork()     // Catch:{ ConnectionResetException -> 0x0039 }
            r3 = r9
            r5 = r10
            r6 = r11
            r7 = r12
            r8 = r13
            int r10 = r3.socketRead(r4, r5, r6, r7, r8)     // Catch:{ ConnectionResetException -> 0x0039 }
            if (r10 <= 0) goto L_0x003e
            java.net.AbstractPlainSocketImpl r9 = r9.impl
            r9.releaseFD()
            return r10
        L_0x0037:
            r10 = move-exception
            goto L_0x0065
        L_0x0039:
            java.net.AbstractPlainSocketImpl r10 = r9.impl     // Catch:{ all -> 0x0037 }
            r10.setConnectionReset()     // Catch:{ all -> 0x0037 }
        L_0x003e:
            java.net.AbstractPlainSocketImpl r10 = r9.impl
            r10.releaseFD()
            java.net.AbstractPlainSocketImpl r10 = r9.impl
            boolean r10 = r10.isClosedOrPending()
            if (r10 != 0) goto L_0x005d
            java.net.AbstractPlainSocketImpl r10 = r9.impl
            boolean r10 = r10.isConnectionReset()
            if (r10 != 0) goto L_0x0057
            r10 = 1
            r9.eof = r10
            return r1
        L_0x0057:
            java.net.SocketException r9 = new java.net.SocketException
            r9.<init>((java.lang.String) r2)
            throw r9
        L_0x005d:
            java.net.SocketException r9 = new java.net.SocketException
            java.lang.String r10 = "Socket closed"
            r9.<init>((java.lang.String) r10)
            throw r9
        L_0x0065:
            java.net.AbstractPlainSocketImpl r9 = r9.impl
            r9.releaseFD()
            throw r10
        L_0x006b:
            if (r12 != 0) goto L_0x006f
            r9 = 0
            return r9
        L_0x006f:
            java.lang.ArrayIndexOutOfBoundsException r9 = new java.lang.ArrayIndexOutOfBoundsException
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            java.lang.String r0 = "length == "
            r13.<init>((java.lang.String) r0)
            r13.append((int) r12)
            java.lang.String r12 = " off == "
            r13.append((java.lang.String) r12)
            r13.append((int) r11)
            java.lang.String r11 = " buffer length == "
            r13.append((java.lang.String) r11)
            int r10 = r10.length
            r13.append((int) r10)
            java.lang.String r10 = r13.toString()
            r9.<init>((java.lang.String) r10)
            throw r9
        L_0x0094:
            java.net.SocketException r9 = new java.net.SocketException
            r9.<init>((java.lang.String) r2)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.SocketInputStream.read(byte[], int, int, int):int");
    }

    public int read() throws IOException {
        if (this.eof) {
            return -1;
        }
        byte[] bArr = new byte[1];
        this.temp = bArr;
        if (read(bArr, 0, 1) <= 0) {
            return -1;
        }
        return this.temp[0] & 255;
    }

    public long skip(long j) throws IOException {
        int read;
        if (j <= 0) {
            return 0;
        }
        int min = (int) Math.min(1024, j);
        byte[] bArr = new byte[min];
        long j2 = j;
        while (j2 > 0 && (read = read(bArr, 0, (int) Math.min((long) min, j2))) >= 0) {
            j2 -= (long) read;
        }
        return j - j2;
    }

    public int available() throws IOException {
        if (this.eof) {
            return 0;
        }
        return this.impl.available();
    }

    public void close() throws IOException {
        if (!this.closing) {
            this.closing = true;
            Socket socket2 = this.socket;
            if (socket2 == null) {
                this.impl.close();
            } else if (!socket2.isClosed()) {
                this.socket.close();
            }
            this.closing = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void setEOF(boolean z) {
        this.eof = z;
    }
}
