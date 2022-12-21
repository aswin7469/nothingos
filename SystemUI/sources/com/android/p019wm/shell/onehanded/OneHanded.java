package com.android.p019wm.shell.onehanded;

import android.content.res.Configuration;
import android.os.SystemProperties;
import com.android.p019wm.shell.common.annotations.ExternalThread;

@ExternalThread
/* renamed from: com.android.wm.shell.onehanded.OneHanded */
public interface OneHanded {
    public static final boolean sIsSupportOneHandedMode = SystemProperties.getBoolean(OneHandedController.SUPPORT_ONE_HANDED_MODE, false);

    IOneHanded createExternalInterface() {
        return null;
    }

    boolean isOneHandedEnabled();

    boolean isSwipeToNotificationEnabled();

    void onConfigChanged(Configuration configuration);

    void onKeyguardVisibilityChanged(boolean z);

    void onUserSwitch(int i);

    void registerEventCallback(OneHandedEventCallback oneHandedEventCallback);

    void registerTransitionCallback(OneHandedTransitionCallback oneHandedTransitionCallback);

    void setLockedDisabled(boolean z, boolean z2);

    void startOneHanded();

    void stopOneHanded();

    void stopOneHanded(int i);
}
