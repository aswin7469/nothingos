package com.android.systemui.biometrics;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.TaskStackListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.SensorPrivacyManager;
import android.hardware.biometrics.BiometricStateListener;
import android.hardware.biometrics.IBiometricContextListener;
import android.hardware.biometrics.IBiometricSysuiReceiver;
import android.hardware.biometrics.PromptInfo;
import android.hardware.display.DisplayManager;
import android.hardware.face.FaceManager;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.IFingerprintAuthenticatorsRegisteredCallback;
import android.hardware.fingerprint.IUdfpsHbmListener;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserManager;
import android.util.DisplayUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.DisplayInfo;
import android.view.WindowManager;
import com.android.internal.os.SomeArgs;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.C1894R;
import com.android.systemui.CoreStartable;
import com.android.systemui.biometrics.AuthContainerView;
import com.android.systemui.biometrics.BiometricDisplayListener;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.doze.DozeReceiver;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.Execution;
import com.nothing.utils.NTVibrateUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Provider;
import kotlin.Unit;

@SysUISingleton
public class AuthController extends CoreStartable implements CommandQueue.Callbacks, AuthDialogCallback, DozeReceiver {
    private static final boolean DEBUG = true;
    private static final int SENSOR_PRIVACY_DELAY = 500;
    private static final String TAG = "AuthController";
    private final ActivityTaskManager mActivityTaskManager;
    private boolean mAllFingerprintAuthenticatorsRegistered;
    @Background
    private final DelayableExecutor mBackgroundExecutor;
    private IBiometricContextListener mBiometricContextListener;
    private final BiometricStateListener mBiometricStateListener = new BiometricStateListener() {
        public void onEnrollmentsChanged(int i, int i2, boolean z) {
            AuthController.this.mHandler.post(new AuthController$3$$ExternalSyntheticLambda0(this, i, i2, z));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onEnrollmentsChanged$0$com-android-systemui-biometrics-AuthController$3 */
        public /* synthetic */ void mo30643xccf0943c(int i, int i2, boolean z) {
            AuthController.this.handleEnrollmentsChanged(i, i2, z);
        }
    };
    final BroadcastReceiver mBroadcastReceiver;
    /* access modifiers changed from: private */
    public final Set<Callback> mCallbacks = new HashSet();
    private final CommandQueue mCommandQueue;
    AuthDialog mCurrentDialog;
    private SomeArgs mCurrentDialogArgs;
    private final DisplayManager mDisplayManager;
    private final Execution mExecution;
    private final PointF mFaceAuthSensorLocation;
    private final FaceManager mFaceManager;
    private final List<FaceSensorPropertiesInternal> mFaceProps;
    private final IFingerprintAuthenticatorsRegisteredCallback mFingerprintAuthenticatorsRegisteredCallback = new IFingerprintAuthenticatorsRegisteredCallback.Stub() {
        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onAllAuthenticatorsRegistered$0$com-android-systemui-biometrics-AuthController$2 */
        public /* synthetic */ void mo30641xf644ebf5(List list) {
            AuthController.this.handleAllFingerprintAuthenticatorsRegistered(list);
        }

        public void onAllAuthenticatorsRegistered(List<FingerprintSensorPropertiesInternal> list) {
            AuthController.this.mHandler.post(new AuthController$2$$ExternalSyntheticLambda0(this, list));
        }
    };
    private PointF mFingerprintLocation;
    private final FingerprintManager mFingerprintManager;
    private List<FingerprintSensorPropertiesInternal> mFpProps;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private final LockPatternUtils mLockPatternUtils;
    final BiometricDisplayListener mOrientationListener;
    IBiometricSysuiReceiver mReceiver;
    private final SensorPrivacyManager mSensorPrivacyManager;
    private SidefpsController mSidefpsController;
    private final Provider<SidefpsController> mSidefpsControllerFactory;
    private List<FingerprintSensorPropertiesInternal> mSidefpsProps;
    private Point mStableDisplaySize = new Point();
    private final StatusBarStateController mStatusBarStateController;
    final TaskStackListener mTaskStackListener = new TaskStackListener() {
        public void onTaskStackChanged() {
            AuthController.this.mHandler.post(new AuthController$1$$ExternalSyntheticLambda0(AuthController.this));
        }
    };
    private Rect mUdfpsBounds;
    private UdfpsController mUdfpsController;
    private final Provider<UdfpsController> mUdfpsControllerFactory;
    private final SparseBooleanArray mUdfpsEnrolledForUser;
    private IUdfpsHbmListener mUdfpsHbmListener;
    private List<FingerprintSensorPropertiesInternal> mUdfpsProps;
    private final UserManager mUserManager;
    private final WakefulnessLifecycle mWakefulnessLifecycle;
    private final WindowManager mWindowManager;

    public interface Callback {
        void onAllAuthenticatorsRegistered() {
        }

        void onBiometricPromptDismissed() {
        }

        void onBiometricPromptShown() {
        }

        void onEnrollmentsChanged() {
        }

        void onUdfpsLocationChanged() {
        }
    }

    /* access modifiers changed from: private */
    public void cancelIfOwnerIsNotInForeground() {
        this.mExecution.assertIsMainThread();
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            try {
                String opPackageName = authDialog.getOpPackageName();
                Log.w(TAG, "Task stack changed, current client: " + opPackageName);
                List tasks = this.mActivityTaskManager.getTasks(1);
                if (!tasks.isEmpty()) {
                    String packageName = ((ActivityManager.RunningTaskInfo) tasks.get(0)).topActivity.getPackageName();
                    if (!packageName.contentEquals((CharSequence) opPackageName) && !Utils.isSystem(this.mContext, opPackageName)) {
                        Log.e(TAG, "Evicting client due to: " + packageName);
                        this.mCurrentDialog.dismissWithoutCallback(true);
                        this.mCurrentDialog = null;
                        this.mOrientationListener.disable();
                        for (Callback onBiometricPromptDismissed : this.mCallbacks) {
                            onBiometricPromptDismissed.onBiometricPromptDismissed();
                        }
                        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
                        if (iBiometricSysuiReceiver != null) {
                            iBiometricSysuiReceiver.onDialogDismissed(3, (byte[]) null);
                            this.mReceiver = null;
                        }
                    }
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Remote exception", e);
            }
        }
    }

    public boolean areAllFingerprintAuthenticatorsRegistered() {
        return this.mAllFingerprintAuthenticatorsRegistered;
    }

    /* access modifiers changed from: private */
    public void handleAllFingerprintAuthenticatorsRegistered(List<FingerprintSensorPropertiesInternal> list) {
        this.mExecution.assertIsMainThread();
        Log.d(TAG, "handleAllAuthenticatorsRegistered | sensors: " + Arrays.toString(list.toArray()));
        this.mAllFingerprintAuthenticatorsRegistered = true;
        this.mFpProps = list;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (FingerprintSensorPropertiesInternal next : this.mFpProps) {
            if (next.isAnyUdfpsType()) {
                arrayList.add(next);
            }
            if (next.isAnySidefpsType()) {
                arrayList2.add(next);
            }
        }
        if (arrayList.isEmpty()) {
            arrayList = null;
        }
        this.mUdfpsProps = arrayList;
        if (arrayList != null) {
            UdfpsController udfpsController = this.mUdfpsControllerFactory.get();
            this.mUdfpsController = udfpsController;
            udfpsController.addCallback(new UdfpsController.Callback() {
                public void onFingerUp() {
                }

                public void onFingerDown() {
                    if (AuthController.this.mCurrentDialog != null) {
                        AuthController.this.mCurrentDialog.onPointerDown();
                    }
                }
            });
            this.mUdfpsController.setAuthControllerUpdateUdfpsLocation(new AuthController$$ExternalSyntheticLambda0(this));
            this.mUdfpsController.setHalControlsIllumination(this.mUdfpsProps.get(0).halControlsIllumination);
            this.mUdfpsBounds = this.mUdfpsProps.get(0).getLocation().getRect();
            updateUdfpsLocation();
        }
        if (arrayList2.isEmpty()) {
            arrayList2 = null;
        }
        this.mSidefpsProps = arrayList2;
        if (arrayList2 != null) {
            this.mSidefpsController = this.mSidefpsControllerFactory.get();
        }
        this.mFingerprintManager.registerBiometricStateListener(this.mBiometricStateListener);
        updateFingerprintLocation();
        for (Callback onAllAuthenticatorsRegistered : this.mCallbacks) {
            onAllAuthenticatorsRegistered.onAllAuthenticatorsRegistered();
        }
    }

    /* access modifiers changed from: private */
    public void handleEnrollmentsChanged(int i, int i2, boolean z) {
        this.mExecution.assertIsMainThread();
        Log.d(TAG, "handleEnrollmentsChanged, userId: " + i + ", sensorId: " + i2 + ", hasEnrollments: " + z);
        List<FingerprintSensorPropertiesInternal> list = this.mUdfpsProps;
        if (list == null) {
            Log.d(TAG, "handleEnrollmentsChanged, mUdfpsProps is null");
        } else {
            for (FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal : list) {
                if (fingerprintSensorPropertiesInternal.sensorId == i2) {
                    this.mUdfpsEnrolledForUser.put(i, z);
                }
            }
        }
        for (Callback onEnrollmentsChanged : this.mCallbacks) {
            onEnrollmentsChanged.onEnrollmentsChanged();
        }
    }

    public void addCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void removeCallback(Callback callback) {
        this.mCallbacks.remove(callback);
    }

    public void dozeTimeTick() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.dozeTimeTick();
        }
    }

    public void onTryAgainPressed() {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e(TAG, "onTryAgainPressed: Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onTryAgainPressed();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException when handling try again", e);
        }
    }

    public void onDeviceCredentialPressed() {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e(TAG, "onDeviceCredentialPressed: Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onDeviceCredentialPressed();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException when handling credential button", e);
        }
    }

    public void onSystemEvent(int i) {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e(TAG, "onSystemEvent(" + i + "): Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onSystemEvent(i);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException when sending system event", e);
        }
    }

    public void onDialogAnimatedIn() {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e(TAG, "onDialogAnimatedIn: Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onDialogAnimatedIn();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException when sending onDialogAnimatedIn", e);
        }
    }

    public void onDismissed(int i, byte[] bArr) {
        switch (i) {
            case 1:
                sendResultAndCleanUp(3, bArr);
                return;
            case 2:
                sendResultAndCleanUp(2, bArr);
                return;
            case 3:
                sendResultAndCleanUp(1, bArr);
                return;
            case 4:
                sendResultAndCleanUp(4, bArr);
                return;
            case 5:
                sendResultAndCleanUp(5, bArr);
                return;
            case 6:
                sendResultAndCleanUp(6, bArr);
                return;
            case 7:
                sendResultAndCleanUp(7, bArr);
                return;
            default:
                Log.e(TAG, "Unhandled reason: " + i);
                return;
        }
    }

    public PointF getUdfpsLocation() {
        if (this.mUdfpsController == null || this.mUdfpsBounds == null) {
            return null;
        }
        return new PointF((float) this.mUdfpsBounds.centerX(), (float) this.mUdfpsBounds.centerY());
    }

    public float getUdfpsRadius() {
        Rect rect;
        if (this.mUdfpsController == null || (rect = this.mUdfpsBounds) == null) {
            return -1.0f;
        }
        return ((float) rect.height()) / 2.0f;
    }

    public float getScaleFactor() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController == null || udfpsController.mOverlayParams == null) {
            return 1.0f;
        }
        return this.mUdfpsController.mOverlayParams.getScaleFactor();
    }

    public PointF getFingerprintSensorLocation() {
        if (getUdfpsLocation() != null) {
            return getUdfpsLocation();
        }
        return this.mFingerprintLocation;
    }

    public PointF getFaceAuthSensorLocation() {
        if (this.mFaceProps == null || this.mFaceAuthSensorLocation == null) {
            return null;
        }
        DisplayInfo displayInfo = new DisplayInfo();
        this.mContext.getDisplay().getDisplayInfo(displayInfo);
        float physicalPixelDisplaySizeRatio = DisplayUtils.getPhysicalPixelDisplaySizeRatio(this.mStableDisplaySize.x, this.mStableDisplaySize.y, displayInfo.getNaturalWidth(), displayInfo.getNaturalHeight());
        return new PointF(this.mFaceAuthSensorLocation.x * physicalPixelDisplaySizeRatio, this.mFaceAuthSensorLocation.y * physicalPixelDisplaySizeRatio);
    }

    public void onAodInterrupt(int i, int i2, float f, float f2) {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.onAodInterrupt(i, i2, f, f2);
        }
    }

    public void onCancelUdfps() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.onCancelUdfps();
        }
    }

    private void sendResultAndCleanUp(int i, byte[] bArr) {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e(TAG, "sendResultAndCleanUp: Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onDialogDismissed(i, bArr);
        } catch (RemoteException e) {
            Log.w(TAG, "Remote exception", e);
        }
        onDialogDismissed(i);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public AuthController(Context context, Execution execution, CommandQueue commandQueue, ActivityTaskManager activityTaskManager, WindowManager windowManager, FingerprintManager fingerprintManager, FaceManager faceManager, Provider<UdfpsController> provider, Provider<SidefpsController> provider2, DisplayManager displayManager, WakefulnessLifecycle wakefulnessLifecycle, UserManager userManager, LockPatternUtils lockPatternUtils, StatusBarStateController statusBarStateController, @Main Handler handler, @Background DelayableExecutor delayableExecutor) {
        super(context);
        Context context2 = context;
        FaceManager faceManager2 = faceManager;
        StatusBarStateController statusBarStateController2 = statusBarStateController;
        C19674 r10 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (AuthController.this.mCurrentDialog != null && "android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                    Log.w(AuthController.TAG, "ACTION_CLOSE_SYSTEM_DIALOGS received");
                    AuthController.this.mCurrentDialog.dismissWithoutCallback(true);
                    AuthController.this.mCurrentDialog = null;
                    AuthController.this.mOrientationListener.disable();
                    for (Callback onBiometricPromptDismissed : AuthController.this.mCallbacks) {
                        onBiometricPromptDismissed.onBiometricPromptDismissed();
                    }
                    try {
                        if (AuthController.this.mReceiver != null) {
                            AuthController.this.mReceiver.onDialogDismissed(3, (byte[]) null);
                            AuthController.this.mReceiver = null;
                        }
                    } catch (RemoteException e) {
                        Log.e(AuthController.TAG, "Remote exception", e);
                    }
                }
            }
        };
        this.mBroadcastReceiver = r10;
        this.mExecution = execution;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mUserManager = userManager;
        this.mLockPatternUtils = lockPatternUtils;
        Handler handler2 = handler;
        this.mHandler = handler2;
        this.mBackgroundExecutor = delayableExecutor;
        this.mCommandQueue = commandQueue;
        this.mActivityTaskManager = activityTaskManager;
        this.mFingerprintManager = fingerprintManager;
        this.mFaceManager = faceManager2;
        this.mUdfpsControllerFactory = provider;
        this.mSidefpsControllerFactory = provider2;
        DisplayManager displayManager2 = displayManager;
        this.mDisplayManager = displayManager2;
        this.mWindowManager = windowManager;
        this.mUdfpsEnrolledForUser = new SparseBooleanArray();
        this.mOrientationListener = new BiometricDisplayListener(context, displayManager2, handler2, BiometricDisplayListener.SensorType.Generic.INSTANCE, new AuthController$$ExternalSyntheticLambda1(this));
        this.mStatusBarStateController = statusBarStateController2;
        statusBarStateController2.addCallback(new StatusBarStateController.StateListener() {
            public void onDozingChanged(boolean z) {
                AuthController.this.notifyDozeChanged(z);
            }
        });
        this.mFaceProps = faceManager2 != null ? faceManager.getSensorPropertiesInternal() : null;
        int[] intArray = context.getResources().getIntArray(C1894R.array.config_face_auth_props);
        if (intArray == null || intArray.length < 2) {
            this.mFaceAuthSensorLocation = null;
        } else {
            this.mFaceAuthSensorLocation = new PointF((float) intArray[0], (float) intArray[1]);
        }
        updateFingerprintLocation();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        context.registerReceiver(r10, intentFilter, 2);
        this.mSensorPrivacyManager = (SensorPrivacyManager) context.getSystemService(SensorPrivacyManager.class);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-biometrics-AuthController  reason: not valid java name */
    public /* synthetic */ Unit m2563lambda$new$0$comandroidsystemuibiometricsAuthController() {
        onOrientationChanged();
        return Unit.INSTANCE;
    }

    private int getDisplayWidth() {
        DisplayInfo displayInfo = new DisplayInfo();
        this.mContext.getDisplay().getDisplayInfo(displayInfo);
        return displayInfo.getNaturalWidth();
    }

    private void updateFingerprintLocation() {
        int displayWidth = getDisplayWidth() / 2;
        try {
            displayWidth = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.physical_fingerprint_sensor_center_screen_location_x);
        } catch (Resources.NotFoundException unused) {
        }
        this.mFingerprintLocation = new PointF((float) displayWidth, (float) this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.physical_fingerprint_sensor_center_screen_location_y));
    }

    /* access modifiers changed from: private */
    public void updateUdfpsLocation() {
        if (this.mUdfpsController != null) {
            DisplayInfo displayInfo = new DisplayInfo();
            this.mContext.getDisplay().getDisplayInfo(displayInfo);
            float physicalPixelDisplaySizeRatio = DisplayUtils.getPhysicalPixelDisplaySizeRatio(this.mStableDisplaySize.x, this.mStableDisplaySize.y, displayInfo.getNaturalWidth(), displayInfo.getNaturalHeight());
            FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal = this.mUdfpsProps.get(0);
            Rect rect = this.mUdfpsBounds;
            Rect rect2 = fingerprintSensorPropertiesInternal.getLocation().getRect();
            this.mUdfpsBounds = rect2;
            rect2.scale(physicalPixelDisplaySizeRatio);
            this.mUdfpsController.updateOverlayParams(fingerprintSensorPropertiesInternal.sensorId, new UdfpsOverlayParams(this.mUdfpsBounds, displayInfo.getNaturalWidth(), displayInfo.getNaturalHeight(), physicalPixelDisplaySizeRatio, displayInfo.rotation));
            if (!Objects.equals(rect, this.mUdfpsBounds)) {
                for (Callback onUdfpsLocationChanged : this.mCallbacks) {
                    onUdfpsLocationChanged.onUdfpsLocationChanged();
                }
            }
        }
    }

    public void start() {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        if (fingerprintManager != null) {
            fingerprintManager.addAuthenticatorsRegisteredCallback(this.mFingerprintAuthenticatorsRegisteredCallback);
        }
        this.mStableDisplaySize = this.mDisplayManager.getStableDisplaySize();
        this.mActivityTaskManager.registerTaskStackListener(this.mTaskStackListener);
    }

    public void setBiometicContextListener(IBiometricContextListener iBiometricContextListener) {
        this.mBiometricContextListener = iBiometricContextListener;
        notifyDozeChanged(this.mStatusBarStateController.isDozing());
    }

    /* access modifiers changed from: private */
    public void notifyDozeChanged(boolean z) {
        IBiometricContextListener iBiometricContextListener = this.mBiometricContextListener;
        if (iBiometricContextListener != null) {
            try {
                iBiometricContextListener.onDozeChanged(z);
            } catch (RemoteException unused) {
                Log.w(TAG, "failed to notify initial doze state");
            }
        }
    }

    public void setUdfpsHbmListener(IUdfpsHbmListener iUdfpsHbmListener) {
        this.mUdfpsHbmListener = iUdfpsHbmListener;
    }

    public IUdfpsHbmListener getUdfpsHbmListener() {
        return this.mUdfpsHbmListener;
    }

    public void showAuthenticationDialog(PromptInfo promptInfo, IBiometricSysuiReceiver iBiometricSysuiReceiver, int[] iArr, boolean z, boolean z2, int i, long j, String str, long j2, int i2) {
        int[] iArr2 = iArr;
        long j3 = j;
        long j4 = j2;
        int i3 = i2;
        int authenticators = promptInfo.getAuthenticators();
        StringBuilder sb = new StringBuilder();
        boolean z3 = false;
        for (int append : iArr2) {
            sb.append(append).append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        Log.d(TAG, "showAuthenticationDialog, authenticators: " + authenticators + ", sensorIds: " + sb.toString() + ", credentialAllowed: " + z + ", requireConfirmation: " + z2 + ", operationId: " + j3 + ", requestId: " + j4 + ", multiSensorConfig: " + i3);
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = promptInfo;
        obtain.arg2 = iBiometricSysuiReceiver;
        obtain.arg3 = iArr2;
        obtain.arg4 = Boolean.valueOf(z);
        obtain.arg5 = Boolean.valueOf(z2);
        obtain.argi1 = i;
        obtain.arg6 = str;
        obtain.argl1 = j3;
        obtain.argl2 = j4;
        obtain.argi2 = i3;
        if (this.mCurrentDialog != null) {
            Log.w(TAG, "mCurrentDialog: " + this.mCurrentDialog);
            z3 = true;
        }
        showDialog(obtain, z3, (Bundle) null);
    }

    public void onBiometricAuthenticated(int i) {
        Log.d(TAG, "onBiometricAuthenticated: ");
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            authDialog.onAuthenticationSucceeded(i);
        } else {
            Log.w(TAG, "onBiometricAuthenticated callback but dialog gone");
        }
        NTVibrateUtils.getInstance(this.mContext).playVerityVibrate(NTVibrateUtils.VibrateType.FINGERPRINT_VERIFICATION_SUCCEEDED);
    }

    public void onBiometricHelp(int i, String str) {
        Log.d(TAG, "onBiometricHelp: " + str);
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            authDialog.onHelp(i, str);
        } else {
            Log.w(TAG, "onBiometricHelp callback but dialog gone");
        }
    }

    public List<FingerprintSensorPropertiesInternal> getUdfpsProps() {
        return this.mUdfpsProps;
    }

    private String getErrorString(int i, int i2, int i3) {
        if (i != 2) {
            return i != 8 ? "" : FaceManager.getErrorString(this.mContext, i2, i3);
        }
        return FingerprintManager.getErrorString(this.mContext, i2, i3);
    }

    public void onBiometricError(int i, int i2, int i3) {
        String str;
        boolean z = false;
        Log.d(TAG, String.format("onBiometricError(%d, %d, %d)", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)));
        boolean z2 = i2 == 7 || i2 == 9;
        boolean z3 = i2 == 1 && this.mSensorPrivacyManager.isSensorPrivacyEnabled(1, 2);
        if (i2 == 100 || i2 == 3 || z3) {
            z = true;
        }
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog == null) {
            Log.w(TAG, "onBiometricError callback but dialog is gone");
        } else if (authDialog.isAllowDeviceCredentials() && z2) {
            Log.d(TAG, "onBiometricError, lockout");
            this.mCurrentDialog.animateToCredentialUI();
        } else if (z) {
            if (i2 == 100) {
                str = this.mContext.getString(17039810);
            } else {
                str = getErrorString(i, i2, i3);
            }
            Log.d(TAG, "onBiometricError, soft error: " + str);
            if (z3) {
                this.mHandler.postDelayed(new AuthController$$ExternalSyntheticLambda3(this, i), 500);
            } else {
                this.mCurrentDialog.onAuthenticationFailed(i, str);
            }
        } else {
            String errorString = getErrorString(i, i2, i3);
            Log.d(TAG, "onBiometricError, hard error: " + errorString);
            this.mCurrentDialog.onError(i, errorString);
        }
        if (!(i2 == 5 || i2 == 10)) {
            NTVibrateUtils.getInstance(this.mContext).playVerityVibrate(NTVibrateUtils.VibrateType.FINGERPRINT_VERIFICATION_ERROR);
        }
        onCancelUdfps();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onBiometricError$1$com-android-systemui-biometrics-AuthController */
    public /* synthetic */ void mo30623x1f6803c1(int i) {
        this.mCurrentDialog.onAuthenticationFailed(i, this.mContext.getString(17040307));
    }

    public void hideAuthenticationDialog(long j) {
        Log.d(TAG, "hideAuthenticationDialog: " + this.mCurrentDialog);
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog == null) {
            Log.d(TAG, "dialog already gone");
        } else if (j != authDialog.getRequestId()) {
            Log.w(TAG, "ignore - ids do not match: " + j + " current: " + this.mCurrentDialog.getRequestId());
        } else {
            this.mCurrentDialog.dismissFromSystemServer();
            this.mCurrentDialog = null;
            this.mOrientationListener.disable();
        }
    }

    public boolean isUdfpsFingerDown() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController == null) {
            return false;
        }
        return udfpsController.isFingerDown();
    }

    public boolean isFaceAuthEnrolled(int i) {
        if (this.mFaceProps == null) {
            return false;
        }
        return this.mFaceManager.hasEnrolledTemplates(i);
    }

    public boolean isUdfpsEnrolled(int i) {
        if (this.mUdfpsController == null) {
            return false;
        }
        return this.mUdfpsEnrolledForUser.get(i);
    }

    private void showDialog(SomeArgs someArgs, boolean z, Bundle bundle) {
        SomeArgs someArgs2 = someArgs;
        Bundle bundle2 = bundle;
        this.mCurrentDialogArgs = someArgs2;
        PromptInfo promptInfo = (PromptInfo) someArgs2.arg1;
        ((Boolean) someArgs2.arg4).booleanValue();
        boolean booleanValue = ((Boolean) someArgs2.arg5).booleanValue();
        int i = someArgs2.argi1;
        long j = someArgs2.argl1;
        long j2 = someArgs2.argl2;
        int i2 = someArgs2.argi2;
        DelayableExecutor delayableExecutor = this.mBackgroundExecutor;
        WakefulnessLifecycle wakefulnessLifecycle = this.mWakefulnessLifecycle;
        UserManager userManager = this.mUserManager;
        UserManager userManager2 = userManager;
        PromptInfo promptInfo2 = promptInfo;
        WakefulnessLifecycle wakefulnessLifecycle2 = wakefulnessLifecycle;
        int i3 = i;
        int i4 = i2;
        int i5 = i;
        AuthDialog buildDialog = buildDialog(delayableExecutor, promptInfo2, booleanValue, i3, (int[]) someArgs2.arg3, (String) someArgs2.arg6, z, j, j2, i4, wakefulnessLifecycle2, userManager2, this.mLockPatternUtils);
        if (buildDialog == null) {
            Log.e(TAG, "Unsupported type configuration");
            return;
        }
        Bundle bundle3 = bundle;
        Log.d(TAG, "userId: " + i5 + " savedState: " + bundle3 + " mCurrentDialog: " + this.mCurrentDialog + " newDialog: " + buildDialog);
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            authDialog.dismissWithoutCallback(false);
        }
        this.mReceiver = (IBiometricSysuiReceiver) someArgs.arg2;
        for (Callback onBiometricPromptShown : this.mCallbacks) {
            onBiometricPromptShown.onBiometricPromptShown();
        }
        this.mCurrentDialog = buildDialog;
        buildDialog.show(this.mWindowManager, bundle3);
        this.mOrientationListener.enable();
        if (!promptInfo.isAllowBackgroundAuthentication()) {
            this.mHandler.post(new AuthController$$ExternalSyntheticLambda2(this));
        }
    }

    private void onDialogDismissed(int i) {
        Log.d(TAG, "onDialogDismissed: " + i);
        if (this.mCurrentDialog == null) {
            Log.w(TAG, "Dialog already dismissed");
        }
        for (Callback onBiometricPromptDismissed : this.mCallbacks) {
            onBiometricPromptDismissed.onBiometricPromptDismissed();
        }
        this.mReceiver = null;
        this.mCurrentDialog = null;
        this.mOrientationListener.disable();
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateFingerprintLocation();
        updateUdfpsLocation();
        if (this.mCurrentDialog != null) {
            Bundle bundle = new Bundle();
            this.mCurrentDialog.onSaveState(bundle);
            this.mCurrentDialog.dismissWithoutCallback(false);
            this.mCurrentDialog = null;
            this.mOrientationListener.disable();
            if (!bundle.getBoolean(AuthDialog.KEY_CONTAINER_GOING_AWAY, false)) {
                if (bundle.getBoolean(AuthDialog.KEY_CREDENTIAL_SHOWING)) {
                    ((PromptInfo) this.mCurrentDialogArgs.arg1).setAuthenticators(32768);
                }
                showDialog(this.mCurrentDialogArgs, true, bundle);
            }
        }
    }

    private void onOrientationChanged() {
        updateFingerprintLocation();
        updateUdfpsLocation();
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            authDialog.onOrientationChanged();
        }
    }

    /* access modifiers changed from: protected */
    public AuthDialog buildDialog(@Background DelayableExecutor delayableExecutor, PromptInfo promptInfo, boolean z, int i, int[] iArr, String str, boolean z2, long j, long j2, int i2, WakefulnessLifecycle wakefulnessLifecycle, UserManager userManager, LockPatternUtils lockPatternUtils) {
        PromptInfo promptInfo2 = promptInfo;
        boolean z3 = z;
        int i3 = i;
        return new AuthContainerView.Builder(this.mContext).setCallback(this).setPromptInfo(promptInfo).setRequireConfirmation(z).setUserId(i).setOpPackageName(str).setSkipIntro(z2).setOperationId(j).setRequestId(j2).setMultiSensorConfig(i2).build(delayableExecutor, iArr, this.mFpProps, this.mFaceProps, wakefulnessLifecycle, userManager, lockPatternUtils);
    }

    public void showDimLayer(boolean z, String str) {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.showDimLayer(z, str);
        }
    }

    public void dismissFingerprintIcon(String str) {
        Log.d(TAG, "dismissFingerprintIcon: " + str);
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.dismissFingerprintIcon();
        }
    }

    public void showFingerprintIcon(String str) {
        Log.d(TAG, "showFingerprintIcon: " + str);
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.showFingerprintIcon();
        }
    }

    public boolean isFpIconVisible() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            return udfpsController.isFpIconVisible();
        }
        return false;
    }

    public RectF getSensorRectF() {
        if (this.mUdfpsBounds == null) {
            return null;
        }
        return new RectF(this.mUdfpsBounds);
    }

    public void handleShowShutdownUi(boolean z, String str) {
        dismissFingerprintIcon("handleShowShutdownUi");
    }
}
