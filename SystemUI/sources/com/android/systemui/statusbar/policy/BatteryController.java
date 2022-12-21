package com.android.systemui.statusbar.policy;

import android.view.View;
import com.android.systemui.Dumpable;
import com.android.systemui.demomode.DemoMode;
import java.lang.ref.WeakReference;
import java.p026io.PrintWriter;

public interface BatteryController extends DemoMode, Dumpable, CallbackController<BatteryStateChangeCallback> {

    public interface BatteryStateChangeCallback {
        void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        }

        void onBatteryUnknownStateChanged(boolean z) {
        }

        void onExtremeBatterySaverChanged(boolean z) {
        }

        void onPowerSaveChanged(boolean z) {
        }

        void onReverseChanged(boolean z, int i, String str) {
        }

        void onWirelessChargingChanged(boolean z) {
        }
    }

    public interface EstimateFetchCompletion {
        void onBatteryRemainingEstimateRetrieved(String str);
    }

    void clearLastPowerSaverStartView() {
    }

    void dump(PrintWriter printWriter, String[] strArr);

    void getEstimatedTimeRemainingString(EstimateFetchCompletion estimateFetchCompletion) {
    }

    WeakReference<View> getLastPowerSaverStartView() {
        return null;
    }

    void init() {
    }

    boolean isAodPowerSave();

    boolean isExtremeSaverOn() {
        return false;
    }

    boolean isPluggedIn();

    boolean isPluggedInWireless() {
        return false;
    }

    boolean isPowerSave();

    boolean isReverseOn() {
        return false;
    }

    boolean isReverseSupported() {
        return false;
    }

    boolean isWirelessCharging() {
        return false;
    }

    void setPowerSaveMode(boolean z, View view);

    void setReverseState(boolean z) {
    }

    void setPowerSaveMode(boolean z) {
        setPowerSaveMode(z, (View) null);
    }
}
