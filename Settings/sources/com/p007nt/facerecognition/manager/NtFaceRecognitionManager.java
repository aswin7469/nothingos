package com.p007nt.facerecognition.manager;

import android.app.ActivityManagerNative;
import android.content.Context;
import android.graphics.Rect;
import android.os.Binder;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.util.Slog;
import android.view.Surface;
import com.p007nt.facerecognition.manager.IFaceRecognitionServiceReceiver;
import java.util.List;

/* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager */
public class NtFaceRecognitionManager {
    public AuthenticationCallback mAuthenticationCallback;
    private Context mContext;
    public EnrollmentCallback mEnrollmentCallback;
    public Handler mHandler;
    public RemovalCallback mRemovalCallback;
    public FaceMetadata mRemovalFaceMetadata;
    private IFaceRecognitionManagerService mService;
    private IFaceRecognitionServiceReceiver mServiceReceiver = new IFaceRecognitionServiceReceiver.Stub() {
        public void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(107, cameraPreviewProperty).sendToTarget();
        }

        public void onEnrollResult(long j, int i, int i2, int i3) {
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
            NtFaceRecognitionManager.this.mHandler.obtainMessage(105, i3, 0, new FaceMetadata((CharSequence) null, i2, i, j)).sendToTarget();
        }

        public void onEnumerated(long j, int i, int i2, int i3) {
            NtFaceRecognitionManager.this.mHandler.obtainMessage(106, i, i2, Long.valueOf(j)).sendToTarget();
        }

