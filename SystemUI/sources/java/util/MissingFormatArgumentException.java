package java.util;

public class MissingFormatArgumentException extends IllegalFormatException {
    private static final long serialVersionUID = 19190115;

    /* renamed from: s */
    private String f701s;

    public MissingFormatArgumentException(String str) {
        str.getClass();
        this.f701s = str;
    }

    public String getFormatSpecifier() {
        return this.f701s;
    }

    public String getMessage() {
        return "Format specifier '" + this.f701s + "'";
    }
}
