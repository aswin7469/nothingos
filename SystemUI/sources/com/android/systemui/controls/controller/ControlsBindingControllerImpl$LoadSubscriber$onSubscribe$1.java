package com.android.systemui.controls.controller;

import android.service.controls.IControlsSubscription;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo64987d2 = {"<anonymous>", "", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsBindingControllerImpl.kt */
final class ControlsBindingControllerImpl$LoadSubscriber$onSubscribe$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ ControlsBindingControllerImpl this$0;
    final /* synthetic */ ControlsBindingControllerImpl.LoadSubscriber this$1;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ControlsBindingControllerImpl$LoadSubscriber$onSubscribe$1(ControlsBindingControllerImpl controlsBindingControllerImpl, ControlsBindingControllerImpl.LoadSubscriber loadSubscriber) {
        super(0);
        this.this$0 = controlsBindingControllerImpl;
        this.this$1 = loadSubscriber;
    }

    public final void invoke() {
        ControlsProviderLifecycleManager access$getCurrentProvider$p = this.this$0.currentProvider;
        if (access$getCurrentProvider$p != null) {
            IControlsSubscription access$getSubscription$p = this.this$1.subscription;
            if (access$getSubscription$p == null) {
                Intrinsics.throwUninitializedPropertyAccessException("subscription");
                access$getSubscription$p = null;
            }
            access$getCurrentProvider$p.cancelSubscription(access$getSubscription$p);
        }
    }
}
