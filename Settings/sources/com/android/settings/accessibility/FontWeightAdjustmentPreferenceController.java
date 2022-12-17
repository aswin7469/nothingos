package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.R$string;
import com.android.settings.accessibility.TextReadingResetController;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.core.instrumentation.SettingsStatsLog;

public class FontWeightAdjustmentPreferenceController extends TogglePreferenceController implements TextReadingResetController.ResetStateListener {
    static final int BOLD_TEXT_ADJUSTMENT = 300;
    private int mEntryPoint;

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

    public FontWeightAdjustmentPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "font_weight_adjustment", 0) == BOLD_TEXT_ADJUSTMENT;
    }

    public boolean setChecked(boolean z) {
        SettingsStatsLog.write(454, AccessibilityStatsLogUtils.convertToItemKeyName(getPreferenceKey()), z ? 1 : 0, AccessibilityStatsLogUtils.convertToEntryPoint(this.mEntryPoint));
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "font_weight_adjustment", z ? BOLD_TEXT_ADJUSTMENT : 0);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_accessibility;
    }

    public void resetState() {
        setChecked(false);
    }

    /* access modifiers changed from: package-private */
    public void setEntryPoint(int i) {
        this.mEntryPoint = i;
    }
}
