package com.android.systemui.controls.p010ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;

/* renamed from: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda4 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlActionCoordinatorImpl$$ExternalSyntheticLambda4 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ SharedPreferences f$1;

    public /* synthetic */ ControlActionCoordinatorImpl$$ExternalSyntheticLambda4(int i, SharedPreferences sharedPreferences) {
        this.f$0 = i;
        this.f$1 = sharedPreferences;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        ControlActionCoordinatorImpl.m2687showSettingsDialogIfNeeded$lambda8(this.f$0, this.f$1, dialogInterface);
    }
}
