package dalvik.system;

import android.annotation.SystemApi;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public class AppSpecializationHooks {
    private AppSpecializationHooks() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void handleCompatChangesBeforeBindingApplication() {
        com.android.i18n.system.AppSpecializationHooks.handleCompatChangesBeforeBindingApplication();
    }
}
