package com.android.systemui.statusbar.policy;

import android.hardware.devicestate.DeviceStateManager;

/* renamed from: com.android.systemui.statusbar.policy.DeviceStateRotationLockSettingController$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3156x3b31e417 implements DeviceStateManager.DeviceStateCallback {
    public final /* synthetic */ DeviceStateRotationLockSettingController f$0;

    public /* synthetic */ C3156x3b31e417(DeviceStateRotationLockSettingController deviceStateRotationLockSettingController) {
        this.f$0 = deviceStateRotationLockSettingController;
    }

    public final void onStateChanged(int i) {
        this.f$0.updateDeviceState(i);
    }
}
