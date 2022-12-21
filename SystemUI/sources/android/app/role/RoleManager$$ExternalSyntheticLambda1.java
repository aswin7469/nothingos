package android.app.role;

import android.os.Bundle;
import android.os.RemoteCallback;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RoleManager$$ExternalSyntheticLambda1 implements RemoteCallback.OnResultListener {
    public final /* synthetic */ Executor f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ RoleManager$$ExternalSyntheticLambda1(Executor executor, Consumer consumer) {
        this.f$0 = executor;
        this.f$1 = consumer;
    }

    public final void onResult(Bundle bundle) {
        this.f$0.execute(new RoleManager$$ExternalSyntheticLambda0(bundle, this.f$1));
    }
}
