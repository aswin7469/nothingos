package com.android.settings.development.bluetooth;

import android.bluetooth.BluetoothCodecConfig;
import android.content.Context;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.development.BluetoothA2dpConfigStore;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class BluetoothLHDCQualityDialogPreferenceController extends AbstractBluetoothDialogPreferenceController {
    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "bluetooth_select_a2dp_lhdc_playback_quality";
    }

    public BluetoothLHDCQualityDialogPreferenceController(Context context, Lifecycle lifecycle, BluetoothA2dpConfigStore bluetoothA2dpConfigStore) {
        super(context, lifecycle, bluetoothA2dpConfigStore);
    }

    @Override // com.android.settingslib.development.DeveloperOptionsPreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        ((BaseBluetoothDialogPreference) this.mPreference).setCallback(this);
    }

    @Override // com.android.settings.development.bluetooth.AbstractBluetoothDialogPreferenceController
    protected void writeConfigurationValues(int i) {
        this.mBluetoothA2dpConfigStore.setCodecSpecific1Value(i <= 9 ? i | 32768 : 32773L);
    }

    @Override // com.android.settings.development.bluetooth.AbstractBluetoothDialogPreferenceController
    protected int getCurrentIndexByConfig(BluetoothCodecConfig bluetoothCodecConfig) {
        if (bluetoothCodecConfig == null) {
            Log.e("BtLhdcAudioQualityCtr", "Unable to get current config index. Config is null.");
        }
        return convertCfgToBtnIndex((int) bluetoothCodecConfig.getCodecSpecific1());
    }

    @Override // com.android.settings.development.bluetooth.BaseBluetoothDialogPreference.Callback
    public List<Integer> getSelectableIndex() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i <= 9; i++) {
            arrayList.add(Integer.valueOf(i));
        }
        return arrayList;
    }

    @Override // com.android.settings.development.bluetooth.AbstractBluetoothDialogPreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        BluetoothCodecConfig currentCodecConfig = getCurrentCodecConfig();
        if (currentCodecConfig != null && (currentCodecConfig.getCodecType() == 7 || currentCodecConfig.getCodecType() == 6 || currentCodecConfig.getCodecType() == 5 || currentCodecConfig.getCodecType() == 8)) {
            preference.setEnabled(true);
            return;
        }
        preference.setEnabled(false);
        preference.setSummary("");
    }

    @Override // com.android.settings.development.bluetooth.AbstractBluetoothDialogPreferenceController
    public void onHDAudioEnabled(boolean z) {
        this.mPreference.setEnabled(false);
    }

    int convertCfgToBtnIndex(int i) {
        return (49152 & i) != 32768 ? getDefaultIndex() : i & 255;
    }
}
