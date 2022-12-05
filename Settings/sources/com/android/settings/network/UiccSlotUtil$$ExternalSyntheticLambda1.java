package com.android.settings.network;

import android.telephony.UiccSlotInfo;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class UiccSlotUtil$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ UiccSlotUtil$$ExternalSyntheticLambda1 INSTANCE = new UiccSlotUtil$$ExternalSyntheticLambda1();

    private /* synthetic */ UiccSlotUtil$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean lambda$getSlotInfos$0;
        lambda$getSlotInfos$0 = UiccSlotUtil.lambda$getSlotInfos$0((UiccSlotInfo) obj);
        return lambda$getSlotInfos$0;
    }
}
