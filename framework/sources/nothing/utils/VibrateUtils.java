package nothing.utils;

import android.content.Context;
import android.util.Log;
import com.sysaac.haptic.AACHapticUtils;
import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes4.dex */
public class VibrateUtils {
    private static final String TAG = "VibrateUtils";
    private static VibrateUtils sInstance;
    private AACHapticUtils mAACHapticUtils;
    private Context mContext;
    private boolean mSupportRichTap;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes4.dex */
    public @interface VibrateType {
        public static final String FINGERPRINT_VERIFICATION_ERROR = "/system/etc/richtapresources/NT_unlock_error.he";
        public static final String FINGERPRINT_VERIFICATION_SUCCEEDED = "/system/etc/richtapresources/NT_unlock_successful.he";
    }

    public static VibrateUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new VibrateUtils(context);
        }
        return sInstance;
    }

    public VibrateUtils(Context context) {
        this.mContext = context.getApplicationContext();
        AACHapticUtils aACHapticUtils = AACHapticUtils.getInstance();
        this.mAACHapticUtils = aACHapticUtils;
        aACHapticUtils.init(this.mContext);
        this.mSupportRichTap = this.mAACHapticUtils.isSupportedRichTap();
    }

    public boolean playVerityVibrate(String vibrateType) {
        if (!this.mSupportRichTap) {
            return false;
        }
        File file = new File(vibrateType);
        if (!file.exists()) {
            return false;
        }
        this.mAACHapticUtils.playPattern(file, 1);
        Log.d(TAG, "playVerityVibrate");
        return true;
    }
}
