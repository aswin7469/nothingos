package com.android.settings.wifi.addappnetworks;

import android.net.wifi.hotspot2.PasspointConfiguration;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class AddAppNetworksFragment$$ExternalSyntheticLambda3 implements Predicate {
    public final /* synthetic */ PasspointConfiguration f$0;

    public /* synthetic */ AddAppNetworksFragment$$ExternalSyntheticLambda3(PasspointConfiguration passpointConfiguration) {
        this.f$0 = passpointConfiguration;
    }

    public final boolean test(Object obj) {
        return ((PasspointConfiguration) obj).equals(this.f$0);
    }
}
