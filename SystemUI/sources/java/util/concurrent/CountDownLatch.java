package java.util.concurrent;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class CountDownLatch {
    private final Sync sync;

    private static final class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 4982264981922014374L;

        Sync(int i) {
            setState(i);
        }

        /* access modifiers changed from: package-private */
        public int getCount() {
            return getState();
        }

        /* access modifiers changed from: protected */
        public int tryAcquireShared(int i) {
            return getState() == 0 ? 1 : -1;
        }

        /* access modifiers changed from: protected */
        public boolean tryReleaseShared(int i) {
            int state;
            int i2;
            do {
                state = getState();
                if (state == 0) {
                    return false;
                }
                i2 = state - 1;
            } while (!compareAndSetState(state, i2));
            if (i2 == 0) {
                return true;
            }
            return false;
        }
    }

    public CountDownLatch(int i) {
        if (i >= 0) {
            this.sync = new Sync(i);
            return;
        }
        throw new IllegalArgumentException("count < 0");
    }

    public void await() throws InterruptedException {
        this.sync.acquireSharedInterruptibly(1);
    }

    public boolean await(long j, TimeUnit timeUnit) throws InterruptedException {
        return this.sync.tryAcquireSharedNanos(1, timeUnit.toNanos(j));
    }

    public void countDown() {
        this.sync.releaseShared(1);
    }

    public long getCount() {
        return (long) this.sync.getCount();
    }

    public String toString() {
        return super.toString() + "[Count = " + this.sync.getCount() + NavigationBarInflaterView.SIZE_MOD_END;
    }
}
