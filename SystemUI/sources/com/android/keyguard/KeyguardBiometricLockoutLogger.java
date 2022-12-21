package com.android.keyguard;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.CoreStartable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.log.SessionTracker;
import com.google.zxing.pdf417.PDF417Common;
import java.p026io.PrintWriter;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 #2\u00020\u0001:\u0002#$B)\b\u0007\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ#\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00190\u0018H\u0016¢\u0006\u0002\u0010\u001aJ\u0010\u0010\u001b\u001a\u00020\f2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u0010\u0010\u001e\u001a\u00020\f2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u0010\u0010\u001f\u001a\u00020\u00142\u0006\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020\u0014H\u0016R\u000e\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000¨\u0006%"}, mo64987d2 = {"Lcom/android/keyguard/KeyguardBiometricLockoutLogger;", "Lcom/android/systemui/CoreStartable;", "context", "Landroid/content/Context;", "uiEventLogger", "Lcom/android/internal/logging/UiEventLogger;", "keyguardUpdateMonitor", "Lcom/android/keyguard/KeyguardUpdateMonitor;", "sessionTracker", "Lcom/android/systemui/log/SessionTracker;", "(Landroid/content/Context;Lcom/android/internal/logging/UiEventLogger;Lcom/android/keyguard/KeyguardUpdateMonitor;Lcom/android/systemui/log/SessionTracker;)V", "encryptedOrLockdown", "", "faceLockedOut", "fingerprintLockedOut", "mKeyguardUpdateMonitorCallback", "Lcom/android/keyguard/KeyguardUpdateMonitorCallback;", "timeout", "unattendedUpdate", "dump", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "isStrongAuthTimeout", "flags", "", "isUnattendedUpdate", "log", "event", "Lcom/android/keyguard/KeyguardBiometricLockoutLogger$PrimaryAuthRequiredEvent;", "start", "Companion", "PrimaryAuthRequiredEvent", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: KeyguardBiometricLockoutLogger.kt */
public final class KeyguardBiometricLockoutLogger extends CoreStartable {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public boolean encryptedOrLockdown;
    /* access modifiers changed from: private */
    public boolean faceLockedOut;
    /* access modifiers changed from: private */
    public boolean fingerprintLockedOut;
    /* access modifiers changed from: private */
    public final KeyguardUpdateMonitor keyguardUpdateMonitor;
    private final KeyguardUpdateMonitorCallback mKeyguardUpdateMonitorCallback = new KeyguardBiometricLockoutLogger$mKeyguardUpdateMonitorCallback$1(this);
    private final SessionTracker sessionTracker;
    /* access modifiers changed from: private */
    public boolean timeout;
    private final UiEventLogger uiEventLogger;
    /* access modifiers changed from: private */
    public boolean unattendedUpdate;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public KeyguardBiometricLockoutLogger(Context context, UiEventLogger uiEventLogger2, KeyguardUpdateMonitor keyguardUpdateMonitor2, SessionTracker sessionTracker2) {
        super(context);
        Intrinsics.checkNotNullParameter(uiEventLogger2, "uiEventLogger");
        Intrinsics.checkNotNullParameter(keyguardUpdateMonitor2, "keyguardUpdateMonitor");
        Intrinsics.checkNotNullParameter(sessionTracker2, "sessionTracker");
        this.uiEventLogger = uiEventLogger2;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor2;
        this.sessionTracker = sessionTracker2;
    }

    public void start() {
        this.mKeyguardUpdateMonitorCallback.onStrongAuthStateChanged(KeyguardUpdateMonitor.getCurrentUser());
        this.keyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
    }

    /* access modifiers changed from: private */
    public final boolean isUnattendedUpdate(int i) {
        return Companion.containsFlag(i, 64);
    }

    /* access modifiers changed from: private */
    public final boolean isStrongAuthTimeout(int i) {
        Companion companion = Companion;
        return companion.containsFlag(i, 16) || companion.containsFlag(i, 128);
    }

    /* access modifiers changed from: private */
    public final void log(PrimaryAuthRequiredEvent primaryAuthRequiredEvent) {
        this.uiEventLogger.log((UiEventLogger.UiEventEnum) primaryAuthRequiredEvent, this.sessionTracker.getSessionId(1));
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("  mFingerprintLockedOut=" + this.fingerprintLockedOut);
        printWriter.println("  mFaceLockedOut=" + this.faceLockedOut);
        printWriter.println("  mIsEncryptedOrLockdown=" + this.encryptedOrLockdown);
        printWriter.println("  mIsUnattendedUpdate=" + this.unattendedUpdate);
        printWriter.println("  mIsTimeout=" + this.timeout);
    }

    @Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\n\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u000f\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000bj\u0002\b\fj\u0002\b\r¨\u0006\u000e"}, mo64987d2 = {"Lcom/android/keyguard/KeyguardBiometricLockoutLogger$PrimaryAuthRequiredEvent;", "", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "mId", "", "(Ljava/lang/String;II)V", "getId", "PRIMARY_AUTH_REQUIRED_FINGERPRINT_LOCKED_OUT", "PRIMARY_AUTH_REQUIRED_FINGERPRINT_LOCKED_OUT_RESET", "PRIMARY_AUTH_REQUIRED_FACE_LOCKED_OUT", "PRIMARY_AUTH_REQUIRED_FACE_LOCKED_OUT_RESET", "PRIMARY_AUTH_REQUIRED_ENCRYPTED_OR_LOCKDOWN", "PRIMARY_AUTH_REQUIRED_TIMEOUT", "PRIMARY_AUTH_REQUIRED_UNATTENDED_UPDATE", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: KeyguardBiometricLockoutLogger.kt */
    public enum PrimaryAuthRequiredEvent implements UiEventLogger.UiEventEnum {
        PRIMARY_AUTH_REQUIRED_FINGERPRINT_LOCKED_OUT(924),
        PRIMARY_AUTH_REQUIRED_FINGERPRINT_LOCKED_OUT_RESET(925),
        PRIMARY_AUTH_REQUIRED_FACE_LOCKED_OUT(926),
        PRIMARY_AUTH_REQUIRED_FACE_LOCKED_OUT_RESET(927),
        PRIMARY_AUTH_REQUIRED_ENCRYPTED_OR_LOCKDOWN(PDF417Common.MAX_CODEWORDS_IN_BARCODE),
        PRIMARY_AUTH_REQUIRED_TIMEOUT(PDF417Common.NUMBER_OF_CODEWORDS),
        PRIMARY_AUTH_REQUIRED_UNATTENDED_UPDATE(931);
        
        private final int mId;

        private PrimaryAuthRequiredEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0002¨\u0006\b"}, mo64987d2 = {"Lcom/android/keyguard/KeyguardBiometricLockoutLogger$Companion;", "", "()V", "containsFlag", "", "strongAuthFlags", "", "flagCheck", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: KeyguardBiometricLockoutLogger.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* access modifiers changed from: private */
        public final boolean containsFlag(int i, int i2) {
            return (i & i2) != 0;
        }

        private Companion() {
        }
    }
}
