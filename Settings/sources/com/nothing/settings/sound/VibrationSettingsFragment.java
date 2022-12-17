package com.nothing.settings.sound;

import android.content.Context;
import com.android.settings.R$xml;
import com.android.settings.accessibility.RingVibrationTogglePreferenceController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class VibrationSettingsFragment extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R$xml.nt_vibration) {
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return VibrationSettingsFragment.buildPreferenceControllers(context, (VibrationSettingsFragment) null, (Lifecycle) null);
        }
    };

    public int getMetricsCategory() {
        return 336;
    }

    public int getPreferenceScreenResId() {
        return R$xml.nt_vibration;
    }

    public String getLogTag() {
        return VibrationSettingsFragment.class.getName();
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, this, getSettingsLifecycle());
    }

    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, VibrationSettingsFragment vibrationSettingsFragment, Lifecycle lifecycle) {
        ArrayList arrayList = new ArrayList();
        RingVibrationTogglePreferenceController ringVibrationTogglePreferenceController = new RingVibrationTogglePreferenceController(context, "vibrate_for_calls");
        VibrateOnTouchPreferenceController vibrateOnTouchPreferenceController = new VibrateOnTouchPreferenceController(context, vibrationSettingsFragment, lifecycle);
        NotificationVibrationPreferenceController notificationVibrationPreferenceController = new NotificationVibrationPreferenceController(context, vibrationSettingsFragment, lifecycle);
        arrayList.add(ringVibrationTogglePreferenceController);
        arrayList.add(vibrateOnTouchPreferenceController);
        arrayList.add(notificationVibrationPreferenceController);
        return arrayList;
    }
}
