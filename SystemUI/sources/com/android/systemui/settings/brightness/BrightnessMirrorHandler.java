package com.android.systemui.settings.brightness;

import android.view.View;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\nJ\u000e\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\bJ\b\u0010\u000e\u001a\u00020\nH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u000e¢\u0006\u0002\n\u0000¨\u0006\u000f"}, mo64987d2 = {"Lcom/android/systemui/settings/brightness/BrightnessMirrorHandler;", "", "brightnessController", "Lcom/android/systemui/settings/brightness/MirroredBrightnessController;", "(Lcom/android/systemui/settings/brightness/MirroredBrightnessController;)V", "brightnessMirrorListener", "Lcom/android/systemui/statusbar/policy/BrightnessMirrorController$BrightnessMirrorListener;", "mirrorController", "Lcom/android/systemui/statusbar/policy/BrightnessMirrorController;", "onQsPanelAttached", "", "onQsPanelDettached", "setController", "controller", "updateBrightnessMirror", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: BrightnessMirrorHandler.kt */
public final class BrightnessMirrorHandler {
    private final MirroredBrightnessController brightnessController;
    private final BrightnessMirrorController.BrightnessMirrorListener brightnessMirrorListener = new BrightnessMirrorHandler$$ExternalSyntheticLambda0(this);
    private BrightnessMirrorController mirrorController;

    public BrightnessMirrorHandler(MirroredBrightnessController mirroredBrightnessController) {
        Intrinsics.checkNotNullParameter(mirroredBrightnessController, "brightnessController");
        this.brightnessController = mirroredBrightnessController;
    }

    /* access modifiers changed from: private */
    /* renamed from: brightnessMirrorListener$lambda-0  reason: not valid java name */
    public static final void m3022brightnessMirrorListener$lambda0(BrightnessMirrorHandler brightnessMirrorHandler, View view) {
        Intrinsics.checkNotNullParameter(brightnessMirrorHandler, "this$0");
        brightnessMirrorHandler.updateBrightnessMirror();
    }

    public final void onQsPanelAttached() {
        BrightnessMirrorController brightnessMirrorController = this.mirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.addCallback(this.brightnessMirrorListener);
        }
    }

    public final void onQsPanelDettached() {
        BrightnessMirrorController brightnessMirrorController = this.mirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.removeCallback(this.brightnessMirrorListener);
        }
    }

    public final void setController(BrightnessMirrorController brightnessMirrorController) {
        Intrinsics.checkNotNullParameter(brightnessMirrorController, "controller");
        BrightnessMirrorController brightnessMirrorController2 = this.mirrorController;
        if (brightnessMirrorController2 != null) {
            brightnessMirrorController2.removeCallback(this.brightnessMirrorListener);
        }
        this.mirrorController = brightnessMirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.addCallback(this.brightnessMirrorListener);
        }
        updateBrightnessMirror();
    }

    private final void updateBrightnessMirror() {
        BrightnessMirrorController brightnessMirrorController = this.mirrorController;
        if (brightnessMirrorController != null) {
            this.brightnessController.setMirror(brightnessMirrorController);
        }
    }
}
