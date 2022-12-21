package java.p026io;

/* renamed from: java.io.DefaultFileSystem */
class DefaultFileSystem {
    DefaultFileSystem() {
    }

    public static FileSystem getFileSystem() {
        return new UnixFileSystem();
    }
}
