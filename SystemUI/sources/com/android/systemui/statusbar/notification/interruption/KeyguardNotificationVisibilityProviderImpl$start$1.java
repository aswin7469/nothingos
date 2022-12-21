package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.statusbar.policy.KeyguardStateController;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0016¨\u0006\u0005"}, mo64987d2 = {"com/android/systemui/statusbar/notification/interruption/KeyguardNotificationVisibilityProviderImpl$start$1", "Lcom/android/systemui/statusbar/policy/KeyguardStateController$Callback;", "onKeyguardShowingChanged", "", "onUnlockedChanged", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: KeyguardNotificationVisibilityProvider.kt */
public final class KeyguardNotificationVisibilityProviderImpl$start$1 implements KeyguardStateController.Callback {
    final /* synthetic */ KeyguardNotificationVisibilityProviderImpl this$0;

    KeyguardNotificationVisibilityProviderImpl$start$1(KeyguardNotificationVisibilityProviderImpl keyguardNotificationVisibilityProviderImpl) {
        this.this$0 = keyguardNotificationVisibilityProviderImpl;
    }

    public void onUnlockedChanged() {
        this.this$0.notifyStateChanged("onUnlockedChanged");
    }

    public void onKeyguardShowingChanged() {
        this.this$0.notifyStateChanged("onKeyguardShowingChanged");
    }
}
