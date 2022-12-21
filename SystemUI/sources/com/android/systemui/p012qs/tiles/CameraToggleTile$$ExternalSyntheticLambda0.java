package com.android.systemui.p012qs.tiles;

import android.provider.DeviceConfig;
import java.util.function.Supplier;

/* renamed from: com.android.systemui.qs.tiles.CameraToggleTile$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CameraToggleTile$$ExternalSyntheticLambda0 implements Supplier {
    public final Object get() {
        return Boolean.valueOf(DeviceConfig.getBoolean("privacy", "camera_toggle_enabled", true));
    }
}
