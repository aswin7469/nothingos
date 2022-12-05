package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import java.util.Set;
@Deprecated
/* loaded from: classes.dex */
public class LocalBluetoothAdapter {
    private static LocalBluetoothAdapter sInstance;
    private final BluetoothAdapter mAdapter;
    private LocalBluetoothProfileManager mProfileManager;
    private int mState = Integer.MIN_VALUE;

    private LocalBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.mAdapter = bluetoothAdapter;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setProfileManager(LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mProfileManager = localBluetoothProfileManager;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized LocalBluetoothAdapter getInstance() {
        LocalBluetoothAdapter localBluetoothAdapter;
        BluetoothAdapter defaultAdapter;
        synchronized (LocalBluetoothAdapter.class) {
            if (sInstance == null && (defaultAdapter = BluetoothAdapter.getDefaultAdapter()) != null) {
                sInstance = new LocalBluetoothAdapter(defaultAdapter);
            }
            localBluetoothAdapter = sInstance;
        }
        return localBluetoothAdapter;
    }

    public boolean enable() {
        return this.mAdapter.enable();
    }

    public Set<BluetoothDevice> getBondedDevices() {
        return this.mAdapter.getBondedDevices();
    }

    public BluetoothLeScanner getBluetoothLeScanner() {
        return this.mAdapter.getBluetoothLeScanner();
    }

    public int getState() {
        return this.mAdapter.getState();
    }

    public int getConnectionState() {
        return this.mAdapter.getConnectionState();
    }

    public synchronized int getBluetoothState() {
        syncBluetoothState();
        return this.mState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBluetoothStateInt(int i) {
        LocalBluetoothProfileManager localBluetoothProfileManager;
        synchronized (this) {
            if (this.mState == i) {
                return;
            }
            this.mState = i;
            if (i != 12 || (localBluetoothProfileManager = this.mProfileManager) == null) {
                return;
            }
            localBluetoothProfileManager.setBluetoothStateOn();
        }
    }

    boolean syncBluetoothState() {
        if (this.mAdapter.getState() != this.mState) {
            setBluetoothStateInt(this.mAdapter.getState());
            return true;
        }
        return false;
    }

    public boolean setBluetoothEnabled(boolean z) {
        boolean disable;
        if (z) {
            disable = this.mAdapter.enable();
        } else {
            disable = this.mAdapter.disable();
        }
        if (disable) {
            setBluetoothStateInt(z ? 11 : 13);
        } else {
            syncBluetoothState();
        }
        return disable;
    }
}
