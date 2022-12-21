package com.android.systemui.statusbar.policy;

import android.content.SharedPreferences;
import com.android.systemui.controls.controller.SeedResponse;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DeviceControlsControllerImpl$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ DeviceControlsControllerImpl f$0;
    public final /* synthetic */ SharedPreferences f$1;

    public /* synthetic */ DeviceControlsControllerImpl$$ExternalSyntheticLambda1(DeviceControlsControllerImpl deviceControlsControllerImpl, SharedPreferences sharedPreferences) {
        this.f$0 = deviceControlsControllerImpl;
        this.f$1 = sharedPreferences;
    }

    public final void accept(Object obj) {
        DeviceControlsControllerImpl.m3234seedFavorites$lambda5(this.f$0, this.f$1, (SeedResponse) obj);
    }
}
