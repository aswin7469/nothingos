package android.net;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.util.Log;
import java.util.List;

public class ConnectivityResources {
    private static final String RESOURCES_APK_INTENT = "com.android.server.connectivity.intent.action.SERVICE_CONNECTIVITY_RESOURCES_APK";
    private static final String RES_PKG_DIR = "/apex/com.android.tethering/";
    private static Context sTestResourcesContext;
    private final Context mContext;
    private Context mResourcesContext = null;

    public ConnectivityResources(Context context) {
        this.mContext = context;
    }

    public static void setResourcesContextForTest(Context context) {
        sTestResourcesContext = context;
    }

    public synchronized Context getResourcesContext() {
        Context context = sTestResourcesContext;
        if (context != null) {
            return context;
        }
        Context context2 = this.mResourcesContext;
        if (context2 != null) {
            return context2;
        }
        List<ResolveInfo> queryIntentActivities = this.mContext.getPackageManager().queryIntentActivities(new Intent(RESOURCES_APK_INTENT), 1048576);
        queryIntentActivities.removeIf(new ConnectivityResources$$ExternalSyntheticLambda0());
        if (queryIntentActivities.size() > 1) {
            Log.wtf("ConnectivityResources", "More than one package found: " + queryIntentActivities);
        }
        if (!queryIntentActivities.isEmpty()) {
            try {
                Context createPackageContext = this.mContext.createPackageContext(queryIntentActivities.get(0).activityInfo.applicationInfo.packageName, 0);
                this.mResourcesContext = createPackageContext;
                return createPackageContext;
            } catch (PackageManager.NameNotFoundException e) {
                throw new IllegalStateException("Resolved package not found", e);
            }
        } else {
            throw new IllegalStateException("No connectivity resource package found");
        }
    }

    static /* synthetic */ boolean lambda$getResourcesContext$0(ResolveInfo resolveInfo) {
        return !resolveInfo.activityInfo.applicationInfo.sourceDir.startsWith(RES_PKG_DIR);
    }

    public Resources get() {
        return getResourcesContext().getResources();
    }
}
