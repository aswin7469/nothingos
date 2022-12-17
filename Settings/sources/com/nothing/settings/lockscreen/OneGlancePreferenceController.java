package com.nothing.settings.lockscreen;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.core.TogglePreferenceController;

public class OneGlancePreferenceController extends TogglePreferenceController {
    private static final String SETTING_KEY = "nothing_widget_one_glance_weather";

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public int getSliceHighlightMenuRes() {
        return 0;
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public OneGlancePreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), SETTING_KEY, 1) != 0;
    }

    public boolean setChecked(boolean z) {
        return Settings.Global.putInt(this.mContext.getContentResolver(), SETTING_KEY, z ? 1 : 0);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setEnabled(getAvailabilityStatus() != 5);
        refreshSummary(preference);
    }
}
