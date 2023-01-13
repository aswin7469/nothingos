package java.p026io;

import android.system.OsConstants;
import dalvik.system.BlockGuard;
import dalvik.system.CloseGuard;
import java.nio.channels.FileChannel;
import libcore.p030io.IoBridge;
import libcore.p030io.IoTracker;
import libcore.p030io.IoUtils;
import sun.nio.p033ch.FileChannelImpl;

/* renamed from: java.io.FileInputStream */
public class FileInputStream extends InputStream {
    private FileChannel channel;
    private final Object closeLock;
    private volatile boolean closed;

    /* renamed from: fd */
    private final FileDescriptor f517fd;
    private final CloseGuard guard;
    private final boolean isFdOwner;
    private final String path;
    private final IoTracker tracker;

    private native int available0() throws IOException;

    private native long skip0(long j) throws IOException, UseManualSkipException;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public FileInputStream(String name) throws FileNotFoundException {
        this(name != null ? new File(name) : null);
    }

    public FileInputStream(File file) throws FileNotFoundException {
        String str = null;
        this.channel = null;
        this.closeLock = new Object();
        this.closed = false;
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        this.tracker = new IoTracker();
        str = file != null ? file.getPath() : str;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkRead(str);
        }
        if (str == null) {
            throw new NullPointerException();
        } else if (!file.isInvalid()) {
            FileDescriptor open = IoBridge.open(str, OsConstants.O_RDONLY);
            this.f517fd = open;
            this.isFdOwner = true;
            this.path = str;
            IoUtils.setFdOwner(open, this);
            closeGuard.open("close");
        } else {
            throw new FileNotFoundException("Invalid file path");
        }
    }

    public FileInputStream(FileDescriptor fdObj) {
        this(fdObj, false);
    }

    public FileInputStream(FileDescriptor fdObj, boolean isFdOwner2) {
        this.channel = null;
        this.closeLock = new Object();
        this.closed = false;
        this.guard = CloseGuard.get();
        this.tracker = new IoTracker();
        if (fdObj != null) {
            this.f517fd = fdObj;
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

    public int read() throws IOException {
        byte[] bArr = new byte[1];
        if (read(bArr, 0, 1) != -1) {
            return bArr[0] & 255;
        }
        return -1;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (!this.closed || len <= 0) {
            this.tracker.trackIo(len, IoTracker.Mode.READ);
            return IoBridge.read(this.f517fd, b, off, len);
        }
        throw new IOException("Stream Closed");
    }

    public long skip(long n) throws IOException {
        if (!this.closed) {
            try {
                BlockGuard.getThreadPolicy().onReadFromDisk();
                return skip0(n);
            } catch (UseManualSkipException e) {
                return super.skip(n);
            }
        } else {
            throw new IOException("Stream Closed");
        }
    }

    /* renamed from: java.io.FileInputStream$UseManualSkipException */
    private static class UseManualSkipException extends Exception {
        private UseManualSkipException() {
        }
    }

    public int available() throws IOException {
        if (!this.closed) {
            return available0();
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
        libcore.p030io.IoBridge.closeAndSignalBlockedThreads(r2.f517fd);
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
            java.io.FileDescriptor r0 = r2.f517fd
            libcore.p030io.IoBridge.closeAndSignalBlockedThreads(r0)
        L_0x0022:
            return
        L_0x0023:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0023 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.FileInputStream.close():void");
    }

    public final FileDescriptor getFD() throws IOException {
        FileDescriptor fileDescriptor = this.f517fd;
        if (fileDescriptor != null) {
            return fileDescriptor;
        }
        throw new IOException();
    }

    public FileChannel getChannel() {
        FileChannel fileChannel;
        synchronized (this) {
            if (this.channel == null) {
                this.channel = FileChannelImpl.open(this.f517fd, this.path, true, false, this);
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
        FileDescriptor fileDescriptor = this.f517fd;
        if (fileDescriptor != null && fileDescriptor != FileDescriptor.f516in) {
            close();
        }
    }
}
