package com.nothing.systemui.biometrics;

import android.content.Context;
import android.util.Log;
import android.view.OrientationEventListener;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.nothing.systemui.util.NTLogUtil;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AuthRippleControllerEx {
    private static final String TAG = "AuthRippleControllerEx";
    private boolean isKeyguardShow = false;
    private boolean isRotationLocked = false;
    private final RotationLockController.RotationLockControllerCallback mCallback = new RotationLockController.RotationLockControllerCallback() {
        public void onRotationLockStateChanged(boolean z, boolean z2) {
            AuthRippleControllerEx.this.updateOrientationEventListener();
        }
    };
    private Context mContext;
    /* access modifiers changed from: private */
    public int mOrientation = 0;
    private OrientationEventListener orientationEventListener;

    @Inject
    public AuthRippleControllerEx(Context context) {
        this.mContext = context;
    }

    public void init() {
        this.orientationEventListener = new OrientationEventListener(this.mContext) {
            public void onOrientationChanged(int i) {
                int unused = AuthRippleControllerEx.this.mOrientation = i;
                Log.d(AuthRippleControllerEx.TAG, "orientation = " + AuthRippleControllerEx.this.mOrientation);
            }
        };
        ((RotationLockController) Dependency.get(RotationLockController.class)).addCallback(this.mCallback);
    }

    public boolean isDeviceLandscape() {
        NTLogUtil.m1686d(TAG, " isDeviceLandscape:" + this.mOrientation);
        if (((RotationLockController) Dependency.get(RotationLockController.class)).isRotationLocked()) {
            return false;
        }
        int i = this.mOrientation;
        if ((i <= 45 || i >= 135) && (i <= 225 || i >= 315)) {
            return false;
        }
        return true;
    }

    public void onKeyguardShowingChanged() {
        updateOrientationEventListener();
    }

    /* access modifiers changed from: private */
    public void updateOrientationEventListener() {
        Log.d(TAG, "updateOrientationEventListener canDetectOrientation:" + this.orientationEventListener.canDetectOrientation() + " isRotationLocked:" + ((RotationLockController) Dependency.get(RotationLockController.class)).isRotationLocked() + " isKeyguardShow:" + ((KeyguardStateController) Dependency.get(KeyguardStateController.class)).isShowing());
        if (((KeyguardStateController) Dependency.get(KeyguardStateController.class)).isShowing()) {
            OrientationEventListener orientationEventListener2 = this.orientationEventListener;
            if (orientationEventListener2 != null && orientationEventListener2.canDetectOrientation()) {
                if (!((RotationLockController) Dependency.get(RotationLockController.class)).isRotationLocked()) {
                    this.orientationEventListener.enable();
                } else {
                    this.orientationEventListener.disable();
                }
            }
        } else {
            OrientationEventListener orientationEventListener3 = this.orientationEventListener;
            if (orientationEventListener3 != null) {
                orientationEventListener3.disable();
            }
        }
    }
}
