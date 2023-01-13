package java.util;

public class IllegalFormatConversionException extends IllegalFormatException {
    private static final long serialVersionUID = 17000126;
    private Class<?> arg;

    /* renamed from: c */
    private char f687c;

    public IllegalFormatConversionException(char c, Class<?> cls) {
        cls.getClass();
        this.f687c = c;
        this.arg = cls;
    }

    public char getConversion() {
        return this.f687c;
    }

    public Class<?> getArgumentClass() {
        return this.arg;
    }

    public String getMessage() {
        return String.format("%c != %s", Character.valueOf(this.f687c), this.arg.getName());
    }
}
