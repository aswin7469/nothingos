package java.lang.reflect;

public class InvocationTargetException extends ReflectiveOperationException {
    private static final long serialVersionUID = 4085088731926701167L;
    private Throwable target;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    protected InvocationTargetException() {
        super((Throwable) null);
        Throwable th = null;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public InvocationTargetException(Throwable th) {
        super((Throwable) null);
        Throwable th2 = null;
        this.target = th;
    }

    public InvocationTargetException(Throwable th, String str) {
        super(str, (Throwable) null);
        this.target = th;
    }

    public Throwable getTargetException() {
        return this.target;
    }

    public Throwable getCause() {
        return this.target;
    }
}
