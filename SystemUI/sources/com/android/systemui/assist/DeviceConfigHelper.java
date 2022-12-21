package com.android.systemui.assist;

import android.provider.DeviceConfig;
import com.android.systemui.DejankUtils;
import com.android.systemui.dagger.SysUISingleton;
import java.util.concurrent.Executor;

@SysUISingleton
public class DeviceConfigHelper {
    public long getLong(String str, long j) {
        return ((Long) DejankUtils.whitelistIpcs(new DeviceConfigHelper$$ExternalSyntheticLambda0(str, j))).longValue();
    }

    public int getInt(String str, int i) {
        return ((Integer) DejankUtils.whitelistIpcs(new DeviceConfigHelper$$ExternalSyntheticLambda2(str, i))).intValue();
    }

    public String getString(String str, String str2) {
        return (String) DejankUtils.whitelistIpcs(new DeviceConfigHelper$$ExternalSyntheticLambda3(str, str2));
    }

    public boolean getBoolean(String str, boolean z) {
        return ((Boolean) DejankUtils.whitelistIpcs(new DeviceConfigHelper$$ExternalSyntheticLambda1(str, z))).booleanValue();
    }

    public void addOnPropertiesChangedListener(Executor executor, DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener) {
        DeviceConfig.addOnPropertiesChangedListener("systemui", executor, onPropertiesChangedListener);
    }

    public void removeOnPropertiesChangedListener(DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener) {
        DeviceConfig.removeOnPropertiesChangedListener(onPropertiesChangedListener);
    }
}
