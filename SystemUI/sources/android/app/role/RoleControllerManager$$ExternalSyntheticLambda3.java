package android.app.role;

import com.android.permission.jarjar.com.android.internal.infra.ServiceConnector;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RoleControllerManager$$ExternalSyntheticLambda3 implements ServiceConnector.Job {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ RoleControllerManager$$ExternalSyntheticLambda3(String str, String str2, int i) {
        this.f$0 = str;
        this.f$1 = str2;
        this.f$2 = i;
    }

    public final Object run(Object obj) {
        return RoleControllerManager.lambda$onAddRoleHolder$1(this.f$0, this.f$1, this.f$2, (IRoleController) obj);
    }
}
