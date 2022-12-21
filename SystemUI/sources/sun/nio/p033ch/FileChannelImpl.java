package sun.nio.p033ch;

import dalvik.system.BlockGuard;
import dalvik.system.CloseGuard;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.NonReadableChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.OverlappingFileLockException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.WritableByteChannel;
import java.p026io.Closeable;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import sun.misc.Cleaner;
import sun.security.action.GetPropertyAction;

/* renamed from: sun.nio.ch.FileChannelImpl */
public class FileChannelImpl extends FileChannel {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long MAPPED_TRANSFER_SIZE = 8388608;
    private static final int MAP_PV = 2;
    private static final int MAP_RO = 0;
    private static final int MAP_RW = 1;
    private static final int TRANSFER_SIZE = 8192;
    private static final long allocationGranularity = initIDs();
    private static volatile boolean fileSupported = true;
    private static boolean isSharedFileLockTable;
    private static volatile boolean pipeSupported = true;
    private static volatile boolean propertyChecked;
    private static volatile boolean transferSupported = true;
    private final boolean append;

    /* renamed from: fd */
    public final FileDescriptor f883fd;
    private volatile FileLockTable fileLockTable;
    private final CloseGuard guard;

    /* renamed from: nd */
    private final FileDispatcher f884nd;
    private final Object parent;
    private final String path;
    private final Object positionLock = new Object();
    private final boolean readable;
    private final NativeThreadSet threads = new NativeThreadSet(2);
    private final boolean writable;

    private static native long initIDs();

    private native long map0(int i, long j, long j2) throws IOException;

    private native long position0(FileDescriptor fileDescriptor, long j);

    private native long transferTo0(FileDescriptor fileDescriptor, long j, long j2, FileDescriptor fileDescriptor2);

    /* access modifiers changed from: private */
    public static native int unmap0(long j, long j2);

    private FileChannelImpl(FileDescriptor fd, String path2, boolean readable2, boolean writable2, boolean append2, Object parent2) {
        CloseGuard closeGuard = CloseGuard.get();
        this.guard = closeGuard;
        this.f883fd = fd;
        this.readable = readable2;
        this.writable = writable2;
        this.append = append2;
        this.parent = parent2;
        this.path = path2;
        this.f884nd = new FileDispatcherImpl(append2);
        if (fd != null && fd.valid()) {
            closeGuard.open("close");
        }
    }

    public static FileChannel open(FileDescriptor fd, String path2, boolean readable2, boolean writable2, Object parent2) {
        return new FileChannelImpl(fd, path2, readable2, writable2, false, parent2);
    }

    public static FileChannel open(FileDescriptor fd, String path2, boolean readable2, boolean writable2, boolean append2, Object parent2) {
        return new FileChannelImpl(fd, path2, readable2, writable2, append2, parent2);
    }

    private void ensureOpen() throws IOException {
        if (!isOpen()) {
            throw new ClosedChannelException();
        }
    }

    /* access modifiers changed from: protected */
    public void implCloseChannel() throws IOException {
        this.guard.close();
        if (this.fileLockTable != null) {
            for (FileLock next : this.fileLockTable.removeAll()) {
                synchronized (next) {
                    if (next.isValid()) {
                        this.f884nd.release(this.f883fd, next.position(), next.size());
                        ((FileLockImpl) next).invalidate();
                    }
                }
            }
        }
        this.threads.signalAndWait();
        Object obj = this.parent;
        if (obj != null) {
            ((Closeable) obj).close();
        } else {
            this.f884nd.close(this.f883fd);
        }
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

    public int read(ByteBuffer dst) throws IOException {
        ensureOpen();
        if (this.readable) {
            synchronized (this.positionLock) {
                int i = 0;
                int i2 = -1;
                boolean z = true;
                try {
                    begin();
                    i2 = this.threads.add();
                    if (!isOpen()) {
                        this.threads.remove(i2);
                        if (0 <= 0) {
                            z = false;
                        }
                        end(z);
                        return 0;
                    }
                    do {
                        i = IOUtil.read(this.f883fd, dst, -1, this.f884nd);
                        if (i != -3 || !isOpen()) {
                            int normalize = IOStatus.normalize(i);
                        }
                        i = IOUtil.read(this.f883fd, dst, -1, this.f884nd);
                        break;
                    } while (!isOpen());
                    int normalize2 = IOStatus.normalize(i);
                    this.threads.remove(i2);
                    if (i <= 0) {
                        z = false;
                    }
                    end(z);
                    return normalize2;
                } catch (Throwable th) {
                    this.threads.remove(i2);
                    if (i <= 0) {
                        z = false;
                    }
                    end(z);
                    throw th;
                }
            }
        } else {
            throw new NonReadableChannelException();
        }
    }

    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        if (offset < 0 || length < 0 || offset > dsts.length - length) {
            throw new IndexOutOfBoundsException();
        }
        ensureOpen();
        if (this.readable) {
            synchronized (this.positionLock) {
                long j = 0;
                int i = -1;
                boolean z = true;
                try {
                    begin();
                    i = this.threads.add();
                    if (!isOpen()) {
                        this.threads.remove(i);
                        if (0 <= 0) {
                            z = false;
                        }
                        end(z);
                        return 0;
                    }
                    do {
                        j = IOUtil.read(this.f883fd, dsts, offset, length, this.f884nd);
                        if (j != -3 || !isOpen()) {
                            long normalize = IOStatus.normalize(j);
                        }
                        j = IOUtil.read(this.f883fd, dsts, offset, length, this.f884nd);
                        break;
                    } while (!isOpen());
                    long normalize2 = IOStatus.normalize(j);
                    this.threads.remove(i);
                    if (j <= 0) {
                        z = false;
                    }
                    end(z);
                    return normalize2;
                } catch (Throwable th) {
                    this.threads.remove(i);
                    if (j <= 0) {
                        z = false;
                    }
                    end(z);
                    throw th;
                }
            }
        } else {
            throw new NonReadableChannelException();
        }
    }

