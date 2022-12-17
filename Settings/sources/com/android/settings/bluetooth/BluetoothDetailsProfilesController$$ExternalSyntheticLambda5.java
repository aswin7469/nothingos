package com.android.settings.bluetooth;

import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothProfile;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BluetoothDetailsProfilesController$$ExternalSyntheticLambda5 implements Predicate {
    public final /* synthetic */ LocalBluetoothProfile f$0;

    public /* synthetic */ BluetoothDetailsProfilesController$$ExternalSyntheticLambda5(LocalBluetoothProfile localBluetoothProfile) {
        this.f$0 = localBluetoothProfile;
    }

    public final boolean test(Object obj) {
        return this.f$0.isEnabled(((CachedBluetoothDevice) obj).getDevice());
    }
}
