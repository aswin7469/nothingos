package com.android.settings.development.bluetooth;

import android.bluetooth.BluetoothCodecConfig;
import android.content.Context;
import android.util.Log;
import androidx.preference.PreferenceScreen;
import com.android.settings.development.BluetoothA2dpConfigStore;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class BluetoothSampleRateDialogPreferenceController extends AbstractBluetoothDialogPreferenceController {
    public String getPreferenceKey() {
        return "bluetooth_sample_rate_settings";
    }

    public BluetoothSampleRateDialogPreferenceController(Context context, Lifecycle lifecycle, BluetoothA2dpConfigStore bluetoothA2dpConfigStore) {
        super(context, lifecycle, bluetoothA2dpConfigStore);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        ((BaseBluetoothDialogPreference) this.mPreference).setCallback(this);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeConfigurationValues(int r1) {
        /*
            r0 = this;
            switch(r1) {
                case 0: goto L_0x0013;
                case 1: goto L_0x0011;
                case 2: goto L_0x000f;
                case 3: goto L_0x000d;
                case 4: goto L_0x000a;
                case 5: goto L_0x0007;
                case 6: goto L_0x0004;
                default: goto L_0x0003;
            }
        L_0x0003:
            goto L_0x0026
        L_0x0004:
            r1 = 32
            goto L_0x0027
        L_0x0007:
            r1 = 16
            goto L_0x0027
        L_0x000a:
            r1 = 8
            goto L_0x0027
        L_0x000d:
            r1 = 4
            goto L_0x0027
        L_0x000f:
            r1 = 2
            goto L_0x0027
        L_0x0011:
            r1 = 1
            goto L_0x0027
        L_0x0013:
            android.bluetooth.BluetoothCodecConfig r1 = r0.getCurrentCodecConfig()
            if (r1 == 0) goto L_0x0026
            int r1 = r1.getCodecType()
            android.bluetooth.BluetoothCodecConfig r1 = r0.getSelectableByCodecType(r1)
            int r1 = com.android.settings.development.bluetooth.AbstractBluetoothDialogPreferenceController.getHighestSampleRate(r1)
            goto L_0x0027
        L_0x0026:
            r1 = 0
        L_0x0027:
            com.android.settings.development.BluetoothA2dpConfigStore r0 = r0.mBluetoothA2dpConfigStore
            r0.setSampleRate(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.development.bluetooth.BluetoothSampleRateDialogPreferenceController.writeConfigurationValues(int):void");
    }

    /* access modifiers changed from: protected */
    public int getCurrentIndexByConfig(BluetoothCodecConfig bluetoothCodecConfig) {
        if (bluetoothCodecConfig == null) {
            Log.e("BtSampleRateCtr", "Unable to get current config index. Config is null.");
        }
        return convertCfgToBtnIndex(bluetoothCodecConfig.getSampleRate());
    }

    public List<Integer> getSelectableIndex() {
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

    /* access modifiers changed from: package-private */
    public int convertCfgToBtnIndex(int i) {
        int defaultIndex = getDefaultIndex();
        if (i == 1) {
            return 1;
        }
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
}
