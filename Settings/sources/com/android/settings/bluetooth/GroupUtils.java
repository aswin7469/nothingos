package com.android.settings.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.DeviceGroup;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.R;
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
import java.util.List;
/* loaded from: classes.dex */
public class GroupUtils {
    private static final boolean D = ConnectedDeviceDashboardFragment.DBG_GROUP;
    private static final boolean V = Log.isLoggable("GroupUtilss", 2);
    private LocalBluetoothProfile mBCProfile;
    private CachedBluetoothDeviceManager mCacheDeviceNamanger;
    private Context mCtx;
    private DeviceGroup mDeviceGroup;
    private DeviceGroupClientProfile mGroupClientProfile;
    private boolean mIsGroupEnabled = false;
    private LocalBluetoothManager mLocalBluetoothManager;
    protected LocalBluetoothProfileManager mProfileManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isGroupDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        if (!this.mIsGroupEnabled) {
            if (!D) {
                return false;
            }
            loge(" GroupProfile not enabled");
            return false;
        }
        if (!cachedBluetoothDevice.isGroupDevice() && cachedBluetoothDevice.isTypeUnKnown()) {
            updateGroupStatus(cachedBluetoothDevice, cachedBluetoothDevice.getDevice().getDeviceType());
        }
        if (D) {
            Log.d("GroupUtilss", "isGroupDevice " + cachedBluetoothDevice.isGroupDevice() + cachedBluetoothDevice + " name " + cachedBluetoothDevice.getName() + " type " + cachedBluetoothDevice.getmType());
        }
        return cachedBluetoothDevice.isGroupDevice();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getGroupTitle(int i) {
        return " " + (i + 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getGroupId(CachedBluetoothDevice cachedBluetoothDevice) {
        int groupId = cachedBluetoothDevice.getGroupId();
        if (groupId == -1) {
            loge(" groupId is -1");
        }
        if (D) {
            Log.d("GroupUtilss", "getgroupId " + groupId + " device " + cachedBluetoothDevice);
        }
        return groupId;
    }

    private void updateGroupStatus(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        cachedBluetoothDevice.setDeviceType(i);
        CachedBluetoothDevice findDevice = this.mCacheDeviceNamanger.findDevice(cachedBluetoothDevice.getDevice());
        if (findDevice != null) {
            findDevice.setDeviceType(i);
            if (!D) {
                return;
            }
            Log.d("GroupUtilss", "updateGroupStatus updated " + cachedBluetoothDevice + " " + i);
            return;
        }
        loge("updateGroupStatus failed  " + cachedBluetoothDevice + " groupId " + i);
    }

    public GroupUtils(Context context) {
        this.mBCProfile = null;
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
            if (D) {
                Log.d("GroupUtilss", "isNewGroup val " + groupId + " key " + groupPreferenceCategory.getKey());
            }
            if (i == groupId) {
                break;
            }
            i2++;
        }
        if (D) {
            Log.d("GroupUtilss", "isNewGroup id  " + i + "  val " + z);
        }
        return z;
    }

    private boolean isAllFilled(int i, ArrayList<GroupPreferenceCategory> arrayList) {
        for (int i2 = 0; i2 < arrayList.size() - 1; i2++) {
            GroupPreferenceCategory groupPreferenceCategory = arrayList.get(i2);
            if (groupPreferenceCategory == null) {
                loge("isAllFilled");
                return false;
            } else if (i == groupPreferenceCategory.getGroupId()) {
                return false;
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

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v3, types: [androidx.preference.Preference] */
    public void addPreference(ArrayList<GroupPreferenceCategory> arrayList, Preference preference, GearPreference.OnGearClickListener onGearClickListener) {
        GroupPreferenceCategory existingGroup;
        int groupId = getGroupId(preference);
        if (groupId == -1) {
            loge("addPreference groupId is not valid " + groupId);
            return;
        }
        boolean isNewGroup = isNewGroup(groupId, arrayList);
        boolean z = D;
        if (z) {
            Log.d("GroupUtilss", "addPreference  " + preference + " isNewGroup " + isNewGroup);
        }
        if (isNewGroup) {
            GroupBluetoothSettingsPreference hedaer = getHedaer(groupId, onGearClickListener);
            existingGroup = getParentGroup(arrayList, preference);
            if (existingGroup == null) {
                loge("getParentGroup not found for groupId " + groupId);
                isAllGroupsFilled(arrayList, hedaer);
                return;
            }
            existingGroup.setGroupId(groupId);
            existingGroup.addPreference(hedaer);
            existingGroup.addPreference(preference);
            existingGroup.setVisible(true);
        } else {
            existingGroup = getExistingGroup(arrayList, preference);
            if (existingGroup == null) {
                loge("getExistingGroup not found for groupId " + groupId);
                return;
            }
            existingGroup.addPreference(preference);
        }
        if (!z) {
            return;
        }
        Log.d("GroupUtilss", "addPreference  key " + existingGroup.getKey());
    }

    public void removePreference(ArrayList<GroupPreferenceCategory> arrayList, Preference preference) {
        GroupPreferenceCategory existingGroup = getExistingGroup(arrayList, preference);
        if (existingGroup == null) {
            loge("removePreference group null ");
            removePreference(arrayList.get(arrayList.size() - 1), preference);
            return;
        }
        existingGroup.removePreference(preference);
        if (existingGroup.getPreferenceCount() != 1) {
            return;
        }
        existingGroup.setGroupId(-1);
        existingGroup.removeAll();
        existingGroup.setVisible(false);
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
            boolean z = D;
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
        if (isAllFilled) {
            if (groupPreferenceCategory == null) {
                loge("isAllGroupsFilled received invalid group");
            } else if (!groupPreferenceCategory.getKey().contains("remaining")) {
                loge("isAllGroupsFilled not last group");
            } else {
                int preferenceCount = groupPreferenceCategory.getPreferenceCount();
                if (D) {
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
                            if (D) {
                                Log.d("GroupUtilss", "isAllGroupsFilled updated chCount " + incrementChildCound);
                            }
                        }
                    }
                    i++;
                }
                if (z) {
                    return;
                }
                int incrementChildCound2 = groupBluetoothSettingsPreference.incrementChildCound();
                groupPreferenceCategory.addPreference(groupBluetoothSettingsPreference);
                if (!D) {
                    return;
                }
                Log.d("GroupUtilss", "isAllGroupsFilled added chCount " + incrementChildCound2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<CachedBluetoothDevice> getCahcedDevice(int i) {
        Collection<CachedBluetoothDevice> cachedDevicesCopy = this.mCacheDeviceNamanger.getCachedDevicesCopy();
        ArrayList<CachedBluetoothDevice> arrayList = new ArrayList<>();
        for (CachedBluetoothDevice cachedBluetoothDevice : cachedDevicesCopy) {
            if (cachedBluetoothDevice != null && isGroupDeviceBonded(cachedBluetoothDevice) && getGroupId(cachedBluetoothDevice) == i) {
                arrayList.add(cachedBluetoothDevice);
            }
        }
        if (D) {
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

    /* JADX INFO: Access modifiers changed from: package-private */
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
            if (anyBCConnectedDevice == null) {
                return;
            }
            bundle.putString("device_address", anyBCConnectedDevice.getAddress());
            bundle.putShort("group_op", (short) 1);
            new SubSettingLauncher(this.mCtx).setDestination("com.android.settings.bluetooth.BluetoothSADetail").setArguments(bundle).setTitleRes(R.string.bluetooth_search_broadcasters).setSourceMetricsCategory(25).launch();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean connectGroup(int i) {
        if (isValid()) {
            return this.mGroupClientProfile.connectGroup(i);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean disconnectGroup(int i) {
        if (isValid()) {
            return this.mGroupClientProfile.disconnectGroup(i);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean forgetGroup(int i) {
        if (isValid()) {
            return this.mGroupClientProfile.forgetGroup(i);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isGroupDiscoveryInProgress(int i) {
        if (!isValid()) {
            return false;
        }
        return this.mGroupClientProfile.isGroupDiscoveryInProgress(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startGroupDiscovery(int i) {
        if (!isValid()) {
            return false;
        }
        boolean isGroupDiscoveryInProgress = this.mGroupClientProfile.isGroupDiscoveryInProgress(i);
        if (D) {
            Log.d("GroupUtilss", "startGroupDiscovery " + i + "isDiscovering " + isGroupDiscoveryInProgress);
        }
        if (isGroupDiscoveryInProgress) {
            return false;
        }
        this.mGroupClientProfile.startGroupDiscovery(i);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean stopGroupDiscovery(int i) {
        if (!isValid()) {
            return false;
        }
        boolean isGroupDiscoveryInProgress = this.mGroupClientProfile.isGroupDiscoveryInProgress(i);
        if (D) {
            Log.d("GroupUtilss", "stopGroupDiscovery " + i + "isDiscovering " + isGroupDiscoveryInProgress);
        }
        if (!isGroupDiscoveryInProgress) {
            return false;
        }
        this.mGroupClientProfile.stopGroupDiscovery(i);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:8:0x001a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public int getGroupSize(int i) {
        int i2;
        if (isValid()) {
            DeviceGroup group = this.mGroupClientProfile.getGroup(i);
            this.mDeviceGroup = group;
            if (group != null) {
                i2 = group.getDeviceGroupSize();
                if (D) {
                    Log.d("GroupUtilss", "getDeviceGroupSize size " + i2);
                }
                return i2;
            }
        }
        i2 = 0;
        if (D) {
        }
        return i2;
    }

    public boolean isHidePCGGroups() {
        boolean z;
        List<BluetoothDevice> mostRecentlyConnectedDevices = BluetoothAdapter.getDefaultAdapter().getMostRecentlyConnectedDevices();
        if (mostRecentlyConnectedDevices != null && mostRecentlyConnectedDevices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : mostRecentlyConnectedDevices) {
                CachedBluetoothDevice findDevice = this.mCacheDeviceNamanger.findDevice(bluetoothDevice);
                if (findDevice != null && isGroupDeviceBondedOnly(findDevice)) {
                    z = false;
                    break;
                }
            }
        }
        z = true;
        if (D) {
            Log.d("GroupUtilss", "isHidePCGGroups " + z);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isHideGroupOptions(int i) {
        boolean z;
        Collection<CachedBluetoothDevice> cachedDevicesCopy = this.mCacheDeviceNamanger.getCachedDevicesCopy();
        if (cachedDevicesCopy != null && cachedDevicesCopy.size() > 0) {
            for (CachedBluetoothDevice cachedBluetoothDevice : cachedDevicesCopy) {
                if (cachedBluetoothDevice != null && isGroupDeviceBonded(cachedBluetoothDevice) && isGroupIdMatch(cachedBluetoothDevice, i)) {
                    z = false;
                    break;
                }
            }
        }
        z = true;
        if (D) {
            Log.d("GroupUtilss", "isHideGroupOptions " + z);
        }
        return z;
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
            if (D) {
                Log.d("GroupUtilss", "isGroupEnabled advAudioFeatureMask " + i);
            }
            if (i == 0) {
                return;
            }
            this.mIsGroupEnabled = true;
        } catch (Exception e) {
            this.mIsGroupEnabled = false;
            Log.e("GroupUtilss", "isGroupEnabled " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isUpdate(int i, CachedBluetoothDevice cachedBluetoothDevice) {
        return isGroupDevice(cachedBluetoothDevice) && i == getGroupId(cachedBluetoothDevice);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
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
        if (D) {
            Log.d("GroupUtilss", "addDevice cachedDevice " + cachedBluetoothDevice + " name " + cachedBluetoothDevice.getName() + " is added " + z);
        }
        return z2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
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
        if (D) {
            Log.d("GroupUtilss", "removeDevice cachedDevice " + cachedBluetoothDevice + " name " + cachedBluetoothDevice.getName() + " isremoved " + z2);
        }
        return z;
    }
}
