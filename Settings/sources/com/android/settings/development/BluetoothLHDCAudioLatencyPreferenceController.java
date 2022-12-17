package com.android.settings.development;

import android.bluetooth.BluetoothCodecConfig;
import android.content.Context;
import com.android.settings.R$array;
import com.android.settingslib.core.lifecycle.Lifecycle;

public class BluetoothLHDCAudioLatencyPreferenceController extends AbstractBluetoothA2dpPreferenceController {
    /* access modifiers changed from: protected */
    public int getDefaultIndex() {
        return 0;
    }

    public String getPreferenceKey() {
        return "bluetooth_select_a2dp_codec_lhdc_latency";
    }

    public BluetoothLHDCAudioLatencyPreferenceController(Context context, Lifecycle lifecycle, BluetoothA2dpConfigStore bluetoothA2dpConfigStore) {
        super(context, lifecycle, bluetoothA2dpConfigStore);
    }

    /* access modifiers changed from: protected */
    public String[] getListValues() {
        return this.mContext.getResources().getStringArray(R$array.bluetooth_a2dp_codec_lhdc_latency_values);
    }

    /* access modifiers changed from: protected */
    public String[] getListSummaries() {
        return this.mContext.getResources().getStringArray(R$array.bluetooth_a2dp_codec_lhdc_latency_summaries);
    }

    /* access modifiers changed from: protected */
    public void writeConfigurationValues(Object obj) {
        int findIndexOfValue = this.mPreference.findIndexOfValue(obj.toString());
        int i = 49152;
        if (findIndexOfValue <= 1) {
            i = 49152 | findIndexOfValue;
        }
        this.mBluetoothA2dpConfigStore.setCodecSpecific2Value(i);
    }

    /* access modifiers changed from: protected */
    public int getCurrentA2dpSettingIndex(BluetoothCodecConfig bluetoothCodecConfig) {
        int codecSpecific2 = (int) bluetoothCodecConfig.getCodecSpecific2();
        if ((codecSpecific2 & 49152) == 49152) {
            return codecSpecific2 & 1;
        }
        return 0;
    }
}
