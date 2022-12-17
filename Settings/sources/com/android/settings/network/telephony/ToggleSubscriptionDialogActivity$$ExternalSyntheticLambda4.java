package com.android.settings.network.telephony;

import android.telephony.UiccPortInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ToggleSubscriptionDialogActivity$$ExternalSyntheticLambda4 implements Predicate {
    public final boolean test(Object obj) {
        return ((UiccPortInfo) obj).isActive();
    }
}
