package com.nothing.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.CallbackController;

public interface BatteryShareController extends CallbackController<CallBack> {

    public interface CallBack {
        void onBatteryShareChange();
    }

    boolean getBatteryShareEnabled();

    boolean isWirelessCharging() {
        return false;
    }

    void setBatteryShareEnable();
}
