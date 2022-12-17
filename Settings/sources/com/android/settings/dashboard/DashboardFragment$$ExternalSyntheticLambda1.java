package com.android.settings.dashboard;

import com.android.settingslib.core.AbstractPreferenceController;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DashboardFragment$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ DashboardFragment f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ List f$2;

    public /* synthetic */ DashboardFragment$$ExternalSyntheticLambda1(DashboardFragment dashboardFragment, List list, List list2) {
        this.f$0 = dashboardFragment;
        this.f$1 = list;
        this.f$2 = list2;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$checkUiBlocker$2(this.f$1, this.f$2, (AbstractPreferenceController) obj);
    }
}
