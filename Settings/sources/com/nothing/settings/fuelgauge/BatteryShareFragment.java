package com.nothing.settings.fuelgauge;

import android.content.Context;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class BatteryShareFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.nt_battery_share) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return BatteryShareFragment.buildPreferenceControllers(context, (BatteryShareFragment) null, (Lifecycle) null);
        }
    };

    public int getMetricsCategory() {
        return 1821;
    }

    public int getPreferenceScreenResId() {
        return R$xml.nt_battery_share;
    }

    public String getLogTag() {
        return BatteryShareFragment.class.getName();
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, this, getSettingsLifecycle());
    }

    /* access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, BatteryShareFragment batteryShareFragment, Lifecycle lifecycle) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new AllowBatterySharePreferenceController(context));
        arrayList.add(new BatteryShareSizePreferenceController(context));
        return arrayList;
    }

    public void onDestroy() {
        super.onDestroy();
        getPreferenceControllers().forEach(new BatteryShareFragment$$ExternalSyntheticLambda0());
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$onDestroy$0(AbstractPreferenceController abstractPreferenceController) {
        if (abstractPreferenceController instanceof BatteryShareSizePreferenceController) {
            ((BatteryShareSizePreferenceController) abstractPreferenceController).releaseChargingReceiver();
        }
    }
}
