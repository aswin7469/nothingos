package com.android.keyguard;

import android.view.View;
import android.view.ViewGroup;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.systemui.statusbar.policy.UserSwitcherController;

/* renamed from: com.android.keyguard.KeyguardSecurityContainer$UserSwitcherViewMode$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C1624xd955338c implements View.OnClickListener {
    public final /* synthetic */ KeyguardSecurityContainer.UserSwitcherViewMode f$0;
    public final /* synthetic */ ViewGroup f$1;
    public final /* synthetic */ UserSwitcherController.BaseUserAdapter f$2;

    public /* synthetic */ C1624xd955338c(KeyguardSecurityContainer.UserSwitcherViewMode userSwitcherViewMode, ViewGroup viewGroup, UserSwitcherController.BaseUserAdapter baseUserAdapter) {
        this.f$0 = userSwitcherViewMode;
        this.f$1 = viewGroup;
        this.f$2 = baseUserAdapter;
    }

    public final void onClick(View view) {
        this.f$0.mo26048x6c7f668e(this.f$1, this.f$2, view);
    }
}
