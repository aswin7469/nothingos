package com.nothing.systemui.statusbar.policy;

import com.android.systemui.Dumpable;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.statusbar.policy.CallbackController;

public interface TeslaInfoController extends CallbackController<Callback>, Dumpable {

    public interface Callback {
        void onActiveStateChanged();

        void onInfoChanged();
    }

    String getBatteryRange();

    QSTile.Icon getProfile();

    String getUserName();

    boolean isConnected();

    boolean shouldShowTeslaInfo();
}
