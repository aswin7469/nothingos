package com.android.systemui.statusbar.phone;

import com.android.systemui.C1893R;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo64987d2 = {"com/android/systemui/statusbar/phone/LargeScreenShadeHeaderController$bindConfigurationListener$listener$1", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "onDensityOrFontScaleChanged", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.statusbar.phone.LargeScreenShadeHeaderController$bindConfigurationListener$listener$1 */
/* compiled from: LargeScreenShadeHeaderController.kt */
public final class C2993x12adfc0d implements ConfigurationController.ConfigurationListener {
    final /* synthetic */ LargeScreenShadeHeaderController this$0;

    C2993x12adfc0d(LargeScreenShadeHeaderController largeScreenShadeHeaderController) {
        this.this$0 = largeScreenShadeHeaderController;
    }

    public void onDensityOrFontScaleChanged() {
        FontSizeUtils.updateFontSizeFromStyle(this.this$0.clock, C1893R.style.TextAppearance_QS_Status);
        FontSizeUtils.updateFontSizeFromStyle(this.this$0.date, C1893R.style.TextAppearance_QS_Status);
        this.this$0.qsCarrierGroup.updateTextAppearance(C1893R.style.TextAppearance_QS_Status);
    }
}
