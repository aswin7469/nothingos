package com.nothing.p023os.device;

import android.os.Bundle;

/* renamed from: com.nothing.os.device.DeviceServiceController$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DeviceServiceController$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ DeviceServiceController f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ Bundle f$2;

    public /* synthetic */ DeviceServiceController$$ExternalSyntheticLambda3(DeviceServiceController deviceServiceController, int i, Bundle bundle) {
        this.f$0 = deviceServiceController;
        this.f$1 = i;
        this.f$2 = bundle;
    }

    public final void run() {
        DeviceServiceController.m3493onSuccess$lambda1(this.f$0, this.f$1, this.f$2);
    }
}
