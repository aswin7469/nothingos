package com.android.keyguard;

import com.android.keyguard.CarrierTextManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CarrierTextManager$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ CarrierTextManager.CarrierTextCallback f$0;

    public /* synthetic */ CarrierTextManager$$ExternalSyntheticLambda2(CarrierTextManager.CarrierTextCallback carrierTextCallback) {
        this.f$0 = carrierTextCallback;
    }

    public final void run() {
        this.f$0.updateCarrierInfo(new CarrierTextManager.CarrierTextCallbackInfo("", (CharSequence[]) null, false, (int[]) null));
    }
}
