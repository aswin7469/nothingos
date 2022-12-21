package com.android.systemui.assist;

import android.provider.DeviceConfig;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DeviceConfigHelper$$ExternalSyntheticLambda0 implements Supplier {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ long f$1;

    public /* synthetic */ DeviceConfigHelper$$ExternalSyntheticLambda0(String str, long j) {
        this.f$0 = str;
        this.f$1 = j;
    }

    public final Object get() {
        return Long.valueOf(DeviceConfig.getLong("systemui", this.f$0, this.f$1));
    }
}
