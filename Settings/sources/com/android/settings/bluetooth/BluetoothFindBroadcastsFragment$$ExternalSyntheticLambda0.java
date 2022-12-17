package com.android.settings.bluetooth;

import androidx.preference.Preference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BluetoothFindBroadcastsFragment$$ExternalSyntheticLambda0 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ BluetoothFindBroadcastsFragment f$0;
    public final /* synthetic */ BluetoothBroadcastSourcePreference f$1;

    public /* synthetic */ BluetoothFindBroadcastsFragment$$ExternalSyntheticLambda0(BluetoothFindBroadcastsFragment bluetoothFindBroadcastsFragment, BluetoothBroadcastSourcePreference bluetoothBroadcastSourcePreference) {
        this.f$0 = bluetoothFindBroadcastsFragment;
        this.f$1 = bluetoothBroadcastSourcePreference;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$createBluetoothBroadcastSourcePreference$0(this.f$1, preference);
    }
}
