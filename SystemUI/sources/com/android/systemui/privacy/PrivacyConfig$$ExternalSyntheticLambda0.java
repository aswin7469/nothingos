package com.android.systemui.privacy;

import android.provider.DeviceConfig;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class PrivacyConfig$$ExternalSyntheticLambda0 implements DeviceConfig.OnPropertiesChangedListener {
    public final /* synthetic */ PrivacyConfig f$0;

    public /* synthetic */ PrivacyConfig$$ExternalSyntheticLambda0(PrivacyConfig privacyConfig) {
        this.f$0 = privacyConfig;
    }

    public final void onPropertiesChanged(DeviceConfig.Properties properties) {
        PrivacyConfig.m2872devicePropertiesChangedListener$lambda3(this.f$0, properties);
    }
}
