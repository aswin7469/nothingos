package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.hardware.biometrics.BiometricSourceType;
import com.android.systemui.Dumpable;
import com.android.systemui.R$integer;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.tuner.TunerService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: KeyguardBypassController.kt */
/* loaded from: classes.dex */
public class KeyguardBypassController implements Dumpable, StackScrollAlgorithm.BypassController {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private boolean altBouncerShowing;
    private boolean bouncerShowing;
    private boolean bypassEnabled;
    private final int bypassOverride;
    private boolean hasFaceFeature;
    private boolean isPulseExpanding;
    private boolean launchingAffordance;
    @NotNull
    private final KeyguardStateController mKeyguardStateController;
    @Nullable
    private PendingUnlock pendingUnlock;
    private boolean qSExpanded;
    @NotNull
    private final StatusBarStateController statusBarStateController;
    public BiometricUnlockController unlockController;
    private boolean userHasDeviceEntryIntent;

    public final boolean getUserHasDeviceEntryIntent() {
        return this.userHasDeviceEntryIntent;
    }

    public final void setUserHasDeviceEntryIntent(boolean z) {
        this.userHasDeviceEntryIntent = z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: KeyguardBypassController.kt */
    /* loaded from: classes.dex */
    public static final class PendingUnlock {
        private final boolean isStrongBiometric;
        @NotNull
        private final BiometricSourceType pendingUnlockType;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PendingUnlock)) {
                return false;
            }
            PendingUnlock pendingUnlock = (PendingUnlock) obj;
            return this.pendingUnlockType == pendingUnlock.pendingUnlockType && this.isStrongBiometric == pendingUnlock.isStrongBiometric;
        }

        public int hashCode() {
            int hashCode = this.pendingUnlockType.hashCode() * 31;
            boolean z = this.isStrongBiometric;
            if (z) {
                z = true;
            }
            int i = z ? 1 : 0;
            int i2 = z ? 1 : 0;
            return hashCode + i;
        }

        @NotNull
        public String toString() {
            return "PendingUnlock(pendingUnlockType=" + this.pendingUnlockType + ", isStrongBiometric=" + this.isStrongBiometric + ')';
        }

        public PendingUnlock(@NotNull BiometricSourceType pendingUnlockType, boolean z) {
            Intrinsics.checkNotNullParameter(pendingUnlockType, "pendingUnlockType");
            this.pendingUnlockType = pendingUnlockType;
            this.isStrongBiometric = z;
        }

        @NotNull
        public final BiometricSourceType getPendingUnlockType() {
            return this.pendingUnlockType;
        }

        public final boolean isStrongBiometric() {
            return this.isStrongBiometric;
        }
    }

    @NotNull
    public final BiometricUnlockController getUnlockController() {
        BiometricUnlockController biometricUnlockController = this.unlockController;
        if (biometricUnlockController != null) {
            return biometricUnlockController;
        }
        Intrinsics.throwUninitializedPropertyAccessException("unlockController");
        throw null;
    }

    public final void setUnlockController(@NotNull BiometricUnlockController biometricUnlockController) {
        Intrinsics.checkNotNullParameter(biometricUnlockController, "<set-?>");
        this.unlockController = biometricUnlockController;
    }

    public final void setPulseExpanding(boolean z) {
        this.isPulseExpanding = z;
    }

    @Override // com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm.BypassController
    public boolean isBypassEnabled() {
        return getBypassEnabled();
    }

    public final boolean getBypassEnabled() {
        boolean z;
        int i = this.bypassOverride;
        if (i != 1) {
            z = i != 2 ? this.bypassEnabled : false;
        } else {
            z = true;
        }
        return z && this.mKeyguardStateController.isFaceAuthEnabled();
    }

    public final void setBouncerShowing(boolean z) {
        this.bouncerShowing = z;
    }

    public final boolean getAltBouncerShowing() {
        return this.altBouncerShowing;
    }

    public final void setAltBouncerShowing(boolean z) {
        this.altBouncerShowing = z;
    }

    public final void setLaunchingAffordance(boolean z) {
        this.launchingAffordance = z;
    }

    public final void setQSExpanded(boolean z) {
        boolean z2 = this.qSExpanded != z;
        this.qSExpanded = z;
        if (!z2 || z) {
            return;
        }
        maybePerformPendingUnlock();
    }

    public KeyguardBypassController(@NotNull Context context, @NotNull final TunerService tunerService, @NotNull StatusBarStateController statusBarStateController, @NotNull NotificationLockscreenUserManager lockscreenUserManager, @NotNull KeyguardStateController keyguardStateController, @NotNull DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(tunerService, "tunerService");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(lockscreenUserManager, "lockscreenUserManager");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.mKeyguardStateController = keyguardStateController;
        this.statusBarStateController = statusBarStateController;
        this.bypassOverride = context.getResources().getInteger(R$integer.config_face_unlock_bypass_override);
        boolean hasSystemFeature = context.getPackageManager().hasSystemFeature("android.hardware.biometrics.face");
        this.hasFaceFeature = hasSystemFeature;
        if (!hasSystemFeature) {
            return;
        }
        dumpManager.registerDumpable("KeyguardBypassController", this);
        statusBarStateController.addCallback(new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.phone.KeyguardBypassController.1
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onStateChanged(int i) {
                if (i != 1) {
                    KeyguardBypassController.this.pendingUnlock = null;
                }
            }
        });
        final int i = context.getResources().getBoolean(17891556) ? 1 : 0;
        tunerService.addTunable(new TunerService.Tunable() { // from class: com.android.systemui.statusbar.phone.KeyguardBypassController.2
            @Override // com.android.systemui.tuner.TunerService.Tunable
            public void onTuningChanged(@Nullable String str, @Nullable String str2) {
                KeyguardBypassController.this.bypassEnabled = tunerService.getValue(str, i) != 0;
            }
        }, "face_unlock_dismisses_keyguard");
        lockscreenUserManager.addUserChangedListener(new NotificationLockscreenUserManager.UserChangedListener() { // from class: com.android.systemui.statusbar.phone.KeyguardBypassController.3
            @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager.UserChangedListener
            public void onUserChanged(int i2) {
                KeyguardBypassController.this.pendingUnlock = null;
            }
        });
    }

    public final boolean onBiometricAuthenticated(@NotNull BiometricSourceType biometricSourceType, boolean z) {
        Intrinsics.checkNotNullParameter(biometricSourceType, "biometricSourceType");
        if (biometricSourceType != BiometricSourceType.FACE || !getBypassEnabled()) {
            return true;
        }
        boolean canBypass = canBypass();
        if (!canBypass && (this.isPulseExpanding || this.qSExpanded)) {
            this.pendingUnlock = new PendingUnlock(biometricSourceType, z);
        }
        return canBypass;
    }

    public final void maybePerformPendingUnlock() {
        PendingUnlock pendingUnlock = this.pendingUnlock;
        if (pendingUnlock != null) {
            Intrinsics.checkNotNull(pendingUnlock);
            BiometricSourceType pendingUnlockType = pendingUnlock.getPendingUnlockType();
            PendingUnlock pendingUnlock2 = this.pendingUnlock;
            Intrinsics.checkNotNull(pendingUnlock2);
            if (!onBiometricAuthenticated(pendingUnlockType, pendingUnlock2.isStrongBiometric())) {
                return;
            }
            BiometricUnlockController unlockController = getUnlockController();
            PendingUnlock pendingUnlock3 = this.pendingUnlock;
            Intrinsics.checkNotNull(pendingUnlock3);
            BiometricSourceType pendingUnlockType2 = pendingUnlock3.getPendingUnlockType();
            PendingUnlock pendingUnlock4 = this.pendingUnlock;
            Intrinsics.checkNotNull(pendingUnlock4);
            unlockController.startWakeAndUnlock(pendingUnlockType2, pendingUnlock4.isStrongBiometric());
            this.pendingUnlock = null;
        }
    }

    public final boolean canBypass() {
        if (getBypassEnabled()) {
            return this.bouncerShowing || this.altBouncerShowing || (this.statusBarStateController.getState() == 1 && !this.launchingAffordance && !this.isPulseExpanding && !this.qSExpanded);
        }
        return false;
    }

    public final boolean canPlaySubtleWindowAnimations() {
        return getBypassEnabled() && this.statusBarStateController.getState() == 1 && !this.qSExpanded;
    }

    public final void onStartedGoingToSleep() {
        this.pendingUnlock = null;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println("KeyguardBypassController:");
        PendingUnlock pendingUnlock = this.pendingUnlock;
        if (pendingUnlock != null) {
            Intrinsics.checkNotNull(pendingUnlock);
            pw.println(Intrinsics.stringPlus("  mPendingUnlock.pendingUnlockType: ", pendingUnlock.getPendingUnlockType()));
            PendingUnlock pendingUnlock2 = this.pendingUnlock;
            Intrinsics.checkNotNull(pendingUnlock2);
            pw.println(Intrinsics.stringPlus("  mPendingUnlock.isStrongBiometric: ", Boolean.valueOf(pendingUnlock2.isStrongBiometric())));
        } else {
            pw.println(Intrinsics.stringPlus("  mPendingUnlock: ", pendingUnlock));
        }
        pw.println(Intrinsics.stringPlus("  bypassEnabled: ", Boolean.valueOf(getBypassEnabled())));
        pw.println(Intrinsics.stringPlus("  canBypass: ", Boolean.valueOf(canBypass())));
        pw.println(Intrinsics.stringPlus("  bouncerShowing: ", Boolean.valueOf(this.bouncerShowing)));
        pw.println(Intrinsics.stringPlus("  altBouncerShowing: ", Boolean.valueOf(this.altBouncerShowing)));
        pw.println(Intrinsics.stringPlus("  isPulseExpanding: ", Boolean.valueOf(this.isPulseExpanding)));
        pw.println(Intrinsics.stringPlus("  launchingAffordance: ", Boolean.valueOf(this.launchingAffordance)));
        pw.println(Intrinsics.stringPlus("  qSExpanded: ", Boolean.valueOf(this.qSExpanded)));
        pw.println(Intrinsics.stringPlus("  hasFaceFeature: ", Boolean.valueOf(this.hasFaceFeature)));
        pw.println(Intrinsics.stringPlus("  userHasDeviceEntryIntent: ", Boolean.valueOf(this.userHasDeviceEntryIntent)));
    }

    /* compiled from: KeyguardBypassController.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
