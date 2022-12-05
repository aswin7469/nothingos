package com.nt.settings.face;

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
import com.nt.facerecognition.manager.CameraPreviewProperty;
import com.nt.facerecognition.manager.FaceMetadata;
import com.nt.facerecognition.manager.IFaceRecognitionManagerService;
import com.nt.facerecognition.manager.NtFaceRecognitionManager;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class NtFaceRecognitionImpl implements NtIFaceRecognition {
    private NtFaceRecognitionManager.AuthenticationCallback mAuthenticationCallback;
    private CancellationSignal mCancellationSignal;
    private Context mContext;
    private int mCreateTimes;
    private IBinder.DeathRecipient mDeathRecipient;
    private boolean mDone;
    private boolean mEnrolling;
    private NtFaceRecognitionManager.EnrollmentCallback mEnrollmentCallback;
    private List<FaceMetadata> mFaceMetadatas;
    private int mFaceRecognitionFailedCounts;
    private int mFaceRecognitionTotalFailedCounts;
    private boolean mFaceServiceDead;
    private Handler mHandler;
    private CharSequence mNoticeString;
    private NtFaceRecognitionManager mNtFaceRecognitionManager;
    private PowerManager mPowerManager;
    private NtFaceRecognitionManager.RemovalCallback mRemovalCallback;
    private IFaceRecognitionManagerService mService;
    private int mTargetRecognitionFaiedCount;
    private int mUserId;
    private Vibrator mVibrator;
    private NtFaceRecognitionManager.WarmUpHardwareDeviceCallback mWarmUpHardwareDeviceCallback;
    private int onFaceSaveErrorNum;

    static /* synthetic */ int access$208(NtFaceRecognitionImpl ntFaceRecognitionImpl) {
        int i = ntFaceRecognitionImpl.onFaceSaveErrorNum;
        ntFaceRecognitionImpl.onFaceSaveErrorNum = i + 1;
        return i;
    }

    static /* synthetic */ int access$508(NtFaceRecognitionImpl ntFaceRecognitionImpl) {
        int i = ntFaceRecognitionImpl.mFaceRecognitionFailedCounts;
        ntFaceRecognitionImpl.mFaceRecognitionFailedCounts = i + 1;
        return i;
    }

    static /* synthetic */ int access$708(NtFaceRecognitionImpl ntFaceRecognitionImpl) {
        int i = ntFaceRecognitionImpl.mFaceRecognitionTotalFailedCounts;
        ntFaceRecognitionImpl.mFaceRecognitionTotalFailedCounts = i + 1;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void vibrate() {
        this.mVibrator.vibrate(new long[]{100, 30, 300}, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createNtFaceRecognitionManager(final Runnable runnable) {
        this.mCreateTimes++;
        IBinder service = ServiceManager.getService("face_recognition_server" + UserHandle.myUserId());
        if (service != null) {
            try {
                this.mCreateTimes = 0;
                service.linkToDeath(this.mDeathRecipient, 0);
                this.mService = IFaceRecognitionManagerService.Stub.asInterface(service);
                Log.d("NTFaceRecognitionImpl", "mService " + this.mService);
                this.mNtFaceRecognitionManager = new NtFaceRecognitionManager(this.mContext, this.mService);
                if (this.mFaceServiceDead) {
                    this.mFaceServiceDead = false;
                }
                if (runnable == null) {
                    return;
                }
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (this.mHandler == null || this.mCreateTimes > 10) {
        } else {
            Log.d("NTFaceRecognitionImpl", "face_recognition_server " + UserHandle.myUserId() + " is null " + this.mCreateTimes);
            this.mHandler.postDelayed(new Runnable() { // from class: com.nt.settings.face.NtFaceRecognitionImpl.5
                @Override // java.lang.Runnable
                public void run() {
                    NtFaceRecognitionImpl.this.createNtFaceRecognitionManager(runnable);
                }
            }, 120L);
        }
    }

    public NtFaceRecognitionImpl(Context context, Handler handler) {
        this(context, UserHandle.myUserId(), handler);
    }

    public NtFaceRecognitionImpl(Context context) {
        this(context, UserHandle.myUserId(), null);
    }

    public NtFaceRecognitionImpl(Context context, int i, Handler handler) {
        this.mFaceRecognitionTotalFailedCounts = 0;
        this.mFaceRecognitionFailedCounts = 0;
        this.mUserId = UserHandle.myUserId();
        this.onFaceSaveErrorNum = 0;
        this.mNoticeString = "";
        this.mCreateTimes = 0;
        this.mFaceServiceDead = true;
        this.mWarmUpHardwareDeviceCallback = new NtFaceRecognitionManager.WarmUpHardwareDeviceCallback() { // from class: com.nt.settings.face.NtFaceRecognitionImpl.1
            public void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) {
                if (NtFaceRecognitionImpl.this.mHandler == null || cameraPreviewProperty == null) {
                    return;
                }
                Log.d("NTFaceRecognitionImpl", "onWarmUpHardwareDeviceResult (" + cameraPreviewProperty.getCameraWidth() + ", " + cameraPreviewProperty.getCameraHeight() + ", " + cameraPreviewProperty.getCameraIsOpen() + ")");
                Message message = new Message();
                message.what = 10;
                message.arg1 = cameraPreviewProperty.getCameraIsOpen() ? 1 : 0;
                message.obj = cameraPreviewProperty;
                NtFaceRecognitionImpl.this.mHandler.sendMessage(message);
            }
        };
        this.mEnrollmentCallback = new NtFaceRecognitionManager.EnrollmentCallback() { // from class: com.nt.settings.face.NtFaceRecognitionImpl.2
            public void onEnrollmentError(int i2, CharSequence charSequence) {
                Log.d("NTFaceRecognitionImpl", "onEnrollmentError--> errMsgId:" + i2 + ",errString:" + ((Object) charSequence));
                if (NtFaceRecognitionImpl.this.mHandler != null) {
                    NtFaceRecognitionImpl.this.mHandler.obtainMessage(4, charSequence).sendToTarget();
                }
            }

            public void onEnrollmentHelp(int i2, CharSequence charSequence) {
                if (!TextUtils.isEmpty(charSequence) && !charSequence.equals(NtFaceRecognitionImpl.this.mNoticeString)) {
                    NtFaceRecognitionImpl.access$208(NtFaceRecognitionImpl.this);
                    if (NtFaceRecognitionImpl.this.onFaceSaveErrorNum >= 50) {
                        NtFaceRecognitionImpl.this.onFaceSaveErrorNum = 0;
                        NtFaceRecognitionImpl.this.mNoticeString = charSequence;
                        if (NtFaceRecognitionImpl.this.mHandler != null) {
                            NtFaceRecognitionImpl.this.mHandler.obtainMessage(4, charSequence).sendToTarget();
                        }
                    }
                }
                Log.d("NTFaceRecognitionImpl", "onEnrollmentHelp--> helpMsgId:" + i2 + ",helpString:" + ((Object) charSequence) + ", onFaceSaveErrorNum:" + NtFaceRecognitionImpl.this.onFaceSaveErrorNum + ",mHandler:" + NtFaceRecognitionImpl.this.mHandler);
            }

            public void onEnrollmentProgress(int i2) {
                boolean z = false;
                NtFaceRecognitionImpl.this.onFaceSaveErrorNum = 0;
                if (NtFaceRecognitionImpl.this.mHandler == null) {
                    return;
                }
                int i3 = (5 - i2) * 20;
                NtFaceRecognitionImpl ntFaceRecognitionImpl = NtFaceRecognitionImpl.this;
                if (i2 == 0) {
                    z = true;
                }
                ntFaceRecognitionImpl.mDone = z;
                if (NtFaceRecognitionImpl.this.mDone) {
                    NtFaceRecognitionImpl.this.updateSaveFeatureProgress(i3, "");
                    if (NtFaceRecognitionImpl.this.mHandler != null) {
                        NtFaceRecognitionImpl.this.mHandler.sendEmptyMessageDelayed(13, 500L);
                    }
                }
                Log.d("NTFaceRecognitionImpl", "onEnrollmentProgress! remaining:" + i2 + ",mHandler:" + NtFaceRecognitionImpl.this.mHandler);
            }
        };
        this.mRemovalCallback = new NtFaceRecognitionManager.RemovalCallback() { // from class: com.nt.settings.face.NtFaceRecognitionImpl.3
            public void onRemovalSucceeded(FaceMetadata faceMetadata, int i2) {
                if (faceMetadata != null) {
                    Log.d("NTFaceRecognitionImpl", "onRemovalSucceeded:" + ((Object) faceMetadata.getName()) + ", id:" + faceMetadata.getFaceId() + ",remaining:" + i2);
                }
            }

            public void onRemovalError(FaceMetadata faceMetadata, int i2, CharSequence charSequence) {
                if (faceMetadata != null) {
                    Log.d("NTFaceRecognitionImpl", "onRemovalError:" + ((Object) faceMetadata.getName()) + ", id:" + faceMetadata.getFaceId() + ",errorMsgId:" + i2 + ", errString:" + ((Object) charSequence));
                }
            }
        };
        this.mAuthenticationCallback = new NtFaceRecognitionManager.AuthenticationCallback() { // from class: com.nt.settings.face.NtFaceRecognitionImpl.4
            public void onAuthenticationError(int i2, CharSequence charSequence) {
                handleAuthenticationError(i2);
                Log.d("NTFaceRecognitionImpl", "onAuthenticationError, errorCode:" + i2 + ", errString:" + ((Object) charSequence));
            }

            public void onAuthenticationHelp(int i2, CharSequence charSequence) {
                Log.d("NTFaceRecognitionImpl", "onAuthenticationHelp, helpCode:" + i2 + ", helpString:" + ((Object) charSequence));
            }

            public void onAuthenticationSucceeded(NtFaceRecognitionManager.AuthenticationResult authenticationResult) {
                Log.d("NTFaceRecognitionImpl", "onAuthenticationSucceeded.");
                if (NtFaceRecognitionImpl.this.mHandler != null) {
                    NtFaceRecognitionImpl.this.mHandler.sendEmptyMessage(8002);
                }
            }

            public void onAuthenticationFailed(int i2) {
                handleAuthenticationError(i2);
            }

            private void handleAuthenticationError(int i2) {
                Log.d("NTFaceRecognitionImpl", "onAuthenticationFailed. errorCode:" + i2 + ", mFaceRecognitionFailedCounts:" + NtFaceRecognitionImpl.this.mFaceRecognitionFailedCounts + ", mTargetRecognitionFaiedCount:" + NtFaceRecognitionImpl.this.mTargetRecognitionFaiedCount + ", mFaceRecognitionTotalFailedCounts:" + NtFaceRecognitionImpl.this.mFaceRecognitionTotalFailedCounts);
                if (i2 == 9) {
                    NtFaceRecognitionImpl.this.vibrate();
                    if (NtFaceRecognitionImpl.this.mHandler == null) {
                        return;
                    }
                    NtFaceRecognitionImpl.this.mHandler.sendEmptyMessage(8005);
                    return;
                }
                NtFaceRecognitionImpl.access$708(NtFaceRecognitionImpl.this);
                if (NtFaceRecognitionImpl.this.mFaceRecognitionTotalFailedCounts % 5 == 0) {
                    NtFaceRecognitionImpl.access$508(NtFaceRecognitionImpl.this);
                    NtFaceRecognitionImpl.this.vibrate();
                    if (NtFaceRecognitionImpl.this.mHandler != null) {
                        NtFaceRecognitionImpl.this.mHandler.sendEmptyMessage(8001);
                    }
                    if (NtFaceRecognitionImpl.this.mHandler != null) {
                        NtFaceRecognitionImpl.this.mHandler.sendEmptyMessage(8004);
                    }
                }
                if (NtFaceRecognitionImpl.this.mFaceRecognitionFailedCounts < NtFaceRecognitionImpl.this.mTargetRecognitionFaiedCount || NtFaceRecognitionImpl.this.mHandler == null) {
                    return;
                }
                NtFaceRecognitionImpl.this.mHandler.sendEmptyMessage(8005);
            }

            public void onAuthenticationAcquired(int i2) {
                Log.d("NTFaceRecognitionImpl", "onAuthenticationAcquired, acquireInfo:" + i2);
                super.onAuthenticationAcquired(i2);
            }

            public void onScreenBrightnessOverride(int i2, float f) {
                Log.d("NTFaceRecognitionImpl", "onScreenBrightnessOverride, screenbrightnessvalue:" + i2 + ",ambientLux:" + f + ",mHandler:" + NtFaceRecognitionImpl.this.mHandler);
                if (NtFaceRecognitionImpl.this.mHandler != null) {
                    NtFaceRecognitionImpl.this.mHandler.removeMessages(8006);
                    Message message = new Message();
                    message.what = 8006;
                    message.arg1 = i2;
                    Log.d("NTFaceRecognitionImpl", "onScreenBrightnessOverride sendMessage");
                    NtFaceRecognitionImpl.this.mHandler.sendMessage(message);
                }
            }

            public void onAuthenticationTimeout() {
                if (NtFaceRecognitionImpl.this.mHandler != null) {
                    NtFaceRecognitionImpl.this.mHandler.sendEmptyMessage(8007);
                }
            }
        };
        this.mDeathRecipient = new IBinder.DeathRecipient() { // from class: com.nt.settings.face.NtFaceRecognitionImpl.6
            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                Log.d("NTFaceRecognitionImpl", "KeyguardViewMediator binderDied");
                NtFaceRecognitionImpl.this.mFaceServiceDead = true;
                NtFaceRecognitionImpl.this.mCreateTimes = 0;
                NtFaceRecognitionImpl.this.createNtFaceRecognitionManager(null);
            }
        };
        this.mContext = context.getApplicationContext();
        this.mUserId = i;
        this.mHandler = handler;
        this.mVibrator = (Vibrator) context.getSystemService("vibrator");
        createNtFaceRecognitionManager(null);
        this.mPowerManager = (PowerManager) this.mContext.getSystemService("power");
        Log.d("NTFaceRecognitionImpl", "NtFaceRecognitionImpl: onCreate! ");
    }

    @Override // com.nt.settings.face.NtIFaceRecognition
    public boolean warmUpHardwareDeviceForPreview(final Surface surface, Handler handler, final int i, final int i2) {
        this.mHandler = handler;
        Log.d("NTFaceRecognitionImpl", "warm up device...... mHandler:" + this.mHandler + ",surface:" + surface);
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() { // from class: com.nt.settings.face.NtFaceRecognitionImpl.9
                @Override // java.lang.Runnable
                public void run() {
                    NtFaceRecognitionImpl.this.mNtFaceRecognitionManager.warmUpHardwareDeviceForPreview(NtFaceRecognitionImpl.this.mWarmUpHardwareDeviceCallback, surface, i, i2);
                }
            });
            return true;
        }
        this.mNtFaceRecognitionManager.warmUpHardwareDeviceForPreview(this.mWarmUpHardwareDeviceCallback, surface, i, i2);
        return true;
    }

    @Override // com.nt.settings.face.NtIFaceRecognition
    public void startEnroll(Handler handler, final byte[] bArr, final Rect rect) {
        this.mHandler = handler;
        this.onFaceSaveErrorNum = 0;
        this.mDone = false;
        CancellationSignal cancellationSignal = new CancellationSignal();
        this.mCancellationSignal = cancellationSignal;
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() { // from class: com.nt.settings.face.NtFaceRecognitionImpl.10
                @Override // java.lang.Runnable
                public void run() {
                    NtFaceRecognitionImpl.this.mNtFaceRecognitionManager.enroll(bArr, NtFaceRecognitionImpl.this.mCancellationSignal, 0, NtFaceRecognitionImpl.this.mUserId, NtFaceRecognitionImpl.this.mEnrollmentCallback, rect);
                }
            });
        } else {
            this.mNtFaceRecognitionManager.enroll(bArr, cancellationSignal, 0, this.mUserId, this.mEnrollmentCallback, rect);
        }
        this.mEnrolling = true;
        Log.d("NTFaceRecognitionImpl", "startEnroll......");
    }

    @Override // com.nt.settings.face.NtIFaceRecognition
    public void endEnroll() {
        cancelEnrollment();
        this.mHandler = null;
        this.onFaceSaveErrorNum = 0;
        Log.d("NTFaceRecognitionImpl", "endEnroll......");
    }

    private void cancelEnrollment() {
        if (this.mEnrolling) {
            this.mCancellationSignal.cancel();
            this.mEnrolling = false;
        }
    }

    @Override // com.nt.settings.face.NtIFaceRecognition
    public boolean closeHardwareDevice() {
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() { // from class: com.nt.settings.face.NtFaceRecognitionImpl.11
                @Override // java.lang.Runnable
                public void run() {
                    NtFaceRecognitionImpl.this.mNtFaceRecognitionManager.closeHardwareDevice();
                }
            });
            return true;
        }
        this.mNtFaceRecognitionManager.closeHardwareDevice();
        return true;
    }

    @Override // com.nt.settings.face.NtIFaceRecognition
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
            Log.d("NTFaceRecognitionImpl", "face id:" + iArr[i]);
        }
        return iArr;
    }

    @Override // com.nt.settings.face.NtIFaceRecognition
    public int getFaceMetadatasCount() {
        return getIds().length;
    }

    @Override // com.nt.settings.face.NtIFaceRecognition
    public void deleteFaceMetadatas() {
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() { // from class: com.nt.settings.face.NtFaceRecognitionImpl.12
                @Override // java.lang.Runnable
                public void run() {
                    NtFaceRecognitionImpl.this.mNtFaceRecognitionManager.removeAllFaceMetadata(NtFaceRecognitionImpl.this.mUserId, NtFaceRecognitionImpl.this.mRemovalCallback);
                }
            });
        } else {
            this.mNtFaceRecognitionManager.removeAllFaceMetadata(this.mUserId, this.mRemovalCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSaveFeatureProgress(int i, CharSequence charSequence) {
        if (this.mHandler != null) {
            Message message = new Message();
            message.what = 7;
            message.arg1 = i;
            message.obj = charSequence;
            Log.d("NTFaceRecognitionImpl", "updateSaveFeatureProgress msg.arg1:" + message.arg1);
            this.mHandler.sendMessage(message);
        }
    }

    @Override // com.nt.settings.face.NtIFaceRecognition
    public void rename(final int i, final int i2, final String str) {
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() { // from class: com.nt.settings.face.NtFaceRecognitionImpl.14
                @Override // java.lang.Runnable
                public void run() {
                    NtFaceRecognitionImpl.this.mNtFaceRecognitionManager.rename(i, i2, str);
                }
            });
        } else {
            this.mNtFaceRecognitionManager.rename(i, i2, str);
        }
    }

    @Override // com.nt.settings.face.NtIFaceRecognition
    public List<FaceMetadata> getEnrolledFaceMetadatas(final int i) {
        final ArrayList arrayList = new ArrayList();
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() { // from class: com.nt.settings.face.NtFaceRecognitionImpl.15
                @Override // java.lang.Runnable
                public void run() {
                    arrayList.addAll(NtFaceRecognitionImpl.this.mNtFaceRecognitionManager.getEnrolledFaceMetadatas(i));
                }
            });
        } else {
            arrayList.addAll(this.mNtFaceRecognitionManager.getEnrolledFaceMetadatas(i));
        }
        return arrayList;
    }

    @Override // com.nt.settings.face.NtIFaceRecognition
    public void removeFaceMetadata(final FaceMetadata faceMetadata, final NtFaceRecognitionManager.RemovalCallback removalCallback) {
        if (this.mFaceServiceDead) {
            createNtFaceRecognitionManager(new Runnable() { // from class: com.nt.settings.face.NtFaceRecognitionImpl.16
                @Override // java.lang.Runnable
                public void run() {
                    NtFaceRecognitionImpl.this.mNtFaceRecognitionManager.remove(faceMetadata, NtFaceRecognitionImpl.this.mUserId, removalCallback);
                }
            });
        } else {
            this.mNtFaceRecognitionManager.remove(faceMetadata, this.mUserId, removalCallback);
        }
    }
}
