package com.nothing.systemui.facerecognition;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.Dependency;
import com.android.systemui.dagger.SysUISingleton;
import com.google.android.collect.Lists;
import com.nothing.systemui.facerecognition.KeyguardFaceRecognitionImpl;
import com.nothing.systemui.facerecognition.manager.CameraWorkThreadManager;
import com.nothing.utils.NTVibrateUtils;
import com.p025nt.facerecognition.manager.IFaceRecognitionManagerService;
import java.util.ArrayList;
import javax.inject.Inject;

@SysUISingleton
public class FaceRecognitionController implements KeyguardUpdateMonitor.FaceRecognitionCallback {
    private static final String FACE_UNLOCK_FAILED = "0";
    private static final String FACE_UNLOCK_SUCCESS = "1";
    private static final int MSG_FACE_RECOGNITION_FAILED_SHAKE = 508;
    private static final int MSG_KEYGUARD_DONE = 9;
    private static final int RECONNECTION_COUNT = 5;
    private static final String TAG_FACE = "FaceRecognitionController";
    private static final int VIBRATE_TIME_SHORT = 100;
    private static FaceRecognitionController mFaceRecognitionController;
    private final ArrayList<IFaceRecognitionAnimationCallback> mCallbacks = Lists.newArrayList();
    private ViewGroup mCameraPerviewView = null;
    /* access modifiers changed from: private */
    public CameraWorkThreadManager mCameraWorkThreadManager;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        public void binderDied() {
            Log.d(FaceRecognitionController.TAG_FACE, "KeyguardViewMediator binderDied");
        }
    };
    /* access modifiers changed from: private */
    public final ArrayList<FaceLockStateCallback> mFaceLockStateCallbacks = Lists.newArrayList();
    private ContentObserver mFaceLockscreenObserver = new ContentObserver(this.mHandler) {
        public void onChange(boolean z) {
            for (int i = 0; i < FaceRecognitionController.this.mFaceLockStateCallbacks.size(); i++) {
                ((FaceLockStateCallback) FaceRecognitionController.this.mFaceLockStateCallbacks.get(i)).onFaceLockStateChanged(z);
            }
        }
    };
    private Handler mFaceRecognitionHandler;
    private HandlerThread mFaceRecognitionHandlerThread;
    /* access modifiers changed from: private */
    public IFaceRecognitionManagerService mFaceRecognitionService;
    private ServiceConnection mFaceRecognitionServiceConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(FaceRecognitionController.TAG_FACE, "onServiceDisconnected name = " + componentName);
            if (FaceRecognitionController.this.mLinkToDeadSuccess) {
                FaceRecognitionController.this.mFaceRecognitionService.asBinder().unlinkToDeath(FaceRecognitionController.this.mDeathRecipient, 0);
                boolean unused = FaceRecognitionController.this.mLinkToDeadSuccess = false;
            }
            IFaceRecognitionManagerService unused2 = FaceRecognitionController.this.mFaceRecognitionService = null;
            FaceRecognitionController.this.mCameraWorkThreadManager.stopCamera();
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(FaceRecognitionController.TAG_FACE, "onServiceConnected name = " + componentName + ",mContext:" + FaceRecognitionController.this.mContext);
            try {
                iBinder.linkToDeath(FaceRecognitionController.this.mDeathRecipient, 0);
                boolean unused = FaceRecognitionController.this.mLinkToDeadSuccess = true;
            } catch (RemoteException e) {
                boolean unused2 = FaceRecognitionController.this.mLinkToDeadSuccess = false;
                Log.w(FaceRecognitionController.TAG_FACE, "linkToDeath e = " + e);
            }
            IFaceRecognitionManagerService unused3 = FaceRecognitionController.this.mFaceRecognitionService = IFaceRecognitionManagerService.Stub.asInterface(iBinder);
            ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).updateFaceRecognitionState();
        }
    };
    /* access modifiers changed from: private */
    public boolean mFrozenMode;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public boolean mIsFailed;
    private boolean mIsStartedCameraPerview = false;
    /* access modifiers changed from: private */
    public boolean mIsStartingCamera = false;
    /* access modifiers changed from: private */
    public boolean mIsTimeOut;
    private boolean mIsbindSerivce = false;
    /* access modifiers changed from: private */
    public KeyguardFaceRecognitionImpl mKeyguardFaceRecognitionImpl;
    private KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback;
    /* access modifiers changed from: private */
    public boolean mLinkToDeadSuccess;
    private PowerManager mPM;
    private Runnable mResetFaceImageRunnable = new Runnable() {
        public void run() {
            FaceRecognitionController.this.resetFaceImage();
        }
    };
    private boolean mScreenOn = true;
    private CreateFaceStartCameraPerviewCallback mStartCameraPerviewCallback = new CreateFaceStartCameraPerviewCallback();
    private Runnable mStartCamerePerviewRunnable = new Runnable() {
        public void run() {
            long currentTimeMillis = System.currentTimeMillis();
            if (FaceRecognitionController.this.mKeyguardFaceRecognitionImpl != null) {
                FaceRecognitionController.this.mKeyguardFaceRecognitionImpl.warmUpHardwareDevice();
            }
            Log.d(FaceRecognitionController.TAG_FACE, "mStartCamerePerviewRunnable cost :" + (System.currentTimeMillis() - currentTimeMillis));
        }
    };
    /* access modifiers changed from: private */
    public KeyguardViewController mStatusBarKeyguardViewManager;
    private Runnable mStopCamerePerviewRunnable = new Runnable() {
        public void run() {
            long currentTimeMillis = System.currentTimeMillis();
            if (FaceRecognitionController.this.mKeyguardFaceRecognitionImpl != null) {
                FaceRecognitionController.this.mKeyguardFaceRecognitionImpl.closeHardwareDevice();
                FaceRecognitionController.this.mKeyguardFaceRecognitionImpl.stopListeningForFaceRecognition();
                FaceRecognitionController.this.updateScreenBrightnessWhileLowLuminance(-1);
            }
            Log.d(FaceRecognitionController.TAG_FACE, "mStopCamerePerviewRunnable cost :" + (System.currentTimeMillis() - currentTimeMillis));
        }
    };
    private SurfaceView mSurfaceView;
    /* access modifiers changed from: private */
    public int mTryCount;
    private WindowManager mWindowManager;

    public interface FaceLockStateCallback {
        void onFaceLockStateChanged(boolean z);
    }

    public void pauseCameraPreview(boolean z) {
    }

    static /* synthetic */ int access$408(FaceRecognitionController faceRecognitionController) {
        int i = faceRecognitionController.mTryCount;
        faceRecognitionController.mTryCount = i + 1;
        return i;
    }

    @Inject
    public FaceRecognitionController(Context context) {
        this.mContext = context;
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        this.mPM = powerManager;
        this.mScreenOn = powerManager.isScreenOn();
        this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        this.mCameraWorkThreadManager = new CameraWorkThreadManager(this.mStopCamerePerviewRunnable, this.mStartCamerePerviewRunnable);
        this.mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() {
            public void onUserSwitching(int i) {
            }

            public void onUserSwitchComplete(int i) {
                Log.d(FaceRecognitionController.TAG_FACE, "onUserSwitchComplete: userid = " + i);
                if (i == 0) {
                    FaceRecognitionController.this.resetFaceImage();
                    ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).updateFaceRecognitionState();
                } else if (FaceRecognitionController.this.mKeyguardFaceRecognitionImpl != null) {
                    FaceRecognitionController.this.mKeyguardFaceRecognitionImpl.resetAuthUnlockAttempt();
                }
            }
        };
        HandlerThread handlerThread = new HandlerThread("KeyguardFaceRecognitionHandlerThread");
        this.mFaceRecognitionHandlerThread = handlerThread;
        handlerThread.start();
        this.mFaceRecognitionHandler = new Handler(this.mFaceRecognitionHandlerThread.getLooper());
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("nt_face_unlock"), false, this.mFaceLockscreenObserver);
    }

    public void initKeyguardFaceRecognition() {
        KeyguardFaceRecognitionImpl instance = KeyguardFaceRecognitionImpl.getInstance(this.mContext, UserHandle.myUserId(), this.mFaceRecognitionHandler);
        this.mKeyguardFaceRecognitionImpl = instance;
        instance.setStartCameraPreviewCallback(this.mStartCameraPerviewCallback);
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).registerCallback(this.mKeyguardUpdateMonitorCallback);
    }

    private class CreateFaceStartCameraPerviewCallback extends KeyguardFaceRecognitionImpl.StartCameraPreviewCallback {
        private CreateFaceStartCameraPerviewCallback() {
        }

        public void onCameraPreviewLaunching() {
            Log.d(FaceRecognitionController.TAG_FACE, "onStartCameraPerviewStarting  noticeString:");
        }

        public void onCameraPreviewLaunchSucceed(boolean z) {
            Log.d(FaceRecognitionController.TAG_FACE, "onStartCameraPerviewEnd success:" + z + ",mIsStartingCamera " + FaceRecognitionController.this.mIsStartingCamera);
            int unused = FaceRecognitionController.this.mTryCount = 0;
            boolean unused2 = FaceRecognitionController.this.mIsTimeOut = false;
            boolean unused3 = FaceRecognitionController.this.mIsFailed = false;
            FaceRecognitionController.this.onFaceSuccessConnect();
            if (FaceRecognitionController.this.mKeyguardFaceRecognitionImpl != null && FaceRecognitionController.this.mIsStartingCamera) {
                FaceRecognitionController.this.mKeyguardFaceRecognitionImpl.startListeningForFaceRecognition();
            }
        }

        public void onCameraPreviewLaunchError() {
            Log.d(FaceRecognitionController.TAG_FACE, "onStartCameraPerviewError:");
            FaceRecognitionController.this.postToStopCamera();
            if (FaceRecognitionController.this.mTryCount < 5 && ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).shouldListenForFaceRecognition()) {
                FaceRecognitionController.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        FaceRecognitionController.access$408(FaceRecognitionController.this);
                        FaceRecognitionController.this.postToStartCamera();
                    }
                }, 200);
            }
        }

        public void onCameraPreviewTimeout() {
            Log.d(FaceRecognitionController.TAG_FACE, "onCameraPreviewTimeout:");
            FaceRecognitionController.this.postToStopCamera();
        }

        public void onFaceRecognitionError(int i) {
            Log.d(FaceRecognitionController.TAG_FACE, "onFaceRecognitionError featureString:" + i);
            if (i == 7) {
                boolean unused = FaceRecognitionController.this.mIsFailed = true;
                FaceRecognitionController.this.startFailureAnimation();
                FaceRecognitionController.this.postToStopCamera();
                FaceRecognitionController.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        FaceRecognitionController.this.vibrate();
                    }
                }, 300);
            } else if (i == 9) {
                boolean unused2 = FaceRecognitionController.this.mFrozenMode = true;
                FaceRecognitionController.this.startFreezeAnimation();
                FaceRecognitionController.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        FaceRecognitionController.this.vibrate();
                    }
                }, 300);
                FaceRecognitionController.this.postToStopCamera();
            }
        }

        public void onFaceCompare(boolean z) {
            if (z) {
                boolean z2 = false;
                boolean unused = FaceRecognitionController.this.mIsFailed = false;
                boolean unused2 = FaceRecognitionController.this.mIsTimeOut = false;
                if (FaceRecognitionController.this.mKeyguardFaceRecognitionImpl != null) {
                    FaceRecognitionController.this.mKeyguardFaceRecognitionImpl.stopListeningForFaceRecognition();
                    FaceRecognitionController.this.mKeyguardFaceRecognitionImpl.resetAuthUnlockAttempt();
                }
                boolean z3 = Settings.Secure.getIntForUser(FaceRecognitionController.this.mContext.getContentResolver(), "face_unlock_dismisses_keyguard", 0, -2) == 1;
                if (FaceRecognitionController.this.mStatusBarKeyguardViewManager.isShowing() && FaceRecognitionController.this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                    z2 = true;
                }
                ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).onFaceRecognitionSucceeded(true);
                Log.d(FaceRecognitionController.TAG_FACE, "onFaceCompare: isfaceUnlockGoToLauncher = " + z3 + ":" + z2);
                if (z3 || z2) {
                    FaceRecognitionController.this.mHandler.post(new Runnable() {
                        public void run() {
                            FaceRecognitionController.this.mStatusBarKeyguardViewManager.notifyKeyguardAuthenticatedForFaceRecognition(true);
                        }
                    });
                } else {
                    FaceRecognitionController.this.startSuccessAnimation();
                }
                FaceRecognitionController.this.postToStopCamera();
            }
        }

        public void onFaceCompareFailShake() {
            FaceRecognitionController.this.startFailureAnimation();
        }

        public void onFaceLoading() {
            FaceRecognitionController.this.startLoadingAnimation();
        }

        public void onLockoutReset() {
            boolean unused = FaceRecognitionController.this.mFrozenMode = false;
            if (!((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFaceRecognitionSucceeded()) {
                FaceRecognitionController.this.resetFaceImage();
                ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).updateFaceRecognitionState();
            }
        }

        public void onAuthenticationTimeout() {
            Log.d(FaceRecognitionController.TAG_FACE, "onAuthenticationTimeout:");
            boolean unused = FaceRecognitionController.this.mIsTimeOut = true;
            FaceRecognitionController.this.postToStopCamera();
            FaceRecognitionController.this.onFaceAuthenticationTimeout();
        }

        public void onScreenBrightnessOverride(int i, float f) {
            if (FaceRecognitionController.this.mIsStartingCamera) {
                FaceRecognitionController.this.updateScreenBrightnessWhileLowLuminance(i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void vibrate() {
        NTVibrateUtils.getInstance(this.mContext).playVerityVibrate(NTVibrateUtils.VibrateType.FINGERPRINT_VERIFICATION_ERROR);
    }

    public void resetAuthUnlockAttempt() {
        KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl = this.mKeyguardFaceRecognitionImpl;
        if (keyguardFaceRecognitionImpl != null) {
            keyguardFaceRecognitionImpl.resetAuthUnlockAttempt();
        }
    }

    public void cameraPervieViewAddOrRemove(boolean z) {
        if (z) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(2010);
            layoutParams.setTitle("cameraperview");
            layoutParams.format = 1;
            layoutParams.height = 1;
            layoutParams.width = 1;
            layoutParams.x = 0;
            layoutParams.y = 0;
            layoutParams.gravity = 48;
            layoutParams.alpha = 0.0f;
            layoutParams.userActivityTimeout = 10000;
            layoutParams.flags = 280;
            this.mWindowManager.addView(this.mCameraPerviewView, layoutParams);
            this.mIsStartedCameraPerview = true;
            return;
        }
        try {
            this.mWindowManager.removeView(this.mCameraPerviewView);
            this.mIsStartedCameraPerview = false;
        } catch (IllegalArgumentException e) {
            Log.d(TAG_FACE, "", e);
        }
    }

    public void startCameraPreview() {
        Log.d(TAG_FACE, "startCameraPerview Turn on! mIsStartCameraPerview :" + this.mIsStartedCameraPerview);
        this.mCameraWorkThreadManager.startCamera();
    }

    public void stopCameraPreview() {
        pauseCameraPreview(true);
        Log.d(TAG_FACE, "stopCameraPerview Turn off!mIsStartCameraPerview:" + this.mIsStartedCameraPerview);
        this.mCameraWorkThreadManager.stopCamera();
    }

    public void postToStartCamera() {
        if (this.mIsStartingCamera || !this.mScreenOn || isFrozenMode()) {
            Log.d(TAG_FACE, "Not match to start camera, mIsStartingCamera" + this.mIsStartingCamera + ", screenOn " + this.mScreenOn + ", frozonmode " + this.mFrozenMode);
            return;
        }
        this.mIsStartingCamera = true;
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).setIsFaceCameraStarting(this.mIsStartingCamera);
        Log.d(TAG_FACE, "postToStartCamera ");
        startCameraPreview();
    }

    public void postToStopCamera() {
        if (this.mIsStartingCamera) {
            this.mIsStartingCamera = false;
            ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).setIsFaceCameraStarting(this.mIsStartingCamera);
            Log.d(TAG_FACE, "postToStopCamera");
            stopCameraPreview();
        }
    }

    public void setScreenOn(boolean z) {
        this.mScreenOn = z;
        if (!z) {
            this.mHandler.postDelayed(this.mResetFaceImageRunnable, 0);
        } else {
            this.mHandler.removeCallbacks(this.mResetFaceImageRunnable);
        }
    }

    public boolean isCameraPreviewStarting() {
        return this.mIsStartingCamera;
    }

    public void registerCallback(IFaceRecognitionAnimationCallback iFaceRecognitionAnimationCallback) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            if (this.mCallbacks.get(i) == iFaceRecognitionAnimationCallback) {
                Log.d(TAG_FACE, "Object tried to add another callback", new Exception("Called by"));
                return;
            }
        }
        this.mCallbacks.add(iFaceRecognitionAnimationCallback);
    }

    public void startFailureAnimation() {
        userActivity();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).startFailureAnimation();
        }
    }

    public void onFaceSuccessConnect() {
        userActivity();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).onFaceSuccessConnect();
        }
    }

    public void startFreezeAnimation() {
        userActivity();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).startFreezeAnimation();
        }
    }

    public void startSuccessAnimation() {
        userActivity();
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).startSuccessAnimation();
        }
    }

    public void startLoadingAnimation() {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).startLoadingAnimation();
        }
    }

    public void resetFaceImage() {
        if (isFrozenMode()) {
            Log.d(TAG_FACE, "isFrozenMode not reset face image");
            return;
        }
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).resetFaceImage();
        }
    }

    public void onFaceAuthenticationTimeout() {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).onFaceAuthenticationTimeout();
        }
    }

    public void startSlideUpAnimation() {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).startSlideUpAnimation();
        }
    }

    public void setStatusBarKeyguardViewManager(KeyguardViewController keyguardViewController) {
        this.mStatusBarKeyguardViewManager = keyguardViewController;
    }

    public void userActivity() {
        this.mPM.userActivity(SystemClock.uptimeMillis(), false);
    }

    public boolean isFrozenMode() {
        return this.mFrozenMode;
    }

    public boolean isTimeOut() {
        return this.mIsTimeOut;
    }

    public boolean isFailed() {
        return this.mIsFailed;
    }

    public void removeCallback(IFaceRecognitionAnimationCallback iFaceRecognitionAnimationCallback) {
        this.mCallbacks.remove((Object) iFaceRecognitionAnimationCallback);
    }

    /* access modifiers changed from: private */
    public void updateScreenBrightnessWhileLowLuminance(int i) {
        this.mHandler.post(new Runnable() {
            public void run() {
                KeyguardViewController unused = FaceRecognitionController.this.mStatusBarKeyguardViewManager;
            }
        });
    }

    public void registerFaceLockStateCallback(FaceLockStateCallback faceLockStateCallback) {
        for (int i = 0; i < this.mFaceLockStateCallbacks.size(); i++) {
            if (this.mFaceLockStateCallbacks.get(i) == faceLockStateCallback) {
                Log.d(TAG_FACE, "Object tried to add another callback", new Exception("Called by"));
                return;
            }
        }
        this.mFaceLockStateCallbacks.add(faceLockStateCallback);
    }

    public void unRegisterFaceLockStateCallback(FaceLockStateCallback faceLockStateCallback) {
        this.mFaceLockStateCallbacks.remove((Object) faceLockStateCallback);
    }
}
