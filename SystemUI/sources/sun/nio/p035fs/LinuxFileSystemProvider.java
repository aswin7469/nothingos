package sun.nio.p035fs;

import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileTypeDetector;
import java.p026io.IOException;

/* renamed from: sun.nio.fs.LinuxFileSystemProvider */
public class LinuxFileSystemProvider extends UnixFileSystemProvider {
    /* access modifiers changed from: package-private */
    public LinuxFileSystem newFileSystem(String str) {
        return new LinuxFileSystem(this, str);
    }

    /* access modifiers changed from: package-private */
    public LinuxFileStore getFileStore(UnixPath unixPath) throws IOException {
        throw new SecurityException("getFileStore");
    }

    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> cls, LinkOption... linkOptionArr) {
        return super.getFileAttributeView(path, cls, linkOptionArr);
    }

    public DynamicFileAttributeView getFileAttributeView(Path path, String str, LinkOption... linkOptionArr) {
        return super.getFileAttributeView(path, str, linkOptionArr);
    }

    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> cls, LinkOption... linkOptionArr) throws IOException {
        return super.readAttributes(path, cls, linkOptionArr);
    }

    /* access modifiers changed from: package-private */
    public FileTypeDetector getFileTypeDetector() {
        return new MimeTypesFileTypeDetector();
    }
}
