package java.nio.file.spi;

import java.net.URI;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public abstract class FileSystemProvider {
    private static volatile List<FileSystemProvider> installedProviders = null;
    private static boolean loadingProviders = false;
    private static final Object lock = new Object();

    public abstract void checkAccess(Path path, AccessMode... accessModeArr) throws IOException;

    public abstract void copy(Path path, Path path2, CopyOption... copyOptionArr) throws IOException;

    public abstract void createDirectory(Path path, FileAttribute<?>... fileAttributeArr) throws IOException;

    public abstract void delete(Path path) throws IOException;

    public abstract <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> cls, LinkOption... linkOptionArr);

    public abstract FileStore getFileStore(Path path) throws IOException;

    public abstract FileSystem getFileSystem(URI uri);

    public abstract Path getPath(URI uri);

    public abstract String getScheme();

    public abstract boolean isHidden(Path path) throws IOException;

    public abstract boolean isSameFile(Path path, Path path2) throws IOException;

    public abstract void move(Path path, Path path2, CopyOption... copyOptionArr) throws IOException;

    public abstract SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException;

    public abstract DirectoryStream<Path> newDirectoryStream(Path path, DirectoryStream.Filter<? super Path> filter) throws IOException;

    public abstract FileSystem newFileSystem(URI uri, Map<String, ?> map) throws IOException;

    public abstract <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> cls, LinkOption... linkOptionArr) throws IOException;

    public abstract Map<String, Object> readAttributes(Path path, String str, LinkOption... linkOptionArr) throws IOException;

    public abstract void setAttribute(Path path, String str, Object obj, LinkOption... linkOptionArr) throws IOException;

    private static Void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return null;
        }
        securityManager.checkPermission(new RuntimePermission("fileSystemProvider"));
        return null;
    }

    private FileSystemProvider(Void voidR) {
    }

    protected FileSystemProvider() {
        this(checkPermission());
    }

    /* access modifiers changed from: private */
    public static List<FileSystemProvider> loadInstalledProviders() {
        boolean z;
        ArrayList arrayList = new ArrayList();
        Iterator<S> it = ServiceLoader.load(FileSystemProvider.class, ClassLoader.getSystemClassLoader()).iterator();
        while (it.hasNext()) {
            FileSystemProvider fileSystemProvider = (FileSystemProvider) it.next();
            String scheme = fileSystemProvider.getScheme();
            if (!scheme.equalsIgnoreCase("file")) {
                Iterator it2 = arrayList.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        if (((FileSystemProvider) it2.next()).getScheme().equalsIgnoreCase(scheme)) {
                            z = true;
                            break;
                        }
                    } else {
                        z = false;
                        break;
                    }
                }
                if (!z) {
                    arrayList.add(fileSystemProvider);
                }
            }
        }
        return arrayList;
    }

    public static List<FileSystemProvider> installedProviders() {
        if (installedProviders == null) {
            FileSystemProvider provider = FileSystems.getDefault().provider();
            synchronized (lock) {
                if (installedProviders == null) {
                    if (!loadingProviders) {
                        loadingProviders = true;
                        List list = (List) AccessController.doPrivileged(new PrivilegedAction<List<FileSystemProvider>>() {
                            public List<FileSystemProvider> run() {
                                return FileSystemProvider.loadInstalledProviders();
                            }
                        });
                        list.add(0, provider);
                        installedProviders = Collections.unmodifiableList(list);
                    } else {
                        throw new Error("Circular loading of installed providers detected");
                    }
                }
            }
        }
        return installedProviders;
    }

    public FileSystem newFileSystem(Path path, Map<String, ?> map) throws IOException {
        throw new UnsupportedOperationException();
    }

    public InputStream newInputStream(Path path, OpenOption... openOptionArr) throws IOException {
        if (openOptionArr.length > 0) {
            for (StandardOpenOption standardOpenOption : openOptionArr) {
                if (standardOpenOption == StandardOpenOption.APPEND || standardOpenOption == StandardOpenOption.WRITE) {
                    throw new UnsupportedOperationException("'" + standardOpenOption + "' not allowed");
                }
            }
        }
        return Channels.newInputStream((ReadableByteChannel) Files.newByteChannel(path, openOptionArr));
    }

    public OutputStream newOutputStream(Path path, OpenOption... openOptionArr) throws IOException {
        int length = openOptionArr.length;
        HashSet hashSet = new HashSet(length + 3);
        if (length == 0) {
            hashSet.add(StandardOpenOption.CREATE);
            hashSet.add(StandardOpenOption.TRUNCATE_EXISTING);
        } else {
            int length2 = openOptionArr.length;
            int i = 0;
            while (i < length2) {
                StandardOpenOption standardOpenOption = openOptionArr[i];
                if (standardOpenOption != StandardOpenOption.READ) {
                    hashSet.add(standardOpenOption);
                    i++;
                } else {
                    throw new IllegalArgumentException("READ not allowed");
                }
            }
        }
        hashSet.add(StandardOpenOption.WRITE);
        return Channels.newOutputStream((WritableByteChannel) newByteChannel(path, hashSet, new FileAttribute[0]));
    }

    public FileChannel newFileChannel(Path path, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        throw new UnsupportedOperationException();
    }

    public AsynchronousFileChannel newAsynchronousFileChannel(Path path, Set<? extends OpenOption> set, ExecutorService executorService, FileAttribute<?>... fileAttributeArr) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void createSymbolicLink(Path path, Path path2, FileAttribute<?>... fileAttributeArr) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void createLink(Path path, Path path2) throws IOException {
        throw new UnsupportedOperationException();
    }

    public boolean deleteIfExists(Path path) throws IOException {
        try {
            delete(path);
            return true;
        } catch (NoSuchFileException unused) {
            return false;
        }
    }

    public Path readSymbolicLink(Path path) throws IOException {
        throw new UnsupportedOperationException();
    }
}
