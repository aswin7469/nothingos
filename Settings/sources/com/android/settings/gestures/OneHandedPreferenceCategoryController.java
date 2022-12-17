package com.android.settings.gestures;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;

public class OneHandedPreferenceCategoryController extends BasePreferenceController {
    private Preference mPreference;

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

    public OneHandedPreferenceCategoryController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        int i;
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = findPreference;
        if (OneHandedSettingsUtils.getNavigationBarMode(this.mContext) == 0) {
            i = R$string.one_handed_mode_use_shortcut_category;
        } else {
            i = R$string.one_handed_mode_swipe_down_category;
        }
        findPreference.setTitle(i);
    }
}
