package com.android.systemui.accessibility;

import com.android.systemui.accessibility.MagnificationModeSwitch;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WindowMagnification$$ExternalSyntheticLambda0 implements MagnificationModeSwitch.SwitchListener {
    public final /* synthetic */ WindowMagnificationConnectionImpl f$0;

    public /* synthetic */ WindowMagnification$$ExternalSyntheticLambda0(WindowMagnificationConnectionImpl windowMagnificationConnectionImpl) {
        this.f$0 = windowMagnificationConnectionImpl;
    }

    public final void onSwitch(int i, int i2) {
        this.f$0.onChangeMagnificationMode(i, i2);
    }
}
