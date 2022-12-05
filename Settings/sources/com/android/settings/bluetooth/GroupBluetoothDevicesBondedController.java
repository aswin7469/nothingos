package com.android.settings.bluetooth;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.connecteddevice.ConnectedDeviceDashboardFragment;
import com.android.settings.connecteddevice.DevicePreferenceCallback;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
/* loaded from: classes.dex */
public class GroupBluetoothDevicesBondedController extends BasePreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnStart, OnStop, DevicePreferenceCallback {
    private static final boolean DBG = ConnectedDeviceDashboardFragment.DBG_GROUP;
    private static final String KEY = "group_options_bonded_devices";
    private static final String TAG = "GroupBluetoothDevicesBondedController";
    private GroupBluetoothDevicesBondedUpdater mGroupDeviceUpdater;
    private int mGroupId;
    private PreferenceGroup mPreferenceGroup;

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

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return KEY;
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

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public GroupBluetoothDevicesBondedController(Context context, DashboardFragment dashboardFragment, Lifecycle lifecycle, int i) {
        super(context, KEY);
        this.mGroupId = i;
        lifecycle.addObserver(this);
        this.mGroupDeviceUpdater = new GroupBluetoothDevicesBondedUpdater(dashboardFragment.getContext(), dashboardFragment, this, this.mGroupId);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        this.mGroupDeviceUpdater.registerCallback();
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        this.mGroupDeviceUpdater.unregisterCallback();
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        PreferenceGroup preferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(KEY);
        this.mPreferenceGroup = preferenceGroup;
        preferenceGroup.setVisible(false);
        if (isAvailable()) {
            this.mGroupDeviceUpdater.setPrefContext(preferenceScreen.getContext());
            this.mGroupDeviceUpdater.forceUpdate();
        }
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth") ? 0 : 3;
    }

    @Override // com.android.settings.connecteddevice.DevicePreferenceCallback
    public void onDeviceAdded(Preference preference) {
        if (this.mPreferenceGroup.getPreferenceCount() == 0) {
            this.mPreferenceGroup.setVisible(true);
        }
        this.mPreferenceGroup.addPreference(preference);
    }

    @Override // com.android.settings.connecteddevice.DevicePreferenceCallback
    public void onDeviceRemoved(Preference preference) {
        this.mPreferenceGroup.removePreference(preference);
        if (this.mPreferenceGroup.getPreferenceCount() == 0) {
            this.mPreferenceGroup.setVisible(false);
        }
    }
}
