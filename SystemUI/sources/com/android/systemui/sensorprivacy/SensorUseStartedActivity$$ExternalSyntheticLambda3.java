package com.android.systemui.sensorprivacy;

import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SensorUseStartedActivity$$ExternalSyntheticLambda3 implements IndividualSensorPrivacyController.Callback {
    public final /* synthetic */ SensorUseStartedActivity f$0;

    public /* synthetic */ SensorUseStartedActivity$$ExternalSyntheticLambda3(SensorUseStartedActivity sensorUseStartedActivity) {
        this.f$0 = sensorUseStartedActivity;
    }

    public final void onSensorBlockedChanged(int i, boolean z) {
        SensorUseStartedActivity.m3021onCreate$lambda0(this.f$0, i, z);
    }
}
