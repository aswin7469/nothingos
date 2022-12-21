package android.app.role;

import android.annotation.SystemApi;
import android.app.SystemServiceRegistry;
import android.app.role.IRoleManager;
import android.content.Context;
import android.os.IBinder;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public class RoleFrameworkInitializer {
    private RoleFrameworkInitializer() {
    }

    public static void registerServiceWrappers() {
        SystemServiceRegistry.registerContextAwareService("role", RoleManager.class, new RoleFrameworkInitializer$$ExternalSyntheticLambda0());
    }

    static /* synthetic */ RoleManager lambda$registerServiceWrappers$0(Context context, IBinder iBinder) {
        return new RoleManager(context, IRoleManager.Stub.asInterface(iBinder));
    }
}
