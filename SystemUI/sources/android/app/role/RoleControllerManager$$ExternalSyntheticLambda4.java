package android.app.role;

import android.os.Bundle;
import android.os.RemoteCallback;
import java.util.function.BiConsumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RoleControllerManager$$ExternalSyntheticLambda4 implements BiConsumer {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ RemoteCallback f$1;

    public /* synthetic */ RoleControllerManager$$ExternalSyntheticLambda4(String str, RemoteCallback remoteCallback) {
        this.f$0 = str;
        this.f$1 = remoteCallback;
    }

    public final void accept(Object obj, Object obj2) {
        RoleControllerManager.lambda$propagateCallback$8(this.f$0, this.f$1, (Bundle) obj, (Throwable) obj2);
    }
}
