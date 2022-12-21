package com.android.systemui.controls.controller;

import android.service.controls.IControlsSubscription;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo64987d2 = {"<anonymous>", "", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: StatefulControlSubscriber.kt */
final class StatefulControlSubscriber$onSubscribe$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ IControlsSubscription $subs;
    final /* synthetic */ StatefulControlSubscriber this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    StatefulControlSubscriber$onSubscribe$1(StatefulControlSubscriber statefulControlSubscriber, IControlsSubscription iControlsSubscription) {
        super(0);
        this.this$0 = statefulControlSubscriber;
        this.$subs = iControlsSubscription;
    }

    public final void invoke() {
        this.this$0.subscriptionOpen = true;
        this.this$0.subscription = this.$subs;
        this.this$0.provider.startSubscription(this.$subs, this.this$0.requestLimit);
    }
}
