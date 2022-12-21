package com.nothing.systemui.statusbar.connectivity;

import com.android.settingslib.SignalIcon;
import com.android.systemui.statusbar.connectivity.WifiState;

public class WifiSignalControllerEx {
    public int getCurrentIconId(WifiState wifiState, SignalIcon.IconGroup iconGroup, SignalIcon.IconGroup iconGroup2) {
        int i = wifiState.wifiStandard;
        if (i == 4 || i == 5 || i == 6) {
            iconGroup = iconGroup2;
        }
        if (wifiState.connected) {
            return iconGroup.sbIcons[wifiState.inetCondition][wifiState.level];
        }
        if (wifiState.enabled) {
            return iconGroup.sbDiscState;
        }
        return iconGroup.sbNullState;
    }
}
