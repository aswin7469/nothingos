package com.android.p019wm.shell;

import com.android.p019wm.shell.legacysplitscreen.LegacySplitScreenController;
import java.p026io.PrintWriter;
import java.util.function.Consumer;

/* renamed from: com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda8 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ShellCommandHandlerImpl$$ExternalSyntheticLambda8 implements Consumer {
    public final /* synthetic */ PrintWriter f$0;

    public /* synthetic */ ShellCommandHandlerImpl$$ExternalSyntheticLambda8(PrintWriter printWriter) {
        this.f$0 = printWriter;
    }

    public final void accept(Object obj) {
        ((LegacySplitScreenController) obj).dump(this.f$0);
    }
}