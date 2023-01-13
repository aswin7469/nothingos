package com.android.systemui.media;

import android.view.GestureDetector;
import android.view.MotionEvent;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0007*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J,\u0010\u0006\u001a\u00020\u00032\b\u0010\u0007\u001a\u0004\u0018\u00010\u00052\b\u0010\b\u001a\u0004\u0018\u00010\u00052\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0016J,\u0010\f\u001a\u00020\u00032\b\u0010\r\u001a\u0004\u0018\u00010\u00052\b\u0010\u000e\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\nH\u0016¨\u0006\u0011"}, mo65043d2 = {"com/android/systemui/media/MediaCarouselScrollHandler$gestureListener$1", "Landroid/view/GestureDetector$SimpleOnGestureListener;", "onDown", "", "e", "Landroid/view/MotionEvent;", "onFling", "eStart", "eCurrent", "vX", "", "vY", "onScroll", "down", "lastMotion", "distanceX", "distanceY", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaCarouselScrollHandler.kt */
public final class MediaCarouselScrollHandler$gestureListener$1 extends GestureDetector.SimpleOnGestureListener {
    final /* synthetic */ MediaCarouselScrollHandler this$0;

    MediaCarouselScrollHandler$gestureListener$1(MediaCarouselScrollHandler mediaCarouselScrollHandler) {
        this.this$0 = mediaCarouselScrollHandler;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return this.this$0.onFling(f, f2);
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        MediaCarouselScrollHandler mediaCarouselScrollHandler = this.this$0;
        Intrinsics.checkNotNull(motionEvent);
        Intrinsics.checkNotNull(motionEvent2);
        return mediaCarouselScrollHandler.onScroll(motionEvent, motionEvent2, f);
    }

    public boolean onDown(MotionEvent motionEvent) {
        if (!this.this$0.getFalsingProtectionNeeded()) {
            return false;
        }
        this.this$0.falsingCollector.onNotificationStartDismissing();
        return false;
    }
}
