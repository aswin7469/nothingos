package com.android.systemui.dump;

import java.lang.Thread;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DumpHandler$$ExternalSyntheticLambda0 implements Thread.UncaughtExceptionHandler {
    public final /* synthetic */ DumpHandler f$0;

    public /* synthetic */ DumpHandler$$ExternalSyntheticLambda0(DumpHandler dumpHandler) {
        this.f$0 = dumpHandler;
    }

    public final void uncaughtException(Thread thread, Throwable th) {
        DumpHandler.m2747init$lambda0(this.f$0, thread, th);
    }
}
