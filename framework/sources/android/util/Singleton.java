package android.util;
/* loaded from: classes3.dex */
public abstract class Singleton<T> {
    private T mInstance;

    /* renamed from: create */
    protected abstract T mo3561create();

    public final T get() {
        T t;
        synchronized (this) {
            if (this.mInstance == null) {
                this.mInstance = mo3561create();
            }
            t = this.mInstance;
        }
        return t;
    }
}
