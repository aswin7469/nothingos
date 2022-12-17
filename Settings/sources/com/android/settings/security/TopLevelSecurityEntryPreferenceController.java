package com.android.settings.security;

import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.safetycenter.SafetyCenterManagerWrapper;

public class TopLevelSecurityEntryPreferenceController extends BasePreferenceController {
    private final SecuritySettingsFeatureProvider mSecuritySettingsFeatureProvider = FeatureFactory.getFactory(this.mContext).getSecuritySettingsFeatureProvider();

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

    public TopLevelSecurityEntryPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return !SafetyCenterManagerWrapper.get().isEnabled(this.mContext) ? 0 : 2;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        String alternativeSecuritySettingsFragmentClassname;
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return super.handlePreferenceTreeClick(preference);
        }
        if (!this.mSecuritySettingsFeatureProvider.hasAlternativeSecuritySettingsFragment() || (alternativeSecuritySettingsFragmentClassname = this.mSecuritySettingsFeatureProvider.getAlternativeSecuritySettingsFragmentClassname()) == null) {
            return super.handlePreferenceTreeClick(preference);
        }
        new SubSettingLauncher(this.mContext).setDestination(alternativeSecuritySettingsFragmentClassname).setSourceMetricsCategory(getMetricsCategory()).setIsSecondLayerPage(true).launch();
        return true;
    }
}
