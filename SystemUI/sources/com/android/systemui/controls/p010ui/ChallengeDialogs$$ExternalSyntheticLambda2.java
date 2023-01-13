package com.android.systemui.controls.p010ui;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

/* renamed from: com.android.systemui.controls.ui.ChallengeDialogs$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ChallengeDialogs$$ExternalSyntheticLambda2 implements View.OnClickListener {
    public final /* synthetic */ EditText f$0;
    public final /* synthetic */ CheckBox f$1;

    public /* synthetic */ ChallengeDialogs$$ExternalSyntheticLambda2(EditText editText, CheckBox checkBox) {
        this.f$0 = editText;
        this.f$1 = checkBox;
    }

    public final void onClick(View view) {
        ChallengeDialogs.m2674createPinDialog$lambda5$lambda4$lambda3(this.f$0, this.f$1, view);
    }
}
