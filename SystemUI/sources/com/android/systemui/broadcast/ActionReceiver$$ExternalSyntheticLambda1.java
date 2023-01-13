package com.android.systemui.broadcast;

import android.content.Context;
import android.content.Intent;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ActionReceiver$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ Intent f$0;
    public final /* synthetic */ ReceiverData f$1;
    public final /* synthetic */ ActionReceiver f$2;
    public final /* synthetic */ Context f$3;
    public final /* synthetic */ int f$4;

    public /* synthetic */ ActionReceiver$$ExternalSyntheticLambda1(Intent intent, ReceiverData receiverData, ActionReceiver actionReceiver, Context context, int i) {
        this.f$0 = intent;
        this.f$1 = receiverData;
        this.f$2 = actionReceiver;
        this.f$3 = context;
        this.f$4 = i;
    }

    public final void run() {
        ActionReceiver.m2598onReceive$lambda3$lambda2$lambda1(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
