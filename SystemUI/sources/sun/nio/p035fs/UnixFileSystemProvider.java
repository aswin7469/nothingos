package sun.nio.p035fs;

import java.net.URI;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.LinkOption;
import java.nio.file.LinkPermission;
import java.nio.file.NotLinkException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.spi.FileTypeDetector;
import java.p026io.FilePermission;
import java.p026io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import sun.nio.p033ch.ThreadPool;
import sun.security.util.SecurityConstants;

/* renamed from: sun.nio.fs.UnixFileSystemProvider */
public abstract class UnixFileSystemProvider extends AbstractFileSystemProvider {
    private static final String USER_DIR = "user.dir";
    private final UnixFileSystem theFileSystem = newFileSystem(System.getProperty(USER_DIR));

    /* access modifiers changed from: package-private */
    public abstract FileStore getFileStore(UnixPath unixPath) throws IOException;

    public final String getScheme() {
        return "file";
    }

    /* access modifiers changed from: package-private */
    public abstract UnixFileSystem newFileSystem(String str);

    private void checkUri(URI uri) {
        if (!uri.getScheme().equalsIgnoreCase(getScheme())) {
            throw new IllegalArgumentException("URI does not match this provider");
        } else if (uri.getAuthority() != null) {
            throw new IllegalArgumentException("Authority component present");
        } else if (uri.getPath() == null) {
            throw new IllegalArgumentException("Path component is undefined");
        } else if (!uri.getPath().equals("/")) {
            throw new IllegalArgumentException("Path component should be '/'");
        } else if (uri.getQuery() != null) {
            throw new IllegalArgumentException("Query component present");
        } else if (uri.getFragment() != null) {
            throw new IllegalArgumentException("Fragment component present");
        }
    }

    public final FileSystem newFileSystem(URI uri, Map<String, ?> map) {
        checkUri(uri);
        throw new FileSystemAlreadyExistsException();
    }

    public final FileSystem getFileSystem(URI uri) {
        checkUri(uri);
        return this.theFileSystem;
    }

    public Path getPath(URI uri) {
        return UnixUriUtils.fromUri(this.theFileSystem, uri);
    }

