package com.android.systemui.screenshot;

import android.graphics.Rect;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScrollCaptureController;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ScreenshotController$$ExternalSyntheticLambda13 implements ScreenshotController.TransitionDestination {
    public final /* synthetic */ ScreenshotController f$0;
    public final /* synthetic */ ScrollCaptureController.LongScreenshot f$1;

    public /* synthetic */ ScreenshotController$$ExternalSyntheticLambda13(ScreenshotController screenshotController, ScrollCaptureController.LongScreenshot longScreenshot) {
        this.f$0 = screenshotController;
        this.f$1 = longScreenshot;
    }

    public final void setTransitionDestination(Rect rect, Runnable runnable) {
        this.f$0.mo37430x61d93a0b(this.f$1, rect, runnable);
    }
}
