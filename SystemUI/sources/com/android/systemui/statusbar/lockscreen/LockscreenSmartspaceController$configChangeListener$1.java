package com.android.systemui.statusbar.lockscreen;

import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo65043d2 = {"com/android/systemui/statusbar/lockscreen/LockscreenSmartspaceController$configChangeListener$1", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "onThemeChanged", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LockscreenSmartspaceController.kt */
public final class LockscreenSmartspaceController$configChangeListener$1 implements ConfigurationController.ConfigurationListener {
    final /* synthetic */ LockscreenSmartspaceController this$0;

    LockscreenSmartspaceController$configChangeListener$1(LockscreenSmartspaceController lockscreenSmartspaceController) {
        this.this$0 = lockscreenSmartspaceController;
    }

    public void onThemeChanged() {
        this.this$0.execution.assertIsMainThread();
        this.this$0.updateTextColorFromWallpaper();
    }
}
