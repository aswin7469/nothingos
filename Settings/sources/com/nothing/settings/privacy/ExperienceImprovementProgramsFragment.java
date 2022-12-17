package com.nothing.settings.privacy;

import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class ExperienceImprovementProgramsFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.experience_improvement_programs);

    public String getLogTag() {
        return "NtPrivacyExperienceImprovementProgramsFragment";
    }

    public int getMetricsCategory() {
        return 0;
    }

    public int getPreferenceScreenResId() {
        return R$xml.experience_improvement_programs;
    }
}
