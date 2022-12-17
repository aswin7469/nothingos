package com.android.settings.dashboard;

import android.net.Uri;
import androidx.preference.Preference;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DashboardFeatureProviderImpl$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ DashboardFeatureProviderImpl f$0;
    public final /* synthetic */ Uri f$1;
    public final /* synthetic */ DynamicDataObserver f$2;
    public final /* synthetic */ Preference f$3;

    public /* synthetic */ DashboardFeatureProviderImpl$$ExternalSyntheticLambda5(DashboardFeatureProviderImpl dashboardFeatureProviderImpl, Uri uri, DynamicDataObserver dynamicDataObserver, Preference preference) {
        this.f$0 = dashboardFeatureProviderImpl;
        this.f$1 = uri;
        this.f$2 = dynamicDataObserver;
        this.f$3 = preference;
    }

    public final void run() {
        this.f$0.lambda$refreshSwitch$9(this.f$1, this.f$2, this.f$3);
    }
}
