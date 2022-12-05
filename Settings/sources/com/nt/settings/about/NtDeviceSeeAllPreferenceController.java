package com.nt.settings.about;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
/* loaded from: classes2.dex */
public class NtDeviceSeeAllPreferenceController extends BasePreferenceController {
    public static final String KEY = "nt_device_see_all";
    public static final String TAG = "NtDeviceSeeAll";
    private Preference mBtAddressPre;
    private Context mContext;
    private Preference mFactoryWifiMacAddressPre;
    private NtAboutPhoneImeiPreference mImeiPreference;
    private Preference mIpAddressPre;
    private Preference mPreference;
    private Preference mUpTimePre;
    private Preference mWifiMacAddressPre;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 1;
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

    public NtDeviceSeeAllPreferenceController(Context context) {
        super(context, KEY);
        this.mContext = context;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Log.d(TAG, "@_@ ------ displayPreference");
        this.mImeiPreference = (NtAboutPhoneImeiPreference) preferenceScreen.findPreference(NtImeiInfoPreferenceController.KEY);
        this.mIpAddressPre = preferenceScreen.findPreference("wifi_ip_address");
        this.mWifiMacAddressPre = preferenceScreen.findPreference("saved_accesspoints_wifi_mac_address");
        this.mFactoryWifiMacAddressPre = preferenceScreen.findPreference("wifi_mac_address");
        this.mBtAddressPre = preferenceScreen.findPreference("bt_address");
        this.mUpTimePre = preferenceScreen.findPreference("up_time");
        this.mPreference = preferenceScreen.findPreference(KEY);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (KEY.equals(preference.getKey())) {
            if (this.mImeiPreference != null) {
                Log.d(TAG, "@_@ ------ handlePreferenceTreeClick display imei");
                this.mImeiPreference.setVisible(true);
            }
            Preference preference2 = this.mIpAddressPre;
            if (preference2 != null) {
                preference2.setVisible(true);
            }
            Preference preference3 = this.mWifiMacAddressPre;
            if (preference3 != null) {
                preference3.setVisible(true);
            }
            Preference preference4 = this.mFactoryWifiMacAddressPre;
            if (preference4 != null) {
                preference4.setVisible(true);
            }
            Preference preference5 = this.mBtAddressPre;
            if (preference5 != null) {
                preference5.setVisible(true);
            }
            Preference preference6 = this.mUpTimePre;
            if (preference6 != null) {
                preference6.setVisible(true);
            }
            Preference preference7 = this.mPreference;
            if (preference7 != null) {
                preference7.setVisible(false);
            }
        }
        return super.handlePreferenceTreeClick(preference);
    }
}
