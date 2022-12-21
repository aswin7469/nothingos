package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import com.android.systemui.C1893R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u000eH\u0002J\b\u0010\u0012\u001a\u00020\u000eH\u0002J\u0018\u0010\u0013\u001a\u00020\u000e2\u0006\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\nH\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\nX\u000e¢\u0006\b\n\u0000\u0012\u0004\b\u000b\u0010\f¨\u0006\u0016"}, mo64987d2 = {"Lcom/android/systemui/biometrics/AuthBiometricFaceIconController;", "Lcom/android/systemui/biometrics/AuthIconController;", "context", "Landroid/content/Context;", "iconView", "Landroid/widget/ImageView;", "(Landroid/content/Context;Landroid/widget/ImageView;)V", "lastPulseLightToDark", "", "state", "", "getState$annotations", "()V", "handleAnimationEnd", "", "drawable", "Landroid/graphics/drawable/Drawable;", "pulseInNextDirection", "startPulsing", "updateIcon", "oldState", "newState", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AuthBiometricFaceIconController.kt */
public final class AuthBiometricFaceIconController extends AuthIconController {
    private boolean lastPulseLightToDark;
    private int state;

    private static /* synthetic */ void getState$annotations() {
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AuthBiometricFaceIconController(Context context, ImageView imageView) {
        super(context, imageView);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(imageView, "iconView");
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(C1893R.dimen.biometric_dialog_face_icon_size);
        imageView.getLayoutParams().width = dimensionPixelSize;
        imageView.getLayoutParams().height = dimensionPixelSize;
        showStaticDrawable(C1893R.C1895drawable.face_dialog_pulse_dark_to_light);
    }

    private final void startPulsing() {
        this.lastPulseLightToDark = false;
        animateIcon(C1893R.C1895drawable.face_dialog_pulse_dark_to_light, true);
    }

    private final void pulseInNextDirection() {
        animateIcon(this.lastPulseLightToDark ? C1893R.C1895drawable.face_dialog_pulse_dark_to_light : C1893R.C1895drawable.face_dialog_pulse_light_to_dark, true);
        this.lastPulseLightToDark = !this.lastPulseLightToDark;
    }

    public void handleAnimationEnd(Drawable drawable) {
        Intrinsics.checkNotNullParameter(drawable, "drawable");
        int i = this.state;
        if (i == 2 || i == 3) {
            pulseInNextDirection();
        }
    }

    public void updateIcon(int i, int i2) {
        boolean z = i == 4 || i == 3;
        if (i2 == 1) {
            showStaticDrawable(C1893R.C1895drawable.face_dialog_pulse_dark_to_light);
            getIconView().setContentDescription(getContext().getString(C1893R.string.biometric_dialog_face_icon_description_authenticating));
        } else if (i2 == 2) {
            startPulsing();
            getIconView().setContentDescription(getContext().getString(C1893R.string.biometric_dialog_face_icon_description_authenticating));
        } else if (i == 5 && i2 == 6) {
            animateIconOnce(C1893R.C1895drawable.face_dialog_dark_to_checkmark);
            getIconView().setContentDescription(getContext().getString(C1893R.string.biometric_dialog_face_icon_description_confirmed));
        } else if (z && i2 == 0) {
            animateIconOnce(C1893R.C1895drawable.face_dialog_error_to_idle);
            getIconView().setContentDescription(getContext().getString(C1893R.string.biometric_dialog_face_icon_description_idle));
        } else if (z && i2 == 6) {
            animateIconOnce(C1893R.C1895drawable.face_dialog_dark_to_checkmark);
            getIconView().setContentDescription(getContext().getString(C1893R.string.biometric_dialog_face_icon_description_authenticated));
        } else if (i2 == 4 && i != 4) {
            animateIconOnce(C1893R.C1895drawable.face_dialog_dark_to_error);
        } else if (i == 2 && i2 == 6) {
            animateIconOnce(C1893R.C1895drawable.face_dialog_dark_to_checkmark);
            getIconView().setContentDescription(getContext().getString(C1893R.string.biometric_dialog_face_icon_description_authenticated));
        } else if (i2 == 5) {
            animateIconOnce(C1893R.C1895drawable.face_dialog_wink_from_dark);
            getIconView().setContentDescription(getContext().getString(C1893R.string.biometric_dialog_face_icon_description_authenticated));
        } else if (i2 == 0) {
            showStaticDrawable(C1893R.C1895drawable.face_dialog_idle_static);
            getIconView().setContentDescription(getContext().getString(C1893R.string.biometric_dialog_face_icon_description_idle));
        } else {
            Log.w("AuthBiometricFaceIconController", "Unhandled state: " + i2);
        }
        this.state = i2;
    }
}
