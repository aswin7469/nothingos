package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0016\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u001a\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0014J\u0012\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000b\u001a\u00020\nH\u0002J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0014J\u0018\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0016¨\u0006\u0013"}, mo65043d2 = {"Lcom/android/systemui/biometrics/AuthBiometricFingerprintIconController;", "Lcom/android/systemui/biometrics/AuthIconController;", "context", "Landroid/content/Context;", "iconView", "Landroid/widget/ImageView;", "(Landroid/content/Context;Landroid/widget/ImageView;)V", "getAnimationForTransition", "Landroid/graphics/drawable/Drawable;", "oldState", "", "newState", "getIconContentDescription", "", "shouldAnimateForTransition", "", "updateIcon", "", "lastState", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AuthBiometricFingerprintIconController.kt */
public class AuthBiometricFingerprintIconController extends AuthIconController {
    /* access modifiers changed from: protected */
    public boolean shouldAnimateForTransition(int i, int i2) {
        if (i2 == 1 || i2 == 2) {
            if (!(i == 4 || i == 3)) {
                return false;
            }
        } else if (!(i2 == 3 || i2 == 4)) {
            return false;
        }
        return true;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AuthBiometricFingerprintIconController(Context context, ImageView imageView) {
        super(context, imageView);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(imageView, "iconView");
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(C1894R.dimen.biometric_dialog_fingerprint_icon_size);
        imageView.getLayoutParams().width = dimensionPixelSize;
        imageView.getLayoutParams().height = dimensionPixelSize;
    }

    public void updateIcon(int i, int i2) {
        Drawable animationForTransition = getAnimationForTransition(i, i2);
        if (animationForTransition != null) {
            getIconView().setImageDrawable(animationForTransition);
            CharSequence iconContentDescription = getIconContentDescription(i2);
            if (iconContentDescription != null) {
                getIconView().setContentDescription(iconContentDescription);
            }
            AnimatedVectorDrawable animatedVectorDrawable = animationForTransition instanceof AnimatedVectorDrawable ? (AnimatedVectorDrawable) animationForTransition : null;
            if (animatedVectorDrawable != null) {
                animatedVectorDrawable.reset();
                if (shouldAnimateForTransition(i, i2)) {
                    animatedVectorDrawable.forceAnimationOnUI();
                    animatedVectorDrawable.start();
                }
            }
        }
    }

    private final CharSequence getIconContentDescription(int i) {
        Integer num;
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 5:
            case 6:
                num = Integer.valueOf((int) C1894R.string.accessibility_fingerprint_dialog_fingerprint_icon);
                break;
            case 3:
            case 4:
                num = Integer.valueOf((int) C1894R.string.biometric_dialog_try_again);
                break;
            default:
                num = null;
                break;
        }
        if (num != null) {
            return getContext().getString(num.intValue());
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public Drawable getAnimationForTransition(int i, int i2) {
        return getContext().getDrawable(C1894R.C1896drawable.nt_third_part_fp_icon);
    }
}
