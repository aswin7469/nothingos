package java.util;

public class IllegalFormatWidthException extends IllegalFormatException {
    private static final long serialVersionUID = 16660902;

    /* renamed from: w */
    private int f691w;

    public IllegalFormatWidthException(int i) {
        this.f691w = i;
    }

    public int getWidth() {
        return this.f691w;
    }

    public String getMessage() {
        return Integer.toString(this.f691w);
    }
}
