package com.android.systemui.controls.controller;

import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlsControllerImpl$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ ControlsControllerImpl f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ ControlsControllerImpl$$ExternalSyntheticLambda5(ControlsControllerImpl controlsControllerImpl, Consumer consumer) {
        this.f$0 = controlsControllerImpl;
        this.f$1 = consumer;
    }

    public final void run() {
        ControlsControllerImpl.m2618addSeedingFavoritesCallback$lambda2(this.f$0, this.f$1);
    }
}
