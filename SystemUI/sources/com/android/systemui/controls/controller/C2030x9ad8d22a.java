package com.android.systemui.controls.controller;

import android.content.ComponentName;
import java.util.function.Consumer;

/* renamed from: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2030x9ad8d22a implements Runnable {
    public final /* synthetic */ ComponentName f$0;
    public final /* synthetic */ Consumer f$1;
    public final /* synthetic */ ControlsControllerImpl f$2;

    public /* synthetic */ C2030x9ad8d22a(ComponentName componentName, Consumer consumer, ControlsControllerImpl controlsControllerImpl) {
        this.f$0 = componentName;
        this.f$1 = consumer;
        this.f$2 = controlsControllerImpl;
    }

    public final void run() {
        ControlsControllerImpl$loadForComponent$2.m2625error$lambda8(this.f$0, this.f$1, this.f$2);
    }
}
