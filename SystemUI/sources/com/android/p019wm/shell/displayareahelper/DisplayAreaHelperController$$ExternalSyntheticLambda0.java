package com.android.p019wm.shell.displayareahelper;

import android.view.SurfaceControl;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.displayareahelper.DisplayAreaHelperController$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DisplayAreaHelperController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DisplayAreaHelperController f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ SurfaceControl.Builder f$2;
    public final /* synthetic */ Consumer f$3;

    public /* synthetic */ DisplayAreaHelperController$$ExternalSyntheticLambda0(DisplayAreaHelperController displayAreaHelperController, int i, SurfaceControl.Builder builder, Consumer consumer) {
        this.f$0 = displayAreaHelperController;
        this.f$1 = i;
        this.f$2 = builder;
        this.f$3 = consumer;
    }

    public final void run() {
        this.f$0.mo49473x5024e067(this.f$1, this.f$2, this.f$3);
    }
}
