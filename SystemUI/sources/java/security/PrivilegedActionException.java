package java.security;

public class PrivilegedActionException extends Exception {
    private static final long serialVersionUID = 4724086851538908602L;
    private Exception exception;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PrivilegedActionException(Exception exc) {
        super((Throwable) null);
        Throwable th = null;
        this.exception = exc;
    }

    public Exception getException() {
        return this.exception;
    }

    public Throwable getCause() {
        return this.exception;
    }

    public String toString() {
        String name = getClass().getName();
        if (this.exception == null) {
            return name;
        }
        return name + ": " + this.exception.toString();
    }
}
