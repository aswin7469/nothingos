package com.android.systemui.util.sensors;

import android.hardware.Sensor;
import android.hardware.TriggerEventListener;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AsyncSensorManager$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ AsyncSensorManager f$0;
    public final /* synthetic */ TriggerEventListener f$1;
    public final /* synthetic */ Sensor f$2;

    public /* synthetic */ AsyncSensorManager$$ExternalSyntheticLambda5(AsyncSensorManager asyncSensorManager, TriggerEventListener triggerEventListener, Sensor sensor) {
        this.f$0 = asyncSensorManager;
        this.f$1 = triggerEventListener;
        this.f$2 = sensor;
    }

    public final void run() {
        this.f$0.mo47055x22e43dcf(this.f$1, this.f$2);
    }
}
