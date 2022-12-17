package com.nothing.p006ui.support.utils;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import com.sysaac.haptic.AACHapticUtils;
import java.io.File;

/* renamed from: com.nothing.ui.support.utils.NtUIVibrateUtils */
public class NtUIVibrateUtils {
    private static NtUIVibrateUtils sInstance;
    private AACHapticUtils mAACHapticUtils;
    private Context mContext;
    private boolean mSupportRichTap;

    public static NtUIVibrateUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NtUIVibrateUtils(context);
        }
        return sInstance;
    }

    private NtUIVibrateUtils(Context context) {
        this.mContext = context;
        AACHapticUtils instance = AACHapticUtils.getInstance();
        this.mAACHapticUtils = instance;
        this.mSupportRichTap = instance.isSupportedRichTap();
        instance.init(this.mContext);
    }

    public void playSwitchVibrate() {
        if (Settings.System.getInt(this.mContext.getContentResolver(), "haptic_feedback_enabled", 0) == 0) {
            Log.d("NtUIVibrateUtils", "playSwitchVibrate: touch vibrate settings is off !");
            return;
        }
        this.mAACHapticUtils.playPattern(new File("/system/etc/richtapresources/NT_OnOff_button.he"), 1);
        Log.d("NtUIVibrateUtils", "playSwitchVibrate");
    }
}
