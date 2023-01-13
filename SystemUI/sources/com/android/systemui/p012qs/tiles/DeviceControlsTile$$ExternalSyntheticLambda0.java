package com.android.systemui.p012qs.tiles;

import android.content.Intent;
import com.android.systemui.animation.ActivityLaunchAnimator;

/* renamed from: com.android.systemui.qs.tiles.DeviceControlsTile$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DeviceControlsTile$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DeviceControlsTile f$0;
    public final /* synthetic */ Intent f$1;
    public final /* synthetic */ ActivityLaunchAnimator.Controller f$2;

    public /* synthetic */ DeviceControlsTile$$ExternalSyntheticLambda0(DeviceControlsTile deviceControlsTile, Intent intent, ActivityLaunchAnimator.Controller controller) {
        this.f$0 = deviceControlsTile;
        this.f$1 = intent;
        this.f$2 = controller;
    }

    public final void run() {
        DeviceControlsTile.m2980handleClick$lambda4(this.f$0, this.f$1, this.f$2);
    }
}
