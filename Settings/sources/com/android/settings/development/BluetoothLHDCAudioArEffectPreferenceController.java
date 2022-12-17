package com.android.settings.development;

import android.bluetooth.BluetoothCodecConfig;
import android.content.Context;
import com.android.settings.R$array;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class BluetoothLHDCAudioArEffectPreferenceController extends AbstractBluetoothA2dpPreferenceController {
    /* access modifiers changed from: protected */
    public int getDefaultIndex() {
        return 0;
    }

    public String getPreferenceKey() {
        return "bluetooth_enable_a2dp_codec_lhdc_ar_effect";
    }

    public BluetoothLHDCAudioArEffectPreferenceController(Context context, Lifecycle lifecycle, BluetoothA2dpConfigStore bluetoothA2dpConfigStore) {
        super(context, lifecycle, bluetoothA2dpConfigStore);
    }

    /* access modifiers changed from: protected */
    public String[] getListValues() {
        return this.mContext.getResources().getStringArray(R$array.bluetooth_enable_a2dp_codec_lhdc_ar_effect_values);
    }

    /* access modifiers changed from: protected */
    public String[] getListSummaries() {
        return this.mContext.getResources().getStringArray(R$array.bluetooth_enable_a2dp_codec_lhdc_ar_effect_summaries);
    }

    /* access modifiers changed from: protected */
    public void writeConfigurationValues(Object obj) {
        this.mBluetoothA2dpConfigStore.setCodecSpecific3Value(this.mPreference.findIndexOfValue(obj.toString()) != 0 ? 1275068418 : 1275068416);
    }

    /* access modifiers changed from: protected */
    public int getCurrentA2dpSettingIndex(BluetoothCodecConfig bluetoothCodecConfig) {
        int codecSpecific3 = (int) bluetoothCodecConfig.getCodecSpecific3();
        return ((-16777216 & codecSpecific3) != 1275068416 || (codecSpecific3 & 2) == 0) ? 0 : 1;
    }
}
