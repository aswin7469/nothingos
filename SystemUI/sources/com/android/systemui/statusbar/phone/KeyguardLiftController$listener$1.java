package com.android.systemui.statusbar.phone;

import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import com.android.keyguard.ActiveUnlockConfig;
import com.android.systemui.util.Assert;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/statusbar/phone/KeyguardLiftController$listener$1", "Landroid/hardware/TriggerEventListener;", "onTrigger", "", "event", "Landroid/hardware/TriggerEvent;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: KeyguardLiftController.kt */
public final class KeyguardLiftController$listener$1 extends TriggerEventListener {
    final /* synthetic */ KeyguardLiftController this$0;

    KeyguardLiftController$listener$1(KeyguardLiftController keyguardLiftController) {
        this.this$0 = keyguardLiftController;
    }

    public void onTrigger(TriggerEvent triggerEvent) {
        Assert.isMainThread();
        this.this$0.isListening = false;
        this.this$0.updateListeningState();
        this.this$0.keyguardUpdateMonitor.requestFaceAuth(true);
        this.this$0.keyguardUpdateMonitor.requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.WAKE, "KeyguardLiftController");
    }
}
