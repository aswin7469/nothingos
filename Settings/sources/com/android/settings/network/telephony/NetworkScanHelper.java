package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.telephony.CellInfo;
import android.telephony.NetworkScan;
import android.telephony.NetworkScanRequest;
import android.telephony.RadioAccessSpecifier;
import android.telephony.TelephonyManager;
import android.telephony.TelephonyScanManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class NetworkScanHelper {
    static final boolean INCREMENTAL_RESULTS = true;
    static final int INCREMENTAL_RESULTS_PERIODICITY_SEC = 3;
    static final int MAX_SEARCH_TIME_SEC = 254;
    static final int SEARCH_PERIODICITY_SEC = 5;
    private IntentFilter filter = new IntentFilter("qualcomm.intent.action.ACTION_INCREMENTAL_NW_SCAN_IND");
    private Context mContext;
    private final Executor mExecutor;
    private final TelephonyScanManager.NetworkScanCallback mInternalNetworkScanCallback;
    private final LegacyIncrementalScanBroadcastReceiver mLegacyIncrScanReceiver;
    private final NetworkScanCallback mNetworkScanCallback;
    private NetworkScan mNetworkScanRequester;
    private final TelephonyManager mTelephonyManager;

    /* loaded from: classes.dex */
    public interface NetworkScanCallback {
        void onComplete();

        void onError(int i);

        void onResults(List<CellInfo> list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$hasNrSaCapability$0(int i) {
        if (i == 2) {
            return INCREMENTAL_RESULTS;
        }
        return false;
    }

    public NetworkScanHelper(Context context, TelephonyManager telephonyManager, NetworkScanCallback networkScanCallback, Executor executor) {
        this.mContext = context;
        this.mTelephonyManager = telephonyManager;
        this.mNetworkScanCallback = networkScanCallback;
        NetworkScanCallbackImpl networkScanCallbackImpl = new NetworkScanCallbackImpl();
        this.mInternalNetworkScanCallback = networkScanCallbackImpl;
        this.mExecutor = executor;
        this.mLegacyIncrScanReceiver = new LegacyIncrementalScanBroadcastReceiver(this.mContext, networkScanCallbackImpl);
    }

    NetworkScanRequest createNetworkScanForPreferredAccessNetworks() {
        long preferredNetworkTypeBitmask = this.mTelephonyManager.getPreferredNetworkTypeBitmask() & 906119;
        ArrayList arrayList = new ArrayList();
        int i = (preferredNetworkTypeBitmask > 0L ? 1 : (preferredNetworkTypeBitmask == 0L ? 0 : -1));
        if (i == 0 || (32843 & preferredNetworkTypeBitmask) != 0) {
            arrayList.add(new RadioAccessSpecifier(1, null, null));
        }
        if (i == 0 || (93108 & preferredNetworkTypeBitmask) != 0) {
            arrayList.add(new RadioAccessSpecifier(2, null, null));
        }
        if (i == 0 || (397312 & preferredNetworkTypeBitmask) != 0) {
            arrayList.add(new RadioAccessSpecifier(3, null, null));
        }
        if (i == 0 || (hasNrSaCapability() && (preferredNetworkTypeBitmask & 524288) != 0)) {
            arrayList.add(new RadioAccessSpecifier(6, null, null));
            Log.d("NetworkScanHelper", "radioAccessSpecifiers add NGRAN.");
        }
        return new NetworkScanRequest(0, (RadioAccessSpecifier[]) arrayList.toArray(new RadioAccessSpecifier[arrayList.size()]), 5, MAX_SEARCH_TIME_SEC, INCREMENTAL_RESULTS, 3, null);
    }

    public void startNetworkScan(int i) {
        Log.d("NetworkScanHelper", "startNetworkScan: " + i);
        if (i == 1) {
            if (this.mNetworkScanRequester != null) {
                return;
            }
            NetworkScan requestNetworkScan = this.mTelephonyManager.requestNetworkScan(createNetworkScanForPreferredAccessNetworks(), this.mExecutor, this.mInternalNetworkScanCallback);
            this.mNetworkScanRequester = requestNetworkScan;
            if (requestNetworkScan != null) {
                return;
            }
            onError(10000);
        } else if (i != 2) {
        } else {
            this.mContext.registerReceiver(this.mLegacyIncrScanReceiver, this.filter);
            boolean performIncrementalScan = TelephonyUtils.performIncrementalScan(this.mContext, this.mTelephonyManager.getSlotIndex());
            Log.d("NetworkScanHelper", "success: " + performIncrementalScan);
            if (performIncrementalScan) {
                return;
            }
            onError(10000);
        }
    }

    public void stopNetworkQuery() {
        NetworkScan networkScan = this.mNetworkScanRequester;
        if (networkScan != null) {
            networkScan.stopScan();
            this.mNetworkScanRequester = null;
            return;
        }
        try {
            int slotIndex = this.mTelephonyManager.getSlotIndex();
            if (slotIndex >= 0 && slotIndex < this.mTelephonyManager.getActiveModemCount()) {
                TelephonyUtils.abortIncrementalScan(this.mContext, slotIndex);
            } else {
                Log.d("NetworkScanHelper", "slotIndex is invalid, skipping abort");
            }
            this.mContext.unregisterReceiver(this.mLegacyIncrScanReceiver);
        } catch (IllegalArgumentException unused) {
            Log.e("NetworkScanHelper", "IllegalArgumentException");
        } catch (NullPointerException e) {
            Log.e("NetworkScanHelper", "abortIncrementalScan Exception: ", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onResults(List<CellInfo> list) {
        this.mNetworkScanCallback.onResults(list);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onComplete() {
        this.mNetworkScanCallback.onComplete();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onError(int i) {
        this.mNetworkScanCallback.onError(i);
    }

    private boolean hasNrSaCapability() {
        return Arrays.stream(this.mTelephonyManager.getPhoneCapability().getDeviceNrCapabilities()).anyMatch(NetworkScanHelper$$ExternalSyntheticLambda0.INSTANCE);
    }

    /* loaded from: classes.dex */
    private final class NetworkScanCallbackImpl extends TelephonyScanManager.NetworkScanCallback {
        private NetworkScanCallbackImpl() {
        }

        @Override // android.telephony.TelephonyScanManager.NetworkScanCallback
        public void onResults(List<CellInfo> list) {
            Log.d("NetworkScanHelper", "Async scan onResults() results = " + CellInfoUtil.cellInfoListToString(list));
            NetworkScanHelper.this.onResults(list);
        }

        @Override // android.telephony.TelephonyScanManager.NetworkScanCallback
        public void onComplete() {
            Log.d("NetworkScanHelper", "async scan onComplete()");
            NetworkScanHelper.this.onComplete();
        }

        @Override // android.telephony.TelephonyScanManager.NetworkScanCallback
        public void onError(int i) {
            Log.d("NetworkScanHelper", "async scan onError() errorCode = " + i);
            NetworkScanHelper.this.onError(i);
        }
    }
}
