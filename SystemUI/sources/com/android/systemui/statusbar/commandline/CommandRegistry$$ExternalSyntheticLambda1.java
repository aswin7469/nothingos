package com.android.systemui.statusbar.commandline;

import java.util.concurrent.FutureTask;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CommandRegistry$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ FutureTask f$0;

    public /* synthetic */ CommandRegistry$$ExternalSyntheticLambda1(FutureTask futureTask) {
        this.f$0 = futureTask;
    }

    public final void run() {
        CommandRegistry.m3058onShellCommand$lambda1(this.f$0);
    }
}
