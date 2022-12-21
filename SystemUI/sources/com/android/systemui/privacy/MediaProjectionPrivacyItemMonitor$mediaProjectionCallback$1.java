package com.android.systemui.privacy;

import android.media.projection.MediaProjectionInfo;
import android.media.projection.MediaProjectionManager;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0017J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0017¨\u0006\u0007"}, mo64987d2 = {"com/android/systemui/privacy/MediaProjectionPrivacyItemMonitor$mediaProjectionCallback$1", "Landroid/media/projection/MediaProjectionManager$Callback;", "onStart", "", "info", "Landroid/media/projection/MediaProjectionInfo;", "onStop", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaProjectionPrivacyItemMonitor.kt */
public final class MediaProjectionPrivacyItemMonitor$mediaProjectionCallback$1 extends MediaProjectionManager.Callback {
    final /* synthetic */ MediaProjectionPrivacyItemMonitor this$0;

    MediaProjectionPrivacyItemMonitor$mediaProjectionCallback$1(MediaProjectionPrivacyItemMonitor mediaProjectionPrivacyItemMonitor) {
        this.this$0 = mediaProjectionPrivacyItemMonitor;
    }

    public void onStart(MediaProjectionInfo mediaProjectionInfo) {
        Intrinsics.checkNotNullParameter(mediaProjectionInfo, "info");
        Object access$getLock$p = this.this$0.lock;
        MediaProjectionPrivacyItemMonitor mediaProjectionPrivacyItemMonitor = this.this$0;
        synchronized (access$getLock$p) {
            mediaProjectionPrivacyItemMonitor.onMediaProjectionStartedLocked(mediaProjectionInfo);
            Unit unit = Unit.INSTANCE;
        }
        this.this$0.dispatchOnPrivacyItemsChanged();
    }

    public void onStop(MediaProjectionInfo mediaProjectionInfo) {
        Intrinsics.checkNotNullParameter(mediaProjectionInfo, "info");
        Object access$getLock$p = this.this$0.lock;
        MediaProjectionPrivacyItemMonitor mediaProjectionPrivacyItemMonitor = this.this$0;
        synchronized (access$getLock$p) {
            mediaProjectionPrivacyItemMonitor.onMediaProjectionStoppedLocked(mediaProjectionInfo);
            Unit unit = Unit.INSTANCE;
        }
        this.this$0.dispatchOnPrivacyItemsChanged();
    }
}
