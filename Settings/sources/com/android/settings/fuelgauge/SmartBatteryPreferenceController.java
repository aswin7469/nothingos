package com.android.settings.fuelgauge;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.SwitchPreference;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;

public class SmartBatteryPreferenceController extends BasePreferenceController implements Preference.OnPreferenceChangeListener {
    private static final String KEY_SMART_BATTERY = "smart_battery";
    private static final int OFF = 0;

    /* renamed from: ON */
    private static final int f183ON = 1;
    private PowerUsageFeatureProvider mPowerUsageFeatureProvider;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public boolean isPublicSlice() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public SmartBatteryPreferenceController(Context context) {
        super(context, KEY_SMART_BATTERY);
        this.mPowerUsageFeatureProvider = FeatureFactory.getFactory(context).getPowerUsageFeatureProvider(context);
    }

    public int getAvailabilityStatus() {
        return this.mPowerUsageFeatureProvider.isSmartBatterySupported() ? 0 : 3;
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), KEY_SMART_BATTERY);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_battery;
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        boolean z = true;
        if (Settings.Global.getInt(this.mContext.getContentResolver(), "adaptive_battery_management_enabled", 1) != 1) {
            z = false;
        }
        ((SwitchPreference) preference).setChecked(z);
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Settings.Global.putInt(this.mContext.getContentResolver(), "adaptive_battery_management_enabled", ((Boolean) obj).booleanValue() ? 1 : 0);
        return true;
    }
}
