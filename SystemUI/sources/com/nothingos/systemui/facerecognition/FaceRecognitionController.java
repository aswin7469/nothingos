package com.nothingos.systemui.facerecognition;

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
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.Dependency;
import com.google.android.collect.Lists;
import com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl;
import com.nothingos.systemui.facerecognition.manager.CameraWorkThreadManager;
import com.nothingos.utils.NtVibrateUtils;
import com.nt.facerecognition.manager.IFaceRecognitionManagerService;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class FaceRecognitionController implements KeyguardUpdateMonitor.FaceRecognitionCallback {
    private Context mContext;
    private Handler mFaceRecognitionHandler;
    private HandlerThread mFaceRecognitionHandlerThread;
    private IFaceRecognitionManagerService mFaceRecognitionService;
    private boolean mFrozenMode;
    private boolean mIsFailed;
    private boolean mIsTimeOut;
    private KeyguardFaceRecognitionImpl mKeyguardFaceRecognitionImpl;
    private boolean mLinkToDeadSuccess;
    private PowerManager mPM;
    private boolean mScreenOn;
    private KeyguardViewController mStatusBarKeyguardViewManager;
    private int mTryCount;
    private WindowManager mWindowManager;
    private boolean mIsbindSerivce = false;
    private ViewGroup mCameraPerviewView = null;
    private boolean mIsStartedCameraPerview = false;
    private boolean mIsStartingCamera = false;
    private Handler mHandler = new Handler();
    private final ArrayList<IFaceRecognitionAnimationCallback> mCallbacks = Lists.newArrayList();
    private final ArrayList<FaceLockStateCallback> mFaceLockStateCallbacks = Lists.newArrayList();
    private Runnable mStartCamerePerviewRunnable = new Runnable() { // from class: com.nothingos.systemui.facerecognition.FaceRecognitionController.2
        @Override // java.lang.Runnable
        public void run() {
            long currentTimeMillis = System.currentTimeMillis();
            if (FaceRecognitionController.this.mKeyguardFaceRecognitionImpl != null) {
                FaceRecognitionController.this.mKeyguardFaceRecognitionImpl.warmUpHardwareDevice();
            }
            Log.d("FaceRecognitionController", "mStartCamerePerviewRunnable cost :" + (System.currentTimeMillis() - currentTimeMillis));
        }
    };
    private Runnable mStopCamerePerviewRunnable = new Runnable() { // from class: com.nothingos.systemui.facerecognition.FaceRecognitionController.3
        @Override // java.lang.Runnable
        public void run() {
            long currentTimeMillis = System.currentTimeMillis();
            if (FaceRecognitionController.this.mKeyguardFaceRecognitionImpl != null) {
                FaceRecognitionController.this.mKeyguardFaceRecognitionImpl.closeHardwareDevice();
                FaceRecognitionController.this.mKeyguardFaceRecognitionImpl.stopListeningForFaceRecognition();
                FaceRecognitionController.this.updateScreenBrightnessWhileLowLuminance(-1);
            }
            Log.d("FaceRecognitionController", "mStopCamerePerviewRunnable cost :" + (System.currentTimeMillis() - currentTimeMillis));
        }
    };
    private ContentObserver mFaceLockscreenObserver = new ContentObserver(this.mHandler) { // from class: com.nothingos.systemui.facerecognition.FaceRecognitionController.4
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            for (int i = 0; i < FaceRecognitionController.this.mFaceLockStateCallbacks.size(); i++) {
                ((FaceLockStateCallback) FaceRecognitionController.this.mFaceLockStateCallbacks.get(i)).onFaceLockStateChanged(z);
            }
        }
    };
    private CreateFaceStartCameraPerviewCallback mStartCameraPerviewCallback = new CreateFaceStartCameraPerviewCallback();
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.nothingos.systemui.facerecognition.FaceRecognitionController.5
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.d("FaceRecognitionController", "KeyguardViewMediator binderDied");
        }
    };
    private ServiceConnection mFaceRecognitionServiceConnection = new ServiceConnection() { // from class: com.nothingos.systemui.facerecognition.FaceRecognitionController.6
        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("FaceRecognitionController", "onServiceDisconnected name = " + componentName);
            if (FaceRecognitionController.this.mLinkToDeadSuccess) {
                FaceRecognitionController.this.mFaceRecognitionService.asBinder().unlinkToDeath(FaceRecognitionController.this.mDeathRecipient, 0);
                FaceRecognitionController.this.mLinkToDeadSuccess = false;
            }
            FaceRecognitionController.this.mFaceRecognitionService = null;
            FaceRecognitionController.this.mCameraWorkThreadManager.stopCamera();
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("FaceRecognitionController", "onServiceConnected name = " + componentName + ",mContext:" + FaceRecognitionController.this.mContext);
            try {
                iBinder.linkToDeath(FaceRecognitionController.this.mDeathRecipient, 0);
                FaceRecognitionController.this.mLinkToDeadSuccess = true;
            } catch (RemoteException e) {
                FaceRecognitionController.this.mLinkToDeadSuccess = false;
                Log.w("FaceRecognitionController", "linkToDeath e = " + e);
            }
            FaceRecognitionController.this.mFaceRecognitionService = IFaceRecognitionManagerService.Stub.asInterface(iBinder);
            ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).updateFaceRecognitionState();
        }
    };
    private Runnable mResetFaceImageRunnable = new Runnable() { // from class: com.nothingos.systemui.facerecognition.FaceRecognitionController.7
        @Override // java.lang.Runnable
        public void run() {
            FaceRecognitionController.this.resetFaceImage();
        }
    };
    private CameraWorkThreadManager mCameraWorkThreadManager = new CameraWorkThreadManager(this.mStopCamerePerviewRunnable, this.mStartCamerePerviewRunnable);
    private KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.nothingos.systemui.facerecognition.FaceRecognitionController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserSwitching(int i) {
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserSwitchComplete(int i) {
            Log.d("FaceRecognitionController", "onUserSwitchComplete: userid = " + i);
            if (i != 0) {
                if (FaceRecognitionController.this.mKeyguardFaceRecognitionImpl == null) {
                    return;
                }
                FaceRecognitionController.this.mKeyguardFaceRecognitionImpl.resetAuthUnlockAttempt();
                return;
            }
            FaceRecognitionController.this.resetFaceImage();
            ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).updateFaceRecognitionState();
        }
    };

    /* loaded from: classes2.dex */
    public interface FaceLockStateCallback {
        void onFaceLockStateChanged(boolean z);
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitor.FaceRecognitionCallback
    public void pauseCameraPreview(boolean z) {
    }

    static /* synthetic */ int access$408(FaceRecognitionController faceRecognitionController) {
        int i = faceRecognitionController.mTryCount;
        faceRecognitionController.mTryCount = i + 1;
        return i;
    }

    public FaceRecognitionController(Context context) {
        this.mScreenOn = true;
        this.mContext = context;
        PowerManager powerManager = (PowerManager) context.getSystemService("power");
        this.mPM = powerManager;
        this.mScreenOn = powerManager.isScreenOn();
        this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
        HandlerThread handlerThread = new HandlerThread("KeyguardFaceRecognitionHandlerThread");
        this.mFaceRecognitionHandlerThread = handlerThread;
        handlerThread.start();
        this.mFaceRecognitionHandler = new Handler(this.mFaceRecognitionHandlerThread.getLooper());
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).registerCallback(this.mKeyguardUpdateMonitorCallback);
        this.mContext.getContentResolver().registerContentObserver(Settings.Secure.getUriFor("nt_face_unlock"), false, this.mFaceLockscreenObserver);
    }

    public void initKeyguardFaceRecognition() {
        KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl = KeyguardFaceRecognitionImpl.getInstance(this.mContext, UserHandle.myUserId(), this.mFaceRecognitionHandler);
        this.mKeyguardFaceRecognitionImpl = keyguardFaceRecognitionImpl;
        keyguardFaceRecognitionImpl.setStartCameraPreviewCallback(this.mStartCameraPerviewCallback);
    }

    /* loaded from: classes2.dex */
    private class CreateFaceStartCameraPerviewCallback extends KeyguardFaceRecognitionImpl.StartCameraPreviewCallback {
        private CreateFaceStartCameraPerviewCallback() {
        }

        @Override // com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.StartCameraPreviewCallback
        public void onCameraPreviewLaunching() {
            Log.d("FaceRecognitionController", "onStartCameraPerviewStarting  noticeString:");
        }

        @Override // com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.StartCameraPreviewCallback
        public void onCameraPreviewLaunchSucceed(boolean z) {
            Log.d("FaceRecognitionController", "onStartCameraPerviewEnd success:" + z + ",mIsStartingCamera " + FaceRecognitionController.this.mIsStartingCamera);
            FaceRecognitionController.this.mTryCount = 0;
            FaceRecognitionController.this.mIsTimeOut = false;
            FaceRecognitionController.this.mIsFailed = false;
            FaceRecognitionController.this.onFaceSuccessConnect();
            if (FaceRecognitionController.this.mKeyguardFaceRecognitionImpl == null || !FaceRecognitionController.this.mIsStartingCamera) {
                return;
            }
            FaceRecognitionController.this.mKeyguardFaceRecognitionImpl.startListeningForFaceRecognition();
        }

        @Override // com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.StartCameraPreviewCallback
        public void onCameraPreviewLaunchError() {
            Log.d("FaceRecognitionController", "onStartCameraPerviewError:");
            FaceRecognitionController.this.postToStopCamera();
            if (FaceRecognitionController.this.mTryCount >= 5 || !((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).shouldListenForFaceRecognition()) {
                return;
            }
            FaceRecognitionController.this.mHandler.postDelayed(new Runnable() { // from class: com.nothingos.systemui.facerecognition.FaceRecognitionController.CreateFaceStartCameraPerviewCallback.1
                @Override // java.lang.Runnable
                public void run() {
                    FaceRecognitionController.access$408(FaceRecognitionController.this);
                    FaceRecognitionController.this.postToStartCamera();
                }
            }, 200L);
        }

        @Override // com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.StartCameraPreviewCallback
        public void onFaceRecognitionError(int i) {
            Log.d("FaceRecognitionController", "onFaceRecognitionError featureString:" + i);
            if (i == 7) {
                FaceRecognitionController.this.mIsFailed = true;
                FaceRecognitionController.this.startFailureAnimation();
                FaceRecognitionController.this.postToStopCamera();
                FaceRecognitionController.this.mHandler.postDelayed(new Runnable() { // from class: com.nothingos.systemui.facerecognition.FaceRecognitionController.CreateFaceStartCameraPerviewCallback.2
                    @Override // java.lang.Runnable
                    public void run() {
                        FaceRecognitionController.this.vibrate();
                    }
                }, 300L);
            } else if (i != 9) {
            } else {
                FaceRecognitionController.this.mFrozenMode = true;
                FaceRecognitionController.this.startFreezeAnimation();
                FaceRecognitionController.this.mHandler.postDelayed(new Runnable() { // from class: com.nothingos.systemui.facerecognition.FaceRecognitionController.CreateFaceStartCameraPerviewCallback.3
                    @Override // java.lang.Runnable
                    public void run() {
                        FaceRecognitionController.this.vibrate();
                    }
                }, 300L);
                FaceRecognitionController.this.postToStopCamera();
            }
        }

        @Override // com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.StartCameraPreviewCallback
        public void onFaceCompare(boolean z) {
            if (z) {
                boolean z2 = false;
                FaceRecognitionController.this.mIsFailed = false;
                FaceRecognitionController.this.mIsTimeOut = false;
                if (FaceRecognitionController.this.mKeyguardFaceRecognitionImpl != null) {
                    FaceRecognitionController.this.mKeyguardFaceRecognitionImpl.stopListeningForFaceRecognition();
                    FaceRecognitionController.this.mKeyguardFaceRecognitionImpl.resetAuthUnlockAttempt();
                }
                boolean z3 = Settings.Secure.getIntForUser(FaceRecognitionController.this.mContext.getContentResolver(), "face_unlock_dismisses_keyguard", 0, -2) == 1;
                if (FaceRecognitionController.this.mStatusBarKeyguardViewManager.isShowing() && FaceRecognitionController.this.mStatusBarKeyguardViewManager.isBouncerShowing()) {
                    z2 = true;
                }
                ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).onFaceRecognitionSucceeded(true);
                Log.d("FaceRecognitionController", "onFaceCompare: isfaceUnlockGoToLauncher = " + z3);
                if (z3 || z2) {
                    FaceRecognitionController.this.mHandler.post(new Runnable() { // from class: com.nothingos.systemui.facerecognition.FaceRecognitionController.CreateFaceStartCameraPerviewCallback.4
                        @Override // java.lang.Runnable
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

        @Override // com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.StartCameraPreviewCallback
        public void onFaceCompareFailShake() {
            FaceRecognitionController.this.startFailureAnimation();
        }

        @Override // com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.StartCameraPreviewCallback
        public void onFaceLoading() {
            FaceRecognitionController.this.startLoadingAnimation();
        }

        @Override // com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.StartCameraPreviewCallback
        public void onLockoutReset() {
            FaceRecognitionController.this.mFrozenMode = false;
            if (!((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFaceRecognitionSucceeded()) {
                FaceRecognitionController.this.resetFaceImage();
                ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).updateFaceRecognitionState();
            }
        }

        @Override // com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.StartCameraPreviewCallback
        public void onAuthenticationTimeout() {
            Log.d("FaceRecognitionController", "onAuthenticationTimeout:");
            FaceRecognitionController.this.mIsTimeOut = true;
            FaceRecognitionController.this.postToStopCamera();
            FaceRecognitionController.this.onFaceAuthenticationTimeout();
        }

        @Override // com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.StartCameraPreviewCallback
        public void onScreenBrightnessOverride(int i, float f) {
            if (FaceRecognitionController.this.mIsStartingCamera) {
                FaceRecognitionController.this.updateScreenBrightnessWhileLowLuminance(i);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void vibrate() {
        NtVibrateUtils.getInstance(this.mContext).playVerityVibrate("/system/etc/richtapresources/NT_unlock_error.he");
    }

    public void resetAuthUnlockAttempt() {
        KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl = this.mKeyguardFaceRecognitionImpl;
        if (keyguardFaceRecognitionImpl != null) {
            keyguardFaceRecognitionImpl.resetAuthUnlockAttempt();
        }
    }

    public void startCameraPreview() {
        Log.d("FaceRecognitionController", "startCameraPerview Turn on! mIsStartCameraPerview :" + this.mIsStartedCameraPerview);
        this.mCameraWorkThreadManager.startCamera();
    }

    public void stopCameraPreview() {
        pauseCameraPreview(true);
        Log.d("FaceRecognitionController", "stopCameraPerview Turn off!mIsStartCameraPerview:" + this.mIsStartedCameraPerview);
        this.mCameraWorkThreadManager.stopCamera();
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitor.FaceRecognitionCallback
    public void postToStartCamera() {
        if (this.mIsStartingCamera || !this.mScreenOn || isFrozenMode()) {
            Log.d("FaceRecognitionController", "Not match to start camera, mIsStartingCamera" + this.mIsStartingCamera + ", screenOn " + this.mScreenOn + ", frozonmode " + this.mFrozenMode);
            return;
        }
        this.mIsStartingCamera = true;
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).setIsFaceCameraStarting(this.mIsStartingCamera);
        Log.d("FaceRecognitionController", "postToStartCamera ");
        startCameraPreview();
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitor.FaceRecognitionCallback
    public void postToStopCamera() {
        if (!this.mIsStartingCamera) {
            return;
        }
        this.mIsStartingCamera = false;
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).setIsFaceCameraStarting(this.mIsStartingCamera);
        Log.d("FaceRecognitionController", "postToStopCamera");
        stopCameraPreview();
    }

    public void setScreenOn(boolean z) {
        this.mScreenOn = z;
        if (!z) {
            this.mHandler.postDelayed(this.mResetFaceImageRunnable, 0L);
        } else {
            this.mHandler.removeCallbacks(this.mResetFaceImageRunnable);
        }
    }

    @Override // com.android.keyguard.KeyguardUpdateMonitor.FaceRecognitionCallback
    public boolean isCameraPreviewStarting() {
        return this.mIsStartingCamera;
    }

    public void registerCallback(IFaceRecognitionAnimationCallback iFaceRecognitionAnimationCallback) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            if (this.mCallbacks.get(i) == iFaceRecognitionAnimationCallback) {
                Log.d("FaceRecognitionController", "Object tried to add another callback", new Exception("Called by"));
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
            Log.d("FaceRecognitionController", "isFrozenMode not reset face image");
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
        this.mCallbacks.remove(iFaceRecognitionAnimationCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateScreenBrightnessWhileLowLuminance(int i) {
        this.mHandler.post(new Runnable() { // from class: com.nothingos.systemui.facerecognition.FaceRecognitionController.8
            @Override // java.lang.Runnable
            public void run() {
                KeyguardViewController unused = FaceRecognitionController.this.mStatusBarKeyguardViewManager;
            }
        });
    }
}
