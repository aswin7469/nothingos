package com.android.settings.network.helper;

import android.telephony.UiccCardInfo;
import java.util.function.ToIntFunction;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class QueryEsimCardId$$ExternalSyntheticLambda2 implements ToIntFunction {
    public final int applyAsInt(Object obj) {
        return ((UiccCardInfo) obj).getCardId();
    }
}
