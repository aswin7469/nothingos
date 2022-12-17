package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;

public class LockscreenClockPreferenceController extends TogglePreferenceController {
    private static final String SETTING_KEY = "lockscreen_use_double_line_clock";

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

    public LockscreenClockPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), SETTING_KEY, 1) != 0;
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), SETTING_KEY, z ? 1 : 0);
    }

    public CharSequence getSummary() {
        return this.mContext.getText(R$string.lockscreen_double_line_clock_summary);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setEnabled(true);
        refreshSummary(preference);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_display;
    }
}
