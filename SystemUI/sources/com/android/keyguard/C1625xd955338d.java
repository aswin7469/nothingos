package com.android.keyguard;

import com.android.keyguard.KeyguardSecurityContainer;
import com.android.systemui.statusbar.policy.UserSwitcherController;

/* renamed from: com.android.keyguard.KeyguardSecurityContainer$UserSwitcherViewMode$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C1625xd955338d implements UserSwitcherController.UserSwitchCallback {
    public final /* synthetic */ KeyguardSecurityContainer.UserSwitcherViewMode f$0;

    public /* synthetic */ C1625xd955338d(KeyguardSecurityContainer.UserSwitcherViewMode userSwitcherViewMode) {
        this.f$0 = userSwitcherViewMode;
    }

    public final void onUserSwitched() {
        this.f$0.setupUserSwitcher();
    }
}
