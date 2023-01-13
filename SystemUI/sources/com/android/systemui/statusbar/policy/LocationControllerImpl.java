package com.android.systemui.statusbar.policy;

import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.PermissionChecker;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.Utils;
import com.android.systemui.BootCompleteCache;
import com.android.systemui.appops.AppOpItem;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

@SysUISingleton
public class LocationControllerImpl extends BroadcastReceiver implements LocationController, AppOpsController.Callback {
    private final AppOpsController mAppOpsController;
    /* access modifiers changed from: private */
    public boolean mAreActiveLocationRequests;
    private final Handler mBackgroundHandler;
    private final BootCompleteCache mBootCompleteCache;
    private final ContentObserver mContentObserver;
    private final Context mContext;
    private final DeviceConfigProxy mDeviceConfigProxy;
    private final C3185H mHandler;
    private final PackageManager mPackageManager;
    private final SecureSettings mSecureSettings;
    private boolean mShouldDisplayAllAccesses = getAllAccessesSetting();
    private boolean mShowSystemAccessesFlag = getShowSystemFlag();
    /* access modifiers changed from: private */
    public boolean mShowSystemAccessesSetting = getShowSystemSetting();
    private final UiEventLogger mUiEventLogger;
    private final UserTracker mUserTracker;

    @Inject
    public LocationControllerImpl(Context context, AppOpsController appOpsController, DeviceConfigProxy deviceConfigProxy, @Main Looper looper, @Background Handler handler, BroadcastDispatcher broadcastDispatcher, BootCompleteCache bootCompleteCache, UserTracker userTracker, PackageManager packageManager, UiEventLogger uiEventLogger, SecureSettings secureSettings) {
        this.mContext = context;
        this.mAppOpsController = appOpsController;
        this.mDeviceConfigProxy = deviceConfigProxy;
        this.mBootCompleteCache = bootCompleteCache;
        C3185H h = new C3185H(looper);
        this.mHandler = h;
        this.mUserTracker = userTracker;
        this.mUiEventLogger = uiEventLogger;
        this.mSecureSettings = secureSettings;
        this.mBackgroundHandler = handler;
        this.mPackageManager = packageManager;
        C31841 r4 = new ContentObserver(handler) {
            public void onChange(boolean z) {
                LocationControllerImpl locationControllerImpl = LocationControllerImpl.this;
                boolean unused = locationControllerImpl.mShowSystemAccessesSetting = locationControllerImpl.getShowSystemSetting();
            }
        };
        this.mContentObserver = r4;
        secureSettings.registerContentObserverForUser("locationShowSystemOps", (ContentObserver) r4, -1);
        Objects.requireNonNull(handler);
        deviceConfigProxy.addOnPropertiesChangedListener("privacy", new LocationControllerImpl$$ExternalSyntheticLambda0(handler), new LocationControllerImpl$$ExternalSyntheticLambda1(this));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.location.MODE_CHANGED");
        broadcastDispatcher.registerReceiverWithHandler(this, intentFilter, h, UserHandle.ALL);
        appOpsController.addCallback(new int[]{0, 1, 42}, this);
        handler.post(new LocationControllerImpl$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-policy-LocationControllerImpl */
    public /* synthetic */ void mo45933x5109b535(DeviceConfig.Properties properties) {
        this.mShouldDisplayAllAccesses = getAllAccessesSetting();
        this.mShowSystemAccessesFlag = getShowSystemSetting();
        updateActiveLocationRequests();
    }

    public void addCallback(LocationController.LocationChangeCallback locationChangeCallback) {
        this.mHandler.obtainMessage(3, locationChangeCallback).sendToTarget();
        this.mHandler.sendEmptyMessage(1);
    }

    public void removeCallback(LocationController.LocationChangeCallback locationChangeCallback) {
        this.mHandler.obtainMessage(4, locationChangeCallback).sendToTarget();
    }

    public boolean setLocationEnabled(boolean z) {
        int userId = this.mUserTracker.getUserId();
        if (isUserLocationRestricted(userId)) {
            return false;
        }
        Utils.updateLocationEnabled(this.mContext, z, userId, 2);
        return true;
    }

    public boolean isLocationEnabled() {
        return this.mBootCompleteCache.isBootComplete() && ((LocationManager) this.mContext.getSystemService("location")).isLocationEnabledForUser(this.mUserTracker.getUserHandle());
    }

    public boolean isLocationActive() {
        return this.mAreActiveLocationRequests;
    }

    private boolean isUserLocationRestricted(int i) {
        return ((UserManager) this.mContext.getSystemService("user")).hasUserRestriction("no_share_location", UserHandle.of(i));
    }

    private boolean getAllAccessesSetting() {
        return this.mDeviceConfigProxy.getBoolean("privacy", "location_indicators_small_enabled", false);
    }

    private boolean getShowSystemFlag() {
        return this.mDeviceConfigProxy.getBoolean("privacy", "location_indicators_show_system", false);
    }

    /* access modifiers changed from: private */
    public boolean getShowSystemSetting() {
        return this.mSecureSettings.getIntForUser("locationShowSystemOps", 0, -2) == 1;
    }

    /* access modifiers changed from: protected */
    public boolean areActiveHighPowerLocationRequests() {
        List<AppOpItem> activeAppOps = this.mAppOpsController.getActiveAppOps();
        int size = activeAppOps.size();
        for (int i = 0; i < size; i++) {
            if (activeAppOps.get(i).getCode() == 42) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void areActiveLocationRequests() {
        if (this.mShouldDisplayAllAccesses) {
            boolean z = this.mAreActiveLocationRequests;
            boolean z2 = this.mShowSystemAccessesFlag || this.mShowSystemAccessesSetting;
            List<AppOpItem> activeAppOps = this.mAppOpsController.getActiveAppOps();
            List<UserInfo> userProfiles = this.mUserTracker.getUserProfiles();
            int size = activeAppOps.size();
            boolean z3 = false;
            boolean z4 = false;
            boolean z5 = false;
            for (int i = 0; i < size; i++) {
                if (activeAppOps.get(i).getCode() == 1 || activeAppOps.get(i).getCode() == 0) {
                    boolean isSystemApp = isSystemApp(userProfiles, activeAppOps.get(i));
                    if (isSystemApp) {
                        z4 = true;
                    } else {
                        z5 = true;
                    }
                    z3 = z2 || z3 || !isSystemApp;
                }
            }
            boolean areActiveHighPowerLocationRequests = areActiveHighPowerLocationRequests();
            this.mAreActiveLocationRequests = z3;
            if (z3 != z) {
                this.mHandler.sendEmptyMessage(2);
            }
            if (z) {
                return;
            }
            if (areActiveHighPowerLocationRequests || z4 || z5) {
                if (areActiveHighPowerLocationRequests) {
                    this.mUiEventLogger.log(LocationIndicatorEvent.LOCATION_INDICATOR_MONITOR_HIGH_POWER);
                }
                if (z4) {
                    this.mUiEventLogger.log(LocationIndicatorEvent.LOCATION_INDICATOR_SYSTEM_APP);
                }
                if (z5) {
                    this.mUiEventLogger.log(LocationIndicatorEvent.LOCATION_INDICATOR_NON_SYSTEM_APP);
                }
            }
        }
    }

    private boolean isSystemApp(List<UserInfo> list, AppOpItem appOpItem) {
        String opToPermission = AppOpsManager.opToPermission(appOpItem.getCode());
        UserHandle userHandleForUid = UserHandle.getUserHandleForUid(appOpItem.getUid());
        int size = list.size();
        boolean z = false;
        for (int i = 0; i < size; i++) {
            if (list.get(i).getUserHandle().equals(userHandleForUid)) {
                z = true;
            }
        }
        if (!z) {
            return true;
        }
        int permissionFlags = this.mPackageManager.getPermissionFlags(opToPermission, appOpItem.getPackageName(), userHandleForUid);
        if (PermissionChecker.checkPermissionForPreflight(this.mContext, opToPermission, -1, appOpItem.getUid(), appOpItem.getPackageName()) == 0) {
            if ((permissionFlags & 256) == 0) {
                return true;
            }
            return false;
        } else if ((permissionFlags & 512) == 0) {
            return true;
        } else {
            return false;
        }
    }

    /* access modifiers changed from: private */
    public void updateActiveLocationRequests() {
        if (this.mShouldDisplayAllAccesses) {
            this.mBackgroundHandler.post(new LocationControllerImpl$$ExternalSyntheticLambda3(this));
            return;
        }
        boolean z = this.mAreActiveLocationRequests;
        boolean areActiveHighPowerLocationRequests = areActiveHighPowerLocationRequests();
        this.mAreActiveLocationRequests = areActiveHighPowerLocationRequests;
        if (areActiveHighPowerLocationRequests != z) {
            this.mHandler.sendEmptyMessage(2);
            if (this.mAreActiveLocationRequests) {
                this.mUiEventLogger.log(LocationIndicatorEvent.LOCATION_INDICATOR_MONITOR_HIGH_POWER);
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        if ("android.location.MODE_CHANGED".equals(intent.getAction())) {
            this.mHandler.locationSettingsChanged();
        }
    }

    public void onActiveStateChanged(int i, int i2, String str, boolean z) {
        updateActiveLocationRequests();
    }

    /* renamed from: com.android.systemui.statusbar.policy.LocationControllerImpl$H */
    private final class C3185H extends Handler {
        private static final int MSG_ADD_CALLBACK = 3;
        private static final int MSG_LOCATION_ACTIVE_CHANGED = 2;
        private static final int MSG_LOCATION_SETTINGS_CHANGED = 1;
        private static final int MSG_REMOVE_CALLBACK = 4;
        private ArrayList<LocationController.LocationChangeCallback> mSettingsChangeCallbacks = new ArrayList<>();

        C3185H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                locationSettingsChanged();
            } else if (i == 2) {
                locationActiveChanged();
            } else if (i == 3) {
                this.mSettingsChangeCallbacks.add((LocationController.LocationChangeCallback) message.obj);
            } else if (i == 4) {
                this.mSettingsChangeCallbacks.remove((Object) (LocationController.LocationChangeCallback) message.obj);
            }
        }

        private void locationActiveChanged() {
            com.android.systemui.util.Utils.safeForeach(this.mSettingsChangeCallbacks, new LocationControllerImpl$H$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$locationActiveChanged$0$com-android-systemui-statusbar-policy-LocationControllerImpl$H */
        public /* synthetic */ void mo45938xb1ea6500(LocationController.LocationChangeCallback locationChangeCallback) {
            locationChangeCallback.onLocationActiveChanged(LocationControllerImpl.this.mAreActiveLocationRequests);
        }

        /* access modifiers changed from: private */
        public void locationSettingsChanged() {
            com.android.systemui.util.Utils.safeForeach(this.mSettingsChangeCallbacks, new LocationControllerImpl$H$$ExternalSyntheticLambda0(LocationControllerImpl.this.isLocationEnabled()));
        }
    }

    enum LocationIndicatorEvent implements UiEventLogger.UiEventEnum {
        LOCATION_INDICATOR_MONITOR_HIGH_POWER(935),
        LOCATION_INDICATOR_SYSTEM_APP(936),
        LOCATION_INDICATOR_NON_SYSTEM_APP(937);
        
        private final int mId;

        private LocationIndicatorEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
