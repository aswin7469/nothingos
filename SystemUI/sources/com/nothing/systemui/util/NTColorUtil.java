package com.nothing.systemui.util;

import android.content.Context;
import android.os.Debug;
import androidx.core.view.ViewCompat;
import com.android.systemui.C1894R;

public class NTColorUtil {
    private static final String TAG = "NTColorUtil";
    private static int mScrimBehindTintColor = -1;

    public static int getScrimBehindTintColor(Context context) {
        if (context == null) {
            int i = mScrimBehindTintColor;
            return i == -1 ? ViewCompat.MEASURED_STATE_MASK : i;
        }
        if (mScrimBehindTintColor == -1) {
            mScrimBehindTintColor = context.getResources().getColor(C1894R.C1895color.qs_panel_bg_color);
        }
        return mScrimBehindTintColor;
    }

    public static int getNTDefaultTextColor(Context context) {
        if (context != null) {
            return context.getColor(C1894R.C1895color.nt_default_text_color);
        }
        NTLogUtil.m1690w(TAG, "context could not be null, stack: " + Debug.getCallers(3));
        return 0;
    }
}
