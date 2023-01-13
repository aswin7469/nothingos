package com.android.systemui.statusbar.gesture;

import android.content.Context;
import android.view.GestureDetector;
import android.view.InputEvent;
import android.view.MotionEvent;
import com.android.systemui.dagger.SysUISingleton;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000-\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001\b\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016J\r\u0010\u000e\u001a\u00020\u000bH\u0010¢\u0006\u0002\b\u000fJ\r\u0010\u0010\u001a\u00020\u000bH\u0010¢\u0006\u0002\b\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0004\n\u0002\u0010\t¨\u0006\u0012"}, mo65043d2 = {"Lcom/android/systemui/statusbar/gesture/TapGestureDetector;", "Lcom/android/systemui/statusbar/gesture/GenericGestureDetector;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "gestureDetector", "Landroid/view/GestureDetector;", "gestureListener", "com/android/systemui/statusbar/gesture/TapGestureDetector$gestureListener$1", "Lcom/android/systemui/statusbar/gesture/TapGestureDetector$gestureListener$1;", "onInputEvent", "", "ev", "Landroid/view/InputEvent;", "startGestureListening", "startGestureListening$SystemUI_nothingRelease", "stopGestureListening", "stopGestureListening$SystemUI_nothingRelease", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: TapGestureDetector.kt */
public final class TapGestureDetector extends GenericGestureDetector {
    private final Context context;
    private GestureDetector gestureDetector;
    private final TapGestureDetector$gestureListener$1 gestureListener = new TapGestureDetector$gestureListener$1(this);

    /* JADX WARNING: Illegal instructions before constructor call */
    @javax.inject.Inject
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public TapGestureDetector(android.content.Context r2) {
        /*
            r1 = this;
            java.lang.String r0 = "context"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r0)
            java.lang.Class<com.android.systemui.statusbar.gesture.TapGestureDetector> r0 = com.android.systemui.statusbar.gesture.TapGestureDetector.class
            kotlin.reflect.KClass r0 = kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(r0)
            java.lang.String r0 = r0.getSimpleName()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            r1.<init>(r0)
            r1.context = r2
            com.android.systemui.statusbar.gesture.TapGestureDetector$gestureListener$1 r2 = new com.android.systemui.statusbar.gesture.TapGestureDetector$gestureListener$1
            r2.<init>(r1)
            r1.gestureListener = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.gesture.TapGestureDetector.<init>(android.content.Context):void");
    }

    public void onInputEvent(InputEvent inputEvent) {
        Intrinsics.checkNotNullParameter(inputEvent, "ev");
        if (inputEvent instanceof MotionEvent) {
            GestureDetector gestureDetector2 = this.gestureDetector;
            Intrinsics.checkNotNull(gestureDetector2);
            gestureDetector2.onTouchEvent((MotionEvent) inputEvent);
        }
    }

    public void startGestureListening$SystemUI_nothingRelease() {
        super.startGestureListening$SystemUI_nothingRelease();
        this.gestureDetector = new GestureDetector(this.context, this.gestureListener);
    }

    public void stopGestureListening$SystemUI_nothingRelease() {
        super.stopGestureListening$SystemUI_nothingRelease();
        this.gestureDetector = null;
    }
}
