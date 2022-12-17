package com.android.settings.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.DeviceGroup;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.R$string;
import com.android.settings.connecteddevice.ConnectedDeviceDashboardFragment;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.widget.GearPreference;
import com.android.settings.widget.GroupPreferenceCategory;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.CachedBluetoothDeviceManager;
import com.android.settingslib.bluetooth.DeviceGroupClientProfile;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfile;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class GroupUtils {

    /* renamed from: D */
    private static final boolean f177D = ConnectedDeviceDashboardFragment.DBG_GROUP;

    /* renamed from: V */
    private static final boolean f178V = Log.isLoggable("GroupUtilss", 2);
    private LocalBluetoothProfile mBCProfile = null;
    private CachedBluetoothDeviceManager mCacheDeviceNamanger;
    private Context mCtx;
    private DeviceGroup mDeviceGroup;
    private DeviceGroupClientProfile mGroupClientProfile;
    private boolean mIsGroupEnabled = false;
    private LocalBluetoothManager mLocalBluetoothManager;
    protected LocalBluetoothProfileManager mProfileManager;

    /* access modifiers changed from: package-private */
    public boolean isGroupDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        if (this.mIsGroupEnabled) {
            if (!cachedBluetoothDevice.isGroupDevice() && cachedBluetoothDevice.isTypeUnKnown()) {
                updateGroupStatus(cachedBluetoothDevice, cachedBluetoothDevice.getDevice().getDeviceType());
            }
            if (f177D) {
                Log.d("GroupUtilss", "isGroupDevice " + cachedBluetoothDevice.isGroupDevice() + cachedBluetoothDevice + " name " + cachedBluetoothDevice.getName() + " type " + cachedBluetoothDevice.getmType());
            }
            return cachedBluetoothDevice.isGroupDevice();
        } else if (!f177D) {
            return false;
        } else {
            loge(" GroupProfile not enabled");
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public String getGroupTitle(int i) {
        return " " + (i + 1);
    }

    /* access modifiers changed from: package-private */
    public int getGroupId(CachedBluetoothDevice cachedBluetoothDevice) {
        int qGroupId = cachedBluetoothDevice.getQGroupId();
        if (qGroupId == -1) {
            loge(" qgroupId is -1");
        }
        if (f177D) {
            Log.d("GroupUtilss", "qgroupId " + qGroupId + " device " + cachedBluetoothDevice);
        }
        return qGroupId;
    }

    private void updateGroupStatus(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        cachedBluetoothDevice.setDeviceType(i);
        CachedBluetoothDevice findDevice = this.mCacheDeviceNamanger.findDevice(cachedBluetoothDevice.getDevice());
        if (findDevice != null) {
            findDevice.setDeviceType(i);
            if (f177D) {
                Log.d("GroupUtilss", "updateGroupStatus updated " + cachedBluetoothDevice + " " + i);
                return;
            }
            return;
        }
        loge("updateGroupStatus failed  " + cachedBluetoothDevice + " groupId " + i);
    }

    public GroupUtils(Context context) {
        this.mCtx = context;
        this.mCacheDeviceNamanger = Utils.getLocalBtManager(context).getCachedDeviceManager();
        isGroupEnabled();
        LocalBluetoothManager localBtManager = Utils.getLocalBtManager(this.mCtx);
        this.mLocalBluetoothManager = localBtManager;
        if (localBtManager == null) {
            Log.e("GroupUtilss", "Bluetooth is not supported on this device");
            return;
        }
        LocalBluetoothProfileManager profileManager = localBtManager.getProfileManager();
        this.mProfileManager = profileManager;
        this.mBCProfile = profileManager.getBCProfile();
    }

    private int getGroupId(Preference preference) {
        CachedBluetoothDevice cachedBluetoothDevice;
        int i;
        if (preference instanceof BluetoothDevicePreference) {
            cachedBluetoothDevice = ((BluetoothDevicePreference) preference).getBluetoothDevice();
            i = getGroupId(cachedBluetoothDevice);
        } else {
            cachedBluetoothDevice = null;
            i = -1;
        }
        if (i == -1) {
            loge("group id not found " + cachedBluetoothDevice.getAddress());
        }
        return i;
    }

    private boolean isNewGroup(int i, ArrayList<GroupPreferenceCategory> arrayList) {
        boolean z = false;
        int i2 = 0;
        while (true) {
            if (i2 >= arrayList.size() - 1) {
                z = true;
                break;
            }
            GroupPreferenceCategory groupPreferenceCategory = arrayList.get(i2);
            if (groupPreferenceCategory == null) {
                loge("isNewGroup  tempGroup null");
                return false;
            }
            int groupId = groupPreferenceCategory.getGroupId();
            if (f177D) {
                Log.d("GroupUtilss", "isNewGroup val " + groupId + " key " + groupPreferenceCategory.getKey());
            }
            if (i == groupId) {
                break;
            }
            i2++;
        }
        if (f177D) {
            Log.d("GroupUtilss", "isNewGroup id  " + i + "  val " + z);
        }
        return z;
    }

    private boolean isAllFilled(int i, ArrayList<GroupPreferenceCategory> arrayList) {
        int i2 = 0;
        while (i2 < arrayList.size() - 1) {
            GroupPreferenceCategory groupPreferenceCategory = arrayList.get(i2);
            if (groupPreferenceCategory == null) {
                loge("isAllFilled");
                return false;
            } else if (i == groupPreferenceCategory.getGroupId()) {
                return false;
            } else {
                i2++;
            }
        }
        return true;
    }

    private GroupPreferenceCategory getParentGroup(ArrayList<GroupPreferenceCategory> arrayList, Preference preference) {
        for (int i = 0; i < arrayList.size() - 1; i++) {
            GroupPreferenceCategory groupPreferenceCategory = arrayList.get(i);
            if (groupPreferenceCategory.getPreferenceCount() == 0) {
                return groupPreferenceCategory;
            }
        }
        return null;
    }

    private GroupPreferenceCategory getExistingGroup(ArrayList<GroupPreferenceCategory> arrayList, Preference preference) {
        int groupId = getGroupId(preference);
        Iterator<GroupPreferenceCategory> it = arrayList.iterator();
        while (it.hasNext()) {
            GroupPreferenceCategory next = it.next();
            if (groupId == next.getGroupId()) {
                return next;
            }
        }
        return null;
    }

    private GroupBluetoothSettingsPreference getHedaer(int i, GearPreference.OnGearClickListener onGearClickListener) {
        GroupBluetoothSettingsPreference groupBluetoothSettingsPreference = new GroupBluetoothSettingsPreference(this.mCtx, i);
        groupBluetoothSettingsPreference.setOnGearClickListener(onGearClickListener);
        return groupBluetoothSettingsPreference;
    }

    public void addPreference(ArrayList<GroupPreferenceCategory> arrayList, Preference preference, GearPreference.OnGearClickListener onGearClickListener) {
        GroupPreferenceCategory groupPreferenceCategory;
        int groupId = getGroupId(preference);
        if (groupId == -1) {
            loge("addPreference groupId is not valid " + groupId);
            return;
        }
        boolean isNewGroup = isNewGroup(groupId, arrayList);
        boolean z = f177D;
        if (z) {
            Log.d("GroupUtilss", "addPreference  " + preference + " isNewGroup " + isNewGroup);
        }
        if (isNewGroup) {
            GroupBluetoothSettingsPreference hedaer = getHedaer(groupId, onGearClickListener);
            groupPreferenceCategory = getParentGroup(arrayList, preference);
            if (groupPreferenceCategory == null) {
                loge("getParentGroup not found for groupId " + groupId);
                isAllGroupsFilled(arrayList, hedaer);
                return;
            }
            groupPreferenceCategory.setGroupId(groupId);
            groupPreferenceCategory.addPreference(hedaer);
            groupPreferenceCategory.addPreference(preference);
            groupPreferenceCategory.setVisible(true);
        } else {
            groupPreferenceCategory = getExistingGroup(arrayList, preference);
            if (groupPreferenceCategory == null) {
                loge("getExistingGroup not found for groupId " + groupId);
                return;
            }
            groupPreferenceCategory.addPreference(preference);
        }
        if (z) {
            Log.d("GroupUtilss", "addPreference  key " + groupPreferenceCategory.getKey());
        }
    }

    public void removePreference(ArrayList<GroupPreferenceCategory> arrayList, Preference preference) {
        GroupPreferenceCategory existingGroup = getExistingGroup(arrayList, preference);
        if (existingGroup == null) {
            loge("removePreference group null ");
            removePreference(arrayList.get(arrayList.size() - 1), preference);
            return;
        }
        existingGroup.removePreference(preference);
        if (existingGroup.getPreferenceCount() == 1) {
            existingGroup.setGroupId(-1);
            existingGroup.removeAll();
            existingGroup.setVisible(false);
        }
    }

    private void removePreference(GroupPreferenceCategory groupPreferenceCategory, Preference preference) {
        int preferenceCount = groupPreferenceCategory.getPreferenceCount();
        if (preferenceCount == 0) {
            loge("removePreference Header invalid");
            return;
        }
        int groupId = getGroupId(preference);
        if (groupId == -1) {
            loge("removePreference Header groupId is invalid");
            return;
        }
        for (int i = 0; i < preferenceCount; i++) {
            GroupBluetoothSettingsPreference groupBluetoothSettingsPreference = (GroupBluetoothSettingsPreference) groupPreferenceCategory.getPreference(i);
            boolean z = f177D;
            if (z) {
                Log.d("GroupUtilss", "removePreference Header headerPreference " + groupBluetoothSettingsPreference + " header id " + groupBluetoothSettingsPreference.getGroupId() + " groupId " + groupId);
            }
            if (groupBluetoothSettingsPreference.getGroupId() == groupId) {
                int decrementChildCount = groupBluetoothSettingsPreference.decrementChildCount();
                if (z) {
                    Log.d("GroupUtilss", "removePreference Header group id  chCount " + decrementChildCount);
                }
                if (decrementChildCount <= 0) {
                    groupPreferenceCategory.removePreference(groupBluetoothSettingsPreference);
                }
            }
        }
    }

    private void isAllGroupsFilled(ArrayList<GroupPreferenceCategory> arrayList, GroupBluetoothSettingsPreference groupBluetoothSettingsPreference) {
        boolean isAllFilled = isAllFilled(-1, arrayList);
        boolean z = true;
        GroupPreferenceCategory groupPreferenceCategory = arrayList.get(arrayList.size() - 1);
        if (!isAllFilled) {
            return;
        }
        if (groupPreferenceCategory == null) {
            loge("isAllGroupsFilled received invalid group");
        } else if (!groupPreferenceCategory.getKey().contains("remaining")) {
            loge("isAllGroupsFilled not last group");
        } else {
            int preferenceCount = groupPreferenceCategory.getPreferenceCount();
            if (f177D) {
                Log.d("GroupUtilss", "isAllGroupsFilled size " + preferenceCount);
            }
            int i = 0;
            while (true) {
                if (i >= preferenceCount) {
                    z = false;
                    break;
                }
                if (groupPreferenceCategory.getPreference(i) instanceof GroupBluetoothSettingsPreference) {
                    GroupBluetoothSettingsPreference groupBluetoothSettingsPreference2 = (GroupBluetoothSettingsPreference) groupPreferenceCategory.getPreference(i);
                    if (groupBluetoothSettingsPreference.getGroupId() == groupBluetoothSettingsPreference2.getGroupId()) {
                        int incrementChildCound = groupBluetoothSettingsPreference2.incrementChildCound();
                        if (f177D) {
                            Log.d("GroupUtilss", "isAllGroupsFilled updated chCount " + incrementChildCound);
                        }
                    }
                }
                i++;
            }
            if (!z) {
                int incrementChildCound2 = groupBluetoothSettingsPreference.incrementChildCound();
                groupPreferenceCategory.addPreference(groupBluetoothSettingsPreference);
                if (f177D) {
                    Log.d("GroupUtilss", "isAllGroupsFilled added chCount " + incrementChildCound2);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public ArrayList<CachedBluetoothDevice> getCahcedDevice(int i) {
        Collection<CachedBluetoothDevice> cachedDevicesCopy = this.mCacheDeviceNamanger.getCachedDevicesCopy();
        ArrayList<CachedBluetoothDevice> arrayList = new ArrayList<>();
        for (CachedBluetoothDevice next : cachedDevicesCopy) {
            if (next != null && isGroupDeviceBonded(next) && getGroupId(next) == i) {
                arrayList.add(next);
            }
        }
        if (f177D) {
            Log.d("GroupUtilss", "getCahcedDevice " + i + " list " + arrayList + " " + arrayList.size());
        }
        return arrayList;
    }

    public BluetoothDevice getAnyBCConnectedDevice(int i) {
        DeviceGroup group = this.mGroupClientProfile.getGroup(i);
        this.mDeviceGroup = group;
        if (group == null) {
            Log.e("GroupUtilss", "getAnyBCConnectedDevice: dGrp is null");
            return null;
        } else if (this.mBCProfile == null) {
            Log.e("GroupUtilss", "getAnyBCConnectedDevice: BCProfile is null");
            return null;
        } else {
            for (BluetoothDevice bluetoothDevice : group.getDeviceGroupMembers()) {
                if (this.mBCProfile.getConnectionStatus(bluetoothDevice) == 2) {
                    return bluetoothDevice;
                }
            }
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public void launchAddSourceGroup(int i) {
        Class<?> cls;
        try {
            cls = Class.forName("com.android.settings.bluetooth.BluetoothSADetail");
        } catch (ClassNotFoundException unused) {
            Log.e("GroupUtilss", "no SADetail exists");
            cls = null;
        }
        if (cls != null) {
            BluetoothDevice anyBCConnectedDevice = getAnyBCConnectedDevice(i);
            Bundle bundle = new Bundle();
            if (anyBCConnectedDevice != null) {
                bundle.putString("device_address", anyBCConnectedDevice.getAddress());
                bundle.putShort("group_op", 1);
                new SubSettingLauncher(this.mCtx).setDestination("com.android.settings.bluetooth.BluetoothSADetail").setArguments(bundle).setTitleRes(R$string.bluetooth_search_broadcasters).setSourceMetricsCategory(25).launch();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean connectGroup(int i) {
        if (isValid()) {
            return this.mGroupClientProfile.connectGroup(i);
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean disconnectGroup(int i) {
        if (isValid()) {
            return this.mGroupClientProfile.disconnectGroup(i);
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean forgetGroup(int i) {
        if (isValid()) {
            return this.mGroupClientProfile.forgetGroup(i);
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isGroupDiscoveryInProgress(int i) {
        if (!isValid()) {
            return false;
        }
        return this.mGroupClientProfile.isGroupDiscoveryInProgress(i);
    }

    /* access modifiers changed from: package-private */
    public boolean startGroupDiscovery(int i) {
        if (!isValid()) {
            return false;
        }
        boolean isGroupDiscoveryInProgress = this.mGroupClientProfile.isGroupDiscoveryInProgress(i);
        if (f177D) {
            Log.d("GroupUtilss", "startGroupDiscovery " + i + "isDiscovering " + isGroupDiscoveryInProgress);
        }
        if (isGroupDiscoveryInProgress) {
            return false;
        }
        this.mGroupClientProfile.startGroupDiscovery(i);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean stopGroupDiscovery(int i) {
        if (!isValid()) {
            return false;
        }
        boolean isGroupDiscoveryInProgress = this.mGroupClientProfile.isGroupDiscoveryInProgress(i);
        if (f177D) {
            Log.d("GroupUtilss", "stopGroupDiscovery " + i + "isDiscovering " + isGroupDiscoveryInProgress);
        }
        if (!isGroupDiscoveryInProgress) {
            return false;
        }
        this.mGroupClientProfile.stopGroupDiscovery(i);
        return true;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x001a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getGroupSize(int r2) {
        /*
            r1 = this;
            boolean r0 = r1.isValid()
            if (r0 == 0) goto L_0x0015
            com.android.settingslib.bluetooth.DeviceGroupClientProfile r0 = r1.mGroupClientProfile
            android.bluetooth.DeviceGroup r2 = r0.getGroup(r2)
            r1.mDeviceGroup = r2
            if (r2 == 0) goto L_0x0015
            int r1 = r2.getDeviceGroupSize()
            goto L_0x0016
        L_0x0015:
            r1 = 0
        L_0x0016:
            boolean r2 = f177D
            if (r2 == 0) goto L_0x0030
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r0 = "getDeviceGroupSize size "
            r2.append(r0)
            r2.append(r1)
            java.lang.String r2 = r2.toString()
            java.lang.String r0 = "GroupUtilss"
            android.util.Log.d(r0, r2)
        L_0x0030:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bluetooth.GroupUtils.getGroupSize(int):int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0035  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isHidePCGGroups() {
        /*
            r3 = this;
            android.bluetooth.BluetoothAdapter r0 = android.bluetooth.BluetoothAdapter.getDefaultAdapter()
            java.util.List r0 = r0.getMostRecentlyConnectedDevices()
            if (r0 == 0) goto L_0x0030
            int r1 = r0.size()
            if (r1 <= 0) goto L_0x0030
            java.util.Iterator r0 = r0.iterator()
        L_0x0014:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0030
            java.lang.Object r1 = r0.next()
            android.bluetooth.BluetoothDevice r1 = (android.bluetooth.BluetoothDevice) r1
            com.android.settingslib.bluetooth.CachedBluetoothDeviceManager r2 = r3.mCacheDeviceNamanger
            com.android.settingslib.bluetooth.CachedBluetoothDevice r1 = r2.findDevice(r1)
            if (r1 == 0) goto L_0x0014
            boolean r1 = r3.isGroupDeviceBondedOnly(r1)
            if (r1 == 0) goto L_0x0014
            r3 = 0
            goto L_0x0031
        L_0x0030:
            r3 = 1
        L_0x0031:
            boolean r0 = f177D
            if (r0 == 0) goto L_0x004b
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "isHidePCGGroups "
            r0.append(r1)
            r0.append(r3)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "GroupUtilss"
            android.util.Log.d(r1, r0)
        L_0x004b:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bluetooth.GroupUtils.isHidePCGGroups():boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0033  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isHideGroupOptions(int r4) {
        /*
            r3 = this;
            com.android.settingslib.bluetooth.CachedBluetoothDeviceManager r0 = r3.mCacheDeviceNamanger
            java.util.Collection r0 = r0.getCachedDevicesCopy()
            if (r0 == 0) goto L_0x002e
            int r1 = r0.size()
            if (r1 <= 0) goto L_0x002e
            java.util.Iterator r0 = r0.iterator()
        L_0x0012:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x002e
            java.lang.Object r1 = r0.next()
            com.android.settingslib.bluetooth.CachedBluetoothDevice r1 = (com.android.settingslib.bluetooth.CachedBluetoothDevice) r1
            if (r1 == 0) goto L_0x0012
            boolean r2 = r3.isGroupDeviceBonded(r1)
            if (r2 == 0) goto L_0x0012
            boolean r1 = r3.isGroupIdMatch(r1, r4)
            if (r1 == 0) goto L_0x0012
            r3 = 0
            goto L_0x002f
        L_0x002e:
            r3 = 1
        L_0x002f:
            boolean r4 = f177D
            if (r4 == 0) goto L_0x0049
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r0 = "isHideGroupOptions "
            r4.append(r0)
            r4.append(r3)
            java.lang.String r4 = r4.toString()
            java.lang.String r0 = "GroupUtilss"
            android.util.Log.d(r0, r4)
        L_0x0049:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bluetooth.GroupUtils.isHideGroupOptions(int):boolean");
    }

    private boolean isGroupDeviceBondedOnly(CachedBluetoothDevice cachedBluetoothDevice) {
        return cachedBluetoothDevice.getBondState() == 12 && !cachedBluetoothDevice.isConnected() && isGroupDevice(cachedBluetoothDevice);
    }

    private boolean isGroupIdMatch(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        return i == getGroupId(cachedBluetoothDevice);
    }

    private boolean isGroupDeviceBonded(CachedBluetoothDevice cachedBluetoothDevice) {
        return cachedBluetoothDevice.getBondState() == 12 && isGroupDevice(cachedBluetoothDevice);
    }

    private void loge(String str) {
        Log.e("GroupUtilss", str);
    }

    private boolean isValid() {
        if (this.mGroupClientProfile == null) {
            this.mGroupClientProfile = Utils.getLocalBtManager(this.mCtx).getProfileManager().getDeviceGroupClientProfile();
        }
        return this.mGroupClientProfile != null;
    }

    private void isGroupEnabled() {
        try {
            int i = SystemProperties.getInt("persist.vendor.service.bt.adv_audio_mask", 0);
            if (f177D) {
                Log.d("GroupUtilss", "isGroupEnabled advAudioFeatureMask " + i);
            }
            if (i != 0) {
                this.mIsGroupEnabled = true;
            }
        } catch (Exception e) {
            this.mIsGroupEnabled = false;
            Log.e("GroupUtilss", "isGroupEnabled " + e);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isUpdate(int i, CachedBluetoothDevice cachedBluetoothDevice) {
        return isGroupDevice(cachedBluetoothDevice) && i == getGroupId(cachedBluetoothDevice);
    }

    /* access modifiers changed from: package-private */
    public boolean addDevice(ArrayList<CachedBluetoothDevice> arrayList, int i, CachedBluetoothDevice cachedBluetoothDevice) {
        boolean isUpdate = isUpdate(i, cachedBluetoothDevice);
        boolean z = false;
        boolean z2 = true;
        if (isUpdate) {
            Iterator<CachedBluetoothDevice> it = arrayList.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next().getAddress().equals(cachedBluetoothDevice.getAddress())) {
                        z2 = false;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (z2) {
                z = arrayList.add(cachedBluetoothDevice);
            }
        }
        if (f177D) {
            Log.d("GroupUtilss", "addDevice cachedDevice " + cachedBluetoothDevice + " name " + cachedBluetoothDevice.getName() + " is added " + z);
        }
        return z2;
    }

    /* access modifiers changed from: package-private */
    public boolean removeDevice(ArrayList<CachedBluetoothDevice> arrayList, int i, CachedBluetoothDevice cachedBluetoothDevice) {
        boolean z;
        CachedBluetoothDevice cachedBluetoothDevice2;
        boolean isUpdate = isUpdate(i, cachedBluetoothDevice);
        boolean z2 = false;
        if (isUpdate) {
            Iterator<CachedBluetoothDevice> it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    cachedBluetoothDevice2 = null;
                    z = false;
                    break;
                }
                cachedBluetoothDevice2 = it.next();
                if (cachedBluetoothDevice2.getAddress().equals(cachedBluetoothDevice.getAddress())) {
                    z = true;
                    break;
                }
            }
            if (z) {
                z2 = arrayList.remove(cachedBluetoothDevice2);
            }
        } else {
            z = false;
        }
        if (f177D) {
            Log.d("GroupUtilss", "removeDevice cachedDevice " + cachedBluetoothDevice + " name " + cachedBluetoothDevice.getName() + " isremoved " + z2);
        }
        return z;
    }
}
