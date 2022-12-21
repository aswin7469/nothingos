package com.p025nt.facerecognition.manager;

import android.app.ActivityManagerNative;
import android.content.Context;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.Settings;
import android.security.keystore.AndroidKeyStoreProvider;
import android.util.Log;
import android.util.Slog;
import android.view.Surface;
import com.p025nt.facerecognition.manager.IFaceRecognitionServiceLockoutResetCallback;
import com.p025nt.facerecognition.manager.IFaceRecognitionServiceReceiver;
import java.security.Signature;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.Mac;

/* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager */
public class NtFaceRecognitionManager {
    private static final boolean DEBUG = true;
    private static final int MSG_ACQUIRED = 101;
    private static final int MSG_AUTHENTICATION_FACE_DETECTED = 110;
    private static final int MSG_AUTHENTICATION_FAILED = 103;
    private static final int MSG_AUTHENTICATION_SUCCEEDED = 102;
    private static final int MSG_AUTHENTICATION_TIMEOUT = 109;
    private static final int MSG_ENROLL_RESULT = 100;
    private static final int MSG_ENUMERATED = 106;
    private static final int MSG_ERROR = 104;
    private static final int MSG_REMOVED = 105;
    private static final int MSG_SCREEN_BRIGHTNESS_OVERRIDE = 108;
    private static final int MSG_WARMUP_HARDWAREDEVICE_RESULT = 107;
    private static final String TAG = "NtFaceRecognitionManager";
    public AuthenticationCallback mAuthenticationCallback;
    private Context mContext;
    public CryptoObject mCryptoObject;
    public EnrollmentCallback mEnrollmentCallback;
    public EnumerateCallback mEnumerateCallback;
    public Handler mHandler;
    public RemovalCallback mRemovalCallback;
    public FaceMetadata mRemovalFaceMetadata;
    private IFaceRecognitionManagerService mService;
    private IFaceRecognitionServiceReceiver mServiceReceiver = new IFaceRecognitionServiceReceiver.Stub() {
        public void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(107, cameraPreviewProperty).sendToTarget();
        }

        public void onEnrollResult(long j, int i, int i2, int i3) {
            CharSequence charSequence = null;
            NtFaceRecognitionManager.this.mHandler.obtainMessage(100, i3, 0, new FaceMetadata((CharSequence) null, i2, i, j)).sendToTarget();
        }

