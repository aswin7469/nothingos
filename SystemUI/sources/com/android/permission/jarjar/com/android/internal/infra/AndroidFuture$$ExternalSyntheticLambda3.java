package com.android.permission.jarjar.com.android.internal.infra;

import java.util.function.BiConsumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class AndroidFuture$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ BiConsumer f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Throwable f$2;

    public /* synthetic */ AndroidFuture$$ExternalSyntheticLambda3(BiConsumer biConsumer, Object obj, Throwable th) {
        this.f$0 = biConsumer;
        this.f$1 = obj;
        this.f$2 = th;
    }

    public final void run() {
        AndroidFuture.callListener(this.f$0, this.f$1, this.f$2);
    }
}
