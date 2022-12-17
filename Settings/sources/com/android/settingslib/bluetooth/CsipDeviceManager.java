package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CsipDeviceManager {
    private final LocalBluetoothManager mBtManager;
    private final List<CachedBluetoothDevice> mCachedDevices;

    private static boolean isAtLeastT() {
        return true;
    }

    private boolean isValidGroupId(int i) {
        return i != -1;
    }

    CsipDeviceManager(LocalBluetoothManager localBluetoothManager, List<CachedBluetoothDevice> list) {
        this.mBtManager = localBluetoothManager;
        this.mCachedDevices = list;
    }

    /* access modifiers changed from: package-private */
    public void initCsipDeviceIfNeeded(CachedBluetoothDevice cachedBluetoothDevice) {
        int baseGroupId = getBaseGroupId(cachedBluetoothDevice.getDevice());
        if (isValidGroupId(baseGroupId)) {
            log("initCsipDeviceIfNeeded: " + cachedBluetoothDevice + " (group: " + baseGroupId + ")");
            cachedBluetoothDevice.setGroupId(baseGroupId);
        }
    }

    private int getBaseGroupId(BluetoothDevice bluetoothDevice) {
        Map<Integer, ParcelUuid> groupUuidMapByDevice;
        CsipSetCoordinatorProfile csipSetCoordinatorProfile = this.mBtManager.getProfileManager().getCsipSetCoordinatorProfile();
        if (csipSetCoordinatorProfile == null || (groupUuidMapByDevice = csipSetCoordinatorProfile.getGroupUuidMapByDevice(bluetoothDevice)) == null) {
            return -1;
        }
        Iterator<Map.Entry<Integer, ParcelUuid>> it = groupUuidMapByDevice.entrySet().iterator();
        if (it.hasNext()) {
            return ((Integer) it.next().getKey()).intValue();
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public boolean setMemberDeviceIfNeeded(CachedBluetoothDevice cachedBluetoothDevice) {
        int groupId = cachedBluetoothDevice.getGroupId();
        if (!isValidGroupId(groupId)) {
            return false;
        }
        CachedBluetoothDevice cachedDevice = getCachedDevice(groupId);
        log("setMemberDeviceIfNeeded, main: " + cachedDevice + ", member: " + cachedBluetoothDevice);
        if (cachedDevice == null) {
            return false;
        }
        cachedDevice.addMemberDevice(cachedBluetoothDevice);
        cachedBluetoothDevice.setName(cachedDevice.getName());
        return true;
    }

    private CachedBluetoothDevice getCachedDevice(int i) {
        for (int size = this.mCachedDevices.size() - 1; size >= 0; size--) {
            CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevices.get(size);
            if (cachedBluetoothDevice.getGroupId() == i) {
                return cachedBluetoothDevice;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void updateCsipDevices() {
        HashSet<Integer> hashSet = new HashSet<>();
        for (CachedBluetoothDevice next : this.mCachedDevices) {
            if (!isValidGroupId(next.getGroupId())) {
                int baseGroupId = getBaseGroupId(next.getDevice());
                if (isValidGroupId(baseGroupId)) {
                    next.setGroupId(baseGroupId);
                    hashSet.add(Integer.valueOf(baseGroupId));
                }
            }
        }
        for (Integer intValue : hashSet) {
            onGroupIdChanged(intValue.intValue());
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void onGroupIdChanged(int i) {
        if (!isValidGroupId(i)) {
            log("onGroupIdChanged: groupId is invalid");
            return;
        }
        log("onGroupIdChanged: mCachedDevices list =" + this.mCachedDevices.toString());
        LocalBluetoothProfileManager profileManager = this.mBtManager.getProfileManager();
        CachedBluetoothDeviceManager cachedDeviceManager = this.mBtManager.getCachedDeviceManager();
        LeAudioProfile leAudioProfile = profileManager.getLeAudioProfile();
        CachedBluetoothDevice cachedBluetoothDevice = null;
        BluetoothDevice connectedGroupLeadDevice = (leAudioProfile == null || !isAtLeastT()) ? null : leAudioProfile.getConnectedGroupLeadDevice(i);
        if (connectedGroupLeadDevice != null) {
            cachedBluetoothDevice = cachedDeviceManager.findDevice(connectedGroupLeadDevice);
        }
        if (cachedBluetoothDevice != null) {
            List<CachedBluetoothDevice> list = (List) this.mCachedDevices.stream().filter(new CsipDeviceManager$$ExternalSyntheticLambda0(cachedBluetoothDevice, i)).collect(Collectors.toList());
            if (list == null || list.isEmpty()) {
                log("onGroupIdChanged: There is no member device in list.");
                return;
            }
            log("onGroupIdChanged: removed from UI device =" + list + ", with groupId=" + i + " mainDevice= " + cachedBluetoothDevice);
            for (CachedBluetoothDevice cachedBluetoothDevice2 : list) {
                Set<CachedBluetoothDevice> memberDevice = cachedBluetoothDevice2.getMemberDevice();
                if (!memberDevice.isEmpty()) {
                    log("onGroupIdChanged: Transfer the member list into new main device.");
                    for (CachedBluetoothDevice next : memberDevice) {
                        if (!next.equals(cachedBluetoothDevice)) {
                            cachedBluetoothDevice.addMemberDevice(next);
                        }
                    }
                    memberDevice.clear();
                }
                cachedBluetoothDevice.addMemberDevice(cachedBluetoothDevice2);
                this.mCachedDevices.remove(cachedBluetoothDevice2);
                this.mBtManager.getEventManager().dispatchDeviceRemoved(cachedBluetoothDevice2);
            }
            if (!this.mCachedDevices.contains(cachedBluetoothDevice)) {
                this.mCachedDevices.add(cachedBluetoothDevice);
                this.mBtManager.getEventManager().dispatchDeviceAdded(cachedBluetoothDevice);
                return;
            }
            return;
        }
        log("onGroupIdChanged: There is no main device from the LE profile.");
        int i2 = -1;
        for (int size = this.mCachedDevices.size() - 1; size >= 0; size--) {
            CachedBluetoothDevice cachedBluetoothDevice3 = this.mCachedDevices.get(size);
            if (cachedBluetoothDevice3.getGroupId() == i) {
                if (i2 == -1) {
                    i2 = size;
                    cachedBluetoothDevice = cachedBluetoothDevice3;
                } else {
                    log("onGroupIdChanged: removed from UI device =" + cachedBluetoothDevice3 + ", with groupId=" + i + " firstMatchedIndex=" + i2);
                    cachedBluetoothDevice.addMemberDevice(cachedBluetoothDevice3);
                    this.mCachedDevices.remove(size);
                    this.mBtManager.getEventManager().dispatchDeviceRemoved(cachedBluetoothDevice3);
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$onGroupIdChanged$0(CachedBluetoothDevice cachedBluetoothDevice, int i, CachedBluetoothDevice cachedBluetoothDevice2) {
        return !cachedBluetoothDevice2.equals(cachedBluetoothDevice) && cachedBluetoothDevice2.getGroupId() == i;
    }

    /* access modifiers changed from: package-private */
    public boolean onProfileConnectionStateChangedIfProcessed(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        log("onProfileConnectionStateChangedIfProcessed: " + cachedBluetoothDevice + ", state: " + i);
        if (i == 0) {
            CachedBluetoothDevice findMainDevice = findMainDevice(cachedBluetoothDevice);
            if (findMainDevice != null) {
                findMainDevice.refresh();
                return true;
            }
            Set<CachedBluetoothDevice> memberDevice = cachedBluetoothDevice.getMemberDevice();
            if (memberDevice.isEmpty()) {
                return false;
            }
            for (CachedBluetoothDevice next : memberDevice) {
                if (next.isConnected()) {
                    this.mBtManager.getEventManager().dispatchDeviceRemoved(cachedBluetoothDevice);
                    cachedBluetoothDevice.switchMemberDeviceContent(next, cachedBluetoothDevice);
                    cachedBluetoothDevice.refresh();
                    this.mBtManager.getEventManager().dispatchDeviceAdded(cachedBluetoothDevice);
                    return true;
                }
            }
            return false;
        } else if (i != 2) {
            return false;
        } else {
            onGroupIdChanged(cachedBluetoothDevice.getGroupId());
            CachedBluetoothDevice findMainDevice2 = findMainDevice(cachedBluetoothDevice);
            if (findMainDevice2 == null) {
                return false;
            }
            if (findMainDevice2.isConnected()) {
                findMainDevice2.refresh();
                return true;
            }
            this.mBtManager.getEventManager().dispatchDeviceRemoved(findMainDevice2);
            findMainDevice2.switchMemberDeviceContent(findMainDevice2, cachedBluetoothDevice);
            findMainDevice2.refresh();
            this.mBtManager.getEventManager().dispatchDeviceAdded(findMainDevice2);
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public CachedBluetoothDevice findMainDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        List<CachedBluetoothDevice> list;
        if (!(cachedBluetoothDevice == null || (list = this.mCachedDevices) == null)) {
            for (CachedBluetoothDevice next : list) {
                if (isValidGroupId(next.getGroupId())) {
                    Set<CachedBluetoothDevice> memberDevice = next.getMemberDevice();
                    if (memberDevice.isEmpty()) {
                        continue;
                    } else {
                        for (CachedBluetoothDevice next2 : memberDevice) {
                            if (next2 != null && next2.equals(cachedBluetoothDevice)) {
                                return next;
                            }
                        }
                        continue;
                    }
                }
            }
        }
        return null;
    }

    public boolean isExistedGroupId(int i) {
        return getCachedDevice(i) != null;
    }

    private void log(String str) {
        Log.d("CsipDeviceManager", str);
    }
}
