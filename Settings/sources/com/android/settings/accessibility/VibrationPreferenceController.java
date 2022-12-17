package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.provider.Settings;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;

public class VibrationPreferenceController extends BasePreferenceController {
    private final boolean mHasVibrator;

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

    public VibrationPreferenceController(Context context, String str) {
        super(context, str);
        this.mHasVibrator = ((Vibrator) context.getSystemService(Vibrator.class)).hasVibrator();
    }

    public int getAvailabilityStatus() {
        return this.mHasVibrator ? 0 : 3;
    }

    public CharSequence getSummary() {
        int i;
        boolean z = true;
        if (Settings.System.getInt(this.mContext.getContentResolver(), "vibrate_on", 1) != 1) {
            z = false;
        }
        Context context = this.mContext;
        if (z) {
            i = R$string.accessibility_vibration_settings_state_on;
        } else {
            i = R$string.accessibility_vibration_settings_state_off;
        }
        return context.getText(i);
    }
}
