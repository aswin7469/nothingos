package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.nothing.experience.AppTracking;

public class AutoBrightnessPreferenceController extends TogglePreferenceController {
    private final int DEFAULT_VALUE = 1;
    private final String SYSTEM_KEY = "screen_brightness_mode";

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

    public AutoBrightnessPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.System.getInt(this.mContext.getContentResolver(), "screen_brightness_mode", 1) == 1;
    }

    public boolean setChecked(boolean z) {
        Settings.System.putInt(this.mContext.getContentResolver(), "screen_brightness_mode", z ? 1 : 0);
        Bundle bundle = new Bundle();
        bundle.putInt("auto_bright", z);
        AppTracking.getInstance(this.mContext).logProductEvent("Display_Event", bundle);
        return true;
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(17891379) ? 1 : 3;
    }

    public CharSequence getSummary() {
        int i;
        Context context = this.mContext;
        if (isChecked()) {
            i = R$string.auto_brightness_summary_on;
        } else {
            i = R$string.auto_brightness_summary_off;
        }
        return context.getText(i);
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_display;
    }
}
