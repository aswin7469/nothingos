package java.util;

public class IllegalFormatConversionException extends IllegalFormatException {
    private static final long serialVersionUID = 17000126;
    private Class<?> arg;

    /* renamed from: c */
    private char f689c;

    public IllegalFormatConversionException(char c, Class<?> cls) {
        cls.getClass();
        this.f689c = c;
        this.arg = cls;
    }

    public char getConversion() {
        return this.f689c;
    }

    public Class<?> getArgumentClass() {
        return this.arg;
    }

    public String getMessage() {
        return String.format("%c != %s", Character.valueOf(this.f689c), this.arg.getName());
    }
}
