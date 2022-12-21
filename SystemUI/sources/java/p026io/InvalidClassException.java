package java.p026io;

/* renamed from: java.io.InvalidClassException */
public class InvalidClassException extends ObjectStreamException {
    private static final long serialVersionUID = -4333316296251054416L;
    public String classname;

    public InvalidClassException(String str) {
        super(str);
    }

    public InvalidClassException(String str, String str2) {
        super(str2);
        this.classname = str;
    }

    public String getMessage() {
        if (this.classname == null) {
            return super.getMessage();
        }
        return this.classname + "; " + super.getMessage();
    }
}
