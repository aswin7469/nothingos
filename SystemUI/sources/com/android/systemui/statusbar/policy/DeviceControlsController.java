package com.android.systemui.statusbar.policy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: DeviceControlsController.kt */
/* loaded from: classes2.dex */
public interface DeviceControlsController {

    /* compiled from: DeviceControlsController.kt */
    /* loaded from: classes2.dex */
    public interface Callback {
        void onControlsUpdate(@Nullable Integer num);
    }

    void removeCallback();

    void setCallback(@NotNull Callback callback);
}
