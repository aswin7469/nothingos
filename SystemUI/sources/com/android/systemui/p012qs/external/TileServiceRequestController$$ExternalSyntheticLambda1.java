package com.android.systemui.p012qs.external;

import android.content.DialogInterface;
import com.android.systemui.p012qs.external.TileServiceRequestController;

/* renamed from: com.android.systemui.qs.external.TileServiceRequestController$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TileServiceRequestController$$ExternalSyntheticLambda1 implements DialogInterface.OnCancelListener {
    public final /* synthetic */ TileServiceRequestController.SingleShotConsumer f$0;

    public /* synthetic */ TileServiceRequestController$$ExternalSyntheticLambda1(TileServiceRequestController.SingleShotConsumer singleShotConsumer) {
        this.f$0 = singleShotConsumer;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        TileServiceRequestController.m2951createDialog$lambda5$lambda3(this.f$0, dialogInterface);
    }
}
