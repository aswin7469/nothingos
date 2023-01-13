package com.android.systemui.statusbar.gesture;

import android.view.InputEvent;
import android.view.MotionEvent;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0017\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\r\u0010\u0015\u001a\u00020\u0012H\u0010¢\u0006\u0002\b\u0016J\r\u0010\u0017\u001a\u00020\u0012H\u0010¢\u0006\u0002\b\u0018R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000¨\u0006\u0019"}, mo65043d2 = {"Lcom/android/systemui/statusbar/gesture/SwipeStatusBarAwayGestureHandler;", "Lcom/android/systemui/statusbar/gesture/GenericGestureDetector;", "context", "Landroid/content/Context;", "statusBarWindowController", "Lcom/android/systemui/statusbar/window/StatusBarWindowController;", "logger", "Lcom/android/systemui/statusbar/gesture/SwipeStatusBarAwayGestureLogger;", "(Landroid/content/Context;Lcom/android/systemui/statusbar/window/StatusBarWindowController;Lcom/android/systemui/statusbar/gesture/SwipeStatusBarAwayGestureLogger;)V", "monitoringCurrentTouch", "", "startTime", "", "startY", "", "swipeDistanceThreshold", "", "onInputEvent", "", "ev", "Landroid/view/InputEvent;", "startGestureListening", "startGestureListening$SystemUI_nothingRelease", "stopGestureListening", "stopGestureListening$SystemUI_nothingRelease", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SwipeStatusBarAwayGestureHandler.kt */
public class SwipeStatusBarAwayGestureHandler extends GenericGestureDetector {
    private final SwipeStatusBarAwayGestureLogger logger;
    private boolean monitoringCurrentTouch;
    private long startTime;
    private float startY;
    private final StatusBarWindowController statusBarWindowController;
    private int swipeDistanceThreshold;

    /* JADX WARNING: Illegal instructions before constructor call */
    @javax.inject.Inject
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SwipeStatusBarAwayGestureHandler(android.content.Context r2, com.android.systemui.statusbar.window.StatusBarWindowController r3, com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureLogger r4) {
        /*
            r1 = this;
            java.lang.String r0 = "context"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r0)
            java.lang.String r0 = "statusBarWindowController"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
            java.lang.String r0 = "logger"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
            java.lang.Class<com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler> r0 = com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler.class
            kotlin.reflect.KClass r0 = kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(r0)
            java.lang.String r0 = r0.getSimpleName()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            r1.<init>(r0)
            r1.statusBarWindowController = r3
            r1.logger = r4
            android.content.res.Resources r2 = r2.getResources()
            r3 = 17105559(0x1050297, float:2.44301E-38)
            int r2 = r2.getDimensionPixelSize(r3)
            r1.swipeDistanceThreshold = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler.<init>(android.content.Context, com.android.systemui.statusbar.window.StatusBarWindowController, com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureLogger):void");
    }

    public void onInputEvent(InputEvent inputEvent) {
        Intrinsics.checkNotNullParameter(inputEvent, "ev");
        if (inputEvent instanceof MotionEvent) {
            MotionEvent motionEvent = (MotionEvent) inputEvent;
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked != 2) {
                        if (actionMasked != 3) {
                            return;
                        }
                    } else if (this.monitoringCurrentTouch) {
                        float y = motionEvent.getY();
                        float f = this.startY;
                        if (y < f && f - motionEvent.getY() >= ((float) this.swipeDistanceThreshold) && motionEvent.getEventTime() - this.startTime < 500) {
                            this.monitoringCurrentTouch = false;
                            this.logger.logGestureDetected((int) motionEvent.getY());
                            onGestureDetected$SystemUI_nothingRelease(motionEvent);
                            return;
                        }
                        return;
                    } else {
                        return;
                    }
                }
                if (this.monitoringCurrentTouch) {
                    this.logger.logGestureDetectionEndedWithoutTriggering((int) motionEvent.getY());
                }
                this.monitoringCurrentTouch = false;
            } else if (motionEvent.getY() < ((float) this.statusBarWindowController.getStatusBarHeight()) || motionEvent.getY() > ((float) (this.statusBarWindowController.getStatusBarHeight() * 3))) {
                this.monitoringCurrentTouch = false;
            } else {
                this.logger.logGestureDetectionStarted((int) motionEvent.getY());
                this.startY = motionEvent.getY();
                this.startTime = motionEvent.getEventTime();
                this.monitoringCurrentTouch = true;
            }
        }
    }

    public void startGestureListening$SystemUI_nothingRelease() {
        super.startGestureListening$SystemUI_nothingRelease();
        this.logger.logInputListeningStarted();
    }

    public void stopGestureListening$SystemUI_nothingRelease() {
        super.stopGestureListening$SystemUI_nothingRelease();
        this.logger.logInputListeningStopped();
    }
}
