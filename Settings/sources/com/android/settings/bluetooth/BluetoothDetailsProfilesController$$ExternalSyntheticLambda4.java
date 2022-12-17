package com.android.settings.bluetooth;

import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BluetoothDetailsProfilesController$$ExternalSyntheticLambda4 implements Predicate {
    public final boolean test(Object obj) {
        return ((CachedBluetoothDevice) obj).isBusy();
    }
}
