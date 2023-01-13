package com.android.systemui.controls.p010ui;

import android.app.AlertDialog;
import android.content.DialogInterface;

/* renamed from: com.android.systemui.controls.ui.StatusBehavior$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StatusBehavior$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ ControlWithState f$0;
    public final /* synthetic */ AlertDialog.Builder f$1;
    public final /* synthetic */ ControlViewHolder f$2;

    public /* synthetic */ StatusBehavior$$ExternalSyntheticLambda0(ControlWithState controlWithState, AlertDialog.Builder builder, ControlViewHolder controlViewHolder) {
        this.f$0 = controlWithState;
        this.f$1 = builder;
        this.f$2 = controlViewHolder;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        StatusBehavior.m2725showNotFoundDialog$lambda4$lambda2(this.f$0, this.f$1, this.f$2, dialogInterface, i);
    }
}
