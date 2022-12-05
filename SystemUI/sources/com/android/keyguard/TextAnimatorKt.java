package com.android.keyguard;

import android.util.SparseArray;
import kotlin.jvm.functions.Function0;
/* compiled from: TextAnimator.kt */
/* loaded from: classes.dex */
public final class TextAnimatorKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final <V> V getOrElse(SparseArray<V> sparseArray, int i, Function0<? extends V> function0) {
        V v = sparseArray.get(i);
        if (v == null) {
            V mo1951invoke = function0.mo1951invoke();
            sparseArray.put(i, mo1951invoke);
            return mo1951invoke;
        }
        return v;
    }
}
