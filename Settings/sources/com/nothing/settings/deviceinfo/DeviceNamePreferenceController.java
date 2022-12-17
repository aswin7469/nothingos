package com.nothing.settings.deviceinfo;

import android.app.usage.StorageStatsManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SoftApConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.BidiFormatter;
import android.text.SpannedString;
import android.text.format.Formatter;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$bool;
import com.android.settings.R$string;
import com.android.settings.Utils;
import com.android.settings.bluetooth.BluetoothLengthDeviceNameFilter;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.deviceinfo.storage.StorageEntry;
import com.android.settings.wifi.tether.WifiDeviceNameTextValidator;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnCreate;
import com.android.settingslib.core.lifecycle.events.OnSaveInstanceState;
import com.android.settingslib.utils.ThreadUtils;
import com.nothing.settings.deviceinfo.aboutphone.DeviceBasicInfoPreference;
import com.nothing.settings.deviceinfo.aboutphone.EditDeviceNameDialog;
import java.io.File;
import java.io.IOException;

public class DeviceNamePreferenceController extends BasePreferenceController implements DeviceBasicInfoPreference.Validator, DeviceBasicInfoPreference.OnClickListener, Preference.OnPreferenceChangeListener, LifecycleObserver, OnSaveInstanceState, OnCreate {
    private static final String ACTION_NT_SOFTWARE_INFO = "android.settings.ACTION_NT_SOFTWARE_INFO";
    private static final String ACTION_STORAGE_INFO = "android.settings.INTERNAL_STORAGE_SETTINGS";
    public static final int CLICK_EVENT_DEVICE_NAME = 0;
    public static final int CLICK_EVENT_SOFTWARE_INFO = 1;
    public static final int CLICK_EVENT_STORAGE_INFO = 2;
    private static final String KEY_PENDING_DEVICE_NAME = "key_pending_device_name";
    static final int RES_SHOW_DEVICE_NAME_BOOL = R$bool.config_show_device_name;
    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private String mDeviceName;
    private DeviceNamePreferenceHost mHost;
    private String mPendingDeviceName;
    /* access modifiers changed from: private */
    public DeviceBasicInfoPreference mPreference;
    private StorageEntry mStorageEntry;
    private StorageStatsManager mStorageStatsManager;
    long mTotalBytes;
    long mUsedBytes;
    private final WifiDeviceNameTextValidator mWifiDeviceNameTextValidator = new WifiDeviceNameTextValidator();
    protected WifiManager mWifiManager;

