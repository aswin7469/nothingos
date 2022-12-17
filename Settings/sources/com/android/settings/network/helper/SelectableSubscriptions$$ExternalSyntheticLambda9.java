package com.android.settings.network.helper;

import android.telephony.SubscriptionManager;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SelectableSubscriptions$$ExternalSyntheticLambda9 implements Function {
    public final Object apply(Object obj) {
        return ((SubscriptionManager) obj).getActiveSubscriptionInfoList();
    }
}
