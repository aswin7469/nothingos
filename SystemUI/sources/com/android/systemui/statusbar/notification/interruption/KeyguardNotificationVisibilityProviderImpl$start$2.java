package com.android.systemui.statusbar.notification.interruption;

import com.android.keyguard.KeyguardUpdateMonitorCallback;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/statusbar/notification/interruption/KeyguardNotificationVisibilityProviderImpl$start$2", "Lcom/android/keyguard/KeyguardUpdateMonitorCallback;", "onStrongAuthStateChanged", "", "userId", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: KeyguardNotificationVisibilityProvider.kt */
public final class KeyguardNotificationVisibilityProviderImpl$start$2 extends KeyguardUpdateMonitorCallback {
    final /* synthetic */ KeyguardNotificationVisibilityProviderImpl this$0;

    KeyguardNotificationVisibilityProviderImpl$start$2(KeyguardNotificationVisibilityProviderImpl keyguardNotificationVisibilityProviderImpl) {
        this.this$0 = keyguardNotificationVisibilityProviderImpl;
    }

    public void onStrongAuthStateChanged(int i) {
        this.this$0.notifyStateChanged("onStrongAuthStateChanged");
    }
}
