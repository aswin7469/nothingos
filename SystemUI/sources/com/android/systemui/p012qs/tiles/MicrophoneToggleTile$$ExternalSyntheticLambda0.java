package com.android.systemui.p012qs.tiles;

import android.provider.DeviceConfig;
import java.util.function.Supplier;

/* renamed from: com.android.systemui.qs.tiles.MicrophoneToggleTile$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MicrophoneToggleTile$$ExternalSyntheticLambda0 implements Supplier {
    public final Object get() {
        return Boolean.valueOf(DeviceConfig.getBoolean("privacy", "mic_toggle_enabled", true));
    }
}