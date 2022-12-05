package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.SliderPreferenceController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.HeadsetProfile;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import com.android.settingslib.bluetooth.VcpProfile;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import java.lang.reflect.InvocationTargetException;
/* loaded from: classes.dex */
public class BADeviceVolumeController extends SliderPreferenceController implements CachedBluetoothDevice.Callback, LifecycleObserver, OnPause, OnResume {
    public static final String BLUETOOTH_ADV_AUDIO_MASK_PROP = "persist.vendor.service.bt.adv_audio_mask";
    public static final String BLUETOOTH_VCP_FOR_BROADCAST_PROP = "persist.vendor.service.bt.vcpForBroadcast";
    public static final int BROADCAST_AUDIO_MASK = 2;
    private static final String KEY_BA_DEVICE_VOLUME = "ba_device_volume";
    private static final String TAG = "BADeviceVolumeController";
    private static final String VCACHED_DEVICE_CLASS = "com.android.settingslib.bluetooth.VendorCachedBluetoothDevice";
    AudioManager mAudioManager;
    private CachedBluetoothDevice mCachedDevice;
    private HeadsetProfile mHeadsetProfile;
    private boolean mIsVcpForBroadcastSupported;
    private LocalBluetoothManager mLocalBluetoothManager;
    protected BADeviceVolumePreference mPreference;
    protected LocalBluetoothProfileManager mProfileManager;
    private VcpProfile mVcpProfile = null;
    private Class<?> mVCachedDeviceClass = null;
    private Object mVendorCachedDevice = null;

