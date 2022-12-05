package com.nothingos.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.CallbackController;
/* loaded from: classes2.dex */
public interface BatteryShareController extends CallbackController<CallBack> {

    /* loaded from: classes2.dex */
    public interface CallBack {
        void onBatteryShareChange();
    }

    boolean getBatteryShareEnabled();

    default boolean isWirelessCharging() {
        return false;
    }

    void setBatteryShareEnable();
}
