package com.android.p019wm.shell.pip;

import android.window.WindowContainerTransaction;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ PipTaskOrganizer f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ WindowContainerTransaction f$2;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda1(PipTaskOrganizer pipTaskOrganizer, boolean z, WindowContainerTransaction windowContainerTransaction) {
        this.f$0 = pipTaskOrganizer;
        this.f$1 = z;
        this.f$2 = windowContainerTransaction;
    }

    public final void accept(Object obj) {
        this.f$0.mo50211xfcb41eef(this.f$1, this.f$2, (SplitScreenController) obj);
    }
}
