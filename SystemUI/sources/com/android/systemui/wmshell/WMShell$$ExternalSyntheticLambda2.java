package com.android.systemui.wmshell;

import com.android.p019wm.shell.ShellCommandHandler;
import java.p026io.PrintWriter;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WMShell$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ PrintWriter f$0;

    public /* synthetic */ WMShell$$ExternalSyntheticLambda2(PrintWriter printWriter) {
        this.f$0 = printWriter;
    }

    public final void accept(Object obj) {
        ((ShellCommandHandler) obj).dump(this.f$0);
    }
}
