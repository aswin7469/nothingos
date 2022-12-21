package android.nearby;

import android.annotation.SystemApi;
import android.app.SystemServiceRegistry;
import android.content.Context;
import android.nearby.INearbyManager;
import android.os.IBinder;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class NearbyFrameworkInitializer {
    private NearbyFrameworkInitializer() {
    }

    public static void registerServiceWrappers() {
        SystemServiceRegistry.registerContextAwareService("nearby", NearbyManager.class, new NearbyFrameworkInitializer$$ExternalSyntheticLambda0());
    }

    static /* synthetic */ NearbyManager lambda$registerServiceWrappers$0(Context context, IBinder iBinder) {
        return new NearbyManager(context, INearbyManager.Stub.asInterface(iBinder));
    }
}
