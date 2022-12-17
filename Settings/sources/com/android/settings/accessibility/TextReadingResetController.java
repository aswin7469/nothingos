package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import android.view.View;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$id;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.instrumentation.SettingsStatsLog;
import com.android.settingslib.widget.LayoutPreference;

class TextReadingResetController extends BasePreferenceController {
    private int mEntryPoint;
    private final View.OnClickListener mOnResetClickListener;

    interface ResetStateListener {
        void resetState();
    }

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

    TextReadingResetController(Context context, String str, View.OnClickListener onClickListener) {
        super(context, str);
        this.mOnResetClickListener = onClickListener;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        ((LayoutPreference) preferenceScreen.findPreference(getPreferenceKey())).findViewById(R$id.reset_button).setOnClickListener(new TextReadingResetController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$displayPreference$0(View view) {
        View.OnClickListener onClickListener = this.mOnResetClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(view);
            SettingsStatsLog.write(454, AccessibilityStatsLogUtils.convertToItemKeyName(getPreferenceKey()), -1, AccessibilityStatsLogUtils.convertToEntryPoint(this.mEntryPoint));
        }
    }

    /* access modifiers changed from: package-private */
    public void setEntryPoint(int i) {
        this.mEntryPoint = i;
    }
}
