package com.android.systemui.statusbar.policy;

import kotlin.jvm.functions.Function0;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BatteryStateNotifier$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Function0 f$0;

    public /* synthetic */ BatteryStateNotifier$$ExternalSyntheticLambda0(Function0 function0) {
        this.f$0 = function0;
    }

    public final void run() {
        BatteryStateNotifier.m3224scheduleNotificationCancel$lambda0(this.f$0);
    }
}