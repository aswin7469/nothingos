package com.android.settingslib.bluetooth;

import android.bluetooth.BleBroadcastAudioScanAssistCallback;
import android.bluetooth.BleBroadcastAudioScanAssistManager;
import android.bluetooth.BleBroadcastSourceInfo;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.util.Log;
import androidx.annotation.Keep;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class VendorCachedBluetoothDevice extends CachedBluetoothDevice {
    private static final boolean V = Log.isLoggable("VendorCachedBluetoothDevice", 2);
    private static Map<Integer, BleBroadcastSourceInfo> mBleBroadcastReceiverStates = new HashMap();
    private static Map<CachedBluetoothDevice, VendorCachedBluetoothDevice> mVcbdEntries = new IdentityHashMap();
    private LocalBluetoothProfileManager mProfileManager;
    private BleBroadcastAudioScanAssistManager mScanAssistManager;
    private ScanResult mScanRes = null;
    private BleBroadcastAudioScanAssistCallback mScanAssistCallback = new BleBroadcastAudioScanAssistCallback() { // from class: com.android.settingslib.bluetooth.VendorCachedBluetoothDevice.1
        public void onBleBroadcastAudioSourceAdded(BluetoothDevice bluetoothDevice, byte b, int i) {
        }

        public void onBleBroadcastAudioSourceRemoved(BluetoothDevice bluetoothDevice, byte b, int i) {
        }

        public void onBleBroadcastAudioSourceUpdated(BluetoothDevice bluetoothDevice, byte b, int i) {
        }

        public void onBleBroadcastPinUpdated(BluetoothDevice bluetoothDevice, byte b, int i) {
        }

        public void onBleBroadcastSourceFound(ScanResult scanResult) {
            if (VendorCachedBluetoothDevice.V) {
                Log.d("VendorCachedBluetoothDevice", "onBleBroadcastSourceFound" + scanResult.getDevice());
            }
            VendorCachedBluetoothDevice.this.setScanResult(scanResult);
        }
    };

    public static VendorCachedBluetoothDevice getVendorCachedBluetoothDevice(CachedBluetoothDevice cachedBluetoothDevice, LocalBluetoothProfileManager localBluetoothProfileManager) {
        Map<CachedBluetoothDevice, VendorCachedBluetoothDevice> map = mVcbdEntries;
        VendorCachedBluetoothDevice vendorCachedBluetoothDevice = map != null ? map.get(cachedBluetoothDevice) : null;
        if (vendorCachedBluetoothDevice != null || localBluetoothProfileManager == null) {
            return vendorCachedBluetoothDevice;
        }
        VendorCachedBluetoothDevice vendorCachedBluetoothDevice2 = new VendorCachedBluetoothDevice(cachedBluetoothDevice, localBluetoothProfileManager);
        Log.d("VendorCachedBluetoothDevice", "getVendorCachedBluetoothDevice: created new Instance");
        mVcbdEntries.put(cachedBluetoothDevice, vendorCachedBluetoothDevice2);
        return vendorCachedBluetoothDevice2;
    }

    VendorCachedBluetoothDevice(CachedBluetoothDevice cachedBluetoothDevice, LocalBluetoothProfileManager localBluetoothProfileManager) {
        super(cachedBluetoothDevice);
        this.mProfileManager = null;
        this.mProfileManager = localBluetoothProfileManager;
        mBleBroadcastReceiverStates = new HashMap();
        InitializeSAManager();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.settingslib.bluetooth.CachedBluetoothDevice
    public void onProfileStateChanged(LocalBluetoothProfile localBluetoothProfile, int i) {
        if (V) {
            Log.d("VendorCachedBluetoothDevice", "onProfileStateChanged: profile " + localBluetoothProfile + ", device=" + this.mDevice + ", newProfileState " + i);
        }
        if (!(localBluetoothProfile instanceof BCProfile) || i != 0) {
            return;
        }
        cleanUpSAMananger();
        super.lambda$refresh$0();
    }

    public BleBroadcastAudioScanAssistManager getScanAssistManager() {
        InitializeSAManager();
        return this.mScanAssistManager;
    }

    void InitializeSAManager() {
        this.mScanAssistManager = ((BCProfile) this.mProfileManager.getBCProfile()).getBSAManager(this.mDevice, this.mScanAssistCallback);
    }

    void cleanUpSAMananger() {
        this.mScanAssistManager = null;
        Map<Integer, BleBroadcastSourceInfo> map = mBleBroadcastReceiverStates;
        if (map != null) {
            map.clear();
        }
    }

    void updateBroadcastreceiverStates(BleBroadcastSourceInfo bleBroadcastSourceInfo, int i, int i2) {
        if (mBleBroadcastReceiverStates.get(Integer.valueOf(i)) != null) {
            Log.d("VendorCachedBluetoothDevice", "updateBroadcastreceiverStates: Replacing receiver State Information");
            mBleBroadcastReceiverStates.replace(Integer.valueOf(i), bleBroadcastSourceInfo);
        } else {
            mBleBroadcastReceiverStates.put(Integer.valueOf(i), bleBroadcastSourceInfo);
        }
        super.lambda$refresh$0();
    }

    public int getNumberOfBleBroadcastReceiverStates() {
        int i = 0;
        if (this.mScanAssistManager == null) {
            InitializeSAManager();
            if (this.mScanAssistManager == null) {
                return 0;
            }
        }
        List allBroadcastSourceInformation = this.mScanAssistManager.getAllBroadcastSourceInformation();
        if (allBroadcastSourceInformation != null) {
            i = allBroadcastSourceInformation.size();
        }
        if (V) {
            Log.d("VendorCachedBluetoothDevice", "getNumberOfBleBroadcastReceiverStates:" + i);
        }
        return i;
    }

    public Map<Integer, BleBroadcastSourceInfo> getAllBleBroadcastreceiverStates() {
        if (this.mScanAssistManager == null) {
            InitializeSAManager();
            if (this.mScanAssistManager == null) {
                Log.e("VendorCachedBluetoothDevice", "SA Manager cant be initialized");
                return null;
            }
        }
        List allBroadcastSourceInformation = this.mScanAssistManager.getAllBroadcastSourceInformation();
        if (allBroadcastSourceInformation == null) {
            Log.e("VendorCachedBluetoothDevice", "getAllBleBroadcastreceiverStates: no src Info");
            return null;
        }
        for (int i = 0; i < allBroadcastSourceInformation.size(); i++) {
            BleBroadcastSourceInfo bleBroadcastSourceInfo = (BleBroadcastSourceInfo) allBroadcastSourceInformation.get(i);
            mBleBroadcastReceiverStates.put(Integer.valueOf(bleBroadcastSourceInfo.getSourceId()), bleBroadcastSourceInfo);
        }
        return mBleBroadcastReceiverStates;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onBroadcastReceiverStateChanged(BleBroadcastSourceInfo bleBroadcastSourceInfo, int i, int i2) {
        updateBroadcastreceiverStates(bleBroadcastSourceInfo, i, i2);
    }

    public void setScanResult(ScanResult scanResult) {
        this.mScanRes = scanResult;
    }

    public ScanResult getScanResult() {
        return this.mScanRes;
    }

    @Keep
    public boolean isBroadcastAudioSynced() {
        if (this.mScanAssistManager == null) {
            InitializeSAManager();
            if (this.mScanAssistManager == null) {
                Log.e("VendorCachedBluetoothDevice", "SA Manager cant be initialized");
                return false;
            }
        }
        List allBroadcastSourceInformation = this.mScanAssistManager.getAllBroadcastSourceInformation();
        if (allBroadcastSourceInformation == null) {
            Log.e("VendorCachedBluetoothDevice", "isBroadcastAudioSynced: no src Info");
            return false;
        }
        for (int i = 0; i < allBroadcastSourceInformation.size(); i++) {
            if (((BleBroadcastSourceInfo) allBroadcastSourceInformation.get(i)).getAudioSyncState() == 1) {
                return true;
            }
        }
        Log.d("VendorCachedBluetoothDevice", "isAudioSynced: false");
        return false;
    }
}
