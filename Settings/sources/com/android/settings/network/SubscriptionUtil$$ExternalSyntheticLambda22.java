package com.android.settings.network;

import android.content.Context;
import android.telephony.SubscriptionInfo;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda22 implements Function {
    public final /* synthetic */ Context f$0;

    public /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda22(Context context) {
        this.f$0 = context;
    }

    public final Object apply(Object obj) {
        return SubscriptionUtil.lambda$getUniqueSubscriptionDisplayNames$1(this.f$0, (SubscriptionInfo) obj);
    }
}
