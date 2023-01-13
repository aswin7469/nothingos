package com.android.systemui.controls.controller;

import android.content.ComponentName;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlsControllerImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ ControlsControllerImpl f$0;
    public final /* synthetic */ ComponentName f$1;
    public final /* synthetic */ Consumer f$2;
    public final /* synthetic */ Consumer f$3;

    public /* synthetic */ ControlsControllerImpl$$ExternalSyntheticLambda1(ControlsControllerImpl controlsControllerImpl, ComponentName componentName, Consumer consumer, Consumer consumer2) {
        this.f$0 = controlsControllerImpl;
        this.f$1 = componentName;
        this.f$2 = consumer;
        this.f$3 = consumer2;
    }

    public final void run() {
        ControlsControllerImpl.m2619loadForComponent$lambda1(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
