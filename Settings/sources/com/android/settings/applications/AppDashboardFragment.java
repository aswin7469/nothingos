package com.android.settings.applications;

import android.content.Context;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppDashboardFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider() {
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context, boolean z) {
            SearchIndexableResource searchIndexableResource = new SearchIndexableResource(context);
            searchIndexableResource.xmlResId = R$xml.apps;
            return Arrays.asList(new SearchIndexableResource[]{searchIndexableResource});
        }

        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return AppDashboardFragment.buildPreferenceControllers(context);
        }
    };
    private AppsPreferenceController mAppsPreferenceController;
    private String mIconPackPackageName = null;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "AppDashboardFragment";
    }

    public int getMetricsCategory() {
        return 65;
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new AppsPreferenceController(context));
        return arrayList;
    }

    public int getHelpResource() {
        return R$string.help_url_apps_and_notifications;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.apps;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        ((SpecialAppAccessPreferenceController) use(SpecialAppAccessPreferenceController.class)).setSession(getSettingsLifecycle());
        AppsPreferenceController appsPreferenceController = (AppsPreferenceController) use(AppsPreferenceController.class);
        this.mAppsPreferenceController = appsPreferenceController;
        appsPreferenceController.setFragment(this);
        getSettingsLifecycle().addObserver(this.mAppsPreferenceController);
        getSettingsLifecycle().addObserver((HibernatedAppsPreferenceController) use(HibernatedAppsPreferenceController.class));
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context);
    }

    public void onResume() {
        super.onResume();
        String string = Settings.System.getString(getContentResolver(), "nothing_icon_pack");
        Log.d("AppDashboardFragment", "onResume packageName:" + string + ", mIconPackPackageName:" + this.mIconPackPackageName);
        if (!TextUtils.equals(this.mIconPackPackageName, string)) {
            ((AppsPreferenceController) use(AppsPreferenceController.class)).refreshUi();
            this.mIconPackPackageName = string;
        }
    }
}
