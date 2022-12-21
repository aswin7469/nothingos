package android.app.role;

import android.app.role.RoleControllerService;
import android.os.RemoteCallback;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RoleControllerService$1$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ RoleControllerService.C00011 f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ RemoteCallback f$4;

    public /* synthetic */ RoleControllerService$1$$ExternalSyntheticLambda2(RoleControllerService.C00011 r1, String str, String str2, int i, RemoteCallback remoteCallback) {
        this.f$0 = r1;
        this.f$1 = str;
        this.f$2 = str2;
        this.f$3 = i;
        this.f$4 = remoteCallback;
    }

    public final void run() {
        this.f$0.mo94x7bc265d3(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
