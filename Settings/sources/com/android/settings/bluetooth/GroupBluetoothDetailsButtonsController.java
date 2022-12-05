package com.android.settings.bluetooth;

import android.content.Context;
import android.util.Log;
import android.view.View;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.connecteddevice.ConnectedDeviceDashboardFragment;
import com.android.settings.widget.GroupOptionsPreference;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class GroupBluetoothDetailsButtonsController extends GroupBluetoothDetailsController {
    private static final boolean DBG = ConnectedDeviceDashboardFragment.DBG_GROUP;
    private static String TAG = "GroupBluetoothDetailsButtonsController";
    private int mGroupId;
    private GroupOptionsPreference mGroupOptions;
    private int mGroupSize;
    private GroupUtils mGroupUtils;
    private boolean mIsUpdate = false;
    private int mDiscoveredSize = 0;
    private boolean isRefreshClicked = false;
    private ArrayList<CachedBluetoothDevice> mDevicesList = new ArrayList<>();

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return "group_options";
    }

    public GroupBluetoothDetailsButtonsController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, int i, Lifecycle lifecycle) {
        super(context, preferenceFragmentCompat, i, lifecycle);
        this.mGroupSize = -1;
        this.mGroupId = i;
        GroupUtils groupUtils = new GroupUtils(context);
        this.mGroupUtils = groupUtils;
        this.mGroupSize = groupUtils.getGroupSize(this.mGroupId);
    }

    @Override // com.android.settings.bluetooth.GroupBluetoothDetailsController
    protected void init(PreferenceScreen preferenceScreen) {
        if (DBG) {
            Log.d(TAG, "init ");
        }
        GroupOptionsPreference groupOptionsPreference = (GroupOptionsPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mGroupOptions = groupOptionsPreference;
        groupOptionsPreference.setTextViewText(((GroupBluetoothDetailsController) this).mContext.getString(R.string.group_id) + this.mGroupUtils.getGroupTitle(this.mGroupId));
        this.mGroupOptions.setForgetButtonText(R.string.forget_group);
        this.mGroupOptions.setForgetButtonOnClickListener(new View.OnClickListener() { // from class: com.android.settings.bluetooth.GroupBluetoothDetailsButtonsController$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupBluetoothDetailsButtonsController.this.lambda$init$0(view);
            }
        });
        boolean z = true;
        this.mGroupOptions.setForgetButtonEnabled(true);
        this.mGroupOptions.setTexStatusText(R.string.active);
        this.mGroupOptions.setConnectButtonText(R.string.connect_group);
        this.mGroupOptions.setConnectButtonOnClickListener(new View.OnClickListener() { // from class: com.android.settings.bluetooth.GroupBluetoothDetailsButtonsController$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupBluetoothDetailsButtonsController.this.lambda$init$1(view);
            }
        });
        this.mGroupOptions.setDisconnectButtonText(R.string.disconnect_group);
        this.mGroupOptions.setDisconnectButtonOnClickListener(new View.OnClickListener() { // from class: com.android.settings.bluetooth.GroupBluetoothDetailsButtonsController$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupBluetoothDetailsButtonsController.this.lambda$init$2(view);
            }
        });
        this.mGroupOptions.setCancelRefreshButtonText(R.string.cancel_refresh_group);
        this.mGroupOptions.setCancelRefreshButtonOnClickListener(new View.OnClickListener() { // from class: com.android.settings.bluetooth.GroupBluetoothDetailsButtonsController$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupBluetoothDetailsButtonsController.this.lambda$init$3(view);
            }
        });
        this.mGroupOptions.setCancelRefreshButtonVisible(false);
        this.mGroupOptions.setRefreshButtonText(R.string.refresh_group);
        this.mGroupOptions.setRefreshButtonOnClickListener(new View.OnClickListener() { // from class: com.android.settings.bluetooth.GroupBluetoothDetailsButtonsController$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                GroupBluetoothDetailsButtonsController.this.lambda$init$4(view);
            }
        });
        this.mGroupOptions.setRefreshButtonVisible(false);
        this.mIsUpdate = true;
        if (this.mGroupUtils.getAnyBCConnectedDevice(this.mGroupId) == null) {
            z = false;
        }
        this.mGroupOptions.setAddSourceGroupButtonText(R.string.add_source_group);
        this.mGroupOptions.setAddSourceGroupButtonEnabled(z);
        this.mGroupOptions.setAddSourceGroupButtonVisible(z);
        if (z) {
            this.mGroupOptions.setAddSourceGroupButtonOnClickListener(new View.OnClickListener() { // from class: com.android.settings.bluetooth.GroupBluetoothDetailsButtonsController$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    GroupBluetoothDetailsButtonsController.this.lambda$init$5(view);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$0(View view) {
        onForgetButtonPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1(View view) {
        onConnectButtonPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$2(View view) {
        onDisConnectButtonPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$3(View view) {
        onCacelRefreshButtonPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$4(View view) {
        onRefreshButtonPressed();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$5(View view) {
        onAddSourceGroupButtonPressed();
    }

    private void onAddSourceGroupButtonPressed() {
        this.mGroupUtils.launchAddSourceGroup(this.mGroupId);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (DBG) {
            String str = TAG;
            Log.d(str, "onConnectionStateChanged cachedDevice " + cachedBluetoothDevice + " state " + i);
        }
        if (this.mGroupUtils.isUpdate(this.mGroupId, cachedBluetoothDevice)) {
            refresh();
        }
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        if (DBG) {
            String str = TAG;
            Log.d(str, "onProfileConnectionStateChanged cachedDevice " + cachedBluetoothDevice + " state " + i);
        }
        if (this.mGroupUtils.isUpdate(this.mGroupId, cachedBluetoothDevice)) {
            refresh();
        }
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        boolean removeDevice;
        if (i == 12) {
            removeDevice = this.mGroupUtils.addDevice(this.mDevicesList, this.mGroupId, cachedBluetoothDevice);
        } else {
            removeDevice = i == 10 ? this.mGroupUtils.removeDevice(this.mDevicesList, this.mGroupId, cachedBluetoothDevice) : false;
        }
        if (removeDevice) {
            this.mDiscoveredSize = this.mDevicesList.size();
        }
        if (DBG) {
            String str = TAG;
            Log.d(str, "onDeviceBondStateChanged cachedDevice " + cachedBluetoothDevice + " name " + cachedBluetoothDevice.getName() + " bondState " + i + " isUpdated " + removeDevice + " mDiscoveredSize " + this.mDiscoveredSize);
        }
        if (removeDevice) {
            updateProgressScan();
            refresh();
        }
    }

    @Override // com.android.settings.bluetooth.GroupBluetoothDetailsController, com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        if (DBG) {
            Log.d(TAG, "onStop ");
        }
        super.onStop();
        disableScanning();
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onGroupDiscoveryStatusChanged(int i, int i2, int i3) {
        if (DBG) {
            String str = TAG;
            Log.d(str, "onSetDiscoveryStatusChanged " + i + " status :" + i2 + " Reason :" + i3);
        }
        if (i == this.mGroupId) {
            if (this.isRefreshClicked && i2 == 1) {
                this.isRefreshClicked = false;
            }
            updateProgressScan();
        }
    }

    @Override // com.android.settings.bluetooth.GroupBluetoothDetailsController
    protected void loadDevices() {
        ArrayList<CachedBluetoothDevice> cahcedDevice = this.mGroupUtils.getCahcedDevice(this.mGroupId);
        this.mDevicesList = cahcedDevice;
        this.mDiscoveredSize = cahcedDevice.size();
        if (DBG) {
            String str = TAG;
            Log.d(str, "loadDevices mGroupId " + this.mGroupId + " mGroupSize " + this.mGroupSize + " mDiscoveredSize " + this.mDiscoveredSize);
        }
        updateProgressScan();
    }

    @Override // com.android.settings.bluetooth.GroupBluetoothDetailsController
    protected void refresh() {
        ArrayList<CachedBluetoothDevice> arrayList = new ArrayList(this.mDevicesList);
        if (DBG) {
            Log.d(TAG, "updateFlags list " + arrayList + " size " + arrayList.size());
        }
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        for (CachedBluetoothDevice cachedBluetoothDevice : arrayList) {
            if (DBG) {
                Log.d(TAG, "refresh cacheDevice " + cachedBluetoothDevice + " connected " + cachedBluetoothDevice.isConnected() + " busy " + cachedBluetoothDevice.isBusy());
            }
            if (!z && cachedBluetoothDevice.isBusy()) {
                this.mIsUpdate = true;
                z = true;
            }
            if (!z3 && cachedBluetoothDevice.isConnected()) {
                this.mIsUpdate = true;
                z3 = true;
            } else if (!z2 && !cachedBluetoothDevice.isConnected()) {
                this.mIsUpdate = true;
                z2 = true;
            }
            if (!z4 && cachedBluetoothDevice.isConnected() && (cachedBluetoothDevice.isActiveDevice(2) || cachedBluetoothDevice.isActiveDevice(1) || cachedBluetoothDevice.isActiveDevice(21))) {
                z4 = true;
            }
        }
        if (DBG) {
            Log.d(TAG, "refresh isBusy " + z + " showConnect " + z2 + " showDisconnect :" + z3 + " isActive " + z4 + " mIsUpdate " + this.mIsUpdate);
        }
        if (!this.mIsUpdate) {
            return;
        }
        this.mGroupOptions.setConnectButtonEnabled(!z);
        this.mGroupOptions.setDisconnectButtonEnabled(!z);
        this.mGroupOptions.setRefreshButtonEnabled(!z);
        this.mGroupOptions.setCancelRefreshButtonEnabled(!z);
        this.mGroupOptions.setDisconnectButtonEnabled(z3);
        this.mGroupOptions.setDisconnectButtonVisible(z3);
        this.mGroupOptions.setConnectButtonEnabled(z2);
        this.mGroupOptions.setConnectButtonVisible(z2);
        this.mGroupOptions.setTvStatusVisible(z4);
        this.mIsUpdate = false;
    }

    private void onForgetButtonPressed() {
        if (DBG) {
            Log.d(TAG, "onForgetButtonPressed");
        }
        GroupForgetDialogFragment.newInstance(this.mGroupId).show(this.mFragment.getFragmentManager(), "GroupForgetDialogFragment");
    }

    private void onConnectButtonPressed() {
        disableScanning();
        boolean connectGroup = this.mGroupUtils.connectGroup(this.mGroupId);
        if (DBG) {
            String str = TAG;
            Log.d(str, "onConnectButtonPressed connect " + connectGroup);
        }
    }

    private void onDisConnectButtonPressed() {
        disableScanning();
        boolean disconnectGroup = this.mGroupUtils.disconnectGroup(this.mGroupId);
        if (DBG) {
            String str = TAG;
            Log.d(str, "onDisConnectButtonPressed disconnect " + disconnectGroup);
        }
    }

    private void onRefreshButtonPressed() {
        this.isRefreshClicked = this.mGroupUtils.startGroupDiscovery(this.mGroupId);
        if (DBG) {
            String str = TAG;
            Log.d(str, "onRefreshButtonPressed isRefreshClicked " + this.isRefreshClicked);
        }
    }

    private void onCacelRefreshButtonPressed() {
        this.isRefreshClicked = false;
        boolean stopGroupDiscovery = this.mGroupUtils.stopGroupDiscovery(this.mGroupId);
        if (DBG) {
            String str = TAG;
            Log.d(str, "onCacelRefreshButtonPressed stopDiscovery " + stopGroupDiscovery);
        }
    }

    private void updateProgressScan() {
        int i = this.mGroupSize;
        boolean z = i > 0 && i > this.mDiscoveredSize;
        boolean isGroupDiscoveryInProgress = this.mGroupUtils.isGroupDiscoveryInProgress(this.mGroupId);
        if (!z) {
            this.mGroupOptions.setProgressScanVisible(z);
            this.mGroupOptions.setCancelRefreshButtonVisible(z);
            this.mGroupOptions.setRefreshButtonVisible(z);
        } else if (isGroupDiscoveryInProgress) {
            this.mGroupOptions.setProgressScanVisible(true);
            this.mGroupOptions.setRefreshButtonVisible(false);
            this.mGroupOptions.setCancelRefreshButtonVisible(true);
        } else {
            this.mGroupOptions.setProgressScanVisible(false);
            this.mGroupOptions.setRefreshButtonVisible(true);
            this.mGroupOptions.setCancelRefreshButtonVisible(false);
        }
        if (DBG) {
            String str = TAG;
            Log.d(str, "updateProgressScan showRefresh " + z + ", isRefreshing " + isGroupDiscoveryInProgress + " mDiscoveredSize " + this.mDiscoveredSize);
        }
    }

    private void disableScanning() {
        if (this.isRefreshClicked) {
            this.mGroupUtils.stopGroupDiscovery(this.mGroupId);
        }
    }
}
