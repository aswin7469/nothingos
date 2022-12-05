package com.android.settings.location;

import android.content.Context;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.location.LocationEnabler;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.widget.SettingsMainSwitchBar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* loaded from: classes.dex */
public class LocationSettings extends DashboardFragment implements LocationEnabler.LocationModeChangeListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.location_settings);
    private LocationEnabler mLocationEnabler;
    private LocationSwitchBarController mSwitchBarController;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "LocationSettings";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 63;
    }

    @Override // com.android.settings.SettingsPreferenceFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        SettingsActivity settingsActivity = (SettingsActivity) getActivity();
        SettingsMainSwitchBar switchBar = settingsActivity.getSwitchBar();
        switchBar.setTitle(getContext().getString(R.string.location_settings_primary_switch_title));
        switchBar.show();
        this.mSwitchBarController = new LocationSwitchBarController(settingsActivity, switchBar, getSettingsLifecycle());
        this.mLocationEnabler = new LocationEnabler(getContext(), this, getSettingsLifecycle());
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        ((AppLocationPermissionPreferenceController) use(AppLocationPermissionPreferenceController.class)).init(this);
        ((RecentLocationAccessPreferenceController) use(RecentLocationAccessPreferenceController.class)).init(this);
        ((RecentLocationAccessSeeAllButtonPreferenceController) use(RecentLocationAccessSeeAllButtonPreferenceController.class)).init(this);
        ((LocationForWorkPreferenceController) use(LocationForWorkPreferenceController.class)).init(this);
        ((LocationSettingsFooterPreferenceController) use(LocationSettingsFooterPreferenceController.class)).init(this);
        ((AgpsPreferenceController) use(AgpsPreferenceController.class)).init(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.location_settings;
    }

    @Override // com.android.settings.location.LocationEnabler.LocationModeChangeListener
    public void onLocationModeChanged(int i, boolean z) {
        if (this.mLocationEnabler.isEnabled(i)) {
            scrollToPreference("recent_location_access");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void addPreferencesSorted(List<Preference> list, PreferenceGroup preferenceGroup) {
        Collections.sort(list, Comparator.comparing(LocationSettings$$ExternalSyntheticLambda0.INSTANCE));
        for (Preference preference : list) {
            preferenceGroup.addPreference(preference);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$addPreferencesSorted$0(Preference preference) {
        return preference.getTitle().toString();
    }

    @Override // com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return R.string.help_url_location_access;
    }
}
