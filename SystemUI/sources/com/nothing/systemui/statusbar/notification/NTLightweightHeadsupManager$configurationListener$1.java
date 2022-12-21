package com.nothing.systemui.statusbar.notification;

import android.content.res.Configuration;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0003H\u0016J\b\u0010\u0007\u001a\u00020\u0003H\u0016¨\u0006\b"}, mo64987d2 = {"com/nothing/systemui/statusbar/notification/NTLightweightHeadsupManager$configurationListener$1", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "onConfigChanged", "", "newConfig", "Landroid/content/res/Configuration;", "onThemeChanged", "onUiModeChanged", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NTLightweightHeadsupManager.kt */
public final class NTLightweightHeadsupManager$configurationListener$1 implements ConfigurationController.ConfigurationListener {
    final /* synthetic */ NTLightweightHeadsupManager this$0;

    public void onThemeChanged() {
    }

    public void onUiModeChanged() {
    }

    NTLightweightHeadsupManager$configurationListener$1(NTLightweightHeadsupManager nTLightweightHeadsupManager) {
        this.this$0 = nTLightweightHeadsupManager;
    }

    public void onConfigChanged(Configuration configuration) {
        Intrinsics.checkNotNullParameter(configuration, "newConfig");
        if (this.this$0.densityDpi == configuration.densityDpi) {
            if (this.this$0.fontScale == configuration.fontScale) {
                return;
            }
        }
        this.this$0.densityDpi = configuration.densityDpi;
        this.this$0.fontScale = configuration.fontScale;
        this.this$0.initViews();
    }
}
