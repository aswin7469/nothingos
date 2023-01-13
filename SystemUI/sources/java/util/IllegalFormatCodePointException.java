package java.util;

public class IllegalFormatCodePointException extends IllegalFormatException {
    private static final long serialVersionUID = 19080630;

    /* renamed from: c */
    private int f686c;

    public IllegalFormatCodePointException(int i) {
        this.f686c = i;
    }

    public int getCodePoint() {
        return this.f686c;
    }

    public String getMessage() {
        return String.format("Code point = %#x", Integer.valueOf(this.f686c));
    }
}
