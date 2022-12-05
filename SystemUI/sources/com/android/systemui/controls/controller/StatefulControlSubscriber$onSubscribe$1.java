package com.android.systemui.controls.controller;

import android.service.controls.IControlsSubscription;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatefulControlSubscriber.kt */
/* loaded from: classes.dex */
final class StatefulControlSubscriber$onSubscribe$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ IControlsSubscription $subs;
    final /* synthetic */ StatefulControlSubscriber this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StatefulControlSubscriber$onSubscribe$1(StatefulControlSubscriber statefulControlSubscriber, IControlsSubscription iControlsSubscription) {
        super(0);
        this.this$0 = statefulControlSubscriber;
        this.$subs = iControlsSubscription;
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
        ControlsProviderLifecycleManager controlsProviderLifecycleManager;
        long j;
        this.this$0.subscriptionOpen = true;
        this.this$0.subscription = this.$subs;
        controlsProviderLifecycleManager = this.this$0.provider;
        IControlsSubscription iControlsSubscription = this.$subs;
        j = this.this$0.requestLimit;
        controlsProviderLifecycleManager.startSubscription(iControlsSubscription, j);
    }
}
