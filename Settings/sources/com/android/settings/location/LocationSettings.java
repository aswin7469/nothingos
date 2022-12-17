package com.android.settings.location;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.SettingsActivity;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.location.LocationEnabler;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.SettingsMainSwitchBar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LocationSettings extends DashboardFragment implements LocationEnabler.LocationModeChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.location_settings);
    private ContentObserver mContentObserver;
    /* access modifiers changed from: private */
    public RecentLocationAccessPreferenceController mController;
    private LocationEnabler mLocationEnabler;
    private LocationSwitchBarController mSwitchBarController;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "LocationSettings";
    }

    public int getMetricsCategory() {
        return 63;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        SettingsMainSwitchBar switchBar = settingsActivity.getSwitchBar();
        switchBar.setTitle(getContext().getString(R$string.location_settings_primary_switch_title));
        switchBar.show();
        this.mSwitchBarController = new LocationSwitchBarController(settingsActivity, switchBar, getSettingsLifecycle());
        this.mLocationEnabler = new LocationEnabler(getContext(), this, getSettingsLifecycle());
        this.mContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
            public void onChange(boolean z) {
                LocationSettings.this.mController.updateShowSystem();
            }
        };
        getContentResolver().registerContentObserver(Settings.Secure.getUriFor("locationShowSystemOps"), false, this.mContentObserver);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((AppLocationPermissionPreferenceController) use(AppLocationPermissionPreferenceController.class)).init(this);
        RecentLocationAccessPreferenceController recentLocationAccessPreferenceController = (RecentLocationAccessPreferenceController) use(RecentLocationAccessPreferenceController.class);
        this.mController = recentLocationAccessPreferenceController;
        recentLocationAccessPreferenceController.init(this);
        ((RecentLocationAccessSeeAllButtonPreferenceController) use(RecentLocationAccessSeeAllButtonPreferenceController.class)).init(this);
        ((LocationForWorkPreferenceController) use(LocationForWorkPreferenceController.class)).init(this);
        ((LocationSettingsFooterPreferenceController) use(LocationSettingsFooterPreferenceController.class)).init(this);
        ((AgpsPreferenceController) use(AgpsPreferenceController.class)).init(this);
    }

    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(this.mContentObserver);
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.location_settings;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        replaceEnterpriseStringTitle("managed_profile_location_switch", "Settings.WORK_PROFILE_LOCATION_SWITCH_TITLE", R$string.managed_profile_location_switch_title);
    }

    public void onLocationModeChanged(int i, boolean z) {
        if (this.mLocationEnabler.isEnabled(i)) {
            scrollToPreference("recent_location_access");
        }
    }

    static void addPreferencesSorted(List<Preference> list, PreferenceGroup preferenceGroup) {
        Collections.sort(list, Comparator.comparing(new LocationSettings$$ExternalSyntheticLambda0()));
        for (Preference addPreference : list) {
            preferenceGroup.addPreference(addPreference);
        }
    }

    public int getHelpResource() {
        return R$string.help_url_location_access;
    }
}
