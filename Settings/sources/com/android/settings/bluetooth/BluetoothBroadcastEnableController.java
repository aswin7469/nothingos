package com.android.settings.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothCodecStatus;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import androidx.annotation.Keep;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.TogglePreferenceController;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.BroadcastProfile;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import java.util.UUID;

@Keep
public class BluetoothBroadcastEnableController extends TogglePreferenceController implements LifecycleObserver, OnDestroy, BluetoothCallback {
    public static final String BLUETOOTH_LE_AUDIO_MASK_PROP = "persist.vendor.service.bt.adv_audio_mask";
    public static final int BROADCAST_AUDIO_MASK = 4;
    public static final String KEY_BROADCAST_ENABLE = "bluetooth_screen_broadcast_enable";
    public static final String TAG = "BluetoothBroadcastEnableController";
    private boolean isBluetoothLeBroadcastAudioSupported = false;
    /* access modifiers changed from: private */
    public BroadcastProfile mBapBroadcastProfile = null;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mCallbacksRegistered = false;
    private Context mContext;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            Log.d(BluetoothBroadcastEnableController.TAG, "BT state, msg = " + Integer.toString(message.what));
            switch (message.what) {
                case 10:
                case 11:
                case 13:
                    BluetoothBroadcastEnableController.this.reset_pending = false;
                    BluetoothBroadcastEnableController.this.onStateChanged(false);
                    BluetoothBroadcastEnableController.this.mBapBroadcastProfile = null;
                    if (BluetoothBroadcastEnableController.this.mPreference != null) {
                        BluetoothBroadcastEnableController.this.mPreference.setEnabled(false);
                        return;
                    }
                    return;
                case 12:
                    if (BluetoothBroadcastEnableController.this.mPreference != null) {
                        BluetoothBroadcastEnableController.this.mPreference.setEnabled(true);
                    }
                    BluetoothBroadcastEnableController bluetoothBroadcastEnableController = BluetoothBroadcastEnableController.this;
                    bluetoothBroadcastEnableController.mBapBroadcastProfile = (BroadcastProfile) bluetoothBroadcastEnableController.mManager.getProfileManager().getBroadcastProfile();
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public LocalBluetoothManager mManager = null;
    /* access modifiers changed from: private */
    public RestrictedSwitchPreference mPreference = null;
    private boolean mState = false;
    /* access modifiers changed from: private */
    public boolean reset_pending = false;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
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

    public BluetoothBroadcastEnableController(Context context, String str, Lifecycle lifecycle) {
        super(context, str);
        Log.d(TAG, "Constructor()");
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
        Init(context);
    }

    private void Init(Context context) {
        this.mContext = context;
        boolean z = false;
        if ((SystemProperties.getInt("persist.vendor.service.bt.adv_audio_mask", 0) & 4) == 4) {
            z = true;
        }
        this.isBluetoothLeBroadcastAudioSupported = z;
        if (z) {
            LocalBluetoothManager localBtManager = Utils.getLocalBtManager(context);
            this.mManager = localBtManager;
            this.mBapBroadcastProfile = (BroadcastProfile) localBtManager.getProfileManager().getBroadcastProfile();
            if (!this.mCallbacksRegistered) {
                Log.d(TAG, "Registering EventManager callbacks");
                this.mCallbacksRegistered = true;
                this.mManager.getEventManager().registerCallback(this);
            }
        }
        Log.d(TAG, "Init done");
    }

    private void updateState(boolean z) {
        Log.d(TAG, "updateState req " + Boolean.toString(z));
        if (z != this.mState) {
            RestrictedSwitchPreference restrictedSwitchPreference = this.mPreference;
            if (restrictedSwitchPreference != null) {
                restrictedSwitchPreference.setEnabled(false);
            }
            Log.d(TAG, "updateState to " + Boolean.toString(z));
            this.mBapBroadcastProfile.setBroadcastMode(z);
        }
    }

    /* access modifiers changed from: private */
    public void onStateChanged(boolean z) {
        Log.d(TAG, "onStateChanged " + Boolean.toString(z));
        this.mState = z;
        RestrictedSwitchPreference restrictedSwitchPreference = this.mPreference;
        if (restrictedSwitchPreference != null) {
            restrictedSwitchPreference.setChecked(z);
        }
        if (!this.mState && this.reset_pending) {
            this.reset_pending = false;
            updateState(true);
        }
    }

    public void onBluetoothStateChanged(int i) {
        Log.d(TAG, "onBluetoothStateChanged" + Integer.toString(i));
        switch (i) {
            case 10:
            case 11:
            case 13:
                Handler handler = this.mHandler;
                handler.sendMessage(handler.obtainMessage(i));
                return;
            case 12:
                Handler handler2 = this.mHandler;
                handler2.sendMessageDelayed(handler2.obtainMessage(i), 200);
                return;
            default:
                return;
        }
    }

    public void onBroadcastStateChanged(int i) {
        Log.d(TAG, "onBroadcastStateChanged" + Integer.toString(i));
        if (i == 10) {
            RestrictedSwitchPreference restrictedSwitchPreference = this.mPreference;
            if (restrictedSwitchPreference != null) {
                restrictedSwitchPreference.setEnabled(true);
            }
            onStateChanged(false);
        } else if (i == 12) {
            RestrictedSwitchPreference restrictedSwitchPreference2 = this.mPreference;
            if (restrictedSwitchPreference2 != null) {
                restrictedSwitchPreference2.setEnabled(true);
            }
            onStateChanged(true);
        }
    }

    public void onBroadcastKeyGenerated() {
        Log.d(TAG, "onBroadcastKeyGenerated");
        if (this.mState) {
            this.reset_pending = true;
            updateState(false);
        }
    }

    public String getPreferenceKey() {
        Log.d(TAG, "getPreferenceKey");
        return KEY_BROADCAST_ENABLE;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Log.d(TAG, "displayPreference");
        RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = restrictedSwitchPreference;
        if (this.isBluetoothLeBroadcastAudioSupported) {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            this.mBluetoothAdapter = defaultAdapter;
            onBluetoothStateChanged(defaultAdapter.getState());
            if (this.mBluetoothAdapter.getState() == 12 && this.mBapBroadcastProfile.isProfileReady()) {
                int broadcastStatus = this.mBapBroadcastProfile.getBroadcastStatus();
                Log.d(TAG, "get status done");
                if (broadcastStatus == 12 || broadcastStatus == 14) {
                    onStateChanged(true);
                } else {
                    onStateChanged(false);
                }
            }
        } else {
            restrictedSwitchPreference.setVisible(false);
        }
    }

    public boolean isChecked() {
        return this.mState;
    }

    public boolean setChecked(boolean z) {
        updateState(z);
        return true;
    }

    public int getAvailabilityStatus() {
        Log.d(TAG, "getAvailabilityStatus");
        return this.isBluetoothLeBroadcastAudioSupported ? 0 : 3;
    }

    public boolean hasAsyncUpdate() {
        Log.d(TAG, "hasAsyncUpdate");
        return true;
    }

    public boolean isPublicSlice() {
        Log.d(TAG, "isPublicSlice");
        return true;
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        this.mCallbacksRegistered = false;
        LocalBluetoothManager localBluetoothManager = this.mManager;
        if (localBluetoothManager != null) {
            localBluetoothManager.getEventManager().unregisterCallback(this);
        }
    }

    public int getSliceHighlightMenuRes() {
        Log.d(TAG, "getSliceHighlightMenuRes");
        return this.isBluetoothLeBroadcastAudioSupported ? 0 : 3;
    }
}
