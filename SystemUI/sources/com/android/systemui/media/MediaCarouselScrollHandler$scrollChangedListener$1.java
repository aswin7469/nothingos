package com.android.systemui.media;

import android.view.View;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J2\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00072\u0006\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u0007H\u0016¨\u0006\u000b"}, mo65043d2 = {"com/android/systemui/media/MediaCarouselScrollHandler$scrollChangedListener$1", "Landroid/view/View$OnScrollChangeListener;", "onScrollChange", "", "v", "Landroid/view/View;", "scrollX", "", "scrollY", "oldScrollX", "oldScrollY", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaCarouselScrollHandler.kt */
public final class MediaCarouselScrollHandler$scrollChangedListener$1 implements View.OnScrollChangeListener {
    final /* synthetic */ MediaCarouselScrollHandler this$0;

    MediaCarouselScrollHandler$scrollChangedListener$1(MediaCarouselScrollHandler mediaCarouselScrollHandler) {
        this.this$0 = mediaCarouselScrollHandler;
    }

    public void onScrollChange(View view, int i, int i2, int i3, int i4) {
        if (this.this$0.getPlayerWidthPlusPadding() != 0) {
            int relativeScrollX = this.this$0.scrollView.getRelativeScrollX();
            MediaCarouselScrollHandler mediaCarouselScrollHandler = this.this$0;
            mediaCarouselScrollHandler.onMediaScrollingChanged(relativeScrollX / mediaCarouselScrollHandler.getPlayerWidthPlusPadding(), relativeScrollX % this.this$0.getPlayerWidthPlusPadding());
        }
    }
}
