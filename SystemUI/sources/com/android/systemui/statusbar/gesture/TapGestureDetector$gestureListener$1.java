package com.android.systemui.statusbar.gesture;

import android.view.GestureDetector;
import android.view.MotionEvent;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/statusbar/gesture/TapGestureDetector$gestureListener$1", "Landroid/view/GestureDetector$SimpleOnGestureListener;", "onSingleTapUp", "", "e", "Landroid/view/MotionEvent;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: TapGestureDetector.kt */
public final class TapGestureDetector$gestureListener$1 extends GestureDetector.SimpleOnGestureListener {
    final /* synthetic */ TapGestureDetector this$0;

    TapGestureDetector$gestureListener$1(TapGestureDetector tapGestureDetector) {
        this.this$0 = tapGestureDetector;
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "e");
        this.this$0.onGestureDetected$SystemUI_nothingRelease(motionEvent);
        return true;
    }
}
