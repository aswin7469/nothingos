package com.android.settings.datetime.timezone;

import android.content.Context;
import android.content.IntentFilter;
import android.icu.text.LocaleDisplayNames;

public class RegionPreferenceController extends BaseTimeZonePreferenceController {
    private static final String PREFERENCE_KEY = "region";
    private final LocaleDisplayNames mLocaleDisplayNames;
    private String mRegionId = "";

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

    public RegionPreferenceController(Context context) {
        super(context, PREFERENCE_KEY);
        this.mLocaleDisplayNames = LocaleDisplayNames.getInstance(context.getResources().getConfiguration().getLocales().get(0));
    }

    public CharSequence getSummary() {
        return this.mLocaleDisplayNames.regionDisplayName(this.mRegionId);
    }

    public void setRegionId(String str) {
        this.mRegionId = str;
    }

    public String getRegionId() {
        return this.mRegionId;
    }
}
