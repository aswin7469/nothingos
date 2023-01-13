package com.android.systemui.controls.p010ui;

import android.content.DialogInterface;

/* renamed from: com.android.systemui.controls.ui.ChallengeDialogs$$ExternalSyntheticLambda5 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ChallengeDialogs$$ExternalSyntheticLambda5 implements DialogInterface.OnShowListener {
    public final /* synthetic */ ChallengeDialogs$createPinDialog$1 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ ChallengeDialogs$$ExternalSyntheticLambda5(ChallengeDialogs$createPinDialog$1 challengeDialogs$createPinDialog$1, int i, boolean z) {
        this.f$0 = challengeDialogs$createPinDialog$1;
        this.f$1 = i;
        this.f$2 = z;
    }

    public final void onShow(DialogInterface dialogInterface) {
        ChallengeDialogs.m2673createPinDialog$lambda5$lambda4(this.f$0, this.f$1, this.f$2, dialogInterface);
    }
}
