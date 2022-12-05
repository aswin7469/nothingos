package com.nothingos.utils;

import android.content.Context;
import android.util.Log;
import com.sysaac.haptic.AACHapticUtils;
import java.io.File;
/* loaded from: classes2.dex */
public class NtVibrateUtils {
    private static NtVibrateUtils sInstance;
    private AACHapticUtils mAACHapticUtils;
    private Context mContext;
    private boolean mSupportRichTap;

    public static NtVibrateUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NtVibrateUtils(context);
        }
        return sInstance;
    }

    public NtVibrateUtils(Context context) {
        this.mContext = context.getApplicationContext();
        AACHapticUtils aACHapticUtils = AACHapticUtils.getInstance();
        this.mAACHapticUtils = aACHapticUtils;
        aACHapticUtils.init(this.mContext);
        this.mSupportRichTap = this.mAACHapticUtils.isSupportedRichTap();
    }

    public boolean playVerityVibrate(String str) {
        if (!this.mSupportRichTap) {
            return false;
        }
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        this.mAACHapticUtils.playPattern(file, 1);
        Log.d("NtVibrateUtils", "playVerityVibrate");
        return true;
    }
}
