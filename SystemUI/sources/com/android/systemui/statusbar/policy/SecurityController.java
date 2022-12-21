package com.android.systemui.statusbar.policy;

import android.app.admin.DeviceAdminInfo;
import android.content.ComponentName;
import android.graphics.drawable.Drawable;
import com.android.systemui.Dumpable;

public interface SecurityController extends CallbackController<SecurityControllerCallback>, Dumpable {

    public interface SecurityControllerCallback {
        void onStateChanged();
    }

    DeviceAdminInfo getDeviceAdminInfo();

    ComponentName getDeviceOwnerComponentOnAnyUser();

    String getDeviceOwnerName();

    CharSequence getDeviceOwnerOrganizationName();

    int getDeviceOwnerType(ComponentName componentName);

    Drawable getIcon(DeviceAdminInfo deviceAdminInfo);

    CharSequence getLabel(DeviceAdminInfo deviceAdminInfo);

    String getPrimaryVpnName();

    String getProfileOwnerName();

    CharSequence getWorkProfileOrganizationName();

    String getWorkProfileVpnName();

    boolean hasCACertInCurrentUser();

    boolean hasCACertInWorkProfile();

    boolean hasProfileOwner();

    boolean hasWorkProfile();

    boolean isDeviceManaged();

    boolean isNetworkLoggingEnabled();

    boolean isParentalControlsEnabled();

    boolean isProfileOwnerOfOrganizationOwnedDevice();

    boolean isVpnBranded();

    boolean isVpnEnabled();

    boolean isVpnRestricted();

    boolean isWorkProfileOn();

    void onUserSwitched(int i);
}
