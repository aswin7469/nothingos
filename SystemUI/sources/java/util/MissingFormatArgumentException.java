package java.util;

public class MissingFormatArgumentException extends IllegalFormatException {
    private static final long serialVersionUID = 19190115;

    /* renamed from: s */
    private String f699s;

    public MissingFormatArgumentException(String str) {
        str.getClass();
        this.f699s = str;
    }

    public String getFormatSpecifier() {
        return this.f699s;
    }

    public String getMessage() {
        return "Format specifier '" + this.f699s + "'";
    }
}