        public void onScreenBrightnessOverrided(int i, float f) {
            Log.w("NtFaceRecognitionManager", "mServiceReceiver screenbrightnessvalue: " + i + ",ambientLux:" + f);
            NtFaceRecognitionManager.this.mHandler.obtainMessage(108, 0, i, Float.valueOf(f)).sendToTarget();
        }
    };
    private IBinder mToken = new Binder();
    public WarmUpHardwareDeviceCallback mWarmUpHardwareDeviceCallback;

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$AuthenticationCallback */
    public static abstract class AuthenticationCallback {
        public void onAuthenticationAcquired(int i) {
        }

        public abstract void onAuthenticationError(int i, CharSequence charSequence);

        public void onAuthenticationFaceDetected(boolean z) {
        }

        public abstract void onAuthenticationFailed(int i);

        public abstract void onAuthenticationHelp(int i, CharSequence charSequence);

        public abstract void onAuthenticationSucceeded(AuthenticationResult authenticationResult);

        public abstract void onAuthenticationTimeout();

        public abstract void onScreenBrightnessOverride(int i, float f);
    }

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$CryptoObject */
    public static final class CryptoObject {
    }

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$EnrollmentCallback */
    public static abstract class EnrollmentCallback {
        public abstract void onEnrollmentError(int i, CharSequence charSequence);

        public abstract void onEnrollmentHelp(int i, CharSequence charSequence);

        public abstract void onEnrollmentProgress(int i);
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
        public abstract void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty);
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

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$AuthenticationResult */
    public static class AuthenticationResult {
        private FaceMetadata mFaceMetadata;
        private int mUserId;

        public AuthenticationResult(CryptoObject cryptoObject, FaceMetadata faceMetadata, int i) {
            this.mFaceMetadata = faceMetadata;
            this.mUserId = i;
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
            Log.w("NtFaceRecognitionManager", "Remote exception while WarmUp Hardware Device for Preview: ", e);
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
                    Log.w("NtFaceRecognitionManager", "enrollment already canceled");
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
                    Log.w("NtFaceRecognitionManager", "Remote exception in enroll: ", e);
                    enrollmentCallback.onEnrollmentError(1, getErrorString(1, 0));
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
            Log.w("NtFaceRecognitionManager", "Exception while Close Hardware Device: ", e);
            return false;
        }
    }

    public void remove(FaceMetadata faceMetadata, int i, RemovalCallback removalCallback) {
        IFaceRecognitionManagerService iFaceRecognitionManagerService = this.mService;
        if (iFaceRecognitionManagerService != null) {
            try {
                this.mRemovalCallback = removalCallback;
                this.mRemovalFaceMetadata = faceMetadata;
                iFaceRecognitionManagerService.remove(this.mToken, faceMetadata.getFaceId(), faceMetadata.getGroupId(), i, this.mServiceReceiver);
            } catch (Exception e) {
                Log.w("NtFaceRecognitionManager", "Remote exception in remove: ", e);
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
                Log.w("NtFaceRecognitionManager", "Remote exception in remove: ", e);
                if (removalCallback != null) {
                    removalCallback.onRemovalError((FaceMetadata) null, 1, getErrorString(1, 0));
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
                Log.w("NtFaceRecognitionManager", "Exception while rename: ", e);
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
            Log.w("NtFaceRecognitionManager", "Exception while get Enrolled FaceMetadatas: ", e);
            return null;
        }
    }

    /* renamed from: com.nt.facerecognition.manager.NtFaceRecognitionManager$MyHandler */
    private class MyHandler extends Handler {
        private MyHandler(Context context) {
            super(context.getMainLooper());
        }

        public void handleMessage(Message message) {
            Log.d("NtFaceRecognitionManager", "handleMessage  msg.what:" + message.what);
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
            NtFaceRecognitionManager ntFaceRecognitionManager = NtFaceRecognitionManager.this;
            if (ntFaceRecognitionManager.mRemovalCallback == null) {
                return;
            }
            if (faceMetadata == null || ntFaceRecognitionManager.mRemovalFaceMetadata == null) {
                Log.d("NtFaceRecognitionManager", "Received MSG_REMOVED");
                return;
            }
            int faceId = faceMetadata.getFaceId();
            int faceId2 = NtFaceRecognitionManager.this.mRemovalFaceMetadata.getFaceId();
            if (faceId2 == 0 || faceId == 0 || faceId == faceId2) {
                int groupId = faceMetadata.getGroupId();
                int groupId2 = NtFaceRecognitionManager.this.mRemovalFaceMetadata.getGroupId();
                if (groupId != groupId2) {
                    Log.w("NtFaceRecognitionManager", "Group id didn't match: " + groupId + " != " + groupId2);
                    return;
                }
                NtFaceRecognitionManager.this.mRemovalCallback.onRemovalSucceeded(faceMetadata, i);
                return;
            }
            Log.w("NtFaceRecognitionManager", "Face id didn't match: " + faceId + " != " + faceId2);
        }

        private void sendEnumeratedResult(long j, int i, int i2) {
            NtFaceRecognitionManager.this.getClass();
        }

        private void sendErrorResult(long j, int i, int i2) {
            int i3 = i == 8 ? i2 + 1000 : i;
            NtFaceRecognitionManager ntFaceRecognitionManager = NtFaceRecognitionManager.this;
            EnrollmentCallback enrollmentCallback = ntFaceRecognitionManager.mEnrollmentCallback;
            if (enrollmentCallback != null) {
                enrollmentCallback.onEnrollmentError(i3, ntFaceRecognitionManager.getErrorString(i, i2));
                return;
            }
            AuthenticationCallback authenticationCallback = ntFaceRecognitionManager.mAuthenticationCallback;
            if (authenticationCallback != null) {
                authenticationCallback.onAuthenticationError(i3, ntFaceRecognitionManager.getErrorString(i, i2));
                return;
            }
            RemovalCallback removalCallback = ntFaceRecognitionManager.mRemovalCallback;
            if (removalCallback != null) {
                removalCallback.onRemovalError(ntFaceRecognitionManager.mRemovalFaceMetadata, i3, ntFaceRecognitionManager.getErrorString(i, i2));
            } else {
                ntFaceRecognitionManager.getClass();
            }
        }

        private void sendWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) {
            WarmUpHardwareDeviceCallback warmUpHardwareDeviceCallback = NtFaceRecognitionManager.this.mWarmUpHardwareDeviceCallback;
            if (warmUpHardwareDeviceCallback != null) {
                warmUpHardwareDeviceCallback.onWarmUpHardwareDeviceResult(cameraPreviewProperty);
            }
        }

        private void sendScreenBrightnessOverride(int i, float f) {
            if (NtFaceRecognitionManager.this.mAuthenticationCallback != null) {
                Log.w("NtFaceRecognitionManager", "sendScreenBrightnessOverride screenbrightnessvalue: " + i + ",ambientLux:" + f);
                NtFaceRecognitionManager.this.mAuthenticationCallback.onScreenBrightnessOverride(i, f);
            }
        }

        private void sendEnrollResult(FaceMetadata faceMetadata, int i) {
            EnrollmentCallback enrollmentCallback = NtFaceRecognitionManager.this.mEnrollmentCallback;
            if (enrollmentCallback != null) {
                enrollmentCallback.onEnrollmentProgress(i);
            }
        }

        private void sendAuthenticatedSucceeded(FaceMetadata faceMetadata, int i) {
            NtFaceRecognitionManager ntFaceRecognitionManager = NtFaceRecognitionManager.this;
            AuthenticationCallback authenticationCallback = ntFaceRecognitionManager.mAuthenticationCallback;
            if (authenticationCallback != null) {
                ntFaceRecognitionManager.getClass();
                authenticationCallback.onAuthenticationSucceeded(new AuthenticationResult((CryptoObject) null, faceMetadata, i));
            }
        }

        private void sendAuthenticatedFailed(int i) {
            AuthenticationCallback authenticationCallback = NtFaceRecognitionManager.this.mAuthenticationCallback;
            if (authenticationCallback != null) {
                authenticationCallback.onAuthenticationFailed(i);
            }
        }

        private void sendAuthenticatedTimeout() {
            AuthenticationCallback authenticationCallback = NtFaceRecognitionManager.this.mAuthenticationCallback;
            if (authenticationCallback != null) {
                authenticationCallback.onAuthenticationTimeout();
            }
        }

        private void sendAuthenticatedFaceDetected(boolean z) {
            AuthenticationCallback authenticationCallback = NtFaceRecognitionManager.this.mAuthenticationCallback;
            if (authenticationCallback != null) {
                authenticationCallback.onAuthenticationFaceDetected(z);
            }
        }

        private void sendAcquiredResult(long j, int i, int i2) {
            AuthenticationCallback authenticationCallback = NtFaceRecognitionManager.this.mAuthenticationCallback;
            if (authenticationCallback != null) {
                authenticationCallback.onAuthenticationAcquired(i);
            }
            String acquiredString = NtFaceRecognitionManager.this.getAcquiredString(i, i2);
            if (acquiredString != null) {
                if (i == 6) {
                    i = i2 + 1000;
                }
                NtFaceRecognitionManager ntFaceRecognitionManager = NtFaceRecognitionManager.this;
                EnrollmentCallback enrollmentCallback = ntFaceRecognitionManager.mEnrollmentCallback;
                if (enrollmentCallback != null) {
                    enrollmentCallback.onEnrollmentHelp(i, acquiredString);
                    return;
                }
                AuthenticationCallback authenticationCallback2 = ntFaceRecognitionManager.mAuthenticationCallback;
                if (authenticationCallback2 != null) {
                    authenticationCallback2.onAuthenticationHelp(i, acquiredString);
                }
            }
        }
    }

    public NtFaceRecognitionManager(Context context, IFaceRecognitionManagerService iFaceRecognitionManagerService) {
        if (this.mService == null || context == null) {
            Slog.v("NtFaceRecognitionManager", "NtFaceRecognitionManager was null");
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
                Log.w("NtFaceRecognitionManager", "Exception while cancel Enrollment: ", e);
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
