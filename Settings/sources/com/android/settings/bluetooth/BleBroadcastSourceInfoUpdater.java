package com.android.settings.bluetooth;

import android.bluetooth.BleBroadcastSourceInfo;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import androidx.preference.Preference;
import com.android.settings.R;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.widget.GearPreference;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.VendorCachedBluetoothDevice;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
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
        this.mSourceInfoEntryListener = new GearPreference.OnGearClickListener() { // from class: com.android.settings.bluetooth.BleBroadcastSourceInfoUpdater$$ExternalSyntheticLambda0
            @Override // com.android.settings.widget.GearPreference.OnGearClickListener
            public final void onGearClick(GearPreference gearPreference) {
                BleBroadcastSourceInfoUpdater.this.lambda$new$0(gearPreference);
            }
        };
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

    @Override // com.android.settingslib.bluetooth.BluetoothCallback
    public void onBluetoothStateChanged(int i) {
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoUpdater", "onBluetoothStateChanged");
        if (i == 10) {
            removeAllBleBroadcastSourceInfosFromPreference();
        }
    }

    public void forceUpdate() {
        if (this.mCachedDevice != null && this.mVendorCachedDevice.getNumberOfBleBroadcastReceiverStates() > 0) {
            Map<Integer, BleBroadcastSourceInfo> allBleBroadcastreceiverStates = this.mVendorCachedDevice.getAllBleBroadcastreceiverStates();
            if (allBleBroadcastreceiverStates == null) {
                Log.e("BleBroadcastSourceInfoUpdater", "srcInfos is null");
                return;
            }
            for (Map.Entry<Integer, BleBroadcastSourceInfo> entry : allBleBroadcastreceiverStates.entrySet()) {
                update(entry.getKey(), entry.getValue());
            }
            return;
        }
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoUpdater", "remove all the preferences as there are no rcvr states");
        removeAllBleBroadcastSourceInfosFromPreference();
    }

    public void removeAllBleBroadcastSourceInfosFromPreference() {
        for (Map.Entry<Integer, Preference> entry : this.mPreferenceMap.entrySet()) {
            this.mBleSourceInfoPreferenceCallback.onBroadcastSourceInfoRemoved(entry.getValue());
        }
        this.mPreferenceMap.clear();
    }

    @Override // com.android.settingslib.bluetooth.CachedBluetoothDevice.Callback
    public void onDeviceAttributesChanged() {
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoUpdater", "onDeviceAttributesChanged");
        forceUpdate();
    }

    public void setPrefContext(Context context) {
        this.mPrefContext = context;
    }

    protected void update(Integer num, BleBroadcastSourceInfo bleBroadcastSourceInfo) {
        addPreference(num, bleBroadcastSourceInfo);
    }

    protected void addPreference(Integer num, BleBroadcastSourceInfo bleBroadcastSourceInfo) {
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
        if (bleBroadcastSourceInfo2 != null && bleBroadcastSourceInfo2.equals(bleBroadcastSourceInfo)) {
            BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoUpdater", "No change in SI" + num);
            return;
        }
        BroadcastScanAssistanceUtils.debug("BleBroadcastSourceInfoUpdater", "source info Updated: " + num);
        bleBroadcastSourceInfoPreference2.setBleBroadcastSourceInfo(bleBroadcastSourceInfo);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: launchSourceInfoDetails */
    public void lambda$new$0(Preference preference) {
        BleBroadcastSourceInfoPreference bleBroadcastSourceInfoPreference = (BleBroadcastSourceInfoPreference) preference;
        Parcelable bleBroadcastSourceInfo = bleBroadcastSourceInfoPreference.getBleBroadcastSourceInfo();
        if (bleBroadcastSourceInfo == null) {
            return;
        }
        int intValue = bleBroadcastSourceInfoPreference.getSourceInfoIndex().intValue();
        Bundle bundle = new Bundle();
        bundle.putString("device_address", this.mCachedDevice.getAddress());
        bundle.putParcelable("broadcast_source_info", bleBroadcastSourceInfo);
        bundle.putInt("broadcast_source_index", intValue);
        new SubSettingLauncher(this.mFragment.getContext()).setDestination(BleBroadcastSourceInfoDetailsFragment.class.getName()).setArguments(bundle).setTitleRes(R.string.source_info_details_title).setSourceMetricsCategory(this.mFragment.getMetricsCategory()).launch();
    }
}
