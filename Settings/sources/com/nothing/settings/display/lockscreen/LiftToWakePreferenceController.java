package com.nothing.settings.display.lockscreen;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import androidx.preference.Preference;
import com.android.settings.core.TogglePreferenceController;
import com.nothing.experience.AppTracking;

public class LiftToWakePreferenceController extends TogglePreferenceController {
    private static final String SETTING_KEY = "wake_gesture_enabled";

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

    public LiftToWakePreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), SETTING_KEY, 0) != 0;
    }

    public boolean setChecked(boolean z) {
        Bundle bundle = new Bundle();
        bundle.putInt("lift_wake", z ? 1 : 0);
        AppTracking.getInstance(this.mContext).logProductEvent("Display_Event", bundle);
        return Settings.Secure.putInt(this.mContext.getContentResolver(), SETTING_KEY, z);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setEnabled(getAvailabilityStatus() != 5);
        refreshSummary(preference);
    }
}
