package com.android.wifitrackerlib;

import android.net.wifi.WifiConfiguration;
import java.util.function.Function;
/* loaded from: classes2.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda8 implements Function {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda8 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda8();

    private /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda8() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        Integer lambda$updateWifiConfigurations$20;
        lambda$updateWifiConfigurations$20 = WifiPickerTracker.lambda$updateWifiConfigurations$20((WifiConfiguration) obj);
        return lambda$updateWifiConfigurations$20;
    }
}
