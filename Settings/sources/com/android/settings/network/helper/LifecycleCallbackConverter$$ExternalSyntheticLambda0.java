package com.android.settings.network.helper;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class LifecycleCallbackConverter$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ LifecycleCallbackConverter f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ LifecycleCallbackConverter$$ExternalSyntheticLambda0(LifecycleCallbackConverter lifecycleCallbackConverter, long j, Object obj) {
        this.f$0 = lifecycleCallbackConverter;
        this.f$1 = j;
        this.f$2 = obj;
    }

    public final void run() {
        this.f$0.lambda$postResultToUiThread$0(this.f$1, this.f$2);
    }
}
