package com.android.systemui.assist;

import android.provider.DeviceConfig;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DeviceConfigHelper$$ExternalSyntheticLambda2 implements Supplier {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ DeviceConfigHelper$$ExternalSyntheticLambda2(String str, int i) {
        this.f$0 = str;
        this.f$1 = i;
    }

    public final Object get() {
        return Integer.valueOf(DeviceConfig.getInt("systemui", this.f$0, this.f$1));
    }
}
