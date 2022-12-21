package com.android.systemui.statusbar.charging;

import com.android.systemui.statusbar.policy.BatteryController;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0016¨\u0006\t"}, mo64987d2 = {"com/android/systemui/statusbar/charging/WiredChargingRippleController$registerCallbacks$batteryStateChangeCallback$1", "Lcom/android/systemui/statusbar/policy/BatteryController$BatteryStateChangeCallback;", "onBatteryLevelChanged", "", "level", "", "nowPluggedIn", "", "charging", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.statusbar.charging.WiredChargingRippleController$registerCallbacks$batteryStateChangeCallback$1 */
/* compiled from: WiredChargingRippleController.kt */
public final class C2604x82710e47 implements BatteryController.BatteryStateChangeCallback {
    final /* synthetic */ WiredChargingRippleController this$0;

    C2604x82710e47(WiredChargingRippleController wiredChargingRippleController) {
        this.this$0 = wiredChargingRippleController;
    }

    public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        if (!this.this$0.batteryController.isPluggedInWireless()) {
            Boolean access$getPluggedIn$p = this.this$0.pluggedIn;
            this.this$0.pluggedIn = Boolean.valueOf(z);
            if ((access$getPluggedIn$p == null || !access$getPluggedIn$p.booleanValue()) && z) {
                this.this$0.startRippleWithDebounce$SystemUI_nothingRelease();
            }
        }
    }
}
