package com.nt.settings.aod;

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
import android.widget.Switch;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import com.nothing.experience.AppTracking;
/* loaded from: classes2.dex */
public class NtAODSwitchPreferenceController extends BasePreferenceController implements OnMainSwitchChangeListener {
    private static final String AOD_SUPPRESSED_TOKEN = "winddown";
    private static final int MY_USER = UserHandle.myUserId();
    private static final String PROP_AWARE_AVAILABLE = "ro.vendor.aware_available";
    private AmbientDisplayConfiguration mConfig;
    private MainSwitchPreference mPreference;
    private final int ON = 1;
    private final int OFF = 0;

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
    public boolean isPublicSlice() {
        return true;
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NtAODSwitchPreferenceController(Context context, String str) {
        super(context, str);
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return (!isAvailable(getConfig()) || SystemProperties.getBoolean(PROP_AWARE_AVAILABLE, false)) ? 3 : 0;
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        boolean z = false;
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "doze_always_on", 0) == 1) {
            z = true;
        }
        this.mPreference.updateStatus(z);
        refreshSummary(preference);
    }

    @Override // com.android.settings.slices.Sliceable
    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), "ambient_display_always_on");
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        Context context = this.mContext;
        return context.getText(isAodSuppressedByBedtime(context) ? R.string.aware_summary_when_bedtime_on : R.string.doze_always_on_summary);
    }

    public NtAODSwitchPreferenceController setConfig(AmbientDisplayConfiguration ambientDisplayConfiguration) {
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
            return ((PowerManager) context.getSystemService(PowerManager.class)).isAmbientDisplaySuppressedForTokenByApp(AOD_SUPPRESSED_TOKEN, context.getPackageManager().getApplicationInfo(context.getString(17039920), 0).uid);
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = mainSwitchPreference;
        mainSwitchPreference.addOnSwitchChangeListener(this);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.settingslib.widget.OnMainSwitchChangeListener
    public void onSwitchChanged(Switch r3, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putInt("aod", z ? 1 : 0);
        AppTracking.getInstance(this.mContext).logProductEvent("Display_Event", bundle);
        Settings.Secure.putInt(this.mContext.getContentResolver(), "doze_always_on", z);
    }
}
