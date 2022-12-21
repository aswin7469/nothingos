package com.android.settingslib.mobile;

import android.os.Handler;
import android.os.Looper;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Objects;
import tech.nothing.settingslib.mobile.SignalSmooth;
import tech.nothing.settingslib.mobile.SignalSmoothImpl;

public class MobileStatusTracker {
    private static final String TAG = "MobileStatusTracker";
    /* access modifiers changed from: private */
    public final Callback mCallback;
    private final SubscriptionDefaults mDefaults;
    private final SignalSmoothImpl.OnDelayTimeoutListener mDelayTimeoutListener = new DelayTimeoutListener();
    /* access modifiers changed from: private */
    public final MobileStatus mMobileStatus;
    private final TelephonyManager mPhone;
    private final Handler mReceiverHandler;
    /* access modifiers changed from: private */
    public SignalSmooth mSignalSmooth = SignalSmoothImpl.EMPTY_IMPL;
    private final SubscriptionInfo mSubscriptionInfo;
    private final MobileTelephonyCallback mTelephonyCallback;

    public interface Callback {
        void onMobileStatusChanged(boolean z, MobileStatus mobileStatus);
    }

    private class DelayTimeoutListener implements SignalSmoothImpl.OnDelayTimeoutListener {
        private DelayTimeoutListener() {
        }

