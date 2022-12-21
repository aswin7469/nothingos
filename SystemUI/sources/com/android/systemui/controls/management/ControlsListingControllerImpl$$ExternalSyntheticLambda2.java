package com.android.systemui.controls.management;

import com.android.systemui.controls.management.ControlsListingController;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlsListingControllerImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ ControlsListingControllerImpl f$0;
    public final /* synthetic */ ControlsListingController.ControlsListingCallback f$1;

    public /* synthetic */ ControlsListingControllerImpl$$ExternalSyntheticLambda2(ControlsListingControllerImpl controlsListingControllerImpl, ControlsListingController.ControlsListingCallback controlsListingCallback) {
        this.f$0 = controlsListingControllerImpl;
        this.f$1 = controlsListingCallback;
    }

    public final void run() {
        ControlsListingControllerImpl.m2651addCallback$lambda5(this.f$0, this.f$1);
    }
}
