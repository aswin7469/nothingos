package com.android.systemui.accessibility;

import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ModeSwitchesController$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ int f$0;

    public /* synthetic */ ModeSwitchesController$$ExternalSyntheticLambda1(int i) {
        this.f$0 = i;
    }

    public final void accept(Object obj) {
        ((MagnificationModeSwitch) obj).onConfigurationChanged(this.f$0);
    }
}
