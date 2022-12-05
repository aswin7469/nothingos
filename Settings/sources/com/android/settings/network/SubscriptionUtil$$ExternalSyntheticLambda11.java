package com.android.settings.network;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda11 implements Predicate {
    public static final /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda11 INSTANCE = new SubscriptionUtil$$ExternalSyntheticLambda11();

    private /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda11() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean lambda$getUniqueSubscriptionDisplayNames$0;
        lambda$getUniqueSubscriptionDisplayNames$0 = SubscriptionUtil.lambda$getUniqueSubscriptionDisplayNames$0((SubscriptionInfo) obj);
        return lambda$getUniqueSubscriptionDisplayNames$0;
    }
}
