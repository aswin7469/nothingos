package com.android.systemui.p012qs;

import com.android.systemui.p012qs.carrier.QSCarrierGroupController;

/* renamed from: com.android.systemui.qs.QuickStatusBarHeaderController$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class QuickStatusBarHeaderController$$ExternalSyntheticLambda1 implements QSCarrierGroupController.OnSingleCarrierChangedListener {
    public final /* synthetic */ QuickStatusBarHeader f$0;

    public /* synthetic */ QuickStatusBarHeaderController$$ExternalSyntheticLambda1(QuickStatusBarHeader quickStatusBarHeader) {
        this.f$0 = quickStatusBarHeader;
    }

    public final void onSingleCarrierChanged(boolean z) {
        this.f$0.setIsSingleCarrier(z);
    }
}
