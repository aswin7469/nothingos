package com.android.systemui.navigationbar;

import android.content.Context;
import android.os.SystemClock;
import android.util.Slog;
import android.widget.Toast;
import com.android.systemui.C1893R;
import com.android.systemui.SysUIToast;

public class ScreenPinningNotify {
    private static final long SHOW_TOAST_MINIMUM_INTERVAL = 1000;
    private static final String TAG = "ScreenPinningNotify";
    private final Context mContext;
    private long mLastShowToastTime;
    private Toast mLastToast;

    public ScreenPinningNotify(Context context) {
        this.mContext = context;
    }

    public void showPinningStartToast() {
        makeAllUserToastAndShow(C1893R.string.screen_pinning_start);
    }

    public void showPinningExitToast() {
        makeAllUserToastAndShow(C1893R.string.screen_pinning_exit);
    }

    public void showEscapeToast(boolean z, boolean z2) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (elapsedRealtime - this.mLastShowToastTime < 1000) {
            Slog.i(TAG, "Ignore toast since it is requested in very short interval.");
            return;
        }
        Toast toast = this.mLastToast;
        if (toast != null) {
            toast.cancel();
        }
        this.mLastToast = makeAllUserToastAndShow(z ? C1893R.string.screen_pinning_toast_gesture_nav : z2 ? C1893R.string.screen_pinning_toast : C1893R.string.screen_pinning_toast_recents_invisible);
        this.mLastShowToastTime = elapsedRealtime;
    }

    private Toast makeAllUserToastAndShow(int i) {
        Toast makeText = SysUIToast.makeText(this.mContext, i, 1);
        makeText.show();
        return makeText;
    }
}
