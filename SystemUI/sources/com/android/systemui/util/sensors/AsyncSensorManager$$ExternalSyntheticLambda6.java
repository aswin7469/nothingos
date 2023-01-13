package com.android.systemui.util.sensors;

import com.android.systemui.plugins.SensorManagerPlugin;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AsyncSensorManager$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ AsyncSensorManager f$0;
    public final /* synthetic */ SensorManagerPlugin.Sensor f$1;
    public final /* synthetic */ SensorManagerPlugin.SensorEventListener f$2;

    public /* synthetic */ AsyncSensorManager$$ExternalSyntheticLambda6(AsyncSensorManager asyncSensorManager, SensorManagerPlugin.Sensor sensor, SensorManagerPlugin.SensorEventListener sensorEventListener) {
        this.f$0 = asyncSensorManager;
        this.f$1 = sensor;
        this.f$2 = sensorEventListener;
    }

    public final void run() {
        this.f$0.mo47063x9b218268(this.f$1, this.f$2);
    }
}
