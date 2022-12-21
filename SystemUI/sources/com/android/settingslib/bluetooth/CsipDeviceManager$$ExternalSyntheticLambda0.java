package com.android.settingslib.bluetooth;

import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class CsipDeviceManager$$ExternalSyntheticLambda0 implements Predicate {
    public final /* synthetic */ CachedBluetoothDevice f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ CsipDeviceManager$$ExternalSyntheticLambda0(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        this.f$0 = cachedBluetoothDevice;
        this.f$1 = i;
    }

    public final boolean test(Object obj) {
        return CsipDeviceManager.lambda$onGroupIdChanged$0(this.f$0, this.f$1, (CachedBluetoothDevice) obj);
    }
}
