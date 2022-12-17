package com.android.settings.connecteddevice;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.settings.bluetooth.BluetoothDeviceUpdater;
import com.android.settings.bluetooth.ConnectedBluetoothDeviceUpdater;
import com.android.settings.connecteddevice.dock.DockUpdater;
import com.android.settings.connecteddevice.usb.ConnectedUsbDeviceUpdater;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;

public class ConnectedDeviceGroupController extends BasePreferenceController implements PreferenceControllerMixin, LifecycleObserver, OnStart, OnStop, DevicePreferenceCallback {
    private static final String KEY = "connected_device_list";
    private BluetoothDeviceUpdater mBluetoothDeviceUpdater;
    private DockUpdater mConnectedDockUpdater;
    private ConnectedUsbDeviceUpdater mConnectedUsbDeviceUpdater;
    private final PackageManager mPackageManager;
    PreferenceGroup mPreferenceGroup;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public String getPreferenceKey() {
        return KEY;
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public ConnectedDeviceGroupController(Context context) {
        super(context, KEY);
        this.mPackageManager = context.getPackageManager();
    }

    public void onStart() {
        BluetoothDeviceUpdater bluetoothDeviceUpdater = this.mBluetoothDeviceUpdater;
        if (bluetoothDeviceUpdater != null) {
            bluetoothDeviceUpdater.registerCallback();
            this.mBluetoothDeviceUpdater.refreshPreference();
        }
        ConnectedUsbDeviceUpdater connectedUsbDeviceUpdater = this.mConnectedUsbDeviceUpdater;
        if (connectedUsbDeviceUpdater != null) {
            connectedUsbDeviceUpdater.registerCallback();
        }
        this.mConnectedDockUpdater.registerCallback();
    }

    public void onStop() {
        BluetoothDeviceUpdater bluetoothDeviceUpdater = this.mBluetoothDeviceUpdater;
        if (bluetoothDeviceUpdater != null) {
            bluetoothDeviceUpdater.unregisterCallback();
        }
        ConnectedUsbDeviceUpdater connectedUsbDeviceUpdater = this.mConnectedUsbDeviceUpdater;
        if (connectedUsbDeviceUpdater != null) {
            connectedUsbDeviceUpdater.unregisterCallback();
        }
        this.mConnectedDockUpdater.unregisterCallback();
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        PreferenceGroup preferenceGroup = (PreferenceGroup) preferenceScreen.findPreference(KEY);
        this.mPreferenceGroup = preferenceGroup;
        preferenceGroup.setVisible(false);
        if (isAvailable()) {
            Context context = preferenceScreen.getContext();
            BluetoothDeviceUpdater bluetoothDeviceUpdater = this.mBluetoothDeviceUpdater;
            if (bluetoothDeviceUpdater != null) {
                bluetoothDeviceUpdater.setPrefContext(context);
                this.mBluetoothDeviceUpdater.forceUpdate();
            }
            ConnectedUsbDeviceUpdater connectedUsbDeviceUpdater = this.mConnectedUsbDeviceUpdater;
            if (connectedUsbDeviceUpdater != null) {
                connectedUsbDeviceUpdater.initUsbPreference(context);
            }
            this.mConnectedDockUpdater.setPreferenceContext(context);
            this.mConnectedDockUpdater.forceUpdate();
        }
    }

    public int getAvailabilityStatus() {
        return (hasBluetoothFeature() || hasUsbFeature() || this.mConnectedDockUpdater != null) ? 1 : 3;
    }

    public void onDeviceAdded(Preference preference) {
        if (this.mPreferenceGroup.getPreferenceCount() == 0) {
            this.mPreferenceGroup.setVisible(true);
        }
        this.mPreferenceGroup.addPreference(preference);
    }

    public void onDeviceRemoved(Preference preference) {
        this.mPreferenceGroup.removePreference(preference);
        if (this.mPreferenceGroup.getPreferenceCount() == 0) {
            this.mPreferenceGroup.setVisible(false);
        }
    }

    /* access modifiers changed from: package-private */
    public void init(BluetoothDeviceUpdater bluetoothDeviceUpdater, ConnectedUsbDeviceUpdater connectedUsbDeviceUpdater, DockUpdater dockUpdater) {
        this.mBluetoothDeviceUpdater = bluetoothDeviceUpdater;
        this.mConnectedUsbDeviceUpdater = connectedUsbDeviceUpdater;
        this.mConnectedDockUpdater = dockUpdater;
    }

    public void init(DashboardFragment dashboardFragment) {
        Context context = dashboardFragment.getContext();
        DockUpdater connectedDockUpdater = FeatureFactory.getFactory(context).getDockUpdaterFeatureProvider().getConnectedDockUpdater(context, this);
        ConnectedUsbDeviceUpdater connectedUsbDeviceUpdater = null;
        ConnectedBluetoothDeviceUpdater connectedBluetoothDeviceUpdater = hasBluetoothFeature() ? new ConnectedBluetoothDeviceUpdater(context, dashboardFragment, this) : null;
        if (hasUsbFeature()) {
            connectedUsbDeviceUpdater = new ConnectedUsbDeviceUpdater(context, dashboardFragment, this);
        }
        init(connectedBluetoothDeviceUpdater, connectedUsbDeviceUpdater, connectedDockUpdater);
    }

    private boolean hasBluetoothFeature() {
        return this.mPackageManager.hasSystemFeature("android.hardware.bluetooth");
    }

    private boolean hasUsbFeature() {
        return this.mPackageManager.hasSystemFeature("android.hardware.usb.accessory") || this.mPackageManager.hasSystemFeature("android.hardware.usb.host");
    }
}
