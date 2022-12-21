package sun.nio.p033ch;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.FileLock;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* renamed from: sun.nio.ch.AsynchronousFileChannelImpl */
abstract class AsynchronousFileChannelImpl extends AsynchronousFileChannel {
    protected final ReadWriteLock closeLock = new ReentrantReadWriteLock();
    protected volatile boolean closed;
    protected final ExecutorService executor;
    protected final FileDescriptor fdObj;
    private volatile FileLockTable fileLockTable;
    protected final boolean reading;
    protected final boolean writing;

    /* access modifiers changed from: package-private */
    public abstract <A> Future<FileLock> implLock(long j, long j2, boolean z, A a, CompletionHandler<FileLock, ? super A> completionHandler);

    /* access modifiers changed from: package-private */
    public abstract <A> Future<Integer> implRead(ByteBuffer byteBuffer, long j, A a, CompletionHandler<Integer, ? super A> completionHandler);

    /* access modifiers changed from: protected */
    public abstract void implRelease(FileLockImpl fileLockImpl) throws IOException;

    /* access modifiers changed from: package-private */
    public abstract <A> Future<Integer> implWrite(ByteBuffer byteBuffer, long j, A a, CompletionHandler<Integer, ? super A> completionHandler);

    protected AsynchronousFileChannelImpl(FileDescriptor fileDescriptor, boolean z, boolean z2, ExecutorService executorService) {
        this.fdObj = fileDescriptor;
        this.reading = z;
        this.writing = z2;
        this.executor = executorService;
    }

    /* access modifiers changed from: package-private */
    public final ExecutorService executor() {
        return this.executor;
    }

    public final boolean isOpen() {
        return !this.closed;
    }

    /* access modifiers changed from: protected */
    public final void begin() throws IOException {
        this.closeLock.readLock().lock();
        if (this.closed) {
            throw new ClosedChannelException();
        }
    }

    /* access modifiers changed from: protected */
    public final void end() {
        this.closeLock.readLock().unlock();
    }

    /* access modifiers changed from: protected */
    public final void end(boolean z) throws IOException {
        end();
        if (!z && !isOpen()) {
            throw new AsynchronousCloseException();
        }
    }

    public final Future<FileLock> lock(long j, long j2, boolean z) {
        return implLock(j, j2, z, (Object) null, (CompletionHandler) null);
    }

    public final <A> void lock(long j, long j2, boolean z, A a, CompletionHandler<FileLock, ? super A> completionHandler) {
        if (completionHandler != null) {
            implLock(j, j2, z, a, completionHandler);
            return;
        }
        throw new NullPointerException("'handler' is null");
    }

    /* access modifiers changed from: package-private */
    public final void ensureFileLockTableInitialized() throws IOException {
        if (this.fileLockTable == null) {
            synchronized (this) {
                if (this.fileLockTable == null) {
                    this.fileLockTable = FileLockTable.newSharedFileLockTable(this, this.fdObj);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void invalidateAllLocks() throws IOException {
        if (this.fileLockTable != null) {
            for (FileLock next : this.fileLockTable.removeAll()) {
                synchronized (next) {
                    if (next.isValid()) {
                        FileLockImpl fileLockImpl = (FileLockImpl) next;
                        implRelease(fileLockImpl);
                        fileLockImpl.invalidate();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public final FileLockImpl addToFileLockTable(long j, long j2, boolean z) {
        try {
            this.closeLock.readLock().lock();
            if (this.closed) {
                end();
                return null;
            }
            ensureFileLockTableInitialized();
            FileLockImpl fileLockImpl = new FileLockImpl((AsynchronousFileChannel) this, j, j2, z);
            this.fileLockTable.add(fileLockImpl);
            end();
            return fileLockImpl;
        } catch (IOException e) {
            throw new AssertionError((Object) e);
        } catch (Throwable th) {
            end();
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public final void removeFromFileLockTable(FileLockImpl fileLockImpl) {
        this.fileLockTable.remove(fileLockImpl);
    }

    /* access modifiers changed from: package-private */
    public final void release(FileLockImpl fileLockImpl) throws IOException {
        try {
            begin();
            implRelease(fileLockImpl);
            removeFromFileLockTable(fileLockImpl);
        } finally {
            end();
        }
    }

    public final Future<Integer> read(ByteBuffer byteBuffer, long j) {
        return implRead(byteBuffer, j, (Object) null, (CompletionHandler) null);
    }

    public final <A> void read(ByteBuffer byteBuffer, long j, A a, CompletionHandler<Integer, ? super A> completionHandler) {
        if (completionHandler != null) {
            implRead(byteBuffer, j, a, completionHandler);
            return;
        }
        throw new NullPointerException("'handler' is null");
    }

    public final Future<Integer> write(ByteBuffer byteBuffer, long j) {
        return implWrite(byteBuffer, j, (Object) null, (CompletionHandler) null);
    }

    public final <A> void write(ByteBuffer byteBuffer, long j, A a, CompletionHandler<Integer, ? super A> completionHandler) {
        if (completionHandler != null) {
            implWrite(byteBuffer, j, a, completionHandler);
            return;
        }
        throw new NullPointerException("'handler' is null");
    }
}
