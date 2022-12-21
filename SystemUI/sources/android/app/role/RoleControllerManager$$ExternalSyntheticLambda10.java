package android.app.role;

import com.android.permission.jarjar.com.android.internal.infra.ServiceConnector;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class RoleControllerManager$$ExternalSyntheticLambda10 implements ServiceConnector.Job {
    public final /* synthetic */ String f$0;

    public /* synthetic */ RoleControllerManager$$ExternalSyntheticLambda10(String str) {
        this.f$0 = str;
    }

    public final Object run(Object obj) {
        return RoleControllerManager.lambda$isRoleVisible$5(this.f$0, (IRoleController) obj);
    }
}
