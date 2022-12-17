package com.nothing.settings.face;

import android.content.Context;
import android.graphics.Rect;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import com.p007nt.facerecognition.manager.CameraPreviewProperty;
import com.p007nt.facerecognition.manager.FaceMetadata;
import com.p007nt.facerecognition.manager.IFaceRecognitionManagerService;
import com.p007nt.facerecognition.manager.NtFaceRecognitionManager;
import java.util.ArrayList;
import java.util.List;

public class FaceEnrollImpl implements IFaceEnroll {
    private NtFaceRecognitionManager.AuthenticationCallback mAuthenticationCallback;
    /* access modifiers changed from: private */
    public CancellationSignal mCancellationSignal;
    private Context mContext;
    /* access modifiers changed from: private */
    public int mCreateTimes;
    private IBinder.DeathRecipient mDeathRecipient;
    /* access modifiers changed from: private */
    public boolean mDone;
    private boolean mEnrolling;
    /* access modifiers changed from: private */
    public NtFaceRecognitionManager.EnrollmentCallback mEnrollmentCallback;
    private List<FaceMetadata> mFaceMetadatas;
    /* access modifiers changed from: private */
    public int mFaceRecognitionFailedCounts;
    /* access modifiers changed from: private */
    public int mFaceRecognitionTotalFailedCounts;
    /* access modifiers changed from: private */
    public boolean mFaceServiceDead;
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    public CharSequence mNoticeString;
    /* access modifiers changed from: private */
    public NtFaceRecognitionManager mNtFaceRecognitionManager;
    private PowerManager mPowerManager;
    /* access modifiers changed from: private */
    public NtFaceRecognitionManager.RemovalCallback mRemovalCallback;
    private IFaceRecognitionManagerService mService;
    /* access modifiers changed from: private */
    public int mTargetRecognitionFaiedCount;
    /* access modifiers changed from: private */
    public int mUserId;
    private Vibrator mVibrator;
    /* access modifiers changed from: private */
    public NtFaceRecognitionManager.WarmUpHardwareDeviceCallback mWarmUpHardwareDeviceCallback;
    /* access modifiers changed from: private */
    public int onFaceSaveErrorNum;

    public void vibrate() {
        this.mVibrator.vibrate(new long[]{100, 30, 300}, -1);
    }

    public void createNtFaceRecognitionManager(final Runnable runnable) {
        this.mCreateTimes++;
        IBinder service = ServiceManager.getService("face_recognition_server" + UserHandle.myUserId());
        if (service != null) {
            try {
                this.mCreateTimes = 0;
                service.linkToDeath(this.mDeathRecipient, 0);
                this.mService = IFaceRecognitionManagerService.Stub.asInterface(service);
                Log.d("Face", "FaceEnrollImpl::mService " + this.mService);
                this.mNtFaceRecognitionManager = new NtFaceRecognitionManager(this.mContext, this.mService);
                if (this.mFaceServiceDead) {
                    this.mFaceServiceDead = false;
                }
                if (runnable != null) {
                    runnable.run();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (this.mHandler != null && this.mCreateTimes <= 10) {
            Log.d("Face", "FaceEnrollImpl face_recognition_server " + UserHandle.myUserId() + " is null " + this.mCreateTimes);
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    FaceEnrollImpl.this.createNtFaceRecognitionManager(runnable);
                }
            }, 120);
        }
    }

    public FaceEnrollImpl(Context context, Handler handler) {
        this(context, UserHandle.myUserId(), handler);
    }

    public FaceEnrollImpl(Context context) {
        this(context, UserHandle.myUserId(), (Handler) null);
    }

