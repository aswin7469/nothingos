package com.android.systemui.sensorprivacy;

import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SensorUseStartedActivity$$ExternalSyntheticLambda4 implements IndividualSensorPrivacyController.Callback {
    public final /* synthetic */ SensorUseStartedActivity f$0;

    public /* synthetic */ SensorUseStartedActivity$$ExternalSyntheticLambda4(SensorUseStartedActivity sensorUseStartedActivity) {
        this.f$0 = sensorUseStartedActivity;
    }

    public final void onSensorBlockedChanged(int i, boolean z) {
        SensorUseStartedActivity.m3022onCreate$lambda2(this.f$0, i, z);
    }
}
