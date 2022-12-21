package android.app.role;

import android.os.Bundle;
import android.os.RemoteCallback;
import com.android.permission.jarjar.com.android.internal.infra.AndroidFuture;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RoleControllerManager$$ExternalSyntheticLambda2 implements RemoteCallback.OnResultListener {
    public final /* synthetic */ AndroidFuture f$0;

    public /* synthetic */ RoleControllerManager$$ExternalSyntheticLambda2(AndroidFuture androidFuture) {
        this.f$0 = androidFuture;
    }

    public final void onResult(Bundle bundle) {
        this.f$0.complete(bundle);
    }
}
