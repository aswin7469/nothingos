package com.android.settings.development.bluetooth;

import android.bluetooth.BluetoothCodecConfig;
import android.content.Context;
import android.util.Log;
import androidx.preference.PreferenceScreen;
import com.android.settings.development.BluetoothA2dpConfigStore;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class BluetoothSampleRateDialogPreferenceController extends AbstractBluetoothDialogPreferenceController {
    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "bluetooth_sample_rate_settings";
    }

    public BluetoothSampleRateDialogPreferenceController(Context context, Lifecycle lifecycle, BluetoothA2dpConfigStore bluetoothA2dpConfigStore) {
        super(context, lifecycle, bluetoothA2dpConfigStore);
    }

    @Override // com.android.settingslib.development.DeveloperOptionsPreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        ((BaseBluetoothDialogPreference) this.mPreference).setCallback(this);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.android.settings.development.bluetooth.AbstractBluetoothDialogPreferenceController
    protected void writeConfigurationValues(int i) {
        int i2;
        switch (i) {
            case 0:
                BluetoothCodecConfig currentCodecConfig = getCurrentCodecConfig();
                if (currentCodecConfig != null) {
                    i2 = AbstractBluetoothDialogPreferenceController.getHighestSampleRate(getSelectableByCodecType(currentCodecConfig.getCodecType()));
                    break;
                }
                i2 = 0;
                break;
            case 1:
                i2 = 1;
                break;
            case 2:
                i2 = 2;
                break;
            case 3:
                i2 = 4;
                break;
            case 4:
                i2 = 8;
                break;
            case 5:
                i2 = 16;
                break;
            case 6:
                i2 = 32;
                break;
            default:
                i2 = 0;
                break;
        }
        this.mBluetoothA2dpConfigStore.setSampleRate(i2);
    }

    @Override // com.android.settings.development.bluetooth.AbstractBluetoothDialogPreferenceController
    protected int getCurrentIndexByConfig(BluetoothCodecConfig bluetoothCodecConfig) {
        if (bluetoothCodecConfig == null) {
            Log.e("BtSampleRateCtr", "Unable to get current config index. Config is null.");
        }
        return convertCfgToBtnIndex(bluetoothCodecConfig.getSampleRate());
    }

    @Override // com.android.settings.development.bluetooth.BaseBluetoothDialogPreference.Callback
    public List<Integer> getSelectableIndex() {
        int[] iArr;
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(getDefaultIndex()));
        BluetoothCodecConfig currentCodecConfig = getCurrentCodecConfig();
        if (currentCodecConfig != null) {
            int sampleRate = getSelectableByCodecType(currentCodecConfig.getCodecType()).getSampleRate();
            for (int i : AbstractBluetoothDialogPreferenceController.SAMPLE_RATES) {
                if ((sampleRate & i) != 0) {
                    arrayList.add(Integer.valueOf(convertCfgToBtnIndex(i)));
                }
            }
        }
        return arrayList;
    }

    int convertCfgToBtnIndex(int i) {
        int defaultIndex = getDefaultIndex();
        if (i != 1) {
            if (i == 2) {
                return 2;
            }
            if (i == 4) {
                return 3;
            }
            if (i == 8) {
                return 4;
            }
            if (i == 16) {
                return 5;
            }
            if (i == 32) {
                return 6;
            }
            Log.e("BtSampleRateCtr", "Unsupported config:" + i);
            return defaultIndex;
        }
        return 1;
    }
}
