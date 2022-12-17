package com.android.settings.network.telephony;

import android.telephony.SubscriptionInfo;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MobileNetworkSettings$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ MobileNetworkSettings f$0;
    public final /* synthetic */ SubscriptionInfo f$1;

    public /* synthetic */ MobileNetworkSettings$$ExternalSyntheticLambda2(MobileNetworkSettings mobileNetworkSettings, SubscriptionInfo subscriptionInfo) {
        this.f$0 = mobileNetworkSettings;
        this.f$1 = subscriptionInfo;
    }

    public final void run() {
        this.f$0.lambda$onSubscriptionDetailChanged$2(this.f$1);
    }
}
