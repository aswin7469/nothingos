package com.android.systemui.controls.controller;

import android.os.IBinder;
import android.service.controls.Control;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;

/* renamed from: com.android.systemui.controls.controller.ControlsBindingControllerImpl$LoadSubscriber$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2024x1b4f6958 implements Runnable {
    public final /* synthetic */ ControlsBindingControllerImpl.LoadSubscriber f$0;
    public final /* synthetic */ Control f$1;
    public final /* synthetic */ ControlsBindingControllerImpl f$2;
    public final /* synthetic */ IBinder f$3;

    public /* synthetic */ C2024x1b4f6958(ControlsBindingControllerImpl.LoadSubscriber loadSubscriber, Control control, ControlsBindingControllerImpl controlsBindingControllerImpl, IBinder iBinder) {
        this.f$0 = loadSubscriber;
        this.f$1 = control;
        this.f$2 = controlsBindingControllerImpl;
        this.f$3 = iBinder;
    }

    public final void run() {
        ControlsBindingControllerImpl.LoadSubscriber.m2607onNext$lambda2(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
