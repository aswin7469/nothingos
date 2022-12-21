package com.android.systemui.controls.management;

import com.android.systemui.controls.controller.ControlsController;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlsFavoritingActivity$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ ControlsFavoritingActivity f$0;
    public final /* synthetic */ CharSequence f$1;

    public /* synthetic */ ControlsFavoritingActivity$$ExternalSyntheticLambda3(ControlsFavoritingActivity controlsFavoritingActivity, CharSequence charSequence) {
        this.f$0 = controlsFavoritingActivity;
        this.f$1 = charSequence;
    }

    public final void accept(Object obj) {
        ControlsFavoritingActivity.m2643loadControls$lambda9$lambda7(this.f$0, this.f$1, (ControlsController.LoadData) obj);
    }
}
