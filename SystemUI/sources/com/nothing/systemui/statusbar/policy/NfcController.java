package com.nothing.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.CallbackController;

public interface NfcController extends CallbackController<CallBack> {
    public static final int NFC_REFRESH_DELAY = 1000;

    public interface CallBack {
        void onBatteryWirelessChange();
    }

    boolean isWirelessCharging() {
        return false;
    }
}
