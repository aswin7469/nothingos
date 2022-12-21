package com.android.systemui.controls.management;

import java.util.List;
import java.util.concurrent.Executor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AppAdapter$callback$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ AppAdapter f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ Executor f$2;

    public /* synthetic */ AppAdapter$callback$1$$ExternalSyntheticLambda0(AppAdapter appAdapter, List list, Executor executor) {
        this.f$0 = appAdapter;
        this.f$1 = list;
        this.f$2 = executor;
    }

    public final void run() {
        AppAdapter$callback$1.m2634onServicesUpdated$lambda1(this.f$0, this.f$1, this.f$2);
    }
}
