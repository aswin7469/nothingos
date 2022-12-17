package com.nothing.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.content.Intent;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.deviceinfo.BuildNumberPreferenceController;
import com.android.settings.search.BaseSearchIndexProvider;

public class DeviceSoftwareInfoFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.about_phone_software_info);
    private BuildNumberPreferenceController mBuildNumberPreferenceController;

    public String getLogTag() {
        return "DeviceSoftwareInfoFragment";
    }

    public int getMetricsCategory() {
        return 1247;
    }

    public int getPreferenceScreenResId() {
        return R$xml.about_phone_software_info;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (!this.mBuildNumberPreferenceController.onActivityResult(i, i2, intent)) {
            super.onActivityResult(i, i2, intent);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        BuildNumberPreferenceController buildNumberPreferenceController = (BuildNumberPreferenceController) use(BuildNumberPreferenceController.class);
        this.mBuildNumberPreferenceController = buildNumberPreferenceController;
        buildNumberPreferenceController.setHost(this);
    }
}
