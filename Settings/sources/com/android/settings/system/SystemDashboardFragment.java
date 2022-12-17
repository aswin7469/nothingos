package com.android.settings.system;

import android.content.Context;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.BaseSearchIndexProvider;
import java.util.Arrays;
import java.util.List;

public class SystemDashboardFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean z) {
            SearchIndexableResource searchIndexableResource = new SearchIndexableResource(context);
            searchIndexableResource.xmlResId = R$xml.system_dashboard_fragment;
            return Arrays.asList(new SearchIndexableResource[]{searchIndexableResource});
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "SystemDashboardFrag";
    }

    public int getMetricsCategory() {
        return 744;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (getVisiblePreferenceCount(preferenceScreen) == preferenceScreen.getInitialExpandedChildrenCount() + 1) {
            preferenceScreen.setInitialExpandedChildrenCount(Integer.MAX_VALUE);
        }
        showRestrictionDialog();
    }

    public void showRestrictionDialog() {
        Bundle arguments = getArguments();
        if (arguments != null && arguments.getBoolean("show_aware_dialog_disabled", false)) {
            FeatureFactory.getFactory(getContext()).getAwareFeatureProvider().showRestrictionDialog(this);
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.system_dashboard_fragment;
    }

    public int getHelpResource() {
        return R$string.help_url_system_dashboard;
    }

    private int getVisiblePreferenceCount(PreferenceGroup preferenceGroup) {
        int i = 0;
        for (int i2 = 0; i2 < preferenceGroup.getPreferenceCount(); i2++) {
            Preference preference = preferenceGroup.getPreference(i2);
            if (preference instanceof PreferenceGroup) {
                i += getVisiblePreferenceCount((PreferenceGroup) preference);
            } else if (preference.isVisible()) {
                i++;
            }
        }
        return i;
    }
}
