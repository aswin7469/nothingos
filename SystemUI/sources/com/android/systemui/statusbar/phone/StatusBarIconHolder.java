package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.UserHandle;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;

public class StatusBarIconHolder {
    public static final int TYPE_ICON = 0;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_WIFI = 1;
    private StatusBarIcon mIcon;
    private StatusBarSignalPolicy.MobileIconState mMobileState;
    private int mTag = 0;
    private int mType = 0;
    private StatusBarSignalPolicy.WifiIconState mWifiState;

    private StatusBarIconHolder() {
    }

    public static StatusBarIconHolder fromIcon(StatusBarIcon statusBarIcon) {
        StatusBarIconHolder statusBarIconHolder = new StatusBarIconHolder();
        statusBarIconHolder.mIcon = statusBarIcon;
        return statusBarIconHolder;
    }

    public static StatusBarIconHolder fromResId(Context context, int i, CharSequence charSequence) {
        StatusBarIconHolder statusBarIconHolder = new StatusBarIconHolder();
        statusBarIconHolder.mIcon = new StatusBarIcon(UserHandle.SYSTEM, context.getPackageName(), Icon.createWithResource(context, i), 0, 0, charSequence);
        return statusBarIconHolder;
    }

    public static StatusBarIconHolder fromWifiIconState(StatusBarSignalPolicy.WifiIconState wifiIconState) {
        StatusBarIconHolder statusBarIconHolder = new StatusBarIconHolder();
        statusBarIconHolder.mWifiState = wifiIconState;
        statusBarIconHolder.mType = 1;
        return statusBarIconHolder;
    }

    public static StatusBarIconHolder fromMobileIconState(StatusBarSignalPolicy.MobileIconState mobileIconState) {
        StatusBarIconHolder statusBarIconHolder = new StatusBarIconHolder();
        statusBarIconHolder.mMobileState = mobileIconState;
        statusBarIconHolder.mType = 2;
        statusBarIconHolder.mTag = mobileIconState.subId;
        return statusBarIconHolder;
    }

    public static StatusBarIconHolder fromCallIndicatorState(Context context, StatusBarSignalPolicy.CallIndicatorIconState callIndicatorIconState) {
        StatusBarIconHolder statusBarIconHolder = new StatusBarIconHolder();
        statusBarIconHolder.mIcon = new StatusBarIcon(UserHandle.SYSTEM, context.getPackageName(), Icon.createWithResource(context, callIndicatorIconState.isNoCalling ? callIndicatorIconState.noCallingResId : callIndicatorIconState.callStrengthResId), 0, 0, callIndicatorIconState.isNoCalling ? callIndicatorIconState.noCallingDescription : callIndicatorIconState.callStrengthDescription);
        statusBarIconHolder.mTag = callIndicatorIconState.subId;
        return statusBarIconHolder;
    }

    public int getType() {
        return this.mType;
    }

    public StatusBarIcon getIcon() {
        return this.mIcon;
    }

    public void setIcon(StatusBarIcon statusBarIcon) {
        this.mIcon = statusBarIcon;
    }

    public StatusBarSignalPolicy.WifiIconState getWifiState() {
        return this.mWifiState;
    }

    public void setWifiState(StatusBarSignalPolicy.WifiIconState wifiIconState) {
        this.mWifiState = wifiIconState;
    }

    public StatusBarSignalPolicy.MobileIconState getMobileState() {
        return this.mMobileState;
    }

    public void setMobileState(StatusBarSignalPolicy.MobileIconState mobileIconState) {
        this.mMobileState = mobileIconState;
    }

    public boolean isVisible() {
        int i = this.mType;
        if (i == 0) {
            return this.mIcon.visible;
        }
        if (i == 1) {
            return this.mWifiState.visible;
        }
        if (i != 2) {
            return true;
        }
        return this.mMobileState.visible;
    }

    public void setVisible(boolean z) {
        if (isVisible() != z) {
            int i = this.mType;
            if (i == 0) {
                this.mIcon.visible = z;
            } else if (i == 1) {
                this.mWifiState.visible = z;
            } else if (i == 2) {
                this.mMobileState.visible = z;
            }
        }
    }

    public int getTag() {
        return this.mTag;
    }
}
