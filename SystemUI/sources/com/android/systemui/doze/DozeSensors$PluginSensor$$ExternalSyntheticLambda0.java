package com.android.systemui.doze;

import com.android.systemui.doze.DozeSensors;
import com.android.systemui.plugins.SensorManagerPlugin;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DozeSensors$PluginSensor$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DozeSensors.PluginSensor f$0;
    public final /* synthetic */ SensorManagerPlugin.SensorEvent f$1;

    public /* synthetic */ DozeSensors$PluginSensor$$ExternalSyntheticLambda0(DozeSensors.PluginSensor pluginSensor, SensorManagerPlugin.SensorEvent sensorEvent) {
        this.f$0 = pluginSensor;
        this.f$1 = sensorEvent;
    }

    public final void run() {
        this.f$0.mo32454x4d4cfd3f(this.f$1);
    }
}
