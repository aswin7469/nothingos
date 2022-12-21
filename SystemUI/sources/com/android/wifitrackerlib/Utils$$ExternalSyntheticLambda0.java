package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import java.util.function.ToIntFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Utils$$ExternalSyntheticLambda0 implements ToIntFunction {
    public final int applyAsInt(Object obj) {
        return ((ScanResult) obj).level;
    }
}
