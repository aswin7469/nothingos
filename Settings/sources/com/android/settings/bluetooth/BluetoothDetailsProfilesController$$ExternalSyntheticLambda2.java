package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settingslib.bluetooth.A2dpProfile;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BluetoothDetailsProfilesController$$ExternalSyntheticLambda2 implements Preference.OnPreferenceClickListener {
    public final /* synthetic */ BluetoothDetailsProfilesController f$0;
    public final /* synthetic */ SwitchPreference f$1;
    public final /* synthetic */ A2dpProfile f$2;
    public final /* synthetic */ BluetoothDevice f$3;

    public /* synthetic */ BluetoothDetailsProfilesController$$ExternalSyntheticLambda2(BluetoothDetailsProfilesController bluetoothDetailsProfilesController, SwitchPreference switchPreference, A2dpProfile a2dpProfile, BluetoothDevice bluetoothDevice) {
        this.f$0 = bluetoothDetailsProfilesController;
        this.f$1 = switchPreference;
        this.f$2 = a2dpProfile;
        this.f$3 = bluetoothDevice;
    }

    public final boolean onPreferenceClick(Preference preference) {
        return this.f$0.lambda$maybeAddHighQualityAudioPref$3(this.f$1, this.f$2, this.f$3, preference);
    }
}
