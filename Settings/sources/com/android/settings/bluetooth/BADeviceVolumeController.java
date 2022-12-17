package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.SystemProperties;
import android.sysprop.BluetoothProperties;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.SliderPreferenceController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.HeadsetProfile;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import com.android.settingslib.bluetooth.VcpProfile;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

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
    private boolean mIsVcpForBroadcastSupported = false;
    private LocalBluetoothManager mLocalBluetoothManager;
    protected BADeviceVolumePreference mPreference;
    protected LocalBluetoothProfileManager mProfileManager;
    private Class<?> mVCachedDeviceClass = null;
    private VcpProfile mVcpProfile = null;
    private Object mVendorCachedDevice = null;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getPreferenceKey() {
        return KEY_BA_DEVICE_VOLUME;
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public boolean isPublicSlice() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BADeviceVolumeController(Context context) {
        super(context, KEY_BA_DEVICE_VOLUME);
        boolean z;
        Optional isProfileBapBroadcastSourceEnabled = BluetoothProperties.isProfileBapBroadcastSourceEnabled();
        Boolean bool = Boolean.FALSE;
        boolean z2 = true;
        if (((Boolean) isProfileBapBroadcastSourceEnabled.orElse(bool)).booleanValue() || ((Boolean) BluetoothProperties.isProfileBapBroadcastAssistEnabled().orElse(bool)).booleanValue()) {
            Log.d(TAG, "Broadcast is supported");
            z = true;
        } else {
            z = false;
        }
        this.mIsVcpForBroadcastSupported = ((SystemProperties.getInt("persist.vendor.service.bt.adv_audio_mask", 0) & 2) != 2 || !SystemProperties.getBoolean(BLUETOOTH_VCP_FOR_BROADCAST_PROP, false)) ? false : z2;
        if (z) {
            this.mIsVcpForBroadcastSupported = false;
        }
        Log.d(TAG, "mIsVcpForBroadcastSupported: " + this.mIsVcpForBroadcastSupported);
    }

    public int getAvailabilityStatus() {
        Log.d(TAG, "getAvailabilityStatus");
        return this.mIsVcpForBroadcastSupported ? 0 : 3;
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), KEY_BA_DEVICE_VOLUME);
    }

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

    public void onPause() {
        Log.d(TAG, "onPause");
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        if (cachedBluetoothDevice != null) {
            cachedBluetoothDevice.unregisterCallback(this);
        }
    }

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
                this.mVendorCachedDevice = cls.getDeclaredMethod("getVendorCachedBluetoothDevice", new Class[]{CachedBluetoothDevice.class, LocalBluetoothProfileManager.class}).invoke((Object) null, new Object[]{this.mCachedDevice, this.mProfileManager});
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            this.mHeadsetProfile = this.mProfileManager.getHeadsetProfile();
        }
    }

    /* access modifiers changed from: protected */
    public void refresh() {
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
        if (this.mVcpProfile.getConnectionStatus(device) != 2 || (this.mVcpProfile.getConnectionMode(device) & 2) == 0) {
            this.mPreference.setVisible(false);
            return;
        }
        Log.d(TAG, "VCP is connected for broadcast ");
        this.mPreference.setVisible(true);
        if (!enableSlider || z) {
            this.mPreference.setProgress(0);
            this.mPreference.setEnabled(false);
            return;
        }
        this.mPreference.setEnabled(true);
        int absoluteVolume = this.mVcpProfile.getAbsoluteVolume(device);
        if (absoluteVolume != -1) {
            this.mPreference.setProgress(absoluteVolume);
        }
    }

    public void onDeviceAttributesChanged() {
        refresh();
    }

    public int getSliderPosition() {
        BADeviceVolumePreference bADeviceVolumePreference = this.mPreference;
        if (bADeviceVolumePreference != null) {
            return bADeviceVolumePreference.getProgress();
        }
        VcpProfile vcpProfile = this.mVcpProfile;
        if (vcpProfile != null) {
            return vcpProfile.getAbsoluteVolume(this.mCachedDevice.getDevice());
        }
        return 0;
    }

    public boolean setSliderPosition(int i) {
        BADeviceVolumePreference bADeviceVolumePreference = this.mPreference;
        if (bADeviceVolumePreference != null) {
            bADeviceVolumePreference.setProgress(i);
        }
        VcpProfile vcpProfile = this.mVcpProfile;
        if (vcpProfile == null) {
            return false;
        }
        vcpProfile.setAbsoluteVolume(this.mCachedDevice.getDevice(), i);
        return true;
    }

    public int getMax() {
        BADeviceVolumePreference bADeviceVolumePreference = this.mPreference;
        if (bADeviceVolumePreference != null) {
            return bADeviceVolumePreference.getMax();
        }
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null) {
            return audioManager.getStreamMaxVolume(3);
        }
        return 0;
    }

    public int getMin() {
        BADeviceVolumePreference bADeviceVolumePreference = this.mPreference;
        if (bADeviceVolumePreference != null) {
            return bADeviceVolumePreference.getMin();
        }
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null) {
            return audioManager.getStreamMinVolume(3);
        }
        return 0;
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
