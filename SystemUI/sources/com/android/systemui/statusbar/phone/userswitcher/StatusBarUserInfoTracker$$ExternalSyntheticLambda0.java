package com.android.systemui.statusbar.phone.userswitcher;

import android.graphics.drawable.Drawable;
import com.android.systemui.statusbar.policy.UserInfoController;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StatusBarUserInfoTracker$$ExternalSyntheticLambda0 implements UserInfoController.OnUserInfoChangedListener {
    public final /* synthetic */ StatusBarUserInfoTracker f$0;

    public /* synthetic */ StatusBarUserInfoTracker$$ExternalSyntheticLambda0(StatusBarUserInfoTracker statusBarUserInfoTracker) {
        this.f$0 = statusBarUserInfoTracker;
    }

    public final void onUserInfoChanged(String str, Drawable drawable, String str2) {
        StatusBarUserInfoTracker.m3225userInfoChangedListener$lambda0(this.f$0, str, drawable, str2);
    }
}
