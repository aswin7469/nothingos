package com.android.settings.security;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.R$bool;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;

public class ScreenPinningPreferenceController extends BasePreferenceController {
    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ScreenPinningPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R$bool.config_show_screen_pinning_settings) ? 0 : 2;
    }

    public CharSequence getSummary() {
        if (Settings.System.getInt(this.mContext.getContentResolver(), "lock_to_app_enabled", 0) != 0) {
            return this.mContext.getText(R$string.screen_pinning_switch_on_text);
        }
        return this.mContext.getText(R$string.screen_pinning_switch_off_text);
    }
}
