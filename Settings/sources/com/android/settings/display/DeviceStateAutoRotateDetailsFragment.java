package com.android.settings.display;

import android.content.Context;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.search.Indexable$SearchIndexProvider;
import com.android.settingslib.search.SearchIndexableRaw;
import java.util.List;

public class DeviceStateAutoRotateDetailsFragment extends DashboardFragment {
    public static final Indexable$SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.device_state_auto_rotate_settings) {
        public List<SearchIndexableRaw> getRawDataToIndex(Context context, boolean z) {
            return DeviceStateAutoRotationHelper.getRawDataToIndex(context, z);
        }
    };

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "DeviceStateAutoRotateDetailsFragment";
    }

    public int getMetricsCategory() {
        return 1867;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.device_state_auto_rotate_settings;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        DeviceStateAutoRotationHelper.initControllers(getLifecycle(), useAll(DeviceStateAutoRotateSettingController.class));
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return DeviceStateAutoRotationHelper.createPreferenceControllers(context);
    }
}
