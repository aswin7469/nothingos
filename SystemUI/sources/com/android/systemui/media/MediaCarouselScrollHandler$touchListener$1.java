package com.android.systemui.media;

import android.view.MotionEvent;
import com.android.systemui.Gefingerpoken;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0007\u001a\u0004\u0018\u00010\u0005H\u0016¨\u0006\b"}, mo64987d2 = {"com/android/systemui/media/MediaCarouselScrollHandler$touchListener$1", "Lcom/android/systemui/Gefingerpoken;", "onInterceptTouchEvent", "", "ev", "Landroid/view/MotionEvent;", "onTouchEvent", "motionEvent", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaCarouselScrollHandler.kt */
public final class MediaCarouselScrollHandler$touchListener$1 implements Gefingerpoken {
    final /* synthetic */ MediaCarouselScrollHandler this$0;

    MediaCarouselScrollHandler$touchListener$1(MediaCarouselScrollHandler mediaCarouselScrollHandler) {
        this.this$0 = mediaCarouselScrollHandler;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        MediaCarouselScrollHandler mediaCarouselScrollHandler = this.this$0;
        Intrinsics.checkNotNull(motionEvent);
        return mediaCarouselScrollHandler.onTouch(motionEvent);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        MediaCarouselScrollHandler mediaCarouselScrollHandler = this.this$0;
        Intrinsics.checkNotNull(motionEvent);
        return mediaCarouselScrollHandler.onInterceptTouch(motionEvent);
    }
}
