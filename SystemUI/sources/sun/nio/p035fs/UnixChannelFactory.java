package sun.nio.p035fs;

import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.p026io.FileDescriptor;
import java.util.Set;
import sun.misc.JavaIOFileDescriptorAccess;
import sun.misc.SharedSecrets;
import sun.nio.p033ch.FileChannelImpl;
import sun.nio.p033ch.SimpleAsynchronousFileChannelImpl;
import sun.nio.p033ch.ThreadPool;

/* renamed from: sun.nio.fs.UnixChannelFactory */
class UnixChannelFactory {
    private static final JavaIOFileDescriptorAccess fdAccess = SharedSecrets.getJavaIOFileDescriptorAccess();

    protected UnixChannelFactory() {
    }

    /* renamed from: sun.nio.fs.UnixChannelFactory$Flags */
    protected static class Flags {
        boolean append;
        boolean create;
        boolean createNew;
        boolean deleteOnClose;
        boolean dsync;
        boolean noFollowLinks;
        boolean read;
        boolean sync;
        boolean truncateExisting;
        boolean write;

        protected Flags() {
        }

        static Flags toFlags(Set<? extends OpenOption> set) {
            Flags flags = new Flags();
            for (OpenOption openOption : set) {
                if (openOption instanceof StandardOpenOption) {
                    switch (C47901.$SwitchMap$java$nio$file$StandardOpenOption[((StandardOpenOption) openOption).ordinal()]) {
                        case 1:
                            flags.read = true;
                            break;
                        case 2:
                            flags.write = true;
                            break;
                        case 3:
                            flags.append = true;
                            break;
                        case 4:
                            flags.truncateExisting = true;
                            break;
                        case 5:
                            flags.create = true;
                            break;
                        case 6:
                            flags.createNew = true;
                            break;
                        case 7:
                            flags.deleteOnClose = true;
                            break;
                        case 8:
                            break;
                        case 9:
                            flags.sync = true;
                            break;
                        case 10:
                            flags.dsync = true;
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                } else if (openOption != LinkOption.NOFOLLOW_LINKS || UnixConstants.O_NOFOLLOW == 0) {
                    openOption.getClass();
                    throw new UnsupportedOperationException(openOption + " not supported");
                } else {
                    flags.noFollowLinks = true;
                }
            }
            return flags;
        }
    }

