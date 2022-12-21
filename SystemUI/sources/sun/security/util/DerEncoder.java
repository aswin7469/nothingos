package sun.security.util;

import java.p026io.IOException;
import java.p026io.OutputStream;

public interface DerEncoder {
    void derEncode(OutputStream outputStream) throws IOException;
}
