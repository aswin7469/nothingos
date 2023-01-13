package com.android.systemui.statusbar.phone;

import com.android.systemui.C1894R;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo65043d2 = {"com/android/systemui/statusbar/phone/LargeScreenShadeHeaderController$bindConfigurationListener$listener$1", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "onDensityOrFontScaleChanged", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.phone.LargeScreenShadeHeaderController$bindConfigurationListener$listener$1 */
/* compiled from: LargeScreenShadeHeaderController.kt */
public final class C3003x12adfc0d implements ConfigurationController.ConfigurationListener {
    final /* synthetic */ LargeScreenShadeHeaderController this$0;

    C3003x12adfc0d(LargeScreenShadeHeaderController largeScreenShadeHeaderController) {
        this.this$0 = largeScreenShadeHeaderController;
    }

    public void onDensityOrFontScaleChanged() {
        FontSizeUtils.updateFontSizeFromStyle(this.this$0.clock, C1894R.style.TextAppearance_QS_Status);
        FontSizeUtils.updateFontSizeFromStyle(this.this$0.date, C1894R.style.TextAppearance_QS_Status);
        this.this$0.qsCarrierGroup.updateTextAppearance(C1894R.style.TextAppearance_QS_Status);
    }
}
