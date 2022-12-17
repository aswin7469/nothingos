package com.android.settings.bluetooth;

import android.bluetooth.BleBroadcastSourceInfo;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.R$string;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.widget.GearPreference;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.VendorCachedBluetoothDevice;
import java.util.HashMap;
import java.util.Map;

public abstract class BleBroadcastSourceInfoUpdater implements CachedBluetoothDevice.Callback, BluetoothCallback {
    protected final BleBroadcastSourceInfoPreferenceCallback mBleSourceInfoPreferenceCallback;
    protected final CachedBluetoothDevice mCachedDevice;
    protected DashboardFragment mFragment;
    private LocalBluetoothManager mLocalManager;
    protected Context mPrefContext;
    protected final Map<Integer, Preference> mPreferenceMap;
    final GearPreference.OnGearClickListener mSourceInfoEntryListener;
    protected final VendorCachedBluetoothDevice mVendorCachedDevice;

    public BleBroadcastSourceInfoUpdater(Context context, DashboardFragment dashboardFragment, BleBroadcastSourceInfoPreferenceCallback bleBroadcastSourceInfoPreferenceCallback, CachedBluetoothDevice cachedBluetoothDevice) {
        this(dashboardFragment, bleBroadcastSourceInfoPreferenceCallback, cachedBluetoothDevice);
    }

    BleBroadcastSourceInfoUpdater(DashboardFragment dashboardFragment, BleBroadcastSourceInfoPreferenceCallback bleBroadcastSourceInfoPreferenceCallback, CachedBluetoothDevice cachedBluetoothDevice) {
        this.mSourceInfoEntryListener = new BleBroadcastSourceInfoUpdater$$ExternalSyntheticLambda0(this);
        this.mCachedDevice = cachedBluetoothDevice;
        this.mVendorCachedDevice = VendorCachedBluetoothDevice.getVendorCachedBluetoothDevice(cachedBluetoothDevice, Utils.getLocalBtManager(this.mPrefContext).getProfileManager());
        this.mFragment = dashboardFragment;
        this.mBleSourceInfoPreferenceCallback = bleBroadcastSourceInfoPreferenceCallback;
        this.mPreferenceMap = new HashMap();
        LocalBluetoothManager localBtManager = Utils.getLocalBtManager(this.mPrefContext);
        this.mLocalManager = localBtManager;
        localBtManager.getEventManager().registerCallback(this);
    }

    public void registerCallback() {
        this.mCachedDevice.registerCallback(this);
        forceUpdate();
    }

    public void unregisterCallback() {
        this.mCachedDevice.unregisterCallback(this);
    }

    public void onBluetoothStateChanged(int i) {
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoUpdater", "onBluetoothStateChanged");
        if (i == 10) {
            removeAllBleBroadcastSourceInfosFromPreference();
        }
    }

    public void forceUpdate() {
        if (this.mCachedDevice == null || this.mVendorCachedDevice.getNumberOfBleBroadcastReceiverStates() <= 0) {
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoUpdater", "remove all the preferences as there are no rcvr states");
            removeAllBleBroadcastSourceInfosFromPreference();
            return;
        }
        Map<Integer, BleBroadcastSourceInfo> allBleBroadcastreceiverStates = this.mVendorCachedDevice.getAllBleBroadcastreceiverStates();
        if (allBleBroadcastreceiverStates == null) {
            Log.e("BleBroadcastSourceInfoUpdater", "srcInfos is null");
            return;
        }
        removeAllBleBroadcastSourceInfosFromPreference();
        for (Map.Entry next : allBleBroadcastreceiverStates.entrySet()) {
            update((Integer) next.getKey(), (BleBroadcastSourceInfo) next.getValue());
        }
    }

    public void removeAllBleBroadcastSourceInfosFromPreference() {
        for (Map.Entry<Integer, Preference> value : this.mPreferenceMap.entrySet()) {
            this.mBleSourceInfoPreferenceCallback.onBroadcastSourceInfoRemoved((Preference) value.getValue());
        }
        this.mPreferenceMap.clear();
    }

    public void onDeviceAttributesChanged() {
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoUpdater", "onDeviceAttributesChanged");
        forceUpdate();
    }

    public void setPrefContext(Context context) {
        this.mPrefContext = context;
    }

    /* access modifiers changed from: protected */
    public void update(Integer num, BleBroadcastSourceInfo bleBroadcastSourceInfo) {
        addPreference(num, bleBroadcastSourceInfo);
    }

    /* access modifiers changed from: protected */
    public void addPreference(Integer num, BleBroadcastSourceInfo bleBroadcastSourceInfo) {
        bleBroadcastSourceInfo.getSourceDevice();
        bleBroadcastSourceInfo.getSourceId();
        if (!this.mPreferenceMap.containsKey(num)) {
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoUpdater", "source info addition");
            BleBroadcastSourceInfoPreference bleBroadcastSourceInfoPreference = new BleBroadcastSourceInfoPreference(this.mPrefContext, this.mCachedDevice, bleBroadcastSourceInfo, num, 1);
            bleBroadcastSourceInfoPreference.setOnGearClickListener(this.mSourceInfoEntryListener);
            if (this instanceof Preference.OnPreferenceClickListener) {
                bleBroadcastSourceInfoPreference.setOnPreferenceClickListener((Preference.OnPreferenceClickListener) this);
            }
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoUpdater", "source info newly added: " + num);
            this.mPreferenceMap.put(num, bleBroadcastSourceInfoPreference);
            this.mBleSourceInfoPreferenceCallback.onBroadcastSourceInfoAdded(bleBroadcastSourceInfoPreference);
            return;
        }
        BleBroadcastSourceInfoPreference bleBroadcastSourceInfoPreference2 = (BleBroadcastSourceInfoPreference) this.mPreferenceMap.get(num);
        BleBroadcastSourceInfo bleBroadcastSourceInfo2 = bleBroadcastSourceInfoPreference2.getBleBroadcastSourceInfo();
        if (bleBroadcastSourceInfo2 == null || !bleBroadcastSourceInfo2.equals(bleBroadcastSourceInfo)) {
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoUpdater", "source info Updated: " + num);
            bleBroadcastSourceInfoPreference2.setBleBroadcastSourceInfo(bleBroadcastSourceInfo);
            return;
        }
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoUpdater", "No change in SI" + num);
    }

    /* access modifiers changed from: protected */
    /* renamed from: launchSourceInfoDetails */
    public void lambda$new$0(Preference preference) {
        BleBroadcastSourceInfoPreference bleBroadcastSourceInfoPreference = (BleBroadcastSourceInfoPreference) preference;
        BleBroadcastSourceInfo bleBroadcastSourceInfo = bleBroadcastSourceInfoPreference.getBleBroadcastSourceInfo();
        if (bleBroadcastSourceInfo != null) {
            int intValue = bleBroadcastSourceInfoPreference.getSourceInfoIndex().intValue();
            Bundle bundle = new Bundle();
            bundle.putString("device_address", this.mCachedDevice.getAddress());
            bundle.putParcelable("broadcast_source_info", bleBroadcastSourceInfo);
            bundle.putInt("broadcast_source_index", intValue);
            new SubSettingLauncher(this.mFragment.getContext()).setDestination(BleBroadcastSourceInfoDetailsFragment.class.getName()).setArguments(bundle).setTitleRes(R$string.source_info_details_title).setSourceMetricsCategory(this.mFragment.getMetricsCategory()).launch();
        }
    }
}
