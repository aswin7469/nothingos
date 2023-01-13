package com.android.systemui.controls.controller;

import android.os.IBinder;
import android.service.controls.Control;
import android.util.Log;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo65043d2 = {"<anonymous>", "", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatefulControlSubscriber.kt */
final class StatefulControlSubscriber$onNext$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ Control $control;
    final /* synthetic */ IBinder $token;
    final /* synthetic */ StatefulControlSubscriber this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    StatefulControlSubscriber$onNext$1(StatefulControlSubscriber statefulControlSubscriber, IBinder iBinder, Control control) {
        super(0);
        this.this$0 = statefulControlSubscriber;
        this.$token = iBinder;
        this.$control = control;
    }

    public final void invoke() {
        if (!this.this$0.subscriptionOpen) {
            Log.w("StatefulControlSubscriber", "Refresh outside of window for token:" + this.$token);
        } else {
            this.this$0.controller.refreshStatus(this.this$0.provider.getComponentName(), this.$control);
        }
    }
}
