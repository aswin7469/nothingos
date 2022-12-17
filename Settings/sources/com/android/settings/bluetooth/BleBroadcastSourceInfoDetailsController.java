package com.android.settings.bluetooth;

import android.bluetooth.BleBroadcastAudioScanAssistManager;
import android.bluetooth.BleBroadcastSourceChannel;
import android.bluetooth.BleBroadcastSourceInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.EditTextPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R$drawable;
import com.android.settings.R$string;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.VendorCachedBluetoothDevice;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.widget.ActionButtonsPreference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BleBroadcastSourceInfoDetailsController extends BluetoothDetailsController implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    private final String EMPTY_BD_ADDRESS = "00:00:00:00:00:00";
    private String EMPTY_ENTRY = "EMPTY ENTRY";
    private boolean isBroadcastPINUpdated = false;
    private int mAudioSyncState;
    private List<BleBroadcastSourceChannel> mBisIndicies;
    private BleBroadcastSourceInfo mBleBroadcastSourceInfo;
    private String mBroadcastCode;
    private CachedBluetoothDevice mCachedDevice;
    /* access modifiers changed from: private */
    public boolean mGroupOp = false;
    private boolean mIsButtonRefreshOnly = false;
    private boolean mIsValueChanged = false;
    private int mMetadataSyncState;
    private boolean mPAsyncCtrlNeeded = false;
    /* access modifiers changed from: private */
    public AlertDialog mScanAssistGroupOpDialog = null;
    private BleBroadcastAudioScanAssistManager mScanAssistanceMgr;
    private MultiSelectListPreference mSourceAudioSyncStatusPref;
    private SwitchPreference mSourceAudioSyncSwitchPref;
    private Preference mSourceDevicePref;
    private Preference mSourceEncStatusPref;
    private Preference mSourceIdPref;
    private PreferenceCategory mSourceInfoContainer;
    private int mSourceInfoIndex;
    private Preference mSourceMetadataPref;
    private Preference mSourceMetadataSyncStatusPref;
    private SwitchPreference mSourceMetadataSyncSwitchPref;
    private EditTextPreference mSourceUpdateBcastCodePref;
    private ActionButtonsPreference mSourceUpdateSourceInfoPref;
    private VendorCachedBluetoothDevice mVendorCachedDevice;

    private int getSyncState(int i, int i2) {
        if (i2 == 1 && i == 2) {
            return 2;
        }
        if (i != 2) {
            return 1;
        }
        return (i2 == 1 || i != 2) ? -1 : 0;
    }

    /* access modifiers changed from: package-private */
    public String getAudioSyncStatusString(int i) {
        return i != 0 ? i != 1 ? "UNKNOWN" : "IN SYNC" : "NOT IN SYNC";
    }

    /* access modifiers changed from: package-private */
    public String getEncryptionStatusString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? "ENCRYPTION STATE UNKNOWN" : "INCORRECT BROADCAST PIN" : "DECRYPTING SUCCESSFULLY" : "PIN UPDATE NEEDED" : "UNENCRYPTED STREAMING";
    }

    /* access modifiers changed from: package-private */
    public String getMetadataSyncStatusString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? "UNKNOWN" : "NO PAST" : "SYNC FAIL" : "IN SYNC" : "SYNCINFO NEEDED" : "IDLE";
    }

    public String getPreferenceKey() {
        return "broadcast_source_details_category";
    }

    public BleBroadcastSourceInfoDetailsController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, BleBroadcastSourceInfo bleBroadcastSourceInfo, CachedBluetoothDevice cachedBluetoothDevice, int i, Lifecycle lifecycle) {
        super(context, preferenceFragmentCompat, cachedBluetoothDevice, lifecycle);
        this.mBleBroadcastSourceInfo = bleBroadcastSourceInfo;
        this.mCachedDevice = cachedBluetoothDevice;
        VendorCachedBluetoothDevice vendorCachedBluetoothDevice = VendorCachedBluetoothDevice.getVendorCachedBluetoothDevice(cachedBluetoothDevice, Utils.getLocalBtManager(context).getProfileManager());
        this.mVendorCachedDevice = vendorCachedBluetoothDevice;
        this.mScanAssistanceMgr = vendorCachedBluetoothDevice.getScanAssistManager();
        lifecycle.addObserver(this);
        this.mSourceInfoIndex = i;
        clearInputs();
        this.mPAsyncCtrlNeeded = false;
    }

    private void clearInputs() {
        this.mMetadataSyncState = 2;
        this.mAudioSyncState = 65535;
        this.mBroadcastCode = null;
        this.isBroadcastPINUpdated = false;
    }

    /* access modifiers changed from: private */
    public void triggerRemoveBroadcastSource() {
        forceUpdateBroadcastSourceToLoseSync();
        BleBroadcastAudioScanAssistManager bleBroadcastAudioScanAssistManager = this.mScanAssistanceMgr;
        if (bleBroadcastAudioScanAssistManager != null) {
            bleBroadcastAudioScanAssistManager.removeBroadcastSource(this.mBleBroadcastSourceInfo.getSourceId(), this.mGroupOp);
        }
    }

    private void forceUpdateBroadcastSourceToLoseSync() {
        BleBroadcastSourceInfo bleBroadcastSourceInfo = this.mBleBroadcastSourceInfo;
        if (bleBroadcastSourceInfo != null && bleBroadcastSourceInfo.getMetadataSyncState() == 2) {
            Log.e("BleBroadcastSourceInfoDetailsController", "triggerUpdateBroadcastsource with PA off");
            this.mMetadataSyncState = 0;
            triggerUpdateBroadcastSource();
            SystemClock.sleep(200);
        }
    }

    private void onRemoveBroadcastSourceInfoPressed() {
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":onRemoveBroadcastSourceInfoPressed:" + this.mBleBroadcastSourceInfo);
        if (this.mCachedDevice.isGroupDevice()) {
            String name = this.mCachedDevice.getName();
            if (TextUtils.isEmpty(name)) {
                name = this.mContext.getString(R$string.bluetooth_device);
            }
            String string = this.mContext.getString(R$string.group_remove_source_message, new Object[]{name});
            String string2 = this.mContext.getString(R$string.group_remove_source_title);
            C07671 r6 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (BleBroadcastSourceInfoDetailsController.this.mScanAssistGroupOpDialog != null) {
                        BleBroadcastSourceInfoDetailsController.this.mScanAssistGroupOpDialog.dismiss();
                        BleBroadcastSourceInfoDetailsController.this.mScanAssistGroupOpDialog = null;
                    }
                    BleBroadcastSourceInfoDetailsController.this.mGroupOp = true;
                    BleBroadcastSourceInfoDetailsController.this.triggerRemoveBroadcastSource();
                }
            };
            C07682 r7 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (BleBroadcastSourceInfoDetailsController.this.mScanAssistGroupOpDialog != null) {
                        BleBroadcastSourceInfoDetailsController.this.mScanAssistGroupOpDialog.dismiss();
                        BleBroadcastSourceInfoDetailsController.this.mScanAssistGroupOpDialog = null;
                    }
                    BleBroadcastSourceInfoDetailsController.this.mGroupOp = false;
                    BleBroadcastSourceInfoDetailsController.this.triggerRemoveBroadcastSource();
                }
            };
            this.mGroupOp = false;
            this.mScanAssistGroupOpDialog = BroadcastScanAssistanceUtils.showAssistanceGroupOptionsDialog(this.mContext, this.mScanAssistGroupOpDialog, r6, r7, string2, Html.fromHtml(string));
            return;
        }
        this.mGroupOp = false;
        triggerRemoveBroadcastSource();
    }

    /* access modifiers changed from: private */
    public void triggerUpdateBroadcastSource() {
        if (this.mScanAssistanceMgr != null) {
            int syncState = getSyncState(this.mMetadataSyncState, this.mAudioSyncState);
            if (syncState == -1) {
                Log.e("BleBroadcastSourceInfoDetailsController", "triggerUpdateBroadcastSource: Invalid sync Input, Ignore");
                return;
            }
            Log.e("BleBroadcastSourceInfoDetailsController", "triggerUpdateBroadcastSource:  " + syncState);
            this.mScanAssistanceMgr.updateBroadcastSource(this.mBleBroadcastSourceInfo.getSourceId(), syncState, this.mBisIndicies, this.mGroupOp);
            this.mIsValueChanged = false;
            if (this.isBroadcastPINUpdated) {
                this.mScanAssistanceMgr.setBroadcastCode(this.mBleBroadcastSourceInfo.getSourceId(), this.mBroadcastCode, this.mGroupOp);
                this.isBroadcastPINUpdated = false;
            }
            clearInputs();
        }
    }

    private void onUpdateBroadcastSourceInfoPressed() {
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + "onUpdateBroadcastSourceInfoPressed:" + this.mBleBroadcastSourceInfo);
        if (this.mCachedDevice.isGroupDevice()) {
            String name = this.mCachedDevice.getName();
            if (TextUtils.isEmpty(name)) {
                name = this.mContext.getString(R$string.bluetooth_device);
            }
            String string = this.mContext.getString(R$string.group_update_source_message, new Object[]{name});
            String string2 = this.mContext.getString(R$string.group_update_source_title);
            C07693 r6 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (BleBroadcastSourceInfoDetailsController.this.mScanAssistGroupOpDialog != null) {
                        BleBroadcastSourceInfoDetailsController.this.mScanAssistGroupOpDialog.dismiss();
                        BleBroadcastSourceInfoDetailsController.this.mScanAssistGroupOpDialog = null;
                    }
                    BleBroadcastSourceInfoDetailsController.this.mGroupOp = true;
                    BleBroadcastSourceInfoDetailsController.this.triggerUpdateBroadcastSource();
                }
            };
            C07704 r7 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (BleBroadcastSourceInfoDetailsController.this.mScanAssistGroupOpDialog != null) {
                        BleBroadcastSourceInfoDetailsController.this.mScanAssistGroupOpDialog.dismiss();
                        BleBroadcastSourceInfoDetailsController.this.mScanAssistGroupOpDialog = null;
                    }
                    BleBroadcastSourceInfoDetailsController.this.mGroupOp = false;
                    BleBroadcastSourceInfoDetailsController.this.triggerUpdateBroadcastSource();
                }
            };
            this.mGroupOp = false;
            this.mScanAssistGroupOpDialog = BroadcastScanAssistanceUtils.showAssistanceGroupOptionsDialog(this.mContext, this.mScanAssistGroupOpDialog, r6, r7, string2, Html.fromHtml(string));
            return;
        }
        this.mGroupOp = false;
        triggerUpdateBroadcastSource();
    }

    /* access modifiers changed from: protected */
    public void init(PreferenceScreen preferenceScreen) {
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mSourceInfoContainer = preferenceCategory;
        this.mSourceIdPref = preferenceCategory.findPreference("broadcast_si_sourceId");
        this.mSourceDevicePref = this.mSourceInfoContainer.findPreference("broadcast_si_source_address");
        this.mSourceEncStatusPref = this.mSourceInfoContainer.findPreference("broadcast_si_encryption_state");
        this.mSourceMetadataPref = this.mSourceInfoContainer.findPreference("broadcast_si_metadata");
        this.mSourceMetadataSyncStatusPref = this.mSourceInfoContainer.findPreference("broadcast_si_metadata_state");
        if (this.mPAsyncCtrlNeeded) {
            SwitchPreference switchPreference = (SwitchPreference) this.mSourceInfoContainer.findPreference("broadcast_si_enable_metadata_sync");
            this.mSourceMetadataSyncSwitchPref = switchPreference;
            if (switchPreference != null) {
                switchPreference.setOnPreferenceClickListener(this);
            }
        }
        MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) this.mSourceInfoContainer.findPreference("broadcast_si_audio_state");
        this.mSourceAudioSyncStatusPref = multiSelectListPreference;
        if (multiSelectListPreference != null) {
            multiSelectListPreference.setOnPreferenceChangeListener(this);
        }
        SwitchPreference switchPreference2 = (SwitchPreference) this.mSourceInfoContainer.findPreference("broadcast_si_enable_audio_sync");
        this.mSourceAudioSyncSwitchPref = switchPreference2;
        if (switchPreference2 != null) {
            switchPreference2.setOnPreferenceClickListener(this);
        }
        EditTextPreference editTextPreference = (EditTextPreference) this.mSourceInfoContainer.findPreference("update_broadcast_code");
        this.mSourceUpdateBcastCodePref = editTextPreference;
        if (editTextPreference != null) {
            editTextPreference.setOnPreferenceClickListener(this);
            this.mSourceUpdateBcastCodePref.setOnPreferenceChangeListener(this);
        }
        this.mSourceUpdateSourceInfoPref = ((ActionButtonsPreference) this.mSourceInfoContainer.findPreference("bcast_si_update_button")).setButton1Text(R$string.update_sourceinfo_btn_txt).setButton1Enabled(false).setButton1OnClickListener(new C0771x79c59d7e(this)).setButton2Text(R$string.remove_sourceinfo_btn_txt).setButton2Icon(R$drawable.ic_settings_close).setButton2Enabled(false).setButton2OnClickListener(new C0772x79c59d7f(this));
        refresh();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$0(View view) {
        onUpdateBroadcastSourceInfoPressed();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1(View view) {
        onRemoveBroadcastSourceInfoPressed();
    }

    public void onDeviceAttributesChanged() {
        Map<Integer, BleBroadcastSourceInfo> allBleBroadcastreceiverStates = this.mVendorCachedDevice.getAllBleBroadcastreceiverStates();
        if (allBleBroadcastreceiverStates != null) {
            for (Map.Entry next : allBleBroadcastreceiverStates.entrySet()) {
                BleBroadcastSourceInfo bleBroadcastSourceInfo = (BleBroadcastSourceInfo) next.getValue();
                if (((Integer) next.getKey()).intValue() == this.mSourceInfoIndex) {
                    BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":matching source Info");
                    if (bleBroadcastSourceInfo.isEmptyEntry()) {
                        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":source info seem to be removed");
                        this.mBleBroadcastSourceInfo = bleBroadcastSourceInfo;
                    } else if (!bleBroadcastSourceInfo.equals(this.mBleBroadcastSourceInfo)) {
                        this.mBleBroadcastSourceInfo = bleBroadcastSourceInfo;
                        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":Update in Broadcast Source Information");
                    } else {
                        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":No Update to Source Information values");
                    }
                } else {
                    BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":Ignore this case");
                }
            }
            refresh();
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        String key = preference.getKey();
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":onPreferenceChange" + obj);
        if (key.equals("update_broadcast_code")) {
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            this.isBroadcastPINUpdated = true;
            this.mBroadcastCode = (String) obj;
        } else if (key.equals("broadcast_si_audio_state")) {
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", ">>Checked:" + obj);
            MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) preference;
            multiSelectListPreference.getEntries();
            multiSelectListPreference.getValues();
            Set<String> set = (Set) obj;
            String[] strArr = new String[set.size()];
            int i = 0;
            for (String str : set) {
                strArr[i] = str;
                for (int i2 = 0; i2 < this.mBisIndicies.size(); i2++) {
                    if (str.equals(this.mBisIndicies.get(i2).getDescription())) {
                        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", "Selected: value[" + i2 + "]- " + str);
                        if (this.mBisIndicies.get(i2).getStatus()) {
                            this.mBisIndicies.get(i2).setStatus(false);
                        } else {
                            this.mBisIndicies.get(i2).setStatus(true);
                        }
                    }
                }
                BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", "value[" + i + "]- " + str);
                i++;
            }
            this.mIsValueChanged = true;
        }
        this.mIsButtonRefreshOnly = true;
        refresh();
        return true;
    }

    public boolean onPreferenceClick(Preference preference) {
        SwitchPreference switchPreference;
        String key = preference.getKey();
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":onPreferenceClick");
        this.mIsValueChanged = true;
        if (this.mPAsyncCtrlNeeded && key.equals("broadcast_si_enable_metadata_sync")) {
            SwitchPreference switchPreference2 = (SwitchPreference) preference;
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":Meta data sync state: " + switchPreference2.isChecked());
            if (switchPreference2.isChecked()) {
                this.mMetadataSyncState = 2;
            } else {
                this.mMetadataSyncState = 0;
            }
            SwitchPreference switchPreference3 = this.mSourceAudioSyncSwitchPref;
            if (switchPreference3 != null) {
                if (switchPreference3.isChecked()) {
                    this.mAudioSyncState = 1;
                } else {
                    this.mAudioSyncState = 0;
                }
            }
        }
        if (key.equals("broadcast_si_enable_audio_sync")) {
            SwitchPreference switchPreference4 = (SwitchPreference) preference;
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":Audio sync state:  " + switchPreference4.isChecked());
            if (switchPreference4.isChecked()) {
                this.mAudioSyncState = 1;
            } else {
                this.mAudioSyncState = 0;
            }
            if (this.mPAsyncCtrlNeeded && (switchPreference = this.mSourceMetadataSyncSwitchPref) != null) {
                if (switchPreference.isChecked()) {
                    this.mMetadataSyncState = 1;
                } else {
                    this.mMetadataSyncState = 0;
                }
            }
        } else if (key.equals("update_broadcast_code")) {
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":>>Pin code updated:  " + editTextPreference.getText());
            this.mIsValueChanged = false;
            this.isBroadcastPINUpdated = true;
            this.mBroadcastCode = editTextPreference.getText();
        } else {
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":unhandled preference");
            this.mIsValueChanged = false;
        }
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":onPreferenceClick" + this.mBleBroadcastSourceInfo);
        this.mIsButtonRefreshOnly = true;
        refresh();
        return true;
    }

    public void onPause() {
        super.onPause();
        this.mCachedDevice.unregisterCallback(this);
    }

    public void onResume() {
        super.onResume();
        this.mCachedDevice.registerCallback(this);
    }

    private boolean isPinUpdatedNeeded() {
        boolean z = false;
        if (BroadcastScanAssistanceUtils.isLocalDevice(this.mBleBroadcastSourceInfo.getSourceDevice())) {
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", "Local Device, Dont allow User to update PWD");
            return false;
        }
        if (this.mBleBroadcastSourceInfo.getEncryptionStatus() == 1) {
            z = true;
        }
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", "isPinUpdatedNeeded return" + z);
        return z;
    }

    /* access modifiers changed from: protected */
    public void refresh() {
        String str;
        int i;
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":refresh: " + this.mBleBroadcastSourceInfo + " mSourceIndex" + this.mSourceInfoIndex);
        this.mSourceIdPref.setSummary((CharSequence) String.valueOf(this.mBleBroadcastSourceInfo.getSourceId()));
        BluetoothDevice sourceDevice = this.mBleBroadcastSourceInfo.getSourceDevice();
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        byte[] bArr = null;
        if (sourceDevice == null || defaultAdapter == null) {
            str = null;
        } else {
            if (defaultAdapter.getAddress().equals(sourceDevice.getAddress())) {
                str = defaultAdapter.getName() + "(Self)";
            } else {
                str = sourceDevice.getAlias();
            }
            if (str == null) {
                str = String.valueOf(sourceDevice.getAddress());
            }
        }
        if (str == null || str.equals("00:00:00:00:00:00")) {
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":NULL source device");
            str = "EMPTY_ENTRY";
        }
        this.mSourceDevicePref.setSummary((CharSequence) str);
        this.mSourceEncStatusPref.setSummary((CharSequence) getEncryptionStatusString(this.mBleBroadcastSourceInfo.getEncryptionStatus()));
        if (this.mBleBroadcastSourceInfo.isEmptyEntry()) {
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":Source Information seem to be Empty");
            if (this.mPAsyncCtrlNeeded) {
                this.mSourceMetadataSyncSwitchPref.setEnabled(false);
            }
            this.mSourceAudioSyncSwitchPref.setEnabled(false);
            this.mSourceUpdateBcastCodePref.setEnabled(false);
            this.mSourceUpdateSourceInfoPref.setButton1Enabled(false);
            this.mSourceUpdateSourceInfoPref.setButton2Enabled(false);
            this.mSourceAudioSyncStatusPref.setEnabled(false);
            this.mIsValueChanged = false;
            return;
        }
        if (this.mPAsyncCtrlNeeded) {
            this.mSourceMetadataSyncSwitchPref.setEnabled(true);
        }
        this.mSourceAudioSyncSwitchPref.setEnabled(true);
        this.mSourceUpdateBcastCodePref.setEnabled(isPinUpdatedNeeded());
        if (!this.mIsButtonRefreshOnly) {
            this.mSourceMetadataSyncStatusPref.setSummary((CharSequence) getMetadataSyncStatusString(this.mBleBroadcastSourceInfo.getMetadataSyncState()));
            this.mSourceAudioSyncStatusPref.setSummary((CharSequence) getAudioSyncStatusString(this.mBleBroadcastSourceInfo.getAudioSyncState()));
            List<BleBroadcastSourceChannel> broadcastChannelsSyncStatus = this.mBleBroadcastSourceInfo.getBroadcastChannelsSyncStatus();
            this.mBisIndicies = broadcastChannelsSyncStatus;
            if (broadcastChannelsSyncStatus != null) {
                String[] strArr = new String[broadcastChannelsSyncStatus.size()];
                boolean[] zArr = new boolean[this.mBisIndicies.size()];
                HashSet hashSet = new HashSet();
                for (int i2 = 0; i2 < this.mBisIndicies.size(); i2++) {
                    strArr[i2] = this.mBisIndicies.get(i2).getDescription();
                    zArr[i2] = this.mBisIndicies.get(i2).getStatus();
                }
                hashSet.addAll(Arrays.asList(strArr));
                this.mSourceAudioSyncStatusPref.setEntries(strArr);
                this.mSourceAudioSyncStatusPref.setEntryValues(strArr);
                this.mSourceAudioSyncStatusPref.setValues(hashSet);
            }
            if (this.mPAsyncCtrlNeeded) {
                this.mSourceMetadataSyncSwitchPref.setChecked(this.mBleBroadcastSourceInfo.getMetadataSyncState() == 2);
            }
            this.mSourceAudioSyncSwitchPref.setChecked(this.mBleBroadcastSourceInfo.getAudioSyncState() == 1);
            if (this.mBisIndicies != null) {
                i = 0;
                while (true) {
                    if (i >= this.mBisIndicies.size()) {
                        break;
                    } else if (this.mBisIndicies.get(i).getStatus()) {
                        break;
                    } else {
                        i++;
                    }
                }
            }
            i = -1;
            if (i != -1) {
                bArr = this.mBisIndicies.get(i).getMetadata();
            }
            if (bArr != null) {
                String str2 = new String(bArr);
                BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoDetailsController", this.mSourceInfoIndex + ":Metadata:" + str2);
                this.mSourceMetadataPref.setSummary((CharSequence) str2);
            } else {
                this.mSourceMetadataPref.setSummary((CharSequence) "NONE");
            }
            this.mSourceUpdateSourceInfoPref.setButton2Enabled(true);
        }
        if (this.mIsValueChanged || this.isBroadcastPINUpdated) {
            this.mSourceUpdateSourceInfoPref.setButton1Enabled(true);
        } else {
            this.mSourceUpdateSourceInfoPref.setButton1Enabled(false);
        }
        this.mIsButtonRefreshOnly = false;
    }
}
