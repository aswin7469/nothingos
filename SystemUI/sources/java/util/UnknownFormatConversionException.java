package java.util;

public class UnknownFormatConversionException extends IllegalFormatException {
    private static final long serialVersionUID = 19060418;

    /* renamed from: s */
    private String f714s;

    public UnknownFormatConversionException(String str) {
        str.getClass();
        this.f714s = str;
    }

    public String getConversion() {
        return this.f714s;
    }

    public String getMessage() {
        return String.format("Conversion = '%s'", this.f714s);
    }
}
