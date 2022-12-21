package com.android.systemui.util.service;

import com.android.systemui.util.service.Observer;
import java.lang.ref.WeakReference;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PackageObserver$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ Observer.Callback f$0;

    public /* synthetic */ PackageObserver$$ExternalSyntheticLambda0(Observer.Callback callback) {
        this.f$0 = callback;
    }

    public final boolean test(Object obj) {
        return PackageObserver.lambda$removeCallback$0(this.f$0, (WeakReference) obj);
    }
}
