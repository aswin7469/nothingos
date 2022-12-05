package com.android.settings.bluetooth;

import android.bluetooth.BleBroadcastAudioScanAssistCallback;
import android.bluetooth.BleBroadcastAudioScanAssistManager;
import android.bluetooth.BleBroadcastSourceChannel;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.BidiFormatter;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settingslib.bluetooth.BCProfile;
import com.android.settingslib.bluetooth.BluetoothDeviceFilter;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import com.android.settingslib.bluetooth.VendorCachedBluetoothDevice;
import com.android.settingslib.widget.FooterPreference;
import java.util.List;
/* loaded from: classes.dex */
public class BluetoothSADetail extends DeviceListPreferenceFragment {
    BluetoothProgressCategory mAvailableDevicesCategory;
    private CachedBluetoothDevice mCachedDevice;
    Context mContext;
    FooterPreference mFooterPreference;
    private boolean mInitialScanStarted;
    protected LocalBluetoothProfileManager mProfileManager;
    private AlertDialog mScanAssistDetailsDialog;
    BleBroadcastAudioScanAssistManager mScanAssistManager;
    Preference mScanDelegatorName;
    CachedBluetoothDevice clickedDevice = null;
    String mBroadcastPinCode = null;
    boolean mScanning = true;
    boolean mGroupOperation = false;
    AlertDialog mCommonMsgDialog = null;
    BleBroadcastAudioScanAssistCallback mScanAssistCallback = new BleBroadcastAudioScanAssistCallback() { // from class: com.android.settings.bluetooth.BluetoothSADetail.1
        DialogInterface.OnClickListener commonMessageListener = new DialogInterface.OnClickListener() { // from class: com.android.settings.bluetooth.BluetoothSADetail.1.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                BroadcastScanAssistanceUtils.debug("BluetoothSADetail", ">>OK clicked");
                AlertDialog alertDialog = BluetoothSADetail.this.mCommonMsgDialog;
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                BluetoothSADetail.this.finish();
            }
        };

