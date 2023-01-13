package com.android.systemui.biometrics;

import android.hardware.fingerprint.IUdfpsOverlayControllerCallback;
import android.util.Log;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0011\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016¨\u0006\u0004"}, mo65043d2 = {"com/android/systemui/biometrics/UdfpsShell$showOverlay$1", "Landroid/hardware/fingerprint/IUdfpsOverlayControllerCallback$Stub;", "onUserCanceled", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UdfpsShell.kt */
public final class UdfpsShell$showOverlay$1 extends IUdfpsOverlayControllerCallback.Stub {
    UdfpsShell$showOverlay$1() {
    }

    public void onUserCanceled() {
        Log.e("UdfpsShell", "User cancelled");
    }
}
