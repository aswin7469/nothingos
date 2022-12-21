package com.android.systemui.util.sensors;

import android.hardware.SensorManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AsyncSensorManager$$ExternalSyntheticLambda8 implements Runnable {
    public final /* synthetic */ AsyncSensorManager f$0;
    public final /* synthetic */ SensorManager.DynamicSensorCallback f$1;

    public /* synthetic */ AsyncSensorManager$$ExternalSyntheticLambda8(AsyncSensorManager asyncSensorManager, SensorManager.DynamicSensorCallback dynamicSensorCallback) {
        this.f$0 = asyncSensorManager;
        this.f$1 = dynamicSensorCallback;
    }

    public final void run() {
        this.f$0.mo47049xc3ae84a7(this.f$1);
    }
}
