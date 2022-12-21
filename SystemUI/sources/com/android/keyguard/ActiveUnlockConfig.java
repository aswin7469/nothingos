package com.android.keyguard;

import android.content.ContentResolver;
import android.os.Handler;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.settings.SecureSettings;
import java.lang.annotation.RetentionPolicy;
import java.p026io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000g\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t*\u0001\u001b\b\u0007\u0018\u0000 02\u00020\u0001:\u0003./0B)\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ%\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 2\u000e\u0010!\u001a\n\u0012\u0006\b\u0001\u0012\u00020#0\"H\u0016¢\u0006\u0002\u0010$J\u0006\u0010%\u001a\u00020\u0017J\u000e\u0010&\u001a\u00020\u00172\u0006\u0010'\u001a\u00020(J\u000e\u0010)\u001a\u00020\u00172\u0006\u0010*\u001a\u00020\rJ\u000e\u0010+\u001a\u00020\u00172\u0006\u0010,\u001a\u00020\rJ\b\u0010-\u001a\u00020\u0017H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0017X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0017X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u00020\u001bX\u0004¢\u0006\u0004\n\u0002\u0010\u001c¨\u00061"}, mo64987d2 = {"Lcom/android/keyguard/ActiveUnlockConfig;", "Lcom/android/systemui/Dumpable;", "handler", "Landroid/os/Handler;", "secureSettings", "Lcom/android/systemui/util/settings/SecureSettings;", "contentResolver", "Landroid/content/ContentResolver;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Landroid/os/Handler;Lcom/android/systemui/util/settings/SecureSettings;Landroid/content/ContentResolver;Lcom/android/systemui/dump/DumpManager;)V", "faceAcquireInfoToTriggerBiometricFailOn", "", "", "faceErrorsToTriggerBiometricFailOn", "keyguardUpdateMonitor", "Lcom/android/keyguard/KeyguardUpdateMonitor;", "getKeyguardUpdateMonitor", "()Lcom/android/keyguard/KeyguardUpdateMonitor;", "setKeyguardUpdateMonitor", "(Lcom/android/keyguard/KeyguardUpdateMonitor;)V", "onUnlockIntentWhenBiometricEnrolled", "requestActiveUnlockOnBioFail", "", "requestActiveUnlockOnUnlockIntent", "requestActiveUnlockOnWakeup", "settingsObserver", "com/android/keyguard/ActiveUnlockConfig$settingsObserver$1", "Lcom/android/keyguard/ActiveUnlockConfig$settingsObserver$1;", "dump", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "isActiveUnlockEnabled", "shouldAllowActiveUnlockFromOrigin", "requestOrigin", "Lcom/android/keyguard/ActiveUnlockConfig$ACTIVE_UNLOCK_REQUEST_ORIGIN;", "shouldRequestActiveUnlockOnFaceAcquireInfo", "acquiredInfo", "shouldRequestActiveUnlockOnFaceError", "errorCode", "shouldRequestActiveUnlockOnUnlockIntentFromBiometricEnrollment", "ACTIVE_UNLOCK_REQUEST_ORIGIN", "BiometricType", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ActiveUnlockConfig.kt */
public final class ActiveUnlockConfig implements Dumpable {
    public static final int BIOMETRIC_TYPE_ANY_FACE = 1;
    public static final int BIOMETRIC_TYPE_ANY_FINGERPRINT = 2;
    public static final int BIOMETRIC_TYPE_NONE = 0;
    public static final int BIOMETRIC_TYPE_UNDER_DISPLAY_FINGERPRINT = 3;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String TAG = "ActiveUnlockConfig";
    /* access modifiers changed from: private */
    public final ContentResolver contentResolver;
    /* access modifiers changed from: private */
    public Set<Integer> faceAcquireInfoToTriggerBiometricFailOn = new LinkedHashSet();
    /* access modifiers changed from: private */
    public Set<Integer> faceErrorsToTriggerBiometricFailOn = SetsKt.mutableSetOf(3);
    private final Handler handler;
    private KeyguardUpdateMonitor keyguardUpdateMonitor;
    /* access modifiers changed from: private */
    public Set<Integer> onUnlockIntentWhenBiometricEnrolled = SetsKt.mutableSetOf(0);
    /* access modifiers changed from: private */
    public boolean requestActiveUnlockOnBioFail;
    /* access modifiers changed from: private */
    public boolean requestActiveUnlockOnUnlockIntent;
    /* access modifiers changed from: private */
    public boolean requestActiveUnlockOnWakeup;
    /* access modifiers changed from: private */
    public final SecureSettings secureSettings;
    private final ActiveUnlockConfig$settingsObserver$1 settingsObserver;

