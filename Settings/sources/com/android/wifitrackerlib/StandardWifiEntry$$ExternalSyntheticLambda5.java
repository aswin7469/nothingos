package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.ToIntFunction;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StandardWifiEntry$$ExternalSyntheticLambda5 implements ToIntFunction {
    public final int applyAsInt(Object obj) {
        return ((ScanResult) obj).level;
    }
}
