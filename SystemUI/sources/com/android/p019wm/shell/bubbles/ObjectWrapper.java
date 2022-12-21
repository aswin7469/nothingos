package com.android.p019wm.shell.bubbles;

import android.os.Binder;
import android.os.IBinder;

/* renamed from: com.android.wm.shell.bubbles.ObjectWrapper */
public class ObjectWrapper<T> extends Binder {
    private T mObject;

    public ObjectWrapper(T t) {
        this.mObject = t;
    }

    public T get() {
        return this.mObject;
    }

    public void clear() {
        this.mObject = null;
    }

    public static IBinder wrap(Object obj) {
        return new ObjectWrapper(obj);
    }
}
