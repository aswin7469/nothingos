package com.nothing.systemui.navigationbar;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Toast;
import com.android.systemui.C1893R;
import com.nothing.gamemode.NTGameModeHelper;
import com.nothing.systemui.NTDependencyEx;

public class NavigationBarEx {
    private static final long MISTOUCH_PREVENTION_RESET_DURATION = 2000;
    private static final String TAG = "NavigationBarEx";
    private NTGameModeHelper mGameModeHelper = ((NTGameModeHelper) NTDependencyEx.get(NTGameModeHelper.class));
    private long mRecentInterceptTime;
    private Toast mToast;

    public boolean isRegionSamplingAvailable(int i) {
        return (i == 4 || i == 3) ? false : true;
    }

    public boolean shouldInterceptRecentKey(Context context) {
        if (!this.mGameModeHelper.isMistouchPreventEnabled() || SystemClock.uptimeMillis() - this.mRecentInterceptTime < 2000) {
            hideToast(context);
            return false;
        }
        this.mRecentInterceptTime = SystemClock.uptimeMillis();
        showToast(context);
        return true;
    }

    public void showToast(Context context) {
        Toast makeText = Toast.makeText(context, C1893R.string.click_again, 0);
        this.mToast = makeText;
        makeText.show();
    }

    public void hideToast(Context context) {
        Toast toast = this.mToast;
        if (toast != null) {
            toast.cancel();
        }
    }
}
