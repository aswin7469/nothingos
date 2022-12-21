package jdk.internal.ref;

import java.lang.ref.Cleaner;
import java.lang.ref.ReferenceQueue;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import jdk.internal.misc.InnocuousThread;

public final class CleanerImpl implements Runnable {
    private static Function<Cleaner, CleanerImpl> cleanerImplAccess;
    final PhantomCleanable<?> phantomCleanableList = new PhantomCleanableRef();
    final ReferenceQueue<Object> queue = new ReferenceQueue<>();

    public static void setCleanerImplAccess(Function<Cleaner, CleanerImpl> function) {
        if (cleanerImplAccess == null) {
            cleanerImplAccess = function;
            return;
        }
        throw new InternalError("cleanerImplAccess");
    }

    static CleanerImpl getCleanerImpl(Cleaner cleaner) {
        return cleanerImplAccess.apply(cleaner);
    }

    public void start(Cleaner cleaner, ThreadFactory threadFactory) {
        if (getCleanerImpl(cleaner) == this) {
            new CleanerCleanable(cleaner);
            if (threadFactory == null) {
                threadFactory = InnocuousThreadFactory.factory();
            }
            Thread newThread = threadFactory.newThread(this);
            newThread.setDaemon(true);
            newThread.start();
            return;
        }
        throw new AssertionError((Object) "wrong cleaner");
    }

    public void run() {
        Thread currentThread = Thread.currentThread();
        InnocuousThread innocuousThread = currentThread instanceof InnocuousThread ? (InnocuousThread) currentThread : null;
        while (!this.phantomCleanableList.isListEmpty()) {
            if (innocuousThread != null) {
                innocuousThread.eraseThreadLocals();
            }
            try {
                Cleaner.Cleanable cleanable = (Cleaner.Cleanable) this.queue.remove(60000);
                if (cleanable != null) {
                    cleanable.clean();
                }
            } catch (Throwable unused) {
            }
        }
    }

    public static final class PhantomCleanableRef extends PhantomCleanable<Object> {
        private final Runnable action;

        public PhantomCleanableRef(Object obj, Cleaner cleaner, Runnable runnable) {
            super(obj, cleaner);
            this.action = runnable;
        }

        PhantomCleanableRef() {
            this.action = null;
        }

        /* access modifiers changed from: protected */
        public void performCleanup() {
            this.action.run();
        }

        public Object get() {
            throw new UnsupportedOperationException("get");
        }

        public void clear() {
            throw new UnsupportedOperationException("clear");
        }
    }

    static final class InnocuousThreadFactory implements ThreadFactory {
        static final ThreadFactory factory = new InnocuousThreadFactory();
        final AtomicInteger cleanerThreadNumber = new AtomicInteger();

        InnocuousThreadFactory() {
        }

        static ThreadFactory factory() {
            return factory;
        }

        public Thread newThread(final Runnable runnable) {
            return (Thread) AccessController.doPrivileged(new PrivilegedAction<Thread>() {
                public Thread run() {
                    Thread newThread = InnocuousThread.newThread(runnable);
                    newThread.setPriority(8);
                    newThread.setName("Cleaner-" + InnocuousThreadFactory.this.cleanerThreadNumber.getAndIncrement());
                    return newThread;
                }
            });
        }
    }

    static final class CleanerCleanable extends PhantomCleanable<Cleaner> {
        /* access modifiers changed from: protected */
        public void performCleanup() {
        }

        CleanerCleanable(Cleaner cleaner) {
            super(cleaner, cleaner);
        }
    }
}
