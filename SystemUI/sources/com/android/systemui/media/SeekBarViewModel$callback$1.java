package com.android.systemui.media;

import android.media.session.MediaController;
import android.media.session.PlaybackState;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0003H\u0016¨\u0006\u0007"}, mo64987d2 = {"com/android/systemui/media/SeekBarViewModel$callback$1", "Landroid/media/session/MediaController$Callback;", "onPlaybackStateChanged", "", "state", "Landroid/media/session/PlaybackState;", "onSessionDestroyed", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SeekBarViewModel.kt */
public final class SeekBarViewModel$callback$1 extends MediaController.Callback {
    final /* synthetic */ SeekBarViewModel this$0;

    SeekBarViewModel$callback$1(SeekBarViewModel seekBarViewModel) {
        this.this$0 = seekBarViewModel;
    }

    public void onPlaybackStateChanged(PlaybackState playbackState) {
        this.this$0.playbackState = playbackState;
        if (this.this$0.playbackState != null) {
            Integer num = 0;
            if (!num.equals(this.this$0.playbackState)) {
                this.this$0.checkIfPollingNeeded();
                return;
            }
        }
        this.this$0.clearController();
    }

    public void onSessionDestroyed() {
        this.this$0.clearController();
    }
}
