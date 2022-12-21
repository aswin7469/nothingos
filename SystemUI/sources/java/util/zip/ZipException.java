package java.util.zip;

import java.p026io.IOException;

public class ZipException extends IOException {
    private static final long serialVersionUID = 8000196834066748623L;

    public ZipException() {
    }

    public ZipException(String str) {
        super(str);
    }
}
