package com.android.systemui.controls.p010ui;

import android.app.PendingIntent;
import com.android.p019wm.shell.TaskView;
import java.util.function.Consumer;

/* renamed from: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlActionCoordinatorImpl$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ ControlActionCoordinatorImpl f$0;
    public final /* synthetic */ PendingIntent f$1;
    public final /* synthetic */ ControlViewHolder f$2;

    public /* synthetic */ ControlActionCoordinatorImpl$$ExternalSyntheticLambda0(ControlActionCoordinatorImpl controlActionCoordinatorImpl, PendingIntent pendingIntent, ControlViewHolder controlViewHolder) {
        this.f$0 = controlActionCoordinatorImpl;
        this.f$1 = pendingIntent;
        this.f$2 = controlViewHolder;
    }

    public final void accept(Object obj) {
        ControlActionCoordinatorImpl.m2678showDetail$lambda7$lambda6$lambda5(this.f$0, this.f$1, this.f$2, (TaskView) obj);
    }
}
