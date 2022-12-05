package com.android.keyguard;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
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
import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.biometrics.CryptoObject;
import android.hardware.biometrics.IBiometricEnabledOnKeyguardCallback;
import android.hardware.face.FaceManager;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintManager;
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
import android.os.Vibrator;
import android.provider.Settings;
import android.service.dreams.IDreamManager;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import androidx.lifecycle.Observer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.settingslib.WirelessUtils;
import com.android.settingslib.fuelgauge.BatteryStatus;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.R$string;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.StatusBarState;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.Assert;
import com.android.systemui.util.RingerModeTracker;
import com.google.android.collect.Lists;
import com.nothingos.systemui.facerecognition.FaceRecognitionController;
import com.nothingos.utils.NtVibrateUtils;
import com.nothingos.utils.SystemUIEventUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class KeyguardUpdateMonitor implements TrustManager.TrustListener, Dumpable {
    public static final boolean CORE_APPS_ONLY;
    private static final boolean DEBUG_FACE;
    private static final boolean DEBUG_FINGERPRINT;
    private static final ComponentName FALLBACK_HOME_COMPONENT = new ComponentName("com.android.settings", "com.android.settings.FallbackHome");
    private static int sCurrentUser;
    private boolean mAssistantVisible;
    private final AuthController mAuthController;
    private boolean mAuthInterruptActive;
    private final Executor mBackgroundExecutor;
    @VisibleForTesting
    BatteryStatus mBatteryStatus;
    private BiometricManager mBiometricManager;
    private boolean mBouncer;
    @VisibleForTesting
    protected final BroadcastReceiver mBroadcastAllReceiver;
    private final BroadcastDispatcher mBroadcastDispatcher;
    @VisibleForTesting
    protected final BroadcastReceiver mBroadcastReceiver;
    private final Context mContext;
    private boolean mCredentialAttempted;
    private boolean mDeviceInteractive;
    private final DevicePolicyManager mDevicePolicyManager;
    private ContentObserver mDeviceProvisionedObserver;
    private final IDreamManager mDreamManager;
    private CancellationSignal mFaceCancelSignal;
    private boolean mFaceLockedOutPermanent;
    private FaceManager mFaceManager;
    private WeakReference<FaceRecognitionCallback> mFaceRecognitionCallbackRef;
    private List<FaceSensorPropertiesInternal> mFaceSensorProperties;
    private CancellationSignal mFingerprintCancelSignal;
    private boolean mFingerprintLockedOut;
    private boolean mFingerprintLockedOutPermanent;
    private FingerprintManager mFpm;
    private boolean mGoingToSleep;
    private final Handler mHandler;
    private boolean mHasLockscreenWallpaper;
    private final boolean mIsAutomotive;
    private boolean mIsDreaming;
    private boolean mIsFaceAuthUserRequested;
    private boolean mIsFaceEnrolled;
    private final boolean mIsPrimaryUser;
    private boolean mIsUdfpsEnrolled;
    private KeyguardBypassController mKeyguardBypassController;
    private boolean mKeyguardGoingAway;
    private boolean mKeyguardIsVisible;
    private boolean mKeyguardOccluded;
    private boolean mLockIconPressed;
    private LockPatternUtils mLockPatternUtils;
    private int mLockScreenMode;
    private boolean mLogoutEnabled;
    private boolean mNeedsSlowUnlockTransition;
    private boolean mOccludingAppRequestingFace;
    private boolean mOccludingAppRequestingFp;
    private int mPhoneState;
    private int mRingMode;
    private RingerModeTracker mRingerModeTracker;
    private boolean mScreenOn;
    private boolean mSecureCameraLaunched;
    private boolean mShowing;
    private int mStatusBarState;
    private final StatusBarStateController mStatusBarStateController;
    private final StatusBarStateController.StateListener mStatusBarStateControllerListener;
    private StrongAuthTracker mStrongAuthTracker;
    private List<SubscriptionInfo> mSubscriptionInfo;
    private SubscriptionManager mSubscriptionManager;
    private boolean mSwitchingUser;
    @VisibleForTesting
    protected boolean mTelephonyCapable;
    private final TelephonyListenerManager mTelephonyListenerManager;
    private TelephonyManager mTelephonyManager;
    private ContentObserver mTimeFormatChangeObserver;
    private TrustManager mTrustManager;
    private UserManager mUserManager;
    private final UserSwitchObserver mUserSwitchObserver;
    private final Vibrator mVibrator;
    HashMap<Integer, SimData> mSimDatas = new HashMap<>();
    HashMap<Integer, ServiceState> mServiceStates = new HashMap<>();
    private final boolean mAcquiredHapticEnabled = false;
    private final ArrayList<WeakReference<KeyguardUpdateMonitorCallback>> mCallbacks = Lists.newArrayList();
    private int mFingerprintRunningState = 0;
    private int mFaceRunningState = 0;
    private int mActiveMobileDataSubscription = -1;
    private int mHardwareFingerprintUnavailableRetryCount = 0;
    private int mHardwareFaceUnavailableRetryCount = 0;
    private final Runnable mFpCancelNotReceived = new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda7
        @Override // java.lang.Runnable
        public final void run() {
            KeyguardUpdateMonitor.this.lambda$new$0();
        }
    };
    private final Runnable mFaceCancelNotReceived = new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            KeyguardUpdateMonitor.this.lambda$new$1();
        }
    };
    private final Observer<Integer> mRingerModeObserver = new Observer<Integer>() { // from class: com.android.keyguard.KeyguardUpdateMonitor.2
        @Override // androidx.lifecycle.Observer
        public void onChanged(Integer num) {
            KeyguardUpdateMonitor.this.mHandler.obtainMessage(305, num.intValue(), 0).sendToTarget();
        }
    };
    private SparseBooleanArray mBiometricEnabledForUser = new SparseBooleanArray();
    private IBiometricEnabledOnKeyguardCallback mBiometricEnabledCallback = new AnonymousClass3();
    @VisibleForTesting
    public TelephonyCallback.ActiveDataSubscriptionIdListener mPhoneStateListener = new TelephonyCallback.ActiveDataSubscriptionIdListener() { // from class: com.android.keyguard.KeyguardUpdateMonitor.4
        @Override // android.telephony.TelephonyCallback.ActiveDataSubscriptionIdListener
        public void onActiveDataSubscriptionIdChanged(int i) {
            KeyguardUpdateMonitor.this.mActiveMobileDataSubscription = i;
            KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(328);
        }
    };
    private SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.keyguard.KeyguardUpdateMonitor.5
        @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
        public void onSubscriptionsChanged() {
            KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(328);
        }
    };
    private SparseBooleanArray mUserIsUnlocked = new SparseBooleanArray();
    private SparseBooleanArray mUserHasTrust = new SparseBooleanArray();
    private SparseBooleanArray mUserTrustIsManaged = new SparseBooleanArray();
    private SparseBooleanArray mUserTrustIsUsuallyManaged = new SparseBooleanArray();
    private SparseBooleanArray mUserFaceUnlockRunning = new SparseBooleanArray();
    private Map<Integer, Intent> mSecondaryLockscreenRequirement = new HashMap();
    @VisibleForTesting
    SparseArray<BiometricAuthenticated> mUserFingerprintAuthenticated = new SparseArray<>();
    @VisibleForTesting
    SparseArray<BiometricAuthenticated> mUserFaceAuthenticated = new SparseArray<>();
    private final KeyguardListenQueue mListenModels = new KeyguardListenQueue();
    private Runnable mUpdateBiometricListeningState = new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda6
        @Override // java.lang.Runnable
        public final void run() {
            KeyguardUpdateMonitor.this.updateBiometricListeningState();
        }
    };
    private Runnable mRetryFingerprintAuthentication = new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor.7
        @Override // java.lang.Runnable
        public void run() {
            Log.w("KeyguardUpdateMonitor", "Retrying fingerprint after HW unavailable, attempt " + KeyguardUpdateMonitor.this.mHardwareFingerprintUnavailableRetryCount);
            if (KeyguardUpdateMonitor.this.mFpm.isHardwareDetected()) {
                KeyguardUpdateMonitor.this.updateFingerprintListeningState();
            } else if (KeyguardUpdateMonitor.this.mHardwareFingerprintUnavailableRetryCount >= 20) {
            } else {
                KeyguardUpdateMonitor.access$708(KeyguardUpdateMonitor.this);
                KeyguardUpdateMonitor.this.mHandler.postDelayed(KeyguardUpdateMonitor.this.mRetryFingerprintAuthentication, 500L);
            }
        }
    };
    private Runnable mRetryFaceAuthentication = new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor.8
        @Override // java.lang.Runnable
        public void run() {
            Log.w("KeyguardUpdateMonitor", "Retrying face after HW unavailable, attempt " + KeyguardUpdateMonitor.this.mHardwareFaceUnavailableRetryCount);
            KeyguardUpdateMonitor.this.updateFaceListeningState();
        }
    };
    private DisplayClientState mDisplayClientState = new DisplayClientState();
    private final FingerprintManager.LockoutResetCallback mFingerprintLockoutResetCallback = new FingerprintManager.LockoutResetCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor.11
        public void onLockoutReset(int i) {
            KeyguardUpdateMonitor.this.handleFingerprintLockoutReset();
        }
    };
    private final FaceManager.LockoutResetCallback mFaceLockoutResetCallback = new FaceManager.LockoutResetCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor.12
        public void onLockoutReset(int i) {
            KeyguardUpdateMonitor.this.handleFaceLockoutReset();
        }
    };
    private final FingerprintManager.FingerprintDetectionCallback mFingerprintDetectionCallback = new FingerprintManager.FingerprintDetectionCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda1
        public final void onFingerprintDetected(int i, int i2, boolean z) {
            KeyguardUpdateMonitor.this.lambda$new$3(i, i2, z);
        }
    };
    @VisibleForTesting
    final FingerprintManager.AuthenticationCallback mFingerprintAuthenticationCallback = new FingerprintManager.AuthenticationCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor.13
        private boolean mPlayedAcquiredHaptic;

        @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
        public void onAuthenticationFailed() {
            Log.e("KeyguardUpdateMonitor", "======onAuthenticationFailed======");
            KeyguardUpdateMonitor.this.handleFingerprintAuthFailed();
            NtVibrateUtils.getInstance(KeyguardUpdateMonitor.this.mContext).playVerityVibrate("/system/etc/richtapresources/NT_unlock_error.he");
            SystemUIEventUtils.collectUnLockResults(KeyguardUpdateMonitor.this.mContext, "unlock_fail", 4);
        }

        @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult authenticationResult) {
            Trace.beginSection("KeyguardUpdateMonitor#onAuthenticationSucceeded");
            KeyguardUpdateMonitor.this.handleFingerprintAuthenticated(authenticationResult.getUserId(), authenticationResult.isStrongBiometric());
            Trace.endSection();
            Log.d("KeyguardUpdateMonitor", "======onAuthenticationSucceeded======");
            if (!this.mPlayedAcquiredHaptic && KeyguardUpdateMonitor.this.isUdfpsEnrolled()) {
                KeyguardUpdateMonitor.this.playAcquiredHaptic();
            }
            NtVibrateUtils.getInstance(KeyguardUpdateMonitor.this.mContext).playVerityVibrate("/system/etc/richtapresources/NT_unlock_successful.he");
            SystemUIEventUtils.collectUnLockResults(KeyguardUpdateMonitor.this.mContext, "unlock_success", 4);
        }

        @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
        public void onAuthenticationHelp(int i, CharSequence charSequence) {
            Log.d("KeyguardUpdateMonitor", "======onAuthenticationHelp======helpMsgId=" + i + ", helpString=" + ((Object) charSequence));
            if (i == 1001 || i == 1002 || i == 1003) {
                return;
            }
            KeyguardUpdateMonitor.this.handleFingerprintHelp(i, charSequence.toString());
        }

        @Override // android.hardware.fingerprint.FingerprintManager.AuthenticationCallback
        public void onAuthenticationError(int i, CharSequence charSequence) {
            Log.e("KeyguardUpdateMonitor", "======onAuthenticationError====== errId = " + i + " errString = " + ((Object) charSequence));
            if (i != 5) {
                NtVibrateUtils.getInstance(KeyguardUpdateMonitor.this.mContext).playVerityVibrate("/system/etc/richtapresources/NT_unlock_error.he");
                KeyguardUpdateMonitor.this.handleFingerprintError(i, charSequence.toString());
            }
        }

        public void onAuthenticationAcquired(int i) {
            KeyguardUpdateMonitor.this.handleFingerprintAcquired(i);
            if (i != 0 || !KeyguardUpdateMonitor.this.isUdfpsEnrolled()) {
                return;
            }
            this.mPlayedAcquiredHaptic = true;
            KeyguardUpdateMonitor.this.playAcquiredHaptic();
        }

        public void onUdfpsPointerDown(int i) {
            Log.d("KeyguardUpdateMonitor", "onUdfpsPointerDown, sensorId: " + i);
            this.mPlayedAcquiredHaptic = false;
        }

        public void onUdfpsPointerUp(int i) {
            Log.d("KeyguardUpdateMonitor", "onUdfpsPointerUp, sensorId: " + i);
        }
    };
    private final FaceManager.FaceDetectionCallback mFaceDetectionCallback = new FaceManager.FaceDetectionCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda0
        public final void onFaceDetected(int i, int i2, boolean z) {
            KeyguardUpdateMonitor.this.lambda$new$4(i, i2, z);
        }
    };
    @VisibleForTesting
    final FaceManager.AuthenticationCallback mFaceAuthenticationCallback = new FaceManager.AuthenticationCallback() { // from class: com.android.keyguard.KeyguardUpdateMonitor.14
        public void onAuthenticationFailed() {
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
        }

        public void onAuthenticationAcquired(int i) {
            KeyguardUpdateMonitor.this.handleFaceAcquired(i);
        }
    };
    private final TaskStackChangeListener mTaskStackListener = new TaskStackChangeListener() { // from class: com.android.keyguard.KeyguardUpdateMonitor.19
        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public void onTaskStackChangedBackground() {
            try {
                ActivityTaskManager.RootTaskInfo rootTaskInfo = ActivityTaskManager.getService().getRootTaskInfo(0, 4);
                if (rootTaskInfo == null) {
                    return;
                }
                KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(335, Boolean.valueOf(rootTaskInfo.visible)));
            } catch (RemoteException e) {
                Log.e("KeyguardUpdateMonitor", "unable to check task stack", e);
            }
        }
    };
    private Runnable mDismissFpIconRunnable = new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor.20
        @Override // java.lang.Runnable
        public void run() {
            KeyguardUpdateMonitor.this.dismissFingerprintIcon();
        }
    };
    private boolean mFaceRecognitionSucceeded = false;
    private boolean mIsFaceCameraStarting = false;
    private boolean mPanelDown = false;
    private boolean mLowPowerState = false;
    private boolean mDeviceProvisioned = isDeviceProvisionedInSettingsDb();

    /* loaded from: classes.dex */
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

    @VisibleForTesting
    public void playAcquiredHaptic() {
    }

    static /* synthetic */ int access$708(KeyguardUpdateMonitor keyguardUpdateMonitor) {
        int i = keyguardUpdateMonitor.mHardwareFingerprintUnavailableRetryCount;
        keyguardUpdateMonitor.mHardwareFingerprintUnavailableRetryCount = i + 1;
        return i;
    }

    static {
        boolean z = Build.IS_DEBUGGABLE;
        DEBUG_FACE = z;
        DEBUG_FINGERPRINT = z;
        try {
            CORE_APPS_ONLY = IPackageManager.Stub.asInterface(ServiceManager.getService("package")).isOnlyCoreApps();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        Log.e("KeyguardUpdateMonitor", "Fp cancellation not received, transitioning to STOPPED");
        this.mFingerprintRunningState = 0;
        updateFingerprintListeningState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        Log.e("KeyguardUpdateMonitor", "Face cancellation not received, transitioning to STOPPED");
        this.mFaceRunningState = 0;
        updateFaceListeningState();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.keyguard.KeyguardUpdateMonitor$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 extends IBiometricEnabledOnKeyguardCallback.Stub {
        AnonymousClass3() {
        }

        public void onChanged(final boolean z, final int i) throws RemoteException {
            KeyguardUpdateMonitor.this.mHandler.post(new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardUpdateMonitor.AnonymousClass3.this.lambda$onChanged$0(i, z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onChanged$0(int i, boolean z) {
            KeyguardUpdateMonitor.this.mBiometricEnabledForUser.put(i, z);
            KeyguardUpdateMonitor.this.updateBiometricListeningState();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class BiometricAuthenticated {
        private final boolean mAuthenticated;
        private final boolean mIsStrongBiometric;

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

    public void onTrustChanged(boolean z, int i, int i2) {
        Assert.isMainThread();
        this.mUserHasTrust.put(i, z);
        updateBiometricListeningState();
        for (int i3 = 0; i3 < this.mCallbacks.size(); i3++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i3).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTrustChanged(i);
                if (z && i2 != 0) {
                    keyguardUpdateMonitorCallback.onTrustGrantedWithFlags(i2, i);
                }
            }
        }
    }

    public void onTrustError(CharSequence charSequence) {
        dispatchErrorMessage(charSequence);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSimSubscriptionInfoChanged() {
        Assert.isMainThread();
        Log.v("KeyguardUpdateMonitor", "onSubscriptionInfoChanged()");
        List<SubscriptionInfo> completeActiveSubscriptionInfoList = this.mSubscriptionManager.getCompleteActiveSubscriptionInfoList();
        if (completeActiveSubscriptionInfoList != null) {
            Iterator<SubscriptionInfo> it = completeActiveSubscriptionInfoList.iterator();
            while (it.hasNext()) {
                Log.v("KeyguardUpdateMonitor", "SubInfo:" + it.next());
            }
        } else {
            Log.v("KeyguardUpdateMonitor", "onSubscriptionInfoChanged: list is null");
        }
        List<SubscriptionInfo> subscriptionInfo = getSubscriptionInfo(true);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < subscriptionInfo.size(); i++) {
            SubscriptionInfo subscriptionInfo2 = subscriptionInfo.get(i);
            if (refreshSimState(subscriptionInfo2.getSubscriptionId(), subscriptionInfo2.getSimSlotIndex())) {
                arrayList.add(subscriptionInfo2);
            }
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            SimData simData = this.mSimDatas.get(Integer.valueOf(((SubscriptionInfo) arrayList.get(i2)).getSimSlotIndex()));
            if (simData != null) {
                for (int i3 = 0; i3 < this.mCallbacks.size(); i3++) {
                    KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i3).get();
                    if (keyguardUpdateMonitorCallback != null) {
                        keyguardUpdateMonitorCallback.onSimStateChanged(simData.subId, simData.slotId, simData.simState);
                    }
                }
            }
        }
        callbacksRefreshCarrierInfo();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleAirplaneModeChanged() {
        callbacksRefreshCarrierInfo();
    }

    private void callbacksRefreshCarrierInfo() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
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
                subscriptionInfo.remove(subscriptionInfo2);
            } else {
                if (subscriptionInfo2.getSubscriptionId() == this.mActiveMobileDataSubscription) {
                    subscriptionInfo2 = subscriptionInfo3;
                }
                subscriptionInfo.remove(subscriptionInfo2);
            }
        }
        return subscriptionInfo;
    }

    public void onTrustManagedChanged(boolean z, int i) {
        Assert.isMainThread();
        this.mUserTrustIsManaged.put(i, z);
        this.mUserTrustIsUsuallyManaged.put(i, this.mTrustManager.isTrustUsuallyManaged(i));
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTrustManagedChanged(i);
            }
        }
    }

    public void setCredentialAttempted() {
        this.mCredentialAttempted = true;
        updateBiometricListeningState();
    }

    public void setKeyguardGoingAway(boolean z) {
        this.mKeyguardGoingAway = z;
        updateBiometricListeningState();
    }

    public void setKeyguardOccluded(boolean z) {
        this.mKeyguardOccluded = z;
        updateBiometricListeningState();
    }

    public void requestFaceAuthOnOccludingApp(boolean z) {
        this.mOccludingAppRequestingFace = z;
        updateFaceListeningState();
    }

    public void requestFingerprintAuthOnOccludingApp(boolean z) {
        this.mOccludingAppRequestingFp = z;
        updateFingerprintListeningState();
    }

    public void onCameraLaunched() {
        this.mSecureCameraLaunched = true;
        updateBiometricListeningState();
    }

    public boolean isDreaming() {
        return this.mIsDreaming;
    }

    public void awakenFromDream() {
        IDreamManager iDreamManager;
        if (!this.mIsDreaming || (iDreamManager = this.mDreamManager) == null) {
            return;
        }
        try {
            iDreamManager.awaken();
        } catch (RemoteException unused) {
            Log.e("KeyguardUpdateMonitor", "Unable to awaken from dream");
        }
    }

    @VisibleForTesting
    protected void onFingerprintAuthenticated(int i, boolean z) {
        Assert.isMainThread();
        Trace.beginSection("KeyGuardUpdateMonitor#onFingerPrintAuthenticated");
        this.mUserFingerprintAuthenticated.put(i, new BiometricAuthenticated(true, z));
        if (getUserCanSkipBouncer(i)) {
            this.mTrustManager.unlockedByBiometricForUser(i, BiometricSourceType.FINGERPRINT);
        }
        this.mFingerprintCancelSignal = null;
        updateBiometricListeningState();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAuthenticated(i, BiometricSourceType.FINGERPRINT, z);
            }
        }
        Handler handler = this.mHandler;
        handler.sendMessageDelayed(handler.obtainMessage(336), 500L);
        this.mAssistantVisible = false;
        reportSuccessfulBiometricUnlock(z, i);
        Trace.endSection();
    }

    private void reportSuccessfulBiometricUnlock(final boolean z, final int i) {
        this.mBackgroundExecutor.execute(new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor.6
            @Override // java.lang.Runnable
            public void run() {
                KeyguardUpdateMonitor.this.mLockPatternUtils.reportSuccessfulBiometricUnlock(z, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFingerprintAuthFailed() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAuthFailed(BiometricSourceType.FINGERPRINT);
            }
        }
        handleFingerprintHelp(-1, this.mContext.getString(R$string.kg_fingerprint_not_recognized));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFingerprintAcquired(int i) {
        Assert.isMainThread();
        if (i != 0) {
            return;
        }
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAcquired(BiometricSourceType.FINGERPRINT);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFingerprintAuthenticated(int i, boolean z) {
        Trace.beginSection("KeyGuardUpdateMonitor#handlerFingerPrintAuthenticated");
        try {
            int i2 = ActivityManager.getService().getCurrentUser().id;
            if (i2 != i) {
                Log.d("KeyguardUpdateMonitor", "Fingerprint authenticated for wrong user: " + i);
            } else if (!isFingerprintDisabled(i2)) {
                onFingerprintAuthenticated(i2, z);
                setFingerprintRunningState(0);
                Trace.endSection();
            } else {
                Log.d("KeyguardUpdateMonitor", "Fingerprint disabled by DPM for userId: " + i2);
            }
        } catch (RemoteException e) {
            Log.e("KeyguardUpdateMonitor", "Failed to get current user id: ", e);
        } finally {
            setFingerprintRunningState(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFingerprintHelp(int i, String str) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricHelp(i, str, BiometricSourceType.FINGERPRINT);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFingerprintError(int i, String str) {
        Assert.isMainThread();
        Log.d("KeyguardUpdateMonitor", "handleFingerprintError: id = " + i + " errString = " + str);
        if (this.mHandler.hasCallbacks(this.mFpCancelNotReceived)) {
            this.mHandler.removeCallbacks(this.mFpCancelNotReceived);
        }
        this.mFingerprintCancelSignal = null;
        if ((i == 5 && this.mFingerprintRunningState == 3) || i == 1006) {
            setFingerprintRunningState(0);
            updateFingerprintListeningState();
        } else {
            setFingerprintRunningState(0);
        }
        if (i == 1) {
            this.mHandler.postDelayed(this.mRetryFingerprintAuthentication, 500L);
        }
        if (i == 9) {
            this.mFingerprintLockedOutPermanent = true;
            requireStrongAuthIfAllLockedOut();
        }
        if (i == 7 || i == 9) {
            this.mFingerprintLockedOut = true;
            if (isUdfpsEnrolled()) {
                updateFingerprintListeningState();
            }
        }
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricError(i, str, BiometricSourceType.FINGERPRINT);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFingerprintLockoutReset() {
        this.mFingerprintLockedOut = false;
        this.mFingerprintLockedOutPermanent = false;
        if (isUdfpsEnrolled()) {
            this.mHandler.postDelayed(new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardUpdateMonitor.this.updateFingerprintListeningState();
                }
            }, 600L);
        } else {
            updateFingerprintListeningState();
        }
    }

    private void setFingerprintRunningState(int i) {
        boolean z = false;
        boolean z2 = this.mFingerprintRunningState == 1;
        if (i == 1) {
            z = true;
        }
        this.mFingerprintRunningState = i;
        Log.d("KeyguardUpdateMonitor", "fingerprintRunningState: " + this.mFingerprintRunningState);
        if (z2 != z) {
            notifyFingerprintRunningStateChanged();
        }
    }

    private void notifyFingerprintRunningStateChanged() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricRunningStateChanged(isFingerprintDetectionRunning(), BiometricSourceType.FINGERPRINT);
            }
        }
    }

    @VisibleForTesting
    protected void onFaceAuthenticated(int i, boolean z) {
        Trace.beginSection("KeyGuardUpdateMonitor#onFaceAuthenticated");
        Assert.isMainThread();
        this.mUserFaceAuthenticated.put(i, new BiometricAuthenticated(true, z));
        if (getUserCanSkipBouncer(i)) {
            this.mTrustManager.unlockedByBiometricForUser(i, BiometricSourceType.FACE);
        }
        this.mFaceCancelSignal = null;
        updateBiometricListeningState();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAuthenticated(i, BiometricSourceType.FACE, z);
            }
        }
        this.mAssistantVisible = false;
        reportSuccessfulBiometricUnlock(z, i);
        Trace.endSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFaceAuthFailed() {
        Assert.isMainThread();
        this.mFaceCancelSignal = null;
        setFaceRunningState(0);
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAuthFailed(BiometricSourceType.FACE);
            }
        }
        handleFaceHelp(-2, this.mContext.getString(R$string.kg_face_not_recognized));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFaceAcquired(int i) {
        Assert.isMainThread();
        if (i != 0) {
            return;
        }
        if (DEBUG_FACE) {
            Log.d("KeyguardUpdateMonitor", "Face acquired");
        }
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricAcquired(BiometricSourceType.FACE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFaceAuthenticated(int i, boolean z) {
        Trace.beginSection("KeyGuardUpdateMonitor#handlerFaceAuthenticated");
        try {
            if (this.mGoingToSleep) {
                Log.d("KeyguardUpdateMonitor", "Aborted successful auth because device is going to sleep.");
                return;
            }
            int i2 = ActivityManager.getService().getCurrentUser().id;
            if (i2 != i) {
                Log.d("KeyguardUpdateMonitor", "Face authenticated for wrong user: " + i);
            } else if (isFaceDisabled(i2)) {
                Log.d("KeyguardUpdateMonitor", "Face authentication disabled by DPM for userId: " + i2);
            } else {
                if (DEBUG_FACE) {
                    Log.d("KeyguardUpdateMonitor", "Face auth succeeded for user " + i2);
                }
                onFaceAuthenticated(i2, z);
                setFaceRunningState(0);
                Trace.endSection();
            }
        } catch (RemoteException e) {
            Log.e("KeyguardUpdateMonitor", "Failed to get current user id: ", e);
        } finally {
            setFaceRunningState(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFaceHelp(int i, String str) {
        Assert.isMainThread();
        if (DEBUG_FACE) {
            Log.d("KeyguardUpdateMonitor", "Face help received: " + str);
        }
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricHelp(i, str, BiometricSourceType.FACE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFaceError(int i, String str) {
        int i2;
        Assert.isMainThread();
        if (DEBUG_FACE) {
            Log.d("KeyguardUpdateMonitor", "Face error received: " + str);
        }
        if (this.mHandler.hasCallbacks(this.mFaceCancelNotReceived)) {
            this.mHandler.removeCallbacks(this.mFaceCancelNotReceived);
        }
        this.mFaceCancelSignal = null;
        if (i == 5 && this.mFaceRunningState == 3) {
            setFaceRunningState(0);
            updateFaceListeningState();
        } else {
            setFaceRunningState(0);
        }
        if ((i == 1 || i == 2) && (i2 = this.mHardwareFaceUnavailableRetryCount) < 20) {
            this.mHardwareFaceUnavailableRetryCount = i2 + 1;
            this.mHandler.removeCallbacks(this.mRetryFaceAuthentication);
            this.mHandler.postDelayed(this.mRetryFaceAuthentication, 500L);
        }
        if (i == 9) {
            this.mFaceLockedOutPermanent = true;
            requireStrongAuthIfAllLockedOut();
        }
        for (int i3 = 0; i3 < this.mCallbacks.size(); i3++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i3).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricError(i, str, BiometricSourceType.FACE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFaceLockoutReset() {
        this.mFaceLockedOutPermanent = false;
        updateFaceListeningState();
    }

    private void setFaceRunningState(int i) {
        boolean z = false;
        boolean z2 = this.mFaceRunningState == 1;
        if (i == 1) {
            z = true;
        }
        this.mFaceRunningState = i;
        Log.d("KeyguardUpdateMonitor", "faceRunningState: " + this.mFaceRunningState);
        if (z2 != z) {
            notifyFaceRunningStateChanged();
        }
    }

    private void notifyFaceRunningStateChanged() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricRunningStateChanged(isFaceDetectionRunning(), BiometricSourceType.FACE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFaceUnlockStateChanged(boolean z, int i) {
        Assert.isMainThread();
        this.mUserFaceUnlockRunning.put(i, z);
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onFaceUnlockStateChanged(z, i);
            }
        }
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
        return !(devicePolicyManager == null || (devicePolicyManager.getKeyguardDisabledFeatures(null, i) & 32) == 0) || isSimPinSecure();
    }

    private boolean isFaceDisabled(final int i) {
        final DevicePolicyManager devicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService("device_policy");
        return ((Boolean) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda11
            @Override // java.util.function.Supplier
            public final Object get() {
                Boolean lambda$isFaceDisabled$2;
                lambda$isFaceDisabled$2 = KeyguardUpdateMonitor.this.lambda$isFaceDisabled$2(devicePolicyManager, i);
                return lambda$isFaceDisabled$2;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$isFaceDisabled$2(DevicePolicyManager devicePolicyManager, int i) {
        return Boolean.valueOf(!(devicePolicyManager == null || (devicePolicyManager.getKeyguardDisabledFeatures(null, i) & 128) == 0) || isSimPinSecure());
    }

    private boolean getIsFaceAuthenticated() {
        BiometricAuthenticated biometricAuthenticated = this.mUserFaceAuthenticated.get(getCurrentUser());
        if (biometricAuthenticated != null) {
            return biometricAuthenticated.mAuthenticated;
        }
        return false;
    }

    private void requireStrongAuthIfAllLockedOut() {
        boolean z = true;
        boolean z2 = (this.mFaceLockedOutPermanent || !shouldListenForFace()) && !getIsFaceAuthenticated();
        if (!this.mFingerprintLockedOutPermanent && shouldListenForFingerprint(isUdfpsEnrolled())) {
            z = false;
        }
        if (!z2 || !z) {
            return;
        }
        Log.d("KeyguardUpdateMonitor", "All biometrics locked out - requiring strong auth");
        this.mLockPatternUtils.requireStrongAuth(8, getCurrentUser());
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
        return (biometricAuthenticated != null && biometricAuthenticated.mAuthenticated && isUnlockingWithBiometricAllowed(biometricAuthenticated.mIsStrongBiometric)) || (biometricAuthenticated2 != null && biometricAuthenticated2.mAuthenticated && isUnlockingWithBiometricAllowed(biometricAuthenticated2.mIsStrongBiometric)) || this.mFaceRecognitionSucceeded;
    }

    public boolean getUserTrustIsManaged(int i) {
        return this.mUserTrustIsManaged.get(i) && !isTrustDisabled(i);
    }

    private void updateSecondaryLockscreenRequirement(int i) {
        Intent intent = this.mSecondaryLockscreenRequirement.get(Integer.valueOf(i));
        boolean isSecondaryLockscreenEnabled = this.mDevicePolicyManager.isSecondaryLockscreenEnabled(UserHandle.of(i));
        boolean z = true;
        if (!isSecondaryLockscreenEnabled || intent != null) {
            if (!isSecondaryLockscreenEnabled && intent != null) {
                this.mSecondaryLockscreenRequirement.put(Integer.valueOf(i), null);
            }
            z = false;
        } else {
            ComponentName profileOwnerOrDeviceOwnerSupervisionComponent = this.mDevicePolicyManager.getProfileOwnerOrDeviceOwnerSupervisionComponent(UserHandle.of(i));
            if (profileOwnerOrDeviceOwnerSupervisionComponent == null) {
                Log.e("KeyguardUpdateMonitor", "No Profile Owner or Device Owner supervision app found for User " + i);
            } else {
                ResolveInfo resolveService = this.mContext.getPackageManager().resolveService(new Intent("android.app.action.BIND_SECONDARY_LOCKSCREEN_SERVICE").setPackage(profileOwnerOrDeviceOwnerSupervisionComponent.getPackageName()), 0);
                if (resolveService != null && resolveService.serviceInfo != null) {
                    this.mSecondaryLockscreenRequirement.put(Integer.valueOf(i), new Intent().setComponent(resolveService.serviceInfo.getComponentName()));
                }
            }
            z = false;
        }
        if (z) {
            for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onSecondaryLockscreenRequirementChanged(i);
                }
            }
        }
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

    private boolean isEncryptedOrLockdown(int i) {
        int strongAuthForUser = this.mStrongAuthTracker.getStrongAuthForUser(i);
        return containsFlag(strongAuthForUser, 1) || (containsFlag(strongAuthForUser, 2) || containsFlag(strongAuthForUser, 32));
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

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyStrongAuthStateChanged(int i) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onStrongAuthStateChanged(i);
            }
        }
    }

    public boolean isScreenOn() {
        return this.mScreenOn;
    }

    private void dispatchErrorMessage(CharSequence charSequence) {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTrustAgentErrorMessage(charSequence);
            }
        }
    }

    @VisibleForTesting
    void setAssistantVisible(boolean z) {
        this.mAssistantVisible = z;
        updateBiometricListeningState();
    }

    /* loaded from: classes.dex */
    static class DisplayClientState {
        DisplayClientState() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(int i, int i2, boolean z) {
        handleFingerprintAuthenticated(i2, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(int i, int i2, boolean z) {
        handleFaceAuthenticated(i2, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class SimData {
        public int simState;
        public int slotId;
        public int subId;

        SimData(int i, int i2, int i3) {
            this.simState = i;
            this.slotId = i2;
            this.subId = i3;
        }

        static SimData fromIntent(Intent intent) {
            if (!"android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
                throw new IllegalArgumentException("only handles intent ACTION_SIM_STATE_CHANGED");
            }
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

        public String toString() {
            return "SimData{state=" + this.simState + ",slotId=" + this.slotId + ",subId=" + this.subId + "}";
        }
    }

    /* loaded from: classes.dex */
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

    protected void handleStartedWakingUp() {
        Trace.beginSection("KeyguardUpdateMonitor#handleStartedWakingUp");
        Assert.isMainThread();
        updateBiometricListeningState();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onStartedWakingUp();
            }
        }
        this.mHandler.removeCallbacks(this.mDismissFpIconRunnable);
        Trace.endSection();
    }

    protected void handleStartedGoingToSleep(int i) {
        Assert.isMainThread();
        this.mLockIconPressed = false;
        clearBiometricRecognized();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onStartedGoingToSleep(i);
            }
        }
        this.mGoingToSleep = true;
        updateBiometricListeningState();
    }

    protected void handleFinishedGoingToSleep(int i) {
        Assert.isMainThread();
        this.mGoingToSleep = false;
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onFinishedGoingToSleep(i);
            }
        }
        updateBiometricListeningState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScreenTurnedOn() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onScreenTurnedOn();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleScreenTurnedOff() {
        DejankUtils.startDetectingBlockingIpcs("KeyguardUpdateMonitor#handleScreenTurnedOff");
        Assert.isMainThread();
        this.mHardwareFingerprintUnavailableRetryCount = 0;
        this.mHardwareFaceUnavailableRetryCount = 0;
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onScreenTurnedOff();
            }
        }
        DejankUtils.stopDetectingBlockingIpcs("KeyguardUpdateMonitor#handleScreenTurnedOff");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDreamingStateChanged(int i) {
        Assert.isMainThread();
        boolean z = true;
        if (i != 1) {
            z = false;
        }
        this.mIsDreaming = z;
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onDreamingStateChanged(this.mIsDreaming);
            }
        }
        updateBiometricListeningState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUserInfoChanged(int i) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onUserInfoChanged(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUserUnlocked(int i) {
        Assert.isMainThread();
        this.mUserIsUnlocked.put(i, true);
        this.mNeedsSlowUnlockTransition = resolveNeedsSlowUnlockTransition();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onUserUnlocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUserStopped(int i) {
        Assert.isMainThread();
        this.mUserIsUnlocked.put(i, this.mUserManager.isUserUnlocked(i));
    }

    @VisibleForTesting
    void handleUserRemoved(int i) {
        Assert.isMainThread();
        this.mUserIsUnlocked.delete(i);
        this.mUserTrustIsUsuallyManaged.delete(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleKeyguardGoingAway(boolean z) {
        Assert.isMainThread();
        setKeyguardGoingAway(z);
    }

    @VisibleForTesting
    protected void setStrongAuthTracker(StrongAuthTracker strongAuthTracker) {
        StrongAuthTracker strongAuthTracker2 = this.mStrongAuthTracker;
        if (strongAuthTracker2 != null) {
            this.mLockPatternUtils.unregisterStrongAuthTracker(strongAuthTracker2);
        }
        this.mStrongAuthTracker = strongAuthTracker;
        this.mLockPatternUtils.registerStrongAuthTracker(strongAuthTracker);
    }

    @VisibleForTesting
    void resetBiometricListeningState() {
        this.mFingerprintRunningState = 0;
        this.mFaceRunningState = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerRingerTracker() {
        this.mRingerModeTracker.getRingerMode().observeForever(this.mRingerModeObserver);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    public KeyguardUpdateMonitor(Context context, Looper looper, BroadcastDispatcher broadcastDispatcher, DumpManager dumpManager, RingerModeTracker ringerModeTracker, Executor executor, StatusBarStateController statusBarStateController, LockPatternUtils lockPatternUtils, AuthController authController, TelephonyListenerManager telephonyListenerManager, FeatureFlags featureFlags, Vibrator vibrator) {
        StatusBarStateController.StateListener stateListener = new StatusBarStateController.StateListener() { // from class: com.android.keyguard.KeyguardUpdateMonitor.1
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onStateChanged(int i) {
                Log.i("KeyguardUpdateMonitor", "onStateChanged newState=" + i + ", oldState=" + KeyguardUpdateMonitor.this.mStatusBarState, new Exception());
                KeyguardUpdateMonitor.this.mStatusBarState = i;
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onExpandedChanged(boolean z) {
                for (int i = 0; i < KeyguardUpdateMonitor.this.mCallbacks.size(); i++) {
                    KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = (KeyguardUpdateMonitorCallback) ((WeakReference) KeyguardUpdateMonitor.this.mCallbacks.get(i)).get();
                    if (keyguardUpdateMonitorCallback != null) {
                        keyguardUpdateMonitorCallback.onShadeExpandedChanged(z);
                    }
                }
            }
        };
        this.mStatusBarStateControllerListener = stateListener;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.keyguard.KeyguardUpdateMonitor.9
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                Log.d("KeyguardUpdateMonitor", "received broadcast " + action);
                if ("android.intent.action.TIME_TICK".equals(action) || "android.intent.action.TIME_SET".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(301);
                } else if ("android.intent.action.TIMEZONE_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(339, intent.getStringExtra("time-zone")));
                } else if ("android.intent.action.BATTERY_CHANGED".equals(action)) {
                    Log.d("KeyguardUpdateMonitor", "received broadcast ACTION_BATTERY_CHANGED:, time=" + System.currentTimeMillis());
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(302, new BatteryStatus(intent)));
                } else if ("android.intent.action.SIM_STATE_CHANGED".equals(action)) {
                    SimData fromIntent = SimData.fromIntent(intent);
                    if (intent.getBooleanExtra("rebroadcastOnUnlock", false)) {
                        if (fromIntent.simState != 1) {
                            return;
                        }
                        KeyguardUpdateMonitor.this.mHandler.obtainMessage(338, Boolean.TRUE).sendToTarget();
                        return;
                    }
                    Log.v("KeyguardUpdateMonitor", "action " + action + " state: " + intent.getStringExtra("ss") + " slotId: " + fromIntent.slotId + " subid: " + fromIntent.subId);
                    KeyguardUpdateMonitor.this.mHandler.obtainMessage(304, fromIntent.subId, fromIntent.slotId, Integer.valueOf(fromIntent.simState)).sendToTarget();
                } else if ("android.intent.action.PHONE_STATE".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(306, intent.getStringExtra("state")));
                } else if ("android.intent.action.AIRPLANE_MODE".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(329);
                } else if ("android.intent.action.SERVICE_STATE".equals(action)) {
                    ServiceState newFromBundle = ServiceState.newFromBundle(intent.getExtras());
                    int intExtra = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
                    Log.v("KeyguardUpdateMonitor", "action " + action + " serviceState=" + newFromBundle + " subId=" + intExtra);
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(330, intExtra, 0, newFromBundle));
                } else if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(328);
                } else if (!"android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED".equals(action)) {
                } else {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(337);
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() { // from class: com.android.keyguard.KeyguardUpdateMonitor.10
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if ("android.app.action.NEXT_ALARM_CLOCK_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(301);
                } else if ("android.intent.action.USER_INFO_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(317, intent.getIntExtra("android.intent.extra.user_handle", getSendingUserId()), 0));
                } else if ("com.android.facelock.FACE_UNLOCK_STARTED".equals(action)) {
                    Trace.beginSection("KeyguardUpdateMonitor.mBroadcastAllReceiver#onReceive ACTION_FACE_UNLOCK_STARTED");
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(327, 1, getSendingUserId()));
                    Trace.endSection();
                } else if ("com.android.facelock.FACE_UNLOCK_STOPPED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(327, 0, getSendingUserId()));
                } else if ("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(309, Integer.valueOf(getSendingUserId())));
                } else if ("android.intent.action.USER_UNLOCKED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(334, getSendingUserId(), 0));
                } else if ("android.intent.action.USER_STOPPED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(340, intent.getIntExtra("android.intent.extra.user_handle", -1), 0));
                } else if ("android.intent.action.USER_REMOVED".equals(action)) {
                    KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(341, intent.getIntExtra("android.intent.extra.user_handle", -1), 0));
                } else if (!"android.nfc.action.REQUIRE_UNLOCK_FOR_NFC".equals(action)) {
                } else {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(345);
                }
            }
        };
        this.mBroadcastAllReceiver = broadcastReceiver2;
        UserSwitchObserver userSwitchObserver = new UserSwitchObserver() { // from class: com.android.keyguard.KeyguardUpdateMonitor.17
            public void onUserSwitching(int i, IRemoteCallback iRemoteCallback) {
                KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(310, i, 0, iRemoteCallback));
            }

            public void onUserSwitchComplete(int i) throws RemoteException {
                KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(314, i, 0));
            }
        };
        this.mUserSwitchObserver = userSwitchObserver;
        this.mContext = context;
        this.mSubscriptionManager = SubscriptionManager.from(context);
        this.mTelephonyListenerManager = telephonyListenerManager;
        this.mStrongAuthTracker = new StrongAuthTracker(context, new Consumer() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                KeyguardUpdateMonitor.this.notifyStrongAuthStateChanged(((Integer) obj).intValue());
            }
        });
        this.mBackgroundExecutor = executor;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mRingerModeTracker = ringerModeTracker;
        this.mStatusBarStateController = statusBarStateController;
        statusBarStateController.addCallback(stateListener);
        this.mStatusBarState = statusBarStateController.getState();
        this.mLockPatternUtils = lockPatternUtils;
        this.mAuthController = authController;
        dumpManager.registerDumpable(KeyguardUpdateMonitor.class.getName(), this);
        this.mVibrator = vibrator;
        Handler handler = new Handler(looper) { // from class: com.android.keyguard.KeyguardUpdateMonitor.15
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 301:
                        KeyguardUpdateMonitor.this.handleTimeUpdate();
                        return;
                    case 302:
                        KeyguardUpdateMonitor.this.handleBatteryUpdate((BatteryStatus) message.obj);
                        return;
                    case 303:
                    case 307:
                    case 311:
                    case 313:
                    case 315:
                    case 316:
                    case 323:
                    case 324:
                    case 325:
                    case 326:
                    default:
                        super.handleMessage(message);
                        return;
                    case 304:
                        KeyguardUpdateMonitor.this.handleSimStateChange(message.arg1, message.arg2, ((Integer) message.obj).intValue());
                        return;
                    case 305:
                        KeyguardUpdateMonitor.this.handleRingerModeChange(message.arg1);
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
                    case 321:
                        KeyguardUpdateMonitor.this.handleStartedGoingToSleep(message.arg1);
                        return;
                    case 322:
                        KeyguardUpdateMonitor.this.handleKeyguardBouncerChanged(message.arg1);
                        return;
                    case 327:
                        Trace.beginSection("KeyguardUpdateMonitor#handler MSG_FACE_UNLOCK_STATE_CHANGED");
                        KeyguardUpdateMonitor.this.handleFaceUnlockStateChanged(message.arg1 != 0, message.arg2);
                        Trace.endSection();
                        return;
                    case 328:
                        KeyguardUpdateMonitor.this.handleSimSubscriptionInfoChanged();
                        return;
                    case 329:
                        KeyguardUpdateMonitor.this.handleAirplaneModeChanged();
                        return;
                    case 330:
                        KeyguardUpdateMonitor.this.handleServiceStateChange(message.arg1, (ServiceState) message.obj);
                        return;
                    case 331:
                        KeyguardUpdateMonitor.this.handleScreenTurnedOn();
                        return;
                    case 332:
                        Trace.beginSection("KeyguardUpdateMonitor#handler MSG_SCREEN_TURNED_ON");
                        KeyguardUpdateMonitor.this.handleScreenTurnedOff();
                        Trace.endSection();
                        return;
                    case 333:
                        KeyguardUpdateMonitor.this.handleDreamingStateChanged(message.arg1);
                        return;
                    case 334:
                        KeyguardUpdateMonitor.this.handleUserUnlocked(message.arg1);
                        return;
                    case 335:
                        KeyguardUpdateMonitor.this.setAssistantVisible(((Boolean) message.obj).booleanValue());
                        return;
                    case 336:
                        KeyguardUpdateMonitor.this.updateBiometricListeningState();
                        return;
                    case 337:
                        KeyguardUpdateMonitor.this.updateLogoutEnabled();
                        return;
                    case 338:
                        KeyguardUpdateMonitor.this.updateTelephonyCapable(((Boolean) message.obj).booleanValue());
                        return;
                    case 339:
                        KeyguardUpdateMonitor.this.handleTimeZoneUpdate((String) message.obj);
                        return;
                    case 340:
                        KeyguardUpdateMonitor.this.handleUserStopped(message.arg1);
                        return;
                    case 341:
                        KeyguardUpdateMonitor.this.handleUserRemoved(message.arg1);
                        return;
                    case 342:
                        KeyguardUpdateMonitor.this.handleKeyguardGoingAway(((Boolean) message.obj).booleanValue());
                        return;
                    case 343:
                        KeyguardUpdateMonitor.this.handleLockScreenMode();
                        return;
                    case 344:
                        KeyguardUpdateMonitor.this.handleTimeFormatUpdate((String) message.obj);
                        return;
                    case 345:
                        KeyguardUpdateMonitor.this.handleRequireUnlockForNfc();
                        return;
                    case 346:
                        KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
                        keyguardUpdateMonitor.handleFaceRecognitionSucceeded(keyguardUpdateMonitor.mFaceRecognitionSucceeded);
                        KeyguardUpdateMonitor.this.updateBiometricListeningState();
                        return;
                }
            }
        };
        this.mHandler = handler;
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
        broadcastDispatcher.registerReceiverWithHandler(broadcastReceiver, intentFilter, handler);
        executor.execute(new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardUpdateMonitor.this.lambda$new$5();
            }
        });
        handler.post(new Runnable() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardUpdateMonitor.this.registerRingerTracker();
            }
        });
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.USER_INFO_CHANGED");
        intentFilter2.addAction("android.app.action.NEXT_ALARM_CLOCK_CHANGED");
        intentFilter2.addAction("com.android.facelock.FACE_UNLOCK_STARTED");
        intentFilter2.addAction("com.android.facelock.FACE_UNLOCK_STOPPED");
        intentFilter2.addAction("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
        intentFilter2.addAction("android.intent.action.USER_UNLOCKED");
        intentFilter2.addAction("android.intent.action.USER_STOPPED");
        intentFilter2.addAction("android.intent.action.USER_REMOVED");
        intentFilter2.addAction("android.nfc.action.REQUIRE_UNLOCK_FOR_NFC");
        broadcastDispatcher.registerReceiverWithHandler(broadcastReceiver2, intentFilter2, handler, UserHandle.ALL);
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mSubscriptionListener);
        try {
            ActivityManager.getService().registerUserSwitchObserver(userSwitchObserver, "KeyguardUpdateMonitor");
        } catch (RemoteException e) {
            e.rethrowAsRuntimeException();
        }
        TrustManager trustManager = (TrustManager) context.getSystemService(TrustManager.class);
        this.mTrustManager = trustManager;
        trustManager.registerTrustListener(this);
        setStrongAuthTracker(this.mStrongAuthTracker);
        this.mDreamManager = IDreamManager.Stub.asInterface(ServiceManager.getService("dreams"));
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.fingerprint")) {
            this.mFpm = (FingerprintManager) context.getSystemService("fingerprint");
        }
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.biometrics.face")) {
            FaceManager faceManager = (FaceManager) context.getSystemService("face");
            this.mFaceManager = faceManager;
            this.mFaceSensorProperties = faceManager.getSensorPropertiesInternal();
        }
        if (this.mFpm != null || this.mFaceManager != null) {
            BiometricManager biometricManager = (BiometricManager) context.getSystemService(BiometricManager.class);
            this.mBiometricManager = biometricManager;
            biometricManager.registerEnabledOnKeyguardCallback(this.mBiometricEnabledCallback);
        }
        FingerprintManager fingerprintManager = this.mFpm;
        if (fingerprintManager != null) {
            fingerprintManager.addLockoutResetCallback(this.mFingerprintLockoutResetCallback);
        }
        FaceManager faceManager2 = this.mFaceManager;
        if (faceManager2 != null) {
            faceManager2.addLockoutResetCallback(this.mFaceLockoutResetCallback);
        }
        this.mIsAutomotive = isAutomotive();
        TaskStackChangeListeners.getInstance().registerTaskStackListener(this.mTaskStackListener);
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        this.mUserManager = userManager;
        this.mIsPrimaryUser = userManager.isPrimaryUser();
        int currentUser = ActivityManager.getCurrentUser();
        this.mUserIsUnlocked.put(currentUser, this.mUserManager.isUserUnlocked(currentUser));
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        this.mDevicePolicyManager = devicePolicyManager;
        this.mLogoutEnabled = devicePolicyManager.isLogoutEnabled();
        updateSecondaryLockscreenRequirement(currentUser);
        for (UserInfo userInfo : this.mUserManager.getUsers()) {
            SparseBooleanArray sparseBooleanArray = this.mUserTrustIsUsuallyManaged;
            int i = userInfo.id;
            sparseBooleanArray.put(i, this.mTrustManager.isTrustUsuallyManaged(i));
        }
        updateAirplaneModeState();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.mTelephonyManager = telephonyManager;
        if (telephonyManager != null) {
            this.mTelephonyListenerManager.addActiveDataSubscriptionIdListener(this.mPhoneStateListener);
            for (int i2 = 0; i2 < this.mTelephonyManager.getActiveModemCount(); i2++) {
                int simState = this.mTelephonyManager.getSimState(i2);
                int[] subscriptionIds = this.mSubscriptionManager.getSubscriptionIds(i2);
                if (subscriptionIds != null) {
                    for (int i3 : subscriptionIds) {
                        this.mHandler.obtainMessage(304, i3, i2, Integer.valueOf(simState)).sendToTarget();
                    }
                }
            }
        }
        updateLockScreenMode(featureFlags.isKeyguardLayoutEnabled());
        this.mTimeFormatChangeObserver = new ContentObserver(this.mHandler) { // from class: com.android.keyguard.KeyguardUpdateMonitor.16
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                KeyguardUpdateMonitor.this.mHandler.sendMessage(KeyguardUpdateMonitor.this.mHandler.obtainMessage(344, Settings.System.getString(KeyguardUpdateMonitor.this.mContext.getContentResolver(), "time_12_24")));
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Settings.System.getUriFor("time_12_24"), false, this.mTimeFormatChangeObserver, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5() {
        Intent registerReceiver;
        int defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
        ServiceState serviceStateForSubscriber = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).getServiceStateForSubscriber(defaultSubscriptionId);
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(330, defaultSubscriptionId, 0, serviceStateForSubscriber));
        if (this.mBatteryStatus == null && (registerReceiver = this.mContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"))) != null && this.mBatteryStatus == null) {
            this.mBroadcastReceiver.onReceive(this.mContext, registerReceiver);
        }
    }

    private void updateLockScreenMode(boolean z) {
        if (z != this.mLockScreenMode) {
            this.mLockScreenMode = z ? 1 : 0;
            this.mHandler.sendEmptyMessage(343);
        }
    }

    private void updateUdfpsEnrolled(int i) {
        this.mIsUdfpsEnrolled = this.mAuthController.isUdfpsEnrolled(i);
    }

    private void updateFaceEnrolled(final int i) {
        this.mIsFaceEnrolled = ((Boolean) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda10
            @Override // java.util.function.Supplier
            public final Object get() {
                Boolean lambda$updateFaceEnrolled$6;
                lambda$updateFaceEnrolled$6 = KeyguardUpdateMonitor.this.lambda$updateFaceEnrolled$6(i);
                return lambda$updateFaceEnrolled$6;
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$updateFaceEnrolled$6(int i) {
        FaceManager faceManager = this.mFaceManager;
        return Boolean.valueOf(faceManager != null && faceManager.isHardwareDetected() && this.mFaceManager.hasEnrolledTemplates(i) && this.mBiometricEnabledForUser.get(i));
    }

    public boolean isUdfpsEnrolled() {
        return this.mIsUdfpsEnrolled;
    }

    public boolean isUdfpsAvailable() {
        return this.mAuthController.getUdfpsProps() != null && !this.mAuthController.getUdfpsProps().isEmpty();
    }

    public boolean isFaceEnrolled() {
        return this.mIsFaceEnrolled;
    }

    private void updateAirplaneModeState() {
        if (!WirelessUtils.isAirplaneModeOn(this.mContext) || this.mHandler.hasMessages(329)) {
            return;
        }
        this.mHandler.sendEmptyMessage(329);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateBiometricListeningState() {
        updateFingerprintListeningState();
        updateFaceListeningState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFingerprintListeningState() {
        if (this.mHandler.hasMessages(336)) {
            return;
        }
        updateUdfpsEnrolled(getCurrentUser());
        boolean shouldListenForFingerprint = shouldListenForFingerprint(isUdfpsEnrolled());
        int i = this.mFingerprintRunningState;
        boolean z = true;
        if (i != 1 && i != 3) {
            z = false;
        }
        if (z && !shouldListenForFingerprint) {
            stopListeningForFingerprint();
        } else if (z || !shouldListenForFingerprint) {
        } else {
            startListeningForFingerprint();
        }
    }

    public boolean isUserUnlocked(int i) {
        return this.mUserIsUnlocked.get(i);
    }

    public void onAuthInterruptDetected(boolean z) {
        Log.d("KeyguardUpdateMonitor", "onAuthInterruptDetected(" + z + ")");
        if (this.mAuthInterruptActive == z) {
            return;
        }
        this.mAuthInterruptActive = z;
        updateFaceListeningState();
    }

    public void requestFaceAuth(boolean z) {
        Log.d("KeyguardUpdateMonitor", "requestFaceAuth() userInitiated=" + z);
        this.mIsFaceAuthUserRequested = z | this.mIsFaceAuthUserRequested;
        updateFaceListeningState();
    }

    public void cancelFaceAuth() {
        stopListeningForFace();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFaceListeningState() {
        updateFaceRecognitionState();
        if (this.mHandler.hasMessages(336)) {
            return;
        }
        this.mHandler.removeCallbacks(this.mRetryFaceAuthentication);
        boolean shouldListenForFace = shouldListenForFace();
        int i = this.mFaceRunningState;
        if (i == 1 && !shouldListenForFace) {
            this.mIsFaceAuthUserRequested = false;
            stopListeningForFace();
        } else if (i == 1 || !shouldListenForFace) {
        } else {
            startListeningForFace();
        }
    }

    private boolean shouldListenForFingerprintAssistant() {
        BiometricAuthenticated biometricAuthenticated = this.mUserFingerprintAuthenticated.get(getCurrentUser());
        if (!this.mAssistantVisible || !this.mKeyguardOccluded) {
            return false;
        }
        return (biometricAuthenticated == null || !biometricAuthenticated.mAuthenticated) && !this.mUserHasTrust.get(getCurrentUser(), false);
    }

    private boolean shouldListenForFaceAssistant() {
        BiometricAuthenticated biometricAuthenticated = this.mUserFaceAuthenticated.get(getCurrentUser());
        if (!this.mAssistantVisible || !this.mKeyguardOccluded) {
            return false;
        }
        return (biometricAuthenticated == null || !biometricAuthenticated.mAuthenticated) && !this.mUserHasTrust.get(getCurrentUser(), false);
    }

    @VisibleForTesting
    protected boolean shouldListenForFingerprint(boolean z) {
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        int currentUser = getCurrentUser();
        boolean z6 = !getUserHasTrust(currentUser);
        boolean shouldListenForFingerprintAssistant = shouldListenForFingerprintAssistant();
        boolean z7 = this.mKeyguardIsVisible || !this.mDeviceInteractive || (this.mBouncer && !this.mKeyguardGoingAway) || this.mGoingToSleep || shouldListenForFingerprintAssistant || (((z5 = this.mKeyguardOccluded) && this.mIsDreaming) || (z5 && z6 && (this.mOccludingAppRequestingFp || z)));
        Log.i("KeyguardUpdateMonitor", "shouldListenKeyguardState:" + z7 + "， mKeyguardIsVisible=" + this.mKeyguardIsVisible + "，mDeviceInteractive=" + this.mDeviceInteractive + ", mBouncer=" + this.mBouncer + ", mKeyguardGoingAway=" + this.mKeyguardGoingAway + ", mGoingToSleep=" + this.mGoingToSleep + ", shouldListenForFingerprintAssistant=" + shouldListenForFingerprintAssistant + ", mKeyguardOccluded=" + this.mKeyguardOccluded + ", mIsDreaming=" + this.mIsDreaming + ", userDoesNotHaveTrust, mOccludingAppRequestingFp=" + this.mOccludingAppRequestingFp + ", isUdfps=" + z + ", mStatusBarState=" + this.mStatusBarState);
        boolean z8 = this.mBiometricEnabledForUser.get(currentUser);
        boolean userCanSkipBouncer = getUserCanSkipBouncer(currentUser);
        boolean isFingerprintDisabled = isFingerprintDisabled(currentUser);
        boolean z9 = !this.mSwitchingUser && !isFingerprintDisabled && (!this.mKeyguardGoingAway || !this.mDeviceInteractive) && this.mIsPrimaryUser && z8;
        Log.i("KeyguardUpdateMonitor", "shouldListenUserState:" + z9 + ", mSwitchingUser=" + this.mSwitchingUser + ", fingerprintDisabledForUser=" + isFingerprintDisabled + ", mKeyguardGoingAway=" + this.mKeyguardGoingAway + ", mDeviceInteractive=" + this.mDeviceInteractive + ", mIsPrimaryUser=" + this.mIsPrimaryUser + ", biometricEnabledForUser=" + z8);
        boolean z10 = !this.mFingerprintLockedOut || !this.mBouncer || !this.mCredentialAttempted;
        Log.i("KeyguardUpdateMonitor", "shouldListenBouncerState:" + z10 + ", mFingerprintLockedOut=" + this.mFingerprintLockedOut + ", mBouncer=" + this.mBouncer + ", mCredentialAttempted=" + this.mCredentialAttempted);
        boolean isEncryptedOrLockdown = isEncryptedOrLockdown(currentUser);
        boolean userNeedsStrongAuth = userNeedsStrongAuth();
        if (!z || (!userCanSkipBouncer && !isEncryptedOrLockdown && !userNeedsStrongAuth && z6 && !this.mFingerprintLockedOut)) {
            z2 = shouldListenForFingerprintAssistant;
            z3 = true;
        } else {
            z2 = shouldListenForFingerprintAssistant;
            z3 = false;
        }
        Log.i("KeyguardUpdateMonitor", "shouldListenUdfpsState:" + z3 + ", isUdfps=" + z + ", userCanSkipBouncer=" + userCanSkipBouncer + ", isEncryptedOrLockdownForUser=" + isEncryptedOrLockdown + ", userNeedsStrongAuth=" + userNeedsStrongAuth + ", userDoesNotHaveTrust=" + z6 + ", mFingerprintLockedOut=" + this.mFingerprintLockedOut + ", mShowing=" + this.mShowing);
        boolean z11 = z7 && z9 && z10 && z3 && !this.mFaceRecognitionSucceeded && this.mShowing;
        Log.i("KeyguardUpdateMonitor", "shouldListenForFingerprint:" + z11 + ", shouldListenKeyguardState=" + z7 + ", shouldListenUserState=" + z9 + ", shouldListenBouncerState=" + z10 + ", shouldListenUdfpsState=" + z3 + ", shouldFaceRecognition=" + (true ^ z4));
        if (!DEBUG_FINGERPRINT) {
            return z11;
        }
        boolean z12 = z11;
        maybeLogListenerModelData(new KeyguardFingerprintListenModel(System.currentTimeMillis(), currentUser, z11, z8, this.mBouncer, userCanSkipBouncer, this.mCredentialAttempted, this.mDeviceInteractive, this.mIsDreaming, isEncryptedOrLockdown, isFingerprintDisabled, this.mFingerprintLockedOut, this.mGoingToSleep, this.mKeyguardGoingAway, this.mKeyguardIsVisible, this.mKeyguardOccluded, this.mOccludingAppRequestingFp, this.mIsPrimaryUser, z2, this.mSwitchingUser, z, z6, userNeedsStrongAuth));
        return z12;
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
        KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
        boolean z5 = keyguardBypassController != null && keyguardBypassController.canBypass();
        boolean z6 = !getUserCanSkipBouncer(currentUser) || z5;
        boolean z7 = (!z3 || (!this.mFaceSensorProperties.isEmpty() && this.mFaceSensorProperties.get(0).supportsFaceDetection)) ? !z4 || (z5 && !this.mBouncer) : false;
        boolean isFaceAuthenticated = getIsFaceAuthenticated();
        boolean isFaceDisabled = isFaceDisabled(currentUser);
        boolean z8 = this.mBiometricEnabledForUser.get(currentUser);
        boolean shouldListenForFaceAssistant = shouldListenForFaceAssistant();
        if ((this.mBouncer || this.mAuthInterruptActive || this.mOccludingAppRequestingFace || z2 || shouldListenForFaceAssistant) && !this.mSwitchingUser && !isFaceDisabled && z6 && !this.mKeyguardGoingAway && z8 && !this.mLockIconPressed && z7 && this.mIsPrimaryUser && ((!this.mSecureCameraLaunched || this.mOccludingAppRequestingFace) && !isFaceAuthenticated)) {
            z = true;
        }
        if (DEBUG_FACE) {
            maybeLogListenerModelData(new KeyguardFaceListenModel(System.currentTimeMillis(), currentUser, z, this.mAuthInterruptActive, z6, z8, this.mBouncer, isFaceAuthenticated, isFaceDisabled, z2, this.mKeyguardGoingAway, shouldListenForFaceAssistant, this.mLockIconPressed, this.mOccludingAppRequestingFace, this.mIsPrimaryUser, z7, this.mSecureCameraLaunched, this.mSwitchingUser));
        }
        return z;
    }

    private void maybeLogListenerModelData(KeyguardListenModel keyguardListenModel) {
        boolean z = true;
        if ((!DEBUG_FACE || !(keyguardListenModel instanceof KeyguardFaceListenModel) || this.mFaceRunningState == 1) && (!DEBUG_FINGERPRINT || !(keyguardListenModel instanceof KeyguardFingerprintListenModel) || this.mFingerprintRunningState == 1)) {
            z = false;
        }
        if (!z || !keyguardListenModel.getListening()) {
            return;
        }
        this.mListenModels.add(keyguardListenModel);
    }

    private void startListeningForFingerprint() {
        int currentUser = getCurrentUser();
        boolean isUnlockWithFingerprintPossible = isUnlockWithFingerprintPossible(currentUser);
        if (this.mFingerprintCancelSignal != null) {
            Log.e("KeyguardUpdateMonitor", "Cancellation signal is not null, high chance of bug in fp auth lifecycle management. FP state: " + this.mFingerprintRunningState + ", unlockPossible: " + isUnlockWithFingerprintPossible);
        }
        int i = this.mFingerprintRunningState;
        if (i == 2) {
            setFingerprintRunningState(3);
        } else if (i == 3) {
        } else {
            Log.v("KeyguardUpdateMonitor", "startListeningForFingerprint()");
            if (!isUnlockWithFingerprintPossible) {
                return;
            }
            this.mFingerprintCancelSignal = new CancellationSignal();
            if (isEncryptedOrLockdown(currentUser)) {
                this.mFpm.detectFingerprint(this.mFingerprintCancelSignal, this.mFingerprintDetectionCallback, currentUser);
            } else {
                this.mFpm.authenticate(null, this.mFingerprintCancelSignal, this.mFingerprintAuthenticationCallback, null, -1, currentUser);
            }
            setFingerprintRunningState(1);
        }
    }

    private void startListeningForFace() {
        int currentUser = getCurrentUser();
        boolean isUnlockWithFacePossible = isUnlockWithFacePossible(currentUser);
        if (this.mFaceCancelSignal != null) {
            Log.e("KeyguardUpdateMonitor", "Cancellation signal is not null, high chance of bug in face auth lifecycle management. Face state: " + this.mFaceRunningState + ", unlockPossible: " + isUnlockWithFacePossible);
        }
        int i = this.mFaceRunningState;
        if (i == 2) {
            setFaceRunningState(3);
        } else if (i == 3) {
        } else {
            Log.v("KeyguardUpdateMonitor", "startListeningForFace(): " + this.mFaceRunningState);
            if (!isUnlockWithFacePossible) {
                return;
            }
            this.mFaceCancelSignal = new CancellationSignal();
            boolean z = !this.mFaceSensorProperties.isEmpty() && this.mFaceSensorProperties.get(0).supportsFaceDetection;
            if (isEncryptedOrLockdown(currentUser) && z) {
                this.mFaceManager.detectFace(this.mFaceCancelSignal, this.mFaceDetectionCallback, currentUser);
            } else {
                KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
                this.mFaceManager.authenticate((CryptoObject) null, this.mFaceCancelSignal, this.mFaceAuthenticationCallback, (Handler) null, currentUser, keyguardBypassController != null && keyguardBypassController.isBypassEnabled());
            }
            setFaceRunningState(1);
        }
    }

    public boolean isUnlockingWithBiometricsPossible(int i) {
        return isUnlockWithFacePossible(i) || isUnlockWithFingerprintPossible(i);
    }

    private boolean isUnlockWithFingerprintPossible(int i) {
        FingerprintManager fingerprintManager = this.mFpm;
        return fingerprintManager != null && fingerprintManager.isHardwareDetected() && !isFingerprintDisabled(i) && this.mFpm.hasEnrolledTemplates(i);
    }

    private boolean isUnlockWithFacePossible(int i) {
        return isFaceAuthEnabledForUser(i) && !isFaceDisabled(i);
    }

    public boolean isFaceAuthEnabledForUser(int i) {
        updateFaceEnrolled(i);
        return this.mIsFaceEnrolled;
    }

    private void stopListeningForFingerprint() {
        Log.v("KeyguardUpdateMonitor", "stopListeningForFingerprint()");
        if (this.mFingerprintRunningState == 1) {
            CancellationSignal cancellationSignal = this.mFingerprintCancelSignal;
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
                this.mFingerprintCancelSignal = null;
                this.mHandler.removeCallbacks(this.mFpCancelNotReceived);
                this.mHandler.postDelayed(this.mFpCancelNotReceived, 1000L);
            }
            setFingerprintRunningState(2);
        }
        if (this.mFingerprintRunningState == 3) {
            setFingerprintRunningState(2);
        }
    }

    private void stopListeningForFace() {
        Log.v("KeyguardUpdateMonitor", "stopListeningForFace()");
        if (this.mFaceRunningState == 1) {
            CancellationSignal cancellationSignal = this.mFaceCancelSignal;
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
                this.mFaceCancelSignal = null;
                this.mHandler.removeCallbacks(this.mFaceCancelNotReceived);
                this.mHandler.postDelayed(this.mFaceCancelNotReceived, 1000L);
            }
            setFaceRunningState(2);
        }
        if (this.mFaceRunningState == 3) {
            setFaceRunningState(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDeviceProvisionedInSettingsDb() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0;
    }

    private void watchForDeviceProvisioning() {
        this.mDeviceProvisionedObserver = new ContentObserver(this.mHandler) { // from class: com.android.keyguard.KeyguardUpdateMonitor.18
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                super.onChange(z);
                KeyguardUpdateMonitor keyguardUpdateMonitor = KeyguardUpdateMonitor.this;
                keyguardUpdateMonitor.mDeviceProvisioned = keyguardUpdateMonitor.isDeviceProvisionedInSettingsDb();
                if (KeyguardUpdateMonitor.this.mDeviceProvisioned) {
                    KeyguardUpdateMonitor.this.mHandler.sendEmptyMessage(308);
                }
                Log.d("KeyguardUpdateMonitor", "DEVICE_PROVISIONED state = " + KeyguardUpdateMonitor.this.mDeviceProvisioned);
            }
        };
        this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("device_provisioned"), false, this.mDeviceProvisionedObserver);
        boolean isDeviceProvisionedInSettingsDb = isDeviceProvisionedInSettingsDb();
        if (isDeviceProvisionedInSettingsDb != this.mDeviceProvisioned) {
            this.mDeviceProvisioned = isDeviceProvisionedInSettingsDb;
            if (!isDeviceProvisionedInSettingsDb) {
                return;
            }
            this.mHandler.sendEmptyMessage(308);
        }
    }

    public void setHasLockscreenWallpaper(boolean z) {
        Assert.isMainThread();
        if (z != this.mHasLockscreenWallpaper) {
            this.mHasLockscreenWallpaper = z;
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onHasLockscreenWallpaperChanged(z);
                }
            }
        }
    }

    public boolean hasLockscreenWallpaper() {
        return this.mHasLockscreenWallpaper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDevicePolicyManagerStateChanged(int i) {
        Assert.isMainThread();
        updateFingerprintListeningState();
        updateSecondaryLockscreenRequirement(i);
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onDevicePolicyManagerStateChanged();
            }
        }
    }

    @VisibleForTesting
    void handleUserSwitching(int i, IRemoteCallback iRemoteCallback) {
        Assert.isMainThread();
        clearBiometricRecognized();
        this.mUserTrustIsUsuallyManaged.put(i, this.mTrustManager.isTrustUsuallyManaged(i));
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onUserSwitching(i);
            }
        }
        try {
            iRemoteCallback.sendResult((Bundle) null);
        } catch (RemoteException unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUserSwitchComplete(int i) {
        Assert.isMainThread();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onUserSwitchComplete(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDeviceProvisioned() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onDeviceProvisioned();
            }
        }
        if (this.mDeviceProvisionedObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mDeviceProvisionedObserver);
            this.mDeviceProvisionedObserver = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePhoneStateChanged(String str) {
        Assert.isMainThread();
        Log.d("KeyguardUpdateMonitor", "handlePhoneStateChanged(" + str + ")");
        if (TelephonyManager.EXTRA_STATE_IDLE.equals(str)) {
            this.mPhoneState = 0;
        } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(str)) {
            this.mPhoneState = 2;
        } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(str)) {
            this.mPhoneState = 1;
        }
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onPhoneStateChanged(this.mPhoneState);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRingerModeChange(int i) {
        Assert.isMainThread();
        Log.d("KeyguardUpdateMonitor", "handleRingerModeChange(" + i + ")");
        this.mRingMode = i;
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onRingerModeChanged(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTimeUpdate() {
        Assert.isMainThread();
        Log.d("KeyguardUpdateMonitor", "handleTimeUpdate");
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTimeChanged();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLockScreenMode() {
        Assert.isMainThread();
        Log.d("KeyguardUpdateMonitor", "handleLockScreenMode(" + this.mLockScreenMode + ")");
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onLockScreenModeChanged(this.mLockScreenMode);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTimeZoneUpdate(String str) {
        Assert.isMainThread();
        Log.d("KeyguardUpdateMonitor", "handleTimeZoneUpdate");
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTimeZoneChanged(TimeZone.getTimeZone(str));
                keyguardUpdateMonitorCallback.onTimeChanged();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTimeFormatUpdate(String str) {
        Assert.isMainThread();
        Log.d("KeyguardUpdateMonitor", "handleTimeFormatUpdate timeFormat=" + str);
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTimeFormatChanged(str);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBatteryUpdate(BatteryStatus batteryStatus) {
        Assert.isMainThread();
        Log.d("KeyguardUpdateMonitor", "handleBatteryUpdate");
        boolean isBatteryUpdateInteresting = isBatteryUpdateInteresting(this.mBatteryStatus, batteryStatus);
        this.mBatteryStatus = batteryStatus;
        if (isBatteryUpdateInteresting) {
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onRefreshBatteryInfo(batteryStatus);
                }
            }
        }
    }

    @VisibleForTesting
    void updateTelephonyCapable(boolean z) {
        Assert.isMainThread();
        if (z == this.mTelephonyCapable) {
            return;
        }
        this.mTelephonyCapable = z;
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTelephonyCapable(this.mTelephonyCapable);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00a8  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0086  */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void handleSimStateChange(int i, int i2, int i3) {
        boolean z;
        SimData simData;
        Assert.isMainThread();
        Log.d("KeyguardUpdateMonitor", "handleSimStateChange(subId=" + i + ", slotId=" + i2 + ", state=" + i3 + ")");
        boolean z2 = true;
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            Log.w("KeyguardUpdateMonitor", "invalid subId in handleSimStateChange()");
            if (i3 == 1) {
                updateTelephonyCapable(true);
                for (SimData simData2 : this.mSimDatas.values()) {
                    if (simData2.slotId == i2) {
                        simData2.simState = 1;
                    }
                }
                z = true;
                simData = this.mSimDatas.get(Integer.valueOf(i2));
                if (simData != null) {
                    this.mSimDatas.put(Integer.valueOf(i2), new SimData(i3, i2, i));
                } else {
                    if (simData.simState == i3 && simData.subId == i && simData.slotId == i2) {
                        z2 = false;
                    }
                    simData.simState = i3;
                    simData.subId = i;
                    simData.slotId = i2;
                }
                if ((z2 && !z) || i3 == 0) {
                    return;
                }
                for (int i4 = 0; i4 < this.mCallbacks.size(); i4++) {
                    KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i4).get();
                    if (keyguardUpdateMonitorCallback != null) {
                        keyguardUpdateMonitorCallback.onSimStateChanged(i, i2, i3);
                    }
                }
            } else if (i3 != 8) {
                return;
            } else {
                updateTelephonyCapable(true);
            }
        }
        z = false;
        simData = this.mSimDatas.get(Integer.valueOf(i2));
        if (simData != null) {
        }
        if (z2) {
        }
        while (i4 < this.mCallbacks.size()) {
        }
    }

    @VisibleForTesting
    void handleServiceStateChange(int i, ServiceState serviceState) {
        Log.d("KeyguardUpdateMonitor", "handleServiceStateChange(subId=" + i + ", serviceState=" + serviceState);
        int i2 = 0;
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            Log.w("KeyguardUpdateMonitor", "invalid subId in handleServiceStateChange()");
            while (i2 < this.mCallbacks.size()) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
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
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback2 = this.mCallbacks.get(i2).get();
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
        Log.d("KeyguardUpdateMonitor", "onKeyguardVisibilityChanged(" + z + ")");
        this.mKeyguardIsVisible = z;
        if (z) {
            this.mSecureCameraLaunched = false;
        }
        KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
        if (keyguardBypassController != null) {
            keyguardBypassController.setUserHasDeviceEntryIntent(false);
        }
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onKeyguardVisibilityChangedRaw(z);
            }
        }
        updateBiometricListeningState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleKeyguardReset() {
        Log.d("KeyguardUpdateMonitor", "handleKeyguardReset");
        updateBiometricListeningState();
        this.mNeedsSlowUnlockTransition = resolveNeedsSlowUnlockTransition();
    }

    private boolean resolveNeedsSlowUnlockTransition() {
        if (isUserUnlocked(getCurrentUser())) {
            return false;
        }
        ResolveInfo resolveActivityAsUser = this.mContext.getPackageManager().resolveActivityAsUser(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME"), 0, getCurrentUser());
        if (resolveActivityAsUser == null) {
            Log.w("KeyguardUpdateMonitor", "resolveNeedsSlowUnlockTransition: returning false since activity could not be resolved.");
            return false;
        }
        return FALLBACK_HOME_COMPONENT.equals(resolveActivityAsUser.getComponentInfo().getComponentName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleKeyguardBouncerChanged(int i) {
        Assert.isMainThread();
        Log.d("KeyguardUpdateMonitor", "handleKeyguardBouncerChanged(" + i + ")");
        boolean z = true;
        if (i != 1) {
            z = false;
        }
        this.mBouncer = z;
        if (z) {
            this.mSecureCameraLaunched = false;
        } else {
            this.mCredentialAttempted = false;
        }
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i2).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onKeyguardBouncerChanged(this.mBouncer);
            }
        }
        updateBiometricListeningState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRequireUnlockForNfc() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onRequireUnlockForNfc();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleReportEmergencyCallAction() {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
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

    public void removeCallback(final KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback) {
        Assert.isMainThread();
        Log.v("KeyguardUpdateMonitor", "*** unregister callback for " + keyguardUpdateMonitorCallback);
        this.mCallbacks.removeIf(new Predicate() { // from class: com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$removeCallback$7;
                lambda$removeCallback$7 = KeyguardUpdateMonitor.lambda$removeCallback$7(KeyguardUpdateMonitorCallback.this, (WeakReference) obj);
                return lambda$removeCallback$7;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$removeCallback$7(KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback, WeakReference weakReference) {
        return weakReference.get() == keyguardUpdateMonitorCallback;
    }

    public void registerCallback(KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback) {
        Assert.isMainThread();
        Log.v("KeyguardUpdateMonitor", "*** register callback for " + keyguardUpdateMonitorCallback);
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            if (this.mCallbacks.get(i).get() == keyguardUpdateMonitorCallback) {
                Log.e("KeyguardUpdateMonitor", "Object tried to add another callback", new Exception("Called by"));
                return;
            }
        }
        this.mCallbacks.add(new WeakReference<>(keyguardUpdateMonitorCallback));
        removeCallback(null);
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
        this.mHandler.post(this.mUpdateBiometricListeningState);
    }

    private void sendUpdates(KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback) {
        keyguardUpdateMonitorCallback.onRefreshBatteryInfo(this.mBatteryStatus);
        keyguardUpdateMonitorCallback.onTimeChanged();
        keyguardUpdateMonitorCallback.onRingerModeChanged(this.mRingMode);
        keyguardUpdateMonitorCallback.onPhoneStateChanged(this.mPhoneState);
        keyguardUpdateMonitorCallback.onRefreshCarrierInfo();
        keyguardUpdateMonitorCallback.onClockVisibilityChanged();
        keyguardUpdateMonitorCallback.onKeyguardVisibilityChangedRaw(this.mKeyguardIsVisible);
        keyguardUpdateMonitorCallback.onTelephonyCapable(this.mTelephonyCapable);
        keyguardUpdateMonitorCallback.onLockScreenModeChanged(this.mLockScreenMode);
        for (Map.Entry<Integer, SimData> entry : this.mSimDatas.entrySet()) {
            SimData value = entry.getValue();
            keyguardUpdateMonitorCallback.onSimStateChanged(value.subId, value.slotId, value.simState);
        }
    }

    public void sendKeyguardReset() {
        this.mHandler.obtainMessage(312).sendToTarget();
    }

    public void sendKeyguardBouncerChanged(boolean z) {
        Log.d("KeyguardUpdateMonitor", "sendKeyguardBouncerChanged(" + z + ")");
        Message obtainMessage = this.mHandler.obtainMessage(322);
        obtainMessage.arg1 = z ? 1 : 0;
        obtainMessage.sendToTarget();
    }

    public void reportSimUnlocked(int i) {
        Log.v("KeyguardUpdateMonitor", "reportSimUnlocked(subId=" + i + ")");
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
        Assert.isMainThread();
        this.mUserFingerprintAuthenticated.clear();
        this.mUserFaceAuthenticated.clear();
        this.mTrustManager.clearAllBiometricRecognized(BiometricSourceType.FINGERPRINT);
        this.mTrustManager.clearAllBiometricRecognized(BiometricSourceType.FACE);
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onBiometricsCleared();
            }
        }
    }

    public boolean isSimPinVoiceSecure() {
        return isSimPinSecure();
    }

    public boolean isSimPinSecure() {
        for (SubscriptionInfo subscriptionInfo : getSubscriptionInfo(false)) {
            if (isSimPinSecure(getSimState(subscriptionInfo.getSubscriptionId()))) {
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
        if (simData.simState != simState || simData.slotId != i2) {
            z = true;
        }
        simData.simState = simState;
        simData.slotId = i2;
        return z;
    }

    public void dispatchStartedWakingUp() {
        synchronized (this) {
            this.mDeviceInteractive = true;
        }
        this.mHandler.sendEmptyMessage(319);
    }

    public void dispatchStartedGoingToSleep(int i) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(321, i, 0));
    }

    public void dispatchFinishedGoingToSleep(int i) {
        synchronized (this) {
            this.mDeviceInteractive = false;
        }
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(320, i, 0));
    }

    public void dispatchScreenTurnedOn() {
        synchronized (this) {
            this.mScreenOn = true;
        }
        this.mHandler.sendEmptyMessage(331);
    }

    public void dispatchScreenTurnedOff() {
        synchronized (this) {
            this.mScreenOn = false;
        }
        this.mHandler.sendEmptyMessage(332);
    }

    public void dispatchDreamingStarted() {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(333, 1, 0));
    }

    public void dispatchDreamingStopped() {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(333, 0, 0));
    }

    public void dispatchKeyguardGoingAway(boolean z) {
        Handler handler = this.mHandler;
        handler.sendMessage(handler.obtainMessage(342, Boolean.valueOf(z)));
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

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLogoutEnabled() {
        Assert.isMainThread();
        boolean isLogoutEnabled = this.mDevicePolicyManager.isLogoutEnabled();
        if (this.mLogoutEnabled != isLogoutEnabled) {
            this.mLogoutEnabled = isLogoutEnabled;
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
                if (keyguardUpdateMonitorCallback != null) {
                    keyguardUpdateMonitorCallback.onLogoutEnabledChanged();
                }
            }
        }
    }

    public boolean isOOS() {
        int activeModemCount = this.mTelephonyManager.getActiveModemCount();
        boolean z = true;
        for (int i = 0; i < activeModemCount; i++) {
            int[] subscriptionIds = this.mSubscriptionManager.getSubscriptionIds(i);
            if (subscriptionIds != null && subscriptionIds.length >= 1) {
                Log.d("KeyguardUpdateMonitor", "slot id:" + i + " subId:" + subscriptionIds[0]);
                ServiceState serviceState = this.mServiceStates.get(Integer.valueOf(subscriptionIds[0]));
                if (serviceState != null) {
                    if (serviceState.isEmergencyOnly() || (serviceState.getVoiceRegState() != 1 && serviceState.getVoiceRegState() != 3)) {
                        z = false;
                    }
                    Log.d("KeyguardUpdateMonitor", "is emergency: " + serviceState.isEmergencyOnly() + "voice state: " + serviceState.getVoiceRegState());
                } else {
                    Log.d("KeyguardUpdateMonitor", "state is NULL");
                }
            }
        }
        return z;
    }

    /* JADX WARN: Removed duplicated region for block: B:54:0x03e4  */
    /* JADX WARN: Removed duplicated region for block: B:57:? A[RETURN, SYNTHETIC] */
    @Override // com.android.systemui.Dumpable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        FaceManager faceManager;
        printWriter.println("KeyguardUpdateMonitor state:");
        printWriter.println("  SIM States:");
        Iterator<SimData> it = this.mSimDatas.values().iterator();
        while (it.hasNext()) {
            printWriter.println("    " + it.next().toString());
        }
        printWriter.println("  Subs:");
        if (this.mSubscriptionInfo != null) {
            for (int i = 0; i < this.mSubscriptionInfo.size(); i++) {
                printWriter.println("    " + this.mSubscriptionInfo.get(i));
            }
        }
        printWriter.println("  Current active data subId=" + this.mActiveMobileDataSubscription);
        printWriter.println("  Service states:");
        for (Integer num : this.mServiceStates.keySet()) {
            int intValue = num.intValue();
            printWriter.println("    " + intValue + "=" + this.mServiceStates.get(Integer.valueOf(intValue)));
        }
        FingerprintManager fingerprintManager = this.mFpm;
        String str = "    enabledByUser=";
        if (fingerprintManager != null && fingerprintManager.isHardwareDetected()) {
            int currentUser = ActivityManager.getCurrentUser();
            int strongAuthForUser = this.mStrongAuthTracker.getStrongAuthForUser(currentUser);
            BiometricAuthenticated biometricAuthenticated = this.mUserFingerprintAuthenticated.get(currentUser);
            printWriter.println("  Fingerprint state (user=" + currentUser + ")");
            StringBuilder sb = new StringBuilder();
            sb.append("    allowed=");
            sb.append(biometricAuthenticated != null && isUnlockingWithBiometricAllowed(biometricAuthenticated.mIsStrongBiometric));
            printWriter.println(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("    auth'd=");
            sb2.append(biometricAuthenticated != null && biometricAuthenticated.mAuthenticated);
            printWriter.println(sb2.toString());
            printWriter.println("    authSinceBoot=" + getStrongAuthTracker().hasUserAuthenticatedSinceBoot());
            printWriter.println("    disabled(DPM)=" + isFingerprintDisabled(currentUser));
            printWriter.println("    possible=" + isUnlockWithFingerprintPossible(currentUser));
            printWriter.println("    listening: actual=" + this.mFingerprintRunningState + " expected=" + (shouldListenForFingerprint(isUdfpsEnrolled()) ? 1 : 0));
            StringBuilder sb3 = new StringBuilder();
            sb3.append("    strongAuthFlags=");
            sb3.append(Integer.toHexString(strongAuthForUser));
            printWriter.println(sb3.toString());
            printWriter.println("    trustManaged=" + getUserTrustIsManaged(currentUser));
            printWriter.println("    udfpsEnrolled=" + isUdfpsEnrolled());
            printWriter.println("    mFingerprintLockedOut=" + this.mFingerprintLockedOut);
            printWriter.println("    mFingerprintLockedOutPermanent=" + this.mFingerprintLockedOutPermanent);
            StringBuilder sb4 = new StringBuilder();
            str = str;
            sb4.append(str);
            sb4.append(this.mBiometricEnabledForUser.get(currentUser));
            printWriter.println(sb4.toString());
            if (isUdfpsEnrolled()) {
                printWriter.println("        shouldListenForUdfps=" + shouldListenForFingerprint(true));
                printWriter.println("        bouncerVisible=" + this.mBouncer);
                printWriter.println("        mStatusBarState=" + StatusBarState.toShortString(this.mStatusBarState));
                faceManager = this.mFaceManager;
                if (faceManager != null && faceManager.isHardwareDetected()) {
                    int currentUser2 = ActivityManager.getCurrentUser();
                    int strongAuthForUser2 = this.mStrongAuthTracker.getStrongAuthForUser(currentUser2);
                    BiometricAuthenticated biometricAuthenticated2 = this.mUserFaceAuthenticated.get(currentUser2);
                    StringBuilder sb5 = new StringBuilder();
                    String str2 = str;
                    sb5.append("  Face authentication state (user=");
                    sb5.append(currentUser2);
                    sb5.append(")");
                    printWriter.println(sb5.toString());
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("    allowed=");
                    sb6.append(biometricAuthenticated2 == null && isUnlockingWithBiometricAllowed(biometricAuthenticated2.mIsStrongBiometric));
                    printWriter.println(sb6.toString());
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("    auth'd=");
                    sb7.append(biometricAuthenticated2 == null && biometricAuthenticated2.mAuthenticated);
                    printWriter.println(sb7.toString());
                    printWriter.println("    authSinceBoot=" + getStrongAuthTracker().hasUserAuthenticatedSinceBoot());
                    printWriter.println("    disabled(DPM)=" + isFaceDisabled(currentUser2));
                    printWriter.println("    possible=" + isUnlockWithFacePossible(currentUser2));
                    printWriter.println("    listening: actual=" + this.mFaceRunningState + " expected=(" + (shouldListenForFace() ? 1 : 0));
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("    strongAuthFlags=");
                    sb8.append(Integer.toHexString(strongAuthForUser2));
                    printWriter.println(sb8.toString());
                    printWriter.println("    trustManaged=" + getUserTrustIsManaged(currentUser2));
                    printWriter.println("    mFaceLockedOutPermanent=" + this.mFaceLockedOutPermanent);
                    printWriter.println(str2 + this.mBiometricEnabledForUser.get(currentUser2));
                    printWriter.println("    mSecureCameraLaunched=" + this.mSecureCameraLaunched);
                }
                this.mListenModels.print(printWriter);
                if (this.mIsAutomotive) {
                    return;
                }
                printWriter.println("  Running on Automotive build");
                return;
            }
        }
        faceManager = this.mFaceManager;
        if (faceManager != null) {
            int currentUser22 = ActivityManager.getCurrentUser();
            int strongAuthForUser22 = this.mStrongAuthTracker.getStrongAuthForUser(currentUser22);
            BiometricAuthenticated biometricAuthenticated22 = this.mUserFaceAuthenticated.get(currentUser22);
            StringBuilder sb52 = new StringBuilder();
            String str22 = str;
            sb52.append("  Face authentication state (user=");
            sb52.append(currentUser22);
            sb52.append(")");
            printWriter.println(sb52.toString());
            StringBuilder sb62 = new StringBuilder();
            sb62.append("    allowed=");
            sb62.append(biometricAuthenticated22 == null && isUnlockingWithBiometricAllowed(biometricAuthenticated22.mIsStrongBiometric));
            printWriter.println(sb62.toString());
            StringBuilder sb72 = new StringBuilder();
            sb72.append("    auth'd=");
            sb72.append(biometricAuthenticated22 == null && biometricAuthenticated22.mAuthenticated);
            printWriter.println(sb72.toString());
            printWriter.println("    authSinceBoot=" + getStrongAuthTracker().hasUserAuthenticatedSinceBoot());
            printWriter.println("    disabled(DPM)=" + isFaceDisabled(currentUser22));
            printWriter.println("    possible=" + isUnlockWithFacePossible(currentUser22));
            printWriter.println("    listening: actual=" + this.mFaceRunningState + " expected=(" + (shouldListenForFace() ? 1 : 0));
            StringBuilder sb82 = new StringBuilder();
            sb82.append("    strongAuthFlags=");
            sb82.append(Integer.toHexString(strongAuthForUser22));
            printWriter.println(sb82.toString());
            printWriter.println("    trustManaged=" + getUserTrustIsManaged(currentUser22));
            printWriter.println("    mFaceLockedOutPermanent=" + this.mFaceLockedOutPermanent);
            printWriter.println(str22 + this.mBiometricEnabledForUser.get(currentUser22));
            printWriter.println("    mSecureCameraLaunched=" + this.mSecureCameraLaunched);
        }
        this.mListenModels.print(printWriter);
        if (this.mIsAutomotive) {
        }
    }

    public void setFaceRecognitionControllerRef(FaceRecognitionCallback faceRecognitionCallback) {
        Log.d("KeyguardUpdateMonitor", "setFaceRecognitionControllerRef: ");
        this.mFaceRecognitionCallbackRef = new WeakReference<>(faceRecognitionCallback);
    }

    public boolean isFaceRecognitionEnable() {
        return Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "nt_face_management_added_data", 0, -2) > 0;
    }

    public synchronized void updateFaceRecognitionState() {
        boolean shouldListenForFaceRecognition = shouldListenForFaceRecognition();
        Log.d("KeyguardUpdateMonitor", "shouldListenForFaceRecognition :" + shouldListenForFaceRecognition);
        if (this.mFaceRecognitionCallbackRef == null) {
            setFaceRecognitionControllerRef((FaceRecognitionCallback) Dependency.get(FaceRecognitionController.class));
        }
        WeakReference<FaceRecognitionCallback> weakReference = this.mFaceRecognitionCallbackRef;
        if (weakReference != null && weakReference.get() != null) {
            boolean isCameraPreviewStarting = this.mFaceRecognitionCallbackRef.get().isCameraPreviewStarting();
            Log.d("KeyguardUpdateMonitor", "updateFaceRecognitionState:  isCameraStart = " + isCameraPreviewStarting);
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
        Log.d("KeyguardUpdateMonitor", "mKeyguardIsVisible " + this.mKeyguardIsVisible + " mBouncer:" + this.mBouncer + " awakeKeyguard = " + z3 + "  satisfiedCondition:" + z + " isEnableFaceRecognition:" + isFaceRecognitionEnable + "  mSwitchingUser:" + this.mSwitchingUser + "  isSimPinSecure:" + isSimPinSecure() + "  isFaceRecognitionSucceeded = " + isFaceRecognitionSucceeded() + "  isUserUnlocked = " + this.mUserManager.isUserUnlocked() + "  statusBarShadeLocked = " + z2 + "  mPanelDown = " + this.mPanelDown + " userNeedsStrongAuth = " + userNeedsStrongAuth);
        return z && z3 && !this.mBouncer && this.mUserManager.isUserUnlocked() && !this.mSwitchingUser && !isSimPinSecure() && !isFaceRecognitionSucceeded() && !this.mPanelDown && !userNeedsStrongAuth;
    }

    public void onFaceRecognitionSucceeded(boolean z) {
        if (z != this.mFaceRecognitionSucceeded) {
            this.mFaceRecognitionSucceeded = z;
            this.mHandler.obtainMessage(346).sendToTarget();
            SystemUIEventUtils.collectUnLockResults(this.mContext, z ? "unlock_success" : "unlock_fail", 5);
        }
    }

    public boolean isFaceRecognitionSucceeded() {
        return this.mFaceRecognitionSucceeded;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFaceRecognitionSucceeded(boolean z) {
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onFaceRecognitionSucceeded(z);
            }
        }
    }

    public void handleTapGestureDown() {
        Log.d("KeyguardUpdateMonitor", "handleTapGestureDown: ");
        if (this.mAuthController.getSensorRectF() == null) {
            return;
        }
        ((StatusBar) Dependency.get(StatusBar.class)).onFingerprintDown();
    }

    public void handleTapGestureUp() {
        Log.d("KeyguardUpdateMonitor", "handleTapGestureUp: ");
        if (this.mAuthController.getSensorRectF() == null) {
            return;
        }
        ((StatusBar) Dependency.get(StatusBar.class)).onFingerprintUp();
    }

    public void dismissFingerprintIcon() {
        this.mAuthController.dismissFingerprintIcon();
    }

    public void setIsFaceCameraStarting(boolean z) {
        this.mIsFaceCameraStarting = z;
    }

    public boolean isFaceCameraStarting() {
        return this.mIsFaceCameraStarting;
    }

    public void setPanelDown(boolean z) {
        Log.d("KeyguardUpdateMonitor", "setPanelDown: paneldown = " + z);
        this.mPanelDown = z;
        updateFaceRecognitionState();
    }

    public void handleTapWake() {
        ((StatusBar) Dependency.get(StatusBar.class)).onTapWake();
        Assert.isMainThread();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            KeyguardUpdateMonitorCallback keyguardUpdateMonitorCallback = this.mCallbacks.get(i).get();
            if (keyguardUpdateMonitorCallback != null) {
                keyguardUpdateMonitorCallback.onTapWake();
            }
        }
    }

    public void resetFingerprintLockout() {
        if (this.mFpm == null || this.mAuthController.getSensorProps() == null) {
            return;
        }
        this.mFpm.resetLockout(this.mAuthController.getSensorProps().sensorId, getCurrentUser(), null);
        this.mFingerprintLockedOut = false;
        this.mFingerprintLockedOutPermanent = false;
    }

    public void doHBM(boolean z) {
        AuthController authController = this.mAuthController;
        if (authController != null) {
            authController.doHBM(z);
        }
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
}
