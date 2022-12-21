package java.lang.ref;

public class PhantomReference<T> extends Reference<T> {
    public T get() {
        return null;
    }

    public PhantomReference(T t, ReferenceQueue<? super T> referenceQueue) {
        super(t, referenceQueue);
    }
}
