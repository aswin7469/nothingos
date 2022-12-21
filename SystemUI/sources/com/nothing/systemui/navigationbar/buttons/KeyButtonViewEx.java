package com.nothing.systemui.navigationbar.buttons;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Toast;
import com.android.systemui.C1893R;
import com.nothing.gamemode.NTGameModeHelper;
import com.nothing.systemui.NTDependencyEx;

public class KeyButtonViewEx {
    private static final long MISTOUCH_PREVENTION_RESET_DURATION = 2000;
    private static final String TAG = "KeyButtonViewEx";
    private long mBackInterceptTime;
    private NTGameModeHelper mGameModeHelper = ((NTGameModeHelper) NTDependencyEx.get(NTGameModeHelper.class));
    private long mHomeInterceptTime;
    private Toast mToast;

    public boolean shouldInterceptBackKey(Context context) {
        if (!this.mGameModeHelper.isMistouchPreventEnabled() || SystemClock.uptimeMillis() - this.mBackInterceptTime < 2000) {
            hideToast(context);
            return false;
        }
        this.mBackInterceptTime = SystemClock.uptimeMillis();
        showToast(context);
        return true;
    }

    public boolean shouldInterceptHomeKey(Context context) {
        if (!this.mGameModeHelper.isMistouchPreventEnabled() || SystemClock.uptimeMillis() - this.mHomeInterceptTime < 2000) {
            hideToast(context);
            return false;
        }
        this.mHomeInterceptTime = SystemClock.uptimeMillis();
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
