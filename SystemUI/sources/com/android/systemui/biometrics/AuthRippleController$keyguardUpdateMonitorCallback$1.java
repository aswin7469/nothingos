package com.android.systemui.biometrics;

import android.hardware.biometrics.BiometricFingerprintConstants;
import android.hardware.biometrics.BiometricSourceType;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0012\u0010\b\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\"\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u00072\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\fH\u0016J\u0010\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\fH\u0016¨\u0006\u000f"}, mo65043d2 = {"com/android/systemui/biometrics/AuthRippleController$keyguardUpdateMonitorCallback$1", "Lcom/android/keyguard/KeyguardUpdateMonitorCallback;", "onBiometricAcquired", "", "biometricSourceType", "Landroid/hardware/biometrics/BiometricSourceType;", "acquireInfo", "", "onBiometricAuthFailed", "onBiometricAuthenticated", "userId", "isStrongBiometric", "", "onKeyguardBouncerStateChanged", "bouncerIsOrWillBeShowing", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AuthRippleController.kt */
public final class AuthRippleController$keyguardUpdateMonitorCallback$1 extends KeyguardUpdateMonitorCallback {
    final /* synthetic */ AuthRippleController this$0;

    AuthRippleController$keyguardUpdateMonitorCallback$1(AuthRippleController authRippleController) {
        this.this$0 = authRippleController;
    }

    public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
        if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
            ((AuthRippleView) this.this$0.mView).fadeDwellRipple();
        }
        this.this$0.showUnlockRipple(biometricSourceType);
    }

    public void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
        if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
            ((AuthRippleView) this.this$0.mView).retractDwellRipple();
        }
    }

    public void onBiometricAcquired(BiometricSourceType biometricSourceType, int i) {
        if (biometricSourceType == BiometricSourceType.FINGERPRINT && BiometricFingerprintConstants.shouldTurnOffHbm(i) && i != 0) {
            ((AuthRippleView) this.this$0.mView).retractDwellRipple();
        }
    }

    public void onKeyguardBouncerStateChanged(boolean z) {
        if (z) {
            ((AuthRippleView) this.this$0.mView).fadeDwellRipple();
        }
    }
}
