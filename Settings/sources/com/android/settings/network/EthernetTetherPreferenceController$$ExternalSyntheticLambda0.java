package com.android.settings.network;

import android.net.EthernetManager;
import android.net.IpConfiguration;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class EthernetTetherPreferenceController$$ExternalSyntheticLambda0 implements EthernetManager.InterfaceStateListener {
    public final /* synthetic */ EthernetTetherPreferenceController f$0;

    public /* synthetic */ EthernetTetherPreferenceController$$ExternalSyntheticLambda0(EthernetTetherPreferenceController ethernetTetherPreferenceController) {
        this.f$0 = ethernetTetherPreferenceController;
    }

    public final void onInterfaceStateChanged(String str, int i, int i2, IpConfiguration ipConfiguration) {
        this.f$0.lambda$onStart$0(str, i, i2, ipConfiguration);
    }
}
