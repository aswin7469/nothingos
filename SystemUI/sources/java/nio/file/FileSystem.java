package java.nio.file;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.p026io.Closeable;
import java.p026io.IOException;
import java.util.Set;

public abstract class FileSystem implements Closeable {
    public abstract void close() throws IOException;

    public abstract Iterable<FileStore> getFileStores();

    public abstract Path getPath(String str, String... strArr);

    public abstract PathMatcher getPathMatcher(String str);

    public abstract Iterable<Path> getRootDirectories();

    public abstract String getSeparator();

    public abstract UserPrincipalLookupService getUserPrincipalLookupService();

    public abstract boolean isOpen();

    public abstract boolean isReadOnly();

    public abstract WatchService newWatchService() throws IOException;

    public abstract FileSystemProvider provider();

    public abstract Set<String> supportedFileAttributeViews();

    protected FileSystem() {
    }
}
