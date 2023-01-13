package com.android.systemui.controls.controller;

import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import kotlin.Metadata;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\b\u0010\u0005\u001a\u00020\u0004H\u0016¨\u0006\u0006"}, mo65043d2 = {"Lcom/android/systemui/controls/controller/ControlsTileResourceConfigurationImpl;", "Lcom/android/systemui/controls/controller/ControlsTileResourceConfiguration;", "()V", "getTileImageId", "", "getTileTitleId", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsTileResourceConfigurationImpl.kt */
public final class ControlsTileResourceConfigurationImpl implements ControlsTileResourceConfiguration {
    public int getTileImageId() {
        return C1894R.C1896drawable.controls_icon;
    }

    public int getTileTitleId() {
        return C1894R.string.quick_controls_title;
    }
}
