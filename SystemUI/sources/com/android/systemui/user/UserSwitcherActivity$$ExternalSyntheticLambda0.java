package com.android.systemui.user;

import android.view.View;
import com.android.systemui.statusbar.policy.UserSwitcherController;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UserSwitcherActivity$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ UserSwitcherActivity f$0;
    public final /* synthetic */ UserSwitcherController.UserRecord f$1;

    public /* synthetic */ UserSwitcherActivity$$ExternalSyntheticLambda0(UserSwitcherActivity userSwitcherActivity, UserSwitcherController.UserRecord userRecord) {
        this.f$0 = userSwitcherActivity;
        this.f$1 = userRecord;
    }

    public final void onClick(View view) {
        UserSwitcherActivity.m3301buildUserViews$lambda9(this.f$0, this.f$1, view);
    }
}
