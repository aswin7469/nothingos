package com.android.keyguard;

import android.app.admin.IKeyguardCallback;
import android.app.admin.IKeyguardClient;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.dagger.KeyguardBouncerScope;
import com.android.systemui.dagger.qualifiers.Main;
import java.util.NoSuchElementException;
import javax.inject.Inject;

public class AdminSecondaryLockScreenController {
    private static final int REMOTE_CONTENT_READY_TIMEOUT_MILLIS = 500;
    private static final String TAG = "AdminSecondaryLockScreenController";
    private final IKeyguardCallback mCallback;
    /* access modifiers changed from: private */
    public IKeyguardClient mClient;
    private final ServiceConnection mConnection;
    private final Context mContext;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private KeyguardSecurityCallback mKeyguardCallback;
    /* access modifiers changed from: private */
    public final IBinder.DeathRecipient mKeyguardClientDeathRecipient;
    private final ViewGroup mParent;
    protected SurfaceHolder.Callback mSurfaceHolderCallback;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitorCallback mUpdateCallback;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitor mUpdateMonitor;
    /* access modifiers changed from: private */
    public AdminSecurityView mView;

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-keyguard-AdminSecondaryLockScreenController */
    public /* synthetic */ void mo25483x660dd360() {
        hide();
        Log.d(TAG, "KeyguardClient service died");
    }

    private AdminSecondaryLockScreenController(Context context, KeyguardSecurityContainer keyguardSecurityContainer, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityCallback keyguardSecurityCallback, @Main Handler handler) {
        this.mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                IKeyguardClient unused = AdminSecondaryLockScreenController.this.mClient = IKeyguardClient.Stub.asInterface(iBinder);
                if (AdminSecondaryLockScreenController.this.mView.isAttachedToWindow() && AdminSecondaryLockScreenController.this.mClient != null) {
                    AdminSecondaryLockScreenController.this.onSurfaceReady();
                    try {
                        iBinder.linkToDeath(AdminSecondaryLockScreenController.this.mKeyguardClientDeathRecipient, 0);
                    } catch (RemoteException e) {
                        Log.e(AdminSecondaryLockScreenController.TAG, "Lost connection to secondary lockscreen service", e);
                        AdminSecondaryLockScreenController.this.dismiss(KeyguardUpdateMonitor.getCurrentUser());
                    }
                }
            }

