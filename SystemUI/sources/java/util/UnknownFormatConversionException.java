package java.util;

public class UnknownFormatConversionException extends IllegalFormatException {
    private static final long serialVersionUID = 19060418;

    /* renamed from: s */
    private String f716s;

    public UnknownFormatConversionException(String str) {
        str.getClass();
        this.f716s = str;
    }

    public String getConversion() {
        return this.f716s;
    }

    public String getMessage() {
        return String.format("Conversion = '%s'", this.f716s);
    }
}
