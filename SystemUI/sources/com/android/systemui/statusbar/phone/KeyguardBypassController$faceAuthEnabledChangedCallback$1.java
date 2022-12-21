package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.policy.KeyguardStateController;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo64987d2 = {"com/android/systemui/statusbar/phone/KeyguardBypassController$faceAuthEnabledChangedCallback$1", "Lcom/android/systemui/statusbar/policy/KeyguardStateController$Callback;", "onFaceAuthEnabledChanged", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: KeyguardBypassController.kt */
public final class KeyguardBypassController$faceAuthEnabledChangedCallback$1 implements KeyguardStateController.Callback {
    final /* synthetic */ KeyguardBypassController this$0;

    KeyguardBypassController$faceAuthEnabledChangedCallback$1(KeyguardBypassController keyguardBypassController) {
        this.this$0 = keyguardBypassController;
    }

    public void onFaceAuthEnabledChanged() {
        this.this$0.notifyListeners();
    }
}
