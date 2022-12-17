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
import com.qti.extphone.ExtTelephonyManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

public class NetworkScanHelper {
    static final boolean INCREMENTAL_RESULTS = true;
    static final int INCREMENTAL_RESULTS_PERIODICITY_SEC = 3;
    static final int MAX_SEARCH_TIME_SEC = 254;
    static final int SEARCH_PERIODICITY_SEC = 5;
    private IntentFilter filter = new IntentFilter("qualcomm.intent.action.ACTION_INCREMENTAL_NW_SCAN_IND");
    private Context mContext;
    private final Executor mExecutor;
    private ExtTelephonyManager mExtTelephonyManager;
    private final TelephonyScanManager.NetworkScanCallback mInternalNetworkScanCallback;
    private final LegacyIncrementalScanBroadcastReceiver mLegacyIncrScanReceiver;
    private final NetworkScanCallback mNetworkScanCallback;
    private NetworkScan mNetworkScanRequester;
    private final TelephonyManager mTelephonyManager;

    public interface NetworkScanCallback {
        void onComplete();

        void onError(int i);

        void onResults(List<CellInfo> list);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$hasNrSaCapability$0(int i) {
        return i == 2;
    }

    public NetworkScanHelper(Context context, TelephonyManager telephonyManager, NetworkScanCallback networkScanCallback, Executor executor) {
        this.mContext = context;
        this.mTelephonyManager = telephonyManager;
        this.mNetworkScanCallback = networkScanCallback;
        NetworkScanCallbackImpl networkScanCallbackImpl = new NetworkScanCallbackImpl();
        this.mInternalNetworkScanCallback = networkScanCallbackImpl;
        this.mExecutor = executor;
        this.mLegacyIncrScanReceiver = new LegacyIncrementalScanBroadcastReceiver(this.mContext, networkScanCallbackImpl);
        this.mExtTelephonyManager = ExtTelephonyManager.getInstance(context);
    }

    /* access modifiers changed from: package-private */
    public NetworkScanRequest createNetworkScanForPreferredAccessNetworks() {
        int i;
        int i2;
        long preferredNetworkTypeBitmask = this.mTelephonyManager.getPreferredNetworkTypeBitmask() & 906119;
        ArrayList arrayList = new ArrayList();
        int i3 = (preferredNetworkTypeBitmask > 0 ? 1 : (preferredNetworkTypeBitmask == 0 ? 0 : -1));
        if (i3 == 0 || (32843 & preferredNetworkTypeBitmask) != 0) {
            arrayList.add(new RadioAccessSpecifier(1, (int[]) null, (int[]) null));
        }
        if (i3 == 0 || (93108 & preferredNetworkTypeBitmask) != 0) {
            arrayList.add(new RadioAccessSpecifier(2, (int[]) null, (int[]) null));
        }
        if (i3 == 0 || (397312 & preferredNetworkTypeBitmask) != 0) {
            arrayList.add(new RadioAccessSpecifier(3, (int[]) null, (int[]) null));
        }
        if (i3 == 0 || (hasNrSaCapability() && (preferredNetworkTypeBitmask & 524288) != 0)) {
            arrayList.add(new RadioAccessSpecifier(6, (int[]) null, (int[]) null));
            Log.d("NetworkScanHelper", "radioAccessSpecifiers add NGRAN.");
        }
        if (MobileNetworkUtils.isCagSnpnEnabled(this.mContext)) {
            i = 0;
            i2 = MobileNetworkUtils.getAccessMode(this.mContext, this.mTelephonyManager.getSlotIndex());
        } else {
            i2 = 1;
            i = 1;
        }
        return new NetworkScanRequest(0, (RadioAccessSpecifier[]) arrayList.toArray(new RadioAccessSpecifier[arrayList.size()]), 5, MAX_SEARCH_TIME_SEC, true, 3, (ArrayList) null, i2, i);
    }

    public void startNetworkScan(int i) {
        Log.d("NetworkScanHelper", "startNetworkScan: " + i);
        if (i == 1) {
            if (this.mNetworkScanRequester == null) {
                NetworkScan requestNetworkScan = this.mTelephonyManager.requestNetworkScan(createNetworkScanForPreferredAccessNetworks(), this.mExecutor, this.mInternalNetworkScanCallback);
                this.mNetworkScanRequester = requestNetworkScan;
                if (requestNetworkScan == null) {
                    onError(10000);
                }
            }
        } else if (i == 2) {
            this.mContext.registerReceiver(this.mLegacyIncrScanReceiver, this.filter);
            boolean performIncrementalScan = TelephonyUtils.performIncrementalScan(this.mContext, this.mTelephonyManager.getSlotIndex());
            Log.d("NetworkScanHelper", "success: " + performIncrementalScan);
            if (!performIncrementalScan) {
                onError(10000);
            }
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
            if (slotIndex < 0 || slotIndex >= this.mTelephonyManager.getActiveModemCount()) {
                Log.d("NetworkScanHelper", "slotIndex is invalid, skipping abort");
            } else {
                TelephonyUtils.abortIncrementalScan(this.mContext, slotIndex);
            }
            this.mContext.unregisterReceiver(this.mLegacyIncrScanReceiver);
        } catch (NullPointerException e) {
            Log.e("NetworkScanHelper", "abortIncrementalScan Exception: ", e);
        } catch (IllegalArgumentException unused) {
            Log.e("NetworkScanHelper", "IllegalArgumentException");
        }
    }

    /* access modifiers changed from: private */
    public void onResults(List<CellInfo> list) {
        this.mNetworkScanCallback.onResults(list);
    }

    /* access modifiers changed from: private */
    public void onComplete() {
        this.mNetworkScanCallback.onComplete();
    }

    /* access modifiers changed from: private */
    public void onError(int i) {
        this.mNetworkScanCallback.onError(i);
    }

    private boolean hasNrSaCapability() {
        return Arrays.stream(this.mTelephonyManager.getPhoneCapability().getDeviceNrCapabilities()).anyMatch(new NetworkScanHelper$$ExternalSyntheticLambda0());
    }

    private final class NetworkScanCallbackImpl extends TelephonyScanManager.NetworkScanCallback {
        private NetworkScanCallbackImpl() {
        }

        public void onResults(List<CellInfo> list) {
            Log.d("NetworkScanHelper", "Async scan onResults() results = " + CellInfoUtil.cellInfoListToString(list));
            NetworkScanHelper.this.onResults(list);
        }

        public void onComplete() {
            Log.d("NetworkScanHelper", "async scan onComplete()");
            NetworkScanHelper.this.onComplete();
        }

        public void onError(int i) {
            Log.d("NetworkScanHelper", "async scan onError() errorCode = " + i);
            NetworkScanHelper.this.onError(i);
        }
    }
}
