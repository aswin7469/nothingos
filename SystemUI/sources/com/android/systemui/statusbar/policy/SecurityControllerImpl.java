package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.VpnManager;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.net.LegacyVpnInfo;
import com.android.internal.net.VpnConfig;
import com.android.systemui.C1893R;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.policy.SecurityController;
import java.p026io.IOException;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import org.xmlpull.p032v1.XmlPullParserException;

@SysUISingleton
public class SecurityControllerImpl extends CurrentUserTracker implements SecurityController {
    private static final int CA_CERT_LOADING_RETRY_TIME_IN_MS = 30000;
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final int NO_NETWORK = -1;
    private static final NetworkRequest REQUEST = new NetworkRequest.Builder().clearCapabilities().build();
    private static final String TAG = "SecurityController";
    private static final String VPN_BRANDED_META_DATA = "com.android.systemui.IS_BRANDED";
    private final Executor mBgExecutor;
    private final BroadcastReceiver mBroadcastReceiver;
    private final ArrayList<SecurityController.SecurityControllerCallback> mCallbacks = new ArrayList<>();
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private int mCurrentUserId;
    private SparseArray<VpnConfig> mCurrentVpns = new SparseArray<>();
    private final DevicePolicyManager mDevicePolicyManager;
    private ArrayMap<Integer, Boolean> mHasCACerts = new ArrayMap<>();
    private final ConnectivityManager.NetworkCallback mNetworkCallback;
    private final PackageManager mPackageManager;
    private final UserManager mUserManager;
    private final VpnManager mVpnManager;
    private int mVpnUserId;

