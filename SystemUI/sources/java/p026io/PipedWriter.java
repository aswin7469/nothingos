package java.p026io;

/* renamed from: java.io.PipedWriter */
public class PipedWriter extends Writer {
    private boolean closed = false;
    private PipedReader sink;

    public PipedWriter(PipedReader pipedReader) throws IOException {
        connect(pipedReader);
    }

    public PipedWriter() {
    }

    public synchronized void connect(PipedReader pipedReader) throws IOException {
        if (pipedReader != null) {
            try {
                if (this.sink != null || pipedReader.connected) {
                    throw new IOException("Already connected");
                } else if (pipedReader.closedByReader || this.closed) {
                    throw new IOException("Pipe closed");
                } else {
                    this.sink = pipedReader;
                    pipedReader.f527in = -1;
                    pipedReader.out = 0;
                    pipedReader.connected = true;
                }
            } catch (Throwable th) {
                throw th;
            }
        } else {
            throw new NullPointerException();
        }
    }

    public void write(int i) throws IOException {
        PipedReader pipedReader = this.sink;
        if (pipedReader != null) {
            pipedReader.receive(i);
            return;
        }
        throw new IOException("Pipe not connected");
    }

    public void write(char[] cArr, int i, int i2) throws IOException {
        PipedReader pipedReader = this.sink;
        if (pipedReader != null) {
            int i3 = i + i2;
            if ((i | i2 | i3 | (cArr.length - i3)) >= 0) {
                pipedReader.receive(cArr, i, i2);
                return;
            }
            throw new IndexOutOfBoundsException();
        }
        throw new IOException("Pipe not connected");
    }

    public synchronized void flush() throws IOException {
        PipedReader pipedReader = this.sink;
        if (pipedReader != null) {
            if (pipedReader.closedByReader || this.closed) {
                throw new IOException("Pipe closed");
            }
            synchronized (this.sink) {
                this.sink.notifyAll();
            }
        }
    }

    public void close() throws IOException {
        this.closed = true;
        PipedReader pipedReader = this.sink;
        if (pipedReader != null) {
            pipedReader.receivedLast();
        }
    }
}
