package android.app.role;

import com.android.permission.jarjar.com.android.internal.infra.ServiceConnector;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RoleControllerManager$$ExternalSyntheticLambda5 implements ServiceConnector.Job {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ RoleControllerManager$$ExternalSyntheticLambda5(String str, int i) {
        this.f$0 = str;
        this.f$1 = i;
    }

    public final Object run(Object obj) {
        return RoleControllerManager.lambda$onClearRoleHolders$3(this.f$0, this.f$1, (IRoleController) obj);
    }
}
