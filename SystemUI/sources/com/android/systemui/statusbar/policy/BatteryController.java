package com.android.systemui.statusbar.policy;

import com.android.systemui.Dumpable;
import com.android.systemui.demomode.DemoMode;
/* loaded from: classes2.dex */
public interface BatteryController extends DemoMode, Dumpable, CallbackController<BatteryStateChangeCallback> {

    /* loaded from: classes2.dex */
    public interface BatteryStateChangeCallback {
        default void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        }

        default void onBatteryUnknownStateChanged(boolean z) {
        }

        default void onPowerSaveChanged(boolean z) {
        }

        default void onWirelessChargingChanged(boolean z) {
        }
    }

    /* loaded from: classes2.dex */
    public interface BatteryStateChangeCallbackExt extends BatteryStateChangeCallback {
        default void onBatteryLevelChangedExt(int i, boolean z, boolean z2, BatteryStateExt batteryStateExt) {
        }
    }

    /* loaded from: classes2.dex */
    public interface EstimateFetchCompletion {
        void onBatteryRemainingEstimateRetrieved(String str);
    }

    default void getEstimatedTimeRemainingString(EstimateFetchCompletion estimateFetchCompletion) {
    }

    default void init() {
    }

    boolean isAodPowerSave();

    boolean isPluggedIn();

    default boolean isPluggedInWireless() {
        return false;
    }

    boolean isPowerSave();

    default boolean isWirelessCharging() {
        return false;
    }

    void setPowerSaveMode(boolean z);

    /* loaded from: classes2.dex */
    public static class BatteryStateExt {
        public int status;
        public int temperature;
        public int voltage;

        public void copy(BatteryStateExt batteryStateExt) {
            this.status = batteryStateExt.status;
            this.temperature = batteryStateExt.temperature;
            this.voltage = batteryStateExt.voltage;
        }

        public String toString() {
            return "BatteryStateExt{status=" + this.status + ", temperature=" + this.temperature + ", voltage=" + this.voltage + '}';
        }
    }
}
