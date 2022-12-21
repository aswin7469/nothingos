package java.util.prefs;

import com.android.launcher3.icons.cache.BaseIconCache;
import java.p026io.File;
import java.p026io.FileInputStream;
import java.p026io.FileNotFoundException;
import java.p026io.FileOutputStream;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import sun.util.locale.BaseLocale;
import sun.util.logging.PlatformLogger;

public class FileSystemPreferences extends AbstractPreferences {
    private static final int EACCES = 13;
    private static final int EAGAIN = 11;
    /* access modifiers changed from: private */
    public static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final int ERROR_CODE = 1;
    private static int INIT_SLEEP_TIME = 50;
    private static final int LOCK_HANDLE = 0;
    private static int MAX_ATTEMPTS = 5;
    private static final int USER_READ_WRITE = 384;
    private static final int USER_RWX = 448;
    private static final int USER_RWX_ALL_RX = 493;
    private static final int USER_RW_ALL_READ = 420;
    /* access modifiers changed from: private */
    public static boolean isSystemRootModified = false;
    /* access modifiers changed from: private */
    public static boolean isSystemRootWritable = false;
    /* access modifiers changed from: private */
    public static boolean isUserRootModified = false;
    /* access modifiers changed from: private */
    public static boolean isUserRootWritable;
    static File systemLockFile;
    static Preferences systemRoot;
    /* access modifiers changed from: private */
    public static File systemRootDir;
    private static int systemRootLockHandle;
    /* access modifiers changed from: private */
    public static File systemRootModFile;
    /* access modifiers changed from: private */
    public static long systemRootModTime;
    static File userLockFile;
    static Preferences userRoot;
    /* access modifiers changed from: private */
    public static File userRootDir;
    private static int userRootLockHandle;
    /* access modifiers changed from: private */
    public static File userRootModFile;
    /* access modifiers changed from: private */
    public static long userRootModTime;
    final List<Change> changeLog;
    /* access modifiers changed from: private */
    public final File dir;
    private final boolean isUserNode;
    private long lastSyncTime;
    NodeCreate nodeCreate;
    /* access modifiers changed from: private */
    public Map<String, String> prefsCache;
    /* access modifiers changed from: private */
    public final File prefsFile;
    /* access modifiers changed from: private */
    public final File tmpFile;

    /* access modifiers changed from: private */
    public static native int chmod(String str, int i);

    private static boolean isDirChar(char c) {
        return (c <= 31 || c >= 127 || c == '/' || c == '.' || c == '_') ? false : true;
    }

    private static native int[] lockFile0(String str, int i, boolean z);

    private static native int unlockFile0(int i);

    /* access modifiers changed from: protected */
    public void flushSpi() throws BackingStoreException {
    }

    /* access modifiers changed from: private */
    public static PlatformLogger getLogger() {
        return PlatformLogger.getLogger("java.util.prefs");
    }

    static synchronized Preferences getUserRoot() {
        Preferences preferences;
        synchronized (FileSystemPreferences.class) {
            if (userRoot == null) {
                setupUserRoot();
                userRoot = new FileSystemPreferences(true);
            }
            preferences = userRoot;
        }
        return preferences;
    }

