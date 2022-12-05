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
public class BluetoothChannelModeDialogPreferenceController extends AbstractBluetoothDialogPreferenceController {
    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "bluetooth_channel_mode_settings";
    }

    public BluetoothChannelModeDialogPreferenceController(Context context, Lifecycle lifecycle, BluetoothA2dpConfigStore bluetoothA2dpConfigStore) {
        super(context, lifecycle, bluetoothA2dpConfigStore);
    }

    @Override // com.android.settingslib.development.DeveloperOptionsPreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        ((BaseBluetoothDialogPreference) this.mPreference).setCallback(this);
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x0006, code lost:
        if (r3 != 2) goto L8;
     */
    @Override // com.android.settings.development.bluetooth.AbstractBluetoothDialogPreferenceController
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void writeConfigurationValues(int i) {
        int i2 = 2;
        if (i == 0) {
            BluetoothCodecConfig currentCodecConfig = getCurrentCodecConfig();
            if (currentCodecConfig != null) {
                i2 = AbstractBluetoothDialogPreferenceController.getHighestChannelMode(getSelectableByCodecType(currentCodecConfig.getCodecType()));
            }
            i2 = 0;
        } else if (i == 1) {
            i2 = 1;
        }
        this.mBluetoothA2dpConfigStore.setChannelMode(i2);
    }

    @Override // com.android.settings.development.bluetooth.AbstractBluetoothDialogPreferenceController
    protected int getCurrentIndexByConfig(BluetoothCodecConfig bluetoothCodecConfig) {
        if (bluetoothCodecConfig == null) {
            Log.e("BtChannelModeCtr", "Unable to get current config index. Config is null.");
        }
        return convertCfgToBtnIndex(bluetoothCodecConfig.getChannelMode());
    }

    @Override // com.android.settings.development.bluetooth.BaseBluetoothDialogPreference.Callback
    public List<Integer> getSelectableIndex() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Integer.valueOf(getDefaultIndex()));
        BluetoothCodecConfig currentCodecConfig = getCurrentCodecConfig();
        if (currentCodecConfig != null) {
            int channelMode = getSelectableByCodecType(currentCodecConfig.getCodecType()).getChannelMode();
            int i = 0;
            while (true) {
                int[] iArr = AbstractBluetoothDialogPreferenceController.CHANNEL_MODES;
                if (i >= iArr.length) {
                    break;
                }
                if ((iArr[i] & channelMode) != 0) {
                    arrayList.add(Integer.valueOf(convertCfgToBtnIndex(iArr[i])));
                }
                i++;
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
            Log.e("BtChannelModeCtr", "Unsupported config:" + i);
            return defaultIndex;
        }
        return 1;
    }
}
