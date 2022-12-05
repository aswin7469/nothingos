package com.nt.settings.panel;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import com.android.settings.R;
import com.android.settings.bluetooth.BluetoothDeviceDetailsFragment;
import com.android.settings.bluetooth.Utils;
import com.android.settings.core.SubSettingLauncher;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes2.dex */
public class BluetoothItem implements SettingItemContent, BluetoothCallback {
    private BluetoothAdapter mBluetoothAdapter;
    private List<CachedBluetoothDevice> mCachedDevices;
    private Context mContext;
    private boolean mInitialScanStarted;
    private LocalBluetoothManager mLocalManager;
    private boolean mScanEnabled;
    private Drawable mSettingsIcon;
    private boolean mShowDevicesWithoutNames;
    private List<SettingItemData> mDevicesData = new ArrayList();
    private List<SettingItemData> mPinnedHeaderData = new ArrayList();
    private SettingItemLiveData mLiveData = new SettingItemLiveData();
    private SettingItemLiveData mPinnedHeaderLiveData = new SettingItemLiveData();
    private HashMap<CachedBluetoothDevice, SettingItemData> mDevicesMap = new HashMap<>();
    Comparator<CachedBluetoothDevice> btComparator = new Comparator<CachedBluetoothDevice>() { // from class: com.nt.settings.panel.BluetoothItem.1
        @Override // java.util.Comparator
        public int compare(CachedBluetoothDevice cachedBluetoothDevice, CachedBluetoothDevice cachedBluetoothDevice2) {
            if (cachedBluetoothDevice == cachedBluetoothDevice2 || cachedBluetoothDevice == null || cachedBluetoothDevice2 == null) {
                return (cachedBluetoothDevice != null || cachedBluetoothDevice2 == null) ? 0 : 1;
            } else if (cachedBluetoothDevice.getBondState() != 12 && cachedBluetoothDevice2.getBondState() != 12) {
                return 0;
            } else {
                if (cachedBluetoothDevice.getBondState() != cachedBluetoothDevice2.getBondState()) {
                    return cachedBluetoothDevice2.getBondState() - cachedBluetoothDevice.getBondState();
                }
                return (cachedBluetoothDevice.isConnected() || !cachedBluetoothDevice2.isConnected()) ? 0 : 1;
            }
        }
    };

