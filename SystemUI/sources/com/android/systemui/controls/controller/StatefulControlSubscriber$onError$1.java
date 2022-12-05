package com.android.systemui.controls.controller;

import android.util.Log;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatefulControlSubscriber.kt */
/* loaded from: classes.dex */
final class StatefulControlSubscriber$onError$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ String $error;
    final /* synthetic */ StatefulControlSubscriber this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StatefulControlSubscriber$onError$1(StatefulControlSubscriber statefulControlSubscriber, String str) {
        super(0);
        this.this$0 = statefulControlSubscriber;
        this.$error = str;
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
        ControlsProviderLifecycleManager controlsProviderLifecycleManager;
        z = this.this$0.subscriptionOpen;
        if (z) {
            this.this$0.subscriptionOpen = false;
            StringBuilder sb = new StringBuilder();
            sb.append("onError receive from '");
            controlsProviderLifecycleManager = this.this$0.provider;
            sb.append(controlsProviderLifecycleManager.getComponentName());
            sb.append("': ");
            sb.append(this.$error);
            Log.e("StatefulControlSubscriber", sb.toString());
        }
    }
}
