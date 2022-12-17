package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.view.KeyCharacterMap;
import com.android.settings.R$string;
import com.android.settings.Utils;
import com.android.settings.core.TogglePreferenceController;

public class PowerButtonEndsCallPreferenceController extends TogglePreferenceController {
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

    public PowerButtonEndsCallPreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), "incall_power_button_behavior", 1) == 2;
    }

    public boolean setChecked(boolean z) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "incall_power_button_behavior", z ? 2 : 1);
    }

    public int getAvailabilityStatus() {
        return (!KeyCharacterMap.deviceHasKey(26) || !Utils.isVoiceCapable(this.mContext)) ? 3 : 0;
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_accessibility;
    }
}
