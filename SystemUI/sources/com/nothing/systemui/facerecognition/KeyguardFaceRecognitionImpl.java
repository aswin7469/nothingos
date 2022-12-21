package com.nothing.systemui.facerecognition;

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
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.util.SystemUIEventUtils;
import com.nothing.utils.NTVibrateUtils;
import com.p025nt.facerecognition.manager.CameraPreviewProperty;
import com.p025nt.facerecognition.manager.IFaceRecognitionManagerService;
import com.p025nt.facerecognition.manager.NtFaceRecognitionManager;

public class KeyguardFaceRecognitionImpl {
    private static final int FACERECOGNITION_ENROLL_MAX_COUNTS = 5;
    private static final int MAX_CREATE_TIMES = 10;
    private static final int NT_FACE_TIMEOUT = 5000;
    private static final String TAG = "KeyguardFaceRecognitionImpl";
    private static KeyguardFaceRecognitionImpl mFaceRecognitionImpl;
    /* access modifiers changed from: private */
    public Runnable compareCDRunnable;
    /* access modifiers changed from: private */
    public boolean mAuthTimeOut;
    /* access modifiers changed from: private */
    public NtFaceRecognitionManager.AuthenticationCallback mAuthenticationCallback;
    /* access modifiers changed from: private */
    public CancellationSignal mCancellationSignal;
    /* access modifiers changed from: private */
    public boolean mCompareCD;
    private int mCompareTimes;
    /* access modifiers changed from: private */
    public Context mContext;
    private int mCreateTimes;
    private IBinder.DeathRecipient mDeathRecipient;
    /* access modifiers changed from: private */
    public Runnable mFaceRecognitionTimeOut;
    private boolean mFaceServiceDead;
    protected Handler mHandler;
    private final NtFaceRecognitionManager.LockoutResetCallback mLockoutResetCallback;
    /* access modifiers changed from: private */
    public NtFaceRecognitionManager mNtFaceRecognitionManager;
    /* access modifiers changed from: private */
    public NTVibrateUtils mNtVibrateUtils;
    /* access modifiers changed from: private */
    public int mRecongnizeFailTime;
    private IFaceRecognitionManagerService mService;
    /* access modifiers changed from: private */
    public StartCameraPreviewCallback mStartCameraPreviewCallback;
    private int mUserId;
    private KeyguardUpdateMonitorCallback mUserSwitch;
    /* access modifiers changed from: private */
    public NtFaceRecognitionManager.WarmUpHardwareDeviceCallback mWarmUpHardwareDeviceCallback;

    public static abstract class StartCameraPreviewCallback {
        public void onAuthenticationTimeout() {
        }

        public void onCameraPreviewLaunchError() {
        }

        public void onCameraPreviewLaunchSucceed(boolean z) {
        }

        public void onCameraPreviewLaunching() {
        }

        public void onCameraPreviewTimeout() {
        }

        public void onFaceCompare(boolean z) {
        }

        public void onFaceCompareFailShake() {
        }

        public void onFaceLoading() {
        }

        public void onFaceRecognitionError(int i) {
        }

        public void onLockoutReset() {
        }

        public void onScreenBrightnessOverride(int i, float f) {
        }
    }

    static /* synthetic */ int access$408(KeyguardFaceRecognitionImpl keyguardFaceRecognitionImpl) {
        int i = keyguardFaceRecognitionImpl.mRecongnizeFailTime;
        keyguardFaceRecognitionImpl.mRecongnizeFailTime = i + 1;
        return i;
    }

    /* access modifiers changed from: private */
    public void resetFaceServiceState() {
        this.mFaceServiceDead = true;
        this.mCreateTimes = 0;
        createNtFaceRecognitionManager((Runnable) null);
    }

    public static KeyguardFaceRecognitionImpl getInstance(Context context, int i, Handler handler) {
        if (mFaceRecognitionImpl == null) {
            mFaceRecognitionImpl = new KeyguardFaceRecognitionImpl(context, i, handler);
        }
        return mFaceRecognitionImpl;
    }

    public KeyguardFaceRecognitionImpl(Context context, Handler handler) {
        this(context, UserHandle.myUserId(), handler);
    }

