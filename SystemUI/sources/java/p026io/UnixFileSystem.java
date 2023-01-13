package java.p026io;

import android.system.ErrnoException;
import android.system.OsConstants;
import dalvik.system.BlockGuard;
import java.security.AccessController;
import libcore.p030io.Libcore;
import sun.security.action.GetPropertyAction;

/* renamed from: java.io.UnixFileSystem */
class UnixFileSystem extends FileSystem {
    private ExpiringCache cache = new ExpiringCache();
    private final char colon = ((String) AccessController.doPrivileged(new GetPropertyAction("path.separator"))).charAt(0);
    private final String javaHome = ((String) AccessController.doPrivileged(new GetPropertyAction("java.home")));
    private ExpiringCache javaHomePrefixCache = new ExpiringCache();
    private final char slash = ((String) AccessController.doPrivileged(new GetPropertyAction("file.separator"))).charAt(0);

    private native String canonicalize0(String str) throws IOException;

    private native boolean createDirectory0(File file);

    private native boolean createFileExclusively0(String str) throws IOException;

    private native int getBooleanAttributes0(String str);

    private native long getLastModifiedTime0(File file);

    private native long getSpace0(File file, int i);

    private static native void initIDs();

    private native String[] list0(File file);

    private native boolean setLastModifiedTime0(File file, long j);

    private native boolean setPermission0(File file, int i, boolean z, boolean z2);

    private native boolean setReadOnly0(File file);

    public String getDefaultParent() {
        return "/";
    }

    public char getSeparator() {
        return this.slash;
    }

    public char getPathSeparator() {
        return this.colon;
    }

    public String normalize(String str) {
        int length = str.length();
        char[] charArray = str.toCharArray();
        int i = 0;
        char c = 0;
        int i2 = 0;
        while (i < length) {
            char c2 = charArray[i];
            if (c2 != '/' || c != '/') {
                charArray[i2] = c2;
                i2++;
            }
            i++;
            c = c2;
        }
        if (c == '/' && length > 1) {
            i2--;
        }
        return i2 != length ? new String(charArray, 0, i2) : str;
    }

    public int prefixLength(String str) {
        if (str.length() != 0 && str.charAt(0) == '/') {
            return 1;
        }
        return 0;
    }

    public String resolve(String str, String str2) {
        if (str2.isEmpty() || str2.equals("/")) {
            return str;
        }
        if (str2.charAt(0) == '/') {
            if (str.equals("/")) {
                return str2;
            }
            return str + str2;
        } else if (str.equals("/")) {
            return str + str2;
        } else {
            return str + '/' + str2;
        }
    }

    public String fromURIPath(String str) {
        return (!str.endsWith("/") || str.length() <= 1) ? str : str.substring(0, str.length() - 1);
    }

    public boolean isAbsolute(File file) {
        return file.getPrefixLength() != 0;
    }

    public String resolve(File file) {
        if (isAbsolute(file)) {
            return file.getPath();
        }
        return resolve(System.getProperty("user.dir"), file.getPath());
    }

    public String canonicalize(String str) throws IOException {
        String str2;
        String parentOrNull;
        String str3;
        if (!useCanonCaches) {
            return canonicalize0(str);
        }
        String str4 = this.cache.get(str);
        if (str4 == null) {
            if (useCanonPrefixCache) {
                str2 = parentOrNull(str);
                if (!(str2 == null || (str3 = this.javaHomePrefixCache.get(str2)) == null)) {
                    String substring = str.substring(str2.length() + 1);
                    String str5 = str3 + this.slash + substring;
                    this.cache.put(str2 + this.slash + substring, str5);
                    str4 = str5;
                }
            } else {
                str2 = null;
            }
            if (str4 == null) {
                BlockGuard.getThreadPolicy().onReadFromDisk();
                BlockGuard.getVmPolicy().onPathAccess(str);
                str4 = canonicalize0(str);
                this.cache.put(str, str4);
                if (useCanonPrefixCache && str2 != null && str2.startsWith(this.javaHome) && (parentOrNull = parentOrNull(str4)) != null && parentOrNull.equals(str2)) {
                    File file = new File(str4);
                    if (file.exists() && !file.isDirectory()) {
                        this.javaHomePrefixCache.put(str2, parentOrNull);
                    }
                }
            }
        }
        return str4;
    }

