package com.android.systemui.statusbar.policy;

import com.android.systemui.controls.management.ControlsListingController;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DeviceControlsControllerImpl$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ DeviceControlsControllerImpl f$0;

    public /* synthetic */ DeviceControlsControllerImpl$$ExternalSyntheticLambda2(DeviceControlsControllerImpl deviceControlsControllerImpl) {
        this.f$0 = deviceControlsControllerImpl;
    }

    public final void accept(Object obj) {
        DeviceControlsControllerImpl.m3241setCallback$lambda1(this.f$0, (ControlsListingController) obj);
    }
}
