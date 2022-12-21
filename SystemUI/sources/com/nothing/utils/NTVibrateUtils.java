package com.nothing.utils;

import android.content.Context;
import android.util.Log;
import com.sysaac.haptic.AACHapticUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.p026io.File;

public class NTVibrateUtils {
    private static final String TAG = "NTVibrateUtils";
    private static NTVibrateUtils sInstance;
    private AACHapticUtils mAACHapticUtils;
    private Context mContext;
    private boolean mSupportRichTap = this.mAACHapticUtils.isSupportedRichTap();

    @Retention(RetentionPolicy.SOURCE)
    public @interface VibrateType {
        public static final String FINGERPRINT_VERIFICATION_ERROR = "/system/etc/richtapresources/NT_unlock_error.he";
        public static final String FINGERPRINT_VERIFICATION_SUCCEEDED = "/system/etc/richtapresources/NT_unlock_successful.he";
    }

    public static NTVibrateUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NTVibrateUtils(context);
        }
        return sInstance;
    }

    public NTVibrateUtils(Context context) {
        this.mContext = context.getApplicationContext();
        AACHapticUtils instance = AACHapticUtils.getInstance();
        this.mAACHapticUtils = instance;
        instance.init(this.mContext);
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
        Log.d(TAG, "playVerityVibrate");
        return true;
    }
}
