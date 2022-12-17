package com.nothing.settings.utils;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import com.sysaac.haptic.AACHapticUtils;
import java.io.File;

public class NtSettingsVibrateUtils {
    private static NtSettingsVibrateUtils sInstance;
    private AACHapticUtils mAACHapticUtils;
    private Context mContext;
    private boolean mSupportRichTap;

    public static NtSettingsVibrateUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NtSettingsVibrateUtils(context);
        }
        return sInstance;
    }

    private NtSettingsVibrateUtils(Context context) {
        this.mContext = context;
        AACHapticUtils instance = AACHapticUtils.getInstance();
        this.mAACHapticUtils = instance;
        this.mSupportRichTap = instance.isSupportedRichTap();
        instance.init(this.mContext);
    }

    public void playSwitchVibrate() {
        if (Settings.System.getInt(this.mContext.getContentResolver(), "haptic_feedback_enabled", 0) == 0) {
            Log.d("NtSettingsVibrateUtils", "playSwitchVibrate: touch vibrate settings is off !");
            return;
        }
        this.mAACHapticUtils.playPattern(new File("/system/etc/richtapresources/NT_OnOff_button.he"), 1);
        Log.d("NtSettingsVibrateUtils", "playSwitchVibrate");
    }
}
