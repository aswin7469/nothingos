package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.SignalIcon;
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

public class FiveGServiceClient {
    private static final boolean DEBUG = true;
    private static final int DELAY_INCREMENT = 2000;
    private static final int DELAY_MILLISECOND = 3000;
    private static final int MAX_RETRY = 4;
    private static final int MESSAGE_NOTIFIY_MONITOR_CALLBACK = 1026;
    private static final int MESSAGE_REBIND = 1024;
    private static final int MESSAGE_REINIT = 1025;
    private static final String TAG = "FiveGServiceClient";
    private static FiveGServiceClient sInstance;
    protected IExtPhoneCallback mCallback = new ExtPhoneCallbackBase() {
        public void onNrIconType(int i, Token token, Status status, NrIconType nrIconType) throws RemoteException {
            Log.d(FiveGServiceClient.TAG, "onNrIconType: slotId = " + i + " token = " + token + " status" + status + " NrIconType = " + nrIconType);
            if (status.get() == 1) {
                FiveGServiceState currentServiceState = FiveGServiceClient.this.getCurrentServiceState(i);
                int unused = currentServiceState.mNrIconType = nrIconType.get();
                FiveGServiceClient.this.update5GIcon(currentServiceState, i);
                FiveGServiceClient.this.notifyListenersIfNecessary(i);
            }
        }
    };
    /* access modifiers changed from: private */
    public Client mClient;
    private Context mContext;
    private final SparseArray<FiveGServiceState> mCurrentServiceStates = new SparseArray<>();
    /* access modifiers changed from: private */
    public ExtTelephonyManager mExtTelephonyManager;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            int i = message.what;
            switch (message.what) {
                case 1024:
                    FiveGServiceClient.this.connectService();
                    return;
                case 1025:
                    FiveGServiceClient.this.initFiveGServiceState();
                    return;
                case FiveGServiceClient.MESSAGE_NOTIFIY_MONITOR_CALLBACK /*1026*/:
                    FiveGServiceClient.this.notifyMonitorCallback();
                    return;
                default:
                    return;
            }
        }
    };
    private int mInitRetryTimes = 0;
    /* access modifiers changed from: private */
    public boolean mIsConnectInProgress = false;
    private final ArrayList<WeakReference<KeyguardUpdateMonitorCallback>> mKeyguardUpdateMonitorCallbacks = Lists.newArrayList();
    private final SparseArray<FiveGServiceState> mLastServiceStates = new SparseArray<>();
    /* access modifiers changed from: private */
    public String mPackageName;
    private ServiceCallback mServiceCallback = new ServiceCallback() {
        public void onConnected() {
            Log.d(FiveGServiceClient.TAG, "ExtTelephony Service connected");
            boolean unused = FiveGServiceClient.this.mServiceConnected = true;
            boolean unused2 = FiveGServiceClient.this.mIsConnectInProgress = false;
            FiveGServiceClient fiveGServiceClient = FiveGServiceClient.this;
            Client unused3 = fiveGServiceClient.mClient = fiveGServiceClient.mExtTelephonyManager.registerCallback(FiveGServiceClient.this.mPackageName, FiveGServiceClient.this.mCallback);
            FiveGServiceClient.this.initFiveGServiceState();
            Log.d(FiveGServiceClient.TAG, "Client = " + FiveGServiceClient.this.mClient);
        }

        public void onDisconnected() {
            Log.d(FiveGServiceClient.TAG, "ExtTelephony Service disconnected...");
            if (FiveGServiceClient.this.mServiceConnected) {
                FiveGServiceClient.this.mExtTelephonyManager.unRegisterCallback(FiveGServiceClient.this.mCallback);
            }
            boolean unused = FiveGServiceClient.this.mServiceConnected = false;
            Client unused2 = FiveGServiceClient.this.mClient = null;
            boolean unused3 = FiveGServiceClient.this.mIsConnectInProgress = false;
            FiveGServiceClient.this.mHandler.sendEmptyMessageDelayed(1024, 5000);
        }
    };
    /* access modifiers changed from: private */
    public boolean mServiceConnected;
    final SparseArray<IFiveGStateListener> mStatesListeners = new SparseArray<>();

    public interface IFiveGStateListener {
        void onStateChanged(FiveGServiceState fiveGServiceState);
    }

    static {
        Log.isLoggable(TAG, 3);
    }

    public static class FiveGServiceState {
        /* access modifiers changed from: private */
        public SignalIcon.MobileIconGroup mIconGroup = TelephonyIcons.UNKNOWN;
        /* access modifiers changed from: private */
        public int mNrIconType = -1;

        public boolean isNrIconTypeValid() {
            int i = this.mNrIconType;
            return (i == -1 || i == 0) ? false : true;
        }

        public SignalIcon.MobileIconGroup getIconGroup() {
            return this.mIconGroup;
        }

        public int getNrIconType() {
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
            StringBuilder sb = new StringBuilder("mNrIconType=");
            sb.append(this.mNrIconType).append(", mIconGroup=").append((Object) this.mIconGroup);
            return sb.toString();
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
        this.mKeyguardUpdateMonitorCallbacks.add(new WeakReference(keyguardUpdateMonitorCallback));
    }

    public void registerListener(int i, IFiveGStateListener iFiveGStateListener) {
        Log.d(TAG, "registerListener phoneId=" + i);
        resetState(i);
        this.mStatesListeners.put(i, iFiveGStateListener);
        if (!isServiceConnected()) {
            connectService();
        } else {
            initFiveGServiceState(i);
        }
    }

    private void resetState(int i) {
        Log.d(TAG, "resetState phoneId=" + i);
        FiveGServiceState currentServiceState = getCurrentServiceState(i);
        int unused = currentServiceState.mNrIconType = -1;
        SignalIcon.MobileIconGroup unused2 = currentServiceState.mIconGroup = TelephonyIcons.UNKNOWN;
        FiveGServiceState lastServiceState = getLastServiceState(i);
        int unused3 = lastServiceState.mNrIconType = -1;
        SignalIcon.MobileIconGroup unused4 = lastServiceState.mIconGroup = TelephonyIcons.UNKNOWN;
    }

    public void unregisterListener(int i) {
        Log.d(TAG, "unregisterListener phoneId=" + i);
        this.mStatesListeners.remove(i);
        this.mCurrentServiceStates.remove(i);
        this.mLastServiceStates.remove(i);
    }

    public boolean isServiceConnected() {
        return this.mServiceConnected;
    }

    /* access modifiers changed from: private */
    public void connectService() {
        if (!isServiceConnected() && !this.mIsConnectInProgress) {
            this.mIsConnectInProgress = true;
            Log.d(TAG, "Connect to ExtTelephony bound service...");
            this.mExtTelephonyManager.connectService(this.mServiceCallback);
        }
    }

    public FiveGServiceState getCurrentServiceState(int i) {
        return getServiceState(i, this.mCurrentServiceStates);
    }

    private FiveGServiceState getLastServiceState(int i) {
        return getServiceState(i, this.mLastServiceStates);
    }

    private static FiveGServiceState getServiceState(int i, SparseArray<FiveGServiceState> sparseArray) {
        FiveGServiceState fiveGServiceState = sparseArray.get(i);
        if (fiveGServiceState != null) {
            return fiveGServiceState;
        }
        FiveGServiceState fiveGServiceState2 = new FiveGServiceState();
        sparseArray.put(i, fiveGServiceState2);
        return fiveGServiceState2;
    }

    /* access modifiers changed from: private */
    public void notifyListenersIfNecessary(int i) {
        FiveGServiceState currentServiceState = getCurrentServiceState(i);
        FiveGServiceState lastServiceState = getLastServiceState(i);
        if (!currentServiceState.equals(lastServiceState)) {
            if (DEBUG) {
                Log.d(TAG, "phoneId(" + i + ") Change in state from " + lastServiceState + " \n\tto " + currentServiceState);
            }
            lastServiceState.copyFrom(currentServiceState);
            IFiveGStateListener iFiveGStateListener = this.mStatesListeners.get(i);
            if (iFiveGStateListener != null) {
                iFiveGStateListener.onStateChanged(currentServiceState);
            }
            this.mHandler.sendEmptyMessage(MESSAGE_NOTIFIY_MONITOR_CALLBACK);
        }
    }

    /* access modifiers changed from: private */
    public void initFiveGServiceState() {
        Log.d(TAG, "initFiveGServiceState size=" + this.mStatesListeners.size());
        for (int i = 0; i < this.mStatesListeners.size(); i++) {
            initFiveGServiceState(this.mStatesListeners.keyAt(i));
        }
    }

    private void initFiveGServiceState(int i) {
        Log.d(TAG, "mServiceConnected=" + this.mServiceConnected + " mClient=" + this.mClient);
        if (this.mServiceConnected && this.mClient != null) {
            Log.d(TAG, "query 5G service state for phoneId " + i);
            try {
                Log.d(TAG, "queryNrIconType result:" + this.mExtTelephonyManager.queryNrIconType(i, this.mClient));
            } catch (Exception e) {
                Log.d(TAG, "initFiveGServiceState: Exception = " + e);
                if (this.mInitRetryTimes < 4 && !this.mHandler.hasMessages(1025)) {
                    this.mHandler.sendEmptyMessageDelayed(1025, (long) ((this.mInitRetryTimes * 2000) + 3000));
                    this.mInitRetryTimes++;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void update5GIcon(FiveGServiceState fiveGServiceState, int i) {
        SignalIcon.MobileIconGroup unused = fiveGServiceState.mIconGroup = getNrIconGroup(fiveGServiceState.mNrIconType, i);
    }

    private SignalIcon.MobileIconGroup getNrIconGroup(int i, int i2) {
        SignalIcon.MobileIconGroup mobileIconGroup = TelephonyIcons.UNKNOWN;
        if (i != 1) {
            if (i == 2) {
                return TelephonyIcons.FIVE_G_UWB;
            }
            if (i != 3) {
                return mobileIconGroup;
            }
        }
        return TelephonyIcons.FIVE_G_BASIC;
    }

    /* access modifiers changed from: private */
    public void notifyMonitorCallback() {
        for (int i = 0; i < this.mKeyguardUpdateMonitorCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mKeyguardUpdateMonitorCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onRefreshCarrierInfo();
            }
        }
    }
}
