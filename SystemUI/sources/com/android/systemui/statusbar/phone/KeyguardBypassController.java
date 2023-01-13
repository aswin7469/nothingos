package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.hardware.biometrics.BiometricSourceType;
import com.android.systemui.C1894R;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.tuner.TunerService;
import java.lang.annotation.RetentionPolicy;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\b\t\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\n*\u0001\"\b\u0017\u0018\u0000 R2\u00020\u00012\u00020\u0002:\u0004QRSTB7\b\u0017\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e¢\u0006\u0002\u0010\u000fJ\u0006\u0010<\u001a\u00020\u0011J\u0006\u0010=\u001a\u00020\u0011J%\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020A2\u000e\u0010B\u001a\n\u0012\u0006\b\u0001\u0012\u00020D0CH\u0016¢\u0006\u0002\u0010EJ\b\u0010F\u001a\u00020\u0011H\u0016J\u0006\u0010G\u001a\u00020?J\b\u0010H\u001a\u00020?H\u0002J\u0016\u0010I\u001a\u00020\u00112\u0006\u0010J\u001a\u00020K2\u0006\u0010L\u001a\u00020\u0011J\u0006\u0010M\u001a\u00020?J\u000e\u0010N\u001a\u00020?2\u0006\u0010O\u001a\u00020,J\u000e\u0010P\u001a\u00020?2\u0006\u0010O\u001a\u00020,R\u001a\u0010\u0010\u001a\u00020\u0011X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u0011X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0013\"\u0004\b\u0018\u0010\u0015R&\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u00118F@BX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0013\"\u0004\b\u001c\u0010\u0015R\u0014\u0010\u001d\u001a\u00020\u001eX\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u001f\u0010 R\u0010\u0010!\u001a\u00020\"X\u0004¢\u0006\u0004\n\u0002\u0010#R\u000e\u0010$\u001a\u00020\u0011X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010%\u001a\u00020\u0011X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0013\"\u0004\b&\u0010\u0015R\u001a\u0010'\u001a\u00020\u0011X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0013\"\u0004\b)\u0010\u0015R\u0014\u0010*\u001a\b\u0012\u0004\u0012\u00020,0+X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010.\u001a\u0004\u0018\u00010/X\u000e¢\u0006\u0002\n\u0000R$\u00100\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u0011@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\u0013\"\u0004\b2\u0010\u0015R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u001a\u00103\u001a\u000204X.¢\u0006\u000e\n\u0000\u001a\u0004\b5\u00106\"\u0004\b7\u00108R\u001a\u00109\u001a\u00020\u0011X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b:\u0010\u0013\"\u0004\b;\u0010\u0015¨\u0006U"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/KeyguardBypassController;", "Lcom/android/systemui/Dumpable;", "Lcom/android/systemui/statusbar/notification/stack/StackScrollAlgorithm$BypassController;", "context", "Landroid/content/Context;", "tunerService", "Lcom/android/systemui/tuner/TunerService;", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "lockscreenUserManager", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Landroid/content/Context;Lcom/android/systemui/tuner/TunerService;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;Lcom/android/systemui/statusbar/policy/KeyguardStateController;Lcom/android/systemui/dump/DumpManager;)V", "altBouncerShowing", "", "getAltBouncerShowing", "()Z", "setAltBouncerShowing", "(Z)V", "bouncerShowing", "getBouncerShowing", "setBouncerShowing", "value", "bypassEnabled", "getBypassEnabled", "setBypassEnabled", "bypassOverride", "", "getBypassOverride$annotations", "()V", "faceAuthEnabledChangedCallback", "com/android/systemui/statusbar/phone/KeyguardBypassController$faceAuthEnabledChangedCallback$1", "Lcom/android/systemui/statusbar/phone/KeyguardBypassController$faceAuthEnabledChangedCallback$1;", "hasFaceFeature", "isPulseExpanding", "setPulseExpanding", "launchingAffordance", "getLaunchingAffordance", "setLaunchingAffordance", "listeners", "", "Lcom/android/systemui/statusbar/phone/KeyguardBypassController$OnBypassStateChangedListener;", "mKeyguardStateController", "pendingUnlock", "Lcom/android/systemui/statusbar/phone/KeyguardBypassController$PendingUnlock;", "qSExpanded", "getQSExpanded", "setQSExpanded", "unlockController", "Lcom/android/systemui/statusbar/phone/BiometricUnlockController;", "getUnlockController", "()Lcom/android/systemui/statusbar/phone/BiometricUnlockController;", "setUnlockController", "(Lcom/android/systemui/statusbar/phone/BiometricUnlockController;)V", "userHasDeviceEntryIntent", "getUserHasDeviceEntryIntent", "setUserHasDeviceEntryIntent", "canBypass", "canPlaySubtleWindowAnimations", "dump", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "isBypassEnabled", "maybePerformPendingUnlock", "notifyListeners", "onBiometricAuthenticated", "biometricSourceType", "Landroid/hardware/biometrics/BiometricSourceType;", "isStrongBiometric", "onStartedGoingToSleep", "registerOnBypassStateChangedListener", "listener", "unregisterOnBypassStateChangedListener", "BypassOverride", "Companion", "OnBypassStateChangedListener", "PendingUnlock", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: KeyguardBypassController.kt */
public class KeyguardBypassController implements Dumpable, StackScrollAlgorithm.BypassController {
    public static final int BYPASS_FADE_DURATION = 67;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int FACE_UNLOCK_BYPASS_ALWAYS = 1;
    private static final int FACE_UNLOCK_BYPASS_NEVER = 2;
    private static final int FACE_UNLOCK_BYPASS_NO_OVERRIDE = 0;
    private boolean altBouncerShowing;
    private boolean bouncerShowing;
    private boolean bypassEnabled;
    private final int bypassOverride;
    private final KeyguardBypassController$faceAuthEnabledChangedCallback$1 faceAuthEnabledChangedCallback = new KeyguardBypassController$faceAuthEnabledChangedCallback$1(this);
    private boolean hasFaceFeature;
    private boolean isPulseExpanding;
    private boolean launchingAffordance;
    private final List<OnBypassStateChangedListener> listeners = new ArrayList();
    private final KeyguardStateController mKeyguardStateController;
    /* access modifiers changed from: private */
    public PendingUnlock pendingUnlock;
    private boolean qSExpanded;
    private final StatusBarStateController statusBarStateController;
    public BiometricUnlockController unlockController;
    private boolean userHasDeviceEntryIntent;

