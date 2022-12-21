package com.nothing.systemui.p024qs.tiles.settings.panel;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfile;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.C1893R;
import com.android.systemui.Dependency;
import com.nothing.p023os.device.DeviceConstant;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QSTileHostEx;
import com.nothing.systemui.p024qs.tiles.BluetoothTileEx;
import com.nothing.systemui.util.NTLogUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.BluetoothItem */
public class BluetoothItem implements SettingItemContent, BluetoothCallback {
    private static final int DELAY_BLUETOOTH_START_SCANNING_MS = 100;
    private static final int DEVICE_BOND_COLLAPSE_PANEL_MS = 3000;
    private static final String TAG = "BluetoothItem";
    Comparator<CachedBluetoothDevice> btComparator = new Comparator<CachedBluetoothDevice>() {
        public int compare(CachedBluetoothDevice cachedBluetoothDevice, CachedBluetoothDevice cachedBluetoothDevice2) {
            if (cachedBluetoothDevice == cachedBluetoothDevice2 || cachedBluetoothDevice == null || cachedBluetoothDevice2 == null) {
                if (cachedBluetoothDevice != null || cachedBluetoothDevice2 == null) {
                    return 0;
                }
                return 1;
            } else if (cachedBluetoothDevice.getBondState() != 12 && cachedBluetoothDevice2.getBondState() != 12) {
                return 0;
            } else {
                if (cachedBluetoothDevice.getBondState() != cachedBluetoothDevice2.getBondState()) {
                    return cachedBluetoothDevice2.getBondState() - cachedBluetoothDevice.getBondState();
                }
                if (cachedBluetoothDevice.isConnected() || !cachedBluetoothDevice2.isConnected()) {
                    return 0;
                }
                return 1;
            }
        }
    };
    /* access modifiers changed from: private */
    public BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothTileEx mBluetoothTileEx = ((BluetoothTileEx) NTDependencyEx.get(BluetoothTileEx.class));
    private String mBondAddr;
    private int mBondState;
    private List<CachedBluetoothDevice> mCachedDevices;
    private String mConnectAddr;
    private int mConnectState = -1;
    private Context mContext;
    private List<SettingItemData> mDevicesData = new ArrayList();
    private HashMap<CachedBluetoothDevice, SettingItemData> mDevicesMap = new HashMap<>();
    /* access modifiers changed from: private */
    public BluetoothPanelDialog mDialog;
    private boolean mIsLeScanEnabled;
    private SettingItemLiveData mLiveData = new SettingItemLiveData();
    private LocalBluetoothManager mLocalManager;
    private List<SettingItemData> mPinnedHeaderData = new ArrayList();
    private SettingItemLiveData mPinnedHeaderLiveData = new SettingItemLiveData();
    private String mProfileAddr;
    private int mProfileState = -1;
    private QSTileHostEx mQSHostEx = ((QSTileHostEx) NTDependencyEx.get(QSTileHostEx.class));
    private ScanCallback mScanCallback = new ScanCallback() {
        public void onScanResult(int i, ScanResult scanResult) {
            super.onScanResult(i, scanResult);
        }

        public void onBatchScanResults(List<ScanResult> list) {
            super.onBatchScanResults(list);
        }

        public void onScanFailed(int i) {
            super.onScanFailed(i);
        }
    };
    /* access modifiers changed from: private */
    public boolean mScanEnabled;
    private Drawable mSettingsIcon;
    private boolean mShowDevicesWithoutNames;

    public void loadData() {
    }

    public void onDeviceDeleted(CachedBluetoothDevice cachedBluetoothDevice) {
    }

    public BluetoothItem(Context context, BluetoothPanelDialog bluetoothPanelDialog) {
        this.mContext = context;
        this.mDialog = bluetoothPanelDialog;
        LocalBluetoothManager localBluetoothManager = (LocalBluetoothManager) Dependency.get(LocalBluetoothManager.class);
        this.mLocalManager = localBluetoothManager;
        if (localBluetoothManager == null) {
            NTLogUtil.m1680d(TAG, "Bluetooth is not supported on this device");
            return;
        }
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        this.mBluetoothLeScanner = defaultAdapter.getBluetoothLeScanner();
        this.mShowDevicesWithoutNames = SystemProperties.getBoolean("persist.bluetooth.showdeviceswithoutnames", false);
        this.mSettingsIcon = this.mContext.getDrawable(C1893R.C1895drawable.ic_settings_24dp);
    }

    public List<SettingItemData> getDates() {
        return this.mDevicesData;
    }

    public SettingItemLiveData getLiveDates() {
        return this.mLiveData;
    }

    public SettingItemLiveData getPinnedHeaderLiveDates() {
        return this.mPinnedHeaderLiveData;
    }

