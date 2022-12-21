package java.util;

public class FormatFlagsConversionMismatchException extends IllegalFormatException {
    private static final long serialVersionUID = 19120414;

    /* renamed from: c */
    private char f675c;

    /* renamed from: f */
    private String f676f;

    public FormatFlagsConversionMismatchException(String str, char c) {
        str.getClass();
        this.f676f = str;
        this.f675c = c;
    }

    public String getFlags() {
        return this.f676f;
    }

    public char getConversion() {
        return this.f675c;
    }

    public String getMessage() {
        return "Conversion = " + this.f675c + ", Flags = " + this.f676f;
    }
}
