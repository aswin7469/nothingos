package com.nothing.settings.display.refreshrate;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;

public class DisplayRefreshRatePreferenceController extends BasePreferenceController {
    private Context mContext;

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

    public DisplayRefreshRatePreferenceController(Context context, String str) {
        super(context, str);
        this.mContext = context;
    }

    public CharSequence getSummary() {
        if (Math.round(Settings.System.getFloat(this.mContext.getContentResolver(), "peak_refresh_rate", (float) this.mContext.getResources().getInteger(17694794))) == Math.round(60.0f)) {
            return this.mContext.getString(R$string.nt_screen_refresh_rate_standard_title);
        }
        return this.mContext.getString(R$string.nt_screen_refresh_rate_high_title);
    }
}
