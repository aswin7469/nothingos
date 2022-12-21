package com.android.systemui.p012qs.dagger;

import android.content.Context;
import android.hardware.display.ColorDisplayManager;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module
/* renamed from: com.android.systemui.qs.dagger.QSFlagsModule */
public interface QSFlagsModule {
    public static final String PM_LITE_ENABLED = "pm_lite";
    public static final String PM_LITE_SETTING = "sysui_pm_lite";
    public static final int PM_LITE_SETTING_DEFAULT = 1;
    public static final String RBC_AVAILABLE = "rbc_available";

    @SysUISingleton
    @Provides
    @Named("rbc_available")
    static boolean isReduceBrightColorsAvailable(Context context) {
        return ColorDisplayManager.isReduceBrightColorsAvailable(context);
    }

    @SysUISingleton
    @Provides
    @Named("pm_lite")
    static boolean isPMLiteEnabled(FeatureFlags featureFlags, GlobalSettings globalSettings) {
        if (!featureFlags.isEnabled(Flags.POWER_MENU_LITE) || globalSettings.getInt(PM_LITE_SETTING, 1) == 0) {
            return false;
        }
        return true;
    }
}
