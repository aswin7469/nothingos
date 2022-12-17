package com.android.settings.accessibility;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.HelpUtils;

public class AccessibilityFooterPreferenceController extends BasePreferenceController {
    private int mHelpResource;
    private String mIntroductionTitle;
    private String mLearnMoreText;

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

    public AccessibilityFooterPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        updateFooterPreferences((AccessibilityFooterPreference) preferenceScreen.findPreference(getPreferenceKey()));
    }

    public void setupHelpLink(int i, String str) {
        this.mHelpResource = i;
        this.mLearnMoreText = str;
    }

    /* access modifiers changed from: protected */
    public int getHelpResource() {
        return this.mHelpResource;
    }

    /* access modifiers changed from: protected */
    public String getLearnMoreText() {
        return this.mLearnMoreText;
    }

    public void setIntroductionTitle(String str) {
        this.mIntroductionTitle = str;
    }

    /* access modifiers changed from: protected */
    public String getIntroductionTitle() {
        return this.mIntroductionTitle;
    }

    private void updateFooterPreferences(AccessibilityFooterPreference accessibilityFooterPreference) {
        Intent intent;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getIntroductionTitle());
        stringBuffer.append("\n\n");
        stringBuffer.append(accessibilityFooterPreference.getTitle());
        accessibilityFooterPreference.setContentDescription(stringBuffer);
        if (getHelpResource() != 0) {
            Context context = this.mContext;
            intent = HelpUtils.getHelpIntent(context, context.getString(getHelpResource()), this.mContext.getClass().getName());
        } else {
            intent = null;
        }
        if (intent != null) {
            accessibilityFooterPreference.setLearnMoreAction(new C0574xaf7e55af(intent));
            accessibilityFooterPreference.setLearnMoreText(getLearnMoreText());
            accessibilityFooterPreference.setLinkEnabled(true);
        } else {
            accessibilityFooterPreference.setLinkEnabled(false);
        }
        accessibilityFooterPreference.setSelectable(false);
    }
}
