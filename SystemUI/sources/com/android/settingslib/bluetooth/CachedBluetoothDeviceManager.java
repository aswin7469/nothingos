package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class CachedBluetoothDeviceManager {
    private static final boolean DEBUG = true;
    private static final String TAG = "CachedBluetoothDeviceManager";
    private final LocalBluetoothManager mBtManager;
    final List<CachedBluetoothDevice> mCachedDevices;
    private Context mContext;
    CsipDeviceManager mCsipDeviceManager;
    HearingAidDeviceManager mHearingAidDeviceManager;
    BluetoothDevice mOngoingSetMemberPair;

    public CachedBluetoothDeviceManager(Context context, LocalBluetoothManager localBluetoothManager) {
        ArrayList arrayList = new ArrayList();
        this.mCachedDevices = arrayList;
        this.mContext = context;
        this.mBtManager = localBluetoothManager;
        this.mHearingAidDeviceManager = new HearingAidDeviceManager(localBluetoothManager, arrayList);
        this.mCsipDeviceManager = new CsipDeviceManager(localBluetoothManager, arrayList);
    }

    public synchronized Collection<CachedBluetoothDevice> getCachedDevicesCopy() {
        return new ArrayList(this.mCachedDevices);
    }

    public static boolean onDeviceDisappeared(CachedBluetoothDevice cachedBluetoothDevice) {
        cachedBluetoothDevice.setJustDiscovered(false);
        if (cachedBluetoothDevice.getBondState() == 10) {
            return true;
        }
        return false;
    }

    public void onDeviceNameUpdated(BluetoothDevice bluetoothDevice) {
        CachedBluetoothDevice findDevice = findDevice(bluetoothDevice);
        if (findDevice != null) {
            findDevice.refreshName();
        }
    }

    public synchronized CachedBluetoothDevice findDevice(BluetoothDevice bluetoothDevice) {
        for (CachedBluetoothDevice next : this.mCachedDevices) {
            if (next.getDevice().equals(bluetoothDevice)) {
                return next;
            }
            Set<CachedBluetoothDevice> memberDevice = next.getMemberDevice();
            if (!memberDevice.isEmpty()) {
                for (CachedBluetoothDevice next2 : memberDevice) {
                    if (next2.getDevice().equals(bluetoothDevice)) {
                        return next2;
                    }
                }
            }
            CachedBluetoothDevice subDevice = next.getSubDevice();
            if (subDevice != null && subDevice.getDevice().equals(bluetoothDevice)) {
                return subDevice;
            }
        }
        return null;
    }

    public CachedBluetoothDevice addDevice(BluetoothDevice bluetoothDevice) {
        CachedBluetoothDevice findDevice;
        LocalBluetoothProfileManager profileManager = this.mBtManager.getProfileManager();
        synchronized (this) {
            findDevice = findDevice(bluetoothDevice);
            if (findDevice == null) {
                findDevice = new CachedBluetoothDevice(this.mContext, profileManager, bluetoothDevice);
                this.mCsipDeviceManager.initCsipDeviceIfNeeded(findDevice);
                this.mHearingAidDeviceManager.initHearingAidDeviceIfNeeded(findDevice);
                if (!this.mCsipDeviceManager.setMemberDeviceIfNeeded(findDevice) && !this.mHearingAidDeviceManager.setSubDeviceIfNeeded(findDevice)) {
                    this.mCachedDevices.add(findDevice);
                    this.mBtManager.getEventManager().dispatchDeviceAdded(findDevice);
                }
            }
        }
        return findDevice;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003a, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String getSubDeviceSummary(com.android.settingslib.bluetooth.CachedBluetoothDevice r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            java.util.Set r0 = r4.getMemberDevice()     // Catch:{ all -> 0x003c }
            boolean r1 = r0.isEmpty()     // Catch:{ all -> 0x003c }
            if (r1 != 0) goto L_0x0027
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x003c }
        L_0x000f:
            boolean r1 = r0.hasNext()     // Catch:{ all -> 0x003c }
            if (r1 == 0) goto L_0x0027
            java.lang.Object r1 = r0.next()     // Catch:{ all -> 0x003c }
            com.android.settingslib.bluetooth.CachedBluetoothDevice r1 = (com.android.settingslib.bluetooth.CachedBluetoothDevice) r1     // Catch:{ all -> 0x003c }
            boolean r2 = r1.isConnected()     // Catch:{ all -> 0x003c }
            if (r2 == 0) goto L_0x000f
            java.lang.String r4 = r1.getConnectionSummary()     // Catch:{ all -> 0x003c }
            monitor-exit(r3)
            return r4
        L_0x0027:
            com.android.settingslib.bluetooth.CachedBluetoothDevice r4 = r4.getSubDevice()     // Catch:{ all -> 0x003c }
            if (r4 == 0) goto L_0x0039
            boolean r0 = r4.isConnected()     // Catch:{ all -> 0x003c }
            if (r0 == 0) goto L_0x0039
            java.lang.String r4 = r4.getConnectionSummary()     // Catch:{ all -> 0x003c }
            monitor-exit(r3)
            return r4
        L_0x0039:
            monitor-exit(r3)
            r3 = 0
            return r3
        L_0x003c:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDeviceManager.getSubDeviceSummary(com.android.settingslib.bluetooth.CachedBluetoothDevice):java.lang.String");
    }

    public synchronized boolean isSubDevice(BluetoothDevice bluetoothDevice) {
        for (CachedBluetoothDevice next : this.mCachedDevices) {
            if (!next.getDevice().equals(bluetoothDevice)) {
                Set<CachedBluetoothDevice> memberDevice = next.getMemberDevice();
                if (!memberDevice.isEmpty()) {
                    for (CachedBluetoothDevice device : memberDevice) {
                        if (device.getDevice().equals(bluetoothDevice)) {
                            return true;
                        }
                    }
                    continue;
                } else {
                    CachedBluetoothDevice subDevice = next.getSubDevice();
                    if (subDevice != null && subDevice.getDevice().equals(bluetoothDevice)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public synchronized void updateHearingAidsDevices() {
        this.mHearingAidDeviceManager.updateHearingAidsDevices();
    }

    public synchronized void updateCsipDevices() {
        this.mCsipDeviceManager.updateCsipDevices();
    }

    public String getName(BluetoothDevice bluetoothDevice) {
        CachedBluetoothDevice findDevice = findDevice(bluetoothDevice);
        if (findDevice != null && findDevice.getName() != null) {
            return findDevice.getName();
        }
        String alias = bluetoothDevice.getAlias();
        if (alias != null) {
            return alias;
        }
        return bluetoothDevice.getAddress();
    }

    public synchronized void clearNonBondedDevices() {
        clearNonBondedSubDevices();
        this.mCachedDevices.removeIf(new CachedBluetoothDeviceManager$$ExternalSyntheticLambda0());
    }

    static /* synthetic */ boolean lambda$clearNonBondedDevices$0(CachedBluetoothDevice cachedBluetoothDevice) {
        return cachedBluetoothDevice.getBondState() == 10;
    }

    private void clearNonBondedSubDevices() {
        for (int size = this.mCachedDevices.size() - 1; size >= 0; size--) {
            CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevices.get(size);
            Set<CachedBluetoothDevice> memberDevice = cachedBluetoothDevice.getMemberDevice();
            if (!memberDevice.isEmpty()) {
                for (Object obj : memberDevice.toArray()) {
                    CachedBluetoothDevice cachedBluetoothDevice2 = (CachedBluetoothDevice) obj;
                    if (cachedBluetoothDevice2.getDevice().getBondState() == 10) {
                        cachedBluetoothDevice.removeMemberDevice(cachedBluetoothDevice2);
                    }
                }
                return;
            }
            CachedBluetoothDevice subDevice = cachedBluetoothDevice.getSubDevice();
            if (subDevice != null && subDevice.getDevice().getBondState() == 10) {
                cachedBluetoothDevice.setSubDevice((CachedBluetoothDevice) null);
            }
        }
    }

    public synchronized void clearAllDevices() {
        for (int size = this.mCachedDevices.size() - 1; size >= 0; size--) {
            CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevices.get(size);
            this.mCachedDevices.remove(size);
        }
    }

    public synchronized void onScanningStateChanged(boolean z) {
        if (z) {
            for (int size = this.mCachedDevices.size() - 1; size >= 0; size--) {
                CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevices.get(size);
                cachedBluetoothDevice.setJustDiscovered(false);
                Set<CachedBluetoothDevice> memberDevice = cachedBluetoothDevice.getMemberDevice();
                if (!memberDevice.isEmpty()) {
                    for (CachedBluetoothDevice justDiscovered : memberDevice) {
                        justDiscovered.setJustDiscovered(false);
                    }
                    return;
                }
                CachedBluetoothDevice subDevice = cachedBluetoothDevice.getSubDevice();
                if (subDevice != null) {
                    subDevice.setJustDiscovered(false);
                }
            }
        }
    }

    public synchronized void onBluetoothStateChanged(int i) {
        if (i == 13) {
            for (int size = this.mCachedDevices.size() - 1; size >= 0; size--) {
                CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevices.get(size);
                Set<CachedBluetoothDevice> memberDevice = cachedBluetoothDevice.getMemberDevice();
                if (!memberDevice.isEmpty()) {
                    for (CachedBluetoothDevice next : memberDevice) {
                        if (next.getBondState() != 12) {
                            cachedBluetoothDevice.removeMemberDevice(next);
                        }
                    }
                } else {
                    CachedBluetoothDevice subDevice = cachedBluetoothDevice.getSubDevice();
                    if (!(subDevice == null || subDevice.getBondState() == 12)) {
                        cachedBluetoothDevice.setSubDevice((CachedBluetoothDevice) null);
                    }
                }
                if (cachedBluetoothDevice.getBondState() != 12) {
                    cachedBluetoothDevice.setJustDiscovered(false);
                    this.mCachedDevices.remove(size);
                }
                cachedBluetoothDevice.mTwspBatteryState = -1;
                cachedBluetoothDevice.mTwspBatteryLevel = -1;
            }
        }
    }

    public synchronized boolean onProfileConnectionStateChangedIfProcessed(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        if (i2 == 21) {
            return this.mHearingAidDeviceManager.onProfileConnectionStateChangedIfProcessed(cachedBluetoothDevice, i);
        } else if (i2 != 25) {
            return false;
        } else {
            return this.mCsipDeviceManager.onProfileConnectionStateChangedIfProcessed(cachedBluetoothDevice, i);
        }
    }

    public synchronized void onDeviceUnpaired(CachedBluetoothDevice cachedBluetoothDevice) {
        CachedBluetoothDevice findMainDevice = this.mCsipDeviceManager.findMainDevice(cachedBluetoothDevice);
        Set<CachedBluetoothDevice> memberDevice = cachedBluetoothDevice.getMemberDevice();
        if (!memberDevice.isEmpty()) {
            for (CachedBluetoothDevice next : memberDevice) {
                next.unpair();
                cachedBluetoothDevice.removeMemberDevice(next);
            }
        } else if (findMainDevice != null) {
            findMainDevice.unpair();
        }
        CachedBluetoothDevice findMainDevice2 = this.mHearingAidDeviceManager.findMainDevice(cachedBluetoothDevice);
        CachedBluetoothDevice subDevice = cachedBluetoothDevice.getSubDevice();
        if (subDevice != null) {
            subDevice.unpair();
            cachedBluetoothDevice.setSubDevice((CachedBluetoothDevice) null);
        } else if (findMainDevice2 != null) {
            findMainDevice2.unpair();
            findMainDevice2.setSubDevice((CachedBluetoothDevice) null);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003a, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean shouldPairByCsip(android.bluetooth.BluetoothDevice r4, int r5) {
        /*
            r3 = this;
            java.lang.String r0 = "Bond "
            monitor-enter(r3)
            android.bluetooth.BluetoothDevice r1 = r3.mOngoingSetMemberPair     // Catch:{ all -> 0x003c }
            if (r1 != 0) goto L_0x0039
            int r1 = r4.getBondState()     // Catch:{ all -> 0x003c }
            r2 = 10
            if (r1 != r2) goto L_0x0039
            com.android.settingslib.bluetooth.CsipDeviceManager r1 = r3.mCsipDeviceManager     // Catch:{ all -> 0x003c }
            boolean r5 = r1.isExistedGroupId(r5)     // Catch:{ all -> 0x003c }
            if (r5 != 0) goto L_0x0018
            goto L_0x0039
        L_0x0018:
            java.lang.String r5 = "CachedBluetoothDeviceManager"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x003c }
            r1.<init>((java.lang.String) r0)     // Catch:{ all -> 0x003c }
            java.lang.String r0 = r4.getName()     // Catch:{ all -> 0x003c }
            java.lang.StringBuilder r0 = r1.append((java.lang.String) r0)     // Catch:{ all -> 0x003c }
            java.lang.String r1 = " by CSIP"
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r1)     // Catch:{ all -> 0x003c }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x003c }
            android.util.Log.d(r5, r0)     // Catch:{ all -> 0x003c }
            r3.mOngoingSetMemberPair = r4     // Catch:{ all -> 0x003c }
            monitor-exit(r3)
            r3 = 1
            return r3
        L_0x0039:
            monitor-exit(r3)
            r3 = 0
            return r3
        L_0x003c:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDeviceManager.shouldPairByCsip(android.bluetooth.BluetoothDevice, int):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003a, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003c, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean onBondStateChangedIfProcess(android.bluetooth.BluetoothDevice r4, int r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            android.bluetooth.BluetoothDevice r0 = r3.mOngoingSetMemberPair     // Catch:{ all -> 0x003e }
            if (r0 == 0) goto L_0x003b
            boolean r0 = r0.equals(r4)     // Catch:{ all -> 0x003e }
            if (r0 != 0) goto L_0x000c
            goto L_0x003b
        L_0x000c:
            r0 = 11
            r1 = 1
            if (r5 != r0) goto L_0x0013
            monitor-exit(r3)
            return r1
        L_0x0013:
            r0 = 0
            r3.mOngoingSetMemberPair = r0     // Catch:{ all -> 0x003e }
            r0 = 10
            if (r5 == r0) goto L_0x0039
            com.android.settingslib.bluetooth.CachedBluetoothDevice r5 = r3.findDevice(r4)     // Catch:{ all -> 0x003e }
            if (r5 != 0) goto L_0x0039
            com.android.settingslib.bluetooth.LocalBluetoothManager r5 = r3.mBtManager     // Catch:{ all -> 0x003e }
            com.android.settingslib.bluetooth.LocalBluetoothProfileManager r5 = r5.getProfileManager()     // Catch:{ all -> 0x003e }
            com.android.settingslib.bluetooth.CachedBluetoothDevice r0 = new com.android.settingslib.bluetooth.CachedBluetoothDevice     // Catch:{ all -> 0x003e }
            android.content.Context r2 = r3.mContext     // Catch:{ all -> 0x003e }
            r0.<init>(r2, r5, r4)     // Catch:{ all -> 0x003e }
            java.util.List<com.android.settingslib.bluetooth.CachedBluetoothDevice> r5 = r3.mCachedDevices     // Catch:{ all -> 0x003e }
            r5.add(r0)     // Catch:{ all -> 0x003e }
            com.android.settingslib.bluetooth.CachedBluetoothDevice r4 = r3.findDevice(r4)     // Catch:{ all -> 0x003e }
            r4.connect()     // Catch:{ all -> 0x003e }
        L_0x0039:
            monitor-exit(r3)
            return r1
        L_0x003b:
            monitor-exit(r3)
            r3 = 0
            return r3
        L_0x003e:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDeviceManager.onBondStateChangedIfProcess(android.bluetooth.BluetoothDevice, int):boolean");
    }

    public boolean isOngoingPairByCsip(BluetoothDevice bluetoothDevice) {
        BluetoothDevice bluetoothDevice2 = this.mOngoingSetMemberPair;
        return bluetoothDevice2 != null && bluetoothDevice2.equals(bluetoothDevice);
    }

    private void log(String str) {
        Log.d(TAG, str);
    }
}