    @Metadata(mo65042d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/KeyguardBypassController$BypassOverride;", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    @Retention(AnnotationRetention.SOURCE)
    @java.lang.annotation.Retention(RetentionPolicy.SOURCE)
    /* compiled from: KeyguardBypassController.kt */
    private @interface BypassOverride {
    }

    @Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/KeyguardBypassController$OnBypassStateChangedListener;", "", "onBypassStateChanged", "", "isEnabled", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: KeyguardBypassController.kt */
    public interface OnBypassStateChangedListener {
        void onBypassStateChanged(boolean z);
    }

    private static /* synthetic */ void getBypassOverride$annotations() {
    }

    public final boolean getUserHasDeviceEntryIntent() {
        return this.userHasDeviceEntryIntent;
    }

    public final void setUserHasDeviceEntryIntent(boolean z) {
        this.userHasDeviceEntryIntent = z;
    }

    @Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000b\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\r\u001a\u00020\u00052\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t¨\u0006\u0013"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/KeyguardBypassController$PendingUnlock;", "", "pendingUnlockType", "Landroid/hardware/biometrics/BiometricSourceType;", "isStrongBiometric", "", "(Landroid/hardware/biometrics/BiometricSourceType;Z)V", "()Z", "getPendingUnlockType", "()Landroid/hardware/biometrics/BiometricSourceType;", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: KeyguardBypassController.kt */
    private static final class PendingUnlock {
        private final boolean isStrongBiometric;
        private final BiometricSourceType pendingUnlockType;

        public static /* synthetic */ PendingUnlock copy$default(PendingUnlock pendingUnlock, BiometricSourceType biometricSourceType, boolean z, int i, Object obj) {
            if ((i & 1) != 0) {
                biometricSourceType = pendingUnlock.pendingUnlockType;
            }
            if ((i & 2) != 0) {
                z = pendingUnlock.isStrongBiometric;
            }
            return pendingUnlock.copy(biometricSourceType, z);
        }

        public final BiometricSourceType component1() {
            return this.pendingUnlockType;
        }

        public final boolean component2() {
            return this.isStrongBiometric;
        }

        public final PendingUnlock copy(BiometricSourceType biometricSourceType, boolean z) {
            Intrinsics.checkNotNullParameter(biometricSourceType, "pendingUnlockType");
            return new PendingUnlock(biometricSourceType, z);
        }

        public boolean equals(Object obj) {
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
            return hashCode + (z ? 1 : 0);
        }

        public String toString() {
            return "PendingUnlock(pendingUnlockType=" + this.pendingUnlockType + ", isStrongBiometric=" + this.isStrongBiometric + ')';
        }

        public PendingUnlock(BiometricSourceType biometricSourceType, boolean z) {
            Intrinsics.checkNotNullParameter(biometricSourceType, "pendingUnlockType");
            this.pendingUnlockType = biometricSourceType;
            this.isStrongBiometric = z;
        }

        public final BiometricSourceType getPendingUnlockType() {
            return this.pendingUnlockType;
        }

        public final boolean isStrongBiometric() {
            return this.isStrongBiometric;
        }
    }

    public final BiometricUnlockController getUnlockController() {
        BiometricUnlockController biometricUnlockController = this.unlockController;
        if (biometricUnlockController != null) {
            return biometricUnlockController;
        }
        Intrinsics.throwUninitializedPropertyAccessException("unlockController");
        return null;
    }

    public final void setUnlockController(BiometricUnlockController biometricUnlockController) {
        Intrinsics.checkNotNullParameter(biometricUnlockController, "<set-?>");
        this.unlockController = biometricUnlockController;
    }

    public final boolean isPulseExpanding() {
        return this.isPulseExpanding;
    }

    public final void setPulseExpanding(boolean z) {
        this.isPulseExpanding = z;
    }

    public boolean isBypassEnabled() {
        return getBypassEnabled();
    }

    public final boolean getBypassEnabled() {
        int i = this.bypassOverride;
        if (!(i != 1 ? i != 2 ? this.bypassEnabled : false : true) || !this.mKeyguardStateController.isFaceAuthEnabled()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public final void setBypassEnabled(boolean z) {
        this.bypassEnabled = z;
        notifyListeners();
    }

    public final boolean getBouncerShowing() {
        return this.bouncerShowing;
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

    public final boolean getLaunchingAffordance() {
        return this.launchingAffordance;
    }

    public final void setLaunchingAffordance(boolean z) {
        this.launchingAffordance = z;
    }

    public final boolean getQSExpanded() {
        return this.qSExpanded;
    }

    public final void setQSExpanded(boolean z) {
        boolean z2 = this.qSExpanded != z;
        this.qSExpanded = z;
        if (z2 && !z) {
            maybePerformPendingUnlock();
        }
    }

    @Inject
    public KeyguardBypassController(Context context, final TunerService tunerService, StatusBarStateController statusBarStateController2, NotificationLockscreenUserManager notificationLockscreenUserManager, KeyguardStateController keyguardStateController, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(tunerService, "tunerService");
        Intrinsics.checkNotNullParameter(statusBarStateController2, "statusBarStateController");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "lockscreenUserManager");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.mKeyguardStateController = keyguardStateController;
        this.statusBarStateController = statusBarStateController2;
        this.bypassOverride = context.getResources().getInteger(C1894R.integer.config_face_unlock_bypass_override);
        boolean hasSystemFeature = context.getPackageManager().hasSystemFeature("android.hardware.biometrics.face");
        this.hasFaceFeature = hasSystemFeature;
        if (hasSystemFeature) {
            dumpManager.registerDumpable("KeyguardBypassController", this);
            statusBarStateController2.addCallback(new StatusBarStateController.StateListener(this) {
                final /* synthetic */ KeyguardBypassController this$0;

                {
                    this.this$0 = r1;
                }

                public void onStateChanged(int i) {
                    if (i != 1) {
                        this.this$0.pendingUnlock = null;
                    }
                }
            });
            final int i = context.getResources().getBoolean(17891660) ? 1 : 0;
            tunerService.addTunable(new TunerService.Tunable(this) {
                final /* synthetic */ KeyguardBypassController this$0;

                {
                    this.this$0 = r1;
                }

                public void onTuningChanged(String str, String str2) {
                    this.this$0.setBypassEnabled(tunerService.getValue(str, i) != 0);
                }
            }, "face_unlock_dismisses_keyguard");
            notificationLockscreenUserManager.addUserChangedListener(new NotificationLockscreenUserManager.UserChangedListener(this) {
                final /* synthetic */ KeyguardBypassController this$0;

                {
                    this.this$0 = r1;
                }

                public void onUserChanged(int i) {
                    this.this$0.pendingUnlock = null;
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public final void notifyListeners() {
        for (OnBypassStateChangedListener onBypassStateChanged : this.listeners) {
            onBypassStateChanged.onBypassStateChanged(getBypassEnabled());
        }
    }

    public final boolean onBiometricAuthenticated(BiometricSourceType biometricSourceType, boolean z) {
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
        PendingUnlock pendingUnlock2 = this.pendingUnlock;
        if (pendingUnlock2 != null) {
            Intrinsics.checkNotNull(pendingUnlock2);
            BiometricSourceType pendingUnlockType = pendingUnlock2.getPendingUnlockType();
            PendingUnlock pendingUnlock3 = this.pendingUnlock;
            Intrinsics.checkNotNull(pendingUnlock3);
            if (onBiometricAuthenticated(pendingUnlockType, pendingUnlock3.isStrongBiometric())) {
                BiometricUnlockController unlockController2 = getUnlockController();
                PendingUnlock pendingUnlock4 = this.pendingUnlock;
                Intrinsics.checkNotNull(pendingUnlock4);
                BiometricSourceType pendingUnlockType2 = pendingUnlock4.getPendingUnlockType();
                PendingUnlock pendingUnlock5 = this.pendingUnlock;
                Intrinsics.checkNotNull(pendingUnlock5);
                unlockController2.startWakeAndUnlock(pendingUnlockType2, pendingUnlock5.isStrongBiometric());
                this.pendingUnlock = null;
            }
        }
    }

    public final boolean canBypass() {
        if (!getBypassEnabled()) {
            return false;
        }
        if (!this.bouncerShowing && !this.altBouncerShowing && (this.statusBarStateController.getState() != 1 || this.launchingAffordance || this.isPulseExpanding || this.qSExpanded)) {
            return false;
        }
        return true;
    }

    public final boolean canPlaySubtleWindowAnimations() {
        if (!getBypassEnabled() || this.statusBarStateController.getState() != 1 || this.qSExpanded) {
            return false;
        }
        return true;
    }

    public final void onStartedGoingToSleep() {
        this.pendingUnlock = null;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("KeyguardBypassController:");
        if (this.pendingUnlock != null) {
            StringBuilder sb = new StringBuilder("  mPendingUnlock.pendingUnlockType: ");
            PendingUnlock pendingUnlock2 = this.pendingUnlock;
            Intrinsics.checkNotNull(pendingUnlock2);
            printWriter.println(sb.append((Object) pendingUnlock2.getPendingUnlockType()).toString());
            StringBuilder sb2 = new StringBuilder("  mPendingUnlock.isStrongBiometric: ");
            PendingUnlock pendingUnlock3 = this.pendingUnlock;
            Intrinsics.checkNotNull(pendingUnlock3);
            printWriter.println(sb2.append(pendingUnlock3.isStrongBiometric()).toString());
        } else {
            printWriter.println("  mPendingUnlock: " + this.pendingUnlock);
        }
        printWriter.println("  bypassEnabled: " + getBypassEnabled());
        printWriter.println("  canBypass: " + canBypass());
        printWriter.println("  bouncerShowing: " + this.bouncerShowing);
        printWriter.println("  altBouncerShowing: " + this.altBouncerShowing);
        printWriter.println("  isPulseExpanding: " + this.isPulseExpanding);
        printWriter.println("  launchingAffordance: " + this.launchingAffordance);
        printWriter.println("  qSExpanded: " + this.qSExpanded);
        printWriter.println("  hasFaceFeature: " + this.hasFaceFeature);
        printWriter.println("  userHasDeviceEntryIntent: " + this.userHasDeviceEntryIntent);
    }

    public final void registerOnBypassStateChangedListener(OnBypassStateChangedListener onBypassStateChangedListener) {
        Intrinsics.checkNotNullParameter(onBypassStateChangedListener, "listener");
        boolean isEmpty = this.listeners.isEmpty();
        this.listeners.add(onBypassStateChangedListener);
        if (isEmpty) {
            this.mKeyguardStateController.addCallback(this.faceAuthEnabledChangedCallback);
        }
    }

    public final void unregisterOnBypassStateChangedListener(OnBypassStateChangedListener onBypassStateChangedListener) {
        Intrinsics.checkNotNullParameter(onBypassStateChangedListener, "listener");
        this.listeners.remove((Object) onBypassStateChangedListener);
        if (this.listeners.isEmpty()) {
            this.mKeyguardStateController.removeCallback(this.faceAuthEnabledChangedCallback);
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\b"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/KeyguardBypassController$Companion;", "", "()V", "BYPASS_FADE_DURATION", "", "FACE_UNLOCK_BYPASS_ALWAYS", "FACE_UNLOCK_BYPASS_NEVER", "FACE_UNLOCK_BYPASS_NO_OVERRIDE", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: KeyguardBypassController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
