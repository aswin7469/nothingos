package com.android.settingslib.users;

import android.app.AppGlobals;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppRestrictionsHelper {
    private static final boolean DEBUG = false;
    private static final String TAG = "AppRestrictionsHelper";
    private final Context mContext;
    private final IPackageManager mIPm;
    private final Injector mInjector;
    private boolean mLeanback;
    private final PackageManager mPackageManager;
    private final boolean mRestrictedProfile;
    HashMap<String, Boolean> mSelectedPackages;
    private final UserHandle mUser;
    private final UserManager mUserManager;
    private List<SelectableAppInfo> mVisibleApps;

    public interface OnDisableUiForPackageListener {
        void onDisableUiForPackage(String str);
    }

    public AppRestrictionsHelper(Context context, UserHandle userHandle) {
        this(new Injector(context, userHandle));
    }

    AppRestrictionsHelper(Injector injector) {
        this.mSelectedPackages = new HashMap<>();
        this.mInjector = injector;
        this.mContext = injector.getContext();
        this.mPackageManager = injector.getPackageManager();
        this.mIPm = injector.getIPackageManager();
        UserHandle user = injector.getUser();
        this.mUser = user;
        UserManager userManager = injector.getUserManager();
        this.mUserManager = userManager;
        this.mRestrictedProfile = userManager.getUserInfo(user.getIdentifier()).isRestricted();
    }

    public void setPackageSelected(String str, boolean z) {
        this.mSelectedPackages.put(str, Boolean.valueOf(z));
    }

    public boolean isPackageSelected(String str) {
        return this.mSelectedPackages.get(str).booleanValue();
    }

    public void setLeanback(boolean z) {
        this.mLeanback = z;
    }

    public List<SelectableAppInfo> getVisibleApps() {
        return this.mVisibleApps;
    }

    public void applyUserAppsStates(OnDisableUiForPackageListener onDisableUiForPackageListener) {
        if (this.mRestrictedProfile || this.mUser.getIdentifier() == UserHandle.myUserId()) {
            for (Map.Entry next : this.mSelectedPackages.entrySet()) {
                applyUserAppState((String) next.getKey(), ((Boolean) next.getValue()).booleanValue(), onDisableUiForPackageListener);
            }
            return;
        }
        Log.e(TAG, "Cannot apply application restrictions on another user!");
    }

    public void applyUserAppState(String str, boolean z, OnDisableUiForPackageListener onDisableUiForPackageListener) {
        int identifier = this.mUser.getIdentifier();
        if (z) {
            try {
                ApplicationInfo applicationInfo = this.mIPm.getApplicationInfo(str, 4194304, identifier);
                if (applicationInfo == null || !applicationInfo.enabled || (applicationInfo.flags & 8388608) == 0) {
                    this.mIPm.installExistingPackageAsUser(str, this.mUser.getIdentifier(), 4194304, 0, (List) null);
                }
                if (applicationInfo != null && (1 & applicationInfo.privateFlags) != 0 && (applicationInfo.flags & 8388608) != 0) {
                    onDisableUiForPackageListener.onDisableUiForPackage(str);
                    this.mIPm.setApplicationHiddenSettingAsUser(str, false, identifier);
                }
            } catch (RemoteException unused) {
            }
        } else if (this.mIPm.getApplicationInfo(str, 0, identifier) == null) {
        } else {
            if (this.mRestrictedProfile) {
                this.mPackageManager.deletePackageAsUser(str, (IPackageDeleteObserver) null, 4, this.mUser.getIdentifier());
                return;
            }
            onDisableUiForPackageListener.onDisableUiForPackage(str);
            this.mIPm.setApplicationHiddenSettingAsUser(str, true, identifier);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x00b9  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0113  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0169  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void fetchAndMergeApps() {
        /*
            r7 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r7.mVisibleApps = r0
            android.content.pm.PackageManager r0 = r7.mPackageManager
            android.content.pm.IPackageManager r1 = r7.mIPm
            java.util.HashSet r2 = new java.util.HashSet
            r2.<init>()
            r7.addSystemImes(r2)
            android.content.Intent r3 = new android.content.Intent
            java.lang.String r4 = "android.intent.action.MAIN"
            r3.<init>(r4)
            boolean r4 = r7.mLeanback
            if (r4 == 0) goto L_0x0024
            java.lang.String r4 = "android.intent.category.LEANBACK_LAUNCHER"
            r3.addCategory(r4)
            goto L_0x0029
        L_0x0024:
            java.lang.String r4 = "android.intent.category.LAUNCHER"
            r3.addCategory(r4)
        L_0x0029:
            java.util.List<com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo> r4 = r7.mVisibleApps
            r7.addSystemApps(r4, r3, r2)
            android.content.Intent r3 = new android.content.Intent
            java.lang.String r4 = "android.appwidget.action.APPWIDGET_UPDATE"
            r3.<init>(r4)
            java.util.List<com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo> r4 = r7.mVisibleApps
            r7.addSystemApps(r4, r3, r2)
            r2 = 4194304(0x400000, float:5.877472E-39)
            java.util.List r2 = r0.getInstalledApplications(r2)
            java.util.Iterator r2 = r2.iterator()
        L_0x0044:
            boolean r3 = r2.hasNext()
            r4 = 8388608(0x800000, float:1.17549435E-38)
            if (r3 == 0) goto L_0x00a2
            java.lang.Object r3 = r2.next()
            android.content.pm.ApplicationInfo r3 = (android.content.pm.ApplicationInfo) r3
            int r5 = r3.flags
            r4 = r4 & r5
            if (r4 != 0) goto L_0x0058
            goto L_0x0044
        L_0x0058:
            int r4 = r3.flags
            r4 = r4 & 1
            if (r4 != 0) goto L_0x0083
            int r4 = r3.flags
            r4 = r4 & 128(0x80, float:1.794E-43)
            if (r4 != 0) goto L_0x0083
            com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo r4 = new com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo
            r4.<init>()
            java.lang.String r5 = r3.packageName
            r4.packageName = r5
            java.lang.CharSequence r5 = r3.loadLabel(r0)
            r4.appName = r5
            java.lang.CharSequence r5 = r4.appName
            r4.activityName = r5
            android.graphics.drawable.Drawable r3 = r3.loadIcon(r0)
            r4.icon = r3
            java.util.List<com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo> r3 = r7.mVisibleApps
            r3.add(r4)
            goto L_0x0044
        L_0x0083:
            java.lang.String r4 = r3.packageName     // Catch:{ NameNotFoundException -> 0x0044 }
            r5 = 0
            android.content.pm.PackageInfo r4 = r0.getPackageInfo(r4, r5)     // Catch:{ NameNotFoundException -> 0x0044 }
            boolean r6 = r7.mRestrictedProfile     // Catch:{ NameNotFoundException -> 0x0044 }
            if (r6 == 0) goto L_0x0044
            java.lang.String r6 = r4.requiredAccountType     // Catch:{ NameNotFoundException -> 0x0044 }
            if (r6 == 0) goto L_0x0044
            java.lang.String r4 = r4.restrictedAccountType     // Catch:{ NameNotFoundException -> 0x0044 }
            if (r4 != 0) goto L_0x0044
            java.util.HashMap<java.lang.String, java.lang.Boolean> r4 = r7.mSelectedPackages     // Catch:{ NameNotFoundException -> 0x0044 }
            java.lang.String r3 = r3.packageName     // Catch:{ NameNotFoundException -> 0x0044 }
            java.lang.Boolean r5 = java.lang.Boolean.valueOf((boolean) r5)     // Catch:{ NameNotFoundException -> 0x0044 }
            r4.put(r3, r5)     // Catch:{ NameNotFoundException -> 0x0044 }
            goto L_0x0044
        L_0x00a2:
            r2 = 0
            android.os.UserHandle r3 = r7.mUser     // Catch:{ RemoteException -> 0x00b6 }
            int r3 = r3.getIdentifier()     // Catch:{ RemoteException -> 0x00b6 }
            r5 = 8192(0x2000, double:4.0474E-320)
            android.content.pm.ParceledListSlice r1 = r1.getInstalledApplications(r5, r3)     // Catch:{ RemoteException -> 0x00b6 }
            if (r1 == 0) goto L_0x00b6
            java.util.List r1 = r1.getList()     // Catch:{ RemoteException -> 0x00b6 }
            goto L_0x00b7
        L_0x00b6:
            r1 = r2
        L_0x00b7:
            if (r1 == 0) goto L_0x00fa
            java.util.Iterator r1 = r1.iterator()
        L_0x00bd:
            boolean r3 = r1.hasNext()
            if (r3 == 0) goto L_0x00fa
            java.lang.Object r3 = r1.next()
            android.content.pm.ApplicationInfo r3 = (android.content.pm.ApplicationInfo) r3
            int r5 = r3.flags
            r5 = r5 & r4
            if (r5 != 0) goto L_0x00cf
            goto L_0x00bd
        L_0x00cf:
            int r5 = r3.flags
            r5 = r5 & 1
            if (r5 != 0) goto L_0x00bd
            int r5 = r3.flags
            r5 = r5 & 128(0x80, float:1.794E-43)
            if (r5 != 0) goto L_0x00bd
            com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo r5 = new com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo
            r5.<init>()
            java.lang.String r6 = r3.packageName
            r5.packageName = r6
            java.lang.CharSequence r6 = r3.loadLabel(r0)
            r5.appName = r6
            java.lang.CharSequence r6 = r5.appName
            r5.activityName = r6
            android.graphics.drawable.Drawable r3 = r3.loadIcon(r0)
            r5.icon = r3
            java.util.List<com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo> r3 = r7.mVisibleApps
            r3.add(r5)
            goto L_0x00bd
        L_0x00fa:
            java.util.List<com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo> r0 = r7.mVisibleApps
            com.android.settingslib.users.AppRestrictionsHelper$AppLabelComparator r1 = new com.android.settingslib.users.AppRestrictionsHelper$AppLabelComparator
            r1.<init>()
            java.util.Collections.sort(r0, r1)
            java.util.HashSet r0 = new java.util.HashSet
            r0.<init>()
            java.util.List<com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo> r1 = r7.mVisibleApps
            int r1 = r1.size()
            int r1 = r1 + -1
        L_0x0111:
            if (r1 < 0) goto L_0x0158
            java.util.List<com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo> r2 = r7.mVisibleApps
            java.lang.Object r2 = r2.get(r1)
            com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo r2 = (com.android.settingslib.users.AppRestrictionsHelper.SelectableAppInfo) r2
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = r2.packageName
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)
            java.lang.String r4 = "+"
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)
            java.lang.CharSequence r4 = r2.activityName
            java.lang.StringBuilder r3 = r3.append((java.lang.Object) r4)
            java.lang.String r3 = r3.toString()
            java.lang.String r4 = r2.packageName
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 != 0) goto L_0x0152
            java.lang.CharSequence r2 = r2.activityName
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L_0x0152
            boolean r2 = r0.contains(r3)
            if (r2 == 0) goto L_0x0152
            java.util.List<com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo> r2 = r7.mVisibleApps
            r2.remove((int) r1)
            goto L_0x0155
        L_0x0152:
            r0.add(r3)
        L_0x0155:
            int r1 = r1 + -1
            goto L_0x0111
        L_0x0158:
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.util.List<com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo> r7 = r7.mVisibleApps
            java.util.Iterator r7 = r7.iterator()
        L_0x0163:
            boolean r1 = r7.hasNext()
            if (r1 == 0) goto L_0x0188
            java.lang.Object r1 = r7.next()
            com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo r1 = (com.android.settingslib.users.AppRestrictionsHelper.SelectableAppInfo) r1
            java.lang.String r2 = r1.packageName
            boolean r2 = r0.containsKey(r2)
            if (r2 == 0) goto L_0x0182
            java.lang.String r2 = r1.packageName
            java.lang.Object r2 = r0.get(r2)
            com.android.settingslib.users.AppRestrictionsHelper$SelectableAppInfo r2 = (com.android.settingslib.users.AppRestrictionsHelper.SelectableAppInfo) r2
            r1.primaryEntry = r2
            goto L_0x0163
        L_0x0182:
            java.lang.String r2 = r1.packageName
            r0.put(r2, r1)
            goto L_0x0163
        L_0x0188:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.users.AppRestrictionsHelper.fetchAndMergeApps():void");
    }

    private void addSystemImes(Set<String> set) {
        for (InputMethodInfo next : this.mInjector.getInputMethodList()) {
            try {
                if (next.isDefault(this.mContext) && isSystemPackage(next.getPackageName())) {
                    set.add(next.getPackageName());
                }
            } catch (Resources.NotFoundException unused) {
            }
        }
    }

    private void addSystemApps(List<SelectableAppInfo> list, Intent intent, Set<String> set) {
        ApplicationInfo appInfoForUser;
        PackageManager packageManager = this.mPackageManager;
        for (ResolveInfo next : packageManager.queryIntentActivities(intent, 8704)) {
            if (!(next.activityInfo == null || next.activityInfo.applicationInfo == null)) {
                String str = next.activityInfo.packageName;
                int i = next.activityInfo.applicationInfo.flags;
                if (!((i & 1) == 0 && (i & 128) == 0) && !set.contains(str)) {
                    int applicationEnabledSetting = packageManager.getApplicationEnabledSetting(str);
                    if (!((applicationEnabledSetting == 4 || applicationEnabledSetting == 2) && ((appInfoForUser = getAppInfoForUser(str, 0, this.mUser)) == null || (appInfoForUser.flags & 8388608) == 0))) {
                        SelectableAppInfo selectableAppInfo = new SelectableAppInfo();
                        selectableAppInfo.packageName = next.activityInfo.packageName;
                        selectableAppInfo.appName = next.activityInfo.applicationInfo.loadLabel(packageManager);
                        selectableAppInfo.icon = next.activityInfo.loadIcon(packageManager);
                        selectableAppInfo.activityName = next.activityInfo.loadLabel(packageManager);
                        if (selectableAppInfo.activityName == null) {
                            selectableAppInfo.activityName = selectableAppInfo.appName;
                        }
                        list.add(selectableAppInfo);
                    }
                }
            }
        }
    }

    private boolean isSystemPackage(String str) {
        try {
            PackageInfo packageInfo = this.mPackageManager.getPackageInfo(str, 0);
            if (packageInfo.applicationInfo == null) {
                return false;
            }
            int i = packageInfo.applicationInfo.flags;
            if ((i & 1) == 0 && (i & 128) == 0) {
                return false;
            }
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
        }
    }

    private ApplicationInfo getAppInfoForUser(String str, int i, UserHandle userHandle) {
        try {
            return this.mIPm.getApplicationInfo(str, (long) i, userHandle.getIdentifier());
        } catch (RemoteException unused) {
            return null;
        }
    }

    public static class SelectableAppInfo {
        public CharSequence activityName;
        public CharSequence appName;
        public Drawable icon;
        public String packageName;
        public SelectableAppInfo primaryEntry;

        public String toString() {
            return this.packageName + ": appName=" + this.appName + "; activityName=" + this.activityName + "; icon=" + this.icon + "; primaryEntry=" + this.primaryEntry;
        }
    }

    private static class AppLabelComparator implements Comparator<SelectableAppInfo> {
        private AppLabelComparator() {
        }

        public int compare(SelectableAppInfo selectableAppInfo, SelectableAppInfo selectableAppInfo2) {
            return selectableAppInfo.activityName.toString().toLowerCase().compareTo(selectableAppInfo2.activityName.toString().toLowerCase());
        }
    }

    static class Injector {
        private Context mContext;
        private UserHandle mUser;

        Injector(Context context, UserHandle userHandle) {
            this.mContext = context;
            this.mUser = userHandle;
        }

        /* access modifiers changed from: package-private */
        public Context getContext() {
            return this.mContext;
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

        /* access modifiers changed from: package-private */
        public UserManager getUserManager() {
            return (UserManager) this.mContext.getSystemService(UserManager.class);
        }

        /* access modifiers changed from: package-private */
        public List<InputMethodInfo> getInputMethodList() {
            return ((InputMethodManager) getContext().getSystemService("input_method")).getInputMethodListAsUser(this.mUser.getIdentifier());
        }
    }
}
