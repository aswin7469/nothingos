package java.lang;

import java.lang.ThreadLocal;

public class InheritableThreadLocal<T> extends ThreadLocal<T> {
    /* access modifiers changed from: protected */
    public T childValue(T t) {
        return t;
    }

    /* access modifiers changed from: package-private */
    public ThreadLocal.ThreadLocalMap getMap(Thread thread) {
        return thread.inheritableThreadLocals;
    }

    /* access modifiers changed from: package-private */
    public void createMap(Thread thread, T t) {
        thread.inheritableThreadLocals = new ThreadLocal.ThreadLocalMap((ThreadLocal<?>) this, (Object) t);
    }
}
