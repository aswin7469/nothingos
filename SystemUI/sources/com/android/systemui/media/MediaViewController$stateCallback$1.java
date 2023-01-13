package com.android.systemui.media;

import com.android.systemui.media.MediaHostStatesManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, mo65043d2 = {"com/android/systemui/media/MediaViewController$stateCallback$1", "Lcom/android/systemui/media/MediaHostStatesManager$Callback;", "onHostStateChanged", "", "location", "", "mediaHostState", "Lcom/android/systemui/media/MediaHostState;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaViewController.kt */
public final class MediaViewController$stateCallback$1 implements MediaHostStatesManager.Callback {
    final /* synthetic */ MediaViewController this$0;

    MediaViewController$stateCallback$1(MediaViewController mediaViewController) {
        this.this$0 = mediaViewController;
    }

    public void onHostStateChanged(int i, MediaHostState mediaHostState) {
        Intrinsics.checkNotNullParameter(mediaHostState, "mediaHostState");
        if (i == this.this$0.getCurrentEndLocation() || i == this.this$0.currentStartLocation) {
            MediaViewController mediaViewController = this.this$0;
            mediaViewController.setCurrentState(mediaViewController.currentStartLocation, this.this$0.getCurrentEndLocation(), this.this$0.currentTransitionProgress, false);
        }
    }
}