    @Override // com.nt.settings.panel.SettingItemContent
    public void loadData() {
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onDeviceDeleted(CachedBluetoothDevice cachedBluetoothDevice) {
    }

    public BluetoothItem(Context context) {
        Log.d("BluetoothItem", "BluetoothItem: ");
        this.mContext = context;
        LocalBluetoothManager localBtManager = Utils.getLocalBtManager(context);
        this.mLocalManager = localBtManager;
        if (localBtManager == null) {
            Log.d("BluetoothItem", "Bluetooth is not supported on this device");
            return;
        }
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mShowDevicesWithoutNames = SystemProperties.getBoolean("persist.bluetooth.showdeviceswithoutnames", false);
        this.mSettingsIcon = this.mContext.getDrawable(R.drawable.ic_settings_24dp);
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public List<SettingItemData> getDates() {
        return this.mDevicesData;
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public SettingItemLiveData getLiveDates() {
        return this.mLiveData;
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public SettingItemLiveData getPinnedHeaderLiveDates() {
        return this.mPinnedHeaderLiveData;
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onStart() {
        if (this.mLocalManager == null) {
            return;
        }
        Log.d("BluetoothItem", "onStart: ");
        this.mLocalManager.setForegroundActivity(this.mContext);
        this.mLocalManager.getEventManager().registerCallback(this);
        updateBluetooth();
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onPause() {
        Log.d("BluetoothItem", "onPause: ");
    }

    @Override // com.nt.settings.panel.SettingItemContent
    public void onStop() {
        if (this.mLocalManager == null) {
            return;
        }
        Log.d("BluetoothItem", "onStop: ");
        this.mLocalManager.setForegroundActivity(null);
        this.mLocalManager.getEventManager().unregisterCallback(this);
        disableScanning();
    }

    public boolean isProgressBarVisible() {
        return this.mBluetoothAdapter.isDiscovering();
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onBluetoothStateChanged(int i) {
        updateContent(i);
        Log.d("BluetoothItem", "onBluetoothStateChanged: state " + i);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onScanningStateChanged(boolean z) {
        Log.d("BluetoothItem", "onScanningStateChanged: started " + z);
        if (z || !this.mScanEnabled) {
            return;
        }
        startScanning();
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
        dealWithDevice(cachedBluetoothDevice);
        this.mLiveData.setDataChange(this.mDevicesData);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        Log.d("BluetoothItem", "onDeviceBondStateChanged: state " + i + " cachedDevice " + getTitle(cachedBluetoothDevice));
        updateDevice(cachedBluetoothDevice);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        Log.d("BluetoothItem", "onConnectionStateChanged: state " + i + " cachedDevice " + getTitle(cachedBluetoothDevice));
        updateDevice(cachedBluetoothDevice);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        Log.d("BluetoothItem", "onProfileConnectionStateChanged: state " + i + " cachedDevice " + cachedBluetoothDevice.getName());
        updateDevice(cachedBluetoothDevice);
    }

    void enableScanning() {
        Log.d("BluetoothItem", "enableScanning: mInitialScanStarted " + this.mInitialScanStarted + " mScanEnabled " + this.mScanEnabled);
        if (!this.mScanEnabled) {
            startScanning();
            this.mScanEnabled = true;
        }
    }

    void disableScanning() {
        Log.d("BluetoothItem", "disableScanning: mScanEnabled " + this.mScanEnabled);
        if (this.mScanEnabled) {
            stopScanning();
            this.mScanEnabled = false;
        }
    }

    void startScanning() {
        Log.d("BluetoothItem", "startScanning: mBluetoothAdapter.isDiscovering() " + this.mBluetoothAdapter.isDiscovering());
        if (!this.mBluetoothAdapter.isDiscovering()) {
            this.mBluetoothAdapter.startDiscovery();
        }
    }

    void stopScanning() {
        Log.d("BluetoothItem", "stopScanning: mBluetoothAdapter.isDiscovering() " + this.mBluetoothAdapter.isDiscovering());
        if (this.mBluetoothAdapter.isDiscovering()) {
            this.mBluetoothAdapter.cancelDiscovery();
        }
    }

    void updateBluetooth() {
        Log.d("BluetoothItem", "updateBluetooth: mBluetoothAdapter.isEnabled() " + this.mBluetoothAdapter.isEnabled());
        if (this.mBluetoothAdapter.isEnabled()) {
            updateContent(this.mBluetoothAdapter.getState());
            return;
        }
        clearDevicesDataAndUpdate(true);
        updateHeader(true, false);
    }

    void updateContent(int i) {
        if (i == 10) {
            clearDevicesDataAndUpdate(true);
            updateHeader(true, false);
        } else if (i != 12) {
        } else {
            clearDevicesDataAndUpdate(false);
            updateHeader(true, true);
            this.mBluetoothAdapter.enable();
            initDevicesData(this.mInitialScanStarted);
            enableScanning();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initDevicesData(boolean z) {
        ArrayList arrayList = new ArrayList(this.mLocalManager.getCachedDeviceManager().getCachedDevicesCopy());
        this.mCachedDevices = arrayList;
        arrayList.sort(this.btComparator);
        Log.d("BluetoothItem", "initDevicesData: size " + this.mCachedDevices.size());
        for (CachedBluetoothDevice cachedBluetoothDevice : this.mCachedDevices) {
            dealWithDevice(cachedBluetoothDevice);
        }
        this.mLiveData.setDataChange(this.mDevicesData);
    }

    private void clearDevicesDataAndUpdate(boolean z) {
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
        if (shouldFilterDevice(cachedBluetoothDevice)) {
            return;
        }
        SettingItemData settingItemData = new SettingItemData();
        updateDataAccordingToDeviceState(cachedBluetoothDevice, settingItemData);
        this.mDevicesData.add(settingItemData);
        this.mDevicesMap.put(cachedBluetoothDevice, settingItemData);
    }

    private void updateDataAccordingToDeviceState(final CachedBluetoothDevice cachedBluetoothDevice, SettingItemData settingItemData) {
        settingItemData.title = getTitle(cachedBluetoothDevice);
        settingItemData.subTitle = getSubtitle(cachedBluetoothDevice);
        settingItemData.titleDrawable = new PanelCircleDrawable(getDrawable(cachedBluetoothDevice));
        if (cachedBluetoothDevice.getBondState() != 12) {
            settingItemData.actionDrawable = null;
            settingItemData.contentClickListener = new View.OnClickListener() { // from class: com.nt.settings.panel.BluetoothItem.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    cachedBluetoothDevice.connect();
                    BluetoothItem.this.initDevicesData(false);
                }
            };
            return;
        }
        settingItemData.actionDrawable = this.mSettingsIcon;
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.nt.settings.panel.BluetoothItem.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("device_address", cachedBluetoothDevice.getDevice().getAddress());
                new SubSettingLauncher(BluetoothItem.this.mContext).setDestination(BluetoothDeviceDetailsFragment.class.getName()).setArguments(bundle).setTitleRes(R.string.device_details_title).setSourceMetricsCategory(1018).launch();
            }
        };
        settingItemData.actionClickListener = onClickListener;
        settingItemData.contentClickListener = onClickListener;
        if (cachedBluetoothDevice.isConnected()) {
            return;
        }
        settingItemData.contentClickListener = new View.OnClickListener() { // from class: com.nt.settings.panel.BluetoothItem.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                cachedBluetoothDevice.connect();
                BluetoothItem.this.initDevicesData(false);
            }
        };
    }

    private void updateDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        if (this.mCachedDevices != null) {
            clearDevicesDataAndUpdate(true);
            this.mCachedDevices.sort(this.btComparator);
            for (CachedBluetoothDevice cachedBluetoothDevice2 : this.mCachedDevices) {
                dealWithDevice(cachedBluetoothDevice2);
            }
        }
        if (this.mDevicesMap.containsKey(cachedBluetoothDevice)) {
            updateDataAccordingToDeviceState(cachedBluetoothDevice, this.mDevicesMap.get(cachedBluetoothDevice));
            this.mLiveData.setDataChange(this.mDevicesData);
        }
    }

    private void updateHeader(boolean z, boolean z2) {
        if (z) {
            if (this.mPinnedHeaderData.size() > 0) {
                this.mPinnedHeaderData.get(0).isChecked = z2;
            } else {
                this.mPinnedHeaderData.add(0, getHeaderItem(z2));
            }
        } else {
            this.mPinnedHeaderData.clear();
        }
        this.mPinnedHeaderLiveData.setDataChange(this.mPinnedHeaderData);
    }

    private SettingItemData getHeaderItem(boolean z) {
        SettingItemData settingItemData = new SettingItemData();
        settingItemData.title = this.mContext.getString(R.string.bluetooth);
        settingItemData.hasToggle = true;
        settingItemData.canForward = true;
        settingItemData.isChecked = z;
        settingItemData.switchListener = new CompoundButton.OnCheckedChangeListener() { // from class: com.nt.settings.panel.BluetoothItem.5
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                if (z2) {
                    BluetoothItem.this.mBluetoothAdapter.enable();
                    return;
                }
                BluetoothItem.this.mBluetoothAdapter.disable();
                BluetoothItem.this.mScanEnabled = false;
            }
        };
        settingItemData.contentClickListener = new View.OnClickListener() { // from class: com.nt.settings.panel.BluetoothItem.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                BluetoothItem.this.mContext.startActivity(new Intent("android.settings.BLUETOOTH_SETTINGS"));
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
                return this.mContext.getString(R.string.wifi_display_status_connected);
            }
            if (!isConnecting(cachedBluetoothDevice)) {
                return null;
            }
            return this.mContext.getString(R.string.bluetooth_connecting);
        } else if (cachedBluetoothDevice.getBondState() != 11) {
            return null;
        } else {
            return this.mContext.getString(R.string.bluetooth_pairing);
        }
    }

    private Drawable getDrawable(CachedBluetoothDevice cachedBluetoothDevice) {
        return (Drawable) cachedBluetoothDevice.getDrawableWithDescription().first;
    }

    private boolean isConnecting(CachedBluetoothDevice cachedBluetoothDevice) {
        for (LocalBluetoothProfile localBluetoothProfile : cachedBluetoothDevice.getProfiles()) {
            if (cachedBluetoothDevice.getProfileConnectionState(localBluetoothProfile) == 1) {
                return true;
            }
        }
        return false;
    }
}
