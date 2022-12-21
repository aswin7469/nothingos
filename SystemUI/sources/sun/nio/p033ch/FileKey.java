package sun.nio.p033ch;

import java.p026io.FileDescriptor;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.FileKey */
public class FileKey {
    private long st_dev;
    private long st_ino;

    private native void init(FileDescriptor fileDescriptor) throws IOException;

    private FileKey() {
    }

    public static FileKey create(FileDescriptor fileDescriptor) throws IOException {
        FileKey fileKey = new FileKey();
        fileKey.init(fileDescriptor);
        return fileKey;
    }

    public int hashCode() {
        long j = this.st_dev;
        long j2 = this.st_ino;
        return ((int) (j ^ (j >>> 32))) + ((int) ((j2 >>> 32) ^ j2));
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FileKey)) {
            return false;
        }
        FileKey fileKey = (FileKey) obj;
        return this.st_dev == fileKey.st_dev && this.st_ino == fileKey.st_ino;
    }
}
