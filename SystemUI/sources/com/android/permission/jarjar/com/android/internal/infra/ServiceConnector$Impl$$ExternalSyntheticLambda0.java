package com.android.permission.jarjar.com.android.internal.infra;

import com.android.permission.jarjar.com.android.internal.infra.ServiceConnector;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ServiceConnector$Impl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ServiceConnector.Impl f$0;

    public /* synthetic */ ServiceConnector$Impl$$ExternalSyntheticLambda0(ServiceConnector.Impl impl) {
        this.f$0 = impl;
    }

    public final void run() {
        this.f$0.unbindJobThread();
    }
}
