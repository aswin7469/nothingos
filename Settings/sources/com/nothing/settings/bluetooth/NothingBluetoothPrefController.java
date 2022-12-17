package com.nothing.settings.bluetooth;

import android.bluetooth.BluetoothCodecStatus;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.bluetooth.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import com.nothing.p005os.device.DeviceServiceController;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NothingBluetoothPrefController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop, OnDestroy, CachedBluetoothDevice.Callback, BluetoothCallback {
    private static final String TAG = "NothingBluetoothPrefController";
    private CachedBluetoothDevice mCachedDevice;
    private Context mContext;
    private Map<String, Preference> mControllerPreferences = new HashMap();
    DeviceServiceController mDeviceServiceController;
    private boolean mIsAdd;
    private boolean mIsAirpodsDevice;
    private boolean mIsConnected;
    private boolean mIsNothingEarDevice;
    private LocalBluetoothManager mLocalManager;
    PreferenceScreen mScreen;
    private int mState = -1;

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
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

    public /* bridge */ /* synthetic */ void onA2dpCodecConfigChanged(CachedBluetoothDevice cachedBluetoothDevice, BluetoothCodecStatus bluetoothCodecStatus) {
        super.onA2dpCodecConfigChanged(cachedBluetoothDevice, bluetoothCodecStatus);
    }

    public /* bridge */ /* synthetic */ void onAclConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onAclConnectionStateChanged(cachedBluetoothDevice, i);
    }

    public /* bridge */ /* synthetic */ void onActiveDeviceChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onActiveDeviceChanged(cachedBluetoothDevice, i);
    }

    public /* bridge */ /* synthetic */ void onAudioModeChanged() {
        super.onAudioModeChanged();
    }

    public /* bridge */ /* synthetic */ void onBluetoothStateChanged(int i) {
        super.onBluetoothStateChanged(i);
    }

    public /* bridge */ /* synthetic */ void onBroadcastKeyGenerated() {
        super.onBroadcastKeyGenerated();
    }

    public /* bridge */ /* synthetic */ void onBroadcastStateChanged(int i) {
        super.onBroadcastStateChanged(i);
    }

    public /* bridge */ /* synthetic */ void onConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onConnectionStateChanged(cachedBluetoothDevice, i);
    }

    public void onDestroy() {
    }

    public /* bridge */ /* synthetic */ void onDeviceAdded(CachedBluetoothDevice cachedBluetoothDevice) {
        super.onDeviceAdded(cachedBluetoothDevice);
    }

    public /* bridge */ /* synthetic */ void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        super.onDeviceBondStateChanged(cachedBluetoothDevice, i);
    }

    public /* bridge */ /* synthetic */ void onDeviceDeleted(CachedBluetoothDevice cachedBluetoothDevice) {
        super.onDeviceDeleted(cachedBluetoothDevice);
    }

    public /* bridge */ /* synthetic */ void onGroupDiscoveryStatusChanged(int i, int i2, int i3) {
        super.onGroupDiscoveryStatusChanged(i, i2, i3);
    }

    public /* bridge */ /* synthetic */ void onNewGroupFound(CachedBluetoothDevice cachedBluetoothDevice, int i, UUID uuid) {
        super.onNewGroupFound(cachedBluetoothDevice, i, uuid);
    }

    public /* bridge */ /* synthetic */ void onScanningStateChanged(boolean z) {
        super.onScanningStateChanged(z);
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public NothingBluetoothPrefController(Context context, CachedBluetoothDevice cachedBluetoothDevice, Lifecycle lifecycle, DeviceServiceController deviceServiceController, String str) {
        super(context, str);
        this.mIsConnected = cachedBluetoothDevice.isConnected();
        this.mCachedDevice = cachedBluetoothDevice;
        lifecycle.addObserver(this);
        this.mDeviceServiceController = deviceServiceController;
        this.mContext = context;
        this.mIsAirpodsDevice = NothingBluetoothUtil.getinstance().checkUUIDIsAirpod(this.mContext, this.mCachedDevice.getDevice());
        LocalBluetoothManager localBtManager = Utils.getLocalBtManager(context);
        this.mLocalManager = localBtManager;
        if (localBtManager == null) {
            Log.d(TAG, "Bluetooth is not supported on this device");
        }
    }

    public void onDeviceAttributesChanged() {
        if (this.mCachedDevice != null) {
            refresh();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00e2, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void refresh() {
        /*
            r4 = this;
            monitor-enter(r4)
            com.android.settingslib.bluetooth.CachedBluetoothDevice r0 = r4.mCachedDevice     // Catch:{ all -> 0x00e3 }
            boolean r0 = r0.isConnected()     // Catch:{ all -> 0x00e3 }
            r4.mIsConnected = r0     // Catch:{ all -> 0x00e3 }
            com.nothing.settings.bluetooth.NothingBluetoothUtil r0 = com.nothing.settings.bluetooth.NothingBluetoothUtil.getinstance()     // Catch:{ all -> 0x00e3 }
            android.content.Context r1 = r4.mContext     // Catch:{ all -> 0x00e3 }
            com.android.settingslib.bluetooth.CachedBluetoothDevice r2 = r4.mCachedDevice     // Catch:{ all -> 0x00e3 }
            java.lang.String r2 = r2.getAddress()     // Catch:{ all -> 0x00e3 }
            boolean r0 = r0.isNothingEarDevice(r1, r2)     // Catch:{ all -> 0x00e3 }
            r4.mIsNothingEarDevice = r0     // Catch:{ all -> 0x00e3 }
            java.lang.String r0 = "NothingBluetoothPrefController"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00e3 }
            r1.<init>()     // Catch:{ all -> 0x00e3 }
            java.lang.String r2 = "mIsConnected:"
            r1.append(r2)     // Catch:{ all -> 0x00e3 }
            boolean r2 = r4.mIsConnected     // Catch:{ all -> 0x00e3 }
            r1.append(r2)     // Catch:{ all -> 0x00e3 }
            java.lang.String r2 = ", isAdd:"
            r1.append(r2)     // Catch:{ all -> 0x00e3 }
            boolean r2 = r4.mIsAdd     // Catch:{ all -> 0x00e3 }
            r1.append(r2)     // Catch:{ all -> 0x00e3 }
            java.lang.String r2 = ", mControllerPreferences:"
            r1.append(r2)     // Catch:{ all -> 0x00e3 }
            java.util.Map<java.lang.String, androidx.preference.Preference> r2 = r4.mControllerPreferences     // Catch:{ all -> 0x00e3 }
            r1.append(r2)     // Catch:{ all -> 0x00e3 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00e3 }
            android.util.Log.d(r0, r1)     // Catch:{ all -> 0x00e3 }
            java.lang.String r0 = "NothingBluetoothPrefController"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x00e3 }
            r1.<init>()     // Catch:{ all -> 0x00e3 }
            java.lang.String r2 = "mIsNothingEarDevice:"
            r1.append(r2)     // Catch:{ all -> 0x00e3 }
            boolean r2 = r4.mIsNothingEarDevice     // Catch:{ all -> 0x00e3 }
            r1.append(r2)     // Catch:{ all -> 0x00e3 }
            java.lang.String r2 = ", isAirpodsDevice:"
            r1.append(r2)     // Catch:{ all -> 0x00e3 }
            boolean r2 = r4.mIsAirpodsDevice     // Catch:{ all -> 0x00e3 }
            r1.append(r2)     // Catch:{ all -> 0x00e3 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x00e3 }
            android.util.Log.d(r0, r1)     // Catch:{ all -> 0x00e3 }
            boolean r0 = r4.mIsAirpodsDevice     // Catch:{ all -> 0x00e3 }
            if (r0 == 0) goto L_0x006f
            monitor-exit(r4)
            return
        L_0x006f:
            java.util.Map<java.lang.String, androidx.preference.Preference> r0 = r4.mControllerPreferences     // Catch:{ all -> 0x00e3 }
            r1 = 1
            if (r0 == 0) goto L_0x007a
            int r0 = r0.size()     // Catch:{ all -> 0x00e3 }
            if (r0 != 0) goto L_0x0093
        L_0x007a:
            boolean r0 = r4.mIsNothingEarDevice     // Catch:{ all -> 0x00e3 }
            if (r0 == 0) goto L_0x0093
            android.os.Bundle r0 = new android.os.Bundle     // Catch:{ all -> 0x00e3 }
            r0.<init>()     // Catch:{ all -> 0x00e3 }
            java.lang.String r2 = "device_address"
            com.android.settingslib.bluetooth.CachedBluetoothDevice r3 = r4.mCachedDevice     // Catch:{ all -> 0x00e3 }
            java.lang.String r3 = r3.getAddress()     // Catch:{ all -> 0x00e3 }
            r0.putString(r2, r3)     // Catch:{ all -> 0x00e3 }
            com.nothing.os.device.DeviceServiceController r2 = r4.mDeviceServiceController     // Catch:{ all -> 0x00e3 }
            r2.getCommand(r1, r0)     // Catch:{ all -> 0x00e3 }
        L_0x0093:
            boolean r0 = r4.mIsConnected     // Catch:{ all -> 0x00e3 }
            if (r0 == 0) goto L_0x00c0
            boolean r0 = r4.mIsNothingEarDevice     // Catch:{ all -> 0x00e3 }
            if (r0 == 0) goto L_0x00c0
            java.util.Map<java.lang.String, androidx.preference.Preference> r0 = r4.mControllerPreferences     // Catch:{ all -> 0x00e3 }
            if (r0 == 0) goto L_0x00e1
            boolean r2 = r4.mIsAdd     // Catch:{ all -> 0x00e3 }
            if (r2 != 0) goto L_0x00e1
            java.util.Collection r0 = r0.values()     // Catch:{ all -> 0x00e3 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x00e3 }
        L_0x00ab:
            boolean r2 = r0.hasNext()     // Catch:{ all -> 0x00e3 }
            if (r2 == 0) goto L_0x00bd
            java.lang.Object r2 = r0.next()     // Catch:{ all -> 0x00e3 }
            androidx.preference.Preference r2 = (androidx.preference.Preference) r2     // Catch:{ all -> 0x00e3 }
            androidx.preference.PreferenceScreen r3 = r4.mScreen     // Catch:{ all -> 0x00e3 }
            r3.addPreference(r2)     // Catch:{ all -> 0x00e3 }
            goto L_0x00ab
        L_0x00bd:
            r4.mIsAdd = r1     // Catch:{ all -> 0x00e3 }
            goto L_0x00e1
        L_0x00c0:
            java.util.Map<java.lang.String, androidx.preference.Preference> r0 = r4.mControllerPreferences     // Catch:{ all -> 0x00e3 }
            if (r0 == 0) goto L_0x00e1
            java.util.Collection r0 = r0.values()     // Catch:{ all -> 0x00e3 }
            java.util.Iterator r0 = r0.iterator()     // Catch:{ all -> 0x00e3 }
        L_0x00cc:
            boolean r1 = r0.hasNext()     // Catch:{ all -> 0x00e3 }
            if (r1 == 0) goto L_0x00de
            java.lang.Object r1 = r0.next()     // Catch:{ all -> 0x00e3 }
            androidx.preference.Preference r1 = (androidx.preference.Preference) r1     // Catch:{ all -> 0x00e3 }
            androidx.preference.PreferenceScreen r2 = r4.mScreen     // Catch:{ all -> 0x00e3 }
            r2.removePreference(r1)     // Catch:{ all -> 0x00e3 }
            goto L_0x00cc
        L_0x00de:
            r0 = 0
            r4.mIsAdd = r0     // Catch:{ all -> 0x00e3 }
        L_0x00e1:
            monitor-exit(r4)
            return
        L_0x00e3:
            r0 = move-exception
            monitor-exit(r4)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.settings.bluetooth.NothingBluetoothPrefController.refresh():void");
    }

    public void init(Map<String, Preference> map) {
        this.mControllerPreferences = map;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mScreen = preferenceScreen;
    }

    public void onStart() {
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        if (cachedBluetoothDevice != null) {
            cachedBluetoothDevice.registerCallback(this);
        }
        Log.d(TAG, "onStart mLocalManager:" + this.mLocalManager);
        LocalBluetoothManager localBluetoothManager = this.mLocalManager;
        if (localBluetoothManager != null) {
            localBluetoothManager.setForegroundActivity(this.mContext);
            this.mLocalManager.getEventManager().registerCallback(this);
        }
    }

    public void onStop() {
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        if (cachedBluetoothDevice != null) {
            cachedBluetoothDevice.unregisterCallback(this);
        }
        Log.d(TAG, "onStop mLocalManager:" + this.mLocalManager);
        LocalBluetoothManager localBluetoothManager = this.mLocalManager;
        if (localBluetoothManager != null) {
            localBluetoothManager.setForegroundActivity((Context) null);
            this.mLocalManager.getEventManager().unregisterCallback(this);
        }
    }

    public void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        Log.d(TAG, "onConnectionStateChanged: state " + i + " cachedDevice addr:" + cachedBluetoothDevice.getAddress() + ", mState:" + this.mState);
        if (this.mState != i) {
            Bundle bundle = new Bundle();
            bundle.putString("device_address", this.mCachedDevice.getAddress());
            if (i == 2) {
                this.mDeviceServiceController.sendCommand(0, bundle);
                Log.d(TAG, "STATE_CONNECTED--");
            } else if (i == 0) {
                Log.d(TAG, "STATE_DISCONNECTED--");
            }
            this.mState = i;
        }
    }
}
