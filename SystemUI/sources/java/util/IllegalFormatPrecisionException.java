package java.util;

public class IllegalFormatPrecisionException extends IllegalFormatException {
    private static final long serialVersionUID = 18711008;

    /* renamed from: p */
    private int f688p;

    public IllegalFormatPrecisionException(int i) {
        this.f688p = i;
    }

    public int getPrecision() {
        return this.f688p;
    }

    public String getMessage() {
        return Integer.toString(this.f688p);
    }
}
