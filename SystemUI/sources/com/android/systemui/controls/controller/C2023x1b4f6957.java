package com.android.systemui.controls.controller;

import com.android.systemui.controls.controller.ControlsBindingControllerImpl;

/* renamed from: com.android.systemui.controls.controller.ControlsBindingControllerImpl$LoadSubscriber$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2023x1b4f6957 implements Runnable {
    public final /* synthetic */ ControlsBindingControllerImpl.LoadSubscriber f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ C2023x1b4f6957(ControlsBindingControllerImpl.LoadSubscriber loadSubscriber, Runnable runnable) {
        this.f$0 = loadSubscriber;
        this.f$1 = runnable;
    }

    public final void run() {
        ControlsBindingControllerImpl.LoadSubscriber.m2606maybeTerminateAndRun$lambda3(this.f$0, this.f$1);
    }
}
