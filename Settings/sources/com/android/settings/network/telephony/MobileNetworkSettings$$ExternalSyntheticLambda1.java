package com.android.settings.network.telephony;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MobileNetworkSettings$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ MobileNetworkSettings f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ MobileNetworkSettings$$ExternalSyntheticLambda1(MobileNetworkSettings mobileNetworkSettings, Consumer consumer) {
        this.f$0 = mobileNetworkSettings;
        this.f$1 = consumer;
    }

    public final void run() {
        this.f$0.lambda$onSubscriptionDetailChanged$1(this.f$1);
    }
}
