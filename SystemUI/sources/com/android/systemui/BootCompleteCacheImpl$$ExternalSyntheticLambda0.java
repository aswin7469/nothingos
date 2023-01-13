package com.android.systemui;

import com.android.systemui.BootCompleteCache;
import java.lang.ref.WeakReference;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BootCompleteCacheImpl$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ BootCompleteCache.BootCompleteListener f$0;

    public /* synthetic */ BootCompleteCacheImpl$$ExternalSyntheticLambda0(BootCompleteCache.BootCompleteListener bootCompleteListener) {
        this.f$0 = bootCompleteListener;
    }

    public final boolean test(Object obj) {
        return BootCompleteCacheImpl.m2519removeListener$lambda4$lambda3(this.f$0, (WeakReference) obj);
    }
}
