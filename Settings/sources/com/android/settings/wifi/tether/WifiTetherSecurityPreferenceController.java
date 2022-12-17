package com.android.settings.wifi.tether;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.SoftApConfiguration;
import android.net.wifi.WifiManager;
import android.util.FeatureFlagUtils;
import android.util.Log;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.settings.R$array;
import com.android.settings.wifi.tether.WifiTetherBasePreferenceController;
import java.util.LinkedHashMap;
import java.util.Map;

public class WifiTetherSecurityPreferenceController extends WifiTetherBasePreferenceController implements WifiManager.SoftApCallback {
    boolean mIsDualSapSupported;
    boolean mIsOweSapSupported = true;
    boolean mIsWpa3Supported = true;
    private Map<Integer, String> mSecurityMap = new LinkedHashMap();
    private int mSecurityValue;
    private Resources mWifiRes;
    private Context mWifiResContext;
    private String[] securityNames;
    private String[] securityValues;

    public WifiTetherSecurityPreferenceController(Context context, WifiTetherBasePreferenceController.OnTetherConfigUpdateListener onTetherConfigUpdateListener) {
        super(context, onTetherConfigUpdateListener);
        this.mIsDualSapSupported = false;
        this.securityNames = this.mContext.getResources().getStringArray(R$array.wifi_tether_security);
        this.securityValues = this.mContext.getResources().getStringArray(R$array.wifi_tether_security_values);
        for (int i = 0; i < this.securityNames.length; i++) {
            this.mSecurityMap.put(Integer.valueOf(Integer.parseInt(this.securityValues[i])), this.securityNames[i]);
        }
        this.mWifiManager.registerSoftApCallback(context.getMainExecutor(), this);
        this.mIsDualSapSupported = this.mWifiManager.isBridgedApConcurrencySupported();
        try {
            Context createPackageContext = this.mContext.createPackageContext("com.android.wifi.resources", 3);
            this.mWifiResContext = createPackageContext;
            this.mWifiRes = createPackageContext.getResources();
        } catch (Exception e) {
            Log.e("wifi_tether_security", "exception in createPackageContext: " + e);
            throw new RuntimeException(e);
        }
    }

    private int getWifiResId(String str, String str2) {
        Resources resources = this.mWifiRes;
        if (resources != null) {
            return resources.getIdentifier(str2, str, "com.android.wifi.resources");
        }
        Log.e("wifi_tether_security", "no WIFI resources, fail to get " + str + "." + str2);
        return -1;
    }

    public String getPreferenceKey() {
        return FeatureFlagUtils.isEnabled(this.mContext, "settings_tether_all_in_one") ? "wifi_tether_security_2" : "wifi_tether_security";
    }

