package com.android.settings.network.telephony;

import android.app.Activity;
import android.telephony.SubscriptionInfo;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MobileNetworkSettings$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ SubscriptionInfo f$0;

    public /* synthetic */ MobileNetworkSettings$$ExternalSyntheticLambda0(SubscriptionInfo subscriptionInfo) {
        this.f$0 = subscriptionInfo;
    }

    public final void accept(Object obj) {
        MobileNetworkSettings.lambda$onSubscriptionDetailChanged$0(this.f$0, (Activity) obj);
    }
}
