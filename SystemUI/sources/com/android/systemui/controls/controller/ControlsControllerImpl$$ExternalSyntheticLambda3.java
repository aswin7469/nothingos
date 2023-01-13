package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.service.controls.Control;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlsControllerImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ ComponentName f$0;
    public final /* synthetic */ Control f$1;
    public final /* synthetic */ ControlsControllerImpl f$2;

    public /* synthetic */ ControlsControllerImpl$$ExternalSyntheticLambda3(ComponentName componentName, Control control, ControlsControllerImpl controlsControllerImpl) {
        this.f$0 = componentName;
        this.f$1 = control;
        this.f$2 = controlsControllerImpl;
    }

    public final void run() {
        ControlsControllerImpl.m2620refreshStatus$lambda10(this.f$0, this.f$1, this.f$2);
    }
}
