package com.android.settings.sim.receivers;

import android.telephony.UiccCardInfo;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SimSlotChangeHandler$$ExternalSyntheticLambda1 implements Predicate {
    public final boolean test(Object obj) {
        return ((UiccCardInfo) obj).isMultipleEnabledProfilesSupported();
    }
}
