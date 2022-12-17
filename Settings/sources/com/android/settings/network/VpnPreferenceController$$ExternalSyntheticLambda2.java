package com.android.settings.network;

import com.android.internal.net.VpnProfile;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class VpnPreferenceController$$ExternalSyntheticLambda2 implements Predicate {
    public final boolean test(Object obj) {
        return VpnProfile.isLegacyType(((VpnProfile) obj).type);
    }
}
