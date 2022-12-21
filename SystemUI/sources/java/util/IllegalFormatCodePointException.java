package java.util;

public class IllegalFormatCodePointException extends IllegalFormatException {
    private static final long serialVersionUID = 19080630;

    /* renamed from: c */
    private int f688c;

    public IllegalFormatCodePointException(int i) {
        this.f688c = i;
    }

    public int getCodePoint() {
        return this.f688c;
    }

    public String getMessage() {
        return String.format("Code point = %#x", Integer.valueOf(this.f688c));
    }
}
