package com.android.keyguard;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(mo65042d1 = {"\u0000A\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u001e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010#\n\u0000\n\u0002\u0010\"\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J.\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00030\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0016J.\u0010\u0013\u001a\u00020\u000b2\b\u0010\u0014\u001a\u0004\u0018\u00010\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00110\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00110\u0019H\u0002J\u0006\u0010\u001a\u001a\u00020\u000bJ\u0016\u0010\u001b\u001a\u00020\u000b2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00030\u000fH\u0002R\u0016\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0007\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\b\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"}, mo65043d2 = {"com/android/keyguard/ActiveUnlockConfig$settingsObserver$1", "Landroid/database/ContentObserver;", "bioFailUri", "Landroid/net/Uri;", "kotlin.jvm.PlatformType", "faceAcquireInfoUri", "faceErrorsUri", "unlockIntentUri", "unlockIntentWhenBiometricEnrolledUri", "wakeUri", "onChange", "", "selfChange", "", "uris", "", "flags", "", "userId", "processStringArray", "stringSetting", "", "out", "", "default", "", "register", "registerUri", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ActiveUnlockConfig.kt */
public final class ActiveUnlockConfig$settingsObserver$1 extends ContentObserver {
    private final Uri bioFailUri;
    private final Uri faceAcquireInfoUri;
    private final Uri faceErrorsUri;
    final /* synthetic */ ActiveUnlockConfig this$0;
    private final Uri unlockIntentUri;
    private final Uri unlockIntentWhenBiometricEnrolledUri;
    private final Uri wakeUri;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ActiveUnlockConfig$settingsObserver$1(ActiveUnlockConfig activeUnlockConfig, Handler handler) {
        super(handler);
        this.this$0 = activeUnlockConfig;
        this.wakeUri = activeUnlockConfig.secureSettings.getUriFor("active_unlock_on_wake");
        this.unlockIntentUri = activeUnlockConfig.secureSettings.getUriFor("active_unlock_on_unlock_intent");
        this.bioFailUri = activeUnlockConfig.secureSettings.getUriFor("active_unlock_on_biometric_fail");
        this.faceErrorsUri = activeUnlockConfig.secureSettings.getUriFor("active_unlock_on_face_errors");
        this.faceAcquireInfoUri = activeUnlockConfig.secureSettings.getUriFor("active_unlock_on_face_acquire_info");
        this.unlockIntentWhenBiometricEnrolledUri = activeUnlockConfig.secureSettings.getUriFor("active_unlock_on_unlock_intent_when_biometric_enrolled");
    }

    public final void register() {
        registerUri(CollectionsKt.listOf(this.wakeUri, this.unlockIntentUri, this.bioFailUri, this.faceErrorsUri, this.faceAcquireInfoUri, this.unlockIntentWhenBiometricEnrolledUri));
        onChange(true, new ArrayList(), 0, KeyguardUpdateMonitor.getCurrentUser());
    }

    private final void registerUri(Collection<? extends Uri> collection) {
        for (Uri registerContentObserver : collection) {
            this.this$0.contentResolver.registerContentObserver(registerContentObserver, false, this, -1);
        }
    }

    public void onChange(boolean z, Collection<? extends Uri> collection, int i, int i2) {
        Intrinsics.checkNotNullParameter(collection, "uris");
        if (KeyguardUpdateMonitor.getCurrentUser() == i2) {
            boolean z2 = true;
            if (z || collection.contains(this.wakeUri)) {
                ActiveUnlockConfig activeUnlockConfig = this.this$0;
                activeUnlockConfig.requestActiveUnlockOnWakeup = activeUnlockConfig.secureSettings.getIntForUser("active_unlock_on_wake", 0, KeyguardUpdateMonitor.getCurrentUser()) == 1;
            }
            if (z || collection.contains(this.unlockIntentUri)) {
                ActiveUnlockConfig activeUnlockConfig2 = this.this$0;
                activeUnlockConfig2.requestActiveUnlockOnUnlockIntent = activeUnlockConfig2.secureSettings.getIntForUser("active_unlock_on_unlock_intent", 0, KeyguardUpdateMonitor.getCurrentUser()) == 1;
            }
            if (z || collection.contains(this.bioFailUri)) {
                ActiveUnlockConfig activeUnlockConfig3 = this.this$0;
                if (activeUnlockConfig3.secureSettings.getIntForUser("active_unlock_on_biometric_fail", 0, KeyguardUpdateMonitor.getCurrentUser()) != 1) {
                    z2 = false;
                }
                activeUnlockConfig3.requestActiveUnlockOnBioFail = z2;
            }
            if (z || collection.contains(this.faceErrorsUri)) {
                processStringArray(this.this$0.secureSettings.getStringForUser("active_unlock_on_face_errors", KeyguardUpdateMonitor.getCurrentUser()), this.this$0.faceErrorsToTriggerBiometricFailOn, SetsKt.setOf(3));
            }
            if (z || collection.contains(this.faceAcquireInfoUri)) {
                processStringArray(this.this$0.secureSettings.getStringForUser("active_unlock_on_face_acquire_info", KeyguardUpdateMonitor.getCurrentUser()), this.this$0.faceAcquireInfoToTriggerBiometricFailOn, SetsKt.emptySet());
            }
            if (z || collection.contains(this.unlockIntentWhenBiometricEnrolledUri)) {
                processStringArray(this.this$0.secureSettings.getStringForUser("active_unlock_on_unlock_intent_when_biometric_enrolled", KeyguardUpdateMonitor.getCurrentUser()), this.this$0.onUnlockIntentWhenBiometricEnrolled, SetsKt.setOf(0));
            }
        }
    }

    private final void processStringArray(String str, Set<Integer> set, Set<Integer> set2) {
        set.clear();
        if (str != null) {
            for (String str2 : StringsKt.split$default((CharSequence) str, new String[]{"|"}, false, 0, 6, (Object) null)) {
                try {
                    set.add(Integer.valueOf(Integer.parseInt(str2)));
                } catch (NumberFormatException unused) {
                    Log.e(ActiveUnlockConfig.TAG, "Passed an invalid setting=" + str2);
                }
            }
            return;
        }
        set.addAll(set2);
    }
}
