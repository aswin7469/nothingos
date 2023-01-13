package java.util;

public class MissingFormatWidthException extends IllegalFormatException {
    private static final long serialVersionUID = 15560123;

    /* renamed from: s */
    private String f700s;

    public MissingFormatWidthException(String str) {
        str.getClass();
        this.f700s = str;
    }

    public String getFormatSpecifier() {
        return this.f700s;
    }

    public String getMessage() {
        return this.f700s;
    }
}
