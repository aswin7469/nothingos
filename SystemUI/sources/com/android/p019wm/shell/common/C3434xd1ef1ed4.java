package com.android.p019wm.shell.common;

import android.content.res.Configuration;
import com.android.p019wm.shell.common.DisplayController;

/* renamed from: com.android.wm.shell.common.DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda5 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3434xd1ef1ed4 implements Runnable {
    public final /* synthetic */ DisplayController.DisplayWindowListenerImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ Configuration f$2;

    public /* synthetic */ C3434xd1ef1ed4(DisplayController.DisplayWindowListenerImpl displayWindowListenerImpl, int i, Configuration configuration) {
        this.f$0 = displayWindowListenerImpl;
        this.f$1 = i;
        this.f$2 = configuration;
    }

    public final void run() {
        this.f$0.mo49034xae15a375(this.f$1, this.f$2);
    }
}
