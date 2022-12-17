package com.android.settings.accessibility;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothCodecStatus;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.bluetooth.BluetoothDeviceDetailsFragment;
import com.android.settings.bluetooth.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.core.SubSettingLauncher;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.CachedBluetoothDeviceManager;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class AccessibilityHearingAidPreferenceController extends BasePreferenceController implements LifecycleObserver, OnStart, OnStop, BluetoothCallback {
    private static final String TAG = "AccessibilityHearingAidPreferenceController";
    private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private FragmentManager mFragmentManager;
    private final BroadcastReceiver mHearingAidChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED".equals(intent.getAction())) {
                if (intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0) == 2) {
                    AccessibilityHearingAidPreferenceController accessibilityHearingAidPreferenceController = AccessibilityHearingAidPreferenceController.this;
                    accessibilityHearingAidPreferenceController.updateState(accessibilityHearingAidPreferenceController.mHearingAidPreference);
                    return;
                }
                AccessibilityHearingAidPreferenceController.this.mHearingAidPreference.setSummary(R$string.accessibility_hearingaid_not_connected_summary);
            } else if ("android.bluetooth.adapter.action.STATE_CHANGED".equals(intent.getAction()) && intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE) != 12) {
                AccessibilityHearingAidPreferenceController.this.mHearingAidPreference.setSummary(R$string.accessibility_hearingaid_not_connected_summary);
            }
        }
    };
    /* access modifiers changed from: private */
    public Preference mHearingAidPreference;
    private final LocalBluetoothManager mLocalBluetoothManager = getLocalBluetoothManager();

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

    public /* bridge */ /* synthetic */ void onProfileConnectionStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i, int i2) {
        super.onProfileConnectionStateChanged(cachedBluetoothDevice, i, i2);
    }

    public /* bridge */ /* synthetic */ void onScanningStateChanged(boolean z) {
        super.onScanningStateChanged(z);
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AccessibilityHearingAidPreferenceController(Context context, String str) {
        super(context, str);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mHearingAidPreference = preferenceScreen.findPreference(getPreferenceKey());
    }

    public int getAvailabilityStatus() {
        return isHearingAidProfileSupported() ? 0 : 3;
    }

    public void onStart() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.hearingaid.profile.action.CONNECTION_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        this.mContext.registerReceiver(this.mHearingAidChangedReceiver, intentFilter);
        this.mLocalBluetoothManager.getEventManager().registerCallback(this);
    }

    public void onStop() {
        this.mContext.unregisterReceiver(this.mHearingAidChangedReceiver);
        this.mLocalBluetoothManager.getEventManager().unregisterCallback(this);
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }
        CachedBluetoothDevice connectedHearingAidDevice = getConnectedHearingAidDevice();
        if (connectedHearingAidDevice == null) {
            launchHearingAidInstructionDialog();
            return true;
        }
        launchBluetoothDeviceDetailSetting(connectedHearingAidDevice);
        return true;
    }

    public CharSequence getSummary() {
        CachedBluetoothDevice connectedHearingAidDevice = getConnectedHearingAidDevice();
        if (connectedHearingAidDevice == null) {
            return this.mContext.getText(R$string.accessibility_hearingaid_not_connected_summary);
        }
        int connectedHearingAidDeviceNum = getConnectedHearingAidDeviceNum();
        String name = connectedHearingAidDevice.getName();
        int deviceSide = connectedHearingAidDevice.getDeviceSide();
        CachedBluetoothDevice subDevice = connectedHearingAidDevice.getSubDevice();
        if (connectedHearingAidDeviceNum > 1) {
            return this.mContext.getString(R$string.accessibility_hearingaid_more_device_summary, new Object[]{name});
        } else if (subDevice != null && subDevice.isConnected()) {
            return this.mContext.getString(R$string.accessibility_hearingaid_left_and_right_side_device_summary, new Object[]{name});
        } else if (deviceSide == -1) {
            return this.mContext.getString(R$string.accessibility_hearingaid_active_device_summary, new Object[]{name});
        } else if (deviceSide == 0) {
            return this.mContext.getString(R$string.accessibility_hearingaid_left_side_device_summary, new Object[]{name});
        } else {
            return this.mContext.getString(R$string.accessibility_hearingaid_right_side_device_summary, new Object[]{name});
        }
    }

    public void onActiveDeviceChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (cachedBluetoothDevice != null && i == 21) {
            HearingAidUtils.launchHearingAidPairingDialog(this.mFragmentManager, cachedBluetoothDevice);
        }
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    /* access modifiers changed from: package-private */
    public CachedBluetoothDevice getConnectedHearingAidDevice() {
        if (!isHearingAidProfileSupported()) {
            return null;
        }
        CachedBluetoothDeviceManager cachedDeviceManager = this.mLocalBluetoothManager.getCachedDeviceManager();
        for (BluetoothDevice next : this.mLocalBluetoothManager.getProfileManager().getHearingAidProfile().getConnectedDevices()) {
            if (!cachedDeviceManager.isSubDevice(next)) {
                return cachedDeviceManager.findDevice(next);
            }
        }
        return null;
    }

    private int getConnectedHearingAidDeviceNum() {
        if (!isHearingAidProfileSupported()) {
            return 0;
        }
        return (int) this.mLocalBluetoothManager.getProfileManager().getHearingAidProfile().getConnectedDevices().stream().filter(new C0577x99de13bd(this.mLocalBluetoothManager.getCachedDeviceManager())).count();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getConnectedHearingAidDeviceNum$0(CachedBluetoothDeviceManager cachedBluetoothDeviceManager, BluetoothDevice bluetoothDevice) {
        return !cachedBluetoothDeviceManager.isSubDevice(bluetoothDevice);
    }

    private boolean isHearingAidProfileSupported() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return false;
        }
        return this.mBluetoothAdapter.getSupportedProfiles().contains(21);
    }

    private LocalBluetoothManager getLocalBluetoothManager() {
        FutureTask futureTask = new FutureTask(new C0576x99de13bc(this));
        try {
            futureTask.run();
            return (LocalBluetoothManager) futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.w(TAG, "Error getting LocalBluetoothManager.", e);
            return null;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ LocalBluetoothManager lambda$getLocalBluetoothManager$1() throws Exception {
        return Utils.getLocalBtManager(this.mContext);
    }

    /* access modifiers changed from: package-private */
    public void setPreference(Preference preference) {
        this.mHearingAidPreference = preference;
    }

    /* access modifiers changed from: package-private */
    public void launchBluetoothDeviceDetailSetting(CachedBluetoothDevice cachedBluetoothDevice) {
        if (cachedBluetoothDevice != null) {
            Bundle bundle = new Bundle();
            bundle.putString("device_address", cachedBluetoothDevice.getDevice().getAddress());
            new SubSettingLauncher(this.mContext).setDestination(BluetoothDeviceDetailsFragment.class.getName()).setArguments(bundle).setTitleRes(R$string.device_details_title).setSourceMetricsCategory(2).launch();
        }
    }

    /* access modifiers changed from: package-private */
    public void launchHearingAidInstructionDialog() {
        HearingAidDialogFragment.newInstance().show(this.mFragmentManager, HearingAidDialogFragment.class.toString());
    }
}
