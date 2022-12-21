package com.android.systemui.controls.controller;

import com.android.systemui.C1893R;
import com.android.systemui.dagger.SysUISingleton;
import kotlin.Metadata;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\b\u0010\u0005\u001a\u00020\u0004H\u0016¨\u0006\u0006"}, mo64987d2 = {"Lcom/android/systemui/controls/controller/ControlsTileResourceConfigurationImpl;", "Lcom/android/systemui/controls/controller/ControlsTileResourceConfiguration;", "()V", "getTileImageId", "", "getTileTitleId", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsTileResourceConfigurationImpl.kt */
public final class ControlsTileResourceConfigurationImpl implements ControlsTileResourceConfiguration {
    public int getTileImageId() {
        return C1893R.C1895drawable.controls_icon;
    }

    public int getTileTitleId() {
        return C1893R.string.quick_controls_title;
    }
}
