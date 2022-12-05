package com.android.settings.connecteddevice;

import android.bluetooth.BluetoothCodecStatus;
import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.bluetooth.GroupSavedBluetoothDeviceUpdater;
import com.android.settings.bluetooth.Utils;
import com.android.settings.connecteddevice.dock.DockUpdater;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothAdapter;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.UUID;
/* loaded from: classes.dex */
public class GroupPreviouslyConnectedDevicePreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop, DevicePreferenceCallback, BluetoothCallback {
    private static final int MAX_DEVICE_NUM = 3;
    private static final String TAG = "GroupPreviouslyConnectedDevicePreferenceController";
    private GroupSavedBluetoothDeviceUpdater mBluetoothDeviceUpdater;
    private LocalBluetoothAdapter mLocalAdapter;
    private PreferenceGroup mPreferenceGroup;
    private int mPreferenceSize;
    private DockUpdater mSavedDockUpdater;
    private LocalBluetoothManager manager;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onA2dpCodecConfigChanged(CachedBluetoothDevice cachedBluetoothDevice, BluetoothCodecStatus bluetoothCodecStatus) {
        super.onA2dpCodecConfigChanged(cachedBluetoothDevice, bluetoothCodecStatus);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onAclConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onAclConnectionStateChanged(cachedBluetoothDevice, i);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onActiveDeviceChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onActiveDeviceChanged(cachedBluetoothDevice, i);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onAudioModeChanged() {
        super.onAudioModeChanged();
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onBroadcastKeyGenerated() {
        super.onBroadcastKeyGenerated();
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onBroadcastStateChanged(int i) {
        super.onBroadcastStateChanged(i);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onConnectionStateChanged(cachedBluetoothDevice, i);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
        super.onDeviceAdded(cachedBluetoothDevice);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onDeviceBondStateChanged(cachedBluetoothDevice, i);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onDeviceDeleted(CachedBluetoothDevice cachedBluetoothDevice) {
        super.onDeviceDeleted(cachedBluetoothDevice);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onGroupDiscoveryStatusChanged(int i, int i2, int i3) {
        super.onGroupDiscoveryStatusChanged(i, i2, i3);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onNewGroupFound(CachedBluetoothDevice cachedBluetoothDevice, int i, UUID uuid) {
        super.onNewGroupFound(cachedBluetoothDevice, i, uuid);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        super.onProfileConnectionStateChanged(cachedBluetoothDevice, i, i2);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public /* bridge */ /* synthetic */ void onScanningStateChanged(boolean z) {
        super.onScanningStateChanged(z);
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GroupPreviouslyConnectedDevicePreferenceController(Context context, String str) {
        super(context, str);
        this.mSavedDockUpdater = FeatureFactory.getFactory(context).getDockUpdaterFeatureProvider().getSavedDockUpdater(context, this);
        LocalBluetoothManager localBtManager = Utils.getLocalBtManager(context);
        this.manager = localBtManager;
        if (localBtManager != null) {
            this.mLocalAdapter = localBtManager.getBluetoothAdapter();
        }
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth") ? 0 : 2;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        PreferenceGroup preferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreferenceGroup = preferenceGroup;
        preferenceGroup.setVisible(false);
        if (isAvailable()) {
            Context context = preferenceScreen.getContext();
            this.mBluetoothDeviceUpdater.setPrefContext(context);
            this.mSavedDockUpdater.setPreferenceContext(context);
        }
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        this.mBluetoothDeviceUpdater.registerCallback();
        this.mSavedDockUpdater.registerCallback();
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        this.mBluetoothDeviceUpdater.unregisterCallback();
        this.mSavedDockUpdater.unregisterCallback();
    }

    public void init(DashboardFragment dashboardFragment) {
        this.mBluetoothDeviceUpdater = new GroupSavedBluetoothDeviceUpdater(dashboardFragment.getContext(), dashboardFragment, this);
    }

    @Override // com.android.settings.connecteddevice.DevicePreferenceCallback
    public void onDeviceAdded(Preference preference) {
        int i = this.mPreferenceSize + 1;
        this.mPreferenceSize = i;
        if (i <= 3) {
            this.mPreferenceGroup.addPreference(preference);
        }
        updatePreferenceVisiblity();
    }

    @Override // com.android.settings.connecteddevice.DevicePreferenceCallback
    public void onDeviceRemoved(Preference preference) {
        this.mPreferenceSize--;
        this.mPreferenceGroup.removePreference(preference);
        updatePreferenceVisiblity();
    }

    void updatePreferenceVisiblity() {
        LocalBluetoothAdapter localBluetoothAdapter = this.mLocalAdapter;
        boolean z = false;
        if (localBluetoothAdapter != null && localBluetoothAdapter.getBluetoothState() == 12) {
            PreferenceGroup preferenceGroup = this.mPreferenceGroup;
            if (this.mPreferenceSize > 0) {
                z = true;
            }
            preferenceGroup.setVisible(z);
            return;
        }
        this.mPreferenceGroup.setVisible(false);
    }

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onBluetoothStateChanged(int i) {
        updatePreferenceVisiblity();
    }
}
