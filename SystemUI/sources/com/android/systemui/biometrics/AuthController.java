package com.android.systemui.biometrics;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.TaskStackListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.graphics.RectF;
import android.hardware.biometrics.IBiometricSysuiReceiver;
import android.hardware.biometrics.PromptInfo;
import android.hardware.display.DisplayManager;
import android.hardware.face.FaceManager;
import android.hardware.face.FaceSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.hardware.fingerprint.FingerprintStateListener;
import android.hardware.fingerprint.IFingerprintAuthenticatorsRegisteredCallback;
import android.hardware.fingerprint.IUdfpsHbmListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import com.android.systemui.R$array;
import com.android.systemui.R$dimen;
import com.android.systemui.SystemUI;
import com.android.systemui.assist.ui.DisplayUtils;
import com.android.systemui.biometrics.AuthContainerView;
import com.android.systemui.doze.DozeReceiver;
import com.android.systemui.statusbar.CommandQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Provider;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* loaded from: classes.dex */
public class AuthController extends SystemUI implements CommandQueue.Callbacks, AuthDialogCallback, DozeReceiver {
    private final ActivityTaskManager mActivityTaskManager;
    @VisibleForTesting
    final BroadcastReceiver mBroadcastReceiver;
    private final CommandQueue mCommandQueue;
    @VisibleForTesting
    AuthDialog mCurrentDialog;
    private SomeArgs mCurrentDialogArgs;
    private final PointF mFaceAuthSensorLocation;
    private final FaceManager mFaceManager;
    private final List<FaceSensorPropertiesInternal> mFaceProps;
    private final PointF mFingerprintLocation;
    private final FingerprintManager mFingerprintManager;
    private List<FingerprintSensorPropertiesInternal> mFpProps;
    @VisibleForTesting
    final BiometricOrientationEventListener mOrientationListener;
    @VisibleForTesting
    IBiometricSysuiReceiver mReceiver;
    private SidefpsController mSidefpsController;
    private final Provider<SidefpsController> mSidefpsControllerFactory;
    private List<FingerprintSensorPropertiesInternal> mSidefpsProps;
    @VisibleForTesting
    TaskStackListener mTaskStackListener;
    private UdfpsController mUdfpsController;
    private final Provider<UdfpsController> mUdfpsControllerFactory;
    private IUdfpsHbmListener mUdfpsHbmListener;
    private List<FingerprintSensorPropertiesInternal> mUdfpsProps;
    private final WindowManager mWindowManager;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Set<Callback> mCallbacks = new HashSet();
    private final FingerprintStateListener mFingerprintStateListener = new FingerprintStateListener() { // from class: com.android.systemui.biometrics.AuthController.1
        public void onEnrollmentsChanged(int i, int i2, boolean z) {
            Log.d("AuthController", "onEnrollmentsChanged, userId: " + i + ", sensorId: " + i2 + ", hasEnrollments: " + z);
            for (FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal : AuthController.this.mUdfpsProps) {
                if (fingerprintSensorPropertiesInternal.sensorId == i2) {
                    Log.d("AuthController", "onEnrollmentsChanged put=" + i + ", hasEnrollments=" + z);
                    AuthController.this.mUdfpsEnrolledForUser.put(i, z);
                }
            }
        }
    };
    private final IFingerprintAuthenticatorsRegisteredCallback mFingerprintAuthenticatorsRegisteredCallback = new IFingerprintAuthenticatorsRegisteredCallback.Stub() { // from class: com.android.systemui.biometrics.AuthController.2
        public void onAllAuthenticatorsRegistered(List<FingerprintSensorPropertiesInternal> list) {
            Log.d("AuthController", "onFingerprintProvidersAvailable | sensors: " + Arrays.toString(list.toArray()));
            AuthController.this.mFpProps = list;
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal : AuthController.this.mFpProps) {
                if (fingerprintSensorPropertiesInternal.isAnyUdfpsType()) {
                    arrayList.add(fingerprintSensorPropertiesInternal);
                }
                if (fingerprintSensorPropertiesInternal.isAnySidefpsType()) {
                    arrayList2.add(fingerprintSensorPropertiesInternal);
                }
            }
            AuthController authController = AuthController.this;
            if (arrayList.isEmpty()) {
                arrayList = null;
            }
            authController.mUdfpsProps = arrayList;
            if (AuthController.this.mUdfpsProps != null) {
                Log.i("AuthController", "onFingerprintProvidersAvailable mUdfpsController mUdfpsControllerFactory.get()=!!!!!!!!!!!!!");
                AuthController authController2 = AuthController.this;
                authController2.mUdfpsController = (UdfpsController) authController2.mUdfpsControllerFactory.mo1933get();
            }
            Log.i("AuthController", "onFingerprintProvidersAvailable mUdfpsController mUdfpsController=====" + AuthController.this.mUdfpsController);
            AuthController authController3 = AuthController.this;
            if (arrayList2.isEmpty()) {
                arrayList2 = null;
            }
            authController3.mSidefpsProps = arrayList2;
            if (AuthController.this.mSidefpsProps != null) {
                AuthController authController4 = AuthController.this;
                authController4.mSidefpsController = (SidefpsController) authController4.mSidefpsControllerFactory.mo1933get();
            }
            for (Callback callback : AuthController.this.mCallbacks) {
                callback.onAllAuthenticatorsRegistered();
            }
        }
    };
    private Runnable mOpenHBMRunnable = new Runnable() { // from class: com.android.systemui.biometrics.AuthController.5
        @Override // java.lang.Runnable
        public void run() {
            if (AuthController.this.mUdfpsController != null) {
                AuthController.this.mUdfpsController.doHBM(true);
            }
        }
    };
    private final SparseBooleanArray mUdfpsEnrolledForUser = new SparseBooleanArray();

    /* loaded from: classes.dex */
    public interface Callback {
        void onAllAuthenticatorsRegistered();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void access$100(AuthController authController) {
        authController.handleTaskStackChanged();
    }

    /* loaded from: classes.dex */
    private class BiometricTaskStackListener extends TaskStackListener {
        private BiometricTaskStackListener() {
        }

        public void onTaskStackChanged() {
            Handler handler = AuthController.this.mHandler;
            final AuthController authController = AuthController.this;
            handler.post(new Runnable() { // from class: com.android.systemui.biometrics.AuthController$BiometricTaskStackListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AuthController.access$100(AuthController.this);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleTaskStackChanged() {
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            try {
                String opPackageName = authDialog.getOpPackageName();
                Log.w("AuthController", "Task stack changed, current client: " + opPackageName);
                List tasks = this.mActivityTaskManager.getTasks(1);
                if (tasks.isEmpty()) {
                    return;
                }
                String packageName = ((ActivityManager.RunningTaskInfo) tasks.get(0)).topActivity.getPackageName();
                if (packageName.contentEquals(opPackageName) || Utils.isSystem(this.mContext, opPackageName)) {
                    return;
                }
                Log.w("AuthController", "Evicting client due to: " + packageName);
                this.mCurrentDialog.dismissWithoutCallback(true);
                this.mCurrentDialog = null;
                this.mOrientationListener.disable();
                IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
                if (iBiometricSysuiReceiver == null) {
                    return;
                }
                iBiometricSysuiReceiver.onDialogDismissed(3, (byte[]) null);
                this.mReceiver = null;
            } catch (RemoteException e) {
                Log.e("AuthController", "Remote exception", e);
            }
        }
    }

    public void addCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void removeCallback(Callback callback) {
        this.mCallbacks.remove(callback);
    }

    @Override // com.android.systemui.doze.DozeReceiver
    public void dozeTimeTick() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.dozeTimeTick();
        }
    }

    @Override // com.android.systemui.biometrics.AuthDialogCallback
    public void onTryAgainPressed() {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e("AuthController", "onTryAgainPressed: Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onTryAgainPressed();
        } catch (RemoteException e) {
            Log.e("AuthController", "RemoteException when handling try again", e);
        }
    }

    @Override // com.android.systemui.biometrics.AuthDialogCallback
    public void onDeviceCredentialPressed() {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e("AuthController", "onDeviceCredentialPressed: Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onDeviceCredentialPressed();
        } catch (RemoteException e) {
            Log.e("AuthController", "RemoteException when handling credential button", e);
        }
    }

    @Override // com.android.systemui.biometrics.AuthDialogCallback
    public void onSystemEvent(int i) {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e("AuthController", "onSystemEvent(" + i + "): Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onSystemEvent(i);
        } catch (RemoteException e) {
            Log.e("AuthController", "RemoteException when sending system event", e);
        }
    }

    @Override // com.android.systemui.biometrics.AuthDialogCallback
    public void onDialogAnimatedIn() {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e("AuthController", "onDialogAnimatedIn: Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onDialogAnimatedIn();
        } catch (RemoteException e) {
            Log.e("AuthController", "RemoteException when sending onDialogAnimatedIn", e);
        }
    }

    @Override // com.android.systemui.biometrics.AuthDialogCallback
    public void onStartFingerprintNow() {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e("AuthController", "onStartUdfpsNow: Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onStartFingerprintNow();
        } catch (RemoteException e) {
            Log.e("AuthController", "RemoteException when sending onDialogAnimatedIn", e);
        }
    }

    @Override // com.android.systemui.biometrics.AuthDialogCallback
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
                Log.e("AuthController", "Unhandled reason: " + i);
                return;
        }
    }

    public PointF getUdfpsSensorLocation() {
        if (this.mUdfpsController == null) {
            return null;
        }
        return new PointF(this.mUdfpsController.getSensorLocation().centerX(), this.mUdfpsController.getSensorLocation().centerY());
    }

    public PointF getFingerprintSensorLocation() {
        if (getUdfpsSensorLocation() != null) {
            return getUdfpsSensorLocation();
        }
        return this.mFingerprintLocation;
    }

    public PointF getFaceAuthSensorLocation() {
        if (this.mFaceProps == null || this.mFaceAuthSensorLocation == null) {
            return null;
        }
        PointF pointF = this.mFaceAuthSensorLocation;
        return new PointF(pointF.x, pointF.y);
    }

    public void onAodInterrupt(final int i, final int i2, final float f, final float f2) {
        if (this.mUdfpsController == null) {
            return;
        }
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.biometrics.AuthController.4
            @Override // java.lang.Runnable
            public void run() {
                AuthController.this.mUdfpsController.onAodInterrupt(i, i2, f, f2);
            }
        }, 150L);
    }

    public void onCancelUdfps() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController == null) {
            return;
        }
        udfpsController.onCancelUdfps();
    }

    private void sendResultAndCleanUp(int i, byte[] bArr) {
        IBiometricSysuiReceiver iBiometricSysuiReceiver = this.mReceiver;
        if (iBiometricSysuiReceiver == null) {
            Log.e("AuthController", "sendResultAndCleanUp: Receiver is null");
            return;
        }
        try {
            iBiometricSysuiReceiver.onDialogDismissed(i, bArr);
        } catch (RemoteException e) {
            Log.w("AuthController", "Remote exception", e);
        }
        onDialogDismissed(i);
    }

    public AuthController(Context context, CommandQueue commandQueue, ActivityTaskManager activityTaskManager, WindowManager windowManager, FingerprintManager fingerprintManager, FaceManager faceManager, Provider<UdfpsController> provider, Provider<SidefpsController> provider2, DisplayManager displayManager, Handler handler) {
        super(context);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.biometrics.AuthController.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if (AuthController.this.mCurrentDialog == null || !"android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                    return;
                }
                Log.w("AuthController", "ACTION_CLOSE_SYSTEM_DIALOGS received");
                AuthController.this.mCurrentDialog.dismissWithoutCallback(true);
                AuthController authController = AuthController.this;
                authController.mCurrentDialog = null;
                authController.mOrientationListener.disable();
                try {
                    IBiometricSysuiReceiver iBiometricSysuiReceiver = AuthController.this.mReceiver;
                    if (iBiometricSysuiReceiver == null) {
                        return;
                    }
                    iBiometricSysuiReceiver.onDialogDismissed(3, (byte[]) null);
                    AuthController.this.mReceiver = null;
                } catch (RemoteException e) {
                    Log.e("AuthController", "Remote exception", e);
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mCommandQueue = commandQueue;
        this.mActivityTaskManager = activityTaskManager;
        this.mFingerprintManager = fingerprintManager;
        this.mFaceManager = faceManager;
        this.mUdfpsControllerFactory = provider;
        this.mSidefpsControllerFactory = provider2;
        this.mWindowManager = windowManager;
        this.mOrientationListener = new BiometricOrientationEventListener(context, new Function0() { // from class: com.android.systemui.biometrics.AuthController$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function0
            /* renamed from: invoke */
            public final Object mo1951invoke() {
                Unit lambda$new$0;
                lambda$new$0 = AuthController.this.lambda$new$0();
                return lambda$new$0;
            }
        }, displayManager, handler);
        this.mFaceProps = faceManager != null ? faceManager.getSensorPropertiesInternal() : null;
        int[] intArray = context.getResources().getIntArray(R$array.config_face_auth_props);
        if (intArray == null || intArray.length < 2) {
            this.mFaceAuthSensorLocation = null;
        } else {
            this.mFaceAuthSensorLocation = new PointF(intArray[0], intArray[1]);
        }
        this.mFingerprintLocation = new PointF(DisplayUtils.getWidth(this.mContext) / 2, this.mContext.getResources().getDimensionPixelSize(R$dimen.physical_fingerprint_sensor_center_screen_location_y));
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Unit lambda$new$0() {
        onOrientationChanged();
        return Unit.INSTANCE;
    }

    @Override // com.android.systemui.SystemUI
    public void start() {
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        FingerprintManager fingerprintManager = this.mFingerprintManager;
        if (fingerprintManager != null) {
            fingerprintManager.addAuthenticatorsRegisteredCallback(this.mFingerprintAuthenticatorsRegisteredCallback);
            this.mFingerprintManager.registerFingerprintStateListener(this.mFingerprintStateListener);
        }
        BiometricTaskStackListener biometricTaskStackListener = new BiometricTaskStackListener();
        this.mTaskStackListener = biometricTaskStackListener;
        this.mActivityTaskManager.registerTaskStackListener(biometricTaskStackListener);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void setUdfpsHbmListener(IUdfpsHbmListener iUdfpsHbmListener) {
        this.mUdfpsHbmListener = iUdfpsHbmListener;
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void showAuthenticationDialog(PromptInfo promptInfo, IBiometricSysuiReceiver iBiometricSysuiReceiver, int[] iArr, boolean z, boolean z2, int i, long j, String str, long j2, int i2) {
        int authenticators = promptInfo.getAuthenticators();
        StringBuilder sb = new StringBuilder();
        boolean z3 = false;
        for (int i3 : iArr) {
            sb.append(i3);
            sb.append(" ");
        }
        Log.d("AuthController", "showAuthenticationDialog, authenticators: " + authenticators + ", sensorIds: " + sb.toString() + ", credentialAllowed: " + z + ", requireConfirmation: " + z2 + ", operationId: " + j + ", requestId: " + j2 + ", multiSensorConfig: " + i2);
        SomeArgs obtain = SomeArgs.obtain();
        obtain.arg1 = promptInfo;
        obtain.arg2 = iBiometricSysuiReceiver;
        obtain.arg3 = iArr;
        obtain.arg4 = Boolean.valueOf(z);
        obtain.arg5 = Boolean.valueOf(z2);
        obtain.argi1 = i;
        obtain.arg6 = str;
        obtain.arg7 = Long.valueOf(j);
        obtain.arg8 = Long.valueOf(j2);
        obtain.argi2 = i2;
        if (this.mCurrentDialog != null) {
            Log.w("AuthController", "mCurrentDialog: " + this.mCurrentDialog);
            z3 = true;
        }
        showDialog(obtain, z3, null);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void onBiometricAuthenticated() {
        Log.d("AuthController", "onBiometricAuthenticated: ");
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            authDialog.onAuthenticationSucceeded();
        } else {
            Log.w("AuthController", "onBiometricAuthenticated callback but dialog gone");
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void onBiometricHelp(int i, String str) {
        Log.d("AuthController", "onBiometricHelp: " + str);
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            authDialog.onHelp(i, str);
        } else {
            Log.w("AuthController", "onBiometricHelp callback but dialog gone");
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

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void onBiometricError(int i, int i2, int i3) {
        String errorString;
        boolean z = false;
        Log.d("AuthController", String.format("onBiometricError(%d, %d, %d)", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3)));
        boolean z2 = i2 == 7 || i2 == 9;
        if (i2 == 100 || i2 == 3) {
            z = true;
        }
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            if (authDialog.isAllowDeviceCredentials() && z2) {
                Log.d("AuthController", "onBiometricError, lockout");
                this.mCurrentDialog.animateToCredentialUI();
            } else if (z) {
                if (i2 == 100) {
                    errorString = this.mContext.getString(17039790);
                } else {
                    errorString = getErrorString(i, i2, i3);
                }
                Log.d("AuthController", "onBiometricError, soft error: " + errorString);
                this.mCurrentDialog.onAuthenticationFailed(i, errorString);
            } else {
                String errorString2 = getErrorString(i, i2, i3);
                Log.d("AuthController", "onBiometricError, hard error: " + errorString2);
                this.mCurrentDialog.onError(i, errorString2);
            }
        } else {
            Log.w("AuthController", "onBiometricError callback but dialog is gone");
        }
        onCancelUdfps();
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void hideAuthenticationDialog() {
        Log.d("AuthController", "hideAuthenticationDialog: " + this.mCurrentDialog);
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog == null) {
            Log.d("AuthController", "dialog already gone");
            return;
        }
        authDialog.dismissFromSystemServer();
        this.mCurrentDialog = null;
        this.mOrientationListener.disable();
    }

    public boolean isUdfpsFingerDown() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController == null) {
            return false;
        }
        return udfpsController.isFingerDown();
    }

    public boolean isUdfpsEnrolled(int i) {
        Log.i("AuthController", "mUdfpsController=" + this.mUdfpsController);
        if (this.mUdfpsController == null) {
            return false;
        }
        Log.d("AuthController", "isUdfpsEnrolled mUdfpsEnrolledForUser.get(userId)=" + this.mUdfpsEnrolledForUser.get(i));
        return this.mUdfpsEnrolledForUser.get(i);
    }

    private void showDialog(SomeArgs someArgs, boolean z, Bundle bundle) {
        this.mCurrentDialogArgs = someArgs;
        PromptInfo promptInfo = (PromptInfo) someArgs.arg1;
        int[] iArr = (int[]) someArgs.arg3;
        boolean booleanValue = ((Boolean) someArgs.arg4).booleanValue();
        boolean booleanValue2 = ((Boolean) someArgs.arg5).booleanValue();
        int i = someArgs.argi1;
        AuthDialog buildDialog = buildDialog(promptInfo, booleanValue2, i, iArr, booleanValue, (String) someArgs.arg6, z, ((Long) someArgs.arg7).longValue(), ((Long) someArgs.arg8).longValue(), someArgs.argi2);
        if (buildDialog == null) {
            Log.e("AuthController", "Unsupported type configuration");
            return;
        }
        Log.d("AuthController", "userId: " + i + " savedState: " + bundle + " mCurrentDialog: " + this.mCurrentDialog + " newDialog: " + buildDialog);
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            authDialog.dismissWithoutCallback(false);
        }
        this.mReceiver = (IBiometricSysuiReceiver) someArgs.arg2;
        this.mCurrentDialog = buildDialog;
        buildDialog.show(this.mWindowManager, bundle);
        this.mOrientationListener.enable();
    }

    private void onDialogDismissed(int i) {
        Log.d("AuthController", "onDialogDismissed: " + i);
        if (this.mCurrentDialog == null) {
            Log.w("AuthController", "Dialog already dismissed");
        }
        this.mReceiver = null;
        this.mCurrentDialog = null;
        this.mOrientationListener.disable();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.SystemUI
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mCurrentDialog != null) {
            Bundle bundle = new Bundle();
            this.mCurrentDialog.onSaveState(bundle);
            this.mCurrentDialog.dismissWithoutCallback(false);
            this.mCurrentDialog = null;
            this.mOrientationListener.disable();
            if (bundle.getInt("container_state") == 4) {
                return;
            }
            if (bundle.getBoolean("credential_showing")) {
                ((PromptInfo) this.mCurrentDialogArgs.arg1).setAuthenticators(32768);
            }
            showDialog(this.mCurrentDialogArgs, true, bundle);
        }
    }

    private void onOrientationChanged() {
        AuthDialog authDialog = this.mCurrentDialog;
        if (authDialog != null) {
            authDialog.onOrientationChanged();
        }
    }

    protected AuthDialog buildDialog(PromptInfo promptInfo, boolean z, int i, int[] iArr, boolean z2, String str, boolean z3, long j, long j2, int i2) {
        return new AuthContainerView.Builder(this.mContext).setCallback(this).setPromptInfo(promptInfo).setRequireConfirmation(z).setUserId(i).setOpPackageName(str).setSkipIntro(z3).setOperationId(j).setRequestId(j2).setMultiSensorConfig(i2).build(iArr, z2, this.mFpProps, this.mFaceProps);
    }

    public RectF getSensorRectF() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController == null) {
            return null;
        }
        return udfpsController.getSensorLocation();
    }

    public void dismissFingerprintIcon() {
        Log.d("AuthController", "dismissFingerprintIcon: ");
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            udfpsController.dismissFingerprintIcon();
        }
    }

    public void showFingerprintIcon() {
        Log.d("AuthController", "showFingerprintIcon: ");
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

    public FingerprintSensorPropertiesInternal getSensorProps() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController != null) {
            return udfpsController.getSensorProps();
        }
        return null;
    }

    public void doHBM(boolean z) {
        if (this.mHandler.hasCallbacks(this.mOpenHBMRunnable)) {
            this.mHandler.removeCallbacks(this.mOpenHBMRunnable);
        }
        if (z) {
            this.mHandler.postDelayed(this.mOpenHBMRunnable, 100L);
            return;
        }
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController == null) {
            return;
        }
        udfpsController.doHBM(false);
    }
}