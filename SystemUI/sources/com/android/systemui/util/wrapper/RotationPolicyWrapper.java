package com.android.systemui.util.wrapper;

import com.android.internal.view.RotationPolicy;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0005H&J\b\u0010\u0007\u001a\u00020\u0005H&J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0003H&J\u0010\u0010\r\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u0005H&J\u0018\u0010\u000f\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u0003H&J\u0010\u0010\u0011\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0012À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/util/wrapper/RotationPolicyWrapper;", "", "getRotationLockOrientation", "", "isCameraRotationEnabled", "", "isRotationLockToggleVisible", "isRotationLocked", "registerRotationPolicyListener", "", "listener", "Lcom/android/internal/view/RotationPolicy$RotationPolicyListener;", "userHandle", "setRotationLock", "enabled", "setRotationLockAtAngle", "rotation", "unregisterRotationPolicyListener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: RotationPolicyWrapper.kt */
public interface RotationPolicyWrapper {
    int getRotationLockOrientation();

    boolean isCameraRotationEnabled();

    boolean isRotationLockToggleVisible();

    boolean isRotationLocked();

    void registerRotationPolicyListener(RotationPolicy.RotationPolicyListener rotationPolicyListener, int i);

    void setRotationLock(boolean z);

    void setRotationLockAtAngle(boolean z, int i);

    void unregisterRotationPolicyListener(RotationPolicy.RotationPolicyListener rotationPolicyListener);
}
