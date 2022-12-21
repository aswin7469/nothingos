package sun.nio.p035fs;

import com.sun.nio.file.SensitivityWatchEventModifier;
import dalvik.system.CloseGuard;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.p026io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import sun.misc.Unsafe;

/* renamed from: sun.nio.fs.LinuxWatchService */
class LinuxWatchService extends AbstractWatchService {
    /* access modifiers changed from: private */
    public static final Unsafe unsafe = Unsafe.getUnsafe();
    /* access modifiers changed from: private */
    public final Poller poller;

    private static native void configureBlocking(int i, boolean z) throws UnixException;

    /* access modifiers changed from: private */
    public static native int[] eventOffsets();

    /* access modifiers changed from: private */
    public static native int eventSize();

    /* access modifiers changed from: private */
    public static native int inotifyAddWatch(int i, long j, int i2) throws UnixException;

    private static native int inotifyInit() throws UnixException;

    /* access modifiers changed from: private */
    public static native void inotifyRmWatch(int i, int i2) throws UnixException;

    /* access modifiers changed from: private */
    public static native int poll(int i, int i2) throws UnixException;

    private static native void socketpair(int[] iArr) throws UnixException;

    LinuxWatchService(UnixFileSystem unixFileSystem) throws IOException {
        String str;
        try {
            int inotifyInit = inotifyInit();
            int[] iArr = new int[2];
            try {
                configureBlocking(inotifyInit, false);
                socketpair(iArr);
                configureBlocking(iArr[0], false);
                Poller poller2 = new Poller(unixFileSystem, this, inotifyInit, iArr);
                this.poller = poller2;
                poller2.start();
            } catch (UnixException e) {
                UnixNativeDispatcher.close(inotifyInit);
                throw new IOException(e.errorString());
            }
        } catch (UnixException e2) {
            if (e2.errno() == UnixConstants.EMFILE) {
                str = "User limit of inotify instances reached or too many open files";
            } else {
                str = e2.errorString();
            }
            throw new IOException(str);
        }
    }

    /* access modifiers changed from: package-private */
    public WatchKey register(Path path, WatchEvent.Kind<?>[] kindArr, WatchEvent.Modifier... modifierArr) throws IOException {
        return this.poller.register(path, kindArr, modifierArr);
    }

    /* access modifiers changed from: package-private */
    public void implClose() throws IOException {
        this.poller.close();
    }

    /* renamed from: sun.nio.fs.LinuxWatchService$LinuxWatchKey */
    private static class LinuxWatchKey extends AbstractWatchKey {
        private final int ifd;

        /* renamed from: wd */
        private volatile int f908wd;

        LinuxWatchKey(UnixPath unixPath, LinuxWatchService linuxWatchService, int i, int i2) {
            super(unixPath, linuxWatchService);
            this.ifd = i;
            this.f908wd = i2;
        }

        /* access modifiers changed from: package-private */
        public int descriptor() {
            return this.f908wd;
        }

        /* access modifiers changed from: package-private */
        public void invalidate(boolean z) {
            if (z) {
                try {
                    LinuxWatchService.inotifyRmWatch(this.ifd, this.f908wd);
                } catch (UnixException unused) {
                }
            }
            this.f908wd = -1;
        }

        public boolean isValid() {
            return this.f908wd != -1;
        }

        public void cancel() {
            if (isValid()) {
                ((LinuxWatchService) watcher()).poller.cancel(this);
            }
        }
    }

    /* renamed from: sun.nio.fs.LinuxWatchService$Poller */
    private static class Poller extends AbstractPoller {
        private static final int BUFFER_SIZE = 8192;
        private static final int IN_ATTRIB = 4;
        private static final int IN_CREATE = 256;
        private static final int IN_DELETE = 512;
        private static final int IN_IGNORED = 32768;
        private static final int IN_MODIFY = 2;
        private static final int IN_MOVED_FROM = 64;
        private static final int IN_MOVED_TO = 128;
        private static final int IN_Q_OVERFLOW = 16384;
        private static final int IN_UNMOUNT = 8192;
        private static final int OFFSETOF_LEN;
        private static final int OFFSETOF_MASK;
        private static final int OFFSETOF_NAME;
        private static final int OFFSETOF_WD;
        private static final int SIZEOF_INOTIFY_EVENT = LinuxWatchService.eventSize();
        private static final int[] offsets;
        private final long address = LinuxWatchService.unsafe.allocateMemory(8192);

