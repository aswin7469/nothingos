package android.app.role;

import com.android.permission.jarjar.com.android.internal.infra.ServiceConnector;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RoleControllerManager$$ExternalSyntheticLambda8 implements ServiceConnector.Job {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ RoleControllerManager$$ExternalSyntheticLambda8(String str, String str2) {
        this.f$0 = str;
        this.f$1 = str2;
    }

    public final Object run(Object obj) {
        return RoleControllerManager.lambda$isApplicationVisibleForRole$4(this.f$0, this.f$1, (IRoleController) obj);
    }
}
