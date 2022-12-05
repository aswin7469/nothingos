package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.SignalIcon$MobileIconGroup;
import com.android.settingslib.mobile.TelephonyIcons;
import com.google.android.collect.Lists;
import com.qti.extphone.Client;
import com.qti.extphone.ExtPhoneCallbackBase;
import com.qti.extphone.ExtTelephonyManager;
import com.qti.extphone.IExtPhoneCallback;
import com.qti.extphone.NrIconType;
import com.qti.extphone.ServiceCallback;
import com.qti.extphone.Status;
import com.qti.extphone.Token;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class FiveGServiceClient {
    private static final boolean DEBUG = true;
    private static FiveGServiceClient sInstance;
    private Client mClient;
    private Context mContext;
    private ExtTelephonyManager mExtTelephonyManager;
    private String mPackageName;
    private boolean mServiceConnected;
    private final ArrayList<WeakReference<KeyguardUpdateMonitorCallback>> mKeyguardUpdateMonitorCallbacks = Lists.newArrayList();
    @VisibleForTesting
    final SparseArray<IFiveGStateListener> mStatesListeners = new SparseArray<>();
    private final SparseArray<FiveGServiceState> mCurrentServiceStates = new SparseArray<>();
    private final SparseArray<FiveGServiceState> mLastServiceStates = new SparseArray<>();
    private int mInitRetryTimes = 0;
    private boolean mIsConnectInProgress = false;
    private ServiceCallback mServiceCallback = new ServiceCallback() { // from class: com.android.systemui.statusbar.policy.FiveGServiceClient.1
        public void onConnected() {
            Log.d("FiveGServiceClient", "ExtTelephony Service connected");
            FiveGServiceClient.this.mServiceConnected = true;
            FiveGServiceClient.this.mIsConnectInProgress = false;
            FiveGServiceClient fiveGServiceClient = FiveGServiceClient.this;
            fiveGServiceClient.mClient = fiveGServiceClient.mExtTelephonyManager.registerCallback(FiveGServiceClient.this.mPackageName, FiveGServiceClient.this.mCallback);
            FiveGServiceClient.this.initFiveGServiceState();
            Log.d("FiveGServiceClient", "Client = " + FiveGServiceClient.this.mClient);
        }

        public void onDisconnected() {
            Log.d("FiveGServiceClient", "ExtTelephony Service disconnected...");
            if (FiveGServiceClient.this.mServiceConnected) {
                FiveGServiceClient.this.mExtTelephonyManager.unRegisterCallback(FiveGServiceClient.this.mCallback);
            }
            FiveGServiceClient.this.mServiceConnected = false;
            FiveGServiceClient.this.mClient = null;
            FiveGServiceClient.this.mIsConnectInProgress = false;
            FiveGServiceClient.this.mHandler.sendEmptyMessageDelayed(1024, 5000L);
        }
    };
    private Handler mHandler = new Handler() { // from class: com.android.systemui.statusbar.policy.FiveGServiceClient.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1024:
                    FiveGServiceClient.this.connectService();
                    return;
                case 1025:
                    FiveGServiceClient.this.initFiveGServiceState();
                    return;
                case 1026:
                    FiveGServiceClient.this.notifyMonitorCallback();
                    return;
                default:
                    return;
            }
        }
    };
    @VisibleForTesting
    protected IExtPhoneCallback mCallback = new ExtPhoneCallbackBase() { // from class: com.android.systemui.statusbar.policy.FiveGServiceClient.3
        public void onNrIconType(int i, Token token, Status status, NrIconType nrIconType) throws RemoteException {
            Log.d("FiveGServiceClient", "onNrIconType: slotId = " + i + " token = " + token + " status" + status + " NrIconType = " + nrIconType);
            if (status.get() == 1) {
                FiveGServiceState currentServiceState = FiveGServiceClient.this.getCurrentServiceState(i);
                currentServiceState.mNrIconType = nrIconType.get();
                FiveGServiceClient.this.update5GIcon(currentServiceState, i);
                FiveGServiceClient.this.notifyListenersIfNecessary(i);
            }
        }
    };

    /* loaded from: classes2.dex */
    public interface IFiveGStateListener {
        void onStateChanged(FiveGServiceState fiveGServiceState);
    }

    static {
        Log.isLoggable("FiveGServiceClient", 3);
    }

    /* loaded from: classes2.dex */
    public static class FiveGServiceState {
        private int mNrIconType = -1;
        private SignalIcon$MobileIconGroup mIconGroup = TelephonyIcons.UNKNOWN;

        public boolean isNrIconTypeValid() {
            int i = this.mNrIconType;
            return (i == -1 || i == 0) ? false : true;
        }

        @VisibleForTesting
        public SignalIcon$MobileIconGroup getIconGroup() {
            return this.mIconGroup;
        }

        @VisibleForTesting
        int getNrIconType() {
            return this.mNrIconType;
        }

        public void copyFrom(FiveGServiceState fiveGServiceState) {
            this.mIconGroup = fiveGServiceState.mIconGroup;
            this.mNrIconType = fiveGServiceState.mNrIconType;
        }

        public boolean equals(FiveGServiceState fiveGServiceState) {
            return this.mIconGroup == fiveGServiceState.mIconGroup && this.mNrIconType == fiveGServiceState.mNrIconType;
        }

        public String toString() {
            return "mNrIconType=" + this.mNrIconType + ", mIconGroup=" + this.mIconGroup;
        }
    }

    public FiveGServiceClient(Context context) {
        this.mContext = context;
        this.mPackageName = context.getPackageName();
        if (this.mExtTelephonyManager == null) {
            this.mExtTelephonyManager = ExtTelephonyManager.getInstance(this.mContext);
        }
    }

    public static FiveGServiceClient getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new FiveGServiceClient(context);
        }
        return sInstance;
    }

    public void registerCallback(KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback) {
        this.mKeyguardUpdateMonitorCallbacks.add(new WeakReference<>(keyguardUpdateMonitorCallback));
    }

    public void registerListener(int i, IFiveGStateListener iFiveGStateListener) {
        Log.d("FiveGServiceClient", "registerListener phoneId=" + i);
        resetState(i);
        this.mStatesListeners.put(i, iFiveGStateListener);
        if (!isServiceConnected()) {
            connectService();
        } else {
            initFiveGServiceState(i);
        }
    }

    private void resetState(int i) {
        Log.d("FiveGServiceClient", "resetState phoneId=" + i);
        FiveGServiceState currentServiceState = getCurrentServiceState(i);
        currentServiceState.mNrIconType = -1;
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup = TelephonyIcons.UNKNOWN;
        currentServiceState.mIconGroup = signalIcon$MobileIconGroup;
        FiveGServiceState lastServiceState = getLastServiceState(i);
        lastServiceState.mNrIconType = -1;
        lastServiceState.mIconGroup = signalIcon$MobileIconGroup;
    }

    public void unregisterListener(int i) {
        Log.d("FiveGServiceClient", "unregisterListener phoneId=" + i);
        this.mStatesListeners.remove(i);
        this.mCurrentServiceStates.remove(i);
        this.mLastServiceStates.remove(i);
    }

    public boolean isServiceConnected() {
        return this.mServiceConnected;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectService() {
        if (isServiceConnected() || this.mIsConnectInProgress) {
            return;
        }
        this.mIsConnectInProgress = true;
        Log.d("FiveGServiceClient", "Connect to ExtTelephony bound service...");
        this.mExtTelephonyManager.connectService(this.mServiceCallback);
    }

    @VisibleForTesting
    public FiveGServiceState getCurrentServiceState(int i) {
        return getServiceState(i, this.mCurrentServiceStates);
    }

    private FiveGServiceState getLastServiceState(int i) {
        return getServiceState(i, this.mLastServiceStates);
    }

    private static FiveGServiceState getServiceState(int i, SparseArray<FiveGServiceState> sparseArray) {
        FiveGServiceState fiveGServiceState = sparseArray.get(i);
        if (fiveGServiceState == null) {
            FiveGServiceState fiveGServiceState2 = new FiveGServiceState();
            sparseArray.put(i, fiveGServiceState2);
            return fiveGServiceState2;
        }
        return fiveGServiceState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyListenersIfNecessary(int i) {
        FiveGServiceState currentServiceState = getCurrentServiceState(i);
        FiveGServiceState lastServiceState = getLastServiceState(i);
        if (!currentServiceState.equals(lastServiceState)) {
            if (DEBUG) {
                Log.d("FiveGServiceClient", "phoneId(" + i + ") Change in state from " + lastServiceState + " \n\tto " + currentServiceState);
            }
            lastServiceState.copyFrom(currentServiceState);
            IFiveGStateListener iFiveGStateListener = this.mStatesListeners.get(i);
            if (iFiveGStateListener != null) {
                iFiveGStateListener.onStateChanged(currentServiceState);
            }
            this.mHandler.sendEmptyMessage(1026);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initFiveGServiceState() {
        Log.d("FiveGServiceClient", "initFiveGServiceState size=" + this.mStatesListeners.size());
        for (int i = 0; i < this.mStatesListeners.size(); i++) {
            initFiveGServiceState(this.mStatesListeners.keyAt(i));
        }
    }

    private void initFiveGServiceState(int i) {
        Log.d("FiveGServiceClient", "mServiceConnected=" + this.mServiceConnected + " mClient=" + this.mClient);
        if (!this.mServiceConnected || this.mClient == null) {
            return;
        }
        Log.d("FiveGServiceClient", "query 5G service state for phoneId " + i);
        try {
            Log.d("FiveGServiceClient", "queryNrIconType result:" + this.mExtTelephonyManager.queryNrIconType(i, this.mClient));
        } catch (Exception e) {
            Log.d("FiveGServiceClient", "initFiveGServiceState: Exception = " + e);
            if (this.mInitRetryTimes >= 4 || this.mHandler.hasMessages(1025)) {
                return;
            }
            this.mHandler.sendEmptyMessageDelayed(1025, (this.mInitRetryTimes * 2000) + 3000);
            this.mInitRetryTimes++;
        }
    }

    @VisibleForTesting
    void update5GIcon(FiveGServiceState fiveGServiceState, int i) {
        fiveGServiceState.mIconGroup = getNrIconGroup(fiveGServiceState.mNrIconType, i);
    }

    private SignalIcon$MobileIconGroup getNrIconGroup(int i, int i2) {
        SignalIcon$MobileIconGroup signalIcon$MobileIconGroup = TelephonyIcons.UNKNOWN;
        if (i != 1) {
            return i != 2 ? signalIcon$MobileIconGroup : TelephonyIcons.FIVE_G_UWB;
        }
        return TelephonyIcons.FIVE_G_BASIC;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyMonitorCallback() {
        for (int i = 0; i < this.mKeyguardUpdateMonitorCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mKeyguardUpdateMonitorCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onRefreshCarrierInfo();
            }
        }
    }
}
