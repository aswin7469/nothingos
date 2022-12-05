package com.android.settings.connecteddevice;

import android.content.Context;
import com.android.settings.R;
import com.android.settings.bluetooth.GroupUtils;
import com.android.settings.dashboard.DashboardFragment;
/* loaded from: classes.dex */
public class GroupPreviouslyConnectedDeviceDashboardFragment extends DashboardFragment {
    private GroupUtils mGroupUtils;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "GroupPreviouslyConnectedDeviceDashboardFragment";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 0;
    }

    @Override // com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return R.string.group_help_url_previously_connected_devices;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.previously_connected_group_devices;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mGroupUtils = new GroupUtils(context);
        ((GroupSavedDeviceController) use(GroupSavedDeviceController.class)).init(this);
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (this.mGroupUtils.isHidePCGGroups()) {
            finish();
        }
    }
}
