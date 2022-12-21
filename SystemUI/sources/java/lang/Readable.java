package java.lang;

import java.nio.CharBuffer;
import java.p026io.IOException;

public interface Readable {
    int read(CharBuffer charBuffer) throws IOException;
}
