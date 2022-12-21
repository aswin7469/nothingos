package com.android.systemui.biometrics;

import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0016¨\u0006\u0005"}, mo64987d2 = {"com/android/systemui/biometrics/AuthRippleController$configurationChangedListener$1", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "onThemeChanged", "", "onUiModeChanged", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AuthRippleController.kt */
public final class AuthRippleController$configurationChangedListener$1 implements ConfigurationController.ConfigurationListener {
    final /* synthetic */ AuthRippleController this$0;

    AuthRippleController$configurationChangedListener$1(AuthRippleController authRippleController) {
        this.this$0 = authRippleController;
    }

    public void onUiModeChanged() {
        this.this$0.updateRippleColor();
    }

    public void onThemeChanged() {
        this.this$0.updateRippleColor();
    }
}
