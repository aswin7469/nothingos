package java.util;

public class MissingFormatWidthException extends IllegalFormatException {
    private static final long serialVersionUID = 15560123;

    /* renamed from: s */
    private String f702s;

    public MissingFormatWidthException(String str) {
        str.getClass();
        this.f702s = str;
    }

    public String getFormatSpecifier() {
        return this.f702s;
    }

    public String getMessage() {
        return this.f702s;
    }
}
