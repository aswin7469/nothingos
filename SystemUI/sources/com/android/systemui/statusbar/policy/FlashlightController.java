package com.android.systemui.statusbar.policy;

import com.android.systemui.Dumpable;
/* loaded from: classes2.dex */
public interface FlashlightController extends CallbackController<FlashlightListener>, Dumpable {

    /* loaded from: classes2.dex */
    public interface FlashlightListener {
        void onFlashlightAvailabilityChanged(boolean z);

        void onFlashlightChanged(boolean z);

        void onFlashlightError();
    }

    boolean hasFlashlight();

    boolean isAvailable();

    boolean isEnabled();

    void setFlashlight(boolean z);
}
