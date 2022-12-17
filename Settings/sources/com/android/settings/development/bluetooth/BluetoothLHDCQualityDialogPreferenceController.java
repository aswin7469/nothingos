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

public class BluetoothLHDCQualityDialogPreferenceController extends AbstractBluetoothDialogPreferenceController {
    public String getPreferenceKey() {
        return "bluetooth_select_a2dp_lhdc_playback_quality";
    }

    public BluetoothLHDCQualityDialogPreferenceController(Context context, Lifecycle lifecycle, BluetoothA2dpConfigStore bluetoothA2dpConfigStore) {
        super(context, lifecycle, bluetoothA2dpConfigStore);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        ((BaseBluetoothDialogPreference) this.mPreference).setCallback(this);
    }

    /* access modifiers changed from: protected */
    public void writeConfigurationValues(int i) {
        this.mBluetoothA2dpConfigStore.setCodecSpecific1Value(i <= 9 ? (long) (i | 32768) : 32773);
    }

    /* access modifiers changed from: protected */
    public int getCurrentIndexByConfig(BluetoothCodecConfig bluetoothCodecConfig) {
        if (bluetoothCodecConfig == null) {
            Log.e("BtLhdcAudioQualityCtr", "Unable to get current config index. Config is null.");
        }
        return convertCfgToBtnIndex((int) bluetoothCodecConfig.getCodecSpecific1());
    }

    public List<Integer> getSelectableIndex() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i <= 9; i++) {
            arrayList.add(Integer.valueOf(i));
        }
        return arrayList;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        BluetoothCodecConfig currentCodecConfig = getCurrentCodecConfig();
        if (currentCodecConfig == null || !(currentCodecConfig.getCodecType() == 8 || currentCodecConfig.getCodecType() == 7 || currentCodecConfig.getCodecType() == 6 || currentCodecConfig.getCodecType() == 9)) {
            preference.setEnabled(false);
            preference.setSummary((CharSequence) "");
            return;
        }
        preference.setEnabled(true);
    }

    public void onHDAudioEnabled(boolean z) {
        this.mPreference.setEnabled(false);
    }

    /* access modifiers changed from: package-private */
    public int convertCfgToBtnIndex(int i) {
        return (49152 & i) != 32768 ? getDefaultIndex() : i & 255;
    }
}
