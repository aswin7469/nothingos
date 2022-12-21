package android.safetycenter;

import android.annotation.SystemApi;
import android.app.SystemServiceRegistry;
import android.content.Context;
import android.os.IBinder;
import android.safetycenter.ISafetyCenterManager;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class SafetyCenterFrameworkInitializer {
    private SafetyCenterFrameworkInitializer() {
    }

    public static void registerServiceWrappers() {
        SystemServiceRegistry.registerContextAwareService("safety_center", SafetyCenterManager.class, new SafetyCenterFrameworkInitializer$$ExternalSyntheticLambda0());
    }

    static /* synthetic */ SafetyCenterManager lambda$registerServiceWrappers$0(Context context, IBinder iBinder) {
        return new SafetyCenterManager(context, ISafetyCenterManager.Stub.asInterface(iBinder));
    }
}
