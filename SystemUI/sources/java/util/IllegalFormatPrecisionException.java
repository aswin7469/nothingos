package java.util;

public class IllegalFormatPrecisionException extends IllegalFormatException {
    private static final long serialVersionUID = 18711008;

    /* renamed from: p */
    private int f690p;

    public IllegalFormatPrecisionException(int i) {
        this.f690p = i;
    }

    public int getPrecision() {
        return this.f690p;
    }

    public String getMessage() {
        return Integer.toString(this.f690p);
    }
}