    @Inject
    public SecurityControllerImpl(Context context, @Background Handler handler, BroadcastDispatcher broadcastDispatcher, @Background Executor executor, DumpManager dumpManager) {
        super(broadcastDispatcher);
        C31831 r0 = new ConnectivityManager.NetworkCallback() {
            public void onAvailable(Network network) {
                if (SecurityControllerImpl.DEBUG) {
                    Log.d(SecurityControllerImpl.TAG, "onAvailable " + network.getNetId());
                }
                SecurityControllerImpl.this.updateState();
                SecurityControllerImpl.this.fireCallbacks();
            }

            public void onLost(Network network) {
                if (SecurityControllerImpl.DEBUG) {
                    Log.d(SecurityControllerImpl.TAG, "onLost " + network.getNetId());
                }
                SecurityControllerImpl.this.updateState();
                SecurityControllerImpl.this.fireCallbacks();
            }
        };
        this.mNetworkCallback = r0;
        C31842 r1 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int intExtra;
                if ("android.security.action.TRUST_STORE_CHANGED".equals(intent.getAction())) {
                    SecurityControllerImpl.this.refreshCACerts(getSendingUserId());
                } else if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction()) && (intExtra = intent.getIntExtra("android.intent.extra.user_handle", -10000)) != -10000) {
                    SecurityControllerImpl.this.refreshCACerts(intExtra);
                }
            }
        };
        this.mBroadcastReceiver = r1;
        this.mContext = context;
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        this.mConnectivityManager = connectivityManager;
        this.mVpnManager = (VpnManager) context.getSystemService(VpnManager.class);
        this.mPackageManager = context.getPackageManager();
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mBgExecutor = executor;
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.security.action.TRUST_STORE_CHANGED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        broadcastDispatcher.registerReceiverWithHandler(r1, intentFilter, handler, UserHandle.ALL);
        connectivityManager.registerNetworkCallback(REQUEST, (ConnectivityManager.NetworkCallback) r0);
        onUserSwitched(ActivityManager.getCurrentUser());
        startTracking();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("SecurityController state:");
        printWriter.print("  mCurrentVpns={");
        for (int i = 0; i < this.mCurrentVpns.size(); i++) {
            if (i > 0) {
                printWriter.print(", ");
            }
            printWriter.print(this.mCurrentVpns.keyAt(i));
            printWriter.print('=');
            printWriter.print(this.mCurrentVpns.valueAt(i).user);
        }
        printWriter.println("}");
    }

    public boolean isDeviceManaged() {
        return this.mDevicePolicyManager.isDeviceManaged();
    }

    public String getDeviceOwnerName() {
        return this.mDevicePolicyManager.getDeviceOwnerNameOnAnyUser();
    }

    public boolean hasProfileOwner() {
        return this.mDevicePolicyManager.getProfileOwnerAsUser(this.mCurrentUserId) != null;
    }

    public String getProfileOwnerName() {
        for (int profileOwnerNameAsUser : this.mUserManager.getProfileIdsWithDisabled(this.mCurrentUserId)) {
            String profileOwnerNameAsUser2 = this.mDevicePolicyManager.getProfileOwnerNameAsUser(profileOwnerNameAsUser);
            if (profileOwnerNameAsUser2 != null) {
                return profileOwnerNameAsUser2;
            }
        }
        return null;
    }

    public CharSequence getDeviceOwnerOrganizationName() {
        return this.mDevicePolicyManager.getDeviceOwnerOrganizationName();
    }

    public CharSequence getWorkProfileOrganizationName() {
        int workProfileUserId = getWorkProfileUserId(this.mCurrentUserId);
        if (workProfileUserId == -10000) {
            return null;
        }
        return this.mDevicePolicyManager.getOrganizationNameForUser(workProfileUserId);
    }

    public String getPrimaryVpnName() {
        VpnConfig vpnConfig = this.mCurrentVpns.get(this.mVpnUserId);
        if (vpnConfig != null) {
            return getNameForVpnConfig(vpnConfig, new UserHandle(this.mVpnUserId));
        }
        return null;
    }

    private int getWorkProfileUserId(int i) {
        for (UserInfo userInfo : this.mUserManager.getProfiles(i)) {
            if (userInfo.isManagedProfile()) {
                return userInfo.id;
            }
        }
        return -10000;
    }

    public boolean hasWorkProfile() {
        return getWorkProfileUserId(this.mCurrentUserId) != -10000;
    }

    public boolean isWorkProfileOn() {
        UserHandle of = UserHandle.of(getWorkProfileUserId(this.mCurrentUserId));
        return of != null && !this.mUserManager.isQuietModeEnabled(of);
    }

    public boolean isProfileOwnerOfOrganizationOwnedDevice() {
        return this.mDevicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile();
    }

    public String getWorkProfileVpnName() {
        VpnConfig vpnConfig;
        int workProfileUserId = getWorkProfileUserId(this.mVpnUserId);
        if (workProfileUserId == -10000 || (vpnConfig = this.mCurrentVpns.get(workProfileUserId)) == null) {
            return null;
        }
        return getNameForVpnConfig(vpnConfig, UserHandle.of(workProfileUserId));
    }

    public ComponentName getDeviceOwnerComponentOnAnyUser() {
        return this.mDevicePolicyManager.getDeviceOwnerComponentOnAnyUser();
    }

    public int getDeviceOwnerType(ComponentName componentName) {
        return this.mDevicePolicyManager.getDeviceOwnerType(componentName);
    }

    public boolean isNetworkLoggingEnabled() {
        return this.mDevicePolicyManager.isNetworkLoggingEnabled((ComponentName) null);
    }

    public boolean isVpnEnabled() {
        for (int i : this.mUserManager.getProfileIdsWithDisabled(this.mVpnUserId)) {
            if (this.mCurrentVpns.get(i) != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isVpnRestricted() {
        return this.mUserManager.getUserInfo(this.mCurrentUserId).isRestricted() || this.mUserManager.hasUserRestriction("no_config_vpn", new UserHandle(this.mCurrentUserId));
    }

    public boolean isVpnBranded() {
        String packageNameForVpnConfig;
        VpnConfig vpnConfig = this.mCurrentVpns.get(this.mVpnUserId);
        if (vpnConfig == null || (packageNameForVpnConfig = getPackageNameForVpnConfig(vpnConfig)) == null) {
            return false;
        }
        return isVpnPackageBranded(packageNameForVpnConfig);
    }

    public boolean hasCACertInCurrentUser() {
        Boolean bool = this.mHasCACerts.get(Integer.valueOf(this.mCurrentUserId));
        return bool != null && bool.booleanValue();
    }

    public boolean hasCACertInWorkProfile() {
        Boolean bool;
        int workProfileUserId = getWorkProfileUserId(this.mCurrentUserId);
        if (workProfileUserId == -10000 || (bool = this.mHasCACerts.get(Integer.valueOf(workProfileUserId))) == null || !bool.booleanValue()) {
            return false;
        }
        return true;
    }

    public void removeCallback(SecurityController.SecurityControllerCallback securityControllerCallback) {
        synchronized (this.mCallbacks) {
            if (securityControllerCallback != null) {
                if (DEBUG) {
                    Log.d(TAG, "removeCallback " + securityControllerCallback);
                }
                this.mCallbacks.remove((Object) securityControllerCallback);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002e, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addCallback(com.android.systemui.statusbar.policy.SecurityController.SecurityControllerCallback r5) {
        /*
            r4 = this;
            java.lang.String r0 = "addCallback "
            java.util.ArrayList<com.android.systemui.statusbar.policy.SecurityController$SecurityControllerCallback> r1 = r4.mCallbacks
            monitor-enter(r1)
            if (r5 == 0) goto L_0x002d
            java.util.ArrayList<com.android.systemui.statusbar.policy.SecurityController$SecurityControllerCallback> r2 = r4.mCallbacks     // Catch:{ all -> 0x002f }
            boolean r2 = r2.contains(r5)     // Catch:{ all -> 0x002f }
            if (r2 == 0) goto L_0x0010
            goto L_0x002d
        L_0x0010:
            boolean r2 = DEBUG     // Catch:{ all -> 0x002f }
            if (r2 == 0) goto L_0x0026
            java.lang.String r2 = "SecurityController"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x002f }
            r3.<init>((java.lang.String) r0)     // Catch:{ all -> 0x002f }
            java.lang.StringBuilder r0 = r3.append((java.lang.Object) r5)     // Catch:{ all -> 0x002f }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x002f }
            android.util.Log.d(r2, r0)     // Catch:{ all -> 0x002f }
        L_0x0026:
            java.util.ArrayList<com.android.systemui.statusbar.policy.SecurityController$SecurityControllerCallback> r4 = r4.mCallbacks     // Catch:{ all -> 0x002f }
            r4.add(r5)     // Catch:{ all -> 0x002f }
            monitor-exit(r1)     // Catch:{ all -> 0x002f }
            return
        L_0x002d:
            monitor-exit(r1)     // Catch:{ all -> 0x002f }
            return
        L_0x002f:
            r4 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x002f }
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.SecurityControllerImpl.addCallback(com.android.systemui.statusbar.policy.SecurityController$SecurityControllerCallback):void");
    }

    public void onUserSwitched(int i) {
        this.mCurrentUserId = i;
        UserInfo userInfo = this.mUserManager.getUserInfo(i);
        if (userInfo.isRestricted()) {
            this.mVpnUserId = userInfo.restrictedProfileParentId;
        } else {
            this.mVpnUserId = this.mCurrentUserId;
        }
        fireCallbacks();
    }

    public boolean isParentalControlsEnabled() {
        return getProfileOwnerOrDeviceOwnerSupervisionComponent() != null;
    }

    public DeviceAdminInfo getDeviceAdminInfo() {
        return getDeviceAdminInfo(getProfileOwnerOrDeviceOwnerComponent());
    }

    public Drawable getIcon(DeviceAdminInfo deviceAdminInfo) {
        if (deviceAdminInfo == null) {
            return null;
        }
        return deviceAdminInfo.loadIcon(this.mPackageManager);
    }

    public CharSequence getLabel(DeviceAdminInfo deviceAdminInfo) {
        if (deviceAdminInfo == null) {
            return null;
        }
        return deviceAdminInfo.loadLabel(this.mPackageManager);
    }

    private ComponentName getProfileOwnerOrDeviceOwnerSupervisionComponent() {
        return this.mDevicePolicyManager.getProfileOwnerOrDeviceOwnerSupervisionComponent(new UserHandle(this.mCurrentUserId));
    }

    private ComponentName getProfileOwnerOrDeviceOwnerComponent() {
        return getProfileOwnerOrDeviceOwnerSupervisionComponent();
    }

    private DeviceAdminInfo getDeviceAdminInfo(ComponentName componentName) {
        try {
            ResolveInfo resolveInfo = new ResolveInfo();
            resolveInfo.activityInfo = this.mPackageManager.getReceiverInfo(componentName, 128);
            return new DeviceAdminInfo(this.mContext, resolveInfo);
        } catch (PackageManager.NameNotFoundException | IOException | XmlPullParserException unused) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void refreshCACerts(int i) {
        this.mBgExecutor.execute(new SecurityControllerImpl$$ExternalSyntheticLambda0(this, i));
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00a7  */
    /* renamed from: lambda$refreshCACerts$0$com-android-systemui-statusbar-policy-SecurityControllerImpl */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void mo46070xc8cf2670(int r8) {
        /*
            r7 = this;
            java.lang.String r0 = "Refreshing CA Certs "
            java.lang.String r1 = "SecurityController"
            r2 = 0
            android.content.Context r3 = r7.mContext     // Catch:{ RemoteException | AssertionError | InterruptedException -> 0x0068, all -> 0x0066 }
            android.os.UserHandle r4 = android.os.UserHandle.of(r8)     // Catch:{ RemoteException | AssertionError | InterruptedException -> 0x0068, all -> 0x0066 }
            android.security.KeyChain$KeyChainConnection r3 = android.security.KeyChain.bindAsUser(r3, r4)     // Catch:{ RemoteException | AssertionError | InterruptedException -> 0x0068, all -> 0x0066 }
            android.security.IKeyChainService r4 = r3.getService()     // Catch:{ all -> 0x005a }
            android.content.pm.StringParceledListSlice r4 = r4.getUserCaAliases()     // Catch:{ all -> 0x005a }
            java.util.List r4 = r4.getList()     // Catch:{ all -> 0x005a }
            boolean r4 = r4.isEmpty()     // Catch:{ all -> 0x005a }
            if (r4 != 0) goto L_0x0023
            r4 = 1
            goto L_0x0024
        L_0x0023:
            r4 = 0
        L_0x0024:
            android.util.Pair r5 = new android.util.Pair     // Catch:{ all -> 0x005a }
            java.lang.Integer r6 = java.lang.Integer.valueOf((int) r8)     // Catch:{ all -> 0x005a }
            java.lang.Boolean r4 = java.lang.Boolean.valueOf((boolean) r4)     // Catch:{ all -> 0x005a }
            r5.<init>(r6, r4)     // Catch:{ all -> 0x005a }
            if (r3 == 0) goto L_0x0039
            r3.close()     // Catch:{ RemoteException | AssertionError | InterruptedException -> 0x0037 }
            goto L_0x0039
        L_0x0037:
            r3 = move-exception
            goto L_0x006a
        L_0x0039:
            boolean r8 = DEBUG
            if (r8 == 0) goto L_0x004d
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>((java.lang.String) r0)
            java.lang.StringBuilder r8 = r8.append((java.lang.Object) r5)
            java.lang.String r8 = r8.toString()
            android.util.Log.d(r1, r8)
        L_0x004d:
            java.lang.Object r8 = r5.second
            if (r8 == 0) goto L_0x00a0
            android.util.ArrayMap<java.lang.Integer, java.lang.Boolean> r8 = r7.mHasCACerts
            java.lang.Object r0 = r5.first
            java.lang.Integer r0 = (java.lang.Integer) r0
            java.lang.Object r1 = r5.second
            goto L_0x0098
        L_0x005a:
            r4 = move-exception
            if (r3 == 0) goto L_0x0065
            r3.close()     // Catch:{ all -> 0x0061 }
            goto L_0x0065
        L_0x0061:
            r3 = move-exception
            r4.addSuppressed(r3)     // Catch:{ RemoteException | AssertionError | InterruptedException -> 0x0068, all -> 0x0066 }
        L_0x0065:
            throw r4     // Catch:{ RemoteException | AssertionError | InterruptedException -> 0x0068, all -> 0x0066 }
        L_0x0066:
            r8 = move-exception
            goto L_0x00a3
        L_0x0068:
            r3 = move-exception
            r5 = r2
        L_0x006a:
            java.lang.String r4 = "failed to get CA certs"
            android.util.Log.i(r1, r4, r3)     // Catch:{ all -> 0x00a1 }
            android.util.Pair r3 = new android.util.Pair     // Catch:{ all -> 0x00a1 }
            java.lang.Integer r8 = java.lang.Integer.valueOf((int) r8)     // Catch:{ all -> 0x00a1 }
            r3.<init>(r8, r2)     // Catch:{ all -> 0x00a1 }
            boolean r8 = DEBUG
            if (r8 == 0) goto L_0x008c
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>((java.lang.String) r0)
            java.lang.StringBuilder r8 = r8.append((java.lang.Object) r3)
            java.lang.String r8 = r8.toString()
            android.util.Log.d(r1, r8)
        L_0x008c:
            java.lang.Object r8 = r3.second
            if (r8 == 0) goto L_0x00a0
            android.util.ArrayMap<java.lang.Integer, java.lang.Boolean> r8 = r7.mHasCACerts
            java.lang.Object r0 = r3.first
            java.lang.Integer r0 = (java.lang.Integer) r0
            java.lang.Object r1 = r3.second
        L_0x0098:
            java.lang.Boolean r1 = (java.lang.Boolean) r1
            r8.put(r0, r1)
            r7.fireCallbacks()
        L_0x00a0:
            return
        L_0x00a1:
            r8 = move-exception
            r2 = r5
        L_0x00a3:
            boolean r3 = DEBUG
            if (r3 == 0) goto L_0x00b7
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r0)
            java.lang.StringBuilder r0 = r3.append((java.lang.Object) r2)
            java.lang.String r0 = r0.toString()
            android.util.Log.d(r1, r0)
        L_0x00b7:
            if (r2 == 0) goto L_0x00cd
            java.lang.Object r0 = r2.second
            if (r0 == 0) goto L_0x00cd
            android.util.ArrayMap<java.lang.Integer, java.lang.Boolean> r0 = r7.mHasCACerts
            java.lang.Object r1 = r2.first
            java.lang.Integer r1 = (java.lang.Integer) r1
            java.lang.Object r2 = r2.second
            java.lang.Boolean r2 = (java.lang.Boolean) r2
            r0.put(r1, r2)
            r7.fireCallbacks()
        L_0x00cd:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.SecurityControllerImpl.mo46070xc8cf2670(int):void");
    }

    private String getNameForVpnConfig(VpnConfig vpnConfig, UserHandle userHandle) {
        if (vpnConfig.legacy) {
            return this.mContext.getString(C1893R.string.legacy_vpn_name);
        }
        String str = vpnConfig.user;
        try {
            Context context = this.mContext;
            return VpnConfig.getVpnLabel(context.createPackageContextAsUser(context.getPackageName(), 0, userHandle), str).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Package " + str + " is not present", e);
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void fireCallbacks() {
        synchronized (this.mCallbacks) {
            Iterator<SecurityController.SecurityControllerCallback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onStateChanged();
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateState() {
        LegacyVpnInfo legacyVpnInfo;
        SparseArray<VpnConfig> sparseArray = new SparseArray<>();
        for (UserInfo userInfo : this.mUserManager.getUsers()) {
            VpnConfig vpnConfig = this.mVpnManager.getVpnConfig(userInfo.id);
            if (vpnConfig != null && (!vpnConfig.legacy || ((legacyVpnInfo = this.mVpnManager.getLegacyVpnInfo(userInfo.id)) != null && legacyVpnInfo.state == 3))) {
                sparseArray.put(userInfo.id, vpnConfig);
            }
        }
        this.mCurrentVpns = sparseArray;
    }

    private String getPackageNameForVpnConfig(VpnConfig vpnConfig) {
        if (vpnConfig.legacy) {
            return null;
        }
        return vpnConfig.user;
    }

    private boolean isVpnPackageBranded(String str) {
        try {
            ApplicationInfo applicationInfo = this.mPackageManager.getApplicationInfo(str, 128);
            if (!(applicationInfo == null || applicationInfo.metaData == null)) {
                if (applicationInfo.isSystemApp()) {
                    return applicationInfo.metaData.getBoolean(VPN_BRANDED_META_DATA, false);
                }
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        return false;
    }
}
