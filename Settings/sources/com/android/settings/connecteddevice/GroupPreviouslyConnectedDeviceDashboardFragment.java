package com.android.settings.connecteddevice;

import android.content.Context;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.bluetooth.GroupUtils;
import com.android.settings.dashboard.DashboardFragment;

public class GroupPreviouslyConnectedDeviceDashboardFragment extends DashboardFragment {
    private GroupUtils mGroupUtils;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "GroupPreviouslyConnectedDeviceDashboardFragment";
    }

    public int getMetricsCategory() {
        return 0;
    }

    public int getHelpResource() {
        return R$string.group_help_url_previously_connected_devices;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.previously_connected_group_devices;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        this.mGroupUtils = new GroupUtils(context);
        ((GroupSavedDeviceController) use(GroupSavedDeviceController.class)).init(this);
    }

    public void onResume() {
        super.onResume();
        if (this.mGroupUtils.isHidePCGGroups()) {
            finish();
        }
    }
}
