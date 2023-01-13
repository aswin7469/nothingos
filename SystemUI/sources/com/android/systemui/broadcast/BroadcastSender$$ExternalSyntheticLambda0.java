package com.android.systemui.broadcast;

import com.android.systemui.util.wakelock.WakeLock;
import kotlin.jvm.functions.Function0;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BroadcastSender$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Function0 f$0;
    public final /* synthetic */ WakeLock f$1;
    public final /* synthetic */ BroadcastSender f$2;

    public /* synthetic */ BroadcastSender$$ExternalSyntheticLambda0(Function0 function0, WakeLock wakeLock, BroadcastSender broadcastSender) {
        this.f$0 = function0;
        this.f$1 = wakeLock;
        this.f$2 = broadcastSender;
    }

    public final void run() {
        BroadcastSender.m2600sendInBackground$lambda0(this.f$0, this.f$1, this.f$2);
    }
}