        /* renamed from: fs */
        private final UnixFileSystem f909fs;
        private final CloseGuard guard;
        private final int ifd;
        private final int[] socketpair;
        private final LinuxWatchService watcher;
        private final Map<Integer, LinuxWatchKey> wdToKey = new HashMap();

        static {
            int[] r0 = LinuxWatchService.eventOffsets();
            offsets = r0;
            OFFSETOF_WD = r0[0];
            OFFSETOF_MASK = r0[1];
            OFFSETOF_LEN = r0[3];
            OFFSETOF_NAME = r0[4];
        }

        Poller(UnixFileSystem fs, LinuxWatchService watcher2, int ifd2, int[] sp) {
            CloseGuard closeGuard = CloseGuard.get();
            this.guard = closeGuard;
            this.f909fs = fs;
            this.watcher = watcher2;
            this.ifd = ifd2;
            this.socketpair = sp;
            closeGuard.open("close");
        }

        /* access modifiers changed from: package-private */
        public void wakeup() throws IOException {
            try {
                UnixNativeDispatcher.write(this.socketpair[1], this.address, 1);
            } catch (UnixException e) {
                throw new IOException(e.errorString());
            }
        }

        /* access modifiers changed from: package-private */
        public Object implRegister(Path obj, Set<? extends WatchEvent.Kind<?>> set, WatchEvent.Modifier... modifiers) {
            NativeBuffer asNativeBuffer;
            UnixPath unixPath = (UnixPath) obj;
            int i = 0;
            for (WatchEvent.Kind<Path> kind : set) {
                if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                    i |= 384;
                } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                    i |= 576;
                } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    i |= 6;
                }
            }
            if (modifiers.length > 0) {
                for (WatchEvent.Modifier modifier : modifiers) {
                    if (modifier == null) {
                        return new NullPointerException();
                    }
                    if (!(modifier instanceof SensitivityWatchEventModifier)) {
                        return new UnsupportedOperationException("Modifier not supported");
                    }
                }
            }
            try {
                if (!UnixFileAttributes.get(unixPath, true).isDirectory()) {
                    return new NotDirectoryException(unixPath.getPathForExceptionMessage());
                }
                try {
                    asNativeBuffer = NativeBuffers.asNativeBuffer(unixPath.getByteArrayForSysCalls());
                    int r3 = LinuxWatchService.inotifyAddWatch(this.ifd, asNativeBuffer.address(), i);
                    asNativeBuffer.release();
                    LinuxWatchKey linuxWatchKey = this.wdToKey.get(Integer.valueOf(r3));
                    if (linuxWatchKey != null) {
                        return linuxWatchKey;
                    }
                    LinuxWatchKey linuxWatchKey2 = new LinuxWatchKey(unixPath, this.watcher, this.ifd, r3);
                    this.wdToKey.put(Integer.valueOf(r3), linuxWatchKey2);
                    return linuxWatchKey2;
                } catch (UnixException e) {
                    if (e.errno() == UnixConstants.ENOSPC) {
                        return new IOException("User limit of inotify watches reached");
                    }
                    return e.asIOException(unixPath);
                } catch (Throwable th) {
                    asNativeBuffer.release();
                    throw th;
                }
            } catch (UnixException e2) {
                return e2.asIOException(unixPath);
            }
        }

        /* access modifiers changed from: package-private */
        public void implCancelKey(WatchKey obj) {
            LinuxWatchKey linuxWatchKey = (LinuxWatchKey) obj;
            if (linuxWatchKey.isValid()) {
                this.wdToKey.remove(Integer.valueOf(linuxWatchKey.descriptor()));
                linuxWatchKey.invalidate(true);
            }
        }

        /* access modifiers changed from: package-private */
        public void implCloseAll() {
            this.guard.close();
            for (Map.Entry<Integer, LinuxWatchKey> value : this.wdToKey.entrySet()) {
                ((LinuxWatchKey) value.getValue()).invalidate(true);
            }
            this.wdToKey.clear();
            LinuxWatchService.unsafe.freeMemory(this.address);
            UnixNativeDispatcher.close(this.socketpair[0]);
            UnixNativeDispatcher.close(this.socketpair[1]);
            UnixNativeDispatcher.close(this.ifd);
        }

        /* access modifiers changed from: protected */
        public void finalize() throws Throwable {
            try {
                CloseGuard closeGuard = this.guard;
                if (closeGuard != null) {
                    closeGuard.warnIfOpen();
                }
                close();
            } finally {
                super.finalize();
            }
        }

        public void run() {
            int r2;
            int i;
            int i2;
            while (true) {
                try {
                    r2 = LinuxWatchService.poll(this.ifd, this.socketpair[0]);
                    i = UnixNativeDispatcher.read(this.ifd, this.address, 8192);
                } catch (UnixException e) {
                    if (e.errno() != UnixConstants.EAGAIN) {
                        int i3 = r2;
                        throw e;
                    }
                } catch (UnixException e2) {
                    if (e2.errno() == UnixConstants.EAGAIN) {
                        i = 0;
                    } else {
                        int i4 = r2;
                        throw e2;
                    }
                } catch (UnixException e3) {
                    e3.printStackTrace();
                    return;
                }
                if (r2 > 1 || (r2 == 1 && i == 0)) {
                    UnixNativeDispatcher.read(this.socketpair[0], this.address, 8192);
                    if (processRequests()) {
                        return;
                    }
                }
                int i5 = 0;
                while (i5 < i) {
                    long j = this.address + ((long) i5);
                    int i6 = LinuxWatchService.unsafe.getInt(((long) OFFSETOF_WD) + j);
                    int i7 = LinuxWatchService.unsafe.getInt(((long) OFFSETOF_MASK) + j);
                    int i8 = LinuxWatchService.unsafe.getInt(((long) OFFSETOF_LEN) + j);
                    UnixPath unixPath = null;
                    if (i8 > 0) {
                        int i9 = i8;
                        while (true) {
                            if (i9 <= 0) {
                                break;
                            }
                            if (LinuxWatchService.unsafe.getByte(((((long) OFFSETOF_NAME) + j) + ((long) i9)) - 1) != 0) {
                                break;
                            }
                            i9--;
                        }
                        if (i9 > 0) {
                            byte[] bArr = new byte[i9];
                            int i10 = 0;
                            while (i10 < i9) {
                                bArr[i10] = LinuxWatchService.unsafe.getByte(((long) OFFSETOF_NAME) + j + ((long) i10));
                                i10++;
                                r2 = r2;
                                j = j;
                            }
                            i2 = r2;
                            long j2 = j;
                            unixPath = new UnixPath(this.f909fs, bArr);
                            processEvent(i6, i7, unixPath);
                            i5 += SIZEOF_INOTIFY_EVENT + i8;
                            r2 = i2;
                        }
                    }
                    i2 = r2;
                    long j3 = j;
                    processEvent(i6, i7, unixPath);
                    i5 += SIZEOF_INOTIFY_EVENT + i8;
                    r2 = i2;
                }
                int i11 = r2;
            }
        }

        private WatchEvent.Kind<?> maskToEventKind(int mask) {
            if ((mask & 2) > 0) {
                return StandardWatchEventKinds.ENTRY_MODIFY;
            }
            if ((mask & 4) > 0) {
                return StandardWatchEventKinds.ENTRY_MODIFY;
            }
            if ((mask & 256) > 0) {
                return StandardWatchEventKinds.ENTRY_CREATE;
            }
            if ((mask & 128) > 0) {
                return StandardWatchEventKinds.ENTRY_CREATE;
            }
            if ((mask & 512) > 0) {
                return StandardWatchEventKinds.ENTRY_DELETE;
            }
            if ((mask & 64) > 0) {
                return StandardWatchEventKinds.ENTRY_DELETE;
            }
            return null;
        }

        private void processEvent(int wd, int mask, UnixPath name) {
            WatchEvent.Kind<?> maskToEventKind;
            if ((mask & 16384) > 0) {
                for (Map.Entry<Integer, LinuxWatchKey> value : this.wdToKey.entrySet()) {
                    ((LinuxWatchKey) value.getValue()).signalEvent(StandardWatchEventKinds.OVERFLOW, (Object) null);
                }
                return;
            }
            LinuxWatchKey linuxWatchKey = this.wdToKey.get(Integer.valueOf(wd));
            if (linuxWatchKey != null) {
                if ((32768 & mask) > 0) {
                    this.wdToKey.remove(Integer.valueOf(wd));
                    linuxWatchKey.invalidate(false);
                    linuxWatchKey.signal();
                } else if (name != null && (maskToEventKind = maskToEventKind(mask)) != null) {
                    linuxWatchKey.signalEvent(maskToEventKind, name);
                }
            }
        }
    }
}
