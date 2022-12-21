package java.lang.ref;

public class SoftReference<T> extends Reference<T> {
    private static long clock;
    private long timestamp = clock;

    public SoftReference(T t) {
        super(t);
    }

    public SoftReference(T t, ReferenceQueue<? super T> referenceQueue) {
        super(t, referenceQueue);
    }

    public T get() {
        T t = super.get();
        if (t != null) {
            long j = this.timestamp;
            long j2 = clock;
            if (j != j2) {
                this.timestamp = j2;
            }
        }
        return t;
    }
}
