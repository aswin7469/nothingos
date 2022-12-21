package sun.nio.p033ch;

import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.p026io.IOException;

/* renamed from: sun.nio.ch.FileLockImpl */
public class FileLockImpl extends FileLock {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private volatile boolean invalid;

    FileLockImpl(FileChannel fileChannel, long j, long j2, boolean z) {
        super(fileChannel, j, j2, z);
    }

    FileLockImpl(AsynchronousFileChannel asynchronousFileChannel, long j, long j2, boolean z) {
        super(asynchronousFileChannel, j, j2, z);
    }

    public boolean isValid() {
        return !this.invalid;
    }

    /* access modifiers changed from: package-private */
    public void invalidate() {
        this.invalid = true;
    }

    public synchronized void release() throws IOException {
        Channel acquiredBy = acquiredBy();
        if (!acquiredBy.isOpen()) {
            throw new ClosedChannelException();
        } else if (isValid()) {
            if (acquiredBy instanceof FileChannelImpl) {
                ((FileChannelImpl) acquiredBy).release(this);
            } else if (acquiredBy instanceof AsynchronousFileChannelImpl) {
                ((AsynchronousFileChannelImpl) acquiredBy).release(this);
            } else {
                throw new AssertionError();
            }
            invalidate();
        }
    }
}
