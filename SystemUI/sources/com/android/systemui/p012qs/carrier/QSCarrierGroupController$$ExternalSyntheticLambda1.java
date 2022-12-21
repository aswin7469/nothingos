package com.android.systemui.p012qs.carrier;

import com.android.keyguard.CarrierTextManager;
import java.util.function.Consumer;

/* renamed from: com.android.systemui.qs.carrier.QSCarrierGroupController$$ExternalSyntheticLambda1 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class QSCarrierGroupController$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ QSCarrierGroupController f$0;

    public /* synthetic */ QSCarrierGroupController$$ExternalSyntheticLambda1(QSCarrierGroupController qSCarrierGroupController) {
        this.f$0 = qSCarrierGroupController;
    }

    public final void accept(Object obj) {
        this.f$0.handleUpdateCarrierInfo((CarrierTextManager.CarrierTextCallbackInfo) obj);
    }
}
