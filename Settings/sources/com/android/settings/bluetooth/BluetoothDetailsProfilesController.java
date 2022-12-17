package com.android.settings.bluetooth;

import android.bluetooth.BluetoothCodecStatus;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.SystemProperties;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settingslib.bluetooth.A2dpProfile;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.HeadsetProfile;
import com.android.settingslib.bluetooth.LeAudioProfile;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfile;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import com.android.settingslib.bluetooth.MapProfile;
import com.android.settingslib.bluetooth.PanProfile;
import com.android.settingslib.bluetooth.PbapServerProfile;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.nothing.p006ui.support.NtCustSwitchPreference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BluetoothDetailsProfilesController extends BluetoothDetailsController implements Preference.OnPreferenceClickListener, LocalBluetoothProfileManager.ServiceListener, BluetoothCallback {
    static final String HIGH_QUALITY_AUDIO_PREF_TAG = "A2dpProfileHighQualityAudio";
    private List<CachedBluetoothDevice> mAllOfCachedDevices;
    private CachedBluetoothDevice mCachedDevice;
    /* access modifiers changed from: private */
    public boolean mCodecChanged = false;
    /* access modifiers changed from: private */
    public boolean mDisableCodecChange = false;
    private boolean mForceShow = false;
    private PreferenceFragmentCompat mFragment;
    private GroupBluetoothProfileSwitchConfirmDialog mGroupBluetoothProfileConfirm;
    private int mGroupId = -1;
    private GroupUtils mGroupUtils;
    private boolean mIsGroupDevice = false;
    private boolean mIsLeContactSharingEnabled = false;
    private boolean mIsProfileConfirmDialogSupported = false;
    private LocalBluetoothManager mManager;
    private LocalBluetoothProfile mProfile;
    private Map<String, List<CachedBluetoothDevice>> mProfileDeviceMap = new HashMap();
    private LocalBluetoothProfileManager mProfileManager;
    private NtCustSwitchPreference mProfilePref;
    PreferenceCategory mProfilesContainer;

    public String getPreferenceKey() {
        return "bluetooth_profiles";
    }

    public BluetoothDetailsProfilesController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, LocalBluetoothManager localBluetoothManager, CachedBluetoothDevice cachedBluetoothDevice, Lifecycle lifecycle) {
        super(context, preferenceFragmentCompat, cachedBluetoothDevice, lifecycle);
        this.mManager = localBluetoothManager;
        this.mProfileManager = localBluetoothManager.getProfileManager();
        this.mCachedDevice = cachedBluetoothDevice;
        this.mAllOfCachedDevices = getAllOfCachedBluetoothDevices();
        lifecycle.addObserver(this);
        this.mFragment = preferenceFragmentCompat;
        GroupUtils groupUtils = new GroupUtils(context);
        this.mGroupUtils = groupUtils;
        boolean isGroupDevice = groupUtils.isGroupDevice(this.mCachedDevice);
        this.mIsGroupDevice = isGroupDevice;
        if (isGroupDevice) {
            this.mGroupId = this.mGroupUtils.getGroupId(this.mCachedDevice);
        }
        this.mIsProfileConfirmDialogSupported = SystemProperties.getBoolean("persist.vendor.service.bt.profile_confirm_dialog", false);
        this.mForceShow = "1".equals(SystemProperties.get("persist.bluetooth.nt_show_ui", "0"));
    }

    /* access modifiers changed from: protected */
    public void init(PreferenceScreen preferenceScreen) {
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mProfilesContainer = preferenceCategory;
        preferenceCategory.setLayoutResource(R$layout.preference_bluetooth_profile_category);
        this.mIsLeContactSharingEnabled = DeviceConfig.getBoolean("settings_ui", "bt_le_audio_contact_sharing_enabled", true);
        refresh();
    }

    private NtCustSwitchPreference createProfilePreference(Context context, LocalBluetoothProfile localBluetoothProfile) {
        NtCustSwitchPreference ntCustSwitchPreference = new NtCustSwitchPreference(context);
        ntCustSwitchPreference.setKey(localBluetoothProfile.toString());
        ntCustSwitchPreference.setTitle(localBluetoothProfile.getNameResource(this.mCachedDevice.getDevice()));
        ntCustSwitchPreference.setOnPreferenceClickListener(this);
        ntCustSwitchPreference.setOrder(localBluetoothProfile.getOrdinal());
        return ntCustSwitchPreference;
    }

    private void refreshProfilePreference(SwitchPreference switchPreference, LocalBluetoothProfile localBluetoothProfile) {
        BluetoothDevice device = this.mCachedDevice.getDevice();
        boolean isLeAudioEnabled = isLeAudioEnabled();
        boolean z = localBluetoothProfile instanceof A2dpProfile;
        if (z || (localBluetoothProfile instanceof HeadsetProfile)) {
            if (isLeAudioEnabled) {
                Log.d("BtDetailsProfilesCtrl", "LE is enabled, gray out " + localBluetoothProfile.toString());
                switchPreference.setEnabled(false);
            } else {
                List list = this.mProfileDeviceMap.get(localBluetoothProfile.toString());
                switchPreference.setEnabled(!(list != null && list.stream().anyMatch(new BluetoothDetailsProfilesController$$ExternalSyntheticLambda3())));
            }
        } else if (localBluetoothProfile instanceof LeAudioProfile) {
            List list2 = this.mProfileDeviceMap.get(localBluetoothProfile.toString());
            boolean z2 = list2 != null && list2.stream().anyMatch(new BluetoothDetailsProfilesController$$ExternalSyntheticLambda4());
            if (isLeAudioEnabled && !z2) {
                A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
                HeadsetProfile headsetProfile = this.mProfileManager.getHeadsetProfile();
                grayOutPreferenceWhenLeAudioIsEnabled(a2dpProfile);
                grayOutPreferenceWhenLeAudioIsEnabled(headsetProfile);
            }
            switchPreference.setEnabled(!z2);
        } else if (!(localBluetoothProfile instanceof PbapServerProfile) || !isLeAudioEnabled || this.mIsLeContactSharingEnabled) {
            switchPreference.setEnabled(!this.mCachedDevice.isBusy());
        } else {
            switchPreference.setEnabled(false);
        }
        if (localBluetoothProfile instanceof MapProfile) {
            switchPreference.setChecked(device.getMessageAccessPermission() == 1);
        } else if (localBluetoothProfile instanceof PbapServerProfile) {
            switchPreference.setChecked(device.getPhonebookAccessPermission() == 1);
        } else if (localBluetoothProfile instanceof PanProfile) {
            switchPreference.setChecked(localBluetoothProfile.getConnectionStatus(device) == 2);
        } else {
            switchPreference.setChecked(localBluetoothProfile.isEnabled(device));
        }
        if (z) {
            A2dpProfile a2dpProfile2 = (A2dpProfile) localBluetoothProfile;
            NtCustSwitchPreference ntCustSwitchPreference = (NtCustSwitchPreference) this.mProfilesContainer.findPreference(HIGH_QUALITY_AUDIO_PREF_TAG);
            if (ntCustSwitchPreference != null) {
                if (!a2dpProfile2.isEnabled(device) || !a2dpProfile2.supportsHighQualityAudio(device)) {
                    ntCustSwitchPreference.setVisible(false);
                } else {
                    ntCustSwitchPreference.setVisible(true);
                    ntCustSwitchPreference.setTitle((CharSequence) a2dpProfile2.getHighQualityAudioOptionLabel(device));
                    ntCustSwitchPreference.setChecked(a2dpProfile2.isHighQualityAudioEnabled(device));
                    ntCustSwitchPreference.setEnabled(!this.mCachedDevice.isBusy());
                }
            }
        }
        boolean z3 = localBluetoothProfile instanceof HeadsetProfile;
        if (z3) {
            HeadsetProfile headsetProfile2 = (HeadsetProfile) localBluetoothProfile;
            NtCustSwitchPreference ntCustSwitchPreference2 = (NtCustSwitchPreference) this.mProfilesContainer.findPreference("HeadetProfileLowLatencyAudio");
            if (ntCustSwitchPreference2 != null) {
                if (!headsetProfile2.isEnabled(device) || !this.mForceShow) {
                    ntCustSwitchPreference2.setVisible(false);
                } else {
                    ntCustSwitchPreference2.setVisible(true);
                    ntCustSwitchPreference2.setTitle(R$string.low_latency_audio);
                    ntCustSwitchPreference2.setChecked(headsetProfile2.getLowLatencyMode(device));
                    ntCustSwitchPreference2.setEnabled(!this.mCachedDevice.isBusy());
                }
            }
        }
        if (z3) {
            HeadsetProfile headsetProfile3 = (HeadsetProfile) localBluetoothProfile;
            SwitchPreference switchPreference2 = (SwitchPreference) this.mProfilesContainer.findPreference("EQAudio");
            if (switchPreference2 == null) {
                return;
            }
            if (!headsetProfile3.isEnabled(device) || (!headsetProfile3.supportEq(device) && !this.mForceShow)) {
                switchPreference2.setVisible(false);
                return;
            }
            switchPreference2.setVisible(true);
            switchPreference2.setTitle(R$string.eq_audio);
            switchPreference2.setChecked(headsetProfile3.getEqMode(device));
            switchPreference2.setEnabled(!this.mCachedDevice.isBusy());
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0008, code lost:
        r2 = r2.mProfileDeviceMap.get(r0.toString());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isLeAudioEnabled() {
        /*
            r2 = this;
            com.android.settingslib.bluetooth.LocalBluetoothProfileManager r0 = r2.mProfileManager
            com.android.settingslib.bluetooth.LeAudioProfile r0 = r0.getLeAudioProfile()
            if (r0 == 0) goto L_0x0027
            java.util.Map<java.lang.String, java.util.List<com.android.settingslib.bluetooth.CachedBluetoothDevice>> r2 = r2.mProfileDeviceMap
            java.lang.String r1 = r0.toString()
            java.lang.Object r2 = r2.get(r1)
            java.util.List r2 = (java.util.List) r2
            if (r2 == 0) goto L_0x0027
            java.util.stream.Stream r2 = r2.stream()
            com.android.settings.bluetooth.BluetoothDetailsProfilesController$$ExternalSyntheticLambda5 r1 = new com.android.settings.bluetooth.BluetoothDetailsProfilesController$$ExternalSyntheticLambda5
            r1.<init>(r0)
            boolean r2 = r2.anyMatch(r1)
            if (r2 == 0) goto L_0x0027
            r2 = 1
            return r2
        L_0x0027:
            r2 = 0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bluetooth.BluetoothDetailsProfilesController.isLeAudioEnabled():boolean");
    }

    private void grayOutPreferenceWhenLeAudioIsEnabled(LocalBluetoothProfile localBluetoothProfile) {
        SwitchPreference switchPreference;
        if (localBluetoothProfile != null && (switchPreference = (SwitchPreference) this.mProfilesContainer.findPreference(localBluetoothProfile.toString())) != null) {
            Log.d("BtDetailsProfilesCtrl", "LE is enabled, gray out " + localBluetoothProfile.toString());
            switchPreference.setEnabled(false);
        }
    }

    private void enableProfile(LocalBluetoothProfile localBluetoothProfile) {
        BluetoothDevice device = this.mCachedDevice.getDevice();
        if (localBluetoothProfile instanceof PbapServerProfile) {
            device.setPhonebookAccessPermission(1);
            return;
        }
        if (localBluetoothProfile instanceof MapProfile) {
            device.setMessageAccessPermission(1);
        }
        if (localBluetoothProfile instanceof LeAudioProfile) {
            enableLeAudioProfile(localBluetoothProfile);
        } else {
            localBluetoothProfile.setEnabled(device, true);
        }
    }

    private void disableProfile(LocalBluetoothProfile localBluetoothProfile) {
        if (localBluetoothProfile instanceof LeAudioProfile) {
            disableLeAudioProfile(localBluetoothProfile);
            return;
        }
        BluetoothDevice device = this.mCachedDevice.getDevice();
        localBluetoothProfile.setEnabled(device, false);
        if (localBluetoothProfile instanceof MapProfile) {
            device.setMessageAccessPermission(2);
        } else if (localBluetoothProfile instanceof PbapServerProfile) {
            device.setPhonebookAccessPermission(2);
        }
    }

    public boolean onPreferenceClick(Preference preference) {
        LocalBluetoothProfile profileByName = this.mProfileManager.getProfileByName(preference.getKey());
        this.mProfile = profileByName;
        if (profileByName == null) {
            PbapServerProfile pbapProfile = this.mManager.getProfileManager().getPbapProfile();
            if (!TextUtils.equals(preference.getKey(), pbapProfile.toString())) {
                return false;
            }
            this.mProfile = pbapProfile;
        }
        this.mProfilePref = (NtCustSwitchPreference) preference;
        if (!this.mIsGroupDevice || !this.mIsProfileConfirmDialogSupported) {
            enableOrDisableProfile();
            return true;
        }
        showProfileConfirmDialog();
        return true;
    }

    private List<LocalBluetoothProfile> getProfiles() {
        ArrayList arrayList = new ArrayList();
        this.mProfileDeviceMap.clear();
        List<CachedBluetoothDevice> list = this.mAllOfCachedDevices;
        if (list != null && !list.isEmpty()) {
            for (CachedBluetoothDevice next : this.mAllOfCachedDevices) {
                for (LocalBluetoothProfile next2 : next.getConnectableProfiles()) {
                    if (this.mProfileDeviceMap.containsKey(next2.toString())) {
                        this.mProfileDeviceMap.get(next2.toString()).add(next);
                        Log.d("BtDetailsProfilesCtrl", "getProfiles: " + next2.toString() + " add device " + next.getDevice().getAnonymizedAddress());
                    } else {
                        ArrayList arrayList2 = new ArrayList();
                        arrayList2.add(next);
                        this.mProfileDeviceMap.put(next2.toString(), arrayList2);
                        arrayList.add(next2);
                    }
                }
            }
            BluetoothDevice device = this.mCachedDevice.getDevice();
            if (device.getPhonebookAccessPermission() != 0) {
                arrayList.add(this.mManager.getProfileManager().getPbapProfile());
            }
            MapProfile mapProfile = this.mManager.getProfileManager().getMapProfile();
            if (device.getMessageAccessPermission() != 0) {
                arrayList.add(mapProfile);
            }
            Log.d("BtDetailsProfilesCtrl", "getProfiles:result:" + arrayList);
        }
        return arrayList;
    }

    private List<CachedBluetoothDevice> getAllOfCachedBluetoothDevices() {
        ArrayList arrayList = new ArrayList();
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        if (cachedBluetoothDevice == null) {
            return arrayList;
        }
        arrayList.add(cachedBluetoothDevice);
        if (this.mCachedDevice.getGroupId() != -1) {
            for (CachedBluetoothDevice add : this.mCachedDevice.getMemberDevice()) {
                arrayList.add(add);
            }
        }
        return arrayList;
    }

    private void disableLeAudioProfile(LocalBluetoothProfile localBluetoothProfile) {
        if (localBluetoothProfile == null || this.mProfileDeviceMap.get(localBluetoothProfile.toString()) == null) {
            Log.e("BtDetailsProfilesCtrl", "There is no the LE profile or no device in mProfileDeviceMap. Do nothing.");
            return;
        }
        for (CachedBluetoothDevice cachedBluetoothDevice : this.mProfileDeviceMap.get(localBluetoothProfile.toString())) {
            Log.d("BtDetailsProfilesCtrl", "User disable LE device: " + cachedBluetoothDevice.getDevice().getAnonymizedAddress());
            localBluetoothProfile.setEnabled(cachedBluetoothDevice.getDevice(), false);
        }
        A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
        HeadsetProfile headsetProfile = this.mProfileManager.getHeadsetProfile();
        enableProfileAfterUserDisablesLeAudio(a2dpProfile);
        enableProfileAfterUserDisablesLeAudio(headsetProfile);
    }

    private void enableLeAudioProfile(LocalBluetoothProfile localBluetoothProfile) {
        if (localBluetoothProfile == null || this.mProfileDeviceMap.get(localBluetoothProfile.toString()) == null) {
            Log.e("BtDetailsProfilesCtrl", "There is no the LE profile or no device in mProfileDeviceMap. Do nothing.");
            return;
        }
        A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
        HeadsetProfile headsetProfile = this.mProfileManager.getHeadsetProfile();
        disableProfileBeforeUserEnablesLeAudio(a2dpProfile);
        disableProfileBeforeUserEnablesLeAudio(headsetProfile);
        for (CachedBluetoothDevice cachedBluetoothDevice : this.mProfileDeviceMap.get(localBluetoothProfile.toString())) {
            Log.d("BtDetailsProfilesCtrl", "User enable LE device: " + cachedBluetoothDevice.getDevice().getAnonymizedAddress());
            localBluetoothProfile.setEnabled(cachedBluetoothDevice.getDevice(), true);
        }
    }

    private void disableProfileBeforeUserEnablesLeAudio(LocalBluetoothProfile localBluetoothProfile) {
        if (localBluetoothProfile != null && this.mProfileDeviceMap.get(localBluetoothProfile.toString()) != null) {
            Log.d("BtDetailsProfilesCtrl", "Disable " + localBluetoothProfile.toString() + " before user enables LE");
            for (CachedBluetoothDevice cachedBluetoothDevice : this.mProfileDeviceMap.get(localBluetoothProfile.toString())) {
                if (localBluetoothProfile.isEnabled(cachedBluetoothDevice.getDevice())) {
                    localBluetoothProfile.setEnabled(cachedBluetoothDevice.getDevice(), false);
                } else {
                    Log.d("BtDetailsProfilesCtrl", "The " + localBluetoothProfile.toString() + " profile is disabled. Do nothing.");
                }
            }
        }
    }

    private void enableProfileAfterUserDisablesLeAudio(LocalBluetoothProfile localBluetoothProfile) {
        if (localBluetoothProfile != null && this.mProfileDeviceMap.get(localBluetoothProfile.toString()) != null) {
            Log.d("BtDetailsProfilesCtrl", "enable " + localBluetoothProfile.toString() + "after user disables LE");
            for (CachedBluetoothDevice cachedBluetoothDevice : this.mProfileDeviceMap.get(localBluetoothProfile.toString())) {
                if (!localBluetoothProfile.isEnabled(cachedBluetoothDevice.getDevice())) {
                    localBluetoothProfile.setEnabled(cachedBluetoothDevice.getDevice(), true);
                } else {
                    Log.d("BtDetailsProfilesCtrl", "The " + localBluetoothProfile.toString() + " profile is enabled. Do nothing.");
                }
            }
        }
    }

    private void maybeAddHighQualityAudioPref(LocalBluetoothProfile localBluetoothProfile) {
        if (localBluetoothProfile instanceof A2dpProfile) {
            BluetoothDevice device = this.mCachedDevice.getDevice();
            A2dpProfile a2dpProfile = (A2dpProfile) localBluetoothProfile;
            if (a2dpProfile.isProfileReady() && a2dpProfile.supportsHighQualityAudio(device)) {
                NtCustSwitchPreference ntCustSwitchPreference = new NtCustSwitchPreference(this.mProfilesContainer.getContext());
                ntCustSwitchPreference.setKey(HIGH_QUALITY_AUDIO_PREF_TAG);
                ntCustSwitchPreference.setVisible(false);
                ntCustSwitchPreference.setOnPreferenceClickListener(new BluetoothDetailsProfilesController$$ExternalSyntheticLambda2(this, ntCustSwitchPreference, a2dpProfile, device));
                this.mProfilesContainer.addPreference(ntCustSwitchPreference);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$maybeAddHighQualityAudioPref$3(final SwitchPreference switchPreference, A2dpProfile a2dpProfile, BluetoothDevice bluetoothDevice, Preference preference) {
        switchPreference.setEnabled(false);
        this.mDisableCodecChange = true;
        this.mCodecChanged = false;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                BluetoothDetailsProfilesController.this.mDisableCodecChange = false;
                if (BluetoothDetailsProfilesController.this.mCodecChanged) {
                    switchPreference.setEnabled(true);
                }
            }
        }, 680);
        boolean isChecked = ((SwitchPreference) preference).isChecked();
        if ((a2dpProfile.isMandatoryCodec(bluetoothDevice) && !isChecked) || (!a2dpProfile.isMandatoryCodec(bluetoothDevice) && isChecked)) {
            switchPreference.setEnabled(true);
        }
        a2dpProfile.setHighQualityAudioEnabled(this.mCachedDevice.getDevice(), isChecked);
        return true;
    }

    public void onPause() {
        for (CachedBluetoothDevice unregisterCallback : this.mAllOfCachedDevices) {
            unregisterCallback.unregisterCallback(this);
        }
        this.mProfileManager.removeServiceListener(this);
        this.mManager.getEventManager().unregisterCallback(this);
    }

    public void onResume() {
        for (CachedBluetoothDevice registerCallback : this.mAllOfCachedDevices) {
            registerCallback.registerCallback(this);
        }
        this.mProfileManager.addServiceListener(this);
        this.mManager.getEventManager().registerCallback(this);
    }

    public void onDeviceAttributesChanged() {
        for (CachedBluetoothDevice unregisterCallback : this.mAllOfCachedDevices) {
            unregisterCallback.unregisterCallback(this);
        }
        List<CachedBluetoothDevice> allOfCachedBluetoothDevices = getAllOfCachedBluetoothDevices();
        this.mAllOfCachedDevices = allOfCachedBluetoothDevices;
        for (CachedBluetoothDevice registerCallback : allOfCachedBluetoothDevices) {
            registerCallback.registerCallback(this);
        }
        super.onDeviceAttributesChanged();
    }

    public void onServiceConnected() {
        refresh();
    }

    public void onServiceDisconnected() {
        refresh();
    }

    private void updateA2dpHighQualityAudioPref() {
        A2dpProfile a2dpProfile;
        Iterator<LocalBluetoothProfile> it = getProfiles().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            LocalBluetoothProfile next = it.next();
            if (next instanceof A2dpProfile) {
                if (next.isProfileReady()) {
                    a2dpProfile = (A2dpProfile) next;
                }
            }
        }
        a2dpProfile = null;
        if (a2dpProfile != null) {
            BluetoothDevice device = this.mCachedDevice.getDevice();
            SwitchPreference switchPreference = (SwitchPreference) this.mProfilesContainer.findPreference(HIGH_QUALITY_AUDIO_PREF_TAG);
            if (switchPreference != null && a2dpProfile.isEnabled(device) && a2dpProfile.supportsHighQualityAudio(device)) {
                switchPreference.setTitle((CharSequence) a2dpProfile.getHighQualityAudioOptionLabel(device));
                switchPreference.setChecked(a2dpProfile.isHighQualityAudioEnabled(device));
                if (!this.mDisableCodecChange) {
                    switchPreference.setEnabled(true);
                }
                this.mCodecChanged = true;
            }
        }
    }

    public void onA2dpCodecConfigChanged(CachedBluetoothDevice cachedBluetoothDevice, BluetoothCodecStatus bluetoothCodecStatus) {
        if (cachedBluetoothDevice.equals(this.mCachedDevice)) {
            updateA2dpHighQualityAudioPref();
        }
    }

    /* access modifiers changed from: protected */
    public void refresh() {
        for (LocalBluetoothProfile next : getProfiles()) {
            if (next != null && next.isProfileReady()) {
                SwitchPreference switchPreference = (SwitchPreference) this.mProfilesContainer.findPreference(next.toString());
                if (switchPreference == null) {
                    switchPreference = createProfilePreference(this.mProfilesContainer.getContext(), next);
                    this.mProfilesContainer.addPreference(switchPreference);
                    maybeAddHighQualityAudioPref(next);
                    maybeAddLowLatencyPref(next);
                }
                refreshProfilePreference(switchPreference, next);
            }
        }
        for (LocalBluetoothProfile obj : this.mCachedDevice.getRemovedProfiles()) {
            SwitchPreference switchPreference2 = (SwitchPreference) this.mProfilesContainer.findPreference(obj.toString());
            if (switchPreference2 != null) {
                this.mProfilesContainer.removePreference(switchPreference2);
            }
        }
        if (this.mProfilesContainer.findPreference("bottom_preference") == null) {
            Preference preference = new Preference(this.mContext);
            preference.setLayoutResource(R$layout.preference_bluetooth_profile_category);
            preference.setEnabled(false);
            preference.setKey("bottom_preference");
            preference.setOrder(99);
            preference.setSelectable(false);
            this.mProfilesContainer.addPreference(preference);
        }
    }

    private void initGroupBluetoothProfileConfirm() {
        if (this.mIsGroupDevice) {
            GroupBluetoothProfileSwitchConfirmDialog groupBluetoothProfileSwitchConfirmDialog = this.mGroupBluetoothProfileConfirm;
            if (groupBluetoothProfileSwitchConfirmDialog != null) {
                groupBluetoothProfileSwitchConfirmDialog.dismiss();
                this.mGroupBluetoothProfileConfirm = null;
            }
            GroupBluetoothProfileSwitchConfirmDialog newInstance = GroupBluetoothProfileSwitchConfirmDialog.newInstance(this.mGroupId);
            this.mGroupBluetoothProfileConfirm = newInstance;
            newInstance.setPairingController(this);
        }
    }

    public void onDialogNegativeClick() {
        resetProfileSwitch();
        this.mGroupBluetoothProfileConfirm.dismiss();
    }

    public void onDialogPositiveClick() {
        enableOrDisableProfile();
        this.mGroupBluetoothProfileConfirm.dismiss();
    }

    private void showProfileConfirmDialog() {
        initGroupBluetoothProfileConfirm();
        this.mGroupBluetoothProfileConfirm.show(this.mFragment.getFragmentManager(), "GroupBluetoothProfileSwitchConfirmDialog");
    }

    private void resetProfileSwitch() {
        NtCustSwitchPreference ntCustSwitchPreference = this.mProfilePref;
        ntCustSwitchPreference.setChecked(!ntCustSwitchPreference.isChecked());
    }

    private void enableOrDisableProfile() {
        if (this.mProfilePref.isChecked()) {
            enableProfile(this.mProfile);
        } else {
            disableProfile(this.mProfile);
        }
        refreshProfilePreference(this.mProfilePref, this.mProfile);
    }

    public void maybeAddLowLatencyPref(LocalBluetoothProfile localBluetoothProfile) {
        if (localBluetoothProfile instanceof HeadsetProfile) {
            BluetoothDevice device = this.mCachedDevice.getDevice();
            HeadsetProfile headsetProfile = (HeadsetProfile) localBluetoothProfile;
            if (headsetProfile.isProfileReady() && this.mForceShow) {
                NtCustSwitchPreference ntCustSwitchPreference = new NtCustSwitchPreference(this.mProfilesContainer.getContext());
                ntCustSwitchPreference.setKey("HeadetProfileLowLatencyAudio");
                ntCustSwitchPreference.setVisible(false);
                ntCustSwitchPreference.setOnPreferenceClickListener(new BluetoothDetailsProfilesController$$ExternalSyntheticLambda0(this, ntCustSwitchPreference, headsetProfile, device));
                this.mProfilesContainer.addPreference(ntCustSwitchPreference);
            }
            if (!headsetProfile.isProfileReady()) {
                return;
            }
            if (headsetProfile.supportEq(device) || this.mForceShow) {
                NtCustSwitchPreference ntCustSwitchPreference2 = new NtCustSwitchPreference(this.mProfilesContainer.getContext());
                ntCustSwitchPreference2.setKey("EQAudio");
                ntCustSwitchPreference2.setVisible(false);
                ntCustSwitchPreference2.setOnPreferenceClickListener(new BluetoothDetailsProfilesController$$ExternalSyntheticLambda1(this, ntCustSwitchPreference2, headsetProfile, device));
                this.mProfilesContainer.addPreference(ntCustSwitchPreference2);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$maybeAddLowLatencyPref$4(SwitchPreference switchPreference, HeadsetProfile headsetProfile, BluetoothDevice bluetoothDevice, Preference preference) {
        switchPreference.setEnabled(true);
        boolean isChecked = ((SwitchPreference) preference).isChecked();
        if ((headsetProfile.getLowLatencyMode(bluetoothDevice) && !isChecked) || (!headsetProfile.getLowLatencyMode(bluetoothDevice) && isChecked)) {
            switchPreference.setEnabled(true);
        }
        headsetProfile.setLowLatencyMode(this.mCachedDevice.getDevice(), isChecked);
        for (LocalBluetoothProfile next : getProfiles()) {
            if (next.isProfileReady() && (next instanceof A2dpProfile)) {
                SystemProperties.set("persist.bluetooth.sbc_bitpool", "39");
                ((A2dpProfile) next).setHighQualityAudioEnabled(this.mCachedDevice.getDevice(), !isChecked);
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$maybeAddLowLatencyPref$5(SwitchPreference switchPreference, HeadsetProfile headsetProfile, BluetoothDevice bluetoothDevice, Preference preference) {
        switchPreference.setEnabled(true);
        boolean isChecked = ((SwitchPreference) preference).isChecked();
        if ((headsetProfile.getEqMode(bluetoothDevice) && !isChecked) || (!headsetProfile.getEqMode(bluetoothDevice) && isChecked)) {
            switchPreference.setEnabled(true);
        }
        headsetProfile.setEqMode(this.mCachedDevice.getDevice(), isChecked);
        return true;
    }
}
