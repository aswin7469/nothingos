package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.os.ParcelUuid;
import java.time.Duration;
import java.util.List;
import java.util.Set;

@Deprecated
public class LocalBluetoothAdapter {
    private static final int SCAN_EXPIRATION_MS = 300000;
    private static final String TAG = "LocalBluetoothAdapter";
    private static LocalBluetoothAdapter sInstance;
    private final BluetoothAdapter mAdapter;
    private long mLastScan;
    private LocalBluetoothProfileManager mProfileManager;
    private int mState = Integer.MIN_VALUE;

    private LocalBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
        this.mAdapter = bluetoothAdapter;
    }

    /* access modifiers changed from: package-private */
    public void setProfileManager(LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mProfileManager = localBluetoothProfileManager;
    }

    static synchronized LocalBluetoothAdapter getInstance() {
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

    public void cancelDiscovery() {
        this.mAdapter.cancelDiscovery();
    }

    public boolean enable() {
        return this.mAdapter.enable();
    }

    public boolean disable() {
        return this.mAdapter.disable();
    }

    public String getAddress() {
        return this.mAdapter.getAddress();
    }

    /* access modifiers changed from: package-private */
    public void getProfileProxy(Context context, BluetoothProfile.ServiceListener serviceListener, int i) {
        this.mAdapter.getProfileProxy(context, serviceListener, i);
    }

    public Set<BluetoothDevice> getBondedDevices() {
        return this.mAdapter.getBondedDevices();
    }

    public String getName() {
        return this.mAdapter.getName();
    }

    public int getScanMode() {
        return this.mAdapter.getScanMode();
    }

    public BluetoothLeScanner getBluetoothLeScanner() {
        return this.mAdapter.getBluetoothLeScanner();
    }

    public int getState() {
        return this.mAdapter.getState();
    }

    public ParcelUuid[] getUuids() {
        List uuidsList = this.mAdapter.getUuidsList();
        ParcelUuid[] parcelUuidArr = new ParcelUuid[uuidsList.size()];
        uuidsList.toArray(parcelUuidArr);
        return parcelUuidArr;
    }

    public boolean isDiscovering() {
        return this.mAdapter.isDiscovering();
    }

    public boolean isEnabled() {
        return this.mAdapter.isEnabled();
    }

    public int getConnectionState() {
        return this.mAdapter.getConnectionState();
    }

    public void setDiscoverableTimeout(int i) {
        this.mAdapter.setDiscoverableTimeout(Duration.ofSeconds((long) i));
    }

    public long getDiscoveryEndMillis() {
        return this.mAdapter.getDiscoveryEndMillis();
    }

    public void setName(String str) {
        this.mAdapter.setName(str);
    }

    public void setScanMode(int i) {
        this.mAdapter.setScanMode(i);
    }

    public boolean setScanMode(int i, int i2) {
        return this.mAdapter.setDiscoverableTimeout(Duration.ofSeconds((long) i2)) == 0 && this.mAdapter.setScanMode(i) == 0;
    }

    public void startScanning(boolean z) {
        if (!this.mAdapter.isDiscovering()) {
            if (!z) {
                if (this.mLastScan + 300000 <= System.currentTimeMillis()) {
                    A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
                    if (a2dpProfile == null || !a2dpProfile.isA2dpPlaying()) {
                        A2dpSinkProfile a2dpSinkProfile = this.mProfileManager.getA2dpSinkProfile();
                        if (a2dpSinkProfile != null && a2dpSinkProfile.isAudioPlaying()) {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            if (this.mAdapter.startDiscovery()) {
                this.mLastScan = System.currentTimeMillis();
            }
        }
    }

    public void stopScanning() {
        if (this.mAdapter.isDiscovering()) {
            this.mAdapter.cancelDiscovery();
        }
    }

    public synchronized int getBluetoothState() {
        syncBluetoothState();
        return this.mState;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x000e, code lost:
        r1 = r1.mProfileManager;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0010, code lost:
        if (r1 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0012, code lost:
        r1.setBluetoothStateOn();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x000c, code lost:
        if (r2 != 12) goto L_?;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setBluetoothStateInt(int r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            int r0 = r1.mState     // Catch:{ all -> 0x0016 }
            if (r0 != r2) goto L_0x0007
            monitor-exit(r1)     // Catch:{ all -> 0x0016 }
            return
        L_0x0007:
            r1.mState = r2     // Catch:{ all -> 0x0016 }
            monitor-exit(r1)     // Catch:{ all -> 0x0016 }
            r0 = 12
            if (r2 != r0) goto L_0x0015
            com.android.settingslib.bluetooth.LocalBluetoothProfileManager r1 = r1.mProfileManager
            if (r1 == 0) goto L_0x0015
            r1.setBluetoothStateOn()
        L_0x0015:
            return
        L_0x0016:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0016 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.LocalBluetoothAdapter.setBluetoothStateInt(int):void");
    }

    /* access modifiers changed from: package-private */
    public boolean syncBluetoothState() {
        if (this.mAdapter.getState() == this.mState) {
            return false;
        }
        setBluetoothStateInt(this.mAdapter.getState());
        return true;
    }

    public boolean setBluetoothEnabled(boolean z) {
        boolean z2;
        if (z) {
            z2 = this.mAdapter.enable();
        } else {
            z2 = this.mAdapter.disable();
        }
        if (z2) {
            setBluetoothStateInt(z ? 11 : 13);
        } else {
            syncBluetoothState();
        }
        return z2;
    }

    public BluetoothDevice getRemoteDevice(String str) {
        return this.mAdapter.getRemoteDevice(str);
    }

    public List<Integer> getSupportedProfiles() {
        return this.mAdapter.getSupportedProfiles();
    }
}
