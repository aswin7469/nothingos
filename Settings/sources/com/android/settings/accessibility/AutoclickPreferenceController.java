package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;

public class AutoclickPreferenceController extends BasePreferenceController {
    public int getAvailabilityStatus() {
        return 0;
    }

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

    public AutoclickPreferenceController(Context context, String str) {
        super(context, str);
    }

    public CharSequence getSummary() {
        boolean z = false;
        if (Settings.Secure.getInt(this.mContext.getContentResolver(), "accessibility_autoclick_enabled", 0) == 1) {
            z = true;
        }
        if (!z) {
            return this.mContext.getResources().getText(R$string.accessibility_feature_state_off);
        }
        return ToggleAutoclickPreferenceFragment.getAutoclickPreferenceSummary(this.mContext.getResources(), Settings.Secure.getInt(this.mContext.getContentResolver(), "accessibility_autoclick_delay", 600));
    }
}
