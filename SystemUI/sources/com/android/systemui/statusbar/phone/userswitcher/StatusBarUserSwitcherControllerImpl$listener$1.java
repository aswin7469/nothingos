package com.android.systemui.statusbar.phone.userswitcher;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\u0010\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0007"}, mo64987d2 = {"com/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherControllerImpl$listener$1", "Lcom/android/systemui/statusbar/phone/userswitcher/CurrentUserChipInfoUpdatedListener;", "onCurrentUserChipInfoUpdated", "", "onStatusBarUserSwitcherSettingChanged", "enabled", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: StatusBarUserSwitcherController.kt */
public final class StatusBarUserSwitcherControllerImpl$listener$1 implements CurrentUserChipInfoUpdatedListener {
    final /* synthetic */ StatusBarUserSwitcherControllerImpl this$0;

    StatusBarUserSwitcherControllerImpl$listener$1(StatusBarUserSwitcherControllerImpl statusBarUserSwitcherControllerImpl) {
        this.this$0 = statusBarUserSwitcherControllerImpl;
    }

    public void onCurrentUserChipInfoUpdated() {
        this.this$0.updateChip();
    }

    public void onStatusBarUserSwitcherSettingChanged(boolean z) {
        this.this$0.updateEnabled();
    }
}