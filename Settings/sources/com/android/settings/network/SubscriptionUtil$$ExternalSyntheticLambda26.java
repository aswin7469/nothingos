package com.android.settings.network;

import android.content.Context;
import com.android.settings.network.SubscriptionUtil;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SubscriptionUtil$$ExternalSyntheticLambda26 implements Function {
    public final /* synthetic */ boolean f$0;
    public final /* synthetic */ Context f$1;

    public /* synthetic */ SubscriptionUtil$$ExternalSyntheticLambda26(boolean z, Context context) {
        this.f$0 = z;
        this.f$1 = context;
    }

    public final Object apply(Object obj) {
        return SubscriptionUtil.lambda$getNtUniqueSubscriptionDisplayNames$21(this.f$0, this.f$1, (SubscriptionUtil.AnonymousClass2DisplayInfo) obj);
    }
}
