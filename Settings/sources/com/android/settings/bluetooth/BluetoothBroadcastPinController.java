package com.android.settings.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothCodecStatus;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.Log;
import androidx.annotation.Keep;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.BroadcastProfile;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

@Keep
public class BluetoothBroadcastPinController extends BasePreferenceController implements LifecycleObserver, OnDestroy, BluetoothCallback {
    public static final String BLUETOOTH_LE_AUDIO_MASK_PROP = "persist.vendor.service.bt.adv_audio_mask";
    public static final int BROADCAST_AUDIO_MASK = 4;
    public static final String KEY_BROADCAST_AUDIO_PIN = "bluetooth_screen_broadcast_pin_configure";
    public static final String TAG = "BluetoothBroadcastPinController";
    private boolean isBluetoothLeBroadcastAudioSupported;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mCallbacksRegistered;
    private Context mContext;
    private Fragment mFragment = null;
    private Handler mHandler;
    private LocalBluetoothManager mManager;
    private MetricsFeatureProvider mMetricsFeatureProvider;
    RestrictedPreference mPreference;
    private Runnable mRunnable;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getPreferenceKey() {
        return KEY_BROADCAST_AUDIO_PIN;
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ void onA2dpCodecConfigChanged(CachedBluetoothDevice cachedBluetoothDevice, BluetoothCodecStatus bluetoothCodecStatus) {
        super.onA2dpCodecConfigChanged(cachedBluetoothDevice, bluetoothCodecStatus);
    }

    public /* bridge */ /* synthetic */ void onAclConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onAclConnectionStateChanged(cachedBluetoothDevice, i);
    }

