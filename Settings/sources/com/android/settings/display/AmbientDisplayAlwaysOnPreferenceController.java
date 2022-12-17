package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.nothing.experience.AppTracking;
import com.nothing.settings.widget.PrimarySwitchPreference;

public class AmbientDisplayAlwaysOnPreferenceController extends TogglePreferenceController {
    private static final String AOD_SUPPRESSED_TOKEN = "winddown";
    private static final int MY_USER = UserHandle.myUserId();
    private static final String PROP_AWARE_AVAILABLE = "ro.vendor.aware_available";
    private final int OFF = 0;

    /* renamed from: ON */
    private final int f179ON = 1;
    private AmbientDisplayConfiguration mConfig;
    private Preference mPreference;

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

    public AmbientDisplayAlwaysOnPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        if (!isAvailable(getConfig()) || SystemProperties.getBoolean(PROP_AWARE_AVAILABLE, false)) {
            return 3;
        }
        return 0;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mPreference = preferenceScreen.findPreference("ambient_display_always_on");
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        Preference preference2 = this.mPreference;
        if (preference2 instanceof PrimarySwitchPreference) {
            ((PrimarySwitchPreference) preference2).setChecked(isChecked());
        }
        refreshSummary(preference);
    }

    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), "ambient_display_always_on");
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_display;
    }

    public boolean isChecked() {
        return getConfig().alwaysOnEnabled(MY_USER);
    }

    public boolean setChecked(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putInt("aod", z ? 1 : 0);
        AppTracking.getInstance(this.mContext).logProductEvent("Display_Event", bundle);
        Settings.Secure.putInt(this.mContext.getContentResolver(), "doze_always_on", z);
        return true;
    }

    public CharSequence getSummary() {
        int i;
        Context context = this.mContext;
        if (isAodSuppressedByBedtime(context)) {
            i = R$string.aware_summary_when_bedtime_on;
        } else {
            i = R$string.doze_always_on_summary;
        }
        return context.getText(i);
    }

    public AmbientDisplayAlwaysOnPreferenceController setConfig(AmbientDisplayConfiguration ambientDisplayConfiguration) {
        this.mConfig = ambientDisplayConfiguration;
        return this;
    }

    public static boolean isAvailable(AmbientDisplayConfiguration ambientDisplayConfiguration) {
        return ambientDisplayConfiguration.alwaysOnAvailableForUser(MY_USER);
    }

    private AmbientDisplayConfiguration getConfig() {
        if (this.mConfig == null) {
            this.mConfig = new AmbientDisplayConfiguration(this.mContext);
        }
        return this.mConfig;
    }

    public static boolean isAodSuppressedByBedtime(Context context) {
        try {
            return ((PowerManager) context.getSystemService(PowerManager.class)).isAmbientDisplaySuppressedForTokenByApp(AOD_SUPPRESSED_TOKEN, context.getPackageManager().getApplicationInfo(context.getString(17039951), 0).uid);
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }
}
