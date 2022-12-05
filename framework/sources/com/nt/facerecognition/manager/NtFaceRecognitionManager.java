package com.nt.facerecognition.manager;

import android.app.ActivityManagerNative;
import android.content.Context;
import android.graphics.Rect;
import android.os.Binder;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.NtSettings;
import android.provider.Settings;
import android.security.keystore.AndroidKeyStoreProvider;
import android.util.Log;
import android.util.Slog;
import android.view.Surface;
import com.nt.facerecognition.manager.IFaceRecognitionServiceLockoutResetCallback;
import com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver;
import java.security.Signature;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.Mac;
/* loaded from: classes4.dex */
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
    private AuthenticationCallback mAuthenticationCallback;
    private Context mContext;
    private CryptoObject mCryptoObject;
    private EnrollmentCallback mEnrollmentCallback;
    private EnumerateCallback mEnumerateCallback;
    private Handler mHandler;
    private RemovalCallback mRemovalCallback;
    private FaceMetadata mRemovalFaceMetadata;
    private IFaceRecognitionManagerService mService;
    private WarmUpHardwareDeviceCallback mWarmUpHardwareDeviceCallback;
    private IBinder mToken = new Binder();
    private IFaceRecognitionServiceReceiver mServiceReceiver = new IFaceRecognitionServiceReceiver.Stub() { // from class: com.nt.facerecognition.manager.NtFaceRecognitionManager.2
        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(107, cameraPreviewProperty).sendToTarget();
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onEnrollResult(long deviceId, int faceId, int groupId, int remaining) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(100, remaining, 0, new FaceMetadata(null, groupId, faceId, deviceId)).sendToTarget();
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onAcquired(long deviceId, int acquireInfo, int vendorCode) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(101, acquireInfo, vendorCode, Long.valueOf(deviceId)).sendToTarget();
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onAuthenticationSucceeded(long deviceId, FaceMetadata faceMetadata, int userId) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(102, userId, 0, faceMetadata).sendToTarget();
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onAuthenticationFailed(long deviceId, int error) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(103, error, 0).sendToTarget();
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onAuthenticationTimeout(long deviceId) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(109).sendToTarget();
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onAuthenticationFaceDetected(long deviceId, boolean detected) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(110, Boolean.valueOf(detected)).sendToTarget();
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onError(long deviceId, int error, int vendorCode) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(104, error, vendorCode, Long.valueOf(deviceId)).sendToTarget();
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onRemoved(long deviceId, int faceId, int groupId, int remaining) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(105, remaining, 0, new FaceMetadata(null, groupId, faceId, deviceId)).sendToTarget();
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onEnumerated(long deviceId, int faceId, int groupId, int remaining) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(106, faceId, groupId, Long.valueOf(deviceId)).sendToTarget();
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onScreenBrightnessOverrided(int screenbrightnessvalue, float ambientLux) {
            Log.w(NtFaceRecognitionManager.TAG, "mServiceReceiver screenbrightnessvalue: " + screenbrightnessvalue + ",ambientLux:" + ambientLux);
            NtFaceRecognitionManager.this.mHandler.obtainMessage(108, 0, screenbrightnessvalue, Float.valueOf(ambientLux)).sendToTarget();
        }
    };

    /* loaded from: classes4.dex */
    private class OnEnrollCancelListener implements CancellationSignal.OnCancelListener {
        private OnEnrollCancelListener() {
        }

        @Override // android.os.CancellationSignal.OnCancelListener
        public void onCancel() {
            NtFaceRecognitionManager.this.cancelEnrollment();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class OnAuthenticationCancelListener implements CancellationSignal.OnCancelListener {
        private CryptoObject mCrypto;

        public OnAuthenticationCancelListener(CryptoObject crypto) {
            this.mCrypto = crypto;
        }

        @Override // android.os.CancellationSignal.OnCancelListener
        public void onCancel() {
            NtFaceRecognitionManager.this.cancelAuthentication(this.mCrypto);
        }
    }

    /* loaded from: classes4.dex */
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
            return 0L;
        }
    }

    /* loaded from: classes4.dex */
    public static class AuthenticationResult {
        private CryptoObject mCryptoObject;
        private FaceMetadata mFaceMetadata;
        private int mUserId;

        public AuthenticationResult(CryptoObject crypto, FaceMetadata faceMetadata, int userId) {
            this.mCryptoObject = crypto;
            this.mFaceMetadata = faceMetadata;
            this.mUserId = userId;
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

    /* loaded from: classes4.dex */
    public static abstract class AuthenticationCallback {
        public void onAuthenticationError(int errorCode, CharSequence errString) {
        }

        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        }

        public void onAuthenticationSucceeded(AuthenticationResult result) {
        }

        public void onAuthenticationFailed(int errorCode) {
        }

        public void onAuthenticationTimeout() {
        }

        public void onAuthenticationFaceDetected(boolean detected) {
        }

        public void onAuthenticationAcquired(int acquireInfo) {
        }

        public void onScreenBrightnessOverride(int screenbrightnessvalue, float ambientLux) {
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class EnrollmentCallback {
        public void onEnrollmentError(int errMsgId, CharSequence errString) {
        }

        public void onEnrollmentHelp(int helpMsgId, CharSequence helpString) {
        }

        public void onEnrollmentProgress(int remaining) {
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class WarmUpHardwareDeviceCallback {
        public void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) {
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class RemovalCallback {
        public void onRemovalError(FaceMetadata faceMetadata, int errMsgId, CharSequence errString) {
        }

        public void onRemovalSucceeded(FaceMetadata faceMetadata, int remaining) {
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class EnumerateCallback {
        public void onEnumerateError(int errMsgId, CharSequence errString) {
        }

        public void onEnumerate(FaceMetadata faceMetadata) {
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class LockoutResetCallback {
        public void onLockoutReset() {
        }
    }

    public void authenticate(CryptoObject crypto, CancellationSignal cancel, int flags, AuthenticationCallback callback, Handler handler) {
        authenticate(crypto, cancel, flags, callback, handler, UserHandle.myUserId());
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

    public boolean warmUpHardwareDevice(WarmUpHardwareDeviceCallback callback) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return false;
        }
        try {
            this.mWarmUpHardwareDeviceCallback = callback;
            boolean result = iFaceRecognitionManagerService.warmUpHardwareDevice(this.mServiceReceiver);
            return result;
        } catch (Exception e) {
            Log.w(TAG, "Remote exception while WarmUp Hardware Device for Face Recognition: ", e);
            return false;
        }
    }

    public void authenticate(CryptoObject crypto, CancellationSignal cancel, int flags, AuthenticationCallback callback, Handler handler, int userId) {
        if (callback != null) {
            if (cancel != null) {
                if (!cancel.isCanceled()) {
                    cancel.setOnCancelListener(new OnAuthenticationCancelListener(crypto));
                } else {
                    Log.w(TAG, "authentication already canceled");
                    return;
                }
            }
            if (this.mService != null) {
                try {
                    useHandler(handler);
                    this.mAuthenticationCallback = callback;
                    this.mCryptoObject = crypto;
                    long sessionId = crypto != null ? crypto.getOpId() : 0L;
                    this.mService.authenticate(this.mToken, sessionId, userId, this.mServiceReceiver, flags, this.mContext.getOpPackageName());
                    return;
                } catch (Exception e) {
                    Log.w(TAG, "Remote exception while authenticating: ", e);
                    if (callback != null) {
                        callback.onAuthenticationError(1, getErrorString(1, 0));
                        return;
                    }
                    return;
                }
            }
            return;
        }
        throw new IllegalArgumentException("Must supply an authentication callback");
    }

    public boolean warmUpHardwareDeviceForPreview(WarmUpHardwareDeviceCallback callback, Surface surface, int width, int height) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return false;
        }
        try {
            this.mWarmUpHardwareDeviceCallback = callback;
            boolean result = iFaceRecognitionManagerService.warmUpHardwareDeviceForPreview(this.mServiceReceiver, surface, width, height);
            return result;
        } catch (Exception e) {
            Log.w(TAG, "Remote exception while WarmUp Hardware Device for Preview: ", e);
            return false;
        }
    }

    public void enroll(byte[] token, CancellationSignal cancel, int flags, int userId, EnrollmentCallback callback, Rect frameMarkRect) {
        if (userId == -2) {
            userId = getCurrentUserId();
        }
        if (callback == null) {
            throw new IllegalArgumentException("Must supply an enrollment callback");
        }
        if (cancel != null) {
            if (cancel.isCanceled()) {
                Log.w(TAG, "enrollment already canceled");
                return;
            }
            cancel.setOnCancelListener(new OnEnrollCancelListener());
        }
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                this.mEnrollmentCallback = callback;
                iFaceRecognitionManagerService.enroll(this.mToken, token, userId, this.mServiceReceiver, flags, this.mContext.getOpPackageName(), frameMarkRect);
            } catch (Exception e) {
                Log.w(TAG, "Remote exception in enroll: ", e);
                if (callback != null) {
                    callback.onEnrollmentError(1, getErrorString(1, 0));
                }
            }
        }
    }

    public boolean closeHardwareDevice() {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return false;
        }
        try {
            boolean result = iFaceRecognitionManagerService.closeHardwareDevice();
            return result;
        } catch (Exception e) {
            Log.w(TAG, "Exception while Close Hardware Device: ", e);
            return false;
        }
    }

    public long preEnroll() {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return 0L;
        }
        try {
            long result = iFaceRecognitionManagerService.preEnroll(this.mToken);
            return result;
        } catch (Exception e) {
            Log.w(TAG, "Exception while pre Enroll: ", e);
            return 0L;
        }
    }

    public int postEnroll() {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return 0;
        }
        try {
            int result = iFaceRecognitionManagerService.postEnroll(this.mToken);
            return result;
        } catch (Exception e) {
            Log.w(TAG, "Exception while post Enroll: ", e);
            return 0;
        }
    }

    public void setActiveUser(int userId) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                iFaceRecognitionManagerService.setActiveUser(userId);
            } catch (Exception e) {
                Log.w(TAG, "Exception while set Active User: ", e);
            }
        }
    }

    private boolean getFaceUnlock(Context context) {
        boolean z = false;
        if (Settings.Secure.getInt(context.getContentResolver(), NtSettings.Secure.FACERECOGNITION_FACE_UNLOCK, 0) == 1 && Settings.Secure.getInt(context.getContentResolver(), NtSettings.Secure.FACERECOGNITION_ADDED_DATA, 0) > 0) {
            z = true;
        }
        boolean isFaceUnlock = z;
        return isFaceUnlock;
    }

    public int warmUpOrStopHardwareDeviceByDisplayState(boolean warmUp) {
        if (getFaceUnlock(this.mContext)) {
            long start = System.currentTimeMillis();
            if (this.mService != null) {
                try {
                    Log.i(TAG, "warmUp: " + warmUp);
                    this.mService.warmUpOrStopHardwareDeviceByDisplayState(warmUp);
                } catch (Exception e) {
                    Log.w(TAG, "Remote exception when warmUp: ", e);
                }
            }
            Log.d(TAG, "warmUp cost :" + (System.currentTimeMillis() - start));
        }
        return 0;
    }

    public void remove(FaceMetadata faceMetadata, int userId, RemovalCallback callback) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                this.mRemovalCallback = callback;
                this.mRemovalFaceMetadata = faceMetadata;
                iFaceRecognitionManagerService.remove(this.mToken, faceMetadata.getFaceId(), faceMetadata.getGroupId(), userId, this.mServiceReceiver);
            } catch (Exception e) {
                Log.w(TAG, "Remote exception in remove: ", e);
                if (callback != null) {
                    callback.onRemovalError(faceMetadata, 1, getErrorString(1, 0));
                }
            }
        }
    }

    public void removeAllFaceMetadata(int userId, RemovalCallback callback) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                this.mRemovalCallback = callback;
                iFaceRecognitionManagerService.removeAllFaceMetadata(this.mToken, userId, this.mServiceReceiver);
            } catch (Exception e) {
                Log.w(TAG, "Remote exception in remove: ", e);
                if (callback != null) {
                    callback.onRemovalError(null, 1, getErrorString(1, 0));
                }
            }
        }
    }

    public void enumerate(int userId, EnumerateCallback callback) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                this.mEnumerateCallback = callback;
                iFaceRecognitionManagerService.enumerate(this.mToken, userId, this.mServiceReceiver);
            } catch (Exception e) {
                Log.w(TAG, "Remote exception in enumerate: ", e);
                if (callback != null) {
                    callback.onEnumerateError(1, getErrorString(1, 0));
                }
            }
        }
    }

    public void rename(int faceId, int userId, String newName) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                iFaceRecognitionManagerService.rename(faceId, userId, newName);
            } catch (Exception e) {
                Log.w(TAG, "Exception while rename: ", e);
            }
        }
    }

    public List<FaceMetadata> getEnrolledFaceMetadatas(int userId) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                return iFaceRecognitionManagerService.getEnrolledFaceMetadatas(userId, this.mContext.getOpPackageName());
            } catch (Exception e) {
                Log.w(TAG, "Exception while get Enrolled FaceMetadatas: ", e);
                return null;
            }
        }
        return null;
    }

    public List<FaceMetadata> getEnrolledFaceMetadatas() {
        return getEnrolledFaceMetadatas(UserHandle.myUserId());
    }

    public boolean hasEnrolledFaceMetadatas() {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                return iFaceRecognitionManagerService.hasEnrolledFaceMetadatas(UserHandle.myUserId(), this.mContext.getOpPackageName());
            } catch (Exception e) {
                Log.w(TAG, "Exception while has EnrolledFace Metadatas: ", e);
                return false;
            }
        }
        return false;
    }

    public boolean hasEnrolledFaceMetadatas(int userId) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                return iFaceRecognitionManagerService.hasEnrolledFaceMetadatas(userId, this.mContext.getOpPackageName());
            } catch (Exception e) {
                Log.w(TAG, "Exception while has Enrolled FaceMetadatas by userId: ", e);
                return false;
            }
        }
        return false;
    }

    public boolean isHardwareDetected() {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                return iFaceRecognitionManagerService.isHardwareDetected(0L, this.mContext.getOpPackageName());
            } catch (Exception e) {
                Log.w(TAG, "Exception while has Enrolled FaceMetadatas by userId: ", e);
                return false;
            }
        }
        return false;
    }

    public long getAuthenticatorId() {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                return iFaceRecognitionManagerService.getAuthenticatorId(this.mContext.getOpPackageName());
            } catch (Exception e) {
                Log.w(TAG, "Exception while get Authenticator Id: ", e);
                return 0L;
            }
        }
        return 0L;
    }

    public void resetTimeout(byte[] token) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                iFaceRecognitionManagerService.resetTimeout(token);
            } catch (Exception e) {
                Log.w(TAG, "Exception while reset Timeout: ", e);
            }
        }
    }

    public void addLockoutResetCallback(final LockoutResetCallback callback) {
        if (this.mService != null) {
            try {
                final PowerManager powerManager = (PowerManager) this.mContext.getSystemService(PowerManager.class);
                this.mService.addLockoutResetCallback(new IFaceRecognitionServiceLockoutResetCallback.Stub() { // from class: com.nt.facerecognition.manager.NtFaceRecognitionManager.1
                    @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceLockoutResetCallback
                    public void onLockoutReset(long deviceId, IRemoteCallback serverCallback) throws RemoteException {
                        try {
                            final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(1, "lockoutResetCallback");
                            wakeLock.setReferenceCounted(false);
                            wakeLock.acquire();
                            NtFaceRecognitionManager.this.mHandler.post(new Runnable() { // from class: com.nt.facerecognition.manager.NtFaceRecognitionManager.1.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    try {
                                        callback.onLockoutReset();
                                    } finally {
                                        wakeLock.release();
                                    }
                                }
                            });
                        } finally {
                            serverCallback.sendResult(null);
                        }
                    }
                });
            } catch (Exception e) {
                Log.w(TAG, "Exception while add Lockout ResetCallback: ", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class MyHandler extends Handler {
        private MyHandler(Context context) {
            super(context.getMainLooper());
        }

        private MyHandler(Looper looper) {
            super(looper);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Log.d(NtFaceRecognitionManager.TAG, "handleMessage  msg.what:" + msg.what);
            switch (msg.what) {
                case 100:
                    sendEnrollResult((FaceMetadata) msg.obj, msg.arg1);
                    return;
                case 101:
                    sendAcquiredResult(((Long) msg.obj).longValue(), msg.arg1, msg.arg2);
                    return;
                case 102:
                    sendAuthenticatedSucceeded((FaceMetadata) msg.obj, msg.arg1);
                    return;
                case 103:
                    sendAuthenticatedFailed(msg.arg1);
                    return;
                case 104:
                    sendErrorResult(((Long) msg.obj).longValue(), msg.arg1, msg.arg2);
                    return;
                case 105:
                    sendRemovedResult((FaceMetadata) msg.obj, msg.arg1);
                    return;
                case 106:
                    sendEnumeratedResult(((Long) msg.obj).longValue(), msg.arg1, msg.arg2);
                    break;
                case 107:
                    sendWarmUpHardwareDeviceResult((CameraPreviewProperty) msg.obj);
                    return;
                case 108:
                    break;
                case 109:
                    sendAuthenticatedTimeout();
                    return;
                case 110:
                    sendAuthenticatedFaceDetected(((Boolean) msg.obj).booleanValue());
                    return;
                default:
                    return;
            }
            sendScreenBrightnessOverride(msg.arg2, ((Float) msg.obj).floatValue());
        }

        private void sendRemovedResult(FaceMetadata faceMetadata, int remaining) {
            if (NtFaceRecognitionManager.this.mRemovalCallback != null) {
                if (faceMetadata == null || NtFaceRecognitionManager.this.mRemovalFaceMetadata == null) {
                    Log.d(NtFaceRecognitionManager.TAG, "Received MSG_REMOVED");
                    return;
                }
                int faceId = faceMetadata.getFaceId();
                int reqFaceId = NtFaceRecognitionManager.this.mRemovalFaceMetadata.getFaceId();
                if (reqFaceId != 0 && faceId != 0 && faceId != reqFaceId) {
                    Log.w(NtFaceRecognitionManager.TAG, "Face id didn't match: " + faceId + " != " + reqFaceId);
                    return;
                }
                int groupId = faceMetadata.getGroupId();
                int reqGroupId = NtFaceRecognitionManager.this.mRemovalFaceMetadata.getGroupId();
                if (groupId == reqGroupId) {
                    NtFaceRecognitionManager.this.mRemovalCallback.onRemovalSucceeded(faceMetadata, remaining);
                    return;
                }
                Log.w(NtFaceRecognitionManager.TAG, "Group id didn't match: " + groupId + " != " + reqGroupId);
            }
        }

        private void sendEnumeratedResult(long deviceId, int faceId, int groupId) {
            if (NtFaceRecognitionManager.this.mEnumerateCallback != null) {
                NtFaceRecognitionManager.this.mEnumerateCallback.onEnumerate(new FaceMetadata(null, groupId, faceId, deviceId));
            }
        }

        private void sendErrorResult(long deviceId, int errMsgId, int vendorCode) {
            int clientErrMsgId = errMsgId == 8 ? vendorCode + 1000 : errMsgId;
            if (NtFaceRecognitionManager.this.mEnrollmentCallback == null) {
                if (NtFaceRecognitionManager.this.mAuthenticationCallback == null) {
                    if (NtFaceRecognitionManager.this.mRemovalCallback == null) {
                        if (NtFaceRecognitionManager.this.mEnumerateCallback != null) {
                            NtFaceRecognitionManager.this.mEnumerateCallback.onEnumerateError(clientErrMsgId, NtFaceRecognitionManager.this.getErrorString(errMsgId, vendorCode));
                            return;
                        }
                        return;
                    }
                    NtFaceRecognitionManager.this.mRemovalCallback.onRemovalError(NtFaceRecognitionManager.this.mRemovalFaceMetadata, clientErrMsgId, NtFaceRecognitionManager.this.getErrorString(errMsgId, vendorCode));
                    return;
                }
                NtFaceRecognitionManager.this.mAuthenticationCallback.onAuthenticationError(clientErrMsgId, NtFaceRecognitionManager.this.getErrorString(errMsgId, vendorCode));
                return;
            }
            NtFaceRecognitionManager.this.mEnrollmentCallback.onEnrollmentError(clientErrMsgId, NtFaceRecognitionManager.this.getErrorString(errMsgId, vendorCode));
        }

        private void sendWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) {
            if (NtFaceRecognitionManager.this.mWarmUpHardwareDeviceCallback != null) {
                NtFaceRecognitionManager.this.mWarmUpHardwareDeviceCallback.onWarmUpHardwareDeviceResult(cameraPreviewProperty);
            }
        }

        private void sendScreenBrightnessOverride(int screenbrightnessvalue, float ambientLux) {
            if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                Log.w(NtFaceRecognitionManager.TAG, "sendScreenBrightnessOverride screenbrightnessvalue: " + screenbrightnessvalue + ",ambientLux:" + ambientLux);
                NtFaceRecognitionManager.this.mAuthenticationCallback.onScreenBrightnessOverride(screenbrightnessvalue, ambientLux);
            }
        }

        private void sendEnrollResult(FaceMetadata faceMetadata, int remaining) {
            if (NtFaceRecognitionManager.this.mEnrollmentCallback != null) {
                NtFaceRecognitionManager.this.mEnrollmentCallback.onEnrollmentProgress(remaining);
            }
        }

        private void sendAuthenticatedSucceeded(FaceMetadata faceMetadata, int userId) {
            if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                AuthenticationResult result = new AuthenticationResult(NtFaceRecognitionManager.this.mCryptoObject, faceMetadata, userId);
                NtFaceRecognitionManager.this.mAuthenticationCallback.onAuthenticationSucceeded(result);
            }
        }

        private void sendAuthenticatedFailed(int clientErrMsgId) {
            if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                NtFaceRecognitionManager.this.mAuthenticationCallback.onAuthenticationFailed(clientErrMsgId);
            }
        }

        private void sendAuthenticatedTimeout() {
            if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                NtFaceRecognitionManager.this.mAuthenticationCallback.onAuthenticationTimeout();
            }
        }

        private void sendAuthenticatedFaceDetected(boolean detected) {
            if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                NtFaceRecognitionManager.this.mAuthenticationCallback.onAuthenticationFaceDetected(detected);
            }
        }

        private void sendAcquiredResult(long deviceId, int acquireInfo, int vendorCode) {
            if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                NtFaceRecognitionManager.this.mAuthenticationCallback.onAuthenticationAcquired(acquireInfo);
            }
            String msg = NtFaceRecognitionManager.this.getAcquiredString(acquireInfo, vendorCode);
            if (msg == null) {
                return;
            }
            int clientInfo = acquireInfo == 6 ? vendorCode + 1000 : acquireInfo;
            if (NtFaceRecognitionManager.this.mEnrollmentCallback != null) {
                NtFaceRecognitionManager.this.mEnrollmentCallback.onEnrollmentHelp(clientInfo, msg);
            } else if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                NtFaceRecognitionManager.this.mAuthenticationCallback.onAuthenticationHelp(clientInfo, msg);
            }
        }
    }

    public NtFaceRecognitionManager(Context context, IFaceRecognitionManagerService service) {
        if (this.mService == null || context == null) {
            Slog.v(TAG, "NtFaceRecognitionManager was null");
        }
        this.mContext = context;
        this.mService = service;
        this.mHandler = new MyHandler(context);
    }

    private int getCurrentUserId() {
        try {
            return ActivityManagerNative.getDefault().getCurrentUser().id;
        } catch (Exception e) {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    /* JADX INFO: Access modifiers changed from: private */
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

    public String getErrorString(int msg) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getErrorString(int errMsg, int vendorCode) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getAcquiredString(int acquireInfo, int vendorCode) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService == null) {
            return null;
        }
        try {
            String reportString = iFaceRecognitionManagerService.getReportString(acquireInfo);
            return reportString;
        } catch (Exception e) {
            return null;
        }
    }
}
