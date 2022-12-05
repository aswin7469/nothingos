package android.bluetooth;

import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanResult;
import android.content.AttributionSource;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
/* loaded from: classes.dex */
public final class BleBroadcastAudioScanAssistManager {
    public static final String ACTION_BROADCAST_SOURCE_INFO = "android.bluetooth.BroadcastAudioSAManager.action.BROADCAST_SOURCE_INFO";
    private static final boolean DBG = true;
    public static final int SYNC_AUDIO = 1;
    public static final int SYNC_METADATA = 0;
    public static final int SYNC_METADATA_AUDIO = 2;
    private static final String TAG = "BleBroadcastAudioScanAssistManager";
    private static final boolean VDBG = true;
    BleBroadcastAudioScanAssistCallback mAppCallback;
    private final AttributionSource mAttributionSource;
    private BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mBluetoothDevice;
    BluetoothSyncHelper mBluetoothSyncHelper;
    int mSyncState = 0;
    BleBroadcastSourceInfo mBroadcastAudioSourceInfo = null;
    private byte INVALID_SOURCE_ID = -1;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface BroadcastAssistSyncState {
    }

    /* loaded from: classes.dex */
    private final class BassclientServiceListener implements BluetoothProfile.ServiceListener {
        private BassclientServiceListener() {
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            BleBroadcastAudioScanAssistManager.log(BleBroadcastAudioScanAssistManager.TAG, "BassService connected");
            BleBroadcastAudioScanAssistManager.this.onBluetoothSyncHelperStateChanged(true, proxy);
        }

