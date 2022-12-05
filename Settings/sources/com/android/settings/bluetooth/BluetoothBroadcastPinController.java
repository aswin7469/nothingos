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
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.BroadcastProfile;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
@Keep
/* loaded from: classes.dex */
public class BluetoothBroadcastPinController extends BasePreferenceController implements OnDestroy, BluetoothCallback {
    public static final String BLUETOOTH_LE_AUDIO_MASK_PROP = "persist.vendor.service.bt.adv_audio_mask";
    public static final int BROADCAST_AUDIO_MASK = 4;
    public static final String KEY_BROADCAST_AUDIO_PIN = "bluetooth_screen_broadcast_pin_configure";
    public static final String TAG = "BluetoothBroadcastPinController";
    private boolean isBluetoothLeBroadcastAudioSupported;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mCallbacksRegistered;
    private Context mContext;
    private Fragment mFragment;
    private Handler mHandler;
    private LocalBluetoothManager mManager;
    private MetricsFeatureProvider mMetricsFeatureProvider;
    RestrictedPreference mPreference;
    private Runnable mRunnable;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return KEY_BROADCAST_AUDIO_PIN;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onA2dpCodecConfigChanged(CachedBluetoothDevice cachedBluetoothDevice, BluetoothCodecStatus bluetoothCodecStatus) {
        super.onA2dpCodecConfigChanged(cachedBluetoothDevice, bluetoothCodecStatus);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onAclConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onAclConnectionStateChanged(cachedBluetoothDevice, i);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onActiveDeviceChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onActiveDeviceChanged(cachedBluetoothDevice, i);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onAudioModeChanged() {
        super.onAudioModeChanged();
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onBroadcastStateChanged(int i) {
        super.onBroadcastStateChanged(i);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onConnectionStateChanged(cachedBluetoothDevice, i);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
        super.onDeviceAdded(cachedBluetoothDevice);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onDeviceBondStateChanged(cachedBluetoothDevice, i);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onDeviceDeleted(CachedBluetoothDevice cachedBluetoothDevice) {
        super.onDeviceDeleted(cachedBluetoothDevice);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onGroupDiscoveryStatusChanged(int i, int i2, int i3) {
        super.onGroupDiscoveryStatusChanged(i, i2, i3);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onNewGroupFound(CachedBluetoothDevice cachedBluetoothDevice, int i, UUID uuid) {
        super.onNewGroupFound(cachedBluetoothDevice, i, uuid);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        super.onProfileConnectionStateChanged(cachedBluetoothDevice, i, i2);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onScanningStateChanged(boolean z) {
        super.onScanningStateChanged(z);
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BluetoothBroadcastPinController(Context context) {
        super(context, KEY_BROADCAST_AUDIO_PIN);
        this.mFragment = null;
        boolean z = false;
        this.isBluetoothLeBroadcastAudioSupported = false;
        this.mCallbacksRegistered = false;
        this.mManager = null;
        this.mRunnable = new Runnable() { // from class: com.android.settings.bluetooth.BluetoothBroadcastPinController.1
            @Override // java.lang.Runnable
            public void run() {
                BluetoothBroadcastPinController.this.onBroadcastKeyGenerated();
            }
        };
        this.isBluetoothLeBroadcastAudioSupported = (SystemProperties.getInt("persist.vendor.service.bt.adv_audio_mask", 0) & 4) == 4 ? true : z;
        Log.d(TAG, "Constructor()");
        this.mContext = context;
        this.mHandler = new Handler();
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.isBluetoothLeBroadcastAudioSupported) {
            this.mManager = Utils.getLocalBtManager(context);
            if (this.mCallbacksRegistered) {
                return;
            }
            Log.d(TAG, "Registering EventManager callbacks");
            this.mCallbacksRegistered = true;
            this.mManager.getEventManager().registerCallback(this);
        }
    }

    public BluetoothBroadcastPinController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, String str) {
        super(context, KEY_BROADCAST_AUDIO_PIN);
        this.mFragment = null;
        this.isBluetoothLeBroadcastAudioSupported = false;
        this.mCallbacksRegistered = false;
        this.mManager = null;
        this.mRunnable = new Runnable() { // from class: com.android.settings.bluetooth.BluetoothBroadcastPinController.1
            @Override // java.lang.Runnable
            public void run() {
                BluetoothBroadcastPinController.this.onBroadcastKeyGenerated();
            }
        };
        Log.d(TAG, "PinController()" + str);
        this.mFragment = preferenceFragmentCompat;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public void setFragment(Fragment fragment) {
        Log.d(TAG, "setFragment");
        this.mFragment = fragment;
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        Log.d(TAG, "getAvailabilityStatus");
        return this.isBluetoothLeBroadcastAudioSupported ? 0 : 3;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
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

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public boolean handlePreferenceTreeClick(Preference preference) {
        Log.d(TAG, "PinController: handlePreferenceTreeClick");
        if (KEY_BROADCAST_AUDIO_PIN.equals(preference.getKey())) {
            Log.d(TAG, "PinController: handlePreferenceTreeClick true");
            new BluetoothBroadcastPinFragment().show(this.mFragment.getFragmentManager(), "PinFragment");
            return true;
        }
        return false;
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onBluetoothStateChanged(int i) {
        int i2;
        Log.d(TAG, "onBluetoothStateChanged" + Integer.toString(i));
        if (i == 10) {
            i2 = 0;
        } else if (i != 12) {
            return;
        } else {
            i2 = 200;
        }
        this.mHandler.postDelayed(this.mRunnable, i2);
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
            int i3 = 15 - i;
            if (bArr[i3] == 0) {
                break;
            }
            bArr2[i2] = bArr[i3];
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

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onBroadcastKeyGenerated() {
        String str;
        Log.d(TAG, "onBroadcastKeyGenerated");
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BroadcastProfile broadcastProfile = (BroadcastProfile) this.mManager.getProfileManager().getBroadcastProfile();
        String str2 = "Broadcast code: ";
        if (this.mBluetoothAdapter.getState() == 12 && broadcastProfile.isProfileReady()) {
            byte[] encryptionKey = broadcastProfile.getEncryptionKey();
            if (encryptionKey.length == 16) {
                for (int i = 0; i < encryptionKey.length; i++) {
                    Log.d(TAG, "pin(" + Integer.toString(i) + "): " + String.format("%02X", Byte.valueOf(encryptionKey[i])));
                }
                str = convertBytesToString(encryptionKey);
            } else {
                str = "Unavailable";
            }
            if (str.equals("")) {
                str2 = "No Broadcast code";
            }
            this.mPreference.setSummary(str2 + str);
            this.mPreference.setVisible(true);
            if (str.equals("Unavailable")) {
                this.mPreference.setEnabled(false);
                return;
            } else {
                this.mPreference.setEnabled(true);
                return;
            }
        }
        this.mPreference.setSummary(str2 + "Unavailable");
        this.mPreference.setEnabled(false);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnDestroy
    public void onDestroy() {
        Log.d(TAG, "onDestory");
        this.mCallbacksRegistered = false;
        LocalBluetoothManager localBluetoothManager = this.mManager;
        if (localBluetoothManager != null) {
            localBluetoothManager.getEventManager().unregisterCallback(this);
        }
    }
}
