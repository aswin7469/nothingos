package com.android.settings.network;

import android.content.Context;
import java.util.function.Supplier;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda10 implements Supplier {
    public final /* synthetic */ Context f$0;

    public /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda10(Context context) {
        this.f$0 = context;
    }

    public final Object get() {
        return SubscriptionUtil.getAvailableSubscriptions(this.f$0).stream().filter(new SubscriptionUtil$$ExternalSyntheticLambda21()).map(new SubscriptionUtil$$ExternalSyntheticLambda22(this.f$0));
    }
}
