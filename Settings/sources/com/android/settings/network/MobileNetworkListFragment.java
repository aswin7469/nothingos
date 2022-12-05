package com.android.settings.network;

import android.content.Context;
import android.os.UserManager;
import android.provider.SearchIndexableResource;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class MobileNetworkListFragment extends DashboardFragment {
    static final String KEY_PREFERENCE_CATEGORY_DOWNLOADED_SIM = "provider_model_downloaded_sim_category";
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() { // from class: com.android.settings.network.MobileNetworkListFragment.1
        @Override // com.android.settings.search.BaseSearchIndexProvider, com.android.settingslib.search.Indexable$SearchIndexProvider
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean z) {
            int i;
            ArrayList arrayList = new ArrayList();
            SearchIndexableResource searchIndexableResource = new SearchIndexableResource(context);
            if (Utils.isProviderModelEnabled(context)) {
                i = R.xml.network_provider_sims_list;
            } else {
                i = R.xml.mobile_network_list;
            }
            searchIndexableResource.xmlResId = i;
            arrayList.add(searchIndexableResource);
            return arrayList;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.android.settings.search.BaseSearchIndexProvider
        public boolean isPageSearchEnabled(Context context) {
            return ((UserManager) context.getSystemService(UserManager.class)).isAdminUser();
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "NetworkListFragment";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1627;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        if (Utils.isProviderModelEnabled(getContext())) {
            return R.xml.network_provider_sims_list;
        }
        return R.xml.mobile_network_list;
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        if (Utils.isProviderModelEnabled(getContext())) {
            arrayList.add(new NetworkProviderSimsCategoryController(context, "provider_model_sim_category", getSettingsLifecycle()));
            arrayList.add(new NetworkProviderDownloadedSimsCategoryController(context, KEY_PREFERENCE_CATEGORY_DOWNLOADED_SIM, getSettingsLifecycle()));
        } else {
            arrayList.add(new MobileNetworkListController(getContext(), mo959getLifecycle()));
        }
        return arrayList;
    }
}
