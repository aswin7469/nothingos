package com.android.systemui.privacy;

import java.lang.ref.WeakReference;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PrivacyItemController$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ WeakReference f$0;

    public /* synthetic */ PrivacyItemController$$ExternalSyntheticLambda0(WeakReference weakReference) {
        this.f$0 = weakReference;
    }

    public final boolean test(Object obj) {
        return PrivacyItemController.m2882removeCallback$lambda6(this.f$0, (WeakReference) obj);
    }
}
