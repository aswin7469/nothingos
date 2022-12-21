package com.android.systemui.controls.controller;

import android.content.ComponentName;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlsControllerImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ComponentName f$0;
    public final /* synthetic */ CharSequence f$1;
    public final /* synthetic */ ControlInfo f$2;
    public final /* synthetic */ ControlsControllerImpl f$3;

    public /* synthetic */ ControlsControllerImpl$$ExternalSyntheticLambda2(ComponentName componentName, CharSequence charSequence, ControlInfo controlInfo, ControlsControllerImpl controlsControllerImpl) {
        this.f$0 = componentName;
        this.f$1 = charSequence;
        this.f$2 = controlInfo;
        this.f$3 = controlsControllerImpl;
    }

    public final void run() {
        ControlsControllerImpl.m2612addFavorite$lambda8(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
