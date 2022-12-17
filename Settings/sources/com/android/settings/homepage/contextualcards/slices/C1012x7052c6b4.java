package com.android.settings.homepage.contextualcards.slices;

import android.content.Context;
import com.android.settings.homepage.contextualcards.slices.BluetoothUpdateWorker;
import com.android.settingslib.bluetooth.LocalBluetoothManager;

/* renamed from: com.android.settings.homepage.contextualcards.slices.BluetoothUpdateWorker$LoadBtManagerHandler$$ExternalSyntheticLambda1 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C1012x7052c6b4 implements LocalBluetoothManager.BluetoothManagerCallback {
    public final /* synthetic */ BluetoothUpdateWorker.LoadBtManagerHandler f$0;

    public /* synthetic */ C1012x7052c6b4(BluetoothUpdateWorker.LoadBtManagerHandler loadBtManagerHandler) {
        this.f$0 = loadBtManagerHandler;
    }

    public final void onBluetoothManagerInitialized(Context context, LocalBluetoothManager localBluetoothManager) {
        this.f$0.lambda$getLocalBtManager$1(context, localBluetoothManager);
    }
}
