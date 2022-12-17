package com.google.android.setupdesign.transition;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.Window;
import com.google.android.material.transition.platform.MaterialSharedAxis;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.util.BuildCompatUtils;
import com.google.android.setupdesign.R$anim;
import com.google.android.setupdesign.util.ThemeHelper;

public class TransitionHelper {
    static boolean isFinishCalled = false;
    static boolean isStartActivity = false;
    static boolean isStartActivityForResult = false;

    @TargetApi(21)
    public static void applyBackwardTransition(Activity activity, int i) {
        if (i == 2) {
            activity.overridePendingTransition(R$anim.sud_slide_back_in, R$anim.sud_slide_back_out);
        } else if (i == 3) {
            activity.overridePendingTransition(R$anim.sud_stay, 17432577);
        } else if (i == 1) {
            TypedArray obtainStyledAttributes = activity.obtainStyledAttributes(16973825, new int[]{16842938, 16842939});
            activity.overridePendingTransition(obtainStyledAttributes.getResourceId(0, 0), obtainStyledAttributes.getResourceId(1, 0));
            obtainStyledAttributes.recycle();
        } else if (i == 4) {
            activity.overridePendingTransition(R$anim.sud_pre_p_activity_close_enter, R$anim.sud_pre_p_activity_close_exit);
        } else if (i == -1) {
            activity.overridePendingTransition(0, 0);
        } else if (i == 5 && getConfigTransitionType(activity) == 1) {
            Window window = activity.getWindow();
            if (window != null) {
                window.setReenterTransition(new MaterialSharedAxis(0, false));
                window.setReturnTransition(new MaterialSharedAxis(0, false));
                return;
            }
            Log.w("TransitionHelper", "applyBackwardTransition: Invalid window=" + window);
        }
    }

    public static int getConfigTransitionType(Context context) {
        if (!BuildCompatUtils.isAtLeastS() || !ThemeHelper.shouldApplyExtendedPartnerConfig(context)) {
            return 0;
        }
        return PartnerConfigHelper.get(context).getInteger(context, PartnerConfig.CONFIG_TRANSITION_TYPE, 0);
    }
}
