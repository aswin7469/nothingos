package com.android.settings.network;

import android.security.LegacyVpnProfileStore;
import com.android.internal.net.VpnProfile;
import java.util.function.Function;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class VpnPreferenceController$$ExternalSyntheticLambda1 implements Function {
    public final Object apply(Object obj) {
        return VpnProfile.decode((String) obj, LegacyVpnProfileStore.get("VPN_" + ((String) obj)));
    }
}
