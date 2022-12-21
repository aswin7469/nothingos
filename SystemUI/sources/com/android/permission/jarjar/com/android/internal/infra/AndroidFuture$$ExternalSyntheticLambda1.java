package com.android.permission.jarjar.com.android.internal.infra;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AndroidFuture$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ AndroidFuture f$0;

    public /* synthetic */ AndroidFuture$$ExternalSyntheticLambda1(AndroidFuture androidFuture) {
        this.f$0 = androidFuture;
    }

    public final void run() {
        this.f$0.triggerTimeout();
    }
}
