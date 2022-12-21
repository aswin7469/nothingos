package com.android.systemui.controls.management;

import android.os.UserHandle;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlsListingControllerImpl$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ ControlsListingControllerImpl f$0;
    public final /* synthetic */ UserHandle f$1;

    public /* synthetic */ ControlsListingControllerImpl$$ExternalSyntheticLambda3(ControlsListingControllerImpl controlsListingControllerImpl, UserHandle userHandle) {
        this.f$0 = controlsListingControllerImpl;
        this.f$1 = userHandle;
    }

    public final void run() {
        ControlsListingControllerImpl.m2652changeUser$lambda4(this.f$0, this.f$1);
    }
}
