package com.nt.settings.sound;

import android.content.Context;
import android.os.Bundle;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.notification.VibrateOnTouchPreferenceController;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class NtVibrationSettings extends DashboardFragment {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_vibration) { // from class: com.nt.settings.sound.NtVibrationSettings.1
        @Override // com.android.settings.search.BaseSearchIndexProvider
        public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
            return NtVibrationSettings.buildPreferenceControllers(context, null, null);
        }
    };

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 336;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.nt_vibration;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return NtVibrationSettings.class.getName();
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, this, getSettingsLifecycle());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static List<AbstractPreferenceController> buildPreferenceControllers(Context context, NtVibrationSettings ntVibrationSettings, Lifecycle lifecycle) {
        ArrayList arrayList = new ArrayList();
        VibrateOnTouchPreferenceController vibrateOnTouchPreferenceController = new VibrateOnTouchPreferenceController(context, ntVibrationSettings, lifecycle);
        NtNotificationVibrationPreferenceController ntNotificationVibrationPreferenceController = new NtNotificationVibrationPreferenceController(context, ntVibrationSettings, lifecycle);
        arrayList.add(vibrateOnTouchPreferenceController);
        arrayList.add(ntNotificationVibrationPreferenceController);
        return arrayList;
    }
}
