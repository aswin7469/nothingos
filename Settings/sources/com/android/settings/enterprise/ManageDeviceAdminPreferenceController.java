package com.android.settings.enterprise;

import android.content.Context;
import android.content.IntentFilter;
import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
/* loaded from: classes.dex */
public class ManageDeviceAdminPreferenceController extends BasePreferenceController {
    private final EnterprisePrivacyFeatureProvider mFeatureProvider;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ManageDeviceAdminPreferenceController(Context context, String str) {
        super(context, str);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getEnterprisePrivacyFeatureProvider(context);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        int numberOfActiveDeviceAdminsForCurrentUserAndManagedProfile = this.mFeatureProvider.getNumberOfActiveDeviceAdminsForCurrentUserAndManagedProfile();
        if (numberOfActiveDeviceAdminsForCurrentUserAndManagedProfile == 0) {
            return this.mContext.getResources().getString(R.string.number_of_device_admins_none);
        }
        return this.mContext.getResources().getQuantityString(R.plurals.number_of_device_admins, numberOfActiveDeviceAdminsForCurrentUserAndManagedProfile, Integer.valueOf(numberOfActiveDeviceAdminsForCurrentUserAndManagedProfile));
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R.bool.config_show_manage_device_admin) ? 0 : 3;
    }
}
