package com.android.systemui.util.wrapper;

import android.content.Context;
import android.os.Trace;
import com.android.internal.view.RotationPolicy;
import com.android.systemui.util.settings.SecureSettings;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\nH\u0016J\b\u0010\u000b\u001a\u00020\nH\u0016J\b\u0010\f\u001a\u00020\nH\u0016J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\bH\u0016J\u0010\u0010\u0012\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\nH\u0016J\u0018\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\bH\u0016J\u0010\u0010\u0016\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0017"}, mo65043d2 = {"Lcom/android/systemui/util/wrapper/RotationPolicyWrapperImpl;", "Lcom/android/systemui/util/wrapper/RotationPolicyWrapper;", "context", "Landroid/content/Context;", "secureSettings", "Lcom/android/systemui/util/settings/SecureSettings;", "(Landroid/content/Context;Lcom/android/systemui/util/settings/SecureSettings;)V", "getRotationLockOrientation", "", "isCameraRotationEnabled", "", "isRotationLockToggleVisible", "isRotationLocked", "registerRotationPolicyListener", "", "listener", "Lcom/android/internal/view/RotationPolicy$RotationPolicyListener;", "userHandle", "setRotationLock", "enabled", "setRotationLockAtAngle", "rotation", "unregisterRotationPolicyListener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: RotationPolicyWrapper.kt */
public final class RotationPolicyWrapperImpl implements RotationPolicyWrapper {
    private final Context context;
    private final SecureSettings secureSettings;

    @Inject
    public RotationPolicyWrapperImpl(Context context2, SecureSettings secureSettings2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(secureSettings2, "secureSettings");
        this.context = context2;
        this.secureSettings = secureSettings2;
    }

    public void setRotationLockAtAngle(boolean z, int i) {
        RotationPolicy.setRotationLockAtAngle(this.context, z, i);
    }

    public int getRotationLockOrientation() {
        return RotationPolicy.getRotationLockOrientation(this.context);
    }

    public boolean isRotationLockToggleVisible() {
        return RotationPolicy.isRotationLockToggleVisible(this.context);
    }

    public boolean isRotationLocked() {
        return RotationPolicy.isRotationLocked(this.context);
    }

    public boolean isCameraRotationEnabled() {
        return this.secureSettings.getInt("camera_autorotate", 0) == 1;
    }

    public void registerRotationPolicyListener(RotationPolicy.RotationPolicyListener rotationPolicyListener, int i) {
        Intrinsics.checkNotNullParameter(rotationPolicyListener, "listener");
        RotationPolicy.registerRotationPolicyListener(this.context, rotationPolicyListener, i);
    }

    public void unregisterRotationPolicyListener(RotationPolicy.RotationPolicyListener rotationPolicyListener) {
        Intrinsics.checkNotNullParameter(rotationPolicyListener, "listener");
        RotationPolicy.unregisterRotationPolicyListener(this.context, rotationPolicyListener);
    }

    public void setRotationLock(boolean z) {
        Trace.beginSection("RotationPolicyWrapperImpl#setRotationLock");
        try {
            RotationPolicy.setRotationLock(this.context, z);
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }
}
