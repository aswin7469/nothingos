package java.nio.charset;

public class UnsupportedCharsetException extends IllegalArgumentException {
    private static final long serialVersionUID = 1490765524727386367L;
    private String charsetName;

    public UnsupportedCharsetException(String str) {
        super(String.valueOf((Object) str));
        this.charsetName = str;
    }

    public String getCharsetName() {
        return this.charsetName;
    }
}
