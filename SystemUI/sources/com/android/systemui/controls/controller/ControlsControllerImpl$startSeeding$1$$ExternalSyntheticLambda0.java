package com.android.systemui.controls.controller;

import android.content.ComponentName;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlsControllerImpl$startSeeding$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ List f$0;
    public final /* synthetic */ ControlsControllerImpl f$1;
    public final /* synthetic */ Consumer f$2;
    public final /* synthetic */ ComponentName f$3;
    public final /* synthetic */ List f$4;
    public final /* synthetic */ boolean f$5;

    public /* synthetic */ ControlsControllerImpl$startSeeding$1$$ExternalSyntheticLambda0(List list, ControlsControllerImpl controlsControllerImpl, Consumer consumer, ComponentName componentName, List list2, boolean z) {
        this.f$0 = list;
        this.f$1 = controlsControllerImpl;
        this.f$2 = consumer;
        this.f$3 = componentName;
        this.f$4 = list2;
        this.f$5 = z;
    }

    public final void run() {
        ControlsControllerImpl$startSeeding$1.m2628accept$lambda2(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
