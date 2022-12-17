package com.android.settings.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemProperties;
import android.text.BidiFormatter;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import com.android.settings.R$string;
import com.android.settings.dashboard.RestrictedDashboardFragment;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.BluetoothDeviceFilter;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.nothing.p005os.device.DeviceServiceConnector;
import com.nothing.p005os.device.DeviceServiceController;
import com.nothing.p005os.device.IDeviceBitmap;
import com.nothing.settings.bluetooth.NothingBluetoothUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class DeviceListPreferenceFragment extends RestrictedDashboardFragment implements BluetoothCallback {
    BluetoothAdapter mBluetoothAdapter;
    private DeviceServiceController mDeviceControl;
    PreferenceGroup mDeviceListGroup;
    final HashMap<CachedBluetoothDevice, BluetoothDevicePreference> mDevicePreferenceMap = new HashMap<>();
    private DeviceServiceConnector.Callback mDeviceServiceConnectorCallback = new DeviceServiceConnector.Callback() {
        public void onDeviceServiceConnected() {
        }

        public void onDeviceServiceDisConnected() {
        }

        public void onFail(int i, int i2) {
        }

        public void onSuccess(int i, Bundle bundle) {
            String string = bundle != null ? bundle.getString("device_address") : null;
            if (i == 3 && string != null) {
                String string2 = bundle.getString("KEY_VALUE_STRING");
                Log.d("DeviceListPreferenceFragment", "onSuccess GET_EAR_BITMAP version:" + string2);
                try {
                    String modeID = NothingBluetoothUtil.getinstance().getModeID(DeviceListPreferenceFragment.this.getActivity(), string);
                    Bitmap defaultBitmap = IDeviceBitmap.Stub.asInterface(bundle.getBinder("KEY_BITMAP")).getDefaultBitmap();
                    NothingBluetoothUtil.getinstance().saveModuleIDEarBitmap(DeviceListPreferenceFragment.this.getActivity(), defaultBitmap, modeID);
                    if (!TextUtils.isEmpty(string2)) {
                        NothingBluetoothUtil.getinstance().saveAirpodsVersion(DeviceListPreferenceFragment.this.getActivity(), string, string2);
                        NothingBluetoothUtil.getinstance().saveModuleIDEarBitmap(DeviceListPreferenceFragment.this.getActivity(), defaultBitmap, string2);
                    }
                } catch (Exception unused) {
                }
            }
        }
    };
    private BluetoothDeviceFilter.Filter mFilter = BluetoothDeviceFilter.ALL_FILTER;
    LocalBluetoothManager mLocalManager;
    private String mSaveedModuleIdBitmap;
    boolean mScanEnabled;
    BluetoothDevice mSelectedDevice;
    final List<BluetoothDevice> mSelectedList = new ArrayList();
    boolean mShowDevicesWithoutNames;

    public abstract String getDeviceListKey();

    /* access modifiers changed from: protected */
    public void initDevicePreference(BluetoothDevicePreference bluetoothDevicePreference) {
    }

    /* access modifiers changed from: package-private */
    public abstract void initPreferencesFromPreferenceScreen();

    DeviceListPreferenceFragment(String str) {
        super(str);
    }

    /* access modifiers changed from: package-private */
    public final void setFilter(BluetoothDeviceFilter.Filter filter) {
        this.mFilter = filter;
    }

    /* access modifiers changed from: package-private */
    public final void setFilter(int i) {
        this.mFilter = BluetoothDeviceFilter.getFilter(i);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LocalBluetoothManager localBtManager = Utils.getLocalBtManager(getActivity());
        this.mLocalManager = localBtManager;
        if (localBtManager == null) {
            Log.e("DeviceListPreferenceFragment", "Bluetooth is not supported on this device");
            return;
        }
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mShowDevicesWithoutNames = SystemProperties.getBoolean("persist.bluetooth.showdeviceswithoutnames", false);
        initPreferencesFromPreferenceScreen();
        this.mDeviceListGroup = (PreferenceCategory) findPreference(getDeviceListKey());
        try {
            DeviceServiceController deviceServiceController = new DeviceServiceController(getActivity());
            this.mDeviceControl = deviceServiceController;
            deviceServiceController.addCallback(this.mDeviceServiceConnectorCallback);
        } catch (Exception unused) {
        }
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            this.mDeviceControl.removeCallback(this.mDeviceServiceConnectorCallback);
        } catch (Exception unused) {
        }
        this.mSaveedModuleIdBitmap = null;
    }

    public void onStart() {
        super.onStart();
        if (this.mLocalManager != null && !isUiRestricted()) {
            this.mLocalManager.setForegroundActivity(getActivity());
            this.mLocalManager.getEventManager().registerCallback(this);
        }
    }

    public void onStop() {
        super.onStop();
        if (this.mLocalManager != null && !isUiRestricted()) {
            removeAllDevices();
            this.mLocalManager.setForegroundActivity((Context) null);
            this.mLocalManager.getEventManager().unregisterCallback(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeAllDevices() {
        this.mDevicePreferenceMap.clear();
        this.mDeviceListGroup.removeAll();
    }

    /* access modifiers changed from: package-private */
    public void addCachedDevices() {
        for (CachedBluetoothDevice onDeviceAdded : this.mLocalManager.getCachedDeviceManager().getCachedDevicesCopy()) {
            onDeviceAdded(onDeviceAdded);
        }
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if ("bt_scan".equals(preference.getKey())) {
            startScanning();
            return true;
        } else if (!(preference instanceof BluetoothDevicePreference)) {
            return super.onPreferenceTreeClick(preference);
        } else {
            BluetoothDevicePreference bluetoothDevicePreference = (BluetoothDevicePreference) preference;
            CachedBluetoothDevice cachedDevice = bluetoothDevicePreference.getCachedDevice();
            BluetoothDevice device = cachedDevice.getDevice();
            this.mSelectedDevice = device;
            this.mSelectedList.add(device);
            onDevicePreferenceClick(bluetoothDevicePreference);
            Log.d("DeviceListPreferenceFragment", "onPreferenceTreeClick airpods:" + NothingBluetoothUtil.getinstance().checkUUIDIsAirpod(getActivity(), cachedDevice.getDevice()));
            if (NothingBluetoothUtil.getinstance().checkUUIDIsAirpod(getActivity(), cachedDevice.getDevice())) {
                String airpodsVersion = NothingBluetoothUtil.getinstance().getAirpodsVersion(getActivity(), cachedDevice.getAddress());
                Log.d("DeviceListPreferenceFragment", "onPreferenceTreeClick version:" + airpodsVersion);
                if (TextUtils.isEmpty(airpodsVersion)) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("KEY_EAR_CONNECTED", false);
                    bundle.putString("device_address", cachedDevice.getAddress());
                    bundle.putBoolean("KEY_IS_AIRPODS", true);
                    this.mDeviceControl.getCommand(3, bundle);
                }
            } else {
                String modeID = NothingBluetoothUtil.getinstance().getModeID(getActivity(), cachedDevice.getAddress());
                if (!TextUtils.isEmpty(modeID) && (TextUtils.isEmpty(this.mSaveedModuleIdBitmap) || !this.mSaveedModuleIdBitmap.equals(modeID))) {
                    this.mSaveedModuleIdBitmap = modeID;
                    getCommand(3, cachedDevice.getAddress(), false, modeID);
                }
            }
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public void onDevicePreferenceClick(BluetoothDevicePreference bluetoothDevicePreference) {
        bluetoothDevicePreference.onClicked();
    }

    public void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
        if (this.mDevicePreferenceMap.get(cachedBluetoothDevice) == null && this.mBluetoothAdapter.getState() == 12 && this.mFilter.matches(cachedBluetoothDevice.getDevice())) {
            createDevicePreference(cachedBluetoothDevice);
        }
    }

    /* access modifiers changed from: package-private */
    public void createDevicePreference(CachedBluetoothDevice cachedBluetoothDevice) {
        if (this.mDeviceListGroup == null) {
            Log.w("DeviceListPreferenceFragment", "Trying to create a device preference before the list group/category exists!");
            return;
        }
        String address = cachedBluetoothDevice.getDevice().getAddress();
        BluetoothDevicePreference bluetoothDevicePreference = (BluetoothDevicePreference) getCachedPreference(address);
        if (bluetoothDevicePreference == null) {
            bluetoothDevicePreference = new BluetoothDevicePreference(getPrefContext(), cachedBluetoothDevice, this.mShowDevicesWithoutNames, 2);
            bluetoothDevicePreference.setKey(address);
            bluetoothDevicePreference.hideSecondTarget(true);
            this.mDeviceListGroup.addPreference(bluetoothDevicePreference);
        }
        initDevicePreference(bluetoothDevicePreference);
        this.mDevicePreferenceMap.put(cachedBluetoothDevice, bluetoothDevicePreference);
    }

    /* access modifiers changed from: package-private */
    public void updateFooterPreference(Preference preference) {
        BidiFormatter instance = BidiFormatter.getInstance();
        preference.setTitle((CharSequence) getString(R$string.bluetooth_footer_mac_message, instance.unicodeWrap(this.mBluetoothAdapter.getAddress())));
    }

    public void onDeviceDeleted(CachedBluetoothDevice cachedBluetoothDevice) {
        BluetoothDevicePreference remove = this.mDevicePreferenceMap.remove(cachedBluetoothDevice);
        if (remove != null) {
            this.mDeviceListGroup.removePreference(remove);
        }
    }

    /* access modifiers changed from: package-private */
    public void enableScanning() {
        if (!this.mScanEnabled) {
            startScanning();
            this.mScanEnabled = true;
        }
    }

    /* access modifiers changed from: package-private */
    public void disableScanning() {
        if (this.mScanEnabled) {
            stopScanning();
            this.mScanEnabled = false;
        }
    }

    public void onScanningStateChanged(boolean z) {
        if (!z && this.mScanEnabled) {
            startScanning();
        }
    }

    public void addDeviceCategory(PreferenceGroup preferenceGroup, int i, BluetoothDeviceFilter.Filter filter, boolean z) {
        cacheRemoveAllPrefs(preferenceGroup);
        preferenceGroup.setTitle(i);
        this.mDeviceListGroup = preferenceGroup;
        if (z) {
            setFilter(BluetoothDeviceFilter.UNBONDED_DEVICE_FILTER);
            addCachedDevices();
        }
        setFilter(filter);
        preferenceGroup.setEnabled(true);
        removeCachedPrefs(preferenceGroup);
    }

    /* access modifiers changed from: package-private */
    public void startScanning() {
        if (!this.mBluetoothAdapter.isDiscovering()) {
            this.mBluetoothAdapter.startDiscovery();
        }
    }

    /* access modifiers changed from: package-private */
    public void stopScanning() {
        if (this.mBluetoothAdapter.isDiscovering()) {
            this.mBluetoothAdapter.cancelDiscovery();
        }
    }

    public void getCommand(int i, String str, boolean z, String str2) {
        if (this.mDeviceControl != null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("KEY_EAR_CONNECTED", z);
            bundle.putString("device_address", str);
            bundle.putString("KEY_MODEL_ID", str2);
            this.mDeviceControl.getCommand(3, bundle);
        }
    }
}
