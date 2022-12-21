package java.lang.ref;

public final class FinalizerReference<T> extends Reference<T> {
    private static final Object LIST_LOCK = new Object();
    private static FinalizerReference<?> head = null;
    public static final ReferenceQueue<Object> queue = new ReferenceQueue<>();
    private FinalizerReference<?> next;
    private FinalizerReference<?> prev;
    private T zombie;

    private final native T getReferent();

    private native boolean makeCircularListIfUnenqueued();

    public FinalizerReference(T t, ReferenceQueue<? super T> referenceQueue) {
        super(t, referenceQueue);
    }

    public T get() {
        return this.zombie;
    }

    public void clear() {
        this.zombie = null;
    }

    public static void add(Object obj) {
        FinalizerReference<?> finalizerReference = new FinalizerReference<>(obj, queue);
        synchronized (LIST_LOCK) {
            finalizerReference.prev = null;
            FinalizerReference<?> finalizerReference2 = head;
            finalizerReference.next = finalizerReference2;
            if (finalizerReference2 != null) {
                finalizerReference2.prev = finalizerReference;
            }
            head = finalizerReference;
        }
    }

    public static void remove(FinalizerReference<?> finalizerReference) {
        synchronized (LIST_LOCK) {
            FinalizerReference<?> finalizerReference2 = finalizerReference.next;
            FinalizerReference<?> finalizerReference3 = finalizerReference.prev;
            finalizerReference.next = null;
            finalizerReference.prev = null;
            if (finalizerReference3 != null) {
                finalizerReference3.next = finalizerReference2;
            } else {
                head = finalizerReference2;
            }
            if (finalizerReference2 != null) {
                finalizerReference2.prev = finalizerReference3;
            }
        }
    }

    public static void finalizeAllEnqueued(long j) throws InterruptedException {
        Sentinel sentinel;
        do {
            sentinel = new Sentinel();
        } while (!enqueueSentinelReference(sentinel));
        sentinel.awaitFinalization(j);
    }

    private static boolean enqueueSentinelReference(Sentinel sentinel) {
        synchronized (LIST_LOCK) {
            for (FinalizerReference<?> finalizerReference = head; finalizerReference != null; finalizerReference = finalizerReference.next) {
                if (finalizerReference.getReferent() == sentinel) {
                    finalizerReference.clearReferent();
                    finalizerReference.zombie = sentinel;
                    if (!finalizerReference.makeCircularListIfUnenqueued()) {
                        return false;
                    }
                    ReferenceQueue.add(finalizerReference);
                    return true;
                }
            }
            throw new AssertionError((Object) "newly-created live Sentinel not on list!");
        }
    }

    private static class Sentinel {
        boolean finalized;

        private Sentinel() {
            this.finalized = false;
        }

        /* access modifiers changed from: protected */
        public synchronized void finalize() throws Throwable {
            if (!this.finalized) {
                this.finalized = true;
                notifyAll();
            } else {
                throw new AssertionError();
            }
        }

        /* access modifiers changed from: package-private */
        public synchronized void awaitFinalization(long j) throws InterruptedException {
            long nanoTime = System.nanoTime() + j;
            while (true) {
                if (this.finalized) {
                    break;
                } else if (j != 0) {
                    long nanoTime2 = nanoTime - System.nanoTime();
                    if (nanoTime2 <= 0) {
                        break;
                    }
                    wait(nanoTime2 / 1000000, (int) (nanoTime2 % 1000000));
                } else {
                    wait();
                }
            }
        }
    }
}
