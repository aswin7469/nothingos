package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0005H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/statusbar/notification/interruption/KeyguardNotificationVisibilityProviderImpl$start$3", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController$StateListener;", "onStateChanged", "", "newState", "", "onUpcomingStateChanged", "state", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: KeyguardNotificationVisibilityProvider.kt */
public final class KeyguardNotificationVisibilityProviderImpl$start$3 implements StatusBarStateController.StateListener {
    final /* synthetic */ KeyguardNotificationVisibilityProviderImpl this$0;

    KeyguardNotificationVisibilityProviderImpl$start$3(KeyguardNotificationVisibilityProviderImpl keyguardNotificationVisibilityProviderImpl) {
        this.this$0 = keyguardNotificationVisibilityProviderImpl;
    }

    public void onStateChanged(int i) {
        this.this$0.notifyStateChanged("onStatusBarStateChanged");
    }

    public void onUpcomingStateChanged(int i) {
        this.this$0.notifyStateChanged("onStatusBarUpcomingStateChanged");
    }
}
