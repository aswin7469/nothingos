package com.android.systemui.shared.system;

import android.view.ViewTreeObserver;
import com.android.systemui.shared.system.ViewTreeObserverWrapper;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ViewTreeObserverWrapper$$ExternalSyntheticLambda0 implements ViewTreeObserver.OnComputeInternalInsetsListener {
    public final /* synthetic */ ViewTreeObserverWrapper.OnComputeInsetsListener f$0;

    public /* synthetic */ ViewTreeObserverWrapper$$ExternalSyntheticLambda0(ViewTreeObserverWrapper.OnComputeInsetsListener onComputeInsetsListener) {
        this.f$0 = onComputeInsetsListener;
    }

    public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        ViewTreeObserverWrapper.lambda$addOnComputeInsetsListener$0(this.f$0, internalInsetsInfo);
    }
}
