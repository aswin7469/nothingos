package com.android.settings.security;

import android.content.DialogInterface;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UnificationConfirmationDialog$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ SecuritySettings f$0;

    public /* synthetic */ UnificationConfirmationDialog$$ExternalSyntheticLambda1(SecuritySettings securitySettings) {
        this.f$0 = securitySettings;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.startUnification();
    }
}
