package sun.nio.p035fs;

import java.nio.file.ClosedWatchServiceException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.p026io.IOException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/* renamed from: sun.nio.fs.AbstractWatchService */
abstract class AbstractWatchService implements WatchService {
    private final WatchKey CLOSE_KEY = new AbstractWatchKey((Path) null, (AbstractWatchService) null) {
        public void cancel() {
        }

        public boolean isValid() {
            return true;
        }
    };
    private final Object closeLock = new Object();
    private volatile boolean closed;
    private final LinkedBlockingDeque<WatchKey> pendingKeys = new LinkedBlockingDeque<>();

    /* access modifiers changed from: package-private */
    public abstract void implClose() throws IOException;

    /* access modifiers changed from: package-private */
    public abstract WatchKey register(Path path, WatchEvent.Kind<?>[] kindArr, WatchEvent.Modifier... modifierArr) throws IOException;

    protected AbstractWatchService() {
    }

    /* access modifiers changed from: package-private */
    public final void enqueueKey(WatchKey watchKey) {
        this.pendingKeys.offer(watchKey);
    }

    private void checkOpen() {
        if (this.closed) {
            throw new ClosedWatchServiceException();
        }
    }

    private void checkKey(WatchKey watchKey) {
        if (watchKey == this.CLOSE_KEY) {
            enqueueKey(watchKey);
        }
        checkOpen();
    }

    public final WatchKey poll() {
        checkOpen();
        WatchKey poll = this.pendingKeys.poll();
        checkKey(poll);
        return poll;
    }

    public final WatchKey poll(long j, TimeUnit timeUnit) throws InterruptedException {
        checkOpen();
        WatchKey poll = this.pendingKeys.poll(j, timeUnit);
        checkKey(poll);
        return poll;
    }

    public final WatchKey take() throws InterruptedException {
        checkOpen();
        WatchKey take = this.pendingKeys.take();
        checkKey(take);
        return take;
    }

    /* access modifiers changed from: package-private */
    public final boolean isOpen() {
        return !this.closed;
    }

    /* access modifiers changed from: package-private */
    public final Object closeLock() {
        return this.closeLock;
    }

    public final void close() throws IOException {
        synchronized (this.closeLock) {
            if (!this.closed) {
                this.closed = true;
                implClose();
                this.pendingKeys.clear();
                this.pendingKeys.offer(this.CLOSE_KEY);
            }
        }
    }
}
