package com.android.systemui.statusbar.phone;

import android.util.Log;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.plugins.ActivityStarter;

@SysUISingleton
public class KeyguardDismissUtil implements KeyguardDismissHandler {
    private static final String TAG = "KeyguardDismissUtil";
    private volatile KeyguardDismissHandler mDismissHandler;

    public void setDismissHandler(KeyguardDismissHandler keyguardDismissHandler) {
        this.mDismissHandler = keyguardDismissHandler;
    }

    public void executeWhenUnlocked(ActivityStarter.OnDismissAction onDismissAction, boolean z, boolean z2) {
        KeyguardDismissHandler keyguardDismissHandler = this.mDismissHandler;
        if (keyguardDismissHandler == null) {
            Log.wtf(TAG, "KeyguardDismissHandler not set.");
            onDismissAction.onDismiss();
            return;
        }
        keyguardDismissHandler.executeWhenUnlocked(onDismissAction, z, z2);
    }
}
