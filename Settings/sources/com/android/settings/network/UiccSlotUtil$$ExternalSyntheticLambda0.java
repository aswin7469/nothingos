package com.android.settings.network;

import android.telephony.UiccSlotInfo;
import java.util.function.IntFunction;
/* loaded from: classes.dex */
public final /* synthetic */ class UiccSlotUtil$$ExternalSyntheticLambda0 implements IntFunction {
    public static final /* synthetic */ UiccSlotUtil$$ExternalSyntheticLambda0 INSTANCE = new UiccSlotUtil$$ExternalSyntheticLambda0();

    private /* synthetic */ UiccSlotUtil$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.IntFunction
    public final Object apply(int i) {
        UiccSlotInfo[] lambda$getSlotInfos$1;
        lambda$getSlotInfos$1 = UiccSlotUtil.lambda$getSlotInfos$1(i);
        return lambda$getSlotInfos$1;
    }
}
