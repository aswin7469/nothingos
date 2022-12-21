package com.android.keyguard;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.app.UserSwitchObserver;
import android.app.admin.DevicePolicyManager;
import android.app.trust.TrustManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.hardware.SensorPrivacyManager;
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.biometrics.CryptoObject;
import android.hardware.biometrics.IBiometricEnabledOnKeyguardCallback;
import android.hardware.face.FaceManager;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IRemoteCallback;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.Trace;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.service.dreams.IDreamManager;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.ActiveUnlockConfig;
import com.android.settingslib.WirelessUtils;
import com.android.settingslib.fuelgauge.BatteryStatus;
import com.android.settingslib.media.MediaOutputConstants;
import com.android.systemui.C1893R;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.Assert;
import com.google.android.collect.Lists;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.facerecognition.FaceRecognitionController;
import com.nothing.systemui.keyguard.KeyguardUpdateMonitorEx;
import com.nothing.systemui.util.NTLogUtil;
import com.nothing.systemui.util.SystemUIEventUtils;
import com.nothing.utils.NTVibrateUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import javax.inject.Inject;

@SysUISingleton
public class KeyguardUpdateMonitor implements TrustManager.TrustListener, Dumpable {
    private static final String ACTION_FACE_UNLOCK_STARTED = "com.android.facelock.FACE_UNLOCK_STARTED";
    private static final String ACTION_FACE_UNLOCK_STOPPED = "com.android.facelock.FACE_UNLOCK_STOPPED";
    private static final int BIOMETRIC_ACTION_START = 0;
    private static final int BIOMETRIC_ACTION_STOP = 1;
    private static final int BIOMETRIC_ACTION_UPDATE = 2;
    public static final int BIOMETRIC_HELP_FACE_NOT_RECOGNIZED = -2;
    private static final int BIOMETRIC_HELP_FINGERPRINT_NOT_RECOGNIZED = -1;
    private static final int BIOMETRIC_LOCKOUT_RESET_DELAY_MS = 600;
    private static final int BIOMETRIC_STATE_CANCELLING = 2;
    private static final int BIOMETRIC_STATE_CANCELLING_RESTARTING = 3;
    private static final int BIOMETRIC_STATE_RUNNING = 1;
    private static final int BIOMETRIC_STATE_STOPPED = 0;
    public static final boolean CORE_APPS_ONLY;
    /* access modifiers changed from: private */
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    private static final boolean DEBUG_ACTIVE_UNLOCK = Build.IS_DEBUGGABLE;
    private static final boolean DEBUG_FACE = Build.IS_DEBUGGABLE;
    private static final boolean DEBUG_FINGERPRINT = Build.IS_DEBUGGABLE;
    private static final boolean DEBUG_SIM_STATES = true;
    private static final boolean DEBUG_SPEW = false;
    private static final int DEFAULT_CANCEL_SIGNAL_TIMEOUT = 3000;
    private static final ComponentName FALLBACK_HOME_COMPONENT = new ComponentName(MediaOutputConstants.SETTINGS_PACKAGE_NAME, "com.android.settings.FallbackHome");
    private static final int FINGERPRINT_CONTINUE_DELAY_MS = 500;
    private static final int HAL_ERROR_RETRY_MAX = 20;
    private static final int HAL_ERROR_RETRY_TIMEOUT = 500;
    private static final int MSG_AIRPLANE_MODE_CHANGED = 329;
    private static final int MSG_ASSISTANT_STACK_CHANGED = 335;
    private static final int MSG_BATTERY_UPDATE = 302;
    private static final int MSG_BIOMETRIC_AUTHENTICATION_CONTINUE = 336;
    private static final int MSG_DEVICE_POLICY_MANAGER_STATE_CHANGED = 337;
    private static final int MSG_DEVICE_PROVISIONED = 308;
    private static final int MSG_DPM_STATE_CHANGED = 309;
    private static final int MSG_DREAMING_STATE_CHANGED = 333;
    private static final int MSG_FACE_UNLOCK_STATE_CHANGED = 327;
    private static final int MSG_FINISHED_GOING_TO_SLEEP = 320;
    private static final int MSG_KEYGUARD_BOUNCER_CHANGED = 322;
    private static final int MSG_KEYGUARD_DISMISS_ANIMATION_FINISHED = 346;
    private static final int MSG_KEYGUARD_GOING_AWAY = 342;
    private static final int MSG_KEYGUARD_RESET = 312;
    private static final int MSG_PHONE_STATE_CHANGED = 306;
    private static final int MSG_REPORT_EMERGENCY_CALL_ACTION = 318;
    private static final int MSG_REQUIRE_NFC_UNLOCK = 345;
    private static final int MSG_RESET_FACE_RECOGNITION_STATE = 347;
    private static final int MSG_SCREEN_TURNED_OFF = 332;
    private static final int MSG_SERVICE_STATE_CHANGE = 330;
    private static final int MSG_SIM_STATE_CHANGE = 304;
    private static final int MSG_SIM_SUBSCRIPTION_INFO_CHANGED = 328;
    private static final int MSG_STARTED_GOING_TO_SLEEP = 321;
    private static final int MSG_STARTED_WAKING_UP = 319;
    private static final int MSG_TELEPHONY_CAPABLE = 338;
    private static final int MSG_TIMEZONE_UPDATE = 339;
    private static final int MSG_TIME_FORMAT_UPDATE = 344;
    private static final int MSG_TIME_UPDATE = 301;
    private static final int MSG_USER_INFO_CHANGED = 317;
    private static final int MSG_USER_REMOVED = 341;
    private static final int MSG_USER_STOPPED = 340;
    private static final int MSG_USER_SWITCHING = 310;
    private static final int MSG_USER_SWITCH_COMPLETE = 314;
    private static final int MSG_USER_UNLOCKED = 334;
    private static final String TAG = "KeyguardUpdateMonitor";
    private static int sCurrentUser;
    /* access modifiers changed from: private */
    public int mActiveMobileDataSubscription;
    /* access modifiers changed from: private */
    public final ActiveUnlockConfig mActiveUnlockConfig;
    private boolean mAssistantVisible;
    private final AuthController mAuthController;
    private boolean mAuthInterruptActive;
    private final Executor mBackgroundExecutor;
    BatteryStatus mBatteryStatus;
    private IBiometricEnabledOnKeyguardCallback mBiometricEnabledCallback;
    /* access modifiers changed from: private */
    public SparseBooleanArray mBiometricEnabledForUser;
    private BiometricManager mBiometricManager;
    /* access modifiers changed from: private */
    public boolean mBouncerFullyShown;
    private boolean mBouncerIsOrWillBeShowing;
    protected final BroadcastReceiver mBroadcastAllReceiver;
    private final BroadcastDispatcher mBroadcastDispatcher;
    protected final BroadcastReceiver mBroadcastReceiver;
    /* access modifiers changed from: private */
    public final ArrayList<WeakReference<KeyguardUpdateMonitorCallback>> mCallbacks;
    /* access modifiers changed from: private */
    public final Context mContext;
    private boolean mCredentialAttempted;
    private boolean mDeviceInteractive;
    private final DevicePolicyManager mDevicePolicyManager;
    /* access modifiers changed from: private */
    public boolean mDeviceProvisioned;
    private ContentObserver mDeviceProvisionedObserver;
    private DisplayClientState mDisplayClientState;
    private final IDreamManager mDreamManager;
    final FaceManager.AuthenticationCallback mFaceAuthenticationCallback;
    private final Runnable mFaceCancelNotReceived;
    CancellationSignal mFaceCancelSignal;
    private final FaceManager.FaceDetectionCallback mFaceDetectionCallback;
    private boolean mFaceLockedOutPermanent;
    private final FaceManager.LockoutResetCallback mFaceLockoutResetCallback;
    private FaceManager mFaceManager;
    private WeakReference<FaceRecognitionCallback> mFaceRecognitionCallbackRef;
    /* access modifiers changed from: private */
    public boolean mFaceRecognitionSucceeded = false;
    private int mFaceRunningState;
    private List<FaceSensorPropertiesInternal> mFaceSensorProperties;
    final FingerprintManager.AuthenticationCallback mFingerprintAuthenticationCallback;
    CancellationSignal mFingerprintCancelSignal;
    private final FingerprintManager.FingerprintDetectionCallback mFingerprintDetectionCallback;
    private boolean mFingerprintLockedOut;
    private boolean mFingerprintLockedOutPermanent;
    private final FingerprintManager.LockoutResetCallback mFingerprintLockoutResetCallback;
    private int mFingerprintRunningState;
    private List<FingerprintSensorPropertiesInternal> mFingerprintSensorProperties;
    private final Runnable mFpCancelNotReceived;
    /* access modifiers changed from: private */
    public FingerprintManager mFpm;
    private boolean mGoingToSleep;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    /* access modifiers changed from: private */
    public int mHardwareFaceUnavailableRetryCount;
    /* access modifiers changed from: private */
    public int mHardwareFingerprintUnavailableRetryCount;
    private final InteractionJankMonitor mInteractionJankMonitor;
    private final boolean mIsAutomotive;
    private boolean mIsDreaming;
    private boolean mIsFaceAuthUserRequested;
    private boolean mIsFaceCameraStarting = false;
    private boolean mIsFaceEnrolled;
    private final boolean mIsPrimaryUser;
    private HashMap<Integer, Boolean> mIsUnlockWithFingerprintPossible;
    /* access modifiers changed from: private */
    public KeyguardBypassController mKeyguardBypassController;
    private boolean mKeyguardGoingAway;
    private boolean mKeyguardIsVisible;
    private boolean mKeyguardOccluded;
    private final LatencyTracker mLatencyTracker;
    private final KeyguardListenQueue mListenModels;
    private boolean mLockIconPressed;
    /* access modifiers changed from: private */
    public LockPatternUtils mLockPatternUtils;
    private boolean mLogoutEnabled;
    private boolean mLowPowerState = false;
    private boolean mNeedsSlowUnlockTransition;
    private boolean mOccludingAppRequestingFace;
    private boolean mOccludingAppRequestingFp;
    private boolean mPanelDown = false;
    private int mPhoneState;
    public TelephonyCallback.ActiveDataSubscriptionIdListener mPhoneStateListener;
    private Runnable mRetryFaceAuthentication;
    /* access modifiers changed from: private */
    public Runnable mRetryFingerprintAuthentication;
    private int mRingMode;
    private Map<Integer, Intent> mSecondaryLockscreenRequirement;
    private boolean mSecureCameraLaunched;
    private SensorPrivacyManager mSensorPrivacyManager;
    HashMap<Integer, ServiceState> mServiceStates;
    private boolean mShowing;
    HashMap<Integer, SimData> mSimDatas;
    /* access modifiers changed from: private */
    public int mStatusBarState;
    private final StatusBarStateController mStatusBarStateController;
    private final StatusBarStateController.StateListener mStatusBarStateControllerListener;
    private StrongAuthTracker mStrongAuthTracker;
    private List<SubscriptionInfo> mSubscriptionInfo;
    private SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionListener;
    private SubscriptionManager mSubscriptionManager;
    private boolean mSwitchingUser;
    private final TaskStackChangeListener mTaskStackListener;
    protected boolean mTelephonyCapable;
    private final TelephonyListenerManager mTelephonyListenerManager;
    private TelephonyManager mTelephonyManager;
    private ContentObserver mTimeFormatChangeObserver;
    private TrustManager mTrustManager;
    /* access modifiers changed from: private */
    public boolean mUdfpsBouncerShowing;
    SparseArray<BiometricAuthenticated> mUserFaceAuthenticated;
    private SparseBooleanArray mUserFaceUnlockRunning;
    SparseArray<BiometricAuthenticated> mUserFingerprintAuthenticated;
    private SparseBooleanArray mUserHasTrust;
    private SparseBooleanArray mUserIsUnlocked;
    private UserManager mUserManager;
    private final UserSwitchObserver mUserSwitchObserver;
    private SparseBooleanArray mUserTrustIsManaged;
    private SparseBooleanArray mUserTrustIsUsuallyManaged;

    public interface FaceRecognitionCallback {
        boolean isCameraPreviewStarting();

        void pauseCameraPreview(boolean z);

        void postToStartCamera();

        void postToStopCamera();
    }

    private boolean containsFlag(int i, int i2) {
        return (i & i2) != 0;
    }

    public static boolean isSimPinSecure(int i) {
        return i == 2 || i == 3 || i == 7;
    }

    /* access modifiers changed from: protected */
    public int getBiometricLockoutDelay() {
        return 600;
    }

    static /* synthetic */ int access$708(KeyguardUpdateMonitor keyguardUpdateMonitor) {
        int i = keyguardUpdateMonitor.mHardwareFingerprintUnavailableRetryCount;
        keyguardUpdateMonitor.mHardwareFingerprintUnavailableRetryCount = i + 1;
        return i;
    }

