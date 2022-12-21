package com.android.systemui.screenshot;

import com.android.systemui.screenshot.ScreenshotController;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ScreenshotController$$ExternalSyntheticLambda21 implements ScreenshotController.QuickShareActionReadyListener {
    public final /* synthetic */ ScreenshotController f$0;

    public /* synthetic */ ScreenshotController$$ExternalSyntheticLambda21(ScreenshotController screenshotController) {
        this.f$0 = screenshotController;
    }

    public final void onActionsReady(ScreenshotController.QuickShareData quickShareData) {
        this.f$0.showUiOnQuickShareActionReady(quickShareData);
    }
}
