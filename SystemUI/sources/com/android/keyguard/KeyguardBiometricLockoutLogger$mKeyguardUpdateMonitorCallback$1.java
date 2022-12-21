package com.android.keyguard;

import android.hardware.biometrics.BiometricSourceType;
import com.android.keyguard.KeyguardBiometricLockoutLogger;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, mo64987d2 = {"com/android/keyguard/KeyguardBiometricLockoutLogger$mKeyguardUpdateMonitorCallback$1", "Lcom/android/keyguard/KeyguardUpdateMonitorCallback;", "onLockedOutStateChanged", "", "biometricSourceType", "Landroid/hardware/biometrics/BiometricSourceType;", "onStrongAuthStateChanged", "userId", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: KeyguardBiometricLockoutLogger.kt */
public final class KeyguardBiometricLockoutLogger$mKeyguardUpdateMonitorCallback$1 extends KeyguardUpdateMonitorCallback {
    final /* synthetic */ KeyguardBiometricLockoutLogger this$0;

    KeyguardBiometricLockoutLogger$mKeyguardUpdateMonitorCallback$1(KeyguardBiometricLockoutLogger keyguardBiometricLockoutLogger) {
        this.this$0 = keyguardBiometricLockoutLogger;
    }

    public void onLockedOutStateChanged(BiometricSourceType biometricSourceType) {
        Intrinsics.checkNotNullParameter(biometricSourceType, "biometricSourceType");
        if (biometricSourceType == BiometricSourceType.FINGERPRINT) {
            boolean isFingerprintLockedOut = this.this$0.keyguardUpdateMonitor.isFingerprintLockedOut();
            if (isFingerprintLockedOut && !this.this$0.fingerprintLockedOut) {
                this.this$0.log(KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_FINGERPRINT_LOCKED_OUT);
            } else if (!isFingerprintLockedOut && this.this$0.fingerprintLockedOut) {
                this.this$0.log(KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_FINGERPRINT_LOCKED_OUT_RESET);
            }
            this.this$0.fingerprintLockedOut = isFingerprintLockedOut;
        } else if (biometricSourceType == BiometricSourceType.FACE) {
            boolean isFaceLockedOut = this.this$0.keyguardUpdateMonitor.isFaceLockedOut();
            if (isFaceLockedOut && !this.this$0.faceLockedOut) {
                this.this$0.log(KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_FACE_LOCKED_OUT);
            } else if (!isFaceLockedOut && this.this$0.faceLockedOut) {
                this.this$0.log(KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_FACE_LOCKED_OUT_RESET);
            }
            this.this$0.faceLockedOut = isFaceLockedOut;
        }
    }

    public void onStrongAuthStateChanged(int i) {
        if (i == KeyguardUpdateMonitor.getCurrentUser()) {
            int strongAuthForUser = this.this$0.keyguardUpdateMonitor.getStrongAuthTracker().getStrongAuthForUser(i);
            boolean isEncryptedOrLockdown = this.this$0.keyguardUpdateMonitor.isEncryptedOrLockdown(i);
            if (isEncryptedOrLockdown && !this.this$0.encryptedOrLockdown) {
                this.this$0.log(KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_ENCRYPTED_OR_LOCKDOWN);
            }
            this.this$0.encryptedOrLockdown = isEncryptedOrLockdown;
            boolean access$isUnattendedUpdate = this.this$0.isUnattendedUpdate(strongAuthForUser);
            if (access$isUnattendedUpdate && !this.this$0.unattendedUpdate) {
                this.this$0.log(KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_UNATTENDED_UPDATE);
            }
            this.this$0.unattendedUpdate = access$isUnattendedUpdate;
            boolean access$isStrongAuthTimeout = this.this$0.isStrongAuthTimeout(strongAuthForUser);
            if (access$isStrongAuthTimeout && !this.this$0.timeout) {
                this.this$0.log(KeyguardBiometricLockoutLogger.PrimaryAuthRequiredEvent.PRIMARY_AUTH_REQUIRED_TIMEOUT);
            }
            this.this$0.timeout = access$isStrongAuthTimeout;
        }
    }
}
