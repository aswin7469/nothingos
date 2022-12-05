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
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.security.KeyChain;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.net.LegacyVpnInfo;
import com.android.internal.net.VpnConfig;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.policy.SecurityController;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executor;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes2.dex */
public class SecurityControllerImpl extends CurrentUserTracker implements SecurityController {
    private static final boolean DEBUG = Log.isLoggable("SecurityController", 3);
    private static final NetworkRequest REQUEST = new NetworkRequest.Builder().clearCapabilities().build();
    private final Executor mBgExecutor;
    private final BroadcastReceiver mBroadcastReceiver;
    private final ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private int mCurrentUserId;
    private final DevicePolicyManager mDevicePolicyManager;
    private final ConnectivityManager.NetworkCallback mNetworkCallback;
    private final PackageManager mPackageManager;
    private final UserManager mUserManager;
    private final VpnManager mVpnManager;
    private int mVpnUserId;
    @GuardedBy({"mCallbacks"})
    private final ArrayList<SecurityController.SecurityControllerCallback> mCallbacks = new ArrayList<>();
    private SparseArray<VpnConfig> mCurrentVpns = new SparseArray<>();
    private ArrayMap<Integer, Boolean> mHasCACerts = new ArrayMap<>();

    public SecurityControllerImpl(Context context, Handler handler, BroadcastDispatcher broadcastDispatcher, Executor executor) {
        super(broadcastDispatcher);
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.systemui.statusbar.policy.SecurityControllerImpl.1
            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onAvailable(Network network) {
                if (SecurityControllerImpl.DEBUG) {
                    Log.d("SecurityController", "onAvailable " + network.getNetId());
                }
                SecurityControllerImpl.this.updateState();
                SecurityControllerImpl.this.fireCallbacks();
            }

            @Override // android.net.ConnectivityManager.NetworkCallback
            public void onLost(Network network) {
                if (SecurityControllerImpl.DEBUG) {
                    Log.d("SecurityController", "onLost " + network.getNetId());
                }
                SecurityControllerImpl.this.updateState();
                SecurityControllerImpl.this.fireCallbacks();
            }
        };
        this.mNetworkCallback = networkCallback;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.SecurityControllerImpl.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                int intExtra;
                if ("android.security.action.TRUST_STORE_CHANGED".equals(intent.getAction())) {
                    SecurityControllerImpl.this.refreshCACerts(getSendingUserId());
                } else if (!"android.intent.action.USER_UNLOCKED".equals(intent.getAction()) || (intExtra = intent.getIntExtra("android.intent.extra.user_handle", -10000)) == -10000) {
                } else {
                    SecurityControllerImpl.this.refreshCACerts(intExtra);
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mContext = context;
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        this.mConnectivityManager = connectivityManager;
        this.mVpnManager = (VpnManager) context.getSystemService(VpnManager.class);
        this.mPackageManager = context.getPackageManager();
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mBgExecutor = executor;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.security.action.TRUST_STORE_CHANGED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        broadcastDispatcher.registerReceiverWithHandler(broadcastReceiver, intentFilter, handler, UserHandle.ALL);
        connectivityManager.registerNetworkCallback(REQUEST, networkCallback);
        onUserSwitched(ActivityManager.getCurrentUser());
        startTracking();
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
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

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean isDeviceManaged() {
        return this.mDevicePolicyManager.isDeviceManaged();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public CharSequence getDeviceOwnerOrganizationName() {
        return this.mDevicePolicyManager.getDeviceOwnerOrganizationName();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public CharSequence getWorkProfileOrganizationName() {
        int workProfileUserId = getWorkProfileUserId(this.mCurrentUserId);
        if (workProfileUserId == -10000) {
            return null;
        }
        return this.mDevicePolicyManager.getOrganizationNameForUser(workProfileUserId);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
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

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean hasWorkProfile() {
        return getWorkProfileUserId(this.mCurrentUserId) != -10000;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean isWorkProfileOn() {
        UserHandle of = UserHandle.of(getWorkProfileUserId(this.mCurrentUserId));
        return of != null && !this.mUserManager.isQuietModeEnabled(of);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean isProfileOwnerOfOrganizationOwnedDevice() {
        return this.mDevicePolicyManager.isOrganizationOwnedDeviceWithManagedProfile();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public String getWorkProfileVpnName() {
        VpnConfig vpnConfig;
        int workProfileUserId = getWorkProfileUserId(this.mVpnUserId);
        if (workProfileUserId == -10000 || (vpnConfig = this.mCurrentVpns.get(workProfileUserId)) == null) {
            return null;
        }
        return getNameForVpnConfig(vpnConfig, UserHandle.of(workProfileUserId));
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public ComponentName getDeviceOwnerComponentOnAnyUser() {
        return this.mDevicePolicyManager.getDeviceOwnerComponentOnAnyUser();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public int getDeviceOwnerType(ComponentName componentName) {
        return this.mDevicePolicyManager.getDeviceOwnerType(componentName);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean isNetworkLoggingEnabled() {
        return this.mDevicePolicyManager.isNetworkLoggingEnabled(null);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean isVpnEnabled() {
        for (int i : this.mUserManager.getProfileIdsWithDisabled(this.mVpnUserId)) {
            if (this.mCurrentVpns.get(i) != null) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean isVpnBranded() {
        String packageNameForVpnConfig;
        VpnConfig vpnConfig = this.mCurrentVpns.get(this.mVpnUserId);
        if (vpnConfig == null || (packageNameForVpnConfig = getPackageNameForVpnConfig(vpnConfig)) == null) {
            return false;
        }
        return isVpnPackageBranded(packageNameForVpnConfig);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean hasCACertInCurrentUser() {
        Boolean bool = this.mHasCACerts.get(Integer.valueOf(this.mCurrentUserId));
        return bool != null && bool.booleanValue();
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean hasCACertInWorkProfile() {
        Boolean bool;
        int workProfileUserId = getWorkProfileUserId(this.mCurrentUserId);
        return (workProfileUserId == -10000 || (bool = this.mHasCACerts.get(Integer.valueOf(workProfileUserId))) == null || !bool.booleanValue()) ? false : true;
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(SecurityController.SecurityControllerCallback securityControllerCallback) {
        synchronized (this.mCallbacks) {
            if (securityControllerCallback == null) {
                return;
            }
            if (DEBUG) {
                Log.d("SecurityController", "removeCallback " + securityControllerCallback);
            }
            this.mCallbacks.remove(securityControllerCallback);
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(SecurityController.SecurityControllerCallback securityControllerCallback) {
        synchronized (this.mCallbacks) {
            if (securityControllerCallback != null) {
                if (!this.mCallbacks.contains(securityControllerCallback)) {
                    if (DEBUG) {
                        Log.d("SecurityController", "addCallback " + securityControllerCallback);
                    }
                    this.mCallbacks.add(securityControllerCallback);
                }
            }
        }
    }

    @Override // com.android.systemui.settings.CurrentUserTracker
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

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public boolean isParentalControlsEnabled() {
        return getProfileOwnerOrDeviceOwnerSupervisionComponent() != null;
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public DeviceAdminInfo getDeviceAdminInfo() {
        return getDeviceAdminInfo(getProfileOwnerOrDeviceOwnerComponent());
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
    public Drawable getIcon(DeviceAdminInfo deviceAdminInfo) {
        if (deviceAdminInfo == null) {
            return null;
        }
        return deviceAdminInfo.loadIcon(this.mPackageManager);
    }

    @Override // com.android.systemui.statusbar.policy.SecurityController
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

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshCACerts(final int i) {
        this.mBgExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.policy.SecurityControllerImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SecurityControllerImpl.this.lambda$refreshCACerts$0(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00a2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$refreshCACerts$0(int i) {
        Pair pair;
        Object obj;
        Object obj2;
        ArrayMap<Integer, Boolean> arrayMap;
        Object obj3;
        Pair pair2 = null;
        try {
            KeyChain.KeyChainConnection bindAsUser = KeyChain.bindAsUser(this.mContext, UserHandle.of(i));
            try {
                pair = new Pair(Integer.valueOf(i), Boolean.valueOf(!bindAsUser.getService().getUserCaAliases().getList().isEmpty()));
                try {
                    try {
                        bindAsUser.close();
                        if (DEBUG) {
                            Log.d("SecurityController", "Refreshing CA Certs " + pair);
                        }
                        obj2 = pair.second;
                    } catch (RemoteException | AssertionError | InterruptedException e) {
                        e = e;
                        Log.i("SecurityController", "failed to get CA certs", e);
                        Pair pair3 = new Pair(Integer.valueOf(i), null);
                        if (DEBUG) {
                            Log.d("SecurityController", "Refreshing CA Certs " + pair3);
                        }
                        obj2 = pair3.second;
                        if (obj2 == null) {
                            return;
                        }
                        arrayMap = this.mHasCACerts;
                        obj3 = pair3.first;
                        arrayMap.put((Integer) obj3, (Boolean) obj2);
                        fireCallbacks();
                    }
                } catch (Throwable th) {
                    th = th;
                    pair2 = pair;
                    if (DEBUG) {
                        Log.d("SecurityController", "Refreshing CA Certs " + pair2);
                    }
                    if (pair2 != null && (obj = pair2.second) != null) {
                        this.mHasCACerts.put((Integer) pair2.first, (Boolean) obj);
                        fireCallbacks();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                if (bindAsUser != null) {
                    try {
                        bindAsUser.close();
                    } catch (Throwable th3) {
                        th2.addSuppressed(th3);
                    }
                }
                throw th2;
            }
        } catch (RemoteException | AssertionError | InterruptedException e2) {
            e = e2;
            pair = null;
        } catch (Throwable th4) {
            th = th4;
            if (DEBUG) {
            }
            if (pair2 != null) {
                this.mHasCACerts.put((Integer) pair2.first, (Boolean) obj);
                fireCallbacks();
            }
            throw th;
        }
        if (obj2 != null) {
            arrayMap = this.mHasCACerts;
            obj3 = pair.first;
            arrayMap.put((Integer) obj3, (Boolean) obj2);
            fireCallbacks();
        }
    }

    private String getNameForVpnConfig(VpnConfig vpnConfig, UserHandle userHandle) {
        if (vpnConfig.legacy) {
            return this.mContext.getString(R$string.legacy_vpn_name);
        }
        String str = vpnConfig.user;
        try {
            Context context = this.mContext;
            return VpnConfig.getVpnLabel(context.createPackageContextAsUser(context.getPackageName(), 0, userHandle), str).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SecurityController", "Package " + str + " is not present", e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireCallbacks() {
        synchronized (this.mCallbacks) {
            Iterator<SecurityController.SecurityControllerCallback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onStateChanged();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
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
            if (applicationInfo != null && applicationInfo.metaData != null && applicationInfo.isSystemApp()) {
                return applicationInfo.metaData.getBoolean("com.android.systemui.IS_BRANDED", false);
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        return false;
    }
}
