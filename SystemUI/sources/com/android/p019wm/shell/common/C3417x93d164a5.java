package com.android.p019wm.shell.common;

import android.view.IDisplayWindowRotationCallback;
import com.android.p019wm.shell.common.DisplayChangeController;

/* renamed from: com.android.wm.shell.common.DisplayChangeController$DisplayWindowRotationControllerImpl$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3417x93d164a5 implements Runnable {
    public final /* synthetic */ DisplayChangeController.DisplayWindowRotationControllerImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ IDisplayWindowRotationCallback f$4;

    public /* synthetic */ C3417x93d164a5(DisplayChangeController.DisplayWindowRotationControllerImpl displayWindowRotationControllerImpl, int i, int i2, int i3, IDisplayWindowRotationCallback iDisplayWindowRotationCallback) {
        this.f$0 = displayWindowRotationControllerImpl;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = i3;
        this.f$4 = iDisplayWindowRotationCallback;
    }

    public final void run() {
        this.f$0.mo49011x15447031(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
