package com.android.settings.location;

import android.content.Context;
import android.os.Bundle;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;

public class LocationWorkProfileSettings extends DashboardFragment {
    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "LocationWorkProfile";
    }

    public int getMetricsCategory() {
        return 1806;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.location_settings_workprofile;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        replaceEnterpriseStringTitle("managed_profile_location_switch", "Settings.WORK_PROFILE_LOCATION_SWITCH_TITLE", R$string.managed_profile_location_switch_title);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((AppLocationPermissionPreferenceController) use(AppLocationPermissionPreferenceController.class)).init(this);
        ((LocationForWorkPreferenceController) use(LocationForWorkPreferenceController.class)).init(this);
        ((RecentLocationAccessSeeAllButtonPreferenceController) use(RecentLocationAccessSeeAllButtonPreferenceController.class)).init(this);
        ((LocationSettingsFooterPreferenceController) use(LocationSettingsFooterPreferenceController.class)).init(this);
        int i = getArguments().getInt("profile");
        RecentLocationAccessPreferenceController recentLocationAccessPreferenceController = (RecentLocationAccessPreferenceController) use(RecentLocationAccessPreferenceController.class);
        recentLocationAccessPreferenceController.init(this);
        recentLocationAccessPreferenceController.setProfileType(i);
    }

    public int getHelpResource() {
        return R$string.help_url_location_access;
    }
}