    static {
        try {
            CORE_APPS_ONLY = IPackageManager.Stub.asInterface(ServiceManager.getService("package")).isOnlyCoreApps();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-keyguard-KeyguardUpdateMonitor  reason: not valid java name */
    public /* synthetic */ void m2292lambda$new$0$comandroidkeyguardKeyguardUpdateMonitor() {
        Log.e(TAG, "Fp cancellation not received, transitioning to STOPPED");
        this.mFingerprintRunningState = 0;
        updateFingerprintListeningState(1);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-keyguard-KeyguardUpdateMonitor  reason: not valid java name */
    public /* synthetic */ void m2293lambda$new$1$comandroidkeyguardKeyguardUpdateMonitor() {
        Log.e(TAG, "Face cancellation not received, transitioning to STOPPED");
        this.mFaceRunningState = 0;
        updateFaceListeningState(1);
    }

    static class BiometricAuthenticated {
        /* access modifiers changed from: private */
        public final boolean mAuthenticated;
        /* access modifiers changed from: private */
        public final boolean mIsStrongBiometric;

        BiometricAuthenticated(boolean z, boolean z2) {
            this.mAuthenticated = z;
            this.mIsStrongBiometric = z2;
        }
    }

    public static synchronized void setCurrentUser(int i) {
        synchronized (KeyguardUpdateMonitor.class) {
            sCurrentUser = i;
        }
    }

    public static synchronized int getCurrentUser() {
        int i;
        synchronized (KeyguardUpdateMonitor.class) {
            i = sCurrentUser;
        }
        return i;
    }

    public void onTrustChanged(boolean z, int i, int i2, List<String> list) {
        String str;
        Assert.isMainThread();
        boolean z2 = this.mUserHasTrust.get(i, false);
        this.mUserHasTrust.put(i, z);
        if (z2 == z || z) {
            updateBiometricListeningState(1);
        } else {
            updateBiometricListeningState(0);
        }
        for (int i3 = 0; i3 < this.mCallbacks.size(); i3++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i3).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTrustChanged(i);
                if (z && i2 != 0) {
                    keyguardUpdateMonitorCallback.onTrustGrantedWithFlags(i2, i);
                }
            }
        }
        if (getCurrentUser() == i) {
            if (getUserHasTrust(i) && list != null) {
                Iterator<String> it = list.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    str = it.next();
                    if (!TextUtils.isEmpty(str)) {
                        break;
                    }
                }
            }
            str = null;
            for (int i4 = 0; i4 < this.mCallbacks.size(); i4++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback2 = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i4).get();
                if (keyguardUpdateMonitorCallback2 != null) {
                    keyguardUpdateMonitorCallback2.showTrustGrantedMessage(str);
                }
            }
        }
    }

    public void onTrustError(CharSequence charSequence) {
        dispatchErrorMessage(charSequence);
    }

    /* access modifiers changed from: private */
    public void handleSimSubscriptionInfoChanged() {
        Assert.isMainThread();
        Log.v(TAG, "onSubscriptionInfoChanged()");
        List<SubscriptionInfo> completeActiveSubscriptionInfoList = this.mSubscriptionManager.getCompleteActiveSubscriptionInfoList();
        if (completeActiveSubscriptionInfoList != null) {
            for (SubscriptionInfo subscriptionInfo : completeActiveSubscriptionInfoList) {
                Log.v(TAG, "SubInfo:" + subscriptionInfo);
            }
        } else {
            Log.v(TAG, "onSubscriptionInfoChanged: list is null");
        }
        List<SubscriptionInfo> subscriptionInfo2 = getSubscriptionInfo(true);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < subscriptionInfo2.size(); i++) {
            SubscriptionInfo subscriptionInfo3 = subscriptionInfo2.get(i);
            if (refreshSimState(subscriptionInfo3.getSubscriptionId(), subscriptionInfo3.getSimSlotIndex())) {
                arrayList.add(subscriptionInfo3);
            }
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            SimData simData = this.mSimDatas.get(Integer.valueOf(((SubscriptionInfo) arrayList.get(i2)).getSimSlotIndex()));
            if (simData != null) {
                for (int i3 = 0; i3 < this.mCallbacks.size(); i3++) {
                    KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i3).get();
                    if (keyguardUpdateMonitorCallback != null) {
                        keyguardUpdateMonitorCallback.onSimStateChanged(simData.subId, simData.slotId, simData.simState);
                    }
                }
            }
        }
        callbacksRefreshCarrierInfo();
    }

    /* access modifiers changed from: private */
    public void handleAirplaneModeChanged() {
        callbacksRefreshCarrierInfo();
    }

    private void callbacksRefreshCarrierInfo() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onRefreshCarrierInfo();
            }
        }
    }

    public List<SubscriptionInfo> getSubscriptionInfo(boolean z) {
        List<SubscriptionInfo> list = this.mSubscriptionInfo;
        if (list == null || z) {
            list = this.mSubscriptionManager.getCompleteActiveSubscriptionInfoList();
        }
        if (list == null) {
            this.mSubscriptionInfo = new ArrayList();
        } else {
            this.mSubscriptionInfo = list;
        }
        return new ArrayList(this.mSubscriptionInfo);
    }

    public List<SubscriptionInfo> getFilteredSubscriptionInfo(boolean z) {
        List<SubscriptionInfo> subscriptionInfo = getSubscriptionInfo(false);
        if (subscriptionInfo.size() == 2) {
            SubscriptionInfo subscriptionInfo2 = subscriptionInfo.get(0);
            SubscriptionInfo subscriptionInfo3 = subscriptionInfo.get(1);
            if (subscriptionInfo2.getGroupUuid() == null || !subscriptionInfo2.getGroupUuid().equals(subscriptionInfo3.getGroupUuid()) || (!subscriptionInfo2.isOpportunistic() && !subscriptionInfo3.isOpportunistic())) {
                return subscriptionInfo;
            }
            if (CarrierConfigManager.getDefaultConfig().getBoolean("always_show_primary_signal_bar_in_opportunistic_network_boolean")) {
                if (!subscriptionInfo2.isOpportunistic()) {
                    subscriptionInfo2 = subscriptionInfo3;
                }
                subscriptionInfo.remove((Object) subscriptionInfo2);
            } else {
                if (subscriptionInfo2.getSubscriptionId() == this.mActiveMobileDataSubscription) {
                    subscriptionInfo2 = subscriptionInfo3;
                }
                subscriptionInfo.remove((Object) subscriptionInfo2);
            }
        }
        return subscriptionInfo;
    }

    public void onTrustManagedChanged(boolean z, int i) {
        Assert.isMainThread();
        this.mUserTrustIsManaged.put(i, z);
        this.mUserTrustIsUsuallyManaged.put(i, this.mTrustManager.isTrustUsuallyManaged(i));
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTrustManagedChanged(i);
            }
        }
    }

    public void setCredentialAttempted() {
        this.mCredentialAttempted = true;
        updateFingerprintListeningState(2);
    }

    public void setKeyguardGoingAway(boolean z) {
        this.mKeyguardGoingAway = z;
        updateBiometricListeningState(1);
    }

    public void setKeyguardOccluded(boolean z) {
        this.mKeyguardOccluded = z;
        updateBiometricListeningState(2);
    }

    public void requestFaceAuthOnOccludingApp(boolean z) {
        this.mOccludingAppRequestingFace = z;
        updateFaceListeningState(2);
    }

    public void requestFingerprintAuthOnOccludingApp(boolean z) {
        this.mOccludingAppRequestingFp = z;
        updateFingerprintListeningState(2);
    }

    public void onCameraLaunched() {
        this.mSecureCameraLaunched = true;
        updateBiometricListeningState(2);
    }

    public boolean isSecureCameraLaunchedOverKeyguard() {
        return this.mSecureCameraLaunched;
    }

    public boolean isDreaming() {
        return this.mIsDreaming;
    }

    public void awakenFromDream() {
        IDreamManager iDreamManager;
        if (this.mIsDreaming && (iDreamManager = this.mDreamManager) != null) {
            try {
                iDreamManager.awaken();
            } catch (RemoteException unused) {
                Log.e(TAG, "Unable to awaken from dream");
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onFingerprintAuthenticated(int i, boolean z) {
        Assert.isMainThread();
        Trace.beginSection("KeyGuardUpdateMonitor#onFingerPrintAuthenticated");
        this.mUserFingerprintAuthenticated.put(i, new BiometricAuthenticated(true, z));
        if (getUserCanSkipBouncer(i)) {
            this.mTrustManager.unlockedByBiometricForUser(i, BiometricSourceType.FINGERPRINT);
        }
        this.mFingerprintCancelSignal = null;
        updateBiometricListeningState(2);
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAuthenticated(i, BiometricSourceType.FINGERPRINT, z);
            }
        }
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(MSG_BIOMETRIC_AUTHENTICATION_CONTINUE), 500);
        this.mAssistantVisible = false;
        reportSuccessfulBiometricUnlock(z, i);
        Trace.endSection();
    }

    private void reportSuccessfulBiometricUnlock(final boolean z, final int i) {
        this.mBackgroundExecutor.execute(new Runnable() {
            public void run() {
                KeyguardUpdateMonitor.this.mLockPatternUtils.reportSuccessfulBiometricUnlock(z, i);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleFingerprintAuthFailed() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAuthFailed(BiometricSourceType.FINGERPRINT);
            }
        }
        if (isUdfpsSupported()) {
            handleFingerprintHelp(-1, this.mContext.getString(17040363));
        } else {
            handleFingerprintHelp(-1, this.mContext.getString(17040349));
        }
    }

    /* access modifiers changed from: private */
    public void handleFingerprintAcquired(int i) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAcquired(BiometricSourceType.FINGERPRINT, i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFingerprintAuthenticated(int i, boolean z) {
        Trace.beginSection("KeyGuardUpdateMonitor#handlerFingerPrintAuthenticated");
        try {
            int i2 = ActivityManager.getService().getCurrentUser().id;
            if (i2 != i) {
                Log.d(TAG, "Fingerprint authenticated for wrong user: " + i);
            } else if (isFingerprintDisabled(i2)) {
                Log.d(TAG, "Fingerprint disabled by DPM for userId: " + i2);
            } else {
                onFingerprintAuthenticated(i2, z);
                setFingerprintRunningState(0);
                Trace.endSection();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to get current user id: ", e);
        } finally {
            setFingerprintRunningState(0);
        }
    }

    /* access modifiers changed from: private */
    public void handleFingerprintHelp(int i, String str) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricHelp(i, str, BiometricSourceType.FINGERPRINT);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFingerprintError(int i, String str) {
        boolean z;
        Assert.isMainThread();
        if (this.mHandler.hasCallbacks(this.mFpCancelNotReceived)) {
            this.mHandler.removeCallbacks(this.mFpCancelNotReceived);
        }
        this.mFingerprintCancelSignal = null;
        if (i == 5 && this.mFingerprintRunningState == 3) {
            setFingerprintRunningState(0);
            updateFingerprintListeningState(2);
        } else {
            setFingerprintRunningState(0);
        }
        if (i == 1) {
            this.mHandler.postDelayed(this.mRetryFingerprintAuthentication, 500);
        }
        if (i == 9) {
            z = (!this.mFingerprintLockedOutPermanent) | false;
            this.mFingerprintLockedOutPermanent = true;
            Log.d(TAG, "Fingerprint locked out - requiring strong auth");
            this.mLockPatternUtils.requireStrongAuth(8, getCurrentUser());
        } else {
            z = false;
        }
        if (i == 7 || i == 9) {
            z |= !this.mFingerprintLockedOut;
            this.mFingerprintLockedOut = true;
            if (isUdfpsEnrolled()) {
                updateFingerprintListeningState(2);
            }
            stopListeningForFace();
        }
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricError(i, str, BiometricSourceType.FINGERPRINT);
            }
        }
        if (z) {
            notifyLockedOutStateChanged(BiometricSourceType.FINGERPRINT);
        }
    }

    /* access modifiers changed from: private */
    public void handleFingerprintLockoutReset(int i) {
        Log.d(TAG, "handleFingerprintLockoutReset: " + i);
        boolean z = this.mFingerprintLockedOut;
        boolean z2 = this.mFingerprintLockedOutPermanent;
        boolean z3 = false;
        boolean z4 = i == 1 || i == 2;
        this.mFingerprintLockedOut = z4;
        boolean z5 = i == 2;
        this.mFingerprintLockedOutPermanent = z5;
        if (!(z4 == z && z5 == z2)) {
            z3 = true;
        }
        if (isUdfpsEnrolled()) {
            this.mHandler.postDelayed(new KeyguardUpdateMonitor$$ExternalSyntheticLambda2(this), (long) getBiometricLockoutDelay());
        } else {
            updateFingerprintListeningState(2);
        }
        if (z3) {
            notifyLockedOutStateChanged(BiometricSourceType.FINGERPRINT);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleFingerprintLockoutReset$2$com-android-keyguard-KeyguardUpdateMonitor */
    public /* synthetic */ void mo26252xc5658f79() {
        updateFingerprintListeningState(2);
    }

    private void setFingerprintRunningState(int i) {
        boolean z = false;
        boolean z2 = this.mFingerprintRunningState == 1;
        if (i == 1) {
            z = true;
        }
        this.mFingerprintRunningState = i;
        Log.d(TAG, "fingerprintRunningState: " + this.mFingerprintRunningState);
        if (z2 != z) {
            notifyFingerprintRunningStateChanged();
        }
    }

    private void notifyFingerprintRunningStateChanged() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricRunningStateChanged(isFingerprintDetectionRunning(), BiometricSourceType.FINGERPRINT);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onFaceAuthenticated(int i, boolean z) {
        Trace.beginSection("KeyGuardUpdateMonitor#onFaceAuthenticated");
        Assert.isMainThread();
        this.mUserFaceAuthenticated.put(i, new BiometricAuthenticated(true, z));
        if (getUserCanSkipBouncer(i)) {
            this.mTrustManager.unlockedByBiometricForUser(i, BiometricSourceType.FACE);
        }
        this.mFaceCancelSignal = null;
        updateBiometricListeningState(2);
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAuthenticated(i, BiometricSourceType.FACE, z);
            }
        }
        this.mAssistantVisible = false;
        reportSuccessfulBiometricUnlock(z, i);
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public void handleFaceAuthFailed() {
        Assert.isMainThread();
        this.mFaceCancelSignal = null;
        setFaceRunningState(0);
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAuthFailed(BiometricSourceType.FACE);
            }
        }
        handleFaceHelp(-2, this.mContext.getString(C1893R.string.kg_face_not_recognized));
    }

    /* access modifiers changed from: private */
    public void handleFaceAcquired(int i) {
        Assert.isMainThread();
        if (DEBUG_FACE) {
            Log.d(TAG, "Face acquired acquireInfo=" + i);
        }
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAcquired(BiometricSourceType.FACE, i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFaceAuthenticated(int i, boolean z) {
        Trace.beginSection("KeyGuardUpdateMonitor#handlerFaceAuthenticated");
        try {
            if (this.mGoingToSleep) {
                Log.d(TAG, "Aborted successful auth because device is going to sleep.");
                return;
            }
            int i2 = ActivityManager.getService().getCurrentUser().id;
            if (i2 != i) {
                Log.d(TAG, "Face authenticated for wrong user: " + i);
            } else if (isFaceDisabled(i2)) {
                Log.d(TAG, "Face authentication disabled by DPM for userId: " + i2);
                setFaceRunningState(0);
            } else {
                if (DEBUG_FACE) {
                    Log.d(TAG, "Face auth succeeded for user " + i2);
                }
                onFaceAuthenticated(i2, z);
                setFaceRunningState(0);
                Trace.endSection();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to get current user id: ", e);
        } finally {
            setFaceRunningState(0);
        }
    }

    /* access modifiers changed from: private */
    public void handleFaceHelp(int i, String str) {
        Assert.isMainThread();
        if (DEBUG_FACE) {
            Log.d(TAG, "Face help received: " + str);
        }
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricHelp(i, str, BiometricSourceType.FACE);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFaceError(int i, String str) {
        boolean z;
        int i2;
        Assert.isMainThread();
        if (DEBUG_FACE) {
            Log.d(TAG, "Face error received: " + str);
        }
        if (this.mHandler.hasCallbacks(this.mFaceCancelNotReceived)) {
            this.mHandler.removeCallbacks(this.mFaceCancelNotReceived);
        }
        this.mFaceCancelSignal = null;
        SensorPrivacyManager sensorPrivacyManager = this.mSensorPrivacyManager;
        boolean isSensorPrivacyEnabled = sensorPrivacyManager != null ? sensorPrivacyManager.isSensorPrivacyEnabled(1, 2) : false;
        if (i == 5 && this.mFaceRunningState == 3) {
            setFaceRunningState(0);
            updateFaceListeningState(2);
        } else {
            setFaceRunningState(0);
        }
        boolean z2 = i == 1;
        if ((z2 || i == 2) && (i2 = this.mHardwareFaceUnavailableRetryCount) < 20) {
            this.mHardwareFaceUnavailableRetryCount = i2 + 1;
            this.mHandler.removeCallbacks(this.mRetryFaceAuthentication);
            this.mHandler.postDelayed(this.mRetryFaceAuthentication, 500);
        }
        if (i == 9) {
            z = !this.mFaceLockedOutPermanent;
            this.mFaceLockedOutPermanent = true;
        } else {
            z = false;
        }
        if (z2 && isSensorPrivacyEnabled) {
            str = this.mContext.getString(C1893R.string.kg_face_sensor_privacy_enabled);
        }
        for (int i3 = 0; i3 < this.mCallbacks.size(); i3++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i3).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricError(i, str, BiometricSourceType.FACE);
            }
        }
        if (z) {
            notifyLockedOutStateChanged(BiometricSourceType.FACE);
        }
    }

    /* access modifiers changed from: private */
    public void handleFaceLockoutReset(int i) {
        Log.d(TAG, "handleFaceLockoutReset: " + i);
        boolean z = this.mFaceLockedOutPermanent;
        boolean z2 = true;
        boolean z3 = i == 2;
        this.mFaceLockedOutPermanent = z3;
        if (z3 == z) {
            z2 = false;
        }
        this.mHandler.postDelayed(new KeyguardUpdateMonitor$$ExternalSyntheticLambda9(this), (long) getBiometricLockoutDelay());
        if (z2) {
            notifyLockedOutStateChanged(BiometricSourceType.FACE);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleFaceLockoutReset$3$com-android-keyguard-KeyguardUpdateMonitor */
    public /* synthetic */ void mo26251x62e5c695() {
        updateFaceListeningState(2);
    }

    private void setFaceRunningState(int i) {
        boolean z = false;
        boolean z2 = this.mFaceRunningState == 1;
        if (i == 1) {
            z = true;
        }
        this.mFaceRunningState = i;
        Log.d(TAG, "faceRunningState: " + this.mFaceRunningState);
        if (z2 != z) {
            notifyFaceRunningStateChanged();
        }
    }

    private void notifyFaceRunningStateChanged() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricRunningStateChanged(isFaceDetectionRunning(), BiometricSourceType.FACE);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleFaceUnlockStateChanged(boolean z, int i) {
        Assert.isMainThread();
        this.mUserFaceUnlockRunning.put(i, z);
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onFaceUnlockStateChanged(z, i);
            }
        }
    }

    public boolean isFaceUnlockRunning(int i) {
        return this.mUserFaceUnlockRunning.get(i);
    }

    public boolean isFingerprintDetectionRunning() {
        return this.mFingerprintRunningState == 1;
    }

    public boolean isFaceDetectionRunning() {
        return this.mFaceRunningState == 1;
    }

    private boolean isTrustDisabled(int i) {
        return isSimPinSecure();
    }

    private boolean isFingerprintDisabled(int i) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
        return !(devicePolicyManager == null || (devicePolicyManager.getKeyguardDisabledFeatures((ComponentName) null, i) & 32) == 0) || isSimPinSecure();
    }

    private boolean isFaceDisabled(int i) {
        return ((Boolean) DejankUtils.whitelistIpcs(new KeyguardUpdateMonitor$$ExternalSyntheticLambda11(this, (DevicePolicyManager) this.mContext.getSystemService("device_policy"), i))).booleanValue();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$isFaceDisabled$4$com-android-keyguard-KeyguardUpdateMonitor */
    public /* synthetic */ Boolean mo26253xccc6a402(DevicePolicyManager devicePolicyManager, int i) {
        return Boolean.valueOf(!(devicePolicyManager == null || (devicePolicyManager.getKeyguardDisabledFeatures((ComponentName) null, i) & 128) == 0) || isSimPinSecure());
    }

    public boolean getIsFaceAuthenticated() {
        BiometricAuthenticated biometricAuthenticated = this.mUserFaceAuthenticated.get(getCurrentUser());
        if (biometricAuthenticated != null) {
            return biometricAuthenticated.mAuthenticated;
        }
        return false;
    }

    public boolean getUserCanSkipBouncer(int i) {
        return getUserHasTrust(i) || getUserUnlockedWithBiometric(i);
    }

    public boolean getUserHasTrust(int i) {
        return !isTrustDisabled(i) && this.mUserHasTrust.get(i);
    }

    public boolean getUserUnlockedWithBiometric(int i) {
        BiometricAuthenticated biometricAuthenticated = this.mUserFingerprintAuthenticated.get(i);
        BiometricAuthenticated biometricAuthenticated2 = this.mUserFaceAuthenticated.get(i);
        boolean z = biometricAuthenticated != null && biometricAuthenticated.mAuthenticated && isUnlockingWithBiometricAllowed(biometricAuthenticated.mIsStrongBiometric);
        boolean z2 = biometricAuthenticated2 != null && biometricAuthenticated2.mAuthenticated && isUnlockingWithBiometricAllowed(biometricAuthenticated2.mIsStrongBiometric);
        boolean isFaceRecognitionSucceeded = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFaceRecognitionSucceeded();
        if (z || z2 || isFaceRecognitionSucceeded) {
            return true;
        }
        return false;
    }

    public boolean getUserUnlockedWithBiometricAndIsBypassing(int i) {
        BiometricAuthenticated biometricAuthenticated = this.mUserFingerprintAuthenticated.get(i);
        BiometricAuthenticated biometricAuthenticated2 = this.mUserFaceAuthenticated.get(i);
        boolean z = biometricAuthenticated != null && biometricAuthenticated.mAuthenticated && isUnlockingWithBiometricAllowed(biometricAuthenticated.mIsStrongBiometric);
        boolean z2 = biometricAuthenticated2 != null && biometricAuthenticated2.mAuthenticated && isUnlockingWithBiometricAllowed(biometricAuthenticated2.mIsStrongBiometric);
        boolean isFaceRecognitionSucceeded = ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFaceRecognitionSucceeded();
        if (z || z2) {
            return true;
        }
        if (!isFaceRecognitionSucceeded || !this.mKeyguardBypassController.canBypass()) {
            return false;
        }
        return true;
    }

    public boolean getUserTrustIsManaged(int i) {
        return this.mUserTrustIsManaged.get(i) && !isTrustDisabled(i);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0087 A[LOOP:0: B:16:0x0087->B:21:0x00a2, LOOP_START, PHI: r3 
      PHI: (r3v1 int) = (r3v0 int), (r3v2 int) binds: [B:15:0x0085, B:21:0x00a2] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARNING: Removed duplicated region for block: B:25:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateSecondaryLockscreenRequirement(int r6) {
        /*
            r5 = this;
            java.util.Map<java.lang.Integer, android.content.Intent> r0 = r5.mSecondaryLockscreenRequirement
            java.lang.Integer r1 = java.lang.Integer.valueOf((int) r6)
            java.lang.Object r0 = r0.get(r1)
            android.content.Intent r0 = (android.content.Intent) r0
            android.app.admin.DevicePolicyManager r1 = r5.mDevicePolicyManager
            android.os.UserHandle r2 = android.os.UserHandle.of(r6)
            boolean r1 = r1.isSecondaryLockscreenEnabled(r2)
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0075
            if (r0 != 0) goto L_0x0075
            android.app.admin.DevicePolicyManager r0 = r5.mDevicePolicyManager
            android.os.UserHandle r1 = android.os.UserHandle.of(r6)
            android.content.ComponentName r0 = r0.getProfileOwnerOrDeviceOwnerSupervisionComponent(r1)
            if (r0 != 0) goto L_0x003d
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "No Profile Owner or Device Owner supervision app found for User "
            r0.<init>((java.lang.String) r1)
            java.lang.StringBuilder r0 = r0.append((int) r6)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "KeyguardUpdateMonitor"
            android.util.Log.e(r1, r0)
            goto L_0x0084
        L_0x003d:
            android.content.Intent r1 = new android.content.Intent
            java.lang.String r4 = "android.app.action.BIND_SECONDARY_LOCKSCREEN_SERVICE"
            r1.<init>(r4)
            java.lang.String r0 = r0.getPackageName()
            android.content.Intent r0 = r1.setPackage(r0)
            android.content.Context r1 = r5.mContext
            android.content.pm.PackageManager r1 = r1.getPackageManager()
            android.content.pm.ResolveInfo r0 = r1.resolveService(r0, r3)
            if (r0 == 0) goto L_0x0084
            android.content.pm.ServiceInfo r1 = r0.serviceInfo
            if (r1 == 0) goto L_0x0084
            android.content.Intent r1 = new android.content.Intent
            r1.<init>()
            android.content.pm.ServiceInfo r0 = r0.serviceInfo
            android.content.ComponentName r0 = r0.getComponentName()
            android.content.Intent r0 = r1.setComponent(r0)
            java.util.Map<java.lang.Integer, android.content.Intent> r1 = r5.mSecondaryLockscreenRequirement
            java.lang.Integer r4 = java.lang.Integer.valueOf((int) r6)
            r1.put(r4, r0)
            goto L_0x0085
        L_0x0075:
            if (r1 != 0) goto L_0x0084
            if (r0 == 0) goto L_0x0084
            java.util.Map<java.lang.Integer, android.content.Intent> r0 = r5.mSecondaryLockscreenRequirement
            java.lang.Integer r1 = java.lang.Integer.valueOf((int) r6)
            r4 = 0
            r0.put(r1, r4)
            goto L_0x0085
        L_0x0084:
            r2 = r3
        L_0x0085:
            if (r2 == 0) goto L_0x00a5
        L_0x0087:
            java.util.ArrayList<java.lang.ref.WeakReference<com.android.keyguard.KeyguardUpdateMonitorCallback>> r0 = r5.mCallbacks
            int r0 = r0.size()
            if (r3 >= r0) goto L_0x00a5
            java.util.ArrayList<java.lang.ref.WeakReference<com.android.keyguard.KeyguardUpdateMonitorCallback>> r0 = r5.mCallbacks
            java.lang.Object r0 = r0.get(r3)
            java.lang.ref.WeakReference r0 = (java.lang.ref.WeakReference) r0
            java.lang.Object r0 = r0.get()
            com.android.keyguard.KeyguardUpdateMonitorCallback r0 = (com.android.keyguard.KeyguardUpdateMonitorCallback) r0
            if (r0 == 0) goto L_0x00a2
            r0.onSecondaryLockscreenRequirementChanged(r6)
        L_0x00a2:
            int r3 = r3 + 1
            goto L_0x0087
        L_0x00a5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardUpdateMonitor.updateSecondaryLockscreenRequirement(int):void");
    }

    public Intent getSecondaryLockscreenRequirement(int i) {
        return this.mSecondaryLockscreenRequirement.get(Integer.valueOf(i));
    }

    public boolean isTrustUsuallyManaged(int i) {
        Assert.isMainThread();
        return this.mUserTrustIsUsuallyManaged.get(i);
    }

    public boolean isUnlockingWithBiometricAllowed(boolean z) {
        return this.mStrongAuthTracker.isUnlockingWithBiometricAllowed(z);
    }

    public boolean isUserInLockdown(int i) {
        return containsFlag(this.mStrongAuthTracker.getStrongAuthForUser(i), 32);
    }

    public boolean isEncryptedOrLockdown(int i) {
        int strongAuthForUser = this.mStrongAuthTracker.getStrongAuthForUser(i);
        boolean z = containsFlag(strongAuthForUser, 2) || containsFlag(strongAuthForUser, 32);
        if (containsFlag(strongAuthForUser, 1) || z) {
            return true;
        }
        return false;
    }

    public boolean userNeedsStrongAuth() {
        return this.mStrongAuthTracker.getStrongAuthForUser(getCurrentUser()) != 0;
    }

    public boolean needsSlowUnlockTransition() {
        return this.mNeedsSlowUnlockTransition;
    }

    public StrongAuthTracker getStrongAuthTracker() {
        return this.mStrongAuthTracker;
    }

    /* access modifiers changed from: private */
    public void notifyStrongAuthStateChanged(int i) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onStrongAuthStateChanged(i);
            }
        }
    }

    private void notifyLockedOutStateChanged(BiometricSourceType biometricSourceType) {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onLockedOutStateChanged(biometricSourceType);
            }
        }
    }

    private void dispatchErrorMessage(CharSequence charSequence) {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTrustAgentErrorMessage(charSequence);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setAssistantVisible(boolean z) {
        this.mAssistantVisible = z;
        updateBiometricListeningState(2);
        if (this.mAssistantVisible) {
            requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.ASSISTANT, "assistant", false);
        }
    }

    static class DisplayClientState {
        public boolean clearing;
        public int clientGeneration;
        public PendingIntent intent;
        public long playbackEventTime;
        public int playbackState;

        DisplayClientState() {
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$5$com-android-keyguard-KeyguardUpdateMonitor  reason: not valid java name */
    public /* synthetic */ void m2294lambda$new$5$comandroidkeyguardKeyguardUpdateMonitor(int i, int i2, boolean z) {
        handleFingerprintAuthenticated(i2, z);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$6$com-android-keyguard-KeyguardUpdateMonitor  reason: not valid java name */
    public /* synthetic */ void m2295lambda$new$6$comandroidkeyguardKeyguardUpdateMonitor(int i, int i2, boolean z) {
        handleFaceAuthenticated(i2, z);
    }

    private static class SimData {
        public int simState;
        public int slotId;
        public int subId;

        SimData(int i, int i2, int i3) {
            this.simState = i;
            this.slotId = i2;
            this.subId = i3;
        }

        static SimData fromIntent(Intent intent) {
            if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra("ss");
                int i = 0;
                int intExtra = intent.getIntExtra("android.telephony.extra.SLOT_INDEX", 0);
                int intExtra2 = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
                if ("ABSENT".equals(stringExtra)) {
                    i = "PERM_DISABLED".equals(intent.getStringExtra("reason")) ? 7 : 1;
                } else {
                    if (!"READY".equals(stringExtra)) {
                        if ("LOCKED".equals(stringExtra)) {
                            String stringExtra2 = intent.getStringExtra("reason");
                            if ("PIN".equals(stringExtra2)) {
                                i = 2;
                            } else if ("PUK".equals(stringExtra2)) {
                                i = 3;
                            }
                        } else if ("NETWORK".equals(stringExtra)) {
                            i = 4;
                        } else if ("CARD_IO_ERROR".equals(stringExtra)) {
                            i = 8;
                        } else if (!"IMSI".equals(stringExtra)) {
                            if ("LOADED".equals(stringExtra)) {
                                i = 10;
                            }
                        }
                    }
                    i = 5;
                }
                return new SimData(i, intExtra, intExtra2);
            }
            throw new IllegalArgumentException("only handles intent ACTION_SIM_STATE_CHANGED");
        }

        public String toString() {
            return "SimData{state=" + this.simState + ",slotId=" + this.slotId + ",subId=" + this.subId + "}";
        }
    }

    public static class StrongAuthTracker extends LockPatternUtils.StrongAuthTracker {
        private final Consumer<Integer> mStrongAuthRequiredChangedCallback;

        public StrongAuthTracker(Context context, Consumer<Integer> consumer) {
            super(context);
            this.mStrongAuthRequiredChangedCallback = consumer;
        }

        public boolean isUnlockingWithBiometricAllowed(boolean z) {
            return isBiometricAllowedForUser(z, KeyguardUpdateMonitor.getCurrentUser());
        }

        public boolean hasUserAuthenticatedSinceBoot() {
            return (getStrongAuthForUser(KeyguardUpdateMonitor.getCurrentUser()) & 1) == 0;
        }

        public void onStrongAuthRequiredChanged(int i) {
            this.mStrongAuthRequiredChangedCallback.accept(Integer.valueOf(i));
        }
    }

    /* access modifiers changed from: protected */
    public void handleStartedWakingUp() {
        Trace.beginSection("KeyguardUpdateMonitor#handleStartedWakingUp");
        Assert.isMainThread();
        updateBiometricListeningState(2);
        requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.WAKE, "wakingUp");
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onStartedWakingUp();
            }
        }
        Trace.endSection();
    }

    /* access modifiers changed from: protected */
    public void handleStartedGoingToSleep(int i) {
        Assert.isMainThread();
        this.mLockIconPressed = false;
        clearBiometricRecognized();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onStartedGoingToSleep(i);
            }
        }
        this.mGoingToSleep = true;
        updateBiometricListeningState(2);
    }

    /* access modifiers changed from: protected */
    public void handleFinishedGoingToSleep(int i) {
        Assert.isMainThread();
        this.mGoingToSleep = false;
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onFinishedGoingToSleep(i);
            }
        }
        updateBiometricListeningState(1);
    }

    /* access modifiers changed from: private */
    public void handleScreenTurnedOff() {
        Assert.isMainThread();
        this.mHardwareFingerprintUnavailableRetryCount = 0;
        this.mHardwareFaceUnavailableRetryCount = 0;
    }

    /* access modifiers changed from: private */
    public void handleDreamingStateChanged(int i) {
        Assert.isMainThread();
        this.mIsDreaming = i == 1;
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onDreamingStateChanged(this.mIsDreaming);
            }
        }
        if (this.mIsDreaming) {
            updateFingerprintListeningState(2);
            updateFaceListeningState(1);
            return;
        }
        updateBiometricListeningState(2);
    }

    /* access modifiers changed from: private */
    public void handleUserInfoChanged(int i) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onUserInfoChanged(i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleUserUnlocked(int i) {
        Assert.isMainThread();
        this.mUserIsUnlocked.put(i, true);
        this.mNeedsSlowUnlockTransition = resolveNeedsSlowUnlockTransition();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onUserUnlocked();
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleUserStopped(int i) {
        Assert.isMainThread();
        this.mUserIsUnlocked.put(i, this.mUserManager.isUserUnlocked(i));
    }

    /* access modifiers changed from: package-private */
    public void handleUserRemoved(int i) {
        Assert.isMainThread();
        this.mUserIsUnlocked.delete(i);
        this.mUserTrustIsUsuallyManaged.delete(i);
    }

    /* access modifiers changed from: private */
    public void handleKeyguardGoingAway(boolean z) {
        Assert.isMainThread();
        setKeyguardGoingAway(z);
    }

    /* access modifiers changed from: protected */
    public void setStrongAuthTracker(StrongAuthTracker strongAuthTracker) {
        StrongAuthTracker strongAuthTracker2 = this.mStrongAuthTracker;
        if (strongAuthTracker2 != null) {
            this.mLockPatternUtils.unregisterStrongAuthTracker(strongAuthTracker2);
        }
        this.mStrongAuthTracker = strongAuthTracker;
        this.mLockPatternUtils.registerStrongAuthTracker(strongAuthTracker);
    }

    /* access modifiers changed from: package-private */
    public void resetBiometricListeningState() {
        this.mFingerprintRunningState = 0;
        this.mFaceRunningState = 0;
    }

    @Inject
    protected KeyguardUpdateMonitor(Context context, @Main Looper looper, BroadcastDispatcher broadcastDispatcher, DumpManager dumpManager, @Background Executor executor, @Main Executor executor2, StatusBarStateController statusBarStateController, LockPatternUtils lockPatternUtils, AuthController authController, TelephonyListenerManager telephonyListenerManager, InteractionJankMonitor interactionJankMonitor, LatencyTracker latencyTracker, ActiveUnlockConfig activeUnlockConfig) {
        Context context2 = context;
        BroadcastDispatcher broadcastDispatcher2 = broadcastDispatcher;
        Executor executor3 = executor;
        StatusBarStateController statusBarStateController2 = statusBarStateController;
        ActiveUnlockConfig activeUnlockConfig2 = activeUnlockConfig;
        C16461 r7 = new StatusBarStateController.StateListener() {
            public void onStateChanged(int i) {
                int unused = KeyguardUpdateMonitor.this.mStatusBarState = i;
            }

            public void onExpandedChanged(boolean z) {
                for (int i = 0; i < KeyguardUpdateMonitor.this.mCallbacks.size(); i++) {
                    KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) ((WeakReference) KeyguardUpdateMonitor.this.mCallbacks.get(i)).get();
                    if (keyguardUpdateMonitorCallback != null) {
                        keyguardUpdateMonitorCallback.onShadeExpandedChanged(z);
                    }
                }
            }
        };
        this.mStatusBarStateControllerListener = r7;
        this.mSimDatas = new HashMap<>();
        this.mServiceStates = new HashMap<>();
        this.mCallbacks = Lists.newArrayList();
        this.mFingerprintRunningState = 0;
        this.mFaceRunningState = 0;
        this.mActiveMobileDataSubscription = -1;
        this.mHardwareFingerprintUnavailableRetryCount = 0;
        this.mHardwareFaceUnavailableRetryCount = 0;
        this.mFpCancelNotReceived = new KeyguardUpdateMonitor$$ExternalSyntheticLambda3(this);
        this.mFaceCancelNotReceived = new KeyguardUpdateMonitor$$ExternalSyntheticLambda4(this);
        this.mBiometricEnabledForUser = new SparseBooleanArray();
        this.mBiometricEnabledCallback = new IBiometricEnabledOnKeyguardCallback.Stub() {
            public void onChanged(boolean z, int i) throws RemoteException {
                KeyguardUpdateMonitor.this.mHandler.post(new KeyguardUpdateMonitor$2$$ExternalSyntheticLambda0(this, i, z));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onChanged$0$com-android-keyguard-KeyguardUpdateMonitor$2  reason: not valid java name */
            public /* synthetic */ void m2297lambda$onChanged$0$comandroidkeyguardKeyguardUpdateMonitor$2(int i, boolean z) {
                KeyguardUpdateMonitor.this.mBiometricEnabledForUser.put(i, z);
                KeyguardUpdateMonitor.this.updateBiometricListeningState(2);
            }
        };
        this.mPhoneStateListener = new TelephonyCallback.ActiveDataSubscriptionIdListener() {
            public void onActiveDataSubscriptionIdChanged(int i) {
                int unused = KeyguardUpdateMonitor.this.mActiveMobileDataSubscription = i;
                KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(KeyguardUpdateMonitor.MSG_SIM_SUBSCRIPTION_INFO_CHANGED);
            }
        };
        this.mSubscriptionListener = new SubscriptionManager.OnSubscriptionsChangedListener() {
            public void onSubscriptionsChanged() {
                KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(KeyguardUpdateMonitor.MSG_SIM_SUBSCRIPTION_INFO_CHANGED);
            }
        };
        this.mUserIsUnlocked = new SparseBooleanArray();
        this.mUserHasTrust = new SparseBooleanArray();
        this.mUserTrustIsManaged = new SparseBooleanArray();
        this.mUserTrustIsUsuallyManaged = new SparseBooleanArray();
        this.mUserFaceUnlockRunning = new SparseBooleanArray();
        this.mSecondaryLockscreenRequirement = new HashMap();
        this.mUserFingerprintAuthenticated = new SparseArray<>();
        this.mUserFaceAuthenticated = new SparseArray<>();
        this.mListenModels = new KeyguardListenQueue();
        this.mRetryFingerprintAuthentication = new Runnable() {
            public void run() {
                Log.w(KeyguardUpdateMonitor.TAG, "Retrying fingerprint after HW unavailable, attempt " + KeyguardUpdateMonitor.this.mHardwareFingerprintUnavailableRetryCount);
                if (KeyguardUpdateMonitor.this.mFpm.isHardwareDetected()) {
                    KeyguardUpdateMonitor.this.updateFingerprintListeningState(2);
                } else if (KeyguardUpdateMonitor.this.mHardwareFingerprintUnavailableRetryCount < 20) {
                    KeyguardUpdateMonitor.access$708(KeyguardUpdateMonitor.this);
                    KeyguardUpdateMonitor.this.mHandler.postDelayed(KeyguardUpdateMonitor.this.mRetryFingerprintAuthentication, 500);
                }
            }
        };
        this.mRetryFaceAuthentication = new Runnable() {
            public void run() {
                Log.w(KeyguardUpdateMonitor.TAG, "Retrying face after HW unavailable, attempt " + KeyguardUpdateMonitor.this.mHardwareFaceUnavailableRetryCount);
                KeyguardUpdateMonitor.this.updateFaceListeningState(2);
            }
        };
        this.mDisplayClientState = new DisplayClientState();
        C16638 r9 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (KeyguardUpdateMonitor.DEBUG) {
                    Log.d(KeyguardUpdateMonitor.TAG, "received broadcast " + action);
                }
                if ("android.intent.action.TIME_TICK".equals(action) || "android.intent.action.TIME_SET".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(301);
                } else if ("android.intent.action.TIMEZONE_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(KeyguardUpdateMonitor.MSG_TIMEZONE_UPDATE, intent.getStringExtra("time-zone")));
                } else if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(302, new BatteryStatus(intent)));
                } else if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    SimData fromIntent = SimData.fromIntent(intent);
                    if (!intent.getBooleanExtra("rebroadcastOnUnlock", false)) {
                        Log.v(KeyguardUpdateMonitor.TAG, "action " + action + " state: " + intent.getStringExtra("ss") + " slotId: " + fromIntent.slotId + " subid: " + fromIntent.subId);
                        KeyguardUpdateMonitor.this.mHandler.obtainMessage(304, fromIntent.subId, fromIntent.slotId, Integer.valueOf(fromIntent.simState)).sendToTarget();
                    } else if (fromIntent.simState == 1) {
                        KeyguardUpdateMonitor.this.mHandler.obtainMessage(KeyguardUpdateMonitor.MSG_TELEPHONY_CAPABLE, true).sendToTarget();
                    }
                } else if ("android.intent.action.PHONE_STATE".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(306, intent.getStringExtra(AuthDialog.KEY_BIOMETRIC_STATE)));
                } else if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(KeyguardUpdateMonitor.MSG_AIRPLANE_MODE_CHANGED);
                } else if ("android.intent.action.SERVICE_STATE".equals(action)) {
                    ServiceState newFromBundle = ServiceState.newFromBundle(intent.getExtras());
                    int intExtra = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
                    if (KeyguardUpdateMonitor.DEBUG) {
                        Log.v(KeyguardUpdateMonitor.TAG, "action " + action + " serviceState=" + newFromBundle + " subId=" + intExtra);
                    }
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(KeyguardUpdateMonitor.MSG_SERVICE_STATE_CHANGE, intExtra, 0, newFromBundle));
                } else if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(KeyguardUpdateMonitor.MSG_SIM_SUBSCRIPTION_INFO_CHANGED);
                } else if ("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(KeyguardUpdateMonitor.MSG_DEVICE_POLICY_MANAGER_STATE_CHANGED);
                }
            }
        };
        this.mBroadcastReceiver = r9;
        C16649 r10 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("android.app.action.NEXT_ALARM_CLOCK_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(301);
                } else if ("android.intent.action.USER_INFO_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(317, intent.getIntExtra("android.intent.extra.user_handle", getSendingUserId()), 0));
                } else if (KeyguardUpdateMonitor.ACTION_FACE_UNLOCK_STARTED.equals(action)) {
                    Trace.beginSection("KeyguardUpdateMonitor.mBroadcastAllReceiver#onReceive ACTION_FACE_UNLOCK_STARTED");
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(KeyguardUpdateMonitor.MSG_FACE_UNLOCK_STATE_CHANGED, 1, getSendingUserId()));
                    Trace.endSection();
                } else if (KeyguardUpdateMonitor.ACTION_FACE_UNLOCK_STOPPED.equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(KeyguardUpdateMonitor.MSG_FACE_UNLOCK_STATE_CHANGED, 0, getSendingUserId()));
                } else if ("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(309, Integer.valueOf(getSendingUserId())));
                } else if ("android.intent.action.USER_UNLOCKED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(KeyguardUpdateMonitor.MSG_USER_UNLOCKED, getSendingUserId(), 0));
                } else if ("android.intent.action.USER_STOPPED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(340, intent.getIntExtra("android.intent.extra.user_handle", -1), 0));
                } else if ("android.intent.action.USER_REMOVED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(KeyguardUpdateMonitor.MSG_USER_REMOVED, intent.getIntExtra("android.intent.extra.user_handle", -1), 0));
                } else if ("android.nfc.action.REQUIRE_UNLOCK_FOR_NFC".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(KeyguardUpdateMonitor.MSG_REQUIRE_NFC_UNLOCK);
                }
            }
        };
        this.mBroadcastAllReceiver = r10;
        this.mFingerprintLockoutResetCallback = new FingerprintManager.LockoutResetCallback() {
            public void onLockoutReset(int i) {
                KeyguardUpdateMonitor.this.handleFingerprintLockoutReset(0);
            }
        };
        this.mFaceLockoutResetCallback = new FaceManager.LockoutResetCallback() {
            public void onLockoutReset(int i) {
                KeyguardUpdateMonitor.this.handleFaceLockoutReset(0);
            }
        };
        this.mFingerprintDetectionCallback = new KeyguardUpdateMonitor$$ExternalSyntheticLambda5(this);
        this.mFingerprintAuthenticationCallback = new FingerprintManager.AuthenticationCallback() {
            public void onAuthenticationFailed() {
                KeyguardUpdateMonitor.this.requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.BIOMETRIC_FAIL, "fingerprintFailure");
                KeyguardUpdateMonitor.this.handleFingerprintAuthFailed();
                NTVibrateUtils.getInstance(KeyguardUpdateMonitor.this.mContext).playVerityVibrate(NTVibrateUtils.VibrateType.FINGERPRINT_VERIFICATION_ERROR);
                SystemUIEventUtils.collectUnLockResults(KeyguardUpdateMonitor.this.mContext, SystemUIEventUtils.EVENT_PROPERTY_KEY_UNLOCK_FAIL, 4);
            }

            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult authenticationResult) {
                ((FaceRecognitionController) NTDependencyEx.get(FaceRecognitionController.class)).postToStopCamera();
                Trace.beginSection("KeyguardUpdateMonitor#onAuthenticationSucceeded");
                KeyguardUpdateMonitor.this.handleFingerprintAuthenticated(authenticationResult.getUserId(), authenticationResult.isStrongBiometric());
                NTVibrateUtils.getInstance(KeyguardUpdateMonitor.this.mContext).playVerityVibrate(NTVibrateUtils.VibrateType.FINGERPRINT_VERIFICATION_SUCCEEDED);
                SystemUIEventUtils.collectUnLockResults(KeyguardUpdateMonitor.this.mContext, SystemUIEventUtils.EVENT_PROPERTY_KEY_UNLOCK_SUCCESS, 4);
                Trace.endSection();
            }

            public void onAuthenticationHelp(int i, CharSequence charSequence) {
                Trace.beginSection("KeyguardUpdateMonitor#onAuthenticationHelp");
                NTLogUtil.m1680d(KeyguardUpdateMonitor.TAG, "onAuthenticationHelp " + i + " msg " + charSequence);
                if (i != 1001 && i != 1002 && i != 1003) {
                    KeyguardUpdateMonitor.this.handleFingerprintHelp(i, charSequence.toString());
                    Trace.endSection();
                }
            }

            public void onAuthenticationError(int i, CharSequence charSequence) {
                Trace.beginSection("KeyguardUpdateMonitor#onAuthenticationError");
                if (i != 5) {
                    NTVibrateUtils.getInstance(KeyguardUpdateMonitor.this.mContext).playVerityVibrate(NTVibrateUtils.VibrateType.FINGERPRINT_VERIFICATION_ERROR);
                }
                KeyguardUpdateMonitor.this.handleFingerprintError(i, charSequence.toString());
                Trace.endSection();
            }

            public void onAuthenticationAcquired(int i) {
                Trace.beginSection("KeyguardUpdateMonitor#onAuthenticationAcquired");
                KeyguardUpdateMonitor.this.handleFingerprintAcquired(i);
                Trace.endSection();
            }

            public void onUdfpsPointerDown(int i) {
                Log.d(KeyguardUpdateMonitor.TAG, "onUdfpsPointerDown, sensorId: " + i);
                KeyguardUpdateMonitor.this.requestFaceAuth(true);
                if (KeyguardUpdateMonitor.this.isFaceDetectionRunning()) {
                    KeyguardUpdateMonitor.this.mKeyguardBypassController.setUserHasDeviceEntryIntent(true);
                }
            }

            public void onUdfpsPointerUp(int i) {
                Log.d(KeyguardUpdateMonitor.TAG, "onUdfpsPointerUp, sensorId: " + i);
            }
        };
        this.mFaceDetectionCallback = new KeyguardUpdateMonitor$$ExternalSyntheticLambda6(this);
        this.mFaceAuthenticationCallback = new FaceManager.AuthenticationCallback() {
            public void onAuthenticationFailed() {
                String str;
                if (KeyguardUpdateMonitor.this.mKeyguardBypassController.canBypass()) {
                    str = "bypass";
                } else if (KeyguardUpdateMonitor.this.mUdfpsBouncerShowing) {
                    str = "udfpsBouncer";
                } else {
                    str = KeyguardUpdateMonitor.this.mBouncerFullyShown ? "bouncer" : "udfpsFpDown";
                }
                KeyguardUpdateMonitor.this.requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.BIOMETRIC_FAIL, "faceFailure-" + str);
                KeyguardUpdateMonitor.this.handleFaceAuthFailed();
                if (KeyguardUpdateMonitor.this.mKeyguardBypassController != null) {
                    KeyguardUpdateMonitor.this.mKeyguardBypassController.setUserHasDeviceEntryIntent(false);
                }
            }

            public void onAuthenticationSucceeded(FaceManager.AuthenticationResult authenticationResult) {
                Trace.beginSection("KeyguardUpdateMonitor#onAuthenticationSucceeded");
                KeyguardUpdateMonitor.this.handleFaceAuthenticated(authenticationResult.getUserId(), authenticationResult.isStrongBiometric());
                Trace.endSection();
                if (KeyguardUpdateMonitor.this.mKeyguardBypassController != null) {
                    KeyguardUpdateMonitor.this.mKeyguardBypassController.setUserHasDeviceEntryIntent(false);
                }
            }

            public void onAuthenticationHelp(int i, CharSequence charSequence) {
                KeyguardUpdateMonitor.this.handleFaceHelp(i, charSequence.toString());
            }

            public void onAuthenticationError(int i, CharSequence charSequence) {
                KeyguardUpdateMonitor.this.handleFaceError(i, charSequence.toString());
                if (KeyguardUpdateMonitor.this.mKeyguardBypassController != null) {
                    KeyguardUpdateMonitor.this.mKeyguardBypassController.setUserHasDeviceEntryIntent(false);
                }
                if (KeyguardUpdateMonitor.this.mActiveUnlockConfig.shouldRequestActiveUnlockOnFaceError(i)) {
                    KeyguardUpdateMonitor.this.requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.BIOMETRIC_FAIL, "faceError-" + i);
                }
            }

            public void onAuthenticationAcquired(int i) {
                KeyguardUpdateMonitor.this.handleFaceAcquired(i);
                if (KeyguardUpdateMonitor.this.mActiveUnlockConfig.shouldRequestActiveUnlockOnFaceAcquireInfo(i)) {
                    KeyguardUpdateMonitor.this.requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.BIOMETRIC_FAIL, "faceAcquireInfo-" + i);
                }
            }
        };
        this.mIsUnlockWithFingerprintPossible = new HashMap<>();
        C165417 r11 = new UserSwitchObserver() {
            public void onUserSwitching(int i, IRemoteCallback iRemoteCallback) {
                KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(310, i, 0, iRemoteCallback));
            }

            public void onUserSwitchComplete(int i) throws RemoteException {
                KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(314, i, 0));
            }
        };
        this.mUserSwitchObserver = r11;
        this.mTaskStackListener = new TaskStackChangeListener() {
            public void onTaskStackChangedBackground() {
                try {
                    ActivityTaskManager.RootTaskInfo rootTaskInfo = ActivityTaskManager.getService().getRootTaskInfo(0, 4);
                    if (rootTaskInfo != null) {
                        KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(KeyguardUpdateMonitor.MSG_ASSISTANT_STACK_CHANGED, Boolean.valueOf(rootTaskInfo.visible)));
                    }
                } catch (RemoteException e) {
                    Log.e(KeyguardUpdateMonitor.TAG, "unable to check task stack", e);
                }
            }
        };
        this.mContext = context2;
        this.mSubscriptionManager = SubscriptionManager.from(context);
        this.mTelephonyListenerManager = telephonyListenerManager;
        this.mDeviceProvisioned = isDeviceProvisionedInSettingsDb();
        this.mStrongAuthTracker = new StrongAuthTracker(context2, new KeyguardUpdateMonitor$$ExternalSyntheticLambda7(this));
        this.mBackgroundExecutor = executor3;
        this.mBroadcastDispatcher = broadcastDispatcher2;
        this.mInteractionJankMonitor = interactionJankMonitor;
        this.mLatencyTracker = latencyTracker;
        this.mStatusBarStateController = statusBarStateController2;
        statusBarStateController2.addCallback(r7);
        this.mStatusBarState = statusBarStateController.getState();
        this.mLockPatternUtils = lockPatternUtils;
        this.mAuthController = authController;
        dumpManager.registerDumpable(getClass().getName(), this);
        this.mSensorPrivacyManager = (SensorPrivacyManager) context2.getSystemService(SensorPrivacyManager.class);
        this.mActiveUnlockConfig = activeUnlockConfig2;
        activeUnlockConfig2.setKeyguardUpdateMonitor(this);
        C165114 r4 = new Handler(looper) {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 301:
                        KeyguardUpdateMonitor.this.handleTimeUpdate();
                        return;
                    case 302:
                        KeyguardUpdateMonitor.this.handleBatteryUpdate((BatteryStatus) message.obj);
                        return;
                    case 304:
                        KeyguardUpdateMonitor.this.handleSimStateChange(message.arg1, message.arg2, ((Integer) message.obj).intValue());
                        return;
                    case 306:
                        KeyguardUpdateMonitor.this.handlePhoneStateChanged((String) message.obj);
                        return;
                    case 308:
                        KeyguardUpdateMonitor.this.handleDeviceProvisioned();
                        return;
                    case 309:
                        KeyguardUpdateMonitor.this.handleDevicePolicyManagerStateChanged(message.arg1);
                        return;
                    case 310:
                        KeyguardUpdateMonitor.this.handleUserSwitching(message.arg1, (IRemoteCallback) message.obj);
                        return;
                    case 312:
                        KeyguardUpdateMonitor.this.handleKeyguardReset();
                        return;
                    case 314:
                        KeyguardUpdateMonitor.this.handleUserSwitchComplete(message.arg1);
                        return;
                    case 317:
                        KeyguardUpdateMonitor.this.handleUserInfoChanged(message.arg1);
                        return;
                    case 318:
                        KeyguardUpdateMonitor.this.handleReportEmergencyCallAction();
                        return;
                    case 319:
                        Trace.beginSection("KeyguardUpdateMonitor#handler MSG_STARTED_WAKING_UP");
                        KeyguardUpdateMonitor.this.handleStartedWakingUp();
                        Trace.endSection();
                        return;
                    case 320:
                        KeyguardUpdateMonitor.this.handleFinishedGoingToSleep(message.arg1);
                        return;
                    case KeyguardUpdateMonitor.MSG_STARTED_GOING_TO_SLEEP /*321*/:
                        KeyguardUpdateMonitor.this.handleStartedGoingToSleep(message.arg1);
                        return;
                    case KeyguardUpdateMonitor.MSG_KEYGUARD_BOUNCER_CHANGED /*322*/:
                        KeyguardUpdateMonitor.this.handleKeyguardBouncerChanged(message.arg1, message.arg2);
                        return;
                    case KeyguardUpdateMonitor.MSG_FACE_UNLOCK_STATE_CHANGED /*327*/:
                        Trace.beginSection("KeyguardUpdateMonitor#handler MSG_FACE_UNLOCK_STATE_CHANGED");
                        KeyguardUpdateMonitor.this.handleFaceUnlockStateChanged(message.arg1 != 0, message.arg2);
                        Trace.endSection();
                        return;
                    case KeyguardUpdateMonitor.MSG_SIM_SUBSCRIPTION_INFO_CHANGED /*328*/:
                        KeyguardUpdateMonitor.this.handleSimSubscriptionInfoChanged();
                        return;
                    case KeyguardUpdateMonitor.MSG_AIRPLANE_MODE_CHANGED /*329*/:
                        KeyguardUpdateMonitor.this.handleAirplaneModeChanged();
                        return;
                    case KeyguardUpdateMonitor.MSG_SERVICE_STATE_CHANGE /*330*/:
                        KeyguardUpdateMonitor.this.handleServiceStateChange(message.arg1, (ServiceState) message.obj);
                        return;
                    case KeyguardUpdateMonitor.MSG_SCREEN_TURNED_OFF /*332*/:
                        Trace.beginSection("KeyguardUpdateMonitor#handler MSG_SCREEN_TURNED_OFF");
                        KeyguardUpdateMonitor.this.handleScreenTurnedOff();
                        Trace.endSection();
                        return;
                    case KeyguardUpdateMonitor.MSG_DREAMING_STATE_CHANGED /*333*/:
                        KeyguardUpdateMonitor.this.handleDreamingStateChanged(message.arg1);
                        return;
                    case KeyguardUpdateMonitor.MSG_USER_UNLOCKED /*334*/:
                        KeyguardUpdateMonitor.this.handleUserUnlocked(message.arg1);
                        return;
                    case KeyguardUpdateMonitor.MSG_ASSISTANT_STACK_CHANGED /*335*/:
                        KeyguardUpdateMonitor.this.setAssistantVisible(((Boolean) message.obj).booleanValue());
                        return;
                    case KeyguardUpdateMonitor.MSG_BIOMETRIC_AUTHENTICATION_CONTINUE /*336*/:
                        KeyguardUpdateMonitor.this.updateBiometricListeningState(2);
                        return;
                    case KeyguardUpdateMonitor.MSG_DEVICE_POLICY_MANAGER_STATE_CHANGED /*337*/:
                        KeyguardUpdateMonitor.this.updateLogoutEnabled();
                        return;
                    case KeyguardUpdateMonitor.MSG_TELEPHONY_CAPABLE /*338*/:
                        KeyguardUpdateMonitor.this.updateTelephonyCapable(((Boolean) message.obj).booleanValue());
                        return;
                    case KeyguardUpdateMonitor.MSG_TIMEZONE_UPDATE /*339*/:
                        KeyguardUpdateMonitor.this.handleTimeZoneUpdate((String) message.obj);
                        return;
                    case 340:
                        KeyguardUpdateMonitor.this.handleUserStopped(message.arg1);
                        return;
                    case KeyguardUpdateMonitor.MSG_USER_REMOVED /*341*/:
                        KeyguardUpdateMonitor.this.handleUserRemoved(message.arg1);
                        return;
                    case KeyguardUpdateMonitor.MSG_KEYGUARD_GOING_AWAY /*342*/:
                        KeyguardUpdateMonitor.this.handleKeyguardGoingAway(((Boolean) message.obj).booleanValue());
                        return;
                    case KeyguardUpdateMonitor.MSG_TIME_FORMAT_UPDATE /*344*/:
                        KeyguardUpdateMonitor.this.handleTimeFormatUpdate((String) message.obj);
                        return;
                    case KeyguardUpdateMonitor.MSG_REQUIRE_NFC_UNLOCK /*345*/:
                        KeyguardUpdateMonitor.this.handleRequireUnlockForNfc();
                        return;
                    case KeyguardUpdateMonitor.MSG_KEYGUARD_DISMISS_ANIMATION_FINISHED /*346*/:
                        KeyguardUpdateMonitor.this.handleKeyguardDismissAnimationFinished();
                        return;
                    case KeyguardUpdateMonitor.MSG_RESET_FACE_RECOGNITION_STATE /*347*/:
                        KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
                        keyguardUpdateMonitor.handleFaceRecognitionSucceeded(keyguardUpdateMonitor.mFaceRecognitionSucceeded);
                        KeyguardUpdateMonitor.this.updateBiometricListeningState(2);
                        return;
                    default:
                        super.handleMessage(message);
                        return;
                }
            }
        };
        this.mHandler = r4;
        if (!this.mDeviceProvisioned) {
            watchForDeviceProvisioning();
        }
        this.mBatteryStatus = new BatteryStatus(1, 100, 0, 0, 0, true);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_TICK");
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.SERVICE_STATE");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        intentFilter.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
        broadcastDispatcher2.registerReceiverWithHandler(r9, intentFilter, r4);
        executor3.execute(new KeyguardUpdateMonitor$$ExternalSyntheticLambda8(this));
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.USER_INFO_CHANGED");
        intentFilter2.addAction("android.app.action.NEXT_ALARM_CLOCK_CHANGED");
        intentFilter2.addAction(ACTION_FACE_UNLOCK_STARTED);
        intentFilter2.addAction(ACTION_FACE_UNLOCK_STOPPED);
        intentFilter2.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
        intentFilter2.addAction("android.intent.action.USER_UNLOCKED");
        intentFilter2.addAction("android.intent.action.USER_STOPPED");
        intentFilter2.addAction("android.intent.action.USER_REMOVED");
        intentFilter2.addAction("android.nfc.action.REQUIRE_UNLOCK_FOR_NFC");
        broadcastDispatcher2.registerReceiverWithHandler(r10, intentFilter2, r4, UserHandle.ALL);
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mSubscriptionListener);
        try {
            ActivityManager.getService().registerUserSwitchObserver(r11, TAG);
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
        TrustManager trustManager = (TrustManager) context2.getSystemService(TrustManager.class);
        this.mTrustManager = trustManager;
        trustManager.registerTrustListener(this);
        setStrongAuthTracker(this.mStrongAuthTracker);
        this.mDreamManager = IDreamManager.Stub.asInterface(ServiceManager.getService("dreams"));
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.fingerprint")) {
            FingerprintManager fingerprintManager = (FingerprintManager) context2.getSystemService("fingerprint");
            this.mFpm = fingerprintManager;
            this.mFingerprintSensorProperties = fingerprintManager.getSensorPropertiesInternal();
        }
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.biometrics.face")) {
            FaceManager faceManager = (FaceManager) context2.getSystemService("face");
            this.mFaceManager = faceManager;
            this.mFaceSensorProperties = faceManager.getSensorPropertiesInternal();
        }
        if (!(this.mFpm == null && this.mFaceManager == null)) {
            BiometricManager biometricManager = (BiometricManager) context2.getSystemService(BiometricManager.class);
            this.mBiometricManager = biometricManager;
            biometricManager.registerEnabledOnKeyguardCallback(this.mBiometricEnabledCallback);
        }
        final Executor executor4 = executor2;
        this.mAuthController.addCallback(new AuthController.Callback() {
            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onAllAuthenticatorsRegistered$0$com-android-keyguard-KeyguardUpdateMonitor$15 */
            public /* synthetic */ void mo26320x7e44b89() {
                KeyguardUpdateMonitor.this.updateBiometricListeningState(2);
            }

            public void onAllAuthenticatorsRegistered() {
                executor4.execute(new KeyguardUpdateMonitor$15$$ExternalSyntheticLambda1(this));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onEnrollmentsChanged$1$com-android-keyguard-KeyguardUpdateMonitor$15 */
            public /* synthetic */ void mo26321x878f1f44() {
                KeyguardUpdateMonitor.this.updateBiometricListeningState(2);
            }

            public void onEnrollmentsChanged() {
                executor4.execute(new KeyguardUpdateMonitor$15$$ExternalSyntheticLambda0(this));
            }
        });
        updateBiometricListeningState(2);
        FingerprintManager fingerprintManager2 = this.mFpm;
        if (fingerprintManager2 != null) {
            fingerprintManager2.addLockoutResetCallback(this.mFingerprintLockoutResetCallback);
        }
        FaceManager faceManager2 = this.mFaceManager;
        if (faceManager2 != null) {
            faceManager2.addLockoutResetCallback(this.mFaceLockoutResetCallback);
        }
        this.mIsAutomotive = isAutomotive();
        TaskStackChangeListeners.getInstance().registerTaskStackListener(this.mTaskStackListener);
        UserManager userManager = (UserManager) context2.getSystemService(UserManager.class);
        this.mUserManager = userManager;
        this.mIsPrimaryUser = userManager.isPrimaryUser();
        int currentUser = ActivityManager.getCurrentUser();
        this.mUserIsUnlocked.put(currentUser, this.mUserManager.isUserUnlocked(currentUser));
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context2.getSystemService(DevicePolicyManager.class);
        this.mDevicePolicyManager = devicePolicyManager;
        this.mLogoutEnabled = devicePolicyManager.isLogoutEnabled();
        updateSecondaryLockscreenRequirement(currentUser);
        for (UserInfo userInfo : this.mUserManager.getUsers()) {
            this.mUserTrustIsUsuallyManaged.put(userInfo.id, this.mTrustManager.isTrustUsuallyManaged(userInfo.id));
        }
        updateAirplaneModeState();
        TelephonyManager telephonyManager = (TelephonyManager) context2.getSystemService("phone");
        this.mTelephonyManager = telephonyManager;
        if (telephonyManager != null) {
            this.mTelephonyListenerManager.addActiveDataSubscriptionIdListener(this.mPhoneStateListener);
            for (int i = 0; i < this.mTelephonyManager.getActiveModemCount(); i++) {
                int simState = this.mTelephonyManager.getSimState(i);
                int[] subscriptionIds = this.mSubscriptionManager.getSubscriptionIds(i);
                if (subscriptionIds != null) {
                    for (int obtainMessage : subscriptionIds) {
                        this.mHandler.obtainMessage(304, obtainMessage, i, Integer.valueOf(simState)).sendToTarget();
                    }
                }
            }
        }
        this.mTimeFormatChangeObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean z) {
                KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(KeyguardUpdateMonitor.MSG_TIME_FORMAT_UPDATE, Settings.System.getString(KeyguardUpdateMonitor.this.mContext.getContentResolver(), "time_12_24")));
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("time_12_24"), false, this.mTimeFormatChangeObserver, -1);
        ((KeyguardUpdateMonitorEx) NTDependencyEx.get(KeyguardUpdateMonitorEx.class)).init(this.mCallbacks, this.mAuthController);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$7$com-android-keyguard-KeyguardUpdateMonitor  reason: not valid java name */
    public /* synthetic */ void m2296lambda$new$7$comandroidkeyguardKeyguardUpdateMonitor() {
        Intent registerReceiver;
        int defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
        ServiceState serviceStateForSubscriber = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).getServiceStateForSubscriber(defaultSubscriptionId);
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(MSG_SERVICE_STATE_CHANGE, defaultSubscriptionId, 0, serviceStateForSubscriber));
        if (this.mBatteryStatus == null && (registerReceiver = this.mContext.registerReceiver((BroadcastReceiver) null, new IntentFilter("android.intent.action.BATTERY_CHANGED"))) != null && this.mBatteryStatus == null) {
            this.mBroadcastReceiver.onReceive(this.mContext, registerReceiver);
        }
    }

    private void updateFaceEnrolled(int i) {
        this.mIsFaceEnrolled = ((Boolean) DejankUtils.whitelistIpcs(new KeyguardUpdateMonitor$$ExternalSyntheticLambda1(this, i))).booleanValue();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateFaceEnrolled$8$com-android-keyguard-KeyguardUpdateMonitor */
    public /* synthetic */ Boolean mo26260xb00e8296(int i) {
        FaceManager faceManager = this.mFaceManager;
        return Boolean.valueOf(faceManager != null && faceManager.isHardwareDetected() && this.mFaceManager.hasEnrolledTemplates(i) && this.mBiometricEnabledForUser.get(i));
    }

    public boolean isUdfpsEnrolled() {
        return this.mAuthController.isUdfpsEnrolled(getCurrentUser());
    }

    public boolean isUdfpsSupported() {
        return this.mAuthController.getUdfpsProps() != null && !this.mAuthController.getUdfpsProps().isEmpty();
    }

    public boolean isFaceEnrolled() {
        return this.mIsFaceEnrolled;
    }

    private void updateAirplaneModeState() {
        if (WirelessUtils.isAirplaneModeOn(this.mContext) && !this.mHandler.hasMessages(MSG_AIRPLANE_MODE_CHANGED)) {
            this.mHandler.sendEmptyMessage(MSG_AIRPLANE_MODE_CHANGED);
        }
    }

    /* access modifiers changed from: private */
    public void updateBiometricListeningState(int i) {
        updateFingerprintListeningState(i);
        updateFaceListeningState(i);
    }

    /* access modifiers changed from: private */
    public void updateFingerprintListeningState(int i) {
        if (!this.mHandler.hasMessages(MSG_BIOMETRIC_AUTHENTICATION_CONTINUE) && this.mAuthController.areAllFingerprintAuthenticatorsRegistered()) {
            boolean shouldListenForFingerprint = shouldListenForFingerprint(isUdfpsSupported());
            int i2 = this.mFingerprintRunningState;
            boolean z = i2 == 1 || i2 == 3;
            if (!z || shouldListenForFingerprint) {
                if (!z && shouldListenForFingerprint) {
                    if (i == 1) {
                        Log.v(TAG, "Ignoring startListeningForFingerprint()");
                    } else {
                        startListeningForFingerprint();
                    }
                }
            } else if (i == 0) {
                Log.v(TAG, "Ignoring stopListeningForFingerprint()");
            } else {
                stopListeningForFingerprint();
            }
        }
    }

    public boolean isUserUnlocked(int i) {
        return this.mUserIsUnlocked.get(i);
    }

    public void onAuthInterruptDetected(boolean z) {
        if (DEBUG) {
            Log.d(TAG, "onAuthInterruptDetected(" + z + NavigationBarInflaterView.KEY_CODE_END);
        }
        if (this.mAuthInterruptActive != z) {
            this.mAuthInterruptActive = z;
            updateFaceListeningState(2);
            requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.WAKE, "onReach");
        }
    }

    public void requestFaceAuth(boolean z) {
        if (DEBUG) {
            Log.d(TAG, "requestFaceAuth() userInitiated=" + z);
        }
        this.mIsFaceAuthUserRequested = z | this.mIsFaceAuthUserRequested;
        updateFaceListeningState(0);
    }

    public boolean isFaceAuthUserRequested() {
        return this.mIsFaceAuthUserRequested;
    }

    public void cancelFaceAuth() {
        stopListeningForFace();
    }

    /* access modifiers changed from: private */
    public void updateFaceListeningState(int i) {
        updateFaceRecognitionState();
        if (!this.mHandler.hasMessages(MSG_BIOMETRIC_AUTHENTICATION_CONTINUE)) {
            this.mHandler.removeCallbacks(this.mRetryFaceAuthentication);
            boolean shouldListenForFace = shouldListenForFace();
            int i2 = this.mFaceRunningState;
            if (i2 != 1 || shouldListenForFace) {
                if (i2 != 1 && shouldListenForFace) {
                    if (i == 1) {
                        Log.v(TAG, "Ignoring startListeningForFace()");
                    } else {
                        startListeningForFace();
                    }
                }
            } else if (i == 0) {
                Log.v(TAG, "Ignoring stopListeningForFace()");
            } else {
                this.mIsFaceAuthUserRequested = false;
                stopListeningForFace();
            }
        }
    }

    private void initiateActiveUnlock(String str) {
        if (!this.mHandler.hasMessages(MSG_BIOMETRIC_AUTHENTICATION_CONTINUE) && shouldTriggerActiveUnlock()) {
            if (DEBUG_ACTIVE_UNLOCK) {
                Log.d("ActiveUnlock", "initiate active unlock triggerReason=" + str);
            }
            this.mTrustManager.reportUserMayRequestUnlock(getCurrentUser());
        }
    }

    private void requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN active_unlock_request_origin, String str, boolean z) {
        if (!this.mHandler.hasMessages(MSG_BIOMETRIC_AUTHENTICATION_CONTINUE)) {
            boolean shouldAllowActiveUnlockFromOrigin = this.mActiveUnlockConfig.shouldAllowActiveUnlockFromOrigin(active_unlock_request_origin);
            if (active_unlock_request_origin == ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.WAKE && !shouldAllowActiveUnlockFromOrigin && this.mActiveUnlockConfig.isActiveUnlockEnabled()) {
                initiateActiveUnlock(str);
            } else if (shouldAllowActiveUnlockFromOrigin && shouldTriggerActiveUnlock()) {
                if (DEBUG_ACTIVE_UNLOCK) {
                    Log.d("ActiveUnlock", "reportUserRequestedUnlock origin=" + active_unlock_request_origin.name() + " reason=" + str + " dismissKeyguard=" + z);
                }
                this.mTrustManager.reportUserRequestedUnlock(getCurrentUser(), z);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0008, code lost:
        r0 = r3.mKeyguardBypassController;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void requestActiveUnlock(com.android.keyguard.ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN r4, java.lang.String r5) {
        /*
            r3 = this;
            boolean r0 = r3.isFaceEnrolled()
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0014
            com.android.systemui.statusbar.phone.KeyguardBypassController r0 = r3.mKeyguardBypassController
            if (r0 == 0) goto L_0x0014
            boolean r0 = r0.canBypass()
            if (r0 == 0) goto L_0x0014
            r0 = r1
            goto L_0x0015
        L_0x0014:
            r0 = r2
        L_0x0015:
            if (r0 != 0) goto L_0x0029
            boolean r0 = r3.mUdfpsBouncerShowing
            if (r0 != 0) goto L_0x0029
            boolean r0 = r3.mBouncerFullyShown
            if (r0 != 0) goto L_0x0029
            com.android.systemui.biometrics.AuthController r0 = r3.mAuthController
            boolean r0 = r0.isUdfpsFingerDown()
            if (r0 == 0) goto L_0x0028
            goto L_0x0029
        L_0x0028:
            r1 = r2
        L_0x0029:
            r3.requestActiveUnlock(r4, r5, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardUpdateMonitor.requestActiveUnlock(com.android.keyguard.ActiveUnlockConfig$ACTIVE_UNLOCK_REQUEST_ORIGIN, java.lang.String):void");
    }

    public void setUdfpsBouncerShowing(boolean z) {
        this.mUdfpsBouncerShowing = z;
        if (z) {
            updateFaceListeningState(0);
            requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.UNLOCK_INTENT, "udfpsBouncer");
        }
    }

    private boolean shouldTriggerActiveUnlock() {
        boolean shouldTriggerActiveUnlockForAssistant = shouldTriggerActiveUnlockForAssistant();
        boolean z = this.mBouncerFullyShown || this.mUdfpsBouncerShowing || (this.mKeyguardIsVisible && !this.mGoingToSleep && this.mStatusBarState != 2);
        int currentUser = getCurrentUser();
        boolean z2 = getUserCanSkipBouncer(currentUser) || !this.mLockPatternUtils.isSecure(currentUser);
        boolean z3 = this.mFingerprintLockedOut || this.mFingerprintLockedOutPermanent;
        int strongAuthForUser = this.mStrongAuthTracker.getStrongAuthForUser(currentUser);
        boolean z4 = containsFlag(strongAuthForUser, 2) || containsFlag(strongAuthForUser, 32);
        boolean z5 = containsFlag(strongAuthForUser, 1) || containsFlag(strongAuthForUser, 16);
        boolean z6 = (this.mAuthInterruptActive || shouldTriggerActiveUnlockForAssistant || z) && !this.mSwitchingUser && !z2 && !z3 && !z4 && !z5 && !this.mKeyguardGoingAway && !this.mSecureCameraLaunched;
        maybeLogListenerModelData(new KeyguardActiveUnlockModel(System.currentTimeMillis(), currentUser, z6, z, this.mAuthInterruptActive, z5, z3, z4, this.mSwitchingUser, shouldTriggerActiveUnlockForAssistant, z2));
        return z6;
    }

    private boolean shouldListenForFingerprintAssistant() {
        BiometricAuthenticated biometricAuthenticated = this.mUserFingerprintAuthenticated.get(getCurrentUser());
        if (!this.mAssistantVisible || !this.mKeyguardOccluded) {
            return false;
        }
        if ((biometricAuthenticated == null || !biometricAuthenticated.mAuthenticated) && !this.mUserHasTrust.get(getCurrentUser(), false)) {
            return true;
        }
        return false;
    }

    private boolean shouldListenForFaceAssistant() {
        BiometricAuthenticated biometricAuthenticated = this.mUserFaceAuthenticated.get(getCurrentUser());
        if (!this.mAssistantVisible || !this.mKeyguardOccluded) {
            return false;
        }
        if ((biometricAuthenticated == null || !biometricAuthenticated.mAuthenticated) && !this.mUserHasTrust.get(getCurrentUser(), false)) {
            return true;
        }
        return false;
    }

    private boolean shouldTriggerActiveUnlockForAssistant() {
        if (!this.mAssistantVisible || !this.mKeyguardOccluded || this.mUserHasTrust.get(getCurrentUser(), false)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0029, code lost:
        r1 = r0.mKeyguardOccluded;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean shouldListenForFingerprint(boolean r30) {
        /*
            r29 = this;
            r0 = r29
            r13 = r30
            int r4 = getCurrentUser()
            boolean r1 = r0.getUserHasTrust(r4)
            r2 = 1
            r12 = r1 ^ 1
            boolean r8 = r29.shouldListenForFingerprintAssistant()
            boolean r1 = r0.mKeyguardIsVisible
            if (r1 != 0) goto L_0x003e
            boolean r1 = r0.mDeviceInteractive
            if (r1 == 0) goto L_0x003e
            boolean r1 = r0.mBouncerIsOrWillBeShowing
            if (r1 == 0) goto L_0x0023
            boolean r1 = r0.mKeyguardGoingAway
            if (r1 == 0) goto L_0x003e
        L_0x0023:
            boolean r1 = r0.mGoingToSleep
            if (r1 != 0) goto L_0x003e
            if (r8 != 0) goto L_0x003e
            boolean r1 = r0.mKeyguardOccluded
            if (r1 == 0) goto L_0x0031
            boolean r5 = r0.mIsDreaming
            if (r5 != 0) goto L_0x003e
        L_0x0031:
            if (r1 == 0) goto L_0x003c
            if (r12 == 0) goto L_0x003c
            boolean r1 = r0.mOccludingAppRequestingFp
            if (r1 != 0) goto L_0x003e
            if (r13 == 0) goto L_0x003c
            goto L_0x003e
        L_0x003c:
            r1 = 0
            goto L_0x003f
        L_0x003e:
            r1 = r2
        L_0x003f:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r6 = "shouldListenKeyguardState:"
            r5.<init>((java.lang.String) r6)
            java.lang.StringBuilder r5 = r5.append((boolean) r1)
            java.lang.String r6 = "， mKeyguardIsVisible="
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)
            boolean r6 = r0.mKeyguardIsVisible
            java.lang.StringBuilder r5 = r5.append((boolean) r6)
            java.lang.String r6 = "，mDeviceInteractive="
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)
            boolean r6 = r0.mDeviceInteractive
            java.lang.StringBuilder r5 = r5.append((boolean) r6)
            java.lang.String r6 = ", mBouncer="
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)
            boolean r7 = r0.mBouncerFullyShown
            java.lang.StringBuilder r5 = r5.append((boolean) r7)
            java.lang.String r7 = ", mBouncerIsOrWillBeShowing="
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r7)
            boolean r9 = r0.mBouncerIsOrWillBeShowing
            java.lang.StringBuilder r5 = r5.append((boolean) r9)
            java.lang.String r9 = ", mKeyguardGoingAway="
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r9)
            boolean r10 = r0.mKeyguardGoingAway
            java.lang.StringBuilder r5 = r5.append((boolean) r10)
            java.lang.String r10 = ", mGoingToSleep="
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r10)
            boolean r10 = r0.mGoingToSleep
            java.lang.StringBuilder r5 = r5.append((boolean) r10)
            java.lang.String r10 = ", shouldListenForFingerprintAssistant="
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r10)
            java.lang.StringBuilder r5 = r5.append((boolean) r8)
            java.lang.String r10 = ", mKeyguardOccluded="
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r10)
            boolean r10 = r0.mKeyguardOccluded
            java.lang.StringBuilder r5 = r5.append((boolean) r10)
            java.lang.String r10 = ", mIsDreaming="
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r10)
            boolean r10 = r0.mIsDreaming
            java.lang.StringBuilder r5 = r5.append((boolean) r10)
            java.lang.String r10 = " userDoesNotHaveTrust:"
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r10)
            java.lang.StringBuilder r5 = r5.append((boolean) r12)
            java.lang.String r10 = ", userDoesNotHaveTrust, mOccludingAppRequestingFp="
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r10)
            boolean r10 = r0.mOccludingAppRequestingFp
            java.lang.StringBuilder r5 = r5.append((boolean) r10)
            java.lang.String r10 = ", isUdfps="
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r10)
            java.lang.StringBuilder r5 = r5.append((boolean) r13)
            java.lang.String r5 = r5.toString()
            java.lang.String r11 = "KeyguardUpdateMonitor"
            android.util.Log.i(r11, r5)
            android.util.SparseBooleanArray r5 = r0.mBiometricEnabledForUser
            boolean r5 = r5.get(r4)
            boolean r15 = r0.getUserCanSkipBouncer(r4)
            boolean r14 = r0.isFingerprintDisabled(r4)
            boolean r2 = r0.mSwitchingUser
            if (r2 != 0) goto L_0x0104
            if (r14 != 0) goto L_0x0104
            boolean r2 = r0.mKeyguardGoingAway
            if (r2 == 0) goto L_0x00fc
            boolean r2 = r0.mDeviceInteractive
            if (r2 != 0) goto L_0x0104
        L_0x00fc:
            boolean r2 = r0.mIsPrimaryUser
            if (r2 == 0) goto L_0x0104
            if (r5 == 0) goto L_0x0104
            r2 = 1
            goto L_0x0105
        L_0x0104:
            r2 = 0
        L_0x0105:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r21 = r8
            java.lang.String r8 = "shouldListenUserState:"
            r3.<init>((java.lang.String) r8)
            java.lang.StringBuilder r3 = r3.append((boolean) r2)
            java.lang.String r8 = ", mSwitchingUser="
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r8)
            boolean r8 = r0.mSwitchingUser
            java.lang.StringBuilder r3 = r3.append((boolean) r8)
            java.lang.String r8 = ", fingerprintDisabledForUser="
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r8)
            java.lang.StringBuilder r3 = r3.append((boolean) r14)
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r9)
            boolean r8 = r0.mKeyguardGoingAway
            java.lang.StringBuilder r3 = r3.append((boolean) r8)
            java.lang.String r8 = ", mDeviceInteractive="
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r8)
            boolean r8 = r0.mDeviceInteractive
            java.lang.StringBuilder r3 = r3.append((boolean) r8)
            java.lang.String r8 = ", mIsPrimaryUser="
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r8)
            boolean r8 = r0.mIsPrimaryUser
            java.lang.StringBuilder r3 = r3.append((boolean) r8)
            java.lang.String r8 = ", biometricEnabledForUser="
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r8)
            java.lang.StringBuilder r3 = r3.append((boolean) r5)
            java.lang.String r3 = r3.toString()
            android.util.Log.i(r11, r3)
            boolean r3 = r0.mFingerprintLockedOut
            if (r3 == 0) goto L_0x016b
            boolean r3 = r0.mBouncerIsOrWillBeShowing
            if (r3 == 0) goto L_0x016b
            boolean r3 = r0.mCredentialAttempted
            if (r3 != 0) goto L_0x0169
            goto L_0x016b
        L_0x0169:
            r3 = 0
            goto L_0x016c
        L_0x016b:
            r3 = 1
        L_0x016c:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r9 = "shouldListenBouncerState:"
            r8.<init>((java.lang.String) r9)
            java.lang.StringBuilder r8 = r8.append((boolean) r3)
            java.lang.String r9 = ", mFingerprintLockedOut="
            java.lang.StringBuilder r8 = r8.append((java.lang.String) r9)
            r23 = r5
            boolean r5 = r0.mFingerprintLockedOut
            java.lang.StringBuilder r5 = r8.append((boolean) r5)
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)
            boolean r6 = r0.mBouncerFullyShown
            java.lang.StringBuilder r5 = r5.append((boolean) r6)
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r7)
            boolean r6 = r0.mBouncerIsOrWillBeShowing
            java.lang.StringBuilder r5 = r5.append((boolean) r6)
            java.lang.String r6 = ", mCredentialAttempted="
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r6)
            boolean r6 = r0.mCredentialAttempted
            java.lang.StringBuilder r5 = r5.append((boolean) r6)
            java.lang.String r5 = r5.toString()
            android.util.Log.i(r11, r5)
            boolean r8 = r0.isEncryptedOrLockdown(r4)
            if (r13 == 0) goto L_0x01bc
            if (r15 != 0) goto L_0x01ba
            if (r8 != 0) goto L_0x01ba
            if (r12 == 0) goto L_0x01ba
            goto L_0x01bc
        L_0x01ba:
            r5 = 0
            goto L_0x01bd
        L_0x01bc:
            r5 = 1
        L_0x01bd:
            if (r1 == 0) goto L_0x01d5
            if (r2 == 0) goto L_0x01d5
            if (r3 == 0) goto L_0x01d5
            if (r5 == 0) goto L_0x01d5
            boolean r1 = r29.isFingerprintLockedOut()
            if (r1 != 0) goto L_0x01d5
            boolean r1 = r0.mFaceRecognitionSucceeded
            if (r1 != 0) goto L_0x01d5
            boolean r1 = r0.mShowing
            if (r1 == 0) goto L_0x01d5
            r6 = 1
            goto L_0x01d6
        L_0x01d5:
            r6 = 0
        L_0x01d6:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "shouldListen: "
            r1.<init>((java.lang.String) r2)
            java.lang.StringBuilder r1 = r1.append((boolean) r6)
            java.lang.String r2 = ", shouldListenUdfpsState:"
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)
            java.lang.StringBuilder r1 = r1.append((boolean) r5)
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r10)
            java.lang.StringBuilder r1 = r1.append((boolean) r13)
            java.lang.String r2 = ", userCanSkipBouncer="
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)
            java.lang.StringBuilder r1 = r1.append((boolean) r15)
            java.lang.String r2 = ", isEncryptedOrLockdownForUser="
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)
            java.lang.StringBuilder r1 = r1.append((boolean) r8)
            java.lang.String r2 = ", userDoesNotHaveTrust="
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)
            java.lang.StringBuilder r1 = r1.append((boolean) r12)
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r9)
            boolean r2 = r29.isFingerprintLockedOut()
            java.lang.StringBuilder r1 = r1.append((boolean) r2)
            java.lang.String r2 = ", mFaceRecognitionSucceeded="
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)
            boolean r2 = r0.mFaceRecognitionSucceeded
            java.lang.StringBuilder r1 = r1.append((boolean) r2)
            java.lang.String r2 = ", mShowing="
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r2)
            boolean r2 = r0.mShowing
            java.lang.StringBuilder r1 = r1.append((boolean) r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.i(r11, r1)
            boolean r1 = DEBUG_FINGERPRINT
            if (r1 != 0) goto L_0x0244
            r28 = r6
            goto L_0x0292
        L_0x0244:
            com.android.keyguard.KeyguardFingerprintListenModel r5 = new com.android.keyguard.KeyguardFingerprintListenModel
            r1 = r5
            long r2 = java.lang.System.currentTimeMillis()
            boolean r7 = r0.mBouncerIsOrWillBeShowing
            boolean r9 = r0.mCredentialAttempted
            boolean r10 = r0.mDeviceInteractive
            boolean r11 = r0.mIsDreaming
            r24 = r5
            boolean r5 = r0.mFingerprintLockedOut
            r25 = r14
            r14 = r5
            boolean r5 = r0.mGoingToSleep
            r26 = r15
            r15 = r5
            boolean r5 = r0.mKeyguardGoingAway
            r16 = r5
            boolean r5 = r0.mKeyguardIsVisible
            r17 = r5
            boolean r5 = r0.mKeyguardOccluded
            r18 = r5
            boolean r5 = r0.mOccludingAppRequestingFp
            r19 = r5
            boolean r5 = r0.mIsPrimaryUser
            r20 = r5
            boolean r5 = r0.mSwitchingUser
            r22 = r5
            r27 = r24
            r5 = r6
            r28 = r6
            r6 = r23
            r23 = r8
            r8 = r26
            r24 = r12
            r12 = r23
            r13 = r25
            r23 = r30
            r1.<init>(r2, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24)
            r1 = r27
            r0.maybeLogListenerModelData(r1)
        L_0x0292:
            return r28
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardUpdateMonitor.shouldListenForFingerprint(boolean):boolean");
    }

    public boolean shouldListenForFace() {
        boolean z = false;
        if (this.mFaceManager == null) {
            return false;
        }
        boolean z2 = this.mKeyguardIsVisible && this.mDeviceInteractive && !this.mGoingToSleep && !(this.mStatusBarState == 2);
        int currentUser = getCurrentUser();
        int strongAuthForUser = this.mStrongAuthTracker.getStrongAuthForUser(currentUser);
        boolean z3 = containsFlag(strongAuthForUser, 2) || containsFlag(strongAuthForUser, 32);
        boolean z4 = containsFlag(strongAuthForUser, 1) || containsFlag(strongAuthForUser, 16);
        boolean z5 = this.mFingerprintLockedOut || this.mFingerprintLockedOutPermanent;
        KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
        boolean z6 = keyguardBypassController != null && keyguardBypassController.canBypass();
        boolean z7 = !getUserCanSkipBouncer(currentUser) || z6;
        boolean z8 = (!z3 || (!this.mFaceSensorProperties.isEmpty() && z6 && this.mFaceSensorProperties.get(0).supportsFaceDetection)) ? !z4 || (z6 && !this.mBouncerFullyShown) : false;
        boolean isFaceAuthenticated = getIsFaceAuthenticated();
        boolean isFaceDisabled = isFaceDisabled(currentUser);
        boolean z9 = this.mBiometricEnabledForUser.get(currentUser);
        boolean shouldListenForFaceAssistant = shouldListenForFaceAssistant();
        if (((this.mBouncerFullyShown && !this.mGoingToSleep) || this.mAuthInterruptActive || this.mOccludingAppRequestingFace || z2 || shouldListenForFaceAssistant || this.mAuthController.isUdfpsFingerDown() || this.mUdfpsBouncerShowing) && !this.mSwitchingUser && !isFaceDisabled && z7 && !this.mKeyguardGoingAway && z9 && !this.mLockIconPressed && z8 && this.mIsPrimaryUser && ((!this.mSecureCameraLaunched || this.mOccludingAppRequestingFace) && !isFaceAuthenticated && !z5)) {
            z = true;
        }
        if (DEBUG_FACE) {
            maybeLogListenerModelData(new KeyguardFaceListenModel(System.currentTimeMillis(), currentUser, z, this.mAuthInterruptActive, z7, z9, this.mBouncerFullyShown, isFaceAuthenticated, isFaceDisabled, this.mGoingToSleep, z2, this.mKeyguardGoingAway, shouldListenForFaceAssistant, this.mLockIconPressed, this.mOccludingAppRequestingFace, this.mIsPrimaryUser, z8, this.mSecureCameraLaunched, this.mSwitchingUser, this.mUdfpsBouncerShowing));
        }
        return z;
    }

    private void maybeLogListenerModelData(KeyguardListenModel keyguardListenModel) {
        if (!DEBUG_ACTIVE_UNLOCK || !(keyguardListenModel instanceof KeyguardActiveUnlockModel)) {
            boolean z = DEBUG_FACE;
            boolean z2 = false;
            boolean z3 = (z && (keyguardListenModel instanceof KeyguardFaceListenModel) && this.mFaceRunningState != 1) || (DEBUG_FINGERPRINT && (keyguardListenModel instanceof KeyguardFingerprintListenModel) && this.mFingerprintRunningState != 1);
            if ((z && (keyguardListenModel instanceof KeyguardFaceListenModel) && this.mFaceRunningState == 1) || (DEBUG_FINGERPRINT && (keyguardListenModel instanceof KeyguardFingerprintListenModel) && this.mFingerprintRunningState == 1)) {
                z2 = true;
            }
            if ((z3 && keyguardListenModel.getListening()) || (z2 && !keyguardListenModel.getListening())) {
                this.mListenModels.add(keyguardListenModel);
                return;
            }
            return;
        }
        this.mListenModels.add(keyguardListenModel);
    }

    public void onLockIconPressed() {
        this.mLockIconPressed = true;
        int currentUser = getCurrentUser();
        this.mUserFaceAuthenticated.put(currentUser, (Object) null);
        updateFaceListeningState(2);
        this.mStrongAuthTracker.onStrongAuthRequiredChanged(currentUser);
    }

    private void startListeningForFingerprint() {
        int currentUser = getCurrentUser();
        boolean isUnlockWithFingerprintPossible = isUnlockWithFingerprintPossible(currentUser);
        if (this.mFingerprintCancelSignal != null) {
            Log.e(TAG, "Cancellation signal is not null, high chance of bug in fp auth lifecycle management. FP state: " + this.mFingerprintRunningState + ", unlockPossible: " + isUnlockWithFingerprintPossible);
        }
        int i = this.mFingerprintRunningState;
        if (i == 2) {
            setFingerprintRunningState(3);
        } else if (i != 3) {
            if (DEBUG) {
                Log.v(TAG, "startListeningForFingerprint()");
            }
            if (isUnlockWithFingerprintPossible) {
                this.mFingerprintCancelSignal = new CancellationSignal();
                if (isEncryptedOrLockdown(currentUser)) {
                    this.mFpm.detectFingerprint(this.mFingerprintCancelSignal, this.mFingerprintDetectionCallback, currentUser);
                } else {
                    this.mFpm.authenticate((FingerprintManager.CryptoObject) null, this.mFingerprintCancelSignal, this.mFingerprintAuthenticationCallback, (Handler) null, -1, currentUser, 0);
                }
                setFingerprintRunningState(1);
            }
        }
    }

    private void startListeningForFace() {
        int currentUser = getCurrentUser();
        boolean isUnlockWithFacePossible = isUnlockWithFacePossible(currentUser);
        if (this.mFaceCancelSignal != null) {
            Log.e(TAG, "Cancellation signal is not null, high chance of bug in face auth lifecycle management. Face state: " + this.mFaceRunningState + ", unlockPossible: " + isUnlockWithFacePossible);
        }
        int i = this.mFaceRunningState;
        if (i == 2) {
            setFaceRunningState(3);
        } else if (i != 3) {
            if (DEBUG) {
                Log.v(TAG, "startListeningForFace(): " + this.mFaceRunningState);
            }
            if (isUnlockWithFacePossible) {
                this.mFaceCancelSignal = new CancellationSignal();
                boolean z = !this.mFaceSensorProperties.isEmpty() && this.mFaceSensorProperties.get(0).supportsFaceDetection;
                if (!isEncryptedOrLockdown(currentUser) || !z) {
                    KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
                    this.mFaceManager.authenticate((CryptoObject) null, this.mFaceCancelSignal, this.mFaceAuthenticationCallback, (Handler) null, currentUser, keyguardBypassController != null && keyguardBypassController.isBypassEnabled());
                } else {
                    this.mFaceManager.detectFace(this.mFaceCancelSignal, this.mFaceDetectionCallback, currentUser);
                }
                setFaceRunningState(1);
            }
        }
    }

    public boolean isFingerprintLockedOut() {
        return this.mFingerprintLockedOut || this.mFingerprintLockedOutPermanent;
    }

    public boolean isFaceLockedOut() {
        return this.mFaceLockedOutPermanent;
    }

    public boolean isUnlockingWithBiometricsPossible(int i) {
        return isUnlockWithFacePossible(i) || isUnlockWithFingerprintPossible(i) || isFaceRecognitionEnable();
    }

    private boolean isUnlockWithFingerprintPossible(int i) {
        HashMap<Integer, Boolean> hashMap = this.mIsUnlockWithFingerprintPossible;
        Integer valueOf = Integer.valueOf(i);
        FingerprintManager fingerprintManager = this.mFpm;
        hashMap.put(valueOf, Boolean.valueOf(fingerprintManager != null && fingerprintManager.isHardwareDetected() && !isFingerprintDisabled(i) && this.mFpm.hasEnrolledTemplates(i)));
        return this.mIsUnlockWithFingerprintPossible.get(Integer.valueOf(i)).booleanValue();
    }

    public boolean getCachedIsUnlockWithFingerprintPossible(int i) {
        return this.mIsUnlockWithFingerprintPossible.getOrDefault(Integer.valueOf(i), false).booleanValue();
    }

    private boolean isUnlockWithFacePossible(int i) {
        return isFaceAuthEnabledForUser(i) && !isFaceDisabled(i);
    }

    public boolean isFaceAuthEnabledForUser(int i) {
        updateFaceEnrolled(i);
        return this.mIsFaceEnrolled;
    }

    private void stopListeningForFingerprint() {
        if (DEBUG) {
            Log.v(TAG, "stopListeningForFingerprint()");
        }
        if (this.mFingerprintRunningState == 1) {
            CancellationSignal cancellationSignal = this.mFingerprintCancelSignal;
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
                this.mFingerprintCancelSignal = null;
                this.mHandler.removeCallbacks(this.mFpCancelNotReceived);
                this.mHandler.postDelayed(this.mFpCancelNotReceived, 3000);
            }
            setFingerprintRunningState(2);
        }
        if (this.mFingerprintRunningState == 3) {
            setFingerprintRunningState(2);
        }
    }

    private void stopListeningForFace() {
        if (DEBUG) {
            Log.v(TAG, "stopListeningForFace()");
        }
        if (this.mFaceRunningState == 1) {
            CancellationSignal cancellationSignal = this.mFaceCancelSignal;
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
                this.mFaceCancelSignal = null;
                this.mHandler.removeCallbacks(this.mFaceCancelNotReceived);
                this.mHandler.postDelayed(this.mFaceCancelNotReceived, 3000);
            }
            setFaceRunningState(2);
        }
        if (this.mFaceRunningState == 3) {
            setFaceRunningState(2);
        }
    }

    /* access modifiers changed from: private */
    public boolean isDeviceProvisionedInSettingsDb() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), WizardManagerHelper.SETTINGS_GLOBAL_DEVICE_PROVISIONED, 0) != 0;
    }

    private void watchForDeviceProvisioning() {
        this.mDeviceProvisionedObserver = new ContentObserver(this.mHandler) {
            public void onChange(boolean z) {
                super.onChange(z);
                KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
                boolean unused = keyguardUpdateMonitor.mDeviceProvisioned = keyguardUpdateMonitor.isDeviceProvisionedInSettingsDb();
                if (KeyguardUpdateMonitor.this.mDeviceProvisioned) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(308);
                }
                if (KeyguardUpdateMonitor.DEBUG) {
                    Log.d(KeyguardUpdateMonitor.TAG, "DEVICE_PROVISIONED state = " + KeyguardUpdateMonitor.this.mDeviceProvisioned);
                }
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor(WizardManagerHelper.SETTINGS_GLOBAL_DEVICE_PROVISIONED), false, this.mDeviceProvisionedObserver);
        boolean isDeviceProvisionedInSettingsDb = isDeviceProvisionedInSettingsDb();
        if (isDeviceProvisionedInSettingsDb != this.mDeviceProvisioned) {
            this.mDeviceProvisioned = isDeviceProvisionedInSettingsDb;
            if (isDeviceProvisionedInSettingsDb) {
                this.mHandler.sendEmptyMessage(308);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleDevicePolicyManagerStateChanged(int i) {
        Assert.isMainThread();
        updateFingerprintListeningState(2);
        updateSecondaryLockscreenRequirement(i);
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onDevicePolicyManagerStateChanged();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void handleUserSwitching(int i, IRemoteCallback iRemoteCallback) {
        Assert.isMainThread();
        clearBiometricRecognized();
        this.mUserTrustIsUsuallyManaged.put(i, this.mTrustManager.isTrustUsuallyManaged(i));
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onUserSwitching(i);
            }
        }
        try {
            iRemoteCallback.sendResult((Bundle) null);
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public void handleUserSwitchComplete(int i) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onUserSwitchComplete(i);
            }
        }
        if (this.mFaceManager != null && !this.mFaceSensorProperties.isEmpty()) {
            handleFaceLockoutReset(this.mFaceManager.getLockoutModeForUser(this.mFaceSensorProperties.get(0).sensorId, i));
        }
        if (this.mFpm != null && !this.mFingerprintSensorProperties.isEmpty()) {
            handleFingerprintLockoutReset(this.mFpm.getLockoutModeForUser(this.mFingerprintSensorProperties.get(0).sensorId, i));
        }
        this.mInteractionJankMonitor.end(37);
        this.mLatencyTracker.onActionEnd(12);
    }

    /* access modifiers changed from: private */
    public void handleDeviceProvisioned() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onDeviceProvisioned();
            }
        }
        if (this.mDeviceProvisionedObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mDeviceProvisionedObserver);
            this.mDeviceProvisionedObserver = null;
        }
    }

    /* access modifiers changed from: private */
    public void handlePhoneStateChanged(String str) {
        Assert.isMainThread();
        if (DEBUG) {
            Log.d(TAG, "handlePhoneStateChanged(" + str + NavigationBarInflaterView.KEY_CODE_END);
        }
        if (TelephonyManager.EXTRA_STATE_IDLE.equals(str)) {
            this.mPhoneState = 0;
        } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(str)) {
            this.mPhoneState = 2;
        } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(str)) {
            this.mPhoneState = 1;
        }
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onPhoneStateChanged(this.mPhoneState);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleTimeUpdate() {
        Assert.isMainThread();
        if (DEBUG) {
            Log.d(TAG, "handleTimeUpdate");
        }
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTimeChanged();
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleTimeZoneUpdate(String str) {
        Assert.isMainThread();
        if (DEBUG) {
            Log.d(TAG, "handleTimeZoneUpdate");
        }
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTimeZoneChanged(TimeZone.getTimeZone(str));
                keyguardUpdateMonitorCallback.onTimeChanged();
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleTimeFormatUpdate(String str) {
        Assert.isMainThread();
        if (DEBUG) {
            Log.d(TAG, "handleTimeFormatUpdate timeFormat=" + str);
        }
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTimeFormatChanged(str);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleBatteryUpdate(BatteryStatus batteryStatus) {
        Assert.isMainThread();
        if (DEBUG) {
            Log.d(TAG, "handleBatteryUpdate");
        }
        boolean isBatteryUpdateInteresting = isBatteryUpdateInteresting(this.mBatteryStatus, batteryStatus);
        this.mBatteryStatus = batteryStatus;
        if (isBatteryUpdateInteresting) {
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onRefreshBatteryInfo(batteryStatus);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateTelephonyCapable(boolean z) {
        Assert.isMainThread();
        if (z != this.mTelephonyCapable) {
            this.mTelephonyCapable = z;
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onTelephonyCapable(this.mTelephonyCapable);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x007a  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00ab  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleSimStateChange(int r7, int r8, int r9) {
        /*
            r6 = this;
            com.android.systemui.util.Assert.isMainThread()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "handleSimStateChange(subId="
            r0.<init>((java.lang.String) r1)
            java.lang.StringBuilder r0 = r0.append((int) r7)
            java.lang.String r1 = ", slotId="
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r1)
            java.lang.StringBuilder r0 = r0.append((int) r8)
            java.lang.String r1 = ", state="
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r1)
            java.lang.StringBuilder r0 = r0.append((int) r9)
            java.lang.String r1 = ")"
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "KeyguardUpdateMonitor"
            android.util.Log.d(r1, r0)
            boolean r0 = android.telephony.SubscriptionManager.isValidSubscriptionId(r7)
            r2 = 1
            r3 = 0
            if (r0 != 0) goto L_0x006b
            java.lang.String r0 = "invalid subId in handleSimStateChange()"
            android.util.Log.w(r1, r0)
            if (r9 != r2) goto L_0x0062
            r6.updateTelephonyCapable(r2)
            java.util.HashMap<java.lang.Integer, com.android.keyguard.KeyguardUpdateMonitor$SimData> r0 = r6.mSimDatas
            java.util.Collection r0 = r0.values()
            java.util.Iterator r0 = r0.iterator()
        L_0x004d:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x0060
            java.lang.Object r1 = r0.next()
            com.android.keyguard.KeyguardUpdateMonitor$SimData r1 = (com.android.keyguard.KeyguardUpdateMonitor.SimData) r1
            int r4 = r1.slotId
            if (r4 != r8) goto L_0x004d
            r1.simState = r2
            goto L_0x004d
        L_0x0060:
            r0 = r2
            goto L_0x006c
        L_0x0062:
            r0 = 8
            if (r9 != r0) goto L_0x006a
            r6.updateTelephonyCapable(r2)
            goto L_0x006b
        L_0x006a:
            return
        L_0x006b:
            r0 = r3
        L_0x006c:
            java.util.HashMap<java.lang.Integer, com.android.keyguard.KeyguardUpdateMonitor$SimData> r1 = r6.mSimDatas
            java.lang.Integer r4 = java.lang.Integer.valueOf((int) r8)
            java.lang.Object r1 = r1.get(r4)
            com.android.keyguard.KeyguardUpdateMonitor$SimData r1 = (com.android.keyguard.KeyguardUpdateMonitor.SimData) r1
            if (r1 != 0) goto L_0x0089
            com.android.keyguard.KeyguardUpdateMonitor$SimData r1 = new com.android.keyguard.KeyguardUpdateMonitor$SimData
            r1.<init>(r9, r8, r7)
            java.util.HashMap<java.lang.Integer, com.android.keyguard.KeyguardUpdateMonitor$SimData> r4 = r6.mSimDatas
            java.lang.Integer r5 = java.lang.Integer.valueOf((int) r8)
            r4.put(r5, r1)
            goto L_0x009d
        L_0x0089:
            int r4 = r1.simState
            if (r4 != r9) goto L_0x0097
            int r4 = r1.subId
            if (r4 != r7) goto L_0x0097
            int r4 = r1.slotId
            if (r4 == r8) goto L_0x0096
            goto L_0x0097
        L_0x0096:
            r2 = r3
        L_0x0097:
            r1.simState = r9
            r1.subId = r7
            r1.slotId = r8
        L_0x009d:
            if (r2 != 0) goto L_0x00a1
            if (r0 == 0) goto L_0x00c1
        L_0x00a1:
            if (r9 == 0) goto L_0x00c1
        L_0x00a3:
            java.util.ArrayList<java.lang.ref.WeakReference<com.android.keyguard.KeyguardUpdateMonitorCallback>> r0 = r6.mCallbacks
            int r0 = r0.size()
            if (r3 >= r0) goto L_0x00c1
            java.util.ArrayList<java.lang.ref.WeakReference<com.android.keyguard.KeyguardUpdateMonitorCallback>> r0 = r6.mCallbacks
            java.lang.Object r0 = r0.get(r3)
            java.lang.ref.WeakReference r0 = (java.lang.ref.WeakReference) r0
            java.lang.Object r0 = r0.get()
            com.android.keyguard.KeyguardUpdateMonitorCallback r0 = (com.android.keyguard.KeyguardUpdateMonitorCallback) r0
            if (r0 == 0) goto L_0x00be
            r0.onSimStateChanged(r7, r8, r9)
        L_0x00be:
            int r3 = r3 + 1
            goto L_0x00a3
        L_0x00c1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardUpdateMonitor.handleSimStateChange(int, int, int):void");
    }

    /* access modifiers changed from: package-private */
    public void handleServiceStateChange(int i, ServiceState serviceState) {
        if (DEBUG) {
            Log.d(TAG, "handleServiceStateChange(subId=" + i + ", serviceState=" + serviceState);
        }
        int i2 = 0;
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            Log.w(TAG, "invalid subId in handleServiceStateChange()");
            while (i2 < this.mCallbacks.size()) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onServiceStateChanged(i, serviceState);
                }
                i2++;
            }
            return;
        }
        updateTelephonyCapable(true);
        this.mServiceStates.put(Integer.valueOf(i), serviceState);
        while (i2 < this.mCallbacks.size()) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback2 = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback2 != null) {
                keyguardUpdateMonitorCallback2.onRefreshCarrierInfo();
                keyguardUpdateMonitorCallback2.onServiceStateChanged(i, serviceState);
            }
            i2++;
        }
    }

    public boolean isKeyguardVisible() {
        return this.mKeyguardIsVisible;
    }

    public void onKeyguardVisibilityChanged(boolean z) {
        Assert.isMainThread();
        Log.d(TAG, "onKeyguardVisibilityChanged(" + z + NavigationBarInflaterView.KEY_CODE_END);
        this.mKeyguardIsVisible = z;
        if (z) {
            this.mSecureCameraLaunched = false;
        }
        KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
        if (keyguardBypassController != null) {
            keyguardBypassController.setUserHasDeviceEntryIntent(false);
        }
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onKeyguardVisibilityChangedRaw(z);
            }
        }
        updateBiometricListeningState(2);
    }

    public void onKeyguardOccludedChanged(boolean z) {
        Assert.isMainThread();
        if (DEBUG) {
            Log.d(TAG, "onKeyguardOccludedChanged(" + z + NavigationBarInflaterView.KEY_CODE_END);
        }
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onKeyguardOccludedChanged(z);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleKeyguardReset() {
        if (DEBUG) {
            Log.d(TAG, "handleKeyguardReset");
        }
        updateBiometricListeningState(2);
        this.mNeedsSlowUnlockTransition = resolveNeedsSlowUnlockTransition();
    }

    private boolean resolveNeedsSlowUnlockTransition() {
        if (isUserUnlocked(getCurrentUser())) {
            return false;
        }
        ResolveInfo resolveActivityAsUser = this.mContext.getPackageManager().resolveActivityAsUser(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME"), 0, getCurrentUser());
        if (resolveActivityAsUser != null) {
            return FALLBACK_HOME_COMPONENT.equals(resolveActivityAsUser.getComponentInfo().getComponentName());
        }
        Log.w(TAG, "resolveNeedsSlowUnlockTransition: returning false since activity could not be resolved.");
        return false;
    }

    /* access modifiers changed from: private */
    public void handleKeyguardBouncerChanged(int i, int i2) {
        Assert.isMainThread();
        boolean z = this.mBouncerIsOrWillBeShowing;
        boolean z2 = this.mBouncerFullyShown;
        boolean z3 = true;
        this.mBouncerIsOrWillBeShowing = i == 1;
        if (i2 != 1) {
            z3 = false;
        }
        this.mBouncerFullyShown = z3;
        if (DEBUG) {
            Log.d(TAG, "handleKeyguardBouncerChanged bouncerIsOrWillBeShowing=" + this.mBouncerIsOrWillBeShowing + " bouncerFullyShowing=" + this.mBouncerFullyShown);
        }
        if (this.mBouncerFullyShown) {
            this.mSecureCameraLaunched = false;
        } else {
            this.mCredentialAttempted = false;
        }
        if (z != this.mBouncerIsOrWillBeShowing) {
            for (int i3 = 0; i3 < this.mCallbacks.size(); i3++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i3).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onKeyguardBouncerStateChanged(this.mBouncerIsOrWillBeShowing);
                }
            }
            updateFingerprintListeningState(2);
        }
        boolean z4 = this.mBouncerFullyShown;
        if (z2 != z4) {
            if (z4) {
                requestActiveUnlock(ActiveUnlockConfig.ACTIVE_UNLOCK_REQUEST_ORIGIN.UNLOCK_INTENT, "bouncerFullyShown");
            }
            for (int i4 = 0; i4 < this.mCallbacks.size(); i4++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback2 = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i4).get();
                if (keyguardUpdateMonitorCallback2 != null) {
                    keyguardUpdateMonitorCallback2.onKeyguardBouncerFullyShowingChanged(this.mBouncerFullyShown);
                }
            }
            updateFaceListeningState(2);
        }
    }

    /* access modifiers changed from: private */
    public void handleRequireUnlockForNfc() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onRequireUnlockForNfc();
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleKeyguardDismissAnimationFinished() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onKeyguardDismissAnimationFinished();
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleReportEmergencyCallAction() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onEmergencyCallAction();
            }
        }
    }

    private boolean isBatteryUpdateInteresting(BatteryStatus batteryStatus, BatteryStatus batteryStatus2) {
        boolean isPluggedIn = batteryStatus2.isPluggedIn();
        boolean isPluggedIn2 = batteryStatus.isPluggedIn();
        boolean z = isPluggedIn2 && isPluggedIn && batteryStatus.status != batteryStatus2.status;
        boolean z2 = batteryStatus2.present;
        boolean z3 = batteryStatus.present;
        if (isPluggedIn2 == isPluggedIn && !z && batteryStatus.level == batteryStatus2.level) {
            return ((!isPluggedIn || batteryStatus2.maxChargingWattage == batteryStatus.maxChargingWattage) && z3 == z2 && batteryStatus2.health == batteryStatus.health) ? false : true;
        }
        return true;
    }

    private boolean isAutomotive() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive");
    }

    public void removeCallback(KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback) {
        Assert.isMainThread();
        if (DEBUG) {
            Log.v(TAG, "*** unregister callback for " + keyguardUpdateMonitorCallback);
        }
        this.mCallbacks.removeIf(new KeyguardUpdateMonitor$$ExternalSyntheticLambda0(keyguardUpdateMonitorCallback));
    }

    static /* synthetic */ boolean lambda$removeCallback$9(KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback, WeakReference weakReference) {
        return weakReference.get() == keyguardUpdateMonitorCallback;
    }

    public void registerCallback(KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback) {
        Assert.isMainThread();
        if (DEBUG) {
            Log.v(TAG, "*** register callback for " + keyguardUpdateMonitorCallback);
        }
        int i = 0;
        while (i < this.mCallbacks.size()) {
            if (this.mCallbacks.get(i).get() != keyguardUpdateMonitorCallback) {
                i++;
            } else if (DEBUG) {
                Log.e(TAG, "Object tried to add another callback", new Exception("Called by"));
                return;
            } else {
                return;
            }
        }
        this.mCallbacks.add(new WeakReference(keyguardUpdateMonitorCallback));
        removeCallback((KeyguardUpdateMonitorCallback) null);
        sendUpdates(keyguardUpdateMonitorCallback);
    }

    public void setKeyguardBypassController(KeyguardBypassController keyguardBypassController) {
        this.mKeyguardBypassController = keyguardBypassController;
    }

    public boolean isSwitchingUser() {
        return this.mSwitchingUser;
    }

    public void setSwitchingUser(boolean z) {
        this.mSwitchingUser = z;
        this.mHandler.post(new KeyguardUpdateMonitor$$ExternalSyntheticLambda10(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setSwitchingUser$10$com-android-keyguard-KeyguardUpdateMonitor */
    public /* synthetic */ void mo26259xaf4caa43() {
        updateBiometricListeningState(2);
    }

    private void sendUpdates(KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback) {
        keyguardUpdateMonitorCallback.onRefreshBatteryInfo(this.mBatteryStatus);
        keyguardUpdateMonitorCallback.onTimeChanged();
        keyguardUpdateMonitorCallback.onPhoneStateChanged(this.mPhoneState);
        keyguardUpdateMonitorCallback.onRefreshCarrierInfo();
        keyguardUpdateMonitorCallback.onClockVisibilityChanged();
        keyguardUpdateMonitorCallback.onKeyguardOccludedChanged(this.mKeyguardOccluded);
        keyguardUpdateMonitorCallback.onKeyguardVisibilityChangedRaw(this.mKeyguardIsVisible);
        keyguardUpdateMonitorCallback.onTelephonyCapable(this.mTelephonyCapable);
        for (Map.Entry<Integer, SimData> value : this.mSimDatas.entrySet()) {
            SimData simData = (SimData) value.getValue();
            keyguardUpdateMonitorCallback.onSimStateChanged(simData.subId, simData.slotId, simData.simState);
        }
    }

    public void sendKeyguardReset() {
        this.mHandler.obtainMessage(312).sendToTarget();
    }

    public void sendKeyguardBouncerChanged(boolean z, boolean z2) {
        if (DEBUG) {
            Log.d(TAG, "sendKeyguardBouncerChanged bouncerIsOrWillBeShowing=" + z + " bouncerFullyShown=" + z2);
        }
        Message obtainMessage = this.mHandler.obtainMessage(MSG_KEYGUARD_BOUNCER_CHANGED);
        obtainMessage.arg1 = z ? 1 : 0;
        obtainMessage.arg2 = z2 ? 1 : 0;
        obtainMessage.sendToTarget();
    }

    public void reportSimUnlocked(int i) {
        Log.v(TAG, "reportSimUnlocked(subId=" + i + NavigationBarInflaterView.KEY_CODE_END);
        handleSimStateChange(i, getSlotId(i), 5);
    }

    public void reportEmergencyCallAction(boolean z) {
        if (!z) {
            this.mHandler.obtainMessage(318).sendToTarget();
            return;
        }
        Assert.isMainThread();
        handleReportEmergencyCallAction();
    }

    public boolean isDeviceProvisioned() {
        return this.mDeviceProvisioned;
    }

    public ServiceState getServiceState(int i) {
        return this.mServiceStates.get(Integer.valueOf(i));
    }

    public void clearBiometricRecognized() {
        clearBiometricRecognized(-10000);
    }

    public void clearBiometricRecognizedWhenKeyguardDone(int i) {
        clearBiometricRecognized(i);
    }

    private void clearBiometricRecognized(int i) {
        Assert.isMainThread();
        this.mUserFingerprintAuthenticated.clear();
        this.mUserFaceAuthenticated.clear();
        this.mTrustManager.clearAllBiometricRecognized(BiometricSourceType.FINGERPRINT, i);
        this.mTrustManager.clearAllBiometricRecognized(BiometricSourceType.FACE, i);
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricsCleared();
            }
        }
    }

    public boolean isSimPinVoiceSecure() {
        return isSimPinSecure();
    }

    public boolean isSimPinSecure() {
        for (SubscriptionInfo subscriptionId : getSubscriptionInfo(false)) {
            if (isSimPinSecure(getSimState(subscriptionId.getSubscriptionId()))) {
                return true;
            }
        }
        return false;
    }

    public int getSimState(int i) {
        int slotIndex = SubscriptionManager.getSlotIndex(i);
        if (this.mSimDatas.containsKey(Integer.valueOf(slotIndex))) {
            return this.mSimDatas.get(Integer.valueOf(slotIndex)).simState;
        }
        return 0;
    }

    private int getSlotId(int i) {
        int slotIndex = SubscriptionManager.getSlotIndex(i);
        if (!this.mSimDatas.containsKey(Integer.valueOf(slotIndex))) {
            refreshSimState(i, slotIndex);
        }
        return this.mSimDatas.get(Integer.valueOf(slotIndex)).slotId;
    }

    private boolean refreshSimState(int i, int i2) {
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService("phone");
        boolean z = false;
        int simState = telephonyManager != null ? telephonyManager.getSimState(i2) : 0;
        SimData simData = this.mSimDatas.get(Integer.valueOf(i2));
        if (simData == null) {
            this.mSimDatas.put(Integer.valueOf(i2), new SimData(simState, i2, i));
            return true;
        }
        if (!(simData.simState == simState && simData.slotId == i2)) {
            z = true;
        }
        simData.simState = simState;
        simData.slotId = i2;
        return z;
    }

    public DisplayClientState getCachedDisplayClientState() {
        return this.mDisplayClientState;
    }

    public void dispatchStartedWakingUp() {
        synchronized (this) {
            this.mDeviceInteractive = true;
        }
        this.mHandler.sendEmptyMessage(319);
    }

    public void dispatchStartedGoingToSleep(int i) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(MSG_STARTED_GOING_TO_SLEEP, i, 0));
    }

    public void dispatchFinishedGoingToSleep(int i) {
        synchronized (this) {
            this.mDeviceInteractive = false;
        }
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(320, i, 0));
    }

    public void dispatchScreenTurnedOff() {
        this.mHandler.sendEmptyMessage(MSG_SCREEN_TURNED_OFF);
    }

    public void dispatchDreamingStarted() {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(MSG_DREAMING_STATE_CHANGED, 1, 0));
    }

    public void dispatchDreamingStopped() {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(MSG_DREAMING_STATE_CHANGED, 0, 0));
    }

    public void dispatchKeyguardGoingAway(boolean z) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(MSG_KEYGUARD_GOING_AWAY, Boolean.valueOf(z)));
    }

    public void dispatchKeyguardDismissAnimationFinished() {
        this.mHandler.sendEmptyMessage(MSG_KEYGUARD_DISMISS_ANIMATION_FINISHED);
    }

    public boolean isDeviceInteractive() {
        return this.mDeviceInteractive;
    }

    public boolean isGoingToSleep() {
        return this.mGoingToSleep;
    }

    public int getNextSubIdForState(int i) {
        List<SubscriptionInfo> subscriptionInfo = getSubscriptionInfo(false);
        int i2 = -1;
        int i3 = Integer.MAX_VALUE;
        for (int i4 = 0; i4 < subscriptionInfo.size(); i4++) {
            int subscriptionId = subscriptionInfo.get(i4).getSubscriptionId();
            int slotId = getSlotId(subscriptionId);
            if (i == getSimState(subscriptionId) && i3 > slotId) {
                i2 = subscriptionId;
                i3 = slotId;
            }
        }
        return i2;
    }

    public int getUnlockedSubIdForState(int i) {
        List<SubscriptionInfo> subscriptionInfo = getSubscriptionInfo(false);
        for (int i2 = 0; i2 < subscriptionInfo.size(); i2++) {
            int subscriptionId = subscriptionInfo.get(i2).getSubscriptionId();
            int slotIndex = SubscriptionManager.getSlotIndex(subscriptionId);
            if (i == getSimState(subscriptionId) && KeyguardViewMediator.getUnlockTrackSimState(slotIndex) != 5) {
                return subscriptionId;
            }
        }
        return -1;
    }

    public SubscriptionInfo getSubscriptionInfoForSubId(int i) {
        List<SubscriptionInfo> subscriptionInfo = getSubscriptionInfo(false);
        for (int i2 = 0; i2 < subscriptionInfo.size(); i2++) {
            SubscriptionInfo subscriptionInfo2 = subscriptionInfo.get(i2);
            if (i == subscriptionInfo2.getSubscriptionId()) {
                return subscriptionInfo2;
            }
        }
        return null;
    }

    public boolean isLogoutEnabled() {
        return this.mLogoutEnabled;
    }

    /* access modifiers changed from: private */
    public void updateLogoutEnabled() {
        Assert.isMainThread();
        boolean isLogoutEnabled = this.mDevicePolicyManager.isLogoutEnabled();
        if (this.mLogoutEnabled != isLogoutEnabled) {
            this.mLogoutEnabled = isLogoutEnabled;
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onLogoutEnabledChanged();
                }
            }
        }
    }

    public void destroy() {
        if (((TelephonyManager) this.mContext.getSystemService("phone")) != null) {
            this.mTelephonyListenerManager.removeActiveDataSubscriptionIdListener(this.mPhoneStateListener);
        }
        this.mSubscriptionManager.removeOnSubscriptionsChangedListener(this.mSubscriptionListener);
        if (this.mDeviceProvisionedObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mDeviceProvisionedObserver);
        }
        if (this.mTimeFormatChangeObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mTimeFormatChangeObserver);
        }
        try {
            ActivityManager.getService().unregisterUserSwitchObserver(this.mUserSwitchObserver);
        } catch (RemoteException unused) {
            Log.d(TAG, "RemoteException onDestroy. cannot unregister userSwitchObserver");
        }
        TaskStackChangeListeners.getInstance().unregisterTaskStackListener(this.mTaskStackListener);
        this.mBroadcastDispatcher.unregisterReceiver(this.mBroadcastReceiver);
        this.mBroadcastDispatcher.unregisterReceiver(this.mBroadcastAllReceiver);
        this.mLockPatternUtils.unregisterStrongAuthTracker(this.mStrongAuthTracker);
        this.mTrustManager.unregisterTrustListener(this);
        this.mHandler.removeCallbacksAndMessages((Object) null);
    }

    public boolean isOOS() {
        int activeModemCount = this.mTelephonyManager.getActiveModemCount();
        boolean z = true;
        for (int i = 0; i < activeModemCount; i++) {
            int[] subscriptionIds = this.mSubscriptionManager.getSubscriptionIds(i);
            if (subscriptionIds != null && subscriptionIds.length >= 1) {
                boolean z2 = DEBUG;
                if (z2) {
                    Log.d(TAG, "slot id:" + i + " subId:" + subscriptionIds[0]);
                }
                ServiceState serviceState = this.mServiceStates.get(Integer.valueOf(subscriptionIds[0]));
                if (serviceState != null) {
                    if (serviceState.isEmergencyOnly() || !(serviceState.getVoiceRegState() == 1 || serviceState.getVoiceRegState() == 3)) {
                        z = false;
                    }
                    if (z2) {
                        Log.d(TAG, "is emergency: " + serviceState.isEmergencyOnly() + "voice state: " + serviceState.getVoiceRegState());
                    }
                } else if (z2) {
                    Log.d(TAG, "state is NULL");
                }
            }
        }
        return z;
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x02de  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x02e0  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x02f9  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x02fb  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x03ec  */
    /* JADX WARNING: Removed duplicated region for block: B:58:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dump(java.p026io.PrintWriter r18, java.lang.String[] r19) {
        /*
            r17 = this;
            r0 = r17
            r1 = r18
            java.lang.String r2 = "KeyguardUpdateMonitor state:"
            r1.println((java.lang.String) r2)
            java.lang.String r2 = "  SIM States:"
            r1.println((java.lang.String) r2)
            java.util.HashMap<java.lang.Integer, com.android.keyguard.KeyguardUpdateMonitor$SimData> r2 = r0.mSimDatas
            java.util.Collection r2 = r2.values()
            java.util.Iterator r2 = r2.iterator()
        L_0x0018:
            boolean r3 = r2.hasNext()
            java.lang.String r4 = "    "
            if (r3 == 0) goto L_0x003b
            java.lang.Object r3 = r2.next()
            com.android.keyguard.KeyguardUpdateMonitor$SimData r3 = (com.android.keyguard.KeyguardUpdateMonitor.SimData) r3
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>((java.lang.String) r4)
            java.lang.String r3 = r3.toString()
            java.lang.StringBuilder r3 = r5.append((java.lang.String) r3)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            goto L_0x0018
        L_0x003b:
            java.lang.String r2 = "  Subs:"
            r1.println((java.lang.String) r2)
            java.util.List<android.telephony.SubscriptionInfo> r2 = r0.mSubscriptionInfo
            if (r2 == 0) goto L_0x0066
            r2 = 0
        L_0x0045:
            java.util.List<android.telephony.SubscriptionInfo> r5 = r0.mSubscriptionInfo
            int r5 = r5.size()
            if (r2 >= r5) goto L_0x0066
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>((java.lang.String) r4)
            java.util.List<android.telephony.SubscriptionInfo> r6 = r0.mSubscriptionInfo
            java.lang.Object r6 = r6.get(r2)
            java.lang.StringBuilder r5 = r5.append((java.lang.Object) r6)
            java.lang.String r5 = r5.toString()
            r1.println((java.lang.String) r5)
            int r2 = r2 + 1
            goto L_0x0045
        L_0x0066:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r5 = "  Current active data subId="
            r2.<init>((java.lang.String) r5)
            int r5 = r0.mActiveMobileDataSubscription
            java.lang.StringBuilder r2 = r2.append((int) r5)
            java.lang.String r2 = r2.toString()
            r1.println((java.lang.String) r2)
            java.lang.String r2 = "  Service states:"
            r1.println((java.lang.String) r2)
            java.util.HashMap<java.lang.Integer, android.telephony.ServiceState> r2 = r0.mServiceStates
            java.util.Set r2 = r2.keySet()
            java.util.Iterator r2 = r2.iterator()
        L_0x0089:
            boolean r5 = r2.hasNext()
            if (r5 == 0) goto L_0x00be
            java.lang.Object r5 = r2.next()
            java.lang.Integer r5 = (java.lang.Integer) r5
            int r5 = r5.intValue()
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>((java.lang.String) r4)
            java.lang.StringBuilder r6 = r6.append((int) r5)
            java.lang.String r7 = "="
            java.lang.StringBuilder r6 = r6.append((java.lang.String) r7)
            java.util.HashMap<java.lang.Integer, android.telephony.ServiceState> r7 = r0.mServiceStates
            java.lang.Integer r5 = java.lang.Integer.valueOf((int) r5)
            java.lang.Object r5 = r7.get(r5)
            java.lang.StringBuilder r5 = r6.append((java.lang.Object) r5)
            java.lang.String r5 = r5.toString()
            r1.println((java.lang.String) r5)
            goto L_0x0089
        L_0x00be:
            android.hardware.fingerprint.FingerprintManager r2 = r0.mFpm
            java.lang.String r4 = "    enabledByUser="
            java.lang.String r5 = "    trustManaged="
            java.lang.String r6 = "    strongAuthFlags="
            java.lang.String r7 = "    listening: actual="
            java.lang.String r8 = "    possible="
            java.lang.String r9 = "    disabled(DPM)="
            java.lang.String r10 = "    authSinceBoot="
            java.lang.String r11 = "    auth'd="
            java.lang.String r12 = "    allowed="
            java.lang.String r13 = ")"
            if (r2 == 0) goto L_0x0298
            boolean r2 = r2.isHardwareDetected()
            if (r2 == 0) goto L_0x0298
            int r2 = android.app.ActivityManager.getCurrentUser()
            com.android.keyguard.KeyguardUpdateMonitor$StrongAuthTracker r15 = r0.mStrongAuthTracker
            int r15 = r15.getStrongAuthForUser(r2)
            android.util.SparseArray<com.android.keyguard.KeyguardUpdateMonitor$BiometricAuthenticated> r3 = r0.mUserFingerprintAuthenticated
            java.lang.Object r3 = r3.get(r2)
            com.android.keyguard.KeyguardUpdateMonitor$BiometricAuthenticated r3 = (com.android.keyguard.KeyguardUpdateMonitor.BiometricAuthenticated) r3
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r16 = r4
            java.lang.String r4 = "  Fingerprint state (user="
            r14.<init>((java.lang.String) r4)
            java.lang.StringBuilder r4 = r14.append((int) r2)
            java.lang.StringBuilder r4 = r4.append((java.lang.String) r13)
            java.lang.String r4 = r4.toString()
            r1.println((java.lang.String) r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r14 = "    areAllFpAuthenticatorsRegistered="
            r4.<init>((java.lang.String) r14)
            com.android.systemui.biometrics.AuthController r14 = r0.mAuthController
            boolean r14 = r14.areAllFingerprintAuthenticatorsRegistered()
            java.lang.StringBuilder r4 = r4.append((boolean) r14)
            java.lang.String r4 = r4.toString()
            r1.println((java.lang.String) r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>((java.lang.String) r12)
            if (r3 == 0) goto L_0x0131
            boolean r14 = r3.mIsStrongBiometric
            boolean r14 = r0.isUnlockingWithBiometricAllowed(r14)
            if (r14 == 0) goto L_0x0131
            r14 = 1
            goto L_0x0132
        L_0x0131:
            r14 = 0
        L_0x0132:
            java.lang.StringBuilder r4 = r4.append((boolean) r14)
            java.lang.String r4 = r4.toString()
            r1.println((java.lang.String) r4)
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>((java.lang.String) r11)
            if (r3 == 0) goto L_0x014c
            boolean r3 = r3.mAuthenticated
            if (r3 == 0) goto L_0x014c
            r3 = 1
            goto L_0x014d
        L_0x014c:
            r3 = 0
        L_0x014d:
            java.lang.StringBuilder r3 = r4.append((boolean) r3)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r10)
            com.android.keyguard.KeyguardUpdateMonitor$StrongAuthTracker r4 = r17.getStrongAuthTracker()
            boolean r4 = r4.hasUserAuthenticatedSinceBoot()
            java.lang.StringBuilder r3 = r3.append((boolean) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r9)
            boolean r4 = r0.isFingerprintDisabled(r2)
            java.lang.StringBuilder r3 = r3.append((boolean) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r8)
            boolean r4 = r0.isUnlockWithFingerprintPossible(r2)
            java.lang.StringBuilder r3 = r3.append((boolean) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r7)
            int r4 = r0.mFingerprintRunningState
            java.lang.StringBuilder r3 = r3.append((int) r4)
            java.lang.String r4 = " expected="
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)
            boolean r4 = r17.isUdfpsEnrolled()
            boolean r4 = r0.shouldListenForFingerprint(r4)
            java.lang.StringBuilder r3 = r3.append((int) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r6)
            java.lang.String r4 = java.lang.Integer.toHexString(r15)
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r5)
            boolean r4 = r0.getUserTrustIsManaged(r2)
            java.lang.StringBuilder r3 = r3.append((boolean) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "    mFingerprintLockedOut="
            r3.<init>((java.lang.String) r4)
            boolean r4 = r0.mFingerprintLockedOut
            java.lang.StringBuilder r3 = r3.append((boolean) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "    mFingerprintLockedOutPermanent="
            r3.<init>((java.lang.String) r4)
            boolean r4 = r0.mFingerprintLockedOutPermanent
            java.lang.StringBuilder r3 = r3.append((boolean) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r4 = r16
            r3.<init>((java.lang.String) r4)
            android.util.SparseBooleanArray r14 = r0.mBiometricEnabledForUser
            boolean r2 = r14.get(r2)
            java.lang.StringBuilder r2 = r3.append((boolean) r2)
            java.lang.String r2 = r2.toString()
            r1.println((java.lang.String) r2)
            boolean r2 = r17.isUdfpsSupported()
            if (r2 == 0) goto L_0x0298
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "        udfpsEnrolled="
            r2.<init>((java.lang.String) r3)
            boolean r3 = r17.isUdfpsEnrolled()
            java.lang.StringBuilder r2 = r2.append((boolean) r3)
            java.lang.String r2 = r2.toString()
            r1.println((java.lang.String) r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "        shouldListenForUdfps="
            r2.<init>((java.lang.String) r3)
            r3 = 1
            boolean r14 = r0.shouldListenForFingerprint(r3)
            java.lang.StringBuilder r2 = r2.append((boolean) r14)
            java.lang.String r2 = r2.toString()
            r1.println((java.lang.String) r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r14 = "        mBouncerIsOrWillBeShowing="
            r2.<init>((java.lang.String) r14)
            boolean r14 = r0.mBouncerIsOrWillBeShowing
            java.lang.StringBuilder r2 = r2.append((boolean) r14)
            java.lang.String r2 = r2.toString()
            r1.println((java.lang.String) r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r14 = "        mStatusBarState="
            r2.<init>((java.lang.String) r14)
            int r14 = r0.mStatusBarState
            java.lang.String r14 = com.android.systemui.statusbar.StatusBarState.toString(r14)
            java.lang.StringBuilder r2 = r2.append((java.lang.String) r14)
            java.lang.String r2 = r2.toString()
            r1.println((java.lang.String) r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r14 = "        mUdfpsBouncerShowing="
            r2.<init>((java.lang.String) r14)
            boolean r14 = r0.mUdfpsBouncerShowing
            java.lang.StringBuilder r2 = r2.append((boolean) r14)
            java.lang.String r2 = r2.toString()
            r1.println((java.lang.String) r2)
            goto L_0x0299
        L_0x0298:
            r3 = 1
        L_0x0299:
            android.hardware.face.FaceManager r2 = r0.mFaceManager
            if (r2 == 0) goto L_0x03e3
            boolean r2 = r2.isHardwareDetected()
            if (r2 == 0) goto L_0x03e3
            int r2 = android.app.ActivityManager.getCurrentUser()
            com.android.keyguard.KeyguardUpdateMonitor$StrongAuthTracker r14 = r0.mStrongAuthTracker
            int r14 = r14.getStrongAuthForUser(r2)
            android.util.SparseArray<com.android.keyguard.KeyguardUpdateMonitor$BiometricAuthenticated> r15 = r0.mUserFaceAuthenticated
            java.lang.Object r15 = r15.get(r2)
            com.android.keyguard.KeyguardUpdateMonitor$BiometricAuthenticated r15 = (com.android.keyguard.KeyguardUpdateMonitor.BiometricAuthenticated) r15
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r16 = r4
            java.lang.String r4 = "  Face authentication state (user="
            r3.<init>((java.lang.String) r4)
            java.lang.StringBuilder r3 = r3.append((int) r2)
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r13)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r12)
            if (r15 == 0) goto L_0x02e0
            boolean r4 = r15.mIsStrongBiometric
            boolean r4 = r0.isUnlockingWithBiometricAllowed(r4)
            if (r4 == 0) goto L_0x02e0
            r4 = 1
            goto L_0x02e1
        L_0x02e0:
            r4 = 0
        L_0x02e1:
            java.lang.StringBuilder r3 = r3.append((boolean) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r11)
            if (r15 == 0) goto L_0x02fb
            boolean r4 = r15.mAuthenticated
            if (r4 == 0) goto L_0x02fb
            r4 = 1
            goto L_0x02fc
        L_0x02fb:
            r4 = 0
        L_0x02fc:
            java.lang.StringBuilder r3 = r3.append((boolean) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r10)
            com.android.keyguard.KeyguardUpdateMonitor$StrongAuthTracker r4 = r17.getStrongAuthTracker()
            boolean r4 = r4.hasUserAuthenticatedSinceBoot()
            java.lang.StringBuilder r3 = r3.append((boolean) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r9)
            boolean r4 = r0.isFaceDisabled(r2)
            java.lang.StringBuilder r3 = r3.append((boolean) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r8)
            boolean r4 = r0.isUnlockWithFacePossible(r2)
            java.lang.StringBuilder r3 = r3.append((boolean) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r7)
            int r4 = r0.mFaceRunningState
            java.lang.StringBuilder r3 = r3.append((int) r4)
            java.lang.String r4 = " expected=("
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)
            boolean r4 = r17.shouldListenForFace()
            java.lang.StringBuilder r3 = r3.append((int) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r6)
            java.lang.String r4 = java.lang.Integer.toHexString(r14)
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>((java.lang.String) r5)
            boolean r4 = r0.getUserTrustIsManaged(r2)
            java.lang.StringBuilder r3 = r3.append((boolean) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "    mFaceLockedOutPermanent="
            r3.<init>((java.lang.String) r4)
            boolean r4 = r0.mFaceLockedOutPermanent
            java.lang.StringBuilder r3 = r3.append((boolean) r4)
            java.lang.String r3 = r3.toString()
            r1.println((java.lang.String) r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r4 = r16
            r3.<init>((java.lang.String) r4)
            android.util.SparseBooleanArray r4 = r0.mBiometricEnabledForUser
            boolean r2 = r4.get(r2)
            java.lang.StringBuilder r2 = r3.append((boolean) r2)
            java.lang.String r2 = r2.toString()
            r1.println((java.lang.String) r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "    mSecureCameraLaunched="
            r2.<init>((java.lang.String) r3)
            boolean r3 = r0.mSecureCameraLaunched
            java.lang.StringBuilder r2 = r2.append((boolean) r3)
            java.lang.String r2 = r2.toString()
            r1.println((java.lang.String) r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "    mBouncerFullyShown="
            r2.<init>((java.lang.String) r3)
            boolean r3 = r0.mBouncerFullyShown
            java.lang.StringBuilder r2 = r2.append((boolean) r3)
            java.lang.String r2 = r2.toString()
            r1.println((java.lang.String) r2)
        L_0x03e3:
            com.android.keyguard.KeyguardListenQueue r2 = r0.mListenModels
            r2.print(r1)
            boolean r0 = r0.mIsAutomotive
            if (r0 == 0) goto L_0x03f1
            java.lang.String r0 = "  Running on Automotive build"
            r1.println((java.lang.String) r0)
        L_0x03f1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardUpdateMonitor.dump(java.io.PrintWriter, java.lang.String[]):void");
    }

    public void setFaceRecognitionControllerRef(FaceRecognitionCallback faceRecognitionCallback) {
        Log.d(TAG, "setFaceRecognitionControllerRef: ");
        this.mFaceRecognitionCallbackRef = new WeakReference<>(faceRecognitionCallback);
    }

    public boolean isFaceRecognitionEnable() {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "nt_face_management_added_data", 0, -2) > 0;
    }

    public synchronized void updateFaceRecognitionState() {
        boolean shouldListenForFaceRecognition = shouldListenForFaceRecognition();
        Log.d(TAG, "shouldListenForFaceRecognition :" + shouldListenForFaceRecognition);
        if (this.mFaceRecognitionCallbackRef == null) {
            setFaceRecognitionControllerRef((FaceRecognitionCallback) NTDependencyEx.get(FaceRecognitionController.class));
        }
        WeakReference<FaceRecognitionCallback> weakReference = this.mFaceRecognitionCallbackRef;
        if (!(weakReference == null || weakReference.get() == null)) {
            boolean isCameraPreviewStarting = this.mFaceRecognitionCallbackRef.get().isCameraPreviewStarting();
            Log.d(TAG, "updateFaceRecognitionState:  isCameraStart = " + isCameraPreviewStarting);
            if (!isCameraPreviewStarting && shouldListenForFaceRecognition) {
                this.mFaceRecognitionCallbackRef.get().postToStartCamera();
                this.mFaceRecognitionCallbackRef.get().pauseCameraPreview(false);
            } else if (isCameraPreviewStarting && !shouldListenForFaceRecognition) {
                this.mFaceRecognitionCallbackRef.get().postToStopCamera();
                this.mFaceRecognitionCallbackRef.get().pauseCameraPreview(true);
            }
        }
    }

    public boolean shouldListenForFaceRecognition() {
        boolean isFaceRecognitionEnable = isFaceRecognitionEnable();
        boolean z = isFaceRecognitionEnable && this.mLockPatternUtils.isSecure(getCurrentUser());
        boolean z2 = this.mStatusBarStateController.getState() == 2;
        boolean z3 = this.mKeyguardIsVisible && this.mDeviceInteractive && !this.mGoingToSleep && !z2;
        boolean userNeedsStrongAuth = userNeedsStrongAuth();
        if (DEBUG) {
            StringBuilder append = new StringBuilder("mKeyguardIsVisible ").append(this.mKeyguardIsVisible).append(" mBouncerIsOrWillBeShowing:").append(this.mBouncerIsOrWillBeShowing).append(" mBouncerFullyShown = ").append(this.mBouncerFullyShown).append(" awakeKeyguard = ").append(z3).append("  satisfiedCondition:").append(z).append(" isEnableFaceRecognition:").append(isFaceRecognitionEnable).append("  mSwitchingUser:").append(this.mSwitchingUser).append("  isSimPinSecure:").append(isSimPinSecure()).append("  isFaceRecognitionSucceeded = ").append(isFaceRecognitionSucceeded()).append("  isUserUnlocked = ");
            UserManager userManager = this.mUserManager;
            Log.d(TAG, append.append(userManager != null ? userManager.isUserUnlocked() : false).append("  statusBarShadeLocked = ").append(z2).append("  mPanelDown = ").append(this.mPanelDown).append(" userNeedsStrongAuth = ").append(userNeedsStrongAuth).toString());
        }
        if (!z || !z3 || this.mBouncerFullyShown || this.mBouncerIsOrWillBeShowing || !this.mUserManager.isUserUnlocked() || this.mSwitchingUser || isSimPinSecure() || isFaceRecognitionSucceeded() || this.mPanelDown || userNeedsStrongAuth) {
            return false;
        }
        return true;
    }

    public void onFaceRecognitionSucceeded(boolean z) {
        if (z != this.mFaceRecognitionSucceeded) {
            this.mFaceRecognitionSucceeded = z;
            this.mHandler.obtainMessage(MSG_RESET_FACE_RECOGNITION_STATE).sendToTarget();
        }
    }

    public boolean isFaceRecognitionSucceeded() {
        return this.mFaceRecognitionSucceeded;
    }

    /* access modifiers changed from: private */
    public void handleFaceRecognitionSucceeded(boolean z) {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onFaceRecognitionSucceeded(z);
            }
        }
    }

    public void setIsFaceCameraStarting(boolean z) {
        this.mIsFaceCameraStarting = z;
    }

    public boolean isFaceCameraStarting() {
        return this.mIsFaceCameraStarting;
    }

    public void setPanelDown(boolean z) {
        Log.d(TAG, "setPanelDown: paneldown = " + z);
        this.mPanelDown = z;
        updateFaceRecognitionState();
    }

    public void setLowPowerState(boolean z) {
        this.mLowPowerState = z;
    }

    public boolean isLowPowerState() {
        return this.mLowPowerState;
    }

    public void setShowing(boolean z) {
        this.mShowing = z;
    }

    public Context getContext() {
        return this.mContext;
    }
}
