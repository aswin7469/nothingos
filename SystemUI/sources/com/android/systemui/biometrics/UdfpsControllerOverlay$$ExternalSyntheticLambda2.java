package com.android.systemui.biometrics;

import android.view.accessibility.AccessibilityManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UdfpsControllerOverlay$$ExternalSyntheticLambda2 implements AccessibilityManager.TouchExplorationStateChangeListener {
    public final /* synthetic */ UdfpsControllerOverlay f$0;
    public final /* synthetic */ UdfpsView f$1;

    public /* synthetic */ UdfpsControllerOverlay$$ExternalSyntheticLambda2(UdfpsControllerOverlay udfpsControllerOverlay, UdfpsView udfpsView) {
        this.f$0 = udfpsControllerOverlay;
        this.f$1 = udfpsView;
    }

    public final void onTouchExplorationStateChanged(boolean z) {
        UdfpsControllerOverlay.m2592show$lambda4$lambda3(this.f$0, this.f$1, z);
    }
}
