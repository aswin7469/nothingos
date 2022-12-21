package com.android.settingslib.users;

import android.app.AppGlobals;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArraySet;
import androidx.core.content.IntentCompat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class AppCopyHelper {
    private static final boolean DEBUG = false;
    private static final String TAG = "AppCopyHelper";
    private final IPackageManager mIPm;
    private boolean mLeanback;
    private final PackageManager mPackageManager;
    private final ArraySet<String> mSelectedPackages;
    private final UserHandle mUser;
    private List<SelectableAppInfo> mVisibleApps;

    public AppCopyHelper(Context context, UserHandle userHandle) {
        this(new Injector(context, userHandle));
    }

    AppCopyHelper(Injector injector) {
        this.mSelectedPackages = new ArraySet<>();
        this.mPackageManager = injector.getPackageManager();
        this.mIPm = injector.getIPackageManager();
        this.mUser = injector.getUser();
    }

    public void setPackageSelected(String str, boolean z) {
        if (z) {
            this.mSelectedPackages.add(str);
        } else {
            this.mSelectedPackages.remove(str);
        }
    }

    public void resetSelectedPackages() {
        this.mSelectedPackages.clear();
    }

    public void setLeanback(boolean z) {
        this.mLeanback = z;
    }

    public List<SelectableAppInfo> getVisibleApps() {
        return this.mVisibleApps;
    }

    public void installSelectedApps() {
        for (int i = 0; i < this.mSelectedPackages.size(); i++) {
            installSelectedApp(this.mSelectedPackages.valueAt(i));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0020, code lost:
        if ((r3.flags & 8388608) != 0) goto L_0x0042;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void installSelectedApp(java.lang.String r13) {
        /*
            r12 = this;
            java.lang.String r0 = "Unhiding "
            java.lang.String r1 = "Installing "
            android.os.UserHandle r2 = r12.mUser
            int r2 = r2.getIdentifier()
            android.content.pm.IPackageManager r3 = r12.mIPm     // Catch:{ RemoteException -> 0x0065 }
            r4 = 4194304(0x400000, double:2.0722615E-317)
            android.content.pm.ApplicationInfo r3 = r3.getApplicationInfo(r13, r4, r2)     // Catch:{ RemoteException -> 0x0065 }
            r4 = 8388608(0x800000, float:1.17549435E-38)
            java.lang.String r5 = "AppCopyHelper"
            if (r3 == 0) goto L_0x0022
            boolean r6 = r3.enabled     // Catch:{ RemoteException -> 0x0065 }
            if (r6 == 0) goto L_0x0022
            int r6 = r3.flags     // Catch:{ RemoteException -> 0x0065 }
            r6 = r6 & r4
            if (r6 != 0) goto L_0x0042
        L_0x0022:
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ RemoteException -> 0x0065 }
            r6.<init>((java.lang.String) r1)     // Catch:{ RemoteException -> 0x0065 }
            java.lang.StringBuilder r1 = r6.append((java.lang.String) r13)     // Catch:{ RemoteException -> 0x0065 }
            java.lang.String r1 = r1.toString()     // Catch:{ RemoteException -> 0x0065 }
            android.util.Log.i(r5, r1)     // Catch:{ RemoteException -> 0x0065 }
            android.content.pm.IPackageManager r6 = r12.mIPm     // Catch:{ RemoteException -> 0x0065 }
            android.os.UserHandle r1 = r12.mUser     // Catch:{ RemoteException -> 0x0065 }
            int r8 = r1.getIdentifier()     // Catch:{ RemoteException -> 0x0065 }
            r9 = 4194304(0x400000, float:5.877472E-39)
            r10 = 0
            r11 = 0
            r7 = r13
            r6.installExistingPackageAsUser(r7, r8, r9, r10, r11)     // Catch:{ RemoteException -> 0x0065 }
        L_0x0042:
            if (r3 == 0) goto L_0x0065
            int r1 = r3.privateFlags     // Catch:{ RemoteException -> 0x0065 }
            r1 = r1 & 1
            if (r1 == 0) goto L_0x0065
            int r1 = r3.flags     // Catch:{ RemoteException -> 0x0065 }
            r1 = r1 & r4
            if (r1 == 0) goto L_0x0065
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ RemoteException -> 0x0065 }
            r1.<init>((java.lang.String) r0)     // Catch:{ RemoteException -> 0x0065 }
            java.lang.StringBuilder r0 = r1.append((java.lang.String) r13)     // Catch:{ RemoteException -> 0x0065 }
            java.lang.String r0 = r0.toString()     // Catch:{ RemoteException -> 0x0065 }
            android.util.Log.i(r5, r0)     // Catch:{ RemoteException -> 0x0065 }
            android.content.pm.IPackageManager r12 = r12.mIPm     // Catch:{ RemoteException -> 0x0065 }
            r0 = 0
            r12.setApplicationHiddenSettingAsUser(r13, r0, r2)     // Catch:{ RemoteException -> 0x0065 }
        L_0x0065:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.users.AppCopyHelper.installSelectedApp(java.lang.String):void");
    }

    public void fetchAndMergeApps() {
        this.mVisibleApps = new ArrayList();
        addCurrentUsersApps();
        removeSecondUsersApp();
    }

    private void addCurrentUsersApps() {
        addSystemApps(this.mVisibleApps, new Intent("android.intent.action.MAIN").addCategory(this.mLeanback ? IntentCompat.CATEGORY_LEANBACK_LAUNCHER : "android.intent.category.LAUNCHER"));
        addSystemApps(this.mVisibleApps, new Intent("android.appwidget.action.APPWIDGET_UPDATE"));
        for (ApplicationInfo next : this.mPackageManager.getInstalledApplications(0)) {
            if ((next.flags & 8388608) != 0 && (next.flags & 1) == 0 && (next.flags & 128) == 0) {
                SelectableAppInfo selectableAppInfo = new SelectableAppInfo();
                selectableAppInfo.packageName = next.packageName;
                selectableAppInfo.appName = next.loadLabel(this.mPackageManager);
                selectableAppInfo.icon = next.loadIcon(this.mPackageManager);
                this.mVisibleApps.add(selectableAppInfo);
            }
        }
        HashSet hashSet = new HashSet();
        for (int size = this.mVisibleApps.size() - 1; size >= 0; size--) {
            SelectableAppInfo selectableAppInfo2 = this.mVisibleApps.get(size);
            if (TextUtils.isEmpty(selectableAppInfo2.packageName) || !hashSet.contains(selectableAppInfo2.packageName)) {
                hashSet.add(selectableAppInfo2.packageName);
            } else {
                this.mVisibleApps.remove(size);
            }
        }
        this.mVisibleApps.sort(new AppLabelComparator());
    }

    private void removeSecondUsersApp() {
        HashSet hashSet = new HashSet();
        List installedApplicationsAsUser = this.mPackageManager.getInstalledApplicationsAsUser(8192, this.mUser.getIdentifier());
        for (int size = installedApplicationsAsUser.size() - 1; size >= 0; size--) {
            ApplicationInfo applicationInfo = (ApplicationInfo) installedApplicationsAsUser.get(size);
            if ((applicationInfo.flags & 8388608) != 0) {
                hashSet.add(applicationInfo.packageName);
            }
        }
        for (int size2 = this.mVisibleApps.size() - 1; size2 >= 0; size2--) {
            SelectableAppInfo selectableAppInfo = this.mVisibleApps.get(size2);
            if (!TextUtils.isEmpty(selectableAppInfo.packageName) && hashSet.contains(selectableAppInfo.packageName)) {
                this.mVisibleApps.remove(size2);
            }
        }
    }

    private void addSystemApps(List<SelectableAppInfo> list, Intent intent) {
        for (ResolveInfo next : this.mPackageManager.queryIntentActivities(intent, 0)) {
            if (!(next.activityInfo == null || next.activityInfo.applicationInfo == null)) {
                int i = next.activityInfo.applicationInfo.flags;
                if ((i & 1) != 0 || (i & 128) != 0) {
                    SelectableAppInfo selectableAppInfo = new SelectableAppInfo();
                    selectableAppInfo.packageName = next.activityInfo.packageName;
                    selectableAppInfo.appName = next.activityInfo.applicationInfo.loadLabel(this.mPackageManager);
                    selectableAppInfo.icon = next.activityInfo.loadIcon(this.mPackageManager);
                    list.add(selectableAppInfo);
                }
            }
        }
    }

    public static class SelectableAppInfo {
        public CharSequence appName;
        public Drawable icon;
        public String packageName;

        public String toString() {
            return this.packageName + ": appName=" + this.appName + "; icon=" + this.icon;
        }
    }

    private static class AppLabelComparator implements Comparator<SelectableAppInfo> {
        private AppLabelComparator() {
        }

        public int compare(SelectableAppInfo selectableAppInfo, SelectableAppInfo selectableAppInfo2) {
            return selectableAppInfo.appName.toString().toLowerCase().compareTo(selectableAppInfo2.appName.toString().toLowerCase());
        }
    }

    static class Injector {
        private final Context mContext;
        private final UserHandle mUser;

        Injector(Context context, UserHandle userHandle) {
            this.mContext = context;
            this.mUser = userHandle;
        }

        /* access modifiers changed from: package-private */
        public UserHandle getUser() {
            return this.mUser;
        }

        /* access modifiers changed from: package-private */
        public PackageManager getPackageManager() {
            return this.mContext.getPackageManager();
        }

        /* access modifiers changed from: package-private */
        public IPackageManager getIPackageManager() {
            return AppGlobals.getPackageManager();
        }
    }
}
