package com.nothingos.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.CallbackController;
/* loaded from: classes2.dex */
public interface NfcController extends CallbackController<CallBack> {

    /* loaded from: classes2.dex */
    public interface CallBack {
        void onBatteryWirelessChange();
    }

    default boolean isWirelessCharging() {
        return false;
    }
}
