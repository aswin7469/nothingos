package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothLeBroadcastAssistant;
import android.bluetooth.BluetoothLeBroadcastMetadata;
import android.bluetooth.BluetoothLeBroadcastReceiveState;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanFilter;
import android.content.Context;
import android.util.Log;
import com.android.settingslib.C1757R;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class LocalBluetoothLeBroadcastAssistant implements LocalBluetoothProfile {
    private static final boolean DEBUG = true;
    static final String NAME = "LE_AUDIO_BROADCAST_ASSISTANT";
    private static final int ORDINAL = 1;
    private static final String TAG = "LocalBluetoothLeBroadcastAssistant";
    private static final int UNKNOWN_VALUE_PLACEHOLDER = -1;
    private BluetoothLeBroadcastMetadata mBluetoothLeBroadcastMetadata;
    private BluetoothLeBroadcastMetadata.Builder mBuilder = new BluetoothLeBroadcastMetadata.Builder();
    /* access modifiers changed from: private */
    public final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothLeBroadcastAssistant mService;
    private final BluetoothProfile.ServiceListener mServiceListener;

    public boolean accessProfileEnabled() {
        return false;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 0;
    }

    public int getOrdinal() {
        return 1;
    }

    public int getProfileId() {
        return 29;
    }

    public boolean isAutoConnectable() {
        return true;
    }

    public String toString() {
        return NAME;
    }

    public LocalBluetoothLeBroadcastAssistant(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        C18121 r0 = new BluetoothProfile.ServiceListener() {
            public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
                Log.d(LocalBluetoothLeBroadcastAssistant.TAG, "Bluetooth service connected");
                BluetoothLeBroadcastAssistant unused = LocalBluetoothLeBroadcastAssistant.this.mService = (BluetoothLeBroadcastAssistant) bluetoothProfile;
                List connectedDevices = LocalBluetoothLeBroadcastAssistant.this.mService.getConnectedDevices();
                while (!connectedDevices.isEmpty()) {
                    BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                    CachedBluetoothDevice findDevice = LocalBluetoothLeBroadcastAssistant.this.mDeviceManager.findDevice(bluetoothDevice);
                    if (findDevice == null) {
                        Log.d(LocalBluetoothLeBroadcastAssistant.TAG, "LocalBluetoothLeBroadcastAssistant found new device: " + bluetoothDevice);
                        findDevice = LocalBluetoothLeBroadcastAssistant.this.mDeviceManager.addDevice(bluetoothDevice);
                    }
                    findDevice.onProfileStateChanged(LocalBluetoothLeBroadcastAssistant.this, 2);
                    findDevice.refresh();
                }
                LocalBluetoothLeBroadcastAssistant.this.mProfileManager.callServiceConnectedListeners();
                boolean unused2 = LocalBluetoothLeBroadcastAssistant.this.mIsProfileReady = true;
            }

            public void onServiceDisconnected(int i) {
                if (i != 29) {
                    Log.d(LocalBluetoothLeBroadcastAssistant.TAG, "The profile is not LE_AUDIO_BROADCAST_ASSISTANT");
                    return;
                }
                Log.d(LocalBluetoothLeBroadcastAssistant.TAG, "Bluetooth service disconnected");
                LocalBluetoothLeBroadcastAssistant.this.mProfileManager.callServiceDisconnectedListeners();
                boolean unused = LocalBluetoothLeBroadcastAssistant.this.mIsProfileReady = false;
            }
        };
        this.mServiceListener = r0;
        this.mProfileManager = localBluetoothProfileManager;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, r0, 29);
    }

    public void addSource(BluetoothDevice bluetoothDevice, BluetoothLeBroadcastMetadata bluetoothLeBroadcastMetadata, boolean z) {
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null) {
            Log.d(TAG, "The BluetoothLeBroadcastAssistant is null");
        } else {
            bluetoothLeBroadcastAssistant.addSource(bluetoothDevice, bluetoothLeBroadcastMetadata, z);
        }
    }

    public void addSource(BluetoothDevice bluetoothDevice, int i, int i2, int i3, int i4, int i5, boolean z, byte[] bArr, BluetoothDevice bluetoothDevice2, boolean z2) {
        Log.d(TAG, "addSource()");
        buildMetadata(i, i2, i3, i4, i5, z, bArr, bluetoothDevice2);
        BluetoothDevice bluetoothDevice3 = bluetoothDevice;
        addSource(bluetoothDevice, this.mBluetoothLeBroadcastMetadata, z2);
    }

    private void buildMetadata(int i, int i2, int i3, int i4, int i5, boolean z, byte[] bArr, BluetoothDevice bluetoothDevice) {
        this.mBluetoothLeBroadcastMetadata = this.mBuilder.setSourceDevice(bluetoothDevice, i).setSourceAdvertisingSid(i3).setBroadcastId(i4).setPaSyncInterval(i5).setEncrypted(z).setBroadcastCode(bArr).setPresentationDelayMicros(i2).build();
    }

    public void removeSource(BluetoothDevice bluetoothDevice, int i) {
        Log.d(TAG, "removeSource()");
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null) {
            Log.d(TAG, "The BluetoothLeBroadcastAssistant is null");
        } else {
            bluetoothLeBroadcastAssistant.removeSource(bluetoothDevice, i);
        }
    }

    public void startSearchingForSources(List<ScanFilter> list) {
        Log.d(TAG, "startSearchingForSources()");
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null) {
            Log.d(TAG, "The BluetoothLeBroadcastAssistant is null");
        } else {
            bluetoothLeBroadcastAssistant.startSearchingForSources(list);
        }
    }

    public void stopSearchingForSources() {
        Log.d(TAG, "stopSearchingForSources()");
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null) {
            Log.d(TAG, "The BluetoothLeBroadcastAssistant is null");
        } else {
            bluetoothLeBroadcastAssistant.stopSearchingForSources();
        }
    }

    public boolean isSearchInProgress() {
        Log.d(TAG, "isSearchInProgress()");
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant != null) {
            return bluetoothLeBroadcastAssistant.isSearchInProgress();
        }
        Log.d(TAG, "The BluetoothLeBroadcastAssistant is null");
        return false;
    }

    public List<BluetoothLeBroadcastReceiveState> getAllSources(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "getAllSources()");
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant != null) {
            return bluetoothLeBroadcastAssistant.getAllSources(bluetoothDevice);
        }
        Log.d(TAG, "The BluetoothLeBroadcastAssistant is null");
        return new ArrayList();
    }

    public void registerServiceCallBack(Executor executor, BluetoothLeBroadcastAssistant.Callback callback) {
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null) {
            Log.d(TAG, "The BluetoothLeBroadcast is null.");
        } else {
            bluetoothLeBroadcastAssistant.registerCallback(executor, callback);
        }
    }

    public void unregisterServiceCallBack(BluetoothLeBroadcastAssistant.Callback callback) {
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null) {
            Log.d(TAG, "The BluetoothLeBroadcast is null.");
        } else {
            bluetoothLeBroadcastAssistant.unregisterCallback(callback);
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null) {
            return 0;
        }
        return bluetoothLeBroadcastAssistant.getConnectionState(bluetoothDevice);
    }

    public List<BluetoothDevice> getConnectedDevices() {
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null) {
            return new ArrayList(0);
        }
        return bluetoothLeBroadcastAssistant.getDevicesMatchingConnectionStates(new int[]{2, 1, 3});
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null || bluetoothDevice == null || bluetoothLeBroadcastAssistant.getConnectionPolicy(bluetoothDevice) <= 0) {
            return false;
        }
        return true;
    }

    public int getConnectionPolicy(BluetoothDevice bluetoothDevice) {
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null || bluetoothDevice == null) {
            return 0;
        }
        return bluetoothLeBroadcastAssistant.getConnectionPolicy(bluetoothDevice);
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null || bluetoothDevice == null) {
            return false;
        }
        if (!z) {
            return bluetoothLeBroadcastAssistant.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothLeBroadcastAssistant.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return C1757R.string.summary_empty;
    }

    public int getSummaryResourceForDevice(BluetoothDevice bluetoothDevice) {
        return BluetoothUtils.getConnectionStateSummary(getConnectionStatus(bluetoothDevice));
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d(TAG, "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(29, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w(TAG, "Error cleaning up LeAudio proxy", th);
            }
        }
    }
}
