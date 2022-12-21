package org.w3c.dom.p031ls;

import java.p026io.OutputStream;
import java.p026io.Writer;

/* renamed from: org.w3c.dom.ls.LSOutput */
public interface LSOutput {
    OutputStream getByteStream();

    Writer getCharacterStream();

    String getEncoding();

    String getSystemId();

    void setByteStream(OutputStream outputStream);

    void setCharacterStream(Writer writer);

    void setEncoding(String str);

    void setSystemId(String str);
}
