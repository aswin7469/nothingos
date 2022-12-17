package com.android.settings.network;

import android.text.TextUtils;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class MobileNetworkIntentConverter$$ExternalSyntheticLambda16 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ MobileNetworkIntentConverter$$ExternalSyntheticLambda16(String str) {
        this.f$0 = str;
    }

    public final boolean test(Object obj) {
        return TextUtils.equals(this.f$0, (String) obj);
    }
}
