package java.net;

import java.p026io.IOException;

public class UnknownServiceException extends IOException {
    private static final long serialVersionUID = -4169033248853639508L;

    public UnknownServiceException() {
    }

    public UnknownServiceException(String str) {
        super(str);
    }
}