    public void onStart() {
        if (this.mLocalManager != null) {
            NTLogUtil.m1680d(TAG, "onStart");
            ThreadUtils.postOnMainThreadDelayed(new BluetoothItem$$ExternalSyntheticLambda0(this), 100);
            updateBluetooth();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onStart$0$com-nothing-systemui-qs-tiles-settings-panel-BluetoothItem */
    public /* synthetic */ void mo57820x8bd88055() {
        this.mLocalManager.setForegroundActivity(this.mContext);
        this.mLocalManager.getEventManager().registerCallback(this);
    }

    public void onPause() {
        NTLogUtil.m1680d(TAG, "onPause");
    }

    public void onStop() {
        if (this.mLocalManager != null) {
            NTLogUtil.m1680d(TAG, "onStop");
            if (this.mBluetoothTileEx.getAncCallback() != null) {
                this.mBluetoothTileEx.getAncCallback().onDestroy();
            }
            this.mLocalManager.setForegroundActivity((Context) null);
            this.mLocalManager.getEventManager().unregisterCallback(this);
            disableScanning();
        }
    }

    public boolean isProgressBarVisible() {
        return this.mBluetoothAdapter.isDiscovering();
    }

    public void onBluetoothStateChanged(int i) {
        NTLogUtil.m1680d(TAG, "onBluetoothStateChanged: state " + i);
        updateContent(i);
        this.mDialog.updateWindowSize(false);
    }

    public void onScanningStateChanged(boolean z) {
        NTLogUtil.m1680d(TAG, "onScanningStateChanged: started " + z);
        if (!z && this.mScanEnabled) {
            startScanning();
        }
        if (z) {
            startLeScan();
        } else {
            stopLeScan();
        }
    }

    public void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
        dealWithDevice(cachedBluetoothDevice);
        this.mLiveData.setDataChange(this.mDevicesData);
    }

    public void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        String address = cachedBluetoothDevice.getAddress();
        NTLogUtil.m1680d(TAG, "onDeviceBondStateChanged: state " + i + " cachedDevice " + address + ", mState:" + this.mBondState);
        if (this.mBondState != i || !TextUtils.equals(address, this.mBondAddr)) {
            collapsePanel(3000);
            updateDevice(cachedBluetoothDevice);
            this.mBondState = i;
            this.mBondAddr = address;
        }
    }

