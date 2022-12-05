package com.nt.settings.utils;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import com.sysaac.haptic.AACHapticUtils;
import java.io.File;
/* loaded from: classes2.dex */
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

    public NtSettingsVibrateUtils(Context context) {
        this.mContext = context;
        AACHapticUtils aACHapticUtils = AACHapticUtils.getInstance();
        this.mAACHapticUtils = aACHapticUtils;
        aACHapticUtils.init(this.mContext);
        this.mSupportRichTap = this.mAACHapticUtils.isSupportedRichTap();
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
