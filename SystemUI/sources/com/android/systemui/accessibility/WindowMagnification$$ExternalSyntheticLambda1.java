package com.android.systemui.accessibility;

import java.p026io.PrintWriter;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WindowMagnification$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ PrintWriter f$0;

    public /* synthetic */ WindowMagnification$$ExternalSyntheticLambda1(PrintWriter printWriter) {
        this.f$0 = printWriter;
    }

    public final void accept(Object obj) {
        ((WindowMagnificationController) obj).dump(this.f$0);
    }
}
