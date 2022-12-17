package com.android.settings.applications.specialaccess;

import android.os.Bundle;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class SpecialAccessSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.special_access);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "SpecialAccessSettings";
    }

    public int getMetricsCategory() {
        return 351;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        replaceEnterpriseStringTitle("interact_across_profiles", "Settings.CONNECTED_WORK_AND_PERSONAL_APPS_TITLE", R$string.interact_across_profiles_title);
        replaceEnterpriseStringTitle("device_administrators", "Settings.MANAGE_DEVICE_ADMIN_APPS", R$string.manage_device_admin);
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.special_access;
    }
}
