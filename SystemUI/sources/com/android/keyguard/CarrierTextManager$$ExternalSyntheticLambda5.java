package com.android.keyguard;

import com.android.keyguard.CarrierTextManager;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CarrierTextManager$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ CarrierTextManager.CarrierTextCallback f$0;
    public final /* synthetic */ CarrierTextManager.CarrierTextCallbackInfo f$1;

    public /* synthetic */ CarrierTextManager$$ExternalSyntheticLambda5(CarrierTextManager.CarrierTextCallback carrierTextCallback, CarrierTextManager.CarrierTextCallbackInfo carrierTextCallbackInfo) {
        this.f$0 = carrierTextCallback;
        this.f$1 = carrierTextCallbackInfo;
    }

    public final void run() {
        this.f$0.updateCarrierInfo(this.f$1);
    }
}
