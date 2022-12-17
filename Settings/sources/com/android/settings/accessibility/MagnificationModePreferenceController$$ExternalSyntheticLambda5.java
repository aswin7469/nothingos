package com.android.settings.accessibility;

import android.app.Dialog;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MagnificationModePreferenceController$$ExternalSyntheticLambda5 implements View.OnClickListener {
    public final /* synthetic */ MagnificationModePreferenceController f$0;
    public final /* synthetic */ Dialog f$1;

    public /* synthetic */ MagnificationModePreferenceController$$ExternalSyntheticLambda5(MagnificationModePreferenceController magnificationModePreferenceController, Dialog dialog) {
        this.f$0 = magnificationModePreferenceController;
        this.f$1 = dialog;
    }

    public final void onClick(View view) {
        this.f$0.lambda$updateLinkInTripleTapWarningDialog$1(this.f$1, view);
    }
}
