package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u001a\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0014J\u0018\u0010\u0010\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eH\u0014R\u0014\u0010\u0007\u001a\u00020\bXD¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/biometrics/AuthBiometricFingerprintAndFaceIconController;", "Lcom/android/systemui/biometrics/AuthBiometricFingerprintIconController;", "context", "Landroid/content/Context;", "iconView", "Landroid/widget/ImageView;", "(Landroid/content/Context;Landroid/widget/ImageView;)V", "actsAsConfirmButton", "", "getActsAsConfirmButton", "()Z", "getAnimationForTransition", "Landroid/graphics/drawable/Drawable;", "oldState", "", "newState", "shouldAnimateForTransition", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AuthBiometricFingerprintAndFaceIconController.kt */
public final class AuthBiometricFingerprintAndFaceIconController extends AuthBiometricFingerprintIconController {
    private final boolean actsAsConfirmButton = true;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AuthBiometricFingerprintAndFaceIconController(Context context, ImageView imageView) {
        super(context, imageView);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(imageView, "iconView");
    }

    public boolean getActsAsConfirmButton() {
        return this.actsAsConfirmButton;
    }

    /* access modifiers changed from: protected */
    public boolean shouldAnimateForTransition(int i, int i2) {
        if (i2 == 5) {
            return true;
        }
        if (i2 != 6) {
            return super.shouldAnimateForTransition(i, i2);
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public Drawable getAnimationForTransition(int i, int i2) {
        if (i2 != 5) {
            if (i2 != 6) {
                return super.getAnimationForTransition(i, i2);
            }
            return null;
        } else if (i == 3 || i == 4) {
            return getContext().getDrawable(C1894R.C1896drawable.fingerprint_dialog_error_to_unlock);
        } else {
            return getContext().getDrawable(C1894R.C1896drawable.fingerprint_dialog_fp_to_unlock);
        }
    }
}
