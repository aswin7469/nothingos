package java.lang.ref;

public abstract class Reference<T> {
    private static boolean disableIntrinsic = false;
    private static boolean slowPathEnabled = false;
    Reference<?> pendingNext;
    final ReferenceQueue<? super T> queue;
    Reference queueNext;
    volatile T referent;

    private final native T getReferent();

    private final native boolean refersTo0(Object obj);

    /* access modifiers changed from: package-private */
    public native void clearReferent();

    public T get() {
        return getReferent();
    }

    public final boolean refersTo(T t) {
        return refersTo0(t);
    }

    public void clear() {
        clearReferent();
    }

    @Deprecated(since = "9")
    public boolean isEnqueued() {
        ReferenceQueue<? super T> referenceQueue = this.queue;
        return referenceQueue != null && referenceQueue.isEnqueued(this);
    }

    public boolean enqueue() {
        ReferenceQueue<? super T> referenceQueue = this.queue;
        return referenceQueue != null && referenceQueue.enqueue(this);
    }

    Reference(T t) {
        this(t, (ReferenceQueue) null);
    }

    Reference(T t, ReferenceQueue<? super T> referenceQueue) {
        this.referent = t;
        this.queue = referenceQueue;
    }

    public static void reachabilityFence(Object obj) {
        SinkHolder.sink = obj;
        if (SinkHolder.finalize_count == 0) {
            SinkHolder.sink = null;
        }
    }

    private static class SinkHolder {
        /* access modifiers changed from: private */
        public static volatile int finalize_count;
        static volatile Object sink;
        private static Object sinkUser = new Object() {
            /* access modifiers changed from: protected */
            public void finalize() {
                if (SinkHolder.sink != null || SinkHolder.finalize_count <= 0) {
                    SinkHolder.finalize_count = SinkHolder.finalize_count + 1;
                    return;
                }
                throw new AssertionError((Object) "Can't get here");
            }
        };

        private SinkHolder() {
        }
    }
}
