package com.nothing.settings.apps;

import android.os.Bundle;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class AssistantFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.nt_assistant_settings) {
    };

    public int getMetricsCategory() {
        return 65;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public int getPreferenceScreenResId() {
        return R$xml.nt_assistant_settings;
    }

    public String getLogTag() {
        return AssistantFragment.class.getName();
    }
}