        public void onAcquired(long j, int i, int i2) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(101, i, i2, Long.valueOf(j)).sendToTarget();
        }

        public void onAuthenticationSucceeded(long j, FaceMetadata faceMetadata, int i) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(102, i, 0, faceMetadata).sendToTarget();
        }

        public void onAuthenticationFailed(long j, int i) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(103, i, 0).sendToTarget();
        }

        public void onAuthenticationTimeout(long j) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(109).sendToTarget();
        }

        public void onAuthenticationFaceDetected(long j, boolean z) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(110, Boolean.valueOf(z)).sendToTarget();
        }

        public void onError(long j, int i, int i2) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(104, i, i2, Long.valueOf(j)).sendToTarget();
        }

        public void onRemoved(long j, int i, int i2, int i3) {
            CharSequence charSequence = null;
            NtFaceRecognitionManager.this.mHandler.obtainMessage(105, i3, 0, new FaceMetadata((CharSequence) null, i2, i, j)).sendToTarget();
        }

        public void onEnumerated(long j, int i, int i2, int i3) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(106, i, i2, Long.valueOf(j)).sendToTarget();
        }

        public void onScreenBrightnessOverrided(int i, float f) {
            Log.w(NtFaceRecognitionManager.TAG, "mServiceReceiver screenbrightnessvalue: " + i + ",ambientLux:" + f);
            NtFaceRecognitionManager.this.mHandler.obtainMessage(108, 0, i, Float.valueOf(f)).sendToTarget();
        }
    };
    private IBinder mToken = new Binder();
    public WarmUpHardwareDeviceCallback mWarmUpHardwareDeviceCallback;

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$AuthenticationCallback */
    public static abstract class AuthenticationCallback {
        public void onAuthenticationAcquired(int i) {
        }

        public void onAuthenticationError(int i, CharSequence charSequence) {
        }

        public void onAuthenticationFaceDetected(boolean z) {
        }

        public void onAuthenticationFailed(int i) {
        }

        public void onAuthenticationHelp(int i, CharSequence charSequence) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult authenticationResult) {
        }

        public void onAuthenticationTimeout() {
        }

        public void onScreenBrightnessOverride(int i, float f) {
        }
    }

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$EnrollmentCallback */
    public static abstract class EnrollmentCallback {
        public void onEnrollmentError(int i, CharSequence charSequence) {
        }

        public void onEnrollmentHelp(int i, CharSequence charSequence) {
        }

        public void onEnrollmentProgress(int i) {
        }
    }

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$EnumerateCallback */
    public static abstract class EnumerateCallback {
        public void onEnumerate(FaceMetadata faceMetadata) {
        }

        public void onEnumerateError(int i, CharSequence charSequence) {
        }
    }

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$LockoutResetCallback */
    public static abstract class LockoutResetCallback {
        public void onLockoutReset() {
        }
    }

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$RemovalCallback */
    public static abstract class RemovalCallback {
        public void onRemovalError(FaceMetadata faceMetadata, int i, CharSequence charSequence) {
        }

        public void onRemovalSucceeded(FaceMetadata faceMetadata, int i) {
        }
    }

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$WarmUpHardwareDeviceCallback */
    public static abstract class WarmUpHardwareDeviceCallback {
        public void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) {
        }
    }

    public String getErrorString(int i) {
        return null;
    }

    public String getErrorString(int i, int i2) {
        return null;
    }

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$OnEnrollCancelListener */
    private class OnEnrollCancelListener implements CancellationSignal.OnCancelListener {
        private OnEnrollCancelListener() {
        }

        public void onCancel() {
            NtFaceRecognitionManager.this.cancelEnrollment();
        }
    }

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$OnAuthenticationCancelListener */
    private class OnAuthenticationCancelListener implements CancellationSignal.OnCancelListener {
        private CryptoObject mCrypto;

        public OnAuthenticationCancelListener(CryptoObject cryptoObject) {
            this.mCrypto = cryptoObject;
        }

        public void onCancel() {
            NtFaceRecognitionManager.this.cancelAuthentication(this.mCrypto);
        }
    }

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$CryptoObject */
    public static final class CryptoObject {
        private final Object mCrypto;

        public CryptoObject(Signature signature) {
            this.mCrypto = signature;
        }

        public CryptoObject(Cipher cipher) {
            this.mCrypto = cipher;
        }

        public CryptoObject(Mac mac) {
            this.mCrypto = mac;
        }

        public Signature getSignature() {
            Object obj = this.mCrypto;
            if (obj instanceof Signature) {
                return (Signature) obj;
            }
            return null;
        }

        public Cipher getCipher() {
            Object obj = this.mCrypto;
            if (obj instanceof Cipher) {
                return (Cipher) obj;
            }
            return null;
        }

        public Mac getMac() {
            Object obj = this.mCrypto;
            if (obj instanceof Mac) {
                return (Mac) obj;
            }
            return null;
        }

        public long getOpId() {
            Object obj = this.mCrypto;
            if (obj != null) {
                return AndroidKeyStoreProvider.getKeyStoreOperationHandle(obj);
            }
            return 0;
        }
    }

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$AuthenticationResult */
    public static class AuthenticationResult {
        private CryptoObject mCryptoObject;
        private FaceMetadata mFaceMetadata;
        private int mUserId;

        public AuthenticationResult(CryptoObject cryptoObject, FaceMetadata faceMetadata, int i) {
            this.mCryptoObject = cryptoObject;
            this.mFaceMetadata = faceMetadata;
            this.mUserId = i;
        }

        public CryptoObject getCryptoObject() {
            return this.mCryptoObject;
        }

        public FaceMetadata getFaceMetaData() {
            return this.mFaceMetadata;
        }

        public int getUserId() {
            return this.mUserId;
        }
    }

    public void authenticate(CryptoObject cryptoObject, CancellationSignal cancellationSignal, int i, AuthenticationCallback authenticationCallback, Handler handler) {
        authenticate(cryptoObject, cancellationSignal, i, authenticationCallback, handler, UserHandle.myUserId());
    }

    private void useHandler(Handler handler) {
        Log.w(TAG, "useHandler: handler");
        if (handler != null) {
            this.mHandler = new MyHandler(handler.getLooper());
            Log.w(TAG, "useHandler: handler Looper");
        } else if (this.mHandler.getLooper() != this.mContext.getMainLooper()) {
            this.mHandler = new MyHandler(this.mContext.getMainLooper());
            Log.w(TAG, "useHandler: MainLooper");
        }
    }

    public boolean warmUpHardwareDevice(WarmUpHardwareDeviceCallback warmUpHardwareDeviceCallback) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return false;
        }
        try {
            this.mWarmUpHardwareDeviceCallback = warmUpHardwareDeviceCallback;
            return iFaceRecognitionManagerService.warmUpHardwareDevice(this.mServiceReceiver);
        } catch (Exception e) {
            Log.w(TAG, "Remote exception while WarmUp Hardware Device for Face Recognition: ", e);
            return false;
        }
    }

    public void authenticate(CryptoObject cryptoObject, CancellationSignal cancellationSignal, int i, AuthenticationCallback authenticationCallback, Handler handler, int i2) {
        if (authenticationCallback != null) {
            if (cancellationSignal != null) {
                if (cancellationSignal.isCanceled()) {
                    Log.w(TAG, "authentication already canceled");
                    return;
                }
                cancellationSignal.setOnCancelListener(new OnAuthenticationCancelListener(cryptoObject));
            }
            if (this.mService != null) {
                try {
                    useHandler(handler);
                    this.mAuthenticationCallback = authenticationCallback;
                    this.mCryptoObject = cryptoObject;
                    this.mService.authenticate(this.mToken, cryptoObject != null ? cryptoObject.getOpId() : 0, i2, this.mServiceReceiver, i, this.mContext.getOpPackageName());
                } catch (Exception e) {
                    Log.w(TAG, "Remote exception while authenticating: ", e);
                    if (authenticationCallback != null) {
                        authenticationCallback.onAuthenticationError(1, getErrorString(1, 0));
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Must supply an authentication callback");
        }
    }

    public boolean warmUpHardwareDeviceForPreview(WarmUpHardwareDeviceCallback warmUpHardwareDeviceCallback, Surface surface, int i, int i2) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return false;
        }
        try {
            this.mWarmUpHardwareDeviceCallback = warmUpHardwareDeviceCallback;
            return iFaceRecognitionManagerService.warmUpHardwareDeviceForPreview(this.mServiceReceiver, surface, i, i2);
        } catch (Exception e) {
            Log.w(TAG, "Remote exception while WarmUp Hardware Device for Preview: ", e);
            return false;
        }
    }

    public void enroll(byte[] bArr, CancellationSignal cancellationSignal, int i, int i2, EnrollmentCallback enrollmentCallback, Rect rect) {
        if (i2 == -2) {
            i2 = getCurrentUserId();
        }
        int i3 = i2;
        if (enrollmentCallback != null) {
            if (cancellationSignal != null) {
                if (cancellationSignal.isCanceled()) {
                    Log.w(TAG, "enrollment already canceled");
                    return;
                }
                cancellationSignal.setOnCancelListener(new OnEnrollCancelListener());
            }
            IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
            if (iFaceRecognitionManagerService != null) {
                try {
                    this.mEnrollmentCallback = enrollmentCallback;
                    iFaceRecognitionManagerService.enroll(this.mToken, bArr, i3, this.mServiceReceiver, i, this.mContext.getOpPackageName(), rect);
                } catch (Exception e) {
                    Log.w(TAG, "Remote exception in enroll: ", e);
                    if (enrollmentCallback != null) {
                        enrollmentCallback.onEnrollmentError(1, getErrorString(1, 0));
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Must supply an enrollment callback");
        }
    }

    public boolean closeHardwareDevice() {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return false;
        }
        try {
            return iFaceRecognitionManagerService.closeHardwareDevice();
        } catch (Exception e) {
            Log.w(TAG, "Exception while Close Hardware Device: ", e);
            return false;
        }
    }

    public long preEnroll() {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return 0;
        }
        try {
            return iFaceRecognitionManagerService.preEnroll(this.mToken);
        } catch (Exception e) {
            Log.w(TAG, "Exception while pre Enroll: ", e);
            return 0;
        }
    }

    public int postEnroll() {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return 0;
        }
        try {
            return iFaceRecognitionManagerService.postEnroll(this.mToken);
        } catch (Exception e) {
            Log.w(TAG, "Exception while post Enroll: ", e);
            return 0;
        }
    }

    public void setActiveUser(int i) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                iFaceRecognitionManagerService.setActiveUser(i);
            } catch (Exception e) {
                Log.w(TAG, "Exception while set Active User: ", e);
            }
        }
    }

    private boolean getFaceUnlock(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "nt_face_unlock", 0) == 1 && Settings.Secure.getInt(context.getContentResolver(), "nt_face_management_added_data", 0) > 0;
    }

    public int warmUpOrStopHardwareDeviceByDisplayState(boolean z) {
        if (!getFaceUnlock(this.mContext)) {
            return 0;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mService != null) {
            try {
                Log.i(TAG, "warmUp: " + z);
                this.mService.warmUpOrStopHardwareDeviceByDisplayState(z);
            } catch (Exception e) {
                Log.w(TAG, "Remote exception when warmUp: ", e);
            }
        }
        Log.d(TAG, "warmUp cost :" + (System.currentTimeMillis() - currentTimeMillis));
        return 0;
    }

    public void remove(FaceMetadata faceMetadata, int i, RemovalCallback removalCallback) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                this.mRemovalCallback = removalCallback;
                this.mRemovalFaceMetadata = faceMetadata;
                iFaceRecognitionManagerService.remove(this.mToken, faceMetadata.getFaceId(), faceMetadata.getGroupId(), i, this.mServiceReceiver);
            } catch (Exception e) {
                Log.w(TAG, "Remote exception in remove: ", e);
                if (removalCallback != null) {
                    removalCallback.onRemovalError(faceMetadata, 1, getErrorString(1, 0));
                }
            }
        }
    }

    public void removeAllFaceMetadata(int i, RemovalCallback removalCallback) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                this.mRemovalCallback = removalCallback;
                iFaceRecognitionManagerService.removeAllFaceMetadata(this.mToken, i, this.mServiceReceiver);
            } catch (Exception e) {
                Log.w(TAG, "Remote exception in remove: ", e);
                if (removalCallback != null) {
                    FaceMetadata faceMetadata = null;
                    removalCallback.onRemovalError((FaceMetadata) null, 1, getErrorString(1, 0));
                }
            }
        }
    }

    public void enumerate(int i, EnumerateCallback enumerateCallback) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                this.mEnumerateCallback = enumerateCallback;
                iFaceRecognitionManagerService.enumerate(this.mToken, i, this.mServiceReceiver);
            } catch (Exception e) {
                Log.w(TAG, "Remote exception in enumerate: ", e);
                if (enumerateCallback != null) {
                    enumerateCallback.onEnumerateError(1, getErrorString(1, 0));
                }
            }
        }
    }

    public void rename(int i, int i2, String str) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                iFaceRecognitionManagerService.rename(i, i2, str);
            } catch (Exception e) {
                Log.w(TAG, "Exception while rename: ", e);
            }
        }
    }

    public List<FaceMetadata> getEnrolledFaceMetadatas(int i) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return null;
        }
        try {
            return iFaceRecognitionManagerService.getEnrolledFaceMetadatas(i, this.mContext.getOpPackageName());
        } catch (Exception e) {
            Log.w(TAG, "Exception while get Enrolled FaceMetadatas: ", e);
            return null;
        }
    }

    public List<FaceMetadata> getEnrolledFaceMetadatas() {
        return getEnrolledFaceMetadatas(UserHandle.myUserId());
    }

    public boolean hasEnrolledFaceMetadatas() {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return false;
        }
        try {
            return iFaceRecognitionManagerService.hasEnrolledFaceMetadatas(UserHandle.myUserId(), this.mContext.getOpPackageName());
        } catch (Exception e) {
            Log.w(TAG, "Exception while has EnrolledFace Metadatas: ", e);
            return false;
        }
    }

    public boolean hasEnrolledFaceMetadatas(int i) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return false;
        }
        try {
            return iFaceRecognitionManagerService.hasEnrolledFaceMetadatas(i, this.mContext.getOpPackageName());
        } catch (Exception e) {
            Log.w(TAG, "Exception while has Enrolled FaceMetadatas by userId: ", e);
            return false;
        }
    }

    public boolean isHardwareDetected() {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return false;
        }
        try {
            return iFaceRecognitionManagerService.isHardwareDetected(0, this.mContext.getOpPackageName());
        } catch (Exception e) {
            Log.w(TAG, "Exception while has Enrolled FaceMetadatas by userId: ", e);
            return false;
        }
    }

    public long getAuthenticatorId() {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return 0;
        }
        try {
            return iFaceRecognitionManagerService.getAuthenticatorId(this.mContext.getOpPackageName());
        } catch (Exception e) {
            Log.w(TAG, "Exception while get Authenticator Id: ", e);
            return 0;
        }
    }

    public void resetTimeout(byte[] bArr) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                iFaceRecognitionManagerService.resetTimeout(bArr);
            } catch (Exception e) {
                Log.w(TAG, "Exception while reset Timeout: ", e);
            }
        }
    }

    public void addLockoutResetCallback(final LockoutResetCallback lockoutResetCallback) {
        if (this.mService != null) {
            try {
                final PowerManager powerManager = (PowerManager) this.mContext.getSystemService(PowerManager.class);
                this.mService.addLockoutResetCallback(new IFaceRecognitionServiceLockoutResetCallback.Stub() {
                    public void onLockoutReset(long j, IRemoteCallback iRemoteCallback) throws RemoteException {
                        try {
                            final PowerManager.WakeLock newWakeLock = powerManager.newWakeLock(1, "lockoutResetCallback");
                            newWakeLock.setReferenceCounted(false);
                            newWakeLock.acquire();
                            NtFaceRecognitionManager.this.mHandler.post(new Runnable() {
                                public void run() {
                                    try {
                                        lockoutResetCallback.onLockoutReset();
                                    } finally {
                                        newWakeLock.release();
                                    }
                                }
                            });
                        } finally {
                            Bundle bundle = null;
                            iRemoteCallback.sendResult((Bundle) null);
                        }
                    }
                });
            } catch (Exception e) {
                Log.w(TAG, "Exception while add Lockout ResetCallback: ", e);
            }
        }
    }

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$MyHandler */
    private class MyHandler extends Handler {
        private MyHandler(Context context) {
            super(context.getMainLooper());
        }

        private MyHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            Log.d(NtFaceRecognitionManager.TAG, "handleMessage  msg.what:" + message.what);
            switch (message.what) {
                case 100:
                    sendEnrollResult((FaceMetadata) message.obj, message.arg1);
                    return;
                case 101:
                    sendAcquiredResult(((Long) message.obj).longValue(), message.arg1, message.arg2);
                    return;
                case 102:
                    sendAuthenticatedSucceeded((FaceMetadata) message.obj, message.arg1);
                    return;
                case 103:
                    sendAuthenticatedFailed(message.arg1);
                    return;
                case 104:
                    sendErrorResult(((Long) message.obj).longValue(), message.arg1, message.arg2);
                    return;
                case 105:
                    sendRemovedResult((FaceMetadata) message.obj, message.arg1);
                    return;
                case 106:
                    sendEnumeratedResult(((Long) message.obj).longValue(), message.arg1, message.arg2);
                    break;
                case 107:
                    sendWarmUpHardwareDeviceResult((CameraPreviewProperty) message.obj);
                    return;
                case 108:
                    break;
                case 109:
                    sendAuthenticatedTimeout();
                    return;
                case 110:
                    sendAuthenticatedFaceDetected(((Boolean) message.obj).booleanValue());
                    return;
                default:
                    return;
            }
            sendScreenBrightnessOverride(message.arg2, ((Float) message.obj).floatValue());
        }

        private void sendRemovedResult(FaceMetadata faceMetadata, int i) {
            if (NtFaceRecognitionManager.this.mRemovalCallback == null) {
                return;
            }
            if (faceMetadata == null || NtFaceRecognitionManager.this.mRemovalFaceMetadata == null) {
                Log.d(NtFaceRecognitionManager.TAG, "Received MSG_REMOVED");
                return;
            }
            int faceId = faceMetadata.getFaceId();
            int faceId2 = NtFaceRecognitionManager.this.mRemovalFaceMetadata.getFaceId();
            if (faceId2 == 0 || faceId == 0 || faceId == faceId2) {
                int groupId = faceMetadata.getGroupId();
                int groupId2 = NtFaceRecognitionManager.this.mRemovalFaceMetadata.getGroupId();
                if (groupId != groupId2) {
                    Log.w(NtFaceRecognitionManager.TAG, "Group id didn't match: " + groupId + " != " + groupId2);
                    return;
                }
                NtFaceRecognitionManager.this.mRemovalCallback.onRemovalSucceeded(faceMetadata, i);
                return;
            }
            Log.w(NtFaceRecognitionManager.TAG, "Face id didn't match: " + faceId + " != " + faceId2);
        }

        private void sendEnumeratedResult(long j, int i, int i2) {
            if (NtFaceRecognitionManager.this.mEnumerateCallback != null) {
                CharSequence charSequence = null;
                NtFaceRecognitionManager.this.mEnumerateCallback.onEnumerate(new FaceMetadata((CharSequence) null, i2, i, j));
            }
        }

        private void sendErrorResult(long j, int i, int i2) {
            int i3 = i == 8 ? i2 + 1000 : i;
            if (NtFaceRecognitionManager.this.mEnrollmentCallback != null) {
                NtFaceRecognitionManager.this.mEnrollmentCallback.onEnrollmentError(i3, NtFaceRecognitionManager.this.getErrorString(i, i2));
            } else if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                NtFaceRecognitionManager.this.mAuthenticationCallback.onAuthenticationError(i3, NtFaceRecognitionManager.this.getErrorString(i, i2));
            } else if (NtFaceRecognitionManager.this.mRemovalCallback != null) {
                NtFaceRecognitionManager.this.mRemovalCallback.onRemovalError(NtFaceRecognitionManager.this.mRemovalFaceMetadata, i3, NtFaceRecognitionManager.this.getErrorString(i, i2));
            } else if (NtFaceRecognitionManager.this.mEnumerateCallback != null) {
                NtFaceRecognitionManager.this.mEnumerateCallback.onEnumerateError(i3, NtFaceRecognitionManager.this.getErrorString(i, i2));
            }
        }

        private void sendWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) {
            if (NtFaceRecognitionManager.this.mWarmUpHardwareDeviceCallback != null) {
                NtFaceRecognitionManager.this.mWarmUpHardwareDeviceCallback.onWarmUpHardwareDeviceResult(cameraPreviewProperty);
            }
        }

        private void sendScreenBrightnessOverride(int i, float f) {
            if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                Log.w(NtFaceRecognitionManager.TAG, "sendScreenBrightnessOverride screenbrightnessvalue: " + i + ",ambientLux:" + f);
                NtFaceRecognitionManager.this.mAuthenticationCallback.onScreenBrightnessOverride(i, f);
            }
        }

        private void sendEnrollResult(FaceMetadata faceMetadata, int i) {
            if (NtFaceRecognitionManager.this.mEnrollmentCallback != null) {
                NtFaceRecognitionManager.this.mEnrollmentCallback.onEnrollmentProgress(i);
            }
        }

        private void sendAuthenticatedSucceeded(FaceMetadata faceMetadata, int i) {
            if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                NtFaceRecognitionManager.this.mAuthenticationCallback.onAuthenticationSucceeded(new AuthenticationResult(NtFaceRecognitionManager.this.mCryptoObject, faceMetadata, i));
            }
        }

        private void sendAuthenticatedFailed(int i) {
            if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                NtFaceRecognitionManager.this.mAuthenticationCallback.onAuthenticationFailed(i);
            }
        }

        private void sendAuthenticatedTimeout() {
            if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                NtFaceRecognitionManager.this.mAuthenticationCallback.onAuthenticationTimeout();
            }
        }

        private void sendAuthenticatedFaceDetected(boolean z) {
            if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                NtFaceRecognitionManager.this.mAuthenticationCallback.onAuthenticationFaceDetected(z);
            }
        }

        private void sendAcquiredResult(long j, int i, int i2) {
            if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                NtFaceRecognitionManager.this.mAuthenticationCallback.onAuthenticationAcquired(i);
            }
            String acquiredString = NtFaceRecognitionManager.this.getAcquiredString(i, i2);
            if (acquiredString != null) {
                if (i == 6) {
                    i = i2 + 1000;
                }
                if (NtFaceRecognitionManager.this.mEnrollmentCallback != null) {
                    NtFaceRecognitionManager.this.mEnrollmentCallback.onEnrollmentHelp(i, acquiredString);
                } else if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                    NtFaceRecognitionManager.this.mAuthenticationCallback.onAuthenticationHelp(i, acquiredString);
                }
            }
        }
    }

    public NtFaceRecognitionManager(Context context, IFaceRecognitionManagerService iFaceRecognitionManagerService) {
        if (this.mService == null || context == null) {
            Slog.v(TAG, "NtFaceRecognitionManager was null");
        }
        this.mContext = context;
        this.mService = iFaceRecognitionManagerService;
        this.mHandler = new MyHandler(context);
    }

    private int getCurrentUserId() {
        try {
            return ActivityManagerNative.getDefault().getCurrentUser().id;
        } catch (Exception unused) {
            return 0;
        }
    }

    public void cancelEnrollment() {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                iFaceRecognitionManagerService.cancelEnrollment(this.mToken);
            } catch (Exception e) {
                Log.w(TAG, "Exception while cancel Enrollment: ", e);
            }
        }
    }

    public void cancelAuthentication(CryptoObject cryptoObject) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                iFaceRecognitionManagerService.cancelAuthentication(this.mToken, this.mContext.getOpPackageName());
            } catch (Exception e) {
                Log.w(TAG, "Exception while cancel Authentication: ", e);
            }
        }
    }

    public String getAcquiredString(int i, int i2) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return null;
        }
        try {
            return iFaceRecognitionManagerService.getReportString(i);
        } catch (Exception unused) {
            return null;
        }
    }
}
