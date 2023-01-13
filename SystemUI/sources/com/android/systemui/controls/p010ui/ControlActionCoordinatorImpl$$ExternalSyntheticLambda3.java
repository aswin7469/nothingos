package com.android.systemui.controls.p010ui;

import android.app.PendingIntent;

/* renamed from: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlActionCoordinatorImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ ControlActionCoordinatorImpl f$0;
    public final /* synthetic */ PendingIntent f$1;
    public final /* synthetic */ ControlViewHolder f$2;

    public /* synthetic */ ControlActionCoordinatorImpl$$ExternalSyntheticLambda3(ControlActionCoordinatorImpl controlActionCoordinatorImpl, PendingIntent pendingIntent, ControlViewHolder controlViewHolder) {
        this.f$0 = controlActionCoordinatorImpl;
        this.f$1 = pendingIntent;
        this.f$2 = controlViewHolder;
    }

    public final void run() {
        ControlActionCoordinatorImpl.m2681showDetail$lambda7(this.f$0, this.f$1, this.f$2);
    }
}
