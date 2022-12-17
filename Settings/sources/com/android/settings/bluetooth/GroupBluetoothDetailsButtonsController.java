package com.android.settings.bluetooth;

import android.content.Context;
import android.util.Log;
import android.view.View;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.connecteddevice.ConnectedDeviceDashboardFragment;
import com.android.settings.widget.GroupOptionsPreference;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;

public class GroupBluetoothDetailsButtonsController extends GroupBluetoothDetailsController {
    private static final boolean DBG = ConnectedDeviceDashboardFragment.DBG_GROUP;
    private static String TAG = "GroupBluetoothDetailsButtonsController";
    private boolean isRefreshClicked = false;
    private ArrayList<CachedBluetoothDevice> mDevicesList = new ArrayList<>();
    private int mDiscoveredSize = 0;
    private int mGroupId;
    private GroupOptionsPreference mGroupOptions;
    private int mGroupSize = -1;
    private GroupUtils mGroupUtils;
    private boolean mIsUpdate = false;

    public String getPreferenceKey() {
        return "group_options";
    }

    public GroupBluetoothDetailsButtonsController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, int i, Lifecycle lifecycle) {
        super(context, preferenceFragmentCompat, i, lifecycle);
        this.mGroupId = i;
        GroupUtils groupUtils = new GroupUtils(context);
        this.mGroupUtils = groupUtils;
        this.mGroupSize = groupUtils.getGroupSize(this.mGroupId);
    }

    /* access modifiers changed from: protected */
    public void init(PreferenceScreen preferenceScreen) {
        if (DBG) {
            Log.d(TAG, "init ");
        }
        GroupOptionsPreference groupOptionsPreference = (GroupOptionsPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mGroupOptions = groupOptionsPreference;
        groupOptionsPreference.setTextViewText(this.mContext.getString(R$string.group_id) + this.mGroupUtils.getGroupTitle(this.mGroupId));
        this.mGroupOptions.setForgetButtonText(R$string.forget_group);
        this.mGroupOptions.setForgetButtonOnClickListener(new GroupBluetoothDetailsButtonsController$$ExternalSyntheticLambda0(this));
        boolean z = true;
        this.mGroupOptions.setForgetButtonEnabled(true);
        this.mGroupOptions.setTexStatusText(R$string.active);
        this.mGroupOptions.setConnectButtonText(R$string.connect_group);
        this.mGroupOptions.setConnectButtonOnClickListener(new GroupBluetoothDetailsButtonsController$$ExternalSyntheticLambda1(this));
        this.mGroupOptions.setDisconnectButtonText(R$string.disconnect_group);
        this.mGroupOptions.setDisconnectButtonOnClickListener(new GroupBluetoothDetailsButtonsController$$ExternalSyntheticLambda2(this));
        this.mGroupOptions.setCancelRefreshButtonText(R$string.cancel_refresh_group);
        this.mGroupOptions.setCancelRefreshButtonOnClickListener(new GroupBluetoothDetailsButtonsController$$ExternalSyntheticLambda3(this));
        this.mGroupOptions.setCancelRefreshButtonVisible(false);
        this.mGroupOptions.setRefreshButtonText(R$string.refresh_group);
        this.mGroupOptions.setRefreshButtonOnClickListener(new GroupBluetoothDetailsButtonsController$$ExternalSyntheticLambda4(this));
        this.mGroupOptions.setRefreshButtonVisible(false);
        this.mIsUpdate = true;
        if (this.mGroupUtils.getAnyBCConnectedDevice(this.mGroupId) == null) {
            z = false;
        }
        this.mGroupOptions.setAddSourceGroupButtonText(R$string.add_source_group);
        this.mGroupOptions.setAddSourceGroupButtonEnabled(z);
        this.mGroupOptions.setAddSourceGroupButtonVisible(z);
        if (z) {
            this.mGroupOptions.setAddSourceGroupButtonOnClickListener(new GroupBluetoothDetailsButtonsController$$ExternalSyntheticLambda5(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$0(View view) {
        onForgetButtonPressed();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1(View view) {
        onConnectButtonPressed();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$2(View view) {
        onDisConnectButtonPressed();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$3(View view) {
        onCacelRefreshButtonPressed();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$4(View view) {
        onRefreshButtonPressed();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$5(View view) {
        onAddSourceGroupButtonPressed();
    }

    private void onAddSourceGroupButtonPressed() {
        this.mGroupUtils.launchAddSourceGroup(this.mGroupId);
    }

    public void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (DBG) {
            String str = TAG;
            Log.d(str, "onConnectionStateChanged cachedDevice " + cachedBluetoothDevice + " state " + i);
        }
        if (this.mGroupUtils.isUpdate(this.mGroupId, cachedBluetoothDevice)) {
            refresh();
        }
    }

    public void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        if (DBG) {
            String str = TAG;
            Log.d(str, "onProfileConnectionStateChanged cachedDevice " + cachedBluetoothDevice + " state " + i);
        }
        if (this.mGroupUtils.isUpdate(this.mGroupId, cachedBluetoothDevice)) {
            refresh();
        }
    }

    public void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        boolean z;
        if (i == 12) {
            z = this.mGroupUtils.addDevice(this.mDevicesList, this.mGroupId, cachedBluetoothDevice);
        } else {
            z = i == 10 ? this.mGroupUtils.removeDevice(this.mDevicesList, this.mGroupId, cachedBluetoothDevice) : false;
        }
        if (z) {
            this.mDiscoveredSize = this.mDevicesList.size();
        }
        if (DBG) {
            String str = TAG;
            Log.d(str, "onDeviceBondStateChanged cachedDevice " + cachedBluetoothDevice + " name " + cachedBluetoothDevice.getName() + " bondState " + i + " isUpdated " + z + " mDiscoveredSize " + this.mDiscoveredSize);
        }
        if (z) {
            updateProgressScan();
            refresh();
        }
    }

    public void onStop() {
        if (DBG) {
            Log.d(TAG, "onStop ");
        }
        super.onStop();
        disableScanning();
    }

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

    /* access modifiers changed from: protected */
    public void loadDevices() {
        ArrayList<CachedBluetoothDevice> cahcedDevice = this.mGroupUtils.getCahcedDevice(this.mGroupId);
        this.mDevicesList = cahcedDevice;
        this.mDiscoveredSize = cahcedDevice.size();
        if (DBG) {
            String str = TAG;
            Log.d(str, "loadDevices mGroupId " + this.mGroupId + " mGroupSize " + this.mGroupSize + " mDiscoveredSize " + this.mDiscoveredSize);
        }
        updateProgressScan();
    }

    /* access modifiers changed from: protected */
    public void refresh() {
        ArrayList<CachedBluetoothDevice> arrayList = new ArrayList<>(this.mDevicesList);
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
            if (!z4 && cachedBluetoothDevice.isConnected()) {
                if (cachedBluetoothDevice.isActiveDevice(2) || cachedBluetoothDevice.isActiveDevice(1) || cachedBluetoothDevice.isActiveDevice(21)) {
                    z4 = true;
                }
            }
        }
        if (DBG) {
            Log.d(TAG, "refresh isBusy " + z + " showConnect " + z2 + " showDisconnect :" + z3 + " isActive " + z4 + " mIsUpdate " + this.mIsUpdate);
        }
        if (this.mIsUpdate) {
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
