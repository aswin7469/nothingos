package retrofit2;

import retrofit2.DefaultCallAdapterFactory;

/* renamed from: retrofit2.DefaultCallAdapterFactory$ExecutorCallbackCall$1$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C1073xee4a199c implements Runnable {
    public final /* synthetic */ DefaultCallAdapterFactory.ExecutorCallbackCall.C10721 f$0;
    public final /* synthetic */ Callback f$1;
    public final /* synthetic */ Throwable f$2;

    public /* synthetic */ C1073xee4a199c(DefaultCallAdapterFactory.ExecutorCallbackCall.C10721 r1, Callback callback, Throwable th) {
        this.f$0 = r1;
        this.f$1 = callback;
        this.f$2 = th;
    }

    public final void run() {
        this.f$0.mo18192x714e864(this.f$1, this.f$2);
    }
}
