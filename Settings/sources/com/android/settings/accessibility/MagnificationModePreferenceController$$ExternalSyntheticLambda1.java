package com.android.settings.accessibility;

import android.content.DialogInterface;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MagnificationModePreferenceController$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ MagnificationModePreferenceController f$0;

    public /* synthetic */ MagnificationModePreferenceController$$ExternalSyntheticLambda1(MagnificationModePreferenceController magnificationModePreferenceController) {
        this.f$0 = magnificationModePreferenceController;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.onMagnificationTripleTapWarningDialogNegativeButtonClicked(dialogInterface, i);
    }
}