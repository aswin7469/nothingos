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
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.RestrictedSwitchPreference;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.BroadcastProfile;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import java.util.UUID;
@Keep
/* loaded from: classes.dex */
public class BluetoothBroadcastEnableController extends TogglePreferenceController implements LifecycleObserver, OnResume, OnPause, OnDestroy, BluetoothCallback {
    public static final String BLUETOOTH_LE_AUDIO_MASK_PROP = "persist.vendor.service.bt.adv_audio_mask";
    public static final int BROADCAST_AUDIO_MASK = 4;
    public static final String KEY_BROADCAST_ENABLE = "bluetooth_screen_broadcast_enable";
    public static final String TAG = "BluetoothBroadcastEnableController";
    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    private RestrictedSwitchPreference mPreference = null;
    private boolean mState = false;
    private boolean reset_pending = false;
    private BroadcastProfile mBapBroadcastProfile = null;
    private boolean isBluetoothLeBroadcastAudioSupported = false;
    private boolean mCallbacksRegistered = false;
    private LocalBluetoothManager mManager = null;
    private final Handler mHandler = new Handler() { // from class: com.android.settings.bluetooth.BluetoothBroadcastEnableController.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Log.d(BluetoothBroadcastEnableController.TAG, "BT state, msg = " + Integer.toString(message.what));
            switch (message.what) {
                case 10:
                case 11:
                case 13:
                    BluetoothBroadcastEnableController.this.reset_pending = false;
                    BluetoothBroadcastEnableController.this.onStateChanged(false);
                    BluetoothBroadcastEnableController.this.mBapBroadcastProfile = null;
                    if (BluetoothBroadcastEnableController.this.mPreference == null) {
                        return;
                    }
                    BluetoothBroadcastEnableController.this.mPreference.setEnabled(false);
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

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
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

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BluetoothBroadcastEnableController(Context context, String str) {
        super(context, str);
        Log.d(TAG, "Constructor() with key");
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

    /* JADX INFO: Access modifiers changed from: private */
    public void onStateChanged(boolean z) {
        Log.d(TAG, "onStateChanged " + Boolean.toString(z));
        this.mState = z;
        RestrictedSwitchPreference restrictedSwitchPreference = this.mPreference;
        if (restrictedSwitchPreference != null) {
            restrictedSwitchPreference.setChecked(z);
        }
        if (this.mState || !this.reset_pending) {
            return;
        }
        this.reset_pending = false;
        updateState(true);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
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
                handler2.sendMessageDelayed(handler2.obtainMessage(i), 200L);
                return;
            default:
                return;
        }
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onBroadcastStateChanged(int i) {
        Log.d(TAG, "onBroadcastStateChanged" + Integer.toString(i));
        if (i == 10) {
            RestrictedSwitchPreference restrictedSwitchPreference = this.mPreference;
            if (restrictedSwitchPreference != null) {
                restrictedSwitchPreference.setEnabled(true);
            }
            onStateChanged(false);
        } else if (i != 12) {
        } else {
            RestrictedSwitchPreference restrictedSwitchPreference2 = this.mPreference;
            if (restrictedSwitchPreference2 != null) {
                restrictedSwitchPreference2.setEnabled(true);
            }
            onStateChanged(true);
        }
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onBroadcastKeyGenerated() {
        Log.d(TAG, "onBroadcastKeyGenerated");
        if (this.mState) {
            this.reset_pending = true;
            updateState(false);
        }
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        Log.d(TAG, "getPreferenceKey");
        return KEY_BROADCAST_ENABLE;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Log.d(TAG, "displayPreference");
        RestrictedSwitchPreference restrictedSwitchPreference = (RestrictedSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = restrictedSwitchPreference;
        if (this.isBluetoothLeBroadcastAudioSupported) {
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            this.mBluetoothAdapter = defaultAdapter;
            onBluetoothStateChanged(defaultAdapter.getState());
            if (this.mBluetoothAdapter.getState() != 12 || !this.mBapBroadcastProfile.isProfileReady()) {
                return;
            }
            int broadcastStatus = this.mBapBroadcastProfile.getBroadcastStatus();
            Log.d(TAG, "get status done");
            if (broadcastStatus == 12 || broadcastStatus == 14) {
                onStateChanged(true);
                return;
            } else {
                onStateChanged(false);
                return;
            }
        }
        restrictedSwitchPreference.setVisible(false);
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return this.mState;
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean setChecked(boolean z) {
        updateState(z);
        return true;
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        Log.d(TAG, "getAvailabilityStatus");
        return this.isBluetoothLeBroadcastAudioSupported ? 0 : 3;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public boolean hasAsyncUpdate() {
        Log.d(TAG, "hasAsyncUpdate");
        return true;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public boolean isPublicSlice() {
        Log.d(TAG, "isPublicSlice");
        return true;
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnResume
    public void onResume() {
        Log.d(TAG, "onResume");
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnPause
    public void onPause() {
        Log.d(TAG, "onPause");
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