            public void onServiceDisconnected(ComponentName componentName) {
                IKeyguardClient unused = AdminSecondaryLockScreenController.this.mClient = null;
            }
        };
        this.mKeyguardClientDeathRecipient = new AdminSecondaryLockScreenController$$ExternalSyntheticLambda0(this);
        this.mCallback = new IKeyguardCallback.Stub() {
            public void onDismiss() {
                AdminSecondaryLockScreenController.this.mHandler.post(new AdminSecondaryLockScreenController$2$$ExternalSyntheticLambda0(this));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onDismiss$0$com-android-keyguard-AdminSecondaryLockScreenController$2 */
            public /* synthetic */ void mo25487x11024939() {
                AdminSecondaryLockScreenController.this.dismiss(UserHandle.getCallingUserId());
            }

            public void onRemoteContentReady(SurfaceControlViewHost.SurfacePackage surfacePackage) {
                if (AdminSecondaryLockScreenController.this.mHandler != null) {
                    AdminSecondaryLockScreenController.this.mHandler.removeCallbacksAndMessages((Object) null);
                }
                if (surfacePackage != null) {
                    AdminSecondaryLockScreenController.this.mView.setChildSurfacePackage(surfacePackage);
                } else {
                    AdminSecondaryLockScreenController.this.mHandler.post(new AdminSecondaryLockScreenController$2$$ExternalSyntheticLambda1(this));
                }
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onRemoteContentReady$1$com-android-keyguard-AdminSecondaryLockScreenController$2 */
            public /* synthetic */ void mo25488x7ac332f8() {
                AdminSecondaryLockScreenController.this.dismiss(KeyguardUpdateMonitor.getCurrentUser());
            }
        };
        this.mUpdateCallback = new KeyguardUpdateMonitorCallback() {
            public void onSecondaryLockscreenRequirementChanged(int i) {
                if (AdminSecondaryLockScreenController.this.mUpdateMonitor.getSecondaryLockscreenRequirement(i) == null) {
                    AdminSecondaryLockScreenController.this.dismiss(i);
                }
            }
        };
        this.mSurfaceHolderCallback = new SurfaceHolder.Callback() {
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            }

            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                int currentUser = KeyguardUpdateMonitor.getCurrentUser();
                AdminSecondaryLockScreenController.this.mUpdateMonitor.registerCallback(AdminSecondaryLockScreenController.this.mUpdateCallback);
                if (AdminSecondaryLockScreenController.this.mClient != null) {
                    AdminSecondaryLockScreenController.this.onSurfaceReady();
                }
                AdminSecondaryLockScreenController.this.mHandler.postDelayed(new AdminSecondaryLockScreenController$4$$ExternalSyntheticLambda0(this, currentUser), 500);
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$surfaceCreated$0$com-android-keyguard-AdminSecondaryLockScreenController$4 */
            public /* synthetic */ void mo25492x79808f25(int i) {
                AdminSecondaryLockScreenController.this.dismiss(i);
                Log.w(AdminSecondaryLockScreenController.TAG, "Timed out waiting for secondary lockscreen content.");
            }

            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                AdminSecondaryLockScreenController.this.mUpdateMonitor.removeCallback(AdminSecondaryLockScreenController.this.mUpdateCallback);
            }
        };
        this.mContext = context;
        this.mHandler = handler;
        this.mParent = keyguardSecurityContainer;
        this.mUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardCallback = keyguardSecurityCallback;
        this.mView = new AdminSecurityView(context, this.mSurfaceHolderCallback);
    }

    public void show(Intent intent) {
        if (this.mClient == null) {
            this.mContext.bindService(intent, this.mConnection, 1);
        }
        if (!this.mView.isAttachedToWindow()) {
            this.mParent.addView(this.mView);
        }
    }

    public void hide() {
        if (this.mView.isAttachedToWindow()) {
            this.mParent.removeView(this.mView);
        }
        IKeyguardClient iKeyguardClient = this.mClient;
        if (iKeyguardClient != null) {
            try {
                iKeyguardClient.asBinder().unlinkToDeath(this.mKeyguardClientDeathRecipient, 0);
            } catch (NoSuchElementException unused) {
                Log.w(TAG, "IKeyguardClient death recipient already released");
            }
            this.mContext.unbindService(this.mConnection);
            this.mClient = null;
        }
    }

    /* access modifiers changed from: private */
    public void onSurfaceReady() {
        try {
            IBinder hostToken = this.mView.getHostToken();
            if (hostToken != null) {
                this.mClient.onCreateKeyguardSurface(hostToken, this.mCallback);
            } else {
                hide();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Error in onCreateKeyguardSurface", e);
            dismiss(KeyguardUpdateMonitor.getCurrentUser());
        }
    }

    /* access modifiers changed from: private */
    public void dismiss(int i) {
        this.mHandler.removeCallbacksAndMessages((Object) null);
        if (this.mView.isAttachedToWindow() && i == KeyguardUpdateMonitor.getCurrentUser()) {
            hide();
            KeyguardSecurityCallback keyguardSecurityCallback = this.mKeyguardCallback;
            if (keyguardSecurityCallback != null) {
                keyguardSecurityCallback.dismiss(true, i, true, KeyguardSecurityModel.SecurityMode.Invalid);
            }
        }
    }

    private class AdminSecurityView extends SurfaceView {
        private SurfaceHolder.Callback mSurfaceHolderCallback;

        AdminSecurityView(Context context, SurfaceHolder.Callback callback) {
            super(context);
            this.mSurfaceHolderCallback = callback;
            setZOrderOnTop(true);
        }

        /* access modifiers changed from: protected */
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            getHolder().addCallback(this.mSurfaceHolderCallback);
        }

        /* access modifiers changed from: protected */
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            getHolder().removeCallback(this.mSurfaceHolderCallback);
        }
    }

    @KeyguardBouncerScope
    public static class Factory {
        private final Context mContext;
        private final Handler mHandler;
        private final KeyguardSecurityContainer mParent;
        private final KeyguardUpdateMonitor mUpdateMonitor;

        @Inject
        public Factory(Context context, KeyguardSecurityContainer keyguardSecurityContainer, KeyguardUpdateMonitor keyguardUpdateMonitor, @Main Handler handler) {
            this.mContext = context;
            this.mParent = keyguardSecurityContainer;
            this.mUpdateMonitor = keyguardUpdateMonitor;
            this.mHandler = handler;
        }

        public AdminSecondaryLockScreenController create(KeyguardSecurityCallback keyguardSecurityCallback) {
            return new AdminSecondaryLockScreenController(this.mContext, this.mParent, this.mUpdateMonitor, keyguardSecurityCallback, this.mHandler);
        }
    }
}
