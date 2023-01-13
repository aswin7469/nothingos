package com.android.systemui.broadcast;

import android.content.Context;
import android.content.Intent;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ActionReceiver$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ActionReceiver f$0;
    public final /* synthetic */ Intent f$1;
    public final /* synthetic */ Context f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ ActionReceiver$$ExternalSyntheticLambda0(ActionReceiver actionReceiver, Intent intent, Context context, int i) {
        this.f$0 = actionReceiver;
        this.f$1 = intent;
        this.f$2 = context;
        this.f$3 = i;
    }

    public final void run() {
        ActionReceiver.m2597onReceive$lambda3(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