    public void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        String address = cachedBluetoothDevice.getAddress();
        NTLogUtil.m1680d(TAG, "onDeviceBondStateChanged: state " + i + " cachedDevice " + address + ", mState:" + this.mConnectState);
        if (this.mConnectState != i || !TextUtils.equals(address, this.mConnectAddr)) {
            updateDevice(cachedBluetoothDevice);
            this.mConnectState = i;
            this.mConnectAddr = address;
        }
    }

    public void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        String address = cachedBluetoothDevice.getAddress();
        NTLogUtil.m1680d(TAG, "onProfileConnectionStateChanged: state " + i + " cachedDevice " + address + ", mState:" + this.mProfileState);
        if (this.mProfileState != i || !TextUtils.equals(address, this.mProfileAddr)) {
            updateDevice(cachedBluetoothDevice);
            this.mProfileState = i;
            this.mProfileAddr = address;
        }
        if (this.mBluetoothTileEx.getAncCallback() != null) {
            this.mBluetoothTileEx.getAncCallback().onProfileConnectionStateChanged(cachedBluetoothDevice, i, i2);
        }
    }

    /* access modifiers changed from: package-private */
    public void enableScanning() {
        NTLogUtil.m1680d(TAG, "enableScanning: mScanEnabled " + this.mScanEnabled);
        if (!this.mScanEnabled) {
            startScanning();
            this.mScanEnabled = true;
        }
    }

    /* access modifiers changed from: package-private */
    public void disableScanning() {
        NTLogUtil.m1680d(TAG, "disableScanning: mScanEnabled " + this.mScanEnabled);
        if (this.mScanEnabled) {
            this.mLocalManager.getCachedDeviceManager().clearNonBondedDevices();
            stopScanning();
            this.mScanEnabled = false;
        }
    }

    /* access modifiers changed from: package-private */
    public void startScanning() {
        NTLogUtil.m1680d(TAG, "startScanning: mBluetoothAdapter.isDiscovering() " + this.mBluetoothAdapter.isDiscovering());
        if (!this.mBluetoothAdapter.isDiscovering()) {
            this.mBluetoothAdapter.startDiscovery();
        }
        initDevicesData();
    }

    /* access modifiers changed from: package-private */
    public void stopScanning() {
        NTLogUtil.m1680d(TAG, "stopScanning: mBluetoothAdapter.isDiscovering() " + this.mBluetoothAdapter.isDiscovering());
        if (this.mBluetoothAdapter.isDiscovering()) {
            this.mBluetoothAdapter.cancelDiscovery();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateBluetooth() {
        NTLogUtil.m1680d(TAG, "updateBluetooth: mBluetoothAdapter.isEnabled() " + this.mBluetoothAdapter.isEnabled());
        if (this.mBluetoothAdapter.isEnabled()) {
            updateContent(this.mBluetoothAdapter.getState());
            return;
        }
        clearDevicesDataAndUpdate(true);
        updateHeader(true, false);
    }

    /* access modifiers changed from: package-private */
    public void updateContent(int i) {
        if (i == 10) {
            clearDevicesDataAndUpdate(true);
            updateHeader(true, false);
            stopLeScan();
        } else if (i == 12) {
            clearDevicesDataAndUpdate(false);
            updateHeader(true, true);
            this.mBluetoothAdapter.enable();
            initDevicesData();
            enableScanning();
            startLeScan();
        }
    }

    public void initDevicesData() {
        ArrayList arrayList = new ArrayList(this.mLocalManager.getCachedDeviceManager().getCachedDevicesCopy());
        this.mCachedDevices = arrayList;
        arrayList.sort(this.btComparator);
        NTLogUtil.m1680d(TAG, "initDevicesData: size " + this.mCachedDevices.size());
        for (int i = 0; i < this.mCachedDevices.size(); i++) {
            dealWithDevice(this.mCachedDevices.get(i));
        }
        this.mLiveData.setDataChange(this.mDevicesData);
    }

    private void collapsePanel(int i) {
        ThreadUtils.postOnMainThreadDelayed(new BluetoothItem$$ExternalSyntheticLambda1(this), (long) i);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$collapsePanel$1$com-nothing-systemui-qs-tiles-settings-panel-BluetoothItem */
    public /* synthetic */ void mo57819x85f471e0() {
        this.mDialog.dismissDialog();
        this.mQSHostEx.collapsePanel();
    }

    private void clearDevicesDataAndUpdate(boolean z) {
        NTLogUtil.m1680d(TAG, "clearDevicesDataAndUpdate: " + z);
        this.mDevicesData.clear();
        this.mDevicesMap.clear();
        if (z) {
            this.mLiveData.setDataChange(this.mDevicesData);
        }
    }

    private boolean shouldFilterDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        return this.mDevicesMap.containsKey(cachedBluetoothDevice) || shouldFilterAccordingToName(cachedBluetoothDevice);
    }

    private boolean shouldFilterAccordingToName(CachedBluetoothDevice cachedBluetoothDevice) {
        return !this.mShowDevicesWithoutNames && !cachedBluetoothDevice.hasHumanReadableName();
    }

    private void dealWithDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        if (!shouldFilterDevice(cachedBluetoothDevice)) {
            SettingItemData settingItemData = new SettingItemData();
            updateDataAccordingToDeviceState(cachedBluetoothDevice, settingItemData);
            this.mDevicesData.add(settingItemData);
            this.mDevicesMap.put(cachedBluetoothDevice, settingItemData);
        }
    }

    private void updateDataAccordingToDeviceState(final CachedBluetoothDevice cachedBluetoothDevice, SettingItemData settingItemData) {
        settingItemData.title = getTitle(cachedBluetoothDevice);
        settingItemData.subTitle = getSubtitle(cachedBluetoothDevice);
        settingItemData.titleDrawable = new PanelCircleDrawable(getDrawable(cachedBluetoothDevice));
        settingItemData.macAddress = getAddress(cachedBluetoothDevice);
        settingItemData.isConnected = getConnected(cachedBluetoothDevice);
        settingItemData.cachedDevice = cachedBluetoothDevice;
        settingItemData.isNothingEarDevice = this.mBluetoothTileEx.isNothingEarDevice(settingItemData.macAddress);
        settingItemData.supportAnc = this.mBluetoothTileEx.isSupportAnc(settingItemData.macAddress);
        if (cachedBluetoothDevice.getBondState() != 12) {
            settingItemData.actionDrawable = null;
            settingItemData.contentClickListener = new View.OnClickListener() {
                public void onClick(View view) {
                    cachedBluetoothDevice.connect();
                    BluetoothItem.this.pair(cachedBluetoothDevice);
                    BluetoothItem.this.initDevicesData();
                }
            };
            return;
        }
        settingItemData.actionDrawable = this.mSettingsIcon;
        C41974 r0 = new View.OnClickListener() {
            public void onClick(View view) {
                NTLogUtil.m1680d(BluetoothItem.TAG, "DEVICE_DETAIL");
                Intent intent = new Intent(BluetoothTileEx.DEVICE_DETAIL);
                intent.putExtra(DeviceConstant.KEY_MAC_ADDRESS, cachedBluetoothDevice.getDevice().getAddress());
                BluetoothItem.this.mDialog.startActivity(intent, view);
            }
        };
        settingItemData.actionClickListener = r0;
        settingItemData.contentClickListener = r0;
        if (!cachedBluetoothDevice.isConnected()) {
            settingItemData.contentClickListener = new View.OnClickListener() {
                public void onClick(View view) {
                    cachedBluetoothDevice.connect();
                    BluetoothItem.this.pair(cachedBluetoothDevice);
                    BluetoothItem.this.initDevicesData();
                }
            };
        }
    }

    private void updateDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        if (this.mDevicesMap.containsKey(cachedBluetoothDevice)) {
            updateDataAccordingToDeviceState(cachedBluetoothDevice, this.mDevicesMap.get(cachedBluetoothDevice));
            this.mLiveData.setDataChange(this.mDevicesData);
        }
    }

    private void updateHeader(boolean z, boolean z2) {
        if (!z) {
            this.mPinnedHeaderData.clear();
        } else if (this.mPinnedHeaderData.size() > 0) {
            this.mPinnedHeaderData.get(0).isChecked = z2;
        } else {
            this.mPinnedHeaderData.add(0, getHeaderItem(z2));
        }
        this.mPinnedHeaderLiveData.setDataChange(this.mPinnedHeaderData);
    }

    private SettingItemData getHeaderItem(boolean z) {
        SettingItemData settingItemData = new SettingItemData();
        settingItemData.title = this.mContext.getString(C1893R.string.quick_settings_bluetooth_label);
        settingItemData.hasToggle = true;
        settingItemData.canForward = true;
        settingItemData.isChecked = z;
        settingItemData.switchListener = new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                if (z) {
                    BluetoothItem.this.mBluetoothAdapter.enable();
                    return;
                }
                BluetoothItem.this.mBluetoothAdapter.disable();
                boolean unused = BluetoothItem.this.mScanEnabled = false;
            }
        };
        settingItemData.contentClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                BluetoothItem.this.mDialog.startActivity(new Intent("android.settings.BLUETOOTH_SETTINGS"), view);
            }
        };
        return settingItemData;
    }

    private String getTitle(CachedBluetoothDevice cachedBluetoothDevice) {
        return cachedBluetoothDevice.getName();
    }

    private String getSubtitle(CachedBluetoothDevice cachedBluetoothDevice) {
        if (cachedBluetoothDevice.getBondState() == 12) {
            if (cachedBluetoothDevice.isConnected()) {
                return this.mContext.getString(C1893R.string.quick_settings_connected);
            }
            if (isConnecting(cachedBluetoothDevice)) {
                return this.mContext.getString(C1893R.string.quick_settings_connecting);
            }
            return null;
        } else if (cachedBluetoothDevice.getBondState() == 11) {
            return this.mContext.getString(C1893R.string.bluetooth_pairing);
        } else {
            return null;
        }
    }

    private Drawable getDrawable(CachedBluetoothDevice cachedBluetoothDevice) {
        return (Drawable) cachedBluetoothDevice.getDrawableWithDescription().first;
    }

    private boolean isConnecting(CachedBluetoothDevice cachedBluetoothDevice) {
        for (LocalBluetoothProfile profileConnectionState : cachedBluetoothDevice.getProfiles()) {
            if (cachedBluetoothDevice.getProfileConnectionState(profileConnectionState) == 1) {
                return true;
            }
        }
        return false;
    }

    private String getAddress(CachedBluetoothDevice cachedBluetoothDevice) {
        return cachedBluetoothDevice.getAddress();
    }

    private boolean getConnected(CachedBluetoothDevice cachedBluetoothDevice) {
        return cachedBluetoothDevice.isConnected();
    }

    private void startLeScan() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null && bluetoothAdapter.isLeEnabled() && !this.mIsLeScanEnabled) {
            this.mIsLeScanEnabled = true;
            try {
                this.mBluetoothLeScanner.startScan(this.mScanCallback);
            } catch (Exception unused) {
            }
        }
    }

    private void stopLeScan() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null && bluetoothAdapter.isLeEnabled() && this.mIsLeScanEnabled) {
            this.mIsLeScanEnabled = false;
            try {
                this.mBluetoothLeScanner.stopScan(this.mScanCallback);
            } catch (Exception unused) {
            }
        }
    }

    /* access modifiers changed from: private */
    public void pair(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mBluetoothTileEx.saveConnectedDevice(getAddress(cachedBluetoothDevice));
    }
}
