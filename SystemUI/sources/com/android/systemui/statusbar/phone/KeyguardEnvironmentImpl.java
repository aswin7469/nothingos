package com.android.systemui.statusbar.phone;

import android.service.notification.StatusBarNotification;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import javax.inject.Inject;

@SysUISingleton
public class KeyguardEnvironmentImpl implements NotificationEntryManager.KeyguardEnvironment {
    private static final String TAG = "KeyguardEnvironmentImpl";
    private final DeviceProvisionedController mDeviceProvisionedController;
    private final NotificationLockscreenUserManager mLockscreenUserManager;

    @Inject
    public KeyguardEnvironmentImpl(NotificationLockscreenUserManager notificationLockscreenUserManager, DeviceProvisionedController deviceProvisionedController) {
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mDeviceProvisionedController = deviceProvisionedController;
    }

    public boolean isDeviceProvisioned() {
        return this.mDeviceProvisionedController.isDeviceProvisioned();
    }

    public boolean isNotificationForCurrentProfiles(StatusBarNotification statusBarNotification) {
        return this.mLockscreenUserManager.isCurrentProfile(statusBarNotification.getUserId());
    }
}
