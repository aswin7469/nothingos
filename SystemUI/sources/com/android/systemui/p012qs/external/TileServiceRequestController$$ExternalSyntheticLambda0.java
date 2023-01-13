package com.android.systemui.p012qs.external;

import android.content.DialogInterface;
import com.android.systemui.p012qs.external.TileServiceRequestController;

/* renamed from: com.android.systemui.qs.external.TileServiceRequestController$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TileServiceRequestController$$ExternalSyntheticLambda0 implements DialogInterface.OnClickListener {
    public final /* synthetic */ TileServiceRequestController.SingleShotConsumer f$0;

    public /* synthetic */ TileServiceRequestController$$ExternalSyntheticLambda0(TileServiceRequestController.SingleShotConsumer singleShotConsumer) {
        this.f$0 = singleShotConsumer;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        TileServiceRequestController.m2955createDialog$lambda2(this.f$0, dialogInterface, i);
    }
}