    public interface DeviceNamePreferenceHost {
        void showDeviceNameWarningDialog(String str);
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DeviceNamePreferenceController(Context context, String str) {
        super(context, str);
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        initialStorageStats();
        initializeDeviceName();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = (DeviceBasicInfoPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference.setDeviceName(getSummary().toString());
        this.mPreference.setOnClickListener(this);
        this.mPreference.setValidator(this);
    }

    private void initializeDeviceName() {
        this.mDeviceName = Settings.Global.getString(this.mContext.getContentResolver(), "device_name");
        if (Utils.isSupportCTPA(this.mContext)) {
            this.mDeviceName = Utils.getString(this.mContext, "ext_device_name");
        }
        if (this.mDeviceName == null) {
            this.mDeviceName = Build.MODEL;
        }
    }

    private void initialStorageStats() {
        this.mStorageEntry = StorageEntry.getDefaultInternalStorageEntry(this.mContext);
        this.mStorageStatsManager = (StorageStatsManager) this.mContext.getSystemService(StorageStatsManager.class);
        ThreadUtils.postOnBackgroundThread((Runnable) new Runnable() {
            public void run() {
                try {
                    DeviceNamePreferenceController.this.getStorageStats();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void getStorageStats() throws IOException {
        this.mTotalBytes = 0;
        this.mUsedBytes = 0;
        StorageEntry storageEntry = this.mStorageEntry;
        if (storageEntry == null || !storageEntry.isMounted()) {
            throw new IOException();
        }
        if (this.mStorageEntry.isPrivate()) {
            long totalBytes = this.mStorageStatsManager.getTotalBytes(this.mStorageEntry.getFsUuid());
            this.mTotalBytes = totalBytes;
            this.mUsedBytes = totalBytes - this.mStorageStatsManager.getFreeBytes(this.mStorageEntry.getFsUuid());
        } else {
            File path = this.mStorageEntry.getPath();
            if (path != null) {
                long totalSpace = path.getTotalSpace();
                this.mTotalBytes = totalSpace;
                this.mUsedBytes = totalSpace - path.getFreeSpace();
            } else {
                Log.d("AboutPhoneBasicInfo", "Mounted public storage has null root path: " + this.mStorageEntry);
                throw new IOException();
            }
        }
        ThreadUtils.postOnMainThread(new Runnable() {
            public void run() {
                if (DeviceNamePreferenceController.this.mPreference != null) {
                    BidiFormatter instance = BidiFormatter.getInstance();
                    DeviceNamePreferenceController deviceNamePreferenceController = DeviceNamePreferenceController.this;
                    DeviceNamePreferenceController deviceNamePreferenceController2 = DeviceNamePreferenceController.this;
                    DeviceNamePreferenceController.this.mPreference.setStorageInfo(String.format("%s / %s", new Object[]{deviceNamePreferenceController.getStorageSummary(R$string.storage_usage_summary, deviceNamePreferenceController.mUsedBytes), instance.unicodeWrap(deviceNamePreferenceController2.getStorageSummary(R$string.nt_storage_total_summary, deviceNamePreferenceController2.mTotalBytes))}));
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public String getStorageSummary(int i, long j) {
        Formatter.BytesResult formatBytes = Formatter.formatBytes(this.mContext.getResources(), j, 1);
        return this.mContext.getString(i, new Object[]{formatBytes.value, formatBytes.units});
    }

    public CharSequence getSummary() {
        return this.mDeviceName;
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R$bool.config_show_device_name) ? 0 : 3;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        String str = (String) obj;
        this.mPendingDeviceName = str;
        DeviceNamePreferenceHost deviceNamePreferenceHost = this.mHost;
        if (deviceNamePreferenceHost == null) {
            return true;
        }
        deviceNamePreferenceHost.showDeviceNameWarningDialog(str);
        return true;
    }

    public boolean isTextValid(String str) {
        return this.mWifiDeviceNameTextValidator.isTextValid(str);
    }

    public void updateDeviceName(boolean z) {
        String str;
        if (!z || (str = this.mPendingDeviceName) == null) {
            this.mPreference.setDeviceName(getSummary().toString());
        } else {
            setDeviceName(str);
        }
    }

    public void setHost(DeviceNamePreferenceHost deviceNamePreferenceHost) {
        this.mHost = deviceNamePreferenceHost;
    }

    private void setDeviceName(String str) {
        this.mDeviceName = str;
        setSettingsGlobalDeviceName(str);
        setBluetoothDeviceName(str);
        setTetherSsidName(str);
        this.mPreference.setDeviceName(str);
    }

    private void setSettingsGlobalDeviceName(String str) {
        Settings.Global.putString(this.mContext.getContentResolver(), "device_name", str);
        if (Utils.isSupportCTPA(this.mContext)) {
            Settings.Global.putString(this.mContext.getContentResolver(), "ext_device_name", str);
        }
    }

    private void setBluetoothDeviceName(String str) {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter != null) {
            bluetoothAdapter.setName(getFilteredBluetoothString(str));
        }
    }

    private static final String getFilteredBluetoothString(String str) {
        CharSequence filter = new BluetoothLengthDeviceNameFilter().filter(str, 0, str.length(), new SpannedString(""), 0, 0);
        if (filter == null) {
            return str;
        }
        return filter.toString();
    }

    private void setTetherSsidName(String str) {
        this.mWifiManager.setSoftApConfiguration(new SoftApConfiguration.Builder(this.mWifiManager.getSoftApConfiguration()).setSsid(str).build());
    }

    public void onCreate(Bundle bundle) {
        if (bundle != null) {
            this.mPendingDeviceName = bundle.getString(KEY_PENDING_DEVICE_NAME, (String) null);
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(KEY_PENDING_DEVICE_NAME, this.mPendingDeviceName);
    }

    public void onClick(Preference preference, int i) {
        if (i == 0) {
            showEditDeviceNameDialog(preference);
        } else if (i == 1) {
            Intent intent = new Intent();
            intent.setAction(ACTION_NT_SOFTWARE_INFO);
            this.mContext.startActivity(intent);
        } else if (i == 2) {
            Intent intent2 = new Intent();
            intent2.setAction(ACTION_STORAGE_INFO);
            this.mContext.startActivity(intent2);
        }
    }

    private void showEditDeviceNameDialog(Preference preference) {
        new EditDeviceNameDialog(this.mContext, preference, this.mContext.getResources().getString(R$string.my_device_info_device_name_preference_title), this.mDeviceName).setOnDialogDismissListener(new DeviceNamePreferenceController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showEditDeviceNameDialog$0(String str, boolean z) {
        if (z) {
            String trim = str.trim();
            this.mPendingDeviceName = trim;
            DeviceNamePreferenceHost deviceNamePreferenceHost = this.mHost;
            if (deviceNamePreferenceHost != null) {
                deviceNamePreferenceHost.showDeviceNameWarningDialog(trim);
            }
        }
    }
}
