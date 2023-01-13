package com.android.systemui.statusbar.charging;

import android.content.res.Configuration;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0003H\u0016J\b\u0010\u0007\u001a\u00020\u0003H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/statusbar/charging/WiredChargingRippleController$registerCallbacks$configurationChangedListener$1", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "onConfigChanged", "", "newConfig", "Landroid/content/res/Configuration;", "onThemeChanged", "onUiModeChanged", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.charging.WiredChargingRippleController$registerCallbacks$configurationChangedListener$1 */
/* compiled from: WiredChargingRippleController.kt */
public final class C2611xe97593c0 implements ConfigurationController.ConfigurationListener {
    final /* synthetic */ WiredChargingRippleController this$0;

    C2611xe97593c0(WiredChargingRippleController wiredChargingRippleController) {
        this.this$0 = wiredChargingRippleController;
    }

    public void onUiModeChanged() {
        this.this$0.updateRippleColor();
    }

    public void onThemeChanged() {
        this.this$0.updateRippleColor();
    }

    public void onConfigChanged(Configuration configuration) {
        WiredChargingRippleController wiredChargingRippleController = this.this$0;
        wiredChargingRippleController.normalizedPortPosX = wiredChargingRippleController.context.getResources().getFloat(C1894R.dimen.physical_charger_port_location_normalized_x);
        WiredChargingRippleController wiredChargingRippleController2 = this.this$0;
        wiredChargingRippleController2.normalizedPortPosY = wiredChargingRippleController2.context.getResources().getFloat(C1894R.dimen.physical_charger_port_location_normalized_y);
    }
}
