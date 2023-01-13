package com.android.systemui.statusbar.commandline;

import java.p026io.PrintWriter;
import java.util.concurrent.Callable;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CommandRegistry$$ExternalSyntheticLambda0 implements Callable {
    public final /* synthetic */ Command f$0;
    public final /* synthetic */ PrintWriter f$1;
    public final /* synthetic */ String[] f$2;

    public /* synthetic */ CommandRegistry$$ExternalSyntheticLambda0(Command command, PrintWriter printWriter, String[] strArr) {
        this.f$0 = command;
        this.f$1 = printWriter;
        this.f$2 = strArr;
    }

    public final Object call() {
        return CommandRegistry.m3057onShellCommand$lambda0(this.f$0, this.f$1, this.f$2);
    }
}
