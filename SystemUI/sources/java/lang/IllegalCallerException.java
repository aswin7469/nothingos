package java.lang;

public class IllegalCallerException extends RuntimeException {
    static final long serialVersionUID = -2349421918363102232L;

    public IllegalCallerException() {
    }

    public IllegalCallerException(String str) {
        super(str);
    }

    public IllegalCallerException(String str, Throwable th) {
        super(str, th);
    }

    public IllegalCallerException(Throwable th) {
        super(th);
    }
}
