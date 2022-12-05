package com.android.settings.bluetooth;

import android.content.Context;
import android.content.IntentFilter;
import androidx.annotation.Keep;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
@Keep
/* loaded from: classes.dex */
public class BADevicePreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop, BleBroadcastSourceInfoPreferenceCallback {
    private static final int MAX_DEVICE_NUM = 3;
    private static final String TAG = "BADevicePreferenceController";
    private BluetoothBroadcastSourceInfoEntries mBleSourceInfoUpdater;
    private CachedBluetoothDevice mCachedDevice;
    private PreferenceGroup mPreferenceGroup;
    private int mPreferenceSize;

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

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public BADevicePreferenceController(Context context, Lifecycle lifecycle, String str) {
        super(context, str);
        lifecycle.addObserver(this);
        BroadcastScanAssistanceUtils.debug(TAG, "constructor: KEY" + str);
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth") ? 0 : 2;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return new String("added_sources");
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        BroadcastScanAssistanceUtils.debug(TAG, "displayPreference");
        super.displayPreference(preferenceScreen);
        PreferenceGroup preferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreferenceGroup = preferenceGroup;
        preferenceGroup.setVisible(false);
        if (isAvailable()) {
            BroadcastScanAssistanceUtils.debug(TAG, "registering wth BleSrcInfo updaters");
            Context context = preferenceScreen.getContext();
            BluetoothBroadcastSourceInfoEntries bluetoothBroadcastSourceInfoEntries = this.mBleSourceInfoUpdater;
            if (bluetoothBroadcastSourceInfoEntries == null) {
                return;
            }
            bluetoothBroadcastSourceInfoEntries.setPrefContext(context);
        }
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        BluetoothBroadcastSourceInfoEntries bluetoothBroadcastSourceInfoEntries = this.mBleSourceInfoUpdater;
        if (bluetoothBroadcastSourceInfoEntries != null) {
            bluetoothBroadcastSourceInfoEntries.registerCallback();
        }
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        BluetoothBroadcastSourceInfoEntries bluetoothBroadcastSourceInfoEntries = this.mBleSourceInfoUpdater;
        if (bluetoothBroadcastSourceInfoEntries != null) {
            bluetoothBroadcastSourceInfoEntries.unregisterCallback();
        }
    }

    public void init(DashboardFragment dashboardFragment, CachedBluetoothDevice cachedBluetoothDevice) {
        BroadcastScanAssistanceUtils.debug(TAG, "Init");
        this.mCachedDevice = cachedBluetoothDevice;
        this.mBleSourceInfoUpdater = new BluetoothBroadcastSourceInfoEntries(dashboardFragment.getContext(), dashboardFragment, this, cachedBluetoothDevice);
        this.mPreferenceSize = 0;
    }

    @Override // com.android.settings.bluetooth.BleBroadcastSourceInfoPreferenceCallback
    public void onBroadcastSourceInfoAdded(Preference preference) {
        BroadcastScanAssistanceUtils.debug(TAG, "onBroadcastSourceInfoAdded");
        if (this.mPreferenceSize < 3) {
            boolean addPreference = this.mPreferenceGroup.addPreference(preference);
            BroadcastScanAssistanceUtils.debug(TAG, "addPreference returns" + addPreference);
            this.mPreferenceSize = this.mPreferenceSize + 1;
        }
        updatePreferenceVisiblity();
    }

    @Override // com.android.settings.bluetooth.BleBroadcastSourceInfoPreferenceCallback
    public void onBroadcastSourceInfoRemoved(Preference preference) {
        BroadcastScanAssistanceUtils.debug(TAG, "onBroadcastSourceInfoRemoved");
        this.mPreferenceSize--;
        BroadcastScanAssistanceUtils.debug(TAG, "removePreference returns " + this.mPreferenceGroup.removePreference(preference));
        updatePreferenceVisiblity();
    }

    void setPreferenceGroup(PreferenceGroup preferenceGroup) {
        this.mPreferenceGroup = preferenceGroup;
    }

    void updatePreferenceVisiblity() {
        BroadcastScanAssistanceUtils.debug(TAG, "updatePreferenceVisiblity:" + this.mPreferenceSize);
        this.mPreferenceGroup.setVisible(this.mPreferenceSize > 0);
    }
}
