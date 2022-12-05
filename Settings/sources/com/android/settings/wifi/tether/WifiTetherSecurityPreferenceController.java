package com.android.settings.wifi.tether;

import android.content.Context;
import android.net.wifi.SoftApCapability;
import android.net.wifi.SoftApConfiguration;
import android.net.wifi.WifiManager;
import android.util.FeatureFlagUtils;
import android.util.Log;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.wifi.tether.WifiTetherBasePreferenceController;
import java.util.LinkedHashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class WifiTetherSecurityPreferenceController extends WifiTetherBasePreferenceController implements WifiManager.SoftApCallback {
    boolean mIsDualSapSupported;
    private int mSecurityValue;
    private Map<Integer, String> mSecurityMap = new LinkedHashMap();
    boolean mIsWpa3Supported = true;
    boolean mIsOweSapSupported = true;
    private String[] securityNames = this.mContext.getResources().getStringArray(R.array.wifi_tether_security);
    private String[] securityValues = this.mContext.getResources().getStringArray(R.array.wifi_tether_security_values);

    public WifiTetherSecurityPreferenceController(Context context, WifiTetherBasePreferenceController.OnTetherConfigUpdateListener onTetherConfigUpdateListener) {
        super(context, onTetherConfigUpdateListener);
        this.mIsDualSapSupported = false;
        for (int i = 0; i < this.securityNames.length; i++) {
            this.mSecurityMap.put(Integer.valueOf(Integer.parseInt(this.securityValues[i])), this.securityNames[i]);
        }
        this.mWifiManager.registerSoftApCallback(context.getMainExecutor(), this);
        this.mIsDualSapSupported = this.mWifiManager.isBridgedApConcurrencySupported();
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return FeatureFlagUtils.isEnabled(this.mContext, "settings_tether_all_in_one") ? "wifi_tether_security_2" : "wifi_tether_security";
    }

    @Override // com.android.settings.wifi.tether.WifiTetherBasePreferenceController
    public void updateDisplay() {
        int i;
        Preference preference = this.mPreference;
        if (preference == null) {
            return;
        }
        ListPreference listPreference = (ListPreference) preference;
        SoftApConfiguration softApConfiguration = this.mWifiManager.getSoftApConfiguration();
        for (int i2 = 0; i2 < this.securityNames.length; i2++) {
            this.mSecurityMap.put(Integer.valueOf(Integer.parseInt(this.securityValues[i2])), this.securityNames[i2]);
        }
        listPreference.setEntries((CharSequence[]) this.mSecurityMap.values().stream().toArray(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda11.INSTANCE));
        listPreference.setEntryValues((CharSequence[]) this.mSecurityMap.keySet().stream().map(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda0.INSTANCE).toArray(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda7.INSTANCE));
        if (softApConfiguration.getBand() != 5 || !this.mSecurityMap.keySet().removeIf(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda14.INSTANCE)) {
            i = 1;
        } else {
            listPreference.setEntries((CharSequence[]) this.mSecurityMap.values().stream().toArray(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda5.INSTANCE));
            listPreference.setEntryValues((CharSequence[]) this.mSecurityMap.keySet().stream().map(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda2.INSTANCE).toArray(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda8.INSTANCE));
            i = 3;
        }
        if (!this.mIsWpa3Supported && this.mSecurityMap.keySet().removeIf(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda13.INSTANCE)) {
            listPreference.setEntries((CharSequence[]) this.mSecurityMap.values().stream().toArray(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda10.INSTANCE));
            listPreference.setEntryValues((CharSequence[]) this.mSecurityMap.keySet().stream().map(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda3.INSTANCE).toArray(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda9.INSTANCE));
        } else if ((!this.mIsDualSapSupported || !this.mIsOweSapSupported) && this.mSecurityMap.keySet().removeIf(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda12.INSTANCE)) {
            listPreference.setEntries((CharSequence[]) this.mSecurityMap.values().stream().toArray(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda6.INSTANCE));
            listPreference.setEntryValues((CharSequence[]) this.mSecurityMap.keySet().stream().map(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda1.INSTANCE).toArray(WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda4.INSTANCE));
        }
        int securityType = this.mWifiManager.getSoftApConfiguration().getSecurityType();
        if (this.mSecurityMap.get(Integer.valueOf(securityType)) != null) {
            i = securityType;
        }
        this.mSecurityValue = i;
        listPreference.setSummary(this.mSecurityMap.get(Integer.valueOf(i)));
        listPreference.setValue(String.valueOf(this.mSecurityValue));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$0(int i) {
        return new CharSequence[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$updateDisplay$1(Integer num) {
        return Integer.toString(num.intValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$2(int i) {
        return new CharSequence[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateDisplay$3(Integer num) {
        return num.intValue() < 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$4(int i) {
        return new CharSequence[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$updateDisplay$5(Integer num) {
        return Integer.toString(num.intValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$6(int i) {
        return new CharSequence[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateDisplay$7(Integer num) {
        return num.intValue() > 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$8(int i) {
        return new CharSequence[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$updateDisplay$9(Integer num) {
        return Integer.toString(num.intValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$10(int i) {
        return new CharSequence[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateDisplay$11(Integer num) {
        return num.intValue() > 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$12(int i) {
        return new CharSequence[i];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$updateDisplay$13(Integer num) {
        return Integer.toString(num.intValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$14(int i) {
        return new CharSequence[i];
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        int parseInt = Integer.parseInt((String) obj);
        this.mSecurityValue = parseInt;
        preference.setSummary(this.mSecurityMap.get(Integer.valueOf(parseInt)));
        WifiTetherBasePreferenceController.OnTetherConfigUpdateListener onTetherConfigUpdateListener = this.mListener;
        if (onTetherConfigUpdateListener != null) {
            onTetherConfigUpdateListener.onTetherConfigUpdated(this);
            return true;
        }
        return true;
    }

    public void onCapabilityChanged(SoftApCapability softApCapability) {
        boolean areFeaturesSupported = softApCapability.areFeaturesSupported(4L);
        if (!areFeaturesSupported) {
            Log.i("wifi_tether_security", "WPA3 SAE is not supported on this device");
        }
        boolean areFeaturesSupported2 = softApCapability.areFeaturesSupported(512L);
        if (!areFeaturesSupported2) {
            Log.i("wifi_tether_security", "OWE not supported.");
        }
        if (this.mIsWpa3Supported != areFeaturesSupported || this.mIsOweSapSupported != areFeaturesSupported2) {
            this.mIsWpa3Supported = areFeaturesSupported;
            this.mIsOweSapSupported = areFeaturesSupported2;
            updateDisplay();
        }
        this.mWifiManager.unregisterSoftApCallback(this);
    }

    public int getSecurityType() {
        return this.mSecurityValue;
    }

    public boolean isOweDualSapSupported() {
        return this.mIsDualSapSupported && this.mIsOweSapSupported;
    }
}
