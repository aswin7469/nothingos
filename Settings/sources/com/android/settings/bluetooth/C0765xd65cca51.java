package com.android.settings.bluetooth;

import android.widget.LinearLayout;

/* renamed from: com.android.settings.bluetooth.AdvancedBluetoothDetailsHeaderController$$ExternalSyntheticLambda3 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0765xd65cca51 implements Runnable {
    public final /* synthetic */ AdvancedBluetoothDetailsHeaderController f$0;
    public final /* synthetic */ LinearLayout f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ long f$3;

    public /* synthetic */ C0765xd65cca51(AdvancedBluetoothDetailsHeaderController advancedBluetoothDetailsHeaderController, LinearLayout linearLayout, int i, long j) {
        this.f$0 = advancedBluetoothDetailsHeaderController;
        this.f$1 = linearLayout;
        this.f$2 = i;
        this.f$3 = j;
    }

    public final void run() {
        this.f$0.lambda$showBatteryPredictionIfNecessary$1(this.f$1, this.f$2, this.f$3);
    }
}
