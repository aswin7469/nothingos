package java.p026io;

/* renamed from: java.io.InterruptedIOException */
public class InterruptedIOException extends IOException {
    private static final long serialVersionUID = 4020568460727500567L;
    public int bytesTransferred = 0;

    public InterruptedIOException() {
    }

    public InterruptedIOException(String str) {
        super(str);
    }

    public InterruptedIOException(Throwable th) {
        super(th);
    }

    public InterruptedIOException(String str, Throwable th) {
        super(str, th);
    }
}
