package sun.security.pkcs;

import java.p026io.IOException;

public class ParsingException extends IOException {
    private static final long serialVersionUID = -6316569918966181883L;

    public ParsingException() {
    }

    public ParsingException(String str) {
        super(str);
    }
}
