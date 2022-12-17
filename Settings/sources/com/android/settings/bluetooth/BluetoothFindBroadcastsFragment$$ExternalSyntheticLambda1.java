package com.android.settings.bluetooth;

import android.content.DialogInterface;
import android.widget.EditText;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BluetoothFindBroadcastsFragment$$ExternalSyntheticLambda1 implements DialogInterface.OnClickListener {
    public final /* synthetic */ BluetoothFindBroadcastsFragment f$0;
    public final /* synthetic */ BluetoothBroadcastSourcePreference f$1;
    public final /* synthetic */ EditText f$2;

    public /* synthetic */ BluetoothFindBroadcastsFragment$$ExternalSyntheticLambda1(BluetoothFindBroadcastsFragment bluetoothFindBroadcastsFragment, BluetoothBroadcastSourcePreference bluetoothBroadcastSourcePreference, EditText editText) {
        this.f$0 = bluetoothFindBroadcastsFragment;
        this.f$1 = bluetoothBroadcastSourcePreference;
        this.f$2 = editText;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$launchBroadcastCodeDialog$2(this.f$1, this.f$2, dialogInterface, i);
    }
}
