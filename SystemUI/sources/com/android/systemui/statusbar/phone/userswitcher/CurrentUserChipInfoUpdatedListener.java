package com.android.systemui.statusbar.phone.userswitcher;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\u0010\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0016ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0007À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/userswitcher/CurrentUserChipInfoUpdatedListener;", "", "onCurrentUserChipInfoUpdated", "", "onStatusBarUserSwitcherSettingChanged", "enabled", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: StatusBarUserInfoTracker.kt */
public interface CurrentUserChipInfoUpdatedListener {
    void onCurrentUserChipInfoUpdated();

    void onStatusBarUserSwitcherSettingChanged(boolean z) {
    }
}