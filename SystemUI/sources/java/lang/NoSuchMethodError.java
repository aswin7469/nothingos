package java.lang;

public class NoSuchMethodError extends IncompatibleClassChangeError {
    private static final long serialVersionUID = -3765521442372831335L;

    public NoSuchMethodError() {
    }

    public NoSuchMethodError(String str) {
        super(str);
    }
}
