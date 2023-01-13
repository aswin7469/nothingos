package com.android.systemui.controls.controller;

import java.util.List;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlsControllerImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ControlsControllerImpl f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ Consumer f$2;

    public /* synthetic */ ControlsControllerImpl$$ExternalSyntheticLambda0(ControlsControllerImpl controlsControllerImpl, List list, Consumer consumer) {
        this.f$0 = controlsControllerImpl;
        this.f$1 = list;
        this.f$2 = consumer;
    }

    public final void run() {
        ControlsControllerImpl.m2622seedFavoritesForComponents$lambda3(this.f$0, this.f$1, this.f$2);
    }
}
