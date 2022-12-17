package com.android.settings.location;

import android.content.Context;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class LocationServices extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.location_services);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "LocationServicesSettings";
    }

    public int getMetricsCategory() {
        return 1868;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.location_services;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((LocationInjectedServicesPreferenceController) use(LocationInjectedServicesPreferenceController.class)).init(this);
    }
}
