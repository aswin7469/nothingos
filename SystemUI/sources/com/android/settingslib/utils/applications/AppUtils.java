package com.android.settingslib.utils.applications;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.UserManager;
import android.util.Log;
import com.android.settingslib.utils.C1856R;

public class AppUtils {
    private static final String TAG = "AppUtils";

    public static CharSequence getApplicationLabel(PackageManager packageManager, String str) {
        try {
            return packageManager.getApplicationInfo(str, 4194816).loadLabel(packageManager);
        } catch (PackageManager.NameNotFoundException unused) {
            Log.w(TAG, "Unable to find info for package: " + str);
            return null;
        }
    }

    public static String getAppContentDescription(Context context, String str, int i) {
        CharSequence applicationLabel = getApplicationLabel(context.getPackageManager(), str);
        if (applicationLabel == null) {
            return "";
        }
        if (!((UserManager) context.getSystemService(UserManager.class)).isManagedProfile(i)) {
            return applicationLabel.toString();
        }
        return context.getString(C1856R.string.accessibility_work_profile_app_description, new Object[]{applicationLabel});
    }
}
