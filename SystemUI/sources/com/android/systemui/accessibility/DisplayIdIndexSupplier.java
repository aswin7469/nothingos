package com.android.systemui.accessibility;

import android.hardware.display.DisplayManager;
import android.util.SparseArray;
import android.view.Display;
import java.util.function.Consumer;
/* loaded from: classes.dex */
abstract class DisplayIdIndexSupplier<T> {
    private final DisplayManager mDisplayManager;
    private final SparseArray<T> mSparseArray = new SparseArray<>();

    /* renamed from: createInstance */
    protected abstract T mo313createInstance(Display display);

    /* JADX INFO: Access modifiers changed from: package-private */
    public DisplayIdIndexSupplier(DisplayManager displayManager) {
        this.mDisplayManager = displayManager;
    }

    public T get(int i) {
        T t = this.mSparseArray.get(i);
        if (t != null) {
            return t;
        }
        Display display = this.mDisplayManager.getDisplay(i);
        if (display == null) {
            return null;
        }
        T mo313createInstance = mo313createInstance(display);
        this.mSparseArray.put(i, mo313createInstance);
        return mo313createInstance;
    }

    public T valueAt(int i) {
        return this.mSparseArray.get(i);
    }

    public void forEach(Consumer<T> consumer) {
        for (int i = 0; i < this.mSparseArray.size(); i++) {
            consumer.accept(this.mSparseArray.valueAt(i));
        }
    }
}