    public FaceEnrollImpl(Context context, int i, Handler handler) {
        this.mFaceRecognitionTotalFailedCounts = 0;
        this.mFaceRecognitionFailedCounts = 0;
        this.mUserId = UserHandle.myUserId();
        this.onFaceSaveErrorNum = 0;
        this.mNoticeString = "";
        this.mCreateTimes = 0;
        this.mFaceServiceDead = true;
        this.mWarmUpHardwareDeviceCallback = new NtFaceRecognitionManager.WarmUpHardwareDeviceCallback() {
            public void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) {
                if (FaceEnrollImpl.this.mHandler != null && cameraPreviewProperty != null) {
                    Log.d("Face", "onWarmUpHardwareDeviceResult (" + cameraPreviewProperty.getCameraWidth() + ", " + cameraPreviewProperty.getCameraHeight() + ", " + cameraPreviewProperty.getCameraIsOpen() + ")");
                    Message message = new Message();
                    message.what = 10;
                    message.arg1 = cameraPreviewProperty.getCameraIsOpen() ? 1 : 0;
                    message.obj = cameraPreviewProperty;
                    FaceEnrollImpl.this.mHandler.sendMessage(message);
                }
            }
        };
        this.mEnrollmentCallback = new NtFaceRecognitionManager.EnrollmentCallback() {
            public void onEnrollmentError(int i, CharSequence charSequence) {
                Log.d("Face", "onEnrollmentError--> errMsgId:" + i + ",errString:" + charSequence);
                if (FaceEnrollImpl.this.mHandler != null) {
                    FaceEnrollImpl.this.mHandler.obtainMessage(4, charSequence).sendToTarget();
                }
            }

            public void onEnrollmentHelp(int i, CharSequence charSequence) {
                if (!TextUtils.isEmpty(charSequence) && !charSequence.equals(FaceEnrollImpl.this.mNoticeString)) {
                    FaceEnrollImpl faceEnrollImpl = FaceEnrollImpl.this;
                    faceEnrollImpl.onFaceSaveErrorNum = faceEnrollImpl.onFaceSaveErrorNum + 1;
                    if (FaceEnrollImpl.this.onFaceSaveErrorNum >= 50) {
                        FaceEnrollImpl.this.onFaceSaveErrorNum = 0;
                        FaceEnrollImpl.this.mNoticeString = charSequence;
                        if (FaceEnrollImpl.this.mHandler != null) {
                            FaceEnrollImpl.this.mHandler.obtainMessage(4, charSequence).sendToTarget();
                        }
                    }
                }
                Log.d("Face", "onEnrollmentHelp--> helpMsgId:" + i + ",helpString:" + charSequence + ", onFaceSaveErrorNum:" + FaceEnrollImpl.this.onFaceSaveErrorNum + ",mHandler:" + FaceEnrollImpl.this.mHandler);
            }

            public void onEnrollmentProgress(int i) {
                FaceEnrollImpl.this.mDone = false;
                FaceEnrollImpl.this.onFaceSaveErrorNum = 0;
                if (FaceEnrollImpl.this.mHandler != null) {
                    int i2 = (5 - i) * 20;
                    if (i == 0) {
                        FaceEnrollImpl.this.mDone = true;
                    }
                    if (FaceEnrollImpl.this.mDone) {
                        FaceEnrollImpl.this.updateSaveFeatureProgress(i2, "");
                        if (FaceEnrollImpl.this.mHandler != null) {
                            FaceEnrollImpl.this.mHandler.sendEmptyMessageDelayed(13, 500);
                        }
                    }
                    Log.d("Face", "onEnrollmentProgress! remaining:" + i + ",mHandler:" + FaceEnrollImpl.this.mHandler);
                }
            }
        };
        this.mRemovalCallback = new NtFaceRecognitionManager.RemovalCallback() {
            public void onRemovalSucceeded(FaceMetadata faceMetadata, int i) {
                if (faceMetadata != null) {
                    Log.d("Face", "onRemovalSucceeded:" + faceMetadata.getName() + ", id:" + faceMetadata.getFaceId() + ",remaining:" + i);
                }
            }

            public void onRemovalError(FaceMetadata faceMetadata, int i, CharSequence charSequence) {
                if (faceMetadata != null) {
                    Log.d("Face", "onRemovalError:" + faceMetadata.getName() + ", id:" + faceMetadata.getFaceId() + ",errorMsgId:" + i + ", errString:" + charSequence);
                }
            }
        };
        this.mAuthenticationCallback = new NtFaceRecognitionManager.AuthenticationCallback() {
            public void onAuthenticationError(int i, CharSequence charSequence) {
                handleAuthenticationError(i);
                Log.d("Face", "onAuthenticationError, errorCode:" + i + ", errString:" + charSequence);
            }

            public void onAuthenticationHelp(int i, CharSequence charSequence) {
                Log.d("Face", "onAuthenticationHelp, helpCode:" + i + ", helpString:" + charSequence);
            }

            public void onAuthenticationSucceeded(NtFaceRecognitionManager.AuthenticationResult authenticationResult) {
                Log.d("Face", "onAuthenticationSucceeded.");
                if (FaceEnrollImpl.this.mHandler != null) {
                    FaceEnrollImpl.this.mHandler.sendEmptyMessage(8002);
                }
            }

            public void onAuthenticationFailed(int i) {
                handleAuthenticationError(i);
            }

            private void handleAuthenticationError(int i) {
                Log.d("Face", "onAuthenticationFailed. errorCode:" + i + ", mFaceRecognitionFailedCounts:" + FaceEnrollImpl.this.mFaceRecognitionFailedCounts + ", mTargetRecognitionFaiedCount:" + FaceEnrollImpl.this.mTargetRecognitionFaiedCount + ", mFaceRecognitionTotalFailedCounts:" + FaceEnrollImpl.this.mFaceRecognitionTotalFailedCounts);
                if (i == 9) {
                    FaceEnrollImpl.this.vibrate();
                    if (FaceEnrollImpl.this.mHandler != null) {
                        FaceEnrollImpl.this.mHandler.sendEmptyMessage(8005);
                        return;
                    }
                    return;
                }
                FaceEnrollImpl faceEnrollImpl = FaceEnrollImpl.this;
                faceEnrollImpl.mFaceRecognitionTotalFailedCounts = faceEnrollImpl.mFaceRecognitionTotalFailedCounts + 1;
                if (FaceEnrollImpl.this.mFaceRecognitionTotalFailedCounts % 5 == 0) {
                    FaceEnrollImpl faceEnrollImpl2 = FaceEnrollImpl.this;
                    faceEnrollImpl2.mFaceRecognitionFailedCounts = faceEnrollImpl2.mFaceRecognitionFailedCounts + 1;
                    FaceEnrollImpl.this.vibrate();
                    if (FaceEnrollImpl.this.mHandler != null) {
                        FaceEnrollImpl.this.mHandler.sendEmptyMessage(8001);
                    }
                    if (FaceEnrollImpl.this.mHandler != null) {
                        FaceEnrollImpl.this.mHandler.sendEmptyMessage(8004);
                    }
                }
                if (FaceEnrollImpl.this.mFaceRecognitionFailedCounts >= FaceEnrollImpl.this.mTargetRecognitionFaiedCount && FaceEnrollImpl.this.mHandler != null) {
                    FaceEnrollImpl.this.mHandler.sendEmptyMessage(8005);
                }
            }

            public void onAuthenticationAcquired(int i) {
                Log.d("Face", "onAuthenticationAcquired, acquireInfo:" + i);
                super.onAuthenticationAcquired(i);
            }

            public void onScreenBrightnessOverride(int i, float f) {
                Log.d("Face", "onScreenBrightnessOverride, screenBrightnessValue:" + i + ",ambientLux:" + f + ",mHandler:" + FaceEnrollImpl.this.mHandler);
                if (FaceEnrollImpl.this.mHandler != null) {
                    FaceEnrollImpl.this.mHandler.removeMessages(8006);
                    Message message = new Message();
                    message.what = 8006;
                    message.arg1 = i;
                    Log.d("Face", "onScreenBrightnessOverride sendMessage");
                    FaceEnrollImpl.this.mHandler.sendMessage(message);
                }
            }

            public void onAuthenticationTimeout() {
                if (FaceEnrollImpl.this.mHandler != null) {
                    FaceEnrollImpl.this.mHandler.sendEmptyMessage(8007);
                }
            }
        };
        this.mDeathRecipient = new IBinder.DeathRecipient() {
            public void binderDied() {
                Log.d("Face", "FaceEnrollImpl:: KeyguardViewMediator binderDied");
                FaceEnrollImpl.this.mFaceServiceDead = true;
                FaceEnrollImpl.this.mCreateTimes = 0;
                FaceEnrollImpl.this.createNtFaceRecognitionManager((Runnable) null);
            }
        };
        this.mContext = context.getApplicationContext();
        this.mUserId = i;
        this.mHandler = handler;
        this.mVibrator = (Vibrator) context.getSystemService("vibrator");
        createNtFaceRecognitionManager((Runnable) null);
        this.mPowerManager = (PowerManager) this.mContext.getSystemService("power");
        Log.d("Face", "FaceEnrollImpl: onCreate! ");
    }

    public boolean warmUpHardwareDeviceForPreview(final Surface surface, Handler handler, final int i, final int i2) {
        this.mHandler = handler;
        Log.d("Face", "warm up device...... mHandler:" + this.mHandler + ",surface:" + surface);
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() {
                public void run() {
                    FaceEnrollImpl.this.mNtFaceRecognitionManager.warmUpHardwareDeviceForPreview(FaceEnrollImpl.this.mWarmUpHardwareDeviceCallback, surface, i, i2);
                }
            });
            return true;
        }
        this.mNtFaceRecognitionManager.warmUpHardwareDeviceForPreview(this.mWarmUpHardwareDeviceCallback, surface, i, i2);
        return true;
    }

    public void startEnroll(Handler handler, final byte[] bArr, final Rect rect) {
        this.mHandler = handler;
        this.onFaceSaveErrorNum = 0;
        this.mDone = false;
        CancellationSignal cancellationSignal = new CancellationSignal();
        this.mCancellationSignal = cancellationSignal;
        if (this.mFaceServiceDead) {
            Log.d("Face", "FaceEnrollImpl::startEnroll face service dead, create face recognition manager and enroll");
            createNtFaceRecognitionManager(new Runnable() {
                public void run() {
                    FaceEnrollImpl.this.mNtFaceRecognitionManager.enroll(bArr, FaceEnrollImpl.this.mCancellationSignal, 0, FaceEnrollImpl.this.mUserId, FaceEnrollImpl.this.mEnrollmentCallback, rect);
                }
            });
        } else {
            Log.d("Face", "FaceEnrollImpl::startEnroll enroll");
            this.mNtFaceRecognitionManager.enroll(bArr, cancellationSignal, 0, this.mUserId, this.mEnrollmentCallback, rect);
        }
        this.mEnrolling = true;
        Log.d("Face", "FaceEnrollImpl::startEnroll");
    }

    public void endEnroll() {
        cancelEnrollment();
        this.mHandler = null;
        this.onFaceSaveErrorNum = 0;
        Log.d("Face", "FaceEnrollImpl::endEnroll");
    }

    private void cancelEnrollment() {
        if (this.mEnrolling) {
            this.mCancellationSignal.cancel();
            this.mEnrolling = false;
        }
    }

    public boolean closeHardwareDevice() {
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() {
                public void run() {
                    FaceEnrollImpl.this.mNtFaceRecognitionManager.closeHardwareDevice();
                }
            });
            return true;
        }
        this.mNtFaceRecognitionManager.closeHardwareDevice();
        return true;
    }

    public int[] getIds() {
        List<FaceMetadata> enrolledFaceMetadatas = getEnrolledFaceMetadatas(this.mUserId);
        this.mFaceMetadatas = enrolledFaceMetadatas;
        if (enrolledFaceMetadatas == null) {
            return new int[0];
        }
        int size = enrolledFaceMetadatas.size();
        int[] iArr = new int[size];
        for (int i = 0; i < size; i++) {
            iArr[i] = this.mFaceMetadatas.get(i).getFaceId();
            Log.d("Face", "FaceEnrollImpl  face id:" + iArr[i]);
        }
        return iArr;
    }

    public int getFaceMetadatasCount() {
        return getIds().length;
    }

    public void deleteFaceMetadatas() {
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() {
                public void run() {
                    FaceEnrollImpl.this.mNtFaceRecognitionManager.removeAllFaceMetadata(FaceEnrollImpl.this.mUserId, FaceEnrollImpl.this.mRemovalCallback);
                }
            });
        } else {
            this.mNtFaceRecognitionManager.removeAllFaceMetadata(this.mUserId, this.mRemovalCallback);
        }
    }

    public void updateSaveFeatureProgress(int i, CharSequence charSequence) {
        if (this.mHandler != null) {
            Message message = new Message();
            message.what = 7;
            message.arg1 = i;
            message.obj = charSequence;
            Log.d("Face", "FaceEnrollImpl:: updateSaveFeatureProgress msg.arg1:" + i);
            this.mHandler.sendMessage(message);
        }
    }

    public void rename(final int i, final int i2, final String str) {
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() {
                public void run() {
                    FaceEnrollImpl.this.mNtFaceRecognitionManager.rename(i, i2, str);
                }
            });
        } else {
            this.mNtFaceRecognitionManager.rename(i, i2, str);
        }
    }

    public List<FaceMetadata> getEnrolledFaceMetadatas(final int i) {
        final ArrayList arrayList = new ArrayList();
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() {
                public void run() {
                    arrayList.addAll(FaceEnrollImpl.this.mNtFaceRecognitionManager.getEnrolledFaceMetadatas(i));
                }
            });
        } else {
            arrayList.addAll(this.mNtFaceRecognitionManager.getEnrolledFaceMetadatas(i));
        }
        return arrayList;
    }

    public void removeFaceMetadata(final FaceMetadata faceMetadata, final NtFaceRecognitionManager.RemovalCallback removalCallback) {
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() {
                public void run() {
                    FaceEnrollImpl.this.mNtFaceRecognitionManager.remove(faceMetadata, FaceEnrollImpl.this.mUserId, removalCallback);
                }
            });
        } else {
            this.mNtFaceRecognitionManager.remove(faceMetadata, this.mUserId, removalCallback);
        }
    }
}