    static String parentOrNull(String str) {
        if (str == null) {
            return null;
        }
        char c = File.separatorChar;
        int length = str.length() - 1;
        int i = 0;
        int i2 = 0;
        for (int i3 = length; i3 > 0; i3--) {
            char charAt = str.charAt(i3);
            if (charAt == '.') {
                i++;
                if (i >= 2) {
                    return null;
                }
            } else if (charAt != c) {
                i2++;
                i = 0;
            } else if ((i == 1 && i2 == 0) || i3 == 0 || i3 >= length - 1 || str.charAt(i3 - 1) == c) {
                return null;
            } else {
                return str.substring(0, i3);
            }
        }
        return null;
    }

    public int getBooleanAttributes(File file) {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        BlockGuard.getVmPolicy().onPathAccess(file.getPath());
        int booleanAttributes0 = getBooleanAttributes0(file.getPath());
        String name = file.getName();
        int i = 0;
        if (name.length() > 0 && name.charAt(0) == '.') {
            i = 8;
        }
        return booleanAttributes0 | i;
    }

    public boolean checkAccess(File file, int i) {
        int i2;
        if (i == 1) {
            i2 = OsConstants.X_OK;
        } else if (i == 2) {
            i2 = OsConstants.W_OK;
        } else if (i == 4) {
            i2 = OsConstants.R_OK;
        } else if (i == 8) {
            i2 = OsConstants.F_OK;
        } else {
            throw new IllegalArgumentException("Bad access mode: " + i);
        }
        try {
            return Libcore.f855os.access(file.getPath(), i2);
        } catch (ErrnoException unused) {
            return false;
        }
    }

    public long getLastModifiedTime(File file) {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        BlockGuard.getVmPolicy().onPathAccess(file.getPath());
        return getLastModifiedTime0(file);
    }

    public long getLength(File file) {
        try {
            return Libcore.f855os.stat(file.getPath()).st_size;
        } catch (ErrnoException unused) {
            return 0;
        }
    }

    public boolean setPermission(File file, int i, boolean z, boolean z2) {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(file.getPath());
        return setPermission0(file, i, z, z2);
    }

    public boolean createFileExclusively(String str) throws IOException {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(str);
        return createFileExclusively0(str);
    }

    public boolean delete(File file) {
        this.cache.clear();
        this.javaHomePrefixCache.clear();
        try {
            Libcore.f855os.remove(file.getPath());
            return true;
        } catch (ErrnoException unused) {
            return false;
        }
    }

    public String[] list(File file) {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        BlockGuard.getVmPolicy().onPathAccess(file.getPath());
        return list0(file);
    }

    public boolean createDirectory(File file) {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(file.getPath());
        return createDirectory0(file);
    }

    public boolean rename(File file, File file2) {
        this.cache.clear();
        this.javaHomePrefixCache.clear();
        try {
            Libcore.f855os.rename(file.getPath(), file2.getPath());
            return true;
        } catch (ErrnoException unused) {
            return false;
        }
    }

    public boolean setLastModifiedTime(File file, long j) {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(file.getPath());
        return setLastModifiedTime0(file, j);
    }

    public boolean setReadOnly(File file) {
        BlockGuard.getThreadPolicy().onWriteToDisk();
        BlockGuard.getVmPolicy().onPathAccess(file.getPath());
        return setReadOnly0(file);
    }

    public File[] listRoots() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkRead("/");
            }
            return new File[]{new File("/")};
        } catch (SecurityException unused) {
            return new File[0];
        }
    }

    public long getSpace(File file, int i) {
        BlockGuard.getThreadPolicy().onReadFromDisk();
        BlockGuard.getVmPolicy().onPathAccess(file.getPath());
        return getSpace0(file, i);
    }

    public int compare(File file, File file2) {
        return file.getPath().compareTo(file2.getPath());
    }

    public int hashCode(File file) {
        return file.getPath().hashCode() ^ 1234321;
    }

    static {
        initIDs();
    }
}
