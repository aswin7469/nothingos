package retrofit2;

import retrofit2.DefaultCallAdapterFactory;

/* renamed from: retrofit2.DefaultCallAdapterFactory$ExecutorCallbackCall$1$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C1074xee4a199d implements Runnable {
    public final /* synthetic */ DefaultCallAdapterFactory.ExecutorCallbackCall.C10721 f$0;
    public final /* synthetic */ Callback f$1;
    public final /* synthetic */ Response f$2;

    public /* synthetic */ C1074xee4a199d(DefaultCallAdapterFactory.ExecutorCallbackCall.C10721 r1, Callback callback, Response response) {
        this.f$0 = r1;
        this.f$1 = callback;
        this.f$2 = response;
    }

    public final void run() {
        this.f$0.mo18193xddacc936(this.f$1, this.f$2);
    }
}
