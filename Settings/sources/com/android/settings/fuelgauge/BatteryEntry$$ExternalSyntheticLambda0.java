package com.android.settings.fuelgauge;

import java.util.Comparator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BatteryEntry$$ExternalSyntheticLambda0 implements Comparator {
    public final int compare(Object obj, Object obj2) {
        return Double.compare(((BatteryEntry) obj2).getConsumedPower(), ((BatteryEntry) obj).getConsumedPower());
    }
}
