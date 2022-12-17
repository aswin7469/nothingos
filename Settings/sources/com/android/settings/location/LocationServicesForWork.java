package com.android.settings.location;

import android.content.Context;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class LocationServicesForWork extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.location_services_workprofile);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "LocationServicesForWork";
    }

    public int getMetricsCategory() {
        return 1868;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.location_services_workprofile;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((LocationInjectedServicesForWorkPreferenceController) use(LocationInjectedServicesForWorkPreferenceController.class)).init(this);
    }
}