    /* access modifiers changed from: package-private */
    public UnixPath checkPath(Path path) {
        path.getClass();
        if (path instanceof UnixPath) {
            return (UnixPath) path;
        }
        throw new ProviderMismatchException();
    }

    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> cls, LinkOption... linkOptionArr) {
        UnixPath unixPath = UnixPath.toUnixPath(path);
        boolean followLinks = Util.followLinks(linkOptionArr);
        if (cls == BasicFileAttributeView.class) {
            return UnixFileAttributeViews.createBasicView(unixPath, followLinks);
        }
        if (cls == PosixFileAttributeView.class) {
            return UnixFileAttributeViews.createPosixView(unixPath, followLinks);
        }
        if (cls == FileOwnerAttributeView.class) {
            return UnixFileAttributeViews.createOwnerView(unixPath, followLinks);
        }
        cls.getClass();
        FileAttributeView fileAttributeView = null;
        return null;
    }

    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> cls, LinkOption... linkOptionArr) throws IOException {
        Class cls2;
        if (cls == BasicFileAttributes.class) {
            cls2 = BasicFileAttributeView.class;
        } else if (cls == PosixFileAttributes.class) {
            cls2 = PosixFileAttributeView.class;
        } else {
            cls.getClass();
            throw new UnsupportedOperationException();
        }
        return ((BasicFileAttributeView) getFileAttributeView(path, cls2, linkOptionArr)).readAttributes();
    }

    /* access modifiers changed from: protected */
    public DynamicFileAttributeView getFileAttributeView(Path path, String str, LinkOption... linkOptionArr) {
        UnixPath unixPath = UnixPath.toUnixPath(path);
        boolean followLinks = Util.followLinks(linkOptionArr);
        if (str.equals("basic")) {
            return UnixFileAttributeViews.createBasicView(unixPath, followLinks);
        }
        if (str.equals("posix")) {
            return UnixFileAttributeViews.createPosixView(unixPath, followLinks);
        }
        if (str.equals("unix")) {
            return UnixFileAttributeViews.createUnixView(unixPath, followLinks);
        }
        if (str.equals("owner")) {
            return UnixFileAttributeViews.createOwnerView(unixPath, followLinks);
        }
        return null;
    }

    public FileChannel newFileChannel(Path path, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        UnixPath checkPath = checkPath(path);
        try {
            return UnixChannelFactory.newFileChannel(checkPath, set, UnixFileModeAttribute.toUnixMode(UnixFileModeAttribute.ALL_READWRITE, fileAttributeArr));
        } catch (UnixException e) {
            e.rethrowAsIOException(checkPath);
            return null;
        }
    }

    public AsynchronousFileChannel newAsynchronousFileChannel(Path path, Set<? extends OpenOption> set, ExecutorService executorService, FileAttribute<?>... fileAttributeArr) throws IOException {
        ThreadPool threadPool;
        UnixPath checkPath = checkPath(path);
        int unixMode = UnixFileModeAttribute.toUnixMode(UnixFileModeAttribute.ALL_READWRITE, fileAttributeArr);
        if (executorService == null) {
            threadPool = null;
        } else {
            threadPool = ThreadPool.wrap(executorService, 0);
        }
        try {
            return UnixChannelFactory.newAsynchronousFileChannel(checkPath, set, unixMode, threadPool);
        } catch (UnixException e) {
            e.rethrowAsIOException(checkPath);
            return null;
        }
    }

    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        UnixPath unixPath = UnixPath.toUnixPath(path);
        try {
            return UnixChannelFactory.newFileChannel(unixPath, set, UnixFileModeAttribute.toUnixMode(UnixFileModeAttribute.ALL_READWRITE, fileAttributeArr));
        } catch (UnixException e) {
            e.rethrowAsIOException(unixPath);
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0029 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean implDelete(java.nio.file.Path r4, boolean r5) throws java.p026io.IOException {
        /*
            r3 = this;
            sun.nio.fs.UnixPath r3 = sun.nio.p035fs.UnixPath.toUnixPath(r4)
            r3.checkDelete()
            r4 = 0
            sun.nio.fs.UnixFileAttributes r0 = sun.nio.p035fs.UnixFileAttributes.get(r3, r4)     // Catch:{ UnixException -> 0x001d }
            boolean r1 = r0.isDirectory()     // Catch:{ UnixException -> 0x001b }
            if (r1 == 0) goto L_0x0016
            sun.nio.p035fs.UnixNativeDispatcher.rmdir(r3)     // Catch:{ UnixException -> 0x001b }
            goto L_0x0019
        L_0x0016:
            sun.nio.p035fs.UnixNativeDispatcher.unlink(r3)     // Catch:{ UnixException -> 0x001b }
        L_0x0019:
            r3 = 1
            return r3
        L_0x001b:
            r1 = move-exception
            goto L_0x001f
        L_0x001d:
            r1 = move-exception
            r0 = 0
        L_0x001f:
            if (r5 != 0) goto L_0x002a
            int r5 = r1.errno()
            int r2 = sun.nio.p035fs.UnixConstants.ENOENT
            if (r5 != r2) goto L_0x002a
            return r4
        L_0x002a:
            if (r0 == 0) goto L_0x004d
            boolean r5 = r0.isDirectory()
            if (r5 == 0) goto L_0x004d
            int r5 = r1.errno()
            int r0 = sun.nio.p035fs.UnixConstants.EEXIST
            if (r5 == r0) goto L_0x0043
            int r5 = r1.errno()
            int r0 = sun.nio.p035fs.UnixConstants.ENOTEMPTY
            if (r5 == r0) goto L_0x0043
            goto L_0x004d
        L_0x0043:
            java.nio.file.DirectoryNotEmptyException r4 = new java.nio.file.DirectoryNotEmptyException
            java.lang.String r3 = r3.getPathForExceptionMessage()
            r4.<init>(r3)
            throw r4
        L_0x004d:
            r1.rethrowAsIOException((sun.nio.p035fs.UnixPath) r3)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.UnixFileSystemProvider.implDelete(java.nio.file.Path, boolean):boolean");
    }

    public void copy(Path path, Path path2, CopyOption... copyOptionArr) throws IOException {
        UnixCopyFile.copy(UnixPath.toUnixPath(path), UnixPath.toUnixPath(path2), copyOptionArr);
    }

    public void move(Path path, Path path2, CopyOption... copyOptionArr) throws IOException {
        UnixCopyFile.move(UnixPath.toUnixPath(path), UnixPath.toUnixPath(path2), copyOptionArr);
    }

    public void checkAccess(Path path, AccessMode... accessModeArr) throws IOException {
        boolean z;
        boolean z2;
        boolean z3;
        UnixPath unixPath = UnixPath.toUnixPath(path);
        boolean z4 = true;
        int i = 0;
        if (accessModeArr.length == 0) {
            z3 = false;
            z2 = false;
            z = false;
        } else {
            z3 = false;
            z2 = false;
            z = false;
            for (AccessMode ordinal : accessModeArr) {
                int i2 = C48113.$SwitchMap$java$nio$file$AccessMode[ordinal.ordinal()];
                if (i2 == 1) {
                    z3 = true;
                } else if (i2 == 2) {
                    z2 = true;
                } else if (i2 == 3) {
                    z = true;
                } else {
                    throw new AssertionError((Object) "Should not get here");
                }
            }
            z4 = false;
        }
        if (z4 || z3) {
            unixPath.checkRead();
            i = 0 | (z3 ? UnixConstants.R_OK : UnixConstants.F_OK);
        }
        if (z2) {
            unixPath.checkWrite();
            i |= UnixConstants.W_OK;
        }
        if (z) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkExec(unixPath.getPathForPermissionCheck());
            }
            i |= UnixConstants.X_OK;
        }
        try {
            UnixNativeDispatcher.access(unixPath, i);
        } catch (UnixException e) {
            e.rethrowAsIOException(unixPath);
        }
    }

    /* renamed from: sun.nio.fs.UnixFileSystemProvider$3 */
    static /* synthetic */ class C48113 {
        static final /* synthetic */ int[] $SwitchMap$java$nio$file$AccessMode;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                java.nio.file.AccessMode[] r0 = java.nio.file.AccessMode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$nio$file$AccessMode = r0
                java.nio.file.AccessMode r1 = java.nio.file.AccessMode.READ     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$nio$file$AccessMode     // Catch:{ NoSuchFieldError -> 0x001d }
                java.nio.file.AccessMode r1 = java.nio.file.AccessMode.WRITE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$java$nio$file$AccessMode     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.nio.file.AccessMode r1 = java.nio.file.AccessMode.EXECUTE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.UnixFileSystemProvider.C48113.<clinit>():void");
        }
    }

    public boolean isSameFile(Path path, Path path2) throws IOException {
        UnixPath unixPath = UnixPath.toUnixPath(path);
        if (unixPath.equals(path2)) {
            return true;
        }
        path2.getClass();
        if (!(path2 instanceof UnixPath)) {
            return false;
        }
        UnixPath unixPath2 = (UnixPath) path2;
        unixPath.checkRead();
        unixPath2.checkRead();
        try {
            try {
                return UnixFileAttributes.get(unixPath, true).isSameFile(UnixFileAttributes.get(unixPath2, true));
            } catch (UnixException e) {
                e.rethrowAsIOException(unixPath2);
                return false;
            }
        } catch (UnixException e2) {
            e2.rethrowAsIOException(unixPath);
            return false;
        }
    }

    public boolean isHidden(Path path) {
        UnixPath unixPath = UnixPath.toUnixPath(path);
        unixPath.checkRead();
        UnixPath fileName = unixPath.getFileName();
        if (fileName != null && fileName.asByteArray()[0] == 46) {
            return true;
        }
        return false;
    }

    public FileStore getFileStore(Path path) throws IOException {
        throw new SecurityException("getFileStore");
    }

    public void createDirectory(Path path, FileAttribute<?>... fileAttributeArr) throws IOException {
        UnixPath unixPath = UnixPath.toUnixPath(path);
        unixPath.checkWrite();
        try {
            UnixNativeDispatcher.mkdir(unixPath, UnixFileModeAttribute.toUnixMode(UnixFileModeAttribute.ALL_PERMISSIONS, fileAttributeArr));
        } catch (UnixException e) {
            if (e.errno() != UnixConstants.EISDIR) {
                e.rethrowAsIOException(unixPath);
                return;
            }
            throw new FileAlreadyExistsException(unixPath.toString());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0065  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.nio.file.DirectoryStream<java.nio.file.Path> newDirectoryStream(java.nio.file.Path r7, java.nio.file.DirectoryStream.Filter<? super java.nio.file.Path> r8) throws java.p026io.IOException {
        /*
            r6 = this;
            sun.nio.fs.UnixPath r1 = sun.nio.p035fs.UnixPath.toUnixPath(r7)
            r1.checkRead()
            r8.getClass()
            boolean r6 = sun.nio.p035fs.UnixNativeDispatcher.openatSupported()
            if (r6 == 0) goto L_0x0014
            int r6 = sun.nio.p035fs.UnixConstants.O_NOFOLLOW
            if (r6 != 0) goto L_0x002a
        L_0x0014:
            long r6 = sun.nio.p035fs.UnixNativeDispatcher.opendir(r1)     // Catch:{ UnixException -> 0x001e }
            sun.nio.fs.UnixDirectoryStream r0 = new sun.nio.fs.UnixDirectoryStream     // Catch:{ UnixException -> 0x001e }
            r0.<init>(r1, r6, r8)     // Catch:{ UnixException -> 0x001e }
            return r0
        L_0x001e:
            r6 = move-exception
            int r7 = r6.errno()
            int r0 = sun.nio.p035fs.UnixConstants.ENOTDIR
            if (r7 == r0) goto L_0x006f
            r6.rethrowAsIOException((sun.nio.p035fs.UnixPath) r1)
        L_0x002a:
            r6 = -1
            int r7 = sun.nio.p035fs.UnixConstants.O_RDONLY     // Catch:{ UnixException -> 0x0042 }
            r0 = 0
            int r7 = sun.nio.p035fs.UnixNativeDispatcher.open(r1, r7, r0)     // Catch:{ UnixException -> 0x0042 }
            int r0 = sun.nio.p035fs.UnixNativeDispatcher.dup(r7)     // Catch:{ UnixException -> 0x003f }
            long r6 = sun.nio.p035fs.UnixNativeDispatcher.fdopendir(r7)     // Catch:{ UnixException -> 0x003d }
        L_0x003a:
            r2 = r6
            r4 = r0
            goto L_0x005d
        L_0x003d:
            r2 = move-exception
            goto L_0x0045
        L_0x003f:
            r2 = move-exception
            r0 = r6
            goto L_0x0045
        L_0x0042:
            r2 = move-exception
            r7 = r6
            r0 = r7
        L_0x0045:
            if (r7 == r6) goto L_0x004a
            sun.nio.p035fs.UnixNativeDispatcher.close(r7)
        L_0x004a:
            if (r0 == r6) goto L_0x004f
            sun.nio.p035fs.UnixNativeDispatcher.close(r0)
        L_0x004f:
            int r6 = r2.errno()
            int r7 = sun.nio.p035fs.UnixConstants.ENOTDIR
            if (r6 == r7) goto L_0x0065
            r2.rethrowAsIOException((sun.nio.p035fs.UnixPath) r1)
            r6 = 0
            goto L_0x003a
        L_0x005d:
            sun.nio.fs.UnixSecureDirectoryStream r6 = new sun.nio.fs.UnixSecureDirectoryStream
            r0 = r6
            r5 = r8
            r0.<init>(r1, r2, r4, r5)
            return r6
        L_0x0065:
            java.nio.file.NotDirectoryException r6 = new java.nio.file.NotDirectoryException
            java.lang.String r7 = r1.getPathForExceptionMessage()
            r6.<init>(r7)
            throw r6
        L_0x006f:
            java.nio.file.NotDirectoryException r6 = new java.nio.file.NotDirectoryException
            java.lang.String r7 = r1.getPathForExceptionMessage()
            r6.<init>(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.UnixFileSystemProvider.newDirectoryStream(java.nio.file.Path, java.nio.file.DirectoryStream$Filter):java.nio.file.DirectoryStream");
    }

    public void createSymbolicLink(Path path, Path path2, FileAttribute<?>... fileAttributeArr) throws IOException {
        UnixPath unixPath = UnixPath.toUnixPath(path);
        UnixPath unixPath2 = UnixPath.toUnixPath(path2);
        if (fileAttributeArr.length <= 0) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(new LinkPermission("symbolic"));
                unixPath.checkWrite();
            }
            try {
                UnixNativeDispatcher.symlink(unixPath2.asByteArray(), unixPath);
            } catch (UnixException e) {
                e.rethrowAsIOException(unixPath);
            }
        } else {
            UnixFileModeAttribute.toUnixMode(0, fileAttributeArr);
            throw new UnsupportedOperationException("Initial file attributesnot supported when creating symbolic link");
        }
    }

    public void createLink(Path path, Path path2) throws IOException {
        UnixPath unixPath = UnixPath.toUnixPath(path);
        UnixPath unixPath2 = UnixPath.toUnixPath(path2);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new LinkPermission("hard"));
            unixPath.checkWrite();
            unixPath2.checkWrite();
        }
        try {
            UnixNativeDispatcher.link(unixPath2, unixPath);
        } catch (UnixException e) {
            e.rethrowAsIOException(unixPath, unixPath2);
        }
    }

    public Path readSymbolicLink(Path path) throws IOException {
        UnixPath unixPath = UnixPath.toUnixPath(path);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new FilePermission(unixPath.getPathForPermissionCheck(), SecurityConstants.FILE_READLINK_ACTION));
        }
        try {
            return new UnixPath(unixPath.getFileSystem(), UnixNativeDispatcher.readlink(unixPath));
        } catch (UnixException e) {
            if (e.errno() != UnixConstants.EINVAL) {
                e.rethrowAsIOException(unixPath);
                return null;
            }
            throw new NotLinkException(unixPath.getPathForExceptionMessage());
        }
    }

    /* access modifiers changed from: package-private */
    public FileTypeDetector getFileTypeDetector() {
        return new AbstractFileTypeDetector() {
            public String implProbeContentType(Path path) {
                return null;
            }
        };
    }

    /* access modifiers changed from: package-private */
    public final FileTypeDetector chain(final AbstractFileTypeDetector... abstractFileTypeDetectorArr) {
        return new AbstractFileTypeDetector() {
            /* access modifiers changed from: protected */
            public String implProbeContentType(Path path) throws IOException {
                for (AbstractFileTypeDetector implProbeContentType : abstractFileTypeDetectorArr) {
                    String implProbeContentType2 = implProbeContentType.implProbeContentType(path);
                    if (implProbeContentType2 != null && !implProbeContentType2.isEmpty()) {
                        return implProbeContentType2;
                    }
                }
                return null;
            }
        };
    }
}
