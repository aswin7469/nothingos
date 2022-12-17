package com.android.settings.enterprise;

import com.android.settings.applications.ApplicationFeatureProvider;

/* renamed from: com.android.settings.enterprise.AdminGrantedPermissionsPreferenceControllerBase$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0950x5f15e78f implements ApplicationFeatureProvider.NumberOfAppsCallback {
    public final /* synthetic */ Boolean[] f$0;

    public /* synthetic */ C0950x5f15e78f(Boolean[] boolArr) {
        this.f$0 = boolArr;
    }

    public final void onNumberOfAppsResult(int i) {
        AdminGrantedPermissionsPreferenceControllerBase.lambda$isAvailable$1(this.f$0, i);
    }
}
