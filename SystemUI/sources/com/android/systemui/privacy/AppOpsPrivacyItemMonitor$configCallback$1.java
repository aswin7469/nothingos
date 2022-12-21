package com.android.systemui.privacy;

import com.android.systemui.privacy.PrivacyConfig;
import kotlin.Metadata;
import kotlin.Unit;

@Metadata(mo64986d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0002J\u0010\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/privacy/AppOpsPrivacyItemMonitor$configCallback$1", "Lcom/android/systemui/privacy/PrivacyConfig$Callback;", "onFlagChanged", "", "onFlagLocationChanged", "flag", "", "onFlagMicCameraChanged", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AppOpsPrivacyItemMonitor.kt */
public final class AppOpsPrivacyItemMonitor$configCallback$1 implements PrivacyConfig.Callback {
    final /* synthetic */ AppOpsPrivacyItemMonitor this$0;

    AppOpsPrivacyItemMonitor$configCallback$1(AppOpsPrivacyItemMonitor appOpsPrivacyItemMonitor) {
        this.this$0 = appOpsPrivacyItemMonitor;
    }

    public void onFlagLocationChanged(boolean z) {
        onFlagChanged();
    }

    public void onFlagMicCameraChanged(boolean z) {
        onFlagChanged();
    }

    private final void onFlagChanged() {
        Object access$getLock$p = this.this$0.lock;
        AppOpsPrivacyItemMonitor appOpsPrivacyItemMonitor = this.this$0;
        synchronized (access$getLock$p) {
            appOpsPrivacyItemMonitor.micCameraAvailable = appOpsPrivacyItemMonitor.privacyConfig.getMicCameraAvailable();
            appOpsPrivacyItemMonitor.locationAvailable = appOpsPrivacyItemMonitor.privacyConfig.getLocationAvailable();
            appOpsPrivacyItemMonitor.setListeningStateLocked();
            Unit unit = Unit.INSTANCE;
        }
        this.this$0.dispatchOnPrivacyItemsChanged();
    }
}
