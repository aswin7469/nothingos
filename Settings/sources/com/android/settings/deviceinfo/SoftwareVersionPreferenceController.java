package com.android.settings.deviceinfo;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;

public class SoftwareVersionPreferenceController extends BasePreferenceController {
    private static final String PREF_KEY = "software_version";

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getPreferenceKey() {
        return PREF_KEY;
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

    public SoftwareVersionPreferenceController(Context context) {
        super(context, PREF_KEY);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        preferenceScreen.findPreference(PREF_KEY).setSummary(getSummary());
    }

    public CharSequence getSummary() {
        String string = this.mContext.getString(R$string.device_info_default);
        String string2 = Utils.getString(this.mContext, "ext_meta_software_version");
        return (string2 == null || string2.isEmpty()) ? string : string2;
    }

    public int getAvailabilityStatus() {
        return Utils.isSupportCTPA(this.mContext) ? 0 : 3;
    }
}
