package com.android.systemui.assist;

import android.provider.DeviceConfig;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DeviceConfigHelper$$ExternalSyntheticLambda3 implements Supplier {
    public final /* synthetic */ String f$0;
    public final /* synthetic */ String f$1;

    public /* synthetic */ DeviceConfigHelper$$ExternalSyntheticLambda3(String str, String str2) {
        this.f$0 = str;
        this.f$1 = str2;
    }

    public final Object get() {
        return DeviceConfig.getString("systemui", this.f$0, this.f$1);
    }
}
