package java.nio.file.spi;

import java.nio.file.Path;
import java.p026io.IOException;

public abstract class FileTypeDetector {
    public abstract String probeContentType(Path path) throws IOException;

    private static Void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return null;
        }
        securityManager.checkPermission(new RuntimePermission("fileTypeDetector"));
        return null;
    }

    private FileTypeDetector(Void voidR) {
    }

    protected FileTypeDetector() {
        this(checkPermission());
    }
}
