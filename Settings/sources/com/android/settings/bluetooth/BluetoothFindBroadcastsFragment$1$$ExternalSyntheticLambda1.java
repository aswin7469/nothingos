package com.android.settings.bluetooth;

import android.bluetooth.BluetoothLeBroadcastMetadata;
import com.android.settings.bluetooth.BluetoothFindBroadcastsFragment;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BluetoothFindBroadcastsFragment$1$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ BluetoothFindBroadcastsFragment.C07911 f$0;
    public final /* synthetic */ BluetoothLeBroadcastMetadata f$1;

    public /* synthetic */ BluetoothFindBroadcastsFragment$1$$ExternalSyntheticLambda1(BluetoothFindBroadcastsFragment.C07911 r1, BluetoothLeBroadcastMetadata bluetoothLeBroadcastMetadata) {
        this.f$0 = r1;
        this.f$1 = bluetoothLeBroadcastMetadata;
    }

    public final void run() {
        this.f$0.lambda$onSourceFound$1(this.f$1);
    }
}
