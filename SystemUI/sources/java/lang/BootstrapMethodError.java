package java.lang;

public class BootstrapMethodError extends LinkageError {
    private static final long serialVersionUID = 292;

    public BootstrapMethodError() {
    }

    public BootstrapMethodError(String str) {
        super(str);
    }

    public BootstrapMethodError(String str, Throwable th) {
        super(str, th);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public BootstrapMethodError(Throwable th) {
        super(th == null ? null : th.toString());
        initCause(th);
    }
}
