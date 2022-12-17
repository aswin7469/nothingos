package com.android.settings.bluetooth;

import android.content.Context;
import android.util.Log;
import com.android.settings.R$xml;
import com.android.settings.connecteddevice.ConnectedDeviceDashboardFragment;
import com.android.settings.dashboard.RestrictedDashboardFragment;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

public class GroupBluetoothFragment extends RestrictedDashboardFragment implements BluetoothCallback {
    private static final boolean DBG = ConnectedDeviceDashboardFragment.DBG_GROUP;
    private Context mCtx;
    private int mGroupId = -1;
    private GroupUtils mGroupUtils;
    protected LocalBluetoothManager mLocalManager;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "GroupBluetoothFragment";
    }

    public int getMetricsCategory() {
        return 0;
    }

    public GroupBluetoothFragment() {
        super("no_config_bluetooth");
    }

    public void onAttach(Context context) {
        this.mGroupId = getArguments().getInt("group_id");
        this.mCtx = context;
        this.mGroupUtils = new GroupUtils(this.mCtx);
        LocalBluetoothManager localBtManager = Utils.getLocalBtManager(this.mCtx);
        this.mLocalManager = localBtManager;
        localBtManager.getEventManager().registerCallback(this);
        super.onAttach(context);
        if (this.mGroupId <= -1) {
            Log.w("GroupBluetoothFragment", "onAttach mGroupId not valid " + this.mGroupId);
            finish();
            return;
        }
        use(GroupBluetoothDetailsButtonsController.class);
    }

    public void onResume() {
        super.onResume();
        finishFragmentIfNecessary();
    }

    public void onDetach() {
        super.onDetach();
        this.mLocalManager.getEventManager().unregisterCallback(this);
    }

    /* access modifiers changed from: package-private */
    public void finishFragmentIfNecessary() {
        int i = this.mGroupId;
        if (i < 0 || this.mGroupUtils.isHideGroupOptions(i)) {
            Log.w("GroupBluetoothFragment", "finishFragment");
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.bluetooth_group_details_fragment;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        if (DBG) {
            Log.d("GroupBluetoothFragment", "createPreferenceControllers mGroupId " + this.mGroupId);
        }
        ArrayList arrayList = new ArrayList();
        Lifecycle settingsLifecycle = getSettingsLifecycle();
        arrayList.add(new GroupBluetoothDetailsButtonsController(context, this, this.mGroupId, settingsLifecycle));
        arrayList.add(new GroupBluetoothDevicesAvailableMediaController(context, this, settingsLifecycle, this.mGroupId));
        arrayList.add(new GroupBluetoothDevicesConnectedController(context, this, settingsLifecycle, this.mGroupId));
        arrayList.add(new GroupBluetoothDevicesBondedController(context, this, settingsLifecycle, this.mGroupId));
        return arrayList;
    }

    public void onBluetoothStateChanged(int i) {
        if (DBG) {
            Log.d("GroupBluetoothFragment", "onBluetoothStateChanged bluetoothState " + i);
        }
        if (13 == i) {
            finish();
        }
    }
}
