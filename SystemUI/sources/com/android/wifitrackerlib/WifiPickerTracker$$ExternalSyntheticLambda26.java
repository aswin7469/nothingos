package com.android.wifitrackerlib;

import java.util.function.Predicate;
/* loaded from: classes2.dex */
public final /* synthetic */ class WifiPickerTracker$$ExternalSyntheticLambda26 implements Predicate {
    public static final /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda26 INSTANCE = new WifiPickerTracker$$ExternalSyntheticLambda26();

    private /* synthetic */ WifiPickerTracker$$ExternalSyntheticLambda26() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean lambda$updateWifiEntries$1;
        lambda$updateWifiEntries$1 = WifiPickerTracker.lambda$updateWifiEntries$1((StandardWifiEntry) obj);
        return lambda$updateWifiEntries$1;
    }
}
