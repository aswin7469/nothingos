package java.security.cert;

import java.p026io.IOException;
import java.p026io.OutputStream;

public interface Extension {
    void encode(OutputStream outputStream) throws IOException;

    String getId();

    byte[] getValue();

    boolean isCritical();
}
