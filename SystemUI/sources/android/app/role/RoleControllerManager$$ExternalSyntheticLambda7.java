package android.app.role;

import android.os.Bundle;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RoleControllerManager$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ Throwable f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ Consumer f$2;
    public final /* synthetic */ Bundle f$3;

    public /* synthetic */ RoleControllerManager$$ExternalSyntheticLambda7(Throwable th, String str, Consumer consumer, Bundle bundle) {
        this.f$0 = th;
        this.f$1 = str;
        this.f$2 = consumer;
        this.f$3 = bundle;
    }

    public final void run() {
        RoleControllerManager.lambda$propagateCallback$6(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
