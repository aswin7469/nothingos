package java.p026io;

import com.android.launcher3.icons.cache.BaseIconCache;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import sun.misc.Unsafe;

/* renamed from: java.io.File */
public class File implements Serializable, Comparable<File> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long PATH_OFFSET;
    private static final long PREFIX_LENGTH_OFFSET;
    private static final Unsafe UNSAFE;

    /* renamed from: fs */
    private static final FileSystem f515fs;
    public static final String pathSeparator;
    public static final char pathSeparatorChar;
    public static final String separator;
    public static final char separatorChar;
    private static final long serialVersionUID = 301077366599181567L;
    private volatile transient Path filePath;
    private final String path;
    private final transient int prefixLength;
    private transient PathStatus status = null;

    /* renamed from: java.io.File$PathStatus */
    private enum PathStatus {
        INVALID,
        CHECKED
    }

    static {
        Class<File> cls = File.class;
        FileSystem fileSystem = DefaultFileSystem.getFileSystem();
        f515fs = fileSystem;
        char separator2 = fileSystem.getSeparator();
        separatorChar = separator2;
        separator = "" + separator2;
        char pathSeparator2 = fileSystem.getPathSeparator();
        pathSeparatorChar = pathSeparator2;
        pathSeparator = "" + pathSeparator2;
        try {
            Unsafe unsafe = Unsafe.getUnsafe();
            PATH_OFFSET = unsafe.objectFieldOffset(cls.getDeclaredField("path"));
            PREFIX_LENGTH_OFFSET = unsafe.objectFieldOffset(cls.getDeclaredField("prefixLength"));
            UNSAFE = unsafe;
        } catch (ReflectiveOperationException e) {
            throw new Error((Throwable) e);
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean isInvalid() {
        PathStatus pathStatus;
        if (this.status == null) {
            if (this.path.indexOf(0) < 0) {
                pathStatus = PathStatus.CHECKED;
            } else {
                pathStatus = PathStatus.INVALID;
            }
            this.status = pathStatus;
        }
        if (this.status == PathStatus.INVALID) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public int getPrefixLength() {
        return this.prefixLength;
    }

    private File(String str, int i) {
        this.path = str;
        this.prefixLength = i;
    }

    private File(String str, File file) {
        this.path = f515fs.resolve(file.path, str);
        this.prefixLength = file.prefixLength;
    }

    public File(String str) {
        str.getClass();
        FileSystem fileSystem = f515fs;
        String normalize = fileSystem.normalize(str);
        this.path = normalize;
        this.prefixLength = fileSystem.prefixLength(normalize);
    }

    public File(String str, String str2) {
        str2.getClass();
        if (str == null || str.isEmpty()) {
            this.path = f515fs.normalize(str2);
        } else {
            FileSystem fileSystem = f515fs;
            this.path = fileSystem.resolve(fileSystem.normalize(str), fileSystem.normalize(str2));
        }
        this.prefixLength = f515fs.prefixLength(this.path);
    }

    public File(File file, String str) {
        str.getClass();
        if (file == null) {
            this.path = f515fs.normalize(str);
        } else if (file.path.equals("")) {
            FileSystem fileSystem = f515fs;
            this.path = fileSystem.resolve(fileSystem.getDefaultParent(), fileSystem.normalize(str));
        } else {
            FileSystem fileSystem2 = f515fs;
            this.path = fileSystem2.resolve(file.path, fileSystem2.normalize(str));
        }
        this.prefixLength = f515fs.prefixLength(this.path);
    }

    public File(URI uri) {
        if (!uri.isAbsolute()) {
            throw new IllegalArgumentException("URI is not absolute");
        } else if (!uri.isOpaque()) {
            String scheme = uri.getScheme();
            if (scheme == null || !scheme.equalsIgnoreCase("file")) {
                throw new IllegalArgumentException("URI scheme is not \"file\"");
            } else if (uri.getAuthority() != null) {
                throw new IllegalArgumentException("URI has an authority component");
            } else if (uri.getFragment() != null) {
                throw new IllegalArgumentException("URI has a fragment component");
            } else if (uri.getQuery() == null) {
                String path2 = uri.getPath();
                if (!path2.equals("")) {
                    FileSystem fileSystem = f515fs;
                    String fromURIPath = fileSystem.fromURIPath(path2);
                    char c = separatorChar;
                    String normalize = fileSystem.normalize(c != '/' ? fromURIPath.replace('/', c) : fromURIPath);
                    this.path = normalize;
                    this.prefixLength = fileSystem.prefixLength(normalize);
                    return;
                }
                throw new IllegalArgumentException("URI path component is empty");
            } else {
                throw new IllegalArgumentException("URI has a query component");
            }
        } else {
            throw new IllegalArgumentException("URI is not hierarchical");
        }
    }

    public String getName() {
        int lastIndexOf = this.path.lastIndexOf((int) separatorChar);
        int i = this.prefixLength;
        if (lastIndexOf < i) {
            return this.path.substring(i);
        }
        return this.path.substring(lastIndexOf + 1);
    }

    public String getParent() {
        int i;
        int lastIndexOf = this.path.lastIndexOf((int) separatorChar);
        int i2 = this.prefixLength;
        if (lastIndexOf >= i2) {
            return this.path.substring(0, lastIndexOf);
        }
        if (i2 <= 0 || this.path.length() <= (i = this.prefixLength)) {
            return null;
        }
        return this.path.substring(0, i);
    }

    public File getParentFile() {
        String parent = getParent();
        if (parent == null) {
            return null;
        }
        return new File(parent, this.prefixLength);
    }

    public String getPath() {
        return this.path;
    }

    public boolean isAbsolute() {
        return f515fs.isAbsolute(this);
    }

    public String getAbsolutePath() {
        return f515fs.resolve(this);
    }

    public File getAbsoluteFile() {
        String absolutePath = getAbsolutePath();
        return new File(absolutePath, f515fs.prefixLength(absolutePath));
    }

    public String getCanonicalPath() throws IOException {
        if (!isInvalid()) {
            FileSystem fileSystem = f515fs;
            return fileSystem.canonicalize(fileSystem.resolve(this));
        }
        throw new IOException("Invalid file path");
    }

    public File getCanonicalFile() throws IOException {
        String canonicalPath = getCanonicalPath();
        return new File(canonicalPath, f515fs.prefixLength(canonicalPath));
    }

    private static String slashify(String str, boolean z) {
        char c = separatorChar;
        if (c != '/') {
            str = str.replace(c, '/');
        }
        if (!str.startsWith("/")) {
            str = "/" + str;
        }
        if (str.endsWith("/") || !z) {
            return str;
        }
        return str + "/";
    }

    @Deprecated
    public URL toURL() throws MalformedURLException {
        if (!isInvalid()) {
            return new URL("file", "", slashify(getAbsolutePath(), getAbsoluteFile().isDirectory()));
        }
        throw new MalformedURLException("Invalid file path");
    }

    public URI toURI() {
        try {
            File absoluteFile = getAbsoluteFile();
            String slashify = slashify(absoluteFile.getPath(), absoluteFile.isDirectory());
            if (slashify.startsWith("//")) {
                slashify = "//" + slashify;
            }
            return new URI("file", (String) null, slashify, (String) null);
        } catch (URISyntaxException e) {
            throw new Error((Throwable) e);
        }
    }

    public boolean canRead() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return f515fs.checkAccess(this, 4);
    }

    public boolean canWrite() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return f515fs.checkAccess(this, 2);
    }

    public boolean exists() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return f515fs.checkAccess(this, 8);
    }

    public boolean isDirectory() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        if (!isInvalid() && (f515fs.getBooleanAttributes(this) & 4) != 0) {
            return true;
        }
        return false;
    }

    public boolean isFile() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        if (!isInvalid() && (f515fs.getBooleanAttributes(this) & 2) != 0) {
            return true;
        }
        return false;
    }

    public boolean isHidden() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        if (!isInvalid() && (f515fs.getBooleanAttributes(this) & 8) != 0) {
            return true;
        }
        return false;
    }

    public long lastModified() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return 0;
        }
        return f515fs.getLastModifiedTime(this);
    }

    public long length() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return 0;
        }
        return f515fs.getLength(this);
    }

    public boolean createNewFile() throws IOException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (!isInvalid()) {
            return f515fs.createFileExclusively(this.path);
        }
        throw new IOException("Invalid file path");
    }

    public boolean delete() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkDelete(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return f515fs.delete(this);
    }

    public void deleteOnExit() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkDelete(this.path);
        }
        if (!isInvalid()) {
            DeleteOnExitHook.add(this.path);
        }
    }

    public String[] list() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return null;
        }
        return f515fs.list(this);
    }

    public String[] list(FilenameFilter filenameFilter) {
        String[] list = list();
        if (list == null || filenameFilter == null) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.length; i++) {
            if (filenameFilter.accept(this, list[i])) {
                arrayList.add(list[i]);
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    public File[] listFiles() {
        String[] list = list();
        if (list == null) {
            return null;
        }
        int length = list.length;
        File[] fileArr = new File[length];
        for (int i = 0; i < length; i++) {
            fileArr[i] = new File(list[i], this);
        }
        return fileArr;
    }

    public File[] listFiles(FilenameFilter filenameFilter) {
        String[] list = list();
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : list) {
            if (filenameFilter == null || filenameFilter.accept(this, str)) {
                arrayList.add(new File(str, this));
            }
        }
        return (File[]) arrayList.toArray(new File[arrayList.size()]);
    }

    public File[] listFiles(FileFilter fileFilter) {
        String[] list = list();
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (String file : list) {
            File file2 = new File(file, this);
            if (fileFilter == null || fileFilter.accept(file2)) {
                arrayList.add(file2);
            }
        }
        return (File[]) arrayList.toArray(new File[arrayList.size()]);
    }

    public boolean mkdir() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return f515fs.createDirectory(this);
    }

    public boolean mkdirs() {
        if (exists()) {
            return false;
        }
        if (mkdir()) {
            return true;
        }
        try {
            File canonicalFile = getCanonicalFile();
            File parentFile = canonicalFile.getParentFile();
            if (parentFile == null) {
                return false;
            }
            if ((parentFile.mkdirs() || parentFile.exists()) && canonicalFile.mkdir()) {
                return true;
            }
            return false;
        } catch (IOException unused) {
            return false;
        }
    }

    public boolean renameTo(File file) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
            securityManager.checkWrite(file.path);
        }
        file.getClass();
        if (isInvalid() || file.isInvalid()) {
            return false;
        }
        return f515fs.rename(this, file);
    }

    public boolean setLastModified(long j) {
        if (j >= 0) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkWrite(this.path);
            }
            if (isInvalid()) {
                return false;
            }
            return f515fs.setLastModifiedTime(this, j);
        }
        throw new IllegalArgumentException("Negative time");
    }

    public boolean setReadOnly() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return f515fs.setReadOnly(this);
    }

    public boolean setWritable(boolean z, boolean z2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return f515fs.setPermission(this, 2, z, z2);
    }

    public boolean setWritable(boolean z) {
        return setWritable(z, true);
    }

    public boolean setReadable(boolean z, boolean z2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return f515fs.setPermission(this, 4, z, z2);
    }

    public boolean setReadable(boolean z) {
        return setReadable(z, true);
    }

    public boolean setExecutable(boolean z, boolean z2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return f515fs.setPermission(this, 1, z, z2);
    }

    public boolean setExecutable(boolean z) {
        return setExecutable(z, true);
    }

    public boolean canExecute() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkExec(this.path);
        }
        if (isInvalid()) {
            return false;
        }
        return f515fs.checkAccess(this, 1);
    }

    public static File[] listRoots() {
        return f515fs.listRoots();
    }

    public long getTotalSpace() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("getFileSystemAttributes"));
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return 0;
        }
        return f515fs.getSpace(this, 0);
    }

    public long getFreeSpace() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("getFileSystemAttributes"));
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return 0;
        }
        return f515fs.getSpace(this, 1);
    }

    public long getUsableSpace() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("getFileSystemAttributes"));
            securityManager.checkRead(this.path);
        }
        if (isInvalid()) {
            return 0;
        }
        return f515fs.getSpace(this, 2);
    }

    /* renamed from: java.io.File$TempDirectory */
    private static class TempDirectory {
        private TempDirectory() {
        }

        static File generateFile(String str, String str2, File file) throws IOException {
            long j;
            long randomLongInternal = Math.randomLongInternal();
            if (randomLongInternal == Long.MIN_VALUE) {
                j = 0;
            } else {
                j = Math.abs(randomLongInternal);
            }
            String str3 = str + Long.toString(j) + str2;
            File file2 = new File(file, str3);
            if (str3.equals(file2.getName()) && !file2.isInvalid()) {
                return file2;
            }
            if (System.getSecurityManager() != null) {
                throw new IOException("Unable to create temporary file");
            }
            throw new IOException("Unable to create temporary file, " + file2);
        }
    }

    public static File createTempFile(String str, String str2, File file) throws IOException {
        File generateFile;
        FileSystem fileSystem;
        if (str.length() >= 3) {
            if (str2 == null) {
                str2 = ".tmp";
            }
            if (file == null) {
                file = new File(System.getProperty("java.io.tmpdir", BaseIconCache.EMPTY_CLASS_NAME));
            }
            do {
                generateFile = TempDirectory.generateFile(str, str2, file);
                fileSystem = f515fs;
            } while ((fileSystem.getBooleanAttributes(generateFile) & 1) != 0);
            if (fileSystem.createFileExclusively(generateFile.getPath())) {
                return generateFile;
            }
            throw new IOException("Unable to create temporary file");
        }
        throw new IllegalArgumentException("Prefix string too short");
    }

    public static File createTempFile(String str, String str2) throws IOException {
        return createTempFile(str, str2, (File) null);
    }

    public int compareTo(File file) {
        return f515fs.compare(this, file);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof File) || compareTo((File) obj) != 0) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return f515fs.hashCode(this);
    }

    public String toString() {
        return getPath();
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeChar(separatorChar);
    }

    private synchronized void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String str = (String) objectInputStream.readFields().get("path", (Object) null);
        char readChar = objectInputStream.readChar();
        char c = separatorChar;
        if (readChar != c) {
            str = str.replace(readChar, c);
        }
        FileSystem fileSystem = f515fs;
        String normalize = fileSystem.normalize(str);
        Unsafe unsafe = UNSAFE;
        unsafe.putObject(this, PATH_OFFSET, normalize);
        unsafe.putIntVolatile(this, PREFIX_LENGTH_OFFSET, fileSystem.prefixLength(normalize));
    }

    public Path toPath() {
        Path path2 = this.filePath;
        if (path2 == null) {
            synchronized (this) {
                path2 = this.filePath;
                if (path2 == null) {
                    path2 = FileSystems.getDefault().getPath(this.path, new String[0]);
                    this.filePath = path2;
                }
            }
        }
        return path2;
    }
}
