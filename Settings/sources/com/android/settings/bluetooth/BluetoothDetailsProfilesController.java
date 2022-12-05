package com.android.settings.bluetooth;

import android.bluetooth.BluetoothCodecStatus;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.SystemProperties;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R;
import com.android.settingslib.bluetooth.A2dpProfile;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.HeadsetProfile;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfile;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import com.android.settingslib.bluetooth.MapProfile;
import com.android.settingslib.bluetooth.PanProfile;
import com.android.settingslib.bluetooth.PbapServerProfile;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class BluetoothDetailsProfilesController extends BluetoothDetailsController implements Preference.OnPreferenceClickListener, LocalBluetoothProfileManager.ServiceListener, BluetoothCallback {
    static final String HIGH_QUALITY_AUDIO_PREF_TAG = "A2dpProfileHighQualityAudio";
    private CachedBluetoothDevice mCachedDevice;
    private boolean mForceShow;
    private PreferenceFragmentCompat mFragment;
    private GroupBluetoothProfileSwitchConfirmDialog mGroupBluetoothProfileConfirm;
    private int mGroupId;
    private GroupUtils mGroupUtils;
    private boolean mIsGroupDevice;
    private LocalBluetoothManager mManager;
    private LocalBluetoothProfile mProfile;
    private LocalBluetoothProfileManager mProfileManager;
    private SwitchPreference mProfilePref;
    PreferenceCategory mProfilesContainer;

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "bluetooth_profiles";
    }

    public BluetoothDetailsProfilesController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, LocalBluetoothManager localBluetoothManager, CachedBluetoothDevice cachedBluetoothDevice, Lifecycle lifecycle) {
        super(context, preferenceFragmentCompat, cachedBluetoothDevice, lifecycle);
        this.mIsGroupDevice = false;
        this.mGroupId = -1;
        this.mForceShow = false;
        this.mManager = localBluetoothManager;
        this.mProfileManager = localBluetoothManager.getProfileManager();
        this.mCachedDevice = cachedBluetoothDevice;
        lifecycle.addObserver(this);
        this.mFragment = preferenceFragmentCompat;
        GroupUtils groupUtils = new GroupUtils(context);
        this.mGroupUtils = groupUtils;
        boolean isGroupDevice = groupUtils.isGroupDevice(this.mCachedDevice);
        this.mIsGroupDevice = isGroupDevice;
        if (isGroupDevice) {
            this.mGroupId = this.mGroupUtils.getGroupId(this.mCachedDevice);
        }
        this.mForceShow = "1".equals(SystemProperties.get("persist.bluetooth.nt_show_ui", "0"));
    }

    @Override // com.android.settings.bluetooth.BluetoothDetailsController
    protected void init(PreferenceScreen preferenceScreen) {
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mProfilesContainer = preferenceCategory;
        preferenceCategory.setLayoutResource(R.layout.preference_bluetooth_profile_category);
        refresh();
    }

    private SwitchPreference createProfilePreference(Context context, LocalBluetoothProfile localBluetoothProfile) {
        SwitchPreference switchPreference = new SwitchPreference(context);
        switchPreference.setKey(localBluetoothProfile.toString());
        switchPreference.setTitle(localBluetoothProfile.getNameResource(this.mCachedDevice.getDevice()));
        switchPreference.setOnPreferenceClickListener(this);
        switchPreference.setOrder(localBluetoothProfile.getOrdinal());
        return switchPreference;
    }

    private void refreshProfilePreference(SwitchPreference switchPreference, LocalBluetoothProfile localBluetoothProfile) {
        BluetoothDevice device = this.mCachedDevice.getDevice();
        switchPreference.setEnabled(!this.mCachedDevice.isBusy());
        if (localBluetoothProfile instanceof MapProfile) {
            switchPreference.setChecked(device.getMessageAccessPermission() == 1);
        } else if (localBluetoothProfile instanceof PbapServerProfile) {
            switchPreference.setChecked(device.getPhonebookAccessPermission() == 1);
        } else if (localBluetoothProfile instanceof PanProfile) {
            switchPreference.setChecked(localBluetoothProfile.getConnectionStatus(device) == 2);
        } else {
            switchPreference.setChecked(localBluetoothProfile.isEnabled(device));
        }
        if (localBluetoothProfile instanceof A2dpProfile) {
            A2dpProfile a2dpProfile = (A2dpProfile) localBluetoothProfile;
            SwitchPreference switchPreference2 = (SwitchPreference) this.mProfilesContainer.findPreference(HIGH_QUALITY_AUDIO_PREF_TAG);
            if (switchPreference2 != null) {
                if (a2dpProfile.isEnabled(device) && a2dpProfile.supportsHighQualityAudio(device)) {
                    switchPreference2.setVisible(true);
                    switchPreference2.setTitle(a2dpProfile.getHighQualityAudioOptionLabel(device));
                    switchPreference2.setChecked(a2dpProfile.isHighQualityAudioEnabled(device));
                    switchPreference2.setEnabled(!this.mCachedDevice.isBusy());
                } else {
                    switchPreference2.setVisible(false);
                }
            }
        }
        boolean z = localBluetoothProfile instanceof HeadsetProfile;
        if (z) {
            HeadsetProfile headsetProfile = (HeadsetProfile) localBluetoothProfile;
            SwitchPreference switchPreference3 = (SwitchPreference) this.mProfilesContainer.findPreference("HeadetProfileLowLatencyAudio");
            if (switchPreference3 != null) {
                if (headsetProfile.isEnabled(device) && this.mForceShow) {
                    switchPreference3.setVisible(true);
                    switchPreference3.setTitle(R.string.low_latency_audio);
                    switchPreference3.setChecked(headsetProfile.getLowLatencyMode(device));
                    switchPreference3.setEnabled(!this.mCachedDevice.isBusy());
                } else {
                    switchPreference3.setVisible(false);
                }
            }
        }
        if (z) {
            HeadsetProfile headsetProfile2 = (HeadsetProfile) localBluetoothProfile;
            SwitchPreference switchPreference4 = (SwitchPreference) this.mProfilesContainer.findPreference("EQAudio");
            if (switchPreference4 == null) {
                return;
            }
            if (headsetProfile2.isEnabled(device) && (headsetProfile2.supportEq(device) || this.mForceShow)) {
                switchPreference4.setVisible(true);
                switchPreference4.setTitle(R.string.eq_audio);
                switchPreference4.setChecked(headsetProfile2.getEqMode(device));
                switchPreference4.setEnabled(!this.mCachedDevice.isBusy());
                return;
            }
            switchPreference4.setVisible(false);
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
        localBluetoothProfile.setEnabled(device, true);
    }

    private void disableProfile(LocalBluetoothProfile localBluetoothProfile) {
        BluetoothDevice device = this.mCachedDevice.getDevice();
        localBluetoothProfile.setEnabled(device, false);
        if (localBluetoothProfile instanceof MapProfile) {
            device.setMessageAccessPermission(2);
        } else if (!(localBluetoothProfile instanceof PbapServerProfile)) {
        } else {
            device.setPhonebookAccessPermission(2);
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
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
        this.mProfilePref = (SwitchPreference) preference;
        if (this.mIsGroupDevice) {
            showProfileConfirmDialog();
            return true;
        }
        enableOrDisableProfile();
        return true;
    }

    private List<LocalBluetoothProfile> getProfiles() {
        List<LocalBluetoothProfile> connectableProfiles = this.mCachedDevice.getConnectableProfiles();
        BluetoothDevice device = this.mCachedDevice.getDevice();
        if (device.getPhonebookAccessPermission() != 0) {
            connectableProfiles.add(this.mManager.getProfileManager().getPbapProfile());
        }
        MapProfile mapProfile = this.mManager.getProfileManager().getMapProfile();
        if (device.getMessageAccessPermission() != 0) {
            connectableProfiles.add(mapProfile);
        }
        return connectableProfiles;
    }

    private void maybeAddHighQualityAudioPref(LocalBluetoothProfile localBluetoothProfile) {
        if (!(localBluetoothProfile instanceof A2dpProfile)) {
            return;
        }
        final BluetoothDevice device = this.mCachedDevice.getDevice();
        final A2dpProfile a2dpProfile = (A2dpProfile) localBluetoothProfile;
        if (!a2dpProfile.isProfileReady() || !a2dpProfile.supportsHighQualityAudio(device)) {
            return;
        }
        final SwitchPreference switchPreference = new SwitchPreference(this.mProfilesContainer.getContext());
        switchPreference.setKey(HIGH_QUALITY_AUDIO_PREF_TAG);
        switchPreference.setVisible(false);
        switchPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.settings.bluetooth.BluetoothDetailsProfilesController$$ExternalSyntheticLambda0
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public final boolean onPreferenceClick(Preference preference) {
                boolean lambda$maybeAddHighQualityAudioPref$0;
                lambda$maybeAddHighQualityAudioPref$0 = BluetoothDetailsProfilesController.this.lambda$maybeAddHighQualityAudioPref$0(switchPreference, a2dpProfile, device, preference);
                return lambda$maybeAddHighQualityAudioPref$0;
            }
        });
        this.mProfilesContainer.addPreference(switchPreference);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$maybeAddHighQualityAudioPref$0(SwitchPreference switchPreference, A2dpProfile a2dpProfile, BluetoothDevice bluetoothDevice, Preference preference) {
        switchPreference.setEnabled(false);
        boolean isChecked = ((SwitchPreference) preference).isChecked();
        if ((a2dpProfile.isMandatoryCodec(bluetoothDevice) && !isChecked) || (!a2dpProfile.isMandatoryCodec(bluetoothDevice) && isChecked)) {
            switchPreference.setEnabled(true);
        }
        a2dpProfile.setHighQualityAudioEnabled(this.mCachedDevice.getDevice(), isChecked);
        return true;
    }

    @Override // com.android.settings.bluetooth.BluetoothDetailsController, com.android.settingslib.core.lifecycle.events.OnPause
    public void onPause() {
        super.onPause();
        this.mProfileManager.removeServiceListener(this);
        this.mManager.getEventManager().unregisterCallback(this);
    }

    @Override // com.android.settings.bluetooth.BluetoothDetailsController, com.android.settingslib.core.lifecycle.events.OnResume
    public void onResume() {
        super.onResume();
        this.mProfileManager.addServiceListener(this);
        this.mManager.getEventManager().registerCallback(this);
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfileManager.ServiceListener
    public void onServiceConnected() {
        refresh();
    }

    @Override // com.android.settingslib.bluetooth.LocalBluetoothProfileManager.ServiceListener
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
        if (a2dpProfile == null) {
            return;
        }
        BluetoothDevice device = this.mCachedDevice.getDevice();
        SwitchPreference switchPreference = (SwitchPreference) this.mProfilesContainer.findPreference(HIGH_QUALITY_AUDIO_PREF_TAG);
        if (switchPreference == null || !a2dpProfile.isEnabled(device) || !a2dpProfile.supportsHighQualityAudio(device)) {
            return;
        }
        switchPreference.setTitle(a2dpProfile.getHighQualityAudioOptionLabel(device));
        switchPreference.setChecked(a2dpProfile.isHighQualityAudioEnabled(device));
        switchPreference.setEnabled(true);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onA2dpCodecConfigChanged(CachedBluetoothDevice cachedBluetoothDevice, BluetoothCodecStatus bluetoothCodecStatus) {
        if (!cachedBluetoothDevice.equals(this.mCachedDevice)) {
            return;
        }
        updateA2dpHighQualityAudioPref();
    }

    @Override // com.android.settings.bluetooth.BluetoothDetailsController
    protected void refresh() {
        for (LocalBluetoothProfile localBluetoothProfile : getProfiles()) {
            if (localBluetoothProfile.isProfileReady()) {
                SwitchPreference switchPreference = (SwitchPreference) this.mProfilesContainer.findPreference(localBluetoothProfile.toString());
                if (switchPreference == null) {
                    switchPreference = createProfilePreference(this.mProfilesContainer.getContext(), localBluetoothProfile);
                    this.mProfilesContainer.addPreference(switchPreference);
                    maybeAddHighQualityAudioPref(localBluetoothProfile);
                    maybeAddLowLatencyPref(localBluetoothProfile);
                }
                refreshProfilePreference(switchPreference, localBluetoothProfile);
            }
        }
        for (LocalBluetoothProfile localBluetoothProfile2 : this.mCachedDevice.getRemovedProfiles()) {
            SwitchPreference switchPreference2 = (SwitchPreference) this.mProfilesContainer.findPreference(localBluetoothProfile2.toString());
            if (switchPreference2 != null) {
                this.mProfilesContainer.removePreference(switchPreference2);
            }
        }
        if (this.mProfilesContainer.findPreference("bottom_preference") == null) {
            Preference preference = new Preference(((BluetoothDetailsController) this).mContext);
            preference.setLayoutResource(R.layout.preference_bluetooth_profile_category);
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
        SwitchPreference switchPreference = this.mProfilePref;
        switchPreference.setChecked(!switchPreference.isChecked());
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
        if (!(localBluetoothProfile instanceof HeadsetProfile)) {
            return;
        }
        final BluetoothDevice device = this.mCachedDevice.getDevice();
        final HeadsetProfile headsetProfile = (HeadsetProfile) localBluetoothProfile;
        if (headsetProfile.isProfileReady() && this.mForceShow) {
            final SwitchPreference switchPreference = new SwitchPreference(this.mProfilesContainer.getContext());
            switchPreference.setKey("HeadetProfileLowLatencyAudio");
            switchPreference.setVisible(false);
            switchPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.settings.bluetooth.BluetoothDetailsProfilesController$$ExternalSyntheticLambda1
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public final boolean onPreferenceClick(Preference preference) {
                    boolean lambda$maybeAddLowLatencyPref$1;
                    lambda$maybeAddLowLatencyPref$1 = BluetoothDetailsProfilesController.this.lambda$maybeAddLowLatencyPref$1(switchPreference, headsetProfile, device, preference);
                    return lambda$maybeAddLowLatencyPref$1;
                }
            });
            this.mProfilesContainer.addPreference(switchPreference);
        }
        if (!headsetProfile.isProfileReady()) {
            return;
        }
        if (!headsetProfile.supportEq(device) && !this.mForceShow) {
            return;
        }
        final SwitchPreference switchPreference2 = new SwitchPreference(this.mProfilesContainer.getContext());
        switchPreference2.setKey("EQAudio");
        switchPreference2.setVisible(false);
        switchPreference2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.android.settings.bluetooth.BluetoothDetailsProfilesController$$ExternalSyntheticLambda2
            @Override // androidx.preference.Preference.OnPreferenceClickListener
            public final boolean onPreferenceClick(Preference preference) {
                boolean lambda$maybeAddLowLatencyPref$2;
                lambda$maybeAddLowLatencyPref$2 = BluetoothDetailsProfilesController.this.lambda$maybeAddLowLatencyPref$2(switchPreference2, headsetProfile, device, preference);
                return lambda$maybeAddLowLatencyPref$2;
            }
        });
        this.mProfilesContainer.addPreference(switchPreference2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$maybeAddLowLatencyPref$1(SwitchPreference switchPreference, HeadsetProfile headsetProfile, BluetoothDevice bluetoothDevice, Preference preference) {
        switchPreference.setEnabled(true);
        boolean isChecked = ((SwitchPreference) preference).isChecked();
        if ((headsetProfile.getLowLatencyMode(bluetoothDevice) && !isChecked) || (!headsetProfile.getLowLatencyMode(bluetoothDevice) && isChecked)) {
            switchPreference.setEnabled(true);
        }
        headsetProfile.setLowLatencyMode(this.mCachedDevice.getDevice(), isChecked);
        for (LocalBluetoothProfile localBluetoothProfile : getProfiles()) {
            if (localBluetoothProfile.isProfileReady() && (localBluetoothProfile instanceof A2dpProfile)) {
                SystemProperties.set("persist.bluetooth.sbc_bitpool", "39");
                ((A2dpProfile) localBluetoothProfile).setHighQualityAudioEnabled(this.mCachedDevice.getDevice(), !isChecked);
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$maybeAddLowLatencyPref$2(SwitchPreference switchPreference, HeadsetProfile headsetProfile, BluetoothDevice bluetoothDevice, Preference preference) {
        switchPreference.setEnabled(true);
        boolean isChecked = ((SwitchPreference) preference).isChecked();
        if ((headsetProfile.getEqMode(bluetoothDevice) && !isChecked) || (!headsetProfile.getEqMode(bluetoothDevice) && isChecked)) {
            switchPreference.setEnabled(true);
        }
        headsetProfile.setEqMode(this.mCachedDevice.getDevice(), isChecked);
        return true;
    }
}
