package android.app.role;

import android.os.Bundle;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RoleManager$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Bundle f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ RoleManager$$ExternalSyntheticLambda0(Bundle bundle, Consumer consumer) {
        this.f$0 = bundle;
        this.f$1 = consumer;
    }

    public final void run() {
        RoleManager.lambda$createRemoteCallback$0(this.f$0, this.f$1);
    }
}