        @Override // android.bluetooth.BluetoothProfile.ServiceListener
        public void onServiceDisconnected(int profile) {
            BleBroadcastAudioScanAssistManager.log(BleBroadcastAudioScanAssistManager.TAG, "BassService disconnected");
            BleBroadcastAudioScanAssistManager.this.onBluetoothSyncHelperStateChanged(false, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onBluetoothSyncHelperStateChanged(boolean on, BluetoothProfile proxy) {
        if (on) {
            BluetoothSyncHelper bluetoothSyncHelper = (BluetoothSyncHelper) proxy;
            this.mBluetoothSyncHelper = bluetoothSyncHelper;
            bluetoothSyncHelper.registerAppCallback(this.mBluetoothDevice, this.mAppCallback);
            notifyAll();
            return;
        }
        this.mBluetoothSyncHelper = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BleBroadcastAudioScanAssistManager(BluetoothSyncHelper scanOffloader, BluetoothDevice device, BleBroadcastAudioScanAssistCallback callback) {
        this.mBluetoothSyncHelper = null;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        this.mAppCallback = callback;
        this.mBluetoothDevice = device;
        this.mBluetoothSyncHelper = scanOffloader;
        this.mAttributionSource = defaultAdapter.getAttributionSource();
    }

    protected void finalize() {
        log(TAG, "finalize()");
        BluetoothSyncHelper bluetoothSyncHelper = this.mBluetoothSyncHelper;
        if (bluetoothSyncHelper != null) {
            bluetoothSyncHelper.unregisterAppCallback(this.mBluetoothDevice, this.mAppCallback);
        }
    }

    public boolean searchforLeAudioBroadcasters() {
        log(TAG, "searchforLeAudioBroadcasters: ");
        BluetoothSyncHelper bluetoothSyncHelper = this.mBluetoothSyncHelper;
        if (bluetoothSyncHelper == null) {
            Log.e(TAG, "searchforLeAudioBroadcasters: mBluetoothSyncHelper is null");
            return false;
        }
        return bluetoothSyncHelper.searchforLeAudioBroadcasters(this.mBluetoothDevice, this.mAttributionSource);
    }

    public boolean stopSearchforLeAudioBroadcasters() {
        log(TAG, "stopSearchforLeAudioBroadcasters()");
        BluetoothSyncHelper bluetoothSyncHelper = this.mBluetoothSyncHelper;
        if (bluetoothSyncHelper == null) {
            Log.e(TAG, "stopSearchforLeAudioBroadcasters: mBluetoothSyncHelper is null");
            return false;
        }
        return bluetoothSyncHelper.stopSearchforLeAudioBroadcasters(this.mBluetoothDevice, this.mAttributionSource);
    }

    private int convertMetadataSyncState(int syncState) {
        if (syncState == 2 || syncState == 0) {
            return 2;
        }
        return 0;
    }

    private int convertAudioDataSyncState(int syncState) {
        if (syncState == 2 || syncState == 1) {
            return 1;
        }
        Log.e(TAG, "searchforLeAudioBroadcasters: mBluetoothSyncHelper is null");
        return 0;
    }

    public boolean selectBroadcastSource(ScanResult scanRes, boolean isGroupOp) {
        if (scanRes == null) {
            Log.e(TAG, "selectBroadcastSource: Invalid scan res");
            return false;
        }
        log(TAG, "selectBroadcastSource: " + scanRes);
        BluetoothSyncHelper bluetoothSyncHelper = this.mBluetoothSyncHelper;
        if (bluetoothSyncHelper != null) {
            return bluetoothSyncHelper.selectBroadcastSource(this.mBluetoothDevice, scanRes, isGroupOp, this.mAttributionSource);
        }
        Log.e(TAG, "selectBroadcastSource: mBluetoothSyncHelper is null");
        return false;
    }

    private boolean isValidBroadcastSourceInfo(BleBroadcastSourceInfo srcInfo) {
        boolean ret = true;
        List<BleBroadcastSourceInfo> currentSourceInfos = this.mBluetoothSyncHelper.getAllBroadcastSourceInformation(this.mBluetoothDevice, this.mAttributionSource);
        if (currentSourceInfos == null) {
            Log.e(TAG, "no source info details for remote");
            ret = false;
        } else {
            int i = 0;
            while (true) {
                if (i >= currentSourceInfos.size()) {
                    break;
                } else if (!srcInfo.matches(currentSourceInfos.get(i))) {
                    i++;
                } else {
                    ret = false;
                    break;
                }
            }
        }
        log(TAG, "isValidBroadcastSourceInfo returns: " + ret);
        return ret;
    }

    private boolean isValidSourceId(byte sourceId) {
        boolean retVal = false;
        List<BleBroadcastSourceInfo> currentSourceInfos = this.mBluetoothSyncHelper.getAllBroadcastSourceInformation(this.mBluetoothDevice, this.mAttributionSource);
        if (currentSourceInfos == null) {
            retVal = false;
        } else {
            int i = 0;
            while (true) {
                if (i >= currentSourceInfos.size()) {
                    break;
                } else if (currentSourceInfos.get(i).getSourceId() != sourceId) {
                    i++;
                } else {
                    retVal = true;
                    break;
                }
            }
        }
        log(TAG, "isValidSourceId returns: " + retVal);
        return retVal;
    }

    private void printSelectedIndicies(List<BleBroadcastSourceChannel> selectedBISIndicies) {
        if (selectedBISIndicies == null) {
            log(TAG, "printSelectedIndicies : no selected indicies");
            return;
        }
        for (int i = 0; i < selectedBISIndicies.size(); i++) {
            log(TAG, selectedBISIndicies.get(i).getDescription() + ": " + selectedBISIndicies.get(i).getStatus());
        }
    }

    public boolean addBroadcastSource(BluetoothDevice audioSource, int syncState, List<BleBroadcastSourceChannel> selectedBroadcastChannels, boolean isGroupOp) {
        if (this.mBluetoothSyncHelper == null) {
            log(TAG, "addBroadcastSource: no BluetoothSyncHelper handle");
            return false;
        } else if (syncState != 0 && syncState != 2) {
            log(TAG, "addBroadcastSource: Invalid syncState" + syncState);
            return false;
        } else {
            printSelectedIndicies(selectedBroadcastChannels);
            this.mSyncState = syncState;
            int metadataSyncState = convertMetadataSyncState(syncState);
            int metadataSyncState2 = this.mSyncState;
            int audioSyncState = convertAudioDataSyncState(metadataSyncState2);
            BleBroadcastSourceInfo bleBroadcastSourceInfo = new BleBroadcastSourceInfo(audioSource, (byte) -69, 0, metadataSyncState, audioSyncState, selectedBroadcastChannels);
            this.mBroadcastAudioSourceInfo = bleBroadcastSourceInfo;
            if (isValidBroadcastSourceInfo(bleBroadcastSourceInfo)) {
                this.mBluetoothSyncHelper.addBroadcastSource(this.mBluetoothDevice, this.mBroadcastAudioSourceInfo, isGroupOp, this.mAttributionSource);
                return true;
            }
            log(TAG, "Similar source information already exists");
            return false;
        }
    }

    public boolean updateBroadcastSource(byte sourceId, int syncState, List<BleBroadcastSourceChannel> selectedBroadcastChannels, boolean isGroupOp) {
        if (this.mBluetoothSyncHelper == null) {
            log(TAG, "updateBroadcastSource: no BluetoothSyncHelper handle");
            return false;
        } else if (!isValidSourceId(sourceId)) {
            log(TAG, "updateBroadcastSource: Invalid source Id");
            return false;
        } else {
            log(TAG, "updateBroadcastSource: sourceId" + ((int) sourceId) + ", syncState:" + syncState);
            this.mSyncState = syncState;
            int metadataSyncState = convertMetadataSyncState(syncState);
            int audioSyncState = convertAudioDataSyncState(this.mSyncState);
            printSelectedIndicies(selectedBroadcastChannels);
            log(TAG, "updateBroadcastSource: audioSyncState:" + audioSyncState);
            log(TAG, "updateBroadcastSource: metadataSyncState:" + metadataSyncState);
            BleBroadcastSourceInfo sourceInfo = new BleBroadcastSourceInfo(sourceId);
            sourceInfo.setMetadataSyncState(metadataSyncState);
            sourceInfo.setAudioSyncState(audioSyncState);
            sourceInfo.setSourceId(sourceId);
            sourceInfo.setBroadcastChannelsSyncStatus(selectedBroadcastChannels);
            return this.mBluetoothSyncHelper.updateBroadcastSource(this.mBluetoothDevice, sourceInfo, isGroupOp, this.mAttributionSource);
        }
    }

    public boolean setBroadcastCode(byte sourceId, String broadcastCode, boolean isGroupOp) {
        if (this.mBluetoothSyncHelper == null) {
            log(TAG, "setBroadcastCode: no BluetoothSyncHelper handle");
            return false;
        } else if (!isValidSourceId(sourceId)) {
            log(TAG, "setBroadcastCode: Invalid source Id");
            return false;
        } else {
            log(TAG, "setBroadcastCode: sourceId:" + ((int) sourceId) + "BroadcastCode:" + broadcastCode);
            BleBroadcastSourceInfo sourceInfo = new BleBroadcastSourceInfo(sourceId);
            sourceInfo.setSourceId(sourceId);
            sourceInfo.setBroadcastCode(broadcastCode);
            return this.mBluetoothSyncHelper.setBroadcastCode(this.mBluetoothDevice, sourceInfo, isGroupOp, this.mAttributionSource);
        }
    }

    public boolean removeBroadcastSource(byte sourceId, boolean isGroupOp) {
        if (this.mBluetoothSyncHelper == null) {
            log(TAG, "removeBroadcastSource: no BluetoothSyncHelper handle");
            return false;
        } else if (!isValidSourceId(sourceId)) {
            log(TAG, "removeBroadcastSource: Invalid source Id");
            return false;
        } else {
            log(TAG, "removeBroadcastSource: sourceId" + ((int) sourceId));
            return this.mBluetoothSyncHelper.removeBroadcastSource(this.mBluetoothDevice, sourceId, isGroupOp, this.mAttributionSource);
        }
    }

    public List<BleBroadcastSourceInfo> getAllBroadcastSourceInformation() {
        BluetoothSyncHelper bluetoothSyncHelper = this.mBluetoothSyncHelper;
        if (bluetoothSyncHelper == null) {
            log(TAG, "GetNumberOfAcceptableBroadcastSources: no BluetoothSyncHelper handle");
            return null;
        }
        return bluetoothSyncHelper.getAllBroadcastSourceInformation(this.mBluetoothDevice, this.mAttributionSource);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String TAG2, String msg) {
        BleBroadcastSourceInfo.BASS_Debug(TAG2, msg);
    }
}
