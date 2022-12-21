package java.lang.reflect;

public class UndeclaredThrowableException extends RuntimeException {
    static final long serialVersionUID = 330127114055056639L;
    private Throwable undeclaredThrowable;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public UndeclaredThrowableException(Throwable th) {
        super((Throwable) null);
        Throwable th2 = null;
        this.undeclaredThrowable = th;
    }

    public UndeclaredThrowableException(Throwable th, String str) {
        super(str, (Throwable) null);
        this.undeclaredThrowable = th;
    }

    public Throwable getUndeclaredThrowable() {
        return this.undeclaredThrowable;
    }

    public Throwable getCause() {
        return this.undeclaredThrowable;
    }
}
