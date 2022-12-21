package com.android.systemui.controls.p010ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;

/* renamed from: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda5 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlActionCoordinatorImpl$$ExternalSyntheticLambda5 implements DialogInterface.OnClickListener {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ SharedPreferences f$1;

    public /* synthetic */ ControlActionCoordinatorImpl$$ExternalSyntheticLambda5(int i, SharedPreferences sharedPreferences) {
        this.f$0 = i;
        this.f$1 = sharedPreferences;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        ControlActionCoordinatorImpl.m2683showSettingsDialogIfNeeded$lambda9(this.f$0, this.f$1, dialogInterface, i);
    }
}
