package com.android.settings.network.telephony;

import android.telephony.UiccSlotInfo;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda0 implements Predicate {
    public static final /* synthetic */ ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda0 INSTANCE = new ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda0();

    private /* synthetic */ ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean lambda$isDsdsConditionSatisfied$0;
        lambda$isDsdsConditionSatisfied$0 = ToggleSubscriptionDialogActivity.lambda$isDsdsConditionSatisfied$0((UiccSlotInfo) obj);
        return lambda$isDsdsConditionSatisfied$0;
    }
}
