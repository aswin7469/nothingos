package com.android.systemui.screenshot;

import com.android.systemui.screenshot.ScreenshotController;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ScreenshotController$$ExternalSyntheticLambda11 implements ScreenshotController.ActionsReadyListener {
    public final /* synthetic */ ScreenshotController f$0;

    public /* synthetic */ ScreenshotController$$ExternalSyntheticLambda11(ScreenshotController screenshotController) {
        this.f$0 = screenshotController;
    }

    public final void onActionsReady(ScreenshotController.SavedImageData savedImageData) {
        this.f$0.logSuccessOnActionsReady(savedImageData);
    }
}