    public int write(ByteBuffer src) throws IOException {
        ensureOpen();
        if (this.writable) {
            synchronized (this.positionLock) {
                int i = 0;
                int i2 = -1;
                boolean z = true;
                try {
                    begin();
                    i2 = this.threads.add();
                    if (!isOpen()) {
                        this.threads.remove(i2);
                        if (0 <= 0) {
                            z = false;
                        }
                        end(z);
                        return 0;
                    }
                    do {
                        i = IOUtil.write(this.f883fd, src, -1, this.f884nd);
                        if (i != -3 || !isOpen()) {
                            int normalize = IOStatus.normalize(i);
                        }
                        i = IOUtil.write(this.f883fd, src, -1, this.f884nd);
                        break;
                    } while (!isOpen());
                    int normalize2 = IOStatus.normalize(i);
                    this.threads.remove(i2);
                    if (i <= 0) {
                        z = false;
                    }
                    end(z);
                    return normalize2;
                } catch (Throwable th) {
                    this.threads.remove(i2);
                    if (i <= 0) {
                        z = false;
                    }
                    end(z);
                    throw th;
                }
            }
        } else {
            throw new NonWritableChannelException();
        }
    }

    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        if (offset < 0 || length < 0 || offset > srcs.length - length) {
            throw new IndexOutOfBoundsException();
        }
        ensureOpen();
        if (this.writable) {
            synchronized (this.positionLock) {
                long j = 0;
                int i = -1;
                boolean z = true;
                try {
                    begin();
                    i = this.threads.add();
                    if (!isOpen()) {
                        this.threads.remove(i);
                        if (0 <= 0) {
                            z = false;
                        }
                        end(z);
                        return 0;
                    }
                    do {
                        j = IOUtil.write(this.f883fd, srcs, offset, length, this.f884nd);
                        if (j != -3 || !isOpen()) {
                            long normalize = IOStatus.normalize(j);
                        }
                        j = IOUtil.write(this.f883fd, srcs, offset, length, this.f884nd);
                        break;
                    } while (!isOpen());
                    long normalize2 = IOStatus.normalize(j);
                    this.threads.remove(i);
                    if (j <= 0) {
                        z = false;
                    }
                    end(z);
                    return normalize2;
                } catch (Throwable th) {
                    this.threads.remove(i);
                    if (j <= 0) {
                        z = false;
                    }
                    end(z);
                    throw th;
                }
            }
        } else {
            throw new NonWritableChannelException();
        }
    }

    public long position() throws IOException {
        long size;
        ensureOpen();
        synchronized (this.positionLock) {
            int i = -1;
            boolean z = true;
            try {
                begin();
                i = this.threads.add();
                if (!isOpen()) {
                    this.threads.remove(i);
                    if (-1 <= -1) {
                        z = false;
                    }
                    end(z);
                    return 0;
                }
                if (this.append) {
                    BlockGuard.getThreadPolicy().onWriteToDisk();
                }
                while (true) {
                    size = this.append ? this.f884nd.size(this.f883fd) : position0(this.f883fd, -1);
                    if (size == -3) {
                        if (!isOpen()) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                long normalize = IOStatus.normalize(size);
                this.threads.remove(i);
                if (size <= -1) {
                    z = false;
                }
                end(z);
                return normalize;
            } catch (Throwable th) {
                this.threads.remove(i);
                if (-1 <= -1) {
                    z = false;
                }
                end(z);
                throw th;
            }
        }
    }

    public FileChannel position(long newPosition) throws IOException {
        long position0;
        ensureOpen();
        if (newPosition >= 0) {
            synchronized (this.positionLock) {
                int i = -1;
                boolean z = true;
                try {
                    begin();
                    i = this.threads.add();
                    if (!isOpen()) {
                        this.threads.remove(i);
                        if (-1 <= -1) {
                            z = false;
                        }
                        end(z);
                        return null;
                    }
                    BlockGuard.getThreadPolicy().onReadFromDisk();
                    do {
                        position0 = position0(this.f883fd, newPosition);
                        if (position0 != -3 || !isOpen()) {
                            break;
                        }
                        position0 = position0(this.f883fd, newPosition);
                        break;
                        break;
                    } while (!isOpen());
                    break;
                    this.threads.remove(i);
                    if (position0 <= -1) {
                        z = false;
                    }
                    end(z);
                    return this;
                } catch (Throwable th) {
                    this.threads.remove(i);
                    if (-1 <= -1) {
                        z = false;
                    }
                    end(z);
                    throw th;
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public long size() throws IOException {
        ensureOpen();
        synchronized (this.positionLock) {
            long j = -1;
            int i = -1;
            boolean z = true;
            try {
                begin();
                i = this.threads.add();
                if (!isOpen()) {
                    this.threads.remove(i);
                    if (-1 <= -1) {
                        z = false;
                    }
                    end(z);
                    return -1;
                }
                do {
                    j = this.f884nd.size(this.f883fd);
                    if (j != -3 || !isOpen()) {
                        long normalize = IOStatus.normalize(j);
                    }
                    j = this.f884nd.size(this.f883fd);
                    break;
                } while (!isOpen());
                long normalize2 = IOStatus.normalize(j);
                this.threads.remove(i);
                if (j <= -1) {
                    z = false;
                }
                end(z);
                return normalize2;
            } catch (Throwable th) {
                this.threads.remove(i);
                if (j <= -1) {
                    z = false;
                }
                end(z);
                throw th;
            }
        }
    }

    public FileChannel truncate(long newSize) throws IOException {
        long size;
        long position0;
        long j = newSize;
        ensureOpen();
        if (j < 0) {
            throw new IllegalArgumentException("Negative size");
        } else if (this.writable) {
            synchronized (this.positionLock) {
                int i = -1;
                int i2 = -1;
                begin();
                i2 = this.threads.add();
                if (!isOpen()) {
                    this.threads.remove(i2);
                    end(-1 > -1);
                    return null;
                }
                do {
                    try {
                        size = this.f884nd.size(this.f883fd);
                        if (size != -3 || !isOpen()) {
                        }
                        size = this.f884nd.size(this.f883fd);
                        break;
                    } catch (Throwable th) {
                        this.threads.remove(i2);
                        end(i > -1);
                        throw th;
                    }
                } while (!isOpen());
                if (!isOpen()) {
                    this.threads.remove(i2);
                    end(-1 > -1);
                    return null;
                }
                do {
                    position0 = position0(this.f883fd, -1);
                    if (position0 != -3 || !isOpen()) {
                    }
                    position0 = position0(this.f883fd, -1);
                    break;
                } while (!isOpen());
                if (!isOpen()) {
                    this.threads.remove(i2);
                    end(-1 > -1);
                    return null;
                }
                if (j < size) {
                    do {
                        i = this.f884nd.truncate(this.f883fd, j);
                        if (i != -3 || !isOpen()) {
                        }
                        i = this.f884nd.truncate(this.f883fd, j);
                        break;
                    } while (!isOpen());
                    if (!isOpen()) {
                        this.threads.remove(i2);
                        end(i > -1);
                        return null;
                    }
                }
                if (position0 > j) {
                    position0 = newSize;
                }
                do {
                    if (position0(this.f883fd, position0) != -3 || !isOpen()) {
                        break;
                    }
                    break;
                    break;
                } while (!isOpen());
                break;
                this.threads.remove(i2);
                end(i > -1);
                return this;
            }
        } else {
            throw new NonWritableChannelException();
        }
    }

    public void force(boolean metaData) throws IOException {
        ensureOpen();
        int i = -1;
        int i2 = -1;
        boolean z = true;
        try {
            begin();
            i2 = this.threads.add();
            if (isOpen()) {
                do {
                    i = this.f884nd.force(this.f883fd, metaData);
                    if (i != -3 || !isOpen()) {
                        this.threads.remove(i2);
                    }
                    i = this.f884nd.force(this.f883fd, metaData);
                    break;
                } while (!isOpen());
                this.threads.remove(i2);
                if (i <= -1) {
                    z = false;
                }
                end(z);
            }
        } finally {
            this.threads.remove(i2);
            if (i <= -1) {
                z = false;
            }
            end(z);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:29:0x006b, code lost:
        if (r15 > -1) goto L_0x006f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x0083, code lost:
        if (r15 > -1) goto L_0x006f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0091, code lost:
        if (r15 > -1) goto L_0x006f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00a6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long transferToDirectlyInternal(long r18, int r20, java.nio.channels.WritableByteChannel r21, java.p026io.FileDescriptor r22) throws java.p026io.IOException {
        /*
            r17 = this;
            r8 = r17
            r9 = r21
            r1 = -1
            r3 = -1
            r10 = 1
            r11 = -1
            r13 = 0
            r17.begin()     // Catch:{ all -> 0x009b }
            sun.nio.ch.NativeThreadSet r0 = r8.threads     // Catch:{ all -> 0x009b }
            int r0 = r0.add()     // Catch:{ all -> 0x009b }
            r14 = r0
            boolean r0 = r17.isOpen()     // Catch:{ all -> 0x0098 }
            if (r0 != 0) goto L_0x002b
            sun.nio.ch.NativeThreadSet r0 = r8.threads
            r0.remove(r14)
            int r0 = (r1 > r11 ? 1 : (r1 == r11 ? 0 : -1))
            if (r0 <= 0) goto L_0x0026
            goto L_0x0027
        L_0x0026:
            r10 = r13
        L_0x0027:
            r8.end(r10)
            return r11
        L_0x002b:
            dalvik.system.BlockGuard$Policy r0 = dalvik.system.BlockGuard.getThreadPolicy()     // Catch:{ all -> 0x0098 }
            r0.onWriteToDisk()     // Catch:{ all -> 0x0098 }
            r15 = r1
        L_0x0033:
            java.io.FileDescriptor r2 = r8.f883fd     // Catch:{ all -> 0x0094 }
            r7 = r20
            long r5 = (long) r7     // Catch:{ all -> 0x0094 }
            r1 = r17
            r3 = r18
            r7 = r22
            long r0 = r1.transferTo0(r2, r3, r5, r7)     // Catch:{ all -> 0x0094 }
            r15 = r0
            r0 = -3
            int r0 = (r15 > r0 ? 1 : (r15 == r0 ? 0 : -1))
            if (r0 != 0) goto L_0x004f
            boolean r0 = r17.isOpen()     // Catch:{ all -> 0x0094 }
            if (r0 != 0) goto L_0x0033
        L_0x004f:
            r0 = -6
            int r2 = (r15 > r0 ? 1 : (r15 == r0 ? 0 : -1))
            if (r2 != 0) goto L_0x0073
            boolean r2 = r9 instanceof sun.nio.p033ch.SinkChannelImpl     // Catch:{ all -> 0x0094 }
            if (r2 == 0) goto L_0x005b
            pipeSupported = r13     // Catch:{ all -> 0x0094 }
        L_0x005b:
            boolean r2 = r9 instanceof sun.nio.p033ch.FileChannelImpl     // Catch:{ all -> 0x0094 }
            if (r2 == 0) goto L_0x0062
            fileSupported = r13     // Catch:{ all -> 0x0094 }
        L_0x0062:
            sun.nio.ch.NativeThreadSet r2 = r8.threads
            r2.remove(r14)
            int r2 = (r15 > r11 ? 1 : (r15 == r11 ? 0 : -1))
            if (r2 <= 0) goto L_0x006e
            goto L_0x006f
        L_0x006e:
            r10 = r13
        L_0x006f:
            r8.end(r10)
            return r0
        L_0x0073:
            r0 = -4
            int r2 = (r15 > r0 ? 1 : (r15 == r0 ? 0 : -1))
            if (r2 != 0) goto L_0x0086
            transferSupported = r13     // Catch:{ all -> 0x0094 }
            sun.nio.ch.NativeThreadSet r2 = r8.threads
            r2.remove(r14)
            int r2 = (r15 > r11 ? 1 : (r15 == r11 ? 0 : -1))
            if (r2 <= 0) goto L_0x006e
            goto L_0x006f
        L_0x0086:
            long r0 = sun.nio.p033ch.IOStatus.normalize((long) r15)     // Catch:{ all -> 0x0094 }
            sun.nio.ch.NativeThreadSet r2 = r8.threads
            r2.remove(r14)
            int r2 = (r15 > r11 ? 1 : (r15 == r11 ? 0 : -1))
            if (r2 <= 0) goto L_0x006e
            goto L_0x006f
        L_0x0094:
            r0 = move-exception
            r3 = r14
            r1 = r15
            goto L_0x009c
        L_0x0098:
            r0 = move-exception
            r3 = r14
            goto L_0x009c
        L_0x009b:
            r0 = move-exception
        L_0x009c:
            sun.nio.ch.NativeThreadSet r4 = r8.threads
            r4.remove(r3)
            int r4 = (r1 > r11 ? 1 : (r1 == r11 ? 0 : -1))
            if (r4 <= 0) goto L_0x00a6
            goto L_0x00a7
        L_0x00a6:
            r10 = r13
        L_0x00a7:
            r8.end(r10)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.FileChannelImpl.transferToDirectlyInternal(long, int, java.nio.channels.WritableByteChannel, java.io.FileDescriptor):long");
    }

    private long transferToDirectly(long position, int icount, WritableByteChannel target) throws IOException {
        long transferToDirectlyInternal;
        WritableByteChannel writableByteChannel = target;
        if (!transferSupported) {
            return -4;
        }
        FileDescriptor fileDescriptor = null;
        if (writableByteChannel instanceof FileChannelImpl) {
            if (!fileSupported) {
                return -6;
            }
            fileDescriptor = ((FileChannelImpl) writableByteChannel).f883fd;
        } else if (writableByteChannel instanceof SelChImpl) {
            if ((writableByteChannel instanceof SinkChannelImpl) && !pipeSupported) {
                return -6;
            }
            if (!this.f884nd.canTransferToDirectly((SelectableChannel) writableByteChannel)) {
                return -6;
            }
            fileDescriptor = ((SelChImpl) writableByteChannel).getFD();
        }
        FileDescriptor fileDescriptor2 = fileDescriptor;
        if (fileDescriptor2 == null || IOUtil.fdVal(this.f883fd) == IOUtil.fdVal(fileDescriptor2)) {
            return -4;
        }
        if (!this.f884nd.transferToDirectlyNeedsPositionLock()) {
            return transferToDirectlyInternal(position, icount, target, fileDescriptor2);
        }
        synchronized (this.positionLock) {
            long position2 = position();
            try {
                transferToDirectlyInternal = transferToDirectlyInternal(position, icount, target, fileDescriptor2);
                position(position2);
            } catch (Throwable th) {
                Throwable th2 = th;
                position(position2);
                throw th2;
            }
        }
        return transferToDirectlyInternal;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        unmap(r5);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long transferToTrustedChannel(long r16, long r18, java.nio.channels.WritableByteChannel r20) throws java.p026io.IOException {
        /*
            r15 = this;
            r1 = r20
            boolean r2 = r1 instanceof sun.nio.p033ch.SelChImpl
            boolean r0 = r1 instanceof sun.nio.p033ch.FileChannelImpl
            if (r0 != 0) goto L_0x000d
            if (r2 != 0) goto L_0x000d
            r3 = -4
            return r3
        L_0x000d:
            r3 = r18
            r11 = r3
            r3 = r16
        L_0x0012:
            r5 = 0
            int r0 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1))
            if (r0 <= 0) goto L_0x005a
            r5 = 8388608(0x800000, double:4.144523E-317)
            long r13 = java.lang.Math.min((long) r11, (long) r5)
            java.nio.channels.FileChannel$MapMode r6 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch:{ ClosedByInterruptException -> 0x004c, IOException -> 0x0045 }
            r5 = r15
            r7 = r3
            r9 = r13
            java.nio.MappedByteBuffer r0 = r5.map(r6, r7, r9)     // Catch:{ ClosedByInterruptException -> 0x004c, IOException -> 0x0045 }
            r5 = r0
            int r0 = r1.write(r5)     // Catch:{ all -> 0x003f }
            long r6 = (long) r0
            long r11 = r11 - r6
            if (r2 == 0) goto L_0x0036
            unmap(r5)     // Catch:{ ClosedByInterruptException -> 0x004c, IOException -> 0x0045 }
            goto L_0x005a
        L_0x0036:
            long r6 = (long) r0     // Catch:{ ClosedByInterruptException -> 0x004c, IOException -> 0x0045 }
            long r3 = r3 + r6
            unmap(r5)     // Catch:{ ClosedByInterruptException -> 0x004c, IOException -> 0x0045 }
            goto L_0x0012
        L_0x003f:
            r0 = move-exception
            r6 = r0
            unmap(r5)     // Catch:{ ClosedByInterruptException -> 0x004c, IOException -> 0x0045 }
            throw r6     // Catch:{ ClosedByInterruptException -> 0x004c, IOException -> 0x0045 }
        L_0x0045:
            r0 = move-exception
            int r5 = (r11 > r18 ? 1 : (r11 == r18 ? 0 : -1))
            if (r5 == 0) goto L_0x004b
            goto L_0x005a
        L_0x004b:
            throw r0
        L_0x004c:
            r0 = move-exception
            r5 = r0
            r15.close()     // Catch:{ all -> 0x0053 }
            goto L_0x0059
        L_0x0053:
            r0 = move-exception
            r6 = r0
            r0 = r6
            r5.addSuppressed(r0)
        L_0x0059:
            throw r5
        L_0x005a:
            long r5 = r18 - r11
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.FileChannelImpl.transferToTrustedChannel(long, long, java.nio.channels.WritableByteChannel):long");
    }

    private long transferToArbitraryChannel(long position, int icount, WritableByteChannel target) throws IOException {
        ByteBuffer temporaryDirectBuffer = Util.getTemporaryDirectBuffer(Math.min(icount, 8192));
        long j = 0;
        long j2 = position;
        try {
            Util.erase(temporaryDirectBuffer);
            while (true) {
                if (j >= ((long) icount)) {
                    break;
                }
                temporaryDirectBuffer.limit(Math.min((int) (((long) icount) - j), 8192));
                int read = read(temporaryDirectBuffer, j2);
                if (read <= 0) {
                    break;
                }
                temporaryDirectBuffer.flip();
                int write = target.write(temporaryDirectBuffer);
                j += (long) write;
                if (write != read) {
                    break;
                }
                j2 += (long) write;
                temporaryDirectBuffer.clear();
            }
        } catch (IOException e) {
            if (0 <= 0) {
                throw e;
            }
        } catch (Throwable th) {
            Util.releaseTemporaryDirectBuffer(temporaryDirectBuffer);
            throw th;
        }
        Util.releaseTemporaryDirectBuffer(temporaryDirectBuffer);
        return j;
    }

    public long transferTo(long position, long count, WritableByteChannel target) throws IOException {
        long j = position;
        long j2 = count;
        WritableByteChannel writableByteChannel = target;
        ensureOpen();
        if (!target.isOpen()) {
            throw new ClosedChannelException();
        } else if (!this.readable) {
            throw new NonReadableChannelException();
        } else if ((writableByteChannel instanceof FileChannelImpl) && !((FileChannelImpl) writableByteChannel).writable) {
            throw new NonWritableChannelException();
        } else if (j < 0 || j2 < 0) {
            throw new IllegalArgumentException();
        } else {
            long size = size();
            if (j > size) {
                return 0;
            }
            int min = (int) Math.min(j2, 2147483647L);
            if (size - j < ((long) min)) {
                min = (int) (size - j);
            }
            int i = min;
            long transferToDirectly = transferToDirectly(j, i, writableByteChannel);
            long j3 = transferToDirectly;
            if (transferToDirectly >= 0) {
                return j3;
            }
            int i2 = i;
            long transferToTrustedChannel = transferToTrustedChannel(position, (long) i, target);
            long j4 = transferToTrustedChannel;
            if (transferToTrustedChannel >= 0) {
                return j4;
            }
            return transferToArbitraryChannel(j, i2, writableByteChannel);
        }
    }

    private long transferFromFileChannel(FileChannelImpl src, long position, long count) throws IOException {
        long j;
        MappedByteBuffer map;
        FileChannelImpl fileChannelImpl = src;
        if (fileChannelImpl.readable) {
            synchronized (fileChannelImpl.positionLock) {
                try {
                    long position2 = src.position();
                    long min = Math.min(count, src.size() - position2);
                    long j2 = min;
                    long j3 = position2;
                    long j4 = position;
                    long j5 = min;
                    while (j5 > 0) {
                        try {
                            j = j5;
                            long j6 = j4;
                            try {
                                map = src.map(FileChannel.MapMode.READ_ONLY, j3, Math.min(j5, 8388608));
                            } catch (Throwable th) {
                                th = th;
                                throw th;
                            }
                            try {
                                long write = (long) write(map, j6);
                                j3 += write;
                                long j7 = j6 + write;
                                long j8 = j - write;
                                unmap(map);
                                j4 = j7;
                                j5 = j8;
                                long j9 = count;
                            } catch (IOException e) {
                                IOException iOException = e;
                                if (j != j2) {
                                    unmap(map);
                                } else {
                                    throw iOException;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            long j10 = j4;
                            throw th;
                        }
                    }
                    j = j5;
                    long j11 = j4;
                    long j12 = j2 - j;
                    fileChannelImpl.position(position2 + j12);
                    return j12;
                } catch (Throwable th4) {
                    th = th4;
                    long j13 = position;
                    throw th;
                }
            }
        } else {
            throw new NonReadableChannelException();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0068 A[SYNTHETIC, Splitter:B:32:0x0068] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long transferFromArbitraryChannel(java.nio.channels.ReadableByteChannel r17, long r18, long r20) throws java.p026io.IOException {
        /*
            r16 = this;
            r1 = r20
            r3 = 8192(0x2000, double:4.0474E-320)
            long r5 = java.lang.Math.min((long) r1, (long) r3)
            int r5 = (int) r5
            java.nio.ByteBuffer r6 = sun.nio.p033ch.Util.getTemporaryDirectBuffer(r5)
            r7 = 0
            r9 = r18
            sun.nio.p033ch.Util.erase(r6)     // Catch:{ IOException -> 0x005c, all -> 0x0056 }
        L_0x0014:
            int r0 = (r7 > r1 ? 1 : (r7 == r1 ? 0 : -1))
            if (r0 >= 0) goto L_0x004c
            long r11 = r1 - r7
            long r11 = java.lang.Math.min((long) r11, (long) r3)     // Catch:{ IOException -> 0x005c, all -> 0x0056 }
            int r0 = (int) r11     // Catch:{ IOException -> 0x005c, all -> 0x0056 }
            r6.limit((int) r0)     // Catch:{ IOException -> 0x005c, all -> 0x0056 }
            r11 = r17
            int r0 = r11.read(r6)     // Catch:{ IOException -> 0x0048, all -> 0x0044 }
            if (r0 > 0) goto L_0x002e
            r12 = r16
            goto L_0x0051
        L_0x002e:
            r6.flip()     // Catch:{ IOException -> 0x0048, all -> 0x0044 }
            r12 = r16
            int r13 = r12.write(r6, r9)     // Catch:{ IOException -> 0x0042 }
            long r14 = (long) r13     // Catch:{ IOException -> 0x0042 }
            long r7 = r7 + r14
            if (r13 == r0) goto L_0x003c
        L_0x003b:
            goto L_0x0051
        L_0x003c:
            long r14 = (long) r13     // Catch:{ IOException -> 0x0042 }
            long r9 = r9 + r14
            r6.clear()     // Catch:{ IOException -> 0x0042 }
            goto L_0x0014
        L_0x0042:
            r0 = move-exception
            goto L_0x0061
        L_0x0044:
            r0 = move-exception
            r12 = r16
            goto L_0x006a
        L_0x0048:
            r0 = move-exception
            r12 = r16
            goto L_0x0061
        L_0x004c:
            r12 = r16
            r11 = r17
            goto L_0x003b
        L_0x0051:
            sun.nio.p033ch.Util.releaseTemporaryDirectBuffer(r6)
            return r7
        L_0x0056:
            r0 = move-exception
            r12 = r16
            r11 = r17
            goto L_0x006a
        L_0x005c:
            r0 = move-exception
            r12 = r16
            r11 = r17
        L_0x0061:
            r3 = 0
            int r3 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1))
            if (r3 <= 0) goto L_0x0068
            goto L_0x0051
        L_0x0068:
            throw r0     // Catch:{ all -> 0x0069 }
        L_0x0069:
            r0 = move-exception
        L_0x006a:
            sun.nio.p033ch.Util.releaseTemporaryDirectBuffer(r6)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.FileChannelImpl.transferFromArbitraryChannel(java.nio.channels.ReadableByteChannel, long, long):long");
    }

    public long transferFrom(ReadableByteChannel src, long position, long count) throws IOException {
        ensureOpen();
        if (!src.isOpen()) {
            throw new ClosedChannelException();
        } else if (!this.writable) {
            throw new NonWritableChannelException();
        } else if (position < 0 || count < 0) {
            throw new IllegalArgumentException();
        } else if (position > size()) {
            return 0;
        } else {
            if (!(src instanceof FileChannelImpl)) {
                return transferFromArbitraryChannel(src, position, count);
            }
            return transferFromFileChannel((FileChannelImpl) src, position, count);
        }
    }

    public int read(ByteBuffer dst, long position) throws IOException {
        int readInternal;
        if (dst == null) {
            throw new NullPointerException();
        } else if (position < 0) {
            throw new IllegalArgumentException("Negative position");
        } else if (this.readable) {
            ensureOpen();
            if (!this.f884nd.needsPositionLock()) {
                return readInternal(dst, position);
            }
            synchronized (this.positionLock) {
                readInternal = readInternal(dst, position);
            }
            return readInternal;
        } else {
            throw new NonReadableChannelException();
        }
    }

    /* JADX INFO: finally extract failed */
    private int readInternal(ByteBuffer dst, long position) throws IOException {
        int i = 0;
        int i2 = -1;
        boolean z = true;
        try {
            begin();
            i2 = this.threads.add();
            if (!isOpen()) {
                this.threads.remove(i2);
                if (0 <= 0) {
                    z = false;
                }
                end(z);
                return -1;
            }
            do {
                i = IOUtil.read(this.f883fd, dst, position, this.f884nd);
                if (i != -3 || !isOpen()) {
                    int normalize = IOStatus.normalize(i);
                }
                i = IOUtil.read(this.f883fd, dst, position, this.f884nd);
                break;
            } while (!isOpen());
            int normalize2 = IOStatus.normalize(i);
            this.threads.remove(i2);
            if (i <= 0) {
                z = false;
            }
            end(z);
            return normalize2;
        } catch (Throwable th) {
            this.threads.remove(i2);
            if (i <= 0) {
                z = false;
            }
            end(z);
            throw th;
        }
    }

    public int write(ByteBuffer src, long position) throws IOException {
        int writeInternal;
        if (src == null) {
            throw new NullPointerException();
        } else if (position < 0) {
            throw new IllegalArgumentException("Negative position");
        } else if (this.writable) {
            ensureOpen();
            if (!this.f884nd.needsPositionLock()) {
                return writeInternal(src, position);
            }
            synchronized (this.positionLock) {
                writeInternal = writeInternal(src, position);
            }
            return writeInternal;
        } else {
            throw new NonWritableChannelException();
        }
    }

    /* JADX INFO: finally extract failed */
    private int writeInternal(ByteBuffer src, long position) throws IOException {
        int i = 0;
        int i2 = -1;
        boolean z = true;
        try {
            begin();
            i2 = this.threads.add();
            if (!isOpen()) {
                this.threads.remove(i2);
                if (0 <= 0) {
                    z = false;
                }
                end(z);
                return -1;
            }
            do {
                i = IOUtil.write(this.f883fd, src, position, this.f884nd);
                if (i != -3 || !isOpen()) {
                    int normalize = IOStatus.normalize(i);
                }
                i = IOUtil.write(this.f883fd, src, position, this.f884nd);
                break;
            } while (!isOpen());
            int normalize2 = IOStatus.normalize(i);
            this.threads.remove(i2);
            if (i <= 0) {
                z = false;
            }
            end(z);
            return normalize2;
        } catch (Throwable th) {
            this.threads.remove(i2);
            if (i <= 0) {
                z = false;
            }
            end(z);
            throw th;
        }
    }

    /* renamed from: sun.nio.ch.FileChannelImpl$Unmapper */
    private static class Unmapper implements Runnable {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        static volatile int count;

        /* renamed from: nd */
        private static final NativeDispatcher f885nd = new FileDispatcherImpl();
        static volatile long totalCapacity;
        static volatile long totalSize;
        private volatile long address;
        private final int cap;

        /* renamed from: fd */
        private final FileDescriptor f886fd;
        private final long size;

        static {
            Class<FileChannelImpl> cls = FileChannelImpl.class;
        }

        private Unmapper(long j, long j2, int i, FileDescriptor fileDescriptor) {
            this.address = j;
            this.size = j2;
            this.cap = i;
            this.f886fd = fileDescriptor;
            synchronized (Unmapper.class) {
                count++;
                totalSize += j2;
                totalCapacity += (long) i;
            }
        }

        public void run() {
            if (this.address != 0) {
                int unused = FileChannelImpl.unmap0(this.address, this.size);
                this.address = 0;
                if (this.f886fd.valid()) {
                    try {
                        f885nd.close(this.f886fd);
                    } catch (IOException unused2) {
                    }
                }
                synchronized (Unmapper.class) {
                    count--;
                    totalSize -= this.size;
                    totalCapacity -= (long) this.cap;
                }
            }
        }
    }

    private static void unmap(MappedByteBuffer bb) {
        Cleaner cleaner = ((DirectBuffer) bb).cleaner();
        if (cleaner != null) {
            cleaner.clean();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x0093 A[Catch:{ OutOfMemoryError -> 0x017d, IOException -> 0x0176, InterruptedException -> 0x011e, all -> 0x018a }, LOOP:1: B:50:0x0093->B:53:0x00a5, LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.nio.MappedByteBuffer map(java.nio.channels.FileChannel.MapMode r32, long r33, long r35) throws java.p026io.IOException {
        /*
            r31 = this;
            r7 = r31
            r8 = r32
            r9 = r35
            r31.ensureOpen()
            if (r8 == 0) goto L_0x01c1
            r0 = 0
            int r2 = (r33 > r0 ? 1 : (r33 == r0 ? 0 : -1))
            if (r2 < 0) goto L_0x01b9
            int r2 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r2 < 0) goto L_0x01b1
            long r2 = r33 + r9
            int r2 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
            if (r2 < 0) goto L_0x01a9
            r2 = 2147483647(0x7fffffff, double:1.060997895E-314)
            int r2 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1))
            if (r2 > 0) goto L_0x01a1
            r2 = -1
            java.nio.channels.FileChannel$MapMode r3 = java.nio.channels.FileChannel.MapMode.READ_ONLY
            if (r8 != r3) goto L_0x002a
            r2 = 0
        L_0x0028:
            r11 = r2
            goto L_0x0036
        L_0x002a:
            java.nio.channels.FileChannel$MapMode r3 = java.nio.channels.FileChannel.MapMode.READ_WRITE
            if (r8 != r3) goto L_0x0030
            r2 = 1
            goto L_0x0028
        L_0x0030:
            java.nio.channels.FileChannel$MapMode r3 = java.nio.channels.FileChannel.MapMode.PRIVATE
            if (r8 != r3) goto L_0x0028
            r2 = 2
            goto L_0x0028
        L_0x0036:
            java.nio.channels.FileChannel$MapMode r2 = java.nio.channels.FileChannel.MapMode.READ_ONLY
            if (r8 == r2) goto L_0x0046
            boolean r2 = r7.writable
            if (r2 == 0) goto L_0x0040
            goto L_0x0046
        L_0x0040:
            java.nio.channels.NonWritableChannelException r0 = new java.nio.channels.NonWritableChannelException
            r0.<init>()
            throw r0
        L_0x0046:
            boolean r2 = r7.readable
            if (r2 == 0) goto L_0x019b
            r12 = -1
            r2 = -1
            r31.begin()     // Catch:{ all -> 0x018d }
            sun.nio.ch.NativeThreadSet r3 = r7.threads     // Catch:{ all -> 0x018d }
            int r3 = r3.add()     // Catch:{ all -> 0x018d }
            r14 = r3
            boolean r2 = r31.isOpen()     // Catch:{ all -> 0x018a }
            r3 = 0
            if (r2 != 0) goto L_0x006c
        L_0x005e:
            sun.nio.ch.NativeThreadSet r0 = r7.threads
            r0.remove(r14)
            boolean r0 = sun.nio.p033ch.IOStatus.checkAll(r12)
            r7.end(r0)
            return r3
        L_0x006c:
            sun.nio.ch.FileDispatcher r2 = r7.f884nd     // Catch:{ all -> 0x018a }
            java.io.FileDescriptor r4 = r7.f883fd     // Catch:{ all -> 0x018a }
            long r4 = r2.size(r4)     // Catch:{ all -> 0x018a }
            r15 = r4
            r4 = -3
            int r2 = (r15 > r4 ? 1 : (r15 == r4 ? 0 : -1))
            if (r2 != 0) goto L_0x0082
            boolean r2 = r31.isOpen()     // Catch:{ all -> 0x018a }
            if (r2 != 0) goto L_0x006c
        L_0x0082:
            boolean r2 = r31.isOpen()     // Catch:{ all -> 0x018a }
            if (r2 != 0) goto L_0x0089
            goto L_0x005e
        L_0x0089:
            long r4 = r33 + r9
            int r2 = (r15 > r4 ? 1 : (r15 == r4 ? 0 : -1))
            if (r2 >= 0) goto L_0x00b6
            boolean r2 = r7.writable     // Catch:{ all -> 0x018a }
            if (r2 == 0) goto L_0x00ae
        L_0x0093:
            sun.nio.ch.FileDispatcher r2 = r7.f884nd     // Catch:{ all -> 0x018a }
            java.io.FileDescriptor r4 = r7.f883fd     // Catch:{ all -> 0x018a }
            long r5 = r33 + r9
            int r2 = r2.truncate(r4, r5)     // Catch:{ all -> 0x018a }
            r4 = -3
            if (r2 != r4) goto L_0x00a7
            boolean r4 = r31.isOpen()     // Catch:{ all -> 0x018a }
            if (r4 != 0) goto L_0x0093
        L_0x00a7:
            boolean r4 = r31.isOpen()     // Catch:{ all -> 0x018a }
            if (r4 != 0) goto L_0x00b6
            goto L_0x005e
        L_0x00ae:
            java.io.IOException r0 = new java.io.IOException     // Catch:{ all -> 0x018a }
            java.lang.String r1 = "Channel not open for writing - cannot extend file to required size"
            r0.<init>((java.lang.String) r1)     // Catch:{ all -> 0x018a }
            throw r0     // Catch:{ all -> 0x018a }
        L_0x00b6:
            int r0 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            r17 = 0
            r18 = 1
            if (r0 != 0) goto L_0x00ea
            r12 = 0
            java.io.FileDescriptor r4 = new java.io.FileDescriptor     // Catch:{ all -> 0x018a }
            r4.<init>()     // Catch:{ all -> 0x018a }
            java.nio.DirectByteBuffer r19 = new java.nio.DirectByteBuffer     // Catch:{ all -> 0x018a }
            r1 = 0
            r2 = 0
            r5 = 0
            boolean r0 = r7.writable     // Catch:{ all -> 0x018a }
            if (r0 == 0) goto L_0x00d6
            if (r11 != 0) goto L_0x00d3
            goto L_0x00d6
        L_0x00d3:
            r6 = r17
            goto L_0x00d8
        L_0x00d6:
            r6 = r18
        L_0x00d8:
            r0 = r19
            r0.<init>(r1, r2, r4, r5, r6)     // Catch:{ all -> 0x018a }
            sun.nio.ch.NativeThreadSet r0 = r7.threads
            r0.remove(r14)
            boolean r0 = sun.nio.p033ch.IOStatus.checkAll(r12)
            r7.end(r0)
            return r19
        L_0x00ea:
            long r0 = allocationGranularity     // Catch:{ all -> 0x018a }
            long r0 = r33 % r0
            int r5 = (int) r0
            long r0 = (long) r5
            long r19 = r33 - r0
            long r0 = (long) r5
            long r3 = r9 + r0
            dalvik.system.BlockGuard$Policy r0 = dalvik.system.BlockGuard.getThreadPolicy()     // Catch:{ OutOfMemoryError -> 0x010f }
            r0.onReadFromDisk()     // Catch:{ OutOfMemoryError -> 0x010f }
            r1 = r31
            r2 = r11
            r29 = r3
            r3 = r19
            r8 = r5
            r5 = r29
            long r0 = r1.map0(r2, r3, r5)     // Catch:{ OutOfMemoryError -> 0x010d }
        L_0x010a:
            r12 = r0
            goto L_0x0135
        L_0x010d:
            r0 = move-exception
            goto L_0x0113
        L_0x010f:
            r0 = move-exception
            r29 = r3
            r8 = r5
        L_0x0113:
            r21 = r0
            java.lang.System.m1693gc()     // Catch:{ all -> 0x018a }
            r0 = 100
            java.lang.Thread.sleep(r0)     // Catch:{ InterruptedException -> 0x011e }
        L_0x011d:
            goto L_0x0129
        L_0x011e:
            r0 = move-exception
            r1 = r0
            r0 = r1
            java.lang.Thread r1 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x018a }
            r1.interrupt()     // Catch:{ all -> 0x018a }
            goto L_0x011d
        L_0x0129:
            r1 = r31
            r2 = r11
            r3 = r19
            r5 = r29
            long r0 = r1.map0(r2, r3, r5)     // Catch:{ OutOfMemoryError -> 0x017d }
            goto L_0x010a
        L_0x0135:
            sun.nio.ch.FileDispatcher r0 = r7.f884nd     // Catch:{ IOException -> 0x0176 }
            java.io.FileDescriptor r1 = r7.f883fd     // Catch:{ IOException -> 0x0176 }
            java.io.FileDescriptor r27 = r0.duplicateForMapping(r1)     // Catch:{ IOException -> 0x0176 }
            int r6 = (int) r9
            sun.nio.ch.FileChannelImpl$Unmapper r5 = new sun.nio.ch.FileChannelImpl$Unmapper     // Catch:{ all -> 0x018a }
            r28 = 0
            r21 = r5
            r22 = r12
            r24 = r29
            r26 = r6
            r21.<init>(r22, r24, r26, r27)     // Catch:{ all -> 0x018a }
            java.nio.DirectByteBuffer r21 = new java.nio.DirectByteBuffer     // Catch:{ all -> 0x018a }
            long r0 = (long) r8     // Catch:{ all -> 0x018a }
            long r2 = r12 + r0
            boolean r0 = r7.writable     // Catch:{ all -> 0x018a }
            if (r0 == 0) goto L_0x015b
            if (r11 != 0) goto L_0x015d
        L_0x015b:
            r17 = r18
        L_0x015d:
            r0 = r21
            r1 = r6
            r4 = r27
            r18 = r6
            r6 = r17
            r0.<init>(r1, r2, r4, r5, r6)     // Catch:{ all -> 0x018a }
            sun.nio.ch.NativeThreadSet r0 = r7.threads
            r0.remove(r14)
            boolean r0 = sun.nio.p033ch.IOStatus.checkAll(r12)
            r7.end(r0)
            return r21
        L_0x0176:
            r0 = move-exception
            r1 = r29
            unmap0(r12, r1)     // Catch:{ all -> 0x018a }
            throw r0     // Catch:{ all -> 0x018a }
        L_0x017d:
            r0 = move-exception
            r1 = r29
            r3 = r0
            r0 = r3
            java.io.IOException r3 = new java.io.IOException     // Catch:{ all -> 0x018a }
            java.lang.String r4 = "Map failed"
            r3.<init>(r4, r0)     // Catch:{ all -> 0x018a }
            throw r3     // Catch:{ all -> 0x018a }
        L_0x018a:
            r0 = move-exception
            r2 = r14
            goto L_0x018e
        L_0x018d:
            r0 = move-exception
        L_0x018e:
            sun.nio.ch.NativeThreadSet r1 = r7.threads
            r1.remove(r2)
            boolean r1 = sun.nio.p033ch.IOStatus.checkAll(r12)
            r7.end(r1)
            throw r0
        L_0x019b:
            java.nio.channels.NonReadableChannelException r0 = new java.nio.channels.NonReadableChannelException
            r0.<init>()
            throw r0
        L_0x01a1:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Size exceeds Integer.MAX_VALUE"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x01a9:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Position + size overflow"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x01b1:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Negative size"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x01b9:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Negative position"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x01c1:
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "Mode is null"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.FileChannelImpl.map(java.nio.channels.FileChannel$MapMode, long, long):java.nio.MappedByteBuffer");
    }

    private static boolean isSharedFileLockTable() {
        boolean z;
        if (!propertyChecked) {
            synchronized (FileChannelImpl.class) {
                if (!propertyChecked) {
                    String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.nio.ch.disableSystemWideOverlappingFileLockCheck"));
                    if (str != null) {
                        if (!str.equals("false")) {
                            z = false;
                            isSharedFileLockTable = z;
                            propertyChecked = true;
                        }
                    }
                    z = true;
                    isSharedFileLockTable = z;
                    propertyChecked = true;
                }
            }
        }
        return isSharedFileLockTable;
    }

    /* JADX INFO: finally extract failed */
    private FileLockTable fileLockTable() throws IOException {
        if (this.fileLockTable == null) {
            synchronized (this) {
                if (this.fileLockTable == null) {
                    if (isSharedFileLockTable()) {
                        int add = this.threads.add();
                        try {
                            ensureOpen();
                            this.fileLockTable = FileLockTable.newSharedFileLockTable(this, this.f883fd);
                            this.threads.remove(add);
                        } catch (Throwable th) {
                            this.threads.remove(add);
                            throw th;
                        }
                    } else {
                        this.fileLockTable = new SimpleFileLockTable();
                    }
                }
            }
        }
        return this.fileLockTable;
    }

    /* JADX WARNING: Removed duplicated region for block: B:65:0x00d3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.nio.channels.FileLock lock(long r18, long r20, boolean r22) throws java.p026io.IOException {
        /*
            r17 = this;
            r8 = r17
            r17.ensureOpen()
            if (r22 == 0) goto L_0x0012
            boolean r0 = r8.readable
            if (r0 == 0) goto L_0x000c
            goto L_0x0012
        L_0x000c:
            java.nio.channels.NonReadableChannelException r0 = new java.nio.channels.NonReadableChannelException
            r0.<init>()
            throw r0
        L_0x0012:
            if (r22 != 0) goto L_0x001f
            boolean r0 = r8.writable
            if (r0 == 0) goto L_0x0019
            goto L_0x001f
        L_0x0019:
            java.nio.channels.NonWritableChannelException r0 = new java.nio.channels.NonWritableChannelException
            r0.<init>()
            throw r0
        L_0x001f:
            sun.nio.ch.FileLockImpl r0 = new sun.nio.ch.FileLockImpl
            r1 = r0
            r2 = r17
            r3 = r18
            r5 = r20
            r7 = r22
            r1.<init>((java.nio.channels.FileChannel) r2, (long) r3, (long) r5, (boolean) r7)
            r7 = r0
            sun.nio.ch.FileLockTable r5 = r17.fileLockTable()
            r5.add(r7)
            r6 = 0
            r1 = -1
            r17.begin()     // Catch:{ all -> 0x00cd }
            sun.nio.ch.NativeThreadSet r0 = r8.threads     // Catch:{ all -> 0x00cd }
            int r0 = r0.add()     // Catch:{ all -> 0x00cd }
            r3 = r0
            boolean r0 = r17.isOpen()     // Catch:{ all -> 0x00c6 }
            if (r0 != 0) goto L_0x0061
            if (r6 != 0) goto L_0x004d
            r5.remove(r7)
        L_0x004d:
            sun.nio.ch.NativeThreadSet r0 = r8.threads
            r0.remove(r3)
            r8.end(r6)     // Catch:{ ClosedByInterruptException -> 0x0058 }
            r0 = 0
            return r0
        L_0x0058:
            r0 = move-exception
            r1 = r0
            r0 = r1
            java.nio.channels.FileLockInterruptionException r1 = new java.nio.channels.FileLockInterruptionException
            r1.<init>()
            throw r1
        L_0x0061:
            sun.nio.ch.FileDispatcher r9 = r8.f884nd     // Catch:{ all -> 0x00c6 }
            java.io.FileDescriptor r10 = r8.f883fd     // Catch:{ all -> 0x00c6 }
            r11 = 1
            r12 = r18
            r14 = r20
            r16 = r22
            int r0 = r9.lock(r10, r11, r12, r14, r16)     // Catch:{ all -> 0x00c6 }
            r1 = 2
            if (r0 != r1) goto L_0x007f
            boolean r1 = r17.isOpen()     // Catch:{ all -> 0x007b }
            if (r1 != 0) goto L_0x0061
            goto L_0x007f
        L_0x007b:
            r0 = move-exception
            r1 = r3
            goto L_0x00ce
        L_0x007f:
            boolean r1 = r17.isOpen()     // Catch:{ all -> 0x00c6 }
            if (r1 == 0) goto L_0x00aa
            r1 = 1
            if (r0 != r1) goto L_0x00a4
            sun.nio.ch.FileLockImpl r9 = new sun.nio.ch.FileLockImpl     // Catch:{ all -> 0x00c6 }
            r10 = 0
            r1 = r9
            r2 = r17
            r11 = r3
            r3 = r18
            r13 = r5
            r12 = r6
            r5 = r20
            r14 = r7
            r7 = r10
            r1.<init>((java.nio.channels.FileChannel) r2, (long) r3, (long) r5, (boolean) r7)     // Catch:{ all -> 0x00a2 }
            r1 = r9
            r13.replace(r14, r1)     // Catch:{ all -> 0x00a2 }
            r2 = r1
            r7 = r2
            goto L_0x00a8
        L_0x00a2:
            r0 = move-exception
            goto L_0x00cb
        L_0x00a4:
            r11 = r3
            r13 = r5
            r12 = r6
            r14 = r7
        L_0x00a8:
            r6 = 1
            goto L_0x00ae
        L_0x00aa:
            r11 = r3
            r13 = r5
            r12 = r6
            r14 = r7
        L_0x00ae:
            if (r6 != 0) goto L_0x00b3
            r13.remove(r7)
        L_0x00b3:
            sun.nio.ch.NativeThreadSet r0 = r8.threads
            r0.remove(r11)
            r8.end(r6)     // Catch:{ ClosedByInterruptException -> 0x00bd }
            return r7
        L_0x00bd:
            r0 = move-exception
            r1 = r0
            r0 = r1
            java.nio.channels.FileLockInterruptionException r1 = new java.nio.channels.FileLockInterruptionException
            r1.<init>()
            throw r1
        L_0x00c6:
            r0 = move-exception
            r11 = r3
            r13 = r5
            r12 = r6
            r14 = r7
        L_0x00cb:
            r1 = r11
            goto L_0x00d1
        L_0x00cd:
            r0 = move-exception
        L_0x00ce:
            r13 = r5
            r12 = r6
            r14 = r7
        L_0x00d1:
            if (r12 != 0) goto L_0x00d6
            r13.remove(r14)
        L_0x00d6:
            sun.nio.ch.NativeThreadSet r2 = r8.threads
            r2.remove(r1)
            r8.end(r12)     // Catch:{ ClosedByInterruptException -> 0x00e0 }
            throw r0
        L_0x00e0:
            r0 = move-exception
            r2 = r0
            r0 = r2
            java.nio.channels.FileLockInterruptionException r2 = new java.nio.channels.FileLockInterruptionException
            r2.<init>()
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.FileChannelImpl.lock(long, long, boolean):java.nio.channels.FileLock");
    }

    public FileLock tryLock(long position, long size, boolean shared) throws IOException {
        int i;
        ensureOpen();
        if (shared && !this.readable) {
            throw new NonReadableChannelException();
        } else if (shared || this.writable) {
            FileLockImpl fileLockImpl = new FileLockImpl((FileChannel) this, position, size, shared);
            FileLockTable fileLockTable2 = fileLockTable();
            fileLockTable2.add(fileLockImpl);
            int add = this.threads.add();
            try {
                ensureOpen();
                int lock = this.f884nd.lock(this.f883fd, false, position, size, shared);
                if (lock == -1) {
                    try {
                        fileLockTable2.remove(fileLockImpl);
                        this.threads.remove(add);
                        return null;
                    } catch (Throwable th) {
                        th = th;
                        FileLockTable fileLockTable3 = fileLockTable2;
                        i = add;
                        FileLockImpl fileLockImpl2 = fileLockImpl;
                        this.threads.remove(i);
                        throw th;
                    }
                } else if (lock == 1) {
                    FileLockTable fileLockTable4 = fileLockTable2;
                    i = add;
                    FileLockImpl fileLockImpl3 = fileLockImpl;
                    FileLockImpl fileLockImpl4 = new FileLockImpl((FileChannel) this, position, size, false);
                    FileLockImpl fileLockImpl5 = fileLockImpl4;
                    fileLockTable4.replace(fileLockImpl3, fileLockImpl5);
                    this.threads.remove(i);
                    return fileLockImpl5;
                } else {
                    FileLockTable fileLockTable5 = fileLockTable2;
                    FileLockImpl fileLockImpl6 = fileLockImpl;
                    this.threads.remove(add);
                    return fileLockImpl6;
                }
            } catch (IOException e) {
                i = add;
                fileLockTable2.remove(fileLockImpl);
                throw e;
            } catch (Throwable th2) {
                th = th2;
                this.threads.remove(i);
                throw th;
            }
        } else {
            throw new NonWritableChannelException();
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: package-private */
    public void release(FileLockImpl fli) throws IOException {
        int add = this.threads.add();
        try {
            ensureOpen();
            this.f884nd.release(this.f883fd, fli.position(), fli.size());
            this.threads.remove(add);
            this.fileLockTable.remove(fli);
        } catch (Throwable th) {
            this.threads.remove(add);
            throw th;
        }
    }

    /* renamed from: sun.nio.ch.FileChannelImpl$SimpleFileLockTable */
    private static class SimpleFileLockTable extends FileLockTable {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final List<FileLock> lockList = new ArrayList(2);

        static {
            Class<FileChannelImpl> cls = FileChannelImpl.class;
        }

        private void checkList(long j, long j2) throws OverlappingFileLockException {
            for (FileLock overlaps : this.lockList) {
                if (overlaps.overlaps(j, j2)) {
                    throw new OverlappingFileLockException();
                }
            }
        }

        public void add(FileLock fileLock) throws OverlappingFileLockException {
            synchronized (this.lockList) {
                checkList(fileLock.position(), fileLock.size());
                this.lockList.add(fileLock);
            }
        }

        public void remove(FileLock fileLock) {
            synchronized (this.lockList) {
                this.lockList.remove((Object) fileLock);
            }
        }

        public List<FileLock> removeAll() {
            ArrayList arrayList;
            synchronized (this.lockList) {
                arrayList = new ArrayList(this.lockList);
                this.lockList.clear();
            }
            return arrayList;
        }

        public void replace(FileLock fileLock, FileLock fileLock2) {
            synchronized (this.lockList) {
                this.lockList.remove((Object) fileLock);
                this.lockList.add(fileLock2);
            }
        }
    }
}