        public void onBleBroadcastSourceFound(ScanResult scanResult) {
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "onBleBroadcastSourceFound" + scanResult.getDevice());
            CachedBluetoothDevice findDevice = BluetoothSADetail.this.mLocalManager.getCachedDeviceManager().findDevice(scanResult.getDevice());
            if (findDevice != null) {
                BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "seems like CachedDevice entry already present for this device");
            } else {
                findDevice = BluetoothSADetail.this.mLocalManager.getCachedDeviceManager().addDevice(scanResult.getDevice());
                if (scanResult.getDevice().getAddress().equals(BluetoothAdapter.getDefaultAdapter().getAddress())) {
                    BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "Self DEVICE:");
                } else {
                    ScanRecord scanRecord = scanResult.getScanRecord();
                    if (scanRecord != null && scanRecord.getDeviceName() != null) {
                        String deviceName = scanRecord.getDeviceName();
                        BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "setting name as " + deviceName);
                        findDevice.setName(deviceName);
                    }
                }
            }
            if (BluetoothSADetail.this.mDevicePreferenceMap.get(findDevice) != null) {
                BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "Preference is already present" + scanResult.getDevice());
            } else if (BluetoothSADetail.this.mBluetoothAdapter.getState() != 12) {
            } else {
                BluetoothSADetail.this.createDevicePreference(findDevice);
                VendorCachedBluetoothDevice.getVendorCachedBluetoothDevice(findDevice, BluetoothSADetail.this.mProfileManager).setScanResult(scanResult);
            }
        }

        public void onBleBroadcastSourceSelected(BluetoothDevice bluetoothDevice, int i, List<BleBroadcastSourceChannel> list) {
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "onBleBroadcastSourceSelected" + i + "sel indicies:" + list);
            if (i != 0) {
                BluetoothSADetail.this.getBluetoothName(bluetoothDevice);
                BluetoothSADetail bluetoothSADetail = BluetoothSADetail.this;
                bluetoothSADetail.mCommonMsgDialog = BroadcastScanAssistanceUtils.showScanAssistError(bluetoothSADetail.mContext, bluetoothDevice.getName(), BluetoothSADetail.this.getSourceSelectionErrMessage(i), this.commonMessageListener);
                return;
            }
            BluetoothSADetail.this.launchSyncAndBroadcastIndexOptions(list);
        }

        public void onBleBroadcastAudioSourceAdded(BluetoothDevice bluetoothDevice, byte b, int i) {
            BleBroadcastAudioScanAssistManager bleBroadcastAudioScanAssistManager;
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "onBleBroadcastAudioSourceAdded: rcvr:" + bluetoothDevice + "status:" + i + "srcId" + ((int) b));
            if (i != 0) {
                String bluetoothName = BluetoothSADetail.this.getBluetoothName(bluetoothDevice);
                BluetoothSADetail bluetoothSADetail = BluetoothSADetail.this;
                bluetoothSADetail.mCommonMsgDialog = BroadcastScanAssistanceUtils.showScanAssistError(bluetoothSADetail.mContext, bluetoothName, bluetoothSADetail.getSourceAdditionErrMessage(i), this.commonMessageListener);
                return;
            }
            BluetoothSADetail bluetoothSADetail2 = BluetoothSADetail.this;
            if (bluetoothSADetail2.mGroupOperation) {
                String bluetoothName2 = bluetoothSADetail2.getBluetoothName(bluetoothDevice);
                BluetoothSADetail bluetoothSADetail3 = BluetoothSADetail.this;
                bluetoothSADetail3.mCommonMsgDialog = BroadcastScanAssistanceUtils.showScanAssistError(bluetoothSADetail3.mContext, bluetoothName2, R.string.bluetooth_source_added_message, this.commonMessageListener);
            }
            BluetoothSADetail bluetoothSADetail4 = BluetoothSADetail.this;
            String str = bluetoothSADetail4.mBroadcastPinCode;
            if (str != null) {
                if (i == 0 && (bleBroadcastAudioScanAssistManager = bluetoothSADetail4.mScanAssistManager) != null) {
                    bleBroadcastAudioScanAssistManager.setBroadcastCode(b, str, bluetoothSADetail4.mGroupOperation);
                }
                BluetoothSADetail.this.mBroadcastPinCode = null;
            }
            BluetoothSADetail.this.finish();
        }

        public void onBleBroadcastAudioSourceUpdated(BluetoothDevice bluetoothDevice, byte b, int i) {
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "onBleBroadcastAudioSourceUpdated: rcvr:" + bluetoothDevice + "status:" + i + "srcId" + ((int) b));
            if (i != 0) {
                String bluetoothName = BluetoothSADetail.this.getBluetoothName(bluetoothDevice);
                BluetoothSADetail bluetoothSADetail = BluetoothSADetail.this;
                bluetoothSADetail.mCommonMsgDialog = BroadcastScanAssistanceUtils.showScanAssistError(bluetoothSADetail.mContext, bluetoothName, bluetoothSADetail.getSourceUpdateErrMessage(i), this.commonMessageListener);
            }
        }

        public void onBleBroadcastPinUpdated(BluetoothDevice bluetoothDevice, byte b, int i) {
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "onBleBroadcastPinUpdated: rcvr:" + bluetoothDevice + "status:" + i + "srcId" + ((int) b));
            if (i != 0) {
                String bluetoothName = BluetoothSADetail.this.getBluetoothName(bluetoothDevice);
                BluetoothSADetail bluetoothSADetail = BluetoothSADetail.this;
                bluetoothSADetail.mCommonMsgDialog = BroadcastScanAssistanceUtils.showScanAssistError(bluetoothSADetail.mContext, bluetoothName, R.string.bluetooth_source_setpin_error_message, this.commonMessageListener);
            }
        }

        public void onBleBroadcastAudioSourceRemoved(BluetoothDevice bluetoothDevice, byte b, int i) {
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "onBleBroadcastAudioSourceRemoved: rcvr:" + bluetoothDevice + "status:" + i + "srcId" + ((int) b));
            if (i != 0) {
                String bluetoothName = BluetoothSADetail.this.getBluetoothName(bluetoothDevice);
                BluetoothSADetail bluetoothSADetail = BluetoothSADetail.this;
                bluetoothSADetail.mCommonMsgDialog = BroadcastScanAssistanceUtils.showScanAssistError(bluetoothSADetail.mContext, bluetoothName, bluetoothSADetail.getSourceRemovalErrMessage(i), this.commonMessageListener);
            }
        }
    };

    @Override // com.android.settings.bluetooth.DeviceListPreferenceFragment
    public String getDeviceListKey() {
        return "available_audio_sources";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "BluetoothSADetail";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1018;
    }

    @Override // com.android.settings.bluetooth.DeviceListPreferenceFragment, com.android.settingslib.bluetooth.BluetoothCallback
    public void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getBluetoothName(BluetoothDevice bluetoothDevice) {
        String str = "Scan Delegator";
        if (bluetoothDevice != null) {
            String alias = bluetoothDevice.getAlias();
            if (TextUtils.isEmpty(alias)) {
                alias = bluetoothDevice.getAddress();
            }
            if (alias != null) {
                str = alias;
            }
        }
        BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "getBluetoothName returns" + str);
        return str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSourceSelectionErrMessage(int i) {
        int i2 = R.string.bluetooth_source_selection_error_message;
        switch (i) {
            case 4:
            case 6:
            default:
                return i2;
            case 5:
            case 7:
                return R.string.bluetooth_source_selection_error_src_unavail_message;
            case 8:
                return R.string.bluetooth_source_dup_addition_error_message;
            case 9:
                return R.string.bluetooth_source_no_empty_slot_error_message;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSourceAdditionErrMessage(int i) {
        int i2 = R.string.bluetooth_source_addition_error_message;
        if (i != 8) {
            return i != 9 ? i2 : R.string.bluetooth_source_no_empty_slot_error_message;
        }
        return R.string.bluetooth_source_dup_addition_error_message;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSourceRemovalErrMessage(int i) {
        int i2 = R.string.bluetooth_source_removal_error_message;
        if (i != 2) {
            if (i == 4) {
                return R.string.bluetooth_source_remove_invalid_src_id;
            }
            return i != 16 ? i2 : R.string.bluetooth_source_remove_invalid_group_op;
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSourceUpdateErrMessage(int i) {
        int i2 = R.string.bluetooth_source_update_error_message;
        if (i != 2) {
            if (i == 4) {
                return R.string.bluetooth_source_update_invalid_src_id;
            }
            return i != 16 ? i2 : R.string.bluetooth_source_update_invalid_group_op;
        }
        return i2;
    }

    public BluetoothSADetail() {
        super("no_config_bluetooth");
    }

    @Override // com.android.settings.bluetooth.DeviceListPreferenceFragment
    void createDevicePreference(CachedBluetoothDevice cachedBluetoothDevice) {
        if (this.mDeviceListGroup == null) {
            Log.w("BluetoothSADetail", "Trying to create a device preference before the list group/category exists!");
            return;
        }
        String address = cachedBluetoothDevice.getDevice().getAddress();
        BluetoothDevicePreference bluetoothDevicePreference = (BluetoothDevicePreference) getCachedPreference(address);
        if (bluetoothDevicePreference == null) {
            bluetoothDevicePreference = new BluetoothDevicePreference(getPrefContext(), cachedBluetoothDevice, true, 2);
            bluetoothDevicePreference.setKey(address);
            this.mDeviceListGroup.addPreference(bluetoothDevicePreference);
        }
        initDevicePreference(bluetoothDevicePreference);
        Log.w("BluetoothSADetail", "adding" + cachedBluetoothDevice + "to the Pref map");
        this.mDevicePreferenceMap.put(cachedBluetoothDevice, bluetoothDevicePreference);
    }

    @Override // com.android.settings.dashboard.RestrictedDashboardFragment, com.android.settings.SettingsPreferenceFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mInitialScanStarted = false;
    }

    @Override // com.android.settings.bluetooth.DeviceListPreferenceFragment, com.android.settings.dashboard.DashboardFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStart() {
        BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "OnStart Called");
        super.onStart();
        if (this.mLocalManager == null) {
            Log.e("BluetoothSADetail", "Bluetooth is not supported on this device");
            return;
        }
        updateContent(this.mBluetoothAdapter.getState());
        this.mAvailableDevicesCategory.setProgress(this.mBluetoothAdapter.isDiscovering());
        if (this.mScanAssistManager != null) {
            return;
        }
        if (this.mProfileManager == null) {
            this.mProfileManager = this.mLocalManager.getProfileManager();
        }
        BleBroadcastAudioScanAssistManager bSAManager = ((BCProfile) this.mProfileManager.getBCProfile()).getBSAManager(this.mCachedDevice.getDevice(), this.mScanAssistCallback);
        this.mScanAssistManager = bSAManager;
        if (bSAManager != null) {
            return;
        }
        Log.e("BluetoothSADetail", "On Start: not able to instantiate scanAssistManager");
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "OnAttach Called");
        super.onAttach(context);
        this.mContext = context;
        String string = getArguments().getString("device_address");
        boolean z = true;
        if (getArguments().getShort("group_op") != 1) {
            z = false;
        }
        this.mGroupOperation = z;
        BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(string);
        if (this.mLocalManager == null) {
            Log.e("BluetoothSADetail", "Local mgr is NULL");
            LocalBluetoothManager localBtManager = Utils.getLocalBtManager(getActivity());
            this.mLocalManager = localBtManager;
            if (localBtManager == null) {
                Log.e("BluetoothSADetail", "Bluetooth is not supported on this device");
                return;
            }
        }
        CachedBluetoothDevice findDevice = this.mLocalManager.getCachedDeviceManager().findDevice(remoteDevice);
        this.mCachedDevice = findDevice;
        if (findDevice == null) {
            return;
        }
        LocalBluetoothProfileManager profileManager = this.mLocalManager.getProfileManager();
        this.mProfileManager = profileManager;
        BleBroadcastAudioScanAssistManager bSAManager = ((BCProfile) profileManager.getBCProfile()).getBSAManager(this.mCachedDevice.getDevice(), this.mScanAssistCallback);
        this.mScanAssistManager = bSAManager;
        if (bSAManager != null) {
            return;
        }
        Log.e("BluetoothSADetail", "not able to instantiate scanAssistManager");
    }

    @Override // com.android.settings.bluetooth.DeviceListPreferenceFragment, com.android.settings.dashboard.DashboardFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        if (this.mLocalManager == null) {
            Log.e("BluetoothSADetail", "Bluetooth is not supported on this device");
            return;
        }
        disableScanning();
        this.mDevicePreferenceMap.clear();
        this.mScanAssistManager = null;
    }

    @Override // com.android.settings.bluetooth.DeviceListPreferenceFragment
    void initPreferencesFromPreferenceScreen() {
        Preference findPreference = findPreference("bt_bcast_rcvr_device");
        this.mScanDelegatorName = findPreference;
        findPreference.setSelectable(false);
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        if (cachedBluetoothDevice == null) {
            this.mScanDelegatorName.setSummary("Scan Delegator");
        } else {
            this.mScanDelegatorName.setSummary(getBluetoothName(cachedBluetoothDevice.getDevice()));
        }
        this.mAvailableDevicesCategory = (BluetoothProgressCategory) findPreference("available_audio_sources");
        FooterPreference footerPreference = (FooterPreference) findPreference("footer_preference");
        this.mFooterPreference = footerPreference;
        footerPreference.setSelectable(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.settings.bluetooth.DeviceListPreferenceFragment
    public void enableScanning() {
        if (!this.mInitialScanStarted) {
            if (this.mAvailableDevicesCategory != null) {
                removeAllDevices();
            }
            this.mLocalManager.getCachedDeviceManager().clearNonBondedDevices();
            this.mInitialScanStarted = true;
        }
        if (this.mScanAssistManager != null) {
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "call searchforLeAudioBroadcasters");
            this.mScanAssistManager.searchforLeAudioBroadcasters();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.settings.bluetooth.DeviceListPreferenceFragment
    public void disableScanning() {
        if (this.mScanAssistManager == null || !this.mScanning) {
            return;
        }
        BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "call stopSearchforLeAudioBroadcasters");
        this.mScanAssistManager.stopSearchforLeAudioBroadcasters();
        this.mScanning = false;
    }

    void launchSyncAndBroadcastIndexOptions(final List<BleBroadcastSourceChannel> list) {
        String string;
        Context context = getContext();
        FragmentActivity activity = getActivity();
        if (!isAdded() || activity == null) {
            return;
        }
        final View inflate = getLayoutInflater().inflate(R.layout.select_source_prompt, (ViewGroup) null);
        CachedBluetoothDevice cachedBluetoothDevice = this.clickedDevice;
        String name = cachedBluetoothDevice != null ? cachedBluetoothDevice.getName() : null;
        if (TextUtils.isEmpty(name)) {
            name = context.getString(R.string.bluetooth_device);
        }
        if (this.mGroupOperation) {
            string = context.getString(R.string.bluetooth_grp_source_selection_options_detail, name);
        } else {
            string = context.getString(R.string.bluetooth_source_selection_options_detail, name);
        }
        String string2 = context.getString(R.string.bluetooth_source_selection_options_detail_title);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.android.settings.bluetooth.BluetoothSADetail.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                BroadcastScanAssistanceUtils.debug("BluetoothSADetail", ">>Cancel clicked");
                BluetoothSADetail.this.finish();
            }
        };
        DialogInterface.OnClickListener onClickListener2 = new DialogInterface.OnClickListener() { // from class: com.android.settings.bluetooth.BluetoothSADetail.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                CachedBluetoothDevice cachedBluetoothDevice2;
                if (BluetoothSADetail.this.clickedDevice == null) {
                    Log.w("BluetoothSADetail", "Ignore as there is no clicked device");
                }
                if (BluetoothSADetail.this.clickedDevice.getAddress().equals(BluetoothSADetail.this.mBluetoothAdapter.getAddress())) {
                    BroadcastScanAssistanceUtils.debug("BluetoothSADetail", ">>Local Adapter");
                    BluetoothSADetail.this.mBroadcastPinCode = null;
                } else {
                    BluetoothSADetail.this.mBroadcastPinCode = ((EditText) inflate.findViewById(R.id.broadcastPINcode)).getText().toString();
                    BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "broadcastPinCode: " + BluetoothSADetail.this.mBroadcastPinCode);
                    if (TextUtils.isEmpty(BluetoothSADetail.this.mBroadcastPinCode)) {
                        BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "Empty broacast PinCode");
                        BluetoothSADetail.this.mBroadcastPinCode = null;
                    }
                }
                BluetoothSADetail bluetoothSADetail = BluetoothSADetail.this;
                BleBroadcastAudioScanAssistManager bleBroadcastAudioScanAssistManager = bluetoothSADetail.mScanAssistManager;
                if (bleBroadcastAudioScanAssistManager == null || (cachedBluetoothDevice2 = bluetoothSADetail.clickedDevice) == null) {
                    return;
                }
                bleBroadcastAudioScanAssistManager.addBroadcastSource(cachedBluetoothDevice2.getDevice(), 2, list, BluetoothSADetail.this.mGroupOperation);
            }
        };
        EditText editText = (EditText) inflate.findViewById(R.id.broadcastPINcode);
        CachedBluetoothDevice cachedBluetoothDevice2 = this.clickedDevice;
        if (cachedBluetoothDevice2 != null && cachedBluetoothDevice2.getAddress().equals(this.mBluetoothAdapter.getAddress())) {
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "Local Adapter");
            this.mBroadcastPinCode = null;
            editText.setVisibility(4);
            if (this.mGroupOperation) {
                string = context.getString(R.string.bluetooth_col_grp_source_selection_options_detail, name);
            } else {
                string = context.getString(R.string.bluetooth_col_source_selection_options_detail, name);
            }
        }
        this.mScanAssistDetailsDialog = BroadcastScanAssistanceUtils.showScanAssistDetailsDialog(context, this.mScanAssistDetailsDialog, onClickListener2, onClickListener, string2, Html.fromHtml(string), inflate);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.settings.bluetooth.DeviceListPreferenceFragment
    public void onDevicePreferenceClick(BluetoothDevicePreference bluetoothDevicePreference) {
        disableScanning();
        CachedBluetoothDevice bluetoothDevice = bluetoothDevicePreference.getBluetoothDevice();
        this.clickedDevice = bluetoothDevice;
        VendorCachedBluetoothDevice vendorCachedBluetoothDevice = VendorCachedBluetoothDevice.getVendorCachedBluetoothDevice(bluetoothDevice, this.mProfileManager);
        if (this.mScanAssistManager != null) {
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "calling selectAudioSource");
            this.mScanAssistManager.selectBroadcastSource(vendorCachedBluetoothDevice.getScanResult(), this.mGroupOperation);
        }
    }

    void updateContent(int i) {
        if (i == 10) {
            finish();
        } else if (i != 12) {
        } else {
            this.mDevicePreferenceMap.clear();
            addDeviceCategory(this.mAvailableDevicesCategory, R.string.bluetooth_preference_found_media_devices, BluetoothDeviceFilter.ALL_FILTER, false);
            updateFooterPreference(this.mFooterPreference);
            enableScanning();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.settings.bluetooth.DeviceListPreferenceFragment
    public void updateFooterPreference(Preference preference) {
        preference.setTitle(getString(R.string.bluetooth_footer_mac_message, BidiFormatter.getInstance().unicodeWrap(this.mCachedDevice.getAddress())));
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onBluetoothStateChanged(int i) {
        super.onBluetoothStateChanged(i);
        updateContent(i);
        if (i == 12) {
            showBluetoothTurnedOnToast();
        }
    }

    @Override // com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return R.string.help_url_bluetooth;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.bluetooth_search_bcast_sources;
    }

    void showBluetoothTurnedOnToast() {
        Toast.makeText(getContext(), R.string.connected_device_bluetooth_turned_on_toast, 0).show();
    }
}
