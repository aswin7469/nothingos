package java.lang;

public class ClassNotFoundException extends ReflectiveOperationException {
    private static final long serialVersionUID = 9176873029745254542L;

    /* renamed from: ex */
    private Throwable f535ex;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ClassNotFoundException() {
        super((Throwable) null);
        Throwable th = null;
    }

    public ClassNotFoundException(String str) {
        super(str, (Throwable) null);
    }

    public ClassNotFoundException(String str, Throwable th) {
        super(str, (Throwable) null);
        this.f535ex = th;
    }

    public Throwable getException() {
        return this.f535ex;
    }

    public Throwable getCause() {
        return this.f535ex;
    }
}