package com.nothing.systemui.p024qs.tiles;

import android.view.View;
import com.android.systemui.p012qs.tiles.dialog.InternetDialog;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogController;

/* renamed from: com.nothing.systemui.qs.tiles.InternetTileEx$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class InternetTileEx$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ InternetDialogController f$0;
    public final /* synthetic */ InternetDialog f$1;

    public /* synthetic */ InternetTileEx$$ExternalSyntheticLambda1(InternetDialogController internetDialogController, InternetDialog internetDialog) {
        this.f$0 = internetDialogController;
        this.f$1 = internetDialog;
    }

    public final void onClick(View view) {
        InternetTileEx.m3521setHeaderOnClickListener$lambda1(this.f$0, this.f$1, view);
    }
}
