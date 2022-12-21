package sun.nio.p035fs;

import java.nio.file.FileSystems;
import java.nio.file.spi.FileTypeDetector;

/* renamed from: sun.nio.fs.DefaultFileTypeDetector */
public class DefaultFileTypeDetector {
    private DefaultFileTypeDetector() {
    }

    public static FileTypeDetector create() {
        return ((UnixFileSystemProvider) FileSystems.getDefault().provider()).getFileTypeDetector();
    }
}
