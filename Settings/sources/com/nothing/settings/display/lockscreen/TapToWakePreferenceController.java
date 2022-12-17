package com.nothing.settings.display.lockscreen;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.core.TogglePreferenceController;
import com.nothing.experience.AppTracking;
import com.nothing.settings.utils.CommonUtils;

public class TapToWakePreferenceController extends TogglePreferenceController {
    private static final String SETTING_KEY = "tap_gesture";

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

    public TapToWakePreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), SETTING_KEY, 1) != 0;
    }

    public boolean setChecked(boolean z) {
        CommonUtils.updateGestureMode(z ? 1 : 2);
        Bundle bundle = new Bundle();
        bundle.putInt("tap_wake", z ? 1 : 0);
        AppTracking.getInstance(this.mContext).logProductEvent("Display_Event", bundle);
        Log.d("event", "tap_wake:" + z);
        return Settings.Secure.putInt(this.mContext.getContentResolver(), SETTING_KEY, z);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        preference.setEnabled(getAvailabilityStatus() != 5);
        refreshSummary(preference);
    }
}
