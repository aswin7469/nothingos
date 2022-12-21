package com.android.systemui.statusbar.phone;

import com.android.keyguard.KeyguardUpdateMonitorCallback;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0005H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/statusbar/phone/KeyguardLiftController$keyguardUpdateMonitorCallback$1", "Lcom/android/keyguard/KeyguardUpdateMonitorCallback;", "onKeyguardBouncerFullyShowingChanged", "", "bouncer", "", "onKeyguardVisibilityChanged", "showing", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: KeyguardLiftController.kt */
public final class KeyguardLiftController$keyguardUpdateMonitorCallback$1 extends KeyguardUpdateMonitorCallback {
    final /* synthetic */ KeyguardLiftController this$0;

    KeyguardLiftController$keyguardUpdateMonitorCallback$1(KeyguardLiftController keyguardLiftController) {
        this.this$0 = keyguardLiftController;
    }

    public void onKeyguardBouncerFullyShowingChanged(boolean z) {
        this.this$0.bouncerVisible = z;
        this.this$0.updateListeningState();
    }

    public void onKeyguardVisibilityChanged(boolean z) {
        this.this$0.updateListeningState();
    }
}
