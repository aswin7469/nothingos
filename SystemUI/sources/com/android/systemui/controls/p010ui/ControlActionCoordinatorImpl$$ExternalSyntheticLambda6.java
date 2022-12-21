package com.android.systemui.controls.p010ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;

/* renamed from: com.android.systemui.controls.ui.ControlActionCoordinatorImpl$$ExternalSyntheticLambda6 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ControlActionCoordinatorImpl$$ExternalSyntheticLambda6 implements DialogInterface.OnClickListener {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ SharedPreferences f$1;
    public final /* synthetic */ ControlActionCoordinatorImpl f$2;

    public /* synthetic */ ControlActionCoordinatorImpl$$ExternalSyntheticLambda6(int i, SharedPreferences sharedPreferences, ControlActionCoordinatorImpl controlActionCoordinatorImpl) {
        this.f$0 = i;
        this.f$1 = sharedPreferences;
        this.f$2 = controlActionCoordinatorImpl;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        ControlActionCoordinatorImpl.m2680showSettingsDialogIfNeeded$lambda10(this.f$0, this.f$1, this.f$2, dialogInterface, i);
    }
}
