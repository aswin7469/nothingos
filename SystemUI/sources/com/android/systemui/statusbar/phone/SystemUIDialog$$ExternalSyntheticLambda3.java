package com.android.systemui.statusbar.phone;

import android.content.DialogInterface;
import com.android.systemui.statusbar.phone.SystemUIDialog;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SystemUIDialog$$ExternalSyntheticLambda3 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ SystemUIDialog.DismissReceiver f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ SystemUIDialog$$ExternalSyntheticLambda3(SystemUIDialog.DismissReceiver dismissReceiver, Runnable runnable) {
        this.f$0 = dismissReceiver;
        this.f$1 = runnable;
    }

    public final void onDismiss(DialogInterface dialogInterface) {
        SystemUIDialog.lambda$registerDismissListener$2(this.f$0, this.f$1, dialogInterface);
    }
}
