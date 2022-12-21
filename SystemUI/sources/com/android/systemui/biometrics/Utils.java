package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.biometrics.PromptInfo;
import android.hardware.biometrics.SensorPropertiesInternal;
import android.os.UserManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.widget.LockPatternUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001:\u0001(B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\tH\u0007J1\u0010\r\u001a\u0004\u0018\u0001H\u000e\"\b\b\u0000\u0010\u000e*\u00020\u000f2\u000e\u0010\u0010\u001a\n\u0012\u0004\u0012\u0002H\u000e\u0018\u00010\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0007¢\u0006\u0002\u0010\u0014J\u0018\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u0004H\u0007J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0007J\u0010\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0007J\u0018\u0010\u001e\u001a\u00020\u001a2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0018\u001a\u00020\u0004H\u0007J\u001a\u0010\u001f\u001a\u00020\u001a2\u0006\u0010\n\u001a\u00020\u000b2\b\u0010 \u001a\u0004\u0018\u00010!H\u0007J\u0018\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020'H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006)"}, mo64987d2 = {"Lcom/android/systemui/biometrics/Utils;", "", "()V", "CREDENTIAL_PASSWORD", "", "CREDENTIAL_PATTERN", "CREDENTIAL_PIN", "FINGERPRINT_OVERLAY_LAYOUT_PARAM_FLAGS", "dpToPixels", "", "context", "Landroid/content/Context;", "dp", "findFirstSensorProperties", "T", "Landroid/hardware/biometrics/SensorPropertiesInternal;", "properties", "", "sensorIds", "", "(Ljava/util/List;[I)Landroid/hardware/biometrics/SensorPropertiesInternal;", "getCredentialType", "utils", "Lcom/android/internal/widget/LockPatternUtils;", "userId", "isBiometricAllowed", "", "promptInfo", "Landroid/hardware/biometrics/PromptInfo;", "isDeviceCredentialAllowed", "isManagedProfile", "isSystem", "clientPackage", "", "notifyAccessibilityContentChanged", "", "am", "Landroid/view/accessibility/AccessibilityManager;", "view", "Landroid/view/ViewGroup;", "CredentialType", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: Utils.kt */
public final class Utils {
    public static final int CREDENTIAL_PASSWORD = 3;
    public static final int CREDENTIAL_PATTERN = 2;
    public static final int CREDENTIAL_PIN = 1;
    public static final int FINGERPRINT_OVERLAY_LAYOUT_PARAM_FLAGS = 16777512;
    public static final Utils INSTANCE = new Utils();

    @Metadata(mo64986d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, mo64987d2 = {"Lcom/android/systemui/biometrics/Utils$CredentialType;", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    @Retention(RetentionPolicy.SOURCE)
    /* compiled from: Utils.kt */
    public @interface CredentialType {
    }

    private Utils() {
    }

    @JvmStatic
    public static final float dpToPixels(Context context, float f) {
        Intrinsics.checkNotNullParameter(context, "context");
        return f * (((float) context.getResources().getDisplayMetrics().densityDpi) / ((float) 160));
    }

    @JvmStatic
    public static final void notifyAccessibilityContentChanged(AccessibilityManager accessibilityManager, ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(accessibilityManager, "am");
        Intrinsics.checkNotNullParameter(viewGroup, "view");
        if (accessibilityManager.isEnabled()) {
            AccessibilityEvent obtain = AccessibilityEvent.obtain();
            obtain.setEventType(2048);
            obtain.setContentChangeTypes(1);
            viewGroup.sendAccessibilityEventUnchecked(obtain);
            View view = viewGroup;
            viewGroup.notifySubtreeAccessibilityStateChanged(view, view, 1);
        }
    }

    @JvmStatic
    public static final boolean isDeviceCredentialAllowed(PromptInfo promptInfo) {
        Intrinsics.checkNotNullParameter(promptInfo, "promptInfo");
        return (promptInfo.getAuthenticators() & 32768) != 0;
    }

    @JvmStatic
    public static final boolean isBiometricAllowed(PromptInfo promptInfo) {
        Intrinsics.checkNotNullParameter(promptInfo, "promptInfo");
        return (promptInfo.getAuthenticators() & 255) != 0;
    }

    @JvmStatic
    public static final int getCredentialType(LockPatternUtils lockPatternUtils, int i) {
        Intrinsics.checkNotNullParameter(lockPatternUtils, "utils");
        int keyguardStoredPasswordQuality = lockPatternUtils.getKeyguardStoredPasswordQuality(i);
        if (keyguardStoredPasswordQuality == 65536) {
            return 2;
        }
        if (keyguardStoredPasswordQuality == 131072 || keyguardStoredPasswordQuality == 196608) {
            return 1;
        }
        return (keyguardStoredPasswordQuality == 262144 || keyguardStoredPasswordQuality == 327680 || keyguardStoredPasswordQuality == 393216) ? 3 : 3;
    }

    @JvmStatic
    public static final boolean isManagedProfile(Context context, int i) {
        Intrinsics.checkNotNullParameter(context, "context");
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        if (userManager != null) {
            return userManager.isManagedProfile(i);
        }
        return false;
    }

    @JvmStatic
    public static final <T extends SensorPropertiesInternal> T findFirstSensorProperties(List<? extends T> list, int[] iArr) {
        Intrinsics.checkNotNullParameter(iArr, "sensorIds");
        T t = null;
        if (list == null) {
            return null;
        }
        Iterator it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            T next = it.next();
            if (ArraysKt.contains(iArr, ((SensorPropertiesInternal) next).sensorId)) {
                t = next;
                break;
            }
        }
        return (SensorPropertiesInternal) t;
    }

    @JvmStatic
    public static final boolean isSystem(Context context, String str) {
        Intrinsics.checkNotNullParameter(context, "context");
        if (!(context.checkCallingOrSelfPermission("android.permission.USE_BIOMETRIC_INTERNAL") == 0) || !Intrinsics.areEqual((Object) "android", (Object) str)) {
            return false;
        }
        return true;
    }
}
