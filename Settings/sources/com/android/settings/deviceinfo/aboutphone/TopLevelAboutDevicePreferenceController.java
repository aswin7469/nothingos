package com.android.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.deviceinfo.DeviceNamePreferenceController;

public class TopLevelAboutDevicePreferenceController extends BasePreferenceController {
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

    public TopLevelAboutDevicePreferenceController(Context context, String str) {
        super(context, str);
    }

    public CharSequence getSummary() {
        return new DeviceNamePreferenceController(this.mContext, "unused_key").getSummary();
    }
}
