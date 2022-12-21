package com.android.settingslib.applications;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.hardware.usb.IUsbManager;
import android.net.Uri;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import com.android.settingslib.C1757R;
import com.android.settingslib.Utils;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.applications.instantapps.InstantAppDataProvider;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.ArrayList;

public class AppUtils {
    private static final String TAG = "AppUtils";
    private static final Intent sBrowserIntent = new Intent().setAction("android.intent.action.VIEW").addCategory("android.intent.category.BROWSABLE").setData(Uri.parse("http:"));
    private static InstantAppDataProvider sInstantAppDataProvider;

    public static CharSequence getLaunchByDefaultSummary(ApplicationsState.AppEntry appEntry, IUsbManager iUsbManager, PackageManager packageManager, Context context) {
        int i;
        String str = appEntry.info.packageName;
        boolean z = false;
        boolean z2 = hasPreferredActivities(packageManager, str) || hasUsbDefaults(iUsbManager, str);
        if (packageManager.getIntentVerificationStatusAsUser(str, UserHandle.myUserId()) != 0) {
            z = true;
        }
        if (z2 || z) {
            i = C1757R.string.launch_defaults_some;
        } else {
            i = C1757R.string.launch_defaults_none;
        }
        return context.getString(i);
    }

    public static boolean hasUsbDefaults(IUsbManager iUsbManager, String str) {
        if (iUsbManager == null) {
            return false;
        }
        try {
            return iUsbManager.hasDefaults(str, UserHandle.myUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "mUsbManager.hasDefaults", e);
            return false;
        }
    }

    public static boolean hasPreferredActivities(PackageManager packageManager, String str) {
        ArrayList arrayList = new ArrayList();
        packageManager.getPreferredActivities(new ArrayList(), arrayList, str);
        Log.d(TAG, "Have " + arrayList.size() + " number of activities in preferred list");
        return arrayList.size() > 0;
    }

    public static boolean isInstant(ApplicationInfo applicationInfo) {
        String[] split;
        InstantAppDataProvider instantAppDataProvider = sInstantAppDataProvider;
        if (instantAppDataProvider != null) {
            if (instantAppDataProvider.isInstantApp(applicationInfo)) {
                return true;
            }
        } else if (applicationInfo.isInstantApp()) {
            return true;
        }
        String str = SystemProperties.get("settingsdebug.instant.packages");
        if (!(str == null || str.isEmpty() || applicationInfo.packageName == null || (split = str.split(NavigationBarInflaterView.BUTTON_SEPARATOR)) == null)) {
            for (String contains : split) {
                if (applicationInfo.packageName.contains(contains)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static CharSequence getApplicationLabel(PackageManager packageManager, String str) {
        return com.android.settingslib.utils.applications.AppUtils.getApplicationLabel(packageManager, str);
    }

    public static boolean isHiddenSystemModule(Context context, String str) {
        return ApplicationsState.getInstance((Application) context.getApplicationContext()).isHiddenModule(str);
    }

    public static boolean isSystemModule(Context context, String str) {
        return ApplicationsState.getInstance((Application) context.getApplicationContext()).isSystemModule(str);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:4|5|6) */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x001a, code lost:
        return r1.getPackageInfo(r2, 0).applicationInfo.sourceDir.startsWith(android.os.Environment.getApexDirectory().getAbsolutePath());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x001b, code lost:
        return false;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:4:0x0006 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isMainlineModule(android.content.pm.PackageManager r1, java.lang.String r2) {
        /*
            r0 = 0
            r1.getModuleInfo(r2, r0)     // Catch:{ NameNotFoundException -> 0x0006 }
            r1 = 1
            return r1
        L_0x0006:
            android.content.pm.PackageInfo r1 = r1.getPackageInfo(r2, r0)     // Catch:{ NameNotFoundException -> 0x001b }
            android.content.pm.ApplicationInfo r1 = r1.applicationInfo     // Catch:{ NameNotFoundException -> 0x001b }
            java.lang.String r1 = r1.sourceDir     // Catch:{ NameNotFoundException -> 0x001b }
            java.io.File r2 = android.os.Environment.getApexDirectory()     // Catch:{ NameNotFoundException -> 0x001b }
            java.lang.String r2 = r2.getAbsolutePath()     // Catch:{ NameNotFoundException -> 0x001b }
            boolean r1 = r1.startsWith(r2)     // Catch:{ NameNotFoundException -> 0x001b }
            return r1
        L_0x001b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.applications.AppUtils.isMainlineModule(android.content.pm.PackageManager, java.lang.String):boolean");
    }

    public static String getAppContentDescription(Context context, String str, int i) {
        return com.android.settingslib.utils.applications.AppUtils.getAppContentDescription(context, str, i);
    }

    public static boolean isBrowserApp(Context context, String str, int i) {
        Intent intent = sBrowserIntent;
        intent.setPackage(str);
        for (ResolveInfo resolveInfo : context.getPackageManager().queryIntentActivitiesAsUser(intent, 131072, i)) {
            if (resolveInfo.activityInfo != null && resolveInfo.handleAllWebDataURI) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDefaultBrowser(Context context, String str) {
        return TextUtils.equals(str, context.getPackageManager().getDefaultBrowserPackageNameAsUser(UserHandle.myUserId()));
    }

    public static Drawable getIcon(Context context, ApplicationsState.AppEntry appEntry) {
        if (appEntry == null || appEntry.info == null) {
            return null;
        }
        AppIconCacheManager instance = AppIconCacheManager.getInstance();
        String str = appEntry.info.packageName;
        int i = appEntry.info.uid;
        Drawable drawable = instance.get(str, i);
        if (drawable == null) {
            if (appEntry.apkFile == null || !appEntry.apkFile.exists()) {
                setAppEntryMounted(appEntry, false);
                return context.getDrawable(17303704);
            }
            Drawable badgedIcon = Utils.getBadgedIcon(context, appEntry.info);
            instance.put(str, i, badgedIcon);
            return badgedIcon;
        } else if (appEntry.mounted || appEntry.apkFile == null || !appEntry.apkFile.exists()) {
            return drawable;
        } else {
            setAppEntryMounted(appEntry, true);
            Drawable badgedIcon2 = Utils.getBadgedIcon(context, appEntry.info);
            instance.put(str, i, badgedIcon2);
            return badgedIcon2;
        }
    }

    public static Drawable getIconFromCache(ApplicationsState.AppEntry appEntry) {
        if (appEntry == null || appEntry.info == null) {
            return null;
        }
        return AppIconCacheManager.getInstance().get(appEntry.info.packageName, appEntry.info.uid);
    }

    public static void preloadTopIcons(Context context, ArrayList<ApplicationsState.AppEntry> arrayList, int i) {
        if (arrayList != null && !arrayList.isEmpty() && i > 0) {
            for (int i2 = 0; i2 < Math.min(arrayList.size(), i); i2++) {
                ThreadUtils.postOnBackgroundThread((Runnable) new AppUtils$$ExternalSyntheticLambda0(context, arrayList.get(i2)));
            }
        }
    }

    public static boolean isAppInstalled(ApplicationsState.AppEntry appEntry) {
        if (appEntry == null || appEntry.info == null || (appEntry.info.flags & 8388608) == 0) {
            return false;
        }
        return true;
    }

    private static void setAppEntryMounted(ApplicationsState.AppEntry appEntry, boolean z) {
        if (appEntry.mounted != z) {
            synchronized (appEntry) {
                appEntry.mounted = z;
            }
        }
    }
}
