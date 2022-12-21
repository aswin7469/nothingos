package sun.nio.p035fs;

import java.p026io.IOException;
import java.util.Map;

/* renamed from: sun.nio.fs.DynamicFileAttributeView */
interface DynamicFileAttributeView {
    Map<String, Object> readAttributes(String[] strArr) throws IOException;

    void setAttribute(String str, Object obj) throws IOException;
}
