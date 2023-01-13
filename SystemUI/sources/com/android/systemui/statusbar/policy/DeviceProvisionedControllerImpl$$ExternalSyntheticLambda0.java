package com.android.systemui.statusbar.policy;

import java.util.ArrayList;
import kotlin.jvm.functions.Function1;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DeviceProvisionedControllerImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ArrayList f$0;
    public final /* synthetic */ Function1 f$1;

    public /* synthetic */ DeviceProvisionedControllerImpl$$ExternalSyntheticLambda0(ArrayList arrayList, Function1 function1) {
        this.f$0 = arrayList;
        this.f$1 = function1;
    }

    public final void run() {
        DeviceProvisionedControllerImpl.m3242dispatchChange$lambda7(this.f$0, this.f$1);
    }
}