    public void updateDisplay() {
        int i;
        Preference preference = this.mPreference;
        if (preference != null) {
            ListPreference listPreference = (ListPreference) preference;
            SoftApConfiguration softApConfiguration = this.mWifiManager.getSoftApConfiguration();
            for (int i2 = 0; i2 < this.securityNames.length; i2++) {
                this.mSecurityMap.put(Integer.valueOf(Integer.parseInt(this.securityValues[i2])), this.securityNames[i2]);
            }
            listPreference.setEntries((CharSequence[]) this.mSecurityMap.values().stream().toArray(new WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda0()));
            listPreference.setEntryValues((CharSequence[]) this.mSecurityMap.keySet().stream().map(new WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda6()).toArray(new WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda7()));
            int i3 = 4;
            if ((softApConfiguration.getBand() & 4) == 0 || !this.mSecurityMap.keySet().removeIf(new WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda8())) {
                i = 1;
            } else {
                listPreference.setEntries((CharSequence[]) this.mSecurityMap.values().stream().toArray(new WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda9()));
                listPreference.setEntryValues((CharSequence[]) this.mSecurityMap.keySet().stream().map(new C1505x66b74d22()).toArray(new C1506x66b74d23()));
                i = 3;
            }
            if (!this.mIsWpa3Supported && this.mSecurityMap.keySet().removeIf(new C1507x66b74d24())) {
                listPreference.setEntries((CharSequence[]) this.mSecurityMap.values().stream().toArray(new C1508x66b74d25()));
                listPreference.setEntryValues((CharSequence[]) this.mSecurityMap.keySet().stream().map(new C1509x66b74d26()).toArray(new WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda1()));
            } else if ((!this.mIsDualSapSupported || !this.mIsOweSapSupported) && this.mSecurityMap.keySet().removeIf(new WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda2())) {
                listPreference.setEntries((CharSequence[]) this.mSecurityMap.values().stream().toArray(new WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda3()));
                listPreference.setEntryValues((CharSequence[]) this.mSecurityMap.keySet().stream().map(new WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda4()).toArray(new WifiTetherSecurityPreferenceController$$ExternalSyntheticLambda5()));
            }
            int securityType = this.mWifiManager.getSoftApConfiguration().getSecurityType();
            if (securityType != 5) {
                i3 = securityType;
            }
            if (this.mSecurityMap.get(Integer.valueOf(i3)) != null) {
                i = i3;
            }
            this.mSecurityValue = i;
            listPreference.setSummary(this.mSecurityMap.get(Integer.valueOf(i)));
            listPreference.setValue(String.valueOf(this.mSecurityValue));
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$0(int i) {
        return new CharSequence[i];
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$2(int i) {
        return new CharSequence[i];
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateDisplay$3(Integer num) {
        return num.intValue() < 3;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$4(int i) {
        return new CharSequence[i];
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$6(int i) {
        return new CharSequence[i];
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateDisplay$7(Integer num) {
        return num.intValue() > 1;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$8(int i) {
        return new CharSequence[i];
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$10(int i) {
        return new CharSequence[i];
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateDisplay$11(Integer num) {
        return num.intValue() > 3;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$12(int i) {
        return new CharSequence[i];
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ CharSequence[] lambda$updateDisplay$14(int i) {
        return new CharSequence[i];
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        int parseInt = Integer.parseInt((String) obj);
        this.mSecurityValue = parseInt;
        preference.setSummary((CharSequence) this.mSecurityMap.get(Integer.valueOf(parseInt)));
        WifiTetherBasePreferenceController.OnTetherConfigUpdateListener onTetherConfigUpdateListener = this.mListener;
        if (onTetherConfigUpdateListener == null) {
            return true;
        }
        onTetherConfigUpdateListener.onTetherConfigUpdated(this);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0018, code lost:
        r5 = r4.mWifiRes;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCapabilityChanged(android.net.wifi.SoftApCapability r5) {
        /*
            r4 = this;
            r0 = 4
            boolean r0 = r5.areFeaturesSupported(r0)
            java.lang.String r1 = "wifi_tether_security"
            if (r0 != 0) goto L_0x0010
            java.lang.String r2 = "WPA3 SAE is not supported on this device"
            android.util.Log.i(r1, r2)
        L_0x0010:
            r2 = 2048(0x800, double:1.0118E-320)
            boolean r5 = r5.areFeaturesSupported(r2)
            if (r5 != 0) goto L_0x002d
            android.content.res.Resources r5 = r4.mWifiRes
            if (r5 == 0) goto L_0x002b
            java.lang.String r2 = "bool"
            java.lang.String r3 = "config_vendor_wifi_softap_owe_supported"
            int r2 = r4.getWifiResId(r2, r3)
            boolean r5 = r5.getBoolean(r2)
            if (r5 == 0) goto L_0x002b
            goto L_0x002d
        L_0x002b:
            r5 = 0
            goto L_0x002e
        L_0x002d:
            r5 = 1
        L_0x002e:
            if (r5 != 0) goto L_0x0035
            java.lang.String r2 = "OWE not supported."
            android.util.Log.i(r1, r2)
        L_0x0035:
            boolean r1 = r4.mIsWpa3Supported
            if (r1 != r0) goto L_0x003d
            boolean r1 = r4.mIsOweSapSupported
            if (r1 == r5) goto L_0x0044
        L_0x003d:
            r4.mIsWpa3Supported = r0
            r4.mIsOweSapSupported = r5
            r4.updateDisplay()
        L_0x0044:
            android.net.wifi.WifiManager r5 = r4.mWifiManager
            r5.unregisterSoftApCallback(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.wifi.tether.WifiTetherSecurityPreferenceController.onCapabilityChanged(android.net.wifi.SoftApCapability):void");
    }

    public int getSecurityType() {
        return this.mSecurityValue;
    }

    public boolean isOweDualSapSupported() {
        return this.mIsDualSapSupported && this.mIsOweSapSupported;
    }
}
