package com.android.systemui.p012qs.external;

import android.content.DialogInterface;
import com.android.systemui.p012qs.external.TileServiceRequestController;

/* renamed from: com.android.systemui.qs.external.TileServiceRequestController$$ExternalSyntheticLambda2 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TileServiceRequestController$$ExternalSyntheticLambda2 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ TileServiceRequestController.SingleShotConsumer f$0;

    public /* synthetic */ TileServiceRequestController$$ExternalSyntheticLambda2(TileServiceRequestController.SingleShotConsumer singleShotConsumer) {
        this.f$0 = singleShotConsumer;
    }

    public final void onDismiss(DialogInterface dialogInterface) {
        TileServiceRequestController.m2957createDialog$lambda5$lambda4(this.f$0, dialogInterface);
    }
}
