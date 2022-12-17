package com.android.settings.enterprise;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.R$bool;
import com.android.settings.R$plurals;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;

public class ManageDeviceAdminPreferenceController extends BasePreferenceController {
    private final DevicePolicyManager mDevicePolicyManager = ((DevicePolicyManager) this.mContext.getSystemService(DevicePolicyManager.class));
    private final EnterprisePrivacyFeatureProvider mFeatureProvider;

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

    public ManageDeviceAdminPreferenceController(Context context, String str) {
        super(context, str);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getEnterprisePrivacyFeatureProvider(context);
    }

    public CharSequence getSummary() {
        int numberOfActiveDeviceAdminsForCurrentUserAndManagedProfile = this.mFeatureProvider.getNumberOfActiveDeviceAdminsForCurrentUserAndManagedProfile();
        if (numberOfActiveDeviceAdminsForCurrentUserAndManagedProfile == 0) {
            return this.mDevicePolicyManager.getResources().getString("Settings.NUMBER_OF_DEVICE_ADMINS_NONE", new ManageDeviceAdminPreferenceController$$ExternalSyntheticLambda0(this));
        }
        return this.mContext.getResources().getQuantityString(R$plurals.number_of_device_admins, numberOfActiveDeviceAdminsForCurrentUserAndManagedProfile, new Object[]{Integer.valueOf(numberOfActiveDeviceAdminsForCurrentUserAndManagedProfile)});
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$getSummary$0() {
        return this.mContext.getResources().getString(R$string.number_of_device_admins_none);
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R$bool.config_show_manage_device_admin) ? 0 : 3;
    }
}