    private static void setupUserRoot() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                FileSystemPreferences.userRootDir = new File(System.getProperty("java.util.prefs.userRoot", System.getProperty("user.home")), ".java/.userPrefs");
                if (!FileSystemPreferences.userRootDir.exists()) {
                    if (FileSystemPreferences.userRootDir.mkdirs()) {
                        try {
                            int unused = FileSystemPreferences.chmod(FileSystemPreferences.userRootDir.getCanonicalPath(), 448);
                        } catch (IOException unused2) {
                            FileSystemPreferences.getLogger().warning("Could not change permissions on userRoot directory. ");
                        }
                        FileSystemPreferences.getLogger().info("Created user preferences directory.");
                    } else {
                        FileSystemPreferences.getLogger().warning("Couldn't create user preferences directory. User preferences are unusable.");
                    }
                }
                FileSystemPreferences.isUserRootWritable = FileSystemPreferences.userRootDir.canWrite();
                String property = System.getProperty("user.name");
                File r2 = FileSystemPreferences.userRootDir;
                FileSystemPreferences.userLockFile = new File(r2, ".user.lock." + property);
                File r22 = FileSystemPreferences.userRootDir;
                FileSystemPreferences.userRootModFile = new File(r22, ".userRootModFile." + property);
                if (!FileSystemPreferences.userRootModFile.exists()) {
                    try {
                        FileSystemPreferences.userRootModFile.createNewFile();
                        int r0 = FileSystemPreferences.chmod(FileSystemPreferences.userRootModFile.getCanonicalPath(), FileSystemPreferences.USER_READ_WRITE);
                        if (r0 != 0) {
                            PlatformLogger r1 = FileSystemPreferences.getLogger();
                            r1.warning("Problem creating userRoot mod file. Chmod failed on " + FileSystemPreferences.userRootModFile.getCanonicalPath() + " Unix error code " + r0);
                        }
                    } catch (IOException e) {
                        FileSystemPreferences.getLogger().warning(e.toString());
                    }
                }
                FileSystemPreferences.userRootModTime = FileSystemPreferences.userRootModFile.lastModified();
                return null;
            }
        });
    }

    static synchronized Preferences getSystemRoot() {
        Preferences preferences;
        synchronized (FileSystemPreferences.class) {
            if (systemRoot == null) {
                setupSystemRoot();
                systemRoot = new FileSystemPreferences(false);
            }
            preferences = systemRoot;
        }
        return preferences;
    }

    private static void setupSystemRoot() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                FileSystemPreferences.systemRootDir = new File(System.getProperty("java.util.prefs.systemRoot", "/etc/.java"), ".systemPrefs");
                if (!FileSystemPreferences.systemRootDir.exists()) {
                    FileSystemPreferences.systemRootDir = new File(System.getProperty("java.home"), ".systemPrefs");
                    if (!FileSystemPreferences.systemRootDir.exists()) {
                        if (FileSystemPreferences.systemRootDir.mkdirs()) {
                            FileSystemPreferences.getLogger().info("Created system preferences directory in java.home.");
                            try {
                                int unused = FileSystemPreferences.chmod(FileSystemPreferences.systemRootDir.getCanonicalPath(), FileSystemPreferences.USER_RWX_ALL_RX);
                            } catch (IOException unused2) {
                            }
                        } else {
                            FileSystemPreferences.getLogger().warning("Could not create system preferences directory. System preferences are unusable.");
                        }
                    }
                }
                FileSystemPreferences.isSystemRootWritable = FileSystemPreferences.systemRootDir.canWrite();
                FileSystemPreferences.systemLockFile = new File(FileSystemPreferences.systemRootDir, ".system.lock");
                FileSystemPreferences.systemRootModFile = new File(FileSystemPreferences.systemRootDir, ".systemRootModFile");
                if (!FileSystemPreferences.systemRootModFile.exists() && FileSystemPreferences.isSystemRootWritable) {
                    try {
                        FileSystemPreferences.systemRootModFile.createNewFile();
                        int r0 = FileSystemPreferences.chmod(FileSystemPreferences.systemRootModFile.getCanonicalPath(), FileSystemPreferences.USER_RW_ALL_READ);
                        if (r0 != 0) {
                            PlatformLogger r1 = FileSystemPreferences.getLogger();
                            r1.warning("Chmod failed on " + FileSystemPreferences.systemRootModFile.getCanonicalPath() + " Unix error code " + r0);
                        }
                    } catch (IOException e) {
                        FileSystemPreferences.getLogger().warning(e.toString());
                    }
                }
                FileSystemPreferences.systemRootModTime = FileSystemPreferences.systemRootModFile.lastModified();
                return null;
            }
        });
    }

    private abstract class Change {
        /* access modifiers changed from: package-private */
        public abstract void replay();

        private Change() {
        }
    }

    private class Put extends Change {
        String key;
        String value;

        Put(String str, String str2) {
            super();
            this.key = str;
            this.value = str2;
        }

        /* access modifiers changed from: package-private */
        public void replay() {
            FileSystemPreferences.this.prefsCache.put(this.key, this.value);
        }
    }

    private class Remove extends Change {
        String key;

        Remove(String str) {
            super();
            this.key = str;
        }

        /* access modifiers changed from: package-private */
        public void replay() {
            FileSystemPreferences.this.prefsCache.remove(this.key);
        }
    }

    private class NodeCreate extends Change {
        /* access modifiers changed from: package-private */
        public void replay() {
        }

        private NodeCreate() {
            super();
        }
    }

    private void replayChanges() {
        int size = this.changeLog.size();
        for (int i = 0; i < size; i++) {
            this.changeLog.get(i).replay();
        }
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                FileSystemPreferences.syncWorld();
            }
        });
    }

    /* access modifiers changed from: private */
    public static void syncWorld() {
        Preferences preferences;
        Preferences preferences2;
        synchronized (FileSystemPreferences.class) {
            preferences = userRoot;
            preferences2 = systemRoot;
        }
        if (preferences != null) {
            try {
                preferences.flush();
            } catch (BackingStoreException e) {
                PlatformLogger logger = getLogger();
                logger.warning("Couldn't flush user prefs: " + e);
            }
        }
        if (preferences2 != null) {
            try {
                preferences2.flush();
            } catch (BackingStoreException e2) {
                PlatformLogger logger2 = getLogger();
                logger2.warning("Couldn't flush system prefs: " + e2);
            }
        }
    }

    private FileSystemPreferences(boolean z) {
        super((AbstractPreferences) null, "");
        this.prefsCache = null;
        this.lastSyncTime = 0;
        this.changeLog = new ArrayList();
        this.nodeCreate = null;
        this.isUserNode = z;
        File file = z ? userRootDir : systemRootDir;
        this.dir = file;
        this.prefsFile = new File(file, "prefs.xml");
        this.tmpFile = new File(file, "prefs.tmp");
    }

    public FileSystemPreferences(String str, File file, boolean z) {
        super((AbstractPreferences) null, "");
        this.prefsCache = null;
        this.lastSyncTime = 0;
        ArrayList arrayList = new ArrayList();
        this.changeLog = arrayList;
        this.nodeCreate = null;
        this.isUserNode = z;
        File file2 = new File(str);
        this.dir = file2;
        this.prefsFile = new File(file2, "prefs.xml");
        this.tmpFile = new File(file2, "prefs.tmp");
        this.newNode = !file2.exists();
        if (this.newNode) {
            this.prefsCache = new TreeMap();
            NodeCreate nodeCreate2 = new NodeCreate();
            this.nodeCreate = nodeCreate2;
            arrayList.add(nodeCreate2);
        }
        if (z) {
            userLockFile = file;
            File parentFile = file.getParentFile();
            userRootModFile = new File(parentFile, file.getName() + ".rootmod");
            return;
        }
        systemLockFile = file;
        File parentFile2 = file.getParentFile();
        systemRootModFile = new File(parentFile2, file.getName() + ".rootmod");
    }

    private FileSystemPreferences(FileSystemPreferences fileSystemPreferences, String str) {
        super(fileSystemPreferences, str);
        this.prefsCache = null;
        this.lastSyncTime = 0;
        ArrayList arrayList = new ArrayList();
        this.changeLog = arrayList;
        this.nodeCreate = null;
        this.isUserNode = fileSystemPreferences.isUserNode;
        File file = new File(fileSystemPreferences.dir, dirName(str));
        this.dir = file;
        this.prefsFile = new File(file, "prefs.xml");
        this.tmpFile = new File(file, "prefs.tmp");
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                FileSystemPreferences fileSystemPreferences = FileSystemPreferences.this;
                fileSystemPreferences.newNode = !fileSystemPreferences.dir.exists();
                return null;
            }
        });
        if (this.newNode) {
            this.prefsCache = new TreeMap();
            NodeCreate nodeCreate2 = new NodeCreate();
            this.nodeCreate = nodeCreate2;
            arrayList.add(nodeCreate2);
        }
    }

    public boolean isUserNode() {
        return this.isUserNode;
    }

    /* access modifiers changed from: protected */
    public void putSpi(String str, String str2) {
        initCacheIfNecessary();
        this.changeLog.add(new Put(str, str2));
        this.prefsCache.put(str, str2);
    }

    /* access modifiers changed from: protected */
    public String getSpi(String str) {
        initCacheIfNecessary();
        return this.prefsCache.get(str);
    }

    /* access modifiers changed from: protected */
    public void removeSpi(String str) {
        initCacheIfNecessary();
        this.changeLog.add(new Remove(str));
        this.prefsCache.remove(str);
    }

    private void initCacheIfNecessary() {
        if (this.prefsCache == null) {
            try {
                loadCache();
            } catch (Exception unused) {
                this.prefsCache = new TreeMap();
            }
        }
    }

    private void loadCache() throws BackingStoreException {
        FileInputStream fileInputStream;
        TreeMap treeMap = new TreeMap();
        long j = 0;
        try {
            j = this.prefsFile.lastModified();
            fileInputStream = new FileInputStream(this.prefsFile);
            XmlSupport.importMap(fileInputStream, treeMap);
            fileInputStream.close();
        } catch (Exception e) {
            if (e instanceof InvalidPreferencesFormatException) {
                PlatformLogger logger = getLogger();
                logger.warning("Invalid preferences format in " + this.prefsFile.getPath());
                this.prefsFile.renameTo(new File(this.prefsFile.getParentFile(), "IncorrectFormatPrefs.xml"));
                treeMap = new TreeMap();
            } else if (e instanceof FileNotFoundException) {
                PlatformLogger logger2 = getLogger();
                logger2.warning("Prefs file removed in background " + this.prefsFile.getPath());
            } else {
                PlatformLogger logger3 = getLogger();
                logger3.warning("Exception while reading cache: " + e.getMessage());
                throw new BackingStoreException((Throwable) e);
            }
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        this.prefsCache = treeMap;
        this.lastSyncTime = j;
        return;
        throw th;
    }

    private void writeBackCache() throws BackingStoreException {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                public Void run() throws BackingStoreException {
                    FileOutputStream fileOutputStream;
                    try {
                        if (!FileSystemPreferences.this.dir.exists()) {
                            if (!FileSystemPreferences.this.dir.mkdirs()) {
                                throw new BackingStoreException(FileSystemPreferences.this.dir + " create failed.");
                            }
                        }
                        fileOutputStream = new FileOutputStream(FileSystemPreferences.this.tmpFile);
                        XmlSupport.exportMap(fileOutputStream, FileSystemPreferences.this.prefsCache);
                        fileOutputStream.close();
                        if (FileSystemPreferences.this.tmpFile.renameTo(FileSystemPreferences.this.prefsFile)) {
                            return null;
                        }
                        throw new BackingStoreException("Can't rename " + FileSystemPreferences.this.tmpFile + " to " + FileSystemPreferences.this.prefsFile);
                    } catch (Exception e) {
                        if (e instanceof BackingStoreException) {
                            throw ((BackingStoreException) e);
                        }
                        throw new BackingStoreException((Throwable) e);
                    } catch (Throwable th) {
                        th.addSuppressed(th);
                    }
                    throw th;
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((BackingStoreException) e.getException());
        }
    }

    /* access modifiers changed from: protected */
    public String[] keysSpi() {
        initCacheIfNecessary();
        return (String[]) this.prefsCache.keySet().toArray(new String[this.prefsCache.size()]);
    }

    /* access modifiers changed from: protected */
    public String[] childrenNamesSpi() {
        return (String[]) AccessController.doPrivileged(new PrivilegedAction<String[]>() {
            public String[] run() {
                ArrayList arrayList = new ArrayList();
                File[] listFiles = FileSystemPreferences.this.dir.listFiles();
                if (listFiles != null) {
                    for (int i = 0; i < listFiles.length; i++) {
                        if (listFiles[i].isDirectory()) {
                            arrayList.add(FileSystemPreferences.nodeName(listFiles[i].getName()));
                        }
                    }
                }
                return (String[]) arrayList.toArray(FileSystemPreferences.EMPTY_STRING_ARRAY);
            }
        });
    }

    /* access modifiers changed from: protected */
    public AbstractPreferences childSpi(String str) {
        return new FileSystemPreferences(this, str);
    }

    public void removeNode() throws BackingStoreException {
        synchronized ((isUserNode() ? userLockFile : systemLockFile)) {
            if (lockFile(false)) {
                try {
                    super.removeNode();
                } finally {
                    unlockFile();
                }
            } else {
                throw new BackingStoreException("Couldn't get file lock.");
            }
        }
    }

    /* access modifiers changed from: protected */
    public void removeNodeSpi() throws BackingStoreException {
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                public Void run() throws BackingStoreException {
                    if (FileSystemPreferences.this.changeLog.contains(FileSystemPreferences.this.nodeCreate)) {
                        FileSystemPreferences.this.changeLog.remove((Object) FileSystemPreferences.this.nodeCreate);
                        FileSystemPreferences.this.nodeCreate = null;
                        return null;
                    } else if (!FileSystemPreferences.this.dir.exists()) {
                        return null;
                    } else {
                        FileSystemPreferences.this.prefsFile.delete();
                        FileSystemPreferences.this.tmpFile.delete();
                        File[] listFiles = FileSystemPreferences.this.dir.listFiles();
                        if (listFiles.length != 0) {
                            FileSystemPreferences.getLogger().warning("Found extraneous files when removing node: " + Arrays.asList(listFiles));
                            for (File delete : listFiles) {
                                delete.delete();
                            }
                        }
                        if (FileSystemPreferences.this.dir.delete()) {
                            return null;
                        }
                        throw new BackingStoreException("Couldn't delete dir: " + FileSystemPreferences.this.dir);
                    }
                }
            });
        } catch (PrivilegedActionException e) {
            throw ((BackingStoreException) e.getException());
        }
    }

    public synchronized void sync() throws BackingStoreException {
        boolean z;
        if (isUserNode()) {
            z = false;
        } else {
            z = !isSystemRootWritable;
        }
        synchronized ((isUserNode() ? userLockFile : systemLockFile)) {
            if (lockFile(z)) {
                final Long l = (Long) AccessController.doPrivileged(new PrivilegedAction<Long>() {
                    public Long run() {
                        long j;
                        boolean z = true;
                        if (FileSystemPreferences.this.isUserNode()) {
                            j = FileSystemPreferences.userRootModFile.lastModified();
                            if (FileSystemPreferences.userRootModTime != j) {
                                z = false;
                            }
                            FileSystemPreferences.isUserRootModified = z;
                        } else {
                            j = FileSystemPreferences.systemRootModFile.lastModified();
                            if (FileSystemPreferences.systemRootModTime != j) {
                                z = false;
                            }
                            FileSystemPreferences.isSystemRootModified = z;
                        }
                        return new Long(j);
                    }
                });
                try {
                    super.sync();
                    AccessController.doPrivileged(new PrivilegedAction<Void>() {
                        public Void run() {
                            if (FileSystemPreferences.this.isUserNode()) {
                                FileSystemPreferences.userRootModTime = l.longValue() + 1000;
                                FileSystemPreferences.userRootModFile.setLastModified(FileSystemPreferences.userRootModTime);
                                return null;
                            }
                            FileSystemPreferences.systemRootModTime = l.longValue() + 1000;
                            FileSystemPreferences.systemRootModFile.setLastModified(FileSystemPreferences.systemRootModTime);
                            return null;
                        }
                    });
                } finally {
                    unlockFile();
                }
            } else {
                throw new BackingStoreException("Couldn't get file lock.");
            }
        }
    }

    /* access modifiers changed from: protected */
    public void syncSpi() throws BackingStoreException {
        syncSpiPrivileged();
    }

    private void syncSpiPrivileged() throws BackingStoreException {
        if (isRemoved()) {
            throw new IllegalStateException("Node has been removed");
        } else if (this.prefsCache != null) {
            if (!isUserNode() ? isSystemRootModified : isUserRootModified) {
                long lastModified = this.prefsFile.lastModified();
                if (lastModified != this.lastSyncTime) {
                    loadCache();
                    replayChanges();
                    this.lastSyncTime = lastModified;
                }
            } else if (this.lastSyncTime != 0 && !this.dir.exists()) {
                this.prefsCache = new TreeMap();
                replayChanges();
            }
            if (!this.changeLog.isEmpty()) {
                writeBackCache();
                long lastModified2 = this.prefsFile.lastModified();
                if (this.lastSyncTime <= lastModified2) {
                    long j = lastModified2 + 1000;
                    this.lastSyncTime = j;
                    this.prefsFile.setLastModified(j);
                }
                this.changeLog.clear();
            }
        }
    }

    public void flush() throws BackingStoreException {
        if (!isRemoved()) {
            sync();
        }
    }

    private static String dirName(String str) {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            if (!isDirChar(str.charAt(i))) {
                return BaseLocale.SEP + Base64.byteArrayToAltBase64(byteArray(str));
            }
        }
        return str;
    }

    private static byte[] byteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length * 2)];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            char charAt = str.charAt(i2);
            int i3 = i + 1;
            bArr[i] = (byte) (charAt >> 8);
            i = i3 + 1;
            bArr[i3] = (byte) charAt;
        }
        return bArr;
    }

    /* access modifiers changed from: private */
    public static String nodeName(String str) {
        int i = 0;
        if (str.charAt(0) != '_') {
            return str;
        }
        byte[] altBase64ToByteArray = Base64.altBase64ToByteArray(str.substring(1));
        StringBuffer stringBuffer = new StringBuffer(altBase64ToByteArray.length / 2);
        while (i < altBase64ToByteArray.length) {
            int i2 = i + 1;
            stringBuffer.append((char) (((altBase64ToByteArray[i] & 255) << 8) | (altBase64ToByteArray[i2] & 255)));
            i = i2 + 1;
        }
        return stringBuffer.toString();
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0033 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean lockFile(boolean r10) throws java.lang.SecurityException {
        /*
            r9 = this;
            boolean r0 = r9.isUserNode()
            if (r0 == 0) goto L_0x0009
            java.io.File r1 = userLockFile
            goto L_0x000b
        L_0x0009:
            java.io.File r1 = systemLockFile
        L_0x000b:
            int r2 = INIT_SLEEP_TIME
            long r2 = (long) r2
            r4 = 0
            r5 = r4
            r6 = r5
        L_0x0011:
            int r7 = MAX_ATTEMPTS
            if (r5 >= r7) goto L_0x0040
            if (r0 == 0) goto L_0x001a
            r7 = 384(0x180, float:5.38E-43)
            goto L_0x001c
        L_0x001a:
            r7 = 420(0x1a4, float:5.89E-43)
        L_0x001c:
            java.lang.String r8 = r1.getCanonicalPath()     // Catch:{ IOException -> 0x0033 }
            int[] r7 = lockFile0(r8, r7, r10)     // Catch:{ IOException -> 0x0033 }
            r8 = 1
            r6 = r7[r8]     // Catch:{ IOException -> 0x0033 }
            r7 = r7[r4]     // Catch:{ IOException -> 0x0033 }
            if (r7 == 0) goto L_0x0033
            if (r0 == 0) goto L_0x0030
            userRootLockHandle = r7     // Catch:{ IOException -> 0x0033 }
            goto L_0x0032
        L_0x0030:
            systemRootLockHandle = r7     // Catch:{ IOException -> 0x0033 }
        L_0x0032:
            return r8
        L_0x0033:
            java.lang.Thread.sleep(r2)     // Catch:{ InterruptedException -> 0x003c }
            r7 = 2
            long r2 = r2 * r7
            int r5 = r5 + 1
            goto L_0x0011
        L_0x003c:
            r9.checkLockFile0ErrorCode(r6)
            return r4
        L_0x0040:
            r9.checkLockFile0ErrorCode(r6)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.prefs.FileSystemPreferences.lockFile(boolean):boolean");
    }

    private void checkLockFile0ErrorCode(int i) throws SecurityException {
        String str = "System prefs.";
        if (i == 13) {
            StringBuilder sb = new StringBuilder("Could not lock ");
            if (isUserNode()) {
                str = "User prefs.";
            }
            sb.append(str);
            sb.append(" Lock file access denied.");
            throw new SecurityException(sb.toString());
        } else if (i != 11) {
            PlatformLogger logger = getLogger();
            StringBuilder sb2 = new StringBuilder("Could not lock ");
            if (isUserNode()) {
                str = "User prefs. ";
            }
            sb2.append(str);
            sb2.append(" Unix error code ");
            sb2.append(i);
            sb2.append(BaseIconCache.EMPTY_CLASS_NAME);
            logger.warning(sb2.toString());
        }
    }

    private void unlockFile() {
        boolean isUserNode2 = isUserNode();
        int i = isUserNode2 ? userRootLockHandle : systemRootLockHandle;
        String str = "user";
        if (i == 0) {
            PlatformLogger logger = getLogger();
            StringBuilder sb = new StringBuilder("Unlock: zero lockHandle for ");
            if (!isUserNode2) {
                str = "system";
            }
            sb.append(str);
            sb.append(" preferences.)");
            logger.warning(sb.toString());
            return;
        }
        int unlockFile0 = unlockFile0(i);
        if (unlockFile0 != 0) {
            PlatformLogger logger2 = getLogger();
            StringBuilder sb2 = new StringBuilder("Could not drop file-lock on ");
            if (!isUserNode()) {
                str = "system";
            }
            sb2.append(str);
            sb2.append(" preferences. Unix error code ");
            sb2.append(unlockFile0);
            sb2.append(BaseIconCache.EMPTY_CLASS_NAME);
            logger2.warning(sb2.toString());
            if (unlockFile0 == 13) {
                StringBuilder sb3 = new StringBuilder("Could not unlock");
                sb3.append(isUserNode() ? "User prefs." : "System prefs.");
                sb3.append(" Lock file access denied.");
                throw new SecurityException(sb3.toString());
            }
        }
        if (isUserNode()) {
            userRootLockHandle = 0;
        } else {
            systemRootLockHandle = 0;
        }
    }
}
