package com.android.systemui.p012qs.user;

import android.content.DialogInterface;
import com.android.systemui.statusbar.phone.SystemUIDialog;

/* renamed from: com.android.systemui.qs.user.UserSwitchDialogController$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class UserSwitchDialogController$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ UserSwitchDialogController f$0;
    public final /* synthetic */ SystemUIDialog f$1;

    public /* synthetic */ UserSwitchDialogController$$ExternalSyntheticLambda1(UserSwitchDialogController userSwitchDialogController, SystemUIDialog systemUIDialog) {
        this.f$0 = userSwitchDialogController;
        this.f$1 = systemUIDialog;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        UserSwitchDialogController.m2992showDialog$lambda2$lambda1(this.f$0, this.f$1, dialogInterface, i);
    }
}
