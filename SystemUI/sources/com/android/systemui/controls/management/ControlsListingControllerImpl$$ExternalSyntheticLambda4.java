package com.android.systemui.controls.management;

import java.util.List;
import java.util.Set;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlsListingControllerImpl$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ ControlsListingControllerImpl f$0;
    public final /* synthetic */ Set f$1;
    public final /* synthetic */ List f$2;

    public /* synthetic */ ControlsListingControllerImpl$$ExternalSyntheticLambda4(ControlsListingControllerImpl controlsListingControllerImpl, Set set, List list) {
        this.f$0 = controlsListingControllerImpl;
        this.f$1 = set;
        this.f$2 = list;
    }

    public final void run() {
        ControlsListingControllerImpl.m2655serviceListingCallback$lambda3$lambda2(this.f$0, this.f$1, this.f$2);
    }
}
