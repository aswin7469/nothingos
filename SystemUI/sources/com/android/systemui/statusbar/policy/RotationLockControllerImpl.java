package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.pm.PackageManager;
import com.android.internal.view.RotationPolicy;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.policy.RotationLockController;
import com.android.systemui.util.wrapper.RotationPolicyWrapper;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.inject.Inject;
import javax.inject.Named;

@SysUISingleton
public final class RotationLockControllerImpl implements RotationLockController {
    private final CopyOnWriteArrayList<RotationLockController.RotationLockControllerCallback> mCallbacks;
    private final DeviceStateRotationLockSettingController mDeviceStateRotationLockSettingController;
    private final boolean mIsPerDeviceStateRotationLockEnabled;
    private final RotationPolicyWrapper mRotationPolicy;
    private final RotationPolicy.RotationPolicyListener mRotationPolicyListener = new RotationPolicy.RotationPolicyListener() {
        public void onChange() {
            RotationLockControllerImpl.this.notifyChanged();
        }
    };

    @Inject
    public RotationLockControllerImpl(RotationPolicyWrapper rotationPolicyWrapper, DeviceStateRotationLockSettingController deviceStateRotationLockSettingController, @Named("DEVICE_STATE_ROTATION_LOCK_DEFAULTS") String[] strArr) {
        CopyOnWriteArrayList<RotationLockController.RotationLockControllerCallback> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        this.mCallbacks = copyOnWriteArrayList;
        this.mRotationPolicy = rotationPolicyWrapper;
        this.mDeviceStateRotationLockSettingController = deviceStateRotationLockSettingController;
        boolean z = strArr.length > 0;
        this.mIsPerDeviceStateRotationLockEnabled = z;
        if (z) {
            copyOnWriteArrayList.add(deviceStateRotationLockSettingController);
        }
        setListening(true);
    }

    public void addCallback(RotationLockController.RotationLockControllerCallback rotationLockControllerCallback) {
        this.mCallbacks.add(rotationLockControllerCallback);
        notifyChanged(rotationLockControllerCallback);
    }

    public void removeCallback(RotationLockController.RotationLockControllerCallback rotationLockControllerCallback) {
        this.mCallbacks.remove((Object) rotationLockControllerCallback);
    }

    public int getRotationLockOrientation() {
        return this.mRotationPolicy.getRotationLockOrientation();
    }

    public boolean isRotationLocked() {
        return this.mRotationPolicy.isRotationLocked();
    }

    public boolean isCameraRotationEnabled() {
        return this.mRotationPolicy.isCameraRotationEnabled();
    }

    public void setRotationLocked(boolean z) {
        this.mRotationPolicy.setRotationLock(z);
    }

    public void setRotationLockedAtAngle(boolean z, int i) {
        this.mRotationPolicy.setRotationLockAtAngle(z, i);
    }

    public boolean isRotationLockAffordanceVisible() {
        return this.mRotationPolicy.isRotationLockToggleVisible();
    }

    public void setListening(boolean z) {
        if (z) {
            this.mRotationPolicy.registerRotationPolicyListener(this.mRotationPolicyListener, -1);
        } else {
            this.mRotationPolicy.unregisterRotationPolicyListener(this.mRotationPolicyListener);
        }
        if (this.mIsPerDeviceStateRotationLockEnabled) {
            this.mDeviceStateRotationLockSettingController.setListening(z);
        }
    }

    /* access modifiers changed from: private */
    public void notifyChanged() {
        Iterator<RotationLockController.RotationLockControllerCallback> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            notifyChanged(it.next());
        }
    }

    private void notifyChanged(RotationLockController.RotationLockControllerCallback rotationLockControllerCallback) {
        rotationLockControllerCallback.onRotationLockStateChanged(this.mRotationPolicy.isRotationLocked(), this.mRotationPolicy.isRotationLockToggleVisible());
    }

    public static boolean hasSufficientPermission(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String rotationResolverPackageName = packageManager.getRotationResolverPackageName();
        return rotationResolverPackageName != null && packageManager.checkPermission("android.permission.CAMERA", rotationResolverPackageName) == 0;
    }
}
