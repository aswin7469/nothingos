package com.android.settings.sim.receivers;

import android.content.BroadcastReceiver;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SimSlotChangeReceiver$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ BroadcastReceiver.PendingResult f$0;

    public /* synthetic */ SimSlotChangeReceiver$$ExternalSyntheticLambda1(BroadcastReceiver.PendingResult pendingResult) {
        this.f$0 = pendingResult;
    }

    public final void run() {
        this.f$0.finish();
    }
}
