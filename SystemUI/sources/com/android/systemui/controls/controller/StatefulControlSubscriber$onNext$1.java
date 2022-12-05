package com.android.systemui.controls.controller;

import android.os.IBinder;
import android.service.controls.Control;
import android.util.Log;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatefulControlSubscriber.kt */
/* loaded from: classes.dex */
final class StatefulControlSubscriber$onNext$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ Control $control;
    final /* synthetic */ IBinder $token;
    final /* synthetic */ StatefulControlSubscriber this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StatefulControlSubscriber$onNext$1(StatefulControlSubscriber statefulControlSubscriber, IBinder iBinder, Control control) {
        super(0);
        this.this$0 = statefulControlSubscriber;
        this.$token = iBinder;
        this.$control = control;
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
        boolean z;
        ControlsController controlsController;
        ControlsProviderLifecycleManager controlsProviderLifecycleManager;
        z = this.this$0.subscriptionOpen;
        if (z) {
            controlsController = this.this$0.controller;
            controlsProviderLifecycleManager = this.this$0.provider;
            controlsController.refreshStatus(controlsProviderLifecycleManager.getComponentName(), this.$control);
            return;
        }
        Log.w("StatefulControlSubscriber", Intrinsics.stringPlus("Refresh outside of window for token:", this.$token));
    }
}
