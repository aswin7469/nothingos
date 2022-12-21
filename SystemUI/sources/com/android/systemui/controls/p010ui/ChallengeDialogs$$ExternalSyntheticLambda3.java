package com.android.systemui.controls.p010ui;

import android.content.DialogInterface;
import android.service.controls.actions.ControlAction;

/* renamed from: com.android.systemui.controls.ui.ChallengeDialogs$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ChallengeDialogs$$ExternalSyntheticLambda3 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ControlViewHolder f$0;
    public final /* synthetic */ ControlAction f$1;

    public /* synthetic */ ChallengeDialogs$$ExternalSyntheticLambda3(ControlViewHolder controlViewHolder, ControlAction controlAction) {
        this.f$0 = controlViewHolder;
        this.f$1 = controlAction;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        ChallengeDialogs.m2666createPinDialog$lambda5$lambda0(this.f$0, this.f$1, dialogInterface, i);
    }
}
