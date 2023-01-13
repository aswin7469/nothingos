package java.util;

public class IllegalFormatWidthException extends IllegalFormatException {
    private static final long serialVersionUID = 16660902;

    /* renamed from: w */
    private int f689w;

    public IllegalFormatWidthException(int i) {
        this.f689w = i;
    }

    public int getWidth() {
        return this.f689w;
    }

    public String getMessage() {
        return Integer.toString(this.f689w);
    }
}