        public void onDelayTimeout(ServiceState serviceState) {
            Object obj;
            if (Log.isLoggable(MobileStatusTracker.TAG, 3)) {
                StringBuilder sb = new StringBuilder("onDelayTimeout voiceState=");
                Object obj2 = "";
                if (serviceState == null) {
                    obj = obj2;
                } else {
                    obj = Integer.valueOf(serviceState.getState());
                }
                StringBuilder append = sb.append(obj).append(" dataState=");
                if (serviceState != null) {
                    obj2 = Integer.valueOf(serviceState.getDataRegistrationState());
                }
                Log.d(MobileStatusTracker.TAG, append.append(obj2).toString());
            }
            MobileStatusTracker.this.mMobileStatus.serviceState = serviceState;
            MobileStatusTracker.this.mCallback.onMobileStatusChanged(true, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
        }

        public void onSmoothSignal(SignalStrength signalStrength) {
            String str;
            if (Log.isLoggable(MobileStatusTracker.TAG, 3)) {
                StringBuilder append = new StringBuilder("onSmoothSignal signalStrength=").append((Object) signalStrength);
                if (signalStrength == null) {
                    str = "";
                } else {
                    str = " level=" + signalStrength.getLevel();
                }
                Log.d(MobileStatusTracker.TAG, append.append(str).toString());
            }
            MobileStatusTracker.this.mMobileStatus.signalStrength = signalStrength;
            MobileStatusTracker.this.mCallback.onMobileStatusChanged(true, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
        }
    }

    public MobileStatusTracker(TelephonyManager telephonyManager, Looper looper, SubscriptionInfo subscriptionInfo, SubscriptionDefaults subscriptionDefaults, Callback callback) {
        this.mPhone = telephonyManager;
        Handler handler = new Handler(looper);
        this.mReceiverHandler = handler;
        this.mTelephonyCallback = new MobileTelephonyCallback();
        this.mSubscriptionInfo = subscriptionInfo;
        this.mDefaults = subscriptionDefaults;
        this.mCallback = callback;
        this.mMobileStatus = new MobileStatus();
        updateDataSim();
        handler.post(new MobileStatusTracker$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-settingslib-mobile-MobileStatusTracker  reason: not valid java name */
    public /* synthetic */ void m2505lambda$new$0$comandroidsettingslibmobileMobileStatusTracker() {
        this.mCallback.onMobileStatusChanged(false, new MobileStatus(this.mMobileStatus));
    }

    public MobileTelephonyCallback getTelephonyCallback() {
        return this.mTelephonyCallback;
    }

    public void setListening(boolean z) {
        if (z) {
            TelephonyManager telephonyManager = this.mPhone;
            Handler handler = this.mReceiverHandler;
            Objects.requireNonNull(handler);
            telephonyManager.registerTelephonyCallback(new MobileStatusTracker$$ExternalSyntheticLambda0(handler), this.mTelephonyCallback);
            return;
        }
        this.mPhone.unregisterTelephonyCallback(this.mTelephonyCallback);
    }

    public void setSignalSmooth(boolean z) {
        Log.d(TAG, "setSignalSmooth enabled: " + z);
        if (!z) {
            this.mSignalSmooth.setListening(false);
        } else if (this.mSignalSmooth == SignalSmoothImpl.EMPTY_IMPL) {
            this.mSignalSmooth = new SignalSmoothImpl(this.mReceiverHandler.getLooper(), this.mSubscriptionInfo.getSubscriptionId(), this.mDelayTimeoutListener);
        } else {
            Log.d(TAG, "SignalSmoothImpl already");
            this.mSignalSmooth.setListening(true);
        }
    }

    /* access modifiers changed from: private */
    public void updateDataSim() {
        int activeDataSubId = this.mDefaults.getActiveDataSubId();
        boolean z = true;
        if (SubscriptionManager.isValidSubscriptionId(activeDataSubId)) {
            MobileStatus mobileStatus = this.mMobileStatus;
            if (activeDataSubId != this.mSubscriptionInfo.getSubscriptionId()) {
                z = false;
            }
            mobileStatus.dataSim = z;
            return;
        }
        this.mMobileStatus.dataSim = true;
    }

    /* access modifiers changed from: private */
    public void setActivity(int i) {
        boolean z = false;
        this.mMobileStatus.activityIn = i == 3 || i == 1;
        MobileStatus mobileStatus = this.mMobileStatus;
        if (i == 3 || i == 2) {
            z = true;
        }
        mobileStatus.activityOut = z;
    }

    public class MobileTelephonyCallback extends TelephonyCallback implements TelephonyCallback.ServiceStateListener, TelephonyCallback.SignalStrengthsListener, TelephonyCallback.DataConnectionStateListener, TelephonyCallback.DataActivityListener, TelephonyCallback.CarrierNetworkListener, TelephonyCallback.ActiveDataSubscriptionIdListener, TelephonyCallback.DisplayInfoListener {
        public MobileTelephonyCallback() {
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            String str;
            if (Log.isLoggable(MobileStatusTracker.TAG, 3)) {
                StringBuilder append = new StringBuilder("onSignalStrengthsChanged signalStrength=").append((Object) signalStrength);
                if (signalStrength == null) {
                    str = "";
                } else {
                    str = " level=" + signalStrength.getLevel();
                }
                Log.d(MobileStatusTracker.TAG, append.append(str).toString());
            }
            if (!MobileStatusTracker.this.mSignalSmooth.smoothSignal(signalStrength)) {
                MobileStatusTracker.this.mMobileStatus.signalStrength = signalStrength;
                MobileStatusTracker.this.mCallback.onMobileStatusChanged(true, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
            }
        }

        public void onServiceStateChanged(ServiceState serviceState) {
            Object obj;
            if (Log.isLoggable(MobileStatusTracker.TAG, 3)) {
                StringBuilder sb = new StringBuilder("onServiceStateChanged voiceState=");
                Object obj2 = "";
                if (serviceState == null) {
                    obj = obj2;
                } else {
                    obj = Integer.valueOf(serviceState.getState());
                }
                StringBuilder append = sb.append(obj).append(" dataState=");
                if (serviceState != null) {
                    obj2 = Integer.valueOf(serviceState.getDataRegistrationState());
                }
                Log.d(MobileStatusTracker.TAG, append.append(obj2).toString());
            }
            if (!MobileStatusTracker.this.mSignalSmooth.delayNotifyOos(serviceState)) {
                MobileStatusTracker.this.mMobileStatus.serviceState = serviceState;
                MobileStatusTracker.this.mCallback.onMobileStatusChanged(true, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
            }
        }

        public void onDataConnectionStateChanged(int i, int i2) {
            if (Log.isLoggable(MobileStatusTracker.TAG, 3)) {
                Log.d(MobileStatusTracker.TAG, "onDataConnectionStateChanged: state=" + i + " type=" + i2);
            }
            MobileStatusTracker.this.mMobileStatus.dataState = i;
            MobileStatusTracker.this.mCallback.onMobileStatusChanged(true, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
        }

        public void onDataActivity(int i) {
            if (Log.isLoggable(MobileStatusTracker.TAG, 3)) {
                Log.d(MobileStatusTracker.TAG, "onDataActivity: direction=" + i);
            }
            MobileStatusTracker.this.setActivity(i);
            MobileStatusTracker.this.mCallback.onMobileStatusChanged(false, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
        }

        public void onCarrierNetworkChange(boolean z) {
            if (Log.isLoggable(MobileStatusTracker.TAG, 3)) {
                Log.d(MobileStatusTracker.TAG, "onCarrierNetworkChange: active=" + z);
            }
            MobileStatusTracker.this.mMobileStatus.carrierNetworkChangeMode = z;
            MobileStatusTracker.this.mCallback.onMobileStatusChanged(true, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
        }

        public void onActiveDataSubscriptionIdChanged(int i) {
            if (Log.isLoggable(MobileStatusTracker.TAG, 3)) {
                Log.d(MobileStatusTracker.TAG, "onActiveDataSubscriptionIdChanged: subId=" + i);
            }
            MobileStatusTracker.this.updateDataSim();
            MobileStatusTracker.this.mCallback.onMobileStatusChanged(true, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
        }

        public void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
            if (Log.isLoggable(MobileStatusTracker.TAG, 3)) {
                Log.d(MobileStatusTracker.TAG, "onDisplayInfoChanged: telephonyDisplayInfo=" + telephonyDisplayInfo);
            }
            MobileStatusTracker.this.mMobileStatus.telephonyDisplayInfo = telephonyDisplayInfo;
            MobileStatusTracker.this.mCallback.onMobileStatusChanged(true, new MobileStatus(MobileStatusTracker.this.mMobileStatus));
        }
    }

    public static class SubscriptionDefaults {
        public int getDefaultVoiceSubId() {
            return SubscriptionManager.getDefaultVoiceSubscriptionId();
        }

        public int getDefaultDataSubId() {
            return SubscriptionManager.getDefaultDataSubscriptionId();
        }

        public int getActiveDataSubId() {
            return SubscriptionManager.getActiveDataSubscriptionId();
        }
    }

    public static class MobileStatus {
        public boolean activityIn;
        public boolean activityOut;
        public boolean carrierNetworkChangeMode;
        public boolean dataSim;
        public int dataState = 0;
        public ServiceState serviceState;
        public SignalStrength signalStrength;
        public TelephonyDisplayInfo telephonyDisplayInfo = new TelephonyDisplayInfo(0, 0);

        public MobileStatus() {
        }

        public MobileStatus(MobileStatus mobileStatus) {
            copyFrom(mobileStatus);
        }

        /* access modifiers changed from: protected */
        public void copyFrom(MobileStatus mobileStatus) {
            this.activityIn = mobileStatus.activityIn;
            this.activityOut = mobileStatus.activityOut;
            this.dataSim = mobileStatus.dataSim;
            this.carrierNetworkChangeMode = mobileStatus.carrierNetworkChangeMode;
            this.dataState = mobileStatus.dataState;
            this.serviceState = mobileStatus.serviceState;
            this.signalStrength = mobileStatus.signalStrength;
            this.telephonyDisplayInfo = mobileStatus.telephonyDisplayInfo;
        }

        public String toString() {
            String str;
            Object obj;
            StringBuilder append = new StringBuilder("[activityIn=").append(this.activityIn).append(",activityOut=").append(this.activityOut).append(",dataSim=").append(this.dataSim).append(",carrierNetworkChangeMode=").append(this.carrierNetworkChangeMode).append(",dataState=").append(this.dataState).append(",serviceState=");
            String str2 = "";
            if (this.serviceState == null) {
                str = str2;
            } else {
                str = "mVoiceRegState=" + this.serviceState.getState() + NavigationBarInflaterView.KEY_CODE_START + ServiceState.rilServiceStateToString(this.serviceState.getState()) + "), mDataRegState=" + this.serviceState.getDataRegState() + NavigationBarInflaterView.KEY_CODE_START + ServiceState.rilServiceStateToString(this.serviceState.getDataRegState()) + NavigationBarInflaterView.KEY_CODE_END;
            }
            StringBuilder append2 = append.append(str).append(",signalStrength=");
            SignalStrength signalStrength2 = this.signalStrength;
            if (signalStrength2 == null) {
                obj = str2;
            } else {
                obj = Integer.valueOf(signalStrength2.getLevel());
            }
            StringBuilder append3 = append2.append(obj).append(",telephonyDisplayInfo=");
            TelephonyDisplayInfo telephonyDisplayInfo2 = this.telephonyDisplayInfo;
            if (telephonyDisplayInfo2 != null) {
                str2 = telephonyDisplayInfo2.toString();
            }
            return append3.append(str2).append(']').toString();
        }
    }

    public int getSmoothLevel(SignalStrength signalStrength) {
        return this.mSignalSmooth.getSmoothLevel(signalStrength);
    }
}
