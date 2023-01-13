package java.nio.file;

import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileTreeWalker;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.nio.file.spi.FileSystemProvider;
import java.nio.file.spi.FileTypeDetector;
import java.p026io.BufferedReader;
import java.p026io.BufferedWriter;
import java.p026io.Closeable;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.InputStreamReader;
import java.p026io.OutputStream;
import java.p026io.OutputStreamWriter;
import java.p026io.UncheckedIOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.Spliterators;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import sun.nio.p035fs.DefaultFileTypeDetector;

public final class Files {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int BUFFER_SIZE = 8192;
    private static final int MAX_BUFFER_SIZE = 2147483639;

    private Files() {
    }

    private static FileSystemProvider provider(Path path) {
        return path.getFileSystem().provider();
    }

    private static Runnable asUncheckedRunnable(Closeable closeable) {
        return new Files$$ExternalSyntheticLambda4(closeable);
    }

    static /* synthetic */ void lambda$asUncheckedRunnable$0(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static InputStream newInputStream(Path path, OpenOption... openOptionArr) throws IOException {
        return provider(path).newInputStream(path, openOptionArr);
    }

    public static OutputStream newOutputStream(Path path, OpenOption... openOptionArr) throws IOException {
        return provider(path).newOutputStream(path, openOptionArr);
    }

    public static SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> set, FileAttribute<?>... fileAttributeArr) throws IOException {
        return provider(path).newByteChannel(path, set, fileAttributeArr);
    }

    public static SeekableByteChannel newByteChannel(Path path, OpenOption... openOptionArr) throws IOException {
        HashSet hashSet = new HashSet(openOptionArr.length);
        Collections.addAll(hashSet, openOptionArr);
        return newByteChannel(path, hashSet, new FileAttribute[0]);
    }

    private static class AcceptAllFilter implements DirectoryStream.Filter<Path> {
        static final AcceptAllFilter FILTER = new AcceptAllFilter();

        public boolean accept(Path path) {
            return true;
        }

        private AcceptAllFilter() {
        }
    }

    public static DirectoryStream<Path> newDirectoryStream(Path path) throws IOException {
        return provider(path).newDirectoryStream(path, AcceptAllFilter.FILTER);
    }

    public static DirectoryStream<Path> newDirectoryStream(Path path, String str) throws IOException {
        if (str.equals("*")) {
            return newDirectoryStream(path);
        }
        FileSystem fileSystem = path.getFileSystem();
        final PathMatcher pathMatcher = fileSystem.getPathMatcher("glob:" + str);
        return fileSystem.provider().newDirectoryStream(path, new DirectoryStream.Filter<Path>() {
            public boolean accept(Path path) {
                return PathMatcher.this.matches(path.getFileName());
            }
        });
    }

    public static DirectoryStream<Path> newDirectoryStream(Path path, DirectoryStream.Filter<? super Path> filter) throws IOException {
        return provider(path).newDirectoryStream(path, filter);
    }

