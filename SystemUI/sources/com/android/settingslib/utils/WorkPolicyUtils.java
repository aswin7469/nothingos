package com.android.settingslib.utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.os.UserManager;

public class WorkPolicyUtils {
    private static final int USER_NULL = -10000;
    private final Context mContext;
    private final DevicePolicyManager mDevicePolicyManager;
    private final PackageManager mPackageManager;
    private final UserManager mUserManager;

    public WorkPolicyUtils(Context context) {
        this.mContext = context;
        this.mPackageManager = context.getPackageManager();
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService("device_policy");
    }

    public boolean hasWorkPolicy() {
        return (getWorkPolicyInfoIntentDO() == null && getWorkPolicyInfoIntentPO() == null) ? false : true;
    }

    public boolean showWorkPolicyInfo(Context context) {
        Intent workPolicyInfoIntentDO = getWorkPolicyInfoIntentDO();
        if (workPolicyInfoIntentDO != null) {
            context.startActivity(workPolicyInfoIntentDO);
            return true;
        }
        Intent workPolicyInfoIntentPO = getWorkPolicyInfoIntentPO();
        int managedProfileUserId = getManagedProfileUserId();
        if (workPolicyInfoIntentPO == null || managedProfileUserId == USER_NULL) {
            return false;
        }
        context.startActivityAsUser(workPolicyInfoIntentPO, UserHandle.of(managedProfileUserId));
        return true;
    }

    public Intent getWorkPolicyInfoIntentDO() {
        ComponentName deviceOwnerComponent = getDeviceOwnerComponent();
        if (deviceOwnerComponent == null) {
            return null;
        }
        Intent intent = new Intent("android.settings.SHOW_WORK_POLICY_INFO").setPackage(deviceOwnerComponent.getPackageName());
        if (this.mPackageManager.queryIntentActivities(intent, 0).size() != 0) {
            return intent;
        }
        return null;
    }

    private ComponentName getManagedProfileOwnerComponent(int i) {
        if (i == USER_NULL) {
            return null;
        }
        try {
            Context context = this.mContext;
            return ((DevicePolicyManager) context.createPackageContextAsUser(context.getPackageName(), 0, UserHandle.of(i)).getSystemService("device_policy")).getProfileOwner();
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public Intent getWorkPolicyInfoIntentPO() {
        int managedProfileUserId = getManagedProfileUserId();
        ComponentName managedProfileOwnerComponent = getManagedProfileOwnerComponent(managedProfileUserId);
        if (managedProfileOwnerComponent == null) {
            return null;
        }
        Intent intent = new Intent("android.settings.SHOW_WORK_POLICY_INFO").setPackage(managedProfileOwnerComponent.getPackageName());
        if (this.mPackageManager.queryIntentActivitiesAsUser(intent, 0, UserHandle.of(managedProfileUserId)).size() != 0) {
            return intent;
        }
        return null;
    }

    private ComponentName getDeviceOwnerComponent() {
        if (!this.mPackageManager.hasSystemFeature("android.software.device_admin")) {
            return null;
        }
        return this.mDevicePolicyManager.getDeviceOwnerComponentOnAnyUser();
    }

    public int getManagedProfileUserId() {
        for (UserHandle identifier : this.mUserManager.getAllProfiles()) {
            int identifier2 = identifier.getIdentifier();
            if (this.mUserManager.isManagedProfile(identifier2)) {
                return identifier2;
            }
        }
        return USER_NULL;
    }
}