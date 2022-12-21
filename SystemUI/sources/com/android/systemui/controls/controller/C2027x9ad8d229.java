package com.android.systemui.controls.controller;

import android.content.ComponentName;
import java.util.List;
import java.util.function.Consumer;

/* renamed from: com.android.systemui.controls.controller.ControlsControllerImpl$loadForComponent$2$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2027x9ad8d229 implements Runnable {
    public final /* synthetic */ ComponentName f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ ControlsControllerImpl f$2;
    public final /* synthetic */ Consumer f$3;

    public /* synthetic */ C2027x9ad8d229(ComponentName componentName, List list, ControlsControllerImpl controlsControllerImpl, Consumer consumer) {
        this.f$0 = componentName;
        this.f$1 = list;
        this.f$2 = controlsControllerImpl;
        this.f$3 = consumer;
    }

    public final void run() {
        ControlsControllerImpl$loadForComponent$2.m2619accept$lambda4(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
