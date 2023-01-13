package com.android.systemui.biometrics;

import com.android.systemui.biometrics.AuthController;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0016¨\u0006\u0005"}, mo65043d2 = {"com/android/systemui/biometrics/AuthRippleController$authControllerCallback$1", "Lcom/android/systemui/biometrics/AuthController$Callback;", "onAllAuthenticatorsRegistered", "", "onUdfpsLocationChanged", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AuthRippleController.kt */
public final class AuthRippleController$authControllerCallback$1 implements AuthController.Callback {
    final /* synthetic */ AuthRippleController this$0;

    AuthRippleController$authControllerCallback$1(AuthRippleController authRippleController) {
        this.this$0 = authRippleController;
    }

    public void onAllAuthenticatorsRegistered() {
        this.this$0.updateUdfpsDependentParams();
        this.this$0.updateSensorLocation();
    }

    public void onUdfpsLocationChanged() {
        this.this$0.updateUdfpsDependentParams();
        this.this$0.updateSensorLocation();
    }
}
