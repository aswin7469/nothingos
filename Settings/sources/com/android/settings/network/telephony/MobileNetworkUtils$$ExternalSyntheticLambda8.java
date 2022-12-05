package com.android.settings.network.telephony;

import android.telephony.SubscriptionInfo;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class MobileNetworkUtils$$ExternalSyntheticLambda8 implements Predicate {
    public static final /* synthetic */ MobileNetworkUtils$$ExternalSyntheticLambda8 INSTANCE = new MobileNetworkUtils$$ExternalSyntheticLambda8();

    private /* synthetic */ MobileNetworkUtils$$ExternalSyntheticLambda8() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean lambda$getUniqueSubscriptionDisplayNames$1;
        lambda$getUniqueSubscriptionDisplayNames$1 = MobileNetworkUtils.lambda$getUniqueSubscriptionDisplayNames$1((SubscriptionInfo) obj);
        return lambda$getUniqueSubscriptionDisplayNames$1;
    }
}
