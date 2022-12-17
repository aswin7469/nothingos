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
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settingslib.bluetooth.BCProfile;
import com.android.settingslib.bluetooth.BluetoothDeviceFilter;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import com.android.settingslib.bluetooth.VendorCachedBluetoothDevice;
import com.android.settingslib.widget.FooterPreference;
import java.util.List;

public class BluetoothSADetail extends DeviceListPreferenceFragment {
    CachedBluetoothDevice clickedDevice = null;
    BluetoothProgressCategory mAvailableDevicesCategory;
    String mBroadcastPinCode = null;
    private CachedBluetoothDevice mCachedDevice;
    AlertDialog mCommonMsgDialog = null;
    Context mContext;
    FooterPreference mFooterPreference;
    boolean mGroupOperation = false;
    private boolean mInitialScanStarted;
    protected LocalBluetoothProfileManager mProfileManager;
    BleBroadcastAudioScanAssistCallback mScanAssistCallback = new BleBroadcastAudioScanAssistCallback() {
        DialogInterface.OnClickListener commonMessageListener = new DialogInterface.OnClickListener() {
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
                    if (!(scanRecord == null || scanRecord.getDeviceName() == null)) {
                        String deviceName = scanRecord.getDeviceName();
                        BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "setting name as " + deviceName);
                        findDevice.setName(deviceName);
                    }
                }
            }
            if (BluetoothSADetail.this.mDevicePreferenceMap.get(findDevice) != null) {
                BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "Preference is already present" + scanResult.getDevice());
            } else if (BluetoothSADetail.this.mBluetoothAdapter.getState() == 12) {
                BluetoothSADetail.this.createDevicePreference(findDevice);
                VendorCachedBluetoothDevice.getVendorCachedBluetoothDevice(findDevice, BluetoothSADetail.this.mProfileManager).setScanResult(scanResult);
            }
        }

        public void onBleBroadcastSourceSelected(BluetoothDevice bluetoothDevice, int i, List<BleBroadcastSourceChannel> list) {
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "onBleBroadcastSourceSelected" + i + "sel indicies:" + list);
            if (i == 0) {
                BluetoothSADetail.this.launchSyncAndBroadcastIndexOptions(list);
                return;
            }
            String unused = BluetoothSADetail.this.getBluetoothName(bluetoothDevice);
            BluetoothSADetail bluetoothSADetail = BluetoothSADetail.this;
            bluetoothSADetail.mCommonMsgDialog = BroadcastScanAssistanceUtils.showScanAssistError(bluetoothSADetail.mContext, bluetoothDevice.getName(), BluetoothSADetail.this.getSourceSelectionErrMessage(i), this.commonMessageListener);
        }

        public void onBleBroadcastAudioSourceAdded(BluetoothDevice bluetoothDevice, byte b, int i) {
            BleBroadcastAudioScanAssistManager bleBroadcastAudioScanAssistManager;
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "onBleBroadcastAudioSourceAdded: rcvr:" + bluetoothDevice + "status:" + i + "srcId" + b);
            if (i == 0) {
                BluetoothSADetail bluetoothSADetail = BluetoothSADetail.this;
                if (bluetoothSADetail.mGroupOperation) {
                    String r5 = bluetoothSADetail.getBluetoothName(bluetoothDevice);
                    BluetoothSADetail bluetoothSADetail2 = BluetoothSADetail.this;
                    bluetoothSADetail2.mCommonMsgDialog = BroadcastScanAssistanceUtils.showScanAssistError(bluetoothSADetail2.mContext, r5, R$string.bluetooth_source_added_message, this.commonMessageListener);
                }
                BluetoothSADetail bluetoothSADetail3 = BluetoothSADetail.this;
                String str = bluetoothSADetail3.mBroadcastPinCode;
                if (str != null) {
                    if (i == 0 && (bleBroadcastAudioScanAssistManager = bluetoothSADetail3.mScanAssistManager) != null) {
                        bleBroadcastAudioScanAssistManager.setBroadcastCode(b, str, bluetoothSADetail3.mGroupOperation);
                    }
                    BluetoothSADetail.this.mBroadcastPinCode = null;
                }
                BluetoothSADetail.this.finish();
                return;
            }
            String r52 = BluetoothSADetail.this.getBluetoothName(bluetoothDevice);
            BluetoothSADetail bluetoothSADetail4 = BluetoothSADetail.this;
            bluetoothSADetail4.mCommonMsgDialog = BroadcastScanAssistanceUtils.showScanAssistError(bluetoothSADetail4.mContext, r52, bluetoothSADetail4.getSourceAdditionErrMessage(i), this.commonMessageListener);
        }

        public void onBleBroadcastAudioSourceUpdated(BluetoothDevice bluetoothDevice, byte b, int i) {
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "onBleBroadcastAudioSourceUpdated: rcvr:" + bluetoothDevice + "status:" + i + "srcId" + b);
            if (i != 0) {
                String r3 = BluetoothSADetail.this.getBluetoothName(bluetoothDevice);
                BluetoothSADetail bluetoothSADetail = BluetoothSADetail.this;
                bluetoothSADetail.mCommonMsgDialog = BroadcastScanAssistanceUtils.showScanAssistError(bluetoothSADetail.mContext, r3, bluetoothSADetail.getSourceUpdateErrMessage(i), this.commonMessageListener);
            }
        }

        public void onBleBroadcastPinUpdated(BluetoothDevice bluetoothDevice, byte b, int i) {
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "onBleBroadcastPinUpdated: rcvr:" + bluetoothDevice + "status:" + i + "srcId" + b);
            if (i != 0) {
                String r3 = BluetoothSADetail.this.getBluetoothName(bluetoothDevice);
                BluetoothSADetail bluetoothSADetail = BluetoothSADetail.this;
                bluetoothSADetail.mCommonMsgDialog = BroadcastScanAssistanceUtils.showScanAssistError(bluetoothSADetail.mContext, r3, R$string.bluetooth_source_setpin_error_message, this.commonMessageListener);
            }
        }

        public void onBleBroadcastAudioSourceRemoved(BluetoothDevice bluetoothDevice, byte b, int i) {
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "onBleBroadcastAudioSourceRemoved: rcvr:" + bluetoothDevice + "status:" + i + "srcId" + b);
            if (i != 0) {
                String r3 = BluetoothSADetail.this.getBluetoothName(bluetoothDevice);
                BluetoothSADetail bluetoothSADetail = BluetoothSADetail.this;
                bluetoothSADetail.mCommonMsgDialog = BroadcastScanAssistanceUtils.showScanAssistError(bluetoothSADetail.mContext, r3, bluetoothSADetail.getSourceRemovalErrMessage(i), this.commonMessageListener);
            }
        }
    };
    private AlertDialog mScanAssistDetailsDialog;
    BleBroadcastAudioScanAssistManager mScanAssistManager;
    Preference mScanDelegatorName;
    boolean mScanning = false;

    public String getDeviceListKey() {
        return "available_audio_sources";
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "BluetoothSADetail";
    }

    public int getMetricsCategory() {
        return 1018;
    }

    public void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
    }

    /* access modifiers changed from: private */
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

    /* access modifiers changed from: private */
    public int getSourceSelectionErrMessage(int i) {
        int i2 = R$string.bluetooth_source_selection_error_message;
        switch (i) {
            case 5:
            case 7:
                return R$string.bluetooth_source_selection_error_src_unavail_message;
            case 8:
                return R$string.bluetooth_source_dup_addition_error_message;
            case 9:
                return R$string.bluetooth_source_no_empty_slot_error_message;
            default:
                return i2;
        }
    }

    /* access modifiers changed from: private */
    public int getSourceAdditionErrMessage(int i) {
        int i2 = R$string.bluetooth_source_addition_error_message;
        if (i == 8) {
            return R$string.bluetooth_source_dup_addition_error_message;
        }
        if (i != 9) {
            return i2;
        }
        return R$string.bluetooth_source_no_empty_slot_error_message;
    }

    /* access modifiers changed from: private */
    public int getSourceRemovalErrMessage(int i) {
        int i2 = R$string.bluetooth_source_removal_error_message;
        if (i == 2) {
            return i2;
        }
        if (i == 4) {
            return R$string.bluetooth_source_remove_invalid_src_id;
        }
        if (i != 16) {
            return i2;
        }
        return R$string.bluetooth_source_remove_invalid_group_op;
    }

    /* access modifiers changed from: private */
    public int getSourceUpdateErrMessage(int i) {
        int i2 = R$string.bluetooth_source_update_error_message;
        if (i == 2) {
            return i2;
        }
        if (i == 4) {
            return R$string.bluetooth_source_update_invalid_src_id;
        }
        if (i != 16) {
            return i2;
        }
        return R$string.bluetooth_source_update_invalid_group_op;
    }

    public BluetoothSADetail() {
        super("no_config_bluetooth");
    }

    /* access modifiers changed from: package-private */
    public void createDevicePreference(CachedBluetoothDevice cachedBluetoothDevice) {
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
        this.mAvailableDevicesCategory.setProgress(this.mScanning);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mInitialScanStarted = false;
    }

    public void onStart() {
        BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "OnStart Called");
        super.onStart();
        LocalBluetoothManager localBluetoothManager = this.mLocalManager;
        if (localBluetoothManager == null) {
            Log.e("BluetoothSADetail", "Bluetooth is not supported on this device");
            return;
        }
        if (this.mScanAssistManager == null) {
            if (this.mProfileManager == null) {
                this.mProfileManager = localBluetoothManager.getProfileManager();
            }
            BleBroadcastAudioScanAssistManager bSAManager = ((BCProfile) this.mProfileManager.getBCProfile()).getBSAManager(this.mCachedDevice.getDevice(), this.mScanAssistCallback);
            this.mScanAssistManager = bSAManager;
            if (bSAManager == null) {
                Log.e("BluetoothSADetail", "On Start: not able to instantiate scanAssistManager");
            }
        }
        updateContent(this.mBluetoothAdapter.getState());
        this.mAvailableDevicesCategory.setProgress(this.mBluetoothAdapter.isDiscovering());
    }

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
        if (findDevice != null) {
            LocalBluetoothProfileManager profileManager = this.mLocalManager.getProfileManager();
            this.mProfileManager = profileManager;
            BleBroadcastAudioScanAssistManager bSAManager = ((BCProfile) profileManager.getBCProfile()).getBSAManager(this.mCachedDevice.getDevice(), this.mScanAssistCallback);
            this.mScanAssistManager = bSAManager;
            if (bSAManager == null) {
                Log.e("BluetoothSADetail", "not able to instantiate scanAssistManager");
            }
        }
    }

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

    /* access modifiers changed from: package-private */
    public void initPreferencesFromPreferenceScreen() {
        Preference findPreference = findPreference("bt_bcast_rcvr_device");
        this.mScanDelegatorName = findPreference;
        findPreference.setSelectable(false);
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        if (cachedBluetoothDevice == null) {
            this.mScanDelegatorName.setSummary((CharSequence) "Scan Delegator");
        } else {
            this.mScanDelegatorName.setSummary((CharSequence) getBluetoothName(cachedBluetoothDevice.getDevice()));
        }
        this.mAvailableDevicesCategory = (BluetoothProgressCategory) findPreference("available_audio_sources");
        FooterPreference footerPreference = (FooterPreference) findPreference("footer_preference");
        this.mFooterPreference = footerPreference;
        footerPreference.setSelectable(false);
    }

    /* access modifiers changed from: package-private */
    public void enableScanning() {
        if (!this.mInitialScanStarted) {
            if (this.mAvailableDevicesCategory != null) {
                removeAllDevices();
            }
            this.mLocalManager.getCachedDeviceManager().clearNonBondedDevices();
            this.mInitialScanStarted = true;
        }
        if (this.mScanAssistManager != null && !this.mScanning) {
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "call searchforLeAudioBroadcasters");
            this.mScanAssistManager.searchforLeAudioBroadcasters();
            this.mScanning = true;
        }
    }

    /* access modifiers changed from: package-private */
    public void disableScanning() {
        if (this.mScanAssistManager != null && this.mScanning) {
            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "call stopSearchforLeAudioBroadcasters");
            this.mScanAssistManager.stopSearchforLeAudioBroadcasters();
            this.mScanning = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void launchSyncAndBroadcastIndexOptions(final List<BleBroadcastSourceChannel> list) {
        String str;
        Context context = getContext();
        FragmentActivity activity = getActivity();
        if (isAdded() && activity != null) {
            final View inflate = getLayoutInflater().inflate(R$layout.select_source_prompt, (ViewGroup) null);
            CachedBluetoothDevice cachedBluetoothDevice = this.clickedDevice;
            String name = cachedBluetoothDevice != null ? cachedBluetoothDevice.getName() : null;
            if (TextUtils.isEmpty(name)) {
                name = context.getString(R$string.bluetooth_device);
            }
            if (this.mGroupOperation) {
                str = context.getString(R$string.bluetooth_grp_source_selection_options_detail, new Object[]{name});
            } else {
                str = context.getString(R$string.bluetooth_source_selection_options_detail, new Object[]{name});
            }
            String string = context.getString(R$string.bluetooth_source_selection_options_detail_title);
            C08002 r8 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    BroadcastScanAssistanceUtils.debug("BluetoothSADetail", ">>Cancel clicked");
                    BluetoothSADetail.this.finish();
                }
            };
            C08013 r9 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    CachedBluetoothDevice cachedBluetoothDevice;
                    CachedBluetoothDevice cachedBluetoothDevice2 = BluetoothSADetail.this.clickedDevice;
                    if (cachedBluetoothDevice2 == null) {
                        Log.w("BluetoothSADetail", "Ignore as there is no clicked device");
                        return;
                    }
                    if (cachedBluetoothDevice2.getAddress().equals(BluetoothSADetail.this.mBluetoothAdapter.getAddress())) {
                        BroadcastScanAssistanceUtils.debug("BluetoothSADetail", ">>Local Adapter");
                        BluetoothSADetail.this.mBroadcastPinCode = null;
                    } else {
                        BluetoothSADetail.this.mBroadcastPinCode = ((EditText) inflate.findViewById(R$id.broadcastPINcode)).getText().toString();
                        BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "broadcastPinCode: " + BluetoothSADetail.this.mBroadcastPinCode);
                        if (TextUtils.isEmpty(BluetoothSADetail.this.mBroadcastPinCode)) {
                            BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "Empty broacast PinCode");
                            BluetoothSADetail.this.mBroadcastPinCode = null;
                        }
                    }
                    BluetoothSADetail bluetoothSADetail = BluetoothSADetail.this;
                    BleBroadcastAudioScanAssistManager bleBroadcastAudioScanAssistManager = bluetoothSADetail.mScanAssistManager;
                    if (bleBroadcastAudioScanAssistManager != null && (cachedBluetoothDevice = bluetoothSADetail.clickedDevice) != null) {
                        bleBroadcastAudioScanAssistManager.addBroadcastSource(cachedBluetoothDevice.getDevice(), 2, list, BluetoothSADetail.this.mGroupOperation);
                    }
                }
            };
            EditText editText = (EditText) inflate.findViewById(R$id.broadcastPINcode);
            CachedBluetoothDevice cachedBluetoothDevice2 = this.clickedDevice;
            if (cachedBluetoothDevice2 != null && cachedBluetoothDevice2.getAddress().equals(this.mBluetoothAdapter.getAddress())) {
                BroadcastScanAssistanceUtils.debug("BluetoothSADetail", "Local Adapter");
                this.mBroadcastPinCode = null;
                editText.setVisibility(4);
                if (this.mGroupOperation) {
                    str = context.getString(R$string.bluetooth_col_grp_source_selection_options_detail, new Object[]{name});
                } else {
                    str = context.getString(R$string.bluetooth_col_source_selection_options_detail, new Object[]{name});
                }
            }
            this.mScanAssistDetailsDialog = BroadcastScanAssistanceUtils.showScanAssistDetailsDialog(context, this.mScanAssistDetailsDialog, r9, r8, string, Html.fromHtml(str), inflate);
        }
    }

    /* access modifiers changed from: package-private */
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

    /* access modifiers changed from: package-private */
    public void updateContent(int i) {
        if (i == 10) {
            finish();
        } else if (i == 12) {
            this.mDevicePreferenceMap.clear();
            addDeviceCategory(this.mAvailableDevicesCategory, R$string.bluetooth_preference_found_media_devices, BluetoothDeviceFilter.ALL_FILTER, false);
            updateFooterPreference(this.mFooterPreference);
            enableScanning();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateFooterPreference(Preference preference) {
        BidiFormatter instance = BidiFormatter.getInstance();
        preference.setTitle((CharSequence) getString(R$string.bluetooth_footer_mac_message, instance.unicodeWrap(this.mCachedDevice.getAddress())));
    }

    public void onBluetoothStateChanged(int i) {
        super.onBluetoothStateChanged(i);
        updateContent(i);
        if (i == 12) {
            showBluetoothTurnedOnToast();
        }
    }

    public int getHelpResource() {
        return R$string.help_url_bluetooth;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.bluetooth_search_bcast_sources;
    }

    /* access modifiers changed from: package-private */
    public void showBluetoothTurnedOnToast() {
        Toast.makeText(getContext(), R$string.connected_device_bluetooth_turned_on_toast, 0).show();
    }
}
