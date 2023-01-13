package com.android.systemui.privacy;

import com.android.systemui.privacy.PrivacyItemMonitor;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo65043d2 = {"com/android/systemui/privacy/PrivacyItemController$privacyItemMonitorCallback$1", "Lcom/android/systemui/privacy/PrivacyItemMonitor$Callback;", "onPrivacyItemsChanged", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PrivacyItemController.kt */
public final class PrivacyItemController$privacyItemMonitorCallback$1 implements PrivacyItemMonitor.Callback {
    final /* synthetic */ PrivacyItemController this$0;

    PrivacyItemController$privacyItemMonitorCallback$1(PrivacyItemController privacyItemController) {
        this.this$0 = privacyItemController;
    }

    public void onPrivacyItemsChanged() {
        this.this$0.update();
    }
}
