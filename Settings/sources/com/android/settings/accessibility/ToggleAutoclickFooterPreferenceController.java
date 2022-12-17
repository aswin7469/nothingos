package com.android.settings.accessibility;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.R$string;

public class ToggleAutoclickFooterPreferenceController extends AccessibilityFooterPreferenceController {
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

    public ToggleAutoclickFooterPreferenceController(Context context, String str) {
        super(context, str);
    }

    /* access modifiers changed from: protected */
    public String getLearnMoreText() {
        return this.mContext.getString(R$string.accessibility_autoclick_footer_learn_more_content_description);
    }

    /* access modifiers changed from: protected */
    public String getIntroductionTitle() {
        return this.mContext.getString(R$string.accessibility_autoclick_about_title);
    }

    /* access modifiers changed from: protected */
    public int getHelpResource() {
        return R$string.help_url_autoclick;
    }
}
