package com.android.systemui.controls.p010ui;

import com.android.systemui.controls.p010ui.ControlActionCoordinatorImpl;
import com.android.systemui.plugins.ActivityStarter;

/* renamed from: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda9 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlActionCoordinatorImpl$$ExternalSyntheticLambda9 implements ActivityStarter.OnDismissAction {
    public final /* synthetic */ ControlActionCoordinatorImpl.Action f$0;

    public /* synthetic */ ControlActionCoordinatorImpl$$ExternalSyntheticLambda9(ControlActionCoordinatorImpl.Action action) {
        this.f$0 = action;
    }

    public final boolean onDismiss() {
        return ControlActionCoordinatorImpl.m2678bouncerOrRun$lambda1(this.f$0);
    }
}
