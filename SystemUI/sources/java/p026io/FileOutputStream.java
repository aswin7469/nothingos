package java.p026io;

import android.system.OsConstants;
import dalvik.system.CloseGuard;
import java.nio.channels.FileChannel;
import libcore.p030io.IoBridge;
import libcore.p030io.IoTracker;
import libcore.p030io.IoUtils;
import sun.nio.p033ch.FileChannelImpl;

/* renamed from: java.io.FileOutputStream */
public class FileOutputStream extends OutputStream {
    private final boolean append;
    private FileChannel channel;
    private final Object closeLock;
    private volatile boolean closed;

    /* renamed from: fd */
    private final FileDescriptor f520fd;
    private final CloseGuard guard;
    private final boolean isFdOwner;
    private final String path;
    private final IoTracker tracker;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public FileOutputStream(String name) throws FileNotFoundException {
        this(name != null ? new File(name) : null, false);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public FileOutputStream(String name, boolean append2) throws FileNotFoundException {
        this(name != null ? new File(name) : null, append2);
    }

    public FileOutputStream(File file) throws FileNotFoundException {
        this(file, false);
    }

    public FileOutputStream(File file, boolean append2) throws FileNotFoundException {
        this.closeLock = new Object();
        this.closed = false;
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        this.tracker = new IoTracker();
        String path2 = file != null ? file.getPath() : null;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkWrite(path2);
        }
        if (path2 == null) {
            throw new NullPointerException();
        } else if (!file.isInvalid()) {
            FileDescriptor open = IoBridge.open(path2, OsConstants.O_WRONLY | OsConstants.O_CREAT | (append2 ? OsConstants.O_APPEND : OsConstants.O_TRUNC));
            this.f520fd = open;
            this.isFdOwner = true;
            this.append = append2;
            this.path = path2;
            IoUtils.setFdOwner(open, this);
            closeGuard.open("close");
        } else {
            throw new FileNotFoundException("Invalid file path");
        }
    }

    public FileOutputStream(FileDescriptor fdObj) {
        this(fdObj, false);
    }

    public FileOutputStream(FileDescriptor fdObj, boolean isFdOwner2) {
        this.closeLock = new Object();
        this.closed = false;
        this.guard = CloseGuard.get();
        this.tracker = new IoTracker();
        if (fdObj != null) {
            this.f520fd = fdObj;
            this.append = false;
            this.path = null;
            this.isFdOwner = isFdOwner2;
            if (isFdOwner2) {
                IoUtils.setFdOwner(fdObj, this);
                return;
            }
            return;
        }
        throw new NullPointerException("fdObj == null");
    }

    public void write(int b) throws IOException {
        write(new byte[]{(byte) b}, 0, 1);
    }

    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        if (!this.closed || len <= 0) {
            this.tracker.trackIo(len, IoTracker.Mode.WRITE);
            IoBridge.write(this.f520fd, b, off, len);
            return;
        }
        throw new IOException("Stream Closed");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0014, code lost:
        if (r0 == null) goto L_0x0019;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0016, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001b, code lost:
        if (r2.isFdOwner == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001d, code lost:
        libcore.p030io.IoBridge.closeAndSignalBlockedThreads(r2.f520fd);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x000d, code lost:
        r2.guard.close();
        r0 = r2.channel;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() throws java.p026io.IOException {
        /*
            r2 = this;
            java.lang.Object r0 = r2.closeLock
            monitor-enter(r0)
            boolean r1 = r2.closed     // Catch:{ all -> 0x0023 }
            if (r1 == 0) goto L_0x0009
            monitor-exit(r0)     // Catch:{ all -> 0x0023 }
            return
        L_0x0009:
            r1 = 1
            r2.closed = r1     // Catch:{ all -> 0x0023 }
            monitor-exit(r0)     // Catch:{ all -> 0x0023 }
            dalvik.system.CloseGuard r0 = r2.guard
            r0.close()
            java.nio.channels.FileChannel r0 = r2.channel
            if (r0 == 0) goto L_0x0019
            r0.close()
        L_0x0019:
            boolean r0 = r2.isFdOwner
            if (r0 == 0) goto L_0x0022
            java.io.FileDescriptor r0 = r2.f520fd
            libcore.p030io.IoBridge.closeAndSignalBlockedThreads(r0)
        L_0x0022:
            return
        L_0x0023:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0023 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.FileOutputStream.close():void");
    }

    public final FileDescriptor getFD() throws IOException {
        FileDescriptor fileDescriptor = this.f520fd;
        if (fileDescriptor != null) {
            return fileDescriptor;
        }
        throw new IOException();
    }

    public FileChannel getChannel() {
        FileChannel fileChannel;
        synchronized (this) {
            if (this.channel == null) {
                this.channel = FileChannelImpl.open(this.f520fd, this.path, false, true, this.append, this);
            }
            fileChannel = this.channel;
        }
        return fileChannel;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws IOException {
        CloseGuard closeGuard = this.guard;
        if (closeGuard != null) {
            closeGuard.warnIfOpen();
        }
        FileDescriptor fileDescriptor = this.f520fd;
        if (fileDescriptor == null) {
            return;
        }
        if (fileDescriptor == FileDescriptor.out || this.f520fd == FileDescriptor.err) {
            flush();
        } else {
            close();
        }
    }
}
