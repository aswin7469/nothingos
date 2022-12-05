package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.ToIntFunction;
/* loaded from: classes2.dex */
public final /* synthetic */ class StandardWifiEntry$$ExternalSyntheticLambda5 implements ToIntFunction {
    public static final /* synthetic */ StandardWifiEntry$$ExternalSyntheticLambda5 INSTANCE = new StandardWifiEntry$$ExternalSyntheticLambda5();

    private /* synthetic */ StandardWifiEntry$$ExternalSyntheticLambda5() {
    }

    @Override // java.util.function.ToIntFunction
    public final int applyAsInt(Object obj) {
        int lambda$getScanResultDescription$4;
        lambda$getScanResultDescription$4 = StandardWifiEntry.lambda$getScanResultDescription$4((ScanResult) obj);
        return lambda$getScanResultDescription$4;
    }
}
