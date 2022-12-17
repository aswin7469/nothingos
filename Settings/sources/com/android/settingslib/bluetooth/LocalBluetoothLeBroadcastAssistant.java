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
import com.android.settingslib.R$string;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class LocalBluetoothLeBroadcastAssistant implements LocalBluetoothProfile {
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

    public String toString() {
        return "LE_AUDIO_BROADCAST_ASSISTANT";
    }

    public LocalBluetoothLeBroadcastAssistant(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        C15541 r0 = new BluetoothProfile.ServiceListener() {
            public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
                Log.d("LocalBluetoothLeBroadcastAssistant", "Bluetooth service connected");
                LocalBluetoothLeBroadcastAssistant.this.mService = (BluetoothLeBroadcastAssistant) bluetoothProfile;
                List connectedDevices = LocalBluetoothLeBroadcastAssistant.this.mService.getConnectedDevices();
                while (!connectedDevices.isEmpty()) {
                    BluetoothDevice bluetoothDevice = (BluetoothDevice) connectedDevices.remove(0);
                    CachedBluetoothDevice findDevice = LocalBluetoothLeBroadcastAssistant.this.mDeviceManager.findDevice(bluetoothDevice);
                    if (findDevice == null) {
                        Log.d("LocalBluetoothLeBroadcastAssistant", "LocalBluetoothLeBroadcastAssistant found new device: " + bluetoothDevice);
                        findDevice = LocalBluetoothLeBroadcastAssistant.this.mDeviceManager.addDevice(bluetoothDevice);
                    }
                    findDevice.onProfileStateChanged(LocalBluetoothLeBroadcastAssistant.this, 2);
                    findDevice.refresh();
                }
                LocalBluetoothLeBroadcastAssistant.this.mProfileManager.callServiceConnectedListeners();
                LocalBluetoothLeBroadcastAssistant.this.mIsProfileReady = true;
            }

            public void onServiceDisconnected(int i) {
                if (i != 29) {
                    Log.d("LocalBluetoothLeBroadcastAssistant", "The profile is not LE_AUDIO_BROADCAST_ASSISTANT");
                    return;
                }
                Log.d("LocalBluetoothLeBroadcastAssistant", "Bluetooth service disconnected");
                LocalBluetoothLeBroadcastAssistant.this.mProfileManager.callServiceDisconnectedListeners();
                LocalBluetoothLeBroadcastAssistant.this.mIsProfileReady = false;
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
            Log.d("LocalBluetoothLeBroadcastAssistant", "The BluetoothLeBroadcastAssistant is null");
        } else {
            bluetoothLeBroadcastAssistant.addSource(bluetoothDevice, bluetoothLeBroadcastMetadata, z);
        }
    }

    public void removeSource(BluetoothDevice bluetoothDevice, int i) {
        Log.d("LocalBluetoothLeBroadcastAssistant", "removeSource()");
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null) {
            Log.d("LocalBluetoothLeBroadcastAssistant", "The BluetoothLeBroadcastAssistant is null");
        } else {
            bluetoothLeBroadcastAssistant.removeSource(bluetoothDevice, i);
        }
    }

    public void startSearchingForSources(List<ScanFilter> list) {
        Log.d("LocalBluetoothLeBroadcastAssistant", "startSearchingForSources()");
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null) {
            Log.d("LocalBluetoothLeBroadcastAssistant", "The BluetoothLeBroadcastAssistant is null");
        } else {
            bluetoothLeBroadcastAssistant.startSearchingForSources(list);
        }
    }

    public void stopSearchingForSources() {
        Log.d("LocalBluetoothLeBroadcastAssistant", "stopSearchingForSources()");
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null) {
            Log.d("LocalBluetoothLeBroadcastAssistant", "The BluetoothLeBroadcastAssistant is null");
        } else {
            bluetoothLeBroadcastAssistant.stopSearchingForSources();
        }
    }

    public boolean isSearchInProgress() {
        Log.d("LocalBluetoothLeBroadcastAssistant", "isSearchInProgress()");
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant != null) {
            return bluetoothLeBroadcastAssistant.isSearchInProgress();
        }
        Log.d("LocalBluetoothLeBroadcastAssistant", "The BluetoothLeBroadcastAssistant is null");
        return false;
    }

    public List<BluetoothLeBroadcastReceiveState> getAllSources(BluetoothDevice bluetoothDevice) {
        Log.d("LocalBluetoothLeBroadcastAssistant", "getAllSources()");
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant != null) {
            return bluetoothLeBroadcastAssistant.getAllSources(bluetoothDevice);
        }
        Log.d("LocalBluetoothLeBroadcastAssistant", "The BluetoothLeBroadcastAssistant is null");
        return new ArrayList();
    }

    public void registerServiceCallBack(Executor executor, BluetoothLeBroadcastAssistant.Callback callback) {
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null) {
            Log.d("LocalBluetoothLeBroadcastAssistant", "The BluetoothLeBroadcast is null.");
        } else {
            bluetoothLeBroadcastAssistant.registerCallback(executor, callback);
        }
    }

    public void unregisterServiceCallBack(BluetoothLeBroadcastAssistant.Callback callback) {
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null) {
            Log.d("LocalBluetoothLeBroadcastAssistant", "The BluetoothLeBroadcast is null.");
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

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothLeBroadcastAssistant bluetoothLeBroadcastAssistant = this.mService;
        if (bluetoothLeBroadcastAssistant == null || bluetoothDevice == null || bluetoothLeBroadcastAssistant.getConnectionPolicy(bluetoothDevice) <= 0) {
            return false;
        }
        return true;
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
        return R$string.summary_empty;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d("LocalBluetoothLeBroadcastAssistant", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(29, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("LocalBluetoothLeBroadcastAssistant", "Error cleaning up LeAudio proxy", th);
            }
        }
    }
}
