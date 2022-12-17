package com.nothing.settings.deviceinfo.aboutphone;

import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class AboutDeviceRegulatoryFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.about_device_regulatory_labels);

    public String getLogTag() {
        return "AboutDeviceRegulatoryFragment";
    }

    public int getMetricsCategory() {
        return 0;
    }

    public int getPreferenceScreenResId() {
        return R$xml.about_device_regulatory_labels;
    }
}
