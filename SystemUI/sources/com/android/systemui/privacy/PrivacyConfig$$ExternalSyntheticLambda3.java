package com.android.systemui.privacy;

import java.lang.ref.WeakReference;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PrivacyConfig$$ExternalSyntheticLambda3 implements Predicate {
    public final /* synthetic */ WeakReference f$0;

    public /* synthetic */ PrivacyConfig$$ExternalSyntheticLambda3(WeakReference weakReference) {
        this.f$0 = weakReference;
    }

    public final boolean test(Object obj) {
        return PrivacyConfig.m2874removeCallback$lambda6$lambda5(this.f$0, (WeakReference) obj);
    }
}
