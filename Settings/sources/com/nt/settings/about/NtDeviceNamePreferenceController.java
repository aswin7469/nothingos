package com.nt.settings.about;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.SoftApConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannedString;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.bluetooth.BluetoothLengthDeviceNameFilter;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.wifi.tether.WifiDeviceNameTextValidator;
import com.android.settingslib.core.lifecycle.events.OnCreate;
import com.android.settingslib.core.lifecycle.events.OnSaveInstanceState;
import com.nt.settings.about.NtAboutPhoneBasicInfoPreference;
import com.nt.settings.about.NtAboutPhoneEditTextDialog;
/* loaded from: classes2.dex */
public class NtDeviceNamePreferenceController extends BasePreferenceController implements NtAboutPhoneBasicInfoPreference.Validator, OnSaveInstanceState, OnCreate, NtAboutPhoneBasicInfoPreference.OnClickListener {
    private static final String ACTION_NT_SOFTWARE_INFO = "android.settings.ACTION_NT_SOFTWARE_INFO";
    private static final String KEY_PENDING_DEVICE_NAME = "key_pending_device_name";
    static final int RES_SHOW_DEVICE_NAME_BOOL = R.bool.config_show_device_name;
    private static final String TAG = "NtDeviceNamePrefCtrl";
    private Context mContext;
    private String mDeviceName;
    private DeviceNamePreferenceHost mHost;
    private String mPendingDeviceName;
    private NtAboutPhoneBasicInfoPreference mPreference;
    protected WifiManager mWifiManager;
    private final WifiDeviceNameTextValidator mWifiDeviceNameTextValidator = new WifiDeviceNameTextValidator();
    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    /* loaded from: classes2.dex */
    public interface DeviceNamePreferenceHost {
        void showDeviceNameWarningDialog(String str);
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NtDeviceNamePreferenceController(Context context, String str) {
        super(context, str);
        this.mContext = context;
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        initializeDeviceName();
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        NtAboutPhoneBasicInfoPreference ntAboutPhoneBasicInfoPreference = (NtAboutPhoneBasicInfoPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = ntAboutPhoneBasicInfoPreference;
        ntAboutPhoneBasicInfoPreference.setValidator(this);
        this.mPreference.setOnClickListener(this);
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

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        return this.mDeviceName;
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_device_name) ? 0 : 3;
    }

    @Override // com.nt.settings.about.NtAboutPhoneBasicInfoPreference.Validator
    public boolean isTextValid(String str) {
        return str != null && str.length() <= 32 && str.length() > 0;
    }

    public void updateDeviceName(boolean z) {
        String str;
        if (!z || (str = this.mPendingDeviceName) == null) {
            return;
        }
        setDeviceName(str);
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
        return filter == null ? str : filter.toString();
    }

    private void setTetherSsidName(String str) {
        this.mWifiManager.setSoftApConfiguration(new SoftApConfiguration.Builder(this.mWifiManager.getSoftApConfiguration()).setSsid(str).build());
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnCreate
    public void onCreate(Bundle bundle) {
        if (bundle != null) {
            this.mPendingDeviceName = bundle.getString(KEY_PENDING_DEVICE_NAME, null);
        }
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnSaveInstanceState
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(KEY_PENDING_DEVICE_NAME, this.mPendingDeviceName);
    }

    @Override // com.nt.settings.about.NtAboutPhoneBasicInfoPreference.OnClickListener
    public void onClick(Preference preference, int i) {
        Log.d(TAG, "@_@ ------ onClick item_type = " + i);
        if (i == 10) {
            showDeviceNameDialog(preference);
        } else if (i != 11) {
        } else {
            Intent intent = new Intent();
            intent.setAction(ACTION_NT_SOFTWARE_INFO);
            this.mContext.startActivity(intent);
        }
    }

    private void showDeviceNameDialog(Preference preference) {
        Context context = this.mContext;
        String string = context.getResources().getString(R.string.my_device_info_device_name_preference_title);
        String str = this.mDeviceName;
        new NtAboutPhoneEditTextDialog(context, preference, string, str, str, 1).setOnDialogDismissListener(new NtAboutPhoneEditTextDialog.OnDialogDismissListener() { // from class: com.nt.settings.about.NtDeviceNamePreferenceController.1
            @Override // com.nt.settings.about.NtAboutPhoneEditTextDialog.OnDialogDismissListener
            public void OnDialogDismiss(String str2, boolean z) {
                if (z) {
                    NtDeviceNamePreferenceController.this.mPendingDeviceName = str2.trim();
                    if (NtDeviceNamePreferenceController.this.mHost == null) {
                        return;
                    }
                    NtDeviceNamePreferenceController.this.mHost.showDeviceNameWarningDialog(NtDeviceNamePreferenceController.this.mPendingDeviceName);
                }
            }
        });
    }
}
