package com.android.settings.network.telephony;

import com.android.settings.network.telephony.MobileNetworkUtils;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class MobileNetworkUtils$$ExternalSyntheticLambda4 implements Function {
    public static final /* synthetic */ MobileNetworkUtils$$ExternalSyntheticLambda4 INSTANCE = new MobileNetworkUtils$$ExternalSyntheticLambda4();

    private /* synthetic */ MobileNetworkUtils$$ExternalSyntheticLambda4() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        CharSequence charSequence;
        charSequence = ((MobileNetworkUtils.C1DisplayInfo) obj).uniqueName;
        return charSequence;
    }
}
