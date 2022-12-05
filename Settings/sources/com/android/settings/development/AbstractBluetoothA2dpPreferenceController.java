package com.android.settings.development;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothCodecConfig;
import android.bluetooth.BluetoothCodecStatus;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.development.DeveloperOptionsPreferenceController;
/* loaded from: classes.dex */
public abstract class AbstractBluetoothA2dpPreferenceController extends DeveloperOptionsPreferenceController implements Preference.OnPreferenceChangeListener, PreferenceControllerMixin, BluetoothServiceConnectionListener, LifecycleObserver, OnDestroy {
    static final int STREAMING_LABEL_ID = R.string.bluetooth_select_a2dp_codec_streaming_label;
    protected BluetoothA2dp mBluetoothA2dp;
    protected final BluetoothA2dpConfigStore mBluetoothA2dpConfigStore;
    protected ListPreference mPreference;
    private final String[] mListValues = getListValues();
    private final String[] mListSummaries = getListSummaries();

    protected abstract int getCurrentA2dpSettingIndex(BluetoothCodecConfig bluetoothCodecConfig);

    protected abstract int getDefaultIndex();

    protected abstract String[] getListSummaries();

    protected abstract String[] getListValues();

    @Override // com.android.settings.development.BluetoothServiceConnectionListener
    public void onBluetoothCodecUpdated() {
    }

    protected abstract void writeConfigurationValues(Object obj);

    public AbstractBluetoothA2dpPreferenceController(Context context, Lifecycle lifecycle, BluetoothA2dpConfigStore bluetoothA2dpConfigStore) {
        super(context);
        this.mBluetoothA2dpConfigStore = bluetoothA2dpConfigStore;
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    @Override // com.android.settingslib.development.DeveloperOptionsPreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        ListPreference listPreference = (ListPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = listPreference;
        listPreference.setValue(this.mListValues[getDefaultIndex()]);
        this.mPreference.setSummary(this.mListSummaries[getDefaultIndex()]);
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (this.mBluetoothA2dp == null) {
            return false;
        }
        writeConfigurationValues(obj);
        BluetoothCodecConfig createCodecConfig = this.mBluetoothA2dpConfigStore.createCodecConfig();
        synchronized (this.mBluetoothA2dpConfigStore) {
            BluetoothA2dp bluetoothA2dp = this.mBluetoothA2dp;
            if (bluetoothA2dp != null) {
                BluetoothDevice activeDevice = bluetoothA2dp.getActiveDevice();
                if (activeDevice == null) {
                    return false;
                }
                setCodecConfigPreference(activeDevice, createCodecConfig);
            }
            int findIndexOfValue = this.mPreference.findIndexOfValue(obj.toString());
            if (findIndexOfValue == getDefaultIndex()) {
                this.mPreference.setSummary(this.mListSummaries[findIndexOfValue]);
            } else {
                this.mPreference.setSummary(this.mContext.getResources().getString(STREAMING_LABEL_ID, this.mListSummaries[findIndexOfValue]));
            }
            return true;
        }
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        BluetoothDevice activeDevice;
        BluetoothCodecConfig codecConfig;
        BluetoothA2dp bluetoothA2dp = this.mBluetoothA2dp;
        if (bluetoothA2dp == null || (activeDevice = bluetoothA2dp.getActiveDevice()) == null || getCodecConfig(activeDevice) == null || this.mPreference == null) {
            return;
        }
        synchronized (this.mBluetoothA2dpConfigStore) {
            codecConfig = getCodecConfig(activeDevice);
        }
        int currentA2dpSettingIndex = getCurrentA2dpSettingIndex(codecConfig);
        this.mPreference.setValue(this.mListValues[currentA2dpSettingIndex]);
        if (currentA2dpSettingIndex == getDefaultIndex()) {
            this.mPreference.setSummary(this.mListSummaries[currentA2dpSettingIndex]);
        } else {
            this.mPreference.setSummary(this.mContext.getResources().getString(STREAMING_LABEL_ID, this.mListSummaries[currentA2dpSettingIndex]));
        }
        writeConfigurationValues(this.mListValues[currentA2dpSettingIndex]);
    }

    @Override // com.android.settings.development.BluetoothServiceConnectionListener
    public void onBluetoothServiceConnected(BluetoothA2dp bluetoothA2dp) {
        this.mBluetoothA2dp = bluetoothA2dp;
        updateState(this.mPreference);
    }

    @Override // com.android.settings.development.BluetoothServiceConnectionListener
    public void onBluetoothServiceDisconnected() {
        this.mBluetoothA2dp = null;
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnDestroy
    public void onDestroy() {
        this.mBluetoothA2dp = null;
    }

    void setCodecConfigPreference(BluetoothDevice bluetoothDevice, BluetoothCodecConfig bluetoothCodecConfig) {
        if (bluetoothDevice == null) {
            bluetoothDevice = this.mBluetoothA2dp.getActiveDevice();
        }
        if (bluetoothDevice == null) {
            return;
        }
        this.mBluetoothA2dp.setCodecConfigPreference(bluetoothDevice, bluetoothCodecConfig);
    }

    BluetoothCodecConfig getCodecConfig(BluetoothDevice bluetoothDevice) {
        BluetoothCodecStatus codecStatus;
        BluetoothA2dp bluetoothA2dp = this.mBluetoothA2dp;
        if (bluetoothA2dp != null) {
            if (bluetoothDevice == null) {
                bluetoothDevice = bluetoothA2dp.getActiveDevice();
            }
            if (bluetoothDevice != null && (codecStatus = this.mBluetoothA2dp.getCodecStatus(bluetoothDevice)) != null) {
                return codecStatus.getCodecConfig();
            }
        }
        return null;
    }
}
