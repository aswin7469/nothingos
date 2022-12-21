package com.android.systemui.statusbar.phone;

import com.android.systemui.p012qs.carrier.QSCarrierGroupController;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class LargeScreenShadeHeaderController$$ExternalSyntheticLambda0 implements QSCarrierGroupController.OnSingleCarrierChangedListener {
    public final /* synthetic */ LargeScreenShadeHeaderController f$0;

    public /* synthetic */ LargeScreenShadeHeaderController$$ExternalSyntheticLambda0(LargeScreenShadeHeaderController largeScreenShadeHeaderController) {
        this.f$0 = largeScreenShadeHeaderController;
    }

    public final void onSingleCarrierChanged(boolean z) {
        LargeScreenShadeHeaderController.m3179updateListeners$lambda0(this.f$0, z);
    }
}
