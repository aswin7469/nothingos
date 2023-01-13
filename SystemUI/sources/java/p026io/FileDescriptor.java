package java.p026io;

import android.system.C0308Os;
import android.system.ErrnoException;
import android.system.OsConstants;
import sun.misc.JavaIOFileDescriptorAccess;
import sun.misc.SharedSecrets;

/* renamed from: java.io.FileDescriptor */
public final class FileDescriptor {
    public static final long NO_OWNER = 0;
    public static final FileDescriptor err = new FileDescriptor(2);

    /* renamed from: in */
    public static final FileDescriptor f516in = new FileDescriptor(0);
    public static final FileDescriptor out = new FileDescriptor(1);
    /* access modifiers changed from: private */
    public int descriptor;
    private long ownerId;
    private final Object releaseLock;

    private static native boolean isSocket(int i);

    public native void sync() throws SyncFailedException;

    public FileDescriptor() {
        this.ownerId = 0;
        this.releaseLock = new Object();
        this.descriptor = -1;
    }

    private FileDescriptor(int i) {
        this.ownerId = 0;
        this.releaseLock = new Object();
        this.descriptor = i;
    }

    static {
        SharedSecrets.setJavaIOFileDescriptorAccess(new JavaIOFileDescriptorAccess() {
            public void set(FileDescriptor fileDescriptor, int i) {
                fileDescriptor.descriptor = i;
            }

            public int get(FileDescriptor fileDescriptor) {
                return fileDescriptor.descriptor;
            }

            public void setHandle(FileDescriptor fileDescriptor, long j) {
                throw new UnsupportedOperationException();
            }

            public long getHandle(FileDescriptor fileDescriptor) {
                throw new UnsupportedOperationException();
            }
        });
    }

    public boolean valid() {
        return this.descriptor != -1;
    }

    public final int getInt$() {
        return this.descriptor;
    }

    public final void setInt$(int i) {
        this.descriptor = i;
    }

    public void cloneForFork() {
        try {
            this.descriptor = C0308Os.fcntlInt(this, OsConstants.F_DUPFD_CLOEXEC, 0);
        } catch (ErrnoException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    public long getOwnerId$() {
        return this.ownerId;
    }

    public void setOwnerId$(long j) {
        this.ownerId = j;
    }

    public FileDescriptor release$() {
        FileDescriptor fileDescriptor = new FileDescriptor();
        synchronized (this.releaseLock) {
            fileDescriptor.descriptor = this.descriptor;
            fileDescriptor.ownerId = this.ownerId;
            this.descriptor = -1;
            this.ownerId = 0;
        }
        return fileDescriptor;
    }

    public boolean isSocket$() {
        return isSocket(this.descriptor);
    }
}
