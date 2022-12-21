package com.android.p019wm.shell.common;

import com.android.p019wm.shell.common.DisplayController;
import java.util.List;

/* renamed from: com.android.wm.shell.common.DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3420xd1ef1ed0 implements Runnable {
    public final /* synthetic */ DisplayController.DisplayWindowListenerImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ List f$2;
    public final /* synthetic */ List f$3;

    public /* synthetic */ C3420xd1ef1ed0(DisplayController.DisplayWindowListenerImpl displayWindowListenerImpl, int i, List list, List list2) {
        this.f$0 = displayWindowListenerImpl;
        this.f$1 = i;
        this.f$2 = list;
        this.f$3 = list2;
    }

    public final void run() {
        this.f$0.mo49029x4d802e6f(this.f$1, this.f$2, this.f$3);
    }
}
