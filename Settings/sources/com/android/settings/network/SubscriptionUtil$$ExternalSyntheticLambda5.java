package com.android.settings.network;

import com.android.settings.network.SubscriptionUtil;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda5 implements Function {
    public static final /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda5 INSTANCE = new SubscriptionUtil$$ExternalSyntheticLambda5();

    private /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda5() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        CharSequence charSequence;
        charSequence = ((SubscriptionUtil.C1DisplayInfo) obj).uniqueName;
        return charSequence;
    }
}
