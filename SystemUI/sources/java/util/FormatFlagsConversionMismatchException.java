package java.util;

public class FormatFlagsConversionMismatchException extends IllegalFormatException {
    private static final long serialVersionUID = 19120414;

    /* renamed from: c */
    private char f673c;

    /* renamed from: f */
    private String f674f;

    public FormatFlagsConversionMismatchException(String str, char c) {
        str.getClass();
        this.f674f = str;
        this.f673c = c;
    }

    public String getFlags() {
        return this.f674f;
    }

    public char getConversion() {
        return this.f673c;
    }

    public String getMessage() {
        return "Conversion = " + this.f673c + ", Flags = " + this.f674f;
    }
}
