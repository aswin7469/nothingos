package com.android.systemui.util.service;

import com.android.systemui.util.service.ObservableServiceConnection;
import java.lang.ref.WeakReference;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ObservableServiceConnection$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ ObservableServiceConnection.Callback f$0;

    public /* synthetic */ ObservableServiceConnection$$ExternalSyntheticLambda1(ObservableServiceConnection.Callback callback) {
        this.f$0 = callback;
    }

    public final boolean test(Object obj) {
        return ObservableServiceConnection.lambda$removeCallback$1(this.f$0, (WeakReference) obj);
    }
}
