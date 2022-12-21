package com.android.systemui.controls.management;

import com.android.settingslib.applications.ServiceListing;
import java.util.List;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlsListingControllerImpl$$ExternalSyntheticLambda0 implements ServiceListing.Callback {
    public final /* synthetic */ ControlsListingControllerImpl f$0;

    public /* synthetic */ ControlsListingControllerImpl$$ExternalSyntheticLambda0(ControlsListingControllerImpl controlsListingControllerImpl) {
        this.f$0 = controlsListingControllerImpl;
    }

    public final void onServicesReloaded(List list) {
        ControlsListingControllerImpl.m2654serviceListingCallback$lambda3(this.f$0, list);
    }
}
