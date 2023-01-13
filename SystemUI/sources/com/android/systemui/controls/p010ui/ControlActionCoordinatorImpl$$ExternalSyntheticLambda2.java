package com.android.systemui.controls.p010ui;

import android.app.PendingIntent;
import java.util.List;

/* renamed from: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlActionCoordinatorImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ List f$0;
    public final /* synthetic */ ControlActionCoordinatorImpl f$1;
    public final /* synthetic */ ControlViewHolder f$2;
    public final /* synthetic */ PendingIntent f$3;

    public /* synthetic */ ControlActionCoordinatorImpl$$ExternalSyntheticLambda2(List list, ControlActionCoordinatorImpl controlActionCoordinatorImpl, ControlViewHolder controlViewHolder, PendingIntent pendingIntent) {
        this.f$0 = list;
        this.f$1 = controlActionCoordinatorImpl;
        this.f$2 = controlViewHolder;
        this.f$3 = pendingIntent;
    }

    public final void run() {
        ControlActionCoordinatorImpl.m2682showDetail$lambda7$lambda6(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
