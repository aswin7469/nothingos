package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;

public class AccessibilityButtonFooterPreferenceController extends AccessibilityFooterPreferenceController {
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

    public AccessibilityButtonFooterPreferenceController(Context context, String str) {
        super(context, str);
    }

    /* access modifiers changed from: protected */
    public String getLearnMoreText() {
        return this.mContext.getString(R$string.f136xe1bf3399);
    }

    /* access modifiers changed from: protected */
    public String getIntroductionTitle() {
        return this.mContext.getString(R$string.accessibility_button_about_title);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        int i;
        if (AccessibilityUtil.isGestureNavigateEnabled(this.mContext)) {
            i = R$string.accessibility_button_gesture_description;
        } else {
            i = R$string.accessibility_button_description;
        }
        ((AccessibilityFooterPreference) preferenceScreen.findPreference(getPreferenceKey())).setTitle(this.mContext.getText(i));
        super.displayPreference(preferenceScreen);
    }
}
