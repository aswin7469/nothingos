package com.nothing.systemui.util.sensors;

import com.android.systemui.util.sensors.ThresholdSensorEvent;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.statusbar.phone.CentralSurfacesImplEx;

public class ProximitySensorImplEx {
    public static void alertListeners(ThresholdSensorEvent thresholdSensorEvent) {
        CentralSurfacesImplEx centralSurfacesImplEx = (CentralSurfacesImplEx) NTDependencyEx.get(CentralSurfacesImplEx.class);
        if (centralSurfacesImplEx != null) {
            centralSurfacesImplEx.onThresholdCrossed(thresholdSensorEvent.getBelow());
        }
    }
}
