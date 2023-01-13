package com.android.systemui.biometrics;

import android.graphics.PointF;
import android.util.Log;
import com.android.systemui.biometrics.UdfpsController;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0016¨\u0006\u0005"}, mo65043d2 = {"com/android/systemui/biometrics/AuthRippleController$udfpsControllerCallback$1", "Lcom/android/systemui/biometrics/UdfpsController$Callback;", "onFingerDown", "", "onFingerUp", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AuthRippleController.kt */
public final class AuthRippleController$udfpsControllerCallback$1 implements UdfpsController.Callback {
    final /* synthetic */ AuthRippleController this$0;

    AuthRippleController$udfpsControllerCallback$1(AuthRippleController authRippleController) {
        this.this$0 = authRippleController;
    }

    public void onFingerDown() {
        if (this.this$0.getFingerprintSensorLocation() == null) {
            Log.e("AuthRipple", "fingerprintSensorLocation=null onFingerDown. Skip showing dwell ripple");
            return;
        }
        PointF fingerprintSensorLocation = this.this$0.getFingerprintSensorLocation();
        Intrinsics.checkNotNull(fingerprintSensorLocation);
        ((AuthRippleView) this.this$0.mView).setFingerprintSensorLocation(fingerprintSensorLocation, this.this$0.udfpsRadius);
        this.this$0.showDwellRipple();
    }

    public void onFingerUp() {
        ((AuthRippleView) this.this$0.mView).retractDwellRipple();
    }
}
