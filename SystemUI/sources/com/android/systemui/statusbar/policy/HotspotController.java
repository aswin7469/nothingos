package com.android.systemui.statusbar.policy;

import com.android.systemui.Dumpable;

public interface HotspotController extends CallbackController<Callback>, Dumpable {
    int getNumConnectedDevices();

    boolean isHotspotEnabled();

    boolean isHotspotSupported();

    boolean isHotspotTransient();

    void setHotspotEnabled(boolean z);

    public interface Callback {
        void onHotspotAvailabilityChanged(boolean z) {
        }

        void onHotspotChanged(boolean z, int i);

        void onHotspotChanged(boolean z, int i, int i2) {
            onHotspotChanged(z, i);
        }
    }
}
