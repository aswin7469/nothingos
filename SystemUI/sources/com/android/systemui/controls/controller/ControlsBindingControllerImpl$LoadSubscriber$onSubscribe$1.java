package com.android.systemui.controls.controller;

import android.service.controls.IControlsSubscription;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ControlsBindingControllerImpl.kt */
/* loaded from: classes.dex */
final class ControlsBindingControllerImpl$LoadSubscriber$onSubscribe$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ ControlsBindingControllerImpl this$0;
    final /* synthetic */ ControlsBindingControllerImpl.LoadSubscriber this$1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ControlsBindingControllerImpl$LoadSubscriber$onSubscribe$1(ControlsBindingControllerImpl controlsBindingControllerImpl, ControlsBindingControllerImpl.LoadSubscriber loadSubscriber) {
        super(0);
        this.this$0 = controlsBindingControllerImpl;
        this.this$1 = loadSubscriber;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1951invoke() {
        mo1951invoke();
        return Unit.INSTANCE;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final void mo1951invoke() {
        IControlsSubscription iControlsSubscription;
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.this$0.currentProvider;
        if (controlsProviderLifecycleManager == null) {
            return;
        }
        iControlsSubscription = this.this$1.subscription;
        if (iControlsSubscription != null) {
            controlsProviderLifecycleManager.cancelSubscription(iControlsSubscription);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("subscription");
            throw null;
        }
    }
}