    /* renamed from: sun.nio.fs.UnixChannelFactory$1 */
    static /* synthetic */ class C47901 {
        static final /* synthetic */ int[] $SwitchMap$java$nio$file$StandardOpenOption;

        /* JADX WARNING: Can't wrap try/catch for region: R(20:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|(3:19|20|22)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|22) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                java.nio.file.StandardOpenOption[] r0 = java.nio.file.StandardOpenOption.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$nio$file$StandardOpenOption = r0
                java.nio.file.StandardOpenOption r1 = java.nio.file.StandardOpenOption.READ     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$java$nio$file$StandardOpenOption     // Catch:{ NoSuchFieldError -> 0x001d }
                java.nio.file.StandardOpenOption r1 = java.nio.file.StandardOpenOption.WRITE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$java$nio$file$StandardOpenOption     // Catch:{ NoSuchFieldError -> 0x0028 }
                java.nio.file.StandardOpenOption r1 = java.nio.file.StandardOpenOption.APPEND     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$java$nio$file$StandardOpenOption     // Catch:{ NoSuchFieldError -> 0x0033 }
                java.nio.file.StandardOpenOption r1 = java.nio.file.StandardOpenOption.TRUNCATE_EXISTING     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$java$nio$file$StandardOpenOption     // Catch:{ NoSuchFieldError -> 0x003e }
                java.nio.file.StandardOpenOption r1 = java.nio.file.StandardOpenOption.CREATE     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$java$nio$file$StandardOpenOption     // Catch:{ NoSuchFieldError -> 0x0049 }
                java.nio.file.StandardOpenOption r1 = java.nio.file.StandardOpenOption.CREATE_NEW     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$java$nio$file$StandardOpenOption     // Catch:{ NoSuchFieldError -> 0x0054 }
                java.nio.file.StandardOpenOption r1 = java.nio.file.StandardOpenOption.DELETE_ON_CLOSE     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$java$nio$file$StandardOpenOption     // Catch:{ NoSuchFieldError -> 0x0060 }
                java.nio.file.StandardOpenOption r1 = java.nio.file.StandardOpenOption.SPARSE     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$java$nio$file$StandardOpenOption     // Catch:{ NoSuchFieldError -> 0x006c }
                java.nio.file.StandardOpenOption r1 = java.nio.file.StandardOpenOption.SYNC     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$java$nio$file$StandardOpenOption     // Catch:{ NoSuchFieldError -> 0x0078 }
                java.nio.file.StandardOpenOption r1 = java.nio.file.StandardOpenOption.DSYNC     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.UnixChannelFactory.C47901.<clinit>():void");
        }
    }

    static FileChannel newFileChannel(int i, String str, boolean z, boolean z2) {
        FileDescriptor fileDescriptor = new FileDescriptor();
        fdAccess.set(fileDescriptor, i);
        return FileChannelImpl.open(fileDescriptor, str, z, z2, (Object) null);
    }

    static FileChannel newFileChannel(int i, UnixPath unixPath, String str, Set<? extends OpenOption> set, int i2) throws UnixException {
        Flags flags = Flags.toFlags(set);
        if (!flags.read && !flags.write) {
            if (flags.append) {
                flags.write = true;
            } else {
                flags.read = true;
            }
        }
        if (flags.read && flags.append) {
            throw new IllegalArgumentException("READ + APPEND not allowed");
        } else if (!flags.append || !flags.truncateExisting) {
            return FileChannelImpl.open(open(i, unixPath, str, flags, i2), unixPath.toString(), flags.read, flags.write, flags.append, (Object) null);
        } else {
            throw new IllegalArgumentException("APPEND + TRUNCATE_EXISTING not allowed");
        }
    }

    static FileChannel newFileChannel(UnixPath unixPath, Set<? extends OpenOption> set, int i) throws UnixException {
        return newFileChannel(-1, unixPath, (String) null, set, i);
    }

    static AsynchronousFileChannel newAsynchronousFileChannel(UnixPath unixPath, Set<? extends OpenOption> set, int i, ThreadPool threadPool) throws UnixException {
        Flags flags = Flags.toFlags(set);
        if (!flags.read && !flags.write) {
            flags.read = true;
        }
        if (!flags.append) {
            return SimpleAsynchronousFileChannelImpl.open(open(-1, unixPath, (String) null, flags, i), flags.read, flags.write, threadPool);
        }
        throw new UnsupportedOperationException("APPEND not allowed");
    }

    protected static FileDescriptor open(int i, UnixPath unixPath, String str, Flags flags, int i2) throws UnixException {
        int i3;
        int i4;
        int i5;
        if (!flags.read || !flags.write) {
            i3 = flags.write ? UnixConstants.O_WRONLY : UnixConstants.O_RDONLY;
        } else {
            i3 = UnixConstants.O_RDWR;
        }
        boolean z = true;
        if (flags.write) {
            if (flags.truncateExisting) {
                i3 |= UnixConstants.O_TRUNC;
            }
            if (flags.append) {
                i3 |= UnixConstants.O_APPEND;
            }
            if (flags.createNew) {
                byte[] asByteArray = unixPath.asByteArray();
                if (asByteArray[asByteArray.length - 1] == 46 && (asByteArray.length == 1 || asByteArray[asByteArray.length - 2] == 47)) {
                    throw new UnixException(UnixConstants.EEXIST);
                }
                i5 = UnixConstants.O_CREAT | UnixConstants.O_EXCL;
            } else if (flags.create) {
                i5 = UnixConstants.O_CREAT;
            }
            i3 |= i5;
        }
        if (!flags.createNew && (flags.noFollowLinks || flags.deleteOnClose)) {
            if (flags.deleteOnClose && UnixConstants.O_NOFOLLOW == 0) {
                try {
                    if (UnixFileAttributes.get(unixPath, false).isSymbolicLink()) {
                        throw new UnixException("DELETE_ON_CLOSE specified and file is a symbolic link");
                    }
                } catch (UnixException e) {
                    if (!flags.create || e.errno() != UnixConstants.ENOENT) {
                        throw e;
                    }
                }
            }
            i3 |= UnixConstants.O_NOFOLLOW;
            z = false;
        }
        if (flags.dsync) {
            i3 |= UnixConstants.O_DSYNC;
        }
        if (flags.sync) {
            i3 |= UnixConstants.O_SYNC;
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            if (str == null) {
                str = unixPath.getPathForPermissionCheck();
            }
            if (flags.read) {
                securityManager.checkRead(str);
            }
            if (flags.write) {
                securityManager.checkWrite(str);
            }
            if (flags.deleteOnClose) {
                securityManager.checkDelete(str);
            }
        }
        if (i >= 0) {
            try {
                i4 = UnixNativeDispatcher.openat(i, unixPath.asByteArray(), i3, i2);
            } catch (UnixException e2) {
                e = e2;
                if (flags.createNew && e.errno() == UnixConstants.EISDIR) {
                    e.setError(UnixConstants.EEXIST);
                }
                if (!z && e.errno() == UnixConstants.ELOOP) {
                    e = new UnixException(e.getMessage() + " (NOFOLLOW_LINKS specified)");
                }
                throw e;
            }
        } else {
            i4 = UnixNativeDispatcher.open(unixPath, i3, i2);
        }
        if (flags.deleteOnClose) {
            if (i >= 0) {
                try {
                    UnixNativeDispatcher.unlinkat(i, unixPath.asByteArray(), 0);
                } catch (UnixException unused) {
                }
            } else {
                UnixNativeDispatcher.unlink(unixPath);
            }
        }
        FileDescriptor fileDescriptor = new FileDescriptor();
        fdAccess.set(fileDescriptor, i4);
        return fileDescriptor;
    }
}
