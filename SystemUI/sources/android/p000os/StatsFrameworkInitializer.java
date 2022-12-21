package android.p000os;

import android.annotation.SystemApi;
import android.app.StatsManager;
import android.app.SystemServiceRegistry;
import android.content.Context;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
/* renamed from: android.os.StatsFrameworkInitializer */
public class StatsFrameworkInitializer {
    private static volatile StatsServiceManager sStatsServiceManager;

    private StatsFrameworkInitializer() {
    }

    public static void setStatsServiceManager(StatsServiceManager statsServiceManager) {
        if (sStatsServiceManager != null) {
            throw new IllegalStateException("setStatsServiceManager called twice!");
        } else if (statsServiceManager != null) {
            sStatsServiceManager = statsServiceManager;
        } else {
            throw new NullPointerException("statsServiceManager is null");
        }
    }

    public static StatsServiceManager getStatsServiceManager() {
        return sStatsServiceManager;
    }

    public static void registerServiceWrappers() {
        SystemServiceRegistry.registerContextAwareService("stats", StatsManager.class, new StatsFrameworkInitializer$$ExternalSyntheticLambda0());
    }

    static /* synthetic */ StatsManager lambda$registerServiceWrappers$0(Context context) {
        return new StatsManager(context);
    }
}
