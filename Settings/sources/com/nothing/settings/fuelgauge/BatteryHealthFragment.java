package com.nothing.settings.fuelgauge;

import android.content.Context;
import android.os.Bundle;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class BatteryHealthFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.nt_battery_health) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return BatteryHealthFragment.buildPreferenceControllers(context, (BatteryHealthFragment) null, (Lifecycle) null);
        }
    };

    public int getMetricsCategory() {
        return 1821;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public int getPreferenceScreenResId() {
        return R$xml.nt_battery_health;
    }

    public String getLogTag() {
        return BatteryHealthFragment.class.getName();
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, this, getSettingsLifecycle());
    }

    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, BatteryHealthFragment batteryHealthFragment, Lifecycle lifecycle) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BatteryHealthSettingsPreferenceController(context));
        return arrayList;
    }
}
