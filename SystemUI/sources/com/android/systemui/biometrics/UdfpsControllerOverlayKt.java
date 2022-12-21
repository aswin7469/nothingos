package com.android.systemui.biometrics;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\u0010\b\n\u0002\b\u0002\u001a\f\u0010\u0002\u001a\u00020\u0003*\u00020\u0004H\u0002\u001a\f\u0010\u0005\u001a\u00020\u0003*\u00020\u0004H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\u0006"}, mo64987d2 = {"TAG", "", "isEnrollmentReason", "", "", "isImportantForAccessibility", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UdfpsControllerOverlay.kt */
public final class UdfpsControllerOverlayKt {
    private static final String TAG = "UdfpsControllerOverlay";

    /* access modifiers changed from: private */
    public static final boolean isEnrollmentReason(int i) {
        return i == 1 || i == 2;
    }

    /* access modifiers changed from: private */
    public static final boolean isImportantForAccessibility(int i) {
        return i == 1 || i == 2 || i == 3;
    }
}
