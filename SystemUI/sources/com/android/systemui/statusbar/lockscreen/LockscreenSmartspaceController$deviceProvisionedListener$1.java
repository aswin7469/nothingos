package com.android.systemui.statusbar.lockscreen;

import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0016¨\u0006\u0005"}, mo65043d2 = {"com/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$deviceProvisionedListener$1", "Lcom/android/systemui/statusbar/policy/DeviceProvisionedController$DeviceProvisionedListener;", "onDeviceProvisionedChanged", "", "onUserSetupChanged", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LockscreenSmartspaceController.kt */
public final class LockscreenSmartspaceController$deviceProvisionedListener$1 implements DeviceProvisionedController.DeviceProvisionedListener {
    final /* synthetic */ LockscreenSmartspaceController this$0;

    LockscreenSmartspaceController$deviceProvisionedListener$1(LockscreenSmartspaceController lockscreenSmartspaceController) {
        this.this$0 = lockscreenSmartspaceController;
    }

    public void onDeviceProvisionedChanged() {
        this.this$0.connectSession();
    }

    public void onUserSetupChanged() {
        this.this$0.connectSession();
    }
}
