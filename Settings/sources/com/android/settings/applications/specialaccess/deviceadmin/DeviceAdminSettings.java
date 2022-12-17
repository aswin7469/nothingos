package com.android.settings.applications.specialaccess.deviceadmin;

import android.os.Bundle;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;

public class DeviceAdminSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.device_admin_settings);

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "DeviceAdminSettings";
    }

    public int getMetricsCategory() {
        return 516;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        replaceEnterprisePreferenceScreenTitle("Settings.MANAGE_DEVICE_ADMIN_APPS", R$string.manage_device_admin);
        replaceEnterpriseStringTitle("device_admin_footer", "Settings.NO_DEVICE_ADMINS", R$string.no_device_admins);
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.device_admin_settings;
    }
}
