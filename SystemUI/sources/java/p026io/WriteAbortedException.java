package java.p026io;

/* renamed from: java.io.WriteAbortedException */
public class WriteAbortedException extends ObjectStreamException {
    private static final long serialVersionUID = -3326426625597282442L;
    public Exception detail;

    public WriteAbortedException(String str, Exception exc) {
        super(str);
        initCause((Throwable) null);
        this.detail = exc;
    }

    public String getMessage() {
        if (this.detail == null) {
            return super.getMessage();
        }
        return super.getMessage() + "; " + this.detail.toString();
    }

    public Throwable getCause() {
        return this.detail;
    }
}
