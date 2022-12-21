package com.android.systemui.statusbar.policy;

import com.android.systemui.p012qs.user.UserSwitchDialogController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UserSwitcherController$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ UserSwitcherController f$0;
    public final /* synthetic */ UserSwitcherController.UserRecord f$1;
    public final /* synthetic */ UserSwitchDialogController.DialogShower f$2;

    public /* synthetic */ UserSwitcherController$$ExternalSyntheticLambda4(UserSwitcherController userSwitcherController, UserSwitcherController.UserRecord userRecord, UserSwitchDialogController.DialogShower dialogShower) {
        this.f$0 = userSwitcherController;
        this.f$1 = userRecord;
        this.f$2 = dialogShower;
    }

    public final void accept(Object obj) {
        this.f$0.mo46155xc3fe867e(this.f$1, this.f$2, (Integer) obj);
    }
}
