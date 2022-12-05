package com.android.settings.development;

import android.bluetooth.BluetoothCodecConfig;
import android.content.Context;
import com.android.settings.R;
import com.android.settingslib.core.lifecycle.Lifecycle;
/* loaded from: classes.dex */
public class BluetoothLHDCAudioArEffectPreferenceController extends AbstractBluetoothA2dpPreferenceController {
    @Override // com.android.settings.development.AbstractBluetoothA2dpPreferenceController
    protected int getDefaultIndex() {
        return 0;
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "bluetooth_enable_a2dp_codec_lhdc_ar_effect";
    }

    public BluetoothLHDCAudioArEffectPreferenceController(Context context, Lifecycle lifecycle, BluetoothA2dpConfigStore bluetoothA2dpConfigStore) {
        super(context, lifecycle, bluetoothA2dpConfigStore);
    }

    @Override // com.android.settings.development.AbstractBluetoothA2dpPreferenceController
    protected String[] getListValues() {
        return this.mContext.getResources().getStringArray(R.array.bluetooth_enable_a2dp_codec_lhdc_ar_effect_values);
    }

    @Override // com.android.settings.development.AbstractBluetoothA2dpPreferenceController
    protected String[] getListSummaries() {
        return this.mContext.getResources().getStringArray(R.array.bluetooth_enable_a2dp_codec_lhdc_ar_effect_summaries);
    }

    @Override // com.android.settings.development.AbstractBluetoothA2dpPreferenceController
    protected void writeConfigurationValues(Object obj) {
        this.mBluetoothA2dpConfigStore.setCodecSpecific3Value(((AbstractBluetoothA2dpPreferenceController) this).mPreference.findIndexOfValue(obj.toString()) != 0 ? 1275068418 : 1275068416);
    }

    @Override // com.android.settings.development.AbstractBluetoothA2dpPreferenceController
    protected int getCurrentA2dpSettingIndex(BluetoothCodecConfig bluetoothCodecConfig) {
        int codecSpecific3 = (int) bluetoothCodecConfig.getCodecSpecific3();
        return (((-16777216) & codecSpecific3) != 1275068416 || (codecSpecific3 & 2) == 0) ? 0 : 1;
    }
}
