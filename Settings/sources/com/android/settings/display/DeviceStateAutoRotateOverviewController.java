package com.android.settings.display;

import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.android.settings.core.BasePreferenceController;

public class DeviceStateAutoRotateOverviewController extends BasePreferenceController {
    private static final String ACCESSIBILITY_PREF_KEY = "device_state_auto_rotate_accessibility";

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

    public DeviceStateAutoRotateOverviewController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return isAvailableInternal() ? 0 : 3;
    }

    private boolean isAvailableInternal() {
        if (isA11yPage()) {
            return DeviceStateAutoRotationHelper.isDeviceStateRotationEnabledForA11y(this.mContext);
        }
        return DeviceStateAutoRotationHelper.isDeviceStateRotationEnabled(this.mContext);
    }

    private boolean isA11yPage() {
        return TextUtils.equals(getPreferenceKey(), ACCESSIBILITY_PREF_KEY);
    }
}
