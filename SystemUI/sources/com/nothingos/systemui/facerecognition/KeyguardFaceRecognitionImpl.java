package com.nothingos.systemui.facerecognition;

import android.app.ActivityManager;
import android.content.Context;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
import com.nt.facerecognition.manager.CameraPreviewProperty;
import com.nt.facerecognition.manager.IFaceRecognitionManagerService;
import com.nt.facerecognition.manager.NtFaceRecognitionManager;
/* loaded from: classes2.dex */
public class KeyguardFaceRecognitionImpl {
    private static KeyguardFaceRecognitionImpl mFaceRecognitionImpl;
    private CancellationSignal mCancellationSignal;
    private Context mContext;
    protected Handler mHandler;
    private NtFaceRecognitionManager mNtFaceRecognitionManager;
    private IFaceRecognitionManagerService mService;
    private StartCameraPreviewCallback mStartCameraPreviewCallback;
    private int mUserId;
    private int mRecongnizeFailTime = 0;
    private int mCompareTimes = 0;
    private boolean mCompareCD = false;
    private int mCreateTimes = 0;
    private boolean mFaceServiceDead = true;
    private boolean mAuthTimeOut = false;
    private Runnable compareCDRunnable = new Runnable() { // from class: com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.1
        @Override // java.lang.Runnable
        public void run() {
            KeyguardFaceRecognitionImpl.this.mCompareCD = false;
        }
    };
    private Runnable mFaceRecognitionTimeOut = new Runnable() { // from class: com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.2
        @Override // java.lang.Runnable
        public void run() {
            if (KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback != null) {
                KeyguardFaceRecognitionImpl.this.mAuthTimeOut = true;
                KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onAuthenticationTimeout();
            }
        }
    };
    private KeyguardUpdateMonitorCallback mUserSwitch = new KeyguardUpdateMonitorCallback() { // from class: com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.3
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserSwitchComplete(int i) {
            Log.d("KeyguardFaceRecognitionImpl", "onUserSwitchComplete");
            KeyguardFaceRecognitionImpl.this.resetFaceServiceState();
        }
    };
    private NtFaceRecognitionManager.AuthenticationCallback mAuthenticationCallback = new NtFaceRecognitionManager.AuthenticationCallback() { // from class: com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.4
        public void onAuthenticationTimeout() {
        }

        public void onAuthenticationFailed(int i) {
            Log.i("KeyguardFaceRecognitionImpl", "onAuthenticationFailed mRecongnizeFailTime:" + KeyguardFaceRecognitionImpl.this.mRecongnizeFailTime + ", errMsgId:" + i);
            if (KeyguardFaceRecognitionImpl.this.mCompareCD) {
                return;
            }
            KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl = KeyguardFaceRecognitionImpl.this;
            keyguardFaceRecognitionImpl.mHandler.removeCallbacks(keyguardFaceRecognitionImpl.mFaceRecognitionTimeOut);
            if (((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFaceCameraStarting()) {
                KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl2 = KeyguardFaceRecognitionImpl.this;
                keyguardFaceRecognitionImpl2.mHandler.postDelayed(keyguardFaceRecognitionImpl2.mFaceRecognitionTimeOut, 5000L);
            }
            if (i != 101 && i != 120) {
                return;
            }
            KeyguardFaceRecognitionImpl.access$408(KeyguardFaceRecognitionImpl.this);
            KeyguardFaceRecognitionImpl.this.mCompareCD = true;
            Log.d("KeyguardFaceRecognitionImpl", "compare failed " + KeyguardFaceRecognitionImpl.this.mRecongnizeFailTime);
            if (KeyguardFaceRecognitionImpl.this.mRecongnizeFailTime % 5 == 0) {
                KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onFaceCompareFailShake();
                KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl3 = KeyguardFaceRecognitionImpl.this;
                keyguardFaceRecognitionImpl3.mHandler.postDelayed(keyguardFaceRecognitionImpl3.compareCDRunnable, 500L);
                return;
            }
            KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onFaceLoading();
            KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl4 = KeyguardFaceRecognitionImpl.this;
            keyguardFaceRecognitionImpl4.mHandler.postDelayed(keyguardFaceRecognitionImpl4.compareCDRunnable, 300L);
        }

        public void onAuthenticationSucceeded(NtFaceRecognitionManager.AuthenticationResult authenticationResult) {
            Log.i("KeyguardFaceRecognitionImpl", "onAuthenticationSucceeded");
            KeyguardFaceRecognitionImpl.this.mRecongnizeFailTime = 0;
            KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl = KeyguardFaceRecognitionImpl.this;
            keyguardFaceRecognitionImpl.mHandler.removeCallbacks(keyguardFaceRecognitionImpl.mFaceRecognitionTimeOut);
            if (KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback != null) {
                KeyguardFaceRecognitionImpl.this.mCompareCD = true;
                KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onFaceCompare(true);
            }
        }

        public void onAuthenticationHelp(int i, CharSequence charSequence) {
            KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl = KeyguardFaceRecognitionImpl.this;
            keyguardFaceRecognitionImpl.mHandler.removeCallbacks(keyguardFaceRecognitionImpl.mFaceRecognitionTimeOut);
            if (((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFaceCameraStarting()) {
                KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl2 = KeyguardFaceRecognitionImpl.this;
                keyguardFaceRecognitionImpl2.mHandler.postDelayed(keyguardFaceRecognitionImpl2.mFaceRecognitionTimeOut, 5000L);
            }
        }

        public void onAuthenticationError(int i, CharSequence charSequence) {
            Log.i("KeyguardFaceRecognitionImpl", "onAuthenticationError msgId: " + i + " errString: " + ((Object) charSequence));
            KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl = KeyguardFaceRecognitionImpl.this;
            keyguardFaceRecognitionImpl.mHandler.removeCallbacks(keyguardFaceRecognitionImpl.mFaceRecognitionTimeOut);
            if (KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback != null && !KeyguardFaceRecognitionImpl.this.mAuthTimeOut) {
                KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onFaceRecognitionError(i);
            }
            KeyguardFaceRecognitionImpl.this.mRecongnizeFailTime = 0;
            KeyguardFaceRecognitionImpl.this.mCompareCD = true;
            KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl2 = KeyguardFaceRecognitionImpl.this;
            keyguardFaceRecognitionImpl2.mHandler.postDelayed(keyguardFaceRecognitionImpl2.compareCDRunnable, 300L);
        }

        public void onAuthenticationAcquired(int i) {
            KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl = KeyguardFaceRecognitionImpl.this;
            keyguardFaceRecognitionImpl.mHandler.removeCallbacks(keyguardFaceRecognitionImpl.mFaceRecognitionTimeOut);
            if (((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFaceCameraStarting()) {
                KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl2 = KeyguardFaceRecognitionImpl.this;
                keyguardFaceRecognitionImpl2.mHandler.postDelayed(keyguardFaceRecognitionImpl2.mFaceRecognitionTimeOut, 5000L);
            }
        }

        public void onScreenBrightnessOverride(int i, float f) {
            Log.d("KeyguardFaceRecognitionImpl", "onScreenBrightnessOverride, screenbrightnessvalue:" + i + ",ambientLux:" + f);
            KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onScreenBrightnessOverride(i, f);
        }
    };
    private final NtFaceRecognitionManager.LockoutResetCallback mLockoutResetCallback = new NtFaceRecognitionManager.LockoutResetCallback() { // from class: com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.5
        public void onLockoutReset() {
            Log.i("KeyguardFaceRecognitionImpl", "onLockoutReset");
            KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onLockoutReset();
        }
    };
    private NtFaceRecognitionManager.WarmUpHardwareDeviceCallback mWarmUpHardwareDeviceCallback = new NtFaceRecognitionManager.WarmUpHardwareDeviceCallback() { // from class: com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.6
        public void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) {
            Log.d("KeyguardFaceRecognitionImpl", "onWarmUpHardwareDeviceResult (" + cameraPreviewProperty.getCameraWidth() + ", " + cameraPreviewProperty.getCameraHeight() + ", " + cameraPreviewProperty.getCameraIsOpen() + ")");
            if (KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback != null) {
                if (cameraPreviewProperty.getCameraIsOpen()) {
                    KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onCameraPreviewLaunchSucceed(true);
                    KeyguardFaceRecognitionImpl.this.mAuthTimeOut = false;
                    KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl = KeyguardFaceRecognitionImpl.this;
                    keyguardFaceRecognitionImpl.mHandler.postDelayed(keyguardFaceRecognitionImpl.mFaceRecognitionTimeOut, 5000L);
                    return;
                }
                KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onCameraPreviewLaunchError();
            }
        }
    };
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.12
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.d("KeyguardFaceRecognitionImpl", "KeyguardViewMediator binderDied");
            KeyguardFaceRecognitionImpl.this.resetFaceServiceState();
        }
    };

    /* loaded from: classes2.dex */
    public static abstract class StartCameraPreviewCallback {
        public abstract void onAuthenticationTimeout();

        public abstract void onCameraPreviewLaunchError();

        public abstract void onCameraPreviewLaunchSucceed(boolean z);

        public abstract void onCameraPreviewLaunching();

        public abstract void onFaceCompare(boolean z);

        public abstract void onFaceCompareFailShake();

        public abstract void onFaceLoading();

        public abstract void onFaceRecognitionError(int i);

        public abstract void onLockoutReset();

        public abstract void onScreenBrightnessOverride(int i, float f);
    }

    static /* synthetic */ int access$408(KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl) {
        int i = keyguardFaceRecognitionImpl.mRecongnizeFailTime;
        keyguardFaceRecognitionImpl.mRecongnizeFailTime = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetFaceServiceState() {
        this.mFaceServiceDead = true;
        this.mCreateTimes = 0;
        createNtFaceRecognitionManager(null);
    }

    public static KeyguardFaceRecognitionImpl getInstance(Context context, int i, Handler handler) {
        if (mFaceRecognitionImpl == null) {
            mFaceRecognitionImpl = new KeyguardFaceRecognitionImpl(context, i, handler);
        }
        return mFaceRecognitionImpl;
    }

    public KeyguardFaceRecognitionImpl(Context context, int i, Handler handler) {
        this.mUserId = UserHandle.myUserId();
        this.mContext = context.getApplicationContext();
        this.mUserId = i;
        this.mHandler = handler;
        createNtFaceRecognitionManager(null);
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).registerCallback(this.mUserSwitch);
        Log.d("KeyguardFaceRecognitionImpl", "KeyguardFaceRecognitionImpl: onCreate! ");
    }

    public void setStartCameraPreviewCallback(StartCameraPreviewCallback startCameraPreviewCallback) {
        Log.d("KeyguardFaceRecognitionImpl", "KeyguardFaceRecognitionImpl: setStartCameraPreviewCallback! ");
        this.mStartCameraPreviewCallback = startCameraPreviewCallback;
    }

    public void warmUpHardwareDevice() {
        Log.d("KeyguardFaceRecognitionImpl", "KeyguardFaceRecognitionImpl: warmUpHardwareDevice! mFaceServiceDead = " + this.mFaceServiceDead);
        StartCameraPreviewCallback startCameraPreviewCallback = this.mStartCameraPreviewCallback;
        if (startCameraPreviewCallback != null) {
            startCameraPreviewCallback.onCameraPreviewLaunching();
        }
        this.mCompareCD = false;
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() { // from class: com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.7
                @Override // java.lang.Runnable
                public void run() {
                    KeyguardFaceRecognitionImpl.this.mNtFaceRecognitionManager.warmUpHardwareDevice(KeyguardFaceRecognitionImpl.this.mWarmUpHardwareDeviceCallback);
                }
            });
        } else if (this.mNtFaceRecognitionManager == null) {
        } else {
            Log.d("KeyguardFaceRecognitionImpl", "warmUpHardwareDevice: mNtFaceRecognitionManager =" + this.mNtFaceRecognitionManager);
            this.mNtFaceRecognitionManager.warmUpHardwareDevice(this.mWarmUpHardwareDeviceCallback);
        }
    }

    public void startListeningForFaceRecognition() {
        final int currentUser = ActivityManager.getCurrentUser();
        Log.d("KeyguardFaceRecognitionImpl", "KeyguardFaceRecognitionImpl: startListeningForFaceRecognition! userId = " + currentUser);
        CancellationSignal cancellationSignal = this.mCancellationSignal;
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
        }
        CancellationSignal cancellationSignal2 = new CancellationSignal();
        this.mCancellationSignal = cancellationSignal2;
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() { // from class: com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.8
                @Override // java.lang.Runnable
                public void run() {
                    KeyguardFaceRecognitionImpl.this.mNtFaceRecognitionManager.authenticate((NtFaceRecognitionManager.CryptoObject) null, KeyguardFaceRecognitionImpl.this.mCancellationSignal, 0, KeyguardFaceRecognitionImpl.this.mAuthenticationCallback, KeyguardFaceRecognitionImpl.this.mHandler, currentUser);
                }
            });
        } else {
            NtFaceRecognitionManager ntFaceRecognitionManager = this.mNtFaceRecognitionManager;
            if (ntFaceRecognitionManager != null) {
                ntFaceRecognitionManager.authenticate((NtFaceRecognitionManager.CryptoObject) null, cancellationSignal2, 0, this.mAuthenticationCallback, this.mHandler, currentUser);
            }
        }
        this.mCompareCD = false;
    }

    public void stopListeningForFaceRecognition() {
        Log.d("KeyguardFaceRecognitionImpl", "KeyguardFaceRecognitionImpl: stopListeningForFaceRecognition! ");
        CancellationSignal cancellationSignal = this.mCancellationSignal;
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            this.mCancellationSignal = null;
        }
        this.mCompareCD = false;
    }

    public void closeHardwareDevice() {
        Log.d("KeyguardFaceRecognitionImpl", "KeyguardFaceRecognitionImpl: closeHardwareDevice! ");
        this.mCompareCD = false;
        this.mAuthTimeOut = false;
        this.mHandler.removeCallbacks(this.mFaceRecognitionTimeOut);
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() { // from class: com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.9
                @Override // java.lang.Runnable
                public void run() {
                    KeyguardFaceRecognitionImpl.this.mNtFaceRecognitionManager.closeHardwareDevice();
                }
            });
            return;
        }
        NtFaceRecognitionManager ntFaceRecognitionManager = this.mNtFaceRecognitionManager;
        if (ntFaceRecognitionManager == null) {
            return;
        }
        ntFaceRecognitionManager.closeHardwareDevice();
    }

    public void resetAuthUnlockAttempt() {
        Log.d("KeyguardFaceRecognitionImpl", "KeyguardFaceRecognitionImpl: resetAuthUnlockAttempt! ");
        NtFaceRecognitionManager ntFaceRecognitionManager = this.mNtFaceRecognitionManager;
        if (ntFaceRecognitionManager != null) {
            if (this.mFaceServiceDead) {
                createNtFaceRecognitionManager(new Runnable() { // from class: com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.10
                    @Override // java.lang.Runnable
                    public void run() {
                        KeyguardFaceRecognitionImpl.this.mNtFaceRecognitionManager.resetTimeout(r2);
                    }
                });
            } else if (ntFaceRecognitionManager == null) {
            } else {
                ntFaceRecognitionManager.resetTimeout((byte[]) null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createNtFaceRecognitionManager(final Runnable runnable) {
        this.mCreateTimes++;
        String str = "face_recognition_server" + KeyguardUpdateMonitor.getCurrentUser();
        Log.d("KeyguardFaceRecognitionImpl", "createNtFaceRecognitionManager: faceservice = " + str + "activityManager uid = " + ActivityManager.getCurrentUser());
        IBinder service = ServiceManager.getService(str);
        if (service != null) {
            try {
                this.mCreateTimes = 0;
                service.linkToDeath(this.mDeathRecipient, 0);
                this.mService = IFaceRecognitionManagerService.Stub.asInterface(service);
                Log.d("KeyguardFaceRecognitionImpl", "mService " + this.mService);
                this.mNtFaceRecognitionManager = new NtFaceRecognitionManager(this.mContext, this.mService);
                Log.d("KeyguardFaceRecognitionImpl", "mNtFaceRecognitionManager " + this.mNtFaceRecognitionManager);
                if (this.mService != null) {
                    this.mNtFaceRecognitionManager.addLockoutResetCallback(this.mLockoutResetCallback);
                }
                if (this.mFaceServiceDead) {
                    this.mFaceServiceDead = false;
                    FaceRecognitionController faceRecognitionController = (FaceRecognitionController) Dependency.get(FaceRecognitionController.class);
                    if (faceRecognitionController != null && faceRecognitionController.isCameraPreviewStarting()) {
                        faceRecognitionController.postToStopCamera();
                        faceRecognitionController.postToStartCamera();
                    }
                }
                if (runnable == null) {
                    return;
                }
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (this.mCreateTimes > 10) {
        } else {
            Log.d("KeyguardFaceRecognitionImpl", str + " is null " + this.mCreateTimes);
            this.mHandler.postDelayed(new Runnable() { // from class: com.nothingos.systemui.facerecognition.KeyguardFaceRecognitionImpl.11
                @Override // java.lang.Runnable
                public void run() {
                    KeyguardFaceRecognitionImpl.this.createNtFaceRecognitionManager(runnable);
                }
            }, 150L);
        }
    }
}
