package com.android.settings.dashboard;

import android.content.ContentResolver;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DashboardFragment$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ DashboardFragment f$0;
    public final /* synthetic */ ContentResolver f$1;

    public /* synthetic */ DashboardFragment$$ExternalSyntheticLambda5(DashboardFragment dashboardFragment, ContentResolver contentResolver) {
        this.f$0 = dashboardFragment;
        this.f$1 = contentResolver;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$onStart$7(this.f$1, (DynamicDataObserver) obj);
    }
}