package com.android.settings.network.telephony;

import com.android.settings.network.helper.SubscriptionAnnotation;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class MobileNetworkActivity$$ExternalSyntheticLambda4 implements Predicate {
    public static final /* synthetic */ MobileNetworkActivity$$ExternalSyntheticLambda4 INSTANCE = new MobileNetworkActivity$$ExternalSyntheticLambda4();

    private /* synthetic */ MobileNetworkActivity$$ExternalSyntheticLambda4() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return ((SubscriptionAnnotation) obj).isActive();
    }
}
