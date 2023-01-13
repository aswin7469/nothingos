package com.android.systemui.media;

import com.android.systemui.media.MediaDataManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00005\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J:\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\nH\u0016J\u0010\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J \u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\nH\u0016J\u0018\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\nH\u0016¨\u0006\u0013"}, mo65043d2 = {"com/android/systemui/media/MediaHost$listener$1", "Lcom/android/systemui/media/MediaDataManager$Listener;", "onMediaDataLoaded", "", "key", "", "oldKey", "data", "Lcom/android/systemui/media/MediaData;", "immediately", "", "receivedSmartspaceCardLatency", "", "isSsReactivated", "onMediaDataRemoved", "onSmartspaceMediaDataLoaded", "Lcom/android/systemui/media/SmartspaceMediaData;", "shouldPrioritize", "onSmartspaceMediaDataRemoved", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaHost.kt */
public final class MediaHost$listener$1 implements MediaDataManager.Listener {
    final /* synthetic */ MediaHost this$0;

    MediaHost$listener$1(MediaHost mediaHost) {
        this.this$0 = mediaHost;
    }

    public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i, boolean z2) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData, "data");
        if (z) {
            this.this$0.updateViewVisibility();
        }
    }

    public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(smartspaceMediaData, "data");
        this.this$0.updateViewVisibility();
    }

    public void onMediaDataRemoved(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.this$0.updateViewVisibility();
    }

    public void onSmartspaceMediaDataRemoved(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        if (z) {
            this.this$0.updateViewVisibility();
        }
    }
}
