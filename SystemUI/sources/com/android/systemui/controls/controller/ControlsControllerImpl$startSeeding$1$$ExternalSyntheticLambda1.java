package com.android.systemui.controls.controller;

import android.content.ComponentName;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlsControllerImpl$startSeeding$1$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ Consumer f$0;
    public final /* synthetic */ ComponentName f$1;
    public final /* synthetic */ ControlsControllerImpl f$2;
    public final /* synthetic */ List f$3;

    public /* synthetic */ ControlsControllerImpl$startSeeding$1$$ExternalSyntheticLambda1(Consumer consumer, ComponentName componentName, ControlsControllerImpl controlsControllerImpl, List list) {
        this.f$0 = consumer;
        this.f$1 = componentName;
        this.f$2 = controlsControllerImpl;
        this.f$3 = list;
    }

    public final void run() {
        ControlsControllerImpl$startSeeding$1.m2624error$lambda3(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