    @Metadata(mo64986d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/keyguard/ActiveUnlockConfig$ACTIVE_UNLOCK_REQUEST_ORIGIN;", "", "(Ljava/lang/String;I)V", "WAKE", "UNLOCK_INTENT", "BIOMETRIC_FAIL", "ASSISTANT", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ActiveUnlockConfig.kt */
    public enum ACTIVE_UNLOCK_REQUEST_ORIGIN {
        WAKE,
        UNLOCK_INTENT,
        BIOMETRIC_FAIL,
        ASSISTANT
    }

    @Metadata(mo64986d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, mo64987d2 = {"Lcom/android/keyguard/ActiveUnlockConfig$BiometricType;", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    @Retention(AnnotationRetention.SOURCE)
    @java.lang.annotation.Retention(RetentionPolicy.SOURCE)
    /* compiled from: ActiveUnlockConfig.kt */
    public @interface BiometricType {
    }

    @Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ActiveUnlockConfig.kt */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[ACTIVE_UNLOCK_REQUEST_ORIGIN.values().length];
            iArr[ACTIVE_UNLOCK_REQUEST_ORIGIN.WAKE.ordinal()] = 1;
            iArr[ACTIVE_UNLOCK_REQUEST_ORIGIN.UNLOCK_INTENT.ordinal()] = 2;
            iArr[ACTIVE_UNLOCK_REQUEST_ORIGIN.BIOMETRIC_FAIL.ordinal()] = 3;
            iArr[ACTIVE_UNLOCK_REQUEST_ORIGIN.ASSISTANT.ordinal()] = 4;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    @Inject
    public ActiveUnlockConfig(@Main Handler handler2, SecureSettings secureSettings2, ContentResolver contentResolver2, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(handler2, "handler");
        Intrinsics.checkNotNullParameter(secureSettings2, "secureSettings");
        Intrinsics.checkNotNullParameter(contentResolver2, "contentResolver");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.handler = handler2;
        this.secureSettings = secureSettings2;
        this.contentResolver = contentResolver2;
        ActiveUnlockConfig$settingsObserver$1 activeUnlockConfig$settingsObserver$1 = new ActiveUnlockConfig$settingsObserver$1(this, handler2);
        this.settingsObserver = activeUnlockConfig$settingsObserver$1;
        activeUnlockConfig$settingsObserver$1.register();
        dumpManager.registerDumpable(this);
    }

    @Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tXT¢\u0006\u0002\n\u0000¨\u0006\n"}, mo64987d2 = {"Lcom/android/keyguard/ActiveUnlockConfig$Companion;", "", "()V", "BIOMETRIC_TYPE_ANY_FACE", "", "BIOMETRIC_TYPE_ANY_FINGERPRINT", "BIOMETRIC_TYPE_NONE", "BIOMETRIC_TYPE_UNDER_DISPLAY_FINGERPRINT", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ActiveUnlockConfig.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final KeyguardUpdateMonitor getKeyguardUpdateMonitor() {
        return this.keyguardUpdateMonitor;
    }

    public final void setKeyguardUpdateMonitor(KeyguardUpdateMonitor keyguardUpdateMonitor2) {
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
    }

    public final boolean isActiveUnlockEnabled() {
        return this.requestActiveUnlockOnWakeup || this.requestActiveUnlockOnUnlockIntent || this.requestActiveUnlockOnBioFail;
    }

    public final boolean shouldRequestActiveUnlockOnFaceError(int i) {
        return this.faceErrorsToTriggerBiometricFailOn.contains(Integer.valueOf(i));
    }

    public final boolean shouldRequestActiveUnlockOnFaceAcquireInfo(int i) {
        return this.faceAcquireInfoToTriggerBiometricFailOn.contains(Integer.valueOf(i));
    }

    public final boolean shouldAllowActiveUnlockFromOrigin(ACTIVE_UNLOCK_REQUEST_ORIGIN active_unlock_request_origin) {
        Intrinsics.checkNotNullParameter(active_unlock_request_origin, "requestOrigin");
        int i = WhenMappings.$EnumSwitchMapping$0[active_unlock_request_origin.ordinal()];
        if (i == 1) {
            return this.requestActiveUnlockOnWakeup;
        }
        if (i != 2) {
            if (i != 3) {
                if (i == 4) {
                    return isActiveUnlockEnabled();
                }
                throw new NoWhenBranchMatchedException();
            } else if (this.requestActiveUnlockOnBioFail || this.requestActiveUnlockOnUnlockIntent || this.requestActiveUnlockOnWakeup) {
                return true;
            }
        } else if (this.requestActiveUnlockOnUnlockIntent || this.requestActiveUnlockOnWakeup || shouldRequestActiveUnlockOnUnlockIntentFromBiometricEnrollment()) {
            return true;
        }
        return false;
    }

    private final boolean shouldRequestActiveUnlockOnUnlockIntentFromBiometricEnrollment() {
        KeyguardUpdateMonitor keyguardUpdateMonitor2;
        if (this.requestActiveUnlockOnBioFail && (keyguardUpdateMonitor2 = this.keyguardUpdateMonitor) != null) {
            boolean isFaceEnrolled = keyguardUpdateMonitor2.isFaceEnrolled();
            boolean cachedIsUnlockWithFingerprintPossible = keyguardUpdateMonitor2.getCachedIsUnlockWithFingerprintPossible(KeyguardUpdateMonitor.getCurrentUser());
            boolean isUdfpsEnrolled = keyguardUpdateMonitor2.isUdfpsEnrolled();
            if (!isFaceEnrolled && !cachedIsUnlockWithFingerprintPossible) {
                return this.onUnlockIntentWhenBiometricEnrolled.contains(0);
            }
            if (isFaceEnrolled || !cachedIsUnlockWithFingerprintPossible) {
                if (!cachedIsUnlockWithFingerprintPossible && isFaceEnrolled) {
                    return this.onUnlockIntentWhenBiometricEnrolled.contains(1);
                }
            } else if (this.onUnlockIntentWhenBiometricEnrolled.contains(2) || (isUdfpsEnrolled && this.onUnlockIntentWhenBiometricEnrolled.contains(3))) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Unit unit;
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("Settings:");
        printWriter.println("   requestActiveUnlockOnWakeup=" + this.requestActiveUnlockOnWakeup);
        printWriter.println("   requestActiveUnlockOnUnlockIntent=" + this.requestActiveUnlockOnUnlockIntent);
        printWriter.println("   requestActiveUnlockOnBioFail=" + this.requestActiveUnlockOnBioFail);
        printWriter.println("   requestActiveUnlockOnUnlockIntentWhenBiometricEnrolled=" + this.onUnlockIntentWhenBiometricEnrolled);
        printWriter.println("   requestActiveUnlockOnFaceError=" + this.faceErrorsToTriggerBiometricFailOn);
        printWriter.println("   requestActiveUnlockOnFaceAcquireInfo=" + this.faceAcquireInfoToTriggerBiometricFailOn);
        printWriter.println("Current state:");
        KeyguardUpdateMonitor keyguardUpdateMonitor2 = this.keyguardUpdateMonitor;
        if (keyguardUpdateMonitor2 != null) {
            printWriter.println("   shouldRequestActiveUnlockOnUnlockIntentFromBiometricEnrollment=" + shouldRequestActiveUnlockOnUnlockIntentFromBiometricEnrollment());
            printWriter.println("   faceEnrolled=" + keyguardUpdateMonitor2.isFaceEnrolled());
            printWriter.println("   fpEnrolled=" + keyguardUpdateMonitor2.getCachedIsUnlockWithFingerprintPossible(KeyguardUpdateMonitor.getCurrentUser()));
            printWriter.println("   udfpsEnrolled=" + keyguardUpdateMonitor2.isUdfpsEnrolled());
            unit = Unit.INSTANCE;
        } else {
            unit = null;
        }
        if (unit == null) {
            printWriter.println("   keyguardUpdateMonitor is uninitialized");
        }
    }
}