    @Override // com.android.settings.core.SliderPreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.SliderPreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.core.SliderPreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return KEY_BA_DEVICE_VOLUME;
    }

    @Override // com.android.settings.core.SliderPreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.core.SliderPreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.core.SliderPreferenceController, com.android.settings.slices.Sliceable
    public boolean isPublicSlice() {
        return true;
    }

    @Override // com.android.settings.core.SliderPreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BADeviceVolumeController(Context context) {
        super(context, KEY_BA_DEVICE_VOLUME);
        boolean z = false;
        this.mIsVcpForBroadcastSupported = false;
        if ((SystemProperties.getInt("persist.vendor.service.bt.adv_audio_mask", 0) & 2) == 2 && SystemProperties.getBoolean(BLUETOOTH_VCP_FOR_BROADCAST_PROP, false)) {
            z = true;
        }
        this.mIsVcpForBroadcastSupported = z;
        Log.d(TAG, "mIsVcpForBroadcastSupported: " + this.mIsVcpForBroadcastSupported);
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        Log.d(TAG, "getAvailabilityStatus");
        return this.mIsVcpForBroadcastSupported ? 0 : 3;
    }

    @Override // com.android.settings.core.SliderPreferenceController, com.android.settings.slices.Sliceable
    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), KEY_BA_DEVICE_VOLUME);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        if (isAvailable()) {
            BADeviceVolumePreference bADeviceVolumePreference = (BADeviceVolumePreference) preferenceScreen.findPreference(getPreferenceKey());
            this.mPreference = bADeviceVolumePreference;
            AudioManager audioManager = this.mAudioManager;
            if (audioManager != null) {
                bADeviceVolumePreference.setMax(audioManager.getStreamMaxVolume(3));
                this.mPreference.setMin(this.mAudioManager.getStreamMinVolume(3));
                refresh();
                return;
            }
            bADeviceVolumePreference.setVisible(false);
        }
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnPause
    public void onPause() {
        Log.d(TAG, "onPause");
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        if (cachedBluetoothDevice != null) {
            cachedBluetoothDevice.unregisterCallback(this);
        }
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnResume
    public void onResume() {
        Log.d(TAG, "onResume");
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        if (cachedBluetoothDevice != null) {
            cachedBluetoothDevice.registerCallback(this);
            refresh();
        }
    }

    public void init(DashboardFragment dashboardFragment, LocalBluetoothManager localBluetoothManager, CachedBluetoothDevice cachedBluetoothDevice) {
        Log.d(TAG, "Init");
        if (this.mIsVcpForBroadcastSupported) {
            this.mCachedDevice = cachedBluetoothDevice;
            this.mLocalBluetoothManager = localBluetoothManager;
            LocalBluetoothProfileManager profileManager = localBluetoothManager.getProfileManager();
            this.mProfileManager = profileManager;
            this.mVcpProfile = profileManager.getVcpProfile();
            this.mAudioManager = (AudioManager) this.mContext.getSystemService(AudioManager.class);
            try {
                Class<?> cls = Class.forName(VCACHED_DEVICE_CLASS);
                this.mVCachedDeviceClass = cls;
                this.mVendorCachedDevice = cls.getDeclaredMethod("getVendorCachedBluetoothDevice", CachedBluetoothDevice.class, LocalBluetoothProfileManager.class).invoke(null, this.mCachedDevice, this.mProfileManager);
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            this.mHeadsetProfile = this.mProfileManager.getHeadsetProfile();
        }
    }

    protected void refresh() {
        Log.d(TAG, "refresh");
        if (!this.mIsVcpForBroadcastSupported || this.mVcpProfile == null) {
            Log.d(TAG, "VCP for broadcast is not supported");
            return;
        }
        boolean enableSlider = enableSlider();
        BluetoothDevice device = this.mCachedDevice.getDevice();
        int audioState = this.mHeadsetProfile.getAudioState(device);
        boolean z = audioState == 11 || audioState == 12;
        Log.d(TAG, "VCP refresh showSlider: " + enableSlider + " inCall: " + z);
        if (this.mVcpProfile.getConnectionStatus(device) == 2 && (this.mVcpProfile.getConnectionMode(device) & 2) != 0) {
            Log.d(TAG, "VCP is connected for broadcast ");
            this.mPreference.setVisible(true);
            if (!enableSlider || z) {
                this.mPreference.setProgress(0);
                this.mPreference.setEnabled(false);
                return;
            }
            this.mPreference.setEnabled(true);
            int absoluteVolume = this.mVcpProfile.getAbsoluteVolume(device);
            if (absoluteVolume == -1) {
                return;
            }
            this.mPreference.setProgress(absoluteVolume);
            return;
        }
        this.mPreference.setVisible(false);
    }

    @Override // com.android.settingslib.bluetooth.CachedBluetoothDevice.Callback
    public void onDeviceAttributesChanged() {
        refresh();
    }

    @Override // com.android.settings.core.SliderPreferenceController
    public int getSliderPosition() {
        BADeviceVolumePreference bADeviceVolumePreference = this.mPreference;
        if (bADeviceVolumePreference != null) {
            return bADeviceVolumePreference.getProgress();
        }
        VcpProfile vcpProfile = this.mVcpProfile;
        if (vcpProfile == null) {
            return 0;
        }
        return vcpProfile.getAbsoluteVolume(this.mCachedDevice.getDevice());
    }

    @Override // com.android.settings.core.SliderPreferenceController
    public boolean setSliderPosition(int i) {
        BADeviceVolumePreference bADeviceVolumePreference = this.mPreference;
        if (bADeviceVolumePreference != null) {
            bADeviceVolumePreference.setProgress(i);
        }
        VcpProfile vcpProfile = this.mVcpProfile;
        if (vcpProfile != null) {
            vcpProfile.setAbsoluteVolume(this.mCachedDevice.getDevice(), i);
            return true;
        }
        return false;
    }

    @Override // com.android.settings.core.SliderPreferenceController
    public int getMax() {
        BADeviceVolumePreference bADeviceVolumePreference = this.mPreference;
        if (bADeviceVolumePreference != null) {
            return bADeviceVolumePreference.getMax();
        }
        AudioManager audioManager = this.mAudioManager;
        if (audioManager == null) {
            return 0;
        }
        return audioManager.getStreamMaxVolume(3);
    }

    @Override // com.android.settings.core.SliderPreferenceController
    public int getMin() {
        BADeviceVolumePreference bADeviceVolumePreference = this.mPreference;
        if (bADeviceVolumePreference != null) {
            return bADeviceVolumePreference.getMin();
        }
        AudioManager audioManager = this.mAudioManager;
        if (audioManager == null) {
            return 0;
        }
        return audioManager.getStreamMinVolume(3);
    }

    private boolean enableSlider() {
        Class<?> cls = this.mVCachedDeviceClass;
        if (cls == null || this.mVendorCachedDevice == null) {
            Log.d(TAG, "enableSlider: false");
            return false;
        }
        try {
            Boolean bool = (Boolean) cls.getDeclaredMethod("isBroadcastAudioSynced", new Class[0]).invoke(this.mVendorCachedDevice, new Object[0]);
            Log.d(TAG, "enableSlider: " + bool);
            return bool.booleanValue();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Log.i(TAG, "Exception" + e);
            Log.d(TAG, "enableSlider: false");
            return false;
        }
    }
}
