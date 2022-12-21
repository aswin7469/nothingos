package java.net;

import dalvik.system.BlockGuard;
import java.nio.channels.FileChannel;
import java.p026io.FileDescriptor;
import java.p026io.FileOutputStream;
import java.p026io.IOException;

class SocketOutputStream extends FileOutputStream {
    private boolean closing = false;
    private AbstractPlainSocketImpl impl;
    private Socket socket = null;
    private byte[] temp = new byte[1];

    private native void socketWrite0(FileDescriptor fileDescriptor, byte[] bArr, int i, int i2) throws IOException;

    /* access modifiers changed from: protected */
    public void finalize() {
    }

    public final FileChannel getChannel() {
        return null;
    }

    SocketOutputStream(AbstractPlainSocketImpl abstractPlainSocketImpl) throws IOException {
        super(abstractPlainSocketImpl.getFileDescriptor());
        this.impl = abstractPlainSocketImpl;
        this.socket = abstractPlainSocketImpl.getSocket();
    }

    private void socketWrite(byte[] bArr, int i, int i2) throws IOException {
        if (i2 > 0 && i >= 0 && i2 <= bArr.length - i) {
            FileDescriptor acquireFD = this.impl.acquireFD();
            try {
                BlockGuard.getThreadPolicy().onNetwork();
                socketWrite0(acquireFD, bArr, i, i2);
                this.impl.releaseFD();
            } catch (SocketException e) {
                if (this.impl.isClosedOrPending()) {
                    throw new SocketException("Socket closed");
                }
                throw e;
            } catch (Throwable th) {
                this.impl.releaseFD();
                throw th;
            }
        } else if (i2 != 0) {
            throw new ArrayIndexOutOfBoundsException("len == " + i2 + " off == " + i + " buffer length == " + bArr.length);
        }
    }

    public void write(int i) throws IOException {
        byte[] bArr = this.temp;
        bArr[0] = (byte) i;
        socketWrite(bArr, 0, 1);
    }

    public void write(byte[] bArr) throws IOException {
        socketWrite(bArr, 0, bArr.length);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        socketWrite(bArr, i, i2);
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
}
