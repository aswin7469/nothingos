package com.android.settings.dashboard;

import android.net.Uri;
import androidx.preference.Preference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DashboardFeatureProviderImpl$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ DashboardFeatureProviderImpl f$0;
    public final /* synthetic */ Uri f$1;
    public final /* synthetic */ Preference f$2;
    public final /* synthetic */ DynamicDataObserver f$3;

    public /* synthetic */ DashboardFeatureProviderImpl$$ExternalSyntheticLambda6(DashboardFeatureProviderImpl dashboardFeatureProviderImpl, Uri uri, Preference preference, DynamicDataObserver dynamicDataObserver) {
        this.f$0 = dashboardFeatureProviderImpl;
        this.f$1 = uri;
        this.f$2 = preference;
        this.f$3 = dynamicDataObserver;
    }

    public final void run() {
        this.f$0.lambda$refreshSummary$4(this.f$1, this.f$2, this.f$3);
    }
}
