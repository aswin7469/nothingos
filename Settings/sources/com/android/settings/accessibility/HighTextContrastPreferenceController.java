package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R$string;
import com.android.settings.accessibility.TextReadingResetController;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.core.instrumentation.SettingsStatsLog;

public class HighTextContrastPreferenceController extends TogglePreferenceController implements TextReadingResetController.ResetStateListener {
    private int mEntryPoint;
    private SwitchPreference mSwitchPreference;

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public HighTextContrastPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "high_text_contrast_enabled", 0) == 1;
    }

    public boolean setChecked(boolean z) {
        SettingsStatsLog.write(454, AccessibilityStatsLogUtils.convertToItemKeyName(getPreferenceKey()), z ? 1 : 0, AccessibilityStatsLogUtils.convertToEntryPoint(this.mEntryPoint));
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "high_text_contrast_enabled", z);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_accessibility;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mSwitchPreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public void resetState() {
        setChecked(false);
        updateState(this.mSwitchPreference);
    }

    /* access modifiers changed from: package-private */
    public void setEntryPoint(int i) {
        this.mEntryPoint = i;
    }
}
