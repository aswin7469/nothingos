package com.android.systemui.controls.controller;

import android.service.controls.IControlsSubscriber;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlsProviderLifecycleManager$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ControlsProviderLifecycleManager f$0;
    public final /* synthetic */ IControlsSubscriber.Stub f$1;

    public /* synthetic */ ControlsProviderLifecycleManager$$ExternalSyntheticLambda0(ControlsProviderLifecycleManager controlsProviderLifecycleManager, IControlsSubscriber.Stub stub) {
        this.f$0 = controlsProviderLifecycleManager;
        this.f$1 = stub;
    }

    public final void run() {
        ControlsProviderLifecycleManager.m2629maybeBindAndLoadSuggested$lambda11(this.f$0, this.f$1);
    }
}