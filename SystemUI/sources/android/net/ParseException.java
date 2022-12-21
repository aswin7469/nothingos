package android.net;

public class ParseException extends RuntimeException {
    public String response;

    public ParseException(String str) {
        super(str);
        this.response = str;
    }

    public ParseException(String str, Throwable th) {
        super(str, th);
        this.response = str;
    }
}
