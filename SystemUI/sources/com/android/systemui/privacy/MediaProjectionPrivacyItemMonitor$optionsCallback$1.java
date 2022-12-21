package com.android.systemui.privacy;

import com.android.systemui.privacy.PrivacyConfig;
import kotlin.Metadata;
import kotlin.Unit;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/privacy/MediaProjectionPrivacyItemMonitor$optionsCallback$1", "Lcom/android/systemui/privacy/PrivacyConfig$Callback;", "onFlagMediaProjectionChanged", "", "flag", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaProjectionPrivacyItemMonitor.kt */
public final class MediaProjectionPrivacyItemMonitor$optionsCallback$1 implements PrivacyConfig.Callback {
    final /* synthetic */ MediaProjectionPrivacyItemMonitor this$0;

    MediaProjectionPrivacyItemMonitor$optionsCallback$1(MediaProjectionPrivacyItemMonitor mediaProjectionPrivacyItemMonitor) {
        this.this$0 = mediaProjectionPrivacyItemMonitor;
    }

    public void onFlagMediaProjectionChanged(boolean z) {
        Object access$getLock$p = this.this$0.lock;
        MediaProjectionPrivacyItemMonitor mediaProjectionPrivacyItemMonitor = this.this$0;
        synchronized (access$getLock$p) {
            mediaProjectionPrivacyItemMonitor.mediaProjectionAvailable = mediaProjectionPrivacyItemMonitor.privacyConfig.getMediaProjectionAvailable();
            mediaProjectionPrivacyItemMonitor.setListeningStateLocked();
            Unit unit = Unit.INSTANCE;
        }
        this.this$0.dispatchOnPrivacyItemsChanged();
    }
}
