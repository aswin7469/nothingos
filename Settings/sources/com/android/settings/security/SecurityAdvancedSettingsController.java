package com.android.settings.security;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.CrossProfileApps;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;

public class SecurityAdvancedSettingsController extends BasePreferenceController {
    private final CrossProfileApps mCrossProfileApps;
    private final DevicePolicyManager mDevicePolicyManager;

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

    public SecurityAdvancedSettingsController(Context context, String str) {
        super(context, str);
        this.mCrossProfileApps = (CrossProfileApps) context.getSystemService(CrossProfileApps.class);
        this.mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
    }

    public CharSequence getSummary() {
        if (isWorkProfilePresent()) {
            return this.mDevicePolicyManager.getResources().getString("Settings.MORE_SECURITY_SETTINGS_WORK_PROFILE_SUMMARY", new SecurityAdvancedSettingsController$$ExternalSyntheticLambda0(this));
        }
        return this.mContext.getResources().getString(R$string.security_advanced_settings_no_work_profile_settings_summary);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getSummary$0() {
        return this.mContext.getResources().getString(R$string.security_advanced_settings_work_profile_settings_summary);
    }

    private boolean isWorkProfilePresent() {
        return !this.mCrossProfileApps.getTargetUserProfiles().isEmpty();
    }
}
