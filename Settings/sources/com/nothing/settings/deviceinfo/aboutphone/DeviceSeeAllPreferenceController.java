package com.nothing.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;

public class DeviceSeeAllPreferenceController extends BasePreferenceController {
    public static final String KEY = "nt_device_see_all";
    private static final String KEY_IMEI = "nt_device_imei_info";
    public static final String TAG = "DeviceSeeAll";
    private Preference mBtAddressPre;
    private Preference mFactoryWifiMacAddressPre;
    private DeviceImeiPreference mImeiPreference;
    private Preference mIpAddressPre;
    private Preference mPreference;
    private Preference mUpTimePre;
    private Preference mWifiMacAddressPre;

    public int getAvailabilityStatus() {
        return 1;
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

    public DeviceSeeAllPreferenceController(Context context) {
        super(context, KEY);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mImeiPreference = (DeviceImeiPreference) preferenceScreen.findPreference("nt_device_imei_info");
        this.mIpAddressPre = preferenceScreen.findPreference("wifi_ip_address");
        this.mWifiMacAddressPre = preferenceScreen.findPreference("saved_accesspoints_wifi_mac_address");
        this.mFactoryWifiMacAddressPre = preferenceScreen.findPreference("wifi_mac_address");
        this.mBtAddressPre = preferenceScreen.findPreference("bt_address");
        this.mUpTimePre = preferenceScreen.findPreference("up_time");
        this.mPreference = preferenceScreen.findPreference(KEY);
        showOrHideChild(false);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (KEY.equals(preference.getKey())) {
            showOrHideChild(true);
        }
        return super.handlePreferenceTreeClick(preference);
    }

    private void showOrHideChild(boolean z) {
        DeviceImeiPreference deviceImeiPreference = this.mImeiPreference;
        if (deviceImeiPreference != null) {
            deviceImeiPreference.setVisible(z);
        }
        Preference preference = this.mIpAddressPre;
        if (preference != null) {
            preference.setVisible(z);
        }
        Preference preference2 = this.mWifiMacAddressPre;
        if (preference2 != null) {
            preference2.setVisible(z);
        }
        Preference preference3 = this.mFactoryWifiMacAddressPre;
        if (preference3 != null) {
            preference3.setVisible(z);
        }
        Preference preference4 = this.mBtAddressPre;
        if (preference4 != null) {
            preference4.setVisible(z);
        }
        Preference preference5 = this.mUpTimePre;
        if (preference5 != null) {
            preference5.setVisible(z);
        }
        Preference preference6 = this.mPreference;
        if (preference6 != null) {
            preference6.setVisible(!z);
        }
    }
}
