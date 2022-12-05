package com.nt.settings.about;

import android.content.Context;
import android.content.Intent;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.deviceinfo.BuildNumberPreferenceController;
import com.android.settings.search.BaseSearchIndexProvider;
/* loaded from: classes2.dex */
public class NtAboutPhoneSoftwareInfoFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_about_phone_software_info);
    private BuildNumberPreferenceController mBuildNumberPreferenceController;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "NtAboutPhoneSoftwareInfoFragment";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1247;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.nt_about_phone_software_info;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (this.mBuildNumberPreferenceController.onActivityResult(i, i2, intent)) {
            return;
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        BuildNumberPreferenceController buildNumberPreferenceController = (BuildNumberPreferenceController) use(BuildNumberPreferenceController.class);
        this.mBuildNumberPreferenceController = buildNumberPreferenceController;
        buildNumberPreferenceController.setHost(this);
    }
}
