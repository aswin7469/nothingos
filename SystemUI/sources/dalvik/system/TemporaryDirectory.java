package dalvik.system;

import java.p026io.File;

public class TemporaryDirectory {
    public static void setUpDirectory(String str) {
    }

    public static synchronized void setUpDirectory(File file) {
        synchronized (TemporaryDirectory.class) {
        }
    }
}