    public /* bridge */ /* synthetic */ void onActiveDeviceChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onActiveDeviceChanged(cachedBluetoothDevice, i);
    }

    public /* bridge */ /* synthetic */ void onAudioModeChanged() {
        super.onAudioModeChanged();
    }

    public /* bridge */ /* synthetic */ void onBroadcastStateChanged(int i) {
        super.onBroadcastStateChanged(i);
    }

    public /* bridge */ /* synthetic */ void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onConnectionStateChanged(cachedBluetoothDevice, i);
    }

    public /* bridge */ /* synthetic */ void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
        super.onDeviceAdded(cachedBluetoothDevice);
    }

    public /* bridge */ /* synthetic */ void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onDeviceBondStateChanged(cachedBluetoothDevice, i);
    }

    public /* bridge */ /* synthetic */ void onDeviceDeleted(CachedBluetoothDevice cachedBluetoothDevice) {
        super.onDeviceDeleted(cachedBluetoothDevice);
    }

    public /* bridge */ /* synthetic */ void onGroupDiscoveryStatusChanged(int i, int i2, int i3) {
        super.onGroupDiscoveryStatusChanged(i, i2, i3);
    }

    public /* bridge */ /* synthetic */ void onNewGroupFound(CachedBluetoothDevice cachedBluetoothDevice, int i, UUID uuid) {
        super.onNewGroupFound(cachedBluetoothDevice, i, uuid);
    }

    public /* bridge */ /* synthetic */ void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        super.onProfileConnectionStateChanged(cachedBluetoothDevice, i, i2);
    }

    public /* bridge */ /* synthetic */ void onScanningStateChanged(boolean z) {
        super.onScanningStateChanged(z);
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BluetoothBroadcastPinController(Context context, Lifecycle lifecycle) {
        super(context, KEY_BROADCAST_AUDIO_PIN);
        boolean z = false;
        this.isBluetoothLeBroadcastAudioSupported = false;
        this.mCallbacksRegistered = false;
        this.mManager = null;
        this.mRunnable = new Runnable() {
            public void run() {
                BluetoothBroadcastPinController.this.onBroadcastKeyGenerated();
            }
        };
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
        this.isBluetoothLeBroadcastAudioSupported = (SystemProperties.getInt("persist.vendor.service.bt.adv_audio_mask", 0) & 4) == 4 ? true : z;
        Log.d(TAG, "Constructor()");
        this.mContext = context;
        this.mHandler = new Handler();
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.isBluetoothLeBroadcastAudioSupported) {
            this.mManager = Utils.getLocalBtManager(context);
            if (!this.mCallbacksRegistered) {
                Log.d(TAG, "Registering EventManager callbacks");
                this.mCallbacksRegistered = true;
                this.mManager.getEventManager().registerCallback(this);
            }
        }
    }

    public void setFragment(Fragment fragment) {
        Log.d(TAG, "setFragment");
        this.mFragment = fragment;
    }

    public int getAvailabilityStatus() {
        Log.d(TAG, "getAvailabilityStatus");
        return this.isBluetoothLeBroadcastAudioSupported ? 0 : 3;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Log.d(TAG, "displayPreference");
        RestrictedPreference restrictedPreference = (RestrictedPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = restrictedPreference;
        if (this.isBluetoothLeBroadcastAudioSupported) {
            onBroadcastKeyGenerated();
        } else {
            restrictedPreference.setVisible(false);
        }
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        Log.d(TAG, "PinController: handlePreferenceTreeClick");
        if (!KEY_BROADCAST_AUDIO_PIN.equals(preference.getKey())) {
            return false;
        }
        Log.d(TAG, "PinController: handlePreferenceTreeClick true");
        new BluetoothBroadcastPinFragment().show(this.mFragment.getFragmentManager(), "PinFragment");
        return true;
    }

    public void onBluetoothStateChanged(int i) {
        int i2;
        Log.d(TAG, "onBluetoothStateChanged" + Integer.toString(i));
        if (i == 10) {
            i2 = 0;
        } else if (i == 12) {
            i2 = 200;
        } else {
            return;
        }
        this.mHandler.postDelayed(this.mRunnable, (long) i2);
    }

    private String convertBytesToString(byte[] bArr) {
        String str;
        if (bArr.length != 16) {
            Log.e(TAG, "Not 16 bytes ++++++++++++");
            return "";
        }
        byte[] bArr2 = new byte[16];
        int i = 0;
        int i2 = 0;
        while (i < 16) {
            byte b = bArr[15 - i];
            if (b == 0) {
                break;
            }
            bArr2[i2] = b;
            i++;
            i2++;
        }
        if (i2 == 0) {
            str = new String("");
        } else {
            str = new String(Arrays.copyOfRange(bArr2, 0, i2), StandardCharsets.UTF_8);
        }
        Log.d(TAG, "Pin: " + str);
        return str;
    }

    public void onBroadcastKeyGenerated() {
        String str;
        Log.d(TAG, "onBroadcastKeyGenerated");
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BroadcastProfile broadcastProfile = (BroadcastProfile) this.mManager.getProfileManager().getBroadcastProfile();
        String str2 = "Broadcast code: ";
        if (this.mBluetoothAdapter.getState() != 12 || !broadcastProfile.isProfileReady()) {
            this.mPreference.setSummary((CharSequence) str2 + "Unavailable");
            this.mPreference.setEnabled(false);
            return;
        }
        byte[] encryptionKey = broadcastProfile.getEncryptionKey();
        if (encryptionKey.length == 16) {
            for (int i = 0; i < encryptionKey.length; i++) {
                Log.d(TAG, "pin(" + Integer.toString(i) + "): " + String.format("%02X", new Object[]{Byte.valueOf(encryptionKey[i])}));
            }
            str = convertBytesToString(encryptionKey);
        } else {
            str = "Unavailable";
        }
        if (str.equals("")) {
            str2 = "No Broadcast code";
        }
        this.mPreference.setSummary((CharSequence) str2 + str);
        this.mPreference.setVisible(true);
        if (str.equals("Unavailable")) {
            this.mPreference.setEnabled(false);
        } else {
            this.mPreference.setEnabled(true);
        }
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        this.mCallbacksRegistered = false;
        LocalBluetoothManager localBluetoothManager = this.mManager;
        if (localBluetoothManager != null) {
            localBluetoothManager.getEventManager().unregisterCallback(this);
        }
    }
}
