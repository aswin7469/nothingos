package com.android.systemui.privacy;

import com.android.systemui.privacy.PrivacyConfig;
import com.android.systemui.privacy.PrivacyItemController;
import java.lang.ref.WeakReference;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/privacy/PrivacyItemController$optionsCallback$1", "Lcom/android/systemui/privacy/PrivacyConfig$Callback;", "onFlagLocationChanged", "", "flag", "", "onFlagMediaProjectionChanged", "onFlagMicCameraChanged", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PrivacyItemController.kt */
public final class PrivacyItemController$optionsCallback$1 implements PrivacyConfig.Callback {
    final /* synthetic */ PrivacyItemController this$0;

    PrivacyItemController$optionsCallback$1(PrivacyItemController privacyItemController) {
        this.this$0 = privacyItemController;
    }

    public void onFlagLocationChanged(boolean z) {
        for (WeakReference weakReference : this.this$0.callbacks) {
            PrivacyItemController.Callback callback = (PrivacyItemController.Callback) weakReference.get();
            if (callback != null) {
                callback.onFlagLocationChanged(z);
            }
        }
    }

    public void onFlagMicCameraChanged(boolean z) {
        for (WeakReference weakReference : this.this$0.callbacks) {
            PrivacyItemController.Callback callback = (PrivacyItemController.Callback) weakReference.get();
            if (callback != null) {
                callback.onFlagMicCameraChanged(z);
            }
        }
    }

    public void onFlagMediaProjectionChanged(boolean z) {
        for (WeakReference weakReference : this.this$0.callbacks) {
            PrivacyItemController.Callback callback = (PrivacyItemController.Callback) weakReference.get();
            if (callback != null) {
                callback.onFlagMediaProjectionChanged(z);
            }
        }
    }
}