    public static Path createFile(Path path, FileAttribute<?>... fileAttributeArr) throws IOException {
        newByteChannel(path, EnumSet.m1723of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE), fileAttributeArr).close();
        return path;
    }

    public static Path createDirectory(Path path, FileAttribute<?>... fileAttributeArr) throws IOException {
        provider(path).createDirectory(path, fileAttributeArr);
        return path;
    }

    public static Path createDirectories(Path path, FileAttribute<?>... fileAttributeArr) throws IOException {
        try {
            createAndCheckIsDirectory(path, fileAttributeArr);
            return path;
        } catch (FileAlreadyExistsException e) {
            throw e;
        } catch (IOException unused) {
            try {
                path = path.toAbsolutePath();
                e = null;
            } catch (SecurityException e2) {
                e = e2;
            }
            Path parent = path.getParent();
            while (parent != null) {
                try {
                    provider(parent).checkAccess(parent, new AccessMode[0]);
                    break;
                } catch (NoSuchFileException unused2) {
                    parent = parent.getParent();
                }
            }
            if (parent != null) {
                for (Path resolve : parent.relativize(path)) {
                    parent = parent.resolve(resolve);
                    createAndCheckIsDirectory(parent, fileAttributeArr);
                }
                return path;
            } else if (e == null) {
                throw new FileSystemException(path.toString(), (String) null, "Unable to determine if root directory exists");
            } else {
                throw e;
            }
        }
    }

    private static void createAndCheckIsDirectory(Path path, FileAttribute<?>... fileAttributeArr) throws IOException {
        try {
            createDirectory(path, fileAttributeArr);
        } catch (FileAlreadyExistsException e) {
            if (!isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
                throw e;
            }
        }
    }

    public static Path createTempFile(Path path, String str, String str2, FileAttribute<?>... fileAttributeArr) throws IOException {
        return TempFileHelper.createTempFile((Path) Objects.requireNonNull(path), str, str2, fileAttributeArr);
    }

    public static Path createTempFile(String str, String str2, FileAttribute<?>... fileAttributeArr) throws IOException {
        return TempFileHelper.createTempFile((Path) null, str, str2, fileAttributeArr);
    }

    public static Path createTempDirectory(Path path, String str, FileAttribute<?>... fileAttributeArr) throws IOException {
        return TempFileHelper.createTempDirectory((Path) Objects.requireNonNull(path), str, fileAttributeArr);
    }

    public static Path createTempDirectory(String str, FileAttribute<?>... fileAttributeArr) throws IOException {
        return TempFileHelper.createTempDirectory((Path) null, str, fileAttributeArr);
    }

    public static Path createSymbolicLink(Path path, Path path2, FileAttribute<?>... fileAttributeArr) throws IOException {
        provider(path).createSymbolicLink(path, path2, fileAttributeArr);
        return path;
    }

    public static Path createLink(Path path, Path path2) throws IOException {
        provider(path).createLink(path, path2);
        return path;
    }

    public static void delete(Path path) throws IOException {
        provider(path).delete(path);
    }

    public static boolean deleteIfExists(Path path) throws IOException {
        return provider(path).deleteIfExists(path);
    }

    public static Path copy(Path path, Path path2, CopyOption... copyOptionArr) throws IOException {
        FileSystemProvider provider = provider(path);
        if (provider(path2) == provider) {
            provider.copy(path, path2, copyOptionArr);
        } else {
            CopyMoveHelper.copyToForeignTarget(path, path2, copyOptionArr);
        }
        return path2;
    }

    public static Path move(Path path, Path path2, CopyOption... copyOptionArr) throws IOException {
        FileSystemProvider provider = provider(path);
        if (provider(path2) == provider) {
            provider.move(path, path2, copyOptionArr);
        } else {
            CopyMoveHelper.moveToForeignTarget(path, path2, copyOptionArr);
        }
        return path2;
    }

    public static Path readSymbolicLink(Path path) throws IOException {
        return provider(path).readSymbolicLink(path);
    }

    public static FileStore getFileStore(Path path) throws IOException {
        return provider(path).getFileStore(path);
    }

    public static boolean isSameFile(Path path, Path path2) throws IOException {
        return provider(path).isSameFile(path, path2);
    }

    public static boolean isHidden(Path path) throws IOException {
        return provider(path).isHidden(path);
    }

    private static class FileTypeDetectors {
        static final FileTypeDetector defaultFileTypeDetector = createDefaultFileTypeDetector();
        static final List<FileTypeDetector> installeDetectors = loadInstalledDetectors();

        private FileTypeDetectors() {
        }

        private static FileTypeDetector createDefaultFileTypeDetector() {
            return (FileTypeDetector) AccessController.doPrivileged(new PrivilegedAction<FileTypeDetector>() {
                public FileTypeDetector run() {
                    return DefaultFileTypeDetector.create();
                }
            });
        }

        private static List<FileTypeDetector> loadInstalledDetectors() {
            return (List) AccessController.doPrivileged(new PrivilegedAction<List<FileTypeDetector>>() {
                public List<FileTypeDetector> run() {
                    ArrayList arrayList = new ArrayList();
                    Iterator<S> it = ServiceLoader.load(FileTypeDetector.class, ClassLoader.getSystemClassLoader()).iterator();
                    while (it.hasNext()) {
                        arrayList.add((FileTypeDetector) it.next());
                    }
                    return arrayList;
                }
            });
        }
    }

    public static String probeContentType(Path path) throws IOException {
        for (FileTypeDetector probeContentType : FileTypeDetectors.installeDetectors) {
            String probeContentType2 = probeContentType.probeContentType(path);
            if (probeContentType2 != null) {
                return probeContentType2;
            }
        }
        return FileTypeDetectors.defaultFileTypeDetector.probeContentType(path);
    }

    public static <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> cls, LinkOption... linkOptionArr) {
        return provider(path).getFileAttributeView(path, cls, linkOptionArr);
    }

    public static <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> cls, LinkOption... linkOptionArr) throws IOException {
        return provider(path).readAttributes(path, cls, linkOptionArr);
    }

    public static Path setAttribute(Path path, String str, Object obj, LinkOption... linkOptionArr) throws IOException {
        provider(path).setAttribute(path, str, obj, linkOptionArr);
        return path;
    }

    public static Object getAttribute(Path path, String str, LinkOption... linkOptionArr) throws IOException {
        if (str.indexOf(42) >= 0 || str.indexOf(44) >= 0) {
            throw new IllegalArgumentException(str);
        }
        Map<String, Object> readAttributes = readAttributes(path, str, linkOptionArr);
        int indexOf = str.indexOf(58);
        if (indexOf != -1) {
            str = indexOf == str.length() ? "" : str.substring(indexOf + 1);
        }
        return readAttributes.get(str);
    }

    public static Map<String, Object> readAttributes(Path path, String str, LinkOption... linkOptionArr) throws IOException {
        return provider(path).readAttributes(path, str, linkOptionArr);
    }

    public static Set<PosixFilePermission> getPosixFilePermissions(Path path, LinkOption... linkOptionArr) throws IOException {
        return ((PosixFileAttributes) readAttributes(path, PosixFileAttributes.class, linkOptionArr)).permissions();
    }

    public static Path setPosixFilePermissions(Path path, Set<PosixFilePermission> set) throws IOException {
        PosixFileAttributeView posixFileAttributeView = (PosixFileAttributeView) getFileAttributeView(path, PosixFileAttributeView.class, new LinkOption[0]);
        if (posixFileAttributeView != null) {
            posixFileAttributeView.setPermissions(set);
            return path;
        }
        throw new UnsupportedOperationException();
    }

    public static UserPrincipal getOwner(Path path, LinkOption... linkOptionArr) throws IOException {
        FileOwnerAttributeView fileOwnerAttributeView = (FileOwnerAttributeView) getFileAttributeView(path, FileOwnerAttributeView.class, linkOptionArr);
        if (fileOwnerAttributeView != null) {
            return fileOwnerAttributeView.getOwner();
        }
        throw new UnsupportedOperationException();
    }

    public static Path setOwner(Path path, UserPrincipal userPrincipal) throws IOException {
        FileOwnerAttributeView fileOwnerAttributeView = (FileOwnerAttributeView) getFileAttributeView(path, FileOwnerAttributeView.class, new LinkOption[0]);
        if (fileOwnerAttributeView != null) {
            fileOwnerAttributeView.setOwner(userPrincipal);
            return path;
        }
        throw new UnsupportedOperationException();
    }

    public static boolean isSymbolicLink(Path path) {
        try {
            return readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS).isSymbolicLink();
        } catch (IOException unused) {
            return false;
        }
    }

    public static boolean isDirectory(Path path, LinkOption... linkOptionArr) {
        try {
            return readAttributes(path, BasicFileAttributes.class, linkOptionArr).isDirectory();
        } catch (IOException unused) {
            return false;
        }
    }

    public static boolean isRegularFile(Path path, LinkOption... linkOptionArr) {
        try {
            return readAttributes(path, BasicFileAttributes.class, linkOptionArr).isRegularFile();
        } catch (IOException unused) {
            return false;
        }
    }

    public static FileTime getLastModifiedTime(Path path, LinkOption... linkOptionArr) throws IOException {
        return readAttributes(path, BasicFileAttributes.class, linkOptionArr).lastModifiedTime();
    }

    public static Path setLastModifiedTime(Path path, FileTime fileTime) throws IOException {
        ((BasicFileAttributeView) getFileAttributeView(path, BasicFileAttributeView.class, new LinkOption[0])).setTimes(fileTime, (FileTime) null, (FileTime) null);
        return path;
    }

    public static long size(Path path) throws IOException {
        return readAttributes(path, BasicFileAttributes.class, new LinkOption[0]).size();
    }

    private static boolean followLinks(LinkOption... linkOptionArr) {
        int length = linkOptionArr.length;
        boolean z = true;
        int i = 0;
        while (i < length) {
            LinkOption linkOption = linkOptionArr[i];
            if (linkOption == LinkOption.NOFOLLOW_LINKS) {
                i++;
                z = false;
            } else {
                linkOption.getClass();
                throw new AssertionError((Object) "Should not get here");
            }
        }
        return z;
    }

    public static boolean exists(Path path, LinkOption... linkOptionArr) {
        try {
            if (followLinks(linkOptionArr)) {
                provider(path).checkAccess(path, new AccessMode[0]);
            } else {
                readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            }
            return true;
        } catch (IOException unused) {
            return false;
        }
    }

    public static boolean notExists(Path path, LinkOption... linkOptionArr) {
        try {
            if (followLinks(linkOptionArr)) {
                provider(path).checkAccess(path, new AccessMode[0]);
            } else {
                readAttributes(path, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
            }
        } catch (NoSuchFileException unused) {
            return true;
        } catch (IOException unused2) {
        }
        return false;
    }

    private static boolean isAccessible(Path path, AccessMode... accessModeArr) {
        try {
            provider(path).checkAccess(path, accessModeArr);
            return true;
        } catch (IOException unused) {
            return false;
        }
    }

    public static boolean isReadable(Path path) {
        return isAccessible(path, AccessMode.READ);
    }

    public static boolean isWritable(Path path) {
        return isAccessible(path, AccessMode.WRITE);
    }

    public static boolean isExecutable(Path path) {
        return isAccessible(path, AccessMode.EXECUTE);
    }

    public static Path walkFileTree(Path path, Set<FileVisitOption> set, int i, FileVisitor<? super Path> fileVisitor) throws IOException {
        FileVisitResult fileVisitResult;
        FileTreeWalker fileTreeWalker = new FileTreeWalker(set, i);
        try {
            FileTreeWalker.Event walk = fileTreeWalker.walk(path);
            while (true) {
                int i2 = C43743.$SwitchMap$java$nio$file$FileTreeWalker$EventType[walk.type().ordinal()];
                if (i2 == 1) {
                    IOException ioeException = walk.ioeException();
                    fileVisitResult = ioeException == null ? fileVisitor.visitFile(walk.file(), walk.attributes()) : fileVisitor.visitFileFailed(walk.file(), ioeException);
                } else if (i2 == 2) {
                    fileVisitResult = fileVisitor.preVisitDirectory(walk.file(), walk.attributes());
                    if (fileVisitResult == FileVisitResult.SKIP_SUBTREE || fileVisitResult == FileVisitResult.SKIP_SIBLINGS) {
                        fileTreeWalker.pop();
                    }
                } else if (i2 == 3) {
                    fileVisitResult = fileVisitor.postVisitDirectory(walk.file(), walk.ioeException());
                    if (fileVisitResult == FileVisitResult.SKIP_SIBLINGS) {
                        fileVisitResult = FileVisitResult.CONTINUE;
                    }
                } else {
                    throw new AssertionError((Object) "Should not get here");
                }
                if (Objects.requireNonNull(fileVisitResult) != FileVisitResult.CONTINUE) {
                    if (fileVisitResult == FileVisitResult.TERMINATE) {
                        break;
                    } else if (fileVisitResult == FileVisitResult.SKIP_SIBLINGS) {
                        fileTreeWalker.skipRemainingSiblings();
                    }
                }
                walk = fileTreeWalker.next();
                if (walk == null) {
                    break;
                }
            }
            fileTreeWalker.close();
            return path;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    /* renamed from: java.nio.file.Files$3 */
    static /* synthetic */ class C43743 {
        static final /* synthetic */ int[] $SwitchMap$java$nio$file$FileTreeWalker$EventType;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                java.nio.file.FileTreeWalker$EventType[] r0 = java.nio.file.FileTreeWalker.EventType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$nio$file$FileTreeWalker$EventType = r0
                java.nio.file.FileTreeWalker$EventType r1 = java.nio.file.FileTreeWalker.EventType.ENTRY     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$nio$file$FileTreeWalker$EventType     // Catch:{ NoSuchFieldError -> 0x001d }
                java.nio.file.FileTreeWalker$EventType r1 = java.nio.file.FileTreeWalker.EventType.START_DIRECTORY     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$java$nio$file$FileTreeWalker$EventType     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.nio.file.FileTreeWalker$EventType r1 = java.nio.file.FileTreeWalker.EventType.END_DIRECTORY     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.nio.file.Files.C43743.<clinit>():void");
        }
    }

    public static Path walkFileTree(Path path, FileVisitor<? super Path> fileVisitor) throws IOException {
        return walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, fileVisitor);
    }

    public static BufferedReader newBufferedReader(Path path, Charset charset) throws IOException {
        return new BufferedReader(new InputStreamReader(newInputStream(path, new OpenOption[0]), charset.newDecoder()));
    }

    public static BufferedReader newBufferedReader(Path path) throws IOException {
        return newBufferedReader(path, StandardCharsets.UTF_8);
    }

    public static BufferedWriter newBufferedWriter(Path path, Charset charset, OpenOption... openOptionArr) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(newOutputStream(path, openOptionArr), charset.newEncoder()));
    }

    public static BufferedWriter newBufferedWriter(Path path, OpenOption... openOptionArr) throws IOException {
        return newBufferedWriter(path, StandardCharsets.UTF_8, openOptionArr);
    }

    private static long copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[8192];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read <= 0) {
                return j;
            }
            outputStream.write(bArr, 0, read);
            j += (long) read;
        }
    }

    public static long copy(InputStream inputStream, Path path, CopyOption... copyOptionArr) throws IOException {
        SecurityException e;
        Objects.requireNonNull(inputStream);
        int length = copyOptionArr.length;
        int i = 0;
        boolean z = false;
        while (i < length) {
            StandardCopyOption standardCopyOption = copyOptionArr[i];
            if (standardCopyOption == StandardCopyOption.REPLACE_EXISTING) {
                i++;
                z = true;
            } else if (standardCopyOption == null) {
                throw new NullPointerException("options contains 'null'");
            } else {
                throw new UnsupportedOperationException(standardCopyOption + " not supported");
            }
        }
        if (z) {
            try {
                deleteIfExists(path);
            } catch (SecurityException e2) {
                e = e2;
            }
        }
        e = null;
        try {
            OutputStream newOutputStream = newOutputStream(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            try {
                long copy = copy(inputStream, newOutputStream);
                if (newOutputStream != null) {
                    newOutputStream.close();
                }
                return copy;
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
            throw th;
        } catch (FileAlreadyExistsException e3) {
            if (e != null) {
                throw e;
            }
            throw e3;
        }
    }

    public static long copy(Path path, OutputStream outputStream) throws IOException {
        Objects.requireNonNull(outputStream);
        InputStream newInputStream = newInputStream(path, new OpenOption[0]);
        try {
            long copy = copy(newInputStream, outputStream);
            if (newInputStream != null) {
                newInputStream.close();
            }
            return copy;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static byte[] read(java.p026io.InputStream r5, int r6) throws java.p026io.IOException {
        /*
            byte[] r0 = new byte[r6]
            r1 = 0
        L_0x0003:
            int r2 = r6 - r1
            int r2 = r5.read(r0, r1, r2)
            if (r2 <= 0) goto L_0x000d
            int r1 = r1 + r2
            goto L_0x0003
        L_0x000d:
            if (r2 < 0) goto L_0x003c
            int r2 = r5.read()
            if (r2 >= 0) goto L_0x0016
            goto L_0x003c
        L_0x0016:
            r3 = 2147483639(0x7ffffff7, float:NaN)
            int r4 = r3 - r6
            if (r6 > r4) goto L_0x0026
            int r6 = r6 << 1
            r3 = 8192(0x2000, float:1.14794E-41)
            int r6 = java.lang.Math.max((int) r6, (int) r3)
            goto L_0x0029
        L_0x0026:
            if (r6 == r3) goto L_0x0034
            r6 = r3
        L_0x0029:
            byte[] r0 = java.util.Arrays.copyOf((byte[]) r0, (int) r6)
            int r3 = r1 + 1
            byte r2 = (byte) r2
            r0[r1] = r2
            r1 = r3
            goto L_0x0003
        L_0x0034:
            java.lang.OutOfMemoryError r5 = new java.lang.OutOfMemoryError
            java.lang.String r6 = "Required array size too large"
            r5.<init>(r6)
            throw r5
        L_0x003c:
            if (r6 != r1) goto L_0x003f
            goto L_0x0043
        L_0x003f:
            byte[] r0 = java.util.Arrays.copyOf((byte[]) r0, (int) r1)
        L_0x0043:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.nio.file.Files.read(java.io.InputStream, int):byte[]");
    }

    public static byte[] readAllBytes(Path path) throws IOException {
        InputStream newInputStream;
        SeekableByteChannel newByteChannel = newByteChannel(path, new OpenOption[0]);
        try {
            newInputStream = Channels.newInputStream((ReadableByteChannel) newByteChannel);
            long size = newByteChannel.size();
            if (size <= 2147483639) {
                byte[] read = read(newInputStream, (int) size);
                if (newInputStream != null) {
                    newInputStream.close();
                }
                if (newByteChannel != null) {
                    newByteChannel.close();
                }
                return read;
            }
            throw new OutOfMemoryError("Required array size too large");
        } catch (Throwable th) {
            if (newByteChannel != null) {
                try {
                    newByteChannel.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
        throw th;
    }

    public static List<String> readAllLines(Path path, Charset charset) throws IOException {
        BufferedReader newBufferedReader = newBufferedReader(path, charset);
        try {
            ArrayList arrayList = new ArrayList();
            while (true) {
                String readLine = newBufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                arrayList.add(readLine);
            }
            if (newBufferedReader != null) {
                newBufferedReader.close();
            }
            return arrayList;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public static List<String> readAllLines(Path path) throws IOException {
        return readAllLines(path, StandardCharsets.UTF_8);
    }

    public static Path write(Path path, byte[] bArr, OpenOption... openOptionArr) throws IOException {
        Objects.requireNonNull(bArr);
        OutputStream newOutputStream = newOutputStream(path, openOptionArr);
        try {
            int length = bArr.length;
            int i = length;
            while (i > 0) {
                int min = Math.min(i, 8192);
                newOutputStream.write(bArr, length - i, min);
                i -= min;
            }
            if (newOutputStream != null) {
                newOutputStream.close();
            }
            return path;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public static Path write(Path path, Iterable<? extends CharSequence> iterable, Charset charset, OpenOption... openOptionArr) throws IOException {
        Objects.requireNonNull(iterable);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(newOutputStream(path, openOptionArr), charset.newEncoder()));
        try {
            for (CharSequence append : iterable) {
                bufferedWriter.append(append);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
            return path;
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    public static Path write(Path path, Iterable<? extends CharSequence> iterable, OpenOption... openOptionArr) throws IOException {
        return write(path, iterable, StandardCharsets.UTF_8, openOptionArr);
    }

    public static Stream<Path> list(Path path) throws IOException {
        DirectoryStream<Path> newDirectoryStream = newDirectoryStream(path);
        try {
            final Iterator<Path> it = newDirectoryStream.iterator();
            return (Stream) StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<Path>() {
                public boolean hasNext() {
                    try {
                        return Iterator.this.hasNext();
                    } catch (DirectoryIteratorException e) {
                        throw new UncheckedIOException(e.getCause());
                    }
                }

                public Path next() {
                    try {
                        return (Path) Iterator.this.next();
                    } catch (DirectoryIteratorException e) {
                        throw new UncheckedIOException(e.getCause());
                    }
                }
            }, 1), false).onClose(asUncheckedRunnable(newDirectoryStream));
        } catch (Error | RuntimeException e) {
            try {
                newDirectoryStream.close();
            } catch (IOException e2) {
                e.addSuppressed(e2);
            } catch (Throwable unused) {
            }
            throw e;
        }
    }

    public static Stream<Path> walk(Path path, int i, FileVisitOption... fileVisitOptionArr) throws IOException {
        FileTreeIterator fileTreeIterator = new FileTreeIterator(path, i, fileVisitOptionArr);
        try {
            return ((Stream) StreamSupport.stream(Spliterators.spliteratorUnknownSize(fileTreeIterator, 1), false).onClose(new Files$$ExternalSyntheticLambda0(fileTreeIterator))).map(new Files$$ExternalSyntheticLambda1());
        } catch (Error | RuntimeException e) {
            fileTreeIterator.close();
            throw e;
        }
    }

    public static Stream<Path> walk(Path path, FileVisitOption... fileVisitOptionArr) throws IOException {
        return walk(path, Integer.MAX_VALUE, fileVisitOptionArr);
    }

    public static Stream<Path> find(Path path, int i, BiPredicate<Path, BasicFileAttributes> biPredicate, FileVisitOption... fileVisitOptionArr) throws IOException {
        FileTreeIterator fileTreeIterator = new FileTreeIterator(path, i, fileVisitOptionArr);
        try {
            return ((Stream) StreamSupport.stream(Spliterators.spliteratorUnknownSize(fileTreeIterator, 1), false).onClose(new Files$$ExternalSyntheticLambda0(fileTreeIterator))).filter(new Files$$ExternalSyntheticLambda2(biPredicate)).map(new Files$$ExternalSyntheticLambda3());
        } catch (Error | RuntimeException e) {
            fileTreeIterator.close();
            throw e;
        }
    }

    public static Stream<String> lines(Path path, Charset charset) throws IOException {
        BufferedReader newBufferedReader = newBufferedReader(path, charset);
        try {
            return (Stream) newBufferedReader.lines().onClose(asUncheckedRunnable(newBufferedReader));
        } catch (Error | RuntimeException e) {
            try {
                newBufferedReader.close();
            } catch (IOException e2) {
                e.addSuppressed(e2);
            } catch (Throwable unused) {
            }
            throw e;
        }
    }

    public static Stream<String> lines(Path path) throws IOException {
        return lines(path, StandardCharsets.UTF_8);
    }
}
