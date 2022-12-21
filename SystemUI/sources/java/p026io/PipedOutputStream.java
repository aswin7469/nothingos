package java.p026io;

/* renamed from: java.io.PipedOutputStream */
public class PipedOutputStream extends OutputStream {
    private PipedInputStream sink;

    public PipedOutputStream(PipedInputStream pipedInputStream) throws IOException {
        connect(pipedInputStream);
    }

    public PipedOutputStream() {
    }

    public synchronized void connect(PipedInputStream pipedInputStream) throws IOException {
        if (pipedInputStream != null) {
            try {
                if (this.sink != null || pipedInputStream.connected) {
                    throw new IOException("Already connected");
                }
                this.sink = pipedInputStream;
                pipedInputStream.f528in = -1;
                pipedInputStream.out = 0;
                pipedInputStream.connected = true;
            } catch (Throwable th) {
                throw th;
            }
        } else {
            throw new NullPointerException();
        }
    }

    public void write(int i) throws IOException {
        PipedInputStream pipedInputStream = this.sink;
        if (pipedInputStream != null) {
            pipedInputStream.receive(i);
            return;
        }
        throw new IOException("Pipe not connected");
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        int i3;
        PipedInputStream pipedInputStream = this.sink;
        if (pipedInputStream != null) {
            bArr.getClass();
            if (i < 0 || i > bArr.length || i2 < 0 || (i3 = i + i2) > bArr.length || i3 < 0) {
                throw new IndexOutOfBoundsException();
            } else if (i2 != 0) {
                pipedInputStream.receive(bArr, i, i2);
            }
        } else {
            throw new IOException("Pipe not connected");
        }
    }

    public synchronized void flush() throws IOException {
        PipedInputStream pipedInputStream = this.sink;
        if (pipedInputStream != null) {
            synchronized (pipedInputStream) {
                this.sink.notifyAll();
            }
        }
    }

    public void close() throws IOException {
        PipedInputStream pipedInputStream = this.sink;
        if (pipedInputStream != null) {
            pipedInputStream.receivedLast();
        }
    }
}
