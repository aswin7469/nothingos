package com.android.systemui.assist;

import android.provider.DeviceConfig;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DeviceConfigHelper$$ExternalSyntheticLambda1 implements Supplier {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ DeviceConfigHelper$$ExternalSyntheticLambda1(String str, boolean z) {
        this.f$0 = str;
        this.f$1 = z;
    }

    public final Object get() {
        return Boolean.valueOf(DeviceConfig.getBoolean("systemui", this.f$0, this.f$1));
    }
}
