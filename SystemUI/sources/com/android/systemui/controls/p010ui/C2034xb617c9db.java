package com.android.systemui.controls.p010ui;

import java.util.List;
import kotlin.jvm.functions.Function1;

/* renamed from: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2034xb617c9db implements Runnable {
    public final /* synthetic */ ControlsUiControllerImpl f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ Function1 f$2;

    public /* synthetic */ C2034xb617c9db(ControlsUiControllerImpl controlsUiControllerImpl, List list, Function1 function1) {
        this.f$0 = controlsUiControllerImpl;
        this.f$1 = list;
        this.f$2 = function1;
    }

    public final void run() {
        ControlsUiControllerImpl$createCallback$1.m2709onServicesUpdated$lambda1(this.f$0, this.f$1, this.f$2);
    }
}
