package com.android.systemui.accessibility;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MagnificationModeSwitch$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ MagnificationModeSwitch f$0;

    public /* synthetic */ MagnificationModeSwitch$$ExternalSyntheticLambda1(MagnificationModeSwitch magnificationModeSwitch) {
        this.f$0 = magnificationModeSwitch;
    }

    public final void run() {
        this.f$0.onWindowInsetChanged();
    }
}