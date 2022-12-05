package com.android.systemui.statusbar.policy;

import com.android.systemui.Dumpable;
/* loaded from: classes2.dex */
public interface HotspotController extends CallbackController<Callback>, Dumpable {
    int getNumConnectedDevices();

    boolean isHotspotEnabled();

    boolean isHotspotSupported();

    boolean isHotspotTransient();

    /* loaded from: classes2.dex */
    public interface Callback {
        default void onHotspotAvailabilityChanged(boolean z) {
        }

        void onHotspotChanged(boolean z, int i);

        default void onHotspotChanged(boolean z, int i, int i2) {
            onHotspotChanged(z, i);
        }
    }
}
