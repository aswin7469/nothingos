package java.security;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;

@Deprecated(forRemoval = true, since = "1.2")
public interface Certificate {
    void decode(InputStream inputStream) throws KeyException, IOException;

    void encode(OutputStream outputStream) throws KeyException, IOException;

    String getFormat();

    Principal getGuarantor();

    Principal getPrincipal();

    PublicKey getPublicKey();

    String toString(boolean z);
}
