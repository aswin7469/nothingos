package com.android.settings.bluetooth;

import android.content.Context;
import android.media.AudioDeviceAttributes;
import android.media.AudioManager;
import android.media.Spatializer;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.nothing.p006ui.support.NtCustSwitchPreference;

public class BluetoothDetailsSpatialAudioController extends BluetoothDetailsController implements Preference.OnPreferenceClickListener {
    AudioDeviceAttributes mAudioDevice = new AudioDeviceAttributes(2, 8, this.mCachedDevice.getAddress());
    AudioDeviceAttributes mLEAudioDevice = new AudioDeviceAttributes(2, 26, this.mCachedDevice.getAddress());
    PreferenceCategory mProfilesContainer;
    private final Spatializer mSpatializer;

    public String getPreferenceKey() {
        return "spatial_audio_group";
    }

    public BluetoothDetailsSpatialAudioController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, CachedBluetoothDevice cachedBluetoothDevice, Lifecycle lifecycle) {
        super(context, preferenceFragmentCompat, cachedBluetoothDevice, lifecycle);
        this.mSpatializer = ((AudioManager) context.getSystemService(AudioManager.class)).getSpatializer();
    }

    public boolean isAvailable() {
        return this.mSpatializer.isAvailableForDevice(this.mAudioDevice) || this.mSpatializer.isAvailableForDevice(this.mLEAudioDevice);
    }

    public boolean onPreferenceClick(Preference preference) {
        SwitchPreference switchPreference = (SwitchPreference) preference;
        String key = switchPreference.getKey();
        if (TextUtils.equals(key, "spatial_audio")) {
            if (switchPreference.isChecked()) {
                this.mSpatializer.addCompatibleAudioDevice(this.mAudioDevice);
                this.mSpatializer.addCompatibleAudioDevice(this.mLEAudioDevice);
            } else {
                this.mSpatializer.removeCompatibleAudioDevice(this.mAudioDevice);
                this.mSpatializer.removeCompatibleAudioDevice(this.mLEAudioDevice);
            }
            refresh();
            return true;
        } else if (TextUtils.equals(key, "head_tracking")) {
            this.mSpatializer.setHeadTrackerEnabled(switchPreference.isChecked(), this.mAudioDevice);
            return true;
        } else {
            Log.w("BluetoothSpatialAudioController", "invalid key name.");
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void init(PreferenceScreen preferenceScreen) {
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mProfilesContainer = preferenceCategory;
        preferenceCategory.setLayoutResource(R$layout.preference_bluetooth_profile_category);
        refresh();
    }

    /* access modifiers changed from: protected */
    public void refresh() {
        SwitchPreference switchPreference = (SwitchPreference) this.mProfilesContainer.findPreference("spatial_audio");
        if (switchPreference == null) {
            switchPreference = createSpatialAudioPreference(this.mProfilesContainer.getContext());
            this.mProfilesContainer.addPreference(switchPreference);
        }
        boolean z = false;
        boolean z2 = this.mSpatializer.getCompatibleAudioDevices().contains(this.mAudioDevice) || this.mSpatializer.getCompatibleAudioDevices().contains(this.mLEAudioDevice);
        Log.d("BluetoothSpatialAudioController", "refresh() isSpatialAudioOn : " + z2);
        switchPreference.setChecked(z2);
        SwitchPreference switchPreference2 = (SwitchPreference) this.mProfilesContainer.findPreference("head_tracking");
        if (switchPreference2 == null) {
            switchPreference2 = createHeadTrackingPreference(this.mProfilesContainer.getContext());
            this.mProfilesContainer.addPreference(switchPreference2);
        }
        if (z2 && this.mSpatializer.hasHeadTracker(this.mAudioDevice)) {
            z = true;
        }
        Log.d("BluetoothSpatialAudioController", "refresh() has head tracker : " + this.mSpatializer.hasHeadTracker(this.mAudioDevice));
        switchPreference2.setVisible(z);
        if (z) {
            switchPreference2.setChecked(this.mSpatializer.isHeadTrackerEnabled(this.mAudioDevice));
        }
    }

    /* access modifiers changed from: package-private */
    public SwitchPreference createSpatialAudioPreference(Context context) {
        NtCustSwitchPreference ntCustSwitchPreference = new NtCustSwitchPreference(context);
        ntCustSwitchPreference.setKey("spatial_audio");
        ntCustSwitchPreference.setTitle((CharSequence) context.getString(R$string.bluetooth_details_spatial_audio_title));
        ntCustSwitchPreference.setSummary((CharSequence) context.getString(R$string.bluetooth_details_spatial_audio_summary));
        ntCustSwitchPreference.setOnPreferenceClickListener(this);
        return ntCustSwitchPreference;
    }

    /* access modifiers changed from: package-private */
    public SwitchPreference createHeadTrackingPreference(Context context) {
        NtCustSwitchPreference ntCustSwitchPreference = new NtCustSwitchPreference(context);
        ntCustSwitchPreference.setKey("head_tracking");
        ntCustSwitchPreference.setTitle((CharSequence) context.getString(R$string.bluetooth_details_head_tracking_title));
        ntCustSwitchPreference.setSummary((CharSequence) context.getString(R$string.bluetooth_details_head_tracking_summary));
        ntCustSwitchPreference.setOnPreferenceClickListener(this);
        return ntCustSwitchPreference;
    }
}
