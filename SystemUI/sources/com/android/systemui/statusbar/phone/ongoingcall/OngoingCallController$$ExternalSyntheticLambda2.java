package com.android.systemui.statusbar.phone.ongoingcall;

import android.app.PendingIntent;
import android.view.View;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class OngoingCallController$$ExternalSyntheticLambda2 implements View.OnClickListener {
    public final /* synthetic */ OngoingCallController f$0;
    public final /* synthetic */ PendingIntent f$1;
    public final /* synthetic */ View f$2;

    public /* synthetic */ OngoingCallController$$ExternalSyntheticLambda2(OngoingCallController ongoingCallController, PendingIntent pendingIntent, View view) {
        this.f$0 = ongoingCallController;
        this.f$1 = pendingIntent;
        this.f$2 = view;
    }

    public final void onClick(View view) {
        OngoingCallController.m3210updateChipClickListener$lambda4(this.f$0, this.f$1, this.f$2, view);
    }
}