    public KeyguardFaceRecognitionImpl(Context context, int i, Handler handler) {
        this.mUserId = UserHandle.myUserId();
        this.mRecongnizeFailTime = 0;
        this.mCompareTimes = 0;
        this.mCompareCD = false;
        this.mCreateTimes = 0;
        this.mFaceServiceDead = true;
        this.mAuthTimeOut = false;
        this.compareCDRunnable = new Runnable() {
            public void run() {
                boolean unused = KeyguardFaceRecognitionImpl.this.mCompareCD = false;
            }
        };
        this.mFaceRecognitionTimeOut = new Runnable() {
            public void run() {
                if (KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback != null) {
                    boolean unused = KeyguardFaceRecognitionImpl.this.mAuthTimeOut = true;
                    KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onAuthenticationTimeout();
                }
            }
        };
        this.mUserSwitch = new KeyguardUpdateMonitorCallback() {
            public void onUserSwitchComplete(int i) {
                Log.d(KeyguardFaceRecognitionImpl.TAG, "onUserSwitchComplete");
                KeyguardFaceRecognitionImpl.this.resetFaceServiceState();
            }
        };
        this.mAuthenticationCallback = new NtFaceRecognitionManager.AuthenticationCallback() {
            public void onAuthenticationTimeout() {
            }

            public void onAuthenticationFailed(int i) {
                Log.i(KeyguardFaceRecognitionImpl.TAG, "onAuthenticationFailed mRecongnizeFailTime:" + KeyguardFaceRecognitionImpl.this.mRecongnizeFailTime + ", errMsgId:" + i);
                if (!KeyguardFaceRecognitionImpl.this.mCompareCD) {
                    KeyguardFaceRecognitionImpl.this.mHandler.removeCallbacks(KeyguardFaceRecognitionImpl.this.mFaceRecognitionTimeOut);
                    if (((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFaceCameraStarting()) {
                        KeyguardFaceRecognitionImpl.this.mHandler.postDelayed(KeyguardFaceRecognitionImpl.this.mFaceRecognitionTimeOut, 5000);
                    }
                    if (i == 101 || i == 120) {
                        KeyguardFaceRecognitionImpl.access$408(KeyguardFaceRecognitionImpl.this);
                        boolean unused = KeyguardFaceRecognitionImpl.this.mCompareCD = true;
                        Log.d(KeyguardFaceRecognitionImpl.TAG, "compare failed " + KeyguardFaceRecognitionImpl.this.mRecongnizeFailTime);
                        if (KeyguardFaceRecognitionImpl.this.mRecongnizeFailTime % 5 == 0) {
                            KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onFaceCompareFailShake();
                            KeyguardFaceRecognitionImpl.this.mHandler.postDelayed(KeyguardFaceRecognitionImpl.this.compareCDRunnable, 500);
                            return;
                        }
                        KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onFaceLoading();
                        KeyguardFaceRecognitionImpl.this.mHandler.postDelayed(KeyguardFaceRecognitionImpl.this.compareCDRunnable, 300);
                    }
                }
            }

            public void onAuthenticationSucceeded(NtFaceRecognitionManager.AuthenticationResult authenticationResult) {
                Log.i(KeyguardFaceRecognitionImpl.TAG, "onAuthenticationSucceeded");
                KeyguardFaceRecognitionImpl.this.mNtVibrateUtils.playVerityVibrate(NTVibrateUtils.VibrateType.FINGERPRINT_VERIFICATION_SUCCEEDED);
                int unused = KeyguardFaceRecognitionImpl.this.mRecongnizeFailTime = 0;
                KeyguardFaceRecognitionImpl.this.mHandler.removeCallbacks(KeyguardFaceRecognitionImpl.this.mFaceRecognitionTimeOut);
                if (KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback != null) {
                    boolean unused2 = KeyguardFaceRecognitionImpl.this.mCompareCD = true;
                    KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onFaceCompare(true);
                    SystemUIEventUtils.collectUnLockResults(KeyguardFaceRecognitionImpl.this.mContext, SystemUIEventUtils.EVENT_PROPERTY_KEY_UNLOCK_SUCCESS, 5);
                }
            }

            public void onAuthenticationHelp(int i, CharSequence charSequence) {
                KeyguardFaceRecognitionImpl.this.mHandler.removeCallbacks(KeyguardFaceRecognitionImpl.this.mFaceRecognitionTimeOut);
                if (((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFaceCameraStarting()) {
                    KeyguardFaceRecognitionImpl.this.mHandler.postDelayed(KeyguardFaceRecognitionImpl.this.mFaceRecognitionTimeOut, 5000);
                }
            }

            public void onAuthenticationError(int i, CharSequence charSequence) {
                Log.i(KeyguardFaceRecognitionImpl.TAG, "onAuthenticationError msgId: " + i + " errString: " + charSequence);
                SystemUIEventUtils.collectUnLockResults(KeyguardFaceRecognitionImpl.this.mContext, SystemUIEventUtils.EVENT_PROPERTY_KEY_UNLOCK_FAIL, 5);
                KeyguardFaceRecognitionImpl.this.mHandler.removeCallbacks(KeyguardFaceRecognitionImpl.this.mFaceRecognitionTimeOut);
                if (KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback != null && !KeyguardFaceRecognitionImpl.this.mAuthTimeOut) {
                    KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onFaceRecognitionError(i);
                }
                int unused = KeyguardFaceRecognitionImpl.this.mRecongnizeFailTime = 0;
                boolean unused2 = KeyguardFaceRecognitionImpl.this.mCompareCD = true;
                KeyguardFaceRecognitionImpl.this.mHandler.postDelayed(KeyguardFaceRecognitionImpl.this.compareCDRunnable, 300);
            }

            public void onAuthenticationAcquired(int i) {
                KeyguardFaceRecognitionImpl.this.mHandler.removeCallbacks(KeyguardFaceRecognitionImpl.this.mFaceRecognitionTimeOut);
                if (((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).isFaceCameraStarting()) {
                    KeyguardFaceRecognitionImpl.this.mHandler.postDelayed(KeyguardFaceRecognitionImpl.this.mFaceRecognitionTimeOut, 5000);
                }
            }

            public void onScreenBrightnessOverride(int i, float f) {
                Log.d(KeyguardFaceRecognitionImpl.TAG, "onScreenBrightnessOverride, screenbrightnessvalue:" + i + ",ambientLux:" + f);
                KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onScreenBrightnessOverride(i, f);
            }
        };
        this.mLockoutResetCallback = new NtFaceRecognitionManager.LockoutResetCallback() {
            public void onLockoutReset() {
                Log.i(KeyguardFaceRecognitionImpl.TAG, "onLockoutReset");
                KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onLockoutReset();
            }
        };
        this.mWarmUpHardwareDeviceCallback = new NtFaceRecognitionManager.WarmUpHardwareDeviceCallback() {
            public void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) {
                Log.d(KeyguardFaceRecognitionImpl.TAG, "onWarmUpHardwareDeviceResult (" + cameraPreviewProperty.getCameraWidth() + ", " + cameraPreviewProperty.getCameraHeight() + ", " + cameraPreviewProperty.getCameraIsOpen() + NavigationBarInflaterView.KEY_CODE_END);
                if (KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback == null) {
                    return;
                }
                if (cameraPreviewProperty.getCameraIsOpen()) {
                    KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onCameraPreviewLaunchSucceed(true);
                    boolean unused = KeyguardFaceRecognitionImpl.this.mAuthTimeOut = false;
                    KeyguardFaceRecognitionImpl.this.mHandler.postDelayed(KeyguardFaceRecognitionImpl.this.mFaceRecognitionTimeOut, 5000);
                    return;
                }
                KeyguardFaceRecognitionImpl.this.mStartCameraPreviewCallback.onCameraPreviewLaunchError();
            }
        };
        this.mDeathRecipient = new IBinder.DeathRecipient() {
            public void binderDied() {
                Log.d(KeyguardFaceRecognitionImpl.TAG, "KeyguardViewMediator binderDied");
                KeyguardFaceRecognitionImpl.this.resetFaceServiceState();
            }
        };
        this.mContext = context.getApplicationContext();
        this.mUserId = i;
        this.mHandler = handler;
        createNtFaceRecognitionManager((Runnable) null);
        ((KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class)).registerCallback(this.mUserSwitch);
        this.mNtVibrateUtils = NTVibrateUtils.getInstance(context);
        Log.d(TAG, "KeyguardFaceRecognitionImpl: onCreate! ");
    }

    public void setStartCameraPreviewCallback(StartCameraPreviewCallback startCameraPreviewCallback) {
        Log.d(TAG, "KeyguardFaceRecognitionImpl: setStartCameraPreviewCallback! ");
        this.mStartCameraPreviewCallback = startCameraPreviewCallback;
    }

    public void warmUpHardwareDevice() {
        Log.d(TAG, "KeyguardFaceRecognitionImpl: warmUpHardwareDevice! mFaceServiceDead = " + this.mFaceServiceDead);
        StartCameraPreviewCallback startCameraPreviewCallback = this.mStartCameraPreviewCallback;
        if (startCameraPreviewCallback != null) {
            startCameraPreviewCallback.onCameraPreviewLaunching();
        }
        this.mCompareCD = false;
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() {
                public void run() {
                    KeyguardFaceRecognitionImpl.this.mNtFaceRecognitionManager.warmUpHardwareDevice(KeyguardFaceRecognitionImpl.this.mWarmUpHardwareDeviceCallback);
                }
            });
        } else if (this.mNtFaceRecognitionManager != null) {
            Log.d(TAG, "warmUpHardwareDevice: mNtFaceRecognitionManager =" + this.mNtFaceRecognitionManager);
            this.mNtFaceRecognitionManager.warmUpHardwareDevice(this.mWarmUpHardwareDeviceCallback);
        }
    }

    public void startListeningForFaceRecognition() {
        final int currentUser = ActivityManager.getCurrentUser();
        Log.d(TAG, "KeyguardFaceRecognitionImpl: startListeningForFaceRecognition! userId = " + currentUser);
        CancellationSignal cancellationSignal = this.mCancellationSignal;
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
        }
        CancellationSignal cancellationSignal2 = new CancellationSignal();
        this.mCancellationSignal = cancellationSignal2;
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() {
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
        Log.d(TAG, "KeyguardFaceRecognitionImpl: stopListeningForFaceRecognition! ");
        CancellationSignal cancellationSignal = this.mCancellationSignal;
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            this.mCancellationSignal = null;
        }
        this.mCompareCD = false;
    }

    public void closeHardwareDevice() {
        Log.d(TAG, "KeyguardFaceRecognitionImpl: closeHardwareDevice! ");
        this.mCompareCD = false;
        this.mAuthTimeOut = false;
        this.mHandler.removeCallbacks(this.mFaceRecognitionTimeOut);
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() {
                public void run() {
                    KeyguardFaceRecognitionImpl.this.mNtFaceRecognitionManager.closeHardwareDevice();
                }
            });
            return;
        }
        NtFaceRecognitionManager ntFaceRecognitionManager = this.mNtFaceRecognitionManager;
        if (ntFaceRecognitionManager != null) {
            ntFaceRecognitionManager.closeHardwareDevice();
        }
    }

    public void resetAuthUnlockAttempt() {
        Log.d(TAG, "KeyguardFaceRecognitionImpl: resetAuthUnlockAttempt! ");
        NtFaceRecognitionManager ntFaceRecognitionManager = this.mNtFaceRecognitionManager;
        if (ntFaceRecognitionManager == null) {
            return;
        }
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable((byte[]) null) {
                public void run() {
                    KeyguardFaceRecognitionImpl.this.mNtFaceRecognitionManager.resetTimeout(null);
                }
            });
        } else if (ntFaceRecognitionManager != null) {
            ntFaceRecognitionManager.resetTimeout((byte[]) null);
        }
    }

    /* access modifiers changed from: private */
    public void createNtFaceRecognitionManager(final Runnable runnable) {
        this.mCreateTimes++;
        String str = "face_recognition_server" + KeyguardUpdateMonitor.getCurrentUser();
        Log.d(TAG, "createNtFaceRecognitionManager: faceservice = " + str + "activityManager uid = " + ActivityManager.getCurrentUser());
        IBinder service = ServiceManager.getService(str);
        if (service != null) {
            try {
                this.mCreateTimes = 0;
                service.linkToDeath(this.mDeathRecipient, 0);
                this.mService = IFaceRecognitionManagerService.Stub.asInterface(service);
                Log.d(TAG, "mService " + this.mService);
                this.mNtFaceRecognitionManager = new NtFaceRecognitionManager(this.mContext, this.mService);
                Log.d(TAG, "mNtFaceRecognitionManager " + this.mNtFaceRecognitionManager);
                if (this.mService != null) {
                    this.mNtFaceRecognitionManager.addLockoutResetCallback(this.mLockoutResetCallback);
                }
                if (this.mFaceServiceDead) {
                    this.mFaceServiceDead = false;
                    FaceRecognitionController faceRecognitionController = (FaceRecognitionController) NTDependencyEx.get(FaceRecognitionController.class);
                    if (faceRecognitionController != null && faceRecognitionController.isCameraPreviewStarting()) {
                        faceRecognitionController.postToStopCamera();
                        faceRecognitionController.postToStartCamera();
                    }
                }
                if (runnable != null) {
                    runnable.run();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (this.mCreateTimes <= 10) {
            Log.d(TAG, str + " is null " + this.mCreateTimes);
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    KeyguardFaceRecognitionImpl.this.createNtFaceRecognitionManager(runnable);
                }
            }, 150);
        }
    }
}
