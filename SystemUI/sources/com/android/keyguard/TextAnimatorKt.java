package com.android.keyguard;

import android.util.SparseArray;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;

@Metadata(mo65042d1 = {"\u0000$\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a3\u0010\u0004\u001a\u0002H\u0005\"\u0004\b\u0000\u0010\u0005*\b\u0012\u0004\u0012\u0002H\u00050\u00062\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00050\nH\u0002¢\u0006\u0002\u0010\u000b\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000¨\u0006\f"}, mo65043d2 = {"DEFAULT_ANIMATION_DURATION", "", "TAG_WGHT", "", "getOrElse", "V", "Landroid/util/SparseArray;", "key", "", "defaultValue", "Lkotlin/Function0;", "(Landroid/util/SparseArray;ILkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: TextAnimator.kt */
public final class TextAnimatorKt {
    private static final long DEFAULT_ANIMATION_DURATION = 300;
    private static final String TAG_WGHT = "wght";

    /* access modifiers changed from: private */
    public static final <V> V getOrElse(SparseArray<V> sparseArray, int i, Function0<? extends V> function0) {
        V v = sparseArray.get(i);
        if (v != null) {
            return v;
        }
        V invoke = function0.invoke();
        sparseArray.put(i, invoke);
        return invoke;
    }
}
