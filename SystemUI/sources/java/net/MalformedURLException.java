package java.net;

import java.p026io.IOException;

public class MalformedURLException extends IOException {
    private static final long serialVersionUID = -182787522200415866L;

    public MalformedURLException() {
    }

    public MalformedURLException(String str) {
        super(str);
    }
}
